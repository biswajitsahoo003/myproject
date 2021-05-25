package com.tcl.dias.oms.docusign.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;

import com.tcl.dias.common.beans.Approver;
import com.tcl.dias.common.beans.AttachmentBean;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CommonDocusignResponse;
import com.tcl.dias.common.beans.CustomerAttributeBean;
import com.tcl.dias.common.beans.CustomerDetailsBean;
import com.tcl.dias.common.beans.DocusignAuditBean;
import com.tcl.dias.common.beans.DocusignGetDocumentRequest;
import com.tcl.dias.common.beans.ObjectStorageListenerBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.SignDetailBean;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.CustomerAttributeConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.constants.SfdcServiceStatus;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.AuditMode;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.ThirdPartySource;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.oms.beans.ApproverListBean;
import com.tcl.dias.oms.beans.OmsAttachmentBean;
import com.tcl.dias.oms.constants.DocumentConstant;
import com.tcl.dias.oms.constants.DocusignConstants;
import com.tcl.dias.oms.constants.ExceptionConstants;
import com.tcl.dias.oms.entity.entities.CofDetails;
import com.tcl.dias.oms.entity.entities.DocusignAudit;
import com.tcl.dias.oms.entity.entities.MstOmsAttribute;
import com.tcl.dias.oms.entity.entities.OmsAttachment;
import com.tcl.dias.oms.entity.entities.Order;
import com.tcl.dias.oms.entity.entities.OrderConfirmationAudit;
import com.tcl.dias.oms.entity.entities.Quote;
import com.tcl.dias.oms.entity.entities.QuoteLeAttributeValue;
import com.tcl.dias.oms.entity.entities.QuoteToLe;
import com.tcl.dias.oms.entity.entities.ThirdPartyServiceJob;
import com.tcl.dias.oms.entity.entities.User;
import com.tcl.dias.oms.entity.repository.CofDetailsRepository;
import com.tcl.dias.oms.entity.repository.DocusignAuditRepository;
import com.tcl.dias.oms.entity.repository.MstOmsAttributeRepository;
import com.tcl.dias.oms.entity.repository.OmsAttachmentRepository;
import com.tcl.dias.oms.entity.repository.OrderConfirmationAuditRepository;
import com.tcl.dias.oms.entity.repository.OrderRepository;
import com.tcl.dias.oms.entity.repository.QuoteLeAttributeValueRepository;
import com.tcl.dias.oms.entity.repository.QuoteRepository;
import com.tcl.dias.oms.entity.repository.QuoteToLeRepository;
import com.tcl.dias.oms.entity.repository.ThirdPartyServiceJobsRepository;
import com.tcl.dias.oms.entity.repository.UserRepository;
import com.tcl.dias.oms.pdf.constants.PDFConstants;
import com.tcl.dias.oms.service.NotificationService;
import com.tcl.dias.oms.service.OmsUtilService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * Docusign service class which contains the methods for docusign
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public abstract class DocuSignUtilService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocuSignUtilService.class);
	public static final String CUSTOMER = "CUSTOMER";
	public static final String REVIEWER1 = "REVIEWER1";
	public static final String REVIEWER2 = "REVIEWER2";
	public static final String CUSTOMER1 = "CUSTOMER1";
	public static final String CUSTOMER2 = "CUSTOMER2";
	public static final String COMMERCIAL = "COMMERCIAL";

	@Autowired
	DocusignAuditRepository docusignAuditRepository;

	@Autowired
	OrderConfirmationAuditRepository orderConfirmationAuditRepository;

	@Value("${file.download.queue}")
	String downloadQueue;

	@Value("${info.docusign.cof.sign}")
	String docusignRequestQueue;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${application.env}")
	String appEnv;

	@Value("${rabbitmq.customer.queue}")
	String customerDetailsQueue;

	@Autowired
	QuoteRepository quoteRepository;

	@Autowired
	CofDetailsRepository cofDetailsRepository;

	@Value("${info.docusign.audit}")
	String auditQueue;

	@Value("${info.docusign.resend}")
	String resendQueue;

	@Value("${info.docusign.getdocument}")
	String getDocumentQueue;

	@Value("${info.docusign.retry}")
	String docuSignRetryQueue;

	@Value("${rabbitmq.dop.matrix.email.queue}")
	String dopEmailQueue;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	OmsUtilService omsUtilService;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${rabbitmq.customercode.customerlecode.queue}")
	String customerCodeCustomerLeCodeQueue;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	OrderRepository orderRepository;

	@Value("${info.docusign.envelope.retry}")
	String docusignEnvelopeRetry;

	@Autowired
	QuoteToLeRepository quoteToLeRepository;

	@Autowired
	OmsAttachmentRepository omsAttachmentRepository;

	@Value("${attatchment.queue}")
	String attachmentQueue;

	@Autowired
	NotificationService notificationService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	QuoteLeAttributeValueRepository quoteLeAttributeValueRepository;

	@Autowired
	MstOmsAttributeRepository mstOmsAttributeRepository;

	@Autowired
	ThirdPartyServiceJobsRepository thirdPartyServiceJobsRepository;

	private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

	@Value("${spring.rabbitmq.host}")
	String mqHostName;

	@Value("${webhook.docusign.status}")
	String webhookStatus;

	/**
	 *
	 * This methos is used to process the response from the docusign after receiving
	 * response
	 *
	 * @param docuSignResponse
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public void processDocuSignResponse(CommonDocusignResponse docuSignResponse) throws TclCommonException {
		Boolean sendAck = false;
		if (docuSignResponse != null && docuSignResponse.isStatus()) {
			LOGGER.info("Docusign response recieved for quote ---> is {} ----> {} ", docuSignResponse.getQuoteId(),
					docuSignResponse);
			Optional<Quote> quoteEntity = quoteRepository.findById(docuSignResponse.getQuoteId());
			LOGGER.info("Quote received is {}", quoteEntity);
			if (quoteEntity.isPresent()) {
				if (!docuSignResponse.isStatus()) {
					DocusignAudit docuSignAuditEntity = docusignAuditRepository
							.findByOrderRefUuid(quoteEntity.get().getQuoteCode());
					if (docuSignAuditEntity != null) {
						LOGGER.warn("Deleting the entry in the docusign audit as the Docusign is Failed due to {}",
								docuSignResponse.getErrorMessage());
						docusignAuditRepository.delete(docuSignAuditEntity);
					}
				} else {
					DocusignAudit docuSignAuditEntity = docusignAuditRepository
							.findByOrderRefUuid(quoteEntity.get().getQuoteCode());
					if (docuSignAuditEntity != null) {
						LOGGER.info("Docusign stage is : {}", docuSignResponse.getType());
						if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							LOGGER.info("Inside CUSTOMER : {}", docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setCustomerEnvelopeId(docuSignResponse.getEnvelopeId());
							sendAck = true;
							docuSignAuditEntity.setStatus(DocuSignStatus.PENDING_WITH_CUSTOMER.toString());
						} else if (DocuSignStage.REVIEWER1.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							LOGGER.info("Inside REVIEWER1 : {}", docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setStatus(DocuSignStatus.PENDING_WITH_REVIEWER1.toString());
							docuSignAuditEntity.setApproverOneEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							LOGGER.info("Inside REVIEWER2 : {}", docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setApproverTwoEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.SUPPLIER.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							LOGGER.info("Inside SUPPLIER : {}", docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setSupplierEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							LOGGER.info("Inside CUSTOMER1 : {}", docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setCustomerOneEnvelopeId(docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setStatus(DocuSignStatus.PENDING_WITH_CUSTOMER1.toString());
						} else if (DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							LOGGER.info("Inside CUSTOMER2 : {}", docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setCustomerTwoEnvelopeId(docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setStatus(DocuSignStatus.PENDING_WITH_CUSTOMER2.toString());
						} else if (DocuSignStage.COMMERCIAL.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							LOGGER.info("Inside COMMERCIAL : {}", docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setCommercialEnvelopeId(docuSignResponse.getEnvelopeId());
							docuSignAuditEntity.setStatus(DocuSignStatus.PENDING_WITH_COMMERCIAL.toString());
						}
						docuSignAuditEntity.setUpdatedTime(new Date());
						docusignAuditRepository.save(docuSignAuditEntity);
					} else {
						docuSignAuditEntity = new DocusignAudit();
						docuSignAuditEntity.setCreatedTime(new Date());
						if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							docuSignAuditEntity.setCustomerEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.SUPPLIER.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							docuSignAuditEntity.setSupplierEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.REVIEWER1.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							docuSignAuditEntity.setApproverOneEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							docuSignAuditEntity.setApproverTwoEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							docuSignAuditEntity.setCustomerOneEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							docuSignAuditEntity.setCustomerTwoEnvelopeId(docuSignResponse.getEnvelopeId());
						} else if (DocuSignStage.COMMERCIAL.toString().equalsIgnoreCase(docuSignResponse.getType())) {
							docuSignAuditEntity.setCommercialEnvelopeId(docuSignResponse.getEnvelopeId());
						}
						docuSignAuditEntity.setOrderRefUuid(quoteEntity.get().getQuoteCode());
						docuSignAuditEntity.setStage(docuSignResponse.getType());
						LOGGER.info("Stage set");
						docuSignAuditEntity.setUpdatedTime(new Date());
						docusignAuditRepository.save(docuSignAuditEntity);

					}
					if (sendAck) {
						processDocusignSentConfirmationNotification(quoteEntity.get());
					}
				}
			}
		}

	}

	/**
	 *
	 * processDocuSignErrorNotificationResponse
	 *
	 * @param envelopeResponse
	 * @param envelopeId
	 */
	public void processDocuSignErrorNotificationResponse(String envelopeResponse, String envelopeId) {
		try {
			LOGGER.info("Received the envelopeResponse {} :: envelop Id {}", envelopeResponse, envelopeId);
			ThirdPartyServiceJob thirdServiceJob = new ThirdPartyServiceJob();
			thirdServiceJob.setCreatedBy(Utils.getSource());
			thirdServiceJob.setCreatedTime(new Date());
			thirdServiceJob.setQueueName(docuSignRetryQueue);
			thirdServiceJob.setRetryCount(1);
			thirdServiceJob.setRefId(envelopeId);
			thirdServiceJob.setSeqNum(1);
			thirdServiceJob.setRequestPayload(envelopeResponse);
			thirdServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
			thirdServiceJob.setThirdPartySource(ThirdPartySource.DOCUSIGN.toString());
			thirdPartyServiceJobsRepository.save(thirdServiceJob);
		} catch (Exception e) {
			LOGGER.warn("Cannot process DocuSign notification response {}", ExceptionUtils.getStackTrace(e));
		}
	}

	public void processDocuSignErrorNotificationResponse(CommonDocusignResponse docuSignResponse) {
		try {
			List<ThirdPartyServiceJob> thirdPartySourceEntity = thirdPartyServiceJobsRepository
					.findByRefIdAndThirdPartySource(docuSignResponse.getEnvelopeId(),
							ThirdPartySource.DOCUSIGN.toString());
			if (thirdPartySourceEntity.size() <= 3) {
				ThirdPartyServiceJob thirdServiceJob = new ThirdPartyServiceJob();
				thirdServiceJob.setCreatedBy(Utils.getSource());
				thirdServiceJob.setCreatedTime(new Date());
				thirdServiceJob.setQueueName(docuSignRetryQueue);
				thirdServiceJob.setRetryCount(thirdPartySourceEntity.size());
				thirdServiceJob.setSeqNum(1);
				thirdServiceJob.setRefId(docuSignResponse.getEnvelopeId());
				thirdServiceJob.setRequestPayload(docuSignResponse.getEnvelopeResponse());
				thirdServiceJob.setServiceStatus(SfdcServiceStatus.NEW.toString());
				thirdServiceJob.setThirdPartySource(ThirdPartySource.DOCUSIGN.toString());
				thirdPartyServiceJobsRepository.save(thirdServiceJob);
			}
		} catch (Exception e) {
			LOGGER.warn("Docusign Error  {}", ExceptionUtils.getStackTrace(e));
		}
	}

	@Transactional(readOnly = false, rollbackFor = { TclCommonException.class, RuntimeException.class })
	public String processDocuSignNotificationResponse(CommonDocusignResponse docuSignResponse)
			throws TclCommonException {
		Quote quoteEntity = null;
		DocusignAudit docuSignAudit = null;
		try {
			LOGGER.info("Docusign Response Type "+docuSignResponse.getType());
			if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(docuSignResponse.getType())
					|| DocuSignStage.REVIEWER1.toString().equalsIgnoreCase(docuSignResponse.getType())
					|| DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(docuSignResponse.getType())
					|| DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(docuSignResponse.getType())
					|| DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(docuSignResponse.getType())
					|| DocuSignStage.COMMERCIAL.toString().equalsIgnoreCase(docuSignResponse.getType())) {
				LOGGER.info("Inside Commercial Condition");
				docuSignAudit = getDocuSignAuditBasedOnStage(docuSignResponse, docuSignAudit);
				if (docuSignAudit != null) {
					LOGGER.info("Docusign Audit received is for envelopeId {}", docuSignResponse.getEnvelopeId());
					quoteEntity = quoteRepository.findByQuoteCode(docuSignAudit.getOrderRefUuid());
					docuSignAudit.setStatus(docuSignResponse.getDocusignStatus());
					String stageType = docuSignResponse.getType();
					LOGGER.info("Docusign Stage is -----> {}  for quote code -------> {}  and envelope id ------>  {}",
							stageType, docuSignAudit.getOrderRefUuid(), docuSignResponse.getEnvelopeId());
					docuSignAudit.setStage(Objects.nonNull(stageType) ? stageType : "");
					String statusType = null;
					if (docuSignResponse.getDocusignStatus().contains(DocusignConstants.APPROVED)
							|| docuSignResponse.getDocusignStatus().contains(DocusignConstants.SIGNED))
						statusType = DocusignConstants.APPROVED;

					CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
					if (Objects.nonNull(statusType) && statusType.equalsIgnoreCase(DocusignConstants.APPROVED)
							&& Objects.nonNull(stageType)) {
						processOrderConfAudit(docuSignResponse, docuSignAudit);
						switch (stageType) {
						case CUSTOMER: {
							LOGGER.info("Entering Stage Customer for quote Code ----> {}",
									docuSignAudit.getOrderRefUuid());
							if (docuSignAudit.getCustomerSignedDate() == null
									&& docuSignAudit.getStage().equals(CUSTOMER)) {
								docuSignAudit.setStage(DocuSignStage.SUPPLIER.toString());
								docuSignAudit.setCustomerSignedDate(new Date());
								commonDocusignRequest = constructSupplierDocuSignRequest(docuSignAudit, quoteEntity);
								processDocusignSignedConfirmationNotification(quoteEntity);
								break;
							} else {
								LOGGER.info("Already processed , so ignoring");
								return "";
							}
						}

						case REVIEWER1: {
							if (docuSignAudit.getApproverOneSignedDate() == null
									&& docuSignAudit.getStage().equals(REVIEWER1)) {
								if (Objects.nonNull(docuSignAudit.getApproverTwoEmail())) {
									docuSignAudit.setStage(DocuSignStage.REVIEWER2.toString());
								} else if (Objects.nonNull(docuSignAudit.getCustomerOneEmail())) {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER1.toString());
								} else {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER.toString());
								}

								docuSignAudit.setApproverOneSignedDate(new Date());
								commonDocusignRequest = constructCustomerDocuSignRequest(docuSignAudit, quoteEntity);
								break;
							} else {
								LOGGER.info("Already processed , so ignoring");
								return "";
							}
						}

						case REVIEWER2: {
							if (docuSignAudit.getApproverTwoSignedDate() == null
									&& docuSignAudit.getStage().equals(REVIEWER2)) {
								if (Objects.nonNull(docuSignAudit.getCustomerOneEmail())) {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER1.toString());
								} else {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER.toString());
								}
								docuSignAudit.setApproverTwoSignedDate(new Date());
								commonDocusignRequest = constructCustomerDocuSignRequest(docuSignAudit, quoteEntity);
								break;
							} else {
								LOGGER.info("Already processed , so ignoring");
								return "";
							}
						}

						case CUSTOMER1: {
							if (docuSignAudit.getCustomerOneSignedDate() == null
									&& docuSignAudit.getStage().equals(CUSTOMER1)) {
								if (Objects.nonNull(docuSignAudit.getCustomerTwoEmail())) {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER2.toString());
								} else {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER.toString());
								}
								docuSignAudit.setCustomerOneSignedDate(new Date());
								commonDocusignRequest = constructCustomerDocuSignRequest(docuSignAudit, quoteEntity);
								break;
							} else {
								LOGGER.info("Already processed , so ignoring");
								return "";
							}
						}

						case CUSTOMER2: {
							if (docuSignAudit.getCustomerTwoSignedDate() == null
									&& docuSignAudit.getStage().equals(CUSTOMER2)) {
								docuSignAudit.setStage(DocuSignStage.CUSTOMER.toString());
								docuSignAudit.setCustomerTwoSignedDate(new Date());
								commonDocusignRequest = constructCustomerDocuSignRequest(docuSignAudit, quoteEntity);
								break;
							} else {
								LOGGER.info("Already processed , so ignoring");
								return "";
							}
						}
						case COMMERCIAL: {

							if (docuSignAudit.getStage().equals(COMMERCIAL)) {
								LOGGER.info("Inside Commercial Condition create Customer");
								if (Objects.nonNull(docuSignAudit.getCustomerOneEmail())) {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER.toString());
								} else {
									docuSignAudit.setStage(DocuSignStage.CUSTOMER.toString());
								}
								docuSignAudit.setCommercialSignedDate(new Date());
								commonDocusignRequest = constructCustomerDocuSignRequest(docuSignAudit, quoteEntity);
								break;
							} else {
								LOGGER.info("Already processed , so ignoring");
								return "";
							}
						}
						default:
							break;
						}
						return Utils.convertObjectToJson(commonDocusignRequest);
					} else {
						processOrderConfAudit(docuSignResponse, docuSignAudit);
					}

					docuSignAudit.setUpdatedTime(new Date());
					LOGGER.info("docusign Audit Objevt "+docuSignAudit.toString());
					docusignAuditRepository.save(docuSignAudit);
				}
			} else {
				docuSignAudit = docusignAuditRepository.findBySupplierEnvelopeId(docuSignResponse.getEnvelopeId());
				LOGGER.info("Docusign Audit received is for envelope Id {}", docuSignResponse.getEnvelopeId());

				if (docuSignAudit != null) {
					if (docuSignAudit.getSupplierSignedDate() == null
							&& (docuSignAudit.getStage().equals(DocuSignStage.SUPPLIER.toString()))) {
					quoteEntity = quoteRepository.findByQuoteCode(docuSignAudit.getOrderRefUuid());
					OrderConfirmationAudit orderConfAudit = orderConfirmationAuditRepository
							.findByOrderRefUuid(docuSignAudit.getOrderRefUuid());
					if (orderConfAudit != null) {
						orderConfAudit.setSupplierDocuSignResponse(docuSignResponse.getEnvelopeResponse());
						orderConfAudit.setMode(AuditMode.DOCUSIGN.toString());
						orderConfAudit.setSupplierIp(docuSignResponse.getIp());
						orderConfirmationAuditRepository.save(orderConfAudit);
					}

					if (Objects.nonNull(docuSignResponse.getDocusignStatus()) && docuSignResponse.getDocusignStatus()
							.equalsIgnoreCase(DocuSignStatus.SUPPLIER_DECLINED.toString())) {
						docuSignAudit.setStage(DocuSignStage.INCOMPLETE.toString());
						docuSignAudit.setStatus(docuSignResponse.getDocusignStatus());
					} else if (docuSignResponse.getDocusignStatus()
							.equalsIgnoreCase(DocuSignStatus.SUPPLIER_SIGNED.toString())) {
						docuSignAudit.setStage(DocuSignStage.COMPLETED.toString());
						docuSignAudit.setSupplierSignedDate(new Date());
						docuSignAudit.setStatus(docuSignResponse.getDocusignStatus());
					}
					docuSignAudit.setUpdatedTime(new Date());
					docusignAuditRepository.save(docuSignAudit);
					LOGGER.info("DocuSign Audit updated for envelope Id {}", docuSignAudit.getOrderRefUuid());

						if (docuSignAudit.getStage().equalsIgnoreCase(DocuSignStage.COMPLETED.toString())) {
							if (docuSignResponse.getObjUrl() == null) {
								CofDetails cofDetails = cofDetailsRepository
										.findByOrderUuid(docuSignAudit.getOrderRefUuid());
								LOGGER.info("Cof Details received {}", cofDetails);
								if (cofDetails == null) {
									LOGGER.info("Entering the Non Object storage flow");
									cofDetails = new CofDetails();
									cofDetails.setOrderUuid(docuSignAudit.getOrderRefUuid());
									cofDetails.setUriPath(docuSignResponse.getPath());
									cofDetails.setSource(Source.DOCUSIGN_COF.getSourceType());
									cofDetails.setCreatedBy(docuSignResponse.getName());
									cofDetails.setCreatedTime(new Timestamp((new Date().getTime())));
									cofDetailsRepository.save(cofDetails);
									LOGGER.info("Cof Details updated");
									processForApproveQuotes(docuSignAudit.getOrderRefUuid());
									Map<String,String> req=new HashMap<>();
									req.put("STATUS", "SUCCESS");
									req.put("ENVELOPE_ID", docuSignResponse.getEnvelopeId());
									mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
								
								}
							} else {
								LOGGER.info("Entering the Object storage flow");
								if (quoteEntity != null) {
									for (QuoteToLe quoteToLe : quoteEntity.getQuoteToLes()) {
										LOGGER.info("Saving the CofUploaded Details with {}, {} ",
												docuSignResponse.getRequestId(), docuSignResponse.getObjUrl());
										updateCofUploadedDetails(quoteEntity.getId(), quoteToLe.getId(),
												docuSignResponse.getRequestId(), docuSignResponse.getObjUrl());
									}
								}
								processForApproveQuotes(docuSignAudit.getOrderRefUuid());
								Map<String,String> req=new HashMap<>();
								req.put("STATUS", "SUCCESS");
								req.put("ENVELOPE_ID", docuSignResponse.getEnvelopeId());
								mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
							}
							Map<String,String> req=new HashMap<>();
							req.put("STATUS", "SUCCESS");
							req.put("ENVELOPE_ID", docuSignResponse.getEnvelopeId());
							mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
						}
					} else {
						LOGGER.info("Already processed , so ignoring");
						return "";
					}
				}

			}
		} catch (Exception e) {
			Map<String,String> req=new HashMap<>();
			req.put("STATUS", "FAILURE");
			req.put("ENVELOPE_ID", docuSignResponse.getEnvelopeId());
			mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
			LOGGER.error("Cannot process DocuSign notification response",e);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return "";

	}

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
					throw new TclCommonException(ExceptionConstants.QUOTE_TO_LE_VALIDATION_ERROR,
							ResponseResource.R_CODE_ERROR);

				Quote quote = quoteToLe.get().getQuote();
				Order order = orderRepository.findByQuoteAndStatus(quote, quote.getStatus());

				String attachmentrequest = Utils.convertObjectToJson(attachmentBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				Integer attachmentId = (Integer) mqUtils.sendAndReceive(attachmentQueue, attachmentrequest);
				LOGGER.info("Received the Attachment response with attachment Id {}", attachmentId);
				List<OmsAttachment> omsAttachmentList = omsAttachmentRepository
						.findByQuoteToLeAndAttachmentType(quoteToLe.get(), AttachmentTypeConstants.COF.toString());
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
				LOGGER.info("Oms Attachment Saved with Id  {}", omsAttach.getId());
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

	private DocusignAudit getDocuSignAuditBasedOnStage(CommonDocusignResponse docuSignResponse,
			DocusignAudit docuSignAudit) {
		if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(docuSignResponse.getType())) {
			docuSignAudit = docusignAuditRepository.findByCustomerEnvelopeId(docuSignResponse.getEnvelopeId());
		}
		else if (DocuSignStage.REVIEWER1.toString().equalsIgnoreCase(docuSignResponse.getType())) {
			docuSignAudit = docusignAuditRepository.findByApproverOneEnvelopeId(docuSignResponse.getEnvelopeId());
		}
		else if (DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(docuSignResponse.getType())) {
			docuSignAudit = docusignAuditRepository.findByApproverTwoEnvelopeId(docuSignResponse.getEnvelopeId());
		}
		else if(DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(docuSignResponse.getType())) {
			docuSignAudit = docusignAuditRepository.findByCustomerOneEnvelopeId(docuSignResponse.getEnvelopeId());
		}
		else if(DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(docuSignResponse.getType())) {
			docuSignAudit = docusignAuditRepository.findByCustomerTwoEnvelopeId(docuSignResponse.getEnvelopeId());
		}
		else if(DocuSignStage.COMMERCIAL.toString().equalsIgnoreCase(docuSignResponse.getType())) {
			LOGGER.info("Inside Commercial Data Fetch");
			docuSignAudit = docusignAuditRepository.findByCommercialEnvelopeId(docuSignResponse.getEnvelopeId());
		}
		return docuSignAudit;
	}

	private void processOrderConfAudit(CommonDocusignResponse docuSignResponse, DocusignAudit docuSignAudit) {
		OrderConfirmationAudit orderConfAudit = orderConfirmationAuditRepository
				.findByOrderRefUuid(docuSignAudit.getOrderRefUuid());
		if (orderConfAudit != null) {
			orderConfAudit.setCustomerDocuSignResponse(docuSignResponse.getEnvelopeResponse());
			orderConfAudit.setMode(AuditMode.DOCUSIGN.toString());
			orderConfAudit.setPublicIp(docuSignResponse.getIp());
			orderConfirmationAuditRepository.save(orderConfAudit);
		} else {
			orderConfAudit = new OrderConfirmationAudit();
			orderConfAudit.setCreatedTime(new Date());
			orderConfAudit.setCustomerDocuSignResponse(docuSignResponse.getEnvelopeResponse());
			orderConfAudit.setMode(AuditMode.DOCUSIGN.toString());
			orderConfAudit.setOrderRefUuid(docuSignAudit.getOrderRefUuid());
			orderConfAudit.setPublicIp(docuSignResponse.getIp());
			orderConfAudit.setName(docuSignResponse.getName());
			orderConfAudit.setCreatedTimeUnix(new Timestamp(System.currentTimeMillis()));
			orderConfirmationAuditRepository.save(orderConfAudit);
		}

	}

	public abstract void processForApproveQuotes(String orderRefUuid) throws TclCommonException ;

	/**
	 * constructSupplierDocuSignRequest
	 * 
	 * @param docuSignAudit
	 * @param quoteEntity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private CommonDocusignRequest constructSupplierDocuSignRequest(DocusignAudit docuSignAudit, Quote quoteEntity)
			throws TclCommonException {
		CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
		List<String> anchorStrings = new ArrayList<>();
		anchorStrings.add(PDFConstants.SUPPLIER_SIGNATURE);
		List<String> dateSignedStrings = new ArrayList<>();
		dateSignedStrings.add(PDFConstants.SUPPLIER_SIGNED_DATE);
		List<String> nameStrings = new ArrayList<>();
		nameStrings.add(PDFConstants.SUPPLIER_NAME);
		commonDocusignRequest.setAnchorStrings(anchorStrings);
		commonDocusignRequest.setDateSignedAnchorStrings(dateSignedStrings);
		commonDocusignRequest.setCustomerNameAnchorStrings(nameStrings);
		commonDocusignRequest.setDocumentId("2");
		commonDocusignRequest.setFileName("Customer-Order-Form - " + docuSignAudit.getOrderRefUuid() + ".pdf");
		commonDocusignRequest.setQuoteId(quoteEntity.getId());
		for (QuoteToLe quoteToLe : quoteEntity.getQuoteToLes()) {
			commonDocusignRequest.setQuoteLeId(quoteToLe.getId());
		}
		commonDocusignRequest.setType(DocuSignStage.SUPPLIER.toString());
		commonDocusignRequest.setSubject("Please sign this cof document!!!");
		commonDocusignRequest.setToName("Tatacommunication Support");
		Map<String, String> accountManagerDetails = getAccountManagerDetails(quoteEntity);
		if (accountManagerDetails != null && !accountManagerDetails.isEmpty()) {
			commonDocusignRequest.setCcEmails(accountManagerDetails);
		}

		// DOP Segment mapping logic start
		Map<String,String> dopMapper = null;
		String customerSegment = null;
		String accountManagerName = null;
		if (quoteEntity.getQuoteToLes().size() == 1) {
			QuoteToLe quoteToLe = quoteEntity.getQuoteToLes().stream().findFirst().get();
			CustomerDetailsBean customerDetails = processCustomerData(
					quoteToLe.getQuote().getCustomer().getErfCusCustomerId());
			for (CustomerAttributeBean attribute : customerDetails.getCustomerAttributes()) {
				if (attribute.getName().equals(CustomerAttributeConstants.CUSTOMER_TYPE.getAttributeValue())) {
					customerSegment = attribute.getValue();
					LOGGER.info("Existing Customer Segment-" + customerSegment);
					if (customerSegment!=null && customerSegment.equalsIgnoreCase("Carriers")) {
						customerSegment = CommonConstants.SP_INDIA;
					} else {
						customerSegment = CommonConstants.ENTERPRISE_DIRECT;
					}

				}
			}
			LOGGER.info("Customer Segment-" + customerSegment);
			if (accountManagerDetails.size() == 1) {
				Set<String> keys = accountManagerDetails.keySet();
				Iterator<String> iterator = keys.iterator();
				while (iterator.hasNext()) {
					accountManagerName = (String) iterator.next();
					break;
				}
			}
			LOGGER.info("Account Manager-" + accountManagerName);
			SignDetailBean dopEmailRequest = new SignDetailBean();
			Double convertedCurrencyUSD = 0D;
			dopEmailRequest.setAccountManagerName(accountManagerName);
			dopEmailRequest.setCustomerSegment(customerSegment);
			String existingCurrency = quoteToLe.getCurrencyCode();
			if (Objects.nonNull(existingCurrency) && existingCurrency.equalsIgnoreCase(CommonConstants.INR)) {
				convertedCurrencyUSD = omsUtilService.convertCurrency(CommonConstants.INR, CommonConstants.USD,
						quoteToLe.getTotalTcv());
			} else if (existingCurrency.equalsIgnoreCase(CommonConstants.USD)) {
				convertedCurrencyUSD = quoteToLe.getTotalTcv();
			}
			dopEmailRequest.setOrderValue(convertedCurrencyUSD);
			LOGGER.info("Existing Order Value" + quoteToLe.getTotalTcv());
			LOGGER.info("Converted Order Value" + convertedCurrencyUSD);
			if (validateDopEmailRequest(dopEmailRequest)) {
				Object response = mqUtils.sendAndReceive(dopEmailQueue, Utils.convertObjectToJson(dopEmailRequest));
				if(response!=null) {
					dopMapper =Utils.convertJsonToObject((String)response, Map.class);
				}
			}
			LOGGER.info("DOP Mapper-" + dopMapper);
			if (dopMapper!=null && !dopMapper.isEmpty()) {
				commonDocusignRequest.setToEmail(dopMapper.get("EMAIL"));
				commonDocusignRequest.setToName(dopMapper.get("NAME"));
			} else {
				commonDocusignRequest.setToEmail(customerSupportEmail);
			}
		} else {
			commonDocusignRequest.setToEmail(customerSupportEmail);
		}
		// end
		if (StringUtils.isBlank(commonDocusignRequest.getToEmail())) {
			LOGGER.info("Email Configured for empty as it is older {}", commonDocusignRequest.getToEmail());
			commonDocusignRequest.setToEmail(customerSupportEmail);
		}
		LOGGER.info("Email Configured for supplier" + commonDocusignRequest.getToEmail());
		ApproverListBean approverListBean = new ApproverListBean();
		approverListBean.setSupplierStatus(DocuSignStage.SUPPLIER.toString());
		auditInTheDocusign(docuSignAudit.getOrderRefUuid(), commonDocusignRequest.getToName(), null,
				commonDocusignRequest.getToEmail(), approverListBean);
		return commonDocusignRequest;
	}

	/**
	 * constructCustomerDocuSignRequest
	 *
	 * @param docuSignAudit
	 * @param quoteEntity
	 * @return
	 */
	private CommonDocusignRequest constructCustomerDocuSignRequest(DocusignAudit docuSignAudit, Quote quoteEntity) {
		CommonDocusignRequest commonDocusignRequest = new CommonDocusignRequest();
		if (Objects.nonNull(quoteEntity)) {
			/*List<String> anchorStrings = new ArrayList<>();
			anchorStrings.add(PDFConstants.CUSTOMER_SIGNATURE);
			List<String> nameStrings = new ArrayList<>();
			nameStrings.add(PDFConstants.CUSTOMER_NAME);
			List<String> dateSignedStrings = new ArrayList<>();
			dateSignedStrings.add(PDFConstants.CUSTOMER_SIGNED_DATE);
			commonDocusignRequest.setAnchorStrings(anchorStrings);
			commonDocusignRequest.setDateSignedAnchorStrings(dateSignedStrings);
			commonDocusignRequest.setCustomerNameAnchorStrings(nameStrings);*/
			commonDocusignRequest.setFileName("Customer-Order-Form - " + docuSignAudit.getOrderRefUuid() + ".pdf");
			commonDocusignRequest.setQuoteId(quoteEntity.getId());
			for (QuoteToLe quoteToLe : quoteEntity.getQuoteToLes()) {
				commonDocusignRequest.setQuoteLeId(quoteToLe.getId());
			}
			QuoteToLe quoteToLe = quoteToLeRepository.findByQuote(quoteEntity).get(0);
			String prodName = quoteToLe.getQuoteToLeProductFamilies().stream().findFirst().get().getMstProductFamily()
					.getName();
			String type = StringUtils.isBlank(quoteToLe.getQuoteType()) ? "NEW" : quoteToLe.getQuoteType();
			if (DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(docuSignAudit.getStage())) {
				commonDocusignRequest.setToName(docuSignAudit.getApproverTwoName());
				commonDocusignRequest.setToEmail(docuSignAudit.getApproverTwoEmail());
				commonDocusignRequest.setType(docuSignAudit.getStage());
				commonDocusignRequest.setDocumentId("4");
				List<String> anchorStrings = new ArrayList<>();
				anchorStrings.add(PDFConstants.CUSTOMER_SIGNATURE);
				List<String> nameStrings = new ArrayList<>();
				nameStrings.add(PDFConstants.CUSTOMER_NAME);
				List<String> dateSignedStrings = new ArrayList<>();
				dateSignedStrings.add(PDFConstants.CUSTOMER_SIGNED_DATE);
				commonDocusignRequest.setAnchorStrings(anchorStrings);
				commonDocusignRequest.setDateSignedAnchorStrings(dateSignedStrings);
				commonDocusignRequest.setCustomerNameAnchorStrings(nameStrings);
				List<String> approver2SignedDate = new ArrayList<>();
				approver2SignedDate.add(PDFConstants.APPROVER_2_SIGNED_DATE);
				commonDocusignRequest.setApproverDateAnchorStrings(approver2SignedDate);
			} else if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(docuSignAudit.getStage())) {
				commonDocusignRequest.setToName(docuSignAudit.getCustomerName());
				commonDocusignRequest.setToEmail(docuSignAudit.getCustomerEmail());
				commonDocusignRequest.setType(docuSignAudit.getStage());
				commonDocusignRequest.setDocumentId("1");
				commonDocusignRequest.setAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_SIGNATURE));
				commonDocusignRequest.setCustomerNameAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_NAME));
				commonDocusignRequest.setDateSignedAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER_SIGNED_DATE));
			} else if (DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(docuSignAudit.getStage())) {
				commonDocusignRequest.setToName(docuSignAudit.getCustomerOneName());
				commonDocusignRequest.setToEmail(docuSignAudit.getCustomerOneEmail());
				commonDocusignRequest.setType(docuSignAudit.getStage());
				commonDocusignRequest.setDocumentId("5");
				commonDocusignRequest.setAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_SIGNATURE));
				commonDocusignRequest.setCustomerNameAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_NAME));
				commonDocusignRequest.setDateSignedAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER1_SIGNED_DATE));
			} else if (DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(docuSignAudit.getStage())) {
				commonDocusignRequest.setToName(docuSignAudit.getCustomerTwoName());
				commonDocusignRequest.setToEmail(docuSignAudit.getCustomerTwoEmail());
				commonDocusignRequest.setType(docuSignAudit.getStage());
				commonDocusignRequest.setDocumentId("6");
				commonDocusignRequest.setAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER2_SIGNATURE));
				commonDocusignRequest.setCustomerNameAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER2_NAME));
				commonDocusignRequest.setDateSignedAnchorStrings(Arrays.asList(PDFConstants.CUSTOMER2_SIGNED_DATE));
			}

			if(CommonConstants.PROD.equalsIgnoreCase(appEnv)) {
				commonDocusignRequest.setSubject(
						"Tata Communications: " + prodName + " / " + commonDocusignRequest.getToName() + " / " + type);
			} else {
				commonDocusignRequest.setSubject(
						mqHostName+":::Test::: Tata Communications: " + prodName + " / " + commonDocusignRequest.getToName() + " / " + type);
			}

			return commonDocusignRequest;
		}
		return null;
	}

	/**
	 * Get DocuSign Audit details for the Quote
	 * 
	 * @author ANANDHI VIJAY
	 * 
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public DocusignAuditBean getDocusignAuditDetails(Integer quoteId) throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		Optional<Quote> quote = quoteRepository.findById(quoteId);
		LOGGER.info("Quote received is {}", quote);
		if (!quote.isPresent()) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		try {
			DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.get().getQuoteCode());
			if (docusignAudit != null) {
				return constructDocusignAuditBean(docusignAudit, quote.get().getQuoteCode());
			}
		} catch (Exception e) {
			LOGGER.warn("Cannot get DocuSign Audit Details");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return null;
	}

	private DocusignAuditBean constructDocusignAuditBean(DocusignAudit docusignAudit, String quoteCode) {
		DocusignAuditBean docusignAuditBean = new DocusignAuditBean();
		docusignAuditBean.setCustomerEnvelopeId(docusignAudit.getCustomerEnvelopeId());
		docusignAuditBean.setSupplierEnvelopeId(docusignAudit.getSupplierEnvelopeId());
		docusignAuditBean.setDocusignAuditId(docusignAudit.getId());
		docusignAuditBean.setOrderRefId(quoteCode);
		docusignAuditBean.setStage(docusignAudit.getStage());
		docusignAuditBean.setStatus(getStatusBasedOnStageAndStatus(docusignAudit));
		docusignAuditBean.setCustomerSignedDate(docusignAudit.getCustomerSignedDate());
		docusignAuditBean.setSupplierSignedDate(docusignAudit.getSupplierSignedDate());
		docusignAuditBean.setApproverOneEnvelopeId(docusignAudit.getApproverOneEnvelopeId());
		docusignAuditBean.setApproverTwoEnvelopeId(docusignAudit.getApproverTwoEnvelopeId());
		docusignAuditBean.setCustomerOneEnvelopeId(docusignAudit.getCustomerOneEnvelopeId());
		docusignAuditBean.setCustomerTwoEnvelopeId(docusignAudit.getCustomerTwoEnvelopeId());
		return docusignAuditBean;
	}

	private String getStatusBasedOnStageAndStatus(DocusignAudit docusignAudit) {

		String result = "";

		switch (docusignAudit.getStage()) {
			case DocusignConstants.CUSTOMER: {
				result = getResultForStageCustomer(docusignAudit, result);

				break;
			}
			case DocusignConstants.SUPPLIER: {
				result = getResultForStageSupplier(docusignAudit, result);
				break;
			}
			case DocusignConstants.COMPLETED: {
				result = getResultForStageCompleted(docusignAudit, result);
				break;
			}
			case DocusignConstants.INCOMPLETE: {
				result = getResultForStageIncomplete(docusignAudit, result);
				break;
			}

			case DocusignConstants.REVIEWER1: {
				result = getResultForStageReviewer1(docusignAudit, result);
				break;
			}
			case DocusignConstants.REVIEWER2: {
				result = getResultForStageReviewer2(docusignAudit, result);
				break;
			}
			case DocusignConstants.CUSTOMER1: {
				result = getResultForStageCustomer1(docusignAudit, result);
				break;
			}
			case DocusignConstants.CUSTOMER2: {
				result = getResultForStageCustomer2(docusignAudit, result);
				break;
			}
			default: {
				break;
			}
		}
		return result;
	}

	private String getResultForStageIncomplete(DocusignAudit docusignAudit, String result) {
		if (docusignAudit.getStatus().equalsIgnoreCase(DocusignConstants.SUPPLIER_DECLINED)) {
			result = "Rejected by TCL Signatory " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	private String getResultForStageReviewer2(DocusignAudit docusignAudit, String result) {
		if (DocuSignStatus.REVIEWER1_APPROVED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Signature Pending from Reviewer2 " + docusignAudit.getUpdatedTime();
		} else if (DocuSignStatus.REVIEWER2_DECLINED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Rejected By Reviewer2 " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	private String getResultForStageReviewer1(DocusignAudit docusignAudit, String result) {
		if (DocuSignStatus.PENDING_WITH_REVIEWER1.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Signature Pending from Reviewer1 " + docusignAudit.getUpdatedTime();
		} else if (DocuSignStatus.REVIEWER1_DECLINED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Rejected By Reviewer1 " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	private String getResultForStageCompleted(DocusignAudit docusignAudit, String result) {
		if (docusignAudit.getStatus().equalsIgnoreCase(DocusignConstants.SUPPLIER_SIGNED)) {
			result = "Signature process completed " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	private String getResultForStageSupplier(DocusignAudit docusignAudit, String result) {
		if (docusignAudit.getStatus().equalsIgnoreCase(DocusignConstants.CUSTOMER_SIGNED)) {
			result = "Signature Pending from TCL Signatory " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	private String getResultForStageCustomer2(DocusignAudit docusignAudit, String result) {
		if (DocuSignStatus.CUSTOMER1_SIGNED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Signature Pending from Customer Signer 2 " + docusignAudit.getUpdatedTime();
		} else if (DocuSignStatus.CUSTOMER2_DECLINED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Rejected By Customer Signer 2 " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	private String getResultForStageCustomer1(DocusignAudit docusignAudit, String result) {
		if (DocuSignStatus.REVIEWER1_APPROVED.toString().equalsIgnoreCase(docusignAudit.getStatus())
				|| DocuSignStatus.REVIEWER2_APPROVED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Signature Pending from Customer Signer 1 " + docusignAudit.getUpdatedTime();
		} else if (DocuSignStatus.CUSTOMER1_DECLINED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Rejected By Customer Signer 1 " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	private String getResultForStageCustomer(DocusignAudit docusignAudit, String result) {
		if (DocuSignStatus.PENDING_WITH_CUSTOMER.toString().equalsIgnoreCase(docusignAudit.getStatus())
				|| DocuSignStatus.REVIEWER1_APPROVED.toString().equalsIgnoreCase(docusignAudit.getStatus())
				|| DocuSignStatus.REVIEWER2_APPROVED.toString().equalsIgnoreCase(docusignAudit.getStatus())
				|| DocuSignStatus.CUSTOMER1_SIGNED.toString().equalsIgnoreCase(docusignAudit.getStatus())
				|| DocuSignStatus.CUSTOMER2_SIGNED.toString().equalsIgnoreCase(docusignAudit.getStatus())) {
			result = "Signature Pending from Customer " + docusignAudit.getUpdatedTime();
		} else if (docusignAudit.getStatus().equalsIgnoreCase(DocusignConstants.CUSTOMER_DECLINED)) {
			result = "Rejected by Customer " + docusignAudit.getUpdatedTime();
		} else if (docusignAudit.getStatus().equalsIgnoreCase(DocusignConstants.CUSTOMER_SIGNED)) {
			result = "Signature Pending from TCL Signatory " + docusignAudit.getUpdatedTime();
		}
		return result;
	}

	/**
	 * @author ANANDHI VIJAY Modifies the docusign envelope accoring to the action
	 *         provided
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	public String modifyDocusignDocumentBasedOnAction(Integer quoteId, String action) throws TclCommonException {
		if (quoteId == null || action == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Quote id received is {}", quoteId);
		switch (action) {
		case DocusignConstants.DELETE:
			LOGGER.info("deleting docusign");
			return deleteDocusignByQuoteId(quoteId);
		case DocusignConstants.RESEND:
			LOGGER.info("resending docusign");
			return resendDocusignByQuoteId(quoteId);
		default:
			break;
		}
		return null;
	}

	/**
	 * @author ANANDHI VIJAY Delete Docusign Document By QuoteId
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private String deleteDocusignByQuoteId(Integer quoteId) throws TclCommonException {

		Optional<Quote> quote = quoteRepository.findById(quoteId);
		if (!quote.isPresent()) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		try {
			String envelopeId = null;
			DocusignAudit audit = docusignAuditRepository.findByOrderRefUuid(quote.get().getQuoteCode());
			LOGGER.info("Docusign audit details : {}", audit.toString());
			if (Objects.nonNull(audit) && Objects.nonNull(audit.getStage())) {
				envelopeId = getEnvelopeIdForDeleting(audit);

				if (StringUtils.isNotBlank(envelopeId)) {
					LOGGER.info("deleting docusign");
					LOGGER.info("MDC Filter token value in before Queue call deleteDocusignByQuoteId {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					LOGGER.info("Delete docusign queue name {}",auditQueue);
					String response = (String) mqUtils.sendAndReceive("docusign_audit", envelopeId);
					LOGGER.info("Response from delete docusign queue is {}",response);
					docusignAuditRepository.delete(audit);
					return ResponseResource.RES_SUCCESS;
				}
			}

		} catch (Exception e) {
			LOGGER.warn("Cannot delete DocuSign By QuoteId");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return null;
	}

	/**
	 * @author ANANDHI VIJAY Delete Docusign Document By QuoteId
	 * @param quoteId
	 * @return
	 * @throws TclCommonException
	 */
	private String resendDocusignByQuoteId(Integer quoteId) throws TclCommonException {

		Optional<Quote> quote = quoteRepository.findById(quoteId);
		if (!quote.isPresent()) {
			throw new TclCommonException(ExceptionConstants.QUOTE_EMPTY, ResponseResource.R_CODE_ERROR);
		}
		try {
			DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.get().getQuoteCode());
			if (docusignAudit != null && docusignAudit.getStage() != null) {
				String envelopeId = getEnvelopeIdBaseOnStage(docusignAudit);
				if (envelopeId != null) {
					LOGGER.info("MDC Filter token value in before Queue call resendDocusignByQuoteId {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					mqUtils.sendAndReceive(resendQueue, envelopeId);
				}
				return ResponseResource.RES_SUCCESS;
			}

		} catch (Exception e) {
			LOGGER.warn("Cannot resend DocuSign By QuoteId");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_BAD_REQUEST);
		}
		return null;
	}

	private String getEnvelopeIdBaseOnStage(DocusignAudit docusignAudit) {
		switch (docusignAudit.getStage()) {
		case DocusignConstants.CUSTOMER:
			if (docusignAudit.getCustomerEnvelopeId() != null) {
				return docusignAudit.getCustomerEnvelopeId();
			}
			break;
		case DocusignConstants.SUPPLIER:
			if (docusignAudit.getSupplierEnvelopeId() != null) {
				return docusignAudit.getSupplierEnvelopeId();
			}
			break;
		default:
			break;
		}
		return null;
	}

	/**
	 * validateDocuSign
	 * 
	 * @throws TclCommonException
	 */
	public Boolean validateDeleteDocuSign(String orderRefId, String email) throws TclCommonException {
		DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(orderRefId);
		if (docusignAudit == null) {
			return true;
		}
		if (docusignAudit.getStage().equalsIgnoreCase(DocusignConstants.SUPPLIER)) {
			return false;
		}
		if (docusignAudit.getCustomerEnvelopeId() != null
				&& docusignAudit.getStage().equalsIgnoreCase(DocusignConstants.CUSTOMER)) {
			if (docusignAudit.getCustomerEmail() != null && docusignAudit.getCustomerEmail().equals(email)) {
				LOGGER.info("MDC Filter token value in before Queue call validateDeleteDocuSign resend {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(resendQueue, docusignAudit.getCustomerEnvelopeId());
				return false;
			} else {
				LOGGER.info("MDC Filter token value in before Queue call validateDeleteDocuSign delete {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.sendAndReceive(auditQueue, docusignAudit.getCustomerEnvelopeId());
				docusignAuditRepository.delete(docusignAudit);
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 *
	 * This method puts and entry in Audit table respect to the quote code
	 *
	 * @author ANANDHI VIJAY
	 * @param quoteCode
	 * @param customerEmail
	 * @param supplierEmail
	 */
	public void auditInTheDocusign(String quoteCode, String customerName, String customerEmail, String supplierEmail, ApproverListBean approvers) {
		if (quoteCode != null) {
			DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quoteCode);
			if (docusignAudit != null) {
				if (customerEmail != null) {
					docusignAudit.setCustomerEmail(customerEmail);
					docusignAudit.setCustomerName(customerName);
				}

			} else {
				docusignAudit = new DocusignAudit();
				LOGGER.info("DocuSign audit created");
				docusignAudit.setOrderRefUuid(quoteCode);
				docusignAudit.setCreatedTime(new Date());
				if (customerEmail != null) {
					docusignAudit.setCustomerEmail(customerEmail);
					docusignAudit.setCustomerName(customerName);
					/* docusignAudit.setStage(DocuSignStage.CUSTOMER.toString()); */
				}

			}
			LOGGER.info("Supplier Email-" + supplierEmail);
			if (supplierEmail != null) {
				docusignAudit.setSupplierEmail(supplierEmail);
				LOGGER.info(" Docusign Audit Supplier Email-" + supplierEmail);
			}
			if (Objects.nonNull(approvers) && !approvers.getApprovers().isEmpty()) {
				if (approvers.getApprovers().size() == 1) {
					Approver approver1 = approvers.getApprovers().get(0);
					docusignAudit.setApproverOneEmail(approver1.getEmail());
					docusignAudit.setApproverOneName(approver1.getName());
				} else if (approvers.getApprovers().size() == 2) {
					Approver approver1 = approvers.getApprovers().get(0);
					Approver approver2 = approvers.getApprovers().get(1);
					docusignAudit.setApproverOneEmail(approver1.getEmail());
					docusignAudit.setApproverOneName(approver1.getName());
					docusignAudit.setApproverTwoEmail(approver2.getEmail());
					docusignAudit.setApproverTwoName(approver2.getName());

				}
				//docusignAudit.setStage(DocuSignStage.REVIEWER1.toString());
			}

			if(Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
				if (approvers.getCustomerSigners().size() == 1) {
					docusignAudit.setCustomerOneEmail(approvers.getCustomerSigners().get(0).getEmail());
					docusignAudit.setCustomerOneName(approvers.getCustomerSigners().get(0).getName());
				} else if (approvers.getCustomerSigners().size() == 2) {
					docusignAudit.setCustomerOneEmail(approvers.getCustomerSigners().get(0).getEmail());
					docusignAudit.setCustomerOneName(approvers.getCustomerSigners().get(0).getName());
					docusignAudit.setCustomerTwoEmail(approvers.getCustomerSigners().get(1).getEmail());
					docusignAudit.setCustomerTwoName(approvers.getCustomerSigners().get(1).getName());
				}
				//docusignAudit.setStage(DocuSignStage.CUSTOMER1.toString());
			} else {
				if (customerEmail != null) {
					//docusignAudit.setStage(DocuSignStage.CUSTOMER.toString());
				}
			}

			if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCommercialSigners())) {
				
					docusignAudit.setCommercialEmail(approvers.getCommercialSigners().get(0).getEmail());
					docusignAudit.setCommercialName(approvers.getCommercialSigners().get(0).getName());
			
				// docusignAudit.setStage(DocuSignStage.CUSTOMER1.toString());
			}
			docusignAudit.setStage(setDocuSignStage(approvers));
			docusignAudit.setUpdatedTime(new Date());
			docusignAuditRepository.save(docusignAudit);
			LOGGER.info("DocuSign audit updated");
		}
	}  

	private String setDocuSignStage(ApproverListBean approvers) {
		if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCommercialSigners())) {
			return DocuSignStage.COMMERCIAL.toString();
		} else if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getApprovers())) {
			return DocuSignStage.REVIEWER1.toString();
		} else if (Objects.nonNull(approvers) && !CollectionUtils.isEmpty(approvers.getCustomerSigners())) {
			return DocuSignStage.CUSTOMER1.toString();
		} else if (Objects.nonNull(approvers)
				&& DocuSignStage.SUPPLIER.toString().equalsIgnoreCase(approvers.getSupplierStatus())) {
			return DocuSignStage.SUPPLIER.toString();
		}
		return DocuSignStage.CUSTOMER.toString();
	}

	/**
	 * 
	 * Download docusign envelope documents
	 * 
	 * @param quoteId
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	public String getDocumentBasedOnQuoteCode(Integer quoteId, HttpServletResponse response) throws TclCommonException {
		String tempDownloadUrl = null;
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.get().getQuoteCode());
				if (docusignAudit != null && docusignAudit.getStage() != null) {
					DocusignGetDocumentRequest docusignGetDocumentRequest = constructDocusignGetEnvelopeRequest(
							docusignAudit);
					LOGGER.info("Document request is {}", docusignGetDocumentRequest);
					LOGGER.info("MDC Filter token value in before Queue call getDocumentBasedOnQuoteCode {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY));
					byte[] queueResponse = (byte[]) mqUtils.sendAndReceive(getDocumentQueue,
							Utils.convertObjectToJson(docusignGetDocumentRequest));
					if (queueResponse != null) {
						String fileName = "COF_" + quote.get().getQuoteCode() + ".pdf";
						response.reset();
						response.setContentType(MediaType.APPLICATION_PDF_VALUE);
						response.setContentLength(queueResponse.length);
						response.setHeader(PDFConstants.EXPIRES + CommonConstants.COLON, "0");
						response.setHeader(PDFConstants.CONTENT_DISPOSITION,
								ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
						FileCopyUtils.copy(queueResponse, response.getOutputStream());
					}
				}
			}
		} catch (Exception e) {
			LOGGER.warn("Cannot get document based on quote code");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	private DocusignGetDocumentRequest constructDocusignGetEnvelopeRequest(DocusignAudit docusignAudit) {
		DocusignGetDocumentRequest docusignGetDocumentRequest = new DocusignGetDocumentRequest();
		switch (docusignAudit.getStage()) {
		case DocusignConstants.CUSTOMER:
			if (docusignAudit.getCustomerEnvelopeId() != null) {
				docusignGetDocumentRequest.setEnvelopeId(docusignAudit.getCustomerEnvelopeId());
				docusignGetDocumentRequest.setDocumentId(DocusignConstants.DOCUMENT_ONE);
				docusignGetDocumentRequest.setQuoteCode(docusignAudit.getOrderRefUuid());
			}
			break;
		case DocusignConstants.SUPPLIER:
			if (docusignAudit.getSupplierEnvelopeId() != null) {
				docusignGetDocumentRequest.setEnvelopeId(docusignAudit.getSupplierEnvelopeId());
				docusignGetDocumentRequest.setDocumentId(DocusignConstants.DOCUMENT_TWO);
				docusignGetDocumentRequest.setQuoteCode(docusignAudit.getOrderRefUuid());
			}
			break;
		default:
			break;
		}
		return docusignGetDocumentRequest;
	}

	public DocusignGetDocumentRequest getDocusignGetDocumentRequestBasedOnQuoteCode(Integer quoteId)
			throws TclCommonException {
		if (quoteId == null) {
			throw new TclCommonException(ExceptionConstants.INVALID_INPUT, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quote.get().getQuoteCode());
				if (docusignAudit != null && docusignAudit.getStage() != null) {
					DocusignGetDocumentRequest docusignGetDocumentRequest = constructDocusignGetEnvelopeRequest(
							docusignAudit);
					if (docusignGetDocumentRequest != null && docusignGetDocumentRequest.getDocumentId() != null
							&& docusignGetDocumentRequest.getEnvelopeId() != null) {
						return docusignGetDocumentRequest;
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	@Transactional
	public void processDocusignSignedConfirmationNotification(Quote quote) throws TclCommonException {
		LOGGER.info("processDocusignSentConfirmationNotification method");
		if (Objects.nonNull(quote)) {
			try {
				String accountManagerEmail = null;
				if (quote != null) {
					List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
					if (quoteToLes != null) {
						for (QuoteToLe quoteToLe : quoteToLes) {

							MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
							List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(
									LeAttributesConstants.LE_EMAIL.toString(), CommonConstants.BACTIVE);
							if (!mstOmsAttributes.isEmpty()) {
								mstOmsAttribute = mstOmsAttributes.get(0);
							}
							List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
									.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
							for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
								accountManagerEmail = quoteLeAttributeValue.getAttributeValue();
							}

						}
					}

				}
				Integer userId = quote.getCreatedBy();
				String userName = null;
				String userEmailId = null;
				String customerName = quote.getCustomer().getCustomerName();
				if (userId != null) {
					Optional<User> userDetails = userRepository.findById(userId);
					if (userDetails.isPresent()) {
						userName = userDetails.get().getUsername();
						userEmailId = userDetails.get().getEmailId();
					}
					;
				}
				LOGGER.info("Account manager email : {}", accountManagerEmail);
				LOGGER.info("customerName : {}", customerName);
				LOGGER.info("username : {}", userName);
				if (validateBeforeSendingAcknowledgement(accountManagerEmail, customerName, userName, userEmailId)) {
					LOGGER.info("validateBeforeSendingAcknowledgement succeded");
					notificationService.docusignSignedNOtification(accountManagerEmail, customerName, customerName,
							userName, quote.getQuoteCode());
				}
			} catch (Exception e) {
				LOGGER.warn(e.getMessage());
			}
		}
	}

	@Transactional
	public void processDocusignSentConfirmationNotification(Quote quote) throws TclCommonException {
		LOGGER.info("Entering processDocusignSentConfirmationNotification method");
		try {
			String accountManagerEmail = null;
			if (quote != null) {
				List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
				if (quoteToLes != null) {
					for (QuoteToLe quoteToLe : quoteToLes) {

						MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
						List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository.findByNameAndIsActive(
								LeAttributesConstants.LE_EMAIL.toString(), CommonConstants.BACTIVE);
						if (!mstOmsAttributes.isEmpty()) {
							mstOmsAttribute = mstOmsAttributes.get(0);
						}
						List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
								.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
						for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
							accountManagerEmail = quoteLeAttributeValue.getAttributeValue();
						}

					}
				}

			}
			Integer userId = quote.getCreatedBy();
			String userName = null;
			String userEmailId = null;
			String customerName = quote.getCustomer().getCustomerName();
			if (userId != null) {
				Optional<User> userDetails = userRepository.findById(userId);
				if (userDetails.isPresent()) {
					userName = userDetails.get().getUsername();
					userEmailId = userDetails.get().getEmailId();
				}
				;
			}
			LOGGER.info("Account manager email : {}", accountManagerEmail);
			LOGGER.info("customerName : {}", customerName);
			LOGGER.info("username : {}", userName);
			if (validateBeforeSendingAcknowledgement(accountManagerEmail, customerName, userName, userEmailId)) {
				LOGGER.info("validateBeforeSendingAcknowledgement succeded");
				notificationService.docusignSentNOtification(accountManagerEmail, customerName, customerName, userName,
						quote.getQuoteCode(), userEmailId);
			}
		} catch (Exception e) {
			LOGGER.warn(e.getMessage());
		}
	}

	private Map<String, String> getAccountManagerDetails(Quote quote) {
		Map<String, String> map = new HashMap<>();
		if (quote != null) {
			List<QuoteToLe> quoteToLes = quoteToLeRepository.findByQuote(quote);
			if (quoteToLes != null) {
				for (QuoteToLe quoteToLe : quoteToLes) {
					String name = null;
					String email = null;
					MstOmsAttribute mstOmsAttribute = new MstOmsAttribute();
					List<MstOmsAttribute> mstOmsAttributes = mstOmsAttributeRepository
							.findByNameAndIsActive(LeAttributesConstants.LE_EMAIL.toString(), CommonConstants.BACTIVE);
					if (!mstOmsAttributes.isEmpty()) {
						mstOmsAttribute = mstOmsAttributes.get(0);
					}
					List<QuoteLeAttributeValue> quoteToLeAttribute = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute);
					for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute) {
						email = quoteLeAttributeValue.getAttributeValue();
					}
					MstOmsAttribute mstOmsAttribute1 = new MstOmsAttribute();
					List<MstOmsAttribute> mstOmsAttributes1 = mstOmsAttributeRepository
							.findByNameAndIsActive(LeAttributesConstants.LE_NAME.toString(), CommonConstants.BACTIVE);
					if (!mstOmsAttributes1.isEmpty()) {
						mstOmsAttribute1 = mstOmsAttributes1.get(0);
					}
					List<QuoteLeAttributeValue> quoteToLeAttribute1 = quoteLeAttributeValueRepository
							.findByQuoteToLeAndMstOmsAttribute(quoteToLe, mstOmsAttribute1);
					for (QuoteLeAttributeValue quoteLeAttributeValue : quoteToLeAttribute1) {
						name = quoteLeAttributeValue.getAttributeValue();
					}
					if (name != null && email != null) {
						map.put(name, email);
					}
				}
			}

		}
		return map;
	}

	private Boolean validateBeforeSendingAcknowledgement(String accountManagerEmail, String customerName,
			String userName, String userEmailId) {
		if (accountManagerEmail != null && customerName != null && userName != null && userEmailId != null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * getCustomerDetailsForId
	 * 
	 * @param quoteCode
	 * @return
	 */
	public Map<String, String> getCustomerDetailsForId(String quoteCode) {
		LOGGER.info("Input Quote Code for getting the customer and customerLe id's are {} ", quoteCode);
		Map<String, String> customerMapper = new HashMap<String, String>();
		Quote quoteEntity = quoteRepository.findByQuoteCode(quoteCode);
		if (quoteEntity != null) {
			for (QuoteToLe quoteToLe : quoteEntity.getQuoteToLes()) {
				List<QuoteLeAttributeValue> quoteLeAttributesList = quoteLeAttributeValueRepository
						.findByQuoteToLe(quoteToLe);
				QuoteLeAttributeValue customerCodeLeVal=null;
				QuoteLeAttributeValue customerLeCodeLeVal=null;
				for (QuoteLeAttributeValue quoteLeAttributeValue : quoteLeAttributesList) {
					if(quoteLeAttributeValue.getMstOmsAttribute()!=null && quoteLeAttributeValue.getMstOmsAttribute().getName()!=null && quoteLeAttributeValue.getMstOmsAttribute().getName()
								.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_CODE)) {
						customerCodeLeVal=quoteLeAttributeValue;
					}
					
					if(quoteLeAttributeValue.getMstOmsAttribute()!=null && quoteLeAttributeValue.getMstOmsAttribute().getName()!=null && quoteLeAttributeValue.getMstOmsAttribute().getName()
							.equalsIgnoreCase(LeAttributesConstants.CUSTOMER_LE_CODE)) {
						customerLeCodeLeVal=quoteLeAttributeValue;
				}
				}
				if (customerCodeLeVal!=null && customerLeCodeLeVal!=null) {
					customerMapper.put("CUSTOMER_CODE", customerCodeLeVal.getAttributeValue());
					customerMapper.put("CUSTOMER_LE_CODE", customerLeCodeLeVal.getAttributeValue());
					try {
						TempUploadUrlInfo tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
								Long.parseLong(tempUploadUrlExpiryWindow), customerCodeLeVal.getAttributeValue(),
								customerLeCodeLeVal.getAttributeValue(), false);
						customerMapper.put("TEMP_UPLOAD_URL", tempUploadUrlInfo.getTemporaryUploadUrl());
						customerMapper.put("REQUEST_ID", tempUploadUrlInfo.getRequestId());
						customerMapper.put("OBJ_URL", tempUploadUrlInfo.getContainerName());
					} catch (Exception e) {
						LOGGER.warn("Exception in Getting temporary url {}", ExceptionUtils.getStackTrace(e));
					}
					LOGGER.info("Customer {} ,Customer Le {}", customerCodeLeVal.getAttributeValue(),
							customerLeCodeLeVal.getAttributeValue());
				} else {
					try {
						Set<String> customerLeIdList = new HashSet<>();
						customerLeIdList.add(quoteToLe.getErfCusCustomerLegalEntityId().toString());
						String customerLeIdsStringList = customerLeIdList.stream().collect(Collectors.joining(","));
						LOGGER.info("customer le id string list{}", customerLeIdsStringList);
						String response = (String) mqUtils.sendAndReceive(customerCodeCustomerLeCodeQueue,
								customerLeIdsStringList);
						LOGGER.info("Response from Customer {}", response);
						ObjectStorageListenerBean[] objStorageListenerBeanArray = (ObjectStorageListenerBean[]) Utils
								.convertJsonToObject(response, ObjectStorageListenerBean[].class);
						List<ObjectStorageListenerBean> objStorageListenerBeanList = Arrays
								.asList(objStorageListenerBeanArray);

						if (objStorageListenerBeanList != null) {
							LOGGER.info("customer code :: {}, customer Le Code :: {}",
									objStorageListenerBeanList.get(0).getCustomerCode(),
									objStorageListenerBeanList.get(0).getCustomerLeCode());
							customerMapper.put("CUSTOMER_CODE", objStorageListenerBeanList.get(0).getCustomerCode());
							customerMapper.put("CUSTOMER_LE_CODE",
									objStorageListenerBeanList.get(0).getCustomerLeCode());

							TempUploadUrlInfo tempUploadUrlInfo = fileStorageService.getTempUploadUrl(
									Long.parseLong(tempUploadUrlExpiryWindow),
									objStorageListenerBeanList.get(0).getCustomerCode(),
									objStorageListenerBeanList.get(0).getCustomerLeCode(), false);
							customerMapper.put("TEMP_UPLOAD_URL", tempUploadUrlInfo.getTemporaryUploadUrl());
							customerMapper.put("REQUEST_ID", tempUploadUrlInfo.getRequestId());
							customerMapper.put("OBJ_URL", tempUploadUrlInfo.getContainerName());
							LOGGER.info("Customer {} ,Customer Le {}", customerCodeLeVal.getAttributeValue(),
									customerLeCodeLeVal.getAttributeValue());
						}
					} catch (Exception e) {
						LOGGER.warn("Exception in Getting temporary url {}", ExceptionUtils.getStackTrace(e));
					}
				}
			}
		}
		return customerMapper;
	}

	/**
	 * mockTriggerDocuSign
	 */
	public Boolean mockTriggerDocuSign(Integer quoteId) {
		if (appEnv.equalsIgnoreCase(CommonConstants.PROD)) {
			return false;
		}
		Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
		if (quoteEntity.isPresent()) {
			DocusignAudit docuSignAuditEntity = docusignAuditRepository
					.findByOrderRefUuid(quoteEntity.get().getQuoteCode());
			if (docuSignAuditEntity != null) {
				if (docuSignAuditEntity.getSupplierEnvelopeId() != null) {
					processDocuSignErrorNotificationResponse(
							mockSupplierEnvelopeStatus(docuSignAuditEntity.getSupplierEnvelopeId(),
									quoteEntity.get().getQuoteCode()),
							docuSignAuditEntity.getSupplierEnvelopeId());
				} else if (docuSignAuditEntity.getCustomerEnvelopeId() != null) {
					processDocuSignErrorNotificationResponse(
							mockCustomerEnvelopeStatus(docuSignAuditEntity.getCustomerEnvelopeId(),
									quoteEntity.get().getQuoteCode()),
							docuSignAuditEntity.getCustomerEnvelopeId());
				}
			}
		}
		return true;
	}

	/**
	 * 
	 * mockCustomerEnvelopeStatus - Mocking Customer Status
	 * 
	 * @param envelopeId
	 * @param refId
	 * @return
	 */
	private String mockCustomerEnvelopeStatus(String envelopeId, String refId) {
		return DocumentConstant.DOCUSIGN_CUSTOMER_TEMPLATE
				.replace(DocumentConstant.REF_ID_TEMPLATE, refId.toUpperCase())
				.replace(DocumentConstant.ENV_ID_TEMPLATE, envelopeId);
	}

	/**
	 * 
	 * mockSupplierEnvelopeStatus- Mocking Supplier Status
	 * 
	 * @param envelopeId
	 * @param refId
	 * @return
	 */
	private String mockSupplierEnvelopeStatus(String envelopeId, String refId) {
		return DocumentConstant.DOCUSIGN_SUPLIER_TEMPLATE.replace(DocumentConstant.REF_ID_TEMPLATE, refId.toUpperCase())
				.replace(DocumentConstant.ENV_ID_TEMPLATE, envelopeId);
	}

	/**
	 * @author suruchia Delete Functionality for Docusign in Active Configuration
	 *         Page for Reviewers
	 * @param docusignAudit
	 * @return envelopeId
	 */
	private String getEnvelopeIdForDeleting(DocusignAudit docusignAudit) {
		switch (docusignAudit.getStage()) {
		case DocusignConstants.CUSTOMER:
			if (docusignAudit.getCustomerEnvelopeId() != null) {
				return docusignAudit.getCustomerEnvelopeId();
			}
			break;

		case DocusignConstants.REVIEWER1:
			if (docusignAudit.getApproverOneEnvelopeId() != null) {
				return docusignAudit.getApproverOneEnvelopeId();
			}
			break;

		case DocusignConstants.REVIEWER2:
			if (docusignAudit.getApproverTwoEnvelopeId() != null) {
				return docusignAudit.getApproverTwoEnvelopeId();
			}
			break;

		case DocusignConstants.SUPPLIER:
			if (StringUtils.isNotBlank(docusignAudit.getSupplierEnvelopeId())
					&& (docusignAudit.getStage().equalsIgnoreCase(DocuSignStage.INCOMPLETE.toString()) || docusignAudit
							.getStatus().equalsIgnoreCase(DocuSignStatus.SUPPLIER_DECLINED.toString()) || docusignAudit
								.getStatus().equalsIgnoreCase(DocuSignStatus.CUSTOMER_SIGNED.toString()))) {
				return docusignAudit.getSupplierEnvelopeId();
			}
			break;

		case DocusignConstants.CUSTOMER1: {
			if (docusignAudit.getCustomerOneEnvelopeId() != null) {
				return docusignAudit.getCustomerOneEnvelopeId();
			}
			break;
		}
		case DocusignConstants.CUSTOMER2: {
			if (docusignAudit.getCustomerTwoEnvelopeId() != null) {
				return docusignAudit.getCustomerTwoEnvelopeId();
			}
			break;

		}

		default:
			break;
		}

		return null;
	}

	/**
	 * Method to process customer data
	 *
	 * @param customerId
	 * @return
	 * @throws TclCommonException
	 */
	public CustomerDetailsBean processCustomerData(Integer customerId) throws TclCommonException {
		LOGGER.info("MDC Filter token value in before Queue call processCustomerData {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String customerResponse = (String) mqUtils.sendAndReceive(customerDetailsQueue, String.valueOf(customerId));
		return (CustomerDetailsBean) Utils.convertJsonToObject(customerResponse, CustomerDetailsBean.class);

	}

	/**
	 * Method to validate DOP Email Request
	 * 
	 * @param request
	 * @return
	 */
	public boolean validateDopEmailRequest(SignDetailBean request) {
		boolean flag = false;
		if (Objects.nonNull(request.getAccountManagerName()) && Objects.nonNull(request.getCustomerSegment())
				&& Objects.nonNull(request.getOrderValue()))
			flag = true;
		return flag;
	}

	/**
	 * 
	 * getDocusignStatus - get the status of docusign
	 * 
	 * @param quoteId
	 * @return
	 */
	public Map<String, List<Map<String, Object>>> getDocusignStatus(Integer quoteId) {
		Map<String, List<Map<String, Object>>> docusignStatus = new HashMap<>();
		try {
			List<Map<String, Object>> statusList = new ArrayList<>();
			docusignStatus.put("docusignStatus", statusList);
			Optional<Quote> quoteEntity = quoteRepository.findById(quoteId);
			if (quoteEntity.isPresent()) {
				DocusignAudit docusignAuditEntity = docusignAuditRepository
						.findByOrderRefUuid(quoteEntity.get().getQuoteCode());
				if (docusignAuditEntity != null) {
					int count = 1;
					count = getApproverOneStatus(statusList, docusignAuditEntity, count);
					count = getApproverTwoStatus(statusList, docusignAuditEntity, count);
					count = getCustomerOneStatus(statusList, docusignAuditEntity, count);
					count = getCustomerTwoStatus(statusList, docusignAuditEntity, count);
					count = getCustomerStatus(statusList, docusignAuditEntity, count);
					count = getSupplierStatus(statusList, docusignAuditEntity, count);
					LOGGER.info("total seq Id {}", count - 1);
					recheckStatus(statusList, count - 1);
				} else {
					LOGGER.error("No docusign status available for quoteId :" + quoteId);
				}

			} else {
				LOGGER.error("Invalid quote id :" + quoteId);
			}
		} catch (Exception e) {
			LOGGER.error("Error in getting docusign status", e);
		}
		return docusignStatus;
	}

	private void recheckStatus(List<Map<String, Object>> statusList, int count) {
		for (Map<String, Object> statusMapper : statusList) {
			Integer seqId = (Integer) statusMapper.get("seq_id");
			String status = (String) statusMapper.get("status");
			if (status.contains("Pending") && !(count == seqId)) {
				statusMapper.put("status", "Signed");
				statusMapper.put("comments", "Completed");
			}
		}
	}

	/**
	 * 
	 * getSupplierStatus
	 * 
	 * @param statusList
	 * @param docusignAuditEntity
	 * @param count
	 */
	private int getSupplierStatus(List<Map<String, Object>> statusList, DocusignAudit docusignAuditEntity, int count) {
		if (docusignAuditEntity.getSupplierSignedDate() != null
				|| docusignAuditEntity.getStatus().equals("SUPPLIER_SIGNED")) {
			statusList.add(constructDocusignAttribute(count, "SUPPLIER", "Signed",
					docusignAuditEntity.getSupplierEmail(), docusignAuditEntity.getSupplierSignedDate(),
					docusignAuditEntity.getSupplierEmail() + " signed this cof on "
							+ (docusignAuditEntity.getSupplierSignedDate() != null
									? docusignAuditEntity.getSupplierSignedDate()
									: "-"),
					docusignAuditEntity.getSupplierEnvelopeId()));
			count++;
		} else if (StringUtils.isNotBlank(docusignAuditEntity.getSupplierEnvelopeId())) {
			String status = null;
			String comments = null;
			if (docusignAuditEntity.getStatus().contains("DECLINED")) {
				status = "Declined";
				comments = docusignAuditEntity.getSupplierEmail() + " declined this cof";
			} else {
				status = "Pending for signing";
				comments = "COF is pending for signing with " + docusignAuditEntity.getSupplierEmail();
			}
			statusList.add(constructDocusignAttribute(count, "SUPPLIER", status, docusignAuditEntity.getSupplierEmail(),
					docusignAuditEntity.getSupplierSignedDate(), comments,
					docusignAuditEntity.getSupplierEnvelopeId()));
			count++;
		}
		return count;
	}

	/**
	 * 
	 * getCustomerStatus
	 * 
	 * @param statusList
	 * @param docusignAuditEntity
	 * @param count
	 * @return
	 */
	private int getCustomerStatus(List<Map<String, Object>> statusList, DocusignAudit docusignAuditEntity, int count) {
		if (docusignAuditEntity.getCustomerSignedDate() != null) {
			statusList.add(constructDocusignAttribute(count, "CUSTOMER", "Signed",
					docusignAuditEntity.getCustomerEmail(), docusignAuditEntity.getCustomerSignedDate(),
					docusignAuditEntity.getCustomerName() + " signed this cof on "
							+ docusignAuditEntity.getCustomerSignedDate(),
					docusignAuditEntity.getCustomerEnvelopeId()));
			count++;
		} else if (StringUtils.isNotBlank(docusignAuditEntity.getCustomerEnvelopeId())) {
			String status = null;
			String comments = null;
			if (docusignAuditEntity.getStatus().contains("DECLINED")) {
				status = "Declined";
				comments = docusignAuditEntity.getCustomerName() + " declined this cof";
			} else {
				status = "Pending for signing";
				comments = "COF is pending for signing with " + docusignAuditEntity.getCustomerName();
			}
			statusList.add(constructDocusignAttribute(count, "CUSTOMER", status, docusignAuditEntity.getCustomerEmail(),
					docusignAuditEntity.getCustomerSignedDate(), comments,
					docusignAuditEntity.getCustomerEnvelopeId()));
			count++;
		}
		return count;
	}

	/**
	 * 
	 * getCustomerTwoStatus
	 * 
	 * @param statusList
	 * @param docusignAuditEntity
	 * @param count
	 * @return
	 */
	private int getCustomerTwoStatus(List<Map<String, Object>> statusList, DocusignAudit docusignAuditEntity,
			int count) {
		if (docusignAuditEntity.getCustomerTwoSignedDate() != null) {
			statusList.add(constructDocusignAttribute(count, "CUSTOMER2", "Signed",
					docusignAuditEntity.getCustomerTwoEmail(), docusignAuditEntity.getCustomerTwoSignedDate(),
					docusignAuditEntity.getCustomerTwoName() + " signed this cof on "
							+ docusignAuditEntity.getCustomerTwoSignedDate(),
					docusignAuditEntity.getCustomerTwoEnvelopeId()));
			count++;
		} else if (StringUtils.isNotBlank(docusignAuditEntity.getCustomerTwoEnvelopeId())) {
			String status = null;
			String comments = null;
			if (docusignAuditEntity.getStatus().contains("DECLINED")) {
				status = "Declined";
				comments = docusignAuditEntity.getCustomerTwoName() + " declined this cof";
			} else {
				status = "Pending for signing";
				comments = "COF is pending for signing with " + docusignAuditEntity.getCustomerTwoName();
			}
			statusList.add(constructDocusignAttribute(count, "CUSTOMER2", status,
					docusignAuditEntity.getCustomerTwoEmail(), docusignAuditEntity.getCustomerTwoSignedDate(), comments,
					docusignAuditEntity.getCustomerTwoEnvelopeId()));
			count++;
		}
		return count;
	}

	/**
	 * 
	 * getCustomerOneStatus
	 * 
	 * @param statusList
	 * @param docusignAuditEntity
	 * @param count
	 * @return
	 */
	private int getCustomerOneStatus(List<Map<String, Object>> statusList, DocusignAudit docusignAuditEntity,
			int count) {
		if (docusignAuditEntity.getCustomerOneSignedDate() != null) {
			statusList.add(constructDocusignAttribute(count, "CUSTOMER1", "Signed",
					docusignAuditEntity.getCustomerOneEmail(), docusignAuditEntity.getCustomerOneSignedDate(),
					docusignAuditEntity.getCustomerOneName() + " signed this cof on "
							+ docusignAuditEntity.getCustomerOneSignedDate(),
					docusignAuditEntity.getCustomerOneEnvelopeId()));
			count++;
		} else if (StringUtils.isNotBlank(docusignAuditEntity.getCustomerOneEnvelopeId())) {
			String status = null;
			String comments = null;
			if (docusignAuditEntity.getStatus().contains("DECLINED")) {
				status = "Declined";
				comments = docusignAuditEntity.getCustomerOneName() + " declined this cof";
			} else {
				status = "Pending for signing";
				comments = "COF is pending for signing with " + docusignAuditEntity.getCustomerOneName();
			}
			statusList.add(constructDocusignAttribute(count, "CUSTOMER1", status,
					docusignAuditEntity.getCustomerOneEmail(), docusignAuditEntity.getCustomerOneSignedDate(), comments,
					docusignAuditEntity.getCustomerOneEnvelopeId()));
			count++;
		}
		return count;
	}

	/**
	 * 
	 * getApproverTwoStatus
	 * 
	 * @param statusList
	 * @param docusignAuditEntity
	 * @param count
	 * @return
	 */
	private int getApproverTwoStatus(List<Map<String, Object>> statusList, DocusignAudit docusignAuditEntity,
			int count) {
		if (docusignAuditEntity.getApproverTwoSignedDate() != null) {
			statusList.add(constructDocusignAttribute(count, "REVIEWER2", "Signed",
					docusignAuditEntity.getApproverTwoEmail(), docusignAuditEntity.getApproverTwoSignedDate(),
					docusignAuditEntity.getApproverTwoName() + " reviewed this cof on "
							+ docusignAuditEntity.getApproverTwoSignedDate(),
					docusignAuditEntity.getApproverTwoEnvelopeId()));
			count++;
		} else if (StringUtils.isNotBlank(docusignAuditEntity.getApproverTwoEnvelopeId())) {
			String status = null;
			String comments = null;
			if (docusignAuditEntity.getStatus().contains("DECLINED")) {
				status = "Declined";
				comments = docusignAuditEntity.getApproverTwoName() + " declined this cof";
			} else {
				status = "Pending for review";
				comments = "COF is pending for review with " + docusignAuditEntity.getApproverTwoName();
			}
			statusList.add(constructDocusignAttribute(count, "REVIEWER2", status,
					docusignAuditEntity.getApproverTwoEmail(), docusignAuditEntity.getApproverTwoSignedDate(), comments,
					docusignAuditEntity.getApproverTwoEnvelopeId()));
			count++;
		}
		return count;
	}

	/**
	 * 
	 * getApproverOneStatus
	 * 
	 * @param statusList
	 * @param docusignAuditEntity
	 * @param count
	 * @return
	 */
	private int getApproverOneStatus(List<Map<String, Object>> statusList, DocusignAudit docusignAuditEntity,
			int count) {
		if (docusignAuditEntity.getApproverOneSignedDate() != null) {
			statusList.add(constructDocusignAttribute(count, "REVIEWER1", "Signed",
					docusignAuditEntity.getApproverOneEmail(), docusignAuditEntity.getApproverOneSignedDate(),
					docusignAuditEntity.getApproverOneName() + " reviewed this cof on "
							+ docusignAuditEntity.getApproverOneSignedDate(),
					docusignAuditEntity.getApproverOneEnvelopeId()));
			count++;
		} else if (StringUtils.isNotBlank(docusignAuditEntity.getApproverOneEnvelopeId())) {
			String status = null;
			String comments = null;
			if (docusignAuditEntity.getStatus().contains("DECLINED")) {
				status = "Declined";
				comments = docusignAuditEntity.getApproverOneName() + " declined this cof";
			} else {
				status = "Pending for review";
				comments = "COF is pending for review with " + docusignAuditEntity.getApproverOneName()
						+ " which was initiated on " + docusignAuditEntity.getUpdatedTime();
			}
			statusList.add(constructDocusignAttribute(count, "REVIEWER1", status,
					docusignAuditEntity.getApproverOneEmail(), docusignAuditEntity.getApproverOneSignedDate(), comments,
					docusignAuditEntity.getApproverOneEnvelopeId()));
			count++;
		}
		return count;
	}

	/**
	 * 
	 * constructDocusignAttribute
	 * 
	 * @param count
	 * @param stage
	 * @param status
	 * @param approverEmail
	 * @param signedDate
	 * @param comments
	 * @return
	 */
	private Map<String, Object> constructDocusignAttribute(int count, String stage, String status, String approverEmail,
			Date signedDate, String comments, String envelopId) {
		Map<String, Object> docusignAttrMapper = new HashMap<>();
		docusignAttrMapper.put("seq_id", count);
		docusignAttrMapper.put("stage", stage);
		docusignAttrMapper.put("status", status);
		docusignAttrMapper.put("email", approverEmail);
		docusignAttrMapper.put("signed_timestamp", signedDate != null ? signedDate : "-");
		docusignAttrMapper.put("comments", comments);
		docusignAttrMapper.put("envelopeId", envelopId);
		return docusignAttrMapper;
	}

	public String retryDocusignNotification(Integer quoteId) {
		String status = "Successfully retrigged";
		try {
			Optional<Quote> quote = quoteRepository.findById(quoteId);
			if (quote.isPresent()) {
				String quoteCode = quote.get().getQuoteCode();
				DocusignAudit docusignAudit = docusignAuditRepository.findByOrderRefUuid(quoteCode);
				if (docusignAudit != null) {
					String latestEnvelopeId = null;
					Map<String, List<Map<String, Object>>> docusignStatuses = getDocusignStatus(quoteId);
					if (!docusignStatuses.isEmpty()) {
						List<Map<String, Object>> docusignStatus = docusignStatuses.get("docusignStatus");

						for (Map<String, Object> map : docusignStatus) {
							String comments = (String) map.get("comments");
							if (comments.contains("Pending for")) {
								latestEnvelopeId = (String) map.get("envelopeId");
							}
						}
					}
					if (StringUtils.isNotBlank(latestEnvelopeId)) {
						String response = (String) mqUtils.sendAndReceive(docusignEnvelopeRetry, latestEnvelopeId);
						if (!response.equalsIgnoreCase("Success")) {
							status = "Error in retriggering";
						}

					} else {
						status = "No docusign notification is Pending";
					}

				} else {
					status = "docusign not trigged yet";
				}
			} else {
				status = "invalid quote";
			}
		} catch (Exception e) {
			status = "Error in retriggering";
			LOGGER.error("Error in retryDocusignNotification docusign notification", e);
		}
		return status;
	}

	public Boolean isProcessedCheck(String envelopeId, String stage) {
		DocusignAudit docuSignAudit = null;
		if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(stage)) {
			docuSignAudit = docusignAuditRepository.findByCustomerEnvelopeId(envelopeId);
			if (docuSignAudit.getCustomerSignedDate() == null || docuSignAudit.getSupplierEnvelopeId() == null) {
				return false;
			}
		} else if (DocuSignStage.REVIEWER1.toString().equalsIgnoreCase(stage)) {
			docuSignAudit = docusignAuditRepository.findByApproverOneEnvelopeId(envelopeId);
			if (docuSignAudit.getApproverOneSignedDate() == null) {
				return false;
			}
		} else if (DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(stage)) {
			docuSignAudit = docusignAuditRepository.findByApproverTwoEnvelopeId(envelopeId);
			if (docuSignAudit.getApproverTwoSignedDate() == null) {
				return false;
			}
		} else if (DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(stage)) {
			docuSignAudit = docusignAuditRepository.findByCustomerOneEnvelopeId(envelopeId);
			if (docuSignAudit.getCustomerOneSignedDate() == null) {
				return false;
			}
		} else if (DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(stage)) {
			docuSignAudit = docusignAuditRepository.findByCustomerTwoEnvelopeId(envelopeId);
			if (docuSignAudit.getCustomerTwoSignedDate() == null) {
				return false;
			}
		} else if (DocuSignStage.SUPPLIER.toString().equalsIgnoreCase(stage)) {
			docuSignAudit = docusignAuditRepository.findBySupplierEnvelopeId(envelopeId);
			if (docuSignAudit.getSupplierSignedDate() == null) {
				return false;
			}
		}
		return true;
	}
}
