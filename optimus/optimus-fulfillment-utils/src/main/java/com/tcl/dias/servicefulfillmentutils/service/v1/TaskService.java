
package com.tcl.dias.servicefulfillmentutils.service.v1;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.MailNotificationRequest;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.fulfillment.beans.OdrComponentAttributeBean;
import com.tcl.dias.common.fulfillment.beans.OdrComponentBean;
import com.tcl.dias.common.fulfillment.beans.OdrOrderBean;
import com.tcl.dias.common.fulfillment.beans.OdrServiceDetailBean;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Activity;
import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.Appointment;
import com.tcl.dias.servicefulfillment.entity.entities.Attachment;
import com.tcl.dias.servicefulfillment.entity.entities.CancellationRequest;
import com.tcl.dias.servicefulfillment.entity.entities.CpeMaterialRequestDetails;
import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScContractInfo;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrderAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceLogs;
import com.tcl.dias.servicefulfillment.entity.entities.ServiceStatusDetails;
import com.tcl.dias.servicefulfillment.entity.entities.Stage;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SapPoDtlOptimus;
import com.tcl.dias.servicefulfillment.entity.entities.Stg0SfdcVendorC;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.entities.TaskData;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.entities.TaskRemark;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityRepository;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentDocumentsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.AttachmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.CancellationRequestRepository;
import com.tcl.dias.servicefulfillment.entity.repository.CpeMaterialRequestDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.FieldEngineerRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstAppointmentDocumentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstAppointmentSlotsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceLogsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ServiceStatusDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StageRepository;
import com.tcl.dias.servicefulfillment.entity.repository.Stg0SapPoDtlOptimusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StgSfdcVendorRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRemarkRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillment.entity.repository.VendorsRepository;
import com.tcl.dias.servicefulfillment.specification.SfdcVendorSpecification;
import com.tcl.dias.servicefulfillment.specification.TaskLogSpecification;
import com.tcl.dias.servicefulfillment.specification.TaskSpecification;
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.abstractservice.ITaskDataService;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AssignPM;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssignedGroupingBean;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AssigneeResponse;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.CableMaterialRequest;
import com.tcl.dias.servicefulfillmentutils.beans.CancellationRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.CancellationResponse;
import com.tcl.dias.servicefulfillmentutils.beans.CimHoldRequest;
import com.tcl.dias.servicefulfillmentutils.beans.ConfirmDeferredDeliveryBean;
import com.tcl.dias.servicefulfillmentutils.beans.DocumentIds;
import com.tcl.dias.servicefulfillmentutils.beans.PNRDetails;
import com.tcl.dias.servicefulfillmentutils.beans.PNRScenarios;
import com.tcl.dias.servicefulfillmentutils.beans.PNRTaskDetails;
import com.tcl.dias.servicefulfillmentutils.beans.ProcessTaskLogBean;
import com.tcl.dias.servicefulfillmentutils.beans.SalesNegotiationBean;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceDetailRequest;
import com.tcl.dias.servicefulfillmentutils.beans.ServiceLogBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskAssignmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDataBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskGroupBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskRemarksJeopardyBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskRemarksRequest;
import com.tcl.dias.servicefulfillmentutils.beans.TaskRequest;
import com.tcl.dias.servicefulfillmentutils.beans.TaskResponse;
import com.tcl.dias.servicefulfillmentutils.beans.TerminationRequestBean;
import com.tcl.dias.servicefulfillmentutils.beans.sap.ParentPoBean;
import com.tcl.dias.servicefulfillmentutils.constants.AppointmentConstants;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GroupByConstants;
import com.tcl.dias.servicefulfillmentutils.constants.IpcConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.constants.SalesNegotiationConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.helper.UploadAttachmentComponent;
import com.tcl.dias.servicefulfillmentutils.utils.FulfillmentUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;


/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional(readOnly = true)
public class TaskService extends ServiceFulfillmentBaseService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
	
	private static final String NOTIFY_DEFERRED_DELIVERY_SUBJECT = "Deferred delivery initiated for service ";
	private static final String NOTIFY_SERVICE_UNHOLD_FROM_SALES_TEAM_SUBJECT = "Service status has changed from Hold to Inprogress";
	private static final String NOTIFY_SERVICE_UNHOLD_FROM_SALES_TEAM_MAIL_CONTENT = "Service status has changed from  Hold to Inprogress";
	private static final String localItContactEmailId = "localItContactEmailId";
	

	@Autowired
	ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	TaskAssignmentRepository taskAssignmentRepository;

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ITaskDataService itaskDataService;

	@Value("${rabbitmq.user.details.queue}")
	String userDetailsQueue;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	AttachmentService attachmentService;

	@Autowired
	UploadAttachmentComponent uploadAttachmentComponent;

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	AppointmentDocumentsRepository appointmentDocumentsRepository;

	@Autowired
	FieldEngineerRepository fieldEngineerRepository;

	@Autowired
	VendorsRepository vendorsRepository;

	@Autowired
	MstAppointmentSlotsRepository mstAppointmentSlotsRepository;

	@Autowired
	MstAppointmentDocumentRepository mstAppointmentDocumentRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ComponentAndAttributeService componentAndAttributeService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	TaskPlanRepository taskPlanRepository;

	@Autowired
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	TATService tatService;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	@Value("${app.host}")
	String appHost;


	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Autowired
	FulfillmentUtils fulfillmentUtils;

	@Autowired
	StgSfdcVendorRepository stgSfdcVendorRepository;

	@Autowired
	TaskRemarkRepository taskRemarkRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	FlowableBaseService flowableBaseService;

	@Value("${rabbitmq.sa.service.terminate.queue}")
	private String saServiceTerminateQueue;
	
	@Value("${rabbitmq.sa.service.fti.netpref.queue}")
	String saServiceNetpRefQueue;
	
	
	@Autowired
	Stg0SapPoDtlOptimusRepository stg0SapPoDtlOptimusRepository;
	
	@Autowired
	ServiceStatusDetailsRepository serviceStatusDetailsRepository;

	@Autowired
	SapService sapService;

	@Value("${rabbitmq.o2c.si.terminate.queue}")
	private String siTerminateDetail;

	@Value("${notification.mail.queue}")
	String notificationMailQueue;

	@Autowired
	private ServiceLogsRepository serviceLogsRepository;

	@Autowired
	CpeMaterialRequestDetailsRepository cpeMaterialRequestDetailsRepository;

	@Autowired
	private CancellationRequestRepository cancellationRequestRepository;

	@Value("${rabbitmq.macd.detail.commissioned}")
	private String macdDetailCommissioned;
	
	@Autowired
	private StageRepository stageRepository;
	
	@Autowired
	private StagePlanRepository stagePlanRepository;

	
	@Autowired
	private ProcessPlanRepository processPlanRepository;
	
	@Autowired
	private ActivityRepository activityRepository;
	
	@Autowired
	private ActivityPlanRepository activityPlanRepository;
	
	@Autowired
	private TaskRepository taskRepository;
	
	@Value("${release.resource.cycle}")
	String resourceReleaseCycle;
	
	
	@Autowired
	private AttachmentRepository attachmentRepository;

	@Autowired
	RfService rfService;

	/**
	 * This method is used to get the task count
	 *
	 * @param groupName
	 * @return
	 * @author vivek
	 * @param userName
	 * @param type
	 */
	public List<AssignedGroupBean> getTaskCount(String groupName, String user, String type,String customerName) {
		List<AssignedGroupBean> assignedGroupBeans = new ArrayList<>();

		String name = groupName;
		String userName=user;
		if(type!=null && type.equalsIgnoreCase("user")) {
			userName=userInfoUtils.getUserInformation().getUserId();
		}
		if(groupName==null || groupName.isEmpty())name = userName;

		List<Task> tasks = taskRepository
				.findAll(TaskSpecification.getTaskFilter(groupName, getStatus(), userName, null,null,null,null,null,null,null,null,null,customerName,null,null,null, null,false,null,null,null,null,null,false,null));
		getTaskCountDetails(tasks, assignedGroupBeans, name);

		return assignedGroupBeans;
	}


	/**
	 * Method to get task details based on filter
	 * @param request
	 * @return
	 */
	public List<AssignedGroupingBean> getTaskDetailsBasedOnfilter(TaskRequest request) {
		
		LOGGER.info("Filter request received as :{} ",request.toString());
		
		List<AssignedGroupingBean> assignedGroupingBeans = new ArrayList<>();
		List<Task> tasks = new ArrayList<Task>();

		String userName = request.getUserName();

		if (request.getType() != null && request.getType().equalsIgnoreCase("user")) {
			userName = userInfoUtils.getUserInformation().getUserId();
		}
		Map<String, List<Task>> groupBasedAssignmnetTask = null;
		tasks.addAll(taskRepository.findAll(TaskSpecification.getTaskFilter(request.getGroupName(), request.getStatus(),
				userName, request.getServiceId(), request.getOrderType(), request.getOrderCategory(),request.getOrderSubCategory(),request.getServiceType(),
				request.getTaskName(), request.getServiceCode(), request.getCity(), request.getCountry(),
				request.getCustomerName(), request.getState(), request.getLastMileScenario(), request.getLmProvider(),
				request.getPmName(), request.getIsJeopardyTask(),request.getDistributionCenterName(),request.getDeviceType(),request.getDevicePlatform(),request.getIsIpDownTimeRequired(),request.getIsTxDowntimeReqd(),false,request.getCsmName())));
		
		LOGGER.info("Tasks added first as :{} ",tasks.toString());
		List<String> groupNames=Arrays.asList("CSM","OSP_ADMIN","FIELD_OPS_ADMIN","RF_SD_ADMIN","CUSTOMER","PRODUCT_COMMERCIAL_ADMIN","SALES","SALES_ASSIST", "CIM");

		
		if (request.getServiceId() != null) {
			Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository
					.findById(request.getServiceId());
			if (optionalServiceDetail.isPresent()
					&& (!optionalServiceDetail.get().getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
							|| !optionalServiceDetail.get().getMstStatus().getCode()
									.equalsIgnoreCase(TaskStatusConstants.ACTIVE))) {
				
				LOGGER.info("Admin or sales task need to fetch");

				
				List<Task> adminTask=taskRepository.findAll(TaskSpecification.getTaskFilter(request.getGroupName(),
						request.getStatus(), userName, request.getServiceId(), request.getOrderType(),
						request.getOrderCategory(), request.getOrderSubCategory(), request.getServiceType(),
						Arrays.asList("sales-negotiation", "sales-negotiation-waiting-task",
								"confirm-deferred-delivery","customer-deferred-deliver-waiting","lm-rf-conduct-site-survey-jeopardy",
								"lm-conduct-site-survey-ss-man-jeopardy", "lm-conduct-site-survey-man-jeopardy",
								"lm-cc-conduct-site-survey-jeopardy", "lm-conduct-site-survey-jeopardy","sales-negotiation-jeopardy",
								"lm-complete-ibd-work-jeopardy","lm-apply-prow-jeopardy","lm-complete-osp-work-jeopardy",
								"lm-approve-capex-jeopardy","install-rf-equipment-jeopardy","install-rf-equipment-p2p-jeopardy","customer-hold-negotaition-CIM","sales-assist-ordering","sales-negotiation-SALES"),
						request.getServiceCode(), request.getCity(), request.getCountry(), request.getCustomerName(),
						request.getState(), request.getLastMileScenario(), request.getLmProvider(), request.getPmName(),
						request.getIsJeopardyTask(), request.getDistributionCenterName(), request.getDeviceType(),
						request.getDevicePlatform(), request.getIsIpDownTimeRequired(), request.getIsTxDowntimeReqd(),
						true,request.getCsmName()));
				LOGGER.info("Admin task size:{}",adminTask.size());

				tasks.addAll(adminTask);
				LOGGER.info("Tasks added after admin fetch as :{} ",tasks.toString());
			}

		}
		
		else if (request.getGroupName() != null && groupNames.contains(request.getGroupName())) {
			LOGGER.info("Admin or sales task need to fetch for groupname");
		List<Task> adminTask=	taskRepository.findAll(TaskSpecification.getTaskFilter(request.getGroupName(),
					request.getStatus(), userName, request.getServiceId(), request.getOrderType(),
					request.getOrderCategory(), request.getOrderSubCategory(), request.getServiceType(),
					Arrays.asList("sales-negotiation", "sales-negotiation-waiting-task", "confirm-deferred-delivery",
							"lm-rf-conduct-site-survey-jeopardy", "customer-deferred-deliver-waiting","lm-conduct-site-survey-ss-man-jeopardy",
							"lm-conduct-site-survey-man-jeopardy",
							"lm-cc-conduct-site-survey-jeopardy", "lm-conduct-site-survey-jeopardy","sales-negotiation-jeopardy",
							"lm-complete-ibd-work-jeopardy","lm-apply-prow-jeopardy","lm-complete-osp-work-jeopardy",
							"lm-approve-capex-jeopardy","install-rf-equipment-jeopardy","install-rf-equipment-p2p-jeopardy","customer-hold-negotaition-CIM","sales-assist-ordering","sales-negotiation-SALES"), request.getServiceCode(),
					request.getCity(), request.getCountry(), request.getCustomerName(), request.getState(),
					request.getLastMileScenario(), request.getLmProvider(), request.getPmName(),
					request.getIsJeopardyTask(), request.getDistributionCenterName(), request.getDeviceType(),
					request.getDevicePlatform(), request.getIsIpDownTimeRequired(), request.getIsTxDowntimeReqd(),
					true,request.getCsmName()));
		
		LOGGER.info("Admin task size:{}",adminTask.size());


			tasks.addAll(adminTask);
			LOGGER.info("Tasks added after admin fetch using groupname as :{} ",tasks.toString());
		}
 
		
		if (request.getGroupBy() != null && request.getGroupBy().equalsIgnoreCase(GroupByConstants.GROUPBYSTATUS)) {

			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstStatus().getCode()));
		} else {
			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstTaskDef().getName()));

		}
		
		LOGGER.info("groupBasedAssignmnetTask after groupby :{} ",Objects.toString(groupBasedAssignmnetTask));
		
		AssignedGroupingBean assignedGroupingBean = new AssignedGroupingBean();
		assignedGroupingBean.setGroupName(request.getGroupName());
		groupBasedAssignmnetTask.forEach((taskName, taskList) -> {
			List<TaskGroupBean> taskGroupList = new ArrayList<>();
			TaskGroupBean taskGroup = new TaskGroupBean();
			taskGroup.setName(taskName);
			taskList.forEach(task -> {
				TaskBean taskBean = new TaskBean();
				setTaskDetails(taskBean, task);
				if(!taskGroup.getTaskBeans().contains(taskBean)) {
				taskGroup.getTaskBeans().add(taskBean);
				}
				taskGroupList.add(taskGroup);

			});

			taskGroup.getTaskBeans().sort(Comparator.comparing(TaskBean::getCrfsDate));
			assignedGroupingBean.getTaskGroup().add(taskGroup);
		});
		assignedGroupingBeans.add(assignedGroupingBean);

		return assignedGroupingBeans;
	}

	/**
	 * @param serviceCode 
	 * @param orderCode 
	 * @param serviceId
	 * @return
	 */
	public TaskResponse delayTaskDetails(String orderCode, String serviceCode, Integer serviceId) {
		TaskResponse taskResponse = new TaskResponse();

		List<Task> tasks = taskRepository.findByServiceId(serviceId);

		if (!tasks.isEmpty()) {
			tasks.forEach(task -> {
				try {
				TaskPlan plan = task.getTaskPlans().stream().findFirst().orElse(null);
				if (plan != null) {
					if (plan.getMstStatus()!=null && plan.getMstStatus().getCode() !=null && plan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
						if (plan.getMstTaskDef().getIsCustomerTask() != null

								&& plan.getActualEndTime() != null && plan.getActualStartTime() != null
								&& plan.getActualEndTime().after(plan.getActualStartTime())) {
							int delay = tatService.calculateDelay(task.getMstTaskDef().getOwnerGroup(),
									plan.getActualStartTime(), plan.getActualEndTime());
							LOGGER.info("task {} delay {} Tat {}", plan.getMstTaskDef().getKey(), delay,
									task.getMstTaskDef().getTat());
							if (task.getMstTaskDef().getTat()!=null &&( delay - task.getMstTaskDef().getTat()) > 480) {
								TaskBean bean = constructTaskDetails(task, plan);
								bean.setDelayDays(delay / 480);
								taskResponse.getDelayTasks().add(bean);
							}

						}
					}

					else {
						Timestamp timestamp = new Timestamp(new Date().getTime());
						if (plan.getActualStartTime() != null && plan.getActualStartTime().before(timestamp)) {
							int delay = tatService.calculateDelay(task.getMstTaskDef().getOwnerGroup(), plan.getActualStartTime(),timestamp
									);
							LOGGER.info("task {} delay {} Tat {}",plan.getMstTaskDef().getKey(),delay,task.getMstTaskDef().getTat());
							if (task.getMstTaskDef().getTat()!=null  && (delay-task.getMstTaskDef().getTat())  > 480) {
								TaskBean bean = constructTaskDetails(task, plan);
								bean.setDelayDays(delay / 480);
								taskResponse.getDelayTasks().add(bean);
							}

						}
					}
				}
				}catch(Exception ee) {
					LOGGER.error("delayTaskDetails-error {}",ee);
				}

			});

		}
		return taskResponse;

	}

	private TaskBean constructTaskDetails(Task task, TaskPlan plan) {
		TaskBean taskBean = new TaskBean();
		taskBean.setTaskId(task.getId());
		taskBean.setTaskDefKey(task.getMstTaskDef().getKey());
		taskBean.setActualEndTime(plan.getActualEndTime());
		taskBean.setIsCustomerTask(task.getMstTaskDef().getIsCustomerTask()!=null?task.getMstTaskDef().getIsCustomerTask():"N");
		taskBean.setName(task.getMstTaskDef().getName());
		taskBean.setActualStartTime(plan.getActualStartTime());
		taskBean.setPlannedStartTime(plan.getPlannedStartTime());
		taskBean.setPlannedEndTime(plan.getPlannedEndTime());
		taskBean.setEstimatedEndTime(plan.getEstimatedEndTime());
		taskBean.setEstimatedStartTime(plan.getEstimatedStartTime());

		return taskBean;

	}

	/**s
	 * This method is used to get the latest activity of group
	 *
	 * @param groupName
	 * @return
	 * @author vivek
	 * @param taskId
	 * @param serviceId
	 * @param userName
	 * @param type
	 */
	public List<ProcessTaskLogBean> getLatestActivity(String groupName, String orderCode, Integer serviceId,
			Integer taskId, String user, String type,String wfTaskId) {
		List<ProcessTaskLogBean> taskLogBeans = new ArrayList<>();
		String userName = user;
		if (type != null && type.equalsIgnoreCase("user")) {
			userName = userInfoUtils.getUserInformation().getUserId();
		}

		int size =15;
		if (serviceId != null) size =500;
		Page<ProcessTaskLog> processTaskLogs = processTaskLogRepository
				.findAll(TaskLogSpecification.getTaskLogFilter(taskId, groupName, orderCode, serviceId, userName,wfTaskId),PageRequest.of(0, size));

		processTaskLogs.forEach(taskLogs -> {
			if (taskLogs != null) {

				taskLogBeans.add(setProcessLogs(taskLogs));
			}

		});

		return taskLogBeans;
	}

	/**
	 * used to get user details
	 *
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 * @throws IllegalArgumentException
	 */
	public UserGroupBeans getUserDetails(String groupName) throws TclCommonException, IllegalArgumentException {
		Objects.requireNonNull(groupName, "Groupname cannot be null");
		String response = (String) mqUtils.sendAndReceive(userDetailsQueue, groupName);
		LOGGER.info("Response from used details{}", response);
		return Utils.convertJsonToObject(response, UserGroupBeans.class);
	}

	/**
	 * @param request
	 * @return
	 * @author vivek
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public AssigneeResponse updateAssignee(AssigneeRequest request) throws TclCommonException {
		AssigneeResponse assigneeResponse = new AssigneeResponse();
		Task task = getTaskByIdAndWfTaskId(request.getTaskId(), request.getWfTaskId());
		if (task != null) {
			if (task.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
				throw new TclCommonRuntimeException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.TASK_CLOSED,
						ResponseResource.R_CODE_ERROR);

			}
			task.setClaimTime(new Timestamp(new Date().getTime()));
			updateAssignmentDetails(task, request);
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			task.setCompletedTime(null);
			constructAssigneeResponse(task, request, assigneeResponse);
			taskRepository.save(task);

		}

		return assigneeResponse;
	}

	private void updateAssignmentDetails(Task task, AssigneeRequest request)  {
		TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst().orElse(null);
		if (taskAssignment != null) {

			if(request.getType()!=null && request.getType().equalsIgnoreCase("CLAIM")) {
				if(task.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)){
					throw new TclCommonRuntimeException(com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANNOT_CLAIM, ResponseResource.R_CODE_ERROR);

				}
			}
			taskAssignment.setUserName(request.getAssigneeNameTo());
			taskAssignmentRepository.save(taskAssignment);
			processTaskLogRepository.save(createProcessTaskLog(task, request,isClaimed(request)?"Claimed":"Assigned"));



			try {
				String member = request.getAssigneeNameTo();
				if (request.getAssigneeNameFrom() != null && request.getAssigneeNameTo() != null
						&& !request.getAssigneeNameFrom().equalsIgnoreCase(request.getAssigneeNameTo())) {
					try {
						List<ScContractInfo> scContractInfos = scContractInfoRepository
								.findByScOrder_id(task.getScOrderId());
						ScContractInfo scContractInfo = scContractInfos.stream().findFirst().orElse(null);
						List<String> ccAddresses = new ArrayList<>();
						ccAddresses.add(customerSupportEmail);
						if (scContractInfo != null) {
							ccAddresses.add(scContractInfo.getAccountManagerEmail());
						}
						
						
						String groupName = task.getMstTaskDef().getKey() + "_" + task.getMstTaskDef().getOwnerGroup();

						LOGGER.info("Notify Taks leads for this task Id: {}, and service code is: {}", task.getId(),
								task.getServiceCode());
						List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroupIn(Arrays.asList(groupName, "CIM"));
						
						
						if (mstTaskRegionList != null && !mstTaskRegionList.isEmpty()) {
							ccAddresses.addAll(mstTaskRegionList.stream().filter(re -> re.getEmail() != null)
									.map(region -> region.getEmail()).collect(Collectors.toList()));
						}
						notificationService.notifyTaskAssignment(request.getAssigneeNameTo(), member,
								task.getServiceCode(), task.getMstTaskDef().getName(),
								Utils.converTimeToString(task.getDuedate()), "", false, ccAddresses);
					} catch (Exception e) {
						LOGGER.error("Error in sending customer Mail for task assignment {} ", e);
					}

				}
			} catch (Exception e) {
				LOGGER.error("error in sending notification{}", e);
			}
		}
	}


	private ProcessTaskLog createProcessTaskLog(Task task, AssigneeRequest assigneeRequest, String action) {

		ProcessTaskLog processTaskLog = new ProcessTaskLog();
		processTaskLog.setActive("Y");
		processTaskLog.setCreatedTime(new Timestamp(new Date().getTime()));
		processTaskLog.setTask(task);
		processTaskLog.setAction(action);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		else {
			processTaskLog.setActionFrom(assigneeRequest.getAssigneeNameFrom());

		}
		processTaskLog.setActionTo(assigneeRequest.getAssigneeNameTo());
		processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		processTaskLog.setGroupTo(assigneeRequest.getAssigneeNameTo());
		processTaskLog.setDescrption(assigneeRequest.getDescription());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		return processTaskLog;
	}

	private boolean isClaimed(AssigneeRequest assigneeRequest) {

		if (assigneeRequest.getAssigneeNameFrom() != null && assigneeRequest.getAssigneeNameTo() != null
				&& assigneeRequest.getAssigneeNameFrom().equalsIgnoreCase(assigneeRequest.getAssigneeNameTo())) {
			return true;
		}
		return false;
	}

	/**
	 * this is used to set the process log
	 *
	 * @param taskLogs
	 * @return
	 * @author vivek
	 */
	public ProcessTaskLogBean setProcessLogs(ProcessTaskLog taskLogs) {

		ProcessTaskLogBean bean = new ProcessTaskLogBean();
		bean.setAction(taskLogs.getAction());
		bean.setActionFrom(taskLogs.getActionFrom());
		bean.setActionTo(taskLogs.getActionTo());
		bean.setCreatedTime(taskLogs.getCreatedTime());
		bean.setDescription(taskLogs.getDescrption());
		bean.setServiceId(taskLogs.getServiceId());
		bean.setServiceCode(taskLogs.getServiceCode());
		bean.setScOrderId(taskLogs.getScOrderId());
		bean.setOrderCode(taskLogs.getOrderCode());
		bean.setGroupFrom(taskLogs.getGroupFrom());
		bean.setGroupTo(taskLogs.getGroupTo());
		bean.setErrorMessage(taskLogs.getErroMessage());
		bean.setTask(constructTaskLogsBean(taskLogs.getTask()));
		bean.setCategory(taskLogs.getCategory());
		bean.setSubCategory(taskLogs.getSubCategory());

		return bean;
	}

	private TaskBean constructTaskLogsBean(Task task) {
		TaskBean taskBean = new TaskBean();
		if (task != null) {
			setTaskLogsDetails(taskBean, task);
		}
		return taskBean;
	}

	private void setTaskLogsDetails(TaskBean taskBean, Task task) {
		if (task != null && taskBean != null) {

			taskBean.setTaskId(taskBean.getTaskId());
			taskBean.setTaskId(task.getId());
			taskBean.setCatagory(task.getCatagory());
			taskBean.setOrderCategory(task.getOrderCategory());
			taskBean.setClaimTime(task.getClaimTime());
			taskBean.setCompletedTime(task.getCompletedTime());
			taskBean.setCreatedTime(task.getCreatedTime());
			taskBean.setDuedate(task.getDuedate());
			taskBean.setOrderCode(task.getOrderCode());
			taskBean.setServiceId(task.getServiceId());
			taskBean.setServiceCode(task.getServiceCode());
			taskBean.setStatus(task.getMstStatus().getCode());
			taskBean.setUpdatedTime(task.getUpdatedTime());
			taskBean.setWfProcessInstId(task.getWfProcessInstId());
			taskBean.setWfTaskId(task.getWfTaskId());
			taskBean.setCity(task.getCity());
			taskBean.setScOrderId(task.getScOrderId());
			taskBean.setOrderType(task.getOrderType());
			taskBean.setServiceType(task.getServiceType());
			if (task.getMstTaskDef() != null) {
				taskBean.setButtonLabel(task.getMstTaskDef().getButtonLabel());
				taskBean.setDescription(task.getMstTaskDef().getDescription());
				taskBean.setTitle(task.getMstTaskDef().getTitle());
				taskBean.setAssignedGroup(task.getMstTaskDef().getAssignedGroup());
				taskBean.setTaskDefKey(task.getMstTaskDef().getKey());
				taskBean.setIsManualTask(task.getMstTaskDef().getIsManualTask());
				taskBean.setName(task.getMstTaskDef().getName());
				taskBean.setIsCustomerTask(task.getMstTaskDef().getIsCustomerTask());
				taskBean.setOwnerGroup(task.getMstTaskDef().getOwnerGroup());
			}
			if(task.getScServiceDetail() != null && task.getScServiceDetail().getIsAmended()!=null && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getIsAmended())){
				taskBean.setIsOrderAmendent(task.getScServiceDetail().getIsAmended());
			}
		}
	}


	/**
	 * This method is used to map the task details bean
	 *
	 * @param taskBean
	 * @param task
	 * @author vivek
	 */
	private void setTaskDetails(TaskBean taskBean, Task task) {
		if (task != null && taskBean != null) {
			taskBean.setTaskId(taskBean.getTaskId());
			taskBean.setTaskId(task.getId());
			taskBean.setCatagory(task.getCatagory());
			taskBean.setOrderCategory(task.getOrderCategory());
			taskBean.setOrderSubCategory(task.getOrderSubCategory());
			taskBean.setClaimTime(task.getClaimTime());
			taskBean.setCompletedTime(task.getCompletedTime());
			taskBean.setCreatedTime(task.getCreatedTime());
			taskBean.setDuedate(task.getDuedate());
			taskBean.setOrderCode(task.getOrderCode());
			taskBean.setServiceId(task.getServiceId());
			taskBean.setServiceCode(task.getServiceCode());
			taskBean.setStatus(task.getMstStatus().getCode());
			taskBean.setUpdatedTime(task.getUpdatedTime());
			taskBean.setWfProcessInstId(task.getWfProcessInstId());
			taskBean.setWfTaskId(task.getWfTaskId());
			taskBean.setCity(task.getCity());
			taskBean.setScOrderId(task.getScOrderId());
			taskBean.setOrderType(task.getOrderType());
			taskBean.setServiceType(task.getServiceType());
			taskBean.setCity(task.getCity());
			taskBean.setCustomerName(task.getCustomerName());
			String lmScenario = StringUtils.trimToEmpty(task.getLastMileScenario());
			lmScenario = lmScenario.replaceAll("-", "<br>");
			taskBean.setLastMileScenario(lmScenario);
			taskBean.setLmType(task.getLmType());
			taskBean.setLmProvider(task.getLmProvider());
			taskBean.setSiteType(task.getSiteType());
			if(task.getScServiceDetail()!=null) {
				taskBean.setPmName(task.getScServiceDetail().getAssignedPM());
			}
			taskBean.setIsJeopardyTask(task.getIsJeopardyTask());
			ScOrder scOrder = task.getScServiceDetail().getScOrder();
			taskBean.setCustomerLeName(scOrder.getErfCustLeName());
			if (task.getMstTaskDef() != null) {
				taskBean.setButtonLabel(task.getMstTaskDef().getButtonLabel());
				taskBean.setDescription(task.getMstTaskDef().getDescription());
				taskBean.setTitle(task.getMstTaskDef().getTitle());
				taskBean.setAssignedGroup(task.getMstTaskDef().getAssignedGroup());
				taskBean.setTaskDefKey(task.getMstTaskDef().getKey());
				taskBean.setIsManualTask(task.getMstTaskDef().getIsManualTask());
				taskBean.setName(task.getMstTaskDef().getName());
				taskBean.setIsCustomerTask(task.getMstTaskDef().getIsCustomerTask());
				taskBean.setOwnerGroup(task.getMstTaskDef().getOwnerGroup());
			}
			if (task.getTaskAssignments() != null && !task.getTaskAssignments().isEmpty()) {
				setTaskAssignment(task.getTaskAssignments(), taskBean);
			}
			if(task.getScServiceDetail().getIsAmended()!=null && CommonConstants.Y.equalsIgnoreCase(task.getScServiceDetail().getIsAmended())){
				taskBean.setIsOrderAmendent(task.getScServiceDetail().getIsAmended());
			}
			taskBean.setTaskDelayed(isTaskDelayed(task));
			taskBean.setDowntime(task.getDowntime());
			taskBean.setDistributionCenterName(task.getDistributionCenterName());
			taskBean.setIsTxDowntimeReqd(task.getIsTxDowntimeReqd());
			taskBean.setIsIpDownTimeRequired(task.getIsIpDownTimeRequired());
			taskBean.setDeviceType(task.getDeviceType());
			taskBean.setDevicePlatform(task.getDevicePlatform());
			if(task.getScServiceDetail().getServiceCommissionedDate()!=null)taskBean.setCrfsDate(task.getScServiceDetail().getServiceCommissionedDate());
			taskBean.setTaskAge(ChronoUnit.DAYS.between(task.getCreatedTime().toLocalDateTime(),LocalDateTime.now()));
			taskBean.setTaskClosureCategory(task.getTaskClosureCategory());
		}
	}

	private boolean isTaskDelayed(Task task) {

		try {

			if (task.getDuedate()!=null && new Timestamp(new Date().getTime()).toLocalDateTime()
					.isAfter(task.getDuedate().toLocalDateTime())) {
				return true;

			}
		} catch (Exception e) {
			LOGGER.error("error in task delay{}", e);
		}

		return false;
	}

	/**
	 * @param taskAssignments
	 * @param taskBean
	 * @author vivek
	 */
	private void setTaskAssignment(Set<TaskAssignment> taskAssignments, TaskBean taskBean) {

		if (taskAssignments != null && !taskAssignments.isEmpty()) {
			taskAssignments.forEach(assignment -> {
				TaskAssignmentBean taskAssignmentBean = new TaskAssignmentBean();
				taskAssignmentBean.setGroup(assignment.getGroupName());
				taskAssignmentBean.setOwner(assignment.getOwner());
				taskAssignmentBean.setUserName(assignment.getUserName());
				taskBean.setAssignee(assignment.getUserName());
				taskBean.getTaskAssignments().add(taskAssignmentBean);
			});

		}

	}

	/**
	 * This method is used to set the task count
	 *
	 * @param taskAssignments
	 * @param assignedGroupBeans
	 * @param name
	 * @author vivek
	 */
	private void getTaskCountDetails(List<Task> tasks, List<AssignedGroupBean> assignedGroupBeans, String name) {
		if (name != null && !tasks.isEmpty()) {
			Map<String, Long> requirementCountMap = tasks.stream()
					.collect(Collectors.groupingBy(as -> as.getMstStatus().getCode(), Collectors.counting()));

			assigneCountDetails(requirementCountMap, assignedGroupBeans, name);
		}
	}

	/**
	 * This method is used to assign the task count details
	 *
	 * @param requirementCountMap
	 * @param assignedGroupBeans
	 * @param groupName
	 * @author vivek
	 */
	private void assigneCountDetails(Map<String, Long> requirementCountMap, List<AssignedGroupBean> assignedGroupBeans,
			String groupName) {

		AssignedGroupBean assignedGroupBean = new AssignedGroupBean();
		assignedGroupBean.setGroupName(groupName);
		if (requirementCountMap.containsKey(TaskStatusConstants.CLOSED_STATUS)) {
			assignedGroupBean.setClosedCount(requirementCountMap.get(TaskStatusConstants.CLOSED_STATUS));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.OPENED)) {
			assignedGroupBean.setOpenCount(requirementCountMap.get(TaskStatusConstants.OPENED));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.HOLD)) {
			assignedGroupBean.setHoldCount(requirementCountMap.get(TaskStatusConstants.HOLD));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.ASSIGNED)) {
			assignedGroupBean.setAssignetCount(requirementCountMap.get(TaskStatusConstants.ASSIGNED));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.PENDING)) {
			assignedGroupBean.setPeningCount(requirementCountMap.get(TaskStatusConstants.PENDING));
		}

		if (requirementCountMap.containsKey(TaskStatusConstants.REOPEN)) {
			assignedGroupBean.setReopenCount(requirementCountMap.get(TaskStatusConstants.REOPEN));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.INPROGRESS)) {
			assignedGroupBean.setInprogressCount(requirementCountMap.get(TaskStatusConstants.INPROGRESS));
		}
		assignedGroupBeans.add(assignedGroupBean);
	}

	/**
	 * This method is used to get the task status
	 *
	 * @return
	 * @author vivek
	 */
	private List<String> getStatus() {

		List<String> status = new ArrayList<>();
		status.add(TaskStatusConstants.CLOSED_STATUS);
		status.add(TaskStatusConstants.ASSIGNED);
		status.add(TaskStatusConstants.HOLD);
		status.add(TaskStatusConstants.INPROGRESS);
		status.add(TaskStatusConstants.OPENED);
		status.add(TaskStatusConstants.REOPEN);
		status.add(TaskStatusConstants.PENDING);
		return status;
	}

	/**
	 * This method is used to get the task based on id in parameter
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 * @author diksha garg
	 */
	public TaskBean getTaskBasedOnTaskId(Integer taskId, String wfTaskId) throws TclCommonException {

		if (taskId != null && wfTaskId != null) {
			Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
			if (task != null) {

				Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository
						.findById(task.getServiceId());
				List<String> includedTask = Arrays.asList("lm-rf-conduct-site-survey-jeopardy",
						"lm-conduct-site-survey-ss-man-jeopardy", "lm-conduct-site-survey-man-jeopardy",
						"sales-negotiation", "sales-negotiation-waiting-task", "confirm-deferred-delivery",
						"lm-cc-conduct-site-survey-jeopardy", "lm-conduct-site-survey-jeopardy",
						"customer-hold-negotaition-CIM", "sales-negotiation-SALES", "sales-assist-ordering");

				if (optionalServiceDetails.isPresent()
						

						&& !optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.HOLD)
						|| ((optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.HOLD)
								|| optionalServiceDetails.get().getMstStatus().getCode()
										.equalsIgnoreCase(TaskStatusConstants.DEFERRED_DELIVERY))
								&& (includedTask.contains(task.getMstTaskDef().getKey()))
								&& (!optionalServiceDetails.get().getMstStatus().getCode()
										.equalsIgnoreCase(TaskStatusConstants.CANCELLED)
										|| (optionalServiceDetails.get().getMstStatus().getCode()
												.equalsIgnoreCase(TaskStatusConstants.CANCELLED)
												&& optionalServiceDetails.get().getCancellationFlowTriggered() != null
												&& "Yes".equalsIgnoreCase(optionalServiceDetails.get()
														.getCancellationFlowTriggered()))))) {
					TaskBean taskbean = new TaskBean();
					
					if(optionalServiceDetails.get().getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.JEOPARDY_INITIATED)) {
						taskbean.setJeopardyService(true);
					}
					if (optionalServiceDetails.get().getPriority() != null) {
						taskbean.setPriority(optionalServiceDetails.get().getPriority());
					} else {
						taskbean.setPriority(1F);
					}
					taskbean.setServiceDeliveryDate(optionalServiceDetails.get().getCommittedDeliveryDate());
					setTaskDetails(taskbean, task);					
					setTaskData(task, taskbean);
					Map<String, Object> commonData = itaskDataService.getTaskData(task);

					if (task.getMstTaskDef().getKey().equalsIgnoreCase("izopc-validate-e2e-service-testing-results")) {
						List<Attachment> attachmentList=attachmentRepository.findAllAttachmentsByCategory(task.getServiceId(),"E2ERESULT_CLOUD_SY");
						if(attachmentList!=null && !attachmentList.isEmpty()) {
							List<AttachmentIdBean> attachmentBeanList= new ArrayList<>();
							for(Attachment attachment:attachmentList) {
								AttachmentIdBean attachmentIdBean =new AttachmentIdBean();
								attachmentIdBean.setAttachmentId(attachment.getId());
								attachmentIdBean.setCategory(attachment.getCategory());
								attachmentBeanList.add(attachmentIdBean);
							}
							taskbean.setDocumentIds(attachmentBeanList);
						}
					}
					
					if (task.getMstTaskDef().getKey().equalsIgnoreCase("sales-negotiation")) {
						PNRDetails pnrDetails = getPNRDetails(task.getServiceId());
						commonData.put("pnrDetails", pnrDetails);
					}
					
					if (task.getMstTaskDef().getKey().equalsIgnoreCase("confirm-deferred-delivery")) {
						List<ServiceStatusDetails> serviceStatusDetails = serviceStatusDetailsRepository
								.findByScServiceDetail_idAndStatus(task.getServiceId(),
										TaskStatusConstants.DEFERRED_DELIVERY);
						if (!serviceStatusDetails.isEmpty() && serviceStatusDetails.size() >= 2) {
							commonData.put("deferredDeliverRestrict", true);

						} else {
							commonData.put("deferredDeliverRestrict", false);

						}

					}
				
					
					if (task.getMstTaskDef().getKey().equalsIgnoreCase("wbs-transfer-jeopardy")) {
						List<CpeMaterialRequestDetails> cpeMaterialRequestDetails = cpeMaterialRequestDetailsRepository
								.findByScServiceDetailIdAndServiceCodeAndAvailable(task.getServiceId(), task.getServiceCode(),
										"Y");

						List<CableMaterialRequest> materilaDetails = new ArrayList<>();
						if (!cpeMaterialRequestDetails.isEmpty()) {

							cpeMaterialRequestDetails.forEach(cpeMat -> {

								CableMaterialRequest cableMaterialRequest = new CableMaterialRequest();
								BeanUtils.copyProperties(cpeMat, cableMaterialRequest);
								materilaDetails.add(cableMaterialRequest);

							});

							commonData.put("wbsTransferFailedDetails", materilaDetails);

						}

					}
					if (commonData.get(AppointmentConstants.APPOINTMENTDATE) != null) {
						Timestamp apTimestamp = (Timestamp) commonData.get(AppointmentConstants.APPOINTMENTDATE);
						taskbean.setReschedule(checkIsReschedule(apTimestamp));
					}

					if ("NPL".equalsIgnoreCase(task.getServiceType()) || "NDE".equalsIgnoreCase(task.getServiceType())) {
						Map<String, Object> siteBData = itaskDataService.getTaskData(task, "B");
						taskbean.setSiteBData(siteBData);

					}
					//setRevisitNotAllowed 
					Map<String, String> taskMap = new HashMap<>();
					taskMap.put("confirm-cpe-recovery", "cpe-recovery");
					taskMap.put("confirm-mux-recovery", "mux-recovery");
					taskMap.put("confirm-mast-recovery", "mast-recovery");
					taskMap.put("confirm-rf-recovery", "rf-recovery");
					if(taskMap.containsKey(task.getMstTaskDef().getKey().toLowerCase())){
						List<Appointment> appointment = appointmentRepository.findByServiceIdAndAppointmentType(task.getServiceId(), taskMap.get(task.getMstTaskDef().getKey().toLowerCase()));
						if(appointment !=null && appointment.size()>3) {
							taskbean.setRevisitNotAllowed(true);
						}
						else {
							taskbean.setRevisitNotAllowed(false);
						}
					}
					
					taskbean.setNonEditable(checkIsNonEditable(task));
					taskbean.setCommonData(commonData);
					
					List<String> tasksList = Arrays.asList("terminate-offnet-backhaul-po-trf-date-extension", "terminate-offnet-po-trf-date-extension");
					
					if(tasksList.contains(task.getMstTaskDef().getKey())) {
						List<ServiceLogs> serviceLogs = serviceLogsRepository.findByServiceIdAndType(task.getServiceId(), "TRFS Date");
						if(serviceLogs != null && !serviceLogs.isEmpty()) {
							List<ServiceLogBean> serviceLogBeans = new ArrayList<>();
							for (ServiceLogs serviceLog : serviceLogs) {
								ServiceLogBean serviceLogBean = new ServiceLogBean();
								serviceLogBean.setAttributeValue(serviceLog.getAttributeValue());
								serviceLogBean.setChangedAttributeValue(serviceLog.getChangedAttributeValue());
								serviceLogBean.setCreatedBy(serviceLog.getCreatedBy());
								serviceLogBean.setType(serviceLog.getType());
								serviceLogBean.setCreatedTime(DateUtil.convertDateToTimeStamp(serviceLog.getCreatedTime()));
								serviceLogBeans.add(serviceLogBean);
							}
							taskbean.setServiceLogBeans(serviceLogBeans);
						}
					}

					LOGGER.info("Get Task Details Response for task id {}  -- {}", task.getId(),
							Utils.convertObjectToJson(taskbean));
					return taskbean;

				}

			}
		}

		return new TaskBean();

	}

	private boolean checkIsNonEditable(Task task) {

		try {

			if (task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment")) {

				Task serviceDesignTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
						task.getServiceId(), "enrich-service-design");

				if (serviceDesignTask != null) {
					if ((serviceDesignTask.getMstStatus() != null)
							&& serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)
							|| serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.OPEN)
							|| serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
						return true;
					}
				}

			}else if (task.getMstTaskDef().getKey().equalsIgnoreCase("izopc-advanced-enrichment")) {

				Task serviceDesignTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
						task.getServiceId(), "izopc-manual-enrich-service-design");

				if (serviceDesignTask != null) {
					if ((serviceDesignTask.getMstStatus() != null)
							&& serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)
							|| serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.OPEN)
							|| serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
						return true;
					}
				}

			}
		} catch (Exception e) {
			LOGGER.error("error with in getting non editable changes:{}", e);
		}

		return false;

	}

	/**
	 * @author vivek
	 * @param apTimestamp used to check resche
	 * @return
	 */
	private boolean checkIsReschedule(Timestamp apTimestamp) {

		LocalDateTime scheduleDay = apTimestamp.toLocalDateTime().minusDays(2);

		if (new Timestamp(new Date().getTime()).toLocalDateTime().isBefore(scheduleDay)) {
			return true;
		}

		return false;
	}

	/**
	 * @param task
	 * @param taskbean
	 */
	private void setTaskData(Task task, TaskBean taskbean) {

		if (task.getTaskData() != null) {

			TaskData taskData = taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(task.getId());
			if (taskData != null) {
				TaskDataBean dataBean = new TaskDataBean();
				dataBean.setData(Utils.convertJsonStingToJson(taskData.getData()));
				dataBean.setName(taskData.getName());
				dataBean.setCreatedTime(taskData.getCreatedTime());
				taskbean.setTaskDataBeans(dataBean);
			}

		}

	}

	public Task getTaskByExecution(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		Task task = null;
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		LOGGER.info("task by execution service id {}, current activity id {}", serviceId,
				execution.getCurrentActivityId());
		task = taskRepository.findByServiceIdAndMstStatus_codeAndMstTaskDef_key(serviceId, TaskStatusConstants.OPENED,
				execution.getCurrentActivityId());
		if (Objects.nonNull(task)) {
			return task;
		} else {
			LOGGER.info("no task found {}", task);
			return task;
		}
	}

	/**
	 *
	 * @param taskId
	 * @param attachmentIdBeans
	 * @return AttachmentIdBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public DocumentIds uploadMandatoryAttachments(DocumentIds attachmentIdBeans)
			throws TclCommonException {
		Task task =getTaskByIdAndWfTaskId(attachmentIdBeans.getTaskId(),attachmentIdBeans.getWfTaskId());
		ScServiceDetail scServiceDetail=scServiceDetailRepository.findFirstByUuidAndMstStatus_codeOrderByIdDesc(task.getServiceCode(),"INPROGRESS");


		validateInputs(task, attachmentIdBeans);
		if (attachmentIdBeans.getDocumentIds() != null && !attachmentIdBeans.getDocumentIds().isEmpty()) {
			attachmentIdBeans.getDocumentIds()
					.forEach(attachmentIdBean -> makeEntryInScAttachment(task, attachmentIdBean.getAttachmentId()));
		}

		if(!StringUtils.isEmpty(attachmentIdBeans.getTaxExemptionReason())){
			ScServiceAttribute scServiceAttribute = new ScServiceAttribute();
			scServiceAttribute.setAttributeAltValueLabel(attachmentIdBeans.getTaxExemptionReason());
			scServiceAttribute.setAttributeName("TAX_EXCEMPTED_REASON");
			scServiceAttribute.setAttributeValue(attachmentIdBeans.getTaxExemptionReason());
			scServiceAttribute.setCategory("SITE_PROPERTIES");
			scServiceAttribute.setCreatedBy("root");
			scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsActive("Y");
			scServiceAttribute.setUpdatedBy("root");
			scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceAttribute.setIsAdditionalParam("N");
			scServiceAttribute.setScServiceDetail(scServiceDetail);
			scServiceAttributeRepository.save(scServiceAttribute);
		}

		if(attachmentIdBeans!=null && attachmentIdBeans.getCustomerPoDate()!=null && attachmentIdBeans.getCustomerPoNumber()!=null){

			if(Objects.nonNull(scServiceDetail) && Objects.nonNull(scServiceDetail.getScOrder()) && !CollectionUtils.isEmpty(scServiceDetail.getScOrder().getScOrderAttributes())) {
				List<ScOrderAttribute> scOrderAttributes = scServiceDetail.getScOrder().getScOrderAttributes().stream()
						.filter(s -> s.getAttributeName().equalsIgnoreCase("PO_NUMBER") || s.getAttributeName().equalsIgnoreCase("PO_DATE"))
						.collect(Collectors.toList());

				if (scOrderAttributes.isEmpty()) {

					ScOrder scOrder = scServiceDetail.getScOrder();
					ScOrderAttribute scOrderAttribute = new ScOrderAttribute();

					scOrderAttribute.setAttributeName("PO_NUMBER");
					scOrderAttribute.setAttributeValue(attachmentIdBeans.getCustomerPoNumber());
					scOrderAttribute.setScOrder(scOrder);

					scOrderAttribute.setAttributeAltValueLabel("PO_NUMBER");
					scOrderAttribute.setCreatedBy(Utils.getSource());
					scOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
					scOrderAttribute.setIsActive(CommonConstants.Y);
					scOrderAttribute.setUpdatedBy(Utils.getSource());
					scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));

					scOrderAttributeRepository.save(scOrderAttribute);

					ScOrderAttribute poDate = new ScOrderAttribute();

					poDate.setAttributeName("PO_DATE");
					poDate.setAttributeValue(attachmentIdBeans.getCustomerPoDate());
					poDate.setScOrder(scOrder);
					poDate.setAttributeAltValueLabel("PO_DATE");
					poDate.setCreatedBy(Utils.getSource());
					poDate.setCreatedDate(new Timestamp(new Date().getTime()));
					poDate.setIsActive(CommonConstants.Y);
					poDate.setUpdatedBy(Utils.getSource());
					poDate.setUpdatedDate(new Timestamp(new Date().getTime()));
					scOrderAttributeRepository.save(poDate);

				}
				else {
					scOrderAttributes.forEach(attr->{
						if(attr.getAttributeName().equalsIgnoreCase("PO_NUMBER")) {
							attr.setAttributeValue(attachmentIdBeans.getCustomerPoNumber());

						}
						else if(attr.getAttributeName().equalsIgnoreCase("PO_DATE")) {
							attr.setAttributeValue(attachmentIdBeans.getCustomerPoDate());


						}
						scOrderAttributeRepository.save(attr);

					});
				}
			}

		}
		processTaskLogDetails(task,"CLOSED",attachmentIdBeans.getDelayReason(),null, attachmentIdBeans);
		flowableBaseService.taskDataEntry(task, attachmentIdBeans);
		return attachmentIdBeans;
	}


	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public Task updateTask(Task task) {

		return taskRepository.save(task);

	}

	@Transactional(readOnly = false)
	public String reTriggerTask(Integer taskId, Map<String, Object> map) {
		try {
			Task task = getTaskById(taskId);
			Execution execution = runtimeService.createExecutionQuery().processInstanceId(task.getWfProcessInstId())
					.activityId(task.getMstTaskDef().getKey()).singleResult();

			if(map!=null)runtimeService.trigger(execution.getId(),map);
			else runtimeService.trigger(execution.getId());

			return "SUCCESS";
		}catch(Exception ee) {
			LOGGER.error(ee.getMessage(),ee);
			return ee.getMessage();
		}
	}

	

	@Transactional(readOnly = false)
	public String manuallyCompleteTask(Integer taskId, Map<String, Object> map) throws TclCommonException{

			Task task = getTaskById(taskId);
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(task.getOrderCode(), "Y");
			
		if ("set-terminate-clr-jeopardy".equalsIgnoreCase(task.getMstTaskDef().getKey())) {

			if (map.containsKey("action") && map.get("action") != null) {

				String action = (String) map.get("action");

				if (!action.equalsIgnoreCase("retry")) {

					Optional<ScServiceDetail> OpActiveScServiceDetail = scServiceDetailRepository
							.findById(task.getServiceId());
					if (OpActiveScServiceDetail.isPresent() && !OpActiveScServiceDetail.get().getScOrder()
							.getOrderType().equalsIgnoreCase("Termination")) {
						if (CommonConstants.YES
								.equalsIgnoreCase(OpActiveScServiceDetail.get().getTerminationFlowTriggered())) {
							
							ScServiceDetail oldActiveScServiceDetail = OpActiveScServiceDetail.get();
							
							LOGGER.info("Terminate Service ID:{}",oldActiveScServiceDetail.getUuid());
							
							updateServiceStatusAndCreatedNewStatus(oldActiveScServiceDetail, "TERMINATE",
									"TERMINATEREQUEST");
							oldActiveScServiceDetail.setMstStatus(taskCacheService.getMstStatus("TERMINATE"));
							oldActiveScServiceDetail.setServiceConfigStatus("TERMINATE");
							oldActiveScServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
							scServiceDetailRepository.save(oldActiveScServiceDetail);
							// Terminate the service from service activation
							mqUtils.send(saServiceTerminateQueue, String.valueOf(oldActiveScServiceDetail.getId()));
							rfService.populateP2PandPmpTerminationData(oldActiveScServiceDetail);
						} else {
							if (OpActiveScServiceDetail.isPresent()
									&& OpActiveScServiceDetail.get().getParentUuid() != null) {
								
								
								ScServiceDetail parentServiceDetail = scServiceDetailRepository
										.findFirstByUuidAndMstStatus_codeOrderByIdDesc(
												OpActiveScServiceDetail.get().getParentUuid(),
												TaskStatusConstants.ACTIVE);
								if (Objects.nonNull(parentServiceDetail)) {
									LOGGER.info("Terminate Service ID:{}",parentServiceDetail.getUuid());
									updateServiceStatusAndCreatedNewStatus(parentServiceDetail, "TERMINATE",
											"TERMINATEREQUEST");
									parentServiceDetail.setServiceConfigStatus("TERMINATE");
									parentServiceDetail.setMstStatus(taskCacheService.getMstStatus("TERMINATE"));
									parentServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
									Map<String,String> atMap=new HashMap<>();
									atMap.put("terminationReason","PARALLEL-TERMINATION");
									componentAndAttributeService.updateAttributes(parentServiceDetail.getId(), atMap,
											AttributeConstants.COMPONENT_LM, "A");
									scServiceDetailRepository.save(parentServiceDetail);
									// Terminate the service from service activation
									mqUtils.send(saServiceTerminateQueue, String.valueOf(parentServiceDetail.getId()));
									mqUtils.send(siTerminateDetail, parentServiceDetail.getUuid());
									rfService.populateP2PandPmpTerminationData(parentServiceDetail);
								}
							}

						}
					}else if(OpActiveScServiceDetail.isPresent() && OpActiveScServiceDetail.get().getOrderSubCategory()!=null && 
							OpActiveScServiceDetail.get().getOrderSubCategory().toLowerCase().contains("parallel")
							&& OpActiveScServiceDetail.get().getParentUuid()!=null) {
						LOGGER.info("SetTerminateClrJeoprady for parallel serviceId:{} with parentUUid::{}",OpActiveScServiceDetail.get().getId(),OpActiveScServiceDetail.get().getParentUuid());
						ScServiceDetail oldActiveScServiceDetail = scServiceDetailRepository
								.findByUuidAndMstStatus_code(OpActiveScServiceDetail.get().getParentUuid(), "ACTIVE");
						if (Objects.nonNull(oldActiveScServiceDetail)) {
							LOGGER.info("Changing SC OLD SERVICE ID ACTIVE TO TERMINATE {}",OpActiveScServiceDetail.get().getParentUuid());
							updateServiceStatusAndCreatedNewStatus(oldActiveScServiceDetail, "TERMINATE");
							if(oldActiveScServiceDetail.getIsMigratedOrder()!=null && CommonConstants.Y.equalsIgnoreCase(oldActiveScServiceDetail.getIsMigratedOrder())){
								Map<String,String> atMap=new HashMap<>();
								atMap.put("terminationReason","PARALLEL-TERMINATION");
								componentAndAttributeService.updateAttributes(oldActiveScServiceDetail.getId(), atMap,
										AttributeConstants.COMPONENT_LM, "A");
							}
							oldActiveScServiceDetail.setMstStatus(taskCacheService.getMstStatus("TERMINATE"));
							oldActiveScServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
							oldActiveScServiceDetail.setServiceConfigDate(new Timestamp(new Date().getTime()));
							oldActiveScServiceDetail.setServiceConfigStatus(TaskStatusConstants.TERMINATE);
							scServiceDetailRepository.save(oldActiveScServiceDetail);
							// Terminate the service from service activation
							LOGGER.info("Parent ServiceId::{} for SetTerminateClrJeoprady",oldActiveScServiceDetail.getId());
							mqUtils.send(saServiceTerminateQueue, String.valueOf(oldActiveScServiceDetail.getId()));
							mqUtils.send(siTerminateDetail, oldActiveScServiceDetail.getUuid());
							rfService.populateP2PandPmpTerminationData(oldActiveScServiceDetail);
						}
					}
				}
			}

		}
		if ("comm-valid-service-termination".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findFirstByUuidAndIsMigratedOrder(task.getServiceCode(), "N");
			if (scServiceDetail != null){
				map.put("lrTerminationRequired", false);
			} else {
				map.put("lrTerminationRequired", true);
			}
		}
				
			

			if ("product-commissioning-jeopardy".equals(task.getMstTaskDef().getKey())) {
				LOGGER.info("task invoked for product-commissioning-jeopardy ");

				ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(task.getServiceId()).get();
				String orderType=	OrderCategoryMapping.getOrderType(scServiceDetail, scOrder);
				String orderCategory=OrderCategoryMapping.getOrderCategory(scServiceDetail, scOrder);
			
				scServiceDetail.setBillingStatus(TaskStatusConstants.ACTIVE);
				scServiceDetail.setBillingCompletedDate(new Timestamp(new Date().getTime()));
				scServiceDetailRepository.save(scServiceDetail);
		


				if("MACD".equalsIgnoreCase(orderType) && Objects.nonNull(scServiceDetail.getOrderSubCategory())
						&& scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")){
					LOGGER.info("ProcessL2O:ParallelExists");
					map.put("isParallelExists", "true");
				ScComponentAttribute terminationDate = scComponentAttributesRepository
						.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
								scServiceDetail.getId(), "terminationDate", "LM", "A");
				map.put("terminationEffectiveDate",getParallelTerminationDate(terminationDate.getAttributeValue().concat(" 09:00")));
				if(Objects.nonNull(scServiceDetail.getParentUuid())){
						map.put("parentServiceCode", scServiceDetail.getParentUuid());
						ScServiceDetail scServiceDetailOld = scServiceDetailRepository.findByUuidAndMstStatus_code(scServiceDetail.getParentUuid(), "ACTIVE");
						if("NPL".equalsIgnoreCase(scServiceDetail.getErfPrdCatalogProductName())) {
							if(scServiceDetailOld!=null ) {
								LOGGER.info("optimusMacd is {} " ,true);
								map.put("optimusMacd", true);
								map.put("lrTerminationRequired", false);
							}else {
								LOGGER.info("lrTerminationRequired is {} " ,true);
								map.put("lrTerminationRequired", true);
								map.put("optimusMacd", false);
							}
						}else {
							if(scServiceDetailOld!=null  && "N".equals(scServiceDetailOld.getIsMigratedOrder())) {
								LOGGER.info("lrTerminationRequired is {} " ,true);
								map.put("lrTerminationRequired", false);
								map.put("optimusMacd", true);
							}else {
								LOGGER.info("optimusMacd is {} " ,true);
								map.put("optimusMacd", false);
								map.put("lrTerminationRequired", true);
							}
						}
						
						if(Objects.nonNull(scServiceDetailOld)){
							LOGGER.info("scServiceDetailOldExists");
							ScComponentAttribute parentScComponentAttribute=scComponentAttributesRepository.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(scServiceDetailOld.getId(), "lmType", "LM", "A");
							if(Objects.nonNull(parentScComponentAttribute) && Objects.nonNull(parentScComponentAttribute.getAttributeValue())
									&& !parentScComponentAttribute.getAttributeValue().isEmpty() && parentScComponentAttribute.getAttributeValue().toLowerCase().contains("offnet")){
								LOGGER.info("Parent Service LM Type ::{}",parentScComponentAttribute.getAttributeValue());
								map.put("parentLmType", "offnet");
								
								String tclServiceId=scServiceDetail.getParentUuid();
								Stg0SapPoDtlOptimus stg0SapPoDtlOptimus=sapService.getSapData(tclServiceId);
								ParentPoBean parentPoData=sapService.getParentPoData(tclServiceId);
								sapService.updateParentPoDataForTermination(scServiceDetail,stg0SapPoDtlOptimus,parentPoData);
								
							}
						}
					}
					ScServiceAttribute scServiceDownTimeAttr = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "downtime_duration");
					ScServiceAttribute scServiceDownTimeIndAttr = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "downtime_needed_ind");
					if (Objects.nonNull(scServiceDownTimeAttr) && Objects.nonNull(scServiceDownTimeIndAttr) && !scServiceDownTimeAttr.getAttributeValue().isEmpty()
								&& "yes".equalsIgnoreCase(scServiceDownTimeIndAttr.getAttributeValue())) {
						LOGGER.info("ProcessL2O:ParallelDownTime");
						try {
							Integer parallelDays =Integer.parseInt(scServiceDownTimeAttr.getAttributeValue());
							LOGGER.info("parallel days ={}",parallelDays);
							if(parallelDays>0) {
								map.put("parallelDownTime", "PT"+(parallelDays*24)+"H");
							}else {
								map.put("parallelDownTime", "PT1M");
							}
						}catch(Exception ee) {
							LOGGER.error("paralleldays Exception {}",ee);
							map.put("parallelDownTime", "PT1M");
						}
					}else{
						LOGGER.info("ProcessL2O:ParallelDownTime is 1");
						map.put("parallelDownTime", "PT1M");
					}
				}else {
					
				/*
				 * if (scServiceDetailInActive!=null && "Y".equals(
				 * scServiceDetailInActive.getIsMigratedOrder())) {
				 * LOGGER.info("lrTerminationRequired is {} " ,true);
				 * map.put("lrTerminationRequired", true); map.put("optimusMacd", false); }
				 */
				if (scOrder != null && "MACD".equals(orderType)
						&& !"ADD_SITE".equals(orderCategory)) {
					/*
					 * ScServiceDetail scServiceDetailInActive =
					 * scServiceDetailRepository
					 * .findFirstByUuidAndMstStatus_codeOrderByIdDesc(task.
					 * getServiceCode(), "INACTIVE");
					 */
					ScServiceDetail scServiceDetailActive = scServiceDetailRepository
							.findFirstByServiceLinkIdOrderByIdDesc(task.getServiceCode());

					if (scServiceDetailActive != null && "N".equals(scServiceDetailActive.getIsMigratedOrder())) {
						LOGGER.info("optimusMacd is,scDetailId {},{} ", true, scServiceDetailActive.getId());
						map.put("lrTerminationRequired", false);
						map.put("optimusMacd", true);
					} else {
						LOGGER.info("lrTerminationRequired is {} ", true);
						map.put("lrTerminationRequired", true);
						map.put("optimusMacd", false);
					}
					String upgradeType = "";
					String lmType = "";
					ScServiceAttribute upgradeTypeAttr = scServiceAttributeRepository
							.findFirstByScServiceDetail_idAndAttributeName(scServiceDetail.getId(), "upgrade_type");
					ScComponentAttribute lmTypeAttr = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									scServiceDetail.getId(), "lmType", "LM", "A");
					if(upgradeTypeAttr!=null)upgradeType = StringUtils.trimToEmpty(upgradeTypeAttr.getAttributeValue());
					if(lmTypeAttr!=null)lmType = StringUtils.trimToEmpty(lmTypeAttr.getAttributeValue());
					
					if ((StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType)) 
							&& (StringUtils.isNotBlank(lmType) && lmType.toLowerCase().contains("offnet"))) {
						LOGGER.info("Offnet parallel upgradeType={}",scServiceDetail.getOrderSubCategory(),upgradeType);						
						map.put("parentLmType", "offnet");						
					}
					 

					if (Objects.nonNull(scServiceDetail.getOrderSubCategory())
							&& (scServiceDetail.getOrderSubCategory().toLowerCase().contains("bso")
									|| scServiceDetail.getOrderSubCategory().toLowerCase().contains("Shifting")) || ((StringUtils.isNotBlank(upgradeType) && "parallel".equalsIgnoreCase(upgradeType))
							&& (StringUtils.isNotBlank(lmType) && lmType.toLowerCase().contains("offnet")))) {
						LOGGER.info("LMBSOSHIFTING OrderSubCategory={} upgradeType={}",scServiceDetail.getOrderSubCategory(),upgradeType);
						if (Objects.nonNull(scServiceDetailActive)) {
							LOGGER.info("Prev Service Detail Exists");
							ScComponentAttribute prevScComponentAttribute = scComponentAttributesRepository
									.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
											scServiceDetailActive.getId(), "lmType", "LM", "A");
							if (Objects.nonNull(prevScComponentAttribute)
									&& Objects.nonNull(prevScComponentAttribute.getAttributeValue())
									&& !prevScComponentAttribute.getAttributeValue().isEmpty()
									&& prevScComponentAttribute.getAttributeValue().toLowerCase().contains("offnet")) {
								LOGGER.info("Prev Service Detail LM TYPE::{}",
										prevScComponentAttribute.getAttributeValue());
								map.put("parentLmType", "offnet");

								//updateParentPo for termination in onnet case
								String tclServiceId=scServiceDetail.getUuid();
								Stg0SapPoDtlOptimus stg0SapPoDtlOptimus=sapService.getSapData(tclServiceId);
								ParentPoBean parentPoData=sapService.getParentPoData(tclServiceId);
								sapService.updateParentPoDataForTermination(scServiceDetail,stg0SapPoDtlOptimus,parentPoData);
							}

						}
						ScComponentAttribute billStart = scComponentAttributesRepository
								.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
										scServiceDetail.getId(), "billStartDate", AttributeConstants.COMPONENT_LM, "A");
						if (Objects.nonNull(billStart) && Objects.nonNull(billStart.getAttributeValue())
								&& StringUtils.isNotEmpty(billStart.getAttributeValue())) {
							Map<String, String> atMap = new HashMap<>();
							atMap.put("terminationDate", optimusDateMinusDays(billStart.getAttributeValue(),1));
							componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
									AttributeConstants.COMPONENT_LM, "A");
						}
						
					}
					
					ScComponentAttribute txResourcePathComponentAtt = scComponentAttributesRepository
							.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
									scServiceDetail.getId(), "txResourcePathType", "LM", "A");
					if (Objects.nonNull(txResourcePathComponentAtt)
							&& Objects.nonNull(txResourcePathComponentAtt.getAttributeValue())
							&& !txResourcePathComponentAtt.getAttributeValue().isEmpty() && txResourcePathComponentAtt.getAttributeValue().equalsIgnoreCase("Parallel")) {
						LOGGER.info("Current Service Detail Tx Resource Path::{}",txResourcePathComponentAtt.getAttributeValue());
						map.put("txResourcePathType", "Parallel");
					}
				}
			}
		}else if ("ipc-comm-valid".equals(task.getMstTaskDef().getKey()) || "ipc-commercial-vetting".equals(task.getMstTaskDef().getKey())) {
				if (scOrder != null && "MACD".equals(scOrder.getOrderType())) {
					LOGGER.info("Ipc optimusMacd is {} ", true);
					map.put("optimusMacd", true);
				}
			} else if (IpcConstants.MST_TASK_ATTRIBUTES_IPC_PROJECT_IMPLEMENTATION.equals(task.getMstTaskDef().getKey())) {
				List<Task> procurementTaskL = taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(task.getServiceId(), Arrays.asList(IpcConstants.MST_TASK_ATTRIBUTES_IPC_PROCUREMENT), Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));
				if (!procurementTaskL.isEmpty()) {
					return "Please close pending Procurement Task before closing this task";
				}
			}else if ("ip-terminate-resource-blocked-configuration-jeopardy".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				mqUtils.send(saServiceNetpRefQueue, String.valueOf(task.getServiceId()));
			}
			

			flowableBaseService.taskDataEntry(task, map,map);
			processTaskLogDetails(task,TaskLogConstants.CLOSED,"",null, constructBaseRequest(map));
			return "SUCCESS";
	}
		
	private void updateServiceStatusAndCreatedNewStatus(ScServiceDetail scServiceDetail,String status) {
			
			ServiceStatusDetails serviceStatusDetails=	serviceStatusDetailsRepository.findFirstByScServiceDetail_idOrderByIdDesc(scServiceDetail.getId());
			
			if(serviceStatusDetails!=null) {
				serviceStatusDetails.setEndTime(new Timestamp(new Date().getTime()));
				serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));
				serviceStatusDetailsRepository.save(serviceStatusDetails);
			}
			createServiceStaus(scServiceDetail, status);
	}
	
	private ServiceStatusDetails createServiceStaus(ScServiceDetail scServiceDetail, String mstStatus) {

		ServiceStatusDetails serviceStatusDetails = new ServiceStatusDetails();
		serviceStatusDetails.setScServiceDetail(scServiceDetail);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceStatusDetails.setUserName(userInfoUtils.getUserInformation().getUserId());
		}
		serviceStatusDetails.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setStartTime(new Timestamp(new Date().getTime()));
		serviceStatusDetails.setUpdateTime(new Timestamp(new Date().getTime()));

		serviceStatusDetails.setStatus(mstStatus);
		serviceStatusDetailsRepository.save(serviceStatusDetails);

		return serviceStatusDetails;

	}

	@Transactional(readOnly = false)
	public String manuallyCompleteFlowableTask(String taskId, Map<String, Object> wfMap) {
		try {

			flowableTaskService.complete(taskId, wfMap);
			return "SUCCESS";
		}catch(Exception ee) {
			LOGGER.error(ee.getMessage(),ee);
			return ee.getMessage();
		}
	}
	/**
	 *
	 * This function is used to update assignee for multiple tasks-- L2O Discount delegation Purpose
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public AssigneeResponse updateAssigneeForMultipleTasks(AssigneeRequest request) throws TclCommonException {
		AssigneeResponse assigneeResponse = new AssigneeResponse();
		if (request.getTaskIds() == null) {
			throw new TclCommonException(CommonConstants.ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		request.getTaskIds().stream().forEach(taskId -> {
			Optional<Task> optionalTask = taskRepository.findById(taskId);
			if (optionalTask.isPresent()) {
				Task task = optionalTask.get();
				task.setClaimTime(new Timestamp(new Date().getTime()));
				updateAssignmentDetails(task, request);
				task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
				task.setCompletedTime(null);
				taskRepository.save(task);
				constructAssigneeResponse(task,request,assigneeResponse);
			}
		});

		return assigneeResponse;
	}

	private void constructAssigneeResponse(Task task, AssigneeRequest request, AssigneeResponse assigneeResponse) {
		assigneeResponse.setId(task.getId());
		assigneeResponse.setStatus(task.getMstStatus().getCode());
		assigneeResponse.setName(task.getMstTaskDef().getName());
		assigneeResponse.setAssigneeNameFrom(request.getAssigneeNameFrom());
		assigneeResponse.setAssigneeNameTo(request.getAssigneeNameTo());

	}

	public String getRandomNumberForCrammer() {
		return RandomStringUtils.random(5, true, true).toUpperCase() +"#";
	}

	public String getRandomNumberForNetp(String serviceCode, String processInstanceId, String configType,
			String productName) {
		return "RID_" + serviceCode + "#" + processInstanceId + "#" + configType + "#"+ RandomStringUtils.random(5, true, true).toUpperCase();
	}

	public String getRandomNumberForNetpTx(String serviceCode, String processInstanceId, String configType,
			String productName) {
		return "RID_" + serviceCode + "#" + processInstanceId + "#" + configType + "#"+ RandomStringUtils.random(5, true, true).toUpperCase();
	}

	public Task getTaskById(Integer taskId) {
		if (taskId != null) {
			return taskRepository.findById(taskId).orElse(null);

		}

		return null;
	}


	@Transactional(readOnly = false)
	public ServiceDetailRequest updateservicedetails(ServiceDetailRequest request) throws TclCommonException {
		ServiceDetailRequest serviceDetailRequest = new ServiceDetailRequest();

		String status = request.getStatus();
		

		if (status.equalsIgnoreCase(TaskStatusConstants.HOLD)) {
			boolean cancel = false;
			String message = "INPROGRESS to HOLD";
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(request.getServiceCode(), TaskStatusConstants.INPROGRESS);

			if (scServiceDetail == null) {
				scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(request.getServiceCode(),
						TaskStatusConstants.CANCELLED);
				if (scServiceDetail == null) {

					scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(request.getServiceCode(),
							TaskStatusConstants.HOLD);
					message = "HOLD to HOLD";
					if (scServiceDetail == null) {
						throw new TclCommonException(
								com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
								ResponseResource.R_CODE_ERROR);
					}
				}


			}
			
			if(scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CANCELLED)) {
				cancel = true;
				message = "CANCELLED to HOLD";
			}

			if (!cancel) {
				isEligibleforAmendement(scServiceDetail);
			}

			saveRemarksForOrderAmendment(scServiceDetail, message, request.getAmendedOrderCode());
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.HOLD,"AMENDMENTHOLDREQUESTFROML20");
			scServiceDetail.setRequestForAmendment(CommonConstants.Y);
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
			scServiceDetailRepository.save(scServiceDetail);

		} else if (status.equalsIgnoreCase(TaskStatusConstants.RESUME)) {

			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(request.getServiceCode(), TaskStatusConstants.HOLD);
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.INPROGRESS,"RESUMEREQUESTFROML20");

			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			scServiceDetailRepository.save(scServiceDetail);
			scServiceDetail.setRequestForAmendment(CommonConstants.N);

			saveRemarksForOrderAmendment(scServiceDetail, "HOLD to INPROGRESS", request.getAmendedOrderCode());

		}

		else if (status.equalsIgnoreCase(TaskStatusConstants.TERMINATE)) {
			ScServiceDetail scServiceDetail = null;
			scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(request.getServiceCode(),
					TaskStatusConstants.INPROGRESS);
			isEligibleforAmendement(scServiceDetail);

			if (scServiceDetail == null) {
				scServiceDetail = scServiceDetailRepository.findByUuidAndMstStatus_code(request.getServiceCode(),
						TaskStatusConstants.HOLD);
			}
			saveRemarksForOrderAmendment(scServiceDetail, "HOLD/INPROGRESS to TERMINATE",
					request.getAmendedOrderCode());
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.TERMINATE,"TERMINATEREQUESTFROML20");

			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATE));
			scServiceDetailRepository.save(scServiceDetail);

		}

		else if (status.equalsIgnoreCase(TaskStatusConstants.AMENDED)) {

			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(request.getServiceCode(), TaskStatusConstants.HOLD);
			isEligibleforAmendement(scServiceDetail);
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.AMENDED,"AMENDMENTREQUESTFROML20");

			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.AMENDED));
			scServiceDetailRepository.save(scServiceDetail);
			saveRemarksForOrderAmendment(scServiceDetail, "HOLD to AMENDED", request.getAmendedOrderCode());

		} else {
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
					ResponseResource.R_CODE_ERROR);
		}

		return serviceDetailRequest;
	}
	
	private void isEligibleforAmendement(ScServiceDetail scServiceDetail) throws TclCommonException {
		/*
		 * List<Task> task =
		 * taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(
		 * scServiceDetail.getId(), Arrays.asList("enrich-service-design",
		 * "provide-wbsglcc-details", "po-offnet-lm-provider"),
		 * Arrays.asList(TaskStatusConstants.CLOSED_STATUS, TaskStatusConstants.OPENED,
		 * TaskStatusConstants.INPROGRESS));
		 */
		Task enrichdesign = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
				scServiceDetail.getId(), "product-commissioning-jeopardy");

		if (enrichdesign != null) {
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
					ResponseResource.R_CODE_ERROR);

		}
	}

	private void saveRemarksForOrderAmendment(ScServiceDetail scServiceDetail,String status,String orderCode) {
		String userName = "";
		userName = userInfoUtils.getUserInformation().getUserId();
		String orderCodeMessage="";

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		if(orderCode!=null) {
			orderCodeMessage="and order code is: "+orderCode;
		}
		taskRemark.setRemarks("Order amendment request from L20 on :" + new Date() + " by user :" + userName+""+orderCodeMessage);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}

	private void saveRemarksForCimHoldOrUnhold(ScServiceDetail scServiceDetail,String action, String remarks, Timestamp tentativeDateforHold, String catagory) {
		String userName = "";
		userName = userInfoUtils.getUserInformation().getUserId();

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		if(tentativeDateforHold != null) {
			taskRemark.setRemarks("Service status has been changed to  " + action + " and category as" + catagory + " and Remarks as " + remarks
					+ " and tentativedate is " + new Date(tentativeDateforHold.getTime()));
		} else {
			taskRemark.setRemarks("Service status has been changed to   " + action +" and Remarks as " + remarks);
		}
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}

	
	private String getEffectiveDateForCustomerWaitingTask(String customerWaitingTaskDate) throws TclCommonException {
		try{
			LocalDateTime localDateTime=null;
			if(customerWaitingTaskDate!=null && !customerWaitingTaskDate.isEmpty()) {
				customerWaitingTaskDate = DateUtil
						.convertDateToString(new Date(Timestamp.valueOf(DateUtil.convertStringToTimeStampYYMMDD
								(customerWaitingTaskDate).toLocalDateTime().minusDays(15)).getTime()));
			}else{
				customerWaitingTaskDate = DateUtil
						.convertDateToString(new Date(Timestamp.valueOf(LocalDateTime.now().plusDays(15)).getTime()));
			}
			LOGGER.info("getEffectiveDateForCustomerWaitingTask {} :",customerWaitingTaskDate);
			customerWaitingTaskDate=customerWaitingTaskDate+" "+"00:00";
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(customerWaitingTaskDate);
			localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC"));
			customerWaitingTaskDate=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			LOGGER.info("Derived getEffectiveDateForHoldProcess:{}",customerWaitingTaskDate);
			return customerWaitingTaskDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForCustomerWaitingTask:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}

	public void notifyMrn(Integer serviceId) throws TclCommonException {
		Optional<ScServiceDetail> detail = scServiceDetailRepository.findById(serviceId);
		ScServiceDetail scServiceDetail = detail.get();
		if (scServiceDetail != null) {
			Map<String, String> scComponentAttributesmap = commonFulfillmentUtils
					.getComponentAttributes(Integer.valueOf(serviceId), "LM", "A");
			List<MstTaskRegion> mstTaskRegionList;
			String distributionCenterName = scComponentAttributesmap.getOrDefault("distributionCenterName", null);
			LOGGER.info("Locationf of distributet center name and serviceId : {} {}", distributionCenterName,
					serviceId);
			if (distributionCenterName != null && !distributionCenterName.isEmpty()) {
				mstTaskRegionList = mstTaskRegionRepository.findByGroupAndLocation("SCM-Warehouse",
						distributionCenterName);
			} else {
				mstTaskRegionList = mstTaskRegionRepository.findByGroup("SCM-Warehouse");
			}
			for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
				if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
					notificationService.notifyMrnEmail(mstTaskRegion.getEmail(), scServiceDetail.getUuid());
				}
			}
		}
	}

	@Transactional(readOnly = false)
	public AssigneeResponse updatePM(AssignPM request) throws TclCommonException {
		AssigneeResponse assigneeResponse = new AssigneeResponse();
		ScServiceDetail scServiceDetail = getServiceDetailsByOrderCodeAndServiceDeatils(request.getOrderCode(),
				request.getServiceCode(), request.getServiceId());

		if (scServiceDetail != null) {
			if(request.getIsReassign()!=null && CommonConstants.YES.equalsIgnoreCase(request.getIsReassign())){
				saveRemarks(scServiceDetail,request.getComments());
				processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
						"Re-asignment happen for the service "+scServiceDetail.getUuid()+" to "+request.getAssignedPM() +" from "+scServiceDetail.getAssignedPM()+" by "+userInfoUtils.getUserInformation().getUserId()+" on "+new Timestamp(new Date().getTime()),
						null, null));
				processNotifyReAsignmentServiceForPM(scServiceDetail, request.getAssignedPM());
			}else{
				notifyService(scServiceDetail, request.getAssignedPM());
			}
			scServiceDetail.setAssignedPM(request.getAssignedPM());
			scServiceDetailRepository.save(scServiceDetail);
		}

		return assigneeResponse;
	}


	private void notifyService(ScServiceDetail scServiceDetail, String assinedPmEmailId) {
		try {
			String url = fulfillmentUtils.generateServiceDashboardUrl(scServiceDetail.getScOrderUuid(),
					scServiceDetail.getUuid());
			//Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
			//		scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
			List<String> ccAddresses = new ArrayList<>();
			String toAddress=null;
			List<ScContractInfo> scContractInfos=scContractInfoRepository.findByScOrder_id(scServiceDetail.getScOrder().getId());
			for (ScContractInfo scContractInfo : scContractInfos) {
				toAddress=scContractInfo.getAccountManagerEmail();
			}
			ccAddresses.add(assinedPmEmailId);
			notificationService.notifyCustomerWelcomeV2(scServiceDetail, ccAddresses, assinedPmEmailId,url,toAddress);
		} catch (Exception e) {
			LOGGER.error("Error in notifying", e);
		}

	}
	private void processNotifyReAsignmentServiceForPM(ScServiceDetail scServiceDetail, String assinedPmEmailId) {
		try {
			List<String> toAddresses = new ArrayList<>();
			List<ScContractInfo> scContractInfos=scContractInfoRepository.findByScOrder_id(scServiceDetail.getScOrder().getId());
			if(scContractInfos!=null && scContractInfos.stream().findFirst().isPresent() &&
					scContractInfos.stream().findFirst().get().getAccountManagerEmail()!=null) {
				toAddresses.add(scContractInfos.stream().findFirst().get().getAccountManagerEmail());
			}
			toAddresses.add(assinedPmEmailId);
			toAddresses.add(scServiceDetail.getAssignedPM());
			LOGGER.info("Re-assignment email list for AM,PM and Old PM {}",toAddresses.toString());
			notificationService.notifyReAsignmentServiceForPM(toAddresses,
					scServiceDetail.getScOrder().getOpOrderCode(),scServiceDetail.getUuid(),assinedPmEmailId,scServiceDetail.getAssignedPM(),userInfoUtils.getUserInformation().getUserId());
		} catch (Exception e) {
			LOGGER.error("Error in processNotifyReAsignmentServiceForPM of PM", e);
		}

	}

	@Transactional(readOnly = false)
	   public CimHoldRequest updateCimHold(CimHoldRequest request) throws TclCommonException{
	         ScServiceDetail scServiceDetail = scServiceDetailRepository.findByIdAndUuidAndScOrderUuid(
	               request.getServiceId(), request.getServiceCode(), request.getOrderCode());
	         if (request.getAction().equalsIgnoreCase("Hold")) {
	        	 
	        	
	        	 List<Task> tasks=taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(scServiceDetail.getId(), Arrays.asList("product-commissioning-jeopardy"), Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.CLOSED_STATUS,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));

	     		if(!tasks.isEmpty()) {
	     			throw new TclCommonException(
	     					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.BILLINGTASK_OPEN,
	     					ResponseResource.R_CODE_ERROR);
	     		}
	            if (scServiceDetail.getMstStatus().getCode().equalsIgnoreCase("INPROGRESS")) {
	               initiateHoldProcess(scServiceDetail, request);
					if (request.getDocumentIds() != null && !request.getDocumentIds().isEmpty()) {
						request.getDocumentIds()
								.forEach(attachmentIdBean -> makeEntryInScAttachment(scServiceDetail,
										attachmentIdBean.getAttachmentId()));
					}
	               if (request.getIsCancellation() != null
	                     && CommonConstants.YES.equalsIgnoreCase(request.getIsCancellation())) {
	                  processCancellationProcess(request, scServiceDetail);
	               }
	            } else {
	               throw new TclCommonException(ExceptionConstants.CANT_PUT_THIS_ON_HOLD,
	                     ResponseResource.R_CODE_ERROR);
	            }
	      } else if (request.getAction().equalsIgnoreCase("UnHold")) {
	    	  
			if (scServiceDetail.getRequestForAmendment() != null
					&& CommonConstants.Y.equalsIgnoreCase(scServiceDetail.getRequestForAmendment())) {
				throw new TclCommonException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.ORDER_AMENDMENT_RESTRICT,
						ResponseResource.R_CODE_ERROR);
			}
	         if (scServiceDetail.getMstStatus().getCode().equalsIgnoreCase("Hold") || scServiceDetail.getMstStatus()
	               .getCode().equalsIgnoreCase(TaskStatusConstants.DEFERRED_DELIVERY)) {
	            List<ScServiceDetail> scServiceDetails = scServiceDetailRepository
	                  .findByUuidAndMstStatus_codeAndIsMigratedOrder(request.getServiceCode(),
	                        TaskStatusConstants.INPROGRESS, "N");
	//To do already resource removed we should not allow service change to in progress
	            if (scServiceDetails.isEmpty()) {
	               if (scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED)
	                     || scServiceDetail.getMstStatus().getCode()
	                           .equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED_INITIATED)) {
	                  LOGGER.info(
	                        "Status can't not change to inprogress since sales-negotiation task is already in open or inprogress state : {}",
	                        scServiceDetail.getUuid());
	                  throw new TclCommonException(ExceptionConstants.CANT_PUT_UNHOLD_SINCE_NEGOTIATION,
	                        ResponseResource.R_CODE_ERROR);
	               }
	               checkSalesTaskAvailable(scServiceDetail.getId());
	               checkSalesNegotiationTaskStatus(scServiceDetail.getId());
	               initiateUnHoldProcess(scServiceDetail, request);
	            } else {
	               throw new TclCommonException(ExceptionConstants.OTHER_SERVICES_INPROGRESS,
	                     ResponseResource.R_CODE_ERROR);
	            }
	         } else {
	            throw new TclCommonException(ExceptionConstants.CANT_PUT_THIS_ON_UNHOLD, ResponseResource.R_CODE_ERROR);
	         }
	      }
	         
	         return request;
	   }
	
	/**
	 * @author vivek
	 *
	 * @param scServiceDetail
	 * @param request
	 */
	private void initiateUnHoldProcess(ScServiceDetail scServiceDetail, CimHoldRequest request) {

		ServiceStatusDetails serviceStatusDetails = updateServiceStatusAndCreatedNewStatus(scServiceDetail,
				TaskStatusConstants.INPROGRESS, "UNHOLDREQUESTFROMPM");
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
		if (serviceStatusDetails.getServiceChangeCategory() != null
				&& ("Customer Dependency".equalsIgnoreCase(serviceStatusDetails.getServiceChangeCategory())
						|| "Deferred Delivery".equalsIgnoreCase(serviceStatusDetails.getServiceChangeCategory()))) {
			updateCrfsDateAndRrfsDate(scServiceDetail, serviceStatusDetails.getStartTime());
			processUnHoldEmail(scServiceDetail, request);

		}
		scServiceDetailRepository.save(scServiceDetail);
		saveRemarksForCimHoldOrUnhold(scServiceDetail, TaskStatusConstants.INPROGRESS, request.getHoldReason(), null,
				request.getOnHoldCategory()==null?"UNHOLD-REQUESTFROM-PM":request.getOnHoldCategory());
		processCancellationUnHoldEmail(scServiceDetail, request);

	}


	/**
	 * @author vivek
	 *
	 * @param scServiceDetail
	 * @param request
	 */
	private void processUnHoldEmail(ScServiceDetail scServiceDetail, CimHoldRequest request) {
		try {
			LOGGER.info("Inside processUnHoldEmail method");

				LOGGER.info("processUnHoldEmail serviceId and PM email id : {} ",scServiceDetail.getId(),scServiceDetail.getAssignedPM());
				List<String> toEmailIds = Arrays.asList(scServiceDetail.getAssignedPM());

				notificationService.notifyUnHoldForService(toEmailIds, toEmailIds,
						scServiceDetail.getScOrder().getOpOrderCode(), scServiceDetail.getUuid(),
						request.getHoldReason());

		} catch (Exception ex) {
			LOGGER.error("Error in processUnHoldEmail : {}", ex);

		}
	}


	/**
	 * @author vivek
	 *
	 * @param scServiceDetail
	 * @param request
	 */
	private void processCancellationUnHoldEmail(ScServiceDetail scServiceDetail, CimHoldRequest request) {
		Map<String, String> attributeValues = commonFulfillmentUtils.getComponentAttributesDetails(
				Arrays.asList("isCancellationForHold", "pmEmailId", "salesSupportEmailId",
						"salesPersonEmailId"),
				scServiceDetail.getId(), "LM", "A");
		if (attributeValues.containsKey("isCancellationForHold")
				&& CommonConstants.YES.equalsIgnoreCase(attributeValues.get("isCancellationForHold"))) {
			CimHoldRequest cimHoldRequest = new CimHoldRequest();

			LOGGER.info("isCancellationForHold true for");

			if (scServiceDetail.getAssignedPM() != null) {
				cimHoldRequest.setPmEmailId(scServiceDetail.getAssignedPM());
			}
			if (attributeValues.get("salesPersonEmailId") != null) {
				cimHoldRequest.setSalesPersonEmailId(attributeValues.get("salesPersonEmailId"));
			}
			if (attributeValues.get("salesSupportEmailId") != null) {
				cimHoldRequest.setSalesSupportEmailId(attributeValues.get("salesSupportEmailId"));

			}
			cimHoldRequest.setPmEmailId(scServiceDetail.getAssignedPM());
			processNotifyStatusChanage(cimHoldRequest, scServiceDetail,
					"Status changed to In-progress from On-Hold for ", null);
		}		
	}


	private void checkSalesNegotiationTaskStatus(Integer serviceId) throws  TclCommonException {
		List<String> mstTastDefKey = Arrays.asList("sales-negotiation", "sales-negotiation-waiting-task","sales-assist-ordering","sales-negotiation-SALES");
		List<String> mstStatus = Arrays.asList(TaskStatusConstants.OPENED, TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN);
		LOGGER.info("inside checkSalesNegotiationTaskStatus");

		List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(serviceId, mstTastDefKey, mstStatus);
			if (tasks != null && !tasks.isEmpty()) {
			if (tasks.stream()
					.filter(task -> task.getMstTaskDef().getKey().equalsIgnoreCase("sales-negotiation")
							|| task.getMstTaskDef().getKey().equalsIgnoreCase("sales-assist-ordering")
							|| ("customer-hold-negotaition-CIM").equalsIgnoreCase(task.getMstTaskDef().getKey())
							|| ("sales-negotiation-SALES").equalsIgnoreCase(task.getMstTaskDef().getKey()))
					.findFirst().isPresent()) {
					LOGGER.info("Status can't not change to inprogress since sales-negotiation task is already in open or inprogress state : {}",serviceId);
					throw new TclCommonException(ExceptionConstants.CANT_PUT_UNHOLD_SINCE_NEGOTIATION,
							ResponseResource.R_CODE_ERROR);
				}
				if (tasks.stream().filter(task -> task.getMstTaskDef().getKey().equalsIgnoreCase
						("sales-negotiation-waiting-task") ).findFirst().isPresent()) {
					LOGGER.info("Closing sales-negotiation-waiting-task since and move status to inprogress : {}",serviceId);
					String instanceId=tasks.stream().filter(task -> task.getMstTaskDef().getKey().equalsIgnoreCase
							("sales-negotiation-waiting-task")).findFirst().get().getWfProcessInstId();
					Execution execution = runtimeService.createExecutionQuery().processInstanceId(instanceId)
							.activityId("sales-negotiation-waiting-task").singleResult();
					runtimeService.setVariable(execution.getId(), "cancelNegotiationTask", true);
					runtimeService.trigger(execution.getId());
						}else{
							throw new TclCommonException(ExceptionConstants.OTHER_SERVICES_INPROGRESS,
									ResponseResource.R_CODE_ERROR);
						}
					}
				}



	/**
	 * @author vivek
	 *
	 * @param scServiceDetail
	 * @param request
	 * @throws TclCommonException 
	 */
	private void initiateHoldProcess(ScServiceDetail scServiceDetail, CimHoldRequest request)
			throws TclCommonException {
		scServiceDetail.setOnHoldCategory(request.getOnHoldCategory());
		scServiceDetail.setHoldReason(request.getHoldReason());
		Timestamp holdDate = DateUtil.convertStringToTimeStampYYMMDD(request.getTentativeDateforHold());
		if (holdDate != null) {
			scServiceDetail.setTenatativeHoldResumeDate(holdDate);
		}
		if (request.getOnHoldCategory().equalsIgnoreCase("Deferred Delivery") && Objects.nonNull(holdDate)) {
			scServiceDetail.setIsDeferredDelivery("Yes");
			Timestamp rrfsDate = scServiceDetail.getRrfsDate();
			if(rrfsDate==null) rrfsDate = scServiceDetail.getServiceCommissionedDate();
			
			 rrfsDate = Timestamp.valueOf(rrfsDate.toLocalDateTime().plusDays(ChronoUnit.DAYS.between(
							new Timestamp(new Date().getTime()).toLocalDateTime(), holdDate.toLocalDateTime())));
			
			
			scServiceDetail.setRrfsDate(rrfsDate);
			updateServiceLogs(scServiceDetail.getRrfsDate().toString(), rrfsDate.toString(), scServiceDetail.getId(),
					"RRFS");
			Timestamp crfsDate = Timestamp.valueOf(
					scServiceDetail.getServiceCommissionedDate().toLocalDateTime().plusDays(ChronoUnit.DAYS.between(
							new Timestamp(new Date().getTime()).toLocalDateTime(), holdDate.toLocalDateTime())));
			updateServiceLogs(scServiceDetail.getServiceCommissionedDate().toString(), crfsDate.toString(),
					scServiceDetail.getId(), "CRFS");
			request.setCrfsDate(crfsDate);
			processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
					"Service is in Deferred delivery " + scServiceDetail.getUuid() + " and crfs date chage from  "
							+ scServiceDetail.getServiceCommissionedDate() + " to "
							+ crfsDate + " by " + userInfoUtils.getUserInformation().getUserId() + " on "
							+ new Timestamp(new Date().getTime()),
					null, null));
			scServiceDetail.setServiceCommissionedDate(crfsDate);
			processDeferredDelivery(scServiceDetail, request, holdDate, request.getOnHoldCategory());

		} else {
			scServiceDetail.setIsDeferredDelivery("No");
            processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
                    "Service is in Hold "+scServiceDetail.getUuid()+" by "+userInfoUtils.getUserInformation().getUserId()+" on "+new Timestamp(new Date().getTime()),
                    null, null));
			proccessHoldService(scServiceDetail, request, holdDate, request.getOnHoldCategory());

		}

		scServiceDetailRepository.save(scServiceDetail);
		triggerCustomerOnholdWorkflow(request.getTentativeDateforHold(), request, scServiceDetail);

	}

	private void updateServiceLogs(String currentValue, String changedValue, Integer serviceId, String type) {

		ServiceLogs serviceLogs = new ServiceLogs();
		serviceLogs.setAttributeValue(currentValue);
		serviceLogs.setChangedAttributeValue(changedValue);
		serviceLogs.setServiceId(serviceId);
		if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			serviceLogs.setCreatedBy(userInfoUtils.getUserInformation().getUserId());
		}
		serviceLogs.setType(type);
		serviceLogs.setCreatedTime(new Timestamp(new Date().getTime()));
		serviceLogs.setUpdatedTime(new Timestamp(new Date().getTime()));
		serviceLogsRepository.save(serviceLogs);
	}
	
	


	/**
	 * @author vivek
	 *
	 * @param request
	 * @param scServiceDetail
	 */
	private void processCancellationProcess(CimHoldRequest request, ScServiceDetail scServiceDetail) {

		AttachmentBean attachmentBean = null;
		if (request.getDocumentIds() != null && request.getDocumentIds().stream().filter(
				attachmentIdBean -> attachmentIdBean.getCategory().equalsIgnoreCase("CUSTOMEREMAIL"))
				.findFirst().isPresent()) {
			Optional<Attachment> attachment = attachmentRepository
					.findById(request.getDocumentIds().stream()
							.filter(attachmentIdBean -> attachmentIdBean.getCategory()
									.equalsIgnoreCase("CUSTOMEREMAIL"))
							.findFirst().get().getAttachmentId());
			if (attachment.isPresent()) {
				LOGGER.info("updateCimHold service details attacment present and attachmentId {} {} :",
						scServiceDetail.getId(), attachment.get().getId());
				attachmentBean = AttachmentBean.mapToBean(attachment.get());
			}
		}
		processNotifyStatusChanage(request, scServiceDetail,
				"Status changed to On-Hold from In-progress for ", attachmentBean);		
	}


	/**
	 * @author vivek
	 *
	 * @param scServiceDetail
	 * @param request
	 * @param holdDate
	 * @param onHoldCategory 
	 */
	private void proccessHoldService(ScServiceDetail scServiceDetail, CimHoldRequest request, Timestamp holdDate, String onHoldCategory) {
		updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.HOLD,onHoldCategory);
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
		saveRemarksForCimHoldOrUnhold(scServiceDetail, TaskStatusConstants.HOLD,
				request.getHoldReason(), holdDate,
				request.getOnHoldCategory());		
	}


	/**
	 * @author vivek
	 *
	 * @param scServiceDetail
	 * @param request
	 * @param holdDate
	 * @param onHoldCategory 
	 */
	private void processDeferredDelivery(ScServiceDetail scServiceDetail, CimHoldRequest request, Timestamp holdDate, String onHoldCategory) {
		updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.DEFERRED_DELIVERY,onHoldCategory);
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.DEFERRED_DELIVERY));
		saveRemarksForCimHoldOrUnhold(scServiceDetail, TaskStatusConstants.DEFERRED_DELIVERY,
				request.getHoldReason(), holdDate,
				request.getOnHoldCategory());
		notifyDeferredDeliveryEmail(scServiceDetail, request);		
	}


	private void notifyDeferredDeliveryEmail(ScServiceDetail scServiceDetail, CimHoldRequest request) {
		List<String> toAddresses = new ArrayList<>();
		List<String> ccAddresses = new ArrayList<>();
		ScComponentAttribute customerContactEmailId = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), localItContactEmailId,  AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
		if(customerContactEmailId != null && customerContactEmailId.getAttributeValue() != null) {
			toAddresses.add(customerContactEmailId.getAttributeValue());
		} else {
			LOGGER.info("Customer contact Email Id is not there for given service Id " + scServiceDetail.getId());
			return;
		}
		if(scServiceDetail.getAssignedPM() != null) {
			ccAddresses.add(scServiceDetail.getAssignedPM());
		}
		notificationService.notifyDeferredDelivery(toAddresses, ccAddresses, NOTIFY_DEFERRED_DELIVERY_SUBJECT,
				scServiceDetail.getScOrder().getOpOrderCode(), scServiceDetail.getUuid(), request.getHoldReason());
	}
	

	/**
	 * @author Thamizhselvi Perumal
	 * @param lmProvider
	 * @return List of LmProvider
	 * @throws TclCommonException
	 * this is used to get the vendor details
	 */
	public List<String> getLmProviderDetails(String lmProviderName)
			throws TclCommonException {
		List<String> lmProviders=new ArrayList<>();
		List<Stg0SfdcVendorC> vendors = stgSfdcVendorRepository.findAll(SfdcVendorSpecification.getLmProvider(lmProviderName));
		if (Objects.nonNull(vendors) && !vendors.isEmpty()) {
			vendors.stream().forEach(vendor -> {

				if (vendor.getSfdcProviderNameC() != null && !lmProviders.contains(vendor.getSfdcProviderNameC())) {
					lmProviders.add(vendor.getSfdcProviderNameC());

				} else {

					if (vendor.getName() != null && !lmProviders.contains(vendor.getName())) {
						lmProviders.add(vendor.getName());

					}

				}

			});
			return lmProviders;
		}
		return new ArrayList<String>();
	}


	@Transactional(readOnly = false)
	public Boolean saveTaskRemarks(TaskRemarksRequest taskRemarksRequest) {
		Task task = getTaskByIdAndWfTaskId(taskRemarksRequest.getTaskId(),taskRemarksRequest.getWfTaskId());
		if (task != null || taskRemarksRequest.getServiceId() != null) {
			TaskRemark taskRemark = new TaskRemark();
			taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
			taskRemark.setRemarks(taskRemarksRequest.getUserComments());
			taskRemark.setUsername(taskRemarksRequest.getUsername());
			taskRemark.setTask(task);
			taskRemark.setServiceId(taskRemarksRequest.getServiceId());
			taskRemark.setJeopardyCategory(taskRemarksRequest.getJeopardyCategory());
			taskRemark.setCurrentJeopardyOwner(taskRemarksRequest.getCurrentJeopardyOwner());
			taskRemark.setTargetedCompletionDate(taskRemarksRequest.getTargetedCompletionDate());

			if (task != null) {
				if (task.getTaskAssignments().stream().findFirst().isPresent()) {
					taskRemark.setGroupName(task.getTaskAssignments().stream().findFirst().get().getGroupName());
				}
				task.setIsJeopardyTask("Y".equalsIgnoreCase(taskRemarksRequest.getIsJeopardy()) ? (byte) 1 : (byte) 0);
				taskRemark.setServiceId(task.getServiceId());
				taskRepository.save(task);
				processTaskLogRepository.save(createProcessTaskLog(task, "REMARKS",
						taskRemarksRequest.getUserComments().length() > 500
								? taskRemarksRequest.getUserComments().substring(0, 500)
								: taskRemarksRequest.getUserComments(),
						null,null));
			} else {
				Optional<ScServiceDetail> optionalScservicedetails = scServiceDetailRepository
						.findById(taskRemarksRequest.getServiceId());
				if (optionalScservicedetails.isPresent()) {
					ScServiceDetail scServiceDetail = optionalScservicedetails.get();
					scServiceDetail.setIsJeopardyTask(
							"Y".equalsIgnoreCase(taskRemarksRequest.getIsJeopardy()) ? (byte) 1 : (byte) 0);
					scServiceDetailRepository.save(scServiceDetail);
					processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
							taskRemarksRequest.getUserComments().length() > 500
									? taskRemarksRequest.getUserComments().substring(0, 500)
									: taskRemarksRequest.getUserComments(),
							null, taskRemarksRequest));
				}

			}
			taskRemarkRepository.save(taskRemark);

			return true;
		}
		return false;
	}

	@Transactional(readOnly = false)
	public List<TaskRemarksJeopardyBean> getTaskRemarks(Integer taskId, String wfTaskId){
		Task task = getTaskByIdAndWfTaskId(taskId,wfTaskId);
		if(Objects.nonNull(task)){
			Set<TaskRemark> taskRemarks = task.getTaskRemarks();
			if(!CollectionUtils.isEmpty(taskRemarks)){
				return taskRemarks.stream()
						.map(TaskRemarksJeopardyBean::mapToBean)
						.collect(Collectors.toList());
			}
		}
		return new ArrayList<>();
	}

	@Transactional(readOnly = false)
	public List<TaskRemarksJeopardyBean> getTaskRemarksOnServiceId(String orderCode, String serviceCode, Integer serviceId) {
		List<TaskRemarksJeopardyBean> remarks = new ArrayList<>();
		ScServiceDetail scServiceDetail=getServiceDetailsByOrderCodeAndServiceDeatils(orderCode, serviceCode, serviceId);
		if (scServiceDetail!=null) {
			Byte isJeopardy = scServiceDetail.getIsJeopardyTask();
			List<TaskRemark> taskRemarks = taskRemarkRepository.findByServiceIdOrderByIdDesc(serviceId);
			if (!CollectionUtils.isEmpty(taskRemarks)) {

				taskRemarks.forEach(remark -> {
					remarks.add(constructTaskRemarks(remark, isJeopardy));
				});
			}
		}
		return remarks;

	}
	
	private TaskRemarksJeopardyBean constructTaskRemarks(TaskRemark taskRemark, Byte isJeopardy) {
		TaskRemarksJeopardyBean taskRemarksJeopardyBean = new TaskRemarksJeopardyBean();
		taskRemarksJeopardyBean.setUserComments(taskRemark.getRemarks());
		if (taskRemark.getTask() != null) {
			taskRemarksJeopardyBean.setTaskId(taskRemark.getTask().getId());
		}
		taskRemarksJeopardyBean.setUsername(taskRemark.getUsername());
		taskRemarksJeopardyBean.setCreatedDate(taskRemark.getCreatedDate());
		if (taskRemark.getTask() != null) {
			taskRemarksJeopardyBean.setIsJeopardy((taskRemark.getTask().getIsJeopardyTask() == (byte) 1) ? "Y" : "N");
		} else {
			if (isJeopardy != null) {
				taskRemarksJeopardyBean.setIsJeopardy((isJeopardy == (byte) 1) ? "Y" : "N");

			}
		}
		taskRemarksJeopardyBean.setGroupName(taskRemark.getGroupName());
		taskRemarksJeopardyBean.setJeopardyCategory(taskRemark.getJeopardyCategory());
		taskRemarksJeopardyBean.setCurrentJeopardyOwner(taskRemark.getCurrentJeopardyOwner());
		taskRemarksJeopardyBean.setTargetedCompletionDate(taskRemark.getTargetedCompletionDate());
		return taskRemarksJeopardyBean;
	}


	/**
	 * @param request
	 * @return
	 * @author
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public List<AssigneeResponse> updateBulkAssignee(AssigneeRequest request) throws TclCommonException {

		if(Objects.nonNull(request.getTaskIds()) && request.getTaskIds().isEmpty()){
			throw new TclCommonException(CommonConstants.ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		List<AssigneeResponse> assigneeResponses = new ArrayList<>();
		List<Task> tasks = taskRepository.findByIdIn(request.getTaskIds());
		if (Objects.nonNull(tasks) && !tasks.isEmpty()) {
			tasks.stream().forEach(task -> {
			if (task.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
				throw new TclCommonRuntimeException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.TASK_CLOSED,
						ResponseResource.R_CODE_ERROR);

			}
			task.setClaimTime(new Timestamp(new Date().getTime()));
			updateAssignmentDetails(task, request);
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			AssigneeResponse assigneeResponse = new AssigneeResponse();
			constructAssigneeResponse(task, request, assigneeResponse);
			assigneeResponses.add(assigneeResponse);
			taskRepository.save(task);

			});
		}

		return assigneeResponses;
	}
	
	/**
	 * This method is used to get the task details
	 *
	 * @param groupName
	 * @return
	 * @author vivek
	 */
	public List<AssignedGroupingBean> getTaskDetails(String groupName, String userName, String groupby,
			List<String> status, Integer serviceId,String cutomerName) throws TclCommonException {
		List<AssignedGroupingBean> assignedGroupingBeans = new ArrayList<>();

		Map<String, List<Task>> groupBasedAssignmnetTask = null;
		List<Task> tasks = taskRepository
				.findAll(TaskSpecification.getTaskFilter(groupName, status, userName, serviceId,null,null,null,null,null,null,null,null,cutomerName,null,null,null, null,false,null,null,null,null, null,false,null));
		if (groupby != null && groupby.equalsIgnoreCase(GroupByConstants.GROUPBYSTATUS)) {

			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstStatus().getCode()));
		} else {
			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstTaskDef().getName()));

		}
		AssignedGroupingBean assignedGroupingBean = new AssignedGroupingBean();
		assignedGroupingBean.setGroupName(groupName);
		groupBasedAssignmnetTask.forEach((taskName, taskList) -> {
			List<TaskGroupBean> taskGroupList = new ArrayList<>();
			TaskGroupBean taskGroup = new TaskGroupBean();
			taskGroup.setName(taskName);
			taskList.forEach(task -> {
				Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository
						.findById(task.getServiceId());
				if ((optionalServiceDetails.isPresent()
						&& !optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.TERMINATE)
						&& !optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.HOLD)
						&& !optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.CANCELLED))
						|| (task.getMstTaskDef().getKey().equalsIgnoreCase("confirm-deferred-delivery"))) {
					TaskBean taskBean = new TaskBean();
					if (optionalServiceDetails.get().getPriority() != null) {
						taskBean.setPriority(optionalServiceDetails.get().getPriority());
					} else {
						taskBean.setPriority(1F);
					}
					taskBean.setServiceDeliveryDate(optionalServiceDetails.get().getCommittedDeliveryDate());

					setTaskDetails(taskBean, task);
					taskGroup.getTaskBeans().add(taskBean);
					taskGroupList.add(taskGroup);
				}

			});
			taskGroup.getTaskBeans().sort(Comparator.comparing(TaskBean::getPriority).thenComparing(TaskBean::getDuedate).reversed());
			assignedGroupingBean.getTaskGroup().add(taskGroup);
		});
		assignedGroupingBeans.add(assignedGroupingBean);

		return assignedGroupingBeans;
	}
	@Transactional(readOnly = false)
	public Boolean processTerminationFlow(TerminationRequestBean terminationRequestBean) throws TclCommonException{

		Boolean terminateFlag=false;
		Optional<ScServiceDetail> scServiceDetail =scServiceDetailRepository.findById(terminationRequestBean.getServiceId());
		if(scServiceDetail.isPresent()) {
			LOGGER.info("Termination service details {}",scServiceDetail.get().getId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("terminationEffectiveDate", terminationRequestBean.getTerminationEffectiveDate());
			atMap.put("customerRequestorDate", terminationRequestBean.getCustomerRequestorDate());
			atMap.put("terminationFromTime",terminationRequestBean.getFromTime());
			atMap.put("etcValue", terminationRequestBean.getEtcValue());
			atMap.put("etcWaiver", terminationRequestBean.getEtcWaiver());
			atMap.put("terminationFlowTriggered",CommonConstants.YES);
			atMap.put("contractEndDate",terminationRequestBean.getContractEndDate());
			atMap.put("approvalMailAvailable",terminationRequestBean.getApprovalMailAvailable());
			atMap.put("backdatedTermination",terminationRequestBean.getBackdatedTermination());
			
			saveRemarksForTerminationAndCancellation(scServiceDetail.get(),true);
			componentAndAttributeService.updateAttributes(terminationRequestBean.getServiceId(), atMap, AttributeConstants.COMPONENT_LM, "A");

			if (terminationRequestBean.getDocumentIds() != null && !terminationRequestBean.getDocumentIds().isEmpty()) {
				terminationRequestBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(scServiceDetail.get(), attachmentIdBean.getAttachmentId()));
			}
			scServiceDetail.get().setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATION_INPROGRESS));
			updateServiceStatusAndCreatedNewStatus(scServiceDetail.get(), TaskStatusConstants.TERMINATION_INPROGRESS, TaskStatusConstants.TERMINATION_INPROGRESS);
			scServiceDetail.get().setTerminationFlowTriggered(CommonConstants.YES);
			scServiceDetail.get().setTerminationInitiationDate(new Timestamp(new Date().getTime()));
			scServiceDetail.get().setCustomerRequestorDate(terminationRequestBean.getCustomerRequestorDate());
			scServiceDetail.get().setTerminationEffectiveDate(terminationRequestBean.getTerminationEffectiveDate());
			scServiceDetailRepository.save(scServiceDetail.get());
			terminateFlag=true;
			if(terminateFlag && scServiceDetail.isPresent()){
			processTerminationFlowVariable(scServiceDetail.get(),terminationRequestBean.getTerminationEffectiveDate(),terminationRequestBean.getFromTime());
		}
	}
		return terminateFlag;
	}
	
	/**
	 * @author vivek
	 *
	 * @param cancellationRequestBean
	 * @return
	 * @throws TclCommonException 
	 */
	@Transactional(readOnly = false)
	public CancellationResponse processCancellationFlow(CancellationRequestBean cancellationRequestBean)
			throws TclCommonException {
		CancellationResponse response = new CancellationResponse();
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository
				.findById(cancellationRequestBean.getServiceId());
		if (scServiceDetail.isPresent()) {
			Map<String, Object> processVar = new HashMap<>();
			processVar.put("retainExistingNwresource", cancellationRequestBean.getRetainExistingNwresource());
			processVar.put("cancellationInitiatedBy", cancellationRequestBean.getCancellationInitiatedBy());
			processVar.put("cpeFlowRequired","No");
			processVar.put("isConfirmIPCancel",false);
			processVar.put("isCancelTxConfig",false);
			processVar.put("isRFExists",false);
			processVar.put("isCancelClr",false);

			isEligibleForCancellation(scServiceDetail.get().getId(),processVar,false);

			LOGGER.info("Termination service details {}", scServiceDetail.get().getId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("customerRequestorDate", cancellationRequestBean.getCustomerRequestorDate());
			atMap.put("cancellationFlowTriggered", CommonConstants.YES);
			atMap.put("cancellationReason", cancellationRequestBean.getCancellationReason());
			atMap.put("retainExistingNwresource", cancellationRequestBean.getRetainExistingNwresource());
			atMap.put("cancellationInitiatedBy", cancellationRequestBean.getCancellationInitiatedBy());

			saveRemarksForTerminationAndCancellation(scServiceDetail.get(), false);
			componentAndAttributeService.updateAttributes(cancellationRequestBean.getServiceId(), atMap,
					AttributeConstants.COMPONENT_LM, "A");

			if (cancellationRequestBean.getDocumentIds() != null && !cancellationRequestBean.getDocumentIds().isEmpty()) {
				cancellationRequestBean.getDocumentIds()
						.forEach(attachmentIdBean -> makeEntryInScAttachment(scServiceDetail.get(),
								attachmentIdBean.getAttachmentId()));
			}
			scServiceDetail.get().setCancellationInitiationDate(new Timestamp(new Date().getTime()));
			updateServiceStatusAndCreatedNewStatus(scServiceDetail.get(), TaskStatusConstants.CANCELLATION_INITIATED,
					TaskStatusConstants.CANCELLATION_INITIATED);
				scServiceDetail.get().setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CANCELLATION_INITIATED));
			scServiceDetail.get().setCustomerRequestorDate(cancellationRequestBean.getCustomerRequestorDate());
			scServiceDetailRepository.save(scServiceDetail.get());
			AttachmentBean attachmentBean = null;
			if (cancellationRequestBean.getDocumentIds() != null && cancellationRequestBean.getDocumentIds().stream()
					.filter(attachmentIdBean -> attachmentIdBean.getCategory().equalsIgnoreCase("CANCELEMAIL"))
					.findFirst().isPresent()) {
				Optional<Attachment> attachment = attachmentRepository.findById(cancellationRequestBean.getDocumentIds()
						.stream()
						.filter(attachmentIdBean -> attachmentIdBean.getCategory().equalsIgnoreCase("CANCELEMAIL"))
						.findFirst().get().getAttachmentId());
				if (attachment.isPresent()){
					LOGGER.info("cancellation service details attacment present {}", scServiceDetail.get().getId());
					attachmentBean = AttachmentBean.mapToBean(attachment.get());
				}
			}
			processCancellationFlowVariable(scServiceDetail.get(),processVar);
			if ("Customer".equalsIgnoreCase(cancellationRequestBean.getCancellationInitiatedBy())) {
				processNotifyCacellationForService(cancellationRequestBean, scServiceDetail.get(),
						"Cancellation Initiated for ", attachmentBean);
			}
			else if("Internal Changes".equalsIgnoreCase(cancellationRequestBean.getCancellationInitiatedBy())) {
				processNotifyCacellationForService(cancellationRequestBean,scServiceDetail.get(),"Internal Cancellation Initiated for ",attachmentBean);

			}
			else{
				processNotifyCacellationForService(cancellationRequestBean,scServiceDetail.get(),"Movement to M6 initiated ",attachmentBean);
			}

		}
		return response;
	}
	/**
	 * @author vivek
	 * @param resourceReleaseInprogress 
	 *
	 * @param id
	 * @param processVar,Integer serviceId 
	 * @throws TclCommonException 
	 */
	private void isEligibleForCancellation(Integer serviceId, Map<String, Object> processVar, boolean resourceReleaseInprogress) throws TclCommonException {
		
		List<Task> tasks=taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(serviceId, Arrays.asList("product-commissioning-jeopardy"), Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.CLOSED_STATUS,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));

		if(!tasks.isEmpty()) {
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
					ResponseResource.R_CODE_ERROR);
		}
	
		if(Objects.nonNull(processVar.get("retainExistingNwresource")) &&
				CommonConstants.YES.equalsIgnoreCase(String.valueOf(processVar.get("retainExistingNwresource")))) {
			
			processVar.put("cpeFlowRequired","No");
			processVar.put("isConfirmIPCancel",false);
			processVar.put("isCancelTxConfig",false);
			processVar.put("isRFExists",false);
			processVar.put("isCancelClr",false);
		} else {

			Map<String, Boolean> processVarForCancelFlow = cancelationFlow(serviceId);
			if (Objects.nonNull(processVarForCancelFlow) && !processVarForCancelFlow.isEmpty()) {
				if (processVarForCancelFlow.containsKey("bop") && processVarForCancelFlow.get("bop") == true) {
					LOGGER.info("isCancelClr service details true");
					processVar.put("isCancelClr", true);
				}
				if (processVarForCancelFlow.containsKey("cmip") && processVarForCancelFlow.get("cmip") == true) {
					LOGGER.info("isIPCancel service details true");
					processVar.put("isIPCancel", true);
				}
				if (processVarForCancelFlow.containsKey("rfnoc") && processVarForCancelFlow.get("rfnoc") == true) {
					LOGGER.info("isRFExists service details true");
					processVar.put("isRFExists", true);
				}
				if (processVarForCancelFlow.containsKey("sdnoc") && processVarForCancelFlow.get("sdnoc") == true) {
					LOGGER.info("isCancelTxConfig service details true");
					processVar.put("isCancelTxConfig", true);
				}
				if (processVarForCancelFlow.containsKey("asp") && processVarForCancelFlow.get("asp") == true) {
					LOGGER.info("isAspTask service details true");
					processVar.put("isAspTask", true);
				}
				if (processVarForCancelFlow.containsKey("scmml") && processVarForCancelFlow.get("scmml") == true) {
					LOGGER.info("cpeFlowRequired service details true");
					processVar.put("cpeFlowRequired", CommonConstants.YES);
				}
			}

		}
		
		List<String> releaseResourceTask=Arrays.asList("cpe-po-cancel","confirm-ip-cancel-configuration","cancel-tx-config","manual-rf-cancellation-configuration","cancel-offnet-po","cancel-clr","sales-negotiation","customer-hold-negotaition-CIM","sales-assist-ordering","sales-negotiation-SALES");
		
		List<Task> holdTask=taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId, Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));

		if(!holdTask.isEmpty()) {
			
			holdTask.forEach(hold -> {
				if (resourceReleaseInprogress ) {
					if( !releaseResourceTask.contains(hold.getMstTaskDef().getKey())) {
					hold.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
					}

				} else {
					if (!hold.getMstTaskDef().getKey().equalsIgnoreCase("sales-assist-ordering")) {
						hold.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
					}
				}

				taskRepository.save(hold);
			});
		}
		
	}


	private void saveRemarksForTerminationAndCancellation(ScServiceDetail scServiceDetail,boolean termination) {
		String userName = "";
		userName = userInfoUtils.getUserInformation().getUserId();

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		if(termination) {
		taskRemark.setRemarks("Termination request on :" + new Date() + " by user :" + userName);
		}
		else {
			taskRemark.setRemarks("Cancellation request on :" + new Date() + " by user :" + userName);

		}
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}
	
	
	private void saveRemarksForCancellation(ScServiceDetail scServiceDetail,boolean autoCancelation) {
		String userName = "";
		if(autoCancelation) {
			userName="AUTOCANCELLATION";
		}
		else {
			userName = userInfoUtils.getUserInformation().getUserId();

		}

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		
			taskRemark.setRemarks("Cancellation request on :" + new Date() + " by user :" + userName);

		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}
	private void processTerminationFlowVariable(ScServiceDetail scServiceDetail,String terminationEffectiveDate,String fromTime) throws TclCommonException{

		Map<String, Object> processVar = new HashMap<>();
		String serviceCode=scServiceDetail.getUuid();
		LOGGER.info("Termination Component service id {} ",scServiceDetail.getId());
		Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
				scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

		processVar.put("customerUserName", StringUtils.trimToEmpty(scServiceDetail.getScOrder().getErfCustCustomerName()));
		processVar.put(MasterDefConstants.ORDER_CODE, StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_CODE, StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_TYPE, StringUtils.trimToEmpty(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CATEGORY, StringUtils.trimToEmpty(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());


		processVar.put(MasterDefConstants.PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
		if(scServiceDetail.getErfPrdCatalogProductName().contains(CommonConstants.NPL) ||
				scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(CommonConstants.NPL)) {
			processVar.put("productType", StringUtils.trimToEmpty(CommonConstants.NPL));
		}
		processVar.put(MasterDefConstants.OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
		processVar.put(MasterDefConstants.SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
		processVar.put(MasterDefConstants.SERVICE_ID, scServiceDetail.getId());
		processVar.put(MasterDefConstants.SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));

		processVar.put(MasterDefConstants.CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
		processVar.put(MasterDefConstants.STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
		processVar.put(MasterDefConstants.COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_MOBILE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));

		String lastMileType=Objects.nonNull(scComponentAttributesAMap.get("lmType"))?
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType")):null;
		processVar.put(MasterDefConstants.LM_TYPE, lastMileType);
		processVar.put(MasterDefConstants.LM_CONNECTION_TYPE, Objects.nonNull(scComponentAttributesAMap.get("lmConnectionType"))?
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmConnectionType")):null);
		processVar.put("lastMileScenario", Objects.nonNull(scComponentAttributesAMap.get("lastMileScenario"))?
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastMileScenario")):null);
		
		processVar.put("terminationFlowTriggered","Yes");
		processVar.put("remainderCycle", "R60/PT24H");
		terminationEffectiveDate=getEffectiveDateForTermination(terminationEffectiveDate,fromTime);
		processVar.put("terminationEffectiveDate", terminationEffectiveDate);

		boolean offnetLm=false;

		if(Objects.nonNull(lastMileType) && !lastMileType.isEmpty()) {
			if (lastMileType.toLowerCase().contains("offnet")){
				offnetLm=true;
				processVar.put("parentLmType","offnet");
			} else {
				processVar.put("parentLmType",lastMileType);
			}
		}
		
		if(offnetLm) {
			updateOffnetPoForTermination(scServiceDetail);
		}
		runtimeService.startProcessInstanceByKey("termination-workflow", processVar);
	}

	private String getEffectiveDateForTermination(String terminationEffectiveDate,String fromTime) throws TclCommonException {
		try{
			LocalDateTime localDateTime=null;
			LOGGER.info("effective date for termination {} {} :",terminationEffectiveDate,fromTime);
			terminationEffectiveDate=terminationEffectiveDate+" "+fromTime;
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(terminationEffectiveDate);
			localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC"));
			terminationEffectiveDate=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			LOGGER.info("Derived effective date for termination::{}",terminationEffectiveDate);
			return terminationEffectiveDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForTermination:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	private String getEffectiveDateForHoldProcess(String terminationEffectiveDate) throws TclCommonException {
		try{
			LocalDateTime localDateTime=null;
			if(terminationEffectiveDate==null) {
				terminationEffectiveDate = DateUtil
						.convertDateToString(new Date(Timestamp.valueOf(LocalDateTime.now().plusDays(30)).getTime()));
			}
			LOGGER.info("getEffectiveDateForHoldProcess {} {} :",terminationEffectiveDate);
			terminationEffectiveDate=terminationEffectiveDate+" "+"00:00";
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			Date inputDate=inputDateFormatter.parse(terminationEffectiveDate);
			localDateTime=LocalDateTime.ofInstant(inputDate.toInstant(), ZoneId.of("UTC"));
			terminationEffectiveDate=localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			LOGGER.info("Derived getEffectiveDateForHoldProcess::{}",terminationEffectiveDate);
			return terminationEffectiveDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForHoldProcess:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void updateOffnetPoForTermination(ScServiceDetail scServiceDetail) {
		updateOffnetPoForTermination(scServiceDetail, AttributeConstants.SITETYPE_A, "A_END_LM");

		if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL") || scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NDE")) {
				
			updateOffnetPoForTermination(scServiceDetail, AttributeConstants.SITETYPE_B, "B_END_LM");
		}
	}


	private void updateOffnetPoForTermination(ScServiceDetail scServiceDetail, String siteType,
			String productComponent) {
		
		Stg0SapPoDtlOptimus stg0SapPoDtlOptimus = null;
		if(productComponent!=null && "B_END_LM".equalsIgnoreCase(productComponent)) {
			stg0SapPoDtlOptimusRepository.getDataByServiceIdSiteB(scServiceDetail.getUuid());
		}else {
			stg0SapPoDtlOptimus = stg0SapPoDtlOptimusRepository
				.findFirstByTclServiceIdAndProductComponentOrderByPoCreationDateDesc(scServiceDetail.getUuid());
		}
		if (Objects.nonNull(stg0SapPoDtlOptimus)) {
            LOGGER.info("updateOffnetPoForTermination's Stg0SapPoDtlOptimus details:{}",
                    stg0SapPoDtlOptimus);
        }
        else{
            LOGGER.info("updateOffnetPoForTermination's Stg0SapPoDtlOptimus details are not found");
        }

		if (Objects.nonNull(stg0SapPoDtlOptimus)) {
			if (StringUtils.isNotBlank(stg0SapPoDtlOptimus.getVendorNo())) {
				List<Stg0SfdcVendorC> vendorList = stgSfdcVendorRepository
						.findByVendorIdC(stg0SapPoDtlOptimus.getVendorNo());
				Stg0SfdcVendorC vendor = (Objects.nonNull(vendorList) && !vendorList.isEmpty()
						? vendorList.stream().findFirst().get()
						: null);
				LOGGER.info("updateOffnetPoForTermination for service :{} and vendor details:{}", vendor);

				if (Objects.nonNull(vendor)) {

					LOGGER.info("updateOffnetPoForTermination for service :{} and vendor details found:{}", vendor);

					Map<String, String> map = new HashMap<>();
					map.put("vendorName", vendor.getName());
					if(StringUtils.isNotBlank(vendor.getSfdcProviderNameC())) {
						map.put("sfdcProviderName", vendor.getSfdcProviderNameC());
					}else {
						map.put("sfdcProviderName", vendor.getName());
					}
					map.put("oldOffnetPoNumber", stg0SapPoDtlOptimus.getPoNumber());
					map.put("oldBsoCircuitId", stg0SapPoDtlOptimus.getVendorRefIdOrderId());
					map.put("vendorId", stg0SapPoDtlOptimus.getVendorNo());
					componentAndAttributeService.updateAttributes(scServiceDetail.getId(), map, "LM", siteType);
					LOGGER.info("updateOffnetPoForTermination's Stg0SapPoDtlOptimus details are updated for service Id : {}",scServiceDetail.getUuid());

				}
			}
		}
	}

	
	private void processCancellationFlowVariable(ScServiceDetail scServiceDetail, Map<String, Object> processVar)
			throws TclCommonException {

		String serviceCode = scServiceDetail.getUuid();
		LOGGER.info("inside processCancellationFlowVariable with service id : {} ", scServiceDetail.getId());
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
		processVar.put(SC_ORDER_ID, scServiceDetail.getScOrder().getId());
		processVar.put("customerUserName",
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getErfCustCustomerName()));
		processVar.put(MasterDefConstants.ORDER_CODE,
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_TYPE,
				StringUtils.trimToEmpty(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CATEGORY,
				StringUtils.trimToEmpty(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());
		LOGGER.info("inside processCancellationFlowVariable with order details : {} {} {}",
				scServiceDetail.getScOrder().getOpOrderCode(),scServiceDetail.getScOrder().getOrderType(),
				scServiceDetail.getScOrder().getOrderCategory());
		processVar.put(MasterDefConstants.PRODUCT_NAME,
				StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
		if (scServiceDetail.getErfPrdCatalogProductName().contains(CommonConstants.NPL)
				|| scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(CommonConstants.NPL)) {
			processVar.put("productType", StringUtils.trimToEmpty(CommonConstants.NPL));
		}
		processVar.put(MasterDefConstants.OFFERING_NAME,
				StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
		processVar.put(MasterDefConstants.SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
		processVar.put(MasterDefConstants.SERVICE_ID, scServiceDetail.getId());
		processVar.put(MasterDefConstants.SITE_ADDRESS,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
		LOGGER.info("inside processCancellationFlowVariable with service details : {} {} {} {}",
				scServiceDetail.getId(), scServiceDetail.getScOrder().getOrderType(),
				scComponentAttributesAMap.get("siteAddress"), scServiceDetail.getScOrder().getOrderCategory());
		processVar.put(MasterDefConstants.CITY,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
		processVar.put(MasterDefConstants.STATE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
		processVar.put(MasterDefConstants.COUNTRY,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_MOBILE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_NAME,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
		LOGGER.info("inside processCancellationFlowVariable with local IT details : {} {}",
				scComponentAttributesAMap.get("localItContactName"), scComponentAttributesAMap.get("destinationCity"));
		String lastMileType = Objects.nonNull(scComponentAttributesAMap.get("lmType"))
				? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"))
				: null;
		processVar.put(MasterDefConstants.LM_TYPE, lastMileType);
		processVar.put(MasterDefConstants.LM_CONNECTION_TYPE,
				Objects.nonNull(scComponentAttributesAMap.get("lmConnectionType"))
						? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmConnectionType"))
						: null);
		processVar.put("lastMileScenario",
				Objects.nonNull(scComponentAttributesAMap.get("lastMileScenario"))
						? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastMileScenario"))
						: null);
		LOGGER.info("inside processCancellationFlowVariable with last mile details : {} {} {}",
				lastMileType, scComponentAttributesAMap.get("lmConnectionType"),scComponentAttributesAMap.get("lastMileScenario"));
		processVar.put("cancellationFlowTriggered", "Yes");
		processVar.put("remainderCycle", "R60/PT24H");
		if (Objects.nonNull(lastMileType) && !lastMileType.isEmpty()) {
			if (lastMileType.toLowerCase().contains("offnet")) {
				processVar.put("parentLmType", "offnet");
			} else {
				processVar.put("parentLmType", lastMileType);
			}
		}

		LOGGER.info("Cancellation Flow Triggered succsessfully : {} ",processVar.toString());
		runtimeService.startProcessInstanceByKey("cancellation-interim-workflow", processVar);
	}
	@Transactional(readOnly = false)
	public Boolean cancelClrResource(Integer serviceId, String cancellationInitiatedBy, String retainExistingNwresource) throws TclCommonException{
		Boolean flag = false;
		boolean resourceWaitingTaskAvailable=false;
		Optional<ScServiceDetail> scServiceDetailOptional = scServiceDetailRepository.findById(serviceId);
		LOGGER.info("Cancel Service Id {} :", serviceId);
		if (scServiceDetailOptional.isPresent()) {
			ScServiceDetail scServiceDetail = scServiceDetailOptional.get();
			if (scServiceDetail.getMstStatus().getCode()
					.equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED_INITIATED)
					|| cancellationInitiatedBy != null
							&& SalesNegotiationConstants.AUTORESOURCECANCELLATION.equalsIgnoreCase(cancellationInitiatedBy)) {
				LOGGER.info("Inside MRESOURCE_RELEASED_INITIATED block Cancel process with Service Id {} :", serviceId);
				try {
				scServiceDetail.setUpdatedBy(Utils.getSource());
				scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
				
				Task resourceReleaseWaiting = taskRepository
						.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(scServiceDetail.getId(),
								"resource-release-completion-waiting");
				if (resourceReleaseWaiting != null
						&& (resourceReleaseWaiting.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED)
								|| resourceReleaseWaiting.getMstStatus().getCode()
										.equalsIgnoreCase(TaskStatusConstants.INPROGRESS)
								|| resourceReleaseWaiting.getMstStatus().getCode()
										.equalsIgnoreCase(TaskStatusConstants.REOPEN))) {

					Map<String, Object> processVar = new HashMap<>();
					processVar.put("retainExistingNwresource", "No");
					processVar.put("cpeFlowRequired", "No");
					processVar.put("isConfirmIPCancel", false);
					processVar.put("isCancelTxConfig", false);
					processVar.put("isRFExists", false);
					processVar.put("isCancelClr", false);
					processVar.put("SALES_ORDER","CLOSE");
					resourceWaitingTaskAvailable = true;
					runtimeService.trigger(resourceReleaseWaiting.getWfTaskId(), processVar);

				}
				}
				catch (Exception e) {
					LOGGER.error("resourceReleaseWaiting closure error service id:{} and error:{}",scServiceDetail.getUuid(),e);
				}
				if (!resourceWaitingTaskAvailable) {
					updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.RESOURCE_RELEASED,
							"RESOURCE-RELEASED");
					scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.RESOURCE_RELEASED));
					scServiceDetail.setServiceStatus(TaskStatusConstants.RESOURCE_RELEASED);
					scServiceDetail.setServiceConfigStatus(TaskStatusConstants.RESOURCE_RELEASED);
					scServiceDetail.setServiceConfigDate(new Timestamp(new Date().getTime()));
				}

			} else if (cancellationInitiatedBy != null && "Move to M6".equalsIgnoreCase(cancellationInitiatedBy)) {
				LOGGER.info("Inside M6 Cancel process with Service Id {} :", serviceId);
				updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.MOVETOM6,
						TaskStatusConstants.MOVETOM6);
				scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.MOVETOM6));
				scServiceDetail.setServiceStatus(TaskStatusConstants.MOVETOM6);
				scServiceDetail.setServiceConfigStatus(TaskStatusConstants.MOVETOM6);
			}

			else if (cancellationInitiatedBy != null && "customer".equalsIgnoreCase(cancellationInitiatedBy)) {
				LOGGER.info("Inside Customer Cancel process with Service Id {} :", serviceId);
				updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.CANCELLED, "CANCELREQUEST");
				scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CANCELLED));
				scServiceDetail.setUpdatedBy(Utils.getSource());
				scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
				scServiceDetail.setServiceStatus(TaskStatusConstants.CANCELLED);
				scServiceDetail.setServiceConfigStatus(TaskStatusConstants.CANCELLED);
				scServiceDetail.setServiceConfigDate(new Timestamp(new Date().getTime()));
				flag = true;
				if(scServiceDetail.getOrderSubCategory() != null && scServiceDetail.getOrderSubCategory().toLowerCase().contains("parallel")) {
					mqUtils.send(macdDetailCommissioned, scServiceDetail.getParentUuid());
				}else {
					mqUtils.send(macdDetailCommissioned, scServiceDetail.getUuid());
				}

			}

			scServiceDetail.setUpdatedBy(Utils.getSource());
			scServiceDetail.setUpdatedDate(new Timestamp(new Date().getTime()));
			scServiceDetail.setServiceConfigDate(new Timestamp(new Date().getTime()));
			flag = true;
			scServiceDetailRepository.save(scServiceDetail);

			if (!resourceWaitingTaskAvailable) {
				triggerMailToServiceCancellation(serviceId, scServiceDetail.getUuid(),
						scServiceDetail.getScOrder().getOpOrderCode(), cancellationInitiatedBy,
						retainExistingNwresource);

			}

		}
		return flag;
	}
	
	private void triggerMailToServiceCancellation (Integer serviceId,String serviceCode,String orderCode,String cancellationInitiatedBy,String retainExistingNwresource){
        try {
            LOGGER.info("Inside Cancellation mail trigger for service : {} {}",serviceCode,orderCode);
            List<String> emailIdsList=new ArrayList<>();
            Map<String,String> scComponentAttributesAMap= commonFulfillmentUtils.getComponentAttributesDetails(Arrays.asList("isCancellationForHold", "pmEmailId", "salesSupportEmailId", "salesPersonEmailId"),serviceId,"LM", "A" );

            if(cancellationInitiatedBy!=null && "Move to M6".equalsIgnoreCase(cancellationInitiatedBy)) {
                emailIdsList=getTaskRegionByGrroup("CANCEL_SERVICE");

            }
            if(scComponentAttributesAMap.get("pmEmailId")!=null){
                emailIdsList.add(scComponentAttributesAMap.get("pmEmailId"));
            }
            if(scComponentAttributesAMap.get("salesPersonEmailId")!=null) {
                emailIdsList.add(scComponentAttributesAMap.get("salesPersonEmailId"));
            }
            if(scComponentAttributesAMap.get("salesSupportEmailId")!=null) {
                emailIdsList.add(scComponentAttributesAMap.get("salesSupportEmailId"));
            }
            LOGGER.info("Cancellation user emailid list : {} ", emailIdsList);
            notificationService.notifyServiceCancellation(emailIdsList, emailIdsList, serviceCode, orderCode,cancellationInitiatedBy,retainExistingNwresource);

        }catch (Exception ex){
            LOGGER.error("Error while mail trigger to admin for Service Cancellation : {} ",ex );
        }
    }
	
	public List<String> getTaskRegionByGrroup(String groupName){
	    List<String> mailIdList=new ArrayList<>();
		List<MstTaskRegion> mstTaskRegionListCIM = mstTaskRegionRepository
				.findByGroup(groupName);
        LOGGER.info("Group name to get email id list {} :",groupName);
		if (!mstTaskRegionListCIM.isEmpty()) {
			mailIdList.addAll(
					mstTaskRegionListCIM.stream().filter(region -> region.getEmail() != null)
							.map(region -> region.getEmail()).collect(Collectors.toList()));
			LOGGER.info("Email ids list are {} :",mailIdList);
		}
		return mailIdList;
	}
	
	public Map<String, Boolean> cancelationFlow(Integer serviceId) {
		Map<String, Boolean> cancelMap = new HashMap<>();
		List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId,
				Arrays.asList(TaskStatusConstants.CLOSED_STATUS));
		List<String> status = tasks.stream().map(t -> t.getMstTaskDef().getKey()).collect(Collectors.toList());
		if (status.contains("enrich-service-design") || status.contains("enrich-service-design-jeopardy")) {
			cancelMap.put("bop", true);
		}
		if (status.contains("manual-service-configuration") || status.contains("service-configuration")) {
			cancelMap.put("cmip", true);
		}
		if (status.contains("manual-rf-configuration") || status.contains("rf-configuration")) {
			cancelMap.put("rfnoc", true);
		}
		if (status.contains("tx-configuration-sdh")||status.contains("tx-configuration-mpls")||status.contains("tx-configurations")||status.contains("tx-sdh-configuration-manual") || status.contains("tx-mpls-configuration-manual")) {
			cancelMap.put("sdnoc", true);
		}
		
		if (status.contains("offnet-po") || status.contains("provide-po-colo") || status.contains("po-offnet-lm-provider") ) {
			cancelMap.put("asp", true);
		}
		if(status.contains("cpe-license-pr") || status.contains("cpe-hardware-pr")
			|| status.contains("provide-wbsglcc-details")){
			cancelMap.put("scmml", true);
		}

		return cancelMap;
	}
	
	private String getParallelTerminationDate(String terminationDate) throws TclCommonException {
		try{
			LOGGER.info("effective date for termination {} {} :",terminationDate);
			DateFormat inputDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			inputDateFormatter.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
			LocalDateTime inputLocalTime=LocalDateTime.ofInstant(inputDateFormatter.parse(terminationDate).toInstant(), ZoneId.of("UTC"));
			LocalDateTime currentLocalTime=LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.of("UTC")).plusMinutes(15);
			LOGGER.info("Current Local Time :{}",currentLocalTime);
			LOGGER.info("Input Local Time :{}",inputLocalTime);
			if(inputLocalTime.isAfter(currentLocalTime)) {
				terminationDate=inputLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			}else {
				terminationDate=currentLocalTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'"));
			}
			
			LOGGER.info("Derived effective date for termination::{}",terminationDate);
			return terminationDate;

		} catch (Exception ex) {
			LOGGER.error("Exception for getEffectiveDateForTermination:{}", ex);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ex, ResponseResource.R_CODE_ERROR);
		}
	}
	
	public String optimusDateMinusDays(String commDate, Integer days) {
		Date date = null;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(commDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LocalDateTime ldt = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).minusDays(days);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(out);
	}
	public void processNotifyStatusChanage(CimHoldRequest request, ScServiceDetail scServiceDetail, String subject,AttachmentBean attachmentBean) {
		try {
			LOGGER.info("Inside processNotifyStatusChanage method");
			if (request.getIsCancellation() != null
					&& CommonConstants.YES.equalsIgnoreCase(request.getIsCancellation())) {
				LOGGER.info("Email ids ,isCancellation and serviceId : {} {} {} ", request.getPmEmailId(),
						request.getIsCancellation(), scServiceDetail.getId());
				List<String> toEmailIds = Arrays.asList(scServiceDetail.getAssignedPM(), request.getSalesPersonEmailId(),
						request.getSalesSupportEmailId());
				Map<String, String> atMap = new HashMap<>();
				atMap.put("isCancellationForHold", CommonConstants.YES);
				if (scServiceDetail.getAssignedPM() != null) {
					atMap.put("pmEmailId", scServiceDetail.getAssignedPM());

				}
				if (request.getSalesPersonEmailId() != null) {
					atMap.put("salesPersonEmailId", request.getSalesPersonEmailId());
				}
				if (request.getSalesSupportEmailId() != null) {
					atMap.put("salesSupportEmailId", request.getSalesSupportEmailId());
				}
				componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
						AttributeConstants.COMPONENT_LM, "A");
				notificationService.notifyServiceStatusChange(toEmailIds, null,subject,
						scServiceDetail.getScOrder().getOpOrderCode(), scServiceDetail.getUuid(),
						request.getHoldReason(),null,null, attachmentBean);
			}
		} catch (Exception ex) {
			LOGGER.error("Error in processNotifyStatusChanage : {}", ex);

		}
	}

	public void processNotifyCacellationForService(CancellationRequestBean cancellationRequestBean,
												   ScServiceDetail scServiceDetail, String subject, AttachmentBean attachmentBean) {
		try {

			LOGGER.info("Email ids and serviceId of processNotifyCacellationForService : {} {}",
					cancellationRequestBean.getPmEmailId(), scServiceDetail.getId());
			List<String> toEmailIds = new ArrayList<>();
			List<String> ccEmailIds = new ArrayList<>();
			Map<String, String> atMap = new HashMap<>();
			if (scServiceDetail.getAssignedPM() != null) {
				atMap.put("pmEmailId", scServiceDetail.getAssignedPM());
				toEmailIds.add(scServiceDetail.getAssignedPM());

			}
			if ("Customer".equalsIgnoreCase(cancellationRequestBean.getCancellationInitiatedBy())
					&& cancellationRequestBean.getSalesPersonEmailId() != null) {
				atMap.put("salesPersonEmailId", cancellationRequestBean.getSalesPersonEmailId());
				toEmailIds.add(cancellationRequestBean.getSalesPersonEmailId());

			}
			if ("Customer".equalsIgnoreCase(cancellationRequestBean.getCancellationInitiatedBy())
					&& cancellationRequestBean.getSalesSupportEmailId() != null) {
				atMap.put("salesSupportEmailId", cancellationRequestBean.getSalesSupportEmailId());
				toEmailIds.add(cancellationRequestBean.getSalesSupportEmailId());

			}
			if ("Move to M6".equalsIgnoreCase(cancellationRequestBean.getCancellationInitiatedBy())) {
				toEmailIds.add("OPTIMUS-O2C-SUPPORT@tatacommunications.onmicrosoft.com");
				toEmailIds.add("naison.cheruparamban@tatacommunications.com");
				ccEmailIds.add("S.PONNALAGU@tatacommunications.com");
				ccEmailIds.add("ayyappillai.p@tatacommunications.com");
				ccEmailIds.add("Ramkumar.Moorthy@contractor.tatacommunications.com");
			}
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
					AttributeConstants.COMPONENT_LM, "A");
			notificationService.notifyServiceStatusChange(toEmailIds, ccEmailIds, subject,
					scServiceDetail.getScOrder().getOpOrderCode(), scServiceDetail.getUuid(),
					cancellationRequestBean.getCancellationReason(), cancellationRequestBean.getCancellationInitiatedBy(),
					cancellationRequestBean.getRetainExistingNwresource(), attachmentBean);

		}

		catch (Exception ex) {
			LOGGER.error("Error in processNotifyCacellationForService : {}", ex);

		}
	}

	public List<String> getSaleSupportEmailIds(){
		return mstTaskRegionRepository.findByGroup("SALES_SUPPORT").
				stream().map(salesSupportEmail->salesSupportEmail.getEmail()).collect(Collectors.toList());
	}


	@Transactional(readOnly = false)
	public SalesNegotiationBean processNegotiationTask(SalesNegotiationBean salesNegotiationBean)
			throws TclCommonException {
		String userName = userInfoUtils.getUserInformation().getUserId();
		
		Task task = getTaskByIdAndWfTaskId(salesNegotiationBean.getTaskId(), salesNegotiationBean.getWfTaskId());
		ScServiceDetail scServiceDetail = task.getScServiceDetail();
		
		Map<String, String> varMap = new HashMap<>();
		Map<String, Object> flowMap = new HashMap<>();
		varMap.put("NegotiationProcessIntiationType", salesNegotiationBean.getProcessInitiationType());
		
		boolean taskClose=true;
		
		if(scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.JEOPARDY_INITIATED) && (salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Order Amendment")||salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Cancellation"))){
			taskClose=false;
		}
		
		//Check process initiation type
		if (salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Order Amendment")) {
			
			varMap.put("salesNegotiationOrderAmendmentCode", salesNegotiationBean.getOrderAmendmentCode());
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.AMENDED,
					TaskStatusConstants.AMENDED);
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.AMENDED));

			saveTaskRemarks(scServiceDetail, "sales team has process order amendment on :" + new Date() + " by user :"
					+ userName + " " + "and order code is: " + salesNegotiationBean.getOrderAmendmentCode(), userName);
            processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, salesNegotiationBean.getDelayReason(), null,
                    salesNegotiationBean);
			
		} else if (salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Cancellation")) {

			
			if(!scServiceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CANCELLATION_INITIATED)) {
				throw new TclCommonException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANCELLLATION_NOT_PROCESSED_IN_O2C,
						ResponseResource.R_CODE_ERROR);
			}
			varMap.put("salesNegotiationCancellationOrderCode", salesNegotiationBean.getCancellationOrderCode());
			saveTaskRemarks(
					scServiceDetail, "sales team has process order cancellation on  :" + new Date() + " by user :"
							+ userName + " " + "and order code is: " + salesNegotiationBean.getCancellationOrderCode(),
					userName);
            processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, salesNegotiationBean.getDelayReason(), null,
                    salesNegotiationBean);
		} else if (salesNegotiationBean.getProcessInitiationType().equalsIgnoreCase("Move To Service Delivery")) {
			//If initiation type is Move To Service Delivery then UnHold the service
			unholdServiceFromSalesTeam(scServiceDetail);
            saveTaskRemarks(
                    scServiceDetail, "sales team has process order Move To Service Delivery  on  :" + new Date() + " by user :"
                            + userName + " " + "and order code is: " + salesNegotiationBean.getCancellationOrderCode(),
                    userName);
            processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, salesNegotiationBean.getDelayReason(), null,
                    salesNegotiationBean);
		} 
		flowMap.put("SALES_ORDER", "CLOSE");

		componentAndAttributeService.updateAttributes(scServiceDetail.getId(), varMap, AttributeConstants.COMPONENT_LM,
				"A");
		scServiceDetailRepository.save(scServiceDetail);
		processTaskLogDetails(task, TaskStatusConstants.CLOSED_STATUS, salesNegotiationBean.getDelayReason(), null,
				salesNegotiationBean);
		if(taskClose) {
			return (SalesNegotiationBean) flowableBaseService.taskDataEntry(task, salesNegotiationBean, flowMap);
		}
		else {
			return salesNegotiationBean;
		}

	}
	
	private void unholdServiceFromSalesTeam(ScServiceDetail scServiceDetail) {
		ServiceStatusDetails serviceStatusDetails = updateServiceStatusAndCreatedNewStatus(scServiceDetail,
				TaskStatusConstants.INPROGRESS, "UNHOLDREQUESTFROMSALES");
		Timestamp crfsDate = null;
		if (serviceStatusDetails != null && serviceStatusDetails.getServiceChangeCategory() != null
				&& "Customer Dependency".equalsIgnoreCase(serviceStatusDetails.getServiceChangeCategory()) || "Deferred Delivery".equalsIgnoreCase(serviceStatusDetails.getServiceChangeCategory())) {
			
			crfsDate = updateCrfsDateAndRrfsDate(scServiceDetail, serviceStatusDetails.getStartTime());
		}
		scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
		scServiceDetailRepository.save(scServiceDetail);
		
		// Send Notification Email
		notifyUnHoldFromSalesteam(scServiceDetail, crfsDate);
	}


	private void notifyUnHoldFromSalesteam(ScServiceDetail scServiceDetail, Timestamp crfsDate) {
		List<String> toAddresses = new ArrayList<>();
		List<String> ccAddresses = new ArrayList<>();
		ScComponentAttribute customerContactEmailId = scComponentAttributesRepository
				.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
						scServiceDetail.getId(), localItContactEmailId,  AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
		if(customerContactEmailId != null && customerContactEmailId.getAttributeValue() != null) {
			toAddresses.add(customerContactEmailId.getAttributeValue());
		} else {
			LOGGER.info("Customer contact Email Id is not there for given service Id " + scServiceDetail.getId());
			return;
		}
		if(scServiceDetail.getAssignedPM() != null) {
			ccAddresses.add(scServiceDetail.getAssignedPM());
		}
		notificationService.notifyDynamicContent(toAddresses, ccAddresses, NOTIFY_SERVICE_UNHOLD_FROM_SALES_TEAM_SUBJECT,
				scServiceDetail.getScOrder().getOpOrderCode(), scServiceDetail.getUuid(), crfsDate, NOTIFY_SERVICE_UNHOLD_FROM_SALES_TEAM_MAIL_CONTENT);
	}
	
	private void saveTaskRemarks(ScServiceDetail scServiceDetail, String remarks, String userName) {

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks(remarks);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}
	
	
	@Transactional(readOnly = false)
	public PNRDetails getPNRDetails(Integer serviceId) {
		
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(serviceId);
		PNRDetails pnrDetails = new PNRDetails();
		if (optionalScServiceDetail.isPresent()) {
			ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
			
			List<ScComponentAttribute> scComponentAttributes = getLastMileAttributes(serviceId, scServiceDetail);
			
			if(!scComponentAttributes.isEmpty()) {
				List<Task> tasks = taskRepository.findByServiceIdAndMstStatus_codeIn(serviceId,
						Arrays.asList(TaskStatusConstants.CLOSED_STATUS, TaskStatusConstants.OPENED,
								TaskStatusConstants.REOPEN, TaskStatusConstants.HOLD));
				if(tasks != null && !tasks.isEmpty()) {
					
					boolean isPnrCrossed = checkPnrIsCrossed(scComponentAttributes, tasks, pnrDetails);
					
					if(isPnrCrossed) {
						LOGGER.info("The given service Id is PNR crossed : " + serviceId);
						pnrDetails.setPnrCrossed("Yes");
						List<PNRTaskDetails> pnrTaskDetails = getPnrTaskdetails(tasks);
						pnrDetails.setPnrTaskDetails(pnrTaskDetails);
						
					} else {
						pnrDetails.setPnrCrossed("No");
						LOGGER.info("The given service Id is not crossed PNR : " + serviceId);
					}
				} else {
					LOGGER.info("There is no task is closed/Opened for given service Id : " + serviceId);
				}
				
			} else {
				LOGGER.info(" Last mile Scenario is not there for given service Id : " + serviceId);
			}
			
		} else {
			LOGGER.error(" Service Detail is not found for given service Id : " + serviceId);
		}
		
		
		return pnrDetails;
		
	}


	private List<PNRTaskDetails> getPnrTaskdetails(List<Task> tasks) {
		return tasks.stream().map(task -> {
			PNRTaskDetails pnrTaskDetail = new PNRTaskDetails();
			pnrTaskDetail.setCompletedTime(task.getCompletedTime());
			pnrTaskDetail.setCreatedTime(task.getCreatedTime());
			pnrTaskDetail.setName(task.getMstTaskDef().getName());
			pnrTaskDetail.setStatus(task.getMstStatus().getCode());
			pnrTaskDetail.setTaskDefKey(task.getMstTaskDef().getKey());
			pnrTaskDetail.setSiteType(task.getSiteType());
			pnrTaskDetail.setGroupName(task.getMstTaskDef().getAssignedGroup());

			return pnrTaskDetail;
		}).collect(Collectors.toList());
	}


	private boolean checkPnrIsCrossed(List<ScComponentAttribute> scComponentAttributes, List<Task> tasks, PNRDetails pnrDetails) {
		ScComponentAttribute scComponentAttribute = scComponentAttributes.get(0); 
		
		
		
		boolean isPnrCrossedAStite = false;
		
		List<String> pnrTaskKeys = PNRScenarios.pnrScenarioAndTaskMap.get(scComponentAttribute.getAttributeValue());
		
		if(pnrTaskKeys != null && !pnrTaskKeys.isEmpty()) {
			 isPnrCrossedAStite = tasks.stream()
					.anyMatch(task -> pnrTaskKeys
							.stream().anyMatch(pnrTaskKey -> pnrTaskKey.equals(task.getMstTaskDef().getKey())));
		}

		if(isPnrCrossedAStite) {
			pnrDetails.setPnrCrossedForASite("Yes");
		} else {
			pnrDetails.setPnrCrossedForASite("No");
		}
		
		LOGGER.info("Last mile scenario for Stie Type A, service Id  is : " + scComponentAttribute.getScServiceDetailId() + " is "
				+ scComponentAttribute.getAttributeValue());
		
		boolean isPnrCrossedBStite = false;
		
		if(scComponentAttributes.size() > 1) {
			ScComponentAttribute scComponentAttribute2 = scComponentAttributes.get(1); 
			isPnrCrossedBStite = tasks.stream()
					.anyMatch(task -> PNRScenarios.pnrScenarioAndTaskMap.get(scComponentAttribute2.getAttributeValue())
							.stream().anyMatch(taskAtrr -> taskAtrr.equals(task.getMstTaskDef().getKey())));
			if(isPnrCrossedBStite) {
				pnrDetails.setPnrCrossedForBSite("Yes");
			} else {
				pnrDetails.setPnrCrossedForBSite("No");
			}
			
			LOGGER.info("Last mile scenario for Stie Type B, service Id  is : " + scComponentAttribute2.getScServiceDetailId() + " and scenario is "
					+ scComponentAttribute2.getAttributeValue());
			
		}
		return isPnrCrossedAStite || isPnrCrossedBStite ? true: false ;
	}


	private List<ScComponentAttribute> getLastMileAttributes(Integer serviceId, ScServiceDetail scServiceDetail) {
		List<ScComponentAttribute> scComponentAttributes = new ArrayList<>();
		
		if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IAS") ||
				scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceId, "lastMileScenario", AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
			if(scComponentAttribute != null) {
				scComponentAttributes.add(scComponentAttribute);
			}
		} else {
			ScComponentAttribute scComponentAttributeStieA = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceId, "lastMileScenario", AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_A);
			if(scComponentAttributeStieA != null) {
				scComponentAttributes.add(scComponentAttributeStieA);
			}
			ScComponentAttribute scComponentAttributeStieB = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(
							serviceId, "lastMileScenario", AttributeConstants.COMPONENT_LM, AttributeConstants.SITETYPE_B);
			if(scComponentAttributeStieB != null) {
				scComponentAttributes.add(scComponentAttributeStieB);
			}
		}
		return scComponentAttributes;
	}

	private Timestamp updateCrfsDateAndRrfsDate(ScServiceDetail scServiceDetail, Timestamp holdStartTime) {
		Timestamp currentDate = new Timestamp(new Date().getTime());
		LOGGER.info("Inside updateCrfsDateAndRrfsDate with service code and holdStartTime :{} {}",
				scServiceDetail.getUuid(), holdStartTime);
		if (Objects.nonNull(holdStartTime)) {
			LOGGER.info("Inside updateCrfsDateAndRrfsDate with CRFS date :{}",
					scServiceDetail.getServiceCommissionedDate());

			if (scServiceDetail.getServiceCommissionedDate() != null && currentDate.after(holdStartTime)) {
				Timestamp crfsDate = Timestamp
						.valueOf(scServiceDetail.getServiceCommissionedDate().toLocalDateTime().plusDays(ChronoUnit.DAYS
								.between(holdStartTime.toLocalDateTime(), currentDate.toLocalDateTime())));
				updateServiceLogs(scServiceDetail.getServiceCommissionedDate().toString(), crfsDate.toString(),
						scServiceDetail.getId(), "CRFS");
				scServiceDetail.setServiceCommissionedDate(crfsDate);
			}
		}
		scServiceDetailRepository.save(scServiceDetail);
		return scServiceDetail.getServiceCommissionedDate();
	}

	@Transactional(readOnly = false)
	public CancellationResponse processCancellationFromL2O(ScServiceDetail scServiceDetail, String cancellationInitiatedBy, String cancellationReason, CancellationRequest cancellationRequest,boolean resourceReleaseInprogress,String customerRequestorDate)throws TclCommonException {
		CancellationResponse response = new CancellationResponse();
		Map<String, Object> processVar = new HashMap<>();
		processVar.put("retainExistingNwresource", "No");
		processVar.put("cancellationInitiatedBy", cancellationInitiatedBy);
		processVar.put("cpeFlowRequired","No");
		processVar.put("isConfirmIPCancel",false);
		processVar.put("isCancelTxConfig",false);
		processVar.put("isRFExists",false);
		processVar.put("isCancelClr",false);
		processVar.put("isBillingRequired", false);
		processVar.put("resourceReleaseCycle", resourceReleaseCycle);

		if (resourceReleaseInprogress) {
			processVar.put("resourceReleaseInprogress", "Yes");

		}
		try {
			isEligibleForCancellation(scServiceDetail.getId(), processVar,resourceReleaseInprogress);

			LOGGER.info("Cancellation service details {}", scServiceDetail.getId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("o2cCancellationTriggerDate",DateUtil.convertDateToString(new Date()));
			atMap.put("cancellationFlowTriggered", CommonConstants.YES);
			atMap.put("cancellationReason", cancellationReason);
			atMap.put("retainExistingNwresource", "No");
			atMap.put("cancellationInitiatedBy", cancellationInitiatedBy);

			if(cancellationRequest!=null){
				atMap.put("leadToRFSDays",cancellationRequest.getLeadToRFSDays());
				atMap.put("effectiveDateOfChange",DateUtil.convertDateToString(cancellationRequest.getEffectiveDateOfChange()));
				atMap.put("chargesAbsorbedOrpassed",cancellationRequest.getChargesAbsorbedOrpassed());
				atMap.put("cancellationCreatedBy", cancellationRequest.getCancellationCreatedBy());
				if(cancellationRequest.getChargesAbsorbedOrpassed()!=null && "Passed On".equalsIgnoreCase(cancellationRequest.getChargesAbsorbedOrpassed())
						&& cancellationRequest.getCancellationCharges()!=null && cancellationRequest.getCancellationCharges()>0){
					atMap.put("cancellationCharges",String.valueOf(cancellationRequest.getCancellationCharges()));
				}
				if(Objects.nonNull(cancellationRequest.getCancellationCharges()) && cancellationRequest.getCancellationCharges()>0){
					processVar.put("isBillingRequired", true);
				}
			}
			
			if ("Sale Negotiation Team".equalsIgnoreCase(cancellationInitiatedBy)) {
				processVar.put("isBillingRequired", false);

			}

			saveRemarksForTerminationAndCancellation(scServiceDetail, false);
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
					AttributeConstants.COMPONENT_LM, "A");
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.CANCELLATION_INITIATED, TaskStatusConstants.CANCELLATION_INITIATED);
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CANCELLATION_INITIATED));
			scServiceDetail.setCancellationFlowTriggered(CommonConstants.YES);
			scServiceDetail.setCancellationInitiationDate(new Timestamp(new Date().getTime()));
			scServiceDetail.setCustomerRequestorDate(customerRequestorDate);
			scServiceDetailRepository.save(scServiceDetail);

			processCancellationFlowVariable(scServiceDetail, processVar);
			notifyServiceCancellationInitiation(scServiceDetail);
		}catch (Exception ex){
			LOGGER.error("Error in processCancellationFromL20 service id : {}",scServiceDetail.getUuid());
			LOGGER.error("Error in processCancellationFromL20 with error : {}",ex);

			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANCELLLATION_NOT_PROCESSED_IN_O2C,
					ResponseResource.R_CODE_ERROR);
		}
		return response;
	}

	@Transactional(readOnly = false)
	public ConfirmDeferredDeliveryBean confirmDeliveryTask(
			ConfirmDeferredDeliveryBean confirmDeferredDeliveryBean) throws TclCommonException {
		Map<String, Object> processVar = new HashMap<>();
		Task task = getTaskByIdAndWfTaskId(confirmDeferredDeliveryBean.getTaskId(),
				confirmDeferredDeliveryBean.getWfTaskId());
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository
				.findById(task.getScServiceDetail().getId());
		if (optionalScServiceDetail.isPresent()) {
			ScServiceDetail scServiceDetail=optionalScServiceDetail.get();
			if (confirmDeferredDeliveryBean.getAction() != null
					&& "Resume Service".equalsIgnoreCase(confirmDeferredDeliveryBean.getAction())) {
				Timestamp currentDate=new Timestamp(new Date().getTime());
				updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.INPROGRESS,"DEFERRED-DELIVER-RESUME");
				scServiceDetail
						.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
				if(scServiceDetail.getTenatativeHoldResumeDate().after(currentDate)){
				Timestamp crfsDate=	Timestamp.valueOf(
						scServiceDetail.getServiceCommissionedDate().toLocalDateTime().minusDays(ChronoUnit.DAYS.between(
									currentDate.toLocalDateTime(),scServiceDetail.getServiceCommissionedDate().toLocalDateTime())));
					updateServiceLogs(scServiceDetail.getServiceCommissionedDate().toString(), crfsDate.toString(),
							scServiceDetail.getId(), "CRFS");
					scServiceDetail.setServiceCommissionedDate(crfsDate);
				} else {

					Timestamp crfsDate = Timestamp
							.valueOf(scServiceDetail.getServiceCommissionedDate().toLocalDateTime()
									.plusDays(ChronoUnit.DAYS.between(
											scServiceDetail.getServiceCommissionedDate().toLocalDateTime(),
											currentDate.toLocalDateTime())));
					updateServiceLogs(scServiceDetail.getServiceCommissionedDate().toString(), crfsDate.toString(),
							scServiceDetail.getId(), "CRFS");
					scServiceDetail.setServiceCommissionedDate(crfsDate);

				}
				processVar.put("startSalesNegotiationFlow", CommonConstants.NO);

			} else if (confirmDeferredDeliveryBean.getAction() != null
					&& "Reject Service".equalsIgnoreCase(confirmDeferredDeliveryBean.getAction())) {
				processVar.put("startSalesNegotiationFlow", CommonConstants.YES);

				List<ServiceStatusDetails> serviceStatusDetails = serviceStatusDetailsRepository
						.findByScServiceDetail_idAndStatus(scServiceDetail.getId(),
								TaskStatusConstants.DEFERRED_DELIVERY);
				processVar.put("startSalesNegotiationFlow", CommonConstants.YES);

				if (!serviceStatusDetails.isEmpty() && serviceStatusDetails.size() >= 2) {
					processVar.put("deferredDeliveryEnable", false);
				}else {
					processVar.put("deferredDeliveryEnable", true);

				}

				String waitingTaskDate = getEffectiveDateForCustomerWaitingTask(
						confirmDeferredDeliveryBean.getTentativeDateForRejectService());
				if (waitingTaskDate != null) {
					processVar.put("deferredDeliveryWaitingDate", waitingTaskDate);
					processVar.put("customerNegotiation", CommonConstants.YES);
				}
				scServiceDetail.setTenatativeHoldResumeDate(DateUtil.convertStringToTimeStampYYMMDD(
						confirmDeferredDeliveryBean.getTentativeDateForRejectService()));
				updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.DEFERRED_DELIVERY,
						TaskStatusConstants.DEFERRED_DELIVERY);

			}
			scServiceDetailRepository.save(scServiceDetail);

		}

		return (ConfirmDeferredDeliveryBean) flowableBaseService.taskDataEntry(task, confirmDeferredDeliveryBean,
				processVar);
	}

	private void saveRemarks(ScServiceDetail scServiceDetail, String remarks) {
		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks(remarks);
		taskRemark.setUsername(userInfoUtils.getUserInformation().getUserId());
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
	}

	@Transactional(readOnly = false)
	public String cancelIpcCommValidTask(Integer taskId, String wfTaskId) {
		Task task = taskRepository.findByIdAndWfTaskId(taskId, wfTaskId);
		if(task != null) {
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CANCELLED));
			taskRepository.save(task);
			return "SUCCESS";
		}
		return null;
	}
	
	@Transactional(readOnly = false)
	public String saveIpcTaskAttributes(Integer taskId, String wfTaskId, Map<String, Object> ipcAttributes) {
		LOGGER.info("inside saveIpcTaskAttributes..");
		Task task = taskRepository.findByIdAndWfTaskId(taskId, wfTaskId);
		if (task != null) {
			Optional<ScOrder> scOrder = scOrderRepository.findById(task.getScOrderId());
			if (scOrder.isPresent()) {
				ipcAttributes.forEach((attributeName, attributeValue) -> {
					if ("attachmentId".equals(attributeName) && !attributeValue.toString().isEmpty()) {
						String[] attachmentIdStr = String.valueOf(attributeValue).split(",");
						LOGGER.info("Attachment ids array {}", attachmentIdStr.toString());
						for(String attachmentId: attachmentIdStr) {
							makeEntryInScAttachment(task, Integer.parseInt(attachmentId));
							LOGGER.info("Cascaded attachment Details to Sc_attachments {}", attachmentId);
						}
					} else if (Arrays.asList(IpcConstants.ATTRIBUTE_ACTUAL_DELIVERY_DATE,IpcConstants.ATTRIBUTE_COMMISSIONING_DATE).contains(attributeName)) {
						updateServiceDate(task, attributeName, (String) attributeValue);
					} else if (CommonConstants.FINAL_CPS.equals(attributeName)) {
						Map<String, String> attrmap = new HashMap<>();
						attrmap.put(CommonConstants.FINAL_CPS, (String) attributeValue);
						componentAndAttributeService.updateServiceAttributes(task.getServiceId(), attrmap);
					} else {
						scOrderAttributesUpdate(scOrder, attributeName, attributeValue);
					}
				});
			}
		}
		return "SUCCESS";
	}
	
	private void updateServiceDate(Task task, String attributeName, String attributeValue) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date formattedAttributeValue = sdf.parse(attributeValue);
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(task.getServiceId());
			if(scServiceDetail.isPresent()) {
				if (attributeName.equals(IpcConstants.ATTRIBUTE_ACTUAL_DELIVERY_DATE)) {
					scServiceDetail.get().setActualDeliveryDate(new Timestamp(formattedAttributeValue.getTime()));
				}
				scServiceDetail.get().setCommissionedDate(new Timestamp(formattedAttributeValue.getTime()));
				scServiceDetail.get().setServiceCommissionedDate(new Timestamp(formattedAttributeValue.getTime()));
				scServiceDetailRepository.saveAndFlush(scServiceDetail.get());
			}
		} catch (ParseException e) {
			LOGGER.error("Error in processing {} : {}", attributeName, e);
		}
	}

	private void scOrderAttributesUpdate(Optional<ScOrder> scOrder, String attributeName, Object attributeValue) {
		ScOrderAttribute scOrderAttribute = scOrderAttributeRepository.findByScOrder_IdAndAttributeName(scOrder.get().getId(), attributeName);
		if(null != scOrderAttribute) {
			scOrderAttribute.setAttributeValue(String.valueOf(attributeValue));
			scOrderAttribute.setIsActive(CommonConstants.Y);
			scOrderAttribute.setUpdatedBy(Utils.getSource());
			scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		}else {
			scOrderAttribute = new ScOrderAttribute();
			scOrderAttribute.setScOrder(scOrder.get());
			scOrderAttribute.setAttributeName(attributeName);
			scOrderAttribute.setAttributeValue(String.valueOf(attributeValue));
			scOrderAttribute.setCategory(null);
			scOrderAttribute.setIsActive(CommonConstants.Y);
			scOrderAttribute.setCreatedBy(Utils.getSource());
			scOrderAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
			scOrderAttribute.setUpdatedBy(Utils.getSource());
			scOrderAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
		}
		scOrderAttributeRepository.save(scOrderAttribute);
		LOGGER.info("ScOrderAttribute Name :{} and Value :{} got saved" , attributeName, attributeValue);
	}

	public Boolean sendEmailToCustomer(Map<String, String> emailAttributes) {
		Boolean response = Boolean.FALSE;
		try {
			MailNotificationRequest mailNotificationRequest = new MailNotificationRequest();
			Map<String, Object> variables = new HashMap<>();
			
			List<String> toMails = new ArrayList<String>();
			toMails.add(emailAttributes.get("customerEmailId"));
			
			variables.put("emailContent", emailAttributes.get("emailContent"));
			
			mailNotificationRequest.setFrom("no-reply@tatacommunications.com");
			mailNotificationRequest.setTo(toMails);
			mailNotificationRequest.setSubject("Order Complete! Meet Your Project Manager.");
			mailNotificationRequest.setVariable(variables);
			mailNotificationRequest.setTemplateId("PICE");
			mailNotificationRequest.setProductName("IPC");
			LOGGER.info("sending mail from send email to customer pm task {}",mailNotificationRequest);
			mqUtils.send(notificationMailQueue, Utils.convertObjectToJson(mailNotificationRequest));
			response = Boolean.TRUE;
		}catch(Exception e) {
			LOGGER.error("Error while sending email to customer", e);
		}
		return response;
	}
	
	@Transactional(readOnly = false)
	public boolean processO2CCancellationFlow(String serviceCode,String orderCode)throws TclCommonException{
		Boolean flag=false;
		boolean waitingResourceRelease=false;
		LOGGER.info("Cancellation service code : {}",serviceCode);
		CancellationRequest cancellationRequest=cancellationRequestRepository.findByCancellationServiceCodeAndCancellationOrderCode(serviceCode,orderCode);
		ScServiceDetail serviceDetail=scServiceDetailRepository.findByUuidAndScOrder_OpOrderCodeAndIsMigratedOrder(serviceCode,orderCode);
		if(Objects.nonNull(cancellationRequest) && Objects.nonNull(serviceDetail)){
			if(serviceDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RESOURCE_RELEASED_INITIATED)) {
				waitingResourceRelease=true;
			}
            processCancellationFromL2O(serviceDetail,"Customer",cancellationRequest.getCancellationReason(),cancellationRequest,waitingResourceRelease,null);
			LOGGER.info("Cancellation flow triggerd successfully from O2C for servicd code : {}",serviceCode);
			flag=true;
		}else{
			throw new TclCommonException(CommonConstants.ERROR, ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	  return flag;
	}
	
	@Transactional(readOnly=false)
	public Boolean processDeleteService(Integer serviceId) {
		Boolean response = false;
		Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository
				.findById(serviceId);
		if (scServiceDetail.isPresent() && Objects.nonNull(scServiceDetail.get())) {

			MstStatus mstStatus = taskCacheService.getMstStatus(TaskStatusConstants.DELETED);
			MstStatus mstStatusPlan = taskCacheService.getMstStatus(TaskStatusConstants.PENDING);

			List<Stage> stages = stageRepository.findByServiceId(serviceId);

			updateStagesandSub(stages, mstStatus);

			List<StagePlan> stagePlans = stagePlanRepository.findByServiceId(serviceId);

			updateStagePlansandSub(stagePlans, mstStatusPlan);

			response = true;
		}
		return response;
	}


	private void updateStagePlansandSub(List<StagePlan> stagePlans, MstStatus mstStatusPlan) {
		if (stagePlans != null) {
			stagePlans.stream().forEach(stagePlan -> {
				stagePlan.setMstStatus(mstStatusPlan);
				stagePlan.setActualStartTime(null);
				stagePlan.setActualEndTime(null);
				Set<ProcessPlan> processPlans = stagePlan.getProcessPlans();
				if (processPlans != null) {
					processPlans.stream().forEach(processPlan -> {
						processPlan.setMstStatus(mstStatusPlan);
						processPlan.setActualStartTime(null);
						processPlan.setActualEndTime(null);
						Set<ActivityPlan> activityPlans = processPlan.getActivityPlans();
						if (activityPlans != null) {
							activityPlans.stream().forEach(activityPlan -> {
								activityPlan.setMstStatus(mstStatusPlan);
								activityPlan.setActualStartTime(null);
								activityPlan.setActualEndTime(null);
								Set<TaskPlan> taskPlans = activityPlan.getTaskPlans();
								if (taskPlans != null) {
									taskPlans.stream().forEach(taskPlan -> {
										taskPlan.setMstStatus(mstStatusPlan);
										taskPlan.setActualStartTime(null);
										taskPlan.setActualEndTime(null);
										taskPlanRepository.save(taskPlan);
									});
								}
								activityPlanRepository.save(activityPlan);

							});
						}
						processPlanRepository.save(processPlan);

					});
				}
				stagePlanRepository.save(stagePlan);
			});
		}

	}


	private void updateStagesandSub(List<Stage> stages, MstStatus mstStatus) {
		if (stages != null) {
			stages.stream().forEach(stage -> {
				stage.setMstStatus(mstStatus);
				Set<com.tcl.dias.servicefulfillment.entity.entities.Process> processes = stage.getProcesses();
				if (processes != null) {
					processes.stream().forEach(process -> {

						process.setMstStatus(mstStatus);
						Set<Activity> activities = process.getActivities();
						if (activities != null) {
							activities.stream().forEach(activity -> {
								activity.setMstStatus(mstStatus);
								Set<Task> tasks = activity.getTasks();
								if (tasks != null) {
									tasks.stream().forEach(task -> {
										task.setMstStatus(mstStatus);
										taskRepository.save(task);
									});
								}
								activityRepository.save(activity);

							});
						}
						processRepository.save(process);

					});
				}
				stageRepository.save(stage);
			});
		}
	}
	
	
	@Transactional(readOnly = false)
	public void processReleaseResources(ScServiceDetail scServiceDetail, String cancellationInitiatedBy,
			String cancellationReason, boolean autoCancellation, boolean csmTaskRequired,boolean resourceReleaseNeeded,boolean resourceReleaseInprogress) throws TclCommonException {
		Map<String, Object> processVar = new HashMap<>();
		processVar.put("retainExistingNwresource", "No");
		processVar.put("cancellationInitiatedBy", cancellationInitiatedBy);
		processVar.put("cpeFlowRequired", "No");
		processVar.put("isConfirmIPCancel", false);
		processVar.put("isCancelTxConfig", false);
		processVar.put("isRFExists", false);
		processVar.put("isCancelClr", false);
		processVar.put("csmTaskRequired", csmTaskRequired);
		processVar.put("resourceReleaseNeeded", resourceReleaseNeeded);
		processVar.put("resourceReleaseCycle", resourceReleaseCycle);
		try {
			List<Task> tasks=taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(scServiceDetail.getId(), Arrays.asList("product-commissioning-jeopardy"), Arrays.asList(TaskStatusConstants.OPENED,TaskStatusConstants.CLOSED_STATUS,TaskStatusConstants.INPROGRESS,TaskStatusConstants.REOPEN));

			if(!tasks.isEmpty()) {
				throw new TclCommonException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
						ResponseResource.R_CODE_ERROR);
			}
			if(resourceReleaseNeeded) {
			isEligibleForCancellation(scServiceDetail.getId(), processVar, resourceReleaseInprogress);
			}
			LOGGER.info("processReleaseResources service details {}", scServiceDetail.getId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("customerRequestorDate", DateUtil.convertDateToString(new Date()));
			atMap.put("cancellationFlowTriggered", CommonConstants.YES);
			atMap.put("cancellationReason", cancellationReason);
			atMap.put("retainExistingNwresource", "No");
			atMap.put("cancellationInitiatedBy", cancellationInitiatedBy);

			saveRemarksForCancellation(scServiceDetail, autoCancellation);
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap,
					AttributeConstants.COMPONENT_LM, "A");
			scServiceDetail.setCustomerRequestorDate(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
			scServiceDetailRepository.save(scServiceDetail);

			processReleaseResourcesFlowVariable(scServiceDetail, processVar);
		} catch (Exception ex) {
			LOGGER.error("Error in processReleaseResources service id : {}", scServiceDetail.getUuid());
			LOGGER.error("Error in processReleaseResources with error : {}", ex);

			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANCELLLATION_NOT_PROCESSED_IN_O2C,
					ResponseResource.R_CODE_ERROR);
		}
	}
	
	private void processReleaseResourcesFlowVariable(ScServiceDetail scServiceDetail, Map<String, Object> processVar)
			throws TclCommonException {

		String serviceCode = scServiceDetail.getUuid();
		LOGGER.info("inside processCancellationFlowVariable with service id : {} ", scServiceDetail.getId());
		Map<String, String> scComponentAttributesAMap = commonFulfillmentUtils
				.getComponentAttributes(scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");

		processVar.put("customerUserName",
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getErfCustCustomerName()));
		processVar.put(MasterDefConstants.ORDER_CODE,
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_TYPE,
				StringUtils.trimToEmpty(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CATEGORY,
				StringUtils.trimToEmpty(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());
		LOGGER.info("inside processCancellationFlowVariable with order details : {} {} {}",
				scServiceDetail.getScOrder().getOpOrderCode(),scServiceDetail.getScOrder().getOrderType(),
				scServiceDetail.getScOrder().getOrderCategory());
		processVar.put(MasterDefConstants.PRODUCT_NAME,
				StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
		if (scServiceDetail.getErfPrdCatalogProductName().contains(CommonConstants.NPL)
				|| scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(CommonConstants.NPL)) {
			processVar.put("productType", StringUtils.trimToEmpty(CommonConstants.NPL));
		}
		processVar.put(MasterDefConstants.OFFERING_NAME,
				StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
		processVar.put(MasterDefConstants.SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
		processVar.put(MasterDefConstants.SERVICE_ID, scServiceDetail.getId());
		processVar.put(MasterDefConstants.SITE_ADDRESS,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));
		LOGGER.info("inside processCancellationFlowVariable with service details : {} {} {} {}",
				scServiceDetail.getId(), scServiceDetail.getScOrder().getOrderType(),
				scComponentAttributesAMap.get("siteAddress"), scServiceDetail.getScOrder().getOrderCategory());
		processVar.put(MasterDefConstants.CITY,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
		processVar.put(MasterDefConstants.STATE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
		processVar.put(MasterDefConstants.COUNTRY,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_MOBILE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_NAME,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));
		LOGGER.info("inside processCancellationFlowVariable with local IT details : {} {}",
				scComponentAttributesAMap.get("localItContactName"), scComponentAttributesAMap.get("destinationCity"));
		String lastMileType = Objects.nonNull(scComponentAttributesAMap.get("lmType"))
				? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType"))
				: null;
		processVar.put(MasterDefConstants.LM_TYPE, lastMileType);
		processVar.put(MasterDefConstants.LM_CONNECTION_TYPE,
				Objects.nonNull(scComponentAttributesAMap.get("lmConnectionType"))
						? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmConnectionType"))
						: null);
		processVar.put("lastMileScenario",
				Objects.nonNull(scComponentAttributesAMap.get("lastMileScenario"))
						? StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastMileScenario"))
						: null);
		LOGGER.info("inside processCancellationFlowVariable with last mile details : {} {} {}",
				lastMileType, scComponentAttributesAMap.get("lmConnectionType"),scComponentAttributesAMap.get("lastMileScenario"));
		processVar.put("cancellationFlowTriggered", "Yes");
		processVar.put("remainderCycle", "R60/PT24H");
		if (Objects.nonNull(lastMileType) && !lastMileType.isEmpty()) {
			if (lastMileType.toLowerCase().contains("offnet")) {
				processVar.put("parentLmType", "offnet");
			} else {
				processVar.put("parentLmType", lastMileType);
			}
		}

		LOGGER.info("Cancellation Flow Triggered succsessfully : {} ",processVar.toString());
		runtimeService.startProcessInstanceByKey("release-resources-workflow", processVar);
	}
	
	private void triggerCustomerOnholdWorkflow(String tentativeDateforHold, CimHoldRequest request,
			ScServiceDetail scServiceDetail) throws TclCommonException {
		Map<String, Object> processVar = new HashMap<>();

		processVar.put("customerUserName",
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getErfCustCustomerName()));
		processVar.put(MasterDefConstants.ORDER_CODE,
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_CODE,
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_TYPE,
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOrderType()));
		processVar.put(MasterDefConstants.ORDER_CATEGORY,
				StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOrderCategory()));
		processVar.put(MasterDefConstants.ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());
		processVar.put(MasterDefConstants.SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
		processVar.put(MasterDefConstants.SERVICE_ID, scServiceDetail.getId());
		processVar.put("resourceReleaseCycle", resourceReleaseCycle);


		if ("Customer Dependency".equalsIgnoreCase(request.getOnHoldCategory())
				|| "Deferred Delivery".equalsIgnoreCase(request.getOnHoldCategory())) {
			LOGGER.info("Value of tentativeDateforHold to trigger sales negotiation : {}", tentativeDateforHold);

			if ("Deferred Delivery".equalsIgnoreCase(request.getOnHoldCategory())) {
				LOGGER.info("Inside sale negotiation Confirm Deferred Delivery task flow : {}", tentativeDateforHold);
				tentativeDateforHold = getEffectiveDateForCustomerWaitingTask(tentativeDateforHold);
				if (tentativeDateforHold != null) {
					processVar.put("deferredDeliveryWaitingDate", tentativeDateforHold);
					processVar.put("customerNegotiation", CommonConstants.YES);
				}
				processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
						"Deferred delivery Sales Negotiation Workflow is triggered for service id "
								+ scServiceDetail.getUuid() + " with deferred delivery waiting date "
								+ tentativeDateforHold + " on " + new Timestamp(new Date().getTime()),
						null, null));
				runtimeService.startProcessInstanceByKey("sales-negotiation-workflow", processVar);

			} else {
				LOGGER.info("Inside sale negotiation without Confirm Deferred Delivery task flow : {}",
						tentativeDateforHold);
				processVar.put("salesNegotiationTriggered", "Yes");
				processVar.put("csmTaskRequired", "false");
				tentativeDateforHold = getEffectiveDateForHoldProcess(tentativeDateforHold);
				if (tentativeDateforHold != null) {
					processVar.put("salesNegotiationDate", tentativeDateforHold);
				}
				processVar.put("csmTaskRequired", false);
				processVar.put("waitingTaskRequired", "Yes");
				processVar.put("salesNegotiationTriggered", "Yes");
				processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
						"Hold Sales Negotiation Workflow is triggered for service id " + scServiceDetail.getUuid()
								+ " with hold sales negotiation date " + tentativeDateforHold + " on "
								+ new Timestamp(new Date().getTime()),
						null, null));
				runtimeService.startProcessInstanceByKey("customer-onhold-workflow", processVar);

			}

		}

	}
	/*dont remove it needed for future*/
	private void triggerSalesNegotiationWorkflow(String tentativeDateforHold, CimHoldRequest request,
			ScServiceDetail scServiceDetail) throws TclCommonException {

		if ("Customer Dependency".equalsIgnoreCase(request.getOnHoldCategory())
				|| "Deferred Delivery".equalsIgnoreCase(request.getOnHoldCategory())) {
			Map<String, Object> processVar = new HashMap<>();
			LOGGER.info("Value of tentativeDateforHold to trigger sales negotiation : {}", tentativeDateforHold);

			if ("Deferred Delivery".equalsIgnoreCase(request.getOnHoldCategory())) {
				LOGGER.info("Inside sale negotiation Confirm Deferred Delivery task flow : {}", tentativeDateforHold);
				tentativeDateforHold = getEffectiveDateForCustomerWaitingTask(tentativeDateforHold);
				if (tentativeDateforHold != null) {
					processVar.put("deferredDeliveryWaitingDate", tentativeDateforHold);
					processVar.put("customerNegotiation", CommonConstants.YES);
				}
				processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
						"Deferred delivery Sales Negotiation Workflow is triggered for service id "
								+ scServiceDetail.getUuid() + " with deferred delivery waiting date "
								+ tentativeDateforHold + " on " + new Timestamp(new Date().getTime()),
						null, null));
			}else{
				LOGGER.info("Inside sale negotiation without Confirm Deferred Delivery task flow : {}", tentativeDateforHold);
				processVar.put("salesNegotiationTriggered", "Yes");
				tentativeDateforHold = getEffectiveDateForHoldProcess(tentativeDateforHold);
				if (tentativeDateforHold != null) {
					processVar.put("salesNegotiationDate", tentativeDateforHold);
				}
				processVar.put("waitingTaskRequired", "Yes");
				processVar.put("salesNegotiationTriggered", "Yes");
                processTaskLogRepository.save(createProcessTaskLog(scServiceDetail, "REMARKS",
                        "Hold Sales Negotiation Workflow is triggered for service id "+scServiceDetail.getUuid()+" with hold sales negotiation date "+ tentativeDateforHold +" on "+new Timestamp(new Date().getTime()),
                        null, null));
			}

			processVar.put("customerUserName",
					StringUtils.trimToEmpty(scServiceDetail.getScOrder().getErfCustCustomerName()));
			processVar.put(MasterDefConstants.ORDER_CODE,
					StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
			processVar.put(MasterDefConstants.ORDER_CODE,
					StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
			processVar.put(MasterDefConstants.ORDER_TYPE,
					StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOrderType()));
			processVar.put(MasterDefConstants.ORDER_CATEGORY,
					StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOrderCategory()));
			processVar.put(MasterDefConstants.ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());
			processVar.put(MasterDefConstants.SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
			processVar.put(MasterDefConstants.SERVICE_ID, scServiceDetail.getId());
			runtimeService.startProcessInstanceByKey("sales-negotiation-workflow", processVar);
		}

	}

	private void notifyServiceCancellationInitiation (ScServiceDetail scServiceDetail){
		try {
			LOGGER.info("Inside Cancellation Initiation mail trigger for service : {} {}",scServiceDetail.getUuid());
			List<String> toEmailIds=new ArrayList<>();
			List<String> ccEmailIds=new ArrayList<>();
			Map<String, Object> emailAttribute = new HashMap<>();
			Map<String,String> scComponentAttributes= commonFulfillmentUtils.getComponentAttributesDetails(Arrays.asList("localItContactName", "localItContactEmailId"),scServiceDetail.getId(),"LM", "A" );
			if(scServiceDetail.getAssignedPM()!=null){
				ccEmailIds.add(scServiceDetail.getAssignedPM());
			}
			if(scComponentAttributes!=null){
				if(scComponentAttributes.get("localItContactEmailId")!=null && !scComponentAttributes.get("localItContactEmailId").isEmpty()){
					toEmailIds.add(scComponentAttributes.get("localItContactEmailId"));
				}
				emailAttribute.put("customerName",scComponentAttributes.get("localItContactName"));
			}
			if(scServiceDetail.getUuid()!=null) {
				emailAttribute.put("serviceCode", scServiceDetail.getUuid());
			}
			if (scServiceDetail.getErfPrdCatalogParentProductName() != null){
				emailAttribute.put("productName", scServiceDetail.getErfPrdCatalogProductName());
			}
			if(scServiceDetail.getScOrder()!=null) {
				if (scServiceDetail.getScOrder().getOpOrderCode() != null){
					emailAttribute.put("orderCode", scServiceDetail.getScOrder().getOpOrderCode());
				}
				if (scServiceDetail.getScOrder().getOrderType() != null){
					emailAttribute.put("orderType", scServiceDetail.getScOrder().getOrderType());
				}
			}
			LOGGER.info("Cancellation Initiation to emailids : {} and cc emailids : {}", toEmailIds,ccEmailIds);
			notificationService.notifyServiceCancellationInitiation(toEmailIds, ccEmailIds, emailAttribute);

		}catch (Exception ex){
			LOGGER.error("Error while mail trigger to admin for Service Cancellation initiation : {} ",ex );
		}
	}
	
	private void checkSalesTaskAvailable(Integer serviceId) throws TclCommonException {

		List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(serviceId,
				Arrays.asList("sales-negotiation", "sales-negotiation-SALES",
						"customer-hold-negotaition-CIM", "sales-assist-ordering","sales-negotiation-waiting-task"),
				Arrays.asList(TaskStatusConstants.OPENED,
						TaskStatusConstants.INPROGRESS, TaskStatusConstants.REOPEN));

		if (!tasks.isEmpty()) {
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.CANT_PUT_THIS_ON_UNHOLD,
					ResponseResource.R_CODE_ERROR);
		}

	}
	

	@Transactional(readOnly = false)
	public Boolean processTermination10PercentFlow(OdrOrderBean odrOrderBean, OdrServiceDetailBean odrServiceDetailBean) throws TclCommonException{

		Boolean terminateFlag=false;
		ScServiceDetail scServiceDetail =scServiceDetailRepository.findFirstByUuidOrderByIdDesc(odrServiceDetailBean.getUuid());
		if(scServiceDetail != null) {
			LOGGER.info("Termination service details {}",scServiceDetail.getId());
			Map<String, String> atMap = new HashMap<>();
			atMap.put("terminationEffectiveDate", odrServiceDetailBean.getTerminationEffectiveDate());
			atMap.put("customerRequestorDate", odrServiceDetailBean.getCustomerRequestorDate());
			atMap.put("etcValue", odrServiceDetailBean.getEtcValue());
			atMap.put("etcWaiver", odrServiceDetailBean.getEtcWaiver());
			atMap.put("terminationFlowTriggered",CommonConstants.YES);
			atMap.put("contractEndDate",odrServiceDetailBean.getContractEndDate());
			atMap.put("approvalMailAvailable",odrServiceDetailBean.getApprovalMailAvailable());
			atMap.put("backdatedTermination",odrServiceDetailBean.getBackdatedTermination());
			atMap.put("terminationReason", odrServiceDetailBean.getTerminationReason());
			
			
			atMap.put("optimusServiceId", "No");
			List<ScServiceDetail> scServiceDetailList = scServiceDetailRepository
					.findByUuidAndMstStatus_codeAndIsMigratedOrder(scServiceDetail.getUuid(), "ACTIVE", "N");
			if (scServiceDetailList != null && !scServiceDetailList.isEmpty()) {
				atMap.put("optimusServiceId", "Yes");
			}
			
			saveRemarksForTerminationAndCancellation(scServiceDetail,true);
			componentAndAttributeService.updateAttributes(scServiceDetail.getId(), atMap, AttributeConstants.COMPONENT_LM, "A");
			scServiceDetail.setTerminationFlowTriggered(CommonConstants.YES);
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATION_INITIATED));
			updateServiceStatusAndCreatedNewStatus(scServiceDetail, TaskStatusConstants.TERMINATION_INITIATED, TaskStatusConstants.TERMINATION_INITIATED);
			scServiceDetail.setTerminationInitiationDate(new Timestamp(new Date().getTime()));
			scServiceDetail.setCustomerRequestorDate(odrServiceDetailBean.getCustomerRequestorDate());
			scServiceDetail.setTerminationEffectiveDate(odrServiceDetailBean.getTerminationEffectiveDate());
			scServiceDetailRepository.save(scServiceDetail);
			terminateFlag=true;
			if(terminateFlag){
			  processTermination10PercentFlowVariable(scServiceDetail ,odrServiceDetailBean, atMap.get("optimusServiceId"));
		}
	}
		return terminateFlag;
	}
	
	private void processTermination10PercentFlowVariable(ScServiceDetail scServiceDetail, OdrServiceDetailBean odrServiceDetailBean, String optimusServiceId) throws TclCommonException{

		Map<String, Object> processVar = new HashMap<>();
		String serviceCode=scServiceDetail.getUuid();
		LOGGER.info("Termination Component service id {} ",scServiceDetail.getId());
		Map<String, String> scComponentAttributesAMap =commonFulfillmentUtils.getComponentAttributes(
				scServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A");
		processVar.put(MasterDefConstants.SC_ORDER_ID, scServiceDetail.getScOrder().getId());
		processVar.put("customerUserName", StringUtils.trimToEmpty(scServiceDetail.getScOrder().getErfCustCustomerName()));
		processVar.put(MasterDefConstants.ORDER_CODE, StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_CODE, StringUtils.trimToEmpty(scServiceDetail.getScOrder().getOpOrderCode()));
		processVar.put(MasterDefConstants.ORDER_TYPE, StringUtils.trimToEmpty(OrderCategoryMapping.getOrderType(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CATEGORY, StringUtils.trimToEmpty(OrderCategoryMapping.getOrderCategory(scServiceDetail, scServiceDetail.getScOrder())));
		processVar.put(MasterDefConstants.ORDER_CREATED_DATE, scServiceDetail.getScOrder().getOrderStartDate());


		processVar.put(MasterDefConstants.PRODUCT_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogProductName()));
		if(scServiceDetail.getErfPrdCatalogProductName().contains(CommonConstants.NPL) ||
				scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase(CommonConstants.NPL)) {
			processVar.put("productType", StringUtils.trimToEmpty(CommonConstants.NPL));
		}
		processVar.put(MasterDefConstants.OFFERING_NAME, StringUtils.trimToEmpty(scServiceDetail.getErfPrdCatalogOfferingName()));
		processVar.put(MasterDefConstants.SERVICE_CODE, StringUtils.trimToEmpty(scServiceDetail.getUuid()));
		processVar.put(MasterDefConstants.SERVICE_ID, scServiceDetail.getId());
		processVar.put(MasterDefConstants.SITE_ADDRESS, StringUtils.trimToEmpty(scComponentAttributesAMap.get("siteAddress")));

		processVar.put(MasterDefConstants.CITY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCity")));
		processVar.put(MasterDefConstants.STATE, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationState")));
		processVar.put(MasterDefConstants.COUNTRY, StringUtils.trimToEmpty(scComponentAttributesAMap.get("destinationCountry")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactEmailId")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_MOBILE,
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactMobile")));
		processVar.put(MasterDefConstants.LOCAL_IT_CONTACT_NAME, StringUtils.trimToEmpty(scComponentAttributesAMap.get("localItContactName")));

		String lastMileType=Objects.nonNull(scComponentAttributesAMap.get("lmType"))?
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmType")):null;
		processVar.put(MasterDefConstants.LM_TYPE, lastMileType);
		processVar.put(MasterDefConstants.LM_CONNECTION_TYPE, Objects.nonNull(scComponentAttributesAMap.get("lmConnectionType"))?
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("lmConnectionType")):null);
		processVar.put("lastMileScenario", Objects.nonNull(scComponentAttributesAMap.get("lastMileScenario"))?
				StringUtils.trimToEmpty(scComponentAttributesAMap.get("lastMileScenario")):null);
		
		processVar.put("terminationFlowTriggered","Yes");
		processVar.put("remainderCycle", "R60/PT24H");
		
		processVar.put("terminationEffectiveDate", odrServiceDetailBean.getTerminationEffectiveDate());
		if(odrServiceDetailBean.getNegotiationRequired() != null && odrServiceDetailBean.getNegotiationRequired().equalsIgnoreCase("Yes")) {
			processVar.put("salesTerminationTaskRequired", "Yes");
			LOGGER.info("salesTerminationTaskRequired for service code {}, salesTerminationTaskRequired{}", serviceCode, odrServiceDetailBean.getNegotiationRequired());
		}
		boolean offnetLm=false;
		

		String lmTypeA = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_A, "lmType");
		String lmTypeB = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_B, "lmType");
		
		String offnetBackHaulA = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_A, "offnetBackHaul");
		
		String offnetBackHaulB = getComponentValue(odrServiceDetailBean.getOdrComponentBeans(), AttributeConstants.SITETYPE_B, "offnetBackHaul");
		
		LOGGER.info("lmTypeA {}, lmTypeB {}, offnetBackHaulA {}, offnetBackHaulB {} and service code {}", lmTypeA, lmTypeB, offnetBackHaulA, offnetBackHaulB, serviceCode);
		
		if((offnetBackHaulA != null && offnetBackHaulA.equalsIgnoreCase("Yes")) || (offnetBackHaulB != null && offnetBackHaulB.equalsIgnoreCase("Yes")) ||
				(lmTypeA != null && lmTypeA.toLowerCase().contains("offnet")) || (lmTypeB != null && lmTypeB.toLowerCase().contains("offnet"))
				|| (lmTypeA != null && lmTypeA.equalsIgnoreCase("OffnetWL")) || (lmTypeB != null && lmTypeB.equalsIgnoreCase("OffnetWL"))) {
			offnetLm=true;
			processVar.put("offnetTerminationRequired", "Yes");
		}
		
		if(lmTypeA != null && lmTypeA.toLowerCase().contains("offnet")) {
			offnetLm=true;
			processVar.put("offnetPoTerminationRequired", "Yes");
			LOGGER.info("offnetPoTerminationRequired for service code {}", serviceCode);
		}
		if(lmTypeB != null && lmTypeB.toLowerCase().contains("offnet")) {
			offnetLm=true;
			processVar.put("offnetPoTerminationRequiredB", "Yes");
			LOGGER.info("offnetPoTerminationRequiredB for service code {}", serviceCode);
		}
		if(offnetBackHaulA != null && offnetBackHaulA.equalsIgnoreCase("Yes")) {
			offnetLm=true;
			processVar.put("offnetTerminationBackHaulRequired", "Yes");
			LOGGER.info("offnetTerminationBackHaulRequired for service code {}", serviceCode);
		}
		if(offnetBackHaulB != null && offnetBackHaulB.equalsIgnoreCase("Yes")) {
			offnetLm=true;
			processVar.put("offnetTerminationBackHaulRequiredB", "Yes");
			LOGGER.info("offnetTerminationBackHaulRequiredB for service code {}", serviceCode);
		}
		
		if(scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NPL") || scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("NDE")) {
			processVar.put("siteBType", AttributeConstants.SITETYPE_B);
		}


		if(Objects.nonNull(lastMileType) && !lastMileType.isEmpty()) {
			if (lastMileType.toLowerCase().contains("offnet")){
				offnetLm=true;
				processVar.put("parentLmType","offnet");
			} else {
				processVar.put("parentLmType",lastMileType);
			}
		}
		
		if(offnetLm) {
			updateOffnetPoForTermination(scServiceDetail);
		}
		if (!scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("IAS")
				&& !scServiceDetail.getErfPrdCatalogProductName().equalsIgnoreCase("GVPN")) {
			processVar.put("offnetPoTerminationRequiredB", "No");
			processVar.put("offnetPoTerminationRequired", "No");
			processVar.put("offnetTerminationBackHaulRequiredB", "No");
			processVar.put("offnetTerminationBackHaulRequired", "No");
			processVar.put("offnetHaulExtensionRequired", "No");
			processVar.put("offnetHaulExtensionRequiredB", "No");
			processVar.put("offnetPoExtensionRequired", "No");
			processVar.put("offnetPoExtensionRequiredB", "No");

		}
		
		processVar.put("optimusServiceId", "No");
        List<ScServiceDetail> scServiceDetailList=scServiceDetailRepository.findByUuidAndMstStatus_codeAndIsMigratedOrder(scServiceDetail.getUuid(), "ACTIVE", "N");
        if(scServiceDetailList!=null && !scServiceDetailList.isEmpty()) {
            processVar.put("optimusServiceId", "Yes");
        }
		
		processVar.put("processType", "computeProjectPLan");
		processVar.put("root_endDate", new Timestamp(new Date().getTime()));
		processVar.put("optimusServiceId", optimusServiceId);
		
		runtimeService.startProcessInstanceByKey("termination-negotiation-workflow", processVar);
	}
	
	
	private String getComponentValue(Set<OdrComponentBean> odrComponentBeans, String siteType, String attName) {
		if(odrComponentBeans != null) {
			for (OdrComponentBean odrComponentBean : odrComponentBeans) {
				if(odrComponentBean.getSiteType() != null && odrComponentBean.getSiteType().equals(siteType) &&
						odrComponentBean.getOdrComponentAttributeBeans() != null) {
					Optional<OdrComponentAttributeBean> odrOptional = odrComponentBean.getOdrComponentAttributeBeans().stream()
							.filter(comp -> comp.getName().equals(attName)).findFirst();
					if(odrOptional.isPresent()) {
						return odrOptional.get().getValue();
					}
				}
			}
		}
		return null;
	}

	
	@Transactional(readOnly = false)
	public BaseRequest nmsUpdate(BaseRequest baseRequest)
		throws TclCommonException {
		Task task = getTaskByIdAndWfTaskId(baseRequest.getTaskId(), baseRequest.getWfTaskId());

		processTaskLogDetails(task,TaskStatusConstants.CLOSED_STATUS,baseRequest.getDelayReason(),null, baseRequest);
		return (BaseRequest)flowableBaseService.taskDataEntry(task, baseRequest);	

	}

}

