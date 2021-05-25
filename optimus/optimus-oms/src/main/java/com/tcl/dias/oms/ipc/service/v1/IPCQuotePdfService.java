package com.tcl.dias.oms.ipc.service.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Sets;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.BillingContact;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CustomerContactDetails;
import com.tcl.dias.common.beans.CustomerLeContactDetailBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SPDetails;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.UserInfoUtils;
import com.tcl.dias.common.utils.UserType;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.AddressDetail;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.AttributeDetail;
import com.tcl.dias.oms.beans.ComponentDetail;
import com.tcl.dias.oms.beans.LegalAttributeBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.constants.QuoteStageConstants;
import com.tcl.dias.oms.docusign.service.DocusignService;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MacdDetail;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteCloud;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteTnc;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.MacdDetailRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.QuoteCloudRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteTncRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.ipc.beans.ComponentPdfBean;
import com.tcl.dias.oms.ipc.beans.DeletedVmsBean;
import com.tcl.dias.oms.ipc.beans.ProductSolutionBean;
import com.tcl.dias.oms.ipc.beans.QuoteBean;
import com.tcl.dias.oms.ipc.beans.QuotePdfBean;
import com.tcl.dias.oms.ipc.beans.QuoteToLeBean;
import com.tcl.dias.oms.ipc.beans.QuoteToLeProductFamilyBean;
import com.tcl.dias.oms.ipc.beans.SolutionDetail;
import com.tcl.dias.oms.ipc.beans.SolutionPdfBean;
import com.tcl.dias.oms.ipc.constants.IPCQuoteConstants;
import com.tcl.dias.oms.ipc.constants.IpcPDFConstants;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.partner.constants.PartnerConstants;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
@Transactional
public class IPCQuotePdfService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IPCQuotePdfService.class);

	@Value("${application.env}")
	private String appEnv;

	@Value("${app.host}")	
	private String appHost;

	@Value("${attatchment.queue}")
	private String attachmentQueue;
	
	@Value("${attachment.requestId.queue}")
	private String attachmentRequestIdQueue;

	@Value("${cof.auto.upload.path}")
	private String cofAutoUploadPath;

	@Value("${cof.manual.upload.path}")
	private String cofManualUploadPath;

	@Value("${rabbitmq.billing.contact.queue}")
	private String billingContactQueue;

	@Value("${rabbitmq.customer.contact.email.queue}")
	private String customerLeContactQueue;

	@Value("${info.docusign.cof.sign}")
	private String docusignRequestQueue;

	@Value("${rabbitmq.location.detail}")
	private String locationQueue;
	
	@Value("${spring.rabbitmq.host}")
	private String mqHostName;

	@Value("${rabbitmq.suplierle.queue}")
	private String suplierLeQueue;

	@Value("${swift.api.enabled}")
	private String swiftApiEnabled;
	
	@Value("${temp.download.url.expiryWindow}")
	private String tempDownloadUrlExpiryWindow;
	
	@Value("${temp.upload.url.expiryWindow}")
	private String tempUploadUrlExpiryWindow;

	@Value("${rabbitmq.pricing.ipc.location}")
	private String ipcPricingLocationQueue;
	
	@Value("${rabbitmq.pricing.ipc.datatransfer.price}")
	private String ipcPricingDataTransferQueue;
	
	@Value("${rabbitmq.customer.contact.details.queue}")
	String customerLeContactQueueName;
	
	@Value("${rabbitmq.ipc.si.related.details.queue}")
	String ipcSIRelatedDetailsQueue;
	
	@Value("${rabbitmq.ipc.si.solutions.queue}")
	String ipcSiSolutionsQueue;
	
	@Autowired
	private CofDetailsRepository cofDetailsRepository;

	@Autowired
	private DocusignService docuSignService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private IPCQuoteService ipcQuoteService;

	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	MACDUtils macdUtils;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private OmsAttachmentRepository omsAttachmentRepository;
	
	@Autowired
	private OmsUtilService omsUtilService;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private QuoteToLeRepository quoteToLeRepository;
	
	@Autowired
	private QuoteTncRepository quoteTncRepository;
	
	@Autowired
	private SpringTemplateEngine templateEngine;

	@Autowired
	private QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	private QuoteRepository quoteRepository;

	@Autowired
    private QuotePriceRepository quotePriceRepository;
	
	@Autowired
    private MacdDetailRepository macdDetailRepository;
	
	@Autowired
	UserInfoUtils userInfoUtils;
	
	@Autowired
	QuoteCloudRepository quoteCloudRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	

	private static final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";
	
	private static final Set<String> COMP_TO_SKIP =  Sets.newHashSet("IPC Common", "Storage Partition");

	private static final Set<String> ATTR_TO_DISP =  Sets.newHashSet("vCPU", "vRAM", "Connectivity Type", "L2 Throughput", "Cable Type", "Shared Switch Port");
	
	private static final Set<String> STORAGE_ATTR =  Sets.newHashSet("Storage", "Storage Value", "frontVolumeSize", "minimumCommitment");
	
	private static final List<String> COMPUTES = Arrays.asList("L", "C", "G", "X", "M", "B", "H");

	private static final List<String> VARIANTS = Arrays.asList("Nickel", "Bronze", "Silver", "Cobalt", "Gold",
			"Platinum", "Titanium");

	public static final List<String> SOLUTIONS_NAMES = new ArrayList<>();

	static {
		COMPUTES.forEach(compute -> VARIANTS.forEach(variant -> SOLUTIONS_NAMES.add(compute + "." + variant + CommonConstants.SPACE + IpcPDFConstants.VM_CHARGES)));
		SOLUTIONS_NAMES.add(IPCQuoteConstants.CARBON_VM + CommonConstants.SPACE + IpcPDFConstants.VM_CHARGES);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.DATA_TRANSFER_MIN_COMMITTMENT);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.FIXED_BW);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.VDOM_SOLUTION_NAME);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.VPN_C2S_SOLUTION_NAME);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.VPN_S2S_SOLUTION_NAME);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.BACKUP_SOLUTION_NAME);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.DATABASE_LICENSE_CHARGES);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.DR_LICENSE_CHARGES);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.HYBRID_CONNECTION_CHARGES);
		SOLUTIONS_NAMES.add(IPCQuoteConstants.EARLY_TERMINATION_CHARGES);
	}

	public String processQuotePdf(Integer quoteId, Integer quoteToLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			LOGGER.debug("Processing quote PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = ipcQuoteService.getQuoteDetails(quoteId);
			QuotePdfBean cofPdfRequest = new QuotePdfBean();
			constructVariable(quoteDetail, cofPdfRequest);
			//For Partner Term and Condition content in Quote pdf
			if(Objects.nonNull(userInfoUtils.getUserType()) &&
					userInfoUtils.getUserType().equalsIgnoreCase(UserType.PARTNER.toString())){
				cofPdfRequest.setIsPartner(true);
			}
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if(quoteToLe.isPresent()) {
				cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
				cofPdfRequest.setContractTerm(quoteToLe.get().getTermInMonths());
				if(MACDConstants.MACD_QUOTE_TYPE.equals(cofPdfRequest.getOrderType())) {
					LOGGER.info("Retrieving serviceId from MACD");
					String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
					LOGGER.info("Category::"+category);
					cofPdfRequest.setQuoteCategory(category);
					MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.get().getId());
					cofPdfRequest.setServiceId(macdDetail.getTpsServiceId());
					addPreviousOrderSolutions(cofPdfRequest);
				}else {
					identifyActionTypeAndQuantity(cofPdfRequest);
				}
			}
			
			LOGGER.info("QuoteDetail {}::", Utils.convertObjectToJson(quoteDetail));
			LOGGER.info("CofPdfRequest {}::", Utils.convertObjectToJson(cofPdfRequest));
			@SuppressWarnings("unchecked")
			Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
			Context context = new Context();
			context.setVariables(variable);
			String html = templateEngine.process("ipcquote_template", context);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			PDFGenerator.createPdf(html, bos);
			byte[] outArray = bos.toByteArray();
			String fileName = "Quote_" + quoteDetail.getQuoteCode() + ".pdf";
			response.reset();
			response.setContentType("application/pdf");
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
			FileCopyUtils.copy(outArray, response.getOutputStream());
			bos.flush();
			bos.close();
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (IOException | DocumentException e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	public String processQuoteHtml(Integer quoteId) throws TclCommonException {
		String html = null;
		try {
			LOGGER.debug("Processing quote html PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = ipcQuoteService.getQuoteDetails(quoteId);
			html = getQuoteHtml(quoteDetail);
		} catch (TclCommonException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return html;
	}

	private String getQuoteHtml(QuoteBean quoteDetail) throws TclCommonException {
		QuotePdfBean cofPdfRequest = new QuotePdfBean();
		constructVariable(quoteDetail, cofPdfRequest);

		Integer quoteToLeId=quoteDetail.getLegalEntities().stream().findFirst().get().getQuoteleId();
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);

		if(quoteToLe.isPresent()) {
			cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
			cofPdfRequest.setContractTerm(quoteToLe.get().getTermInMonths());
		}

		@SuppressWarnings("unchecked")
		Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
		Context context = new Context();
		context.setVariables(variable);
		return templateEngine.process("ipcquote_template", context);
	}

	public void constructVariable(QuoteBean quoteDetail, QuotePdfBean cofPdfRequest) throws TclCommonException {
		List<ComponentPdfBean> vmComponents = new ArrayList<>();
		cofPdfRequest.setOrderRef(quoteDetail.getQuoteCode());
		if(cofPdfRequest.getOrderDate() == null) {
			cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(new Date()));
		}
		if(cofPdfRequest.getPresentDate() == null) {
			cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(new Date()));
		}
		QuoteTnc quoteTnc = quoteTncRepository.findByQuoteId(quoteDetail.getQuoteId());
		if (Objects.nonNull(quoteTnc) && StringUtils.isNotBlank(quoteTnc.getTnc())) {
			String tnc = quoteTnc.getTnc().replaceAll("&", "&amp;");
			cofPdfRequest.setTnc(tnc);
			cofPdfRequest.setIsTnc(true);
		}else {
			cofPdfRequest.setIsTnc(false);
			cofPdfRequest.setTnc(CommonConstants.EMPTY);
		}
		cofPdfRequest.setOrderType(quoteDetail.getQuoteType());
		cofPdfRequest.setProductName(CommonConstants.IPC_DESC);	
		Set<String> oldServiceIdsForMigrationFlow = new TreeSet<>();
		for (QuoteToLeBean quoteLe : quoteDetail.getLegalEntities()) {
			cofPdfRequest.setBillingCurrency(quoteLe.getCurrency());
			constructquoteLeAttributes(cofPdfRequest, quoteLe);
			constructSupplierInformations(cofPdfRequest, quoteLe);
			for (QuoteToLeProductFamilyBean productFamily : quoteLe.getProductFamilies()) {
				for (ProductSolutionBean productSolution : productFamily.getSolutions()) {
					cofPdfRequest.getSolutions().addAll(constructSolutions(productSolution, cofPdfRequest, oldServiceIdsForMigrationFlow, vmComponents, quoteDetail, false));
				}
			}
			for (ComponentPdfBean componentPdfBean : vmComponents) {
				String attributes = componentPdfBean.getAttributes();
				String managedAttribute = "Unmanaged VM";
				if(cofPdfRequest.getIsVMsManaged()) {
					managedAttribute = "Managed VM";
				}
				componentPdfBean.setAttributes(attributes + CommonConstants.COMMA + CommonConstants.SPACE + managedAttribute);
			}
			
			Map<String, Object> localIPCPricingLocation = constructIPCPricingLocationDetails(cofPdfRequest.getHostingLocation());
			String city = (String) localIPCPricingLocation.get("CITY");
			String country = (String) localIPCPricingLocation.get("COUNTRY_CODE");
			String state = (String) localIPCPricingLocation.get("STATE");
			String cloudLocation = city + CommonConstants.COMMA + CommonConstants.SPACE + state + CommonConstants.COMMA + CommonConstants.SPACE + country;
			cofPdfRequest.setHostingCountry(country);
			cofPdfRequest.setHostingLocation(cloudLocation);

			if (cofPdfRequest.getIsDataTransferSelected()) {
				constructIPCDataTransferPrice(country, cofPdfRequest);
			}

			cofPdfRequest.setTotalARC(quoteLe.getFinalArc());
			cofPdfRequest.setTotalMRC(quoteLe.getFinalMrc());
			cofPdfRequest.setTotalNRC(quoteLe.getFinalNrc());
			cofPdfRequest.setTotalTCV(quoteLe.getTotalTcv());
			
			if(!oldServiceIdsForMigrationFlow.isEmpty()) {
				cofPdfRequest.setOldServiceId(oldServiceIdsForMigrationFlow.toString().replace("[", "").replace("]", ""));
			}
			
		}
	}
	
	private Set<SolutionPdfBean> constructSolutions(ProductSolutionBean productSolution, QuotePdfBean cofPdfRequest, 
			Set<String> oldServiceIdsForMigrationFlow, List<ComponentPdfBean> vmComponents, QuoteBean quoteDetail, boolean isFromInventory) {
		Set<SolutionPdfBean> solutions = new LinkedHashSet<>();
		
		for (SolutionDetail solutionDetail : productSolution.getCloudSolutions()) {
			if(solutionDetail.getDcLocationId() != null) {
				cofPdfRequest.setHostingLocation(solutionDetail.getDcLocationId());
				if(!("EP_V2_MUM".equals(solutionDetail.getDcLocationId()) 
						|| "EP_V2_DEL".equals(solutionDetail.getDcLocationId()) 
						|| "EP_V2_BL".equals(solutionDetail.getDcLocationId()))) {
					cofPdfRequest.setIsInternational(true);
				}
			}
			if(solutionDetail.getDcCloudType() != null) {
				cofPdfRequest.setDcCloudType(solutionDetail.getDcCloudType());
			}
			
			if (QuoteConstants.IPC_ADDON.getConstantCode().equalsIgnoreCase(productSolution.getOfferingName())) {
				for (ComponentDetail component : solutionDetail.getComponents()) {
					if ("VPN Connection".equalsIgnoreCase(component.getName())) {
						SolutionPdfBean c2sSolutionPdfBean = new SolutionPdfBean();
						c2sSolutionPdfBean.setName(IPCQuoteConstants.VPN_C2S_SOLUTION_NAME);
						ComponentPdfBean c2sComponentPdfBean = new ComponentPdfBean();
						c2sComponentPdfBean.setName("IPC Add-on");
						c2sComponentPdfBean.setAttributes("Type: Client to Site");
						c2sSolutionPdfBean.getComponents().add(c2sComponentPdfBean);
						c2sSolutionPdfBean.setFromServiceInventory(isFromInventory);
						solutions.add(c2sSolutionPdfBean);
						
						SolutionPdfBean s2sSolutionPdfBean = new SolutionPdfBean();
						s2sSolutionPdfBean.setName(IPCQuoteConstants.VPN_S2S_SOLUTION_NAME);
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
						
						QuotePrice quotePrice;
						if(component.getComponentId() != null) {
							quotePrice = getComponentQuotePrice(component);
						} else {
							quotePrice = new QuotePrice();
							quotePrice.setEffectiveMrc(component.getMrc());
							quotePrice.setEffectiveNrc(component.getNrc());
							quotePrice.setEffectiveArc(component.getArc());
						}
						
						if (!Objects.isNull(quotePrice)) {
							c2sSolutionPdfBean.setMrc(quotePrice.getEffectiveMrc());
							s2sSolutionPdfBean.setMrc(quotePrice.getEffectiveMrc());
							if(isFromInventory) {
								c2sSolutionPdfBean.setNrc(0D);
								s2sSolutionPdfBean.setNrc(0D);
							} else {
								c2sSolutionPdfBean.setNrc(quotePrice.getEffectiveNrc());
								s2sSolutionPdfBean.setNrc(quotePrice.getEffectiveNrc());
							}
						}
					} else if ("managed".equalsIgnoreCase(component.getName())) {
						for (AttributeDetail attribute : component.getAttributes()) {
							if ("".equals(attribute.getValue()) || attribute.getValue() == null) {
								continue;
							}
							if ("managed".equalsIgnoreCase(attribute.getName()) && "true".equalsIgnoreCase(attribute.getValue())) {
								cofPdfRequest.setIsVMsManaged(true);
								continue;
							}
						}
					}  else {
						SolutionPdfBean solutionPdfBean = new SolutionPdfBean();
						
						ComponentPdfBean componentPdfBean = new ComponentPdfBean();
						componentPdfBean.setName("IPC Add-on");
						
						if ("VDOM".equalsIgnoreCase(component.getName())) {
							solutionPdfBean.setName(IPCQuoteConstants.VDOM_SOLUTION_NAME);
						} else if ("Additional Ip".equalsIgnoreCase(component.getName())) {
							solutionPdfBean.setName(IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME);
						} else if ("Backup".equalsIgnoreCase(component.getName())) {
							solutionPdfBean.setName(IPCQuoteConstants.BACKUP_SOLUTION_NAME);
						} else if (component.getName().startsWith(IPCQuoteConstants.MYSQL)
								|| component.getName().startsWith(IPCQuoteConstants.MSSQL_SERVER)
								|| component.getName().startsWith(IPCQuoteConstants.POSTGRESQL)) {
							solutionPdfBean.setName(IPCQuoteConstants.DATABASE_LICENSE_CHARGES);
						} else if (component.getName().startsWith(IPCQuoteConstants.ZERTO)
								|| component.getName().startsWith(IPCQuoteConstants.DOUBLE_TAKE)) {
							solutionPdfBean.setName(IPCQuoteConstants.DR_LICENSE_CHARGES);
						} else if (component.getName().startsWith(IPCQuoteConstants.HYBRID_CONNECTION)) {
							solutionPdfBean.setName(IPCQuoteConstants.HYBRID_CONNECTION_CHARGES);
						} else {
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
							} else if (IpcPDFConstants.CLIENT_GROUP_NAME.equalsIgnoreCase(attribute.getName()) 
									|| IpcPDFConstants.VDOM_DETAILS.equalsIgnoreCase(attribute.getName())) {
								continue;
							}
							
							String attributeName = ATTR_TO_DISP.contains(attribute.getName()) ? attribute.getName() : Utils.convertCamelCaseToTitleCase(attribute.getName());
							if(IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME.equals(solutionPdfBean.getName()) 
									|| IPCQuoteConstants.VDOM_SOLUTION_NAME.equals(solutionPdfBean.getName())) {
								if(("Ip Quantity").equals(attributeName) || IPCQuoteConstants.ATTRIBUTE_QUANTITY.equals(attributeName)) {
									solutionPdfBean.setQuantity(Integer.parseInt(attribute.getValue()));
								}
								if (attributes.length() == 0) {
									if(IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME.equals(solutionPdfBean.getName())) {
										attributes.append("Type: IPv4");
									} else if(IPCQuoteConstants.VDOM_SOLUTION_NAME.equals(solutionPdfBean.getName())){
										attributes.append("Type: Shared");
									}
								}
							} else if (IPCQuoteConstants.DATABASE_LICENSE_CHARGES.equals(solutionPdfBean.getName())
									|| IPCQuoteConstants.DR_LICENSE_CHARGES.equals(solutionPdfBean.getName())) {
								if(attributes.length() == 0) {
									attributes.append("Type: ").append(component.getName().split("\\(")[0].trim());
								}
								if(IPCQuoteConstants.ATTRIBUTE_VERSION.equals(attributeName)) {
									attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE).append(IPCQuoteConstants.ATTRIBUTE_VERSION).append(CommonConstants.COLON)
									.append(CommonConstants.SPACE).append(attribute.getValue());
								} else if (IPCQuoteConstants.ATTRIBUTE_VARIANT.equals(attributeName)) {
									attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
											.append(IPCQuoteConstants.ATTRIBUTE_VARIANT).append(CommonConstants.COLON)
											.append(CommonConstants.SPACE).append(attribute.getValue());
								} else if(IPCQuoteConstants.ATTRIBUTE_MANAGED.equalsIgnoreCase(attributeName)) {
									dbLicenseManaged = ("yes").equalsIgnoreCase(attribute.getValue()) ? ", Managed DB" : ", Unmanaged DB";
								} else if(IPCQuoteConstants.ATTRIBUTE_QUANTITY.equals(attributeName)) {
									solutionPdfBean.setQuantity(Integer.parseInt(attribute.getValue()));
								}
							} else if (IPCQuoteConstants.HYBRID_CONNECTION_CHARGES.equals(solutionPdfBean.getName())) {
								if(IPCQuoteConstants.CONNECTIVITY_TYPE.equals(attributeName)) {
									hybridMap.put(IPCQuoteConstants.CONNECTIVE_TYPE, attribute.getValue());
								} else if(IPCQuoteConstants.CABLE_TYPE.equals(attributeName)) {
									hybridMap.put(IPCQuoteConstants.CABLE_TYPE, attribute.getValue());
								} else if (IPCQuoteConstants.L2_THROUGHPUT.equals(attributeName)) {
									hybridMap.put(IPCQuoteConstants.L2_THROUGHPUT, attribute.getValue());
								} else if (IPCQuoteConstants.SHARED_SWITCH_PORT.equals(attributeName)) {
									hybridMap.put(IPCQuoteConstants.SHARED_SWITCH_PORT, attribute.getValue());
								} else if (IPCQuoteConstants.REDUNDANCY.equals(attributeName)) {
									hybridMap.put(IPCQuoteConstants.REDUNDANCY, attribute.getValue());
								}
							} else {
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
						if(IPCQuoteConstants.DATABASE_LICENSE_CHARGES.equals(solutionPdfBean.getName())) {
							attributes.append(dbLicenseManaged).append(", license as per core licensing policy");
						} else if(IPCQuoteConstants.DR_LICENSE_CHARGES.equals(solutionPdfBean.getName())) {
							attributes.append(", for protected VM");
						} else if (IPCQuoteConstants.HYBRID_CONNECTION_CHARGES.equals(solutionPdfBean.getName())) {
							if (attributes.length() == 0) {
								attributes.append(IPCQuoteConstants.CONNECTIVE_TYPE).append(CommonConstants.COLON)
										.append(CommonConstants.SPACE)
										.append(hybridMap.get(IPCQuoteConstants.CONNECTIVE_TYPE));
							}
							if (hybridMap.containsKey(IPCQuoteConstants.CABLE_TYPE)) {
								attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
										.append(IPCQuoteConstants.CABLE_TYPE).append(CommonConstants.COLON)
										.append(CommonConstants.SPACE)
										.append(hybridMap.get(IPCQuoteConstants.CABLE_TYPE));
							}
							if (hybridMap.containsKey(IPCQuoteConstants.L2_THROUGHPUT)) {
								attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE)
										.append(IPCQuoteConstants.L2_THROUGHPUT).append(CommonConstants.COLON)
										.append(CommonConstants.SPACE)
										.append(hybridMap.get(IPCQuoteConstants.L2_THROUGHPUT));
							}
							if (hybridMap.containsKey(IPCQuoteConstants.SHARED_SWITCH_PORT)) {
								attributes.append(" with ").append(IPCQuoteConstants.SHARED_SWITCH_PORT).append("s")
										.append(CommonConstants.COLON).append(CommonConstants.SPACE)
										.append(hybridMap.get(IPCQuoteConstants.SHARED_SWITCH_PORT));
							}
							if (hybridMap.containsKey(IPCQuoteConstants.REDUNDANCY)) {
								if (hybridMap.get(IPCQuoteConstants.REDUNDANCY).equalsIgnoreCase(CommonConstants.YES)) {
									attributes.append(" with ").append(IPCQuoteConstants.REDUNDANCY.toLowerCase());
								} else {
									attributes.append(" without ").append(IPCQuoteConstants.REDUNDANCY.toLowerCase());
								}
							}
						}
						componentPdfBean.setAttributes(attributes.toString());
						
						if (!"".equals(componentPdfBean.getAttributes())) {
							solutionPdfBean.getComponents().add(componentPdfBean);
						}
						
						QuotePrice quotePrice;
						if(component.getComponentId() != null) {
							quotePrice = getComponentQuotePrice(component);
						} else {
							quotePrice = new QuotePrice();
							quotePrice.setEffectiveMrc(component.getMrc());
							quotePrice.setEffectiveNrc(component.getNrc());
							quotePrice.setEffectiveArc(component.getArc());
						}
						
						if (!Objects.isNull(quotePrice)) {
							solutionPdfBean.setMrc(quotePrice.getEffectiveMrc());
							if(isFromInventory) {
								solutionPdfBean.setNrc(0D);
							} else {
								solutionPdfBean.setNrc(quotePrice.getEffectiveNrc());
							}
							solutionPdfBean.setArc(quotePrice.getEffectiveArc());
							solutionPdfBean.setPpuRate(0D);
						}
						
						if(solutionPdfBean.getPricingModel() == null) {
							solutionPdfBean.setPricingModel("Reserved");
						}
					}
				}
			} else {
				SolutionPdfBean solutionPdfBean = new SolutionPdfBean();
				if (QuoteConstants.ACCESS.getConstantCode().equalsIgnoreCase(productSolution.getOfferingName())) {
					solutionPdfBean.setName("Access Charges");
				} else if (IPCQuoteConstants.EARLY_TERMINATION_CHARGES.equalsIgnoreCase(productSolution.getOfferingName())) {
					solutionPdfBean.setName(productSolution.getOfferingName());
				} else if(productSolution.getOfferingName().startsWith(IPCQuoteConstants.CARBON_VM)) {
					solutionPdfBean.setName(IPCQuoteConstants.CARBON_VM + CommonConstants.SPACE + IpcPDFConstants.VM_CHARGES);
				} else {
					solutionPdfBean.setName(solutionDetail.getOfferingName() + CommonConstants.SPACE + IpcPDFConstants.VM_CHARGES);
				}
				solutionPdfBean.setName(solutionPdfBean.getName());
				solutionPdfBean.setLocationId(solutionDetail.getDcLocationId());
				solutionPdfBean.setMrc(Double.sum(solutionPdfBean.getMrc(), solutionDetail.getMrc()));
				if(isFromInventory) {
					solutionPdfBean.setNrc(Double.sum(solutionPdfBean.getNrc(), 0D));
				} else {
					solutionPdfBean.setNrc(Double.sum(solutionPdfBean.getNrc(), solutionDetail.getNrc()));
				}
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
					
					if(("IPC Common").equalsIgnoreCase(component.getName())) {
						for (AttributeDetail attribute : component.getAttributes()) {
							if ("pricingModel".equalsIgnoreCase(attribute.getName())) {
								solutionPdfBean.setPricingModel(attribute.getValue());
								if(!("Reserved").equalsIgnoreCase(solutionPdfBean.getPricingModel())) {
									cofPdfRequest.setHasPayPerUseVM(true);
								}
							} else if(oldServiceIdsForMigrationFlow != null && IpcPDFConstants.OLD_SERVICE_ID.equalsIgnoreCase(attribute.getName()) && attribute.getValue() != null) {
								oldServiceIdsForMigrationFlow.add(attribute.getValue());
							}
						}
					}
					
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
						} else if ((CommonConstants.MACD.equalsIgnoreCase(cofPdfRequest.getOrderType())
								&& IpcPDFConstants.OLD_SERVICE_ID.equalsIgnoreCase(attribute.getName()))
								|| IPCQuoteConstants.VDOM_DETAILS.equalsIgnoreCase(attribute.getName())) {
							continue;
						} else if ( "accessOption".equalsIgnoreCase(attribute.getName())) {
							if("Data Transfer".equalsIgnoreCase(attribute.getValue())) {
								cofPdfRequest.setIsDataTransferSelected(true);
								solutionPdfBean.setName(IPCQuoteConstants.DATA_TRANSFER_MIN_COMMITTMENT);
							} else if("Fixed Bandwidth".equalsIgnoreCase(attribute.getValue())) {
								solutionPdfBean.setName(IPCQuoteConstants.FIXED_BW);
							}
						} else if (oldServiceIdsForMigrationFlow != null && IpcPDFConstants.OLD_SERVICE_ID.equalsIgnoreCase(attribute.getName()) && attribute.getValue() != null) {
							oldServiceIdsForMigrationFlow.add(attribute.getValue());
							continue;
						}
						
						if ("EP_DUBAI".equalsIgnoreCase(solutionPdfBean.getLocationId()) && 
								("portBandwidth".equalsIgnoreCase(attribute.getName()) || ("minimumCommitment".equalsIgnoreCase(attribute.getName())))) {
							String value = "portBandwidth".equalsIgnoreCase(attribute.getName()) ? attribute.getValue() : attribute.getValue() + "GB" ;
							attributes.append("vDOM bundled with ").append(value).append(" cloud access");
						} else if (IPCQuoteConstants.EARLY_TERMINATION_CHARGES.equals(solutionPdfBean.getName())) {
							if(CommonConstants.ATTRIBUTE_REASON.equals(attribute.getName())) {
								attributes.append(CommonConstants.ETC_REASON).append(CommonConstants.COLON).append(CommonConstants.SPACE).append(attribute.getValue());
							} else if(CommonConstants.ATTRIBUTE_FROM_DATE.equals(attribute.getName())) {
								attributes.append(CommonConstants.COMMA).append(CommonConstants.SPACE).append(CommonConstants.ETC_PERIOD).append(CommonConstants.COLON).append(CommonConstants.SPACE).append(attribute.getValue());
							} else if (CommonConstants.ATTRIBUTE_TO_DATE.equals(attribute.getName())) {
								attributes.append(CommonConstants.SPACE).append(CommonConstants.TO_CAMEL_CASE).append(CommonConstants.SPACE).append(attribute.getValue());
							}
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
						
						if (QuoteConstants.ACCESS.getConstantCode().equalsIgnoreCase(productSolution.getOfferingName())) {
							if (!"".equals(componentPdfBean.getAttributes())) {
								componentPdfBean.setAttributes(componentPdfBean.getAttributes() + CommonConstants.COMMA + CommonConstants.SPACE);
							}
							if(quoteDetail != null || (quoteDetail == null && MACDConstants.MACD_QUOTE_TYPE.equals(cofPdfRequest.getOrderType()))) {
								componentPdfBean.setAttributes(componentPdfBean.getAttributes() + "1 Client to Site VPN is bundled, 2 Public IPs is bundled.");
							} else {
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
	
	private void identifyActionTypeAndQuantity(QuotePdfBean cofPdfRequest) {
		Map<String, SolutionPdfBean> normalizedSolutionsMap = new LinkedHashMap<>();
		Set<SolutionPdfBean> normalizedSolutions = new LinkedHashSet<>();
		Map<String, SolutionPdfBean> quoteCloudFromServiceInventoryM = new HashMap<>();
		Map<String, SolutionPdfBean> deletedSolutionPdfBeanM = new HashMap<>();
		Set<String> quoteCloudToBeDeletedS = new HashSet<>();
		
		Set<SolutionPdfBean> selectedSolutions = cofPdfRequest.getSolutions();
		selectedSolutions.forEach(selectedSolution -> {
			SolutionPdfBean normalizedSolution = null;
			
			String selectedSolutionDetailString = keyFormationBasedOnSolution(selectedSolution);
			LOGGER.info("Formed Key String {}" , selectedSolutionDetailString);
			if (MACDConstants.MACD_QUOTE_TYPE.equals(cofPdfRequest.getOrderType())) {
				if(MACDConstants.DELETE_VM.equals(cofPdfRequest.getQuoteCategory()) && !selectedSolution.isFromServiceInventory()) {
					if(selectedSolutionDetailString.contains(IpcPDFConstants.VM_CHARGES) && null != selectedSolution.getParentCloudCode()) {
						quoteCloudToBeDeletedS.add(selectedSolution.getParentCloudCode());
						return;
					} else {
						// For Early Termination Charges in DELETE_VM
						normalizedSolution = processActionTypeAndQuantityForMacd(normalizedSolutionsMap,
								selectedSolutionDetailString, selectedSolution, quoteCloudFromServiceInventoryM,
								quoteCloudToBeDeletedS);
					}
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
					if (IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME.equals(selectedSolution.getName())
							|| IPCQuoteConstants.DATABASE_LICENSE_CHARGES.equals(selectedSolution.getName())
							|| IPCQuoteConstants.DR_LICENSE_CHARGES.equals(selectedSolution.getName()) 
							|| IPCQuoteConstants.VPN_C2S_SOLUTION_NAME.equals(selectedSolution.getName())
							|| IPCQuoteConstants.VPN_S2S_SOLUTION_NAME.equals(selectedSolution.getName())) {
						normalizedSolution.setQuantity(selectedSolution.getQuantity());
						normalizedSolution.setMrc(selectedSolution.getMrc());
						normalizedSolution.setNrc(selectedSolution.getNrc());
					}
				}
			}
			LOGGER.info("selectedSolutionDetailString - {} Value : {}" , selectedSolutionDetailString, normalizedSolution.toString());
			normalizedSolutionsMap.put(selectedSolutionDetailString, normalizedSolution);
		});
		// VPN Connection Changes
		if (normalizedSolutionsMap.containsKey(IPCQuoteConstants.VPN_C2S_SOLUTION_NAME)
				&& normalizedSolutionsMap.containsKey(IPCQuoteConstants.VPN_S2S_SOLUTION_NAME)) {
			SolutionPdfBean normalizedSolutionC2S = normalizedSolutionsMap.get(IPCQuoteConstants.VPN_C2S_SOLUTION_NAME);
			SolutionPdfBean normalizedSolutionS2S = normalizedSolutionsMap.get(IPCQuoteConstants.VPN_S2S_SOLUTION_NAME);
			Double vpnMrc = 0.0;
			Double vpnNrc = 0.0;
			if (normalizedSolutionC2S.getQuantity() > 0 && normalizedSolutionS2S.getQuantity() > 0) {
				vpnMrc = normalizedSolutionC2S.getMrc() / 2;
				vpnNrc = normalizedSolutionC2S.getNrc() / 2;
				normalizedSolutionC2S.setMrc(vpnMrc / normalizedSolutionC2S.getQuantity());
				normalizedSolutionC2S.setNrc(vpnNrc / normalizedSolutionC2S.getQuantity());
				normalizedSolutionS2S.setMrc(vpnMrc / normalizedSolutionS2S.getQuantity());
				normalizedSolutionS2S.setNrc(vpnNrc / normalizedSolutionS2S.getQuantity());
			} else if (normalizedSolutionC2S.getQuantity() > 0) {
				vpnMrc = normalizedSolutionC2S.getMrc();
				vpnNrc = normalizedSolutionC2S.getNrc();
				normalizedSolutionC2S.setMrc(vpnMrc / normalizedSolutionC2S.getQuantity());
				normalizedSolutionC2S.setNrc(vpnNrc / normalizedSolutionC2S.getQuantity());
				normalizedSolutionS2S.setMrc(0.0);
				normalizedSolutionS2S.setNrc(0.0);
			} else if (normalizedSolutionS2S.getQuantity() > 0) {
				vpnMrc = normalizedSolutionS2S.getMrc();
				vpnNrc = normalizedSolutionS2S.getNrc();
				normalizedSolutionS2S.setMrc(vpnMrc / normalizedSolutionS2S.getQuantity());
				normalizedSolutionS2S.setNrc(vpnNrc / normalizedSolutionS2S.getQuantity());
				normalizedSolutionC2S.setMrc(0.0);
				normalizedSolutionC2S.setNrc(0.0);
			}
		} 
		
		normalizedSolutionsMap.keySet().forEach(normalizedSolutionKey -> {
			if(normalizedSolutionKey.startsWith(IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME) 
					|| normalizedSolutionKey.startsWith(IPCQuoteConstants.DATABASE_LICENSE_CHARGES) 
					|| normalizedSolutionKey.startsWith(IPCQuoteConstants.DR_LICENSE_CHARGES)) {
				SolutionPdfBean normalizedSolution = normalizedSolutionsMap.get(normalizedSolutionKey);
				normalizedSolution.setMrc(normalizedSolution.getMrc() / normalizedSolution.getQuantity());
				normalizedSolution.setNrc(normalizedSolution.getNrc() / normalizedSolution.getQuantity());
			}
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
						solutionPdfBean.setActionType(IPCQuoteConstants.ACTION_DELETE);
					} else {
						Integer quantity = solutionBeanFromMainMap.getQuantity();
						solutionBeanFromMainMap.setQuantity(quantity - 1);
						solutionBeanFromMainMap.setActionType(IPCQuoteConstants.ACTION_MODIFY);
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
		
		if(MACDConstants.MACD_QUOTE_TYPE.equals(cofPdfRequest.getOrderType())) {
			cofPdfRequest.setTotalMRC(0.0);
			cofPdfRequest.setTotalNRC(0.0);
		}
		normalizedSolutionsMap.forEach((detailString, normalizedSolution) -> {
			normalizedSolutions.add(normalizedSolution);
			//For MACD Orders recalculating the MRC, NRC values based on the existing elements
			if(MACDConstants.MACD_QUOTE_TYPE.equals(cofPdfRequest.getOrderType())) {
				cofPdfRequest.setTotalMRC(cofPdfRequest.getTotalMRC() + (normalizedSolution.getMrc() * normalizedSolution.getQuantity()));
				cofPdfRequest.setTotalNRC(cofPdfRequest.getTotalNRC() + (normalizedSolution.getNrc() * normalizedSolution.getQuantity()));
			}
		});
		// Upgrade / Downgrade from one VM Flavor to another - delete
		normalizedSolutions.addAll(deletedSolutionPdfBeanM.values().stream()
				.filter(x -> ((x.getActionType() != null && x.getActionType().equals(IPCQuoteConstants.ACTION_DELETE))
						|| MACDConstants.DELETE_VM.equals(cofPdfRequest.getQuoteCategory())))
				.map(v -> {
					v.setActionType(IPCQuoteConstants.ACTION_DELETE);
					return v;
				}).collect(Collectors.toList()));
		
		Map<String, DeletedVmsBean> hostNameForDltVmM = new HashMap<>();
		if (MACDConstants.DELETE_VM.equals(cofPdfRequest.getQuoteCategory())) {
			MstProductComponent prdComp = mstProductComponentRepository.findByName(IPCQuoteConstants.SOLUTION_IPC_COMMON);
            ProductAttributeMaster attr = productAttributeMasterRepository.findByName(IPCQuoteConstants.HOST_NAME);
			quoteCloudToBeDeletedS.forEach(cloudCode -> {
				// Use quoteCloud and fetch hostName from orderProductComponentAttribute
				QuoteCloud quoteCloud = quoteCloudRepository.findFirstByParentCloudCodeOrderByIdDesc(cloudCode);
				List<String> hostNameL = quoteProductComponentRepository.findByQuoteCloudIdAndProdCompIdAndAttrId(quoteCloud.getId(), prdComp.getId(), attr.getId());
				hostNameL.forEach(hostName -> {
					hostNameForDltVmM.put(cloudCode, new DeletedVmsBean(quoteCloud.getResourceDisplayName() + CommonConstants.SPACE + IPCQuoteConstants.VM, 
							hostName, cofPdfRequest.getVmDeletionDate()));
				});
			});
			cofPdfRequest.setDeletedVms(new HashSet<DeletedVmsBean>(hostNameForDltVmM.values()));
		}
		
		String term[] = cofPdfRequest.getContractTerm().split("\\s");
		if(MACDConstants.MACD_QUOTE_TYPE.equals(cofPdfRequest.getOrderType())) {
			cofPdfRequest.setTotalARC(cofPdfRequest.getTotalMRC() * 12);
			cofPdfRequest.setTotalTCV(cofPdfRequest.getTotalMRC() * Double.valueOf(term[0]));
		}
		
		cofPdfRequest.setSolutions(getSortedSolution(normalizedSolutions));
	}
	
	private String keyFormationBasedOnSolution(SolutionPdfBean selectedSolution) {
		if(selectedSolution.getName().contains(IPCQuoteConstants.DATA_TRANSFER_MIN_COMMITTMENT) || selectedSolution.getName().contains(IPCQuoteConstants.FIXED_BW)) {
			return IPCQuoteConstants.ACCESS_TYPE;
		} else if(selectedSolution.getName().contains(IPCQuoteConstants.BACKUP_SOLUTION_NAME)) {
			return IPCQuoteConstants.BACKUP_SOLUTION_NAME;
		} else if (selectedSolution.getName().contains(IPCQuoteConstants.VDOM_SOLUTION_NAME)) {
			return IPCQuoteConstants.VDOM_SOLUTION_NAME;
		} else if (selectedSolution.getName().contains(IPCQuoteConstants.VPN_C2S_SOLUTION_NAME)) {
			return IPCQuoteConstants.VPN_C2S_SOLUTION_NAME;
		} else if (selectedSolution.getName().contains(IPCQuoteConstants.VPN_S2S_SOLUTION_NAME)) {
			return IPCQuoteConstants.VPN_S2S_SOLUTION_NAME;
		} else {
			return getSolutionDetailString(selectedSolution);
		}
	}

	private Set<SolutionPdfBean> getSortedSolution(Set<SolutionPdfBean> solutionBeans) {
		List<SolutionPdfBean> unsortedSolutions = new ArrayList<>();
		unsortedSolutions.addAll(solutionBeans);
		Collections.sort(unsortedSolutions, (final SolutionPdfBean o1, final SolutionPdfBean o2) -> {
			Integer firstIndex = SOLUTIONS_NAMES.indexOf(o1.getName());
			Integer secondIndex = SOLUTIONS_NAMES.indexOf(o2.getName());
			return firstIndex.compareTo(secondIndex);
		});
		Set<SolutionPdfBean> sortedSolutions = new LinkedHashSet<>();
		sortedSolutions.addAll(unsortedSolutions);
		return sortedSolutions;
	}
	
	private SolutionPdfBean processActionTypeAndQuantityForMacd(Map<String, SolutionPdfBean> normalizedSolutionsMap,
			String selectedSolutionDetailString, SolutionPdfBean selectedSolution,
			Map<String, SolutionPdfBean> quoteCloudFromServiceInventoryM, Set<String> quoteCloudToBeDeletedL) {
		SolutionPdfBean normalizedSolution;
		if (normalizedSolutionsMap.containsKey(selectedSolutionDetailString)) {
			normalizedSolution = normalizedSolutionsMap.get(selectedSolutionDetailString);
			LOGGER.info("normalizedSolution in existing element - {}", normalizedSolution.toString());
			if (selectedSolution.isFromServiceInventory()) {
				if (IPCQuoteConstants.ACTION_ADD.equals(normalizedSolution.getActionType())) {
					normalizedSolution.setActionType(IPCQuoteConstants.ACTION_MODIFY);
				}
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcPDFConstants.VM_CHARGES)) {
					quoteCloudFromServiceInventoryM.put(selectedSolution.getCloudCode(), selectedSolution);
				}
			} else {
				if (IPCQuoteConstants.ACTION_ADD.equals(normalizedSolution.getActionType())) {
					normalizedSolution.setActionType(IPCQuoteConstants.ACTION_ADD);
				} else {
					normalizedSolution.setActionType(IPCQuoteConstants.ACTION_MODIFY);
				}
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcPDFConstants.VM_CHARGES) && null != selectedSolution.getParentCloudCode()) {
					quoteCloudToBeDeletedL.add(selectedSolution.getParentCloudCode());
				}
			}
			// except Vdom, Access Type and Backup All other Quantity Should be recalculated
			if (selectedSolutionDetailString.contains(IpcPDFConstants.VM_CHARGES) || selectedSolutionDetailString.contains(IPCQuoteConstants.HYBRID_CONNECTION_CHARGES)) {
				normalizedSolution.setQuantity(normalizedSolution.getQuantity() + 1);
			} else if (selectedSolutionDetailString.contains(IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME)
					|| selectedSolutionDetailString.contains(IPCQuoteConstants.DATABASE_LICENSE_CHARGES)
					|| selectedSolutionDetailString.contains(IPCQuoteConstants.DR_LICENSE_CHARGES)
					|| selectedSolutionDetailString.contains(IPCQuoteConstants.VPN_C2S_SOLUTION_NAME)
					|| selectedSolutionDetailString.contains(IPCQuoteConstants.VPN_S2S_SOLUTION_NAME) ) {
				normalizedSolution.setQuantity(normalizedSolution.getQuantity() + selectedSolution.getQuantity());
				normalizedSolution.setMrc(Double.sum(normalizedSolution.getMrc(), selectedSolution.getMrc()));
				normalizedSolution.setNrc(Double.sum(normalizedSolution.getNrc(), selectedSolution.getNrc()));
			}
		} else {
			normalizedSolution = new SolutionPdfBean(selectedSolution);
			LOGGER.info("normalizedSolution in newly added flow - {}", normalizedSolution.toString());
			if (IPCQuoteConstants.ADDITIONAL_IP_SOLUTION_NAME.equals(selectedSolution.getName())
					|| IPCQuoteConstants.DATABASE_LICENSE_CHARGES.equals(selectedSolution.getName())
					|| IPCQuoteConstants.DR_LICENSE_CHARGES.equals(selectedSolution.getName())
					|| IPCQuoteConstants.VPN_C2S_SOLUTION_NAME.equals(selectedSolution.getName())
					|| IPCQuoteConstants.VPN_S2S_SOLUTION_NAME.equals(selectedSolution.getName())) {
				normalizedSolution.setQuantity(selectedSolution.getQuantity());
				normalizedSolution.setMrc(selectedSolution.getMrc());
				normalizedSolution.setNrc(selectedSolution.getNrc());
			}
			if (selectedSolution.isFromServiceInventory()) {
				normalizedSolution.setActionType(IPCQuoteConstants.ACTION_NO_CHANGE);
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcPDFConstants.VM_CHARGES)) {
					quoteCloudFromServiceInventoryM.put(selectedSolution.getCloudCode(), selectedSolution);
				}
			} else {
				normalizedSolution.setActionType(IPCQuoteConstants.ACTION_ADD);
				//Changes for Upgrade/Downgrade MACD Quote and Cof
				if(selectedSolutionDetailString.contains(IpcPDFConstants.VM_CHARGES) && null != selectedSolution.getParentCloudCode()) {
					quoteCloudToBeDeletedL.add(selectedSolution.getParentCloudCode());
				}
			}
		}
		LOGGER.info("Modified Type {} and Quantity {}", normalizedSolution.getActionType(),
				normalizedSolution.getQuantity());
		return normalizedSolution;
	}

	private String getSolutionDetailString(SolutionPdfBean solution) {
		StringBuilder solutionDetailStringBuilder = new StringBuilder(solution.getName());
		solutionDetailStringBuilder.append("_").append(solution.getPricingModel());
		if (IPCQuoteConstants.DATABASE_LICENSE_CHARGES.equalsIgnoreCase(solution.getName())
				|| IPCQuoteConstants.DR_LICENSE_CHARGES.equalsIgnoreCase(solution.getName())) {
			solutionDetailStringBuilder.append("_").append(round(solution.getMrc() / solution.getQuantity()));
		} else {
			solutionDetailStringBuilder.append("_").append(solution.getMrc());
		}
		solutionDetailStringBuilder.append("_").append(solution.getNrc());
		solutionDetailStringBuilder.append("_").append(solution.getPpuRate());
		solutionDetailStringBuilder.append("_").append(solution.isHasAdditionalStorage());
		List<ComponentPdfBean> components = solution.getComponents();
		components.forEach(component -> {
			solutionDetailStringBuilder.append("_").append(component.getName());
			solutionDetailStringBuilder.append("_").append(component.getAttributes());
		});

		return solutionDetailStringBuilder.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void constructIPCDataTransferPrice(String country, QuotePdfBean cofPdfRequest) throws TclCommonException {
		String dataTransferPriceStr = (String) mqUtils.sendAndReceive(ipcPricingDataTransferQueue, country);
		Map<String, String> dataTransferPriceMap = new HashMap<>();
		if (StringUtils.isNotBlank(dataTransferPriceStr)) {
			LOGGER.info("Response from ipc dataTransfer queue {} :", dataTransferPriceStr);
			dataTransferPriceMap = (Map<String, String>) Utils.convertJsonToObject(dataTransferPriceStr, Map.class);
			if (!"USD".equalsIgnoreCase((cofPdfRequest.getBillingCurrency()))) {
				dataTransferPriceMap
						.replaceAll((key, value) -> String.valueOf(round(omsUtilService.convertCurrency("USD",
								cofPdfRequest.getBillingCurrency(), Double.parseDouble(value)))));
			}
			cofPdfRequest.setDataTransferPricingMap(dataTransferPriceMap);
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> constructIPCPricingLocationDetails(String dcLocationId) throws TclCommonException {
		String ipcPricingLocationResponse = (String) mqUtils.sendAndReceive(ipcPricingLocationQueue, dcLocationId);
		Map<String, Object> localIPCPricingLocation = new HashMap<>();
		if (StringUtils.isNotBlank(ipcPricingLocationResponse)) {
			LOGGER.info("Response from ipc location queue {} :", ipcPricingLocationResponse);
			localIPCPricingLocation = (Map<String, Object>) Utils.convertJsonToObject(ipcPricingLocationResponse,
					Map.class);
		}
		return localIPCPricingLocation;
	}
	
	public double round(double value) {
        return (double) Math.round(value * 100) / 100;
    }

	public String processCofPdf(Integer quoteId, HttpServletResponse response, Boolean nat, Boolean isApproved,
			Integer quoteToLeId,Map<String,String> cofObjectMapper) throws TclCommonException {
		String html = null;
		try {
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = ipcQuoteService.getQuoteDetails(quoteId);
			QuotePdfBean cofPdfRequest = new QuotePdfBean();
			if(cofObjectMapper != null && cofObjectMapper.containsKey("ORDER_CREATED_DATE")) {
				Date orderCreatedDate = new Date(Utils.convertStringToTimeStamp(cofObjectMapper.get("ORDER_CREATED_DATE")));
				cofPdfRequest.setOrderDate(DateUtil.convertDateToMMMString(orderCreatedDate));
				cofPdfRequest.setPresentDate(DateUtil.convertDateToTimeStamp(orderCreatedDate));
			}
 			constructVariable(quoteDetail, cofPdfRequest);
			if (nat != null) {
				cofPdfRequest.setIsNat(nat);
			}
			cofPdfRequest.setBaseUrl(appHost);
			cofPdfRequest.setIsApproved(isApproved);
			LOGGER.info("QuoteDetail {}::", Utils.convertObjectToJson(quoteDetail));
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
			if(quoteToLe.isPresent()) {
				//For Partner Term and Condition content in COF pdf
	 			if (Objects.nonNull(userInfoUtils.getUserType())
	 					&& UserType.PARTNER.toString().equalsIgnoreCase(userInfoUtils.getUserType())){
					if (PartnerConstants.SELL_WITH_CLASSIFICATION
							.equalsIgnoreCase(quoteToLe.get().getClassification())) {
						cofPdfRequest.setIsPartnerSellWith(true);
					}
	 			}
				cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
				cofPdfRequest.setContractTerm(quoteToLe.get().getTermInMonths());
				processMacdAttributes(quoteToLe, cofPdfRequest);
				
				if(cofPdfRequest.getDataTransferPricingMap() != null) {
					ipcQuoteService.updateDataTransferOverageChargesInLeAttributes(cofPdfRequest.getDataTransferPricingMap(), quoteToLe.get());
				}
			}
			
			@SuppressWarnings("unchecked")
			Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
			Context context = new Context();
			context.setVariables(variable);
			html = templateEngine.process("ipccof_template", context);
			String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";

			if (quoteToLe.isPresent()) {
				if (!isApproved) {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					PDFGenerator.createPdf(html, bos);
					byte[] outArray = bos.toByteArray();
					response.reset();
					response.setContentType(MediaType.APPLICATION_PDF_VALUE);
					response.setContentLength(outArray.length);
					response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
					response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
					FileCopyUtils.copy(outArray, response.getOutputStream());
					bos.flush();
					bos.close();
				} else {
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					PDFGenerator.createPdf(html, bos);
					if (swiftApiEnabled.equalsIgnoreCase("true")) {
						InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
						List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe.get());
						Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
								.filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_CODE
										.equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
								.findFirst();
						Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList
								.stream().filter(quoteLeAttributeValue -> LeAttributesConstants.CUSTOMER_LE_CODE
										.equalsIgnoreCase(quoteLeAttributeValue.getMstOmsAttribute().getName()))
								.findFirst();
						if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
							StoredObject storedObject = fileStorageService.uploadObject(fileName, inputStream,
									customerCodeLeVal.get().getAttributeValue(),
									customerLeCodeLeVal.get().getAttributeValue());
							String[] pathArray = storedObject.getPath().split("/");
							updateCofUploadedDetails(quoteId, quoteToLe.get().getId(), storedObject.getName(),
									pathArray[1]);
							if (cofObjectMapper != null) {
								cofObjectMapper.put("FILENAME", storedObject.getName());
								cofObjectMapper.put("OBJECT_STORAGE_PATH", pathArray[1]);
								String tempUrl=fileStorageService.getTempDownloadUrl(storedObject.getName(), 60000, pathArray[1], false);
								cofObjectMapper.put("TEMP_URL", tempUrl);
							}
						}
					} else {
						CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteDetail.getQuoteCode());
						if (cofDetails == null) {
							cofDetails = new CofDetails();
							// Get the file and save it somewhere
							String cofPath = cofAutoUploadPath + quoteDetail.getQuoteCode().toLowerCase();
							File filefolder = new File(cofPath);
							if (!filefolder.exists()) {
								filefolder.mkdirs();

							}
							String fileFullPath = cofPath + CommonConstants.RIGHT_SLASH + fileName;
							try (OutputStream outputStream = new FileOutputStream(fileFullPath)) {
								bos.writeTo(outputStream);
							}
							cofDetails.setOrderUuid(quoteDetail.getQuoteCode());
							cofDetails.setUriPath(fileFullPath);
							cofDetails.setSource(Source.AUTOMATED_COF.getSourceType());
							cofDetails.setCreatedBy(Utils.getSource());
							cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
							cofDetailsRepository.save(cofDetails);
							if (cofObjectMapper != null) {
								cofObjectMapper.put("FILE_SYSTEM_PATH", fileFullPath);
							}
						}
					}
				}

			}
		} catch (TclCommonException e) {
			LOGGER.warn("Error in Generate Cof {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		} catch (IOException | DocumentException e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}
		return html;
	}
	
	public void regenerateAndSaveCof(Integer quoteId, Integer quoteLeId, HttpServletResponse response) throws TclCommonException {
		Quote quote = quoteRepository.findByIdAndStatus(quoteId, (byte) 1);
		if (quote == null) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		Order order = orderRepository.findByQuoteAndStatus(quote, (byte) 1);
		if (order == null) {
			throw new TclCommonException(ExceptionConstants.ORDER_EMPTY, ResponseResource.R_CODE_NOT_FOUND);
		}
		Map<String, String> cofObjectMapper = new HashMap<>();
		cofObjectMapper.put("ORDER_CREATED_DATE", Utils.convertTimeStampToString(new Timestamp(order.getCreatedTime().getTime())));
		CofDetails cofDetail = cofDetailsRepository.findByOrderUuid(quote.getQuoteCode());
		if (cofDetail != null) {
			cofObjectMapper.put("FILE_SYSTEM_PATH", cofDetail.getUriPath());
		}
		for (QuoteToLe quoteLe : quote.getQuoteToLes()) {
			if(quoteLeId != quoteLe.getId()) {
				LOGGER.error("QuoteLeId does not match");
				throw new TclCommonException(ExceptionConstants.INVALID_QUOTE_TYPE, ResponseResource.R_CODE_ERROR);
			}
			if(!(IPCQuoteConstants.MIGRATION).equalsIgnoreCase(quoteLe.getQuoteType())) {
				LOGGER.error("Regenerate COF is supported only for IPC Migration Orders");
				throw new TclCommonException(ExceptionConstants.INVALID_QUOTE_TYPE, ResponseResource.R_CODE_ERROR);
			}
			
			String html = processCofPdf(quoteId, null, Boolean.TRUE, Boolean.TRUE, quoteLe.getId(), cofObjectMapper);
			
			String fileName = "Customer-Order-Form - " + quote.getQuoteCode() + ".pdf";
			
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PDFGenerator.createPdf(html, bos);
				byte[] outArray = bos.toByteArray();
				response.reset();
				response.setContentType(MediaType.APPLICATION_PDF_VALUE);
				response.setContentLength(outArray.length);
				response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
				response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
				FileCopyUtils.copy(outArray, response.getOutputStream());
				bos.flush();
				bos.close();
			} catch (IOException | DocumentException e1) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
			}
		}
	}

	private void processMacdAttributes(Optional<QuoteToLe> quoteToLe, QuotePdfBean cofPdfRequest) throws TclCommonException {
		if(quoteToLe.isPresent() && Objects.nonNull(quoteToLe.get().getQuoteType()) && 
				MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
			if (Objects.nonNull(quoteToLe.get().getTpsSfdcParentOptyId())) {
				cofPdfRequest.setSfdcOpportunityId(quoteToLe.get().getTpsSfdcParentOptyId().toString());
			}
			LOGGER.info("Quote Le Id::"+quoteToLe.get().getId());
			LOGGER.info("Quote Le Category::"+quoteToLe.get().getQuoteCategory());
			String category = getQuoteCategoryValue(quoteToLe.get().getQuoteCategory());
			LOGGER.info("Category::"+category);
			cofPdfRequest.setQuoteCategory(category);
			LOGGER.info("Retrieving serviceId from MACD");
			MacdDetail macdDetail = macdDetailRepository.findByQuoteToLeId(quoteToLe.get().getId());
			cofPdfRequest.setServiceId(macdDetail.getTpsServiceId());
			
			addPreviousOrderSolutions(cofPdfRequest);
		}else {
			identifyActionTypeAndQuantity(cofPdfRequest);
		}
	}
	
	private void addPreviousOrderSolutions(QuotePdfBean cofPdfRequest) throws TclCommonException {
		String ipcSiSolutionsQueueResponse = (String) mqUtils.sendAndReceive(ipcSiSolutionsQueue, cofPdfRequest.getServiceId());
		LOGGER.info("IPC SI Solutions Queue Response :: {}" , ipcSiSolutionsQueueResponse);
		
		List<ComponentPdfBean> vmComponents = new ArrayList<>();
		
		ProductSolutionBean[] productSolutions = Utils.convertJsonToObject(ipcSiSolutionsQueueResponse, ProductSolutionBean[].class);
		if(productSolutions != null) {
			List<ProductSolutionBean> sortedProductSolutions = Arrays.asList(productSolutions);
			sortedProductSolutions.forEach(productSolution -> {
				cofPdfRequest.getSolutions().addAll(constructSolutions(productSolution, cofPdfRequest, null, vmComponents, null, true));
			});
		}
		
		for (ComponentPdfBean componentPdfBean : vmComponents) {
			String attributes = componentPdfBean.getAttributes();
			String managedAttribute = "Unmanaged VM";
			if(cofPdfRequest.getIsVMsManaged()) {
				managedAttribute = "Managed VM";
			}
			componentPdfBean.setAttributes(attributes + CommonConstants.COMMA + CommonConstants.SPACE + managedAttribute);
		}
		
		if (cofPdfRequest.getIsDataTransferSelected()) {
			constructIPCDataTransferPrice(cofPdfRequest.getHostingCountry(), cofPdfRequest);
		}

		identifyActionTypeAndQuantity(cofPdfRequest);
	}
	
	/**
	 * Method to get quote category value
	 * @param quoteCategory
	 * @return
	 */
	public String getQuoteCategoryValue(String quoteCategory)
	{
		String category=null;
		switch(quoteCategory)
		{
			case MACDConstants.ADD_CLOUDVM_SERVICE:
				category=MACDConstants.ADD_VM;
				break;
			case MACDConstants.CONNECTIVITY_UPGRADE_SERVICE:
				category=MACDConstants.CONNECTIVITY_UPGRADE;
				break;
			case MACDConstants.ADDITIONAL_SERVICE_UPGRADE:
				category=MACDConstants.ADDITIONAL_UPGRADE;
				break;
			case MACDConstants.REQUEST_FOR_TERMINATION_SERVICE:
				category=MACDConstants.REQUEST_FOR_TERMINATION;
				break;
			case MACDConstants.UPGRADE_VM_SERVICE:
				category=MACDConstants.UPGRADE_VM;
				break;
			case MACDConstants.DELETE_VM_SERVICE:
				category=MACDConstants.DELETE_VM;
				break;
			default:
				break;
		}
		return category;
	}

	/**
	 * 
	 * updateCofUploadedDetails - this method is used to update the details related
	 * to the document uploaded to the storage container
	 * 
	 * @param quoteId
	 * @param quoteLeId
	 * @param requestId
	 * @return OmsAttachment
	 */
	public OmsAttachmentBean updateCofUploadedDetails(Integer quoteId, Integer quoteLeId, String requestId, String url)
			throws TclCommonException {
		OmsAttachmentBean omsAttachmentBean = null;
		try {
			OmsAttachment omsAttachment = null;
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if (Objects.isNull(quoteId) || Objects.isNull(quoteLeId) || Objects.isNull(requestId))
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);
				AttachmentBean attachmentBean = new AttachmentBean();
				attachmentBean.setFileName(requestId);
				attachmentBean.setPath(url);
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
				if (!quoteToLe.isPresent())
					throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR, ResponseResource.R_CODE_ERROR);

				Quote quote = quoteToLe.get().getQuote();
				Order order = orderRepository.findByQuoteAndStatus(quote, quote.getStatus());

				String attachmentrequest = Utils.convertObjectToJson(attachmentBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				Integer attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentrequest);
				LOGGER.info("Received the Attachment response with attachment Id {}",attachmentId);
				List<OmsAttachment> omsAttachmentList = omsAttachmentRepository.findByQuoteToLeAndAttachmentType(quoteToLe.get(), AttachmentTypeConstants.COF.toString());
				if (!omsAttachmentList.isEmpty()) {
					omsAttachment = omsAttachmentList.get(0);
				} else {
					omsAttachment = new OmsAttachment();
				}
				omsAttachment.setAttachmentType(AttachmentTypeConstants.COF.toString());
				omsAttachment.setErfCusAttachmentId(attachmentId);
				omsAttachment.setQuoteToLe(quoteToLe.get());
				if (order != null) {
					omsAttachment.setReferenceName(CommonConstants.ORDERS);
					omsAttachment.setReferenceId(order.getId());
					omsAttachment.setOrderToLe(order.getOrderToLes().iterator().next());
				} else {
					omsAttachment.setReferenceName(CommonConstants.QUOTES);
					omsAttachment.setReferenceId(quote.getId());
				}
				OmsAttachment omsAttach = omsAttachmentRepository.save(omsAttachment);
				LOGGER.info("Oms Attachment Saved with Id  {}",omsAttach.getId());
				omsAttachmentBean = new OmsAttachmentBean(omsAttach);

				if (order != null) {
					order.setOmsAttachment(omsAttach);
					orderRepository.save(order);
				}

			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return omsAttachmentBean;

	}

	/**
	 * constructquoteLeAttributes
	 * 
	 * @param cofPdfRequest
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructquoteLeAttributes(QuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
			throws TclCommonException {
		String role = userInfoUtils.getUserType();
		CustomerLeContactDetailBean customerLeContact = getCustomerLeContact(quoteLe);
		quoteLe.getLegalAttributes().forEach(quoteLeAttrbutes -> {
			try {
				if (quoteLeAttrbutes.getMstOmsAttribute() != null
						&& quoteLeAttrbutes.getMstOmsAttribute().getName() != null) {
					switch (quoteLeAttrbutes.getMstOmsAttribute().getName()) {
					case LeAttributesConstants.LEGAL_ENTITY_NAME:
						cofPdfRequest.setCustomerContractingEntity(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.LE_STATE_GST_NO:
						if (StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue())
								|| quoteLeAttrbutes.getAttributeValue().trim().equals("-")) {
							cofPdfRequest.setCustomerGstNumber(PDFConstants.NO_REGISTERED_GST);
						} else {
							cofPdfRequest.setCustomerGstNumber(quoteLeAttrbutes.getAttributeValue());
						}
						break;
					case LeAttributesConstants.Vat_Number:
						if (StringUtils.isEmpty(quoteLeAttrbutes.getAttributeValue())
								|| quoteLeAttrbutes.getAttributeValue().trim().equals("-")) {
							cofPdfRequest.setCustomerVatNumber(PDFConstants.NA);
						} else {
							cofPdfRequest.setCustomerVatNumber(quoteLeAttrbutes.getAttributeValue());
						}
						break;
					case LeAttributesConstants.CONTACT_NAME:
//						if(quoteLeAttrbutes.getAttributeValue() != null && !quoteLeAttrbutes.getAttributeValue().isEmpty()) {
//							cofPdfRequest.setCustomerContactName(quoteLeAttrbutes.getAttributeValue());
//						} else 
						if (customerLeContact != null && customerLeContact.getName() != null) {
							cofPdfRequest.setCustomerContactName(customerLeContact.getName());
						}
						break;
					case LeAttributesConstants.CONTACT_NO:
//						if(quoteLeAttrbutes.getAttributeValue() != null && !quoteLeAttrbutes.getAttributeValue().isEmpty()) {
//							cofPdfRequest.setCustomerContactNumber(quoteLeAttrbutes.getAttributeValue());
//						} else 
						if (customerLeContact != null && customerLeContact.getName() != null) {
							cofPdfRequest.setCustomerContactNumber(customerLeContact.getMobilePhone());
						}
						break;
					case LeAttributesConstants.CONTACT_EMAIL:
//						if(quoteLeAttrbutes.getAttributeValue() != null && !quoteLeAttrbutes.getAttributeValue().isEmpty()) {
//							cofPdfRequest.setCustomerEmailId(quoteLeAttrbutes.getAttributeValue());
//						} else 
						if (customerLeContact != null && customerLeContact.getName() != null) {
							cofPdfRequest.setCustomerEmailId(customerLeContact.getEmailId());
						}
						break;
					case LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY:
						cofPdfRequest.setSupplierContactEntity(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.LE_NAME:
						cofPdfRequest.setSupplierAccountManager(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.LE_CONTACT:
						cofPdfRequest.setSupplierContactNumber(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.LE_EMAIL:
						cofPdfRequest.setSupplierEmailId(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_METHOD:
						cofPdfRequest.setBillingMethod(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_FREQUENCY:
						cofPdfRequest.setBillingFreq(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_CURRENCY:
						cofPdfRequest.setBillingCurrency(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.PAYMENT_TERM:
						cofPdfRequest.setPaymentTerm(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.PAYMENT_CURRENCY:
						cofPdfRequest.setPaymentCurrency(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.INVOICE_METHOD:
						cofPdfRequest.setInvoiceMethod(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.BILLING_CONTACT_ID:
						constructBillingInformations(cofPdfRequest, quoteLeAttrbutes);
						break;
					case LeAttributesConstants.CUSTOMER_CONTRACTING_ENTITY:
						constructCustomerLocationDetails(cofPdfRequest, quoteLeAttrbutes);
						break;
					case LeAttributesConstants.MSA:
						cofPdfRequest.setIsMSA(true);
						break;
					case LeAttributesConstants.SERVICE_SCHEDULE:
						cofPdfRequest.setIsSS(true);
						break;
					case LeAttributesConstants.RECURRING_CHARGE_TYPE:
						if (quoteLeAttrbutes.getAttributeValue().equalsIgnoreCase("ARC")) {
							cofPdfRequest.setIsArc(true);
						} else {
							cofPdfRequest.setIsArc(false);
						}
						break;
					case "Cross Border With Holding Tax":
						cofPdfRequest.setWithHoldingTax(Double.parseDouble(quoteLeAttrbutes.getAttributeValue()));
						break;
					case LeAttributesConstants.PO_NUMBER:
						cofPdfRequest.setPoNumber(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.PO_DATE:
						cofPdfRequest.setPoDate(quoteLeAttrbutes.getAttributeValue());
						break;
					case LeAttributesConstants.VM_DELETION_DATE:
						cofPdfRequest.setVmDeletionDate(quoteLeAttrbutes.getAttributeValue());
						break;
					default:
						break;
					}
				}
				if (quoteLe.getTermInMonths() != null) {
					cofPdfRequest.setContractTerm(quoteLe.getTermInMonths());
				}
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		});

		if (role != null) {
			LOGGER.info("IPC USER ROLE :" + role);
			if (cofPdfRequest.getBillingPaymentsName() == null
					&& !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				cofPdfRequest.setBillingPaymentsName(cofPdfRequest.getCustomerContactName());
			}
			if (cofPdfRequest.getBillingContactNumber() == null
					&& !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				cofPdfRequest.setBillingContactNumber(cofPdfRequest.getCustomerContactNumber());
			}
			if (cofPdfRequest.getBillingEmailId() == null
					&& !role.equalsIgnoreCase(UserType.INTERNAL_USERS.toString())) {
				cofPdfRequest.setBillingEmailId(cofPdfRequest.getCustomerEmailId());
			}
		}

	}
	
	public CustomerLeContactDetailBean getCustomerLeContact(QuoteToLeBean quoteToLe)
            throws TclCommonException, IllegalArgumentException {
		if (quoteToLe.getCustomerLegalEntityId() != null) {
			LOGGER.info("MDC Filter token value in before Queue call getCustomerLeContact {} :",
                MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName, String.valueOf(quoteToLe.getCustomerLegalEntityId()));
			CustomerLeContactDetailBean[] customerLeContacts = (CustomerLeContactDetailBean[]) Utils.convertJsonToObject(response,CustomerLeContactDetailBean[].class);
	        if(customerLeContacts.length > 0) {
	        	return customerLeContacts[customerLeContacts.length - 1];
	        }
		}
		return null;
	}

	/**
	 * constructCustomerLocationDetails
	 * 
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructCustomerLocationDetails(QuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call constructCustomerLocationDetails {} :");
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(quoteLeAttrbutes.getAttributeValue()));
		if (StringUtils.isNotBlank(locationResponse)) {
			AddressDetail addressDetail = (AddressDetail) Utils.convertJsonToObject(locationResponse,
					AddressDetail.class);
			LOGGER.info("Printing Address detail for debugging : {} :", addressDetail);
			StringBuilder customerAddressBuilder = new StringBuilder(addressDetail.getAddressLineOne());
			if(addressDetail.getAddressLineTwo() != null && !("").equals(addressDetail.getAddressLineTwo())) {
				customerAddressBuilder.append(" ").append(addressDetail.getAddressLineTwo());
			}
			if(addressDetail.getLocality() != null && !("").equals(addressDetail.getLocality())) {
				customerAddressBuilder.append(" ").append(addressDetail.getLocality());
			}
			cofPdfRequest.setCustomerAddress(customerAddressBuilder.toString());
			cofPdfRequest.setCustomerState(addressDetail.getState());
			cofPdfRequest.setCustomerCity(addressDetail.getCity());
			cofPdfRequest.setCustomerCountry(addressDetail.getCountry());
			cofPdfRequest.setCustomerPincode(addressDetail.getPincode());
			if(addressDetail.getCountry().equalsIgnoreCase(PDFConstants.INDIA) || addressDetail.getCountry().equalsIgnoreCase(PDFConstants.SINGAPORE)) {
				cofPdfRequest.setIsGst(true);
				cofPdfRequest.setIsVat(false);
			} else if(addressDetail.getCountry().equalsIgnoreCase(PDFConstants.HONGKONG)) {
				cofPdfRequest.setIsGst(false);
				cofPdfRequest.setIsVat(false);
			}
		}
	}

	/**
	 * constructBillingInformations
	 *
	 * @param cofPdfRequest
	 * @param quoteLeAttrbutes
	 * @throws TclCommonException
	 */
	private void constructBillingInformations(QuotePdfBean cofPdfRequest, LegalAttributeBean quoteLeAttrbutes)
			throws TclCommonException {
		if (StringUtils.isNotBlank(quoteLeAttrbutes.getAttributeValue())) {
			LOGGER.info("MDC Filter token value in before Queue call constructBillingInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String billingContactResponse = (String) mqUtils.sendAndReceive(billingContactQueue,
					String.valueOf(quoteLeAttrbutes.getAttributeValue()));
			if (StringUtils.isNotBlank(billingContactResponse)) {
				BillingContact billingContact = (BillingContact) Utils.convertJsonToObject(billingContactResponse,
						BillingContact.class);
				cofPdfRequest.setBillingAddress(billingContact.getBillAddr());
				cofPdfRequest.setBillingPaymentsName(billingContact.getFname() + " " + billingContact.getLname());
				cofPdfRequest.setBillingContactNumber(
						StringUtils.isNotBlank(billingContact.getPhoneNumber()) ? billingContact.getPhoneNumber()
								: billingContact.getMobileNumber());
				cofPdfRequest.setBillingEmailId(billingContact.getEmailId());
			}

		}
	}

	/**
	 * constructSupplierInformations
	 * 
	 * @param cofPdfRequest
	 * @param quoteLe
	 * @throws TclCommonException
	 */
	private void constructSupplierInformations(QuotePdfBean cofPdfRequest, QuoteToLeBean quoteLe)
			throws TclCommonException {
		if (quoteLe.getSupplierLegalEntityId() != null) {
			LOGGER.info("MDC Filter token value in before Queue call constructSupplierInformations {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String supplierResponse = (String) mqUtils.sendAndReceive(suplierLeQueue,
					String.valueOf(quoteLe.getSupplierLegalEntityId()));
			if (StringUtils.isNotBlank(supplierResponse)) {
				SPDetails spDetails = (SPDetails) Utils.convertJsonToObject(supplierResponse, SPDetails.class);
				cofPdfRequest.setSupplierAddress(spDetails.getNoticeAddress());
				if (spDetails.getGstnDetails() != null && !spDetails.getGstnDetails().isEmpty()) {
					cofPdfRequest.setSupplierGstnNumber(spDetails.getGstnDetails());
					cofPdfRequest.setSupplierVatNumber(PDFConstants.NA);
				} else {
					cofPdfRequest.setSupplierGstnNumber(PDFConstants.NO_REGISTERED_GST);
					cofPdfRequest.setSupplierVatNumber(PDFConstants.NA);
				}
				AddressDetail addressDetail = getAddressDetailBySupplierId(spDetails.getEntityName());
				if (addressDetail != null) {
					if (PDFConstants.INDIA.equalsIgnoreCase(addressDetail.getCountry())
							|| PDFConstants.SINGAPORE.equalsIgnoreCase(addressDetail.getCountry())) {
						cofPdfRequest.setIsGstSup(true);
						cofPdfRequest.setIsVatSup(false);
					} else if (PDFConstants.HONGKONG.equalsIgnoreCase(addressDetail.getCountry())) {
						cofPdfRequest.setIsGstSup(false);
						cofPdfRequest.setIsVatSup(false);
					} else {
						cofPdfRequest.setIsGstSup(false);
						cofPdfRequest.setIsVatSup(false);
					}
				}
			}
		}
	}

	private AddressDetail getAddressDetailBySupplierId(String supplierEntityName) throws TclCommonException {
		String locationResponse = (String) mqUtils.sendAndReceive(locationQueue, String.valueOf(supplierEntityName));
		return (AddressDetail) Utils.convertJsonToObject(locationResponse, AddressDetail.class);
	}

	public void processDocusign(Integer quoteId, Integer quoteLeId, Boolean nat, String emailId, String name, ApproverListBean approvers) throws TclCommonException {
		try {
			String html = null;
			LOGGER.debug("Processing cof PDF for quote id {}", quoteId);
			QuoteBean quoteDetail = ipcQuoteService.getQuoteDetails(quoteId);
			if (docuSignService.validateDeleteDocuSign(quoteDetail.getQuoteCode(), emailId)) {
				QuotePdfBean cofPdfRequest = new QuotePdfBean();
				constructVariable(quoteDetail, cofPdfRequest);
				if (approvers != null && approvers.getApprovers() != null && !approvers.getApprovers().isEmpty()) {
					showReviewerDataInCof(approvers.getApprovers(), cofPdfRequest);
				}

				// MULTI-DOCUSIGNER
				if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
					constructCustomerDataInCof(approvers.getCustomerSigners(), cofPdfRequest);
				}
				if (nat != null) {
					cofPdfRequest.setIsNat(nat);
				}
				cofPdfRequest.setBaseUrl(appHost);
				cofPdfRequest.setIsDocusign(true);
				if (StringUtils.isNotBlank(emailId)) {
					cofPdfRequest.setCustomerContactNumber(CommonConstants.HYPHEN);
					String customerLeContact = (String) mqUtils.sendAndReceive(customerLeContactQueue, emailId);
					if (StringUtils.isNotBlank(customerLeContact)) {
						CustomerContactDetails customerContactDetails = (CustomerContactDetails) Utils
								.convertJsonToObject(customerLeContact, CustomerContactDetails.class);
						name = customerContactDetails.getName();
						emailId = customerContactDetails.getEmailId();
						cofPdfRequest.setCustomerContactNumber(customerContactDetails.getMobilePhone() != null
								? customerContactDetails.getMobilePhone()
								: customerContactDetails.getOtherPhone());
					}
					cofPdfRequest.setCustomerContactName(name);
					cofPdfRequest.setCustomerEmailId(emailId);
				}
				Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);
				if(quoteToLe.isPresent()) {
					cofPdfRequest.setOrderType(quoteToLe.get().getQuoteType());
					processMacdAttributes(quoteToLe, cofPdfRequest);
					
					if(cofPdfRequest.getDataTransferPricingMap() != null) {
						ipcQuoteService.updateDataTransferOverageChargesInLeAttributes(cofPdfRequest.getDataTransferPricingMap(), quoteToLe.get());
					}
				}
				
				@SuppressWarnings("unchecked")
				Map<String, Object> variable = objectMapper.convertValue(cofPdfRequest, Map.class);
				Context context = new Context();
				context.setVariables(variable);
				html = templateEngine.process("ipccof_template", context);
				String fileName = "Customer-Order-Form - " + quoteDetail.getQuoteCode() + ".pdf";
				CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
				
				if(cofPdfRequest!=null && cofPdfRequest.getApproverEmail1()!=null) {
					List<String> approver1SignedDate = new ArrayList<>();
					approver1SignedDate.add(PDFConstants.APPROVER_1_SIGNED_DATE);
					commonDocusignRequest.setApproverDateAnchorStrings(approver1SignedDate);
				}

				setAnchorStrings(approvers, commonDocusignRequest);
				
				commonDocusignRequest.setFileName(fileName);
				commonDocusignRequest.setPdfHtml(Base64.getEncoder().encodeToString(html.getBytes()));
				commonDocusignRequest.setQuoteId(quoteId);
				commonDocusignRequest.setQuoteLeId(quoteLeId);
				if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
					commonDocusignRequest.setSubject("Please sign this cof document!!!");
				} else {
					commonDocusignRequest.setSubject(mqHostName+":::Test:::Please sign this cof document!!!");
				}
				commonDocusignRequest.setToName(name);
				commonDocusignRequest.setToEmail(emailId);
				if(Objects.nonNull(approvers)) {
					commonDocusignRequest.setApprovers(approvers.getApprovers());
					approvers.getCcEmails().stream().forEach(ccEmail->commonDocusignRequest.getCcEmails().put(ccEmail.getName(),ccEmail.getEmail()));
				} else {
					commonDocusignRequest.setApprovers(new ArrayList<>());
				}
				docuSignService.auditInTheDocusign(quoteDetail.getQuoteCode(),name, emailId, null,approvers);
				LOGGER.info("Approvers --> {}, customer signers --> {}",approvers.getApprovers(),approvers.getCustomerSigners());
				LOGGER.info("Approvers size --> {}, customer signers size --> {}",approvers.getApprovers().size(),approvers.getCustomerSigners().size());
				
				if (Objects.nonNull(approvers) && !approvers.getApprovers().isEmpty()) {
					String reviewerName=approvers.getApprovers().stream().findFirst().get().getName();
					String reviewerEmail=approvers.getApprovers().stream().findFirst().get().getEmail();
					LOGGER.info("Case 1 : Reviewer 1 name -->  {} , Email --> {}", reviewerName, reviewerEmail);
					commonDocusignRequest.setToName(reviewerName);
					commonDocusignRequest.setToEmail(reviewerEmail);
					commonDocusignRequest.setType(DocuSignStage.REVIEWER1.toString());
					commonDocusignRequest.setDocumentId("3");
				}
				else if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners()) && approvers.getApprovers().isEmpty()) {
					Approver customerSignerValue = approvers.getCustomerSigners().stream().findFirst().get();
					commonDocusignRequest.setToName(customerSignerValue.getName());
					commonDocusignRequest.setToEmail(customerSignerValue.getEmail());
					LOGGER.info("Case 2 : Signer 1 name -->  {} , Email --> {}", customerSignerValue.getName(), customerSignerValue.getEmail());
					commonDocusignRequest.setType(DocuSignStage.CUSTOMER1.toString());
					commonDocusignRequest.setDocumentId("5");
				}
				else if(approvers.getApprovers().isEmpty() && approvers.getCustomerSigners().isEmpty()){
					commonDocusignRequest.setToName(name);
					commonDocusignRequest.setToEmail(emailId);
					commonDocusignRequest.setType(DocuSignStage.CUSTOMER.toString());
					commonDocusignRequest.setDocumentId("1");
					LOGGER.info("Case 3 : Customer name -->  {} , Email --> {}", name, emailId);
				}
				LOGGER.info("MDC Filter token value in before Queue call processDocusign {} :");
				mqUtils.send(docusignRequestQueue, Utils.convertObjectToJson(commonDocusignRequest));
				if (!MACDConstants.MACD_QUOTE_TYPE.equalsIgnoreCase(quoteToLe.get().getQuoteType())) {
                    setStage(quoteToLe.get());
                }
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	// set quote stage and save
    private void setStage(QuoteToLe quoteLe) {
        if (!quoteLe.getStage().equals(QuoteStageConstants.ORDER_FORM.getConstantCode())) {
            quoteLe.setStage(QuoteStageConstants.ORDER_FORM.getConstantCode());
            quoteToLeRepository.save(quoteLe);
            LOGGER.info("Quote stage changed to Order Form stage");
        }
    }

	public String processApprovedCof(Integer orderId, Integer orderLeId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = null;
		try {
			Optional<Order> orderEntity = orderRepository.findById(orderId);
			if (orderEntity.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					List<OmsAttachment> omsAttachmentsList = omsAttachmentRepository
							.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, orderId,
									AttachmentTypeConstants.COF.toString());
					if(omsAttachmentsList.isEmpty()) {
						Quote quoteEntity = orderEntity.get().getQuote();
						if(quoteEntity != null) {
							omsAttachmentsList = omsAttachmentRepository
									.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteEntity.getId(),
											AttachmentTypeConstants.COF.toString());
						}
					}
					if (!omsAttachmentsList.isEmpty()) {
						tempDownloadUrl = downloadCofFromStorageContainer(null, null, orderId, orderLeId,null);
					}
				} else {
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(orderEntity.get().getOrderCode());
					if (cofDetails != null) {
						processDownloadCof(response, cofDetails);
					}
				}
			}
		} catch (Exception e1) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	public String downloadCofFromStorageContainer(Integer quoteId, Integer quoteLeId, Integer orderId,
			Integer orderLeId,Map<String,String> cofObjectMapper) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		Order order = null;
		try {
			OmsAttachment omsAttachment = null;
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if ((Objects.isNull(quoteId) && Objects.isNull(quoteLeId))
						&& (Objects.isNull(orderId) && Objects.isNull(orderLeId)))
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

				if (!Objects.isNull(quoteLeId)) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);

					if (quoteToLe.isPresent()) {
						order = orderRepository.findByQuoteAndStatus(quoteToLe.get().getQuote(),
								quoteToLe.get().getQuote().getStatus());
						if (order != null) {
							omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
							if (omsAttachment == null) {
								omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
							}

						} else {
							omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
						}
					}
				} else if (!Objects.isNull(orderId) && !Objects.isNull(orderLeId)) {
					Optional<Order> orderOpt = orderRepository.findById(orderId);
					if (orderOpt.isPresent()) {
						order = orderOpt.get();
						omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
					}
				}
				if (omsAttachment != null) {
					String response = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
							String.valueOf(omsAttachment.getErfCusAttachmentId())));
					if (StringUtils.isNotBlank(response)) {
						AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response,
								AttachmentBean.class);
						if (cofObjectMapper != null) {
							cofObjectMapper.put("FILENAME", attachmentBean.getFileName());
							cofObjectMapper.put("OBJECT_STORAGE_PATH", attachmentBean.getPath());
							String tempUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(), 60000,
									attachmentBean.getPath(), false);
							cofObjectMapper.put("TEMP_URL", tempUrl);
						} else {
							tempDownloadUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(),
									Long.parseLong(tempDownloadUrlExpiryWindow), attachmentBean.getPath(), false);
						}

					}
				}
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;

	}

	private OmsAttachment getOmsAttachmentBasedOnOrder(Order order, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.ORDERS, order.getId(),
						AttachmentTypeConstants.COF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		return omsAttachment;
	}
	
	private OmsAttachment getOmsAttachmentBasedOnQuote(Integer quoteId, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteId,
						AttachmentTypeConstants.COF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		return omsAttachment;
	}

	private void processDownloadCof(HttpServletResponse response, CofDetails cofDetails) throws IOException {
		Path path = Paths.get(cofDetails.getUriPath());
		String fileName = "Customer-Order-Form - " + cofDetails.getOrderUuid() + ".pdf";
		response.reset();
		response.setContentType(MediaType.APPLICATION_PDF_VALUE);
		response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
		response.setHeader(PDFConstants.CONTENT_DISPOSITION, ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
		Files.copy(path, response.getOutputStream());
		// flushes output stream
		response.getOutputStream().flush();
	}

	public void downloadCofPdf(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		try {
			LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteEntity.get().getQuoteCode());
				if (cofDetails != null) {
					processDownloadCof(response, cofDetails);
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}

	public TempUploadUrlInfo uploadCofPdf(Integer quoteId, MultipartFile file, Integer quoteToLeId)
			throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = null;
		try {
			LOGGER.debug("Processing cof upload PDF for quote id {}", quoteId);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
					if (quoteToLe.isPresent()) {
						List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
								.findByQuoteToLe(quoteToLe.get());
						Optional<QuoteLeAttributeValue> customerCodeLeVal = quoteLeAttributesList.stream()
								.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
										.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE))
								.findFirst();
						Optional<QuoteLeAttributeValue> customerLeCodeLeVal = quoteLeAttributesList
								.stream().filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
										.getName().equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE))
								.findFirst();
						if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent())
							tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
									Long.parseLong(tempUploadUrlExpiryWindow),
									customerCodeLeVal.get().getAttributeValue(),
									customerLeCodeLeVal.get().getAttributeValue(),false);
					}
				} else {
					if(file == null)
						throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR, ResponseResource.R_CODE_FORBIDDEN_ERROR); 
					// Get the file and save it somewhere
					String cofPath = cofManualUploadPath + quoteEntity.get().getQuoteCode().toLowerCase();
					File filefolder = new File(cofPath);
					if (!filefolder.exists()) {
						filefolder.mkdirs();

					}
					Path path = Paths.get(cofPath);
					Path filePath = path.resolve(file.getOriginalFilename());
					if (filePath != null)
						Files.deleteIfExists(filePath);
					Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteEntity.get().getQuoteCode());
					if (cofDetails == null) {
						cofDetails = new CofDetails();
						cofDetails.setOrderUuid(quoteEntity.get().getQuoteCode());
						cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
						cofDetails.setSource(Source.MANUAL_COF.getSourceType());
						cofDetails.setCreatedBy(Utils.getSource());
						cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
						cofDetailsRepository.save(cofDetails);
					} else {
						cofDetails.setUriPath(path.toString() + "/" + file.getOriginalFilename());
						cofDetails.setSource(Source.MANUAL_COF.getSourceType());
						cofDetails.setCreatedBy(Utils.getSource());
						cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
						cofDetailsRepository.save(cofDetails);
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempUploadUrlInfo;
	}

	private QuotePrice getComponentQuotePrice(ComponentDetail component) {
		return quotePriceRepository.findByReferenceIdAndReferenceName(String.valueOf(component.getComponentId()), QuoteConstants.COMPONENTS.toString());
	}
	
	public void showReviewerDataInCof(List<Approver> approvers, QuotePdfBean cofPdfRequest)
			throws TclCommonException {
		cofPdfRequest.setShowReviewerTable(true);
		constructApproverInfo(cofPdfRequest, approvers);
	}
	
	public void constructApproverInfo(QuotePdfBean cofPdfRequest,List<Approver> approvers)
			throws TclCommonException {
		if(Objects.nonNull(approvers)&&!approvers.isEmpty())
		{
			if(approvers.size()==1) {
				Approver approver1 = approvers.get(0);
				cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName())?approver1.getName():"NA");
				cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail())?approver1.getEmail():"NA");
				cofPdfRequest.setApproverName2("NA");
				cofPdfRequest.setApproverEmail2("NA");
				cofPdfRequest.setApproverSignedDate2("NA");
				
			}
			else if(approvers.size()==2) {
				Approver approver1 = approvers.get(0);
				Approver approver2 = approvers.get(1);
				
				cofPdfRequest.setApproverName1(Objects.nonNull(approver1.getName())?approver1.getName():"NA");
				cofPdfRequest.setApproverName2(Objects.nonNull(approver2.getName())?approver2.getName():"NA");
				cofPdfRequest.setApproverEmail1(Objects.nonNull(approver1.getEmail())?approver1.getEmail():"NA");
				cofPdfRequest.setApproverEmail2(Objects.nonNull(approver2.getEmail())?approver2.getEmail():"NA");
			}
		}
	}
	
	private void constructCustomerDataInCof(List<Approver> customerSigners, QuotePdfBean cofPdfRequest) {
		cofPdfRequest.setShowCustomerSignerTable(true);
		if (!CollectionUtils.isEmpty(customerSigners)) {
			if (customerSigners.size() == 1) {
				Approver approver1 = customerSigners.get(0);
				cofPdfRequest.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				cofPdfRequest.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				cofPdfRequest.setCustomerName2("NA");
				cofPdfRequest.setCustomerEmail2("NA");
				cofPdfRequest.setCustomerSignedDate2("NA");

			} else if (customerSigners.size() == 2) {
				Approver approver1 = customerSigners.get(0);
				Approver approver2 = customerSigners.get(1);

				cofPdfRequest.setCustomerName1(Objects.nonNull(approver1.getName()) ? approver1.getName() : "NA");
				cofPdfRequest.setCustomerName2(Objects.nonNull(approver2.getName()) ? approver2.getName() : "NA");
				cofPdfRequest.setCustomerEmail1(Objects.nonNull(approver1.getEmail()) ? approver1.getEmail() : "NA");
				cofPdfRequest.setCustomerEmail2(Objects.nonNull(approver2.getEmail()) ? approver2.getEmail() : "NA");
			}
		}
	}
	
	private void setAnchorStrings(ApproverListBean approvers, CommonDocusignRequest commonDocusignRequest) {
		if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
			commonDocusignRequest.setAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_SIGNATURE));
			commonDocusignRequest.setDateSignedAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_SIGNED_DATE));
			commonDocusignRequest.setCustomerNameAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_NAME));
			LOGGER.info("Inside setAnchorStrings If Block");
		} else {
			commonDocusignRequest.setAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_SIGNATURE));
			commonDocusignRequest.setDateSignedAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_SIGNED_DATE));
			commonDocusignRequest.setCustomerNameAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_NAME));
			LOGGER.info("Inside setAnchorStrings else Block");
		}
	}
}
