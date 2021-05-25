package com.tcl.dias.servicefulfillmentutils.delegates;


import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.NotificationTrigger;
import com.tcl.dias.servicefulfillment.entity.entities.ScAdditionalServiceParam;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillment.entity.repository.NotificationTriggerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScAdditionalServiceParamRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.CustomerDepHoldService;
import com.tcl.dias.servicefulfillmentutils.service.v1.NotificationService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskDataService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("notificationDelegate")
public class NotificationDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(NotificationDelegate.class);
	private static final String localItContactEmailId = "localItContactEmailId";
	@Autowired
	NotificationService notificationService;

	@Autowired
	TaskDataService taskDataService;

	@Autowired
	TaskService taskService;
	
	@Autowired
	TaskRepository taskRepository;

	@Autowired
	WorkFlowService workFlowService;

	@Value("${app.host}")
	String appHost;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;
		
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScComponentRepository scComponentRepository; 
	
	@Autowired
	ScAdditionalServiceParamRepository scAdditionalServiceParamRepository;
	
	@Autowired
	CustomerDepHoldService customerDepHoldService;
	
	@Autowired
	NotificationTriggerRepository notificationTriggerRepository;
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	public void execute(DelegateExecution execution) {
		
		
		logger.info("NotificationDelegate invoked for {} Id={}", execution.getCurrentActivityId(), execution.getId());

		Map<String, Object> processMap = execution.getVariables();
		try {
			Integer serviceId = (Integer) processMap.get(SERVICE_ID);
			logger.info("serviceId={}", serviceId);
			Optional<ScServiceDetail> optionalService=scServiceDetailRepository.findById(serviceId);
			ScServiceDetail scServiceDetail=	optionalService.get();
			String status=scServiceDetail.getMstStatus().getCode();
			String serviceCodeProcess = (String) processMap.get(SERVICE_CODE);
			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(scServiceDetail.getId(), "LM", "A");
			logger.info("Inside NotificationDelegate for serviceCode {} and task_def_key {}", serviceCodeProcess, execution.getCurrentActivityId());

			if (execution.getCurrentActivityId() != null && (status.equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
					|| status.equalsIgnoreCase(TaskStatusConstants.ACTIVE))) {
				String taskDefKey = StringUtils.trimToEmpty(execution.getCurrentActivityId());
				// appHost ="https://optimus-dev.tatacommunications.com";
				String url = appHost + "/optimus/tasks/dashboard";
				String customerUrl = appHost + "/optimus";

				if (taskDefKey.startsWith("customer-reminder")) {
					logger.info("Inside  customer-reminder NotificationDelegate for serviceCode {} and task_def_key {}", serviceCodeProcess, execution.getCurrentActivityId());

					processCustomerRemainderAndResourceRelease(taskDefKey, serviceId, serviceCodeProcess,
							scServiceDetail, scComponentAttributesmap, processMap, customerUrl);

				} else if (taskDefKey.startsWith("reminder-")) {

					String taskKey = taskDefKey.replace("reminder-", "").replace("_appchange", "").replaceAll("_reopen",
							"");
					Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,
							taskKey);
					if (task != null && (task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED))) {
						Set<TaskAssignment> taskAssignmentList = task.getTaskAssignments();
						for (TaskAssignment taskAssignment : taskAssignmentList) {
							logger.info("Reminder notification invoked for taskDefKey{} serviceId={} user={}", taskKey,
									serviceId, taskAssignment.getUserName());
							if (StringUtils.isNotBlank(taskAssignment.getUserName())) {
								List<String> ccAdminEmail=null;
                                if(taskAssignment.getGroupName()!=null) {
                                    List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
                                            .findByGroup(taskAssignment.getGroupName());
                                    if(!mstTaskRegionList.isEmpty()) {
                                        ccAdminEmail=mstTaskRegionList.stream().filter(mastRegion->mastRegion.getEmail()!=null).map(email->email.getEmail()).collect(Collectors.toList());
                                    }
                                }
								notificationService.notifyOverDueReminder(taskAssignment.getUserName(),
										task.getScOrderId(), taskAssignment.getUserName(), task.getServiceCode(), url,
										task.getMstTaskDef().getName(), task.getOrderCode(), ccAdminEmail);
								break;
							} else {
								if (taskAssignment.getGroupName() != null) {
									List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
											.findByGroup(taskAssignment.getGroupName());
									for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
										if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
											logger.info(
													"Reminder notification to group for taskDefKey{} serviceId={} user={}",
													taskKey, serviceId, mstTaskRegion.getEmail());
											notificationService.notifyOverDueReminder(mstTaskRegion.getEmail(),
													task.getScOrderId(), taskAssignment.getUserName(),
													task.getServiceCode(), url, task.getMstTaskDef().getName(),
													task.getOrderCode(), null);
										}
									}

								}
							}
						}

					} else {
						logger.error("Reminder notification not triggered for taskDefKey{} serviceId={}", taskKey,
								serviceId);
					}

				} else if (taskDefKey.contains("notify-offnet-po")) {
					String poType = "Offnet LM";

					List<String> toMailIds = new ArrayList<>();
					List<String> ccMailIds = new ArrayList<>();
					List<MstTaskRegion> mstTaskRegionListMgmt = mstTaskRegionRepository.findByGroup("ASP_PO");

					for (MstTaskRegion mstTaskRegion : mstTaskRegionListMgmt) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							toMailIds.add(mstTaskRegion.getEmail());
						}
					}

					ccMailIds.add("diksha.garg@tatacommunications.com");
					
					notificationService.notifyOffnetPoCreationMultiple(toMailIds, ccMailIds, serviceCodeProcess, poType,
							url);

				} else if (taskDefKey.contains("notify-cpe-hardware-pr")
						|| taskDefKey.contains("notify-cpe-license-pr")) {
					String poType = null;
					if (taskDefKey.contains("notify-cpe-hardware-pr")) {
						poType = "Hardware";

					} else if (taskDefKey.contains("notify-cpe-license-pr")) {
						poType = "License";
					}
					List<String> toMailIds = new ArrayList<>();
					List<String> ccMailIds = new ArrayList<>();

					// to SCM_Legal and cc to SCM M&L and SCM_Mgmt.
					List<MstTaskRegion> mstTaskRegionListLegal = mstTaskRegionRepository.findByGroup("SCM_Legal");

					for (MstTaskRegion mstTaskRegion : mstTaskRegionListLegal) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							// notificationService.notifyPoCreation(mstTaskRegion.getEmail(),serviceCodeProcess,
							// poType, url);
							toMailIds.add(mstTaskRegion.getEmail());

						}
					}

					List<MstTaskRegion> mstTaskRegionListMgmt = mstTaskRegionRepository.findByGroup("SCM_Mgmt");

					for (MstTaskRegion mstTaskRegion : mstTaskRegionListMgmt) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							ccMailIds.add(mstTaskRegion.getEmail());
						}
					}
					List<MstTaskRegion> mstTaskRegionListML = mstTaskRegionRepository.findByGroup("SCM_M_L");
					for (MstTaskRegion mstTaskRegion : mstTaskRegionListML) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							ccMailIds.add(mstTaskRegion.getEmail());
						}
					}

					notificationService.notifyPoCreationMultiple(toMailIds, ccMailIds, serviceCodeProcess, poType, url);

				}else if (taskDefKey.contains("notify-sdwan-cpe-hardware-pr")
						|| taskDefKey.contains("notify-sdwan-cpe-license-pr")) {
					String poType = null;
					if (taskDefKey.contains("notify-sdwan-cpe-hardware-pr")) {
						poType = "Hardware";

					} else if (taskDefKey.contains("notify-sdwan-cpe-license-pr")) {
						poType = "License";
					}
					List<String> toMailIds = new ArrayList<>();
					List<String> ccMailIds = new ArrayList<>();

					// to SCM_Legal and cc to SCM M&L and SCM_Mgmt.
					List<MstTaskRegion> mstTaskRegionListLegal = mstTaskRegionRepository.findByGroup("SCM_Legal");

					for (MstTaskRegion mstTaskRegion : mstTaskRegionListLegal) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							// notificationService.notifyPoCreation(mstTaskRegion.getEmail(),serviceCodeProcess,
							// poType, url);
							toMailIds.add(mstTaskRegion.getEmail());

						}
					}

					List<MstTaskRegion> mstTaskRegionListMgmt = mstTaskRegionRepository.findByGroup("SCM_Mgmt");

					for (MstTaskRegion mstTaskRegion : mstTaskRegionListMgmt) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							ccMailIds.add(mstTaskRegion.getEmail());
						}
					}
					List<MstTaskRegion> mstTaskRegionListML = mstTaskRegionRepository.findByGroup("SCM_M_L");
					for (MstTaskRegion mstTaskRegion : mstTaskRegionListML) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							ccMailIds.add(mstTaskRegion.getEmail());
						}
					}
					Integer componentId = (Integer) processMap.get("cpeOverlayComponentId");
					String vendorCode = (String) processMap.get("vendorCode");
					notificationService.notifyPoCreationMultipleIzosdwan(toMailIds, ccMailIds, serviceId, poType, url,
							componentId, vendorCode, serviceCodeProcess);

				} else if (taskDefKey.contains("notify-generate-cpe-mrn")) {
					logger.info("Inside Notify for MRN for serviceCode {} and task def key {}", serviceCodeProcess, execution.getCurrentActivityId());
					List<MstTaskRegion> mstTaskRegionList;
					String distributionCenterName = scComponentAttributesmap.getOrDefault("distributionCenterName", null);
					logger.info("Locationf of distributet center name : {}",distributionCenterName);
					if(distributionCenterName!=null && !distributionCenterName.isEmpty()) {
						mstTaskRegionList = mstTaskRegionRepository.findByGroupAndLocation("SCM-Warehouse", distributionCenterName);
					}else {
						mstTaskRegionList = mstTaskRegionRepository.findByGroup("SCM-Warehouse");
					}
					for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							notificationService.notifyMrnEmail(mstTaskRegion.getEmail(), serviceCodeProcess);
						}
					}

				} else if (taskDefKey.contains("notify-teamsdr-generate-cpe-mrn")) {
					logger.info("Inside Notify for MRN for serviceCode {} and task def key {}", serviceCodeProcess, execution.getCurrentActivityId());
					List<MstTaskRegion> mstTaskRegionList;
					String distributionCenterName = scComponentAttributesmap.getOrDefault("distributionCenterName", null);
					logger.info("Locationf of distributet center name : {}",distributionCenterName);
					if(distributionCenterName!=null && !distributionCenterName.isEmpty()) {
						mstTaskRegionList = mstTaskRegionRepository.findByGroupAndLocation("SCM-Warehouse", distributionCenterName);
					}else {
						mstTaskRegionList = mstTaskRegionRepository.findByGroup("SCM-Warehouse");
					}
					for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							notificationService.notifyMrnEmailMg(mstTaskRegion.getEmail(), serviceCodeProcess);
						}
					}
				} else if (taskDefKey.contains("notify-sdwan-generate-cpe-mrn")) {
					logger.info("Inside SDWAN Notify for MRN for serviceCode {} and task def key {}", serviceCodeProcess, execution.getCurrentActivityId());
					List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("SCM-Warehouse");
					String siteType = (String) processMap.get("site_type");
					logger.info("siteType {}",siteType);
					for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							notificationService.notifySdwanMrnEmail(mstTaskRegion.getEmail(), serviceCodeProcess,siteType);
						}
					}
				}
				else if ("notify-internal-call-mom".equalsIgnoreCase(taskDefKey)
						|| "notify-kick-off-call-mom".equalsIgnoreCase(taskDefKey)) {
					logger.info("Inside SDWAN Notify for Mom serviceCode {} and task def key {}", serviceCodeProcess,
							taskDefKey);
					ScComponent scComponent = scComponentRepository
							.findFirstByScServiceDetailIdAndComponentNameAndSiteTypeOrderByIdAsc(serviceId, "LM", "A");
					ScComponentAttribute scComponentAttribute = null;
					if (taskDefKey.equalsIgnoreCase("notify-internal-call-mom")) {
						logger.info("Inside Notify for Internal Call Mom task def key {}", taskDefKey);
						scComponentAttribute = scComponentAttributesRepository
								.findFirstByScComponent_idAndAttributeName(scComponent.getId(), "internalCallDetails");
					} else if (taskDefKey.equalsIgnoreCase("notify-kick-off-call-mom")) {
						logger.info("Inside Notify for Kickoff Call Mom task def key {}", taskDefKey);
						scComponentAttribute = scComponentAttributesRepository
								.findFirstByScComponent_idAndAttributeName(scComponent.getId(), "kickOffMomDetails");
					}
					if (scComponentAttribute != null && scComponentAttribute.getAttributeValue() != null
							&& !scComponentAttribute.getAttributeValue().isEmpty()) {
						logger.info("MOM attribute exists::{}", scComponentAttribute.getId());
						Optional<ScAdditionalServiceParam> param = scAdditionalServiceParamRepository
								.findById(Integer.valueOf(scComponentAttribute.getAttributeValue()));
						Task task = workFlowService.processServiceTask(execution);
						List<String> emailList = new ArrayList<>();
						if (task != null) {
							logger.info("Task exists::{}", task.getId());
							Map<String, Object> taskDataMap;
							try {
								taskDataMap = taskDataService.getTaskData(task);
								logger.info("taskDataMap in Notification {} ", taskDataMap);
								String subject = "";
								if (taskDefKey.equalsIgnoreCase("notify-internal-call-mom")) {
									logger.info("notifyInternalCallMom mail triggered ");
									subject = "MOM:LLD Preparation Internal Call";
									emailList=getInternalMOMMailDetails(taskDataMap,emailList);
									notificationService.notifyInternalMailMomDetails(emailList, param.get().getValue(),
											subject, task.getOrderCode());
								} else if (taskDefKey.equalsIgnoreCase("notify-kick-off-call-mom")) {
									logger.info("notifyKickOffCallMom mail triggered ");
									emailList=getKickOffMailDetails(taskDataMap,emailList);
									subject = "MOM:LLD Preparation Kickoff Call";
									logger.info("calling kickoff notification method with task def key{} ", taskDefKey);
									notificationService.notifyInternalMailMomDetails(emailList, param.get().getValue(),
											subject, task.getOrderCode());
								}
							} catch (TclCommonException e) {
								logger.error("exception occured {} ", e);
							}
							workFlowService.processServiceTaskCompletion(execution, null, task);
						}else{
							logger.info("MOM related details not exists:{} ", serviceCodeProcess);
						}
					}
				}else if (taskDefKey.contains("notify-generate-endpoint-mrn")) {
					logger.info("Inside Notify for Endpoint MRN for serviceCode {} and task def key {}", serviceCodeProcess, execution.getCurrentActivityId());
					List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("SCM-Warehouse");
					for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
						if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
							notificationService.notifyMrnEmailEndpoint(mstTaskRegion.getEmail(), serviceCodeProcess);
						}
				    } 
				} else {
					Task task = workFlowService.processServiceTask(execution);
					if (task != null && (task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED))) {

						Map<String, Object> taskDataMap = taskDataService.getTaskData(task);
						logger.info("taskDataMap in Notification {} ", taskDataMap);

						/*String customerName = StringUtils
								.trimToEmpty((String) taskDataMap.get(MasterDefConstants.LOCAL_IT_CONTACT_NAME));*/
						String customerName = StringUtils
								.trimToEmpty((String) taskDataMap.get("customerName"));
						String customerEmail = StringUtils
								.trimToEmpty((String) taskDataMap.get(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL));

						String serviceCode = StringUtils
								.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SERVICE_CODE));
						String orderCode = StringUtils
								.trimToEmpty((String) taskDataMap.get(MasterDefConstants.ORDER_CODE));
						String type = task.getMstTaskDef().getTitle();
						String subject = task.getMstTaskDef().getName();

						//customerName = customerName.replace("@legomail.com", "");

						if ("notify-customer-about-document-deficiency".equals(taskDefKey)) {
							url = appHost + "/optimus/orders/" + orderCode + "/service-details/" + serviceId;
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));

							notificationService.notifyValidation(customerEmail, siteAddress, customerName, serviceCode,
									url);
						} else if ("notify-customer-lm-onnet-wireline-site-readiness".equals(taskDefKey)) {
							url = appHost + "/optimus/orders/" + orderCode + "/service-details/" + serviceId;
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));

							notificationService.notifyCustomerReadiness(customerEmail, customerName, orderCode,
									siteAddress, serviceCode, url, type);
						} else if ("lm-notify-mux-integration".equals(taskDefKey)) {
							String sdnocTeamName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SDNOC_TEAM_NAME));
							String plannedEventSchedule = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.PLANNED_EVENT_SCHEDULE));
							notificationService.notifyMuxIntegration(customerEmail, sdnocTeamName, serviceId,
									customerName, plannedEventSchedule, url);
						}else if ("notify-sdwan-customer-about-cpe-installation".equals(taskDefKey)) {
							logger.info("taskDefKey ::{} ", taskDefKey);
							url = appHost + "/optimus/orders/" + orderCode + "/service-details/" + serviceId;

							String fieldEngineerName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_NAME));
							String fieldEngineerContactNumber = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_CONTACT));
							String flowType = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FLOW_TYPE));
							String siteVisitDateTime ="";
							String siteVisitDate = "";
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));
							if(!flowType.isEmpty()){
								if(flowType.equalsIgnoreCase("MACD")){
									logger.info("MACD Service Id::{} with taskDefKey::{}",serviceId,taskDefKey);
									if (taskDataMap.get("downtimeDuration") != null){
										siteVisitDate = (String)taskDataMap.get("downtimeDuration");
									}
									String fromTime ="";
									if (taskDataMap.get("fromTime") != null){
										fromTime = (String)taskDataMap.get("fromTime");
									}
									String toTime ="";
									if (taskDataMap.get("toTime") != null){
										toTime = (String)taskDataMap.get("toTime");
									}
									if (taskDataMap.get("downTimeContactMailId") != null){
										customerEmail = (String)taskDataMap.get("downTimeContactMailId");
									}
									siteVisitDateTime = siteVisitDate + "  " + fromTime +"-"+ toTime;
								}else{
									logger.info("Other than MACD Service Id::{} with taskDefKey::{}",serviceId,taskDefKey);
									Timestamp siteVisitTimestamp = (Timestamp) taskDataMap.get("appointmentDate") != null
											? (Timestamp) taskDataMap.get("appointmentDate")
											: null;
									if (siteVisitTimestamp != null)
										siteVisitDate = DateUtil.convertDateToString(siteVisitTimestamp);
									String siteVisitTime = StringUtils.trimToEmpty((String) taskDataMap.get("appointmentTime"));
									siteVisitDateTime = siteVisitDate + "  " + siteVisitTime;
								}
								logger.info("Service Id::{} with taskDefKey::{} with flowType::{},siteVisitDateTime::{}", serviceId,taskDefKey,flowType,siteVisitDateTime);
							}
							notificationService.notifyCustomerSiteVisit(customerEmail, customerName, orderCode,
									siteAddress, fieldEngineerName, fieldEngineerContactNumber, siteVisitDateTime, url,
									type, serviceCode);
						} else if ("notify-sdwan-fe-cpe-installation".equals(taskDefKey)) {
							logger.info("taskDefKey ::{} ", taskDefKey);
							String fieldEngineerName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_NAME));
							String fieldEngineerEmail = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_EMAIL));

							String siteDemarc = getSiteDemarcDetails(taskDataMap);
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));
							String flowType = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FLOW_TYPE));
							String siteVisitDateTime ="";
							String siteVisitDate = "";
							String siteVisitTimeSlot ="";
							if(!flowType.isEmpty()){
								if(flowType.equalsIgnoreCase("MACD")){
									logger.info("MACD Service Id::{} with taskDefKey::{}",serviceId,taskDefKey);
									if (taskDataMap.get("downtimeDuration") != null){
										siteVisitDate = (String)taskDataMap.get("downtimeDuration");
									}
									String fromTime ="";
									if (taskDataMap.get("fromTime") != null){
										fromTime = (String)taskDataMap.get("fromTime");
									}
									String toTime ="";
									if (taskDataMap.get("toTime") != null){
										toTime = (String)taskDataMap.get("toTime");
									}
									siteVisitDateTime = siteVisitDate + "  " + fromTime +"-"+ toTime;
									siteVisitTimeSlot = fromTime +"-"+ toTime;
								}else{
									logger.info("Other than MACD Service Id::{} with taskDefKey::{}",serviceId,taskDefKey);
									Timestamp siteVisitTimestamp = (Timestamp) taskDataMap.get("appointmentDate") != null
											? (Timestamp) taskDataMap.get("appointmentDate")
											: null;
									if (siteVisitTimestamp != null)
										siteVisitDate = DateUtil.convertDateToString(siteVisitTimestamp);
									String siteVisitTime = StringUtils.trimToEmpty((String) taskDataMap.get("appointmentTime"));
									siteVisitDateTime = siteVisitDate + "  " + siteVisitTime;
									siteVisitTimeSlot = StringUtils
											.trimToEmpty((String) taskDataMap.get("appointmentTime"));
									
								}
								logger.info("Service Id::{} with taskDefKey::{} with flowType::{},siteVisitDateTime::{},siteVisitTimeSlot::{}", serviceId,taskDefKey,flowType,siteVisitDateTime,siteVisitTimeSlot);
							}
							notificationService.notifyFieldEngineer(fieldEngineerEmail, customerName, fieldEngineerName,
									siteAddress, siteDemarc, type, siteVisitDateTime, siteVisitTimeSlot, subject,
									serviceCode);
						}  else if (taskDefKey.startsWith("notify-customer")) {
							url = appHost + "/optimus/orders/" + orderCode + "/service-details/" + serviceId;

							String fieldEngineerName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_NAME));
							String fieldEngineerContactNumber = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_CONTACT));

							String siteVisitDate = "";
							Timestamp siteVisitTimestamp = (Timestamp) taskDataMap.get("appointmentDate") != null
									? (Timestamp) taskDataMap.get("appointmentDate")
									: null;
							if (siteVisitTimestamp != null)
								siteVisitDate = DateUtil.convertDateToString(siteVisitTimestamp);
							String siteVisitTime = StringUtils.trimToEmpty((String) taskDataMap.get("appointmentTime"));
							String siteVisitDateTime = siteVisitDate + "  " + siteVisitTime;
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));
							notificationService.notifyCustomerSiteVisit(customerEmail, customerName, orderCode,
									siteAddress, fieldEngineerName, fieldEngineerContactNumber, siteVisitDateTime, url,
									type, serviceCode);
						} else if (taskDefKey.startsWith("notify-fe")) {
							String fieldEngineerName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_NAME));
							String fieldEngineerEmail = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_EMAIL));

							String siteVisitDate = "";
							Timestamp siteVisitTimestamp = (Timestamp) taskDataMap.get("appointmentDate") != null
									? (Timestamp) taskDataMap.get("appointmentDate")
									: null;
							if (siteVisitTimestamp != null)
								siteVisitDate = DateUtil.convertDateToString(siteVisitTimestamp);
							String siteVisitTime = StringUtils.trimToEmpty((String) taskDataMap.get("appointmentTime"));
							String siteVisitDateTime = siteVisitDate + "  " + siteVisitTime;
							String siteVisitTimeSlot = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_VISIT_TIME_SLOT));
							String siteDemarc = getSiteDemarcDetails(taskDataMap);
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));

							notificationService.notifyFieldEngineer(fieldEngineerEmail, customerName, fieldEngineerName,
									siteAddress, siteDemarc, type, siteVisitDateTime, siteVisitTimeSlot, subject,
									serviceCode);
						} else if (taskDefKey.contains("notify-sdwan-vendor")) {
							logger.info("Inside SDWAN Notify for Vendor for serviceCode {} and task def key {}", serviceCodeProcess, execution.getCurrentActivityId());
							List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup("SDWAN_CPE_INTERNATIONAL_VENDOR");
							String siteType = (String) processMap.get("site_type");
							logger.info("siteType {}",siteType);
							List<String> emailIdList= new ArrayList<>();
							for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
								emailIdList.add(mstTaskRegion.getEmail());
							}
							if (!emailIdList.isEmpty()) {
								logger.info("SDWAN Notify for Vendor mail exists {}",emailIdList.size());
								notificationService.notifySdwanVendorEmail(emailIdList, serviceCodeProcess,siteType);
							}
						}else if (taskDefKey.startsWith("notify-vendor")) {
							String siteVisitDate = "";
							Timestamp siteVisitTimestamp = (Timestamp) taskDataMap.get("appointmentDate") != null
									? (Timestamp) taskDataMap.get("appointmentDate")
									: null;
							if (siteVisitTimestamp != null)
								siteVisitDate = DateUtil.convertDateToString(siteVisitTimestamp);
							String siteVisitTime = StringUtils.trimToEmpty((String) taskDataMap.get("appointmentTime"));
							String siteVisitDateTime = siteVisitDate + "  " + siteVisitTime;

							String siteVisitTimeSlot = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_VISIT_TIME_SLOT));
							String vendorName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.VENDOR_NAME));
							String vendorEmail = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.VENDOR_EMAIL));
							String vendorMobile = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.VENDOR_MOBILE));
							String siteDemarc = getSiteDemarcDetails(taskDataMap);
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));

							Optional<ScServiceDetail> scServiceDetailOpt = scServiceDetailRepository
									.findById(serviceId);
							if (scServiceDetailOpt.isPresent()) {
								scServiceDetail = scServiceDetailOpt.get();
							}

							String rfTechnology = scComponentAttributesmap.getOrDefault("rfTechnology", null);
							notificationService.notifyVendor(taskDefKey, vendorEmail, vendorName, type, customerName,
									siteDemarc, siteAddress, siteVisitDateTime, siteVisitTimeSlot, url, subject,
									serviceCode, rfTechnology);

						} else if ("notify-test-results-vendor".equals(taskDefKey)) {
							String fieldEngineerName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_NAME));
							String fieldEngineerEmail = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_EMAIL));
							String fieldEngineerContactNumber = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.FIELD_ENGINEER_CONTACT));
							String vendorName = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.VENDOR_NAME));
							String vendorEmail = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.VENDOR_EMAIL));
							String vendorMobile = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.VENDOR_MOBILE));
							String siteAddress = StringUtils
									.trimToEmpty((String) taskDataMap.get(MasterDefConstants.SITE_ADDRESS));
							String siteDemarc = getSiteDemarcDetails(taskDataMap);

							notificationService.notifyVendorTestResults(vendorEmail, vendorName, customerName,
									siteDemarc, siteAddress, fieldEngineerName, fieldEngineerEmail,
									fieldEngineerContactNumber, url, serviceCode);
						} else if ("notify-builder-contract".equals(taskDefKey)) {

							String buildingAuthEmail = StringUtils
									.trimToEmpty((String) taskDataMap.get("buildingAuthorityEmailId"));
							String buildingAuthName = StringUtils
									.trimToEmpty((String) taskDataMap.get("buildingAuthorityName"));
							// String sendContractToBA = StringUtils.trimToEmpty((String)
							// taskDataMap.get(MasterDefConstants.));
							String ospEmail = StringUtils.trimToEmpty((String) taskDataMap.get("ospEmail"));
							LocalDateTime twoDaysAheadDate = new Timestamp(new Date().getTime()).toLocalDateTime()
									.plusDays(2);
							final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
							String formattedDateTime = twoDaysAheadDate.format(formatter);
							String address = StringUtils.trimToEmpty((String) taskDataMap.get("ospAddress"));
							AttachmentBean attachment = (AttachmentBean) taskDataMap.get("VCD");
							// notify-builder-contract
							notificationService.notifyBuilderContract(buildingAuthEmail, buildingAuthName, serviceCode,
									ospEmail, formattedDateTime, address, attachment, url);
						} else if ("billing_account_creation_jeopardy".equals(taskDefKey)) {
							LocalDateTime twoDaysAheadDate = new Timestamp(new Date().getTime()).toLocalDateTime()
									.plusDays(2);
							final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
							DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMATTER);
							String formattedDateTime = twoDaysAheadDate.format(formatter);
							notificationService.billingFailureNotification(orderCode, "account");
						} else if (taskDefKey.startsWith("notify-workorder-internal-cabling")) {

							String vendorEmail = StringUtils.trimToEmpty((String) taskDataMap.get("vendorEmail"));
							String casdEmail = StringUtils.trimToEmpty((String) taskDataMap.get("casdEmail"));
							String vendorName = StringUtils.trimToEmpty((String) taskDataMap.get("vendorName"));
							String woNumber = StringUtils.trimToEmpty((String) taskDataMap.get("woNumber"));
							String scopeOfWork = StringUtils.trimToEmpty((String) taskDataMap.get("scopeOfWork"));
							String expectedCompletionDate = StringUtils
									.trimToEmpty((String) taskDataMap.get("expectedCompletionDate"));
							String siteAddress = StringUtils.trimToEmpty((String) taskDataMap.get("siteAddress"));
							String customerLocalContact = StringUtils
									.trimToEmpty((String) taskDataMap.get("customerLocalContact"));
							AttachmentBean attachment = null;
							if (taskDataMap.containsKey("WO")) // notify-workorder-internal-cabling
								attachment = (AttachmentBean) taskDataMap.get("WO");
							else if (taskDataMap.containsKey("WOPE")) // notify-workorder-internal-cabling-pe
								attachment = (AttachmentBean) taskDataMap.get("WOPE");

							notificationService.notifyVendorWorkOrder(vendorEmail, casdEmail, vendorName, woNumber,
									scopeOfWork, expectedCompletionDate, siteAddress, customerLocalContact, attachment,
									serviceCode);
						} else if (taskDefKey.equals("notify-service-acceptance")) {

							String deliveryDate = StringUtils
									.trimToEmpty((String) taskDataMap.get("customerAcceptanceDate"));
							AttachmentBean attachment = (AttachmentBean) taskDataMap.get("Handover-note");
							notificationService.notifyCustomerAcceptance(customerEmail, customerName, deliveryDate,
									orderCode, serviceCode, url, subject, attachment);
						} else if (taskDefKey.equals("notify-termination")) {
							String terminationDate="";
							String parentUuid=null;

							String deliveryDate = StringUtils
									.trimToEmpty((String) taskDataMap.get("customerAcceptanceDate"));
							if(processMap.get("terminationFlowTriggered")!=null && "Yes".equalsIgnoreCase((String)processMap.get("terminationFlowTriggered"))) {
								terminationDate = StringUtils
										.trimToEmpty((String) taskDataMap.get("terminationEffectiveDate"));
							}
							else {
							 terminationDate = StringUtils
									.trimToEmpty((String) taskDataMap.get("terminationDate"));
							}
							Optional<ScServiceDetail> optionalServiceDeatils = scServiceDetailRepository
									.findById(serviceId);
							if (optionalServiceDeatils.isPresent()) {

								if (processMap.get("terminationFlowTriggered") != null && "Yes"
										.equalsIgnoreCase((String) processMap.get("terminationFlowTriggered"))) {
									parentUuid = optionalServiceDeatils.get().getUuid();
								} else {
									parentUuid = optionalServiceDeatils.get().getParentUuid();

								}

								notificationService.notifyCustomerCircuitTermination(customerEmail, customerName,
										orderCode, serviceCode, deliveryDate, terminationDate, parentUuid);

							}

						}

						workFlowService.processServiceTaskCompletion(execution, null, task);
					} else {
						logger.info("Task is null in Notification delegate");
					}
				}
			}

		}catch(Exception e){
			logger.error("ErrorNotificationDelegate", e);
		}
	}

	/**
	 * @author vivek
	 *
	 * @param taskDefKey
	 * @param serviceId
	 * @param serviceCodeProcess
	 * @param processMap 
	 * @param scComponentAttributesmap 
	 * @param scServiceDetail 
	 * @param customerUrl 
	 * @throws TclCommonException 
	 */
	public void processCustomerRemainderAndResourceRelease(String taskDefKey, Integer serviceId,
			String serviceCodeProcess, ScServiceDetail scServiceDetail, Map<String, String> scComponentAttributesmap, Map<String, Object> processMap, String customerUrl) throws TclCommonException {
		try {
		if (scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)) {

			String taskKey = taskDefKey.replace("customer-reminder-", "").replace("_appchange", "")
					.replaceAll("_reopen", "");
			

			logger.info("Customer Reminder notification invoked for taskDefKey{} serviceId={}", taskKey, serviceId);
			// new notification trigger object
			NotificationTrigger notificationTrigger = notificationTriggerEntry(serviceId, serviceCodeProcess,
					taskDefKey, taskKey, "Customer Reminder notification invoked");

			String customerName = StringUtils
					.trimToEmpty((String) processMap.get(MasterDefConstants.LOCAL_IT_CONTACT_NAME));
			String customerEmail = StringUtils
					.trimToEmpty((String) processMap.get(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL));

			List<String> toAddresses = new ArrayList<>();
			List<String> ccAddresses = new ArrayList<>();
			ScComponentAttribute customerContactEmailId = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							scServiceDetail.getId(), localItContactEmailId, AttributeConstants.COMPONENT_LM,
							AttributeConstants.SITETYPE_A);
			if (customerContactEmailId != null && customerContactEmailId.getAttributeValue() != null) {
				//toAddresses.add(customerContactEmailId.getAttributeValue());
			} else {
				logger.info("Customer contact Email Id is not there for given service Id " + scServiceDetail.getId());
				// error updation notification trigger
				notificationTrigger
						.setStatusTrigger("ERROR:" + "Customer contact Email Id is not there for given service Id");
				notificationTriggerRepository.save(notificationTrigger);
				return;
			}
			toAddresses.add(scServiceDetail.getAssignedPM());
			
			if (scServiceDetail.getAssignedPM() != null) {
				ccAddresses.add(scServiceDetail.getAssignedPM());
			}

			Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId, taskKey);
			if (task != null && task.getMstTaskDef().getOwnerGroup().equalsIgnoreCase("CUSTOMER")
					&& task.getMstTaskDef().getAssignedGroup().equalsIgnoreCase("CUSTOMER")
					&& (!task.getMstTaskDef().getKey().equalsIgnoreCase("service-acceptance"))
					&& (!task.getMstTaskDef().getKey().equalsIgnoreCase("experience-survey"))
					&& (task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED))) {

				if (customerDepHoldService.is24DayTrigger(task)) {
					logger.info("Inside is29DayTrigger {} for service Id {}",
							ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()),
							serviceId);
					logger.info("Inside notifyOverDueReminderCustomerBeforeHold for service Id {}", serviceId);
					// day_logger updation notification trigger
					notificationTrigger.setDayLogger(
							"Inside is29DayTrigger and notifyOverDueReminderCustomerBeforeHold for #days :"
									+ ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(),
											LocalDateTime.now()));

					notificationService.notifyOverDueReminderCustomerBeforeHold(customerEmail, task.getScOrderId(),
							customerName, task.getServiceCode(), customerUrl, task.getMstTaskDef().getName(),
							task.getOrderCode(), toAddresses, ccAddresses, notificationTrigger);
				} else if (customerDepHoldService.is30DayHoldTrigger(task) && scServiceDetail.getCreatedDate()
						.after(Timestamp.valueOf(LocalDateTime.of(2020, 11, 15, 0, 0)))) {/*
					logger.info("Inside is30DayHoldTrigger {} for service Id {}",
							ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()),
							serviceId);
					logger.info("Inside notifyForHold for service Id {}", serviceId);
					// day_logger updation notification trigger
					notificationTrigger.setDayLogger("Inside is30DayHoldTrigger and notifyForHold for #days :"
							+ ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()));

					customerDepHoldService.notifyForHoldAndReleaseResources(scServiceDetail, task, serviceId,
							customerEmail, customerName, customerUrl, toAddresses, ccAddresses, notificationTrigger);

				*/} else {
					logger.info("Inside last else {} for service Id {}",
							ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()),
							serviceId);

					// day_logger updation notification trigger
					saveNotificationStatusDailyTrigger(notificationTrigger,
							ChronoUnit.DAYS.between(task.getDuedate().toLocalDateTime(), LocalDateTime.now()));

					logger.info("Inside notifyOverDueReminderCustomer for service Id {}", serviceId);
					notificationService.notifyOverDueReminderCustomer(customerEmail, task.getScOrderId(), customerName,
							task.getServiceCode(), customerUrl, task.getMstTaskDef().getName(), task.getOrderCode());
				}
			} else {
				logger.error("Reminder notification not triggered for taskDefKey{} serviceId={}", taskKey, serviceId);
			}
		}
		}catch(Exception ee) {
			logger.error("customr ramainder error {}",ee);
		}
	}

	private void saveNotificationStatusDailyTrigger(NotificationTrigger notificationTrigger, Long days) {
		notificationTrigger.setDayLogger("Inside last else notifyOverDueReminderCustomer daily trigger for #days :"+days);
		notificationTrigger.setStatusTrigger("Daily Trigger OLD queue");
		notificationTriggerRepository.save(notificationTrigger);
	}

	private NotificationTrigger notificationTriggerEntry(Integer serviceId, String serviceCodeProcess, String taskDefKey, String taskKey, String logger) {
		NotificationTrigger notificationTrigger = new NotificationTrigger();
		notificationTrigger.setServiceId(serviceId);
		notificationTrigger.setServiceCode(serviceCodeProcess);
		notificationTrigger.setReminderKey(taskDefKey);
		notificationTrigger.setTaskDefKey(taskKey);
		notificationTrigger.setLogger(logger);
		notificationTrigger.setTriggerTime(new Timestamp(new Date().getTime()));
		return notificationTrigger;
	}

	private String getSiteDemarcDetails(Map<String, Object> taskDataMap) {
		String siteDemarc="";
		String buildingName = StringUtils.trimToEmpty((String) taskDataMap.get("demarcationBuildingName"));
		if(StringUtils.isNotBlank(buildingName))siteDemarc=siteDemarc+"Building Name: "+buildingName+",";
		String wing = StringUtils.trimToEmpty((String) taskDataMap.get("demarcationWing"));
		if(StringUtils.isNotBlank(wing))siteDemarc=siteDemarc+"Wing: "+wing+",";
		String floor = StringUtils.trimToEmpty((String) taskDataMap.get("demarcationFloor"));
		if(StringUtils.isNotBlank(floor))siteDemarc=siteDemarc+"Floor: "+floor+",";
		String room = StringUtils.trimToEmpty((String) taskDataMap.get("demarcationRoom"));
		if(StringUtils.isNotBlank(room))siteDemarc=siteDemarc+"Room: "+room+",";
		return siteDemarc;
	}
	
	private List<String> getKickOffMailDetails(Map<String, Object> taskDataMap, List<String> emailList) {
		logger.info("getKickOffMailDetails invoked");
		emailList=getCommonMailDetails(taskDataMap, emailList);
		String pocSalesEmail = StringUtils
				.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SALES_EMAIL));
		if (pocSalesEmail != null && !pocSalesEmail.isEmpty()) {
			logger.info("pocSalesEmail exists {} ", pocSalesEmail);
			emailList.add(pocSalesEmail);
		}
		return emailList;
	}
	
	private List<String> getInternalMOMMailDetails(Map<String, Object> taskDataMap, List<String> emailList) {
		logger.info("getInternalMOMDetails invoked");
		String pocCmipEmail = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_CMIP_EMAIL));
		String pocScmMlEmail = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SCM_ML_EMAIL));
		String pocSatSocEmail = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SATSOC_EMAIL));
		String pocScmMgmtEmail = StringUtils
				.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SCM_MGMT_EMAIL));
		String pocCmipEmailSecondary = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_CMIP_EMAIL_SECONDARY));
		String pocSatSocEmailSecondary = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SATSOC_EMAIL_SECONDARY));
		String pocScmMgmtEmailSecondary = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SCM_MGMT_EMAIL_SECONDARY));
		String pocScmMlEmailSecondary = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SCM_ML_EMAIL_SECONDARY));

		logger.info("notifyInternalCallMom mail triggered ");
		emailList=getCommonMailDetails(taskDataMap, emailList);
		if (pocCmipEmail != null && !pocCmipEmail.isEmpty()) {
			logger.info("pocCmipEmail is not null {} ", pocCmipEmail);
			emailList.add(pocCmipEmail);
		}
		if (pocScmMlEmail != null && !pocScmMlEmail.isEmpty()) {
			logger.info("pocScmMlEmail exists {} ", pocScmMlEmail);
			emailList.add(pocScmMlEmail);
		}
		
		if (pocSatSocEmail != null && !pocSatSocEmail.isEmpty()) {
			logger.info("pocSatSocEmail exists {} ", pocSatSocEmail);
			emailList.add(pocSatSocEmail);
		}
		if (pocScmMgmtEmail != null && !pocScmMgmtEmail.isEmpty()) {
			logger.info("pocScmMgmtEmail exists {} ", pocScmMgmtEmail);
			emailList.add(pocScmMgmtEmail);
		}
		if (pocCmipEmailSecondary != null && !pocCmipEmailSecondary.isEmpty()) {
			logger.info("pocCmipEmailSecondary exists {} ", pocCmipEmailSecondary);
			emailList.add(pocCmipEmailSecondary);
		}
		if (pocSatSocEmailSecondary != null && !pocSatSocEmailSecondary.isEmpty()) {
			logger.info("pocSatSocEmailSecondary exists {} ", pocSatSocEmailSecondary);
			emailList.add(pocSatSocEmailSecondary);
		}
		if (pocScmMgmtEmailSecondary != null && !pocScmMgmtEmailSecondary.isEmpty()) {
			logger.info("pocScmMgmtEmailSecondary exists {} ", pocScmMgmtEmailSecondary);
			emailList.add(pocScmMgmtEmailSecondary);
		}
		if (pocScmMlEmailSecondary != null && !pocScmMlEmailSecondary.isEmpty()) {
			logger.info("pocScmMlEmailSecondary exists {} ", pocScmMlEmailSecondary);
			emailList.add(pocScmMlEmailSecondary);
		}
		return emailList;
	}

	private List<String> getCommonMailDetails(Map<String, Object> taskDataMap, List<String> emailList) {
		logger.info("getCommonMailDetails invoked");
		String pocSeEmail = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SE_EMAIL));
		String pocSeEmailSecondary = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_SE_EMAIL_SECONDARY));
		String pocPMEmail = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_PM_EMAIL));
		String pocTdaEmail = StringUtils.trimToEmpty((String) taskDataMap.get(MasterDefConstants.POC_TDA_EMAIL));
		if (pocSeEmail != null && !pocSeEmail.isEmpty()) {
			logger.info("pocSeEmail exists {} ", pocSeEmail);
			emailList.add(pocSeEmail);
		}
		if (pocSeEmailSecondary != null && !pocSeEmailSecondary.isEmpty()) {
			logger.info("pocSeEmailSecondary exists {} ", pocSeEmailSecondary);
			emailList.add(pocSeEmailSecondary);
		}
		if (pocPMEmail != null && !pocPMEmail.isEmpty()) {
			logger.info("pocPMEmail exists {} ", pocPMEmail);
			emailList.add(pocPMEmail);
		}
		if (pocTdaEmail != null && !pocTdaEmail.isEmpty()) {
			logger.info("pocTdaEmail exists {} ", pocTdaEmail);
			emailList.add(pocTdaEmail);
		}
		return emailList;
	}

}
