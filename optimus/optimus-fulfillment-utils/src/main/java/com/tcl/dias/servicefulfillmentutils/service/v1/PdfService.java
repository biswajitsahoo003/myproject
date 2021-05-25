package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcl.dias.common.beans.CustomerCodeBean;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceSla;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceSlaRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceAcceptancePdfBean;
import com.tcl.dias.servicefulfillmentutils.beans.WelcomeLetterPdfBean;
import com.tcl.dias.servicefulfillmentutils.helper.UploadAttachmentComponent;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the PdfService methods for pdf generation
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Service
public class PdfService {

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	ScServiceSlaRepository scServiceSlaRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	SpringTemplateEngine templateEngine;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	UploadAttachmentComponent uploadAttachmentComponent;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${file.upload-dir}")
	String uploadPath;

	@Autowired
	AttachmentRepository attachmentRepository;

	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	AttachmentService attachmentService;
	
	@Autowired
	TaskRepository taskRepository;
	
	@Autowired
	ScAttachmentRepository scAttachmentRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Value("${queue.serviceactivation.handovernote}")
	String servicehandovernote_queue;
	
	@Value("${queue.izopc.serviceactivation.handovernote}")
	String izopcServicehandovernote_queue;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

	private static final Logger LOGGER = LoggerFactory.getLogger(PdfService.class);

	/**
	 * 
	 * processServiceAcceptancePdf
	 * @param serviceId 
	 * 
	 * @return
	 * @return
	 * @throws TclCommonException
	 */
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
			Map<String, String> scComponentAttributesBmap= commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "B");
			
			if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IZOPC")) {
				LOGGER.info("IZOPC HandoverNote ServiceId::{}",scServiceDetail.getId());
				return processIZOPCServiceAcceptancePdf(serviceUuid,serviceId,scServiceDetail, scOrder, scOrderAttributesmap,scServiceAttributesmap, scComponentAttributesmap);
			}
			
			ServiceAcceptancePdfBean saBean = setSaBean(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap,scComponentAttributesBmap);

			String html = getHtml(saBean);

			//LOGGER.info(html);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "sc_handovernote_" + scServiceDetail.getUuid() + ".pdf";

			Attachment attachment = new Attachment();
			attachment.setName(fileName);
			attachment.setIsActive("Y");
			attachment.setCategory("Handover-note");
			attachment.setType("Handover-note");
			attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				LOGGER.info("SwiftApiEnabled for Handover Note with service Id {} ",serviceUuid);
				InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				fileName = ".pdf";

				if (scOrderAttributesmap.containsKey("CUSTOMER_CODE")
						&& scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE")) {
					LOGGER.info("Getting Stored Object...");
					StoredObject storedObject = fileStorageService.uploadObjectWithExten(fileName, inputStream,
							scOrderAttributesmap.get("CUSTOMER_CODE"), scOrderAttributesmap.get("CUSTOMER_LE_CODE"));

					LOGGER.info("Stored Object for Handover Note is -> {}", storedObject.getName());

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
					LOGGER.info("PDF service handover note Attachment {} to be saved at location {} for service Id{}",
							attachment.getName(), pathArray[1],serviceUuid);
					Attachment savedAttachment = attachmentRepository.save(attachment);

					ScAttachment scAttachment = new ScAttachment();
					scAttachment.setAttachment(savedAttachment);
					scAttachment.setScServiceDetail(scServiceDetail);
					scAttachment.setIsActive("Y");
					scAttachment.setSiteType("A");
					scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
					scAttachmentRepository.save(scAttachment);
					LOGGER.info("PDF service handover note Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
					scAttachment.getId(),serviceId,serviceUuid);

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
				LOGGER.info("PDF service handover note Attachment {} to be saved at location {} for service id {}",
						attachment.getName(),attachment.getUriPathOrUrl(),serviceUuid);

				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(savedAttachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType("A");
				scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
				scAttachmentRepository.save(scAttachment);
				LOGGER.info("PDF service handover note Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
						scAttachment.getId(),serviceId,serviceUuid);
				/*
				 * if (cofObjectMapper != null) { cofObjectMapper.put("FILE_SYSTEM_PATH",
				 * fileFullPath); }
				 */ }

	
			
		} catch (Exception e) {
			LOGGER.error("Error in Processing Service Acceptance {}",e);
		}
		return tempDownloadUrl;

	}
	
	private ServiceAcceptancePdfBean setByonIllBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap) throws TclCommonException, IllegalArgumentException {

		ServiceAcceptancePdfBean saBean = new ServiceAcceptancePdfBean();
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		saBean.setServiceId(scServiceDetail.getUuid());
		saBean.setOrderType(scServiceDetail.getOrderType());
		saBean.setOrderCategory(scServiceDetail.getOrderCategory()!=null?scServiceDetail.getOrderCategory():"");
		saBean.setProductName(scServiceDetail.getErfPrdCatalogProductName());
		saBean = getSlaValues(scServiceDetail.getId(), saBean);
		saBean.setLastMileType(scServiceDetail.getLastmileConnectionType());
		saBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress", ""));
		saBean.setDemarcationBuildingName(scComponentAttributesmap.getOrDefault("demarcationBuildingName",""));
		saBean.setDemarcationFloor(scComponentAttributesmap.getOrDefault("demarcationFloor",""));
		saBean.setDemarcationRoom(scComponentAttributesmap.getOrDefault("demarcationRoom",""));
		saBean.setDemarcationWing(scComponentAttributesmap.getOrDefault("demarcationWing", ""));
		String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
		saBean.setBillStartDate(billStartDate);
		saBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", ""));
		saBean.setPortBandwidth(scServiceAttributesmap.get("portBandwidth")!=null?scServiceAttributesmap.get("portBandwidth")+" Mbps":"");
		saBean.setLocalLoopBandwidth(scServiceAttributesmap.get("localLoopBandwidth")!=null?scServiceAttributesmap.get("localLoopBandwidth")+" Mbps":"");
		saBean.setThirdPartyServiceId(scServiceAttributesmap.getOrDefault("thirdPartyServiceID", ""));
		saBean.setThirdPartyWanIpAddress(scServiceAttributesmap.getOrDefault("thirdPartyIPAddress", ""));
		saBean.setThirdPartyProviderName(scServiceAttributesmap.getOrDefault("thirdPartyProviderName", ""));
		saBean.setThirdPartylinkUptimeAgreement(scServiceAttributesmap.getOrDefault("thirdPartylinkUptimeAgreement", ""));
		return saBean;
	}

	private ServiceAcceptancePdfBean setSaBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap, Map<String, String> scComponentAttributesBmap) throws TclCommonException, IllegalArgumentException {

		ServiceAcceptancePdfBean saBean = new ServiceAcceptancePdfBean();
		String response = (String) mqUtils.sendAndReceive(servicehandovernote_queue, scServiceDetail.getUuid());
		if (response != null) {

			Map<String, Object> handoverAttr = (Map<String, Object>) Utils.convertJsonToObject(response, Map.class);
			if (handoverAttr != null) {
				
				saBean.setBaSwitchHostName(
						handoverAttr.get("baSwitchHostName") != null ? (String) handoverAttr.get("baSwitchHostName")
								: "");
				saBean.setBaSwitchIp(
						handoverAttr.get("baSwitchIp") != null ? (String) handoverAttr.get("baSwitchIp") : "");
				saBean.setBaSwitchPort(
						handoverAttr.get("baSwitchPort") != null ? (String) handoverAttr.get("baSwitchPort") : "");
				/*saBean.setInterfaceType(
						handoverAttr.get("interfaceType") != null ? (String) handoverAttr.get("interfaceType") : "");*/
				saBean.setWanInterfaceType(
						handoverAttr.get("wanInterfaceType") != null ? (String) handoverAttr.get("wanInterfaceType")
								: "");
				saBean.setRoutingProtocol(
						handoverAttr.get("routingProtocol") != null ? (String) handoverAttr.get("routingProtocol")
								: "");
				saBean.setExtendedLanRequired(handoverAttr.get("extendedLanRequired") != null
						? (String) handoverAttr.get("extendedLanRequired")
						: "");
				saBean.setWanv4Address(
						handoverAttr.get("wanv4Address") != null ? (String) handoverAttr.get("wanv4Address") : "");
				saBean.setLanv4Address(
						handoverAttr.get("lanv4Address") != null ? (String) handoverAttr.get("lanv4Address") : "");
				saBean.setLanv6Address(
						handoverAttr.get("lanv6Address") != null ? (String) handoverAttr.get("lanv6Address") : "");
				saBean.setWanv6Address(
						handoverAttr.get("wanv6Address") != null ? (String) handoverAttr.get("wanv6Address") : "");
			}
		}
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		saBean.setServiceId(scServiceDetail.getUuid());
		saBean.setOrderType(OrderCategoryMapping.getOrderType(scServiceDetail, scOrder));
		String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);

		saBean.setOrderCategory(orderCategory!=null?orderCategory:"");

		String prodName="";
		if (scServiceDetail.getErfPrdCatalogProductName() != null) {
			if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("ias")) {
				prodName = "IAS - Internet Access Service";

			}
			else if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("gvpn")) {
				prodName = "GVPN - Global Virtual Private Network";
			}
			else if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("npl")) {
				prodName="NPL";
			}
		}
		saBean.setProductName(prodName);
		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			saBean.setCustomerContractingEntity(scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "") + ";"
				+ scOrderAttributesmap.getOrDefault("GST_Address", ""));

		saBean = getSlaValues(scServiceDetail.getId(), saBean);

		saBean.setLastMileType(scServiceDetail.getLastmileConnectionType());
		saBean.setLocalLoopProvider(scComponentAttributesmap.getOrDefault("lmType", ""));

		saBean.setCpeManagement(scComponentAttributesmap.getOrDefault("cpeManagementType", ""));
		saBean.setCpeSerialNumbers(scComponentAttributesmap.getOrDefault("cpeSerialNumber", ""));
		saBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress", ""));
		saBean.setDemarcationBuildingName(scComponentAttributesmap.getOrDefault("demarcationBuildingName",""));
		saBean.setDemarcationFloor(scComponentAttributesmap.getOrDefault("demarcationFloor",""));
		saBean.setDemarcationRoom(scComponentAttributesmap.getOrDefault("demarcationRoom",""));
		saBean.setDemarcationWing(scComponentAttributesmap.getOrDefault("demarcationWing", ""));
		
		saBean.setBsiteAddress(scComponentAttributesBmap.get("siteAddress"));
		saBean.setBdemarcationBuildingName(scComponentAttributesBmap.getOrDefault("demarcationBuildingName",""));
		saBean.setBdemarcationFloor(scComponentAttributesBmap.getOrDefault("demarcationFloor",""));
		saBean.setBdemarcationRoom(scComponentAttributesBmap.getOrDefault("demarcationRoom",""));
		saBean.setBdemarcationWing(scComponentAttributesBmap.getOrDefault("demarcationWing", ""));
		saBean.setCloudName(scComponentAttributesBmap.getOrDefault("cloudName", ""));
		saBean.setBinterfaceType(scComponentAttributesBmap.getOrDefault("interfaceType", ""));
		saBean.setBconnectorType(scComponentAttributesBmap.getOrDefault("Connector Type", ""));

		String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
		saBean.setBillStartDate(billStartDate);
		saBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", ""));
		//saBean.setSaEscalationMatrix("");
		saBean.setPortBandwidth(scComponentAttributesmap.get("portBandwidth")!=null?scComponentAttributesmap.get("portBandwidth")+" Mbps":"");
		saBean.setLocalLoopBandwidth(scComponentAttributesmap.get("localLoopBandwidth")!=null?scComponentAttributesmap.get("localLoopBandwidth")+" Mbps":"");

		saBean.setEndMuxNodeIp(scComponentAttributesmap.getOrDefault("endMuxNodeIp", ""));
		saBean.setEndMuxNodeName(scComponentAttributesmap.getOrDefault("endMuxNodeName", ""));
		saBean.setInterfaceType(scComponentAttributesmap.getOrDefault("interfaceType", ""));
		saBean.setConnectorType(scServiceAttributesmap.getOrDefault("Connector Type", ""));
		saBean.setAsNumber(scComponentAttributesmap.getOrDefault("asNumber", ""));
		saBean.setDeemedAcceptanceApplicable(scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
		String commissioningDate = formatDate(scComponentAttributesmap.getOrDefault("commissioningDate", ""));
		saBean.setCommissioningDate(commissioningDate);
		

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

	private String getHtml(ServiceAcceptancePdfBean saBean) {
		Map<String, Object> variable = objectMapper.convertValue(saBean, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		
		if (saBean.getProductName().equalsIgnoreCase("NPL")) {
			return templateEngine.process("npl_service_acceptance_pdf", context);
		}else if (saBean.getProductName().equalsIgnoreCase("IZOSDWAN") || saBean.getProductName().equalsIgnoreCase("IZO SDWAN")) {
			LOGGER.info("IZOSDWAN Template");
			return templateEngine.process("sdwan_service_acceptance_pdf", context);
		}else if (saBean.getProductName().equalsIgnoreCase("IZOSDWAN_CGW")) {
			LOGGER.info("IZOSDWAN CGW Template");
			return templateEngine.process("sdwan_cgw_service_acceptance_pdf", context);
		}else if (saBean.getProductName().equalsIgnoreCase("GVPN - IZO Private Connect")) {
			LOGGER.info("IZOPC Template");
			return templateEngine.process("izopc_service_acceptance_pdf", context);
		}
		else {
			return templateEngine.process("service_acceptance_pdf", context);
		}
		
	}
	
	

	private ServiceAcceptancePdfBean getSlaValues(Integer serviceId, ServiceAcceptancePdfBean saBean) {
		List<ScServiceSla> slas = scServiceSlaRepository.findAllByScServiceDetail_Id(serviceId);
		if (!CollectionUtils.isEmpty(slas))
			slas.forEach(scServiceSla -> {
				if (scServiceSla.getSlaComponent().equalsIgnoreCase("Round Trip Delay (RTD)"))
					saBean.setRountTripDelay(scServiceSla.getSlaValue());
				if (scServiceSla.getSlaComponent().equalsIgnoreCase("Packet Drop"))
					saBean.setPacketDrop(scServiceSla.getSlaValue());
				if (scServiceSla.getSlaComponent().equalsIgnoreCase("Network Uptime"))
					saBean.setNetworkUptime(scServiceSla.getSlaValue());
				if (scServiceSla.getSlaComponent().equalsIgnoreCase("Service Availability %"))
					saBean.setServiceAvailability(scServiceSla.getSlaValue());
			});
		return saBean;
	}

	@Transactional(readOnly = false)
	public String processSdwanServiceAcceptancePdf(String serviceCode, Integer serviceId) throws TclCommonException {
		LOGGER.info("processSdwanServiceAcceptancePdf method invoked");	
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

			List<String> componentGroups = new ArrayList<>();
			componentGroups.add("UNDERLAY");
			componentGroups.add("OVERLAY");
			ServiceAcceptancePdfBean sdwanBean=null;
			List<ServiceAcceptancePdfBean> saBeanList= new ArrayList<>();
			List<ScServiceDetail> underlayServiceDetailList = new ArrayList<>();
			List<Map<String,Integer>> layDetails=scSolutionComponentRepository.findScServiceDetailByComponentType(scServiceDetail.getScOrderUuid(),componentGroups,"Y",scServiceDetail.getId());
			if(layDetails!=null && !layDetails.isEmpty()){
				LOGGER.info("Details Exists: {}",layDetails);
				for (Map<String,Integer> overUnderlayMap : layDetails) {
					LOGGER.info("Underlay Ids: {}",overUnderlayMap.get("underlayIds"));
					LOGGER.info("Overlay Ids: {}",overUnderlayMap.get("overlayIds"));
					String underlays=String.valueOf(overUnderlayMap.get("underlayIds"));
					String[] underlayList= underlays.split(",");
					String associatedServiceIds="";
			    	for(String underlay:underlayList){
			    		Integer underlayId=Integer.valueOf(underlay);
			    		Optional<ScServiceDetail> underlayServiceDetailOptional=scServiceDetailRepository.findById(underlayId);
			    		if(underlayServiceDetailOptional.isPresent()){
			    			ScServiceDetail underlayServiceDetail=underlayServiceDetailOptional.get();
			    			associatedServiceIds+=underlayServiceDetail.getUuid()+",";
			    			LOGGER.info("ProductName: {}",underlayServiceDetail.getErfPrdCatalogProductName());
			    			if("BYON Internet".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())){
			    				LOGGER.info("BYON Internet: {}",underlayServiceDetail.getId());
			    				Set<ScServiceAttribute> underlayServiceAttributes = new HashSet<>();			
			    				underlayServiceAttributes = underlayServiceDetail.getScServiceAttributes();
				    			Map<String, String> underlayServiceAttributesmap = new HashMap<>();
				    			underlayServiceAttributesmap = underlayServiceAttributes.stream().collect(HashMap::new,
				    					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
				    			Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(underlayServiceDetail.getId(), "LM", "A");
				    			ServiceAcceptancePdfBean underlayBean=setByonIllBean(underlayServiceDetail, scOrder, scOrderAttributesmap, underlayServiceAttributesmap, scComponentAttributesmap);
				    			saBeanList.add(underlayBean);
			    			}else if("IAS".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())
			    					|| ("GVPN".equalsIgnoreCase(underlayServiceDetail.getErfPrdCatalogProductName())
			    							&& underlayServiceDetail.getDestinationCountry().equalsIgnoreCase("India"))){
			    				LOGGER.info("IAS or GVPN: {}",underlayServiceDetail.getId());
			    				underlayServiceDetailList.add(underlayServiceDetail);
			    				Set<ScServiceAttribute> underlayServiceAttributes = new HashSet<>();			
			    				underlayServiceAttributes = underlayServiceDetail.getScServiceAttributes();
				    			Map<String, String> underlayServiceAttributesmap = new HashMap<>();
				    			underlayServiceAttributesmap = underlayServiceAttributes.stream().collect(HashMap::new,
				    					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
				    			Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(underlayServiceDetail.getId(), "LM", "A");
				    			Map<String, String> scComponentAttributesBmap= commonFulfillmentUtils.getComponentAttributes(underlayServiceDetail.getId(), "LM", "B");
				    			ServiceAcceptancePdfBean underlayBean=setSaBean(underlayServiceDetail, scOrder, scOrderAttributesmap, underlayServiceAttributesmap, scComponentAttributesmap,
				    					scComponentAttributesBmap);
				    			saBeanList.add(underlayBean);
			    			}
			    		}
			    	}
			    	sdwanBean =setSdwanBean(scServiceDetail, scOrder, scOrderAttributesmap,
							scServiceAttributesmap,associatedServiceIds);
			    	LOGGER.info("UnderlayList size: {}",saBeanList.size());
			    	sdwanBean.setServiceAcceptancePdfBeanList(saBeanList);
			    	LOGGER.info("SdwanUnderlayList size: {}",sdwanBean.getServiceAcceptancePdfBeanList().size());
				}
			}
			
		
			
			if (sdwanBean != null) {

				String html = getHtml(sdwanBean);

				// LOGGER.info(html);

				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PDFGenerator.createPdf(html, bos);
				byte[] outArray = bos.toByteArray();
				String fileName = "sc_handovernote_" + scServiceDetail.getUuid() + ".pdf";

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
								scOrderAttributesmap.get("CUSTOMER_CODE"),
								scOrderAttributesmap.get("CUSTOMER_LE_CODE"));
						LOGGER.info("Stored Object is -> {}", storedObject.getName());
						String[] pathArray = storedObject.getPath().split("/");
						attachment.setName(storedObject.getName());
						attachment.setUriPathOrUrl(pathArray[1]);
						LOGGER.info("PDF service handover note Attachment {} to be saved at location {}",
								attachment.getName(), pathArray[1]);
						Attachment savedAttachment = attachmentRepository.save(attachment);
						List<ScAttachment> scAttachmentList = new ArrayList<>();
						ScAttachment scAttachment = new ScAttachment();
						scAttachment.setAttachment(savedAttachment);
						scAttachment.setScServiceDetail(scServiceDetail);
						scAttachment.setIsActive("Y");
						scAttachment.setSiteType("A");
						scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
						scAttachmentList.add(scAttachment);
						if(!underlayServiceDetailList.isEmpty()){
							LOGGER.info("underlayServiceDetailList size::{} " ,underlayServiceDetailList.size());
							for(ScServiceDetail underlayServiceDetail:underlayServiceDetailList){
								ScAttachment underlayAttachment = new ScAttachment();
								underlayAttachment.setAttachment(savedAttachment);
								underlayAttachment.setScServiceDetail(underlayServiceDetail);
								underlayAttachment.setIsActive("Y");
								underlayAttachment.setSiteType("A");
								underlayAttachment.setOrderId(underlayServiceDetail.getScOrder().getId());
								scAttachmentList.add(underlayAttachment);
							}
						}
						scAttachmentRepository.saveAll(scAttachmentList);
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
					LOGGER.info("PDF service handover note Attachment {} to be saved at location {}",
							attachment.getName(), attachment.getUriPathOrUrl());
					ScAttachment scAttachment = new ScAttachment();
					scAttachment.setAttachment(savedAttachment);
					scAttachment.setScServiceDetail(scServiceDetail);
					scAttachment.setIsActive("Y");
					scAttachment.setSiteType("A");
					scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
					scAttachmentRepository.save(scAttachment);
					
					if(!underlayServiceDetailList.isEmpty()){
						LOGGER.info("underlayServiceDetailList size::{} " ,underlayServiceDetailList.size());
						for(ScServiceDetail underlayServiceDetail:underlayServiceDetailList){
							ScAttachment underlayAttachment = new ScAttachment();
							scAttachment.setAttachment(savedAttachment);
							scAttachment.setScServiceDetail(underlayServiceDetail);
							scAttachment.setIsActive("Y");
							scAttachment.setSiteType("A");
							scAttachment.setOrderId(underlayServiceDetail.getScOrder().getId());
							scAttachmentRepository.save(underlayAttachment);
						}
					}
					LOGGER.info("Attachment saved in scAttachment with id " + scAttachment.getId());
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in Processing SDWAN Service Acceptance {}",e);
		}
		return tempDownloadUrl;

	}

	private ServiceAcceptancePdfBean setSdwanBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap, String associatedServiceIds) {
		LOGGER.info("SetSdwanBean method invoked::{} ",scServiceDetail.getId());
		ServiceAcceptancePdfBean saBean = new ServiceAcceptancePdfBean();
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		saBean.setServiceId(scServiceDetail.getUuid());
		saBean.setAssociatedServiceIds(associatedServiceIds.substring(0, associatedServiceIds.length() - 1));
		saBean.setOrderType(scServiceDetail.getOrderType());
		saBean.setOrderCategory(scServiceDetail.getOrderCategory()!=null?scServiceDetail.getOrderCategory():"");
		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			saBean.setCustomerContractingEntity(scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "") + ";"
				+ scOrderAttributesmap.getOrDefault("GST_Address", ""));
		
		saBean.setProductName(getProductName(scServiceDetail.getErfPrdCatalogProductName()));
		
		
		Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
		if(!scComponentAttributesmap.isEmpty()){
			LOGGER.info("ScComponentAttributesmap exists::{} ",scComponentAttributesmap);
			saBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress",""));
			saBean.setCpeManagement(scComponentAttributesmap.getOrDefault("cpeManagementType",""));
			String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
			saBean.setBillStartDate(billStartDate);
			saBean.setDeemedAcceptanceApplicable(scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
			saBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", null));
			String commissioningDate = formatDate(scComponentAttributesmap.getOrDefault("commissioningDate", ""));
			saBean.setCommissioningDate(commissioningDate);
		}
		List<ScComponentAttribute> cpeSerialAttrList=scComponentAttributesRepository.findByScServiceDetailIdAndAttributeName(scServiceDetail.getId(), "cpeSerialNumber");
		String[] serialNumber = {""};
		if(cpeSerialAttrList!=null && !cpeSerialAttrList.isEmpty()){
			LOGGER.info("cpeSerialAttrList exists::{} ",cpeSerialAttrList.size());
			cpeSerialAttrList.stream().filter(cpeSerialAttr -> cpeSerialAttr.getAttributeValue()!=null).forEach(cpeSerialAttr -> {
				serialNumber[0]=serialNumber[0]+cpeSerialAttr.getAttributeValue()+",";
			});
			if(!serialNumber[0].isEmpty()){
				serialNumber[0]=serialNumber[0].substring(0, serialNumber[0].length() - 1);
			}
		}
		saBean.setCpeSerialNumbers(serialNumber[0]);
		return saBean;
	}

	
	public String getProductName(String productName){
		if (productName != null) {
			if (productName.equalsIgnoreCase("ias")) {
				productName = "IAS - Internet Access Service";

			}else if (productName.equalsIgnoreCase("gvpn")) {
				productName = "GVPN - Global Virtual Private Network";
			}
		}
		return productName;
	}

	@Transactional(readOnly = false)
	public String processSdwanCGWServiceAcceptancePdf(String serviceCode, Integer serviceId) throws TclCommonException {
		LOGGER.info("processSdwanCGWServiceAcceptancePdf method invoked");	
		String tempDownloadUrl = null;
		try {
			Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail = new ScServiceDetail();
			if (scServiceDetailOpt.isPresent()) {
				scServiceDetail = scServiceDetailOpt.get();
			}
			ScOrder scOrder = scServiceDetail.getScOrder();
			Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
			scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();
			Map<String, String> scOrderAttributesmap = new HashMap<>();
			scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
					(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);
			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			ServiceAcceptancePdfBean saBean = new ServiceAcceptancePdfBean();
			saBean.setOrderId(scServiceDetail.getScOrderUuid());
			saBean.setServiceId(scServiceDetail.getUuid());
			saBean.setAssociatedServiceIds(scServiceDetail.getPriSecServiceLink());
			saBean.setOrderType(scServiceDetail.getOrderType());
			saBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
			saBean.setOrderCategory(scServiceDetail.getOrderCategory());
			if (scOrder.getScContractInfos1().stream().findFirst() != null)
				saBean.setCustomerContractingEntity(
						scOrder.getScContractInfos1().stream().findFirst().get().getErfCustLeName());
			saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "") + ";"
					+ scOrderAttributesmap.getOrDefault("GST_Address", ""));

			saBean.setProductName(getProductName(scServiceDetail.getErfPrdCatalogProductName()));
			if (!scComponentAttributesmap.isEmpty()) {
				LOGGER.info("scComponentAttributesmap exists::{}",scComponentAttributesmap);	
				if(scComponentAttributesmap.containsKey("aggregationBandwidth") && scComponentAttributesmap.get("aggregationBandwidth")!=null && !scComponentAttributesmap.get("aggregationBandwidth").isEmpty()){
					saBean.setPortBandwidth(scComponentAttributesmap.get("aggregationBandwidth")+" Mbps");
				}else{
					saBean.setPortBandwidth("");
				}
				saBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress", ""));
				String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
				saBean.setBillStartDate(billStartDate);
				saBean.setDeemedAcceptanceApplicable(
						scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
				saBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", null));
				String commissioningDate = formatDate(scComponentAttributesmap.getOrDefault("commissioningDate", ""));
				saBean.setCommissioningDate(commissioningDate);
			}

			String html = getHtml(saBean);

			// LOGGER.info(html);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "sc_handovernote_" + scServiceDetail.getUuid() + ".pdf";

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
		}catch (Exception e) {
			LOGGER.error("Error in Processing CGW Service Acceptance {}",e);
		}
		return tempDownloadUrl;
	}


	@Transactional(readOnly = false)
	public String processWelcomeLetterPdf(ScServiceDetail scServiceDetail,String subject,String fromAdd,List<String> toAdd,List<String> ccAdd,String customerName,
					String serviceUuid,String orderType,String customerContractingEntity,String supplierContractingEntity,
			String productName, String customerOrderFormRef) throws TclCommonException {

		String tempDownloadUrl = null;
		try {

			Integer serviceId = scServiceDetail.getId();

			WelcomeLetterPdfBean wlBean = setWlBean(subject, fromAdd, toAdd, ccAdd, customerName, serviceUuid,
					orderType, customerContractingEntity, supplierContractingEntity, productName, customerOrderFormRef);

			String html = getHtmlWelcomeLetter(wlBean);

			// LOGGER.info(html);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "customer_welcome_" + serviceUuid + ".pdf";

			Attachment attachment = new Attachment();
			attachment.setName(fileName);
			attachment.setIsActive("Y");
			attachment.setCategory("Welcome-Letter");
			attachment.setType("Welcome-Letter");
			attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				LOGGER.info("SwiftApiEnabled for Welcome-Letter with serviceId {}", serviceUuid);
				InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				fileName = ".pdf";

				Set<ScOrderAttribute> scOrderAttributes = new HashSet<>();
				scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes();
				
				LOGGER.info("scOrderAttributes {}", scOrderAttributes);

				Map<String, String> scOrderAttributesmap = new HashMap<>();
				scOrderAttributesmap = scOrderAttributes.stream().collect(HashMap::new,
						(m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()), HashMap::putAll);

				if (scOrderAttributesmap.containsKey("CUSTOMER_CODE")
						&& scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE")) {
					LOGGER.info("Getting Stored Object...");
					StoredObject storedObject = fileStorageService.uploadObjectWithExten(fileName, inputStream,
							scOrderAttributesmap.get("CUSTOMER_CODE"), scOrderAttributesmap.get("CUSTOMER_LE_CODE"));

					LOGGER.info("Stored Object Welcome letter PDF is -> {}", storedObject.getName());

					String[] pathArray = storedObject.getPath().split("/");
					/*
					 * Map<String, String> cofMap = new HashMap<>();
					 * cofMap.put("FILENAME", storedObject.getName());
					 * cofMap.put("OBJECT_STORAGE_PATH", pathArray[1]); String
					 * tempUrl =
					 * fileStorageService.getTempDownloadUrl(storedObject.
					 * getName(), 60000, pathArray[1], false);
					 * cofMap.put("TEMP_URL", tempUrl);
					 */
					attachment.setName(storedObject.getName());
					attachment.setUriPathOrUrl(pathArray[1]);
					LOGGER.info("PDF Welcome-Letter Attachment {} to be saved at location {} for service id{}",
							attachment.getName(), pathArray[1], serviceUuid);
					Attachment savedAttachment = attachmentRepository.save(attachment);

					ScAttachment scAttachment = new ScAttachment();
					scAttachment.setAttachment(savedAttachment);
					scAttachment.setScServiceDetail(scServiceDetail);
					scAttachment.setIsActive("Y");
					scAttachment.setSiteType("A");
					scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
					scAttachmentRepository.save(scAttachment);
					LOGGER.info(
							"PDF Welcome-Letter Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
							scAttachment.getId(), serviceId, serviceUuid);
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
				LOGGER.info("PDF Welcome-Letter Attachment {} to be saved at location {} for service id{} ",
						attachment.getName(), attachment.getUriPathOrUrl(), serviceUuid);

				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(savedAttachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType("A");
				scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
				scAttachmentRepository.save(scAttachment);
				LOGGER.info(
						"PDF Welcome-Letter Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
						scAttachment.getId(), serviceId, serviceUuid);
				/*
				 * if (cofObjectMapper != null) {
				 * cofObjectMapper.put("FILE_SYSTEM_PATH", fileFullPath); }
				 */ }

		} catch (Exception e) {
			LOGGER.error("Error in Processing Welcome-Letter PDF {}", e);
		}
		return tempDownloadUrl;

	}

	private String getHtmlWelcomeLetter(WelcomeLetterPdfBean wlBean) {

		Map<String, Object> variable = objectMapper.convertValue(wlBean, Map.class);
		Context context = new Context();
		context.setVariables(variable);

		if (wlBean.getProductName().equalsIgnoreCase("NPL")) {
			return templateEngine.process("customer_welcome_letter_pdf", context); // same

		} else {
			return templateEngine.process("customer_welcome_letter_pdf", context);
		}

	}

	private WelcomeLetterPdfBean setWlBean(String subject,String fromAdd, List<String> toAdd,List<String> ccAdd,String customerName,
			String serviceUuid,String orderType,String customerContractingEntity,String supplierContractingEntity,
			String productName,String customerOrderFormRef)
			throws TclCommonException, IllegalArgumentException {

		WelcomeLetterPdfBean wlBean = new WelcomeLetterPdfBean();

		LOGGER.info(
				"For serviceId {} the values of subject: {} , fromAdd: {} , toAdd: {} , ccAdd: {} , customerName: {} , orderType: {} , customerContractingEntity: {} , supplierContractingEntity: {} , productName: {} , customerOrderFormRef: {}",
				serviceUuid, subject, fromAdd, toAdd, ccAdd, customerName, orderType,
				customerContractingEntity, supplierContractingEntity, productName, customerOrderFormRef);
		
		String toAddress = toAdd.stream() 
                .map(String::valueOf) 
                .collect(Collectors.joining("; ")); 
		String ccAddress = ccAdd.stream() 
                .map(String::valueOf) 
                .collect(Collectors.joining("; ")); 
		
		String customerNameCap = customerName.substring(0, 1).toUpperCase() + customerName.substring(1);
		LOGGER.info(
				"For setWlBean serviceId {} the values of fromAdd: {} , toAdd: {} , ccAdd: {} , customerName: {} ",
				serviceUuid,fromAdd,toAddress,ccAddress,customerNameCap);
		
		wlBean.setSubject(subject);
		wlBean.setFromAddress(fromAdd);
		wlBean.setToAddress(toAddress);
		wlBean.setCcAddress(ccAddress);
		wlBean.setCustomerContractingEntity(customerContractingEntity);
		wlBean.setCustomerName(customerNameCap);
		wlBean.setCustomerOrderFormRef(customerOrderFormRef);
		wlBean.setOrderType(orderType);
		wlBean.setProductName(productName);
		wlBean.setServiceId(serviceUuid);
		wlBean.setSupplierContractingEntity(supplierContractingEntity);
		return wlBean;
		
	}
	
	
	/**
	 * 
	 * processIZOPCServiceAcceptancePdf
	 * @param serviceId 
	 * @param serviceUuid 
	 * @param serviceId 
	 * 
	 * @return
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public String processIZOPCServiceAcceptancePdf(String serviceUuid, Integer serviceId, ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap) throws TclCommonException {
		LOGGER.info("processIZOPCServiceAcceptancePdf with service Id:{} ",serviceUuid);
		String tempDownloadUrl = null;
		try {
			
			ServiceAcceptancePdfBean saBean = setIzopcSaBean(scServiceDetail, scOrder, scOrderAttributesmap,
					scServiceAttributesmap, scComponentAttributesmap);

			String html = getHtml(saBean);

			//LOGGER.info(html);
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "sc_handovernote_" + scServiceDetail.getUuid() + ".pdf";

			Attachment attachment = new Attachment();
			attachment.setName(fileName);
			attachment.setIsActive("Y");
			attachment.setCategory("Handover-note");
			attachment.setType("Handover-note");
			attachment.setCreatedDate(Timestamp.valueOf(LocalDateTime.now()));
			attachment.setUpdatedDate(Timestamp.valueOf(LocalDateTime.now()));
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				LOGGER.info("SwiftApiEnabled for Izopc Handover Note with service Id:{} ",serviceUuid);
				InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
				fileName = ".pdf";

				if (scOrderAttributesmap.containsKey("CUSTOMER_CODE")
						&& scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE")) {
					LOGGER.info("Getting Stored Object...");
					StoredObject storedObject = fileStorageService.uploadObjectWithExten(fileName, inputStream,
							scOrderAttributesmap.get("CUSTOMER_CODE"), scOrderAttributesmap.get("CUSTOMER_LE_CODE"));

					LOGGER.info("Stored Object for Handover Note is -> {}", storedObject.getName());

					String[] pathArray = storedObject.getPath().split("/");
					attachment.setName(storedObject.getName());
					attachment.setUriPathOrUrl(pathArray[1]);
					LOGGER.info("PDF izopc service handover note Attachment {} to be saved at location {} for service Id{}",
							attachment.getName(), pathArray[1],serviceUuid);
					Attachment savedAttachment = attachmentRepository.save(attachment);

					ScAttachment scAttachment = new ScAttachment();
					scAttachment.setAttachment(savedAttachment);
					scAttachment.setScServiceDetail(scServiceDetail);
					scAttachment.setIsActive("Y");
					scAttachment.setSiteType("A");
					scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
					scAttachmentRepository.save(scAttachment);
					LOGGER.info("PDF service handover note Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
					scAttachment.getId(),serviceId,serviceUuid);

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
				LOGGER.info("PDF izopc service handover note Attachment {} to be saved at location {} for service id {}",
						attachment.getName(),attachment.getUriPathOrUrl(),serviceUuid);

				ScAttachment scAttachment = new ScAttachment();
				scAttachment.setAttachment(savedAttachment);
				scAttachment.setScServiceDetail(scServiceDetail);
				scAttachment.setIsActive("Y");
				scAttachment.setSiteType("A");
				scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
				scAttachmentRepository.save(scAttachment);
				LOGGER.info("PDF izopcservice handover note Attachment saved in scAttachment with id {} for service primary key {} and Id {}",
						scAttachment.getId(),serviceId,serviceUuid);
				}

	
			
		} catch (Exception e) {
			LOGGER.error("Error in Processing Izopc Service Handover {}",e);
		}
		return tempDownloadUrl;

	}
	
	private ServiceAcceptancePdfBean setIzopcSaBean(ScServiceDetail scServiceDetail, ScOrder scOrder,
			Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap,
			Map<String, String> scComponentAttributesmap) throws TclCommonException, IllegalArgumentException {
		LOGGER.error("setIzopcSaBean with Service Id:: {}",scServiceDetail.getId());
		ServiceAcceptancePdfBean saBean = new ServiceAcceptancePdfBean();
		saBean.setOrderId(scServiceDetail.getScOrderUuid());
		saBean.setServiceId(scServiceDetail.getUuid());
		saBean.setOrderType(OrderCategoryMapping.getOrderType(scServiceDetail, scOrder));
		String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
		saBean.setOrderCategory(orderCategory!=null?orderCategory:"");
		if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
			saBean.setCustomerContractingEntity(scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
		saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "") + ";"
				+ scOrderAttributesmap.getOrDefault("GST_Address", ""));
		saBean.setPrimarySecondary(scServiceDetail.getPrimarySecondary());
		saBean.setProductName("GVPN - IZO Private Connect");
		saBean.setPortBandwidth(scComponentAttributesmap.get("portBandwidth")!=null?scComponentAttributesmap.get("portBandwidth")+" Mbps":"");
		saBean.setCloudProvider(scServiceAttributesmap.getOrDefault("Cloud Provider", ""));
		
		saBean.setSiteAddress(scComponentAttributesmap.getOrDefault("siteAddress", ""));
		
		String billStartDate = formatDate(scComponentAttributesmap.getOrDefault("billStartDate", ""));
		saBean.setBillStartDate(billStartDate);
		saBean.setBillFreePeriod(scComponentAttributesmap.getOrDefault("billFreePeriod", ""));
		String commissioningDate = formatDate(scComponentAttributesmap.getOrDefault("commissioningDate", ""));
		saBean.setCommissioningDate(commissioningDate);
		saBean.setDeemedAcceptanceApplicable(scComponentAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
		
		String response = (String) mqUtils.sendAndReceive(izopcServicehandovernote_queue, scServiceDetail.getUuid());
		if (response != null) {
			LOGGER.error("setIzopcSaBean with Service Id:: {}",scServiceDetail.getId());
			Map<String, Object> handoverAttr = (Map<String, Object>) Utils.convertJsonToObject(response, Map.class);
			if (handoverAttr != null) {
				saBean.setRoutingProtocol(
						handoverAttr.get("routingProtocol") != null ? (String) handoverAttr.get("routingProtocol")
								: "");
				saBean.setWanv4Address(
						handoverAttr.get("wanv4Address") != null ? (String) handoverAttr.get("wanv4Address") : "");
				saBean.setLanv4Address(
						handoverAttr.get("lanv4Address") != null ? (String) handoverAttr.get("lanv4Address") : "");
				saBean.setAsNumber(scComponentAttributesmap.getOrDefault("bgpAsNumber", ""));
			}
		}
		
		return saBean;
	}
}
