package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.tcl.dias.servicefulfillment.entity.custom.IScTeamsDRServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.*;
import com.tcl.dias.servicefulfillment.entity.repository.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.IScWebexServiceCommercial;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmMaterialAvailabilityBean;
import com.tcl.dias.servicefulfillmentutils.beans.InternalMailMomBean;
import com.tcl.dias.servicefulfillmentutils.beans.MrnListBean;
import com.tcl.dias.servicefulfillmentutils.beans.MrnNotificationBean;
import com.tcl.dias.servicefulfillmentutils.beans.SerialNumberBean;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationNotEligibleNoticationBean;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.sap.response.DisplayMaterialResponse;
import com.tcl.dias.servicefulfillmentutils.sap.response.SapQuantityAvailableRequest;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the NotificationService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly=true,isolation=Isolation.READ_UNCOMMITTED)
public class NotificationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationService.class);

	@Value("${notification.mail.from}")
	String fromAddress;

	@Value("${notification.mail.bcc}")
	String[] notificationBccMail;

	@Value("${notification.template.overdueReminder}")
	String overdueReminder;
	
	@Value("${notification.template.validation}")
	String validationTemplate;
	
	@Value("${notification.template.muxIntegration}")
	String notifyMuxIntegrationTemplate;
	
	@Value("${notification.template.taskAssignment}")
	String notifyTaskAssignmentTemplate;
	
	@Value("${notification.template.vendorArrangeOspIbd}")
	String notifyVendorArrangeOspIbdTemplate;
		
	@Value("${notification.template.customerSiteVisit}")
	String notifyCustomerSiteVisitTemplate;
	
	@Value("${notification.template.fieldEngineerSurvey}")
	String notifyFieldEngineerSurveyTemplate;
	
	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;
	
	@Value("${notification.template.vendorTestResults}")
	String notifyVendorTestResultsTemplate;
		
	@Value("${notification.template.customerSiteReadiness}")
	String notifyCustomerSiteReadinessTemplate;
	
	@Value("${notification.template.builderContract}")
	String notifyBuilderContractTemplate;
	
	@Value("${notification.template.taskAssignmentAdmin}")
	String notifyTaskAssignmentAdminTemplate;
		
	@Value("${notification.template.ipc.taskCompletion}")
	private String notifyIpcTaskCompletionTemplate;
	
	@Value("${notification.template.vendorWorkOrder}")
	String notifyVendorWorkOrderTemplate;
	
	@Value("${notification.template.netpConfiguration}")
	String notifyNetpConfigurationTemplate;
	
	@Value("${swift.api.enabled}")
	String swiftApiEnabled;
		
	@Value("${notification.template.customeroverdue}")
	String customerOverdue;
	
	@Value("${notification.template.automigration.failure}")
	String notifyAutoMigrationFailureTemplate;

	@Value("${notification.template.sdwancreation.failure}")
	String notifySdwanCreationFailureTemplate;

	@Value("${notification.template.ssdump.failure}")
	String notifyTeamForNoSSDump;

	@Autowired
	FileStorageService fileStorageService;
	
	@Autowired
	MstTaskDefRepository mstTaskDefRepository;
	
	@Autowired
	NotificationMailJobRepository notificationMailJobRepository;
	
	@Autowired
	MQUtils mqUtils;
	
	@Autowired
	TaskRepository taskRepository;

	@Autowired
	MstTaskAssignmentRepository  MstTaskAssignmentRepository;
	
	@Autowired
	TaskService taskService;
	
	
	@Value("${app.host}")
	String appHost;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
	
	@Autowired
	ScWebexServiceCommercialRepository scWebexServiceCommercialRepository;

	@Autowired
	ScTeamsDRServiceCommercialRepository scTeamsDRServiceCommercialRepository;

	@Value("${notification.template.notifypocreation}")
	String notifyPoCreationTemplate;
	
	@Value("${notification.template.notifymrncreation}")
	String notifyMrnCreationTemplate;
	
	@Value("${notification.template.notifycpevendor}")
	String notifyCpeVendorTemplate;
	
	@Autowired
	MstStateToDistributionCenterMappingRepository mstStateToDistributionCenterMappingRepository;
	
	@Autowired
	ComponentAndAttributeService componentAndAttributeService;
	
	@Value("${notification.template.notifycustomeracceptance}")
	String notifyCustomerAcceptanceTemplate;
	
	@Value("${notification.template.notifycustomertasks}")
	String notifyCustomerTasksTemplate;
	
	@Value("${notification.template.customerwelcome}")
	String notifyCustomerWelcomeTemplate;
	
	@Value("${notification.template.customerwelcome.v2}")
	String notifyCustomerWelcomeTemplateV2;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Value("${notification.template.circuittermination}")
	String notifyCircuitTerminationTemplate;
	
	@Value("${notification.template.notifyoffnetpocreation}")
	String notifyOffnetPoCreationTemplate;
	
	@Autowired
	CpeCostDetailsRepository cpeCostDetailsRepository;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	ProCreationRepository proCreationRepository;
	
	@Value("${notification.template.serviceCancellation}")
	String notifyServiceCancellationTemplate;
	
	@Value("${notification.template.lldMailMom}")
	String lldMailMomTemplate;
	
	@Value("${notification.template.sdwanTrigger}")
	String notifySDWANTrigger;
	
	@Value("${notification.template.sdwanInvalidServiceCodeTrigger}")
	String notifySDWANInvalidServiceCodeTrigger;

	@Autowired
	PdfService pdfService;

	@Value("${notification.template.serviceStatusChange}")
	String notifyServiceStatusChangeTemplate;	
	
	@Value("${notification.template.terminationNotEligible}")
	String notifyTerminationNotEligibleTemplate;	


	@Value("${notification.template.deferredDelivery}")
	String notifyDeferredDelivery;
	
	@Value("${notification.template.dynamicContent}")
	String notifyDynamicContent;
	
	@Value("${notification.template.customeroverduehold}")
	String customerOverdueHold;

	@Value("${notification.template.customeroverduebeforehold}")
	String customerOverdueBeforeHold;

	@Value("${notification.template.unHoldService}")
	String notifyUnHoldForService;

	@Value("${notification.template.reasignment.pm}")
	String notifyReAsignmentServiceTemplate;
	
	@Autowired
	NotificationTriggerRepository notificationTriggerRepository;

	@Value("${notification.template.cancellation.initiation}")
	String notifyServiceCancellationInitiation;
	
	@Value("${notification.template.termination.remainder}")
	String notifyTermination;
	
	@Value("${notification.template.trfDateExtension}")
	String notifyTrfDateExtension;
	
	@Value("${notification.template.offnetRetained}")
	String notifyOffnetCustomerRetained;
	
	@Value("${notification.template.multiVrfSlaveTrigger.failure}")
	String notifySlaveTriggerFailure;



	/**
	 * This method is used to notify Customer about Document Validation
	 * 
	 * notifyValidation
	 * @param customerMail
	 * @param siteAddress
	 * @param customerName
	 * @param serviceCode
	 * @param serviceLink
	 * @return
	 */

	public boolean notifyValidation(String customerMail, String siteAddress, String customerName, String serviceCode,
			String serviceLink) {
		boolean isSuccess = false;
	
		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(siteAddress,"Site Address cannot be null");
			Objects.requireNonNull(customerName,"Customer Name cannot be null");
			Objects.requireNonNull(serviceCode,"Service Code cannot be null");
			Objects.requireNonNull(serviceLink,"Service Link cannot be null");
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			//toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
					.findByGroup("CIM");
			
			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}
			toAddresses.addAll(ccAddresses);
			map.put("serviceCode", serviceCode);
			map.put("serviceLink", serviceLink);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Mandatory document required!!!");
			mailNotificationRequest
					.setTemplateId(validationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;

	}
	
	/**
	 * This method is used to notify about Mux Integration
	 * 
	 * @author diksha garg	  
	 * notifyMuxIntegration
	 * @param customerMail
	 * @param sdnocTeamName
	 * @param serviceId
	 * @param customerName
	 * @param plannedEventSchedule
	 * @param taskDetails
	 * @return
	 */
	
	public boolean notifyMuxIntegration(String customerMail, String sdnocTeamName,Integer serviceId,String customerName, String plannedEventSchedule,
			String taskDetails) {
		boolean isSuccess = false;
		
		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(sdnocTeamName,"SDNOC Team name cannot be null");
			Objects.requireNonNull(serviceId,"Service ID cannot be null");
			Objects.requireNonNull(customerName,"Customer Name cannot be null");
			Objects.requireNonNull(plannedEventSchedule,"Event Schedule cannot be null");
			Objects.requireNonNull(taskDetails,"Task Details cannot be null");
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			//toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
					.findByGroup("CIM");
			
			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}
			toAddresses.addAll(ccAddresses);
			map.put("sdnocTeamName", sdnocTeamName);
			map.put("serviceId", serviceId);
			map.put("customerName", customerName);
			map.put("plannedEventSchedule", plannedEventSchedule);
			map.put("taskDetails", taskDetails);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Mux Integration Notification");
			mailNotificationRequest
					.setTemplateId(notifyMuxIntegrationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyMuxIntegration {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;

	}
	
	
	
	/**
	 * This method is used to notify Vendor to arrange Field Engineer for OSP IBD Work
	 * 
	 * @author diksha garg
	 * notifyVendorArrangeOspIbd
	 * @param customerMail
	 * @param vendorName
	 * @param workType
	 * @param customerName
	 * @param siteDemarc
	 * @param siteAddress
	 * @param siteVisitDateAndTime
	 * @param checkWorkDetails
	 * @return
	 */	
	public boolean notifyVendor(String taskDefKey, String vendorMail, String vendorName,String workType,String customerName, String siteDemarc,
			String siteAddress,String siteVisitDate,String siteVisitTimeSlot,String checkWorkDetails,String subject,String serviceCode, String rfTechnology) {
		boolean isSuccess = false;
	
		try {
			
			Objects.requireNonNull(vendorMail,"Vendor Mail cannot be null");
			Objects.requireNonNull(vendorName,"Vendor name cannot be null");
			Objects.requireNonNull(workType,"Work Type value cannot be null");
			Objects.requireNonNull(customerName,"Customer Name cannot be null");
			Objects.requireNonNull(siteDemarc,"Site Demarcation value cannot be null");
			Objects.requireNonNull(siteAddress,"Site Address cannot be null");
			Objects.requireNonNull(siteVisitDate,"Site Visit Date cannot be null");
			Objects.requireNonNull(checkWorkDetails,"Work Details cannot be null");
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(vendorMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("vendorName", vendorName);
			map.put("workType", workType);
			map.put("customerName", customerName);
			map.put("siteDemarc", siteDemarc);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("siteVisitTimeSlot",siteVisitTimeSlot);
			map.put("checkWorkDetails", checkWorkDetails);
			map.put("subject", subject);
			map.put("serviceId", serviceCode);
			map.put("rfTechnology", rfTechnology);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
		//	if(taskDefKey.equals("notify-vendor-lm-onnet-wireless-site-survey")) {
			if(rfTechnology!=null && !rfTechnology.isEmpty()) {
				if(rfTechnology.toLowerCase().contains("radwin")) {
					mailNotificationRequest.setIsAttachment(true);					
					Resource resource = new ClassPathResource("Radwin.docx");
					File file = resource.getFile();				
					mailNotificationRequest.setAttachmentName(file.getName());
					mailNotificationRequest.setAttachmentPath(file.getPath());
					LOGGER.info("radwin document {} {}",file.getName(),file.getPath());
					//cofMap.put("FILE_SYSTEM_PATH", file.getPath());
				}
				if(rfTechnology.toLowerCase().contains("cambium")) {
					mailNotificationRequest.setIsAttachment(true);					
					Resource resource = new ClassPathResource("Cambium.docx");
					File file = resource.getFile();				
					mailNotificationRequest.setAttachmentName(file.getName());
					mailNotificationRequest.setAttachmentPath(file.getPath());
					LOGGER.info("cambium document {} {}",file.getName(),file.getPath());
				}
				if(rfTechnology.toLowerCase().contains("wimax")) {
					mailNotificationRequest.setIsAttachment(true);					
					Resource resource = new ClassPathResource("Wimax.docx");
					File file = resource.getFile();				
					mailNotificationRequest.setAttachmentName(file.getName());
					mailNotificationRequest.setAttachmentPath(file.getPath());
					LOGGER.info("wimax document {} {}",file.getName(),file.getPath());

				}
				
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest.setTemplateId(notifyVendorArrangeOspIbdTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyVendor {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;

	}
	
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public boolean notifyOverDueReminder(String customerMail, Integer orderId, String customerName, String serviceCode,
			String serviceLink, String taskName, String orderCode, List<String> ccAdminEmail) {
		boolean isSuccess = false;
		try {
			List<NotificationMailJob> notificationMailJobs = notificationMailJobRepository
					.findByServiceCodeAndStatusAndEmailAndType(serviceCode, "NEW", customerMail,"INTERNAL");
			for (NotificationMailJob notificationMailJob : notificationMailJobs) {
				MailNotificationRequest mailNotificationRequest = (MailNotificationRequest) Utils
						.convertJsonToObject(notificationMailJob.getMailRequest(), MailNotificationRequest.class);
				Map<String, Object> dataMapper = mailNotificationRequest.getVariable();
				@SuppressWarnings("unchecked")
				Map<String, List<String>> serviceMapper = (Map<String, List<String>>) dataMapper.get("serviceList");

				if(serviceMapper.containsKey(serviceCode)){
					List<String> tasks=	serviceMapper.get(serviceCode);
					if(!tasks.contains(taskName)){
						tasks.add(taskName);
					}
					serviceMapper.put(serviceCode, tasks);
				}

				notificationMailJob.setMailRequest(Utils.convertObjectToJson(mailNotificationRequest));
				notificationMailJobRepository.save(notificationMailJob);
			}
			if (notificationMailJobs.isEmpty()) {
				String url = appHost + "/optimus/tasks/dashboard";
				processOverdueInitialService(customerMail, orderId, customerName, serviceCode, url, taskName,orderCode, ccAdminEmail);
			}

		} catch (Exception e) {
			LOGGER.error("Error in sending overdue Mail Template", e);
		}

		return isSuccess;
	}
	
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public boolean notifyOverDueReminderCustomer(String customerMail, Integer orderId, String customerName, String serviceCode,
			String serviceLink, String taskName,String orderCode) {
		boolean isSuccess = false;
		try {
			List<NotificationMailJob> notificationMailJobs = notificationMailJobRepository
					.findByServiceCodeAndStatusAndType(serviceCode, "NEW", "CUSTOMER");

			for (NotificationMailJob notificationMailJob : notificationMailJobs) {
				MailNotificationRequest mailNotificationRequest = (MailNotificationRequest) Utils
						.convertJsonToObject(notificationMailJob.getMailRequest(), MailNotificationRequest.class);
				Map<String, Object> dataMapper = mailNotificationRequest.getVariable();
				@SuppressWarnings("unchecked")
				Map<String, List<String>> serviceMapper = (Map<String, List<String>>) dataMapper.get("serviceList");

				if(serviceMapper.containsKey(serviceCode)){
					List<String> tasks=	serviceMapper.get(serviceCode);
					if(!tasks.contains(taskName)){
						tasks.add(taskName);
					}
					serviceMapper.put(serviceCode, tasks);
				}

				notificationMailJob.setMailRequest(Utils.convertObjectToJson(mailNotificationRequest));
				notificationMailJobRepository.save(notificationMailJob);
			}
			if (notificationMailJobs.isEmpty()) {
				processOverdueReminderCustomer(customerMail, orderId, customerName, serviceCode,serviceLink, taskName,orderCode);
			}

		} catch (Exception e) {
			LOGGER.error("Error in sending overdue Mail Template", e);
		}

		return isSuccess;
	}
	
	
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public void processOverdueReminderCustomer(String customerMail, Integer orderId, String customerName,
			String serviceCode, String serviceLink,String taskName, String orderCode) throws TclCommonException {
		
		try {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		List<String> bccAddresses = new ArrayList<>();
		
		List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
				.findByGroup("CIM");
		
		if (!mstTaskRegionList.isEmpty()) {
			bccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
					.map(region -> region.getEmail()).collect(Collectors.toList()));
		}
		/*if (notificationBccMail != null) {
			bccAddresses.addAll(Arrays.asList(notificationBccMail));
		}*/
		mailNotificationRequest.setBcc(bccAddresses);
		mailNotificationRequest.setSubject("Task Reminder!!!");
		mailNotificationRequest.setTemplateId(customerOverdue);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(customerMail);
		List<String> ccAddresses = new ArrayList<>();
		ccAddresses.add(customerSupportEmail);
		//mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setTo(bccAddresses);
		mailNotificationRequest.setCc(ccAddresses);
		HashMap<String, Object> map = new HashMap<>();
		map.put("customerName", customerName);
		Map<String, List<String>> serviceMapper = new HashMap<>();
		List<String> tasks=new ArrayList<>();
		tasks.add(taskName);
		serviceMapper.put(serviceCode, tasks);
		map.put("serviceList", serviceMapper);
		map.put("serviceLink", serviceLink);
		map.put("orderCode", orderCode);
		mailNotificationRequest.setVariable(map);

		NotificationMailJob notifcationMailJob = new NotificationMailJob();
		notifcationMailJob.setCreatedBy(Utils.getSource());
		notifcationMailJob.setCreatedTime(new Date());
		notifcationMailJob.setMailRequest(Utils.convertObjectToJson(mailNotificationRequest));
		notifcationMailJob.setQueueName(notificationMailQueue);
		notifcationMailJob.setScOrderId(orderId);
		notifcationMailJob.setServiceCode(serviceCode);
		notifcationMailJob.setStatus("NEW");
		notifcationMailJob.setType("CUSTOMER");
		notificationMailJobRepository.save(notifcationMailJob);
		
		} catch (Exception e) {
			LOGGER.error("processOverdueReminderCustomerException ", e);
		}
	}

	
	@Transactional(readOnly=false)
	public void processOverdueInitialService(String email, Integer orderId, String userName,String serviceCode,String serviceLink,
											 String taskName,String orderCode, List<String> ccAdminEmail) throws TclCommonException {
		try {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		List<String> bccAddresses = new ArrayList<>();
		
		if (notificationBccMail != null) {
			bccAddresses.addAll(Arrays.asList(notificationBccMail));
		}
		mailNotificationRequest.setBcc(bccAddresses);
		mailNotificationRequest.setSubject("Task Overdue Reminder!!!");
		mailNotificationRequest.setTemplateId(overdueReminder);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(email);
		List<String> ccAddresses = new ArrayList<>();
		if (ccAdminEmail != null && !ccAdminEmail.isEmpty()) {
			ccAddresses.addAll(ccAdminEmail);
		}
		ccAddresses.add(customerSupportEmail);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setCc(ccAddresses);
		HashMap<String, Object> map = new HashMap<>();
		map.put("userName", userName);
		Map<String, List<String>> serviceMapper = new HashMap<>();
		List<String> tasks=new ArrayList<>();
		tasks.add(taskName);
		serviceMapper.put(serviceCode, tasks);
		map.put("serviceList", serviceMapper);
		map.put("serviceLink", serviceLink);
		map.put("orderCode", orderCode);
		mailNotificationRequest.setVariable(map);
		NotificationMailJob notifcationMailJob = new NotificationMailJob();
		notifcationMailJob.setCreatedBy(Utils.getSource());
		notifcationMailJob.setCreatedTime(new Date());
		notifcationMailJob.setMailRequest(Utils.convertObjectToJson(mailNotificationRequest));
		notifcationMailJob.setQueueName(notificationMailQueue);
		notifcationMailJob.setScOrderId(orderId);
		notifcationMailJob.setServiceCode(serviceCode);
		notifcationMailJob.setEmail(email);
		notifcationMailJob.setStatus("NEW");
		notifcationMailJob.setType("INTERNAL");
		notificationMailJobRepository.save(notifcationMailJob);
		
		} catch (Exception e) {
			LOGGER.error("processOverdueInitialServiceException ", e);
		}
	}
	
	
	
	/**
	 * This method is used to notify Customer about Site Visit
	 * 
	 * notifyCustomerSiteVisit
	 * @param customerMail
	 * @param customerName
	 * @param orderId
	 * @param siteAddress
	 * @param fieldEngineerName
	 * @param fieldEngineerContactNumber
	 * @param siteVisitDate
	 * @param serviceCode 
	 * @return
	 */

	public boolean notifyCustomerSiteVisit(String customerMail, String customerName, String orderCode,
										   String siteAddress, String fieldEngineerName, String fieldEngineerContactNumber,
										   String siteVisitDate, String taskLink,String subject, String serviceCode){
		boolean isSuccess = false;
		
		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(customerName,"Customer Name cannot be null");
			Objects.requireNonNull(orderCode,"Order Id cannot be null");
			Objects.requireNonNull(siteAddress,"Site Address cannot be null");
			Objects.requireNonNull(fieldEngineerName,"Field Engineer Name cannot be null");
			Objects.requireNonNull(fieldEngineerContactNumber,"Field Engineer Contact Number cannot be null");
			Objects.requireNonNull(siteVisitDate,"Site Visit Date cannot be null");
			Objects.requireNonNull(taskLink,"Task Link cannot be null");
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			
			/*List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
					.findByGroup("CIM");
			
			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}*/
			//toAddresses.addAll(ccAddresses);
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("serviceId", serviceCode);
			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
			String cpeSerialNumber="";
			Set<String> cpeSerialNumberSet= new HashSet<>();
			ScServiceDetail scServiceDetail =scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, TaskStatusConstants.INPROGRESS);
			
			if(scServiceDetail!=null){
				if(StringUtils.isNotEmpty(scServiceDetail.getAssignedPM()))
						ccAddresses.add(scServiceDetail.getAssignedPM());
				LOGGER.info("ServiceDetail exists::{}",scServiceDetail.getId());
				List<Integer> scComponentIdList= scComponentRepository.findComponentIdScServiceDetailId(scServiceDetail.getId());
				if(scComponentIdList!=null && !scComponentIdList.isEmpty()){
					LOGGER.info("ScComponentIdList size::{}",scComponentIdList.size());
					List<ScComponentAttribute> scCompAttrList=scComponentAttributesRepository.findByScServiceDetailIdAndScComponent_idInAndAttributeName(scServiceDetail.getId(), scComponentIdList, "cpeSerialNumber");
					if(scCompAttrList!=null && !scCompAttrList.isEmpty()){
						LOGGER.info("ScComponent Attr List size::{}",scCompAttrList.size());
						for(ScComponentAttribute scComponentAttribute:scCompAttrList){
							if(scComponentAttribute.getAttributeValue()!=null && !scComponentAttribute.getAttributeValue().isEmpty()){
								LOGGER.info("ScComponent Attr Value::{}",scComponentAttribute.getAttributeValue());
								cpeSerialNumberSet.add(scComponentAttribute.getAttributeValue());
							}
						}
					}
				}
			}
			if(!cpeSerialNumberSet.isEmpty()){
				LOGGER.info("CpeSerialNumberSet size::{}",cpeSerialNumberSet.size());
				cpeSerialNumber = String.join(", ", cpeSerialNumberSet); 
			}
			map.put("cpeSerialNumber", cpeSerialNumber);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest
					.setTemplateId(notifyCustomerSiteVisitTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyCustomerSiteVisit {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}

	/**
	 * This method is used to notify Field Engineer for Site Survey
	 * 
	 * notifyFieldEngineerSurvey
	 * @param customerMail
	 * @param customerName
	 * @param vendorName
	 * @param siteAddress
	 * @param siteDemarc
	 * @param activityType
	 * @param siteVisitDate
	 * @param siteVisitTimeSlot
	 * @param serviceCode 
	 * @return
	 */

	public boolean notifyFieldEngineer(String toEmail, String customerName, String feName,
										  String siteAddress, String siteDemarc, String activityType, String siteVisitDate,
										  String siteVisitTimeSlot,String subject, String serviceCode) {
		boolean isSuccess = false;
		try {
			
			Objects.requireNonNull(toEmail,"Email cannot be null");
			Objects.requireNonNull(customerName,"Customer Name cannot be null");
			Objects.requireNonNull(feName,"FE Name cannot be null");
			Objects.requireNonNull(siteAddress,"Site Address cannot be null");
			Objects.requireNonNull(activityType,"Activity Type cannot be null");
			Objects.requireNonNull(siteVisitDate,"Site Visit Date cannot be null");
			Objects.requireNonNull(siteVisitTimeSlot,"Site Visit Date cannot be null");
			String url = appHost + "/optimus/tasks/dashboard";	
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(toEmail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("feName", feName);
			map.put("activityType", activityType);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteDemarc", siteDemarc);
			map.put("siteVisitDate", siteVisitDate);
			map.put("siteVisitTimeSlot", siteVisitTimeSlot);
			map.put("subject", subject);
			map.put("portalLink",url);
			map.put("serviceId", serviceCode);
			
			ScServiceDetail scServiceDetail =scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode, TaskStatusConstants.INPROGRESS);
			
			if(scServiceDetail!=null){
				if(StringUtils.isNotEmpty(scServiceDetail.getAssignedPM()))
						ccAddresses.add(scServiceDetail.getAssignedPM());
			}

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest.setTemplateId(notifyFieldEngineerSurveyTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyFieldEngineer {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for Field Engineer Survey ", e);
		}

		return isSuccess;
	}
	

	/**
	 * This method is used to notify Vendor about Test Results
	 * 
	 * @param vendorMail
	 * @param vendorName
	 * @param customerName
	 * @param siteDemarc
	 * @param siteAddress
	 * @param localContactName
	 * @param localContactEmail
	 * @param localContactNumber
	 * @param rfTestResults
	 * @param serviceCode 
	 * @return
	 */
	
	public boolean notifyVendorTestResults(String vendorMail, String vendorName, String customerName, String siteDemarc,
			String siteAddress, String localContactName, String localContactEmail, String localContactNumber, String rfTestResults, String serviceCode) {
		boolean isSuccess = false;
		
		try {
			
			Objects.requireNonNull(vendorMail,"Vendor Mail cannot be null");
			Objects.requireNonNull(vendorName,"Vendor name cannot be null");
			Objects.requireNonNull(localContactName,"Local Contact Name value cannot be null");
			Objects.requireNonNull(customerName,"Customer Name cannot be null");
			Objects.requireNonNull(siteDemarc,"Site Demarcation value cannot be null");
			Objects.requireNonNull(siteAddress,"Site Address cannot be null");
			Objects.requireNonNull(localContactEmail,"Local Contact Email cannot be null");
			Objects.requireNonNull(localContactNumber,"Local Contact Number cannot be null");
			Objects.requireNonNull(rfTestResults,"Rf Test Results cannot be null");
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(vendorMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("vendorName", vendorName);
			map.put("customerName", customerName);
			map.put("localContactName",localContactName);
			map.put("localContactEmail",localContactEmail);
			map.put("localContactNumber",localContactNumber);
			map.put("serviceId", serviceCode);

			map.put("siteDemarc", siteDemarc);
			map.put("siteAddress", siteAddress);
			map.put("rfTestDetails", rfTestResults);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Vendor Test Results Notification");
			mailNotificationRequest
					.setTemplateId(notifyVendorTestResultsTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyVendorTestResults {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify customer about Site Readiness
	 * 
	 * @param customerMail
	 * @param customerName
	 * @param fieldTeam
	 * @param siteAddress
	 * @param siteDemarc
	 * @param activityType
	 * @param siteVisitDate
	 * @param siteVisitTimeSlot
	 * @param portalLink
	 * @return
	 */

	public boolean notifyCustomerSiteReadiness(String customerMail, String customerName, String orderCode,
			   String siteAddress, String serviceCode,String taskLink,String subject) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			//toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
					.findByGroup("CIM");
			
			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}
			toAddresses.addAll(ccAddresses);
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("serviceCode", serviceCode);
			map.put("taskLink", taskLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest
					.setTemplateId(notifyCustomerSiteReadinessTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyCustomerSiteReadiness {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for Field Engineer Survey ", e);
		}

		return isSuccess;
	}

	/**
	 * This method is used to notify customer about Site Readiness
	 * 
	 * @param customerMail
	 * @param customerName
	 * @param fieldTeam
	 * @param siteAddress
	 * @param siteDemarc
	 * @param activityType
	 * @param siteVisitDate
	 * @param siteVisitTimeSlot
	 * @param portalLink
	 * @return
	 */

	public boolean notifyCustomerReadiness(String customerMail, String customerName, String orderCode,
			   String siteAddress, String serviceCode,String taskLink,String subject) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			//toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
					.findByGroup("CIM");
			
			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}
			toAddresses.addAll(ccAddresses);
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("serviceCode", serviceCode);
			map.put("taskLink", taskLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			//TODO: change to site readiness template
			mailNotificationRequest.setTemplateId(notifyCustomerSiteReadinessTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyCustomerReadiness {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	
	
	/**
	 * This method is used to notify about Task Assignment
	 * 
	 * @author diksha garg
	 * notifyTaskAssignment
	 * @param customerMail
	 * @param memberName
	 * @param serviceId
	 * @param taskName
	 * @param expectedClosure
	 * @param taskDetails
	 * @return
	 */
	
	public boolean notifyTaskAssignment(String toMail, String memberName,String serviceCode,String taskName, String expectedClosure,
			String taskDetails,boolean admin,List<String> ccList) {
		boolean isSuccess = false;

		try {
			
			Objects.requireNonNull(toMail,"To Mail cannot be null");
			Objects.requireNonNull(memberName,"Member name cannot be null");
			Objects.requireNonNull(serviceCode,"Service ID cannot be null");
			Objects.requireNonNull(taskName,"Task Name cannot be null");
			Objects.requireNonNull(expectedClosure,"Closure value cannot be null");
			Objects.requireNonNull(taskDetails,"Task Details cannot be null");
			
			String url = appHost + "/optimus/tasks/dashboard";		
			String cDate = null;
			try {
				String sDate1 = expectedClosure;
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
				SimpleDateFormat fmt = new SimpleDateFormat("E, MMM dd yyyy");
				cDate = fmt.format(date1);
			} catch (Exception e) {
				LOGGER.warn("Error in Date Conversion");
			}
			if(StringUtils.isNotBlank(cDate)) {
				expectedClosure=cDate;
			}
			
			memberName = StringUtils.trimToEmpty(memberName).replace("@legomail.com", "");
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(toMail);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.addAll(ccList);
			map.put("memberName", memberName);
			map.put("serviceId", serviceCode);
			map.put("taskName", taskName);
			map.put("expectedClosure", expectedClosure);
			map.put("taskDetails", taskDetails);
			map.put("portalLink",url);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Task Assignment Notification");
			
			if(admin) {
				mailNotificationRequest
				.setTemplateId(notifyTaskAssignmentAdminTemplate);
				
			}
			else {
			mailNotificationRequest
					.setTemplateId(notifyTaskAssignmentTemplate);
			}
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyTaskAssignment {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}

		return isSuccess;

	}

	/**
	 * This method is used to notify about Task Assignment
	 * 
	 * @author Mohamed Danish A
	 * notifyTaskServiceIssueTaskCompletion
	 * @param customerMail
	 * @param memberName
	 * @param serviceId
	 * @param taskName
	 * @param closedDate
	 * @param taskDetails
	 * @return
	 */

	public boolean notifyIpcTaskCompletion(String customerMail, String memberName, String serviceCode,String taskName,
			String closedDate, String taskDetails, String taskStatus) {
		boolean isSuccess = false;

		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(memberName,"Member name cannot be null");
			Objects.requireNonNull(serviceCode,"Service ID cannot be null");
			Objects.requireNonNull(taskName,"Task Name cannot be null");
			Objects.requireNonNull(taskStatus,"Task Status cannot be null");
			Objects.requireNonNull(closedDate,"Task closure date cannot be null");
			Objects.requireNonNull(taskDetails,"Task Details cannot be null");
						
			memberName = StringUtils.trimToEmpty(memberName).replace("@legomail.com", "");
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("memberName", memberName);
			map.put("serviceId", serviceCode);
			map.put("taskName", taskName);
			map.put("taskStatus", taskStatus);
			map.put("taskDetails", taskDetails);
			map.put("closedDate", closedDate);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Task Completion Notification");
			mailNotificationRequest.setTemplateId(notifyIpcTaskCompletionTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for IPC Task Completion.", e);
		}

		return isSuccess;

	}

	
	/**
	 * This method is used to notify about Building Contract
	 * @author diksha garg
	 * 
	 * @param buildingAuthEmail
	 * @param buildingAuthName 
	 * @param serviceId
	 * @param ospEmail
	 * @param formattedDateTime
	 * @param address
	 * @param attachment
	 * @param url
	 */
	public void notifyBuilderContract(String buildingAuthEmail, String buildingAuthName, String serviceId,
			String ospEmail, String formattedDateTime, String address, AttachmentBean attachment, String url) {
		boolean isSuccess = false;

		try {
			
			Objects.requireNonNull(buildingAuthEmail, "Building Authority Email cannot be null");
			// Objects.requireNonNull(sendContractToBA,"Send Contract To BA cannot be
			// null");
			Objects.requireNonNull(buildingAuthName, "Building Authority Name cannot be null");
			Objects.requireNonNull(serviceId, "Service ID cannot be null");
			Objects.requireNonNull(ospEmail, "OSP Email cannot be null");
			Objects.requireNonNull(formattedDateTime, "Date value cannot be null");
			Objects.requireNonNull(address, "Address cannot be null");
			Objects.requireNonNull(attachment, "Attachment cannot be null");

			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(buildingAuthEmail);
			List<String> ccAddresses = new ArrayList<>();
			// map.put("sendContractToBA", sendContractToBA);
			map.put("serviceId", serviceId);
			map.put("buildingAuthName", buildingAuthName);
			map.put("ospEmail", ospEmail);
			map.put("formattedDateTime", formattedDateTime);
			map.put("address", address);
			map.put("attachment", attachment);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(serviceId + " Contract for In-Premise work");
			mailNotificationRequest.setTemplateId(notifyBuilderContractTemplate);
			Map<String, String> cofMap = new HashMap<>();
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				String tempUrl = fileStorageService.getTempDownloadUrl(attachment.getName(), 60000,
						attachment.getStoragePathUrl(), false);
				mailNotificationRequest.setAttachmentName(attachment.getName());
				cofMap.put("TEMP_URL", tempUrl);
				LOGGER.info("CofObject Mapper {}", cofMap);

			} else {
				cofMap.put("FILE_SYSTEM_PATH", attachment.getStoragePathUrl());
			}
			mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setCofObjectMapper(cofMap);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyBuilderContract {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for Builder Contract ", e);
		}

	}
	
	
	
	public void billingFailureNotification(String orderCode,String level) {
		try {
			Objects.requireNonNull(orderCode,"Order code cannot be null");
			
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add("yogeshwaran.magesh@tatacommunications.com;");
			List<String> ccAddresses = new ArrayList<>();
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Billing Failure at "+level+" level for Order "+orderCode);
			mailNotificationRequest
			.setTemplateId(notifyVendorTestResultsTemplate);
			mailNotificationRequest.setIsAttachment(false);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			//mailNotificationRequest.setVariable(map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in notification for Builder Contract ", e);
		}
		
	}


	public void notifyVendorWorkOrder(String vendorEmail, String casdEmail,String vendorName,String woNumber, String scopeOfWork, String expectedCompletionDate,
			String siteAddress, String customerLocalContact, AttachmentBean attachment, String serviceCode) {

		try {
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(vendorEmail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("vendorName", vendorName);
			map.put("woNumber", woNumber);
			map.put("scopeOfWork", scopeOfWork);
			map.put("expectedCompletionDate", expectedCompletionDate);
			map.put("siteAddress", siteAddress);
			map.put("customerLocalContact", customerLocalContact);
			map.put("attachment", attachment);
			map.put("serviceId", serviceCode);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			ccAddresses.add(casdEmail);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Work Order details for Internal Cabling");
			mailNotificationRequest.setTemplateId(notifyBuilderContractTemplate);
			Map<String, String> cofMap = new HashMap<>();
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				String tempUrl = fileStorageService.getTempDownloadUrl(attachment.getName(), 60000,
						attachment.getStoragePathUrl(), false);
				mailNotificationRequest.setAttachmentName(attachment.getName());
				cofMap.put("TEMP_URL", tempUrl);
				LOGGER.info("CofObject Mapper {}", cofMap);

			} else {
				cofMap.put("FILE_SYSTEM_PATH", attachment.getStoragePathUrl());
			}
			mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setCofObjectMapper(cofMap);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyVendorWorkOrder {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));

		} catch (Exception e) {
			LOGGER.error("Error in notification for Work Order details for Internal Cabling", e);
		}

	
	}
	
	/**
	 * This method is used to notify about Po Creation.
	 * 
	 * @author diksha garg
	 * 
	 * @param serviceId
	 * @param poType
	 * @param prNumber
	 * @param poNumber
	 * @param poDate
	 * @param taskLink
	 * @return
	 */

	public boolean notifyPoCreation(String mailId, String serviceId,String poType,String taskLink) {
		boolean isSuccess = false;

		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(mailId);
			List<String> ccAddresses = new ArrayList<>();
			
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceId,"INPROGRESS");
			
			Map<String, String> scComponentAttributesmap=commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("cpeLicencePrNumber", "cpeLicencePoNumber","cpeSupplyLicencePoDate","cpeSupplyHardwarePrNumber","cpeSupplyHardwarePoNumber","cpeSupplyHardwarePoDate"), scServiceDetail.getId(), "LM", "A");
			
			
			map.put("personName", mailId);
			map.put("poType", poType);
			map.put("serviceId", serviceId);
			if (poType.equalsIgnoreCase("license")) {
				map.put("prNumber", scComponentAttributesmap.getOrDefault("cpeLicencePrNumber",""));
				map.put("poNumber", scComponentAttributesmap.getOrDefault("cpeLicencePoNumber", ""));
				map.put("poDate", scComponentAttributesmap.getOrDefault("cpeSupplyLicencePoDate", ""));
			} else {
				map.put("prNumber", scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePrNumber", ""));
				map.put("poNumber", scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePoNumber", ""));
				map.put("poDate", scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePoDate", ""));
			}
			map.put("taskLink", taskLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("PO Creation Notification");
			mailNotificationRequest.setTemplateId(notifyPoCreationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyPoCreation {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("notifyPoCreationException {}", e);
		}

		return isSuccess;

	}
	
	/**
	 * This method is used to notify about Mrn Creation.
	 * 
	 * @author diksha garg
	 * @param emailId
	 * @throws TclCommonException 
	 */
	public void notifyMrnEmail(String emailId, String serviceCode) throws TclCommonException {
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
	
			Map<String, String> scComponentAttributesmap=commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			
	
			MrnNotificationBean mrnNotificationBean = new MrnNotificationBean();
			List<MrnListBean> mrnList = new ArrayList<>();
	
			mrnNotificationBean.setCircuitId(serviceCode);
		//	mrnNotificationBean.setProductName("");
			mrnNotificationBean.setCustomerNameOrProjectName(scServiceDetail.getScOrder().getErfCustLeName());
			String cpetype = scComponentAttributesmap.get("cpeType");
			mrnNotificationBean.setRentalOrSale(cpetype);
	
			List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
					.findByState(scComponentAttributesmap.get("destinationState"));
	
			MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping.stream()
					.findFirst().orElse(null);
	
			mrnNotificationBean.setDistributionCenter(
					mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
	
			mrnNotificationBean.setMrnDate(DateUtil.convertDateToString(new Date()));
			mrnNotificationBean.setMrnNumber(scComponentAttributesmap.getOrDefault("cpeMrnNo", ""));
			mrnNotificationBean.setCpeOptimusId(scComponentAttributesmap.getOrDefault("cpeOptimusId", ""));
			mrnNotificationBean.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
	
			if (cpetype.equalsIgnoreCase("Outright")) {
				LOGGER.info("Cpe Type is outright for serviceCode {}", serviceCode);
				mrnNotificationBean.setDeliveryAddress(scComponentAttributesmap.get("siteAddress"));
				mrnNotificationBean.setDeliveryLocationCity(scComponentAttributesmap.get("destinationCity"));
				mrnNotificationBean.setDeliveryState(scComponentAttributesmap.get("destinationState"));
				mrnNotificationBean.setPincode(scComponentAttributesmap.get("destinationPincode"));
				
				String materialDetails = componentAndAttributeService.getAdditionalAttributes(scServiceDetail,
						"CONFIRMMATERIALAVAIABILITY");
				LOGGER.info("materialDetails for servicecode {} are : {} using CONFIRMMATERIALAVAIABILITY",serviceCode,materialDetails);
	
				ConfirmMaterialAvailabilityBean availabilityBean = Utils.convertJsonToObject(materialDetails,
						ConfirmMaterialAvailabilityBean.class);
	
				int i =1;		
				for(SerialNumberBean displayMaterialResponse : availabilityBean.getSerialNumber()) {
					
					MrnListBean mrnListBean = new MrnListBean();
					mrnListBean.setSn(String.valueOf(i));i++;
					mrnListBean.setCofId(String.valueOf(scServiceDetail.getId()));
					mrnListBean.setMatCode(displayMaterialResponse.getMaterialCode());
					mrnListBean.setDescription(displayMaterialResponse.getMaterialDescription());
					mrnListBean.setEquipmentSnumber(displayMaterialResponse.getSerialNumber());
					mrnListBean.setQuantity(displayMaterialResponse.getQuantity());
					mrnListBean.setTclPoNumber(scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePoNumber", ""));
					mrnListBean.setWbsNumber(scComponentAttributesmap.getOrDefault("level5Wbs", ""));
					mrnList.add(mrnListBean);
				}
				LOGGER.info("MrnList for servicecode {} is : {}",serviceCode,Utils.convertObjectToJson(mrnList));

				
			} else {
				LOGGER.info("Cpe Type is Rental for service Code {}", serviceCode);
				mrnNotificationBean.setDeliveryAddress(scComponentAttributesmap.get("siteAddress"));
				
				mrnNotificationBean.setDeliveryAddress(scComponentAttributesmap.get("siteAddress"));
				mrnNotificationBean.setDeliveryLocationCity(scComponentAttributesmap.get("destinationCity"));
				mrnNotificationBean.setDeliveryState(scComponentAttributesmap.get("destinationState"));
				mrnNotificationBean.setPincode(scComponentAttributesmap.get("destinationPincode"));
				
				String request = componentAndAttributeService.getAdditionalAttributes(scServiceDetail, "SAPINVENTORYCHECKCPE");
				LOGGER.info("request for servicecode {} is : {} using SAPINVENTORYCHECKCPE",serviceCode,request);

				SapQuantityAvailableRequest sapQuantityResponse = Utils.convertJsonToObject(request,
						SapQuantityAvailableRequest.class);

				int i =1;		
				for (DisplayMaterialResponse displayMaterialResponse : sapQuantityResponse.getDisplayMaterial()) { 
					
					MrnListBean mrnListBean = new MrnListBean();
					mrnListBean.setSn(String.valueOf(i));i++;
					mrnListBean.setCofId(String.valueOf(scServiceDetail.getId()));
					mrnListBean.setMatCode(displayMaterialResponse.getMaterialCode());
					mrnListBean.setDescription(displayMaterialResponse.getMaterialDescription());
					mrnListBean.setEquipmentSnumber(displayMaterialResponse.getSAPSerialNumber());
					mrnListBean.setQuantity(displayMaterialResponse.getQuantityAvailable());
					mrnListBean.setTclPoNumber(scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePoNumber", ""));
					mrnListBean.setWbsNumber(scComponentAttributesmap.getOrDefault("level5Wbs", ""));
					mrnList.add(mrnListBean);
				}
				LOGGER.info("MrnList for servicecode {} is : {}",serviceCode,Utils.convertObjectToJson(mrnList));

				
			}
			if (mrnList != null && !mrnList.isEmpty())
				mrnNotificationBean.setMrnList(mrnList);
			mrnNotificationBean.setIssueTo("On Tata Communications Ltd. A/C");
			mrnNotificationBean.setContactName(scComponentAttributesmap.get("localItContactName"));
			mrnNotificationBean.setContactNumber(scComponentAttributesmap.get("localItContactMobile"));
	
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(emailId);
			List<String> ccAddresses = new ArrayList<>();
			
			map.put("mrnDetails", mrnNotificationBean);
	
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
	
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Release MIN");
			mailNotificationRequest.setTemplateId(notifyMrnCreationTemplate);
			//mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyMrnEmail {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyMrnEmailException for service Code {}. Exception occured : {} ", serviceCode, e);
		}

	}

	/**
	 * This method is used to notify customer about acceptance.
	 * 
	 * @author diksha garg
	 * 
	 * @param customerEmail
	 * @param customerName
	 * @param deliveryDate
	 * @param orderCode
	 * @param serviceCode
	 * @param url
	 * @param attachment 
	 * @throws TclCommonException
	 */
	public void notifyCustomerAcceptance(String customerEmail, String customerName, String deliveryDate,
			String orderCode, String serviceCode, String url, String subject, AttachmentBean attachment) throws TclCommonException {
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			//toAddresses.add(customerEmail);
			List<String> ccAddresses = new ArrayList<>();
			
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
					.findByGroup("CIM");
			
			if (!mstTaskRegionList.isEmpty()) {
				toAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}
			String cDate = null;
			try {
				String sDate1 = deliveryDate;
				Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(sDate1);
				SimpleDateFormat fmt = new SimpleDateFormat("E, MMM dd yyyy");
				cDate = fmt.format(date1);
			} catch (Exception e) {
				LOGGER.warn("Error in Date Conversion");
			}
			if(StringUtils.isNotBlank(cDate)) {
				deliveryDate=cDate;
			}
			//toAddresses.addAll(ccAddresses);
			map.put("customerName", customerName);
			map.put("deliveryDate", deliveryDate);
			map.put("serviceId", serviceCode);
			map.put("orderCode", orderCode);
			map.put("taskLink", url);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
	
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest.setTemplateId(notifyCustomerAcceptanceTemplate);
			
			Map<String, String> cofMap = new HashMap<>();
			if (swiftApiEnabled.equalsIgnoreCase("true")) {
				String tempUrl = fileStorageService.getTempDownloadUrl(attachment.getName(), 60000,
						attachment.getStoragePathUrl(), false);
				cofMap.put("TEMP_URL", tempUrl);
				//mailNotificationRequest.setAttachmentName(attachment.getName());
				mailNotificationRequest.setAttachmentName("Service-Handover.pdf");
				LOGGER.info("CofObject Mapper {}", cofMap);
	
			} else {
				cofMap.put("FILE_SYSTEM_PATH", attachment.getStoragePathUrl());
			}
			mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setCofObjectMapper(cofMap);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyCustomerAcceptance {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		}catch(Exception ee) {
			LOGGER.error("notifyCustomerAcceptanceexception {}",ee);
		}

	}
	
	/**
	 * This method is used to notify customer about task.
	 * 
	 * @author diksha garg
	 * 
	 * @param customerMail
	 * @param orderId
	 * @param customerName
	 * @param serviceCode
	 * @param serviceLink
	 * @param taskName
	 * @throws TclCommonException
	 */
	public void notifyCustomerTasks(String customerMail, Integer scOrderId, String customerName, String serviceCode,
			String orderCode, String serviceLink, String taskName) throws TclCommonException {
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			//toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
					.findByGroup("CIM");
			
			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}
			toAddresses.addAll(ccAddresses);
			
			map.put("orderCode", orderCode);
			map.put("customerName", customerName);
			map.put("serviceId", serviceCode);
			map.put("taskLink", serviceLink);
			map.put("taskName", taskName);
			
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
	
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
	
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(taskName);
			mailNotificationRequest.setTemplateId(notifyCustomerTasksTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyCustomerTasks {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			
		}catch(Exception ee) {
			LOGGER.error("notifyCustomerTasksexception {}",ee);
		}
	}

	/**
	 * This method is used to notify customer about welcome letter.
	 * 
	 * @author diksha garg
	 * @param ccAddressesOrig 
	 * @param scComponentAttributesAMap 
	 *
	 */
	public void notifyCustomerWelcome(ScServiceDetail scServiceDetail, List<String> ccAddressesOrig, Map<String, String> scComponentAttributesAMap) throws TclCommonException {
		try {
			LOGGER.info("Sending Mail for Customer Welcome Letter...");
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(scComponentAttributesAMap.get("localItContactEmailId"));
			
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.addAll(ccAddressesOrig);
			LOGGER.info("Recepients of welcome letter for service id{}  -> to {} and cc {}",scServiceDetail.getUuid(),toAddresses,ccAddresses);
			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderType=OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);

			String customerContractingEntity = scServiceDetail.getScOrder().getErfCustLeName();
			String supplierContractingEntity = scServiceDetail.getScOrder().getErfCustSpLeName();
			String productName="";


			if (scServiceDetail.getErfPrdCatalogOfferingName() != null) {
				productName = scServiceDetail.getErfPrdCatalogOfferingName();

			}
			if (scServiceDetail.getAssignedPM() != null && !scServiceDetail.getAssignedPM().isEmpty()) {
				map.put("pmEmailId",scServiceDetail.getAssignedPM());
				ccAddresses.add(scServiceDetail.getAssignedPM());
			}

			if (scServiceDetail.getErfPrdCatalogProductName() != null) {
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("ias"))
					productName = "IAS - Internet Access Service";
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("gvpn"))
					productName = "GVPN - Global Virtual Private Network";
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("npl"))
					productName = "NPL - National Private Line";
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("izopc"))
					productName = "IZOPC - Izo Private Connect";
			}
			
			String customerOrderFormRef = scServiceDetail.getScOrderUuid();
		
			map.put("customerName", scComponentAttributesAMap.get("localItContactName"));
			map.put("serviceId", scServiceDetail.getUuid());
			map.put("orderType", orderType);
			map.put("customerContractingEntity", customerContractingEntity);
			map.put("supplierContractingEntity", supplierContractingEntity);
			map.put("productName", productName);
			map.put("customerOrderFormRef", customerOrderFormRef);
			
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
	
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			if (scServiceDetail.getAssignedPM() != null && !scServiceDetail.getAssignedPM().isEmpty()) {
				map.put("pmEmailId",scServiceDetail.getAssignedPM());
				ccAddresses.add(scServiceDetail.getAssignedPM());
			}
	
			mailNotificationRequest.setBcc(bccAddresses);
			String subject="Welcome Letter";
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest.setTemplateId(notifyCustomerWelcomeTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			
			LOGGER.info("Calling Welcome-Letter Pdf for serviceId {}...", map.get("serviceId"));

			try {
				String fromAdd="no-reply@tatacommunications.com";
				String tempDownloadUrl = pdfService.processWelcomeLetterPdf(scServiceDetail, subject,
						fromAdd, toAddresses, ccAddresses, map.get("customerName").toString(),
						map.get("serviceId").toString(), map.get("orderType").toString(),
						map.get("customerContractingEntity").toString(),
						map.get("supplierContractingEntity").toString(), map.get("productName").toString(),
						map.get("customerOrderFormRef").toString());

				LOGGER.info("Processed Welcome-Letter Pdf for serviceId {} with tempDownloadUrl {}",
						map.get("serviceId"), tempDownloadUrl);
			} catch (Exception e) {
				LOGGER.error("Error in processing Welcome-Letter Pdf for serviceId {} as {}", map.get("serviceId"), e);
			}
			
			LOGGER.info("MDC Filter token value in before Queue call notifyCustomerWelcome {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		}catch(Exception ee) {
			LOGGER.error("notifyCustomerWelcomeexception {}",ee);
		}
	}
	
	public void notifyCustomerWelcomeV2(ScServiceDetail scServiceDetail, List<String> ccAddressesOrig,String pmEmailId,String url,String toAddress) throws TclCommonException {
		try {
			LOGGER.info("Sending Mail for Customer Welcome Letter V2...");
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(toAddress);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.addAll(ccAddressesOrig);
			LOGGER.info("Recepients of welcome letter -> to {} and cc {}",toAddresses,ccAddresses);
			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderType = OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
			
			String customerContractingEntity = scServiceDetail.getScOrder().getErfCustLeName();
			String supplierContractingEntity = scServiceDetail.getScOrder().getErfCustSpLeName();
			String productName="";
			
			
			if (scServiceDetail.getErfPrdCatalogOfferingName() != null) {
				productName = scServiceDetail.getErfPrdCatalogOfferingName();

			}
			
			if (scServiceDetail.getErfPrdCatalogProductName() != null) {
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("ias"))
					productName = "IAS - Internet Access Service";
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("gvpn"))
					productName = "GVPN - Global Virtual Private Network";
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("npl"))
					productName = "NPL - National Private Line";
			}
			
			String customerOrderFormRef = scServiceDetail.getScOrderUuid();
			//map.put("customerName", scComponentAttributesAMap.get("localItContactName"));
			map.put("serviceId", scServiceDetail.getUuid());
			map.put("orderType", orderType);
			map.put("customerContractingEntity", customerContractingEntity);
			map.put("supplierContractingEntity", supplierContractingEntity);
			map.put("productName", productName);
			map.put("customerOrderFormRef", customerOrderFormRef);
			map.put("pmEmailId", pmEmailId);
			map.put("sdbLink", url);
			
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
	
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
				bccAddresses.add(customerSupportEmail);
			}
	
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Your Tata Communications Order - Service Details - "+scServiceDetail.getUuid());
			mailNotificationRequest.setTemplateId(notifyCustomerWelcomeTemplateV2);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyCustomerWelcome {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		}catch(Exception ee) {
			LOGGER.error("notifyCustomerWelcomeexception {}",ee);
		}
	}

	/**
	 * This method is used to notify about Po Creation to multiple (to and cc) addresses
	 * 
	 * @author diksha garg
	 * 
	 * @param serviceId
	 * @param poType
	 * @param prNumber
	 * @param poNumber
	 * @param poDate
	 * @param taskLink
	 * @return
	 */

	public boolean notifyPoCreationMultiple(List<String> toMailIds, List<String> ccMailIds,String serviceId,String poType,String taskLink) {
		boolean isSuccess = false;

		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.addAll(toMailIds);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.addAll(ccMailIds);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceId,"INPROGRESS");
			
			Map<String, String> scComponentAttributesmap=commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("cpeLicencePrNumber", "cpeLicencePoNumber","cpeSupplyLicencePoDate","cpeSupplyHardwarePrNumber","cpeSupplyHardwarePoNumber","cpeSupplyHardwarePoDate"), scServiceDetail.getId(), "LM", "A");
			
			
			map.put("personName", "Team");
			map.put("poType", poType);
			map.put("serviceId", serviceId);
			if (poType.equalsIgnoreCase("license")) {
				map.put("prNumber", scComponentAttributesmap.getOrDefault("cpeLicencePrNumber",""));
				map.put("poNumber", scComponentAttributesmap.getOrDefault("cpeLicencePoNumber", ""));
				map.put("poDate", scComponentAttributesmap.getOrDefault("cpeSupplyLicencePoDate", ""));
			} else {
				map.put("prNumber", scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePrNumber", ""));
				map.put("poNumber", scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePoNumber", ""));
				map.put("poDate", scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePoDate", ""));
			}
			map.put("taskLink", taskLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("PO Creation Notification");
			mailNotificationRequest.setTemplateId(notifyPoCreationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyPoCreationMultiple {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("notifyPoCreationMultipleException {} ", e);
		}

		return isSuccess;

	}
	
	public boolean notifyOffnetPoCreationMultiple(List<String> toMailIds, List<String> ccMailIds,String serviceId,String poType,String taskLink) {
		boolean isSuccess = false;

		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.addAll(toMailIds);
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.addAll(ccMailIds);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceId,"INPROGRESS");
			
			Map<String, String> scComponentAttributesmap=commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("offnetLocalPoNumber","offnetLocalPoDate","offnetLocalPoStatus"), scServiceDetail.getId(), "LM", "A");
			
			Map<String, String> scServiceAttributes = commonFulfillmentUtils.getServiceAttributesAttributes(
					scServiceDetail.getScServiceAttributes().stream().collect(Collectors.toList()));
			
			Map<String, String> orderSubCategoryMap = commonFulfillmentUtils.orderSubcategorySapData();
			
			map.put("personName", "Team");
			map.put("poType", poType);
			map.put("serviceId", serviceId);
			map.put("prNumber", "");
			
			String poNumber= scComponentAttributesmap.getOrDefault("offnetLocalPoNumber", "");
			map.put("poNumber",poNumber);
			map.put("poDate", scComponentAttributesmap.getOrDefault("offnetLocalPoDate", ""));
			
			String supplierName=scServiceAttributes.getOrDefault("closest_provider_bso_name", "");
			map.put("supplierName",supplierName);
			
			String customerName=scServiceDetail.getScOrder().getErfCustCustomerName();
			map.put("customerName",customerName);
			
			String orderId=scServiceDetail.getScOrder().getUuid();
			map.put("orderId",orderId);
			
			String serviceType=orderSubCategoryMap.containsKey(
					scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory() : CommonConstants.NEW)
					? (orderSubCategoryMap.get(scServiceDetail.getOrderSubCategory() != null ? scServiceDetail.getOrderSubCategory()
					: CommonConstants.NEW))
					: CommonConstants.EMPTY;
			map.put("serviceType",serviceType);
			
			map.put("taskLink", taskLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(poNumber+"/"+orderId+"/"+serviceId+"/"+customerName);
			mailNotificationRequest.setTemplateId(notifyOffnetPoCreationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyOffnetPoCreationMultiple {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("notifyPoCreationMultipleException {} ", e);
		}

		return isSuccess;

	}

	public void notifyCustomerCircuitTermination(String customerEmail, String customerName, String orderCode,
			String serviceCode, String deliveryDate, String terminationDate, String parentUuid) {
		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			List<String> ccAddresses = new ArrayList<>();
			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("CIM");

			if (!mstTaskRegionList.isEmpty()) {
				ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
						.map(region -> region.getEmail()).collect(Collectors.toList()));
			}

			if (customerEmail != null) {
				toAddresses.add(customerEmail);
			} else {
				toAddresses.addAll(ccAddresses);
			}
			map.put("serviceId", serviceCode);
			map.put("customerName", customerName);
			map.put("parentServiceId", parentUuid);
			map.put("orderCode", orderCode);
			map.put("terminationDate", terminationDate);
			map.put("customerAcceptanceDate", deliveryDate);
			

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Circuit Termination");
			mailNotificationRequest.setTemplateId(notifyCircuitTerminationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info(
					"MDC Filter token value in before Queue call notifyCustomerCircuitTermination {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));

		} catch (Exception e) {
			LOGGER.error("notifyCustomerCircuitTerminationException {} ", e);
		}}

	public void notifyNetworkTeamForConfiguration(List<String> ccAddresses, List<String> toAddresses,
			String serviceCode, String type, String customerName, String hsuMac, String hsuIp, String netpRefId)
			throws TclCommonException {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("New SERVICE ID:" + serviceCode + " generated");
			mailNotificationRequest.setTemplateId(notifyNetpConfigurationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("serviceId", serviceCode);
			map.put("type", type);
			map.put("customerName", customerName);
			map.put("hsuMac", hsuMac);
			map.put("hsuIp", hsuIp);
			map.put("netpRefId", netpRefId);
			mailNotificationRequest.setVariable(map);
			LOGGER.info(
					"MDC Filter token value in before Queue call notifyNetworkTeamForConfiguration {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyNetworkTeamForConfigurationException {}", e);
		}
	}
	
	
	public void notifyInternationalServiceIdTrigger(List<String> ccAddresses, List<String> toAddresses,String orderCode, String upstreamIds)
			throws TclCommonException {
		LOGGER.info("notifyInternationalServiceIdTrigger method invoked");
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Invalid Service Code generated for Order Code:" + orderCode);
			mailNotificationRequest.setTemplateId(notifySDWANInvalidServiceCodeTrigger);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("orderCode", orderCode);
			map.put("upstreamIds", upstreamIds);
			mailNotificationRequest.setVariable(map);
			LOGGER.info(
					"MDC Filter token value in before Queue call notifyInternationalServiceIdTrigger {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyInternationalServiceIdTrigger {}", e);
		}
	}
	
	public void notifySDWANTrigger(List<String> ccAddresses, List<String> toAddresses,String orderCode)
			throws TclCommonException {
		LOGGER.info("notifySDWANTrigger method invoked");
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Order Code:" + orderCode + " should be retriggered");
			mailNotificationRequest.setTemplateId(notifySDWANTrigger);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("orderCode", orderCode);
			mailNotificationRequest.setVariable(map);
			LOGGER.info(
					"MDC Filter token value in before Queue call notifySDWANTrigger {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifySDWANTrigger {}", e);
		}
	}

	public void notifyOptimusO2CAutoMigrationFailure(Integer serviceId, String serviceUuid, String errorString)
			throws TclCommonException {
		LOGGER.info("notifyOptimusO2CAutoMigrationFailure for  serviceId: {} ", serviceId);
		try {
			HashMap<String, Object> map = new HashMap<>();
			
			String date1 = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			SimpleDateFormat fmt = new SimpleDateFormat("E, MMM dd yyyy");
			String cDate = fmt.format(date1);
			
			map.put("date",cDate);
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add("dimple.subburaj@tatacommunications.com");
			toAddresses.add("Ashish.Kumar4@tatacommunications.com");
			
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add("OPTIMUS-O2C-SUPPORT@tatacommunications.onmicrosoft.com");
			
			LOGGER.info("To Address support : {} ", toAddresses);
			LOGGER.info("CC Address support : {} ", ccAddresses);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("MANUAL/AUTO MIGRATION FOR SERVICE ID:" + serviceUuid + " FAILED");
			mailNotificationRequest.setTemplateId(notifyAutoMigrationFailureTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			if (serviceId != null) {
				map.put("serviceId", serviceId);
			}
			map.put("serviceUuid", serviceUuid);
			map.put("errorString", errorString);
			mailNotificationRequest.setVariable(map);
			LOGGER.info(
					"MDC Filter token value in before Queue call notifyOptimusO2CAutoMigrationFailure {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyOptimusO2CAutoMigrationFailureException {}", e);
		}
	}

	public void notifySdwanInventoryCreationFailure(String solutionCode, String serviceCode, String errorString) throws TclCommonException {
		LOGGER.info("Inside notifySdwanInventoryCreationFailure method for service Code {} under solutionCode {}", serviceCode, solutionCode);
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add("Mayank.Sharma6@tatacommunications.com");
			toAddresses.add("Ashish.Kumar4@tatacommunications.com");

			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add("OPTIMUS-O2C-SUPPORT@tatacommunications.onmicrosoft.com");

			LOGGER.info("To Address support : {} ", toAddresses);
			LOGGER.info("CC Address support : {} ", ccAddresses);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Sdwan Inventory Creation failed for service Code " + serviceCode + " under solution code " +
					solutionCode);
			mailNotificationRequest.setTemplateId(notifySdwanCreationFailureTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("serviceCode", serviceCode);
			map.put("solutionCode", solutionCode);
			map.put("errorString", errorString);
			map.put("date", new Timestamp(new Date().getTime()));
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifySdwanInventoryCreationFailure {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifySdwanInventoryCreationFailure exception occured {}", e.getMessage());
		}
	}

	public void notifySdwanVendorEmail(List<String> toAddresses, String serviceCode, String siteType) throws TclCommonException{
		try {
			LOGGER.info("notifySdwanVendorEmail method invoked:{}", serviceCode);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
			Map<String, String> scComponentAttributesmap=commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", siteType);
			MrnNotificationBean mrnNotificationBean = new MrnNotificationBean();
	
			mrnNotificationBean.setCircuitId(serviceCode);
			mrnNotificationBean.setPartnerPONumber(scComponentAttributesmap.get("cpeSupplyHardwarePoNumber"));
			mrnNotificationBean.setCustomerNameOrProjectName(scServiceDetail.getScOrder().getErfCustLeName());
			mrnNotificationBean.setCpeModel(scComponentAttributesmap.get("EQUIPMENT_MODEL"));
			String cpetype = scComponentAttributesmap.get("cpeType");
			mrnNotificationBean.setRentalOrSale(cpetype);
			if(scComponentAttributesmap.get("cpeBillStartDate")!=null && !scComponentAttributesmap.get("cpeBillStartDate").isEmpty()){
				mrnNotificationBean.setCpeExpectedDeliveryDate(scComponentAttributesmap.get("cpeBillStartDate").replace(" 00:00",""));
			}
			mrnNotificationBean.setDeliveryAddress(scComponentAttributesmap.get("siteAddress"));
			mrnNotificationBean.setDeliveryState(scComponentAttributesmap.get("destinationState"));
			mrnNotificationBean.setPincode(scComponentAttributesmap.get("destinationPincode"));
			mrnNotificationBean.setContactName(scComponentAttributesmap.get("localItContactName"));
			mrnNotificationBean.setContactNumber(scComponentAttributesmap.get("localItContactMobile"));
			HashMap<String, Object> map = new HashMap<>();
			List<String> ccAddresses = new ArrayList<>();
			map.put("cpeExpectedDeliveryDetails", mrnNotificationBean);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("TCL PO No and TCL CKT ID");
			mailNotificationRequest.setTemplateId(notifyCpeVendorTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifySdwanVendorEmail {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifySdwanVendorEmail for service Code {}. Exception occured : {} ", serviceCode, e);
		}

	}

	public void notifySdwanMrnEmail(String emailId, String serviceCode, String siteType) throws TclCommonException{
		try {
			LOGGER.info("notifySdwanMrnEmail {}", emailId);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
	
			Map<String, String> scComponentAttributesmap=commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", siteType);
			
	
			MrnNotificationBean mrnNotificationBean = new MrnNotificationBean();
			List<MrnListBean> mrnList = new ArrayList<>();
	
			mrnNotificationBean.setCircuitId(serviceCode);
			mrnNotificationBean.setCustomerNameOrProjectName(scServiceDetail.getScOrder().getErfCustLeName());
			String cpetype = scComponentAttributesmap.get("cpeType");
			mrnNotificationBean.setRentalOrSale(cpetype);
	
			List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
					.findByState(scComponentAttributesmap.get("destinationState"));
			
			if(distributionCenterMapping!=null && !distributionCenterMapping.isEmpty()){
				MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping.stream()
						.findFirst().orElse(null);
				if(mstStateToDistributionCenterMapping!=null && mstStateToDistributionCenterMapping.getMasterTclDistributionCenter()!=null){
					mrnNotificationBean.setDistributionCenter(
							mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
				}
			}
			mrnNotificationBean.setMrnDate(DateUtil.convertDateToString(new Date()));
			mrnNotificationBean.setMrnNumber(scComponentAttributesmap.getOrDefault("cpeMrnNo", ""));
			mrnNotificationBean.setCpeOptimusId(scComponentAttributesmap.getOrDefault("cpeOptimusId", ""));
			mrnNotificationBean.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
			LOGGER.info("Cpe Type is outright for serviceCode {}", serviceCode);
			mrnNotificationBean.setDeliveryAddress(scComponentAttributesmap.get("siteAddress"));
			mrnNotificationBean.setDeliveryLocationCity(scComponentAttributesmap.get("destinationCity"));
			mrnNotificationBean.setDeliveryState(scComponentAttributesmap.get("destinationState"));
			mrnNotificationBean.setPincode(scComponentAttributesmap.get("destinationPincode"));
			ScComponent scComponent=scComponentRepository.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(scServiceDetail.getId(), "LM", siteType);
			if(scComponent!=null){
				List<CpeCostDetails> cpeCostDetailList=cpeCostDetailsRepository.findByServiceIdAndServiceCodeAndComponentId(scServiceDetail.getId(),serviceCode,scComponent.getId());
				if(cpeCostDetailList!=null && !cpeCostDetailList.isEmpty()){
					LOGGER.info("cpeCostDetailList size::{}",cpeCostDetailList.size());
					int i =1;
					for(CpeCostDetails cpeCostDetails:cpeCostDetailList){
						MrnListBean mrnListBean = new MrnListBean();
						mrnListBean.setSn(String.valueOf(i));i++;
						mrnListBean.setCofId(String.valueOf(cpeCostDetails.getId()));
						mrnListBean.setMatCode(cpeCostDetails.getMaterialCode());
						mrnListBean.setDescription(cpeCostDetails.getDescription());
						mrnListBean.setEquipmentSnumber(cpeCostDetails.getSerialNumber());
						mrnListBean.setQuantity(String.valueOf(cpeCostDetails.getQuantity()));
						mrnListBean.setTclPoNumber(cpeCostDetails.getPoNumber());
						mrnListBean.setWbsNumber(scComponentAttributesmap.getOrDefault("level5Wbs", ""));
						mrnList.add(mrnListBean);
					}
					LOGGER.info("MrnList for servicecode {} is : {}",serviceCode,Utils.convertObjectToJson(mrnList));
				}
			}
			if (mrnList != null && !mrnList.isEmpty())
				mrnNotificationBean.setMrnList(mrnList);
			mrnNotificationBean.setIssueTo("On Tata Communications Ltd. A/C");
			mrnNotificationBean.setContactName(scComponentAttributesmap.get("localItContactName"));
			mrnNotificationBean.setContactNumber(scComponentAttributesmap.get("localItContactMobile"));
	
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(emailId);
			List<String> ccAddresses = new ArrayList<>();
			
			map.put("mrnDetails", mrnNotificationBean);
	
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
	
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Release MIN");
			mailNotificationRequest.setTemplateId(notifyMrnCreationTemplate);
			//mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyMrnEmail {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyMrnEmailException for service Code {}. Exception occured : {} ", serviceCode, e);
		}

	}
	
	public boolean notifyPoCreationMultipleIzosdwan(List<String> toMailIds, List<String> ccMailIds,Integer serviceId,String poType,String taskLink,Integer componentId,String vendorCode,String serviceCode) {
		boolean isSuccess = false;

		try {

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.addAll(toMailIds);
			
//			toAddresses.add("anandhi.vijayaraghavan@tatacommunications.com");
//			toAddresses.add("dimple.subburaj@tatacommunications.com");
//			toAddresses.add("supriya.muchali@tatacommunications.com");
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.addAll(ccMailIds);
			
//			ccAddresses.add("anandhi.vijayaraghavan@tatacommunications.com");
//			ccAddresses.add("dimple.subburaj@tatacommunications.com");
//			ccAddresses.add("supriya.muchali@tatacommunications.com");
			
			ProCreation proCreation = proCreationRepository.findFirstByServiceIdAndServiceCodeAndVendorCodeAndComponentIdAndTypeOrderByIdDesc(serviceId, serviceCode, vendorCode, componentId, poType.toUpperCase());
			
			map.put("personName", "Team");
			map.put("poType", poType);
			map.put("serviceId", serviceCode);
			map.put("prNumber", proCreation.getPrNumber());
			map.put("poNumber", proCreation.getPoNumber());
			map.put("poDate", proCreation.getPoCreatedDate());
				
			//map.put("poDate", scComponentAttributesmap.getOrDefault("cpeSupplyHardwarePoDate", ""));
			
			map.put("taskLink", taskLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			
//			bccAddresses.add("anandhi.vijayaraghavan@tatacommunications.com");
//			bccAddresses.add("dimple.subburaj@tatacommunications.com");
//			bccAddresses.add("supriya.muchali@tatacommunications.com");

			
			  if (notificationBccMail != null) {
			  bccAddresses.addAll(Arrays.asList(notificationBccMail)); }
			 

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("PO Creation Notification");
			mailNotificationRequest.setTemplateId(notifyPoCreationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyPoCreationMultiple {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("notifyPoCreationMultipleException {} ", e);
		}

		return isSuccess;

	}


	public void notifyServiceCancellation(List<String> ccAddresses, List<String> toAddresses,
												  String serviceCode, String orderCode,String cancellationInitiatedBy,String retainExistingNwresource) throws TclCommonException {
		try {
			LOGGER.info("Inside notifyServiceCancellation mail process block");
			Map<String, Object> map = new HashMap<>();
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Status changed to Cancelled for  " + serviceCode);
			mailNotificationRequest.setTemplateId(notifyServiceCancellationTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("serviceCode", serviceCode);
			map.put("orderCode", orderCode);
			map.put("cancellationInitiatedBy", cancellationInitiatedBy);
			map.put("retainExistingNwresource", retainExistingNwresource);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyServiceCancellation {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notifyServiceCancellation {}", e);
		}
	}

	
	public void notifyInternalMailMomDetails( List<String> toAddresses,String internalMomDetails,String subject,String orderCode) throws TclCommonException
	{
		try {
			LOGGER.info("Inside notifyInternalMailMom mail process block");
			InternalMailMomBean[] internalMailMomDetailArray = Utils.convertJsonToObject(internalMomDetails,
					InternalMailMomBean[].class);
			if (internalMailMomDetailArray != null && internalMailMomDetailArray.length > 0) {
				LOGGER.info("internalMomDetails exists:{}", internalMailMomDetailArray.length);
				List<InternalMailMomBean> internalMomDetailsList = Arrays.asList(internalMailMomDetailArray);
				if (internalMomDetailsList != null && !internalMomDetailsList.isEmpty()) {
					LOGGER.info("notifyInternalMailMomDetails internalMomDetailsList is not empty: {}",
							internalMomDetailsList);
					Map<String, Object> map = new HashMap<>();
					map.put("orderCode", orderCode);
					map.put("internalMomDetails", internalMomDetailsList);
					LOGGER.info("To Address mail size :{}, list: {}", toAddresses.size(), toAddresses);
					MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
					mailNotificationRequest.setFrom(fromAddress);
					mailNotificationRequest.setSubject(subject);
					mailNotificationRequest.setTemplateId(lldMailMomTemplate);
					mailNotificationRequest.setTo(toAddresses);
					mailNotificationRequest.setVariable(map);
					LOGGER.info(
							"MDC Filter token value in before Queue call notifyInternalMailMom {} and map value {} :",
							MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
					mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error while triggering notifyInternalMailMomDetails {}", e);
		}
	}
	
	/**
	 * This method is used to notify about Endpoint Mrn Creation.
	 * 
	 * @author Ankit
	 * @param emailId
	 * @throws TclCommonException 
	 */
	
	public void notifyMrnEmailEndpoint(String emailId, String serviceCode) throws TclCommonException {
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(serviceCode,"INPROGRESS");
	
			Map<String, String> scComponentAttributesmap=commonFulfillmentUtils.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			
	
			MrnNotificationBean mrnNotificationBean = new MrnNotificationBean();
			List<MrnListBean> mrnList = new ArrayList<>();
	
			mrnNotificationBean.setCircuitId(serviceCode);
		//	mrnNotificationBean.setProductName("");
			mrnNotificationBean.setCustomerNameOrProjectName(scServiceDetail.getScOrder().getErfCustLeName());
			String endpointType = scComponentAttributesmap.get("endpointType");
			mrnNotificationBean.setRentalOrSale(endpointType);
	
			List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
					.findByState(scComponentAttributesmap.get("destinationState"));
	
			MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping.stream()
					.findFirst().orElse(null);
	
			mrnNotificationBean.setDistributionCenter(
					mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());
	
			mrnNotificationBean.setMrnDate(DateUtil.convertDateToString(new Date()));
			mrnNotificationBean.setMrnNumber(scComponentAttributesmap.getOrDefault("cpeMrnNo", ""));
			mrnNotificationBean.setCpeOptimusId(scComponentAttributesmap.getOrDefault("cpeOptimusId", ""));
			mrnNotificationBean.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());
	
			if (endpointType.equalsIgnoreCase("Outright")) {
				LOGGER.info("Endpoint Type is outright for serviceCode {}", serviceCode);
				mrnNotificationBean.setDeliveryAddress(scComponentAttributesmap.get("siteAddress"));
				mrnNotificationBean.setDeliveryLocationCity(scComponentAttributesmap.get("destinationCity"));
				mrnNotificationBean.setDeliveryState(scComponentAttributesmap.get("destinationState"));
				mrnNotificationBean.setPincode(scComponentAttributesmap.get("destinationPincode"));
				mrnNotificationBean.setCpeInvoiceNumber(scComponentAttributesmap.getOrDefault("cpeInvoiceNumber", ""));
				List<String> componentNameList = new ArrayList<>();
				componentNameList.add("Endpoint");
				List<IScWebexServiceCommercial> scWebexServiceCommercialsList = scWebexServiceCommercialRepository
						.getByScServiceDetailIdAndComponentType(scServiceDetail.getId(), componentNameList);

				Integer i = 1;
				for (IScWebexServiceCommercial scWebexServiceCommercial : scWebexServiceCommercialsList) {

					List<ScComponentAttribute> scComponentAttrs = scComponentAttributesRepository
							.findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									scServiceDetail.getId(), scWebexServiceCommercial.getComponentName(), "A");

					for (ScComponentAttribute scCmptAtr : scComponentAttrs) {
						String serialNumber = null;
						if (scCmptAtr.getAttributeName().equalsIgnoreCase("serialNumber")) {
							serialNumber = scCmptAtr.getAttributeValue();
						}
						MrnListBean mrnListBean = new MrnListBean();
						mrnListBean.setSn(String.valueOf(i));i++;
						mrnListBean.setCofId(String.valueOf(scServiceDetail.getId()));
						mrnListBean.setMatCode(scWebexServiceCommercial.getSaleMaterialCode());
						mrnListBean.setDescription(scWebexServiceCommercial.getComponentDesc());
						mrnListBean.setEquipmentSnumber(serialNumber);
						mrnListBean.setQuantity(scWebexServiceCommercial.getQuantity().toString());
						mrnListBean.setTclPoNumber(scComponentAttributesmap.getOrDefault("webexEndpointPoNumber", " "));
						mrnListBean.setWbsNumber(scComponentAttributesmap.getOrDefault("    ", ""));
						mrnList.add(mrnListBean);
					}
				}
				LOGGER.info("MrnList for servicecode {} is : {}",serviceCode,Utils.convertObjectToJson(mrnList));
			} 
			
			if (mrnList != null && !mrnList.isEmpty())
				mrnNotificationBean.setMrnList(mrnList);
			mrnNotificationBean.setIssueTo("On Tata Communications Ltd. A/C");
			mrnNotificationBean.setContactName(scComponentAttributesmap.get("localItContactName"));
			mrnNotificationBean.setContactNumber(scComponentAttributesmap.get("localItContactMobile"));
	
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(emailId);
			List<String> ccAddresses = new ArrayList<>();
			
			map.put("mrnDetails", mrnNotificationBean);
	
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
	
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Release MIN");
			mailNotificationRequest.setTemplateId(notifyMrnCreationTemplate);
			//mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyMrnEmail {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyMrnEmailException for service Code {}. Exception occured : {} ", serviceCode, e);
		}
	}

	/**
	 * Notify MRN Email Media Gateway
	 *
	 * @param emailId
	 * @param serviceCode
	 * @throws TclCommonException
	 */
	public void notifyMrnEmailMg(String emailId, String serviceCode) throws TclCommonException {
		try {
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(serviceCode, "INPROGRESS");

			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");

			MrnNotificationBean mrnNotificationBean = new MrnNotificationBean();
			List<MrnListBean> mrnList = new ArrayList<>();

			mrnNotificationBean.setCircuitId(serviceCode);
			//	mrnNotificationBean.setProductName("");
			mrnNotificationBean.setCustomerNameOrProjectName(scServiceDetail.getScOrder().getErfCustLeName());
			String cpeType = scComponentAttributesmap.get("cpeType");
			mrnNotificationBean.setRentalOrSale(cpeType);

			List<MstStateToDistributionCenterMapping> distributionCenterMapping = mstStateToDistributionCenterMappingRepository
					.findByState(scComponentAttributesmap.get("destinationState"));

			MstStateToDistributionCenterMapping mstStateToDistributionCenterMapping = distributionCenterMapping.stream()
					.findFirst().orElse(null);

			mrnNotificationBean.setDistributionCenter(
					mstStateToDistributionCenterMapping.getMasterTclDistributionCenter().getDistributionCenterName());

			mrnNotificationBean.setMrnDate(DateUtil.convertDateToString(new Date()));
			mrnNotificationBean.setMrnNumber(scComponentAttributesmap.getOrDefault("cpeMrnNo", ""));
			mrnNotificationBean.setCpeOptimusId(scComponentAttributesmap.getOrDefault("cpeOptimusId", ""));
			mrnNotificationBean.setCustomerName(scServiceDetail.getScOrder().getErfCustLeName());

			if (cpeType.equalsIgnoreCase("Outright")) {
				LOGGER.info("CPE Type is outright for serviceCode {}", serviceCode);
				mrnNotificationBean.setDeliveryAddress(scComponentAttributesmap.get("siteAddress"));
				mrnNotificationBean.setDeliveryLocationCity(scComponentAttributesmap.get("destinationCity"));
				mrnNotificationBean.setDeliveryState(scComponentAttributesmap.get("destinationState"));
				mrnNotificationBean.setPincode(scComponentAttributesmap.get("destinationPincode"));
				mrnNotificationBean.setCpeInvoiceNumber(scComponentAttributesmap.getOrDefault("cpeInvoiceNumber", ""));
				List<String> componentNameList = new ArrayList<>();
				componentNameList.add("Endpoint");
				List<IScTeamsDRServiceCommercial> scTeamsDRServiceCommercials = scTeamsDRServiceCommercialRepository
						.getByScServiceDetailIdAndComponentType(scServiceDetail.getId(), componentNameList);

				Integer i = 1;
				for (IScTeamsDRServiceCommercial scTeamsDRServiceCommercial : scTeamsDRServiceCommercials) {

					List<ScComponentAttribute> scComponentAttrs = scComponentAttributesRepository
							.findByScServiceDetailIdAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									scServiceDetail.getId(), scTeamsDRServiceCommercial.getComponentName(), "A");

					for (ScComponentAttribute scCmptAtr : scComponentAttrs) {
						String serialNumber = null;
						if (scCmptAtr.getAttributeName().equalsIgnoreCase("serialNumber")) {
							serialNumber = scCmptAtr.getAttributeValue();
						}
						MrnListBean mrnListBean = new MrnListBean();
						mrnListBean.setSn(String.valueOf(i));
						i++;
						mrnListBean.setCofId(String.valueOf(scServiceDetail.getId()));
						mrnListBean.setMatCode(scTeamsDRServiceCommercial.getSaleMaterialCode());
						mrnListBean.setDescription(scTeamsDRServiceCommercial.getComponentDesc());
						mrnListBean.setEquipmentSnumber(serialNumber);
						mrnListBean.setQuantity(scTeamsDRServiceCommercial.getQuantity().toString());
						mrnListBean
								.setTclPoNumber(scComponentAttributesmap.getOrDefault("teamsdrEndpointPoNumber", " "));
						mrnListBean.setWbsNumber(scComponentAttributesmap.getOrDefault("    ", ""));
						mrnList.add(mrnListBean);
					}
				}
				LOGGER.info("MrnList for servicecode {} is : {}", serviceCode, Utils.convertObjectToJson(mrnList));
			}

			if (mrnList != null && !mrnList.isEmpty())
				mrnNotificationBean.setMrnList(mrnList);
			mrnNotificationBean.setIssueTo("On Tata Communications Ltd. A/C");
			mrnNotificationBean.setContactName(scComponentAttributesmap.get("localItContactName"));
			mrnNotificationBean.setContactNumber(scComponentAttributesmap.get("localItContactMobile"));

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(emailId);
			List<String> ccAddresses = new ArrayList<>();

			map.put("mrnDetails", mrnNotificationBean);

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Release MIN");
			mailNotificationRequest.setTemplateId(notifyMrnCreationTemplate);
			//mailNotificationRequest.setIsAttachment(true);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyMrnEmail {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyMrnEmailException for service Code {}. Exception occured : {} ", serviceCode, e);
		}
	}
	
	public void notifyServiceStatusChange(List<String> toAddresses,List<String> ccAddresses,String subject,String orderCode,
										  String serviceCode,String reason,String cancellationInitiatedBy ,String retainExistingNwresource,AttachmentBean attachment) throws TclCommonException {

		try {
			LOGGER.info("Inside notifyServiceStatusChange mail process block");
			Map<String, Object> map = new HashMap<>();
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject(subject + serviceCode);
			mailNotificationRequest.setTemplateId(notifyServiceStatusChangeTemplate);
			mailNotificationRequest.setTo(toAddresses);
			if(ccAddresses!=null) {
			mailNotificationRequest.setCc(ccAddresses);
			}
			//mailNotificationRequest.setCc(ccAddresses);
			map.put("orderCode", orderCode);
			map.put("serviceCode", serviceCode);
			map.put("reason", reason);
			map.put("dateOfChange", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			map.put("attachment", attachment);
			if(cancellationInitiatedBy!=null){
				map.put("cancellationInitiatedBy", cancellationInitiatedBy);
			}
			if(retainExistingNwresource!=null){
				map.put("retainExistingNwresource", retainExistingNwresource);
			}
			if(cancellationInitiatedBy!=null && cancellationInitiatedBy.equalsIgnoreCase("Move to M6")){
				map.put("mailContent", "Movement to M6 has been initiated for the following Service ID");
			}
			if(attachment!=null) {
				LOGGER.info("attachment notifyServiceStatusChange received");



				Map<String, String> cofMap = new HashMap<>();
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					String tempUrl = fileStorageService.getTempDownloadUrl(attachment.getName(), 60000,
							attachment.getStoragePathUrl(), false);
					cofMap.put("TEMP_URL", tempUrl);
					mailNotificationRequest.setAttachmentName(attachment.getName());
					//mailNotificationRequest.setAttachmentName("Service-Handover.pdf");
					LOGGER.info("CofObject Mapper {}", cofMap);



				} else {
					cofMap.put("FILE_SYSTEM_PATH", attachment.getStoragePathUrl());
				}
				mailNotificationRequest.setIsAttachment(true);
				mailNotificationRequest.setCofObjectMapper(cofMap);
			}
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyServiceStatusChange {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notifyServiceCancellation {}", e);
		}
	}
	
	
	public void notifyTerminationNotEligible(List<String> toAddresses, List<String> ccAddresses,List<TerminationNotEligibleNoticationBean> terminationNotEligibleNoticationBeans,
			 AttachmentBean attachment, String orderCode, String terminationType) throws TclCommonException {

		try {
			LOGGER.info("Inside notify Termination Not Eligible mail process block");
			Map<String, Object> map = new HashMap<>();
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Termination " + terminationType + " Not Eligible for below circuits in this order " + orderCode);
			mailNotificationRequest.setTemplateId(notifyTerminationNotEligibleTemplate);
			mailNotificationRequest.setTo(toAddresses);
			if (ccAddresses != null) {
				mailNotificationRequest.setCc(ccAddresses);
			}
			//mailNotificationRequest.setCc(ccAddresses);
			map.put("datas", terminationNotEligibleNoticationBeans);
			
			
			if (attachment != null) {
				LOGGER.info("attachment Termination Not Eligible mail received");

				Map<String, String> cofMap = new HashMap<>();
				if (swiftApiEnabled.equalsIgnoreCase("true")) {
					String tempUrl = fileStorageService.getTempDownloadUrl(attachment.getName(), 60000,
							attachment.getStoragePathUrl(), false);
					cofMap.put("TEMP_URL", tempUrl);
					mailNotificationRequest.setAttachmentName(attachment.getName());
					//mailNotificationRequest.setAttachmentName("Service-Handover.pdf");
					LOGGER.info("CofObject Mapper {}", cofMap);

				} else {
					cofMap.put("FILE_SYSTEM_PATH", attachment.getStoragePathUrl());
				}
				mailNotificationRequest.setIsAttachment(true);
				mailNotificationRequest.setCofObjectMapper(cofMap);
			}
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notify Termination Not Eligible {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notify Termination Not Eligible {}", e);
		}
	}
	

	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public void notifyOverDueReminderCustomerOnHold(String customerMail, Integer orderId, String customerName,
			String serviceCode, String serviceLink,String taskName, String orderCode,List<String> toAddresses1,List<String> ccAddresses1, NotificationTrigger notificationTrigger) throws TclCommonException {
		
		try {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setSubject("Service Id On HOLD by SYSTEM due to Customer Tasks Overdue!!!");
		mailNotificationRequest.setTemplateId(customerOverdueHold);
		mailNotificationRequest.setTo(toAddresses1);
		mailNotificationRequest.setCc(ccAddresses1);
		HashMap<String, Object> map = new HashMap<>();
		map.put("customerName", customerName);
		Map<String, List<String>> serviceMapper = new HashMap<>();
		List<String> tasks=new ArrayList<>();
		tasks.add(taskName);
		serviceMapper.put(serviceCode, tasks);
		map.put("serviceList", serviceMapper);
		map.put("serviceLink", serviceLink);
		map.put("orderCode", orderCode);
		mailNotificationRequest.setVariable(map);		
		
		LOGGER.info("MDC Filter token value in before Queue call notifyOverDueReminderCustomerBeforeHold {} and map value {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
		
		notificationTrigger.setStatusTrigger(Utils.convertObjectToJson(mailNotificationRequest));
		notificationTriggerRepository.save(notificationTrigger);
		
		mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		
		} catch (Exception e) {
			LOGGER.error("notifyOverDueReminderCustomerOnHoldException for servicecode {} : {}",serviceCode, e);
			notificationTrigger.setStatusTrigger("ERROR: "+e);
			notificationTriggerRepository.save(notificationTrigger);
		}
	}
	
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public void notifyOverDueReminderCustomerBeforeHold(String customerMail, Integer orderId, String customerName,
			String serviceCode, String serviceLink,String taskName, String orderCode,List<String> toAddresses1,List<String> ccAddresses1, NotificationTrigger notificationTrigger) throws TclCommonException {
		
		try {
			LOGGER.info("Inside notifyOverDueReminderCustomerBeforeHold block");
			Map<String, Object> map = new HashMap<>();

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setTemplateId(customerOverdueBeforeHold);
			mailNotificationRequest.setTo(toAddresses1);
			if (ccAddresses1 != null) {
				mailNotificationRequest.setCc(ccAddresses1);
			}

			mailNotificationRequest
					.setSubject("Customer Tasks overdue - Service Id will be put ON HOLD!!!");

			map.put("customerName", customerName);
			Map<String, List<String>> serviceMapper = new HashMap<>();
			List<String> tasks = new ArrayList<>();
			tasks.add(taskName);
			serviceMapper.put(serviceCode, tasks);
			map.put("serviceList", serviceMapper);
			map.put("serviceLink", serviceLink);
			map.put("orderCode", orderCode);
			mailNotificationRequest.setVariable(map);
			
			
			LOGGER.info("MDC Filter token value in before Queue call notifyOverDueReminderCustomerBeforeHold {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			notificationTrigger.setStatusTrigger(Utils.convertObjectToJson(mailNotificationRequest));
			notificationTriggerRepository.save(notificationTrigger);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));

		} catch (Exception e) {
			LOGGER.error("notifyOverDueReminderCustomerBeforeHoldException for servicecode {} : {}",serviceCode, e);
			notificationTrigger.setStatusTrigger("ERROR: "+e);
			notificationTriggerRepository.save(notificationTrigger);
		}
	}
	
	public void notifyDynamicContent(List<String> toAddresses,List<String> ccAddresses,String subject,String orderCode,
			  String serviceCode, Date crfsDate, String mailContent) {
		try {
		LOGGER.info("Inside notify deferred delivery block {}", serviceCode);
		Map<String, Object> map = new HashMap<>();
		
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setSubject(subject + " " + serviceCode);
		mailNotificationRequest.setTemplateId(notifyDynamicContent);
		mailNotificationRequest.setTo(toAddresses);
		if(ccAddresses!=null) {
			mailNotificationRequest.setCc(ccAddresses);
			}
		map.put("orderCode", orderCode);
		map.put("serviceCode", serviceCode);
		map.put("dateOfChange", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		if(crfsDate != null) {
			map.put("crfsDate", new SimpleDateFormat("dd/MM/yyyy").format(crfsDate));
		}
		map.put("mailContent", mailContent);
		
		mailNotificationRequest.setVariable(map);
		LOGGER.info("MDC Filter token value in before Queue call notify defferd delivery {} and map value {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
		mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notify dynamic content {}", e, serviceCode);
		}
		
	}

	public void notifyDeferredDelivery(List<String> toAddresses,List<String> ccAddresses,String subject,String orderCode,
			  String serviceCode, String reason) {
		try {
		LOGGER.info("Inside notify deferred delivery block");
		Map<String, Object> map = new HashMap<>();

		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		mailNotificationRequest.setSubject(subject + serviceCode);
		mailNotificationRequest.setTemplateId(notifyDeferredDelivery);
		mailNotificationRequest.setTo(toAddresses);
		if(ccAddresses!=null) {
			mailNotificationRequest.setCc(ccAddresses);
			}
		map.put("orderCode", orderCode);
		map.put("serviceCode", serviceCode);
		map.put("dateOfChange", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		map.put("reason", reason);
		mailNotificationRequest.setVariable(map);
		LOGGER.info("MDC Filter token value in before Queue call notify defferd delivery {} and map value {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
		mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notify deffered delivery {}", e, serviceCode);
		}

	}
	public void notifyUnHoldForService(List<String> toAddresses,List<String> ccAddresses,String orderCode,
									   String serviceCode, String reason) {
		try {
			LOGGER.info("Inside notify deferred delivery block");
			Map<String, Object> map = new HashMap<>();

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Status changed to In-progress from On-Hold for" + serviceCode);
			mailNotificationRequest.setTemplateId(notifyUnHoldForService);
			mailNotificationRequest.setTo(toAddresses);
			if(ccAddresses!=null) {
				mailNotificationRequest.setCc(ccAddresses);
			}
			map.put("orderCode", orderCode);
			map.put("serviceCode", serviceCode);
			map.put("reason", reason);

			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notify defferd delivery {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notify deffered delivery {}", e, serviceCode);
		}

	}
	
	public void notifyoffnetRetained(List<String> toAddresses, List<String> ccAddresses, String orderCode,
			String serviceCode) {
		try {
			LOGGER.info("Inside notify offnet Retained block");
			Map<String, Object> map = new HashMap<>();

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Customer Offnet Retained " + serviceCode);
			mailNotificationRequest.setTemplateId(notifyOffnetCustomerRetained);
			mailNotificationRequest.setTo(toAddresses);
			if (ccAddresses != null) {
				mailNotificationRequest.setCc(ccAddresses);
			}
			map.put("orderCode", orderCode);
			map.put("serviceCode", serviceCode);

			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notify Offnet Retained {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notify Offnet Retained {}", e, serviceCode);
		}

	}
	
	public void notifyTrfExtensionDate(List<String> toAddresses, List<String> ccAddresses, String orderCode,
			String serviceCode, String trfExtensionDate) {
		try {
			LOGGER.info("Inside notify TRF Extension block");
			Map<String, Object> map = new HashMap<>();

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("TRF Date Extension " + serviceCode);
			mailNotificationRequest.setTemplateId(notifyTrfDateExtension);
			mailNotificationRequest.setTo(toAddresses);
			if (ccAddresses != null) {
				mailNotificationRequest.setCc(ccAddresses);
			}
			map.put("orderCode", orderCode);
			map.put("serviceCode", serviceCode);
			map.put("dateOfChange", trfExtensionDate);

			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notify TRF Date Extension {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notify TRF Date Extension {}", e, serviceCode);
		}

	}
	
	

	public void notifyReAsignmentServiceForPM(List<String> toAddresses,String orderCode,
											  String serviceCode, String reassignedTo,String assignedFrom,String assignedBy) {
		try {
			LOGGER.info("Inside notifyReAsignmentServiceForPM");
			Map<String, Object> map = new HashMap<>();

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Task transfer for PM");
			mailNotificationRequest.setTemplateId(notifyReAsignmentServiceTemplate);
			mailNotificationRequest.setTo(toAddresses);

			map.put("orderCode", orderCode);
			map.put("serviceCode", serviceCode);
			map.put("reassignedTo", reassignedTo);
			map.put("assignedFrom", assignedFrom);
			map.put("assignedBy", assignedBy);
            LOGGER.info("notifyReAsignmentServiceForPM email template reassignedTo, assignedFrom and assignedBy {} {} {} :",reassignedTo,assignedFrom,assignedBy);
			LOGGER.info("notifyReAsignmentServiceForPM email template reassignedTo, assignedFrom and assignedBy {} {} {} :",reassignedTo,assignedFrom,assignedBy);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyReAsignmentServiceForPM {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notify notifyReAsignmentServiceForPM {}", e, serviceCode);
		}

	}
	public void notifyServiceCancellationInitiation(List<String> toAddresses,List<String> ccAddresses,Map<String, Object> emailAttribute) throws TclCommonException {
		try {
			LOGGER.info("Inside notifyServiceCancellation mail process block");
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Cancelled initiated for  " + emailAttribute.get("serviceCode"));
			mailNotificationRequest.setTemplateId(notifyServiceCancellationInitiation);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(emailAttribute);
			LOGGER.info("MDC Filter token value in before Queue call notifyServiceCancellationInitiation {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), emailAttribute);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error while triggering notifyServiceCancellationInitiation {}", e);
		}
	}
	
	public boolean notifyTermination(Integer serviceId) throws TclCommonException {
		boolean isSuccess = false;
		try {
			LOGGER.info("Inside notifyTermination");
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(serviceId);
			if(scServiceDetail.isPresent() && !scServiceDetail.get().getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.TERMINATION_INPROGRESS)) {
				LOGGER.info("Service code {} and Status {}", scServiceDetail.get().getUuid(), scServiceDetail.get().getMstStatus().getCode());
				return false;
			}
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			List<String> toAddresses = new ArrayList<>();
			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(customerSupportEmail);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setSubject("Termination Reminder!");
			mailNotificationRequest.setTemplateId(notifyTermination);
			
			//set map values
			HashMap<String, Object> map = new HashMap<>();
			map.put("isNpl", "false");
			map.put("todayDate", DateUtil.convertDateToSlashString(new Date()));
			//fetch service details
			if(scServiceDetail.isPresent()) {
				ScServiceDetail detail = scServiceDetail.get();
				map.put("trfsDate", detail.getTerminationEffectiveDate());
				map.put("orderCode", detail.getScOrder().getUuid());
				map.put("custName", detail.getScOrder().getErfCustCustomerName());
				map.put("orderDesc", detail.getScOrder().getErfCustCustomerName()+"_"+detail.getUuid()+"_Termination");
				map.put("tataCommRefId",detail.getUuid());
				map.put("serviceType", detail.getErfPrdCatalogProductName());
				if(detail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL")) {
					map.put("isNpl", "true");
				}
			}
			//fetch component attributes
			Map<String, String> scComponentAttributesmapA=commonFulfillmentUtils.getComponentAttributesDetails(
					Arrays.asList("portBandwidth","destinationCity","localLoopBandwidth","localItContactEmailId","localItContactName"),serviceId, "LM", "A");
			toAddresses.add(scComponentAttributesmapA.getOrDefault("localItContactEmailId",""));
			mailNotificationRequest.setTo(toAddresses);
			map.put("userName", scComponentAttributesmapA.getOrDefault("localItContactName", ""));
			map.put("aEndCity", scComponentAttributesmapA.getOrDefault("destinationCity", "---"));
			if(map.get("isNpl").equals("true")) {
				Map<String, String> scComponentAttributesmapB=commonFulfillmentUtils.getComponentAttributesDetails(
						Arrays.asList("destinationCity"),serviceId, "LM", "B");
				map.put("bEndCity", scComponentAttributesmapB.getOrDefault("destinationCity", "---"));
				map.put("circuitSpeed", scComponentAttributesmapA.getOrDefault("localLoopBandwidth", "NA"));
			}
			else {
				map.put("circuitSpeed", scComponentAttributesmapA.getOrDefault("portBandwidth", "NA"));
			}
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call Notifytermination {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("processTerminationRemianderException ", e);
		}
		return isSuccess;
	}
	
	public void notifyMultiVrfSlaveTriggerFailure(List<String> ccAddresses, List<String> toAddresses,String orderCode, String slaveServiceCode, String masterServiceCode)
			throws TclCommonException {
		LOGGER.info("notifyMultiVrfSlaveTriggerFailure method invoked");
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("Multi VRF Slave Trigger Failed");
			mailNotificationRequest.setTemplateId(notifySlaveTriggerFailure);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("orderCode", orderCode);
			map.put("slaveServiceCode", slaveServiceCode);
			map.put("masterServiceCode", masterServiceCode);
			mailNotificationRequest.setVariable(map);
			LOGGER.info(
					"MDC Filter token value in before Queue call notifyMultiVrfSlaveTriggerFailure {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyMultiVrfSlaveTriggerFailure {}", e);
		}
	}

	public void notifyTeamForNoSSDump(List<String> ccAddresses, List<String> toAddresses, String serviceCode) {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			LOGGER.info("To Address mail size :{} ", toAddresses.size());
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("NO ENTRY IN SSDUMP for " + serviceCode);
			mailNotificationRequest.setTemplateId(notifyTeamForNoSSDump);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("serviceId", serviceCode);
			mailNotificationRequest.setVariable(map);
			LOGGER.info(
					"MDC Filter token value in before Queue call notifyTeamForNoSSDump {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY), map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("notifyTeamForNoSSDumpException {}", e);
		}
	}
}
