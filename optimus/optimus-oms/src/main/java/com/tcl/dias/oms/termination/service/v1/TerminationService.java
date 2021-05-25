package com.tcl.dias.oms.termination.service.v1;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.common.beans.*;
import com.tcl.dias.oms.service.OmsAttachmentService;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.javaswift.joss.model.StoredObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.DocumentException;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.serviceinventory.beans.SIServiceDetailDataBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.beans.QuoteBean;
import com.tcl.dias.oms.beans.QuoteProductComponentBean;
import com.tcl.dias.oms.beans.TerminatedServicesBean;
import com.tcl.dias.oms.beans.TerminationQuoteDetailsBean;
import com.tcl.dias.oms.constants.ChargeableItemConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.constants.MACDConstants;
import com.tcl.dias.oms.constants.QuoteConstants;
import com.tcl.dias.oms.dashboard.constants.DashboardConstant;
import com.tcl.dias.oms.entity.entities.AttachmentsAudit;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.MstProductComponent;
import com.tcl.dias.oms.entity.entities.MstProductFamily;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderToLe;
import com.tcl.dias.oms.entity.entities.ProductAttributeMaster;
import com.tcl.dias.oms.entity.entities.ProductSolution;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteIllSite;
import com.tcl.dias.oms.entity.entities.QuoteIllSiteToService;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteNplLink;
import com.tcl.dias.oms.entity.entities.QuotePrice;
import com.tcl.dias.oms.entity.entities.QuoteProductComponent;
import com.tcl.dias.oms.entity.entities.QuoteProductComponentsAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteSiteServiceTerminationDetails;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.AttachmentsAuditRepository;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.IllSiteRepository;
import com.tcl.dias.oms.entity.repository.MstProductComponentRepository;
import com.tcl.dias.oms.entity.repository.NplLinkRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.OrderToLeRepository;
import com.tcl.dias.oms.entity.repository.ProductAttributeMasterRepository;
import com.tcl.dias.oms.entity.repository.ProductSolutionRepository;
import com.tcl.dias.oms.entity.repository.QuoteIllSiteToServiceRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuotePriceRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentRepository;
import com.tcl.dias.oms.entity.repository.QuoteProductComponentsAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteSiteServiceTerminationDetailsRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.ill.service.v1.IllQuoteService;
import com.tcl.dias.oms.macd.utils.MACDUtils;
import com.tcl.dias.oms.npl.beans.NplQuoteBean;
import com.tcl.dias.oms.npl.service.v1.NplQuoteService;
import com.tcl.dias.oms.pdf.beans.TRFBean;
import com.tcl.dias.oms.pdf.beans.TerminationNTFBean;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.sfdc.constants.SFDCConstants;
import com.tcl.dias.oms.sfdc.service.OmsSfdcService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

@Service
public class TerminationService {
	
	public static final Logger LOGGER = LoggerFactory.getLogger(TerminationService.class);

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;
	
	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	QuoteSiteServiceTerminationDetailsRepository quoteSiteServiceTerminationDetailsRepository;
	
	@Autowired
	IllQuoteService illQuoteService;
	
	@Autowired
	NplQuoteService nplQuoteService;
	
	@Autowired
	NotificationService notificationService;

	@Autowired
	MACDUtils macdUtils;
	
	@Autowired
	MQUtils mqUtils;	
	
	@Autowired
	OmsSfdcService omsSfdcService;
	
	@Autowired
	QuoteRepository quoteRepository;
	
	@Value("${rabbitmq.customer.contact.details.queue}")
	 String customerLeContactQueueName;		

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${temp.download.url.expiryWindow}")
	String tempDownloadUrlExpiryWindow;
	

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Value("${attachment.requestId.queue}")
	String attachmentRequestIdQueue;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	SpringTemplateEngine templateEngine;

	 @Autowired
	 OrderRepository orderRepository;

	@Value("${cof.auto.upload.path}")
	String cofAutoUploadPath;
	
	@Value("${rabbitmq.customer.contact.by.email.queue}")
	String customerContactEmailQueue;
	
	@Value("${rabbitmq.customer.account.manager.email}")
	String customerAccountManagerEmail;			
			
	@Value("${rabbitmq.customer.account.manager}")
	String customerAccountManagerName;			
	
	@Autowired
	protected CofDetailsRepository cofDetailsRepository;	
	
	@Autowired
	MstProductComponentRepository mstProductComponentRepository;
	
	@Autowired
	QuoteProductComponentRepository quoteProductComponentRepository;
	
	@Autowired
	QuotePriceRepository quotePriceRepository;
	
	@Autowired
	ProductSolutionRepository productSolutionRepository;
	
	@Autowired
	ProductAttributeMasterRepository productAttributeMasterRepository;
	
	@Autowired
	QuoteProductComponentsAttributeValueRepository quoteProductComponentsAttributeValueRepository;
	
	@Autowired
	QuoteIllSiteToServiceRepository quoteSiteToServiceRepository;
	
	@Autowired
	NplLinkRepository nplLinkRepository;
	
	@Autowired
	IllSiteRepository illSiteRepository;
	
	@Autowired
	AttachmentsAuditRepository attachAuditRepository;
	
	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;	
	
	@Autowired
	OrderToLeRepository orderToLeRepository;
	
	@Value("${rabbitmq.owner.email.business.segment.queue}")
	String ownerNameBasedOnBusinessSegment;	
	
	@Autowired
	AttachmentsAuditRepository attachmentsAuditRepository;

	@Autowired
	OmsAttachmentService omsAttachmentService;

	public void sendMailAknowledgmentforTermination(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		Map<String, Object> map = populateMapforTerminationNotification(quoteId, quoteToLeId);
		notificationService.notifyTerminationAcknowlegment(map);

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		quoteToLe.get().setIsAckMailTriggered(CommonConstants.ACTIVE);
		quoteToLe.get().setAckMailTriggeredDate(new Date());
		quoteToLeRepository.save(quoteToLe.get());
	}
	
	public void sendInitiationMailforTermination(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		Map<String, Object> map = populateMapforTerminationNotification(quoteId, quoteToLeId);
		notificationService.notifyTerminationInitiation(map);
	}

	public void sendEtcRevisionMailForTermination(Integer quoteId, Integer quoteToLeId, String serviceId) throws TclCommonException {
		LOGGER.info("inside method sendEtcRevisionMailForTermination");
		Map<String, Object> map = populateSiteMapforTerminationNotification(quoteId, quoteToLeId, serviceId);
		notificationService.notifyEtcRevision(map);
	}
	public void sendEtcApplicableMailForTermination(Integer quoteId, Integer quoteToLeId, String serviceId) throws TclCommonException {
		LOGGER.info("inside method sendEtcApplicableMailForTermination");
		Map<String, Object> map = populateSiteMapforTerminationNotification(quoteId, quoteToLeId, serviceId);
		notificationService.notifyEtcApplicability(map);
	}
	
	public OmsAttachmentBean generateAndUploadTRFToStorage(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		Context context = new Context();
		OmsAttachmentBean omsAttachmentBean = null;
		String html = null;
		
		Map<String, Object> variable = populateMapforTerminationNotification(quoteId, quoteToLeId);
		LOGGER.info("Popuated Map object : {}", variable);
		context.setVariables(variable);
		html = templateEngine.process("mtrf_template", context);
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		String quoteCode = quoteToLe.get().getQuote().getQuoteCode();
		String fileName = null;
		List<AttachmentsAudit> attachmentsAuditListTRF = attachmentsAuditRepository.findByQuoteToLeIdAndAttachmentType(quoteToLe.get().getId(), "TRF");
		if(attachmentsAuditListTRF != null && !attachmentsAuditListTRF.isEmpty()) {
			fileName = "TRFForm" + quoteCode + "_version_" + (attachmentsAuditListTRF.size()+1) + ".pdf";
		} else {
		fileName = "TRFForm" + quoteCode + ".pdf";
		}
		LOGGER.info(fileName+"\n"+html);

		if (quoteToLe.isPresent()) {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PDFGenerator.createPdf(html, bos);
				byte[] outArray = bos.toByteArray();
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					LOGGER.info("uploading TRF generated to object storage");
					InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
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
					if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
						StoredObject storedObject = fileStorageService.uploadOverwriteeObjectWithFileName(fileName, inputStream,
								customerCodeLeVal.get().getAttributeValue(),
								customerLeCodeLeVal.get().getAttributeValue());
						String[] pathArray = storedObject.getPath().split("/");
						omsAttachmentBean = updateCofUploadedDetails(quoteId, quoteToLe.get().getId(), storedObject.getName(),
								pathArray[1]);

						Optional<Quote> quote = quoteRepository.findById(quoteId);

						OmsAttachBean omsAttachBean = new OmsAttachBean();
						omsAttachBean.setAttachmentId(omsAttachmentBean.getErfCusAttachmentId());
						omsAttachBean.setAttachmentType(omsAttachmentBean.getAttachmentType());
						omsAttachBean.setQouteLeId(quoteToLeId);
						omsAttachBean.setReferenceId(omsAttachmentBean.getReferenceId());
						omsAttachBean.setReferenceName(omsAttachmentBean.getReferenceName());
						omsAttachBean.setFileName(fileName);
						LOGGER.info("OmsAttachBean : {}",omsAttachBean.toString());
						omsAttachmentService.persistAttachmentAudit(omsAttachBean ,quote.get());
						LOGGER.info("After persisting Attachment Audit");
					}
				} else {
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteToLe.get().getQuote().getQuoteCode());
					if (cofDetails == null) {
						cofDetails = new CofDetails();
						// Get the file and save it somewhere
						String cofPath = cofAutoUploadPath + quoteToLe.get().getQuote().getQuoteCode().toLowerCase();
						File filefolder = new File(cofPath);
						if (!filefolder.exists()) {
							filefolder.mkdirs();
	
						}
						String fileFullPath = cofPath + CommonConstants.RIGHT_SLASH + fileName;
						try (OutputStream outputStream = new FileOutputStream(fileFullPath)) {
							bos.writeTo(outputStream);
						}
						cofDetails.setOrderUuid(quoteToLe.get().getQuote().getQuoteCode());
						cofDetails.setUriPath(fileFullPath);
						cofDetails.setSource(Source.AUTOMATED_COF.getSourceType());
						cofDetails.setCreatedBy(Utils.getSource());
						cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
						cofDetailsRepository.save(cofDetails);
					}
				}
			}catch (TclCommonException e) {
				LOGGER.warn("Error in Generate Cof {}", ExceptionUtils.getStackTrace(e));
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			} catch (IOException | DocumentException e1) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
			}
		}
		return omsAttachmentBean;
		
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> populateMapforTerminationNotification(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		Map<String, Object> map = new HashMap<>();
		TerminationNTFBean trfNTFBean = new TerminationNTFBean();
		List<TRFBean> trfBeanList = new ArrayList<>();
		String productName = null;

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			productName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
			//String custId = quoteToLe.get().getQuote().getCustomer().getErfCusCustomerId().toString();
			
			try {
			if (productName.startsWith("IAS")|| productName.startsWith("GVPN")) {
				trfBeanList = retrieveTerminationDetailsForIASorGVPN(quoteId);
			} else if (productName.startsWith("NPL")|| productName.startsWith("NDE")) {
				trfBeanList = retrieveTerminationDetailsforNPLorNDE(quoteId);
			}
			LOGGER.info("Retrieved Termination Bean List : {}", trfBeanList.toString());
			List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe.get());
			Optional<QuoteLeAttributeValue> custLeName = quoteLeAttributesList.stream()
					.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
							.equalsIgnoreCase(LeAttributesConstants.LEGAL_ENTITY_NAME))
					.findFirst();
			Optional<QuoteLeAttributeValue> tataBillingEntity = quoteLeAttributesList
					.stream().filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
							.getName().equalsIgnoreCase(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					.findFirst();
			
			List<String> customerIds = new ArrayList<>();
			String acctManager = null;
			String acctManagerEmail = null;
			customerIds.add(quoteToLe.get().getQuote().getCustomer().getErfCusCustomerId().toString());
			customerIds.add(quoteToLe.get().getErfCusCustomerLegalEntityId().toString());
			String request = customerIds.stream().collect(Collectors.joining(","));
			String customerLeContacts = (String) mqUtils.sendAndReceive(ownerNameBasedOnBusinessSegment, request);
			if(customerLeContacts != null) {
				LOGGER.info("customerLeContacts {}", Utils.convertObjectToJson(customerLeContacts));
				CustomerContactDetails ownerNameDetails = (CustomerContactDetails) Utils
					.convertJsonToObject(customerLeContacts, CustomerContactDetails.class);
				LOGGER.info("Owner name {}", ownerNameDetails.getEmailId());			
				//String acctManager = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
				//String acctManagerEmail = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
				acctManager = ownerNameDetails.getName();
				acctManagerEmail = ownerNameDetails.getEmailId();
			}

					
			//Prepare Notification Object
			trfNTFBean.setNotificationDate(DateUtil.convertDateToSlashString(new Date()));
			trfNTFBean.setOrderType(quoteToLe.get().getQuoteType());
			trfNTFBean.setTataCommRefId(quoteToLe.get().getQuote().getQuoteCode());
			trfNTFBean.setAccountManagers(acctManager);
			trfNTFBean.setAccountManagersEmail(acctManagerEmail);
			trfNTFBean.setCustomerAccountName(quoteToLe.get().getQuote().getCustomer().getCustomerName());
			trfNTFBean.setLegalEntityName(custLeName.isPresent()?custLeName.get().getAttributeValue():"");
			trfNTFBean.setTataBillingEntity(tataBillingEntity.isPresent()?tataBillingEntity.get().getAttributeValue():"");
			trfNTFBean.setCurrency(quoteToLe.get().getCurrencyCode());
			
			trfNTFBean.setIsMulticircuit(false);
			if(quoteToLe.get().getIsMultiCircuit() == 1)
				trfNTFBean.setIsMulticircuit(true);
			else
				trfNTFBean.setIsMulticircuit(false);
			if(trfBeanList!= null && !trfBeanList.isEmpty()) {
				//Retrieve first bean to set general notification data
				TRFBean firstTRFBean = trfBeanList.get(0);
				String orderDesc = (custLeName.isPresent()?custLeName.get().getAttributeValue(): "")+"_";
				
				
				CustomerLeContactDetailBean communicationRecipientLeContact = getCustomerLeContactByCustomerEmailId(firstTRFBean.getCommunicationRecipient());
				if(Objects.nonNull(communicationRecipientLeContact)) {
					LOGGER.info("Customer Contact Details : {}", communicationRecipientLeContact.toString());
					trfNTFBean.setCustName(communicationRecipientLeContact.getName());
					trfNTFBean.setCustomerEmailId(firstTRFBean.getCommunicationRecipient());
					trfNTFBean.setCustomerPhoneNumber(communicationRecipientLeContact.getMobilePhone());
				}
				
				if(Boolean.TRUE.equals(trfNTFBean.getIsMulticircuit())) {
					orderDesc = orderDesc+"Bulk termination_"+trfBeanList.size()+" ckts";
					trfNTFBean.setTrfBean(trfBeanList);
					BigDecimal cummulativeETC = trfBeanList.stream().filter(Objects::nonNull).map(TRFBean::getEtcAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
					Double cummulativePrevMRC = trfBeanList.stream().filter(Objects::nonNull).mapToDouble(TRFBean::getPreviousMRC).sum();
					trfNTFBean.setPreviousMRC(cummulativePrevMRC);
					trfNTFBean.setEtcAmount(cummulativeETC);
					trfNTFBean.setServiceId(String.valueOf(trfBeanList.size()));
					trfNTFBean.setEtcApplicability(MACDConstants.AS_PER_ANNEXURE);
				} else {
					orderDesc = orderDesc+firstTRFBean.getServiceId()+"_"+quoteToLe.get().getQuoteType();
					trfNTFBean.setPreviousMRC(firstTRFBean.getPreviousMRC());
					trfNTFBean.setEtcAmount(firstTRFBean.getEtcAmount());
					trfNTFBean.setServiceId(firstTRFBean.getServiceId());
					trfNTFBean.setEtcApplicability(firstTRFBean.getEtcApplicability());
				}
				trfNTFBean.setOrderDesc(orderDesc);
				trfNTFBean.setTypeOfService(firstTRFBean.getTypeOfService());
				trfNTFBean.setCommunicationRecipient(firstTRFBean.getCommunicationRecipient());
				trfNTFBean.setCustomerSuccessManagers(firstTRFBean.getCustomerSuccessManagers());
				if (Objects.nonNull(firstTRFBean.getCustomerSuccessManagersEmail()))
					trfNTFBean.setCustomerSuccessManagersEmail(firstTRFBean.getCustomerSuccessManagersEmail());
				else
					firstTRFBean.setCustomerSuccessManagersEmail(MACDConstants.TD_SUPPORT_EMAIL);
				trfNTFBean.setTerminationSubtype(firstTRFBean.getTerminationSubtype());
				trfNTFBean.setCustomerRequestDate(firstTRFBean.getCustomerRequestDate());
				trfNTFBean.setDateOfRequestedTermination(firstTRFBean.getDateOfRequestedTermination());
				trfNTFBean.setEffectiveDateOfChange(firstTRFBean.getEffectiveDateOfChange());
				//trfNTFBean.setEtcApplicability(firstTRFBean.getEtcApplicability());
				trfNTFBean.setEtcRemark(firstTRFBean.getEtcRemark());
				trfNTFBean.setReasonForTermination(firstTRFBean.getReasonForTermination());
				trfNTFBean.setSubTerminationReason(firstTRFBean.getSubTerminationReason());
				if (CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit())) {
					trfNTFBean.setaEndCity(firstTRFBean.getaEndCity());
					trfNTFBean.setbEndCity(firstTRFBean.getbEndCity());
					trfNTFBean.setCircuitSpeed(firstTRFBean.getCircuitSpeed());
				}
				
			}

			map = objectMapper.convertValue(trfNTFBean, Map.class);
			
		} catch (TclCommonException e) {
			LOGGER.error("Error populating termination notification data : ", e.getMessage(), e);
			throw new TclCommonException(e);
		}
		} else {
			LOGGER.error("QuoteToLE not present - cannot send termination notification ");
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> populateSiteMapforTerminationNotification(Integer quoteId, Integer quoteToLeId, String serviceId) throws TclCommonException {
		Map<String, Object> map = new HashMap<>();
		TerminationNTFBean trfNTFBean = new TerminationNTFBean();
		List<TRFBean> trfBeanList = new ArrayList<>();
		String productName = null;

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			productName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
			try {
				if (productName.startsWith("IAS")|| productName.startsWith("GVPN")) {
					trfBeanList = retrieveTerminationDetailsForIASorGVPN(quoteId);
				} else if (productName.startsWith("NPL")|| productName.startsWith("NDE")) {
					trfBeanList = retrieveTerminationDetailsforNPLorNDE(quoteId);
				}
				LOGGER.info("Retrieved Termination Bean List : {}", trfBeanList.toString());
				List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe.get());
				Optional<QuoteLeAttributeValue> custLeName = quoteLeAttributesList.stream()
						.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
								.equalsIgnoreCase(LeAttributesConstants.LEGAL_ENTITY_NAME))
						.findFirst();
				Optional<QuoteLeAttributeValue> tataBillingEntity = quoteLeAttributesList
						.stream().filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
								.getName().equalsIgnoreCase(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
						.findFirst();

				List<String> customerIds = new ArrayList<>();
				String acctManager = null;
				String acctManagerEmail = null;
				customerIds.add(quoteToLe.get().getQuote().getCustomer().getErfCusCustomerId().toString());
				customerIds.add(quoteToLe.get().getErfCusCustomerLegalEntityId().toString());
				String request = customerIds.stream().collect(Collectors.joining(","));
				String customerLeContacts = (String) mqUtils.sendAndReceive(ownerNameBasedOnBusinessSegment, request);
				if(customerLeContacts != null) {
					LOGGER.info("customerLeContacts {}", Utils.convertObjectToJson(customerLeContacts));
					CustomerContactDetails ownerNameDetails = (CustomerContactDetails) Utils
							.convertJsonToObject(customerLeContacts, CustomerContactDetails.class);
					LOGGER.info("Owner name {}", ownerNameDetails.getEmailId());
					acctManager = ownerNameDetails.getName();
					acctManagerEmail = ownerNameDetails.getEmailId();
				}

				//Prepare Notification Object
				trfNTFBean.setNotificationDate(DateUtil.convertDateToSlashString(new Date()));
				trfNTFBean.setOrderType(quoteToLe.get().getQuoteType());
				trfNTFBean.setTataCommRefId(quoteToLe.get().getQuote().getQuoteCode());
				trfNTFBean.setAccountManagers(acctManager);
				trfNTFBean.setAccountManagersEmail(acctManagerEmail);
				trfNTFBean.setCustomerAccountName(quoteToLe.get().getQuote().getCustomer().getCustomerName());
				trfNTFBean.setLegalEntityName(custLeName.isPresent()?custLeName.get().getAttributeValue():"");
				trfNTFBean.setTataBillingEntity(tataBillingEntity.isPresent()?tataBillingEntity.get().getAttributeValue():"");
				trfNTFBean.setCurrency(quoteToLe.get().getCurrencyCode());
				if(trfBeanList!= null && !trfBeanList.isEmpty()) {
					//Retrieve data corresponding to the specific service id
					TRFBean firstTRFBean = trfBeanList.stream().filter(trfBean -> (serviceId.equalsIgnoreCase(trfBean.getServiceId()))).findFirst().get();
					String orderDesc = (custLeName.isPresent()?custLeName.get().getAttributeValue(): "")+"_";

					LOGGER.info("FirstTrfBean  :: {}",firstTRFBean);

					orderDesc = orderDesc+firstTRFBean.getServiceId()+"_"+quoteToLe.get().getQuoteType();
					BigDecimal etcAmount = firstTRFBean.getEtcAmount();
					Double previousMrc = firstTRFBean.getPreviousMRC();
					trfNTFBean.setPreviousMRC(previousMrc);
					trfNTFBean.setEtcAmount(etcAmount);
					trfNTFBean.setServiceId(serviceId);
					trfNTFBean.setEtcApplicability(firstTRFBean.getEtcApplicability());
					trfNTFBean.setOrderDesc(orderDesc);
					trfNTFBean.setTypeOfService(firstTRFBean.getTypeOfService());
					trfNTFBean.setCommunicationRecipient(firstTRFBean.getCommunicationRecipient());
					trfNTFBean.setCustomerSuccessManagers(firstTRFBean.getCustomerSuccessManagers());
					if (Objects.nonNull(firstTRFBean.getCustomerSuccessManagersEmail()))
						trfNTFBean.setCustomerSuccessManagersEmail(firstTRFBean.getCustomerSuccessManagersEmail());
					else
						firstTRFBean.setCustomerSuccessManagersEmail(MACDConstants.TD_SUPPORT_EMAIL);
					trfNTFBean.setTerminationSubtype(firstTRFBean.getTerminationSubtype());
					trfNTFBean.setCustomerRequestDate(firstTRFBean.getCustomerRequestDate());
					trfNTFBean.setDateOfRequestedTermination(firstTRFBean.getDateOfRequestedTermination());
					trfNTFBean.setEffectiveDateOfChange(firstTRFBean.getEffectiveDateOfChange());
					trfNTFBean.setEtcRemark(firstTRFBean.getEtcRemark());
					trfNTFBean.setReasonForTermination(firstTRFBean.getReasonForTermination());
					trfNTFBean.setSubTerminationReason(firstTRFBean.getSubTerminationReason());
					trfNTFBean.setaEndCity(firstTRFBean.getaEndCity());
					trfNTFBean.setbEndCity(firstTRFBean.getbEndCity());
					trfNTFBean.setCircuitSpeed(firstTRFBean.getCircuitSpeed());
				}

				map = objectMapper.convertValue(trfNTFBean, Map.class);

			} catch (TclCommonException e) {
				LOGGER.error("Error populating termination site notification data : ", e.getMessage(), e);
				throw new TclCommonException(e);
			}
		} else {
			LOGGER.error("QuoteToLE not present - cannot send termination  site notification ");
		}
		return map;
	}
	
	private List<TRFBean> retrieveTerminationDetailsForIASorGVPN(Integer quoteId) throws TclCommonException {
		List<TRFBean> trfBeanList = new ArrayList<>();
		QuoteBean quoteDetail = illQuoteService.getQuoteDetails(quoteId, null, false, null, null);
		if (quoteDetail != null) {
			quoteDetail.getLegalEntities().stream().forEach(quoteToLeBean->{
				quoteToLeBean.getProductFamilies().stream().forEach(prodFamilyBean->{
					prodFamilyBean.getSolutions().stream().filter(Objects::nonNull).forEach(solutionBean->{
						solutionBean.getSites().stream().filter(Objects::nonNull).forEach(siteBean-> {
							siteBean.getQuoteSiteServiceTerminationsBean().stream().filter(Objects::nonNull).forEach(terminationDetailBean->{
								TRFBean trfBean = new TRFBean();
								if(terminationDetailBean!=null) {
									trfBean.setCommunicationRecipient(terminationDetailBean.getCommunicationReceipient());
									trfBean.setDateOfRequestedTermination(DateUtil.convertDateToSlashString(terminationDetailBean.getRequestedDateForTermination()));
									trfBean.setCustomerRequestDate(DateUtil.convertDateToSlashString(terminationDetailBean.getCustomerMailReceivedDate()));
									trfBean.setEffectiveDateOfChange(DateUtil.convertDateToSlashString(terminationDetailBean.getEffectiveDateOfChange()));
									trfBean.setCustomerSuccessManagers(terminationDetailBean.getCsmNonCsmName());
									trfBean.setCustomerSuccessManagersEmail(terminationDetailBean.getCsmNonCsmEmail());
									if(CommonConstants.BACTIVE.equals(terminationDetailBean.getEtcApplicable())) {
										trfBean.setEtcApplicability(CommonConstants.YES);
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceId(siteBean.getSiteId());
										Optional<QuoteProductComponent> etcChargeBean	= quoteProductComponentList.stream().filter(component -> (ChargeableItemConstants.ETC_CHARGES.equalsIgnoreCase(component.getMstProductComponent().getName())
												&& siteBean.getLinkType().equalsIgnoreCase(component.getType()))).findFirst();
										List<QuotePrice> quotePriceList = quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(), String.valueOf(etcChargeBean.get().getId()));
										if(quotePriceList != null && !quotePriceList.isEmpty()) {
											if(quotePriceList.get(0) != null && (quotePriceList.get(0).getEffectiveNrc() != null)) {
													trfBean.setEtcAmount(new BigDecimal(quotePriceList.get(0).getEffectiveNrc()));
											} else {
												trfBean.setEtcAmount(new BigDecimal(0.0));
											}
										} else {
											trfBean.setEtcAmount(new BigDecimal(0.0));
										}
									} else {
										trfBean.setEtcApplicability(CommonConstants.NO);
										trfBean.setEtcAmount(new BigDecimal(0.0));
									}
									trfBean.setReasonForTermination(terminationDetailBean.getReasonForTermination());
									trfBean.setServiceId(terminationDetailBean.getServiceId());
									trfBean.setSubTerminationReason(terminationDetailBean.getTerminationSubtype());
									trfBean.setTerminationSubtype(terminationDetailBean.getTerminationSubtype());
									trfBean.setTypeOfService(prodFamilyBean.getProductName());
									trfBean.setEtcRemark(terminationDetailBean.getTerminationRemarks());
									SIServiceDetailDataBean sIServiceDetailDataBean = null;
									try {
										sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(terminationDetailBean.getServiceId());
										if (sIServiceDetailDataBean!=null) {
											LOGGER.info("Response service details {}", sIServiceDetailDataBean.toString());
											trfBean.setAccountManagers(sIServiceDetailDataBean.getAccountManager());
											trfBean.setPreviousMRC(sIServiceDetailDataBean.getMrc());
											trfBean.setaEndCity(sIServiceDetailDataBean.getSourceCity());
											trfBean.setbEndCity(sIServiceDetailDataBean.getDestinationCity());
											trfBean.setCircuitSpeed(sIServiceDetailDataBean.getPortBw()+" " + sIServiceDetailDataBean.getPortBwUnit());
										}
									} catch (Exception e) {
										LOGGER.info("Error when fetching service details {}", e);
										throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
									}
								}
								trfBeanList.add(trfBean);
							});
						});
					});
					});
				});
		}
		return trfBeanList;
	}
	
	private List<TRFBean> retrieveTerminationDetailsforNPLorNDE(Integer quoteId) throws TclCommonException {
		List<TRFBean> trfBeanList = new ArrayList<>();
		NplQuoteBean nplQuoteDetail = nplQuoteService.getQuoteDetails(quoteId, null, false);
		if (nplQuoteDetail != null) {
		nplQuoteDetail.getLegalEntities().stream().forEach(quoteToLeBean->{
			quoteToLeBean.getProductFamilies().stream().forEach(prodFamilyBean->{
				prodFamilyBean.getSolutions().stream().filter(Objects::nonNull).forEach(solutionBean->{
					if((solutionBean.getOfferingName() != null) && CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solutionBean.getOfferingName())) {
						solutionBean.getCrossConnectSite().stream().forEach(siteBean-> {
							siteBean.getQuoteSiteServiceTerminationsBean().stream().forEach(terminationDetailBean->{
							TRFBean trfBean = new TRFBean();
							if(terminationDetailBean!=null) {
								trfBean.setCommunicationRecipient(terminationDetailBean.getCommunicationReceipient());
								trfBean.setDateOfRequestedTermination(DateUtil.convertDateToSlashString(terminationDetailBean.getRequestedDateForTermination()));
								trfBean.setCustomerRequestDate(DateUtil.convertDateToSlashString(terminationDetailBean.getCustomerMailReceivedDate()));
								trfBean.setEffectiveDateOfChange(DateUtil.convertDateToSlashString(terminationDetailBean.getEffectiveDateOfChange()));
								trfBean.setCustomerSuccessManagers(terminationDetailBean.getCsmNonCsmName());
								trfBean.setCustomerSuccessManagersEmail(terminationDetailBean.getCsmNonCsmEmail());
								if(CommonConstants.BACTIVE.equals(terminationDetailBean.getEtcApplicable())) {
									trfBean.setEtcApplicability(CommonConstants.YES);
									List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceId(siteBean.getSiteId());
									Optional<QuoteProductComponent> etcChargeBean	= quoteProductComponentList.stream().filter(component -> (ChargeableItemConstants.ETC_CHARGES.equalsIgnoreCase(component.getMstProductComponent().getName()))).findFirst();
									List<QuotePrice> quotePriceList = quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(), String.valueOf(etcChargeBean.get().getId()));
									if(quotePriceList != null && !quotePriceList.isEmpty()) {
										if(quotePriceList.get(0) != null && (quotePriceList.get(0).getEffectiveNrc() != null)) {
												trfBean.setEtcAmount(new BigDecimal(quotePriceList.get(0).getEffectiveNrc()));
										} else {
											trfBean.setEtcAmount(new BigDecimal(0.0));
										}
									} else {
										trfBean.setEtcAmount(new BigDecimal(0.0));
									}
								} else {
									trfBean.setEtcApplicability(CommonConstants.NO);
									trfBean.setEtcAmount(new BigDecimal(0));
								}
								trfBean.setReasonForTermination(terminationDetailBean.getReasonForTermination());
								trfBean.setServiceId(terminationDetailBean.getServiceId());
								trfBean.setSubTerminationReason(terminationDetailBean.getTerminationSubtype());
								trfBean.setTerminationSubtype(terminationDetailBean.getTerminationSubtype());
								trfBean.setTypeOfService(prodFamilyBean.getProductName());
								trfBean.setEtcRemark(terminationDetailBean.getTerminationRemarks());
								SIServiceDetailDataBean sIServiceDetailDataBean = null;
								try {
									sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(terminationDetailBean.getServiceId());
									if (sIServiceDetailDataBean!=null) {
										LOGGER.info("Cross Connect Response service details {}", sIServiceDetailDataBean.toString());
										trfBean.setAccountManagers(sIServiceDetailDataBean.getAccountManager());
										trfBean.setPreviousMRC(sIServiceDetailDataBean.getMrc());
										trfBean.setaEndCity(sIServiceDetailDataBean.getSourceCity());
										trfBean.setbEndCity(sIServiceDetailDataBean.getDestinationCity());
										trfBean.setCircuitSpeed(sIServiceDetailDataBean.getPortBw()+" " + sIServiceDetailDataBean.getPortBwUnit());
									}
								} catch (Exception e) {
									LOGGER.info("Error when fetching service details {}", e);
									throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
								}
							}
							trfBeanList.add(trfBean);
							});
						});
					} else {
						solutionBean.getLinks().stream().forEach(linkBean-> {
							linkBean.getQuoteSiteServiceTerminationsBean().stream().forEach(terminationDetailLinkBean->{
								TRFBean trfBean = new TRFBean();
								if (terminationDetailLinkBean!= null) {
									trfBean.setCommunicationRecipient(terminationDetailLinkBean.getCommunicationReceipient());
									trfBean.setDateOfRequestedTermination(DateUtil.convertDateToSlashString(terminationDetailLinkBean.getRequestedDateForTermination()));
									trfBean.setCustomerRequestDate(DateUtil.convertDateToSlashString(terminationDetailLinkBean.getCustomerMailReceivedDate()));
									trfBean.setEffectiveDateOfChange(DateUtil.convertDateToSlashString(terminationDetailLinkBean.getEffectiveDateOfChange()));
									trfBean.setCustomerSuccessManagers(terminationDetailLinkBean.getCsmNonCsmName());
									trfBean.setCustomerSuccessManagersEmail(terminationDetailLinkBean.getCsmNonCsmEmail());
									if(CommonConstants.BACTIVE.equals(terminationDetailLinkBean.getEtcApplicable())) {
										trfBean.setEtcApplicability(CommonConstants.YES);
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceId(linkBean.getId());
										Optional<QuoteProductComponent> etcChargeBean	= quoteProductComponentList.stream().filter(component -> (ChargeableItemConstants.ETC_CHARGES.equalsIgnoreCase(component.getMstProductComponent().getName()))).findFirst();
										List<QuotePrice> quotePriceList = quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(), String.valueOf(etcChargeBean.get().getId()));
										if(quotePriceList != null && !quotePriceList.isEmpty()) {
											if(quotePriceList.get(0) != null && (quotePriceList.get(0).getEffectiveNrc() != null)) {
													trfBean.setEtcAmount(new BigDecimal(quotePriceList.get(0).getEffectiveNrc()));
											} else {
												trfBean.setEtcAmount(new BigDecimal(0.0));
											}
										} else {
											trfBean.setEtcAmount(new BigDecimal(0.0));
										}
									} else {
										trfBean.setEtcApplicability(CommonConstants.NO);
										trfBean.setEtcAmount(new BigDecimal(0));
									}
									trfBean.setReasonForTermination(terminationDetailLinkBean.getReasonForTermination());
									trfBean.setServiceId(terminationDetailLinkBean.getServiceId());
									trfBean.setSubTerminationReason(terminationDetailLinkBean.getTerminationSubtype());
									trfBean.setTerminationSubtype(terminationDetailLinkBean.getTerminationSubtype());
									trfBean.setTypeOfService(prodFamilyBean.getProductName());
									trfBean.setEtcRemark(terminationDetailLinkBean.getTerminationRemarks());
									List<SIServiceDetailDataBean> sIServiceDetailDataBean = null;
									try {
										sIServiceDetailDataBean = macdUtils.getServiceDetailNPLTermination(terminationDetailLinkBean.getServiceId());
										if (sIServiceDetailDataBean!=null) { //To be updated based on link info
											LOGGER.info("NPL Response service details {}", sIServiceDetailDataBean.toString());
											trfBean.setAccountManagers(sIServiceDetailDataBean.get(0).getAccountManager());
											trfBean.setPreviousMRC(sIServiceDetailDataBean.get(0).getMrc());
											if("SiteA".equalsIgnoreCase(sIServiceDetailDataBean.get(0).getSiteType())){
												trfBean.setaEndCity(sIServiceDetailDataBean.get(0).getSourceCity());
											} else {
												trfBean.setbEndCity(sIServiceDetailDataBean.get(0).getSourceCity());
											}
											
											if("SiteA".equalsIgnoreCase(sIServiceDetailDataBean.get(1).getSiteType())){
												trfBean.setaEndCity(sIServiceDetailDataBean.get(1).getSourceCity());
											} else {
												trfBean.setbEndCity(sIServiceDetailDataBean.get(1).getSourceCity());
											}
											
											trfBean.setCircuitSpeed(sIServiceDetailDataBean.get(0).getPortBw()+" " + sIServiceDetailDataBean.get(0).getPortBwUnit());
										}
									} catch (Exception e) {
										LOGGER.info("Error when fetching service details {}", e);
										throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
									}
								}
								trfBeanList.add(trfBean);
							});
						});
					}
				});
			});		
		});
		}
		return trfBeanList;
	}
	
	private CustomerLeContactDetailBean getCustomerLeContactByLegalEntityId(QuoteToLe quoteToLe)
			throws TclCommonException, IllegalArgumentException {
		if (quoteToLe.getErfCusCustomerLegalEntityId() != null) {
			LOGGER.info("Customer LE Contact called {}", quoteToLe.getErfCusCustomerLegalEntityId());
			String response = (String) mqUtils.sendAndReceive(customerLeContactQueueName,
					String.valueOf(quoteToLe.getErfCusCustomerLegalEntityId()));
			if (Objects.nonNull(response)) {
			CustomerLeContactDetailBean[] customerLeContacts = (CustomerLeContactDetailBean[]) Utils
					.convertJsonToObject(response, CustomerLeContactDetailBean[].class);
				return customerLeContacts[0];
			} else
				return null;
		} else {
			return null;
		}

	}	
	
	public CustomerLeContactDetailBean getCustomerLeContactByCustomerEmailId(String customerEmail) throws TclCommonException {
		Approver customerBean = new Approver();
		try {
			LOGGER.info("Fetching customer contact for Email : {}", customerEmail);
			LOGGER.info("MDC Filter token value in before Queue call getCustomerLeContactByCustomerEmailId {} :", MDC.get(CommonConstants.MDC_TOKEN_KEY));
			String response = (String) mqUtils.sendAndReceive(customerContactEmailQueue, customerEmail);
			if (Objects.nonNull(response)) {
				CustomerLeContactDetailBean[] customerLeContacts = (CustomerLeContactDetailBean[]) Utils
					.convertJsonToObject(response, CustomerLeContactDetailBean[].class);
				return customerLeContacts[0];
			} 
		} catch (Exception e) {
			LOGGER.error("error in processing customerContactEmailQueue queue{}", e);
		}
		return null;
	}	
	
	public void processTerminationQuoteStageToVerbalAgreement() throws TclCommonException {
		LOGGER.info("Terminations : Inside Auto SFDC Stage Movement to Verbal Agreement");
		LocalDate termWaitPeriodDate = LocalDate.now().minusDays(MACDConstants.QUOTE_WAIT_PERIOD_IN_DAYS);
		Date reqDate = Date.from(termWaitPeriodDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		LOGGER.info("Termination : Stage will be auto moved to Verbal Aggreement for quotes older than : {} ", reqDate);
		
		List<Map<String, Object>> terminationEligibleQuoteDetails = null;
		List<TerminationQuoteDetailsBean> terminationEligibleQuoteDetailsList = new ArrayList<>();
		
		terminationEligibleQuoteDetails = quoteRepository.findEligibleTerminationQuotesForSFDCVerbalAggreementStageMovement(reqDate, MACDConstants.TERMINATION_SERVICE, MACDConstants.TERMINATION_REQUEST_RECEIVED, CommonConstants.BACTIVE);
		LOGGER.info("Query Parameter for fetching eligible quotes for stage movement : {} - {} - {} - {}", reqDate.toString(), MACDConstants.TERMINATION_SERVICE,  MACDConstants.TERMINATION_REQUEST_RECEIVED, CommonConstants.BACTIVE);
		terminationEligibleQuoteDetailsList = getTerminationQuoteDetailsList(terminationEligibleQuoteDetails);
		terminationEligibleQuoteDetailsList.stream().filter(Objects::nonNull).forEach(terminationSrvDetail -> {
			LOGGER.info("Termination Service Detail Response : {}", terminationSrvDetail);
			Boolean updateStageForCurrentQuote = false;
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(terminationSrvDetail.getQuoteToLeId());
			if (terminationSrvDetail.getSalesTaskResponse() != null) {
				TerminationNegotiationResponse termintationNegoResponse = null;
				try {
					termintationNegoResponse = Utils.convertJsonToObject(terminationSrvDetail.getSalesTaskResponse(), TerminationNegotiationResponse.class);
					if(termintationNegoResponse.getNegotiationResponse().equalsIgnoreCase("NOT RETAINED")) {
						updateStageForCurrentQuote = true;
					}
				} catch (TclCommonException e1) {
					LOGGER.error("Error retrieving Sale Task Response : {}", e1.getMessage(), e1);
				}
			} else {
				updateStageForCurrentQuote = true;
			}
			if (quoteToLe.isPresent() && Boolean.TRUE.equals(updateStageForCurrentQuote)) {
				try {
					if(quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit())) {
					omsSfdcService.processUpdateOpportunity(new Date(), terminationSrvDetail.getTpsSFDCOptyId(),
							SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe.get());
					} else {
						List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteToLe.get().getTpsSfdcOptyId());
						if(optyList != null && !optyList.isEmpty()) {
						omsSfdcService.processUpdateOptyDummy(quoteToLe.get(), optyList.get(0).getServiceRefId(), String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()), new Date(), quoteToLe.get().getTpsSfdcOptyId(),
								SFDCConstants.VERBAL_AGREEMENT_STAGE);
						}
					}
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
		}); 
	}	
	
	public void triggerTerminationQuoteStageToVerbalAgreement(String quoteCode) throws TclCommonException {
		LOGGER.info("Termination : Trigger SFDC Stage Movement to Verbal Agreement");
		LOGGER.info("Termination : Stage will be moved to Verbal Aggreement for quote : {} ", quoteCode);
		
		List<Map<String, Object>> terminationEligibleQuoteDetails = null;
		List<TerminationQuoteDetailsBean> terminationEligibleQuoteDetailsList = new ArrayList<>();
		
		terminationEligibleQuoteDetails = quoteRepository.findTerminationQuoteByQuoteCode(quoteCode, MACDConstants.TERMINATION_SERVICE, CommonConstants.BACTIVE);
		terminationEligibleQuoteDetailsList = getTerminationQuoteDetailsList(terminationEligibleQuoteDetails);
		terminationEligibleQuoteDetailsList.stream().filter(Objects::nonNull).forEach(terminationSrvDetail -> {
			LOGGER.info("Termination Service Detail Response : {}", terminationSrvDetail);
			Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(terminationSrvDetail.getQuoteToLeId());
			if (quoteToLe.isPresent()) {
				try {
					if(quoteToLe.get().getIsMultiCircuit() != null && CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit())) {
					omsSfdcService.processUpdateOpportunity(new Date(), terminationSrvDetail.getTpsSFDCOptyId(),
							SFDCConstants.VERBAL_AGREEMENT_STAGE, quoteToLe.get());
					} else {
						List<ThirdPartyServiceJob> optyList = thirdPartyServiceJobsRepository.findByThirdPartySourceAndServiceTypeAndTpsId(SFDCConstants.SFDC, SFDCConstants.CREATE_OPTY, quoteToLe.get().getTpsSfdcOptyId());
						if(optyList != null && !optyList.isEmpty()) {
						omsSfdcService.processUpdateOptyDummy(quoteToLe.get(), optyList.get(0).getServiceRefId(), String.valueOf(quoteToLe.get().getTpsSfdcParentOptyId()), new Date(), quoteToLe.get().getTpsSfdcOptyId(),
								SFDCConstants.VERBAL_AGREEMENT_STAGE);
						}
					}
				} catch (TclCommonException e) {
					e.printStackTrace();
				}
			}
			LOGGER.info("Termination : Stage Verbal Aggreement initiated for quote : {} ", quoteCode);
		}); 
	}	
	
	private static List<TerminationQuoteDetailsBean> getTerminationQuoteDetailsList(List<Map<String, Object>> map) {
		List<TerminationQuoteDetailsBean> quoteDetailsList = new ArrayList<>();
		map.forEach(entry -> {		
			TerminationQuoteDetailsBean quoteDetails = new TerminationQuoteDetailsBean();
			if (entry.get(DashboardConstant.QUOTE_ID) != null) {
				quoteDetails.setQuoteId((Integer) entry.get(DashboardConstant.QUOTE_ID));
				quoteDetails.setQuoteCreatedTime((Date) entry.get(MACDConstants.QUOTE_CREATED_TIME));
				quoteDetails.setTerminationCreatedTime((Date) entry.get(MACDConstants.TERMINATION_CREATED_TIME));
				quoteDetails.setO2cCallInitiatedTime((Date) entry.get(MACDConstants.O2C_CALL_INITIATED_DATE));
			}
			if (entry.get(MACDConstants.QUOTE_LE_ID) != null)
				quoteDetails.setQuoteToLeId((Integer)entry.get(MACDConstants.QUOTE_LE_ID));
			if (entry.get(MACDConstants.SFDC_OPTY_ID) != null)
				quoteDetails.setTpsSFDCOptyId(entry.get(MACDConstants.SFDC_OPTY_ID).toString());
			if (entry.get(DashboardConstant.QUOTE_STAGE) != null)
				quoteDetails.setQuoteStage(entry.get(DashboardConstant.QUOTE_STAGE).toString());
			if (entry.get(DashboardConstant.QUOTE_CODE) != null)
				quoteDetails.setQuoteCode(entry.get(DashboardConstant.QUOTE_CODE).toString());
			if (entry.get(DashboardConstant.QUOTE_CATEGORY) != null)
				quoteDetails.setQuoteCategory(entry.get(DashboardConstant.QUOTE_CATEGORY).toString());
			if (entry.get(MACDConstants.SALES_TASK_RESPONSE) != null)
				quoteDetails.setSalesTaskResponse(entry.get(MACDConstants.SALES_TASK_RESPONSE).toString());
			quoteDetailsList.add(quoteDetails);
		});
					
		return quoteDetailsList;
	}	
	
	public OmsAttachmentBean updateCofUploadedDetails(Integer quoteId, Integer quoteLeId, String requestId, String url)
			throws TclCommonException {
		LOGGER.info("Entering  updateCofUploadedDetails for Termination Service : {} - {} - {} - {}", quoteId, quoteLeId, requestId, url);
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
					throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);

				Quote quote = quoteToLe.get().getQuote();
				Order order = orderRepository.findByQuoteAndStatus(quote, quote.getStatus());

				String attachmentrequest = Utils.convertObjectToJson(attachmentBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				Integer attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentrequest);
				LOGGER.info("Received the Attachment response with attachment Id {}",attachmentId);
				List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
						.findByQuoteToLeAndAttachmentType(quoteToLe.get(), AttachmentTypeConstants.TRF.toString());
				if (!omsAttachmentList.isEmpty()) {
					omsAttachment = omsAttachmentList.get(0);
				} else {
					omsAttachment = new OmsAttachment();
				}
				omsAttachment.setAttachmentType(AttachmentTypeConstants.TRF.toString());
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
	
	public void updateETCProductComponentAndPricingBean(QuoteIllSite illSite, QuoteNplLink nplLink, User user, String referenceName) {
		if(Objects.nonNull(illSite)) {
			List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					illSite.getId(), ChargeableItemConstants.ETC_CHARGES,
					referenceName);
			if(Objects.nonNull(components) && !components.isEmpty()) {
				components.stream().filter(Objects::nonNull).forEach(etcProductComponent -> {
					//etcProductComponent.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(etcProductComponent, 0D));
					MstProductFamily prodFamily = illSite.getProductSolution().getQuoteToLeProductFamily().getMstProductFamily();
					Integer quoteId = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId();
					constructQuoteComponentPrice(quoteId, prodFamily, etcProductComponent);			
				});
			}
		} else if (Objects.nonNull(nplLink)) {
			List<QuoteProductComponent> components = quoteProductComponentRepository.findByReferenceIdAndMstProductComponent_NameAndReferenceName(
					nplLink.getId(), ChargeableItemConstants.ETC_CHARGES,
					referenceName);
			if(Objects.nonNull(components) && !components.isEmpty()) {
				components.stream().filter(Objects::nonNull).forEach(etcProductComponent -> {
					//etcProductComponent.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(etcProductComponent, 0D));
					Optional<ProductSolution> productSolution = productSolutionRepository.findById(nplLink.getProductSolutionId());
					MstProductFamily prodFamily = null;
					if (productSolution.isPresent())
						prodFamily=productSolution.get().getQuoteToLeProductFamily().getMstProductFamily();
					Integer quoteId = nplLink.getQuoteId();
					constructQuoteComponentPrice(quoteId, prodFamily, etcProductComponent);			
				});
			}			
		}
	}
	
	public void constructETCProductComponentAndPricingBean(QuoteIllSite illSite, QuoteNplLink nplLink, User user) {
		Integer refId = null; 
		Integer quoteId = null;
		MstProductFamily prodFamily = null;
		if(Objects.nonNull(illSite)) {
			refId = illSite.getId();
			Optional<ProductSolution> productSolution = productSolutionRepository.findById(illSite.getProductSolution().getId());
			prodFamily = illSite.getProductSolution().getQuoteToLeProductFamily().getMstProductFamily();
			quoteId = illSite.getProductSolution().getQuoteToLeProductFamily().getQuoteToLe().getQuote().getId();
		} else if (Objects.nonNull(nplLink)) {
			refId = nplLink.getId();
			quoteId = nplLink.getQuoteId();
			Optional<ProductSolution> productSolution = productSolutionRepository.findById(nplLink.getProductSolutionId());
			if (productSolution.isPresent())
				prodFamily=productSolution.get().getQuoteToLeProductFamily().getMstProductFamily();
		}
		LOGGER.info("Product Family :{} ",prodFamily.toString());
		QuoteProductComponent etcProductComponent = constructQuoteProductETCComponent(refId, prodFamily, user);
		if(Objects.nonNull(etcProductComponent)) {
			etcProductComponent.setQuoteProductComponentsAttributeValues(constructQuoteAttributes(etcProductComponent, 0D));
			constructQuoteComponentPrice(quoteId, prodFamily, etcProductComponent);
		}
	}
	
	public void updateETCProductComponentAndPricingBean(Integer refId, Double etcPrice, String referenceName,String type) {
		LOGGER.info("Fetching Etc component for {}",type);
		List<QuoteProductComponent> productComponent = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceNameAndType(refId, ChargeableItemConstants.ETC_CHARGES, referenceName,type);
		if(Objects.nonNull(productComponent) && !productComponent.isEmpty()) {
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(productComponent.get(0).getId()), QuoteConstants.COMPONENTS.toString());
			if(Objects.nonNull(price)) {
				price.setEffectiveNrc(etcPrice);
				quotePriceRepository.save(price);
			}
		}
	}	
	
	private Set<QuoteProductComponentsAttributeValue> constructQuoteAttributes(
			QuoteProductComponent quoteProductComponent, Double etc) {
		Set<QuoteProductComponentsAttributeValue> quoteProductComponentsAttributeValues = new HashSet<>();
		if (quoteProductComponent != null) {
			List<QuoteProductComponentsAttributeValue> quoteAttributeValueList = 
					quoteProductComponentsAttributeValueRepository.findByQuoteProductComponent_IdAndProductAttributeMaster_Name(quoteProductComponent.getId(), ChargeableItemConstants.ETC_CHARGES);
			if(Objects.nonNull(quoteAttributeValueList) && !quoteAttributeValueList.isEmpty()) {
				quoteAttributeValueList.stream().filter(Objects::nonNull).forEach(quoteAttributeValue -> {
					quoteAttributeValue.setAttributeValues(String.valueOf(etc));
					quoteProductComponentsAttributeValueRepository.save(quoteAttributeValue);
					quoteProductComponentsAttributeValues.add(quoteAttributeValue);
				});
				
			} else {
				QuoteProductComponentsAttributeValue quoteAttributeValue = new QuoteProductComponentsAttributeValue();
				switch (quoteProductComponent.getMstProductComponent().getName()) {
				case "ETC Charges":
					quoteAttributeValue.setProductAttributeMaster(getProductAttributeMaster(ChargeableItemConstants.ETC_CHARGES));
					quoteAttributeValue.setAttributeValues(String.valueOf(etc));
					quoteAttributeValue.setDisplayValue(ChargeableItemConstants.ETC_CHARGES);
					break;
				}
				quoteAttributeValue.setQuoteProductComponent(quoteProductComponent);
				quoteProductComponentsAttributeValueRepository.save(quoteAttributeValue);
				quoteProductComponentsAttributeValues.add(quoteAttributeValue);
			}
		}
		return quoteProductComponentsAttributeValues;
	}	

	private QuoteProductComponent constructQuoteProductETCComponent(Integer refId, MstProductFamily prodFamily, User user) {
		QuoteProductComponent productComponent = new QuoteProductComponent();
		if (refId != null && prodFamily != null) {
			productComponent.setReferenceId(refId);
			productComponent.setMstProductComponent(getMstETCProductComponent(user));
			productComponent.setMstProductFamily(prodFamily);
			productComponent.setReferenceName(ChargeableItemConstants.ETC_CHARGES);
			quoteProductComponentRepository.save(productComponent);
		} else {
			productComponent = null;
		}
		return productComponent;
	}
	
	private MstProductComponent getMstETCProductComponent(User user) {
		LOGGER.info("Getting master ETC Charges");
		MstProductComponent mstProductComponent = null;
		List<MstProductComponent> mstProductComponents = mstProductComponentRepository
				.findByNameAndStatus(ChargeableItemConstants.ETC_CHARGES, (byte) 1);
		if (mstProductComponents != null && !mstProductComponents.isEmpty()) {
			mstProductComponent = mstProductComponents.get(0);
		}
		if (mstProductComponent == null) {
			mstProductComponent = new MstProductComponent();
			mstProductComponent.setCreatedBy(user.getUsername());
			mstProductComponent.setCreatedTime(new Date());
			mstProductComponent.setName(ChargeableItemConstants.ETC_CHARGES);
			mstProductComponent.setDescription(ChargeableItemConstants.ETC_CHARGES);
			mstProductComponent.setStatus((byte) 1);
			mstProductComponentRepository.save(mstProductComponent);
		}
		LOGGER.info(" exiting get master ETC Charges");
		return mstProductComponent;

	}	
	
	private QuotePrice constructQuoteComponentPrice(Integer quoteId,  MstProductFamily prodFamily, QuoteProductComponent productComponent) {
		QuotePrice quotePrice = null;
		if (quoteId != null && Objects.nonNull(prodFamily) && Objects.nonNull(productComponent)) {
			quotePrice = new QuotePrice();
			quotePrice.setCatalogMrc(0D);
			quotePrice.setCatalogNrc(0D);
			quotePrice.setCatalogArc(0D);
			quotePrice.setReferenceName(QuoteConstants.COMPONENTS.toString());
			quotePrice.setReferenceId(String.valueOf(productComponent.getId()));
			quotePrice.setComputedMrc(0D);
			quotePrice.setComputedNrc(0D);
			quotePrice.setComputedArc(0D);
			quotePrice.setDiscountInPercent(0D);
			quotePrice.setMinimumMrc(0D);
			quotePrice.setMinimumNrc(0D);
			quotePrice.setMinimumArc(0D);
			quotePrice.setEffectiveMrc(0D);
			quotePrice.setEffectiveNrc(0D);
			quotePrice.setEffectiveArc(0D);
			quotePrice.setEffectiveUsagePrice(0D);
			quotePrice.setMstProductFamily(prodFamily);
			quotePrice.setQuoteId(quoteId);
			quotePriceRepository.save(quotePrice);
		}
		return quotePrice;
	}
	
	private ProductAttributeMaster getProductAttributeMaster(String attributeName) {
		return productAttributeMasterRepository.findByName(attributeName);
	}
	
	public List<TerminatedServicesBean> checkForExistingQuotesByServiceIds(List<String> serviceIdList, String productName) throws TclCommonException {
		
		String serviceIds = serviceIdList.stream().collect(Collectors.joining(","));
		LOGGER.info("Termination : Find existing Quotes by Service Ids - {}", serviceIds);

		List<Map<String, Object>> existingTerminationQuoteDetails = null;
		List<TerminatedServicesBean> existingTerminationQuoteDetailList = null;
		if("IAS".equalsIgnoreCase(productName) || "GVPN".equalsIgnoreCase(productName)) {
		existingTerminationQuoteDetails = quoteRepository.findTerminationQuotesByServiceIds(serviceIdList, MACDConstants.TERMINATION_SERVICE, CommonConstants.BACTIVE);
		} else {
		existingTerminationQuoteDetails = quoteRepository.findTerminationQuotesByServiceIdsNPL(serviceIdList, MACDConstants.TERMINATION_SERVICE, CommonConstants.BACTIVE);
		}
		existingTerminationQuoteDetailList = getExisitingTerminationQuoteDetailsList(existingTerminationQuoteDetails);
		LOGGER.info("Termination : Find existing Quotes by Service Ids - {}", existingTerminationQuoteDetailList);
		return existingTerminationQuoteDetailList;
			
	}	
	
	private static List<TerminatedServicesBean> getExisitingTerminationQuoteDetailsList(List<Map<String, Object>> map) {
		List<TerminatedServicesBean> quoteDetailsList = new ArrayList<>();
		map.forEach(entry -> {		
			TerminatedServicesBean quoteDetails = new TerminatedServicesBean();
			if (entry.get(DashboardConstant.QUOTE_ID) != null) {
				quoteDetails.setQuoteId((Integer) entry.get(DashboardConstant.QUOTE_ID));
				quoteDetails.setQuoteCreatedTime((Date) entry.get(MACDConstants.QUOTE_CREATED_TIME));
			}
			if (entry.get(MACDConstants.QUOTE_LE_ID) != null)
				quoteDetails.setQuoteToLeId((Integer)entry.get(MACDConstants.QUOTE_LE_ID));
			if (entry.get(DashboardConstant.SERVICE_ID) != null)
				quoteDetails.setServiceId(entry.get(DashboardConstant.SERVICE_ID).toString());
			if (entry.get(DashboardConstant.QUOTE_CODE) != null)
				quoteDetails.setQuoteCode(entry.get(DashboardConstant.QUOTE_CODE).toString());
			if (entry.get(DashboardConstant.QUOTE_STAGE) != null)
				quoteDetails.setQuoteStage(entry.get(DashboardConstant.QUOTE_STAGE).toString());
			quoteDetailsList.add(quoteDetails);
		});
					
		return quoteDetailsList;
	}	
	
	public OmsAttachmentBean regenerateAndUploadTRFToStorage(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		Context context = new Context();
		OmsAttachmentBean omsAttachmentBean = null;
		String html = null;
		
		Map<String, Object> variable = populateMapforTerminationNotificationforReupload(quoteId, quoteToLeId);
		LOGGER.info("Popuated Map object : {}", variable);
		context.setVariables(variable);
		html = templateEngine.process("mtrf_template", context);
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		String quoteCode = quoteToLe.get().getQuote().getQuoteCode();
		String fileName = null;
		List<AttachmentsAudit> attachmentsAuditListTRF = attachmentsAuditRepository.findByQuoteToLeIdAndAttachmentType(quoteToLe.get().getId(), "TRF");
		if(attachmentsAuditListTRF != null && !attachmentsAuditListTRF.isEmpty()) {
			fileName = "TRFForm" + quoteCode + "_version_" + (attachmentsAuditListTRF.size()+1) + ".pdf";
		} else {
		fileName = "TRFForm" + quoteCode + ".pdf";
		}
		LOGGER.info(fileName+"\n"+html);

		if (quoteToLe.isPresent()) {
			try {
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				PDFGenerator.createPdf(html, bos);
				byte[] outArray = bos.toByteArray();
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					LOGGER.info("uploading TRF generated to object storage");
					InputStream inputStream = new ByteArrayInputStream(bos.toByteArray());
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
					if (customerCodeLeVal.isPresent() && customerLeCodeLeVal.isPresent()) {
						StoredObject storedObject = fileStorageService.uploadOverwriteeObjectWithFileName(fileName, inputStream,
								customerCodeLeVal.get().getAttributeValue(),
								customerLeCodeLeVal.get().getAttributeValue());
						String[] pathArray = storedObject.getPath().split("/");
						omsAttachmentBean = updateCofUploadedDetails(quoteId, quoteToLe.get().getId(), storedObject.getName(),
								pathArray[1]);

						Optional<Quote> quote = quoteRepository.findById(quoteId);

						OmsAttachBean omsAttachBean = new OmsAttachBean();
						omsAttachBean.setAttachmentId(omsAttachmentBean.getErfCusAttachmentId());
						omsAttachBean.setAttachmentType(omsAttachmentBean.getAttachmentType());
						omsAttachBean.setQouteLeId(quoteToLeId);
						omsAttachBean.setReferenceId(omsAttachmentBean.getReferenceId());
						omsAttachBean.setReferenceName(omsAttachmentBean.getReferenceName());
						omsAttachBean.setFileName(fileName);
						LOGGER.info("OmsAttachBean : {}",omsAttachBean.toString());
						omsAttachmentService.persistAttachmentAudit(omsAttachBean ,quote.get());
						LOGGER.info("After persisting Attachment Audit");
					}
				} else {
					CofDetails cofDetails = cofDetailsRepository.findByOrderUuid(quoteToLe.get().getQuote().getQuoteCode());
					if (cofDetails == null) {
						cofDetails = new CofDetails();
						// Get the file and save it somewhere
						String cofPath = cofAutoUploadPath + quoteToLe.get().getQuote().getQuoteCode().toLowerCase();
						File filefolder = new File(cofPath);
						if (!filefolder.exists()) {
							filefolder.mkdirs();
	
						}
						String fileFullPath = cofPath + CommonConstants.RIGHT_SLASH + fileName;
						try (OutputStream outputStream = new FileOutputStream(fileFullPath)) {
							bos.writeTo(outputStream);
						}
						cofDetails.setOrderUuid(quoteToLe.get().getQuote().getQuoteCode());
						cofDetails.setUriPath(fileFullPath);
						cofDetails.setSource(Source.AUTOMATED_COF.getSourceType());
						cofDetails.setCreatedBy(Utils.getSource());
						cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
						cofDetailsRepository.save(cofDetails);
					}
				}
			}catch (TclCommonException e) {
				LOGGER.warn("Error in Generate Cof {}", ExceptionUtils.getStackTrace(e));
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			} catch (IOException | DocumentException e1) {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e1, ResponseResource.R_CODE_ERROR);
			}
		}
		return omsAttachmentBean;
		
	}
	
	private List<TRFBean> retrieveTerminationDetailsForIASorGVPNForReUpload(Integer quoteId) throws TclCommonException {
		List<TRFBean> trfBeanList = new ArrayList<>();
		Optional<Quote> quoteDetail = quoteRepository.findById(quoteId);
		if (quoteDetail.isPresent()) {
			quoteDetail.get().getQuoteToLes().stream().forEach(quoteToLeBean->{
				quoteToLeBean.getQuoteToLeProductFamilies().stream().forEach(prodFamilyBean->{
					prodFamilyBean.getProductSolutions().stream().filter(Objects::nonNull).forEach(solutionBean->{
						solutionBean.getQuoteIllSites().stream().filter(Objects::nonNull).forEach(siteBean-> {
							List<QuoteIllSiteToService> quoteSiteToServiceList = quoteSiteToServiceRepository.findByQuoteIllSite(siteBean);
							quoteSiteToServiceList.stream().filter(Objects::nonNull).forEach(siteToService -> {
								QuoteSiteServiceTerminationDetails terminationDetailBean = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
								TRFBean trfBean = new TRFBean();
								if(terminationDetailBean!=null) {
									trfBean.setCommunicationRecipient(terminationDetailBean.getCommunicationReceipient());
									trfBean.setDateOfRequestedTermination(DateUtil.convertDateToSlashString(terminationDetailBean.getRequestedDateForTermination()));
									trfBean.setCustomerRequestDate(DateUtil.convertDateToSlashString(terminationDetailBean.getCustomerMailReceivedDate()));
									trfBean.setEffectiveDateOfChange(DateUtil.convertDateToSlashString(terminationDetailBean.getEffectiveDateOfChange()));
									trfBean.setCustomerSuccessManagers(terminationDetailBean.getCsmNonCsmName());
									trfBean.setCustomerSuccessManagersEmail(terminationDetailBean.getCsmNonCsmEmail());
									if(CommonConstants.BACTIVE.equals(terminationDetailBean.getEtcApplicable())) {
										trfBean.setEtcApplicability(CommonConstants.YES);
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceId(siteBean.getId());
										Optional<QuoteProductComponent> etcChargeBean	= quoteProductComponentList.stream().filter(component -> (ChargeableItemConstants.ETC_CHARGES.equalsIgnoreCase(component.getMstProductComponent().getName())
												&& siteToService.getType().equalsIgnoreCase(component.getType()))).findFirst();
										List<QuotePrice> quotePriceList = quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(), String.valueOf(etcChargeBean.get().getId()));
										if(quotePriceList != null && !quotePriceList.isEmpty()) {
											if(quotePriceList.get(0) != null && (quotePriceList.get(0).getEffectiveNrc() != null)) {
													trfBean.setEtcAmount(new BigDecimal(quotePriceList.get(0).getEffectiveNrc()));
											} else {
												trfBean.setEtcAmount(new BigDecimal(0.0));
											} 
										} else {
											trfBean.setEtcAmount(new BigDecimal(0.0));
										}
									} else {
										trfBean.setEtcApplicability(CommonConstants.NO);
										trfBean.setEtcAmount(new BigDecimal(0.0));
									}
									trfBean.setReasonForTermination(terminationDetailBean.getReasonForTermination());
									trfBean.setServiceId(terminationDetailBean.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
									trfBean.setSubTerminationReason(terminationDetailBean.getTerminationSubtype());
									trfBean.setTerminationSubtype(terminationDetailBean.getTerminationSubtype());
									trfBean.setTypeOfService(prodFamilyBean.getMstProductFamily().getName());
									trfBean.setEtcRemark(terminationDetailBean.getTerminationRemarks());
									SIServiceDetailDataBean sIServiceDetailDataBean = null;
									try {
										sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(terminationDetailBean.getQuoteIllSiteToService().getErfServiceInventoryTpsServiceId());
										if (sIServiceDetailDataBean!=null) {
											LOGGER.info("Response service details {}", sIServiceDetailDataBean.toString());
											trfBean.setAccountManagers(sIServiceDetailDataBean.getAccountManager());
											trfBean.setPreviousMRC(sIServiceDetailDataBean.getMrc());
											trfBean.setaEndCity(sIServiceDetailDataBean.getSourceCity());
											trfBean.setbEndCity(sIServiceDetailDataBean.getDestinationCity());
											trfBean.setCircuitSpeed(sIServiceDetailDataBean.getPortBw()+" " + sIServiceDetailDataBean.getPortBwUnit());
										}
									} catch (Exception e) {
										LOGGER.info("Error when fetching service details {}", e);
										throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
									}
								}
								trfBeanList.add(trfBean);
							
						});
						});
					});
					});
				});
		}
		return trfBeanList;
	}
	
	private List<TRFBean> retrieveTerminationDetailsforNPLorNDEForReupload(Integer quoteId) throws TclCommonException {
		List<TRFBean> trfBeanList = new ArrayList<>();
		Optional<Quote> nplQuoteDetail = quoteRepository.findById(quoteId);
		if (nplQuoteDetail != null) {
		nplQuoteDetail.get().getQuoteToLes().stream().forEach(quoteToLeBean->{
			quoteToLeBean.getQuoteToLeProductFamilies().stream().forEach(prodFamilyBean->{
				prodFamilyBean.getProductSolutions().stream().filter(Objects::nonNull).forEach(solutionBean->{
					if(solutionBean.getMstProductOffering() != null && solutionBean.getMstProductOffering().getProductName() != null 
							&& CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(solutionBean.getMstProductOffering().getProductName())) {
						List<QuoteIllSite> quoteSitesList = illSiteRepository.findByProductSolution_Id(solutionBean.getId());
						quoteSitesList.stream().forEach(quoteSite -> {
							List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteSiteToServiceRepository.findByQuoteIllSite(quoteSite);
							quoteIllSiteToServiceList.stream().forEach(siteToService -> {
								QuoteSiteServiceTerminationDetails terminationDetailBean = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);
								TRFBean trfBean = new TRFBean();
								if(terminationDetailBean!=null) {
									trfBean.setCommunicationRecipient(terminationDetailBean.getCommunicationReceipient());
									trfBean.setDateOfRequestedTermination(DateUtil.convertDateToSlashString(terminationDetailBean.getRequestedDateForTermination()));
									trfBean.setCustomerRequestDate(DateUtil.convertDateToSlashString(terminationDetailBean.getCustomerMailReceivedDate()));
									trfBean.setEffectiveDateOfChange(DateUtil.convertDateToSlashString(terminationDetailBean.getEffectiveDateOfChange()));
									trfBean.setCustomerSuccessManagers(terminationDetailBean.getCsmNonCsmName());
									trfBean.setCustomerSuccessManagersEmail(terminationDetailBean.getCsmNonCsmEmail());
									if(CommonConstants.BACTIVE.equals(terminationDetailBean.getEtcApplicable())) {
										trfBean.setEtcApplicability(CommonConstants.YES);
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceId(quoteSite.getId());
										Optional<QuoteProductComponent> etcChargeBean	= quoteProductComponentList.stream().filter(component -> (ChargeableItemConstants.ETC_CHARGES.equalsIgnoreCase(component.getMstProductComponent().getName()))).findFirst();
										List<QuotePrice> quotePriceList = quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(), String.valueOf(etcChargeBean.get().getId()));
										if(quotePriceList != null && !quotePriceList.isEmpty()) {
											if(quotePriceList.get(0) != null && (quotePriceList.get(0).getEffectiveNrc() != null)) {
													trfBean.setEtcAmount(new BigDecimal(quotePriceList.get(0).getEffectiveNrc()));
											} else {
												trfBean.setEtcAmount(new BigDecimal(0.0));
											}
										} else {
											trfBean.setEtcAmount(new BigDecimal(0.0));
										}
									} else {
										trfBean.setEtcApplicability(CommonConstants.NO);
										trfBean.setEtcAmount(new BigDecimal(0));
									}
									trfBean.setReasonForTermination(terminationDetailBean.getReasonForTermination());
									trfBean.setServiceId(siteToService.getErfServiceInventoryTpsServiceId());
									trfBean.setSubTerminationReason(terminationDetailBean.getTerminationSubtype());
									trfBean.setTerminationSubtype(terminationDetailBean.getTerminationSubtype());
									trfBean.setTypeOfService(prodFamilyBean.getMstProductFamily().getName());
									trfBean.setEtcRemark(terminationDetailBean.getTerminationRemarks());
									SIServiceDetailDataBean sIServiceDetailDataBean = null;
									try {
										sIServiceDetailDataBean = macdUtils.getServiceDetailIASTermination(siteToService.getErfServiceInventoryTpsServiceId());
										if (sIServiceDetailDataBean!=null) {
											LOGGER.info("Cross Connect Response service details {}", sIServiceDetailDataBean.toString());
											trfBean.setAccountManagers(sIServiceDetailDataBean.getAccountManager());
											trfBean.setPreviousMRC(sIServiceDetailDataBean.getMrc());
											trfBean.setaEndCity(sIServiceDetailDataBean.getSourceCity());
											trfBean.setbEndCity(sIServiceDetailDataBean.getDestinationCity());
											trfBean.setCircuitSpeed(sIServiceDetailDataBean.getPortBw()+" " + sIServiceDetailDataBean.getPortBwUnit());
										}
									} catch (Exception e) {
										LOGGER.info("Error when fetching service details {}", e);
										throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
									}
								}
								trfBeanList.add(trfBean);
								});
							
							}); 
									
		
					} else {
					List<QuoteNplLink> quoteNplLinkList = nplLinkRepository.findByProductSolutionId(solutionBean.getId());
					quoteNplLinkList.stream().forEach(linkBean-> {
						List<QuoteIllSiteToService> quoteIllSiteToServiceList = quoteSiteToServiceRepository.findByQuoteNplLink_Id(linkBean.getId());
						quoteIllSiteToServiceList.stream().forEach(siteToService -> {
							QuoteSiteServiceTerminationDetails terminationDetailLinkBean = quoteSiteServiceTerminationDetailsRepository.findByQuoteIllSiteToService(siteToService);	
						
							
								TRFBean trfBean = new TRFBean();
								if (terminationDetailLinkBean!= null) {
									trfBean.setCommunicationRecipient(terminationDetailLinkBean.getCommunicationReceipient());
									trfBean.setDateOfRequestedTermination(DateUtil.convertDateToSlashString(terminationDetailLinkBean.getRequestedDateForTermination()));
									trfBean.setCustomerRequestDate(DateUtil.convertDateToSlashString(terminationDetailLinkBean.getCustomerMailReceivedDate()));
									trfBean.setEffectiveDateOfChange(DateUtil.convertDateToSlashString(terminationDetailLinkBean.getEffectiveDateOfChange()));
									trfBean.setCustomerSuccessManagers(terminationDetailLinkBean.getCsmNonCsmName());
									trfBean.setCustomerSuccessManagersEmail(terminationDetailLinkBean.getCsmNonCsmEmail());
									if(CommonConstants.BACTIVE.equals(terminationDetailLinkBean.getEtcApplicable())) {
										trfBean.setEtcApplicability(CommonConstants.YES);
										List<QuoteProductComponent> quoteProductComponentList = quoteProductComponentRepository.findByReferenceId(linkBean.getId());
										Optional<QuoteProductComponent> etcChargeBean	= quoteProductComponentList.stream().filter(component -> (ChargeableItemConstants.ETC_CHARGES.equalsIgnoreCase(component.getMstProductComponent().getName()))).findFirst();
										List<QuotePrice> quotePriceList = quotePriceRepository.findByReferenceNameAndReferenceId(QuoteConstants.COMPONENTS.toString(), String.valueOf(etcChargeBean.get().getId()));
										if(quotePriceList != null && !quotePriceList.isEmpty()) {
											if(quotePriceList.get(0) != null && (quotePriceList.get(0).getEffectiveNrc() != null)) {
													trfBean.setEtcAmount(new BigDecimal(quotePriceList.get(0).getEffectiveNrc()));
											} else {
												trfBean.setEtcAmount(new BigDecimal(0.0));
											}
										} else {
											trfBean.setEtcAmount(new BigDecimal(0.0));
										}
									} else {
										trfBean.setEtcApplicability(CommonConstants.NO);
										trfBean.setEtcAmount(new BigDecimal(0));
									}
									trfBean.setReasonForTermination(terminationDetailLinkBean.getReasonForTermination());
									trfBean.setServiceId(siteToService.getErfServiceInventoryTpsServiceId());
									trfBean.setSubTerminationReason(terminationDetailLinkBean.getTerminationSubtype());
									trfBean.setTerminationSubtype(terminationDetailLinkBean.getTerminationSubtype());
									trfBean.setTypeOfService(prodFamilyBean.getMstProductFamily().getName());
									trfBean.setEtcRemark(terminationDetailLinkBean.getTerminationRemarks());
									List<SIServiceDetailDataBean> sIServiceDetailDataBean = null;
									try {
										sIServiceDetailDataBean = macdUtils.getServiceDetailNPLTermination(siteToService.getErfServiceInventoryTpsServiceId());
										if (sIServiceDetailDataBean!=null) { //To be updated based on link info
											LOGGER.info("NPL Response service details {}", sIServiceDetailDataBean.toString());
											trfBean.setAccountManagers(sIServiceDetailDataBean.get(0).getAccountManager());
											trfBean.setPreviousMRC(sIServiceDetailDataBean.get(0).getMrc());
											if("SiteA".equalsIgnoreCase(sIServiceDetailDataBean.get(0).getSiteType())){
												trfBean.setaEndCity(sIServiceDetailDataBean.get(0).getSourceCity());
											} else {
												trfBean.setbEndCity(sIServiceDetailDataBean.get(0).getSourceCity());
											}
											
											if("SiteA".equalsIgnoreCase(sIServiceDetailDataBean.get(1).getSiteType())){
												trfBean.setaEndCity(sIServiceDetailDataBean.get(1).getSourceCity());
											} else {
												trfBean.setbEndCity(sIServiceDetailDataBean.get(1).getSourceCity());
											}
											
											trfBean.setCircuitSpeed(sIServiceDetailDataBean.get(0).getPortBw()+" " + sIServiceDetailDataBean.get(0).getPortBwUnit());
										}
									} catch (Exception e) {
										LOGGER.info("Error when fetching service details {}", e);
										throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
									}
								}
								trfBeanList.add(trfBean);
							});
					
					});
					
						}
					
				});
			});		
		});
		
		}
		return trfBeanList;
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> populateMapforTerminationNotificationforReupload(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		Map<String, Object> map = new HashMap<>();
		TerminationNTFBean trfNTFBean = new TerminationNTFBean();
		List<TRFBean> trfBeanList = new ArrayList<>();
		String productName = null;

		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			productName = quoteToLe.get().getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily().getName();
			//String custId = quoteToLe.get().getQuote().getCustomer().getErfCusCustomerId().toString();
			
			try {
			if (productName.startsWith("IAS")|| productName.startsWith("GVPN")) {
				trfBeanList = retrieveTerminationDetailsForIASorGVPNForReUpload(quoteId);
			} else if (productName.startsWith("NPL")|| productName.startsWith("NDE")) {
				trfBeanList = retrieveTerminationDetailsForIASorGVPNForReUpload(quoteId);
			}
			LOGGER.info("Retrieved Termination Bean List : {}", trfBeanList.toString());
			List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository.findByQuoteToLe(quoteToLe.get());
			Optional<QuoteLeAttributeValue> custLeName = quoteLeAttributesList.stream()
					.filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute().getName()
							.equalsIgnoreCase(LeAttributesConstants.LEGAL_ENTITY_NAME))
					.findFirst();
			Optional<QuoteLeAttributeValue> tataBillingEntity = quoteLeAttributesList
					.stream().filter(quoteLeAttributeValue -> quoteLeAttributeValue.getMstOmsAttribute()
							.getName().equalsIgnoreCase(LeAttributesConstants.SUPPLIER_CONTRACTING_ENTITY))
					.findFirst();
			
			List<String> customerIds = new ArrayList<>();
			String acctManager = null;
			String acctManagerEmail = null;
			customerIds.add(quoteToLe.get().getQuote().getCustomer().getErfCusCustomerId().toString());
			customerIds.add(quoteToLe.get().getErfCusCustomerLegalEntityId().toString());
			String request = customerIds.stream().collect(Collectors.joining(","));
			String customerLeContacts = (String) mqUtils.sendAndReceive(ownerNameBasedOnBusinessSegment, request);
			if(customerLeContacts != null) {
				LOGGER.info("customerLeContacts {}", Utils.convertObjectToJson(customerLeContacts));
				CustomerContactDetails ownerNameDetails = (CustomerContactDetails) Utils
					.convertJsonToObject(customerLeContacts, CustomerContactDetails.class);
				LOGGER.info("Owner name {}", ownerNameDetails.getEmailId());			
				//String acctManager = (String) mqUtils.sendAndReceive(customerAccountManagerName, custId);
				//String acctManagerEmail = (String) mqUtils.sendAndReceive(customerAccountManagerEmail, custId);
				acctManager = ownerNameDetails.getName();
				acctManagerEmail = ownerNameDetails.getEmailId();
			}
			
			//Prepare Notification Object
			trfNTFBean.setNotificationDate(DateUtil.convertDateToSlashString(new Date()));
			trfNTFBean.setOrderType(quoteToLe.get().getQuoteType());
			trfNTFBean.setTataCommRefId(quoteToLe.get().getQuote().getQuoteCode());
			trfNTFBean.setAccountManagers(acctManager);
			trfNTFBean.setAccountManagersEmail(acctManagerEmail);
			trfNTFBean.setCustomerAccountName(quoteToLe.get().getQuote().getCustomer().getCustomerName());
			trfNTFBean.setLegalEntityName(custLeName.isPresent()?custLeName.get().getAttributeValue():"");
			trfNTFBean.setTataBillingEntity(tataBillingEntity.isPresent()?tataBillingEntity.get().getAttributeValue():"");
			trfNTFBean.setCurrency(quoteToLe.get().getCurrencyCode());
			
			trfNTFBean.setIsMulticircuit(false);
			if(quoteToLe.get().getIsMultiCircuit() == 1)
				trfNTFBean.setIsMulticircuit(true);
			else
				trfNTFBean.setIsMulticircuit(false);
			if(trfBeanList!= null && !trfBeanList.isEmpty()) {
				//Retrieve first bean to set general notification data
				TRFBean firstTRFBean = trfBeanList.get(0);
				String orderDesc = (custLeName.isPresent()?custLeName.get().getAttributeValue(): "")+"_";
				
				
				CustomerLeContactDetailBean communicationRecipientLeContact = getCustomerLeContactByCustomerEmailId(firstTRFBean.getCommunicationRecipient());
				if(Objects.nonNull(communicationRecipientLeContact)) {
					LOGGER.info("Customer Contact Details : {}", communicationRecipientLeContact.toString());
					trfNTFBean.setCustName(communicationRecipientLeContact.getName());
					trfNTFBean.setCustomerEmailId(firstTRFBean.getCommunicationRecipient());
					trfNTFBean.setCustomerPhoneNumber(communicationRecipientLeContact.getMobilePhone());
				}
				
				if(Boolean.TRUE.equals(trfNTFBean.getIsMulticircuit())) {
					orderDesc = orderDesc+"Bulk termination_"+trfBeanList.size()+" ckts";
					trfNTFBean.setTrfBean(trfBeanList);
					BigDecimal cummulativeETC = trfBeanList.stream().filter(Objects::nonNull).map(TRFBean::getEtcAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
					Double cummulativePrevMRC = trfBeanList.stream().filter(Objects::nonNull).mapToDouble(TRFBean::getPreviousMRC).sum();
					trfNTFBean.setPreviousMRC(cummulativePrevMRC);
					trfNTFBean.setEtcAmount(cummulativeETC);
					trfNTFBean.setServiceId(String.valueOf(trfBeanList.size()));
				} else {
					orderDesc = orderDesc+firstTRFBean.getServiceId()+"_"+quoteToLe.get().getQuoteType();
					trfNTFBean.setPreviousMRC(firstTRFBean.getPreviousMRC());
					trfNTFBean.setEtcAmount(firstTRFBean.getEtcAmount());
					trfNTFBean.setServiceId(firstTRFBean.getServiceId());
				}
				trfNTFBean.setOrderDesc(orderDesc);
				trfNTFBean.setTypeOfService(firstTRFBean.getTypeOfService());
				trfNTFBean.setCommunicationRecipient(firstTRFBean.getCommunicationRecipient());
				trfNTFBean.setCustomerSuccessManagers(firstTRFBean.getCustomerSuccessManagers());
				if (Objects.nonNull(firstTRFBean.getCustomerSuccessManagersEmail()))
					trfNTFBean.setCustomerSuccessManagersEmail(firstTRFBean.getCustomerSuccessManagersEmail());
				else
					firstTRFBean.setCustomerSuccessManagersEmail(MACDConstants.TD_SUPPORT_EMAIL);
				trfNTFBean.setTerminationSubtype(firstTRFBean.getTerminationSubtype());
				trfNTFBean.setCustomerRequestDate(firstTRFBean.getCustomerRequestDate());
				trfNTFBean.setDateOfRequestedTermination(firstTRFBean.getDateOfRequestedTermination());
				trfNTFBean.setEffectiveDateOfChange(firstTRFBean.getEffectiveDateOfChange());
				trfNTFBean.setEtcApplicability(firstTRFBean.getEtcApplicability());
				trfNTFBean.setEtcRemark(firstTRFBean.getEtcRemark());
				trfNTFBean.setReasonForTermination(firstTRFBean.getReasonForTermination());
				trfNTFBean.setSubTerminationReason(firstTRFBean.getSubTerminationReason());
				if (CommonConstants.BDEACTIVATE.equals(quoteToLe.get().getIsMultiCircuit())) {
					trfNTFBean.setaEndCity(firstTRFBean.getaEndCity());
					trfNTFBean.setbEndCity(firstTRFBean.getbEndCity());
					trfNTFBean.setCircuitSpeed(firstTRFBean.getCircuitSpeed());
				}
				
			}

			map = objectMapper.convertValue(trfNTFBean, Map.class);
			
		} catch (TclCommonException e) {
			LOGGER.error("Error populating termination notification data : ", e.getMessage(), e);
			throw new TclCommonException(e);
		}
		} else {
			LOGGER.error("QuoteToLE not present - cannot send termination notification ");
		}
		return map;
	}
	
	public List<AttachmentsAudit> getAttachmentIDs(Integer quoteToLeId ) throws TclCommonException {
		List<AttachmentsAudit> attachmentAuditList = new ArrayList<AttachmentsAudit>();
		try {
			attachmentAuditList = attachAuditRepository.findByQuoteToLeIdAndAttachmentType(quoteToLeId);
		}catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return attachmentAuditList;
	}
	
	public String retrieveCurrentQuoteStageInfo(Integer quoteId, Integer quoteToLeId) throws TclCommonException {
		LOGGER.info("Retrieving Current Quote Stage Information for Quote : {} - {}",quoteId, quoteToLeId);
		Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteToLeId);
		if (quoteToLe.isPresent()) {
			return quoteToLe.get().getStage();
		} else {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}		
	}
	
	public Double retrieveETChargesFromPricingBean(Integer refId, String referenceName) {
		Double etCharge = 0D;
		List<QuoteProductComponent> productComponent = quoteProductComponentRepository
				.findByReferenceIdAndMstProductComponent_NameAndReferenceName(refId, ChargeableItemConstants.ETC_CHARGES, referenceName);
		if(Objects.nonNull(productComponent) && !productComponent.isEmpty()) {
			QuotePrice price = quotePriceRepository.findByReferenceIdAndReferenceName(
					String.valueOf(productComponent.get(0).getId()), QuoteConstants.COMPONENTS.toString());
			if(Objects.nonNull(price)) {
				etCharge = price.getEffectiveNrc();
			}		
		}
		return etCharge;
	}	
	
	public String downloadTrfFromStorageContainer(Integer quoteId, Integer quoteLeId, Integer orderId,
			Integer orderLeId, Map<String, String> cofObjectMapper) throws TclCommonException {
		String tempDownloadUrl = StringUtils.EMPTY;
		Order order = null;
		try {
			LOGGER.info(
					"Inside Download TRF From Object Storage Container with input with quoteId {} ,quoteLe {} , cofObjMapper {}",
					quoteId, quoteLeId, cofObjectMapper);
			OmsAttachment omsAttachment = null;
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				if ((Objects.isNull(orderId) && Objects.isNull(orderLeId)))
					throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_ERROR);

				if (!Objects.isNull(quoteLeId)) {
					Optional<QuoteToLe> quoteToLe = quoteToLeRepository.findById(quoteLeId);

					if (quoteToLe.isPresent()) {
						order = orderRepository.findByQuoteAndStatus(quoteToLe.get().getQuote(),
								quoteToLe.get().getQuote().getStatus());
						if (order != null) {
							LOGGER.info("Getting oms Attachment Using order {}", order.getId());
							omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
							if (omsAttachment == null) {
								omsAttachment = getOmsAttachmentByQuote(quoteId, omsAttachment);
							}

						} else {
							LOGGER.info("Getting oms Attachment Using quote {}", quoteId);
							omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
							LOGGER.info("Output omsAttachment {}", omsAttachment.getId());
						}
					}
				} else if (!Objects.isNull(orderId) && !Objects.isNull(orderLeId)) {
					LOGGER.info("Getting oms Attachment Using order {}", orderLeId);
					Optional<Order> orderOpt = orderRepository.findById(orderId);
					if (orderOpt.isPresent()) {
						order = orderOpt.get();
						omsAttachment = getOmsAttachmentBasedOnOrder(order, omsAttachment);
						if (omsAttachment == null) {
							quoteId = order.getQuote().getId();
							omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
							Optional<OrderToLe> orderTole = orderToLeRepository.findById(orderLeId);
							if (orderTole.isPresent()) {
								omsAttachment.setOrderToLe(orderTole.get());
								omsAttachment.setReferenceId(orderId);
								omsAttachment.setReferenceName(CommonConstants.ORDERS);
								omsAttachmentRepository.save(omsAttachment);
							}
						}
					}

				}
				if (omsAttachment != null) {
					String response = (String) (mqUtils.sendAndReceive(attachmentRequestIdQueue,
							String.valueOf(omsAttachment.getErfCusAttachmentId())));
					if (StringUtils.isNotBlank(response)) {
						LOGGER.info("Output Received while getting the attachment table {}", response);
						AttachmentBean attachmentBean = (AttachmentBean) Utils.convertJsonToObject(response,
								AttachmentBean.class);
						if (cofObjectMapper != null) {
							cofObjectMapper.put("FILENAME", attachmentBean.getFileName());
							cofObjectMapper.put("OBJECT_STORAGE_PATH", attachmentBean.getPath());
							String tempUrl = fileStorageService.getTempDownloadUrl(attachmentBean.getFileName(), 60000,
									attachmentBean.getPath(), false);
							cofObjectMapper.put("TEMP_URL", tempUrl);
							LOGGER.info("CofObject Mapper {}", cofObjectMapper);
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
						AttachmentTypeConstants.TRF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		return omsAttachment;
	}
	
	private OmsAttachment getOmsAttachmentBasedOnQuote(Integer quoteId, OmsAttachment omsAttachment) {
		List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
				.findByReferenceNameAndReferenceIdAndAttachmentType(CommonConstants.QUOTES, quoteId,
						AttachmentTypeConstants.TRF.toString());
		if (!omsAttachmentList.isEmpty()) {
			omsAttachment = omsAttachmentList.get(0);
		}
		LOGGER.info("Oms Attachment is -----> for quote ----> {} ",
				Optional.of(omsAttachment), quoteRepository.findById(quoteId).get().getQuoteCode());
		return omsAttachment;
	}
	
	private OmsAttachment getOmsAttachmentByQuote(Integer quoteId, OmsAttachment omsAttachment) {
		LOGGER.info("Trying with oms Attachment Using quote {}", quoteId);
		omsAttachment = getOmsAttachmentBasedOnQuote(quoteId, omsAttachment);
		LOGGER.info("Output omsAttachment {}", omsAttachment.getId());
		return omsAttachment;
	}
	
}
