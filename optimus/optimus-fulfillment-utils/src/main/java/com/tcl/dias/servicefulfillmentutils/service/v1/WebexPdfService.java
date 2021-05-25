package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScWebexServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScWebexServiceCommercialRepository;
import com.tcl.dias.servicefulfillmentutils.beans.webex.CugDialOutBean;
import com.tcl.dias.servicefulfillmentutils.beans.webex.MicrositeDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.webex.VoiceServicesDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.WebexServiceAcceptancePdfBean;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the WebexPdfService methods for pdf generation(Handover Note)
 * 
 *
 * @author Ankit kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Service
public class WebexPdfService {
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${file.upload-dir}")
	String uploadPath;

	@Autowired
	AttachmentRepository attachmentRepository;
	
	@Autowired
	ScAttachmentRepository scAttachmentRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScWebexServiceCommercialRepository scWebexServiceCommercialRepository;
	

	private static final Logger LOGGER = LoggerFactory.getLogger(WebexPdfService.class);
	
	@Transactional(readOnly = false)
	public String processServiceAcceptancePdf(String serviceUuid, Integer serviceId) throws TclCommonException {
		String tempDownloadUrl = null;
		try {

			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if(scServiceDetailOpt.isPresent()) {
				scServiceDetail =scServiceDetailOpt.get();
			}
			
			
			ScOrder scOrder = scServiceDetail.getScOrder();
			
			
			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();			
			scServiceAttributes = scServiceDetail.getScServiceAttributes();
			
			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes=scServiceDetail.getScOrder().getScOrderAttributes();
			
			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
			
			Map<String, String> scServiceAttributesmap = new HashMap<>();
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

			
			WebexServiceAcceptancePdfBean saBean = setSaBean(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap);

			String html = getHtml(saBean);

			//LOGGER.info(html);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "webex_handovernote_" + scServiceDetail.getUuid() + ".pdf";

			Attachment attachment = new Attachment();
			attachment.setName(fileName);
			attachment.setIsActive("Y");
			attachment.setCategory("Handover-note");
			attachment.setType("Handover-note");
			attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				LOGGER.info("SwiftApiEnabled for Handover Note");
				InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				fileName = ".pdf";

				if (scOrderAttributesmap.containsKey("CUSTOMER_CODE")
						&& scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE")) {
					LOGGER.info("Getting Stored Object...");
					StoredObject storedObject = fileStorageService.uploadObjectWithExten(fileName, inputStream,
							scOrderAttributesmap.get("CUSTOMER_CODE"), scOrderAttributesmap.get("CUSTOMER_LE_CODE"));

					LOGGER.info("Stored Object is -> {}", storedObject.getName());

					String[] pathArray = storedObject.getPath().split("/");
					/*
					 * Map<String, String> cofMap = new HashMap<>(); cofMap.put("FILENAME",
					 * storedObject.getName()); cofMap.put("OBJECT_STORAGE_PATH", pathArray[1]);
					 * String tempUrl =
					 * fileStorageService.getTempDownloadUrl(storedObject.getName(), 60000,
					 * pathArray[1], false); cofMap.put("TEMP_URL", tempUrl);
					 */
					attachment.setName(storedObject.getName());
					attachment.setUriPathOrUrl(pathArray[1]);
					LOGGER.info("PDF service handover note Attachment {} to be saved at location {}",
							attachment.getName(), pathArray[1]);
					Attachment savedAttachment = attachmentRepository.save(attachment);

					ScAttachment scAttachment = new ScAttachment();
					scAttachment.setAttachment(savedAttachment);
					scAttachment.setScServiceDetail(scServiceDetail);
					scAttachment.setIsActive("Y");
					scAttachment.setSiteType("A");
					scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
					scAttachmentRepository.save(scAttachment);
					LOGGER.info("Attachment saved in scAttachment with id " + scAttachment.getId());

				}
			} else {

				String cofPath = uploadPath;
				File filefolder = new File(cofPath);
				if (!filefolder.exists()) {
					filefolder.mkdirs();

				}
				String fileFullPath = cofPath + fileName;
				try (OutputStream outputStream = new FileOutputStream(fileFullPath)) {
					bos.writeTo(outputStream);
				}
				attachment.setUriPathOrUrl(fileFullPath);
				Attachment savedAttachment = attachmentRepository.save(attachment);
				LOGGER.info("PDF service handover note Attachment {} to be saved at location {}",attachment.getName(),attachment.getUriPathOrUrl());

				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(savedAttachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType("A");
				scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
				scAttachmentRepository.save(scAttachment);
				LOGGER.info("Attachment saved in scAttachment with id "+scAttachment.getId());
			     }

		} catch (Exception e) {
			LOGGER.error("Error in Processing Service Acceptance {}",e);
		}
		return tempDownloadUrl;

	}
	
	private WebexServiceAcceptancePdfBean setSaBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap) throws TclCommonException, IllegalArgumentException, JsonParseException, JsonMappingException, IOException {

		WebexServiceAcceptancePdfBean saBean = new WebexServiceAcceptancePdfBean();
		
		 //OrderDetails
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		saBean.setServiceId(scServiceDetail.getUuid());
		saBean.setOrderType(scOrder.getOrderType());
		saBean.setOrderCategory(scOrder.getOrderCategory());
		
		 if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
	          saBean.setCustomerContractingEntity(scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "") + ";"
				+ scOrderAttributesmap.getOrDefault("GST_Address", ""));

		//Billing Details
		String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
		saBean.setBillStartDate(billStartDate);
		saBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", null));
		saBean.setDeemedAcceptanceApplicable(scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
		String commissioningDate = formatDate(scComponentAttributesmap.getOrDefault("commissioningDate", ""));
		saBean.setCommissioningDate(commissioningDate);
		
		//Service Details
		saBean.setServiceType(scServiceDetail.getErfPrdCatalogOfferingName());
		saBean.setBridge(scServiceAttributesmap.getOrDefault("primary_region", " "));
		saBean.setAudioPlan(scServiceAttributesmap.getOrDefault("audio_model", " "));
	//	saBean.setPayPerUseOrpayPerSeat(scServiceAttributesmap.getOrDefault("payment_model", " "));  // needs to be removed 
		saBean.setAccessType(scServiceAttributesmap.getOrDefault("access_type", " "));
		ScWebexServiceCommercial scWebexServiceCommercial=scWebexServiceCommercialRepository.findFirstByScServiceDetailIdAndComponentType(scServiceDetail.getId(),"Subscription");
		if(Objects.nonNull(scWebexServiceCommercial)) {
		saBean.setSubscription(scWebexServiceCommercial.getComponentName());
		
		 }
		
		//Microsite Details
		ObjectMapper objectMapper=new ObjectMapper();
		List<MicrositeDetailsBean> micrositeDetails=null;
		String microsite=scComponentAttributesmap.getOrDefault("micrositeDetails","");
		if(Objects.nonNull(microsite) && !microsite.equalsIgnoreCase("")) {
		 micrositeDetails = new ArrayList<MicrositeDetailsBean>();
		 micrositeDetails=objectMapper.readValue(microsite,new TypeReference<List<MicrositeDetailsBean>>(){} );
		}
		saBean.setMicrositeDetails(micrositeDetails);
		/*
		 * SKU Details To Be Removed 
		 */
		//Sku Details
//		List<ScWebexServiceCommercial> scWebexServiceCommercialList=scWebexServiceCommercialRepository.findByScServiceDetailIdAndComponentType(scServiceDetail.getId(),"License");
//		List<SkuDetailsBean> skuDetailsList= new ArrayList<SkuDetailsBean>();
//		if(Objects.nonNull(scWebexServiceCommercialList) && !scWebexServiceCommercialList.isEmpty()) {
//			for(ScWebexServiceCommercial scWebexServiceComm:scWebexServiceCommercialList)
//			{
//				SkuDetailsBean skuDetails = new SkuDetailsBean();
//				skuDetails.setComponentName(scWebexServiceComm.getComponentName());
//				skuDetails.setComponentDesc(scWebexServiceComm.getComponentDesc());
//				skuDetails.setQuantity(scWebexServiceComm.getQuantity());
//				skuDetailsList.add(skuDetails);
//			}
//			saBean.setSkuDetails(skuDetailsList);
//			
//			
//			
//		} 
				
		//Voice Services Details
		List<ScServiceDetail> serviceDetails = scServiceDetailRepository.findByScOrderIdAndErfCatalogProductName(scServiceDetail.getScOrder().getOpOrderCode(), "GSIP");
		List<VoiceServicesDetailsBean> voiceServicesDetailList=new ArrayList<VoiceServicesDetailsBean>();
		if (Objects.nonNull(voiceServicesDetailList) && !voiceServicesDetailList.isEmpty()) {
		for(ScServiceDetail scServiceDtl:serviceDetails) {
			VoiceServicesDetailsBean voiceServicesDetails=new VoiceServicesDetailsBean();
			voiceServicesDetails.setNumberType(scServiceDtl.getErfPrdCatalogOfferingName());
			voiceServicesDetails.setSourceCountry(scServiceDtl.getSourceCountry());
			voiceServicesDetails.setDestinationCountry(scServiceDtl.getDestinationCountry()); 
			voiceServicesDetails.setTerminationNumber(scComponentAttributesmap.getOrDefault("terminationNumber", ""));
			voiceServicesDetailList.add(voiceServicesDetails);
		}
		saBean.setVoiceServicesDetails(voiceServicesDetailList);
		}
		
		//CUG Dail in
		List<String> dailInNumbers=null;
		 String dialInNum=scComponentAttributesmap.getOrDefault("cugDialInNumber","");
		if(Objects.nonNull(dialInNum) && !dialInNum.equalsIgnoreCase("")) {
		String [] dialInArr=dialInNum.split(",");
		dailInNumbers=Arrays.asList(dialInArr);
		}
		saBean.setDailInNumbers(dailInNumbers);
		
		//CUG Dail Out
		List<CugDialOutBean> cugDialOutDetails=null;
		String cugDailOut=scComponentAttributesmap.getOrDefault("cugDialOut","");
		if (Objects.nonNull(cugDailOut) && !cugDailOut.equalsIgnoreCase("")) 
		 cugDialOutDetails=objectMapper.readValue(cugDailOut,new TypeReference<List<CugDialOutBean>>(){} );
		
		saBean.setCugDialOutDetails(cugDialOutDetails);
		
		//On Net Dail Back
		List<String> onNetBackNumbers=null;
		String onNetBackNum=scComponentAttributesmap.getOrDefault("onNetDialBack","");
		if (Objects.nonNull(onNetBackNum) && !onNetBackNum.equalsIgnoreCase(""))
		 onNetBackNumbers=objectMapper.readValue(onNetBackNum,new TypeReference<List<String>>(){} );
		
		saBean.setOnNetBackNumbers(onNetBackNumbers);
		
		return saBean;
	
	}
	
	private String formatDate(String sdate1) {
		String date = "";
		if (sdate1 != null && !sdate1.equals("")) {
			try {
			SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
			Date date1 = formatter1.parse(sdate1);
			date = formatter2.format(date1);
			//System.out.println(date);
			} catch(Exception e) {
				LOGGER.error("Date Parsing or Formatting Error : {}",e.getMessage());				
			}
		}
		return date;
	}
	
	private String getHtml(WebexServiceAcceptancePdfBean saBean) {
		Map<String, Object> variable = objectMapper.convertValue(saBean, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("webex_service_acceptance_pdf", context);
	}
	
}
