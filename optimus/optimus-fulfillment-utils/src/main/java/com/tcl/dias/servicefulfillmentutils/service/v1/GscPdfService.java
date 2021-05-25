package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;

import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.GscServiceAcceptancePdfBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.NumberCountryDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SipTrunkDetails;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.TrunkBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the GscPdfService methods for pdf generation(Handover
 * Note)
 * 
 *
 * @author Ankit kumar
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */

@Service
public class GscPdfService {

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
	ScAssetRepository scAssetRepository;

	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	GscService gscService;

	private static final Logger LOGGER = LoggerFactory.getLogger(GscPdfService.class);
	
	private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";
	@Transactional(readOnly = false)
	public String processServiceAcceptancePdf(String serviceUuid, Integer serviceId, Integer flowGroupId)
			throws TclCommonException {
		String tempDownloadUrl = null;
		try {

			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if (scServiceDetailOpt.isPresent()) {
				scServiceDetail = scServiceDetailOpt.get();
			}

			ScOrder scOrder = scServiceDetail.getScOrder();

			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
			scServiceAttributes = scServiceDetail.getScServiceAttributes();

			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();

			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			Map<String, String> scServiceAttributesmap = new HashMap<>();
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("getServiceAttributes response : {}", scServiceAttributesmap);

			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

			GscServiceAcceptancePdfBean saBean = setSaBean(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap, serviceId, flowGroupId);

			String accessType = scServiceDetail.getAccessType();
			String html = "";
			String fileName = "gsc_handovernote_" + scServiceDetail.getUuid() + ".pdf";
			if(isPublicIP(accessType)) {
				html = getHtmlForVas(saBean);
				fileName = "gsc_vas_handovernote_" + scServiceDetail.getUuid() + ".pdf";
			}else {
				html = getHtml(saBean);
			}

			// LOGGER.info(html);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			

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
				LOGGER.info("PDF service handover note Attachment {} to be saved at location {}", attachment.getName(),
						attachment.getUriPathOrUrl());

				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(savedAttachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType("A");
				scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
				scAttachmentRepository.save(scAttachment);
				LOGGER.info("Attachment saved in scAttachment with id " + scAttachment.getId());
			}
		} catch (Exception e) {
			LOGGER.error("Error in Processing Service Acceptance {}", e);
		}
		return tempDownloadUrl;

	}

	private boolean isPublicIP(String accessType) {
		return (accessType!=null && !accessType.isEmpty() && accessType.equalsIgnoreCase("Public IP"));
	}

	@SuppressWarnings("unchecked")
	private String getHtmlForVas(GscServiceAcceptancePdfBean saBean) {
		Map<String, Object> variable = objectMapper.convertValue(saBean, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("gsc_vas_service_acceptance_pdf", context);
	}

	private GscServiceAcceptancePdfBean setSaBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap, Integer serviceId, Integer flowGroupId)
			throws TclCommonException, IllegalArgumentException, JsonParseException, JsonMappingException, IOException {

		GscServiceAcceptancePdfBean saBean = new GscServiceAcceptancePdfBean();

		String accessType = scServiceDetail.getAccessType();
		
		// OrderDetails
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		
		if(!isPublicIP(accessType)) {
			saBean.setServiceId(scServiceDetail.getUuid());
			saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("GST_Address", ""));
		}else {
			saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("Billing Address", ""));
			List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByProductNameAndParentId("SIP", String.valueOf(scServiceDetail.getParentId()));
			if(!CollectionUtils.isEmpty(scServiceDetails)) {
//				Map<String, String> scComponentAttributesmapForSip = commonFulfillmentUtils.getComponentAttributes(scServiceDetails.get(0).getId(), "LM", "A");
				Map<String, String> scComponentAttributesmapForSip = commonFulfillmentUtils.getComponentAndAdditionalAttributes(scServiceDetails.get(0).getId(), "LM", "A");
				LOGGER.info("componentAttributes for SIP : {}", scComponentAttributesmapForSip);
				saBean.setSipTrunkDetails(constructSiptrunkDetails(scComponentAttributesmapForSip));
			}
			
		}
		
		saBean.setOrderType(scOrder.getOrderType());
		saBean.setOrderCategory(scOrder.getOrderCategory());

		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			saBean.setCustomerContractingEntity(
					scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		
		saBean.setGstnVatNo(scOrderAttributesmap.getOrDefault("LeStateGstNumber", ""));

		// Billing Details
		saBean.setDeemedAcceptanceApplicable(scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", "NO"));
		
		if(isPublicIP(accessType)) {
			saBean.setCommissioningDate(scComponentAttributesmap.getOrDefault("commissioningDate", "NA"));
		}
		
		// Service Details
		saBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		saBean.setProductType(scServiceAttributesmap.getOrDefault("serviceType", ""));

		// Number and Country Details
		List<NumberCountryDetailsBean> numberCountryDetailsList = new ArrayList<NumberCountryDetailsBean>();
		List<GscScAsset> torScAssets = scAssetRepository.findByServiceIdandTypeandAssetId(serviceId, "Toll-Free",
				flowGroupId, GscNumberStatus.IN_SERVICE_ACCEPT);
		torScAssets.forEach(scAssets -> {
			NumberCountryDetailsBean numberCountryDetailsBean = new NumberCountryDetailsBean();
			numberCountryDetailsBean.setOriginCountry(scServiceDetail.getSourceCountry());
			numberCountryDetailsBean.setDestinationCountry(scServiceDetail.getDestinationCountry());
			numberCountryDetailsBean.setNumbers(scAssets.getTollfreeName());
			numberCountryDetailsBean.setAccessType(scServiceDetail.getAccessType());

			String terminationNumberIsdCode = scComponentAttributesmap.getOrDefault("terminationNumberIsdCode", "");
			if(terminationNumberIsdCode==null) {
				terminationNumberIsdCode = "";
			}
			
			numberCountryDetailsBean.setOutpulse(terminationNumberIsdCode
					+ "-" + scAssets.getOutpulseName());
			List<String> callTypeList = gscService.getRepcCallTypeList(scServiceDetail.getId());
			numberCountryDetailsBean.setCallType(String.join(",", callTypeList));
			ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeName(scAssets.getTollfreeId(), "billingStartDate");
			if (Objects.nonNull(scAssetAttribute)) {
				numberCountryDetailsBean.setBillingStartDate(scAssetAttribute.getAttributeValue());
			}
			numberCountryDetailsList.add(numberCountryDetailsBean);
		});

		saBean.setNumberCountryDetails(numberCountryDetailsList);
		return saBean;
	}

	/*
	 * private String formatDate(String sdate1) { String date = ""; if (sdate1 !=
	 * null && !sdate1.equals("")) { try { SimpleDateFormat formatter1 = new
	 * SimpleDateFormat("yyyy-MM-dd"); SimpleDateFormat formatter2 = new
	 * SimpleDateFormat("dd-MMM-yyyy"); Date date1 = formatter1.parse(sdate1); date
	 * = formatter2.format(date1); //System.out.println(date); } catch(Exception e)
	 * { LOGGER.error("Date Parsing or Formatting Error : {}",e.getMessage()); } }
	 * return date; }
	 */

	private String getHtml(GscServiceAcceptancePdfBean saBean) {
		Map<String, Object> variable = objectMapper.convertValue(saBean, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("gsc_service_acceptance_pdf", context);
	}
	
	@Transactional(readOnly = false)
	public String processdIdHandoverPdfDownload(String serviceUuid, Integer serviceId, String taskDefKey,
			HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl=null;
		try {
			LOGGER.info("inside processdIdHandoverPdfDownload for serviceId : {} and task : {}", serviceId, taskDefKey);
			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if (scServiceDetailOpt.isPresent()) {
				scServiceDetail = scServiceDetailOpt.get();
			}

			ScOrder scOrder = scServiceDetail.getScOrder();

			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
			scServiceAttributes = scServiceDetail.getScServiceAttributes();

			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();

			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("orderAttributes : {}", scOrderAttributesmap);

			Map<String, String> scServiceAttributesmap = new HashMap<>();
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("serviceAttributes : {}", scServiceAttributesmap);

			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

			LOGGER.info("componentAttributes : {}", scServiceAttributesmap);


			Map<String, String> sipAttributes = new HashMap<>();
			if(scComponentAttributesmap.containsKey("sipServiceId")) {
				sipAttributes = commonFulfillmentUtils.getComponentAttributes(Integer.valueOf(scComponentAttributesmap.get("sipServiceId")),
						AttributeConstants.COMPONENT_LM, "A");
			}
			
			GscServiceAcceptancePdfBean saBean = setdidHandOveDetails(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap, serviceId, taskDefKey, sipAttributes);

			String html = getDIdHandoverHtml(saBean);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);

			String fileName = "gsc_dId_handovernote_" + scServiceDetail.getUuid() + ".pdf";
			byte[] outArray = bos.toByteArray();
			response.reset();
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			response.setContentLength(outArray.length);
			response.setHeader("Expires" + CommonConstants.COLON, "0");
			response.setHeader("Content-Disposition", ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());

			bos.flush();
			bos.close();
		} catch (Exception e) {
			LOGGER.error("Error in Processing  DID service handover note{}", e);
		}
		return tempDownloadUrl;

	}
	
	@Transactional(readOnly = false)
	public String processdIdHandoverPdf(String serviceUuid, Integer serviceId, String taskdefKey)
			throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			LOGGER.info("inside processdIdHandoverPdf for serviceId : {} and task : {}",serviceId,taskdefKey);
			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if (scServiceDetailOpt.isPresent()) {
				scServiceDetail = scServiceDetailOpt.get();
			}

			ScOrder scOrder = scServiceDetail.getScOrder();

			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
			scServiceAttributes = scServiceDetail.getScServiceAttributes();

			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();

			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("orderAttributes : {}", scOrderAttributesmap); 
			
			Map<String, String> scServiceAttributesmap = new HashMap<>();
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("serviceAttributes : {}", scServiceAttributesmap);

			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			
			Map<String, String> sipAttributes = new HashMap<>();
			if(scComponentAttributesmap.containsKey("sipServiceId")) {
				sipAttributes = commonFulfillmentUtils.getComponentAttributes(Integer.valueOf(scComponentAttributesmap.get("sipServiceId")),
						AttributeConstants.COMPONENT_LM, "A");
			}
			
			LOGGER.info("componentAttributes : {}", scServiceAttributesmap);

			GscServiceAcceptancePdfBean saBean = setdidHandOveDetails(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap, serviceId, taskdefKey,sipAttributes );

			String html = getDIdHandoverHtml(saBean);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);

			String fileName = "gsc_dId_handovernote_" + scServiceDetail.getUuid() + ".pdf";
			Attachment attachment = new Attachment();
			attachment.setName(fileName);
			attachment.setIsActive("Y");
			attachment.setCategory("Handover-note");
			attachment.setType("Handover-note");
			attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				LOGGER.info("SwiftApiEnabled for DID Handover Note");
				InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				fileName = ".pdf";

				if (scOrderAttributesmap.containsKey("CUSTOMER_CODE")
						&& scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE")) {
					LOGGER.info("Getting Stored Object...");
					StoredObject storedObject = fileStorageService.uploadObjectWithExten(fileName, inputStream,
							scOrderAttributesmap.get("CUSTOMER_CODE"), scOrderAttributesmap.get("CUSTOMER_LE_CODE"));

					LOGGER.info("Stored Object is -> {}", storedObject.getName());

					String[] pathArray = storedObject.getPath().split("/");
					attachment.setName(storedObject.getName());
					attachment.setUriPathOrUrl(pathArray[1]);
					LOGGER.info("DID service handover note Attachment {} to be saved at location {}",
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
				LOGGER.info("DID service handover note Attachment {} to be saved at location {}", attachment.getName(),
						attachment.getUriPathOrUrl());

				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(savedAttachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType("A");
				scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
				scAttachmentRepository.save(scAttachment);
				LOGGER.info("Attachment saved in scAttachment with id " + scAttachment.getId());
			}
		} catch (Exception e) {
			LOGGER.error("Error in Processing  DID service handover note{}", e);
		}
		return tempDownloadUrl;

	}

	private GscServiceAcceptancePdfBean setdidHandOveDetails(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap, Integer serviceId, String taskdefKey, Map<String, String> sipAttributes)
			throws TclCommonException, IllegalArgumentException, JsonParseException, JsonMappingException, IOException {

		LOGGER.info(" inside setdidHandOveDetails..");
		GscServiceAcceptancePdfBean saBean = new GscServiceAcceptancePdfBean();

		// OrderDetails
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		saBean.setOrderType(scOrder.getOrderType());
		saBean.setOrderCategory(scOrder.getOrderCategory());
		saBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		saBean.setProductType(scServiceAttributesmap.getOrDefault("serviceType", ""));
		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			saBean.setCustomerContractingEntity(
					scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		saBean.setCustomerAddress(scOrderAttributesmap.getOrDefault("Billing Address", ""));
		saBean.setGstnVatNo(scOrderAttributesmap.getOrDefault("LeStateGstNumber", ""));

		// Billing Details
		saBean.setDeemedAcceptanceApplicable("No");
		saBean.setCommissioningDate("NA");

		//sip details
		List<SipTrunkDetails> sipTrunkList = constructSiptrunkDetails(sipAttributes);
		if(sipTrunkList!=null && !sipTrunkList.isEmpty()) {
			saBean.setSipTrunkDetails(sipTrunkList);
			saBean.setIsSipTrunkAvailable(true);
			LOGGER.info("SiP Trunk Details {} :", Utils.convertObjectToJson(saBean.getSipTrunkDetails()));
		}
		else {
			saBean.setIsSipTrunkAvailable(false);
			LOGGER.info("SiP Trunk Details is empty{}");
		}

		// Number and Country Details
		List<NumberCountryDetailsBean> numberCountryDetailsList = new ArrayList<NumberCountryDetailsBean>();
		LOGGER.info("task def key:{}", taskdefKey);
		List<ScAsset> scAssets = scAssetRepository.findByScServiceDetail_idAndStatus(serviceId,
				taskdefKey);
		scAssets.forEach(asset -> {
			NumberCountryDetailsBean numberCountryDetailsBean = new NumberCountryDetailsBean();
			numberCountryDetailsBean.setOriginCountry(scServiceDetail.getSourceCountry());
			numberCountryDetailsBean.setNumbers(asset.getName());
			numberCountryDetailsBean.setAccessType(scServiceDetail.getAccessType());
			ScAssetAttribute scAssetAttribute = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeName(asset.getId(), "billingDate");
			if (Objects.nonNull(scAssetAttribute)) {
				numberCountryDetailsBean.setBillingStartDate(scAssetAttribute.getAttributeValue());
			}
			else {
				numberCountryDetailsBean.setBillingStartDate("");
			}
			numberCountryDetailsList.add(numberCountryDetailsBean);
		});
		saBean.setNumberCountryDetails(numberCountryDetailsList);
		return saBean;
	}

	private String getDIdHandoverHtml(GscServiceAcceptancePdfBean saBean) {
		Map<String, Object> variable = objectMapper.convertValue(saBean, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("gsc_dId_handover_pdf", context);
	}

	@Transactional(readOnly = false)
	public String processVasHandoverPdfDownload(String serviceCode, Integer serviceId, Integer flowGroupId,
			HttpServletResponse response) {
		String tempDownloadUrl = null;
		try {

			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if (scServiceDetailOpt.isPresent()) {
				scServiceDetail = scServiceDetailOpt.get();
			}
			ScOrder scOrder = scServiceDetail.getScOrder();

			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
			scServiceAttributes = scServiceDetail.getScServiceAttributes();

			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();

			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			Map<String, String> scServiceAttributesmap = new HashMap<>();
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("getServiceAttributes response : {}", scServiceAttributesmap);

			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

			GscServiceAcceptancePdfBean saBean = setSaBean(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap, serviceId, flowGroupId);

			String accessType = scServiceDetail.getAccessType();
			String html = "";
			if(!isPublicIP(accessType)) {
				html = getHtml(saBean); 
			}else {
				html = getHtmlForVas(saBean); 
			}
			
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "gsc_vas_handovernote_" + scServiceDetail.getUuid() + ".pdf";
			response.reset();
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			response.setContentLength(outArray.length);
			response.setHeader("Expires" + CommonConstants.COLON, "0");
			response.setHeader("Content-Disposition", ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
			
			FileCopyUtils.copy(outArray, response.getOutputStream());
			
			bos.flush();
			bos.close();
		} catch (Exception e) {
			LOGGER.error("Error in Processing VAS Service Acceptance {}", e);
		}
		return tempDownloadUrl;
	}
	
	@Transactional(readOnly = false)
	public String processSipHandoverPdfDownload(String serviceUuid, Integer serviceId, String taskDefKey,
			HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl=null;
		try {
			LOGGER.info("inside processSipHandoverPdfDownload for serviceId : {} and task : {}", serviceId, taskDefKey);
			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if (scServiceDetailOpt.isPresent()) {
				scServiceDetail = scServiceDetailOpt.get();
			}

			ScOrder scOrder = scServiceDetail.getScOrder();

			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
			scServiceAttributes = scServiceDetail.getScServiceAttributes();

			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();

			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("orderAttributes : {}", scOrderAttributesmap);

			Map<String, String> scServiceAttributesmap = new HashMap<>();
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("serviceAttributes : {}", scServiceAttributesmap);

//			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
//					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAndAdditionalAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_GSCLICENSE, "A");

			LOGGER.info("componentAttributes : {}", scServiceAttributesmap);

			GscServiceAcceptancePdfBean saBean = setSipHandOveDetails(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap, serviceId);

			String html = getSipHandoverHtml(saBean);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);

			String fileName = "gsc_sip_handovernote_" + scServiceDetail.getUuid() + ".pdf";
			byte[] outArray = bos.toByteArray();
			response.reset();
			response.setContentType(MediaType.APPLICATION_PDF_VALUE);
			response.setContentLength(outArray.length);
			response.setHeader("Expires" + CommonConstants.COLON, "0");
			response.setHeader("Content-Disposition", ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());

			bos.flush();
			bos.close();
		} catch (Exception e) {
			LOGGER.error("Error in Processing  SIP service handover note{}", e);
		}
		return tempDownloadUrl;

	}
	
	@Transactional(readOnly = false)
	public String processSipHandoverPdf(String serviceUuid, Integer serviceId, String taskdefKey)
			throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			LOGGER.info("inside processSipHandoverPdf for serviceId : {} and task : {}",serviceId,taskdefKey);
			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if (scServiceDetailOpt.isPresent()) {
				scServiceDetail = scServiceDetailOpt.get();
			}

			ScOrder scOrder = scServiceDetail.getScOrder();

			Set<ScServiceAttribute> scServiceAttributes = new HashSet<>();
			scServiceAttributes = scServiceDetail.getScServiceAttributes();

			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();

			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("orderAttributes : {}", scOrderAttributesmap); 
			
			Map<String, String> scServiceAttributesmap = new HashMap<>();
			scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

			LOGGER.info("serviceAttributes : {}", scServiceAttributesmap);

//          Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
//					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAndAdditionalAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_GSCLICENSE, "A");
			
			LOGGER.info("componentAttributes : {}", scServiceAttributesmap);

			GscServiceAcceptancePdfBean saBean = setSipHandOveDetails(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap, serviceId);

			String html = getSipHandoverHtml(saBean);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);

			String fileName = "gsc_sip_handovernote_" + scServiceDetail.getUuid() + ".pdf";
			Attachment attachment = new Attachment();
			attachment.setName(fileName);
			attachment.setIsActive("Y");
			attachment.setCategory("Handover-note");
			attachment.setType("Handover-note");
			attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));

			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				LOGGER.info("SwiftApiEnabled for SIP Handover Note");
				InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				fileName = ".pdf";

				if (scOrderAttributesmap.containsKey("CUSTOMER_CODE")
						&& scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE")) {
					LOGGER.info("Getting Stored Object...");
					StoredObject storedObject = fileStorageService.uploadObjectWithExten(fileName, inputStream,
							scOrderAttributesmap.get("CUSTOMER_CODE"), scOrderAttributesmap.get("CUSTOMER_LE_CODE"));

					LOGGER.info("Stored Object is -> {}", storedObject.getName());

					String[] pathArray = storedObject.getPath().split("/");
					attachment.setName(storedObject.getName());
					attachment.setUriPathOrUrl(pathArray[1]);
					LOGGER.info("SIP service handover note Attachment {} to be saved at location {}",
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
				LOGGER.info("SIP service handover note Attachment {} to be saved at location {}", attachment.getName(),
						attachment.getUriPathOrUrl());

				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(savedAttachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType("A");
				scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
				scAttachmentRepository.save(scAttachment);
				LOGGER.info("Attachment saved in scAttachment with id " + scAttachment.getId());
			}
		} catch (Exception e) {
			LOGGER.error("Error in Processing  SIP service handover note{}", e);
		}
		return tempDownloadUrl;

	}
	
	private GscServiceAcceptancePdfBean setSipHandOveDetails(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap, Integer serviceId)
			throws TclCommonException, IllegalArgumentException, JsonParseException, JsonMappingException, IOException {

		LOGGER.info(" inside setSipHandOveDetails..");
		GscServiceAcceptancePdfBean saBean = new GscServiceAcceptancePdfBean();

		// OrderDetails
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		saBean.setOrderType(scOrder.getOrderType());
		saBean.setOrderCategory(scOrder.getOrderCategory());
		saBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		saBean.setProductType(scServiceAttributesmap.getOrDefault("serviceType", ""));
		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			saBean.setCustomerContractingEntity(
					scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		saBean.setCustomerAddress(scOrderAttributesmap.getOrDefault("Billing Address", ""));
		saBean.setGstnVatNo(scOrderAttributesmap.getOrDefault("LeStateGstNumber", ""));

		// Billing Details
		saBean.setDeemedAcceptanceApplicable("No");
		saBean.setCommissioningDate("NA");
		
		// Sip Trunk DEtails
		List<SipTrunkDetails> sipTrunkList = constructSiptrunkDetails(scComponentAttributesmap);
		saBean.setSipTrunkDetails(sipTrunkList);
		LOGGER.info("SiP Trunk Details {} :", Utils.convertObjectToJson(saBean.getSipTrunkDetails()));

		return saBean;
	}

	public List<SipTrunkDetails> constructSiptrunkDetails(Map<String, String> scComponentAttributesmap) throws TclCommonException {
		List<SipTrunkDetails> sipTrunkList = new ArrayList<SipTrunkDetails>();
		String trunkString = scComponentAttributesmap.getOrDefault("trunks", "");
		if (StringUtils.trimToNull(trunkString)!=null) {
			TrunkBean[] trunks = Utils.convertJsonToObject(trunkString, TrunkBean[].class);
			for (TrunkBean trunk : trunks) {
				SipTrunkDetails sipTrunkDetails = new SipTrunkDetails();
				sipTrunkDetails.setSipInformation(trunk.getGsx());
				sipTrunkDetails.setTrunkName(" ");
				sipTrunkDetails.setSipSignalingIp(" ");
				sipTrunkDetails.setMediaIp1(" ");
				sipTrunkDetails.setMediaIp2(" ");
				String concurrentChannel = scComponentAttributesmap.getOrDefault("noOfConcurrentChannel", "") + "CC"
						+ "/" + scComponentAttributesmap.getOrDefault("callsPerSecondCps", "") + "CPS";
				sipTrunkDetails.setConcurrentChannels1(concurrentChannel);
				sipTrunkDetails.setConcurrentChannels2(concurrentChannel);
				sipTrunkDetails.setVoipProtocol("SIP");
				sipTrunkDetails.setSipTransportProtocol(scComponentAttributesmap.getOrDefault("transportProtocol", ""));
				
				if (scComponentAttributesmap.getOrDefault("requiredOnABNumberE164", "").equalsIgnoreCase("Yes")) {
					sipTrunkDetails.setNumberingFormat("E.164");
				}
				sipTrunkDetails.setCodecsAllowed(scComponentAttributesmap.getOrDefault("codec", ""));
				sipTrunkDetails.setDtmfType(scComponentAttributesmap.getOrDefault("dtmfRelaySupport", ""));
				
				String customerPublicIp=scComponentAttributesmap.getOrDefault("customerPublicIp", "");
				if(customerPublicIp.startsWith("[") && customerPublicIp.endsWith("]")) {
					List<String> customerPublicIps = Utils.convertJsonToObject(customerPublicIp, ArrayList.class);
					sipTrunkDetails.setCustomerSBCIp(String.join(" ,\n", customerPublicIps));
				}
				else {
						sipTrunkDetails.setCustomerSBCIp(customerPublicIp);
				}
				sipTrunkList.add(sipTrunkDetails);
			}
		}
		return sipTrunkList;
	}

	private String getSipHandoverHtml(GscServiceAcceptancePdfBean saBean) {
		Map<String, Object> variable = objectMapper.convertValue(saBean, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("gsc_sip_handover_pdf", context);
	}
}
