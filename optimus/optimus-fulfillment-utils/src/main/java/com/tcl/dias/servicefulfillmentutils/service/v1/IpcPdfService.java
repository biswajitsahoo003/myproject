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
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScAttachment;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScProductDetailAttributes;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScProductDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AttributeDetail;
import com.tcl.dias.servicefulfillmentutils.beans.ComponentDetail;
import com.tcl.dias.servicefulfillmentutils.beans.ComponentPdfBean;
import com.tcl.dias.servicefulfillmentutils.beans.DeletedLineItemDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.IpcServiceAcceptancePdfBean;
import com.tcl.dias.servicefulfillmentutils.beans.ProductSolutionBean;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionDetail;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionPdfBean;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;


@Service
public class IpcPdfService {

    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;

    @Autowired
    CommonFulfillmentUtils commonFulfillmentUtils;

    @Autowired
    SpringTemplateEngine templateEngine;


    @Autowired
    ObjectMapper objectMapper;

    @Value("${swift.api.enabled}")
    String swiftApiEnabled;

    @Value("${file.upload-dir}")
    String uploadPath;

	@Value("${rabbitmq.ipc.si.solutions.queue}")
	String ipcSiSolutionsQueue;
	
    @Autowired
    FileStorageService fileStorageService;

    @Autowired
    AttachmentRepository attachmentRepository;


    @Autowired
    ScAttachmentRepository scAttachmentRepository;
    
    @Autowired
    ScServiceAttributeRepository attributeRepository;
    
    @Autowired
	ScProductDetailAttributeRepository scProductDetailAttributeRepository;
   
    @Autowired
	ScProductDetailRepository scProductDetailRepository;
    
    @Autowired
	MQUtils mqUtils;
    
    private static final List<String> COMPUTES = Arrays.asList("L", "C", "G", "X", "M", "B", "H");

	private static final List<String> VARIANTS = Arrays.asList("Nickel", "Bronze", "Silver", "Cobalt", "Gold",
			"Platinum", "Titanium");
	
	private static final Set<String> ATTR_TO_DISP =  Sets.newHashSet("vCPU", "vRAM","Storage Type","Storage Value","IOPS Value");
	
	private static final Set<String> STORAGE_ATTR =  Sets.newHashSet("Storage", "Storage Value", "frontVolumeSize", "minimumCommitment");
	
	private static final Set<String> COMP_TO_SKIP =  Sets.newHashSet("IPC Common", "Storage Partition");
	
	public static final List<String> SOLUTIONS_NAMES = new ArrayList<>();

	static {
		COMPUTES.forEach(compute -> VARIANTS.forEach(variant -> SOLUTIONS_NAMES.add(compute + "." + variant + CommonConstants.SPACE + IpcConstants.VM_CHARGES)));
		SOLUTIONS_NAMES.add(IpcConstants.CARBON_VM + CommonConstants.SPACE + IpcConstants.VM_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.DATA_TRANSFER_COMM);
		SOLUTIONS_NAMES.add(IpcConstants.FIXED_BW);
		SOLUTIONS_NAMES.add(IpcConstants.VDOM_SMALL);
		SOLUTIONS_NAMES.add(IpcConstants.ADDITIONAL_IP_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.VPN_CLIENT_SITE);
		SOLUTIONS_NAMES.add(IpcConstants.VPN_SITE_SITE);
		SOLUTIONS_NAMES.add(IpcConstants.BACKUP_FE_VOL);
		SOLUTIONS_NAMES.add(IpcConstants.DATABASE_LICENSE_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.DR_LICENSE_CHARGES);
		SOLUTIONS_NAMES.add(IpcConstants.HYBRID_CONNECTION_CHARGES);
	}

    private static final Logger LOGGER = LoggerFactory.getLogger(IpcPdfService.class);
    
    private String cpu;
	private String ram;
	private String storage;
	private String hypervisor;
	private String osType;
	private String osVersion;
	private String accessOption;
	private String accessOptionBw;
	private String accessOptionMinComm;
	private String managed;
	private String storageType;
	private String storageValue;
	private String iopsValue;
	
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
			// Map<String, String> scServiceAttributesmap = new HashMap<>();

			/*
			 * scServiceAttributesmap = scServiceAttributes.stream().collect(HashMap::new,
			 * (m, v) -> m.put(v.getAttributeName(), v.getAttributeValue()),
			 * HashMap::putAll);
			 */

         //   Map<String, String> scComponentAttributesmap= commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
            List<ScServiceAttribute> serviceAttributes = attributeRepository
    				.findByScServiceDetail_idAndAttributeNameIn(Integer.valueOf(scServiceDetail.getId()),
    						Arrays.asList("commDate","billStartDate","deemedAcceptanceApplicable","contractGstAddress"));
            Map<String, String> scServiceAttributesmap =commonFulfillmentUtils.getServiceAttributesAttributes(serviceAttributes);
            Set<ScProductDetail> scProductDetails=new HashSet<>();
            scProductDetails=scServiceDetail.getScProductDetail();
            
            IpcServiceAcceptancePdfBean saBean = setSaBean(scServiceDetail, scOrder, scOrderAttributesmap,
                    scServiceAttributesmap, scProductDetails);

            String html = getHtml(saBean);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PDFGenerator.createPdf(html, bos);
            byte[] outArray = bos.toByteArray();
            String fileName = "ipc_sc_handovernote_" + scServiceDetail.getUuid() + ".pdf";

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




                if (scOrderAttributesmap.containsKey("CUSTOMER_CODE") &&scOrderAttributesmap.containsKey("CUSTOMER_LE_CODE") ) {
                    LOGGER.info("Getting Stored Object...");
                    StoredObject storedObject = fileStorageService.uploadObject(fileName, inputStream,
                            scOrderAttributesmap.get("CUSTOMER_CODE"),scOrderAttributesmap.get("CUSTOMER_LE_CODE"));

                    LOGGER.info("Stored Object is -> {}",storedObject.getName());

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
                    LOGGER.info("PDF service handover note Attachment {} to be saved at location {}",attachment.getName(),pathArray[1]);
                    Attachment savedAttachment = attachmentRepository.save(attachment);

                    ScAttachment scAttachment = new ScAttachment();
                    scAttachment.setAttachment(savedAttachment);
                    scAttachment.setScServiceDetail(scServiceDetail);
                    scAttachment.setIsActive("Y");
                    scAttachment.setSiteType("A");
                    scAttachment.setOrderId(scServiceDetail.getScOrder().getId());
                    scAttachmentRepository.save(scAttachment);
                    LOGGER.info("Attachment saved in scAttachment with id "+scAttachment.getId());

                }
            }else {


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

    private String getHtml(IpcServiceAcceptancePdfBean saBean) {
        Map<String, Object> variable = objectMapper.convertValue(saBean, Map.class);
        Context context = new Context();
        context.setVariables(variable);
        return templateEngine.process("ipc_service_acceptance_pdf", context);
    }

    private IpcServiceAcceptancePdfBean setSaBean(ScServiceDetail scServiceDetail, ScOrder scOrder, Map<String, String> scOrderAttributesmap, Map<String, String> scServiceAttributesmap, Set<ScProductDetail> scProductDetails) throws TclCommonException {
        IpcServiceAcceptancePdfBean saBean=new IpcServiceAcceptancePdfBean();

        //OrderDetail
        saBean.setOrderId(scServiceDetail.getScOrderUuid());
        saBean.setServiceId(scServiceDetail.getUuid());
        saBean.setOrderType(scOrder.getOrderType());
        saBean.setOrderCategory(scOrder.getOrderCategory());
        if (scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst() != null)
            saBean.setCustomerContractingEntity(scServiceDetail.getScOrder().getScContractInfos1().stream().findFirst().get().getErfCustLeName());
        //Billing Details
        String billStartDate = formatDate(formatWithTimeStamp().format(scServiceDetail.getServiceCommissionedDate()));
        saBean.setBillStartDate(billStartDate);
        String commissioningDate = formatDate(formatWithTimeStamp().format(scServiceDetail.getServiceCommissionedDate()));
        saBean.setCommissioningDate(commissioningDate);
        saBean.setDeemedAcceptanceApplicable(scServiceAttributesmap.getOrDefault("deemedAcceptanceApplicable", ""));
        saBean.setCustomerGstNumberAddress(scOrderAttributesmap.getOrDefault("LeStateGstNumber", "")) ;
               //+ ";" + scServiceAttributesmap.getOrDefault("contractGstAddress", ""));
        Optional<ScProductDetail> productDetailattr = scProductDetailRepository
				.findByScServiceDetailIdAndSolutionName(scServiceDetail.getId(), "IPC addon");
        managed = " Unmanaged VM";
        if (productDetailattr.isPresent()) {
			List<ScProductDetailAttribute> managedDetailAttributes = scProductDetailAttributeRepository
					.findByScProductDetail_idAndCategory(productDetailattr.get().getId(), "managed");
			managedDetailAttributes.forEach(detailAttribute -> {
				if ("managed".equals(detailAttribute.getAttributeName()))
					if ("true".equalsIgnoreCase(detailAttribute.getAttributeValue()))
						managed = " Managed VM";
			});
		}
		String Location = scServiceDetail.getSourceCity() + CommonConstants.COMMA + CommonConstants.SPACE + scServiceDetail.getSourceState() + CommonConstants.COMMA + CommonConstants.SPACE + scServiceDetail.getSourceCountry();
		if(!CommonConstants.INDIA_SITES.equalsIgnoreCase(scServiceDetail.getSourceCountry())) {
			saBean.setIsInternational(Boolean.TRUE);
		}
		saBean.setHostingLocation(Location);
		saBean = iterateAndProcessSolutions(saBean, scProductDetails);

        addPreviousOrderSolutions(saBean);
        
		return saBean;
	}
    private IpcServiceAcceptancePdfBean iterateAndProcessSolutions(IpcServiceAcceptancePdfBean saBean, Set<ScProductDetail> scProductDetails) {
    	 for (ScProductDetail productDetail:scProductDetails) {
 	   		SolutionPdfBean solutionPdfBean = null;
 			Set<ScProductDetailAttributes> scProductDetailAttributes=new HashSet<>();
             scProductDetailAttributes=productDetail.getScProductDetailAttributes();
 			if ("ACCESS".equalsIgnoreCase(productDetail.getType())) {
 				solutionPdfBean = new SolutionPdfBean();
 				solutionPdfBean.setName("Data transfer minimum commitment");
 				ComponentPdfBean componentPdfBean = new ComponentPdfBean();
 				String accessType = "";
 				for (ScProductDetailAttributes productDetailAttribute : scProductDetailAttributes) {
 					if (productDetailAttribute.getAttributeName().equalsIgnoreCase("minimumCommitment")) {
 						accessOptionMinComm = productDetailAttribute.getAttributeValue() + " GB";

 					}
 					if (productDetailAttribute.getAttributeName().equalsIgnoreCase("accessOption")) {
 						accessOption = productDetailAttribute.getAttributeValue();
 					}
 					if (productDetailAttribute.getAttributeName().equalsIgnoreCase("portBandwidth")) {
 						accessOptionBw = productDetailAttribute.getAttributeValue();
 					}

 				}
 				accessType = "Data Transfer".equals(accessOption) ? "Minimum Commitment: " + accessOptionMinComm
 						: "Port Bandwidth: " + accessOptionBw;
 				componentPdfBean.setName("Access Type");
 				componentPdfBean.setAttributes(accessType + ", Access Option: " + accessOption
 						+ ", 1 Client to Site VPN is bundled, 2 Public IPs is bundled.");
 				solutionPdfBean.getComponents().add(componentPdfBean);
 				if (solutionPdfBean.getPricingModel() == null) {
 					solutionPdfBean.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
 				}
 				saBean.getSolutions().add(solutionPdfBean);
 			} else if ("ADDON".equalsIgnoreCase(productDetail.getType())) {
 				ComponentPdfBean componentPdfBean = null;
 				Map<String, String> addonsMap = new HashMap<String, String>();
 				Map<String, String> licenseMap = new HashMap<String, String>();
 				Map<String, String> attributesMap = getProductAttributes(scProductDetailAttributes);
 				Map<String, String> hybridMap = new HashMap<String, String>();
 				
 				if (attributesMap.get("clientToSite") != null && Integer.parseInt(attributesMap.get("clientToSite")) > 0) {
 					solutionPdfBean = new SolutionPdfBean();
 					componentPdfBean = new ComponentPdfBean();
 					solutionPdfBean.setName(IpcConstants.VPN_CLIENT_SITE);
 					solutionPdfBean.setQuantity(Integer.parseInt(attributesMap.getOrDefault("clientToSite", "")));
 					componentPdfBean.setName(IpcConstants.IPC_ADDON);
 					componentPdfBean.setAttributes("Type: Client to Site");
 					if (solutionPdfBean.getPricingModel() == null) {
 						solutionPdfBean.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
 					}
 					solutionPdfBean.getComponents().add(componentPdfBean);
 					saBean.getSolutions().add(solutionPdfBean);
 				}
 				if (attributesMap.get("siteToSite") != null && Integer.parseInt(attributesMap.get("siteToSite")) > 0) {
 					solutionPdfBean = new SolutionPdfBean();
 					componentPdfBean = new ComponentPdfBean();
 					solutionPdfBean.setName(IpcConstants.VPN_SITE_SITE);
 					solutionPdfBean.setQuantity(Integer.parseInt(attributesMap.getOrDefault("siteToSite", "")));
 					componentPdfBean.setName(IpcConstants.IPC_ADDON);
 					componentPdfBean.setAttributes("Type: Site to Site");
 					if (solutionPdfBean.getPricingModel() == null) {
 						solutionPdfBean.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
 					}
 					solutionPdfBean.getComponents().add(componentPdfBean);
 					saBean.getSolutions().add(solutionPdfBean);
 				}
 				if (attributesMap.get("frontVolumeSize") != null) {
 					solutionPdfBean = new SolutionPdfBean();
 					componentPdfBean = new ComponentPdfBean();
 					solutionPdfBean.setName(IpcConstants.BACKUP_FE_VOL);
 					componentPdfBean.setName(IpcConstants.IPC_ADDON);
 					componentPdfBean.setAttributes(
 							"Front Volume Size: " + attributesMap.getOrDefault("frontVolumeSize", "") + " GB"
 									+ ", Target Data Storage: " + attributesMap.getOrDefault("targetDataStorage", ""));
 					if (solutionPdfBean.getPricingModel() == null) {
 						solutionPdfBean.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
 					}
 					solutionPdfBean.getComponents().add(componentPdfBean);
 					saBean.getSolutions().add(solutionPdfBean);
 				}
 				if (attributesMap.get("ipQuantity") != null) {
 					solutionPdfBean = new SolutionPdfBean();
 					componentPdfBean = new ComponentPdfBean();
 					solutionPdfBean.setName(IpcConstants.ADDITIONAL_IP_CHARGES);
 					solutionPdfBean.setQuantity(Integer.parseInt(attributesMap.getOrDefault("ipQuantity", "")));
 					componentPdfBean.setName(IpcConstants.IPC_ADDON);
 					componentPdfBean.setAttributes(IpcConstants.ADDITIONAL_IP_DESC);
 					if (solutionPdfBean.getPricingModel() == null) {
 						solutionPdfBean.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
 					}
 					solutionPdfBean.getComponents().add(componentPdfBean);
 					saBean.getSolutions().add(solutionPdfBean);
 				}
 				for (ScProductDetailAttributes productDetailAttribute : scProductDetailAttributes) {

 					if (IpcConstants.VDOM.equalsIgnoreCase(productDetailAttribute.getCategory())) {
 						if (StringUtils.isNotBlank(productDetailAttribute.getAttributeValue())) {
 							if (addonsMap.containsKey(productDetailAttribute.getCategory())) {
 								StringBuilder attributes = new StringBuilder(
 										addonsMap.get(productDetailAttribute.getCategory()));
 								attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
 										.append(productDetailAttribute.getAttributeName()).append(CommonConstants.COLON)
 										.append(CommonConstants.SPACE)
 										.append(productDetailAttribute.getAttributeValue());

 								addonsMap.put(productDetailAttribute.getCategory(), attributes.toString());
 							} else {
 								String value = productDetailAttribute.getAttributeName() + CommonConstants.COLON
 										+ CommonConstants.SPACE + productDetailAttribute.getAttributeValue();
 								addonsMap.put(productDetailAttribute.getCategory(), value);
 							}
 						}
 					}else if(productDetailAttribute.getCategory().startsWith(IpcConstants.MYSQL)
 							|| productDetailAttribute.getCategory().startsWith(IpcConstants.MS_SQL_SERVER)
							|| productDetailAttribute.getCategory().startsWith(IpcConstants.POSTGRESQL)
							|| productDetailAttribute.getCategory().startsWith(IpcConstants.DR_ZERTO)
							|| productDetailAttribute.getCategory().startsWith(IpcConstants.DR_DOUBLE_TAKE)) {
						if (StringUtils.isNotBlank(productDetailAttribute.getAttributeValue()) && IpcConstants.QUANTITY
								.equalsIgnoreCase(productDetailAttribute.getAttributeName())) {
							addonsMap.put(productDetailAttribute.getCategory(),
									productDetailAttribute.getAttributeValue());
						} else if (StringUtils.isNotBlank(productDetailAttribute.getAttributeValue())
								&& (IpcConstants.ATTRIBUTE_NAME_DB_VERSION
										.equalsIgnoreCase(productDetailAttribute.getAttributeName())
										|| IpcConstants.ATTRIBUTE_NAME_MANAGED
												.equalsIgnoreCase(productDetailAttribute.getAttributeName()))) {
							licenseMap.put(productDetailAttribute.getAttributeName(), productDetailAttribute.getAttributeValue());
						} else if (StringUtils.isNotBlank(productDetailAttribute.getAttributeValue())
								&& (IpcConstants.ATTRIBUTE_NAME_VARIANT.equalsIgnoreCase(productDetailAttribute.getAttributeName()))) {
							licenseMap.put(productDetailAttribute.getAttributeName(), productDetailAttribute.getAttributeValue());
						}
					} else if (productDetailAttribute.getCategory().startsWith(IpcConstants.HYBRID_CONNECTION)) {
                        addonsMap.put(productDetailAttribute.getCategory(), IpcConstants.HYBRID_CONNECTION_CHARGES);
                        if(IpcConstants.CONNECTIVITY_TYPE.equals(productDetailAttribute.getAttributeName())) {
                            hybridMap.put(IpcConstants.CONNECTIVE_TYPE, productDetailAttribute.getAttributeValue());
                        } else if(IpcConstants.CABLE_TYPE.equals(productDetailAttribute.getAttributeName())) {
                            hybridMap.put(IpcConstants.CABLE_TYPE, productDetailAttribute.getAttributeValue());
                        } else if (IpcConstants.L2_THROUGHPUT.equals(productDetailAttribute.getAttributeName())) {
                            hybridMap.put(IpcConstants.L2_THROUGHPUT, productDetailAttribute.getAttributeValue());
                        } else if (IpcConstants.SHARED_SWITCH_PORT.equals(productDetailAttribute.getAttributeName())) {
                            hybridMap.put(IpcConstants.SHARED_SWITCH_PORT, productDetailAttribute.getAttributeValue());
                        } else if (IpcConstants.REDUNDANCY.equals(productDetailAttribute.getAttributeName())) {
                            hybridMap.put(IpcConstants.REDUNDANCY, productDetailAttribute.getAttributeValue());
                        }
                    }
 				}
 				for (Map.Entry<String, String> addonAttributeMap : addonsMap.entrySet()) {
 					componentPdfBean = new ComponentPdfBean();
 					solutionPdfBean = new SolutionPdfBean();
 					componentPdfBean.setName(IpcConstants.IPC_ADDON);
 					if (addonAttributeMap.getKey().equalsIgnoreCase(IpcConstants.VDOM)) {
 						solutionPdfBean.setName(IpcConstants.VDOM_SMALL);
 						componentPdfBean.setAttributes("Type: Shared");
 					} else if (addonAttributeMap.getKey().startsWith(IpcConstants.MYSQL)
 							|| addonAttributeMap.getKey().startsWith(IpcConstants.MS_SQL_SERVER)
							|| addonAttributeMap.getKey().startsWith(IpcConstants.POSTGRESQL)) {
						solutionPdfBean.setName(IpcConstants.DATABASE_LICENSE_CHARGES);
						solutionPdfBean.setQuantity(Integer.parseInt(addonAttributeMap.getValue()));
						StringBuilder attributes = new StringBuilder();
						attributes.append("Type: ").append(addonAttributeMap.getKey().split("\\(")[0].trim());
						if (!licenseMap.isEmpty() && attributes.length() != 0) {
							if (licenseMap.containsKey(IpcConstants.ATTRIBUTE_NAME_DB_VERSION)) {
								attributes.append(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE)
										.append(IpcConstants.ATTRIBUTE_NAME_DB_VERSION)
										.append(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE)
										.append(licenseMap.get(IpcConstants.ATTRIBUTE_NAME_DB_VERSION));
							}
							if (licenseMap.containsKey(IpcConstants.ATTRIBUTE_NAME_MANAGED)) {
								String dbManaged = ("yes"
										.equalsIgnoreCase(licenseMap.get(IpcConstants.ATTRIBUTE_NAME_MANAGED)))
												? IpcConstants.MANAGED_DB
												: IpcConstants.UN_MANAGED_DB;
								attributes.append(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE).append(dbManaged);
							}
						}
						attributes.append(", license as per core licensing policy");
						componentPdfBean.setAttributes(attributes.toString());
					} else if (addonAttributeMap.getKey().startsWith(IpcConstants.DR_ZERTO)
							|| addonAttributeMap.getKey().startsWith(IpcConstants.DR_DOUBLE_TAKE)) {
						solutionPdfBean.setName(IpcConstants.DR_LICENSE_CHARGES);
						solutionPdfBean.setQuantity(Integer.parseInt(addonAttributeMap.getValue()));
						StringBuilder attributes = new StringBuilder();
						attributes.append("Type: ").append(addonAttributeMap.getKey().split("\\(")[0].trim());
						if (!licenseMap.isEmpty() && attributes.length() != 0) {
							if (licenseMap.containsKey(IpcConstants.ATTRIBUTE_NAME_VARIANT)) {
								attributes.append(IpcConstants.SPECIAL_CHARACTER_COMMA_WITH_SPACE)
										.append(IpcConstants.ATTRIBUTE_NAME_VARIANT)
										.append(IpcConstants.SPECIAL_CHARACTER_COLON_WITH_SPACE)
										.append(licenseMap.get(IpcConstants.ATTRIBUTE_NAME_VARIANT));
							}
						}
						attributes.append(", for protected VM");
						componentPdfBean.setAttributes(attributes.toString());
					}
					else if (addonAttributeMap.getKey().startsWith(IpcConstants.HYBRID_CONNECTION)) {
						solutionPdfBean.setName(IpcConstants.HYBRID_CONNECTION_CHARGES);
						StringBuilder attributes = new StringBuilder();
						if (attributes.length() == 0) {
							attributes.append(IpcConstants.CONNECTIVE_TYPE).append(CommonConstants.COLON).append(CommonConstants.SPACE)
							.append(CommonConstants.SPACE)
							.append(hybridMap.get(IpcConstants.CONNECTIVE_TYPE));
						}
						if (hybridMap.containsKey(IpcConstants.CABLE_TYPE)) {
							attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
                            .append(IpcConstants.CABLE_TYPE).append(CommonConstants.COLON)
                            .append(CommonConstants.SPACE)
                            .append(hybridMap.get(IpcConstants.CABLE_TYPE));
						} 
						if (hybridMap.containsKey(IpcConstants.L2_THROUGHPUT)) {
							attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
                            .append(IpcConstants.L2_THROUGHPUT).append(CommonConstants.COLON)
                            .append(CommonConstants.SPACE)
                            .append(hybridMap.get(IpcConstants.L2_THROUGHPUT));
						}
						if (hybridMap.containsKey(IpcConstants.SHARED_SWITCH_PORT)) {
							attributes.append(" with ").append(IpcConstants.SHARED_SWITCH_PORT).append("s")
                            .append(CommonConstants.COLON).append(CommonConstants.SPACE)
                            .append(hybridMap.get(IpcConstants.SHARED_SWITCH_PORT));
						}
						if (hybridMap.containsKey(IpcConstants.REDUNDANCY)) {
							if (hybridMap.get(IpcConstants.REDUNDANCY).equalsIgnoreCase(CommonConstants.YES)) {
								attributes.append(" with ").append(IpcConstants.REDUNDANCY.toLowerCase());
							}
							else {
								attributes.append("without").append(IpcConstants.REDUNDANCY.toLowerCase());
							}
						}
						componentPdfBean.setAttributes(attributes.toString());
					}
					else {
 						solutionPdfBean.setName(addonAttributeMap.getKey());
 						componentPdfBean.setAttributes(addonAttributeMap.getValue());
 					}
 					if (solutionPdfBean.getPricingModel() == null) {
 						solutionPdfBean.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
 					}
 					solutionPdfBean.getComponents().add(componentPdfBean);
 					saBean.getSolutions().add(solutionPdfBean);
 				}
 			} else {
 				List<ComponentPdfBean> vmComponents = new ArrayList<>();
 				solutionPdfBean = new SolutionPdfBean();
 				solutionPdfBean.setName((productDetail.getSolutionName().startsWith(IpcConstants.CARBON_VM)
 						? IpcConstants.CARBON_VM : productDetail.getSolutionName()) + CommonConstants.SPACE + IpcConstants.VM_CHARGES);
 				ComponentPdfBean componentIaasPdfBean = new ComponentPdfBean();
 				ComponentPdfBean componentOsPdfBean = new ComponentPdfBean();
 				ComponentPdfBean componentAdditionalStoragePdfBean = new ComponentPdfBean();
 				List<ScProductDetailAttribute> flavourDetailAttributes = scProductDetailAttributeRepository
 						.findByScProductDetail_idAndCategory(productDetail.getId(), "Flavor");
 				flavourDetailAttributes.forEach(detailAttribute -> {
 					if ("vCPU".equals(detailAttribute.getAttributeName()))
 						cpu = detailAttribute.getAttributeName() + ": " + detailAttribute.getAttributeValue() + ", ";
 					if ("vRAM".equals(detailAttribute.getAttributeName()))
 						ram = detailAttribute.getAttributeName() + ": " + detailAttribute.getAttributeValue() + " GB" + ", ";
 					if ("Storage".equals(detailAttribute.getAttributeName()))
 						storage = detailAttribute.getAttributeName() + ": " + detailAttribute.getAttributeValue()
 								+ " GB" + ", ";
 					if ("Hypervisor".equals(detailAttribute.getAttributeName()))
 						hypervisor = detailAttribute.getAttributeName() + ": " + detailAttribute.getAttributeValue()
 								+ ",";
 				});
 				List<ScProductDetailAttribute> detailAttributes = scProductDetailAttributeRepository
 						.findByScProductDetail_idAndCategory(productDetail.getId(), "OS");
 				detailAttributes.forEach(detailAttribute -> {
 					if ("Type".equals(detailAttribute.getAttributeName()))
 						osType = detailAttribute.getAttributeName() + ": " + detailAttribute.getAttributeValue() + ", ";
 					if ("Version".equals(detailAttribute.getAttributeName()))
 						osVersion = detailAttribute.getAttributeName() + ": " + detailAttribute.getAttributeValue();
 				});
 				componentIaasPdfBean.setName("IaaS");
 				componentOsPdfBean.setName("OS");
 				componentIaasPdfBean.setAttributes(cpu + ram + storage + hypervisor + managed);
 				componentOsPdfBean.setAttributes(osType + osVersion);
 				vmComponents.add(componentIaasPdfBean);
 				vmComponents.add(componentOsPdfBean);
 				List<ScProductDetailAttribute> additionalStorageAttributes = scProductDetailAttributeRepository
 						.findByScProductDetail_idAndCategory(productDetail.getId(), IpcConstants.ADDITIONAL_STORAGE);
 				if (CollectionUtils.isNotEmpty(additionalStorageAttributes)) {
 					solutionPdfBean.setHasAdditionalStorage(Boolean.TRUE);
 					additionalStorageAttributes.forEach(detailAttribute -> {
 						if ("Storage Type".equals(detailAttribute.getAttributeName()))
 							storageType = detailAttribute.getAttributeName() + ": "
 									+ detailAttribute.getAttributeValue() + ", ";
 						if ("Storage Value".equals(detailAttribute.getAttributeName()))
 							storageValue = detailAttribute.getAttributeName() + ": "
 									+ detailAttribute.getAttributeValue() + " GB, ";
 						if ("IOPS Value".equals(detailAttribute.getAttributeName()))
 							iopsValue = detailAttribute.getAttributeName() + ": " + detailAttribute.getAttributeValue();
 					});
 					componentAdditionalStoragePdfBean.setName(IpcConstants.ADDITIONAL_STORAGE);
 					componentAdditionalStoragePdfBean.setAttributes(storageType + storageValue + iopsValue);
 					vmComponents.add(componentAdditionalStoragePdfBean);
 				}
 				solutionPdfBean.setComponents(vmComponents);
 				if (solutionPdfBean.getPricingModel() == null) {
 					solutionPdfBean.setPricingModel(IpcConstants.PRICING_MODEL_RESERVED);
 				}
 				solutionPdfBean.setCloudCode(productDetail.getCloudCode());
 				solutionPdfBean.setParentCloudCode(productDetail.getParentCloudCode());
 				saBean.getSolutions().add(solutionPdfBean);

 			}
 		}
    	 return saBean;
	}

	private void addPreviousOrderSolutions(IpcServiceAcceptancePdfBean saBean) throws TclCommonException {
    	String ipcSiSolutionsQueueResponse = (String) mqUtils.sendAndReceive(ipcSiSolutionsQueue, saBean.getServiceId());
		LOGGER.info("IPC SI Solutions Queue Response in HandOver Note :: {}" , ipcSiSolutionsQueueResponse);
		
		List<ComponentPdfBean> vmComponents = new ArrayList<>();
		ProductSolutionBean[] productSolutions = null;
		if(ipcSiSolutionsQueueResponse != null && !ipcSiSolutionsQueueResponse.isEmpty()) {
			productSolutions = Utils.convertJsonToObject(ipcSiSolutionsQueueResponse, ProductSolutionBean[].class);
		}
		if(productSolutions != null) {
			List<ProductSolutionBean> sortedProductSolutions = Arrays.asList(productSolutions);
			sortedProductSolutions.forEach(productSolution -> {
				saBean.getSolutions().addAll(constructSolutions(productSolution, saBean, vmComponents, null, true));
			});
		}
		
		for (ComponentPdfBean componentPdfBean : vmComponents) {
			String attributes = componentPdfBean.getAttributes();
			String managedAttribute = "Unmanaged VM";
			if(saBean.getIsVMsManaged()) {
				managedAttribute = "Managed VM";
			}
			componentPdfBean.setAttributes(attributes + CommonConstants.COMMA + CommonConstants.SPACE + managedAttribute);
		}
		LOGGER.info("saBean.getSolutions() {}" , saBean.getSolutions().toString());
		identifyActionTypeAndQuantity(saBean);
	}

	private Set<SolutionPdfBean> constructSolutions(ProductSolutionBean productSolution,
			IpcServiceAcceptancePdfBean saBean, List<ComponentPdfBean> vmComponents, String quoteDetail, boolean isFromInventory) {

		Set<SolutionPdfBean> solutions = new LinkedHashSet<>();
		
		for (SolutionDetail solutionDetail : productSolution.getCloudSolutions()) {
			if ("IPC addon".equalsIgnoreCase(productSolution.getOfferingName())) {
				for (ComponentDetail component : solutionDetail.getComponents()) {
					if ("VPN Connection".equalsIgnoreCase(component.getName())) {
						SolutionPdfBean c2sSolutionPdfBean = new SolutionPdfBean();
						c2sSolutionPdfBean.setName(IpcConstants.VPN_CLIENT_SITE);
						ComponentPdfBean c2sComponentPdfBean = new ComponentPdfBean();
						c2sComponentPdfBean.setName("IPC Add-on");
						c2sComponentPdfBean.setAttributes("Type: Client to Site");
						c2sSolutionPdfBean.getComponents().add(c2sComponentPdfBean);
						c2sSolutionPdfBean.setFromServiceInventory(isFromInventory);
						solutions.add(c2sSolutionPdfBean);
						
						SolutionPdfBean s2sSolutionPdfBean = new SolutionPdfBean();
						s2sSolutionPdfBean.setName(IpcConstants.VPN_SITE_SITE);
						ComponentPdfBean s2sComponentPdfBean = new ComponentPdfBean();
						s2sComponentPdfBean.setName("IPC Add-on");
						s2sComponentPdfBean.setAttributes("Type: Site to Site");
						s2sSolutionPdfBean.getComponents().add(s2sComponentPdfBean);
						s2sSolutionPdfBean.setFromServiceInventory(isFromInventory);
						solutions.add(s2sSolutionPdfBean);
						
						for (AttributeDetail attribute : component.getAttributes()) {
							if ("".equals(attribute.getValue()) || attribute.getValue() == null) {
								continue;
							}
							
							if("clientToSite".equalsIgnoreCase(attribute.getName())) {
								c2sSolutionPdfBean.setQuantity(Integer.parseInt(attribute.getValue()));
							} else if("siteToSite".equalsIgnoreCase(attribute.getName())) {
								s2sSolutionPdfBean.setQuantity(Integer.parseInt(attribute.getValue()));
							}
						}
						
						if(c2sSolutionPdfBean.getPricingModel() == null) {
							c2sSolutionPdfBean.setPricingModel("Reserved");
						}
						if(s2sSolutionPdfBean.getPricingModel() == null) {
							s2sSolutionPdfBean.setPricingModel("Reserved");
						}
						
					} else if ("managed".equalsIgnoreCase(component.getName())) {
						for (AttributeDetail attribute : component.getAttributes()) {
							if ("".equals(attribute.getValue()) || attribute.getValue() == null) {
								continue;
							}
							if ("managed".equalsIgnoreCase(attribute.getName()) && "true".equalsIgnoreCase(attribute.getValue())) {
								saBean.setIsVMsManaged(true);
								continue;
							}
						}
					}  else {
						SolutionPdfBean solutionPdfBean = new SolutionPdfBean();
						
						ComponentPdfBean componentPdfBean = new ComponentPdfBean();
						componentPdfBean.setName("IPC Add-on");
						
						if ("VDOM".equalsIgnoreCase(component.getName())) {
							solutionPdfBean.setName(IpcConstants.VDOM_SMALL);
						} else if ("Additional Ip".equalsIgnoreCase(component.getName())) {
							solutionPdfBean.setName(IpcConstants.ADDITIONAL_IP_CHARGES);
						} else if ("Backup".equalsIgnoreCase(component.getName())) {
							solutionPdfBean.setName(IpcConstants.BACKUP_FE_VOL);
						} else if (component.getName().startsWith(IpcConstants.MYSQL)
								|| component.getName().startsWith(IpcConstants.MS_SQL_SERVER)
								|| component.getName().startsWith(IpcConstants.POSTGRESQL)) {
							solutionPdfBean.setName(IpcConstants.DATABASE_LICENSE_CHARGES);
						} else if (component.getName().startsWith(IpcConstants.DR_ZERTO)
								|| component.getName().startsWith(IpcConstants.DR_DOUBLE_TAKE)) {
							solutionPdfBean.setName(IpcConstants.DR_LICENSE_CHARGES);
						} else if (component.getName().startsWith(IpcConstants.HYBRID_CONNECTION)) {
							solutionPdfBean.setName(IpcConstants.HYBRID_CONNECTION_CHARGES);
						}
						else {
							solutionPdfBean.setName(component.getName());
						}
						solutionPdfBean.setFromServiceInventory(isFromInventory);
						solutions.add(solutionPdfBean);
						
						StringBuilder attributes = new StringBuilder();
						String dbLicenseManaged = ", Managed DB";
						Map<String,String> hybridMap = new HashMap<>();
						for (AttributeDetail attribute : component.getAttributes()) {
							if ("".equals(attribute.getValue()) || attribute.getValue() == null) {
								continue;
							} else if (IpcConstants.CLIENT_GROUP_NAME.equalsIgnoreCase(attribute.getName()) 
									|| IpcConstants.VDOM_DETAILS.equalsIgnoreCase(attribute.getName())) {
								continue;
							}
							
							String attributeName = ATTR_TO_DISP.contains(attribute.getName()) ? attribute.getName() : Utils.convertCamelCaseToTitleCase(attribute.getName());
							
							if(IpcConstants.ADDITIONAL_IP_CHARGES.equals(solutionPdfBean.getName()) 
									|| IpcConstants.VDOM_SMALL.equals(solutionPdfBean.getName())) {
								if(("Ip Quantity").equals(attributeName) || ("Quantity").equals(attributeName)) {
									solutionPdfBean.setQuantity(Integer.parseInt(attribute.getValue()));
								}
								if (attributes.length() == 0) {
									if(IpcConstants.ADDITIONAL_IP_CHARGES.equals(solutionPdfBean.getName())) {
										attributes.append(IpcConstants.ADDITIONAL_IP_DESC);
									} else if(IpcConstants.VDOM_SMALL.equals(solutionPdfBean.getName())){
										attributes.append("Type: Shared");
									}
								}
							} else if(IpcConstants.DATABASE_LICENSE_CHARGES.equals(solutionPdfBean.getName())
									|| IpcConstants.DR_LICENSE_CHARGES.equals(solutionPdfBean.getName())) {
								if(attributes.length() == 0) {
									attributes.append("Type: ").append(component.getName().split("\\(")[0].trim());
								}
								if(IpcConstants.ATTRIBUTE_NAME_DB_VERSION.equals(attributeName)) {
									attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE).append("Version").append(CommonConstants.COLON)
									.append(CommonConstants.SPACE).append(attribute.getValue());
								} else if (IpcConstants.ATTRIBUTE_NAME_VARIANT.equals(attributeName)) {
									attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
									.append(IpcConstants.ATTRIBUTE_NAME_VARIANT).append(CommonConstants.COLON)
									.append(CommonConstants.SPACE).append(attribute.getValue());
								} else if(IpcConstants.ATTRIBUTE_NAME_MANAGED.equalsIgnoreCase(attributeName)) {
									dbLicenseManaged = ("yes").equalsIgnoreCase(attribute.getValue()) ? ", Managed DB" : ", Unmanaged DB";
								} else if(IpcConstants.QUANTITY.equals(attributeName)) {
									solutionPdfBean.setQuantity(Integer.parseInt(attribute.getValue()));
								}
							}  else if (IpcConstants.HYBRID_CONNECTION_CHARGES.equalsIgnoreCase(solutionPdfBean.getName())) {
								if (IpcConstants.CONNECTIVE_TYPE.equals(attributeName)) {
									hybridMap.put(IpcConstants.CONNECTIVE_TYPE, attribute.getValue());
								} else if (IpcConstants.CABLE_TYPE.equals(attributeName)) {
									hybridMap.put(IpcConstants.CABLE_TYPE, attribute.getValue());
								} else if (IpcConstants.L2_THROUGHPUT.equals(attributeName)) {
									hybridMap.put(IpcConstants.L2_THROUGHPUT, attribute.getValue());
								} else if (IpcConstants.SHARED_SWITCH_PORT.equals(attributeName)) {
									hybridMap.put(IpcConstants.SHARED_SWITCH_PORT, attribute.getValue());
								} else if (IpcConstants.REDUNDANCY.equals(attributeName)) {
									hybridMap.put(IpcConstants.REDUNDANCY, attribute.getValue());
								}
							 }	
							else {
								if (attributes.length() != 0) {
									attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE);
								}
								attributes.append(attributeName).append(CommonConstants.COLON)
									.append(CommonConstants.SPACE).append(attribute.getValue());
						   }

							if (STORAGE_ATTR.contains(attribute.getName())) {
								attributes.append(" GB");
							}
						}
						
						if(IpcConstants.DATABASE_LICENSE_CHARGES.equals(solutionPdfBean.getName())) {
							attributes.append(dbLicenseManaged).append(", license as per core licensing policy");
						} else if(IpcConstants.DR_LICENSE_CHARGES.equals(solutionPdfBean.getName())) {
							attributes.append(", for protected VM");
						} 	else if (IpcConstants.HYBRID_CONNECTION_CHARGES.equals(solutionPdfBean.getName())) {
							if (attributes.length() == 0) {
								attributes.append(IpcConstants.CONNECTIVE_TYPE).append(CommonConstants.COLON).append(CommonConstants.SPACE)
								.append(CommonConstants.SPACE)
								.append(hybridMap.get(IpcConstants.CONNECTIVE_TYPE));
							}
							if (hybridMap.containsKey(IpcConstants.CABLE_TYPE)) {
								attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
	                            .append(IpcConstants.CABLE_TYPE).append(CommonConstants.COLON)
	                            .append(CommonConstants.SPACE)
	                            .append(hybridMap.get(IpcConstants.CABLE_TYPE));
							} 
							if (hybridMap.containsKey(IpcConstants.L2_THROUGHPUT)) {
								attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
	                            .append(IpcConstants.L2_THROUGHPUT).append(CommonConstants.COLON)
	                            .append(CommonConstants.SPACE)
	                            .append(hybridMap.get(IpcConstants.L2_THROUGHPUT));
							}
							if (hybridMap.containsKey(IpcConstants.SHARED_SWITCH_PORT)) {
								attributes.append(" with ").append(IpcConstants.SHARED_SWITCH_PORT).append("s")
	                            .append(CommonConstants.COLON).append(CommonConstants.SPACE)
	                            .append(hybridMap.get(IpcConstants.SHARED_SWITCH_PORT));
							}
							if (hybridMap.containsKey(IpcConstants.REDUNDANCY)) {
								if (hybridMap.get(IpcConstants.REDUNDANCY).equalsIgnoreCase(CommonConstants.YES)) {
									attributes.append(" with ").append(IpcConstants.REDUNDANCY.toLowerCase());
								}
								else {
									attributes.append("without").append(IpcConstants.REDUNDANCY.toLowerCase());
								}
							}
						}
						componentPdfBean.setAttributes(attributes.toString());
						
						if (!"".equals(componentPdfBean.getAttributes())) {
							solutionPdfBean.getComponents().add(componentPdfBean);
						}
						
						if(solutionPdfBean.getPricingModel() == null) {
							solutionPdfBean.setPricingModel("Reserved");
						}
					}
				}
			} else {
				SolutionPdfBean solutionPdfBean = new SolutionPdfBean();
				if (IpcConstants.ACCESS.equalsIgnoreCase(productSolution.getOfferingName())) {
					solutionPdfBean.setName("Access Charges");
				} else if(productSolution.getOfferingName().startsWith(IpcConstants.CARBON_VM)) {
					solutionPdfBean.setName(IpcConstants.CARBON_VM + CommonConstants.SPACE + IpcConstants.VM_CHARGES);
				} else {
					solutionPdfBean.setName(solutionDetail.getOfferingName() + CommonConstants.SPACE + IpcConstants.VM_CHARGES);
				}
				solutionPdfBean.setName(solutionPdfBean.getName());
				solutionPdfBean.setCloudType(solutionDetail.getDcCloudType() != null ? solutionDetail.getDcCloudType() : "DC");
				solutionPdfBean.setLocationId(solutionDetail.getDcLocationId());
				solutionPdfBean.setMrc(Double.sum(solutionPdfBean.getMrc(), solutionDetail.getMrc()));
				solutionPdfBean.setNrc(Double.sum(solutionPdfBean.getNrc(), solutionDetail.getNrc()));
				solutionPdfBean.setArc(Double.sum(solutionPdfBean.getArc(), solutionDetail.getArc()));
				solutionPdfBean.setCloudCode(solutionDetail.getCloudCode());
				solutionPdfBean.setParentCloudCode(solutionDetail.getParentCloudCode());
				if(solutionDetail.getPpuRate() == null) {
					solutionDetail.setPpuRate(0D);
				}
				solutionPdfBean.setPpuRate(Double.sum(solutionPdfBean.getPpuRate(), solutionDetail.getPpuRate()));
				solutionPdfBean.setFromServiceInventory(isFromInventory);
				solutions.add(solutionPdfBean);
				
				for (ComponentDetail component : solutionDetail.getComponents()) {
					ComponentPdfBean componentPdfBean = new ComponentPdfBean();
					
					if(COMP_TO_SKIP.contains(component.getName())){
						continue;
					}
					
					if ("Flavor".equalsIgnoreCase(component.getName())) {
						componentPdfBean.setName("IaaS");
						vmComponents.add(componentPdfBean);
					} else if ("Access Type".equalsIgnoreCase(component.getName()) && 
							"EP_DUBAI".equalsIgnoreCase(solutionPdfBean.getLocationId())){
						componentPdfBean.setName("Managed Firewall Charges");
					} else {
						componentPdfBean.setName(component.getName());
					}
					
					if("Additional Storage".equalsIgnoreCase(componentPdfBean.getName())) {
						solutionPdfBean.setHasAdditionalStorage(true);
					}
					
					StringBuilder attributes = new StringBuilder();
					for (AttributeDetail attribute : component.getAttributes()) {
						if ("".equals(attribute.getValue()) || attribute.getValue() == null) {
							continue;
						} else if ( "IOPS Value".equalsIgnoreCase(attribute.getName()) && "0".equalsIgnoreCase(attribute.getValue())) {
							continue;
						} else if ( "accessOption".equalsIgnoreCase(attribute.getName())) {
							if("Data Transfer".equalsIgnoreCase(attribute.getValue())) {
								solutionPdfBean.setName(IpcConstants.DATA_TRANSFER_COMM);
							} else if("Fixed Bandwidth".equalsIgnoreCase(attribute.getValue())) {
								solutionPdfBean.setName(IpcConstants.FIXED_BW);
							}
						} 
						
						if ("EP_DUBAI".equalsIgnoreCase(solutionPdfBean.getLocationId()) && 
								("portBandwidth".equalsIgnoreCase(attribute.getName()) || ("minimumCommitment".equalsIgnoreCase(attribute.getName())))) {
							String value = "portBandwidth".equalsIgnoreCase(attribute.getName()) ? attribute.getValue() : attribute.getValue() + "GB" ;
							attributes.append("vDOM bundled with ").append(value).append(" cloud access");
						} else {
							String attributeName = ATTR_TO_DISP.contains(attribute.getName()) ? attribute.getName() : Utils.convertCamelCaseToTitleCase(attribute.getName());
							
							if (attributes.length() == 0) {
								attributes.append(attributeName).append(CommonConstants.COLON)
									.append(CommonConstants.SPACE).append(attribute.getValue());
							} else {
								attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
									.append(attributeName).append(CommonConstants.COLON).append(CommonConstants.SPACE)
									.append(attribute.getValue());
							}

							if (STORAGE_ATTR.contains(attribute.getName()) || "vRAM".equalsIgnoreCase(attributeName)) {
								attributes.append(" GB");
							}
						}
						
						componentPdfBean.setAttributes(attributes.toString());
						if (IpcConstants.ACCESS.equalsIgnoreCase(productSolution.getOfferingName())) {
							if (!"".equals(componentPdfBean.getAttributes())) {
								componentPdfBean.setAttributes(componentPdfBean.getAttributes() + CommonConstants.COMMA + CommonConstants.SPACE);
							}
							if(quoteDetail == null) {
								componentPdfBean.setAttributes(StringUtils.stripEnd(componentPdfBean.getAttributes().trim(), ","));
							}
						}
					}
					
					if (!"".equals(componentPdfBean.getAttributes())) {
						solutionPdfBean.getComponents().add(componentPdfBean);
					}
				}
				
				if(solutionPdfBean.getPricingModel() == null) {
					solutionPdfBean.setPricingModel("Reserved");
				}
			}
		}
		
		return solutions;
	
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
    public static SimpleDateFormat formatWithTimeStamp() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter;

	} Map<String, String> getProductAttributes(Set<ScProductDetailAttributes> productAttributes) {

		Map<String, String> productAttributesMap = new HashMap<>();
		for (ScProductDetailAttributes productDetailAttributes : productAttributes) {
			productAttributesMap.put(productDetailAttributes.getAttributeName(),
					productDetailAttributes.getAttributeValue());
		}
		LOGGER.info("getProductAttributes response : {}", productAttributesMap);
		return productAttributesMap;

	}
	
	private void identifyActionTypeAndQuantity(IpcServiceAcceptancePdfBean saSolutionDetails) {
		Map<String, SolutionPdfBean> normalizedSolutionsMap = new LinkedHashMap<>();
		Set<SolutionPdfBean> normalizedSolutions = new LinkedHashSet<>();
		Map<String, SolutionPdfBean> quoteCloudFromServiceInventoryM = new HashMap<>();
		Map<String, SolutionPdfBean> deletedSolutionPdfBeanM = new HashMap<>();
		Set<String> quoteCloudToBeDeletedS = new HashSet<>();
		
		Set<SolutionPdfBean> selectedSolutions = saSolutionDetails.getSolutions();
		selectedSolutions.forEach(selectedSolution -> {
			SolutionPdfBean normalizedSolution = null;
			
			String selectedSolutionDetailString = keyFormationBasedOnSolution(selectedSolution);
			LOGGER.info("Formed Key String {}" , selectedSolutionDetailString);
			if (IpcConstants.MACD.equals(saSolutionDetails.getOrderType())) {
				if(IpcConstants.DELETE_VM.equals(saSolutionDetails.getOrderCategory()) && !selectedSolution.isFromServiceInventory()) {
					if(selectedSolutionDetailString.contains(IpcConstants.VM_CHARGES) && null != selectedSolution.getParentCloudCode()) {
						quoteCloudToBeDeletedS.add(selectedSolution.getParentCloudCode());
					}
					return;
				} else {
					normalizedSolution = processActionTypeAndQuantityForMacd(normalizedSolutionsMap,
						selectedSolutionDetailString, selectedSolution, quoteCloudFromServiceInventoryM,
						quoteCloudToBeDeletedS);
				}
				LOGGER.info("Cloud InventoryMap Values");
				quoteCloudFromServiceInventoryM.forEach((key, value) -> LOGGER.info("Key {} and Value {}" , key, value.toString()));
			} else {
				if (normalizedSolutionsMap.containsKey(selectedSolutionDetailString)) {
					normalizedSolution = normalizedSolutionsMap.get(selectedSolutionDetailString);
					normalizedSolution.setQuantity(normalizedSolution.getQuantity() + 1);
				} else {
					normalizedSolution = new SolutionPdfBean(selectedSolution);
					if (IpcConstants.ADDITIONAL_IP_CHARGES.equals(selectedSolution.getName())
							|| IpcConstants.VPN_CLIENT_SITE.equals(selectedSolution.getName())
							|| IpcConstants.VPN_SITE_SITE.equals(selectedSolution.getName())
							|| IpcConstants.DATABASE_LICENSE_CHARGES.equals(selectedSolution.getName())
							|| IpcConstants.DR_LICENSE_CHARGES.equals(selectedSolution.getName())) {
						normalizedSolution.setQuantity(selectedSolution.getQuantity());
					}
				}
			}
			
			normalizedSolutionsMap.put(selectedSolutionDetailString, normalizedSolution);
		});
		//upgrade and downgrade Logic
		for (String cloudCodeToBeDeleted : quoteCloudToBeDeletedS) {
			if (quoteCloudFromServiceInventoryM.containsKey(cloudCodeToBeDeleted)) {
				SolutionPdfBean solutionPdfBean = quoteCloudFromServiceInventoryM.get(cloudCodeToBeDeleted);
				solutionPdfBean.setQuantity(1);
				String formedKey = keyFormationBasedOnSolution(solutionPdfBean);
				if (normalizedSolutionsMap.containsKey(formedKey)) {
					SolutionPdfBean solutionBeanFromMainMap = normalizedSolutionsMap.get(formedKey);
					LOGGER.info("solutionBeanFromMainMap - fromNormalized Map {}" , solutionBeanFromMainMap.toString());
					if (solutionBeanFromMainMap.getQuantity() == 1) {
						normalizedSolutionsMap.remove(formedKey);
						solutionPdfBean.setActionType(IpcConstants.ACTION_TYPE_DELETE);
					} else {
						Integer quantity = solutionBeanFromMainMap.getQuantity();
						solutionBeanFromMainMap.setQuantity(quantity - 1);
						solutionBeanFromMainMap.setActionType(IpcConstants.ACTION_TYPE_MODIFY);
						normalizedSolutionsMap.put(formedKey, solutionBeanFromMainMap);
					}
					if (deletedSolutionPdfBeanM.containsKey(formedKey)) {
						solutionPdfBean.setQuantity(deletedSolutionPdfBeanM.get(formedKey).getQuantity() + 1);
					}
					solutionPdfBean.setMrc(0.0);
					solutionPdfBean.setNrc(0.0);
					solutionPdfBean.setArc(0.0);
					solutionPdfBean.setPpuRate(0.0);
					deletedSolutionPdfBeanM.put(formedKey, solutionPdfBean);
				}
			}
		}
		
		LOGGER.info("deletedSolutionPdfBeanM Values");
		deletedSolutionPdfBeanM.forEach((key, value) -> LOGGER.info("DeleteKey {} and DeleteValue {}" , key, value.toString()));
		
		LOGGER.info("normalizedSolutionsMap Values");
		normalizedSolutionsMap.forEach((key, value) -> LOGGER.info("mainKey {} and mainValue {}" , key, value.toString()));

		normalizedSolutionsMap.forEach((detailString, normalizedSolution) -> {
			normalizedSolutions.add(normalizedSolution);
		});
		//Upgrade/Downgrade from one VM Falvour to another - delete
		normalizedSolutions.addAll(deletedSolutionPdfBeanM.values().stream()
				.filter(x -> ((x.getActionType() != null && x.getActionType().equals(IpcConstants.ACTION_TYPE_DELETE))
						|| IpcConstants.DELETE_VM.equals(saSolutionDetails.getOrderCategory())))
				.map(v -> {
					v.setActionType(IpcConstants.ACTION_TYPE_DELETE);
					return v;
				}).collect(Collectors.toList()));
		
		if (IpcConstants.DELETE_VM.equals(saSolutionDetails.getOrderCategory())) {
			Map<String, DeletedLineItemDetailsBean> deletedLineItemDetailsBeanM = new HashMap<>();
			List<ScServiceDetail> scServiceDetail = scServiceDetailRepository
					.findByUuidOrderByIdDesc(saSolutionDetails.getServiceId());
			List<ScProductDetail> scProductDetails = scProductDetailRepository
					.findByScServiceDetailId(scServiceDetail.get(0).getId());
			scProductDetails.stream()
					.filter(x -> x.getType().equals(IpcConstants.CLOUD)
							&& !x.getSolutionName().equals(IpcConstants.EARLY_TERMINATION_CHARGES)
							&& x.getParentCloudCode() != null)
					.forEach(scProductDetail -> {
						ScProductDetailAttribute scProductDetailAttribute = scProductDetailAttributeRepository
								.findByScProductDetail_idAndAttributeNameAndCategory(scProductDetail.getId(),
										IpcConstants.HOST_NAME, IpcConstants.IPC_COMMON);
						if (scProductDetailAttribute != null) {
							deletedLineItemDetailsBeanM.put(scProductDetail.getParentCloudCode(),new DeletedLineItemDetailsBean(scProductDetail.getSolutionName() + IpcConstants.SINGLE_SPACE+ IpcConstants.VM,
											scProductDetailAttribute.getAttributeValue()));
						}
					});
			saSolutionDetails.setDeletedVms(new HashSet<DeletedLineItemDetailsBean>(deletedLineItemDetailsBeanM.values()));
		}

		saSolutionDetails.setSolutions(getSortedSolution(normalizedSolutions));
	}
	
	private SolutionPdfBean processActionTypeAndQuantityForMacd(Map<String, SolutionPdfBean> normalizedSolutionsMap,
			String selectedSolutionDetailString, SolutionPdfBean selectedSolution,
			Map<String, SolutionPdfBean> quoteCloudFromServiceInventoryM, Set<String> quoteCloudToBeDeletedS) {

		SolutionPdfBean normalizedSolution;
		if (normalizedSolutionsMap.containsKey(selectedSolutionDetailString)) {
			normalizedSolution = normalizedSolutionsMap.get(selectedSolutionDetailString);
			LOGGER.info("normalizedSolution in existing element - {}", normalizedSolution.toString());
			if (selectedSolution.isFromServiceInventory()) {
				if (IpcConstants.ACTION_TYPE_ADD.equals(normalizedSolution.getActionType())) {
					normalizedSolution.setActionType(IpcConstants.ACTION_TYPE_MODIFY);
				}
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcConstants.VM_CHARGES)) {
					quoteCloudFromServiceInventoryM.put(selectedSolution.getCloudCode(), selectedSolution);
				}
			} else {
				if (IpcConstants.ACTION_TYPE_ADD.equals(normalizedSolution.getActionType())) {
					normalizedSolution.setActionType(IpcConstants.ACTION_TYPE_ADD);
				} else {
					normalizedSolution.setActionType(IpcConstants.ACTION_TYPE_MODIFY);
				}
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcConstants.VM_CHARGES) && null != selectedSolution.getParentCloudCode()) {
					quoteCloudToBeDeletedS.add(selectedSolution.getParentCloudCode());
				}
			}
			// except Vdom, Access Type and Backup All other Quantity Should be recalculated
			if (selectedSolutionDetailString.contains(IpcConstants.VM_CHARGES)) {
				normalizedSolution.setQuantity(normalizedSolution.getQuantity() + 1);
			} else if (selectedSolutionDetailString.contains(IpcConstants.ADDITIONAL_IP_CHARGES)
					|| selectedSolutionDetailString.contains(IpcConstants.VPN_CLIENT_SITE)
					|| selectedSolutionDetailString.contains(IpcConstants.VPN_SITE_SITE) 
					|| selectedSolutionDetailString.contains(IpcConstants.DATABASE_LICENSE_CHARGES)
					|| selectedSolutionDetailString.contains(IpcConstants.DR_LICENSE_CHARGES)) {
				normalizedSolution.setQuantity(normalizedSolution.getQuantity() + selectedSolution.getQuantity());
			}
		} else {
			normalizedSolution = new SolutionPdfBean(selectedSolution);
			LOGGER.info("normalizedSolution in newly added flow - {}", normalizedSolution.toString());
			if (IpcConstants.ADDITIONAL_IP_CHARGES.equals(selectedSolution.getName())
					|| IpcConstants.VPN_CLIENT_SITE.equals(selectedSolution.getName())
					|| IpcConstants.VPN_SITE_SITE.equals(selectedSolution.getName()) 
					|| IpcConstants.DATABASE_LICENSE_CHARGES.equals(selectedSolution.getName())
					|| IpcConstants.DR_LICENSE_CHARGES.equals(selectedSolution.getName())) {
				normalizedSolution.setQuantity(selectedSolution.getQuantity());
			}
			if (selectedSolution.isFromServiceInventory()) {
				normalizedSolution.setActionType(IpcConstants.ACTION_TYPE_NO_CHANGE);
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcConstants.VM_CHARGES)) {
					quoteCloudFromServiceInventoryM.put(selectedSolution.getCloudCode(), selectedSolution);
				}
			} else {
				normalizedSolution.setActionType(IpcConstants.ACTION_TYPE_ADD);
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcConstants.VM_CHARGES) && null != selectedSolution.getParentCloudCode()) {
					quoteCloudToBeDeletedS.add(selectedSolution.getParentCloudCode());
				}
			}
		}
		LOGGER.info("Modified Type {} and Quantity {}", normalizedSolution.getActionType(),
				normalizedSolution.getQuantity());
		return normalizedSolution;
	}

	private Set<SolutionPdfBean> getSortedSolution(Set<SolutionPdfBean> solutionBeans) {
		List<SolutionPdfBean> unsortedSolutions = new ArrayList<>();
		Set<SolutionPdfBean> sortedSolutions = new LinkedHashSet<>();
		unsortedSolutions.addAll(solutionBeans);
		Collections.sort(unsortedSolutions, (final SolutionPdfBean o1, final SolutionPdfBean o2) -> {
			Integer firstIndex = SOLUTIONS_NAMES.indexOf(o1.getName());
			Integer secondIndex = SOLUTIONS_NAMES.indexOf(o2.getName());
			return firstIndex.compareTo(secondIndex);
		});
		sortedSolutions.addAll(unsortedSolutions);
		return sortedSolutions;
	}
	
	private String getSolutionDetailString(SolutionPdfBean solution) {
		StringBuilder solutionDetailStringBuilder = new StringBuilder(solution.getName());
		
		solutionDetailStringBuilder.append("_").append(solution.getPricingModel());
		solutionDetailStringBuilder.append("_").append(solution.isHasAdditionalStorage());
		
		List<ComponentPdfBean> components = solution.getComponents();
		components.forEach(component -> {
			solutionDetailStringBuilder.append("_").append(component.getName());
			solutionDetailStringBuilder.append("_").append(component.getAttributes());
		});
		
		return solutionDetailStringBuilder.toString();
	}
	
	private String keyFormationBasedOnSolution(SolutionPdfBean selectedSolution) {
		if(selectedSolution.getName().contains(IpcConstants.DATA_TRANSFER_COMM) || selectedSolution.getName().contains(IpcConstants.FIXED_BW)) {
			return IpcConstants.ACCESS_TYPE;
		} else if(selectedSolution.getName().contains(IpcConstants.BACKUP_FE_VOL)) {
			return IpcConstants.BACKUP_FE_VOL;
		} else if (selectedSolution.getName().contains(IpcConstants.VDOM_SMALL)) {
			return IpcConstants.VDOM_SMALL;
		}else {
			return getSolutionDetailString(selectedSolution);
		}
	}
	
}
