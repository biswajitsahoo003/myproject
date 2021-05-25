package com.tcl.dias.l2oworkflowutils.service.v1;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.beans.CommercialQuoteDetailBean;
import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskAssignment;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskDef;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskRegion;
import com.tcl.dias.l2oworkflow.entity.entities.NotificationMailJob;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskAssignmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.l2oworkflow.entity.repository.NotificationMailJobRepository;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskRepository;
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
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  

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
	
	@Value("${notification.template.vendorArrangeFeMuxInstall}")
	String notifyVendorArrangeFeMuxInstallTemplate;
	
	@Value("${notification.template.vendorArrangeOspIbd}")
	String notifyVendorArrangeOspIbdTemplate;
	
	@Value("${notification.template.vendorArrangeFeSurvey}")
	String notifyVendorArrangeFeSurveyTemplate;
	
	@Value("${notification.template.customerSiteVisit}")
	String notifyCustomerSiteVisitTemplate;
	
	@Value("${notification.template.fieldEngineerOSP}")
	String notifyFieldEngineerOSPTemplate;
	
	@Value("${notification.template.fieldEngineerIBD}")
	String notifyFieldEngineerIBDTemplate;
	
	@Value("${notification.template.fieldEngineerSurvey}")
	String notifyFieldEngineerSurveyTemplate;
	
	@Value("${notification.template.fieldEngineerMuxInstall}")
	String notifyFieldEngineerMuxInstallTemplate;

	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;
	
	@Value("${notification.template.vendorTestResults}")
	String notifyVendorTestResultsTemplate;
	
	@Value("${notification.template.fieldEngineerWirelessSurvey}")
	String notifyFieldEngineerWirelessSurveyTemplate;
	
	@Value("${notification.template.fieldEngineerRFInstall}")
	String notifyFieldEngineerRFInstallTemplate;
	
	@Value("${notification.template.notification.template.customerSiteVisitInternalCabling}")
	String notifyCustomerSiteVisitInternalCablingTemplate;
	
	@Value("${notification.template.customerSiteVisitRfInstall}")
	String notifyCustomerSiteVisitRfInstallTemplate;
	
	@Value("${notification.template.customerSiteVisitSurvey}")
	String notifyCustomerSiteVisitSurveyTemplate;
	
	@Value("${notification.template.customerCpeInstallation}")
	String notifyCustomerCpeInstallationTemplate;
	
	@Value("${notification.template.customerCpeDelivery}")
	String notifyCustomerCpeDeliveryTemplate;
	
	@Value("${notification.template.customerOffnetSiteSurvey}")
	String notifyCustomerOffnetSiteSurveyTemplate;
	
	@Value("${notification.template.fieldEngineerOffnetSiteSurvey}")
	String notifyFieldEngineerOffnetSiteSurveyTemplate;
	
	@Value("${notification.template.fieldEngineerCpeInstallation}")
	String notifyFieldEngineerCpeInstallationTemplate;
	
	@Value("${notification.template.commercial.reminder}")
	String commercialReminderTemplate;
	
	@Value("${notification.template.commercial.escalation}")
	String commercialEscalateTemplate;
	
	@Value("${notification.template.commercial.approved}")
	String commercialApprovedTemplate;
	
	@Value("${notification.template.commercial.group}")
	String commercialGroupAssignmentTemplate;
	
	@Value("${notification.template.commercial.assigne}")
	String commercialAssigneeTemplate;
	
	@Value("${oms.get.quote.commercial}")
	String quoteDetailsQueue;
	
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
	
	@Autowired
	MfTaskDetailRepository mfTaskDetailRepository;
	
	@Value("${swift.api.enabled}")
	String swiftApiEnabled;
	
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
	
	@Autowired
	SiteDetailRepository siteDetailRepository;
	
	@Value("${app.host}")
	String appHost;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;
	
	@Value("${queue.get.mstUserGroups}")
	String getMstUserDetailsQueue;
	
	@Value("${notification.template.manualFeasibility.assignee}")
	String assignTemplate;
	
	@Value("${notification.template.manualFeasibility.close}")
	String  closeTemplate ;
	
	@Value("${notification.template.manualFeasibility.return}")
	String returnTemplate;


	/**
	 * This method is used to remind Overdue to customer 
	 * 
	 * notifyOverDueReminder
	 * 
	 * @param customerMail
	 * @param orderId
	 * @param mailTemplate
	 * @param customerName
	 * @param serviceCode
	 * @param serviceLink
	 * @return
	 */
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public boolean notifyOverDueReminderCommercial(String customerMail, Integer orderId, String customerName, String serviceCode,
			String serviceLink,String optyName,String accountName,String history,String accountManager) {
		boolean isSuccess = false;
		try {
			
			processOverdueInitialServiceCommercial(customerMail, serviceCode, appHost.concat("/optimus/tasks/dashboard/commercial"), optyName, accountName, history,accountManager);
			

		} catch (Exception e) {
			LOGGER.error("Error in sending overdue Mail Template", e);
		}

		return isSuccess;
	}
	
	
	/**
	 * This method is used to remind Overdue to customer 
	 * 
	 * notifyOverDueReminder
	 * 
	 * @param customerMail
	 * @param orderId
	 * @param mailTemplate
	 * @param customerName
	 * @param serviceCode
	 * @param serviceLink
	 * @return
	 */
	@Transactional(readOnly=false,isolation=Isolation.READ_COMMITTED)
	public boolean notifyOverDueEscalation(String customerMail, Integer orderId, String customerName, String serviceCode,
			String serviceLink,String optyName,String accountName,String history,String accountManager) {
		boolean isSuccess = false;
		try {
			List<NotificationMailJob> notificationMailJobs = notificationMailJobRepository
					.findByScOrderIdAndStatus(orderId, "NEW");
			for (NotificationMailJob notificationMailJob : notificationMailJobs) {
				MailNotificationRequest mailNotificationRequest = (MailNotificationRequest) Utils
						.convertJsonToObject(notificationMailJob.getMailRequest(), MailNotificationRequest.class);
				Map<String, Object> dataMapper = mailNotificationRequest.getVariable();
				
				if(serviceCode!=null) {
					@SuppressWarnings("unchecked")
					Map<String, String> serviceMapper = (Map<String, String>) dataMapper.get("serviceList");
					serviceMapper.put(serviceCode, serviceLink);
				}
				notificationMailJob.setMailRequest(Utils.convertObjectToJson(mailNotificationRequest));
				notificationMailJobRepository.save(notificationMailJob);
			}
			if (notificationMailJobs.isEmpty()) {
				processEscalationInitialService(customerMail, serviceCode, appHost.concat("/optimus/tasks/dashboard/commercial"), optyName, accountName, history,accountManager);
			}

		} catch (Exception e) {
			LOGGER.error("Error in sending overdue Mail Template", e);
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
	
	public boolean notifyTaskAssignmentCommercial(List<String> customerMail,String serviceCode,String optyName,String accountName,String accountManager) {
		boolean isSuccess = false;
	
		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(serviceCode,"Service ID cannot be null");
			//Objects.requireNonNull(optyName,"Opportunity Name cannot be null");
			Objects.requireNonNull(accountName,"Account Name value cannot be null");
			Objects.requireNonNull(accountManager,"Account Manager cannot be null");
			LOGGER.info("assignee notification invoked for customerMail={} ", customerMail);
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.addAll(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("quotecode", serviceCode);
			map.put("link", appHost.concat("/optimus/tasks/dashboard/commercial"));
			map.put("optyname",optyName );
			map.put("accountname", accountName);
			map.put("accountmanager", accountManager);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Commercial task created for Quote ".concat(serviceCode));
			mailNotificationRequest
					.setTemplateId(commercialGroupAssignmentTemplate);
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
	 * This method is used to notify about Task Assignment to individual
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
	
	public boolean notifyTaskAssignmentCommercialToAssigee(List<String> customerMail,String serviceCode,String optyName,String accountName,String accountManager) {
		boolean isSuccess = false;
		
		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(accountName,"Account name cannot be null");
			Objects.requireNonNull(serviceCode,"Service ID cannot be null");
			//Objects.requireNonNull(optyName,"Opportunity Name cannot be null");
			Objects.requireNonNull(accountManager,"Account Manager cannot be null");
			LOGGER.info("assignee notification invoked for customerMail={} ", customerMail);
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.addAll(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("quotecode", serviceCode);
			map.put("link", appHost.concat("/optimus/tasks/dashboard/commercial"));
			map.put("optyname",optyName );
			map.put("accountname", accountName);
			map.put("accountmanager", accountManager);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Commercial task for Quote ".concat(serviceCode).concat(" has been assigned to you"));
			mailNotificationRequest
					.setTemplateId(commercialAssigneeTemplate);
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
	
	public boolean notifyVendorArrangeOspIbd(String vendorMail, String vendorName,String workType,String customerName, String siteDemarc,
			String siteAddress,String siteVisitDate,String siteVisitTimeSlot,String checkWorkDetails) {
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
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Arrange Field Engineer for OSP IBD Notification");
			mailNotificationRequest
					.setTemplateId(notifyVendorArrangeOspIbdTemplate);
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

	private void processOverdueInitialServiceCommercial(String customerMail,String serviceCode,String link, String optyName,String accountName,String history,String accountManager) throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		List<String> bccAddresses = new ArrayList<>();
		if (notificationBccMail != null) {
			bccAddresses.addAll(Arrays.asList(notificationBccMail));
		}
		mailNotificationRequest.setBcc(bccAddresses);
		mailNotificationRequest.setSubject("Quote ".concat(serviceCode).concat(" is awaiting for your approval"));
		mailNotificationRequest.setTemplateId(commercialReminderTemplate);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(customerMail);
		List<String> ccAddresses = new ArrayList<>();
		ccAddresses.add(customerSupportEmail);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setCc(ccAddresses);
		HashMap<String, Object> map = new HashMap<>();
		map.put("quotecode", serviceCode);
		map.put("link", link);
		map.put("optyname",optyName );
		map.put("accountname", accountName);
		map.put("history", history);
		map.put("accountmanager", accountManager);
		mailNotificationRequest.setVariable(map);
		LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		
	}
	
	private void processOverdueInitialService(String customerMail, Integer orderId, String customerName,
			String serviceCode, String serviceLink) throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		List<String> bccAddresses = new ArrayList<>();
		if (notificationBccMail != null) {
			bccAddresses.addAll(Arrays.asList(notificationBccMail));
		}
		mailNotificationRequest.setBcc(bccAddresses);
		mailNotificationRequest.setSubject("Overdue Reminder!!!");
		mailNotificationRequest.setTemplateId(overdueReminder);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(customerMail);
		List<String> ccAddresses = new ArrayList<>();
		ccAddresses.add(customerSupportEmail);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setCc(ccAddresses);
		HashMap<String, Object> map = new HashMap<>();
		map.put("customerName", customerName);
		
		if(serviceCode!=null) {
			Map<String, String> serviceMapper = new HashMap<>();
			serviceMapper.put(serviceCode, serviceLink);
			map.put("serviceList", serviceMapper);
			mailNotificationRequest.setVariable(map);
		}

		NotificationMailJob notifcationMailJob = new NotificationMailJob();
		notifcationMailJob.setCreatedBy(Utils.getSource());
		notifcationMailJob.setCreatedTime(new Date());
		notifcationMailJob.setMailRequest(Utils.convertObjectToJson(mailNotificationRequest));
		notifcationMailJob.setQueueName(notificationMailQueue);
		notifcationMailJob.setScOrderId(orderId);
		notifcationMailJob.setStatus("NEW");
		notificationMailJobRepository.save(notifcationMailJob);
	}
	
	private void processEscalationInitialService(String customerMail,String serviceCode,String link, String optyName,String accountName,String history,String accountManager) throws TclCommonException {
		MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
		mailNotificationRequest.setFrom(fromAddress);
		List<String> bccAddresses = new ArrayList<>();
		if (notificationBccMail != null) {
			bccAddresses.addAll(Arrays.asList(notificationBccMail));
		}
		mailNotificationRequest.setBcc(bccAddresses);
		mailNotificationRequest.setSubject("Quote ".concat(serviceCode).concat(" is auto escalated as the due date has passed"));
		mailNotificationRequest.setTemplateId(commercialEscalateTemplate);
		List<String> toAddresses = new ArrayList<>();
		toAddresses.add(customerMail);
		List<String> ccAddresses = new ArrayList<>();
		ccAddresses.add(customerSupportEmail);
		mailNotificationRequest.setTo(toAddresses);
		mailNotificationRequest.setCc(ccAddresses);
		HashMap<String, Object> map = new HashMap<>();
		map.put("quotecode", serviceCode);
		map.put("link", link);
		map.put("optyname",optyName );
		map.put("accountname", accountName);
		map.put("history", history);
		map.put("accountmanager", accountManager);
		mailNotificationRequest.setVariable(map);
		LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
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
	 * @return
	 */

	public boolean notifyCustomerSiteVisit(String customerMail, String customerName, String orderCode,
										   String siteAddress, String fieldEngineerName, String fieldEngineerContactNumber,
										   String siteVisitDate, String taskLink,String subject){
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
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
	 * @return
	 */

	public boolean notifyFieldEngineerSurvey(String customerMail, String customerName, String vendorName,
										  String siteAddress, String siteDemarc, String activityType, String siteVisitDate,
										  String siteVisitTimeSlot, String portalLink) {
		boolean isSuccess = false;
		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(customerName,"Customer Name cannot be null");
			Objects.requireNonNull(vendorName,"Vendor Name cannot be null");
			Objects.requireNonNull(siteAddress,"Site Address cannot be null");
			Objects.requireNonNull(activityType,"Activity Type cannot be null");
			Objects.requireNonNull(siteVisitDate,"Site Visit Date cannot be null");
			Objects.requireNonNull(siteVisitTimeSlot,"Site Visit Date cannot be null");
			Objects.requireNonNull(portalLink,"Portal Link cannot be null");
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("vendorName", vendorName);
			map.put("activityType", activityType);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteDemarc", siteDemarc);
			map.put("siteVisitDate", siteVisitDate);
			map.put("siteVisitTimeSlot", siteVisitTimeSlot);
			map.put("portalLink",portalLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();

			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Field Engineer Survey");
			mailNotificationRequest
					.setTemplateId(notifyFieldEngineerSurveyTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
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
	 * @return
	 */
	
	public boolean notifyVendorTestResults(String vendorMail, String vendorName, String customerName, String siteDemarc,
			String siteAddress, String localContactName, String localContactEmail, String localContactNumber, String rfTestResults) {
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
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for Field Engineer Survey ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Field Engineer for Mux Installation
	 * 
	 * notifyFieldEngineerMuxInstall
	 * @param customerMail
	 * @param customerName
	 * @param fieldTeam
	 * @param siteAddress
	 * @param siteDemarc
	 * @param activityType
	 * @param siteVisitDate
	 * @param siteVisitTimeSlot
	 * @return
	 */

	public boolean notifyFieldEngineerMuxInstall(String customerMail, String customerName, String fieldTeam,
										  String siteAddress, String siteDemarc, String activityType, String siteVisitDate,
										  String siteVisitTimeSlot, String portalLink) {
		boolean isSuccess = false;
		/*Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
		Objects.requireNonNull(customerName,"Customer Name cannot be null");
		Objects.requireNonNull(fieldTeam,"Field Team cannot be null");
		Objects.requireNonNull(siteAddress,"Site Address cannot be null");
		Objects.requireNonNull(siteDemarc,"Site Demarcation Value cannot be null");
		Objects.requireNonNull(activityType,"Activity Type cannot be null");
		Objects.requireNonNull(siteVisitDate,"Site Visit Date cannot be null");
		Objects.requireNonNull(siteVisitTimeSlot,"Site Visit Date cannot be null");
		Objects.requireNonNull(portalLink,"Portal Link cannot be null");*/
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("fieldTeam", fieldTeam);
			map.put("activityType", activityType);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteDemarc", siteDemarc);
			map.put("siteVisitDate", siteVisitDate);
			map.put("siteVisitTimeSlot", siteVisitTimeSlot);
			map.put("portalLink",portalLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Field Engineer Mux Install");
			mailNotificationRequest
					.setTemplateId(notifyFieldEngineerMuxInstallTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for Mux Install ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Field Engineer about RF Installation
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

	public boolean notifyFieldEngineerRFInstall(String customerMail, String customerName, String fieldTeam,
										  String siteAddress, String siteDemarc, String activityType, String siteVisitDate,
										  String siteVisitTimeSlot, String portalLink) {
		boolean isSuccess = false;
		/*Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
		Objects.requireNonNull(customerName,"Customer Name cannot be null");
		Objects.requireNonNull(fieldTeam,"Field Team cannot be null");
		Objects.requireNonNull(siteAddress,"Site Address cannot be null");
		Objects.requireNonNull(siteDemarc,"Site Demarcation Value cannot be null");
		Objects.requireNonNull(activityType,"Activity Type cannot be null");
		Objects.requireNonNull(siteVisitDate,"Site Visit Date cannot be null");
		Objects.requireNonNull(siteVisitTimeSlot,"Site Visit Date cannot be null");
		Objects.requireNonNull(portalLink,"Portal Link cannot be null");*/
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("fieldTeam", fieldTeam);
			map.put("activityType", activityType);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteDemarc", siteDemarc);
			map.put("siteVisitDate", siteVisitDate);
			map.put("siteVisitTimeSlot", siteVisitTimeSlot);
			map.put("portalLink",portalLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Field Engineer RF Install");
			mailNotificationRequest
					.setTemplateId(notifyFieldEngineerRFInstallTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for Mux Install ", e);
		}

		return isSuccess;
	}

	/**
	 * This method is used to notify Customer about RF Installation
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

	public boolean notifyCustomerRFInstall(String customerMail, String customerName, String orderCode,
			   String siteAddress, String fieldEngineerName, String fieldEngineerContactNumber,
			   String siteVisitDate, String taskLink,String subject) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
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
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Customer about LM Testing
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
	
	public boolean notifyCustomerLMTesting(String customerMail, String customerName, String orderCode,
			   String siteAddress, String fieldEngineerName, String fieldEngineerContactNumber,
			   String siteVisitDate, String taskLink,String subject) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Customer about Internal Cabling
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

	public boolean notifyCustomerInternalCabling(String customerMail, String customerName, String orderCode,
			   String siteAddress, String fieldEngineerName, String fieldEngineerContactNumber,
			   String siteVisitDate, String taskLink,String subject) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	

	/**
	 * This method is used to notify Customer about Wireless Site Survey
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
	
	public boolean notifyCustomerWirelessSiteSurvey(String customerMail, String customerName, String orderCode,
			   String siteAddress, String fieldEngineerName, String fieldEngineerContactNumber,
			   String siteVisitDate, String taskLink,String subject) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderCode);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}

	/**
	 * This method is used to notify Field Engineer about Wireless Site Survey
	 * 
	 * @param customerMail
	 * @param customerName
	 * @param vendorName
	 * @param siteAddress
	 * @param siteDemarc
	 * @param activityType
	 * @param siteVisitDate
	 * @param siteVisitTimeSlot
	 * @param portalLink
	 * @return
	 */
	
	public boolean notifyFieldEngineerWirelessSurvey(String customerMail, String customerName, String fieldTeam,
			  String siteAddress, String siteDemarc, String activityType, String siteVisitDate,
			  String siteVisitTimeSlot, String portalLink) {
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("fieldTeam", fieldTeam);
			map.put("activityType", activityType);
			map.put("customerName", customerName);
			map.put("siteAddress", siteAddress);
			map.put("siteDemarc", siteDemarc);
			map.put("siteVisitDate", siteVisitDate);
			map.put("siteVisitTimeSlot", siteVisitTimeSlot);
			map.put("portalLink",portalLink);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
		
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
		
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Field Engineer Wireless Site Survey");
			mailNotificationRequest
			.setTemplateId(notifyFieldEngineerWirelessSurveyTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
			MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;
			
			} catch (Exception e) {
				LOGGER.error("Error in notification for Wireless Site Survey ", e);
			}
		
			return isSuccess;
	}

	/**
	 * This method is used to notify Customer about Site Visit - Internal Cabling
	 *  
	 * @author diksha garg
	 * @param customerMail
	 * @param fieldEngineerName
	 * @param fieldEngineerContactNumber
	 * @param orderId
	 * @param siteAddress
	 * @param visitReason
	 * @param siteVisitDate
	 * @param taskLink
	 * @param subject
	 * @return
	 */

	public boolean notifyCustomerSiteVisitInternalCabling(String customerMail, String fieldEngineerName, String fieldEngineerContactNumber,
										   String orderId, String siteAddress, String visitReason,
										   String siteVisitDate, String taskLink,String subject){
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderId);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
			map.put("visitReason", visitReason);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest
					.setTemplateId(notifyCustomerSiteVisitInternalCablingTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Customer about Site Visit - Rf Install
	 *  
	 * @author diksha garg
	 * @param customerMail
	 * @param fieldEngineerName
	 * @param fieldEngineerContactNumber
	 * @param orderId
	 * @param siteAddress
	 * @param visitReason
	 * @param siteVisitDate
	 * @param taskLink
	 * @param subject
	 * @return
	 */

	public boolean notifyCustomerSiteVisitRfInstall(String customerMail, String fieldEngineerName, String fieldEngineerContactNumber,
										   String orderId, String siteAddress, String visitReason,
										   String siteVisitDate, String taskLink,String subject){
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderId);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
			map.put("visitReason", visitReason);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest
					.setTemplateId(notifyCustomerSiteVisitRfInstallTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Customer about Site Visit - Site Survey
	 *  
	 * @author diksha garg
	 * @param customerMail
	 * @param fieldEngineerName
	 * @param fieldEngineerContactNumber
	 * @param orderId
	 * @param siteAddress
	 * @param visitReason
	 * @param siteVisitDate
	 * @param taskLink
	 * @param subject
	 * @return
	 */

	public boolean notifyCustomerSiteVisitSiteSurvey(String customerMail, String fieldEngineerName, String fieldEngineerContactNumber,
										   String orderId, String siteAddress, String visitReason,
										   String siteVisitDate, String taskLink,String subject){
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderId);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
			map.put("visitReason", visitReason);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest
					.setTemplateId(notifyCustomerSiteVisitSurveyTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Customer about Cpe Installation
	 *  
	 * @author diksha garg
	 * @param customerMail
	 * @param fieldEngineerName
	 * @param fieldEngineerContactNumber
	 * @param orderId
	 * @param siteAddress
	 * @param visitReason
	 * @param siteVisitDate
	 * @param taskLink
	 * @param subject
	 * @return
	 */
	public boolean notifyCustomerCpeInstallation(String customerMail, String fieldEngineerName, String fieldEngineerContactNumber,
										   String orderId, String siteAddress, String visitReason,
										   String siteVisitDate, String taskLink,String subject){
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderId);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
			map.put("visitReason", visitReason);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest
					.setTemplateId(notifyCustomerSiteVisitSurveyTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Customer about Cpe Delivery
	 *  
	 * @author diksha garg
	 * @param customerMail
	 * @param fieldEngineerName
	 * @param fieldEngineerContactNumber
	 * @param orderId
	 * @param siteAddress
	 * @param visitReason
	 * @param siteVisitDate
	 * @param taskLink
	 * @param subject
	 * @return
	 */
	public boolean notifyCustomerCpeDelivery(String customerMail, String fieldEngineerName, String fieldEngineerContactNumber,
										   String orderId, String siteAddress, String visitReason,
										   String siteVisitDate, String taskLink,String subject){
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderId);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
			map.put("visitReason", visitReason);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest
					.setTemplateId(notifyCustomerSiteVisitSurveyTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	/**
	 * This method is used to notify Customer about Offnet Site Survey
	 *  
	 * @author diksha garg
	 * @param customerMail
	 * @param fieldEngineerName
	 * @param fieldEngineerContactNumber
	 * @param orderId
	 * @param siteAddress
	 * @param visitReason
	 * @param siteVisitDate
	 * @param taskLink
	 * @param subject
	 * @return
	 */
	public boolean notifyCustomerOffnetSiteSurvey(String customerMail, String fieldEngineerName, String fieldEngineerContactNumber,
										   String orderId, String siteAddress, String visitReason,
										   String siteVisitDate, String taskLink,String subject){
		boolean isSuccess = false;
		try {
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("orderId", orderId);
			map.put("siteAddress", siteAddress);
			map.put("siteVisitDate", siteVisitDate);
			map.put("fieldEngineerName", fieldEngineerName);
			map.put("taskLink", taskLink);
			map.put("fieldEngineerContactNumber", fieldEngineerContactNumber);
			map.put("visitReason", visitReason);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}
			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject(subject);
			mailNotificationRequest.setTemplateId(notifyCustomerSiteReadinessTemplate);
			mailNotificationRequest.setTo(toAddresses);
			mailNotificationRequest.setCc(ccAddresses);
			mailNotificationRequest.setVariable(map);
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			isSuccess = true;

		} catch (Exception e) {
			LOGGER.error("Error in notification for customer site visit ", e);
		}

		return isSuccess;
	}
	
	public void commercialDiscountReminder(String notificationKey, String taskKey, Map<String, Object> processMap,String url) throws TclCommonException, IllegalArgumentException {
		try {
			Integer siteDetailId = (Integer) processMap.get("siteDetailId");
			String quoteCode =(String)processMap.get("quoteCode");
			
			//Optional<Task>  taskOptional = taskRepository.findFirstBySiteDetail_idAndMstTaskDef_keyOrderByCreatedTimeDesc(siteDetailId, taskKey);
			Optional<Task>  taskOptional = taskRepository.findFirstBySiteDetail_idAndMstTaskDef_keyAndMstStatus_idOrderByCreatedTimeDesc(siteDetailId, taskKey,4);
			String assignedUser="";
			String history = "";
			CommercialQuoteDetailBean commercialQuoteDetailBean = new CommercialQuoteDetailBean();
			
			Optional<SiteDetail> siteDetail = siteDetailRepository.findById(siteDetailId);
			if(siteDetail.isPresent()) {
				commercialQuoteDetailBean.setAccountName(siteDetail.get().getAccountName());
				commercialQuoteDetailBean.setEmail(siteDetail.get().getQuoteCreatedBy());
				commercialQuoteDetailBean.setOptyId(siteDetail.get().getOpportunityId());
				Map<String, String> approveDetails = taskService.getDiscountDelegationDetails(siteDetail.get());
				if (approveDetails != null) {
					if (approveDetails.containsKey("commercial-discount-1")
							&& approveDetails.get("commercial-discount-1") != null) {
						history = history.concat("Approver 1 :".concat(approveDetails.get("commercial-discount-1")));
					}
					if (approveDetails.containsKey("commercial-discount-2")
							&& approveDetails.get("commercial-discount-2") != null) {
						history = history.concat(" Approver 2 :".concat(approveDetails.get("commercial-discount-2")));
					}
					if (approveDetails.containsKey("commercial-discount-3")
							&& approveDetails.get("commercial-discount-3") != null) {
						history = history.concat(" Approver 3 :".concat(approveDetails.get("commercial-discount-3")));
					}
				}
			}
			
	
			if(taskOptional.isPresent()) {
				Set<TaskAssignment> taskAssignments = taskOptional.get().getTaskAssignments();
				if(!taskAssignments.isEmpty()) {
					for(TaskAssignment taskAssignment : taskAssignments){
						assignedUser = taskAssignment.getUserName();
					LOGGER.info("reminder invoked for notificationKey={} email={} quoteCode={}",notificationKey,assignedUser,quoteCode);
						
						notifyOverDueReminderCommercial(assignedUser, siteDetailId, assignedUser, quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
					}
				}else {
				LOGGER.info("reminder invoked for notificationKey={} email={} quoteCode={}",notificationKey,assignedUser,quoteCode);
					notifyOverDueReminderCommercial(assignedUser, siteDetailId, assignedUser, quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
				}
			
			
			Optional<MstTaskAssignment> mstTaskAssignmentOptional = MstTaskAssignmentRepository.findFirstByAssignedUserAndMstTaskDef_key(assignedUser,taskKey);
			if(mstTaskAssignmentOptional.isPresent()) {			
				
				MstTaskAssignment mstTaskAssignment = mstTaskAssignmentOptional.get();
				String nextTaskKey= mstTaskAssignment.getMstTaskDef().getKey();
				String nextAssignedUser="";
				LOGGER.info("NextAssignedUser={}, BackupUser1={}, BackupUser2={}",mstTaskAssignment.getNextAssignedUser(),mstTaskAssignment.getBackupUser1(),mstTaskAssignment.getBackupUser2());
				if(StringUtils.isNotBlank(mstTaskAssignment.getNextAssignedUser())) {
					nextAssignedUser = mstTaskAssignment.getNextAssignedUser();
				LOGGER.info("escalation level1  invoked for notificationKey={} email={} quoteCode={}",notificationKey,nextAssignedUser,quoteCode);
				notifyOverDueEscalation(nextAssignedUser, siteDetailId, nextAssignedUser, quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
				}			
				if(StringUtils.isNotBlank(mstTaskAssignment.getBackupUser1())) {
				LOGGER.info("escalation level1  invoked for notificationKey={} email={} quoteCode={}",notificationKey,mstTaskAssignment.getBackupUser1(),quoteCode);
				notifyOverDueEscalation(mstTaskAssignment.getBackupUser1(), siteDetailId, mstTaskAssignment.getBackupUser1(), quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
				}
				if(StringUtils.isNotBlank(mstTaskAssignment.getBackupUser2())) {
				LOGGER.info("escalation level1 invoked for notificationKey={} email={} quoteCode={}",notificationKey,mstTaskAssignment.getBackupUser2(),quoteCode);
				notifyOverDueEscalation(mstTaskAssignment.getBackupUser2(), siteDetailId, mstTaskAssignment.getBackupUser2(), quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
				}
				
				if(notificationKey.startsWith("escalation")) {
					Optional<MstTaskAssignment> mstTaskAssignmentOptional2 = MstTaskAssignmentRepository.findFirstByAssignedUserAndMstTaskDef_key(nextAssignedUser,nextTaskKey);
					MstTaskAssignment mstTaskAssignment2 = mstTaskAssignmentOptional2.get();
					LOGGER.info("escalation NextAssignedUser={}, BackupUser1={}, BackupUser2={}",mstTaskAssignment2.getNextAssignedUser(),mstTaskAssignment2.getBackupUser1(),mstTaskAssignment2.getBackupUser2());
					if(StringUtils.isNotBlank(mstTaskAssignment.getNextAssignedUser())) {
					LOGGER.info("escalation level2 invoked for notificationKey={} email={} quoteCode={}",notificationKey,mstTaskAssignment.getNextAssignedUser(),quoteCode);
						notifyOverDueEscalation(mstTaskAssignment.getNextAssignedUser(), siteDetailId, mstTaskAssignment.getNextAssignedUser(), quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
					}
					
					if(StringUtils.isNotBlank(mstTaskAssignment.getBackupUser1())) {
					LOGGER.info("escalation level2 invoked for notificationKey={} email={} quoteCode={}",notificationKey,mstTaskAssignment.getBackupUser1(),quoteCode);
						notifyOverDueEscalation(mstTaskAssignment.getBackupUser1(), siteDetailId, mstTaskAssignment.getBackupUser1(), quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
					}
					if(StringUtils.isNotBlank(mstTaskAssignment.getBackupUser2())) {
					LOGGER.info("escalation level2 invoked for notificationKey={} email={} quoteCode={}",notificationKey,mstTaskAssignment.getBackupUser2(),quoteCode);
						notifyOverDueEscalation(mstTaskAssignment.getBackupUser2(), siteDetailId, mstTaskAssignment.getBackupUser2(), quoteCode, url,commercialQuoteDetailBean.getOptyId(),commercialQuoteDetailBean.getAccountName(),history,commercialQuoteDetailBean.getEmail());
					}
				}
			}
			}
			
		} catch (Exception e) {
			LOGGER.error("Error in fetching approver details", e);
		}
	}
	
	public void groupAssigneeNotification(String notificationKey, String taskKey, Map<String, Object> processMap,
			String url) throws TclCommonException {
		try {
			Integer siteDetailId = (Integer) processMap.get("siteDetailId");
			String quoteCode = (String) processMap.get("quoteCode");
			List<String> email = new ArrayList<>();
			// TODO: take assignee based on region
			
			
			LOGGER.info("assignee notification invoked for taskDefKey={} siteDetailId={}", taskKey,siteDetailId);
	
			MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(taskKey);
			if (mstTaskDef!=null) {
				Optional<SiteDetail> siteDetail = siteDetailRepository.findById(siteDetailId);		
				if (siteDetail.isPresent()) {				
					CommercialQuoteDetailBean commercialQuoteDetailBean = new CommercialQuoteDetailBean();
					commercialQuoteDetailBean.setAccountName(siteDetail.get().getAccountName());
					commercialQuoteDetailBean.setEmail(siteDetail.get().getQuoteCreatedBy());
					commercialQuoteDetailBean.setOptyId(siteDetail.get().getOpportunityId());
					String group = mstTaskDef.getAssignedGroup();
					LOGGER.info("group={} Region={}", group,siteDetail.get().getRegion());
					if (siteDetail.get().getRegion() != null && group != null) {
						List<MstTaskRegion> mstTaskRegions = mstTaskRegionRepository
								.findByRegionAndGroup(siteDetail.get().getRegion(), group);
						if (mstTaskRegions != null && !mstTaskRegions.isEmpty()) {
							mstTaskRegions.stream().forEach(mstTaskRegion -> {
								email.add(mstTaskRegion.getEmail());
							});
						}
						if (commercialQuoteDetailBean != null && email!=null && !email.isEmpty()) {
							notifyTaskAssignmentCommercial(email, quoteCode, commercialQuoteDetailBean.getOptyId(),
									commercialQuoteDetailBean.getAccountName(), commercialQuoteDetailBean.getEmail());
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in groupAssigneeNotification", e);
		}
	}
	/**
	 * 
	 * Notify Commercial Flow Complete
	 * @param customerMail
	 * @param serviceCode
	 * @param optyName
	 * @param accountName
	 * @param history
	 * @return
	 */
	public boolean notifyCommercialFlowComplete(String customerMail,String serviceCode,String optyName,String accountName,String history,String accountManager) {
		boolean isSuccess = false;

		LOGGER.info("assignee notification invoked for quoteCode={}", serviceCode);
		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			//Objects.requireNonNull(optyName,"Opportunity name cannot be null");
			Objects.requireNonNull(serviceCode,"Service ID cannot be null");
			Objects.requireNonNull(accountName,"Task Name cannot be null");
			Objects.requireNonNull(history,"History cannot be null");
			Objects.requireNonNull(accountManager,"Account Manager cannot be null");
			
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("quotecode", serviceCode);
			map.put("link", appHost.concat("/optimus/configuration"));
			map.put("optyname",optyName );
			map.put("accountname", accountName);
			map.put("history", history);
			map.put("accountmanager", accountManager);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Commercial details have been approved");
			mailNotificationRequest
					.setTemplateId(commercialApprovedTemplate);
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
	
	public boolean notifyTaskAssignment(String customerMail, String memberName,String serviceCode,String taskName, String expectedClosure,
			String taskDetails) {
		boolean isSuccess = false;

		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(memberName,"Member name cannot be null");
			Objects.requireNonNull(serviceCode,"Service ID cannot be null");
			Objects.requireNonNull(taskName,"Task Name cannot be null");
			Objects.requireNonNull(expectedClosure,"Closure value cannot be null");
			Objects.requireNonNull(taskDetails,"Task Details cannot be null");
						
			memberName = StringUtils.trimToEmpty(memberName).replace("@legomail.com", "");
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("memberName", memberName);
			map.put("serviceId", serviceCode);
			map.put("taskName", taskName);
			map.put("expectedClosure", expectedClosure);
			map.put("taskDetails", taskDetails);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Task Assignment Notification");
			mailNotificationRequest
					.setTemplateId(notifyTaskAssignmentTemplate);
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
	 * This method is used to notify about Task Assignment to Admin
	 * 
	 * @author diksha garg
	 * notifyTaskAssignmentAdmin
	 * @param customerMail
	 * @param memberName
	 * @param serviceId
	 * @param taskName
	 * @param expectedClosure
	 * @param taskDetails
	 * @return
	 */
	
	public boolean notifyTaskAssignmentAdmin(String customerMail, String memberName,String serviceCode,String taskName, String expectedClosure,
			String taskDetails) {
		boolean isSuccess = false;

		try {
			
			Objects.requireNonNull(customerMail,"Customer Mail cannot be null");
			Objects.requireNonNull(memberName,"Member name cannot be null");
			Objects.requireNonNull(serviceCode,"Service ID cannot be null");
			Objects.requireNonNull(taskName,"Task Name cannot be null");
			Objects.requireNonNull(expectedClosure,"Closure value cannot be null");
			Objects.requireNonNull(taskDetails,"Task Details cannot be null");
						
			memberName = StringUtils.trimToEmpty(memberName).replace("@legomail.com", "");
			HashMap<String, Object> map = new HashMap<>();
			List<String> toAddresses = new ArrayList<>();
			toAddresses.add(customerMail);
			List<String> ccAddresses = new ArrayList<>();
			map.put("memberName", memberName);
			map.put("serviceId", serviceCode);
			map.put("taskName", taskName);
			map.put("expectedClosure", expectedClosure);
			map.put("taskDetails", taskDetails);
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			mailNotificationRequest.setFrom(fromAddress);
			List<String> bccAddresses = new ArrayList<>();
			if (notificationBccMail != null) {
				bccAddresses.addAll(Arrays.asList(notificationBccMail));
			}

			mailNotificationRequest.setBcc(bccAddresses);
			mailNotificationRequest.setSubject("Task Assignment Notification");
			mailNotificationRequest
					.setTemplateId(notifyTaskAssignmentAdminTemplate);
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
			LOGGER.info("MDC Filter token value in before Queue call notifyValidation {} :",
					MDC.get(CommonConstants.MDC_TOKEN_KEY));
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
		} catch (Exception e) {
			LOGGER.error("Error in notification for Builder Contract ", e);
		}
		
	}
	
	/// MANUAL FEASIBILITY NOTIFICATION METHOD STARTS....
	
	/**
	 * @param notificationKey
	 * @param taskKey
	 * @param processMap
	 * @param url
	 * @throws TclCommonException
	 */
	public void manualFeasibilityTaskNotifyAssign(Task task) {
		try {

			// trigger mail for one task assignment at a time
			Map<String, Object> processMap = new HashMap<String, Object>();
			MfTaskDetail taskDetail = populateMapForMFNotification(task, processMap);
			String subject = "Feasibility Task :" + task.getFeasibilityId() + " is assigned with Subject: "
					+ taskDetail.getSubject();
			LOGGER.info(" Assign notification invoked for taskDefKey={} ", task.getMstTaskDef().getKey());
			MstTaskDef mstTaskDef = task.getMstTaskDef();
			List<String> userListForGivenGrp = new ArrayList<String>();

			if (mstTaskDef != null) {
				String response = null;
				if (taskDetail.getAssignedTo() != null) {
					try {
						response = (String) mqUtils.sendAndReceive(getMstUserDetailsQueue,
								Utils.convertObjectToJson(taskDetail.getAssignedTo()));
					} catch (TclCommonException e) {
						LOGGER.error("Error in getting users of group. Exception in queue call!!", e);
					}

					if (StringUtils.isNotBlank(response) && !response.isEmpty()) {
						userListForGivenGrp = Utils.fromJson(response, new TypeReference<List<String>>() {
						});
						
						userListForGivenGrp=	userListForGivenGrp.stream().filter( x -> 
						 x.split("@")[1].contains("tata")).collect(Collectors.toList());
						notifyManualFeasibilityUsers(userListForGivenGrp, processMap, subject, assignTemplate);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in groupAssigneeNotification", e);
		}
	}

	/**
	 * @author krutsrin
	 * @param mfStatus 
	 * @param notificationKey
	 * @param taskKey
	 * @param processMap
	 * @param url
	 * @throws TclCommonException
	 */
	public void manualFeasibilityTaskClose(Task task) {
		try {
			// trigger mail for one task assignment at a time
			Map<String, Object> processMap = new HashMap<String, Object>();
			MfTaskDetail taskDetail = populateMapForMFNotification(task, processMap);
			String subject = "Feasibility Task :" + task.getFeasibilityId() + "'s status is updated to "
					+ taskDetail.getStatus();
			LOGGER.info(" close notification invoked for taskDefKey={} ", task.getMstTaskDef().getKey());
			MstTaskDef mstTaskDef = task.getMstTaskDef();
			List<String> userListForGivenGrp = new ArrayList<String>();

			if (mstTaskDef != null) {
				if (taskDetail.getAssignedFrom() != null && taskDetail.getAssignedTo() != null) {
					userListForGivenGrp.add(taskDetail.getAssignedFrom());
					notifyManualFeasibilityUsers(userListForGivenGrp, processMap, subject, closeTemplate);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in close notification", e);
		}
	}
	
	/**
	 * @author krutsrin
	 * @param notificationKey
	 * @param taskKey
	 * @param processMap
	 * @param url
	 * @throws TclCommonException
	 */
	public void manualFeasibilityTaskReturn(Task task) {
		try {
			// trigger mail for one task assignment at a time
			Map<String,Object> processMap = new HashMap<String,Object>();
			MfTaskDetail taskDetail = populateMapForMFNotification(task, processMap);	
			String subject = "Feasibility Task :"+ task.getFeasibilityId() + "'s status is updated to " + taskDetail.getReason();
			LOGGER.info("Return notification invoked for taskDefKey={} ", task.getMstTaskDef().getKey());

			MstTaskDef mstTaskDef = task.getMstTaskDef();
			List<String> userListForGivenGrp = new ArrayList<String>();

			if (mstTaskDef != null) {
				if (taskDetail.getAssignedFrom() != null && taskDetail.getAssignedTo()!=null) {
					userListForGivenGrp.add(taskDetail.getAssignedFrom());
					notifyManualFeasibilityUsers(userListForGivenGrp, processMap, subject,returnTemplate);
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in return notification", e);
		}
	}
	
	

	/**
	 * 
	 * construct notification
	 * @param task
	 * @param processMap
	 * @return MfTaskDetail
	 * @throws TclCommonException
	 */
	private MfTaskDetail populateMapForMFNotification(Task task, Map<String, Object> processMap)
			throws TclCommonException {
		MfTaskDetail taskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
		
		String fullAddress = null;
		MfDetailAttributes mfdetailAttr = null;
		if (task.getMfDetail() != null && task.getMfDetail().getMfDetails() != null) {
			String mfDetailStr = task.getMfDetail().getMfDetails();
			mfdetailAttr = Utils.convertJsonToObject(mfDetailStr, MfDetailAttributes.class);
			if (mfdetailAttr.getAddressLineOne() != null) {
				fullAddress = mfdetailAttr.getAddressLineOne();
			}
			if (fullAddress != null &&  mfdetailAttr.getAddressLineTwo()!=null) {
				fullAddress = fullAddress + mfdetailAttr.getAddressLineTwo();
			}
		}
		processMap.put("taskId",task.getFeasibilityId());
		processMap.put("taskRequest", taskDetail.getSubject());
		processMap.put("assignedFrom",taskDetail.getAssignedFrom());
		processMap.put("assignedTo",taskDetail.getAssignedTo());
		
		if (task.getCreatedTime() != null) {
			Date date = new Date(task.getCreatedTime().getTime());
			processMap.put("assignedOn", formatter.format(date));
		}
		processMap.put("oppurtunityAccountName",mfdetailAttr.getOpportunityAccountName());
		processMap.put("requestorComments",taskDetail.getRequestorComments());
		processMap.put("responderComments", taskDetail.getResponderComments());
		processMap.put("status", task.getMstStatus().getCode());
		
		processMap.put("localLoopCapacity", mfdetailAttr.getLocalLoopBandwidth());
		processMap.put("localloopInterface", mfdetailAttr.getLocalLoopInterface());
		
		processMap.put("quoteCode", task.getQuoteCode());
		processMap.put("productName", task.getServiceType());
		processMap.put("siteAddress",fullAddress);
		processMap.put("salesLink", appHost.concat("/optimus/tasks/dashboard"));
		processMap.put("reason", taskDetail.getReason());
		processMap.put("feasibilityStatus", taskDetail.getStatus());
		processMap.put("subject", taskDetail.getSubject());
		return taskDetail;
	}
	
	/**
	 * This method is used to notify about Task Assignment
	 * 
	 * @author Kruthika Srinivasan
	 * notifyTaskAssignment
	 * @param template 
	 * @param customerMail
	 * @param memberName
	 * @param serviceId
	 * @param taskName
	 * @param expectedClosure
	 * @param taskDetails
	 * @return
	 */
	
	public boolean notifyManualFeasibilityUsers(List<String> listOfUsers, Map<String, Object> processMap,
			String subject, String template) {
		boolean isSuccess = false;
		try {
			if (processMap != null && !processMap.isEmpty()) {
				Objects.requireNonNull(listOfUsers, "user Mail cannot be null");
				Objects.requireNonNull(subject, "Subject cannot be null");
				Objects.requireNonNull(processMap.get("assignedFrom"), "assignedFrom cannot be null");
				LOGGER.info("assignee notification invoked for user Mail={} ", listOfUsers);

				List<String> ccAddresses = new ArrayList<>();
				MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
				mailNotificationRequest.setFrom(fromAddress);
				List<String> bccAddresses = new ArrayList<>();
				if (notificationBccMail != null) {
					bccAddresses.addAll(Arrays.asList(notificationBccMail));
				}

				mailNotificationRequest.setBcc(bccAddresses);
				mailNotificationRequest.setSubject(subject);
				mailNotificationRequest.setTemplateId(template);
				mailNotificationRequest.setTo(listOfUsers);
				mailNotificationRequest.setCc(ccAddresses);
				mailNotificationRequest.setVariable(processMap);
				LOGGER.info("MDC Filter token value in before Queue call notifyMFUsers {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
				isSuccess = true;

			}
		} catch (Exception e) {
			LOGGER.error("Error in notification for manual feasibility ", e);
		}
		return isSuccess;
	}
	
}
