package com.tcl.dias.docusign.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.docusign.esign.api.AuthenticationApi;
import com.docusign.esign.api.EnvelopesApi;
import com.docusign.esign.client.ApiClient;
import com.docusign.esign.client.ApiException;
import com.docusign.esign.client.Configuration;
import com.docusign.esign.model.Approve;
import com.docusign.esign.model.CarbonCopy;
import com.docusign.esign.model.DateSigned;
import com.docusign.esign.model.Decline;
import com.docusign.esign.model.Document;
import com.docusign.esign.model.EnvelopeDefinition;
import com.docusign.esign.model.EnvelopeEvent;
import com.docusign.esign.model.EnvelopeSummary;
import com.docusign.esign.model.EventNotification;
import com.docusign.esign.model.FullName;
import com.docusign.esign.model.LoginAccount;
import com.docusign.esign.model.LoginInformation;
import com.docusign.esign.model.Recipients;
import com.docusign.esign.model.SignHere;
import com.docusign.esign.model.Signer;
import com.docusign.esign.model.Tabs;
import com.tcl.dias.common.beans.CommonDocusignRequest;
import com.tcl.dias.common.beans.CommonDocusignResponse;
import com.tcl.dias.common.beans.DocusignGetDocumentRequest;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.RestResponse;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.DocuSignStage;
import com.tcl.dias.common.utils.DocuSignStatus;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.PDFGenerator;
import com.tcl.dias.common.utils.RestClientService;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.docusign.beans.DocuSignNotificationResponse;
import com.tcl.dias.docusign.beans.DocuSignRequest;
import com.tcl.dias.docusign.beans.DocusignEnvelope;
import com.tcl.dias.docusign.beans.EnvelopeStatus;
import com.tcl.dias.docusign.constants.DocusignConstants;
import com.tcl.dias.notification.beans.DocusignEnvelopeStatusChangeRequest;
import com.tcl.dias.notification.beans.DocusignMoveFolderRequest;
import com.tcl.dias.notification.beans.Folder;
import com.tcl.dias.notification.beans.FolderDetailsResponse;
import com.tcl.dias.notification.constants.Constants;
import com.tcl.dias.notification.constants.ExceptionConstants;
import com.tcl.dias.notification.constants.ServiceConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class DocuSignService {

	private static final Logger LOGGER = LoggerFactory.getLogger(DocuSignService.class);

	@Value("${docusign.username}")
	String userName;

	@Value("${docusign.password}")
	String password;

	@Value("${docusign.integrator_key}")
	String integratorKey;

	@Value("${info.docusign.cof.response}")
	String docuSignResponseQueue;

	@Value("${info.docusign.get.customer.request}")
	String docusignCustomerRequest;

	@Value("${docusign.base_url}")
	String baseUrl;

	@Value("${email.test.id}")
	String testEmail;

	@Value("${info.docusign.notification.response}")
	String docusignNotications;

	@Value("${info.docusign.error.response}")
	String docusignErrorNotications;

	@Value("${app.host}")
	String appHost;

	@Value("${cof.docusign.upload.path}")
	String cofDocuSignUploadPath;

	@Value("${application.env}")
	String appEnv;

	@Value("${email.check}")
	String emailStrings;

	@Value("${swift.api.enabled}")
	String swiftApiEnabled;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	RestClientService restClientService;

	@Value("${info.docusign.get.document.request}")
	String getDocumentQueue;

	@Autowired
	FileStorageService fileStorageService;

	@Value("${temp.upload.url.expiryWindow}")
	String tempUploadUrlExpiryWindow;
	
	@Value("${webhook.docusign.status}")
	String webhookStatus;

	private final String ATTACHEMENT_FILE_NAME_HEADER = "attachment; filename=\"";

	/**
	 * processDocusign
	 * 
	 * @throws TclCommonException
	 */
	public void processDocusign(CommonDocusignRequest docusignRequest) throws TclCommonException {
		LOGGER.info("Input request to docusign is {}",docusignRequest);
		CommonDocusignResponse docusignResponse = new CommonDocusignResponse();
		docusignResponse.setQuoteId(docusignRequest.getQuoteId());
		docusignResponse.setQuoteLeId(docusignRequest.getQuoteLeId());
		docusignResponse.setStatus(true);
		try {
			String accountId = setLoginInformations(1);
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			PDFGenerator.createPdf(new String(Base64.getDecoder().decode(docusignRequest.getPdfHtml().getBytes())),
					outByteStream);
			String base64Doc = Base64.getEncoder().encodeToString(outByteStream.toByteArray());
			EnvelopeSummary envelopeSummary = constructDocusign(docusignRequest, accountId, base64Doc);
			if (envelopeSummary != null) {
				String envelopeId = envelopeSummary.getEnvelopeId();

				// approver userstory start
				docusignResponse.setType(docusignRequest.getType());

				// approver userstory end

				/* docusignResponse.setType(DocuSignStage.CUSTOMER.toString()); */
				docusignResponse.setEnvelopeId(envelopeId);
				LOGGER.info("MDC Filter token value in before Queue call processDocusign {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(docuSignResponseQueue, Utils.convertObjectToJson(docusignResponse));
				LOGGER.info("Envelope: {}", envelopeId);
			}
		} catch (Exception e) {
			LOGGER.error("Docusign error", e);
			docusignResponse.setStatus(false);
			docusignResponse.setErrorMessage(ExceptionUtils.getMessage(e));
			LOGGER.info("MDC Filter token value in before Queue call processDocusign {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(docuSignResponseQueue, Utils.convertObjectToJson(docusignResponse));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

	}

	/**
	 * constructDocusign
	 * 
	 * @param docusignRequest
	 * @param docusignResponse
	 * @param accountId
	 * @param base64Doc
	 * @throws ApiException
	 * @throws TclCommonException
	 */
	private EnvelopeSummary constructDocusign(CommonDocusignRequest docusignRequest, String accountId, String base64Doc)
			throws ApiException {
		EventNotification notification = new EventNotification();
		List<EnvelopeEvent> envelopEvents = new ArrayList<>();
		EnvelopeEvent envelopeEvent = new EnvelopeEvent();
		envelopeEvent.setEnvelopeEventStatusCode("completed");
		notification.setRequireAcknowledgment("true");
		notification.setIncludeDocumentFields("true");
		notification.setUrl("https://customer.tatacommunications.com/optimus-webhook/v1/notify/docusign/webhook");		
		envelopEvents.add(envelopeEvent);
		notification.setEnvelopeEvents(envelopEvents);
		notification.setIncludeCertificateOfCompletion("false");
		EnvelopeDefinition envDef = new EnvelopeDefinition();
		//if (appEnv.equals(ServiceConstants.PROD.toString()))
			//envDef.setEventNotification(notification);
		envDef.setEmailSubject(docusignRequest.getSubject());
		List<Document> documents = new ArrayList<>();
		documents.add(constructDocument(docusignRequest, base64Doc));
		envDef.setDocuments(documents);
		if (!appEnv.equals(ServiceConstants.PROD.toString())) {
			String[] emailIdsAllowed = emailStrings.split(",");
			for (String email : emailIdsAllowed) {
				if (!docusignRequest.getToEmail().contains(email)) {
					docusignRequest.setToEmail(testEmail);
					LOGGER.info("Test Email is set  {}",testEmail);
				}
			}
		}

		// Approver userstory start
		envDef.setRecipients(new Recipients());
		envDef.getRecipients().setSigners(new ArrayList<Signer>());

		// Adding approvers in recipients with routingorder,approve and decline button
		/*
		 * Integer[] routingOrder = { 1 }; if (docusignRequest.getApprovers() != null) {
		 * docusignRequest.getApprovers().stream().forEach(approverData -> {
		 * 
		 * Signer reviewer = new Signer(); reviewer.setEmail(approverData.getEmail());
		 * reviewer.setName(approverData.getName()); reviewer =
		 * constructReviewer(docusignRequest, reviewer, routingOrder[0]);
		 * envDef.getRecipients().getSigners().add(reviewer); routingOrder[0]++;
		 * 
		 * }); }
		 */
		Signer signer = new Signer();
		LOGGER.info("docusign request after the filter {}",docusignRequest);
		if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(docusignRequest.getType())
				|| DocuSignStage.SUPPLIER.toString().equalsIgnoreCase(docusignRequest.getType())
				|| DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(docusignRequest.getType())
				|| DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(docusignRequest.getType())
				|| DocuSignStage.COMMERCIAL.toString().equalsIgnoreCase(docusignRequest.getType())) {
			// Adding signer in recipient
			constructSigner(docusignRequest, signer, docusignRequest.getType());
		} else if (DocuSignStage.REVIEWER1.toString().equalsIgnoreCase(docusignRequest.getType())
				|| DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(docusignRequest.getType())) {
			// Adding reviewer in recipient
			constructReviewer(docusignRequest, signer, docusignRequest.getType());
		}
		LOGGER.info("Signer/Reviewer info ---> {} ", signer);
		envDef.getRecipients().getSigners().add(signer);
		/*
		 * Signer signer = new Signer(); constructSigner(docusignRequest, signer);
		 * envDef.setRecipients(new Recipients()); envDef.getRecipients().setSigners(new
		 * ArrayList<Signer>()); envDef.getRecipients().getSigners().add(signer);
		 */
		LOGGER.info("App environment {}", appEnv);
		LOGGER.info("CC object is null ---> {} " , Optional.ofNullable(docusignRequest.getCcEmails()));
		if (docusignRequest.getCcEmails() != null && appEnv.equals(ServiceConstants.PROD.toString())) {
			LOGGER.info("CC email ids : {}", docusignRequest.getCcEmails());
			for (Entry<String, String> ccEmail : docusignRequest.getCcEmails().entrySet()) {
				LOGGER.info("CC emails id : {} ", ccEmail.toString());
				if(docusignRequest.getToEmail().toLowerCase().equalsIgnoreCase(ccEmail.getValue().toLowerCase())) {
					continue;
				}
				CarbonCopy ccSigner = new CarbonCopy();
				ccSigner.setEmail(ccEmail.getValue());
				ccSigner.setName(ccEmail.getKey());
				ccSigner.setRecipientId(String.valueOf(envDef.getRecipients().getCarbonCopies().size()) + 1);
				envDef.getRecipients().getCarbonCopies().add(ccSigner);
				LOGGER.info("Set envDEv cc emails {}", envDef.getRecipients().getCarbonCopies().toString());
			}
		}// checking CC functionality for UAT env !!!
		 else if (docusignRequest.getCcEmails() != null && appEnv.equalsIgnoreCase("UAT")) {
			LOGGER.info("CC email ids in request : {}", docusignRequest.getCcEmails());
			for (Entry<String, String> ccEmail : docusignRequest.getCcEmails().entrySet()) {
				LOGGER.info("CC emails id : {} ", ccEmail.toString());
				if(docusignRequest.getToEmail().toLowerCase().equalsIgnoreCase(ccEmail.getValue().toLowerCase())) {
					continue;
				}
				CarbonCopy ccSigner = new CarbonCopy();
				/*ccSigner.setEmail("mansi.bedi@tatacommunications.com");
				ccSigner.setName("Mansi Bedi");*/
				ccSigner.setEmail(ccEmail.getValue());
				ccSigner.setName(ccEmail.getKey());
				ccSigner.setRecipientId(String.valueOf(envDef.getRecipients().getCarbonCopies().size()) + 1);
				envDef.getRecipients().getCarbonCopies().add(ccSigner);
				LOGGER.info("Set envDEv cc emails {}", envDef.getRecipients().getCarbonCopies().toString());
			}
		}



		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(baseUrl);
	//	envDef.getRecipients().setCarbonCopies(new ArrayList<CarbonCopy>());
		envDef.setStatus(DocusignConstants.SENT);
		EnvelopesApi envelopesApi = new EnvelopesApi();
		return createEnvelope(accountId, envDef, envelopesApi, 1);
	}

	/**
	 * createEnvelope
	 * 
	 * @param accountId
	 * @param envDef
	 * @param envelopesApi
	 * @return
	 * @throws ApiException
	 */
	private EnvelopeSummary createEnvelope(String accountId, EnvelopeDefinition envDef, EnvelopesApi envelopesApi,
			Integer retryCount) throws ApiException {
		EnvelopeSummary envelopeSummary = null;
		if (retryCount >= 3) {
			return envelopeSummary;
		}
		try {
			envelopeSummary = envelopesApi.createEnvelope(accountId, envDef);
		} catch (Exception e) {
			LOGGER.warn("Error in Creating Docusign Envelope {}", ExceptionUtils.getStackTrace(e));
			LOGGER.info("Retrying Create Envelope :::{}", retryCount);
			envelopeSummary = createEnvelope(accountId, envDef, envelopesApi, retryCount + 1);
		}
		return envelopeSummary;
	}

	/**
	 * setLoginInformations
	 * 
	 * @return
	 * @throws TclCommonException
	 * @throws ApiException
	 */
	private String setLoginInformations(Integer retryCount) throws TclCommonException, ApiException {
		Utils.disableSslVerification();
		String accId = null;
		if (retryCount >= 3) {
			return accId;
		}
		try {
			Configuration.setDefaultApiClient(constructApiClient());
			AuthenticationApi authApi = new AuthenticationApi();
			AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
			loginOps.setApiPassword(DocusignConstants.TRUE);
			loginOps.setIncludeAccountIdGuid(DocusignConstants.TRUE);
			LoginInformation loginInfo = authApi.login(loginOps);
			List<LoginAccount> loginAccounts = loginInfo.getLoginAccounts();
			accId = loginAccounts.get(0).getAccountId();
		} catch (Exception e) {
			LOGGER.warn("Error in Creating Docusign Envelope {}", ExceptionUtils.getStackTrace(e));
			LOGGER.info("Retrying Login Information :::{}", retryCount);
			accId = setLoginInformations(retryCount + 1);
		}

		return accId;
	}

	@SuppressWarnings({ "unchecked" })
	public void processDocusignNotication(DocuSignNotificationResponse docusignNoticationResponse) throws TclCommonException {
		try {
			LOGGER.info("Entering method processDocusignNotication with envelope id : {}", docusignNoticationResponse
					.getDocuSignEnvelopeInformation().getEnvelopeStatus().getEnvelopeID());
			CommonDocusignResponse docusignResponse = new CommonDocusignResponse();
			docusignResponse.setStatus(true);
			if (docusignNoticationResponse.getDocuSignEnvelopeInformation() != null) {
				DocusignEnvelope envelope = docusignNoticationResponse.getDocuSignEnvelopeInformation();
				if (envelope.getEnvelopeStatus() != null) {
					EnvelopeStatus envelopeStatus = envelope.getEnvelopeStatus();
					String documentId = envelopeStatus.getDocumentStatuses().getDocumentStatus().getID();
					if (envelopeStatus.getStatus().equalsIgnoreCase(DocusignConstants.VOIDED)) {
						Map<String,String> req=new HashMap<>();
						req.put("STATUS", "SUCCESS");
						req.put("ENVELOPE_ID", docusignNoticationResponse
								.getDocuSignEnvelopeInformation().getEnvelopeStatus().getEnvelopeID());
						mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
						return;
					}
					if (documentId.equalsIgnoreCase(DocusignConstants.ONE)
							|| documentId.equalsIgnoreCase(DocusignConstants.FOUR)
							|| documentId.equalsIgnoreCase(DocusignConstants.THREE)
							|| documentId.equalsIgnoreCase(DocusignConstants.FIVE)
							|| documentId.equalsIgnoreCase(DocusignConstants.SIX)
							|| documentId.equalsIgnoreCase(DocusignConstants.NINE)) {
						constructDocusignResponse(envelopeStatus, documentId, docusignNoticationResponse,
								docusignResponse);
					} else {
						byte[] fileByte = getEnvelopeDocuments(envelopeStatus.getEnvelopeID(), documentId, 1);
						if (fileByte != null && fileByte.length > 0) {
							setDocuSignStatusBasedOnEnvelopeStatus(envelopeStatus, docusignResponse);
							String uuid = envelopeStatus.getDocumentStatuses().getDocumentStatus().getName();
							uuid = uuid.substring(uuid.lastIndexOf(Constants.HYPHEN) + 1, uuid.indexOf(Constants.DOT))
									.trim();
							if (swiftApiEnabled.equalsIgnoreCase("true")) {
								String customerMapperStr = (String) mqUtils.sendAndReceive(docusignCustomerRequest,
										uuid.toUpperCase(),240000);
								LOGGER.info("Customer Details Response received {}", customerMapperStr);
								if (StringUtils.isNotBlank( customerMapperStr)) {
									Map<String, String> customerMapper = (Map<String, String>) Utils
											.convertJsonToObject(customerMapperStr, Map.class);
									String tempUrl = customerMapper.get("TEMP_UPLOAD_URL");
									String requestId = customerMapper.get("REQUEST_ID");
									String objUrl = customerMapper.get("OBJ_URL");
									LOGGER.info("TempUrl Recived {}", appHost + CommonConstants.RIGHT_SLASH + tempUrl);
									if (tempUrl != null) {
										String appHost1=appHost;
										/*
										 * if (appEnv.equals(ServiceConstants.PROD.toString())) {
										 * appHost1="https://14.142.130.20"; }
										 */
										/*
										 * RestResponse restResponse = restClientService.putStream( appHost +
										 * CommonConstants.RIGHT_SLASH + tempUrl, fileByte,
										 * envelopeStatus.getDocumentStatuses().getDocumentStatus().getName());
										 */
										//temp fix for customer.tatacommunications.com going via proxy 
										RestResponse restResponse = restClientService.putStream(
												appHost1 + CommonConstants.RIGHT_SLASH + tempUrl, fileByte,
												envelopeStatus.getDocumentStatuses().getDocumentStatus().getName());
										LOGGER.info("Response received {} ", restResponse);
										if (restResponse.getStatus() == Status.SUCCESS) {
											docusignResponse.setEnvelopeId(envelopeStatus.getEnvelopeID());
											docusignResponse.setEnvelopeResponse(
													Utils.convertObjectToJson(docusignNoticationResponse));
											setNameAndIpBasedOnRecipientStatus(envelopeStatus, docusignResponse);
											docusignResponse.setStatus(true);
											docusignResponse.setType(DocuSignStage.SUPPLIER.toString());
											docusignResponse.setRequestId(requestId);
											docusignResponse.setObjUrl(objUrl);
											LOGGER.info(
													"Docusign Report Uploaded Successfully , RequestId : {}  : Object Url: {}",
													requestId, objUrl);
											LOGGER.info(
													"MDC Filter token value in before Queue call processDocusignNotication {} :",
													MDC.get(CommonConstants.MDC_TOKEN_KEY));
											String response=(String) mqUtils.sendAndReceive(docusignNotications,
													Utils.convertObjectToJson(docusignResponse),240000);
											LOGGER.info("Response {}",response);
											//TODO - Update the Docusign status as COMPLTED
										}
									}
								}
							} else {

								String cofPath = cofDocuSignUploadPath + uuid.toLowerCase();
								File filefolder = new File(cofPath);
								if (!filefolder.exists()) {
									filefolder.mkdirs();

								}
								String fileFullPath = cofPath + CommonConstants.RIGHT_SLASH
										+ envelopeStatus.getDocumentStatuses().getDocumentStatus().getName();
								FileUtils.writeByteArrayToFile(new File(fileFullPath), fileByte);
								docusignResponse.setEnvelopeId(envelopeStatus.getEnvelopeID());
								docusignResponse
										.setEnvelopeResponse(Utils.convertObjectToJson(docusignNoticationResponse));
								setNameAndIpBasedOnRecipientStatus(envelopeStatus, docusignResponse);
								docusignResponse.setStatus(true);
								docusignResponse.setType(DocuSignStage.SUPPLIER.toString());
								docusignResponse.setPath(fileFullPath);
								LOGGER.info(
										"MDC Filter token value in before Queue call processDocusignNotication {} :",
										MDC.get(CommonConstants.MDC_TOKEN_KEY));
								mqUtils.sendAndReceive(docusignNotications, Utils.convertObjectToJson(docusignResponse));
							}

						}

					}
				}
			}
		} catch (Exception e) {
			CommonDocusignResponse commonDocusignResponse = new CommonDocusignResponse();
			processErrorNotification(docusignNoticationResponse, commonDocusignResponse);
			LOGGER.error("Docusign error : {}", ExceptionUtils.getStackTrace(e));
			//UPDATE DOCUSIGN STATUS AS INCOMPETE
			Map<String,String> req=new HashMap<>();
			req.put("STATUS", "FAILURE");
			req.put("ENVELOPE_ID", docusignNoticationResponse
					.getDocuSignEnvelopeInformation().getEnvelopeStatus().getEnvelopeID());
			mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
		}

	}

	/**
	 * processErrorNotification
	 * 
	 * @param docusignNoticationResponse
	 * @param commonDocusignResponse
	 */
	private void processErrorNotification(DocuSignNotificationResponse docusignNoticationResponse,
			CommonDocusignResponse commonDocusignResponse) {
		try {
			commonDocusignResponse.setStatus(false);
			commonDocusignResponse.setEnvelopeId(
					docusignNoticationResponse.getDocuSignEnvelopeInformation().getEnvelopeStatus().getEnvelopeID());
			commonDocusignResponse.setEnvelopeResponse(Utils.convertObjectToJson(docusignNoticationResponse));
			LOGGER.info("MDC Filter token value in before Queue call processDocusignErrorNotication {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(docusignErrorNotications, Utils.convertObjectToJson(commonDocusignResponse));
		} catch (TclCommonException e1) {
			LOGGER.error("Docusign error : {}", ExceptionUtils.getStackTrace(e1));
		}
	}

	public void setDocuSignStatusBasedOnEnvelopeStatus(EnvelopeStatus envelopeStatus,
			CommonDocusignResponse docusignResponse) {
		if (envelopeStatus.getStatus().equals(DocusignConstants.COMPLETED)) {
			docusignResponse.setDocusignStatus(DocuSignStatus.SUPPLIER_SIGNED.toString());
		} else {
			docusignResponse.setDocusignStatus(DocuSignStatus.SUPPLIER_DECLINED.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public void setIpAndNameIfMap(EnvelopeStatus envelopeStatus, CommonDocusignResponse docusignResponse) {
		Map<String, Object> recepientStatusMap = (Map<String, Object>) envelopeStatus.getRecipientStatuses()
				.getRecipientStatus();
		docusignResponse.setIp((String) recepientStatusMap.get(Constants.RECIPIENT_IP_ADDRESS));
		docusignResponse.setName((String) recepientStatusMap.get(Constants.EMAIL));
	}

	@SuppressWarnings("unchecked")
	public void setIpAndNameIfList(EnvelopeStatus envelopeStatus, CommonDocusignResponse docusignResponse) {
		List<Map<String, Object>> recepientStatusList = (List<Map<String, Object>>) envelopeStatus
				.getRecipientStatuses().getRecipientStatus();
		if (!recepientStatusList.isEmpty()) {
			Map<String, Object> recepientStatusMap = recepientStatusList.get(0);
			docusignResponse.setIp((String) recepientStatusMap.get(Constants.RECIPIENT_IP_ADDRESS));
			docusignResponse.setName((String) recepientStatusMap.get(Constants.EMAIL));
		}
	}

	public void setNameAndIpBasedOnRecipientStatus(EnvelopeStatus envelopeStatus,
			CommonDocusignResponse docusignResponse) {
		if (envelopeStatus.getRecipientStatuses().getRecipientStatus() instanceof Map<?, ?>) {
			setIpAndNameIfMap(envelopeStatus, docusignResponse);
		} else if (envelopeStatus.getRecipientStatuses().getRecipientStatus() instanceof List<?>) {
			setIpAndNameIfList(envelopeStatus, docusignResponse);
		}
	}

	@SuppressWarnings("unchecked")
	public void constructDocusignResponse(EnvelopeStatus envelopeStatus, String documentId,
			DocuSignNotificationResponse docusignNoticationResponse, CommonDocusignResponse docusignResponse)
			throws TclCommonException, ApiException {
		LOGGER.info("Entering method constructDocusignResponse in Optimus-Notification");
		String recipientStatus = null;
		String recipientName = null;
		List<String> recipientNames = new ArrayList<>();
		byte[] fileByte = getEnvelopeDocuments(envelopeStatus.getEnvelopeID(), documentId, 1);
		String base64Doc = Base64.getEncoder().encodeToString(fileByte);
		String accountId = setLoginInformations(1);
		/*
		 * if (envelopeStatus.getStatus().equals(DocusignConstants.COMPLETED)) {
		 * docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_SIGNED.toString())
		 * ; } else {
		 * docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_DECLINED.toString(
		 * )); }
		 */
		docusignResponse.setEnvelopeId(envelopeStatus.getEnvelopeID());
		LOGGER.info("Envelope Id is : {}",docusignResponse.getEnvelopeId());
		docusignResponse.setEnvelopeResponse(Utils.convertObjectToJson(docusignNoticationResponse));
		if (envelopeStatus.getRecipientStatuses().getRecipientStatus() instanceof Map<?, ?>) {
			LOGGER.info("Response is an instance of Map");
			Map<String, Object> recepientStatusMap = (Map<String, Object>) envelopeStatus.getRecipientStatuses()
					.getRecipientStatus();
			docusignResponse.setIp((String) recepientStatusMap.get(Constants.RECIPIENT_IP_ADDRESS));
			docusignResponse.setName((String) recepientStatusMap.get(Constants.EMAIL));

			recipientStatus = (String) recepientStatusMap.get(Constants.STATUS);
			if (recepientStatusMap.get(Constants.CUSTOMFIELDS) != null) {
				if (recepientStatusMap.get(Constants.CUSTOMFIELDS) instanceof Map) {
					Map<String, Object> customField = (Map<String, Object>) recepientStatusMap
							.get(Constants.CUSTOMFIELDS);
					if (customField != null && !customField.isEmpty()) {
						
						if (customField.get(Constants.CUSTOMFIELD) instanceof List<?>) {
							recipientNames = (List<String>) customField.get(Constants.CUSTOMFIELD);
							recipientName = recipientNames.stream()
									.filter(recipient -> StringUtils.isNotBlank(recipient))
									.findFirst()
									.get();
						}
						else if (customField.get(Constants.CUSTOMFIELD) != null) {
							recipientName = (String) customField.get(Constants.CUSTOMFIELD);
						}
					}
				}
			}
		} else if (envelopeStatus.getRecipientStatuses().getRecipientStatus() instanceof List<?>) {
			LOGGER.info("Response is an instance of List");
			List<Map<String, Object>> recepientStatusList = (List<Map<String, Object>>) envelopeStatus
					.getRecipientStatuses().getRecipientStatus();
			if (!recepientStatusList.isEmpty()) {
				Map<String, Object> recepientStatusMap =new HashMap<>();
				List<Map<String, Object>> recepientStatusMapList = recepientStatusList;
				for (Map<String, Object> map : recepientStatusMapList) {
					if(map.get(Constants.TYPE)!=null && map.get(Constants.TYPE).equals("CarbonCopy")) {
						continue;
					}
					recepientStatusMap=map;
					break;
				}
				docusignResponse.setIp((String) recepientStatusMap.get(Constants.RECIPIENT_IP_ADDRESS));
				docusignResponse.setName((String) recepientStatusMap.get(Constants.EMAIL));
				if (recepientStatusMap.get(Constants.CUSTOMFIELDS) != null) {
					if (recepientStatusMap.get(Constants.CUSTOMFIELDS) instanceof Map) {
						Map<String, Object> customField = (Map<String, Object>) recepientStatusMap
								.get(Constants.CUSTOMFIELDS);
						if (customField != null && !customField.isEmpty()) {
							if (customField.get(Constants.CUSTOMFIELD) instanceof List<?>) {
								recipientNames = (List<String>) customField.get(Constants.CUSTOMFIELD);
								for (String recipient : recipientNames) {
									recipientName=recipient;
									break;
								}
							}
							else if (customField.get(Constants.CUSTOMFIELD) != null) {
								recipientName = (String) customField.get(Constants.CUSTOMFIELD);
							}
						}
					}
				}

				recipientStatus = (String) recepientStatusMap.get(Constants.STATUS);
			}
		}
		docusignResponse.setStatus(true);
		LOGGER.info("RecipientStatus {}, RecipientName{} ", recipientStatus, recipientName);
		if (Objects.nonNull(recipientName) && Objects.nonNull(recipientStatus)) {
			docusignResponse = setTypeAndStatus(docusignResponse, recipientName, recipientStatus);
			LOGGER.info("docusignResponse after set type and status if docusign response is not null"
					+ docusignResponse.toString());
		} else {
			docusignResponse.setType(DocuSignStage.CUSTOMER.toString());
			if (envelopeStatus.getStatus().equals(DocusignConstants.COMPLETED)) {
				docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_SIGNED.toString());
			} else {
				docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_DECLINED.toString());
			}
		}

		if (Objects.isNull(docusignResponse.getType())) {
			docusignResponse.setType(DocuSignStage.CUSTOMER.toString());
			if (envelopeStatus.getStatus().equals(DocusignConstants.COMPLETED)) {
				docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_SIGNED.toString());
			} else {
				docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_DECLINED.toString());
			}

		}
		LOGGER.info(
				"docusignResponse after set type and status if docusign type is null" + docusignResponse.toString());
		LOGGER.info("MDC Filter token value in before Queue call constructDocusignResponseIfDocumentId1 {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		String docuSignResponseStr = (String) mqUtils.sendAndReceive(docusignNotications,
				Utils.convertObjectToJson(docusignResponse));
		LOGGER.info("DocuSign Response received {}", docuSignResponseStr);
		if (StringUtils.isNotBlank(docuSignResponseStr)) {
			CommonDocusignRequest docusignRequest = (CommonDocusignRequest) Utils
					.convertJsonToObject(docuSignResponseStr, CommonDocusignRequest.class);
			EnvelopeSummary envelopeSummary = constructDocusign(docusignRequest, accountId, base64Doc);
			if (envelopeSummary != null) {
				String envelopeId = envelopeSummary.getEnvelopeId();
				docusignResponse.setEnvelopeId(envelopeId);
				docusignResponse.setQuoteId(docusignRequest.getQuoteId());
				docusignResponse.setQuoteLeId(docusignRequest.getQuoteLeId());
				docusignResponse.setType(docusignRequest.getType());
				LOGGER.info("MDC Filter token value in before Queue call constructDocusignResponseIfDocumentId1 {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				LOGGER.info("Docusign Response sent is : {}",  Utils.convertObjectToJson(docusignResponse));
				mqUtils.send(docuSignResponseQueue, Utils.convertObjectToJson(docusignResponse));
				LOGGER.info("Envelope: {}", envelopeId);
				Map<String,String> req=new HashMap<>();
				req.put("STATUS", "SUCCESS");
				req.put("ENVELOPE_ID", docusignNoticationResponse
						.getDocuSignEnvelopeInformation().getEnvelopeStatus().getEnvelopeID());
				mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
			}
		}else {
			Map<String,String> req=new HashMap<>();
			req.put("STATUS", "FAILURE");
			req.put("ENVELOPE_ID", docusignNoticationResponse
					.getDocuSignEnvelopeInformation().getEnvelopeStatus().getEnvelopeID());
			mqUtils.send(webhookStatus, Utils.convertObjectToJson(req));
		}
	}

	/**
	 * constructSigner
	 *
	 * @param docusignRequest
	 * @param signer
	 */
	private void constructSigner(CommonDocusignRequest docusignRequest, Signer signer, String type) {
		signer.setEmail(docusignRequest.getToEmail());
		signer.setName(docusignRequest.getToName());
		if (DocuSignStage.CUSTOMER.toString().equalsIgnoreCase(type)) {
			signer.setRecipientId(new Integer(DocuSignStage.CUSTOMER.getAuditCode()).toString());
		} else if (DocuSignStage.SUPPLIER.toString().equalsIgnoreCase(type)) {
			signer.setRecipientId(new Integer(DocuSignStage.SUPPLIER.getAuditCode()).toString());
		} else if(DocuSignStage.CUSTOMER1.toString().equalsIgnoreCase(type)) {
			signer.setRecipientId(new Integer(DocuSignStage.CUSTOMER1.getAuditCode()).toString());
		} else if(DocuSignStage.CUSTOMER2.toString().equalsIgnoreCase(type)) {
			signer.setRecipientId(new Integer(DocuSignStage.CUSTOMER2.getAuditCode()).toString());
		} else if(DocuSignStage.COMMERCIAL.toString().equalsIgnoreCase(type)) {
			signer.setRecipientId(new Integer(DocuSignStage.COMMERCIAL.getAuditCode()).toString());
		}
		List<String> name = new ArrayList<>();
		name.add(type);
		signer.setCustomFields(name);
		List<SignHere> signHereTabs = new ArrayList<>();
		if (docusignRequest.getAnchorStrings() != null) {
			docusignRequest.getAnchorStrings().stream().forEach(anchorString -> {
				SignHere signHere = new SignHere();
				signHere.setDocumentId(DocusignConstants.ONE);
				signHere.setAnchorString(anchorString);
				signHere.setAnchorXOffset(DocusignConstants.TWO);
				signHere.setAnchorYOffset(DocusignConstants.ZERO);
				signHere.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				signHere.setAnchorUnits(DocusignConstants.INCHES);
				signHereTabs.add(signHere);
			});
		}
		List<DateSigned> dateSignedTabs = new ArrayList<>();
		if (docusignRequest.getDateSignedAnchorStrings() != null
				&& !docusignRequest.getDateSignedAnchorStrings().isEmpty()) {
			for (String dateSignedString : docusignRequest.getDateSignedAnchorStrings()) {
				DateSigned dateSigned = new DateSigned();
				dateSigned.setDocumentId(DocusignConstants.ONE);
				dateSigned.setAnchorString(dateSignedString);
				if (dateSignedString.equals(DocusignConstants.CUSTOMER_SIGNED_DATE)) {
					dateSigned.setAnchorXOffset(DocusignConstants.ONE_SIXTY);
				} else {
					dateSigned.setAnchorXOffset(DocusignConstants.X_SUPP_PIXEL);
				}
				dateSigned.setAnchorYOffset(DocusignConstants.ZERO);
				dateSigned.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				dateSignedTabs.add(dateSigned);
			}
		}
		List<FullName> fullNames = new ArrayList<>();
		if (docusignRequest.getCustomerNameAnchorStrings() != null
				&& !docusignRequest.getCustomerNameAnchorStrings().isEmpty()) {
			for (String nameString : docusignRequest.getCustomerNameAnchorStrings()) {
				FullName fullName = new FullName();
				fullName.setDocumentId(DocusignConstants.ONE);
				fullName.setAnchorString(nameString);
				if (nameString.equals(DocusignConstants.CUSTOMER_NAME)) {
					fullName.setAnchorXOffset(DocusignConstants.ONE_SIXTY);
				} else {
					fullName.setAnchorXOffset(DocusignConstants.X_SUPP_PIXEL);
				}
				fullName.setAnchorYOffset(DocusignConstants.ZERO);
				fullName.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				fullNames.add(fullName);
			}
		}
		
		if (docusignRequest.getCommercialAnchorStrings() != null) {
			docusignRequest.getCommercialAnchorStrings().stream().forEach(anchorString -> {
				SignHere signHere = new SignHere();
				signHere.setDocumentId(DocusignConstants.ONE);
				signHere.setAnchorString(anchorString);
				signHere.setAnchorXOffset(DocusignConstants.TWO);
				signHere.setAnchorYOffset(DocusignConstants.ZERO);
				signHere.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				signHere.setAnchorUnits(DocusignConstants.INCHES);
				signHereTabs.add(signHere);
			});
		}
		
		if (docusignRequest.getCommercialDateSignedAnchorStrings() != null
				&& !docusignRequest.getCommercialDateSignedAnchorStrings().isEmpty()) {
			for (String dateSignedString : docusignRequest.getCommercialDateSignedAnchorStrings()) {
				DateSigned dateSigned = new DateSigned();
				dateSigned.setDocumentId(DocusignConstants.ONE);
				dateSigned.setAnchorString(dateSignedString);
				if (dateSignedString.equals(DocusignConstants.COMMERCIAL_SIGNED_DATE)) {
					dateSigned.setAnchorXOffset(DocusignConstants.ONE_SIXTY);
				} else {
					dateSigned.setAnchorXOffset(DocusignConstants.X_SUPP_PIXEL);
				}
				dateSigned.setAnchorYOffset(DocusignConstants.ZERO);
				dateSigned.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				dateSignedTabs.add(dateSigned);
			}
		}
		
		if (docusignRequest.getCommercialNameAnchorStrings() != null
				&& !docusignRequest.getCommercialNameAnchorStrings().isEmpty()) {
			for (String nameString : docusignRequest.getCommercialNameAnchorStrings()) {
				FullName fullName = new FullName();
				fullName.setDocumentId(DocusignConstants.ONE);
				fullName.setAnchorString(nameString);
				if (nameString.equals(DocusignConstants.COMMERCIAL_NAME)) {
					fullName.setAnchorXOffset(DocusignConstants.ONE_SIXTY);
				} else {
					fullName.setAnchorXOffset(DocusignConstants.X_SUPP_PIXEL);
				}
				fullName.setAnchorYOffset(DocusignConstants.ZERO);
				fullName.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				fullNames.add(fullName);
			}
		}
		
		
		Tabs tabs = new Tabs();
		tabs.setSignHereTabs(signHereTabs);
		tabs.setDateSignedTabs(dateSignedTabs);
		tabs.setFullNameTabs(fullNames);
		signer.setTabs(tabs);
	}

	/**
	 * constructDocument
	 * 
	 * @param docusignRequest
	 * @param base64Doc
	 * @return
	 */
	private Document constructDocument(CommonDocusignRequest docusignRequest, String base64Doc) {
		Document document = new Document();
		document.setDocumentBase64(base64Doc);
		document.setName(docusignRequest.getFileName());
		document.setDocumentId(docusignRequest.getDocumentId());
		return document;
	}

	/**
	 * constructApiClient
	 * 
	 * @return
	 * @throws TclCommonException
	 */
	private ApiClient constructApiClient() throws TclCommonException {
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath(baseUrl);
		DocuSignRequest docuSignRequest = constructCredentials();
		String creds = Utils.convertObjectToJson(docuSignRequest);
		apiClient.addDefaultHeader(DocusignConstants.X_DOCUSIGN_AUTHENTICATION, creds);
		return apiClient;
	}

	/**
	 * constructCredentials
	 * 
	 * @return
	 */
	private DocuSignRequest constructCredentials() {
		DocuSignRequest docuSignRequest = new DocuSignRequest();
		docuSignRequest.setUserName(userName);
		docuSignRequest.setPassword(password);
		docuSignRequest.setIntegratorKey(integratorKey);
		return docuSignRequest;
	}

	/**
	 * 
	 * getEnvelopeDocuments
	 * 
	 * @param envelopeId
	 * @param documentId
	 * @return
	 * @throws TclCommonException
	 */
	public byte[] getEnvelopeDocuments(String envelopeId, String documentId, int retryCount) throws TclCommonException {
		if (retryCount >= 3) {
			return new byte[0];
		}
		try {
			if (retryCount != 1) {
				TimeUnit.SECONDS.sleep(10);
			}
			Utils.disableSslVerification();
			String accountId = setLoginInformations(1);
			EnvelopesApi envelopesApi = new EnvelopesApi();
			return envelopesApi.getDocument(accountId, envelopeId, documentId);
		} catch (Exception e) {
			LOGGER.info("Docusign document error : {}", ExceptionUtils.getStackTrace(e));
			LOGGER.info("Retrying {}", retryCount);
			return getEnvelopeDocuments(envelopeId, documentId, retryCount + 1);
		}
	}

	private FolderDetailsResponse getFolderDetailsForTheAccount(String accountUuid) throws TclCommonException {
		try {
			String url = baseUrl.concat(DocusignConstants.V2_ACCOUNTS_URL).concat(accountUuid)
					.concat(DocusignConstants.FOLDERS_URL);
			Map<String, String> authHeader = new HashMap<>();
			DocuSignRequest docuSignRequest = constructCredentials();
			String creds = Utils.convertObjectToJson(docuSignRequest);
			authHeader.put(DocusignConstants.X_DOCUSIGN_AUTHENTICATION, creds);
			RestResponse getRoleByNameResponse = restClientService.getCallForKeycloak(url, authHeader);
			if (getRoleByNameResponse.getData() != null) {
				return (FolderDetailsResponse) Utils.convertJsonToObject(getRoleByNameResponse.getData(),
						FolderDetailsResponse.class);
			}
		} catch (Exception e) {
			LOGGER.info("Docusign error : {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * This method return login information
	 * 
	 * @return
	 * @throws TclCommonException
	 * @throws ApiException
	 */
	public LoginInformation getLoginInfo() throws TclCommonException, ApiException {
		Utils.disableSslVerification();
		Configuration.setDefaultApiClient(constructApiClient());
		AuthenticationApi authApi = new AuthenticationApi();
		AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
		loginOps.setApiPassword(DocusignConstants.TRUE);
		loginOps.setIncludeAccountIdGuid(DocusignConstants.TRUE);
		return authApi.login(loginOps);
	}

	private String getFolderIdByType(String type, String accountUUid) throws TclCommonException {
		try {
			FolderDetailsResponse folderDetailsResponse = getFolderDetailsForTheAccount(accountUUid);
			if (folderDetailsResponse != null && folderDetailsResponse.getFolders() != null
					&& !folderDetailsResponse.getFolders().isEmpty()) {
				Optional<Folder> recycleFolder = folderDetailsResponse.getFolders().stream()
						.filter(folder -> folder.getType().equals(type)).findFirst();
				if (recycleFolder.isPresent()) {
					return recycleFolder.get().getFolderId();
				}
			}

		} catch (Exception e) {
			LOGGER.info("Docusign error : {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;
	}

	/**
	 * 
	 * Move envelope to recycle bin
	 * 
	 * @param quoteId
	 * @param isCustomerEnvelope
	 * @return
	 * @throws TclCommonException
	 * @throws ApiException
	 */
	public String moveEnvelopeToRecycleBin(String envelopeId) throws TclCommonException, ApiException {
		LOGGER.info("Envelope ID to delete is {}",envelopeId);
		if (envelopeId == null) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String accountUuid = getAccountId();
			if (accountUuid != null) {
				String recycleFolderId = getFolderIdByType(DocusignConstants.RECYCLEBIN, accountUuid);
				String sentItemsFolderId = getFolderIdByType(DocusignConstants.SENTITEMS, accountUuid);
				if (recycleFolderId != null && sentItemsFolderId != null) {
					return deleteDocumentIfPresent(envelopeId, recycleFolderId, sentItemsFolderId, accountUuid);
				}
			}
		} catch (Exception e) {
			LOGGER.info("Docusign error : {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return "";
	}

	private DocusignMoveFolderRequest constructDocusignFolderMoveRequest(String envelopeId, String fromFolderId) {
		List<String> envelopeIds = new ArrayList<>();
		envelopeIds.add(envelopeId);
		DocusignMoveFolderRequest docusignMoveFolderRequest = new DocusignMoveFolderRequest();
		docusignMoveFolderRequest.setFromFolderId(fromFolderId);
		docusignMoveFolderRequest.setEnvelopeIds(envelopeIds);
		return docusignMoveFolderRequest;
	}

	private String deleteDocumentIfPresent(String envelopeId, String recycleFolderId, String sentItemsFolderId,
			String accountUuid) throws TclCommonException {
		if (envelopeId != null) {
			changeTheEnvelopeStatusSent(envelopeId, accountUuid);
			changeTheEnvelopeStatusVoid(envelopeId, accountUuid);
			String url = baseUrl.concat(DocusignConstants.V2_ACCOUNTS_URL).concat(accountUuid)
					.concat(DocusignConstants.FOLDERS_URL_WITH_SLASH).concat(recycleFolderId);
			Map<String, String> authHeader = new HashMap<>();
			String creds = Utils.convertObjectToJson(constructCredentials());
			authHeader.put(DocusignConstants.X_DOCUSIGN_AUTHENTICATION, creds);
			DocusignMoveFolderRequest docusignMoveFolderRequest = constructDocusignFolderMoveRequest(envelopeId,
					sentItemsFolderId);
			RestResponse getRoleByNameResponse = restClientService.putKeyCloak(url,
					Utils.convertObjectToJson(docusignMoveFolderRequest), authHeader);
			if (getRoleByNameResponse.getStatus().equals(Status.SUCCESS)) {
				return ResponseResource.RES_SUCCESS;
			}

		}
		return null;
	}

	/**
	 * @author ANANDHI VIJAY This method returns docusign account ID
	 * @return
	 * @throws TclCommonException
	 * @throws ApiException
	 */
	public String getAccountId() throws TclCommonException, ApiException {
		Utils.disableSslVerification();
		Configuration.setDefaultApiClient(constructApiClient());
		AuthenticationApi authApi = new AuthenticationApi();
		AuthenticationApi.LoginOptions loginOps = authApi.new LoginOptions();
		loginOps.setApiPassword(DocusignConstants.TRUE);
		loginOps.setIncludeAccountIdGuid(DocusignConstants.TRUE);
		return authApi.login(loginOps).getLoginAccounts().get(0).getAccountIdGuid();
	}

	private String changeTheEnvelopeStatusVoid(String envelopeId, String accountUuid) throws TclCommonException {
		try {
			String url = baseUrl.concat(DocusignConstants.V2_ACCOUNTS_URL).concat(accountUuid)
					.concat(DocusignConstants.ENVELOPE_URL).concat(envelopeId);
			Map<String, String> authHeader = new HashMap<>();
			String creds = Utils.convertObjectToJson(constructCredentials());
			authHeader.put(DocusignConstants.X_DOCUSIGN_AUTHENTICATION, creds);
			RestResponse getRoleByNameResponse = restClientService.putKeyCloak(url,
					Utils.convertObjectToJson(constructDocusignEnvelopeStatusChangeRequestVoid()), authHeader);
			if (getRoleByNameResponse.getData() == null) {
				return ResponseResource.RES_SUCCESS;
			}
		} catch (Exception e) {
			LOGGER.info("Docusign error : {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;

	}

	private String changeTheEnvelopeStatusSent(String envelopeId, String accountUuid) throws TclCommonException {
		try {
			String url = baseUrl.concat(DocusignConstants.V2_ACCOUNTS_URL).concat(accountUuid)
					.concat(DocusignConstants.ENVELOPE_URL).concat(envelopeId);
			Map<String, String> authHeader = new HashMap<>();
			String creds = Utils.convertObjectToJson(constructCredentials());
			authHeader.put(DocusignConstants.X_DOCUSIGN_AUTHENTICATION, creds);
			RestResponse getRoleByNameResponse = restClientService.putKeyCloak(url,
					Utils.convertObjectToJson(constructDocusignEnvelopeStatusChangeRequestSent()), authHeader);
			if (getRoleByNameResponse.getData() == null) {
				return ResponseResource.RES_SUCCESS;
			}
		} catch (Exception e) {
			LOGGER.info("Docusign error : {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return null;

	}

	private DocusignEnvelopeStatusChangeRequest constructDocusignEnvelopeStatusChangeRequestVoid() {
		DocusignEnvelopeStatusChangeRequest docusignEnvelopeStatusChangeRequest = new DocusignEnvelopeStatusChangeRequest();
		docusignEnvelopeStatusChangeRequest.setStatus(DocusignConstants.VOID);
		docusignEnvelopeStatusChangeRequest.setVoidedReason(DocusignConstants.VOID_REASON);
		return docusignEnvelopeStatusChangeRequest;
	}

	private DocusignEnvelopeStatusChangeRequest constructDocusignEnvelopeStatusChangeRequestSent() {
		DocusignEnvelopeStatusChangeRequest docusignEnvelopeStatusChangeRequest = new DocusignEnvelopeStatusChangeRequest();
		docusignEnvelopeStatusChangeRequest.setStatus(DocusignConstants.SENT);
		return docusignEnvelopeStatusChangeRequest;
	}

	/**
	 * @author ANANDHI VIJAY Resend the Envelope based on the envelope Id
	 * @param envelopeId
	 * @return
	 * @throws TclCommonException
	 * @throws ApiException
	 */
	public String resendEnvelope(String envelopeId) throws TclCommonException, ApiException {
		try {
			String accountUuid = getAccountId();
			if (envelopeId != null && accountUuid != null) {
				String url = baseUrl.concat(DocusignConstants.V2_ACCOUNTS_URL).concat(accountUuid)
						.concat(DocusignConstants.ENVELOPE_URL).concat(envelopeId).concat(DocusignConstants.RESEND_URL);
				Map<String, String> authHeader = new HashMap<>();
				String creds = Utils.convertObjectToJson(constructCredentials());
				authHeader.put(DocusignConstants.X_DOCUSIGN_AUTHENTICATION, creds);
				RestResponse getRoleByNameResponse = restClientService.putKeyCloak(url,
						Utils.convertObjectToJson(constructDocusignEnvelopeStatusChangeRequestSent()), authHeader);
				if (getRoleByNameResponse.getData() != null) {
					return ResponseResource.RES_SUCCESS;
				}
			}
		} catch (Exception e) {
			LOGGER.info("Docusign error : {}", ExceptionUtils.getStackTrace(e));
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return "";
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
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		try {
			String queue = (String) mqUtils.sendAndReceive(getDocumentQueue, quoteId.toString());
			if (StringUtils.isNotBlank(queue)) {
				DocusignGetDocumentRequest queueResponse = (DocusignGetDocumentRequest) Utils.convertJsonToObject(queue,
						DocusignGetDocumentRequest.class);
				if (queueResponse != null && queueResponse.getDocumentId() != null
						&& queueResponse.getEnvelopeId() != null && queueResponse.getQuoteCode() != null) {
					byte[] pdf = getEnvelopeDocuments(queueResponse.getEnvelopeId(), queueResponse.getDocumentId(), 1);
					if (pdf != null && pdf.length > 0) {

						String fileName = "COF_" + queueResponse.getQuoteCode() + ".pdf";
						response.reset();
						response.setContentType(MediaType.APPLICATION_PDF_VALUE);
						response.setContentLength(pdf.length);
						response.setHeader(DocusignConstants.EXPIRES + CommonConstants.COLON, "0");
						response.setHeader(DocusignConstants.CONTENT_DISPOSITION,
								ATTACHEMENT_FILE_NAME_HEADER + fileName + "\"");
						FileCopyUtils.copy(pdf, response.getOutputStream());
					}
				}
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return tempDownloadUrl;
	}

	/**
	 * constructReviewer
	 *
	 * @param docusignRequest
	 * @param reviewer
	 */
	private Signer constructReviewer(CommonDocusignRequest docusignRequest, Signer reviewer, String type) {

		LOGGER.info("Type is ::: {}", type);

		reviewer.setEmail(docusignRequest.getToEmail());
		reviewer.setName(docusignRequest.getToName());
		if (DocuSignStage.REVIEWER1.toString().equalsIgnoreCase(type)) {
			reviewer.setRecipientId(new Integer(DocuSignStage.REVIEWER1.getAuditCode()).toString());
		} else if (DocuSignStage.REVIEWER2.toString().equalsIgnoreCase(type)) {
			reviewer.setRecipientId(new Integer(DocuSignStage.REVIEWER2.getAuditCode()).toString());
		}
		List<String> name = new ArrayList<>();
		name.add(type);
		reviewer.setCustomFields(name);

		List<Approve> approveTabs = new ArrayList<>();
		if (docusignRequest.getDateSignedAnchorStrings() != null
				&& !docusignRequest.getDateSignedAnchorStrings().isEmpty()) {
			docusignRequest.getDateSignedAnchorStrings().stream().forEach(signedDateString -> {
				Approve approve = new Approve();
				approve.setDocumentId(DocusignConstants.ONE);
				approve.setAnchorString(signedDateString);
				approve.setAnchorXOffset(DocusignConstants.THREE);
				approve.setAnchorYOffset("0.5");
				approve.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				approve.setAnchorUnits(DocusignConstants.INCHES);
				approveTabs.add(approve);
			});
		}

		List<Decline> declineTabs = new ArrayList<>();
		if (docusignRequest.getDateSignedAnchorStrings() != null
				&& !docusignRequest.getDateSignedAnchorStrings().isEmpty()) {
			docusignRequest.getDateSignedAnchorStrings().stream().forEach(signedDateString -> {
				Decline decline = new Decline();
				decline.setDocumentId(DocusignConstants.ONE);
				decline.setAnchorString(signedDateString);
				decline.setAnchorXOffset(DocusignConstants.FIVE);
				decline.setAnchorYOffset("0.5");
				decline.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				decline.setAnchorUnits(DocusignConstants.INCHES);
				declineTabs.add(decline);
			});
		}

		List<DateSigned> dateSignedTabs = new ArrayList<>();
		if (docusignRequest.getApproverDateAnchorStrings() != null
				&& !docusignRequest.getApproverDateAnchorStrings().isEmpty()) {
			for (String dateSignedString : docusignRequest.getApproverDateAnchorStrings()) {
				LOGGER.info("inside the approver anchor");
				DateSigned dateSigned = new DateSigned();
				dateSigned.setDocumentId(DocusignConstants.ONE);
				dateSigned.setAnchorString(dateSignedString);
				if (dateSignedString.equals(DocusignConstants.APPROVER_1_SIGNED_DATE)) {
					dateSigned.setAnchorXOffset("300");
				} else {
					dateSigned.setAnchorXOffset("300");
				}
				dateSigned.setAnchorYOffset(DocusignConstants.ZERO);
				dateSigned.setAnchorIgnoreIfNotPresent(DocusignConstants.FALSE);
				dateSignedTabs.add(dateSigned);
			}
		}
		
		Tabs tabs = new Tabs();
		tabs.setApproveTabs(approveTabs);
		tabs.setDeclineTabs(declineTabs);
		tabs.setDateSignedTabs(dateSignedTabs);
		reviewer.setTabs(tabs);
		return reviewer;
	}

	/**
	 * Method to set type and docusignstatus
	 *
	 * @param docusignResponse
	 * @param roleName
	 * @param recipientStatus
	 * @return
	 */
	private CommonDocusignResponse setTypeAndStatus(CommonDocusignResponse docusignResponse, String recipientName,
			String recipientStatus) {
		LOGGER.info("Setting the docusign Type for getting the flow");
		if (Objects.nonNull(recipientName) && Objects.nonNull(recipientStatus)) {
			switch (recipientName) {
			case "REVIEWER1": {
				LOGGER.info("Entering Reviewer 1 flow");
				docusignResponse.setType(DocuSignStage.REVIEWER1.toString());
				if (DocuSignStatus.COMPLETED.toString().equalsIgnoreCase(recipientStatus)) {
					docusignResponse.setDocusignStatus(DocuSignStatus.REVIEWER1_APPROVED.toString());
				} else {
					docusignResponse.setDocusignStatus(DocuSignStatus.REVIEWER1_DECLINED.toString());
				}
				break;
			}
			case "REVIEWER2": {
				LOGGER.info("Entering Reviewer 2 flow");
				docusignResponse.setType(DocuSignStage.REVIEWER2.toString());
				if (DocuSignStatus.COMPLETED.toString().equalsIgnoreCase(recipientStatus)) {
					docusignResponse.setDocusignStatus(DocuSignStatus.REVIEWER2_APPROVED.toString());
				} else {
					docusignResponse.setDocusignStatus(DocuSignStatus.REVIEWER2_DECLINED.toString());
				}
				break;
			}
			case "CUSTOMER": {
				LOGGER.info("Entering Customer flow");
				docusignResponse.setType(DocuSignStage.CUSTOMER.toString());
				if (DocuSignStatus.COMPLETED.toString().equalsIgnoreCase(recipientStatus)) {
					docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_SIGNED.toString());
				} else {
					docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER_DECLINED.toString());
				}
				break;
			}
			case "CUSTOMER1": {
				LOGGER.info("Entering Customer1 flow");
				docusignResponse.setType(DocuSignStage.CUSTOMER1.toString());
				if (DocuSignStatus.COMPLETED.toString().equalsIgnoreCase(recipientStatus)) {
					docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER1_SIGNED.toString());
				} else {
					docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER1_DECLINED.toString());
				}
				break;
			}
			case "CUSTOMER2": {
				LOGGER.info("Entering Customer2 flow");
				docusignResponse.setType(DocuSignStage.CUSTOMER2.toString());
				if (DocuSignStatus.COMPLETED.toString().equalsIgnoreCase(recipientStatus)) {
					docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER2_SIGNED.toString());
				} else {
					docusignResponse.setDocusignStatus(DocuSignStatus.CUSTOMER2_DECLINED.toString());
				}
				break;
			}
			case "SUPPLIER": {
				LOGGER.info("Entering Supplier flow");
				docusignResponse.setType(DocuSignStage.SUPPLIER.toString());
				if (DocuSignStatus.COMPLETED.toString().equalsIgnoreCase(recipientStatus)) {
					docusignResponse.setDocusignStatus(DocuSignStatus.SUPPLIER_SIGNED.toString());
				} else {
					docusignResponse.setDocusignStatus(DocuSignStatus.SUPPLIER_DECLINED.toString());
				}
				break;
			}
			case "COMMERCIAL": {
				LOGGER.info("Entering Supplier flow");
				docusignResponse.setType(DocuSignStage.COMMERCIAL.toString());
				if (DocuSignStatus.COMPLETED.toString().equalsIgnoreCase(recipientStatus)) {
					docusignResponse.setDocusignStatus(DocuSignStatus.COMMERCIAL_SIGNED.toString());
				} else {
					docusignResponse.setDocusignStatus(DocuSignStatus.COMMERCIAL_DECLINED.toString());
				}
				break;
			}
			default:
				break;
			}
		}
		return docusignResponse;
	}


	/**
	 * processWebHook
	 */
	public void processWebHook(String responseBody) {
		LOGGER.info("Response Received {}", responseBody);
		LOGGER.info("Data received from DS Connect: {}", responseBody);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			org.w3c.dom.Document xml = builder.parse(new InputSource(new StringReader(responseBody)));
			xml.getDocumentElement().normalize();
			LOGGER.info("Connect data parsed!");
			Element envelopeStatus = (Element) xml.getElementsByTagName("EnvelopeStatus").item(0);
			String envelopeId = envelopeStatus.getElementsByTagName("EnvelopeID").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("envelopeId= {}", envelopeId);
			String eStatus = envelopeStatus.getElementsByTagName("Status").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("envelopeStatus= {}", eStatus);
			String timeGenerated = envelopeStatus.getElementsByTagName("TimeGenerated").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("timeGenerated= {}", timeGenerated);
			Element documentStatuses = (Element) envelopeStatus.getElementsByTagName("DocumentStatuses").item(0);
			Element documentStatus = (Element) documentStatuses.getElementsByTagName("DocumentStatus").item(0);
			String documentId = documentStatus.getElementsByTagName("ID").item(0).getChildNodes().item(0)
					.getNodeValue();
			LOGGER.info("documentId= {}", documentId);
			JSONObject xmlJSONObj = XML.toJSONObject(responseBody);
			LOGGER.info("Json Obj for envelopeId {} = {}", envelopeId, xmlJSONObj);
			processDocusignNotication((DocuSignNotificationResponse) Utils.convertJsonToObject(xmlJSONObj.toString(),
					DocuSignNotificationResponse.class));
		} catch (Exception e) {
			LOGGER.info("Error in Response : {} ", e);
		}

	}
	
	public String retryDocusignDocument(String envelopeId) {
		try {
			String accountUuid = getAccountId();
			if (accountUuid != null) {
				String url = baseUrl.concat(DocusignConstants.V2_1_ACCOUNTS_URL).concat(accountUuid)
						.concat(DocusignConstants.CONNECT_PATH).concat(envelopeId)
						.concat(DocusignConstants.RETRY_QUEUE);
				LOGGER.info("Base Url called {}", url);
				Map<String, String> authHeader = new HashMap<>();
				String creds = Utils.convertObjectToJson(constructCredentials());
				authHeader.put(DocusignConstants.X_DOCUSIGN_AUTHENTICATION, creds);
				RestResponse getRoleByNameResponse = restClientService.putKeyCloak(url, "", authHeader);
				LOGGER.info("Response received  {}", getRoleByNameResponse);
				if (getRoleByNameResponse.getStatus().equals(Status.SUCCESS)) {
					return Status.SUCCESS.toString();
				}
			}
		} catch (Exception e) {
			LOGGER.info("Docusign error : {}", ExceptionUtils.getStackTrace(e));
		}
		return Status.FAILURE.toString();
	}

}
