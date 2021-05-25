package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.servicefulfillmentutils.beans.MfTaskTrailBean;
import com.tcl.dias.servicefulfillmentutils.beans.TestEmailRequestPayload;
import com.tcl.dias.servicefulfillmentutils.beans.TestEmailResponse;
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
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.entity.repository.MstTaskAssignmentRepository;
import com.tcl.dias.networkaugment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.networkaugment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.networkaugment.entity.repository.NotificationMailJobRepository;
import com.tcl.dias.networkaugment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.networkaugment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.networkaugment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
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
	
	@Value("${notification.template.notifypocreation}")
	String notifyPoCreationTemplate;
	
	@Value("${notification.template.notifymrncreation}")
	String notifyMrnCreationTemplate;
	
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
	 * @param //customerMail
	 * @param vendorName
	 * @param workType
	 * @param customerName
	 * @param siteDemarc
	 * @param siteAddress
	 * @param //siteVisitDateAndTime
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
			String serviceLink, String taskName, String orderCode) {
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
				processOverdueInitialService(customerMail, orderId, customerName, serviceCode, url, taskName,orderCode);
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
											 String taskName,String orderCode) throws TclCommonException {
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
	 * @param //orderId
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
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("serviceId", serviceCode);

			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
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
	 * @param //customerMail
	 * @param customerName
	 * @param //vendorName
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
	 * @param //fieldTeam
	 * @param siteAddress
	 * @param //siteDemarc
	 * @param //activityType
	 * @param //siteVisitDate
	 * @param //siteVisitTimeSlot
	 * @param //portalLink
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
	 * @param //fieldTeam
	 * @param siteAddress
	 * @param //siteDemarc
	 * @param //activityType
	 * @param //siteVisitDate
	 * @param //siteVisitTimeSlot
	 * @param //portalLink
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
	 * @param //customerMail
	 * @param memberName
	 * @param //serviceId
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
	 * @param //serviceId
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
	 * @param //prNumber
	 * @param //poNumber
	 * @param //poDate
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
	 * @param //orderId
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
			LOGGER.info("Recepients of welcome letter -> to {} and cc {}",toAddresses,ccAddresses);
			ScOrder scOrder = scServiceDetail.getScOrder();
			String orderType = scOrder.getOrderType();
			String customerContractingEntity = scServiceDetail.getScOrder().getErfCustLeName();
			String supplierContractingEntity = scServiceDetail.getScOrder().getErfCustSpLeName();
			String productName="";
			if (scServiceDetail.getErfPrdCatalogProductName() != null) {
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("ias"))
					productName = "IAS - Internet Access Service";
				if (scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("gvpn"))
					productName = "GVPN - Global Virtual Private Network";
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
	
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Welcome Letter");
			mailNotificationRequest.setTemplateId(notifyCustomerWelcomeTemplate);
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
			String orderType = scOrder.getOrderType();
			String customerContractingEntity = scServiceDetail.getScOrder().getErfCustLeName();
			String supplierContractingEntity = scServiceDetail.getScOrder().getErfCustSpLeName();
			String productName="";
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
	 * @param //prNumber
	 * @param //poNumber
	 * @param //poDate
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
			
			
			map.put("personName", "Team");
			map.put("poType", poType);
			map.put("serviceId", serviceId);
			map.put("prNumber", "");
			map.put("poNumber", scComponentAttributesmap.getOrDefault("offnetLocalPoNumber", ""));
			map.put("poDate", scComponentAttributesmap.getOrDefault("offnetLocalPoDate", ""));
			
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

	public void notifyOptimusO2CAutoMigrationFailure(Integer serviceId, String serviceUuid, String errorString)
			throws TclCommonException {
		try {
			HashMap<String, Object> map = new HashMap<>();
			map.put("date", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add("");
			List<String> ccAddresses = new ArrayList<>();
			LOGGER.info("To Address support : {} ", toAddresses);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			mailNotificationRequest.setSubject("AUTO MIGRAATION FOR SERVICE ID:" + serviceUuid + " FAILED");
			mailNotificationRequest.setTemplateId(notifyAutoMigrationFailureTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			map.put("serviceId", serviceId);
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

	public void notifyOriginator(ScServiceDetail scServiceDetail, ScOrder scOrder, Set<NwaEorEquipDetails> nwaEorEquipDetails, List<MfTaskTrailBean> trails) {

		//TODO Figure out the template of the email contents.
		// TODO figure out the variable in those templates and how that data will come to this object.
		// TODO figure out the to/cc/bcc and subject of the email.
		//TODO Figure out how to actually send the email. Internal utils that already exists?
		// Mailgun API etc. Raw SMT using Java mail. etc.
		try {
			LOGGER.info("Sending Mail for Order Completion to Originator...");

			NwaEorEquipDetails nwaEorEquipDetails1 = nwaEorEquipDetails.iterator().next();;

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add("manoj.joshi@tatacommunications.com");
			//toAddresses.add(scOrder.getOriginatorName());

			List<String> ccAddresses = new ArrayList<>();
			//ccAddresses.add(scOrder.getPmContactEmail());
			ccAddresses.add("manoj.joshi@tatacommunications.com");
			LOGGER.info("Order Complete Notification -> to {} and cc {}",toAddresses,ccAddresses);

			map.put("ORDERID", scOrder.getOpOrderCode());
			map.put("ORDERTYPE", scOrder.getOrderType());
			map.put("SCENARIOTYPE", scOrder.getScenarioType());
			map.put("ORDERSTATUS", scServiceDetail.getMstStatus().getCode());
			map.put("ORIGINATOR", scOrder.getOriginatorName());
			map.put("ORIGINATORGROUP", scOrder.getOriginatorGroupId());
			map.put("ORDERCREATIONDATE", scOrder.getOrderCreationDate());
			//map.put("COMMITEDDATE", scOrder.get);
			map.put("ORDERCOMPLETIONDATE", scOrder.getOrderEndDate());

			if(nwaEorEquipDetails1 != null){
				map.put("AENDCITY", nwaEorEquipDetails1.getCity());
				map.put("AENDEQUIPNAME", nwaEorEquipDetails1.getEqpmntName());
				/*map.put("AENDMUXCITY", nwaEorEquipDetails1.getaEndMuxName());
				map.put("AENDMUXEQUIPNAME", customerOrderFormRef);
				map.put("ZENDCITY", customerOrderFormRef);
				map.put("ZENDEQUIPNAME", customerOrderFormRef);*/
			}

			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				//bccAddresses.addAll(Arrays.asList(notificationBccMail));
				bccAddresses.add("manoj.joshi@tatacommunications.com");
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Order Complete Notification");
			mailNotificationRequest.setTemplateId("NOTIFYORIGINATOR");
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);

			mailNotificationRequest.setVariable(map);

			/*if(trails != null && !trails.isEmpty()) {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
				List<String> rowList = new ArrayList<>();
				for(MfTaskTrailBean trail: trails) {
					String row = String.format("<tr height=\"20\">\n" +
							"                    <td>%s</td>\n" +
							"                    <td>%s</td>\n" +
							"                    <td>%s</td>\n" +
							"                    <td width=\"50%%\">%s</td>\n" +
							"                </tr>", trail.getUserGroup(), trail.getTaskName(),simpleDateFormat.format(new Date(trail.getCreatedTime().getTime()))  , trail.getComments());

					rowList.add(row);
				}
				String tableRows = String.join("\\n", rowList);
				map.put("COMMENTROWS", tableRows);
			}*/

			LOGGER.info("Order Completion notification to {} and map value {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY),map);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		}catch(Exception ee) {
			LOGGER.error("notifyOriginatorException {}",ee);
		}
	}

	public TestEmailResponse testEmailWithPayload(TestEmailRequestPayload payload) {
		TestEmailResponse response = new TestEmailResponse();
		try {
			LOGGER.info("Sending Mail for Order Completion to Originator...");

			//NwaEorEquipDetails nwaEorEquipDetails1 = nwaEorEquipDetails.iterator().next();;

			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(payload.getOriginatorName());

			List<String> ccAddresses = new ArrayList<>();
			ccAddresses.add(payload.getPmContactEmail());
			LOGGER.info("Order Complete Notification -> to {} and cc {}",toAddresses,ccAddresses);

			map.put("ORDERID", payload.getOpOrderCode());
			map.put("ORDERTYPE", payload.getOrderType());
			map.put("SCENARIOTYPE", payload.getScenarioType());
			map.put("ORIGINATOR", payload.getOriginatorName());
			map.put("ORIGINATORGROUP", payload.getOriginatorGroupId());
			map.put("ORDERCREATIONDATE", payload.getOrderCreationDate());
			//map.put("COMMITEDDATE", scOrder.get);
			map.put("ORDERCOMPLETIONDATE", payload.getOrderEndDate());


			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				//bccAddresses.addAll(Arrays.asList(notificationBccMail));
				bccAddresses.add("manoj.joshi@tatacommunications.com");
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(payload.getSubject());
			mailNotificationRequest.setTemplateId(payload.getTemplateId());//TODO lookup test email templates. or pick from payload.
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);

			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			response.setError(false);
		}catch(Exception ee) {
			LOGGER.error("notifyOriginatorException {}",ee);
			response.setError(true);
			response.setMessage(ee.getMessage());
		}

		return response;
	}

}
