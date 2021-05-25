package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tcl.dias.servicefulfillmentutils.beans.RejectionTaskBean;
import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.networkaugment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.beans.*;
import com.tcl.dias.servicefulfillmentutils.constants.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.parser.Part;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.networkaugment.specification.TaskLogSpecification;
import com.tcl.dias.networkaugment.specification.TaskSpecification;
import com.tcl.dias.servicefulfillmentutils.helper.UploadAttachmentComponent;
import com.tcl.dias.servicefulfillmentutils.utils.FulfillmentUtils;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import javax.rmi.CORBA.Util;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.*;


/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional(readOnly = true)
public class TaskService extends ServiceFulfillmentBaseService {
	public static final String FIRST_DAY_OF_MONTH = "firstDayOfMonth";
	public static final String LAST_DAY_OF_MONTH = "lastDayOfMonth";
	public static final String YYYY_M_D = "yyyy-M-d";
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
	public static final String TASK_ID = "taskId";
	public static final String TASK_ASSIGNMENT_ID = "taskAssignmentId";
	public static final String TASK_ASSIGNMENT_USER_NAME = "taskAssignmentUserName";
	public static final String STATUS = "status";
	public static final String CLOSED = "CLOSED";
	public static final String INPROGRESS = "INPROGRESS";
	

	@Autowired
	ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	TaskAssignmentRepository taskAssignmentRepository;

	@Autowired
	ProcessRepository processRepository;

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
	ScOrderAttributeRepository scOrderAttributeRepository;

	@Autowired
	TATService tatService;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	@Autowired
	MfTaskDetailAuditRepository mfTaskDetailAuditRepository;

	@Autowired
	NwaRejectionDetailsRepository nwaRejectionDetailsRepository;

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
	TaskRemarkRepository taskRemarkRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	FlowableBaseService flowableBaseService;

	@Autowired
	NwaSuspensionReasonRepository nwaSuspensionReasonRepository;

	@Autowired
	TaskDataService itaskDataService;

	@Autowired
	MfTaskTrailRepository mfTaskTrailRepository;

	@Autowired
	NwaEorEquipDetailsRepository nwaEorEquipDetailsRepository;

	@Autowired
	NwaRemarksDetailsRepository nwaRemarksDetailsRepository;

	@Autowired
	NwaOrderDetailsExtndRepository nwaOrderDetailsExtndRepository;


	private String groupName;
	private String userName;
	private String type;
	private String customerName;

	/**
	 * This method is used to get the task count
	 *
	 * @param groupName
	 * @return
	 * @author vivek
	 * @param //userName
	 * @param type
	 */
	public List<AssignedGroupBean> getTaskCount(String groupName, String userName, String type, String customerName) {
		/*this.groupName = groupName;
		this.userName = user;
		this.type = type;
		this.customerName = customerName;*/

		System.out.println("=======getTaskCount is called");
		System.out.println("groupName = "+groupName);
		System.out.println("userName = "+userName);
		System.out.println("type = "+type);
		List<AssignedGroupBean> assignedGroupBeans = new ArrayList<>();

		//List<java.lang.String> taskKeys = new ArrayList<>();
		//taskKeys.add("raise_eor_order_task");

		String name = groupName;
		String user = userName;
		System.out.println("# groupName and userName and type " + name +user +type);

		if(type!=null && type.equalsIgnoreCase("user")) {
			userName=userInfoUtils.getUserInformation().getUserId();
		}
		if(groupName==null || groupName.isEmpty())name = userName;

		List<Task> tasks = taskRepository
				.findAll(TaskSpecification.getTaskFilter(groupName, getStatus(), userName, null,
						null,null,null,null,null,null,
						null,null,null,null,null,null,
						null,false,null,null,
						null,null,null, null, null));
		getTaskCountDetails(tasks, assignedGroupBeans, name);
		System.out.println("Task repository is being called ......."+ tasks);

		return assignedGroupBeans;
	}


	/**
	 * Method to get task details based on filter
	 * @param request
	 * @return
	 */
	public List<AssignedGroupingBean> getTaskDetailsBasedOnfilter(TaskRequest request) {
		List<AssignedGroupingBean> assignedGroupingBeans = new ArrayList<>();

		String userName = request.getUserName();

		/*if (request.getType() != null && request.getType().equalsIgnoreCase("user")) {
			userName = userInfoUtils.getUserInformation().getUserId();
		}*/




		List<Task> taskList1 = taskRepository.findAll(TaskSpecification.getTaskFilter(request.getGroupName(), request.getStatus(),
				userName, request.getServiceId(), request.getOrderType(), request.getOrderCategory(), request.getOrderSubCategory(),
				request.getServiceType(),
				request.getTaskName(), request.getServiceCode(), request.getCity(), request.getCountry(),
				request.getCustomerName(), request.getState(), request.getLastMileScenario(), request.getLmProvider(),
				request.getPmName(), request.getIsJeopardyTask(), request.getDistributionCenterName(),
				request.getDeviceType(), request.getDevicePlatform(), request.getIsIpDownTimeRequired(),
				request.getIsTxDowntimeReqd(), request.getEquipmentName(), request.getOrderCode()));

		/*if(request.getGroupName().toLowerCase().contains("ip_f")){
			tasks.addAll( taskList1.stream().filter(task -> {
				NwaOrderDetailsExtnd extObj = nwaOrderDetailsExtndRepository.findByScOrderId(task.getServiceId());
				if(extObj!= null && extObj.getFieldOps() != null && extObj.getFieldOps().trim().length()>0 && extObj.getFieldOps().lastIndexOf("-")>-1) {

					String fopsLoc = extObj.getFieldOps().substring(extObj.getFieldOps().lastIndexOf("-")+1, extObj.getFieldOps().length());
					String[] splitArr = request.getGroupName().split("_");
					String grpNameLoc = null;
					if (splitArr.length > 2) {
						grpNameLoc = splitArr[2];
						return grpNameLoc.equalsIgnoreCase(fopsLoc);
					}
				}
				return  false;
			}).collect(Collectors.toList()));
		}else{
			tasks.addAll(taskList1);
		}*/
		List<Task> tasks = new ArrayList<Task>(taskList1);
		Map<String, List<Task>> groupBasedAssignmnetTask = null;
		if (request.getGroupBy() != null && request.getGroupBy().equalsIgnoreCase(GroupByConstants.GROUPBYSTATUS)) {

			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstStatus().getCode()));
		} else {
			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstTaskDef().getName()));

		}

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
			/*taskList.forEach(task -> {
				TaskBean taskBean = new TaskBean();
				taskBean.setProcessType("EOR");
				taskBean.setTechnologyType("IP");
				taskBean.setScenarioType("Provisioning");

				//ScOrder scOrder = task.getScOrder();

				//taskBean.setProcessType(scOrder.getProcessType());
				//taskBean.setTechnologyType(scOrder.getTechnologyType());
				//taskBean.setScenarioType(scOrder.getScenarioType());
				setTaskDetails(taskBean, task);

				taskGroup.getTaskBeans().add(taskBean);
				taskGroupList.add(taskGroup);

			});*/

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


		}
		return taskResponse;

	}

//	private TaskBean constructTaskDetails(Task task) {
//		TaskBean taskBean = new TaskBean();
//		taskBean.setTaskId(task.getId());
//		taskBean.setTaskDefKey(task.getMstTaskDef().getKey());
//		taskBean.setActualEndTime(plan.getActualEndTime());
//		taskBean.setIsCustomerTask(task.getMstTaskDef().getIsCustomerTask()!=null?task.getMstTaskDef().getIsCustomerTask():"N");
//		taskBean.setName(task.getMstTaskDef().getName());
//		taskBean.setActualStartTime(plan.getActualStartTime());
//		taskBean.setPlannedStartTime(plan.getPlannedStartTime());
//		taskBean.setPlannedEndTime(plan.getPlannedEndTime());
//		taskBean.setEstimatedEndTime(plan.getEstimatedEndTime());
//		taskBean.setEstimatedStartTime(plan.getEstimatedStartTime());
//
//		return taskBean;
//
//	}

	/**s
	 * This method is used to get the latest activity of group
	 *
	 * @param groupName
	 * @return
	 * @author vivek
	 * @param taskId
	 * @param serviceId
	 * @param //userName
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
		/*List<Task> tasks = taskRepository.findAll();
		LOGGER.info("Size of the task{}",tasks.size());
		Task	task = tasks.stream().findFirst().orElse(null);*/

		if (task != null) {
			if (task.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
				throw new TclCommonRuntimeException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.TASK_CLOSED,
						ResponseResource.R_CODE_ERROR);

			}
			String orderCode = task.getOrderCode();
			ScOrder scOrder = scOrderRepository.findByOpOrderCode(orderCode);

			task.setClaimTime(new Timestamp(new Date().getTime()));

			Timestamp dueDate = task.getCreatedTime();

			dueDate = Timestamp.from(dueDate.toInstant().plus(task.getMstTaskDef().getTat(), ChronoUnit.MINUTES));
			Integer scOrderId = taskRepository.findOrderIdByTaskId(task.getId());
			/*switch (task.getMstTaskDef().getKey()){
				case "eor_tx_pro_physical_AT_task" :
				case "validate_and_at_task" :
					Optional<NwaOrderDetailsExtnd> nwaOrderDetailsExtndOptional = nwaOrderDetailsExtndRepository.findByOrderId(scOrderId);
					if (nwaOrderDetailsExtndOptional.isPresent()) {
						NwaOrderDetailsExtnd nwaOrderDetailsExtnd = nwaOrderDetailsExtndOptional.get();
						Optional<TaskAssignment> taskAssignmentOptional = taskAssignmentRepository.findByTaskId(task.getId());
						if (taskAssignmentOptional.isPresent()) {
							TaskAssignment taskAssignment = taskAssignmentOptional.get();
							taskAssignment.setOwner(nwaOrderDetailsExtnd.getFieldOps());
							taskAssignment.setGroupName(nwaOrderDetailsExtnd.getFieldOps());
							taskAssignmentRepository.save(taskAssignment);
						}
					}
				break;
			}*/
			/*if("validate_and_at_task".equalsIgnoreCase(task.getMstTaskDef().getKey()) || "eor_tx_pro_physical_AT_task".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				Optional<NwaOrderDetailsExtnd> nwaOrderDetailsExtndOptional = nwaOrderDetailsExtndRepository.findByOrderId(scOrderId);
				if (nwaOrderDetailsExtndOptional.isPresent()) {
					NwaOrderDetailsExtnd nwaOrderDetailsExtnd = nwaOrderDetailsExtndOptional.get();
					Optional<TaskAssignment> taskAssignmentOptional = taskAssignmentRepository.findByTaskId(task.getId());
					if (taskAssignmentOptional.isPresent()) {
						TaskAssignment taskAssignment = taskAssignmentOptional.get();
						taskAssignment.setOwner(nwaOrderDetailsExtnd.getFieldOps());
						taskAssignment.setGroupName(nwaOrderDetailsExtnd.getFieldOps());
						taskAssignmentRepository.save(taskAssignment);
					}
				}
			}*/
			task.setDuedate(dueDate);
			task.setScOrderId(scOrder.getId());
			updateAssignmentDetails(task, request);
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			task.setAssignee(request.getAssigneeNameTo());
			constructAssigneeResponse(task, request, assigneeResponse);
			taskRepository.save(task);

			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setAction("Claim");
			mfTaskTrailBean.setCompletedBy(task.getAssignee());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Task claimed by "+ task.getAssignee() + "at" + task.getClaimTime() );
			mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			mfTaskTrailBean.setScenario(scOrder.getScenarioType());
			//mfTaskTrailBean.setComments("Task claimed by "+ task.getAssignee() + "at" + task.getClaimTime());
			this.setTaskTrail(mfTaskTrailBean);

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

						List<MstTaskRegion> mstTaskRegionListCIM = mstTaskRegionRepository.findByGroup("CIM");

						if (!mstTaskRegionListCIM.isEmpty()) {
							ccAddresses.addAll(mstTaskRegionListCIM.stream().filter(re -> re.getEmail() != null)
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
		/*if (userInfoUtils.getUserInformation() != null && userInfoUtils.getUserInformation().getUserId() != null) {

			processTaskLog.setActionFrom(userInfoUtils.getUserInformation().getUserId());
		}
		else {
			processTaskLog.setActionFrom(assigneeRequest.getAssigneeNameFrom());

		}*/
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
//			taskBean.setOrderCategory(task.getOrderCategory());
//			taskBean.setClaimTime(task.getClaimTime());
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
//			taskBean.setCity(task.getCity());
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
//			taskBean.setCity(task.getCity());
			taskBean.setScOrderId(task.getScOrderId());
			taskBean.setOrderType(task.getOrderType());
			taskBean.setServiceType(task.getServiceType());
			taskBean.setCity(task.getCity());
//			taskBean.setCustomerName(task.getCustomerName());
//			String lmScenario = StringUtils.trimToEmpty(task.getLastMileScenario());
//			lmScenario = lmScenario.replaceAll("-", "<br>");
//			taskBean.setLastMileScenario(lmScenario);
//			taskBean.setLmType(task.getLmType());
//			taskBean.setLmProvider(task.getLmProvider());
			if(task.getScServiceDetail()!=null) {
				taskBean.setPmName(task.getScServiceDetail().getAssignedPM());
				taskBean.setScOrderId(task.getScServiceDetail().getScOrder().getId());
				taskBean.setScenarioType(task.getScServiceDetail().getScOrder().getScenarioType());
				taskBean.setTechnologyType(task.getScServiceDetail().getScOrder().getTechnologyType());
				taskBean.setProcessType(task.getScServiceDetail().getScOrder().getProcessType());
				taskBean.setOriginatorGroupId(task.getScServiceDetail().getScOrder().getOriginatorGroupId());
				taskBean.setOriginatorName(task.getScServiceDetail().getScOrder().getOriginatorName());
				taskBean.setOrderType(task.getScServiceDetail().getScOrder().getOrderType());
			}
			taskBean.setIsJeopardyTask(task.getIsJeopardyTask());
//			ScOrder scOrder = task.getScServiceDetail().getScOrder();
//			taskBean.setCustomerLeName(scOrder.getErfCustLeName());
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
			taskBean.setTaskDelayed(isTaskDelayed(task));
//			taskBean.setDowntime(task.getDowntime());
//			taskBean.setDistributionCenterName(task.getDistributionCenterName());
//			taskBean.setIsTxDowntimeReqd(task.getIsTxDowntimeReqd());
//			taskBean.setIsIpDownTimeRequired(task.getIsIpDownTimeRequired());
//			taskBean.setDeviceType(task.getDeviceType());
//			taskBean.setDevicePlatform(task.getDevicePlatform());
//			if(task.getScServiceDetail().getServiceCommissionedDate()!=null)taskBean.setCrfsDate(task.getScServiceDetail().getServiceCommissionedDate());
			taskBean.setTaskAge(ChronoUnit.DAYS.between(task.getCreatedTime().toLocalDateTime(),LocalDateTime.now()));
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
	 * @param //taskAssignments
	 * @param assignedGroupBeans
	 * @param name
	 * @author vivek
	 */
	private void getTaskCountDetails(List<Task> tasks, List<AssignedGroupBean> assignedGroupBeans, String name) {
		if (/*name != null && */!tasks.isEmpty()) {
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
	public TaskBean getTaskBasedOnTaskId(Integer taskId,String wfTaskId) throws TclCommonException {

		if (taskId!= null && wfTaskId!=null) {
			Task task = getTaskByIdAndWfTaskId(taskId, wfTaskId);
			//TaskBean taskbean = new TaskBean();
			if (task != null) {

				Optional<ScServiceDetail> optionalServiceDetails = scServiceDetailRepository
						.findById(task.getServiceId());
				if (optionalServiceDetails.isPresent()
						&& !optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.TERMINATE)
						&& !optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.HOLD)
						&& !optionalServiceDetails.get().getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.CANCELLED)) {
					TaskBean taskbean = new TaskBean();
					if (optionalServiceDetails.get().getPriority() != null) {
						taskbean.setPriority(optionalServiceDetails.get().getPriority());
					} else {
						taskbean.setPriority(1F);
					}
					taskbean.setAssignee(task.getAssignee());
					taskbean.setTaskId(task.getId());
					taskbean.setTaskDefKey(task.getMstTaskDef().getKey());
					taskbean.setWfTaskId(task.getWfTaskId());
					taskbean.setName(task.getMstTaskDef().getName());
					taskbean.setCatagory(task.getCatagory());
					taskbean.setOrderCategory(task.getOrderCategory());
					taskbean.setClaimTime(task.getClaimTime());
					taskbean.setCompletedTime(task.getCompletedTime());
					taskbean.setCreatedTime(task.getCreatedTime());
					taskbean.setOrderCode(task.getOrderCode());
					taskbean.setScOrderId(task.getScOrderId());
					taskbean.setServiceId(task.getServiceId());
					taskbean.setServiceCode(task.getServiceCode());
					taskbean.setTaskDefKey(task.getMstTaskDef().getKey());
					taskbean.setStatus(task.getMstStatus().getCode());
					taskbean.setUpdatedTime(task.getUpdatedTime());
					taskbean.setWfProcessInstId(task.getWfProcessInstId());
					taskbean.setWfTaskId(task.getWfTaskId());
					taskbean.setAssignedGroup(task.getMstTaskDef().getAssignedGroup());
					taskbean.setAssignee(task.getAssignee());
					taskbean.setOwnerGroup(task.getMstTaskDef().getOwnerGroup());
					taskbean.setTat(task.getMstTaskDef().getTat());
					taskbean.setIsJeopardyTask(task.getIsJeopardyTask());
					taskbean.setServiceDeliveryDate(optionalServiceDetails.get().getCommittedDeliveryDate());
					taskbean.setCompletedTime(task.getCompletedTime());

					NwaRejectionDetails nwaRejectionDetails = nwaRejectionDetailsRepository.findByRejectionToTaskAndOrderId(task.getMstTaskDef().getKey(), task.getServiceId());
					Byte jeopardyTask = task.getIsJeopardyTask();
					if(nwaRejectionDetails != null || (task.getIsJeopardyTask() != null && task.getIsJeopardyTask() == 1)){
						taskbean.setRejectionFlow(true);
					}else {
						taskbean.setRejectionFlow(false);
					}

					setTaskDetails(taskbean, task);
					Map<String, Object> commonData = itaskDataService.getTaskData(task);

					  if
					  (optionalServiceDetails.get().getScOrder().getOrderType().equalsIgnoreCase(
					  "NEW")) {

					  ScServiceAttribute scServiceAttribute = scServiceAttributeRepository
					  .findFirstByScServiceDetail_idAndAttributeName(optionalServiceDetails.get().
					  getId(), "WAN IP Address"); if (scServiceAttribute != null) {
					  commonData.put("wanIpAddress", scServiceAttribute.getAttributeValue()); } }

					/*if (commonData.get(AppointmentConstants.APPOINTMENTDATE) != null) {
						Timestamp apTimestamp = (Timestamp) commonData.get(AppointmentConstants.APPOINTMENTDATE);
						taskbean.setReschedule(checkIsReschedule(apTimestamp));
					}

					if ("NPL".equalsIgnoreCase(task.getServiceType())) {
						Map<String, Object> siteBData = itaskDataService.getTaskData(task, "B");
						taskbean.setSiteBData(siteBData);

					}*/
					taskbean.setNonEditable(checkIsNonEditable(task));
					taskbean.setCommonData(commonData);

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
		Integer serviceId = (Integer) varibleMap.get(SERVICE_ID);
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
	 * @param //taskId
	 * @param attachmentIdBeans
	 * @return AttachmentIdBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public DocumentIds uploadMandatoryAttachments(DocumentIds attachmentIdBeans)
			throws TclCommonException {
		Task task =getTaskByIdAndWfTaskId(attachmentIdBeans.getTaskId(),attachmentIdBeans.getWfTaskId());
		ScServiceDetail scServiceDetail=scServiceDetailRepository.findByUuidAndMstStatus_code(task.getServiceCode(),"INPROGRESS");


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
		processTaskLogDetails(task,"CLOSED",attachmentIdBeans.getDelayReason(),null);
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
			String taskName = (String) task.getMstTaskDef().getName();
			System.out.println("=================++++" +task.getOrderCode() );
		    System.out.println("==============Task key " + task.getMstTaskDef().getKey());

		    NwaEorEquipDetails nwaEorEquipDetails = nwaEorEquipDetailsRepository.findByOrderCode(task.getOrderCode());
			if(nwaEorEquipDetails != null) {
				System.out.println("==============Device Type " + nwaEorEquipDetails.getDeviceType());
				if ("verification_and_update_task".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
					if (nwaEorEquipDetails != null && "ALCATEL IP".equalsIgnoreCase(nwaEorEquipDetails.getDeviceType())) {
						LOGGER.info("isAluPe {} ", true);
						map.put("isAluPe", true);
					} else {
						LOGGER.info("isAluPe {} ", false);
						map.put("isAluPe", false);
					}
				}

				String taskKey = task.getMstTaskDef().getKey().toLowerCase();
				switch (taskKey) {
					case "eor_tx_pro_cramer_status_installed_task":
					case "eor_tx_pro_cramer_status_installed_retry":
					case "eor_tx_pro_cramer_status_installed_retry_task":
					case "eor_eth_pro_cramer_status_installed_task":
					case "eor_eth_pro_cramer_status_installed_retry":
					case "eor_tx_card_pro_create_resource_raiseWO1_task":
					case "eor_tx_card_pro_create_resource_raiseWO1_retry":
						if (nwaEorEquipDetails != null && "SDNWNOC_NWA".equalsIgnoreCase(nwaEorEquipDetails.getSdNocSaNoc()) ) {
							LOGGER.info("isSdNoc {} ", true);
							map.put("isSdNoc", true);
						} else {
							LOGGER.info("isSdNoc {} ", false);
							map.put("isSdNoc", false);
						}
				}
				//if ("eor_tx_pro_cramer_status_installed_task".equalsIgnoreCase(task.getMstTaskDef().getKey()) || "eor_tx_pro_cramer_status_installed_retry".equalsIgnoreCase(task.getMstTaskDef().getKey())) {

				//}
			}

		ScOrder scOrder = scOrderRepository.findByOpOrderCode(task.getOrderCode());
		System.out.println("Originator group : "+scOrder.getOriginatorGroupId() );
		System.out.println("order Code : "+task.getOrderCode());
			if("eor_tx_card_pro_raise_task".equalsIgnoreCase(task.getMstTaskDef().getKey()) || "eor_eth_pro_raise_create_device_task".equalsIgnoreCase(task.getMstTaskDef().getKey())){
				if(scOrder != null){

					if(scOrder.getOriginatorGroupId().toUpperCase().contains("GSPI")){
						LOGGER.info("originatorGSPI",true);
						map.put("originatorGSPI", true);
					}else{
						LOGGER.info("originatorGSPI",false);
						map.put("originatorGSPI", false);
					}
				}
			}

			//if(task.getMstTaskDef().getKey().contains("retry") || task.getIsJeopardyTask() == 1){
			if(task.getIsJeopardyTask()!=null && task.getIsJeopardyTask() == 1){
				if("retry".equalsIgnoreCase((String) map.get("action"))){
					LOGGER.info("retry {} ", true);
					map.put("retry", true);
					task.setIsJeopardyTask((byte) 1);
				}else {
					LOGGER.info("isError {} ", false);
					map.put("isError", false);
					map.put("retry", false);
				}

			}
				/*LOGGER.info("xxxxx is {} ", true);
				map.put("xxxx", true);
				map.put("eor_stage_workflow_ID",task.getActivity().getProcess().getStage().getId());
				map.put("eor-ipprov_ID",task.getActivity().getProcess().getId());
				map.put("eorEquipIp",true);*/
				map.put(ORDER_CODE,task.getOrderCode());
				map.put(SERVICE_CODE, task.getOrderCode());
				map.put(SERVICE_ID, task.getServiceId());
				System.out.println("+++++++++++++++++++++" + map);

			Integer serviceId = task.getServiceId();

			flowableBaseService.taskDataEntry(task, map,map);
			processTaskLogDetails(task,TaskLogConstants.CLOSED,"",null);

		System.out.println("======Service Id is "+serviceId);
		System.out.println("=====taskName is "+taskName);
			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setAction("Complete");
			mfTaskTrailBean.setCompletedBy(task.getAssignee());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Task completed by "+ task.getAssignee() );
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			/*NwaRemarksDetails nwaRemarksDetails = nwaRemarksDetailsRepository.findByOrderIdAndByTaskName(serviceId,taskName);
			if (nwaRemarksDetails != null){
				mfTaskTrailBean.setComments(nwaRemarksDetails.getReason());
			}*/

		if(scOrder!=null){
			//ScOrder scOrder1 = scOrder.get();
			mfTaskTrailBean.setScenario(scOrder.getScenarioType());
		}
			mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
			this.setTaskTrail(mfTaskTrailBean);

			return "SUCCESS";
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
//				task.setClaimTime(new Timestamp(new Date().getTime()));
				updateAssignmentDetails(task, request);
				task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
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
			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(request.getServiceCode(), TaskStatusConstants.INPROGRESS);
			isEligibleforAmendement(scServiceDetail);

			saveRemarksForOrderAmendment(scServiceDetail, "INPROGRESS to HOLD");

			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
			scServiceDetailRepository.save(scServiceDetail);

		} else if (status.equalsIgnoreCase(TaskStatusConstants.RESUME)) {

			ScServiceDetail scServiceDetail = scServiceDetailRepository
					.findByUuidAndMstStatus_code(request.getServiceCode(), TaskStatusConstants.HOLD);
			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			scServiceDetailRepository.save(scServiceDetail);
			saveRemarksForOrderAmendment(scServiceDetail, "HOLD to INPROGRESS");

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
			saveRemarksForOrderAmendment(scServiceDetail, "HOLD/INPROGRESS to TERMINATE");

			scServiceDetail.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.TERMINATE));
			scServiceDetailRepository.save(scServiceDetail);

		}

		return serviceDetailRequest;
	}
	
	private void isEligibleforAmendement(ScServiceDetail scServiceDetail) throws TclCommonException {
		List<Task> task = taskRepository.findByServiceIdAndMstTaskDef_keyInAndMstStatus_codeIn(scServiceDetail.getId(),
				Arrays.asList("enrich-service-design", "provide-wbsglcc-details", "po-offnet-lm-provider"),
				Arrays.asList(TaskStatusConstants.CLOSED_STATUS, TaskStatusConstants.OPENED,
						TaskStatusConstants.INPROGRESS));
		Task enrichdesign = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
				scServiceDetail.getId(), "enrich-service-design-async");

		if (!task.isEmpty() || enrichdesign != null) {
			throw new TclCommonException(
					com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.NOT_ELIGIBLE_FOR_ORDER_AMENDMENT,
					ResponseResource.R_CODE_NOT_FOUND);

		}
	}

	private void saveRemarksForOrderAmendment(ScServiceDetail scServiceDetail,String status) {
		String userName = "";
		userName = userInfoUtils.getUserInformation().getUserId();

		TaskRemark taskRemark = new TaskRemark();
		taskRemark.setCreatedDate(new Timestamp(new Date().getTime()));
		taskRemark.setRemarks("Order amendment request from L20 on :" + new Date() + " by user :" + userName);
		taskRemark.setUsername(userName);
		taskRemark.setServiceId(scServiceDetail.getId());
		taskRemarkRepository.save(taskRemark);
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
//				task.setIsJeopardyTask("Y".equalsIgnoreCase(taskRemarksRequest.getIsJeopardy()) ? (byte) 1 : (byte) 0);
				taskRemark.setServiceId(task.getServiceId());
				taskRepository.save(task);
				processTaskLogRepository.save(createProcessTaskLog(task, "REMARKS",
						taskRemarksRequest.getUserComments().length() > 500
								? taskRemarksRequest.getUserComments().substring(0, 500)
								: taskRemarksRequest.getUserComments(),
						null));
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
							null));
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
//			taskRemarksJeopardyBean.setIsJeopardy((taskRemark.getTask().getIsJeopardyTask() == (byte) 1) ? "Y" : "N");
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
//			task.setClaimTime(new Timestamp(new Date().getTime()));
			updateAssignmentDetails(task, request);
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			AssigneeResponse assigneeResponse = new AssigneeResponse();
			constructAssigneeResponse(task, request, assigneeResponse);
			assigneeResponses.add(assigneeResponse);
			taskRepository.save(task);

				MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
				mfTaskTrailBean.setTaskId(task.getId());
				mfTaskTrailBean.setAction("Bulk Assign");
				mfTaskTrailBean.setCompletedBy(task.getAssignee());
				mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
				mfTaskTrailBean.setDescription("Task assigned to "+ task.getAssignee() );
				mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
				mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
				mfTaskTrailBean.setScenario(task.getScOrder().getScenarioType());
				//mfTaskTrailBean.setComments("Task assigned to "+ task.getAssignee());
				this.setTaskTrail(mfTaskTrailBean);

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
				.findAll(TaskSpecification.getTaskFilter(groupName, status, userName, serviceId,null,
						null,null,null,null,null,null,
						null,cutomerName,null,null,null, null,
						false,null,null,null,
						null, null, null, null));
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
				if (optionalServiceDetails.isPresent() && !optionalServiceDetails.get().getMstStatus().getCode()
						.equalsIgnoreCase(TaskStatusConstants.TERMINATE) && !optionalServiceDetails.get().getMstStatus().getCode()
						.equalsIgnoreCase(TaskStatusConstants.HOLD) &&  !optionalServiceDetails.get().getMstStatus().getCode()
						.equalsIgnoreCase(TaskStatusConstants.CANCELLED)) {
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

	public Task findByOrderId(Integer orderId){

		return  taskRepository.findByScOrderId(orderId);
	}

	public Task findByOrderCode(String orderCode){

		return  taskRepository.findByOrderCode(orderCode);
	}

	/*
	 ** =================================================================================
	 **
	 ** This Part of the Code was Developed By Prasad Munaga
	 ** Date: 16 December 2020
	 **
	 ** =================================================================================
	 */

	/**
	 * @param request
	 * @return
	 * @author vivek
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public AssigneeResponse transferTask(AssigneeRequest request) throws TclCommonException {
		System.out.println("=====>> transferTask: " + request);
		AssigneeResponse assigneeResponse = new AssigneeResponse();

		Optional<Task> taskOptional = taskRepository.findById(request.getTaskId());

		if (taskOptional != null && taskOptional.isPresent()) {
			Task task = taskOptional.get();
			if (task.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
				throw new TclCommonRuntimeException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.TASK_CLOSED,
						ResponseResource.R_CODE_ERROR);

			}
			task.setClaimTime(new Timestamp(new Date().getTime()));
			task.setAssignee(request.getAssigneeNameTo());
			updateAssignmentDetails(task, request);
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			task.setAssignee(request.getAssigneeNameTo());
			constructAssigneeResponse(task, request, assigneeResponse);
			taskRepository.save(task);

			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setAction("Transfer");
			mfTaskTrailBean.setCompletedBy(request.getAssigneeNameFrom());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Task transfer from "+ request.getAssigneeNameFrom() + "to " + request.getAssigneeNameTo());
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
			mfTaskTrailBean.setScenario(task.getScOrder().getScenarioType());
			//mfTaskTrailBean.setComments("Task transfer from "+ request.getAssigneeNameFrom() + "to " + request.getAssigneeNameTo());
			this.setTaskTrail(mfTaskTrailBean);


		}

		return assigneeResponse;
	}

	/**
	 * @param taskId
	 * @param reqStatus
	 * @return
	 * @author vivek
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public TaskStatusChangeResponse updateOrderAndTaskStatus(Integer taskId, String reqStatus) throws TclCommonException {
		TaskStatusChangeResponse taskStatusChangeResponse = new TaskStatusChangeResponse();
		Optional<Task> taskOptional = taskRepository.findById(taskId);
		Task task =null;
		if(taskOptional.isPresent()) {
			task = taskOptional.get();
		}

		if (task != null) {
			MstStatus mstStatus = task.getMstStatus();
			if(mstStatus == null) {
				throw new TclCommonRuntimeException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.TASK_CLOSED,
						ResponseResource.R_CODE_ERROR);
			}

			String taskStatus = mstStatus.getCode();
			/*if((TaskStatusConstants.HOLD.equalsIgnoreCase(taskStatus) && TaskStatusConstants.RESUME.equalsIgnoreCase(reqStatus)) ||
					!(TaskStatusConstants.INPROGRESS.equalsIgnoreCase(taskStatus) && "RELEASE".equalsIgnoreCase(reqStatus))) {
				throw new TclCommonRuntimeException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.TASK_CLOSED,
						ResponseResource.R_CODE_ERROR);
			}*/


			String nextStatus = null;
			if(TaskStatusConstants.TERMINATE.equalsIgnoreCase(reqStatus)) {
				nextStatus = TaskStatusConstants.TERMINATE;
			} else if(TaskStatusConstants.RESUME.equalsIgnoreCase(reqStatus)) {
				nextStatus = TaskStatusConstants.INPROGRESS;
			} else if("RELEASE".equalsIgnoreCase(reqStatus)) {
				nextStatus = TaskStatusConstants.OPENED;
			} else if("CLOSED".equalsIgnoreCase(reqStatus)){
				nextStatus = TaskStatusConstants.CLOSED_STATUS;
			}

			taskStatusChangeResponse.setTaskId(taskId);
			task.setMstStatus(taskCacheService.getMstStatus(nextStatus));
			taskRepository.save(task);

			ScOrder scOrder = scOrderRepository.findByOpOrderCode(task.getOrderCode());
			if(scOrder != null) {
				taskStatusChangeResponse.setScOrderId(scOrder.getId());
				scOrder.setOrderStatus(nextStatus);
				scOrderRepository.save(scOrder);

				MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
				mfTaskTrailBean.setTaskId(task.getId());
				mfTaskTrailBean.setAction("Task "+reqStatus );
				mfTaskTrailBean.setCompletedBy(task.getAssignee());
				mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
				mfTaskTrailBean.setDescription("Task change from "+ taskStatus + "to " + nextStatus);
				mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
				mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
				mfTaskTrailBean.setScenario(task.getScOrder().getScenarioType());
				//mfTaskTrailBean.setComments("Task Status change from "+ taskStatus + "to " + nextStatus);
				this.setTaskTrail(mfTaskTrailBean);

			}
		}

		return taskStatusChangeResponse;
	}

	/**
	 * @param taskSuspensionRequest
	 * @return
	 * @author vivek
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public TaskStatusChangeResponse suspendOrderAndTask(TaskSuspensionRequest taskSuspensionRequest) throws TclCommonException {
		TaskStatusChangeResponse taskStatusChangeResponse = new TaskStatusChangeResponse();
		Optional<Task> taskOptional = taskRepository.findById(taskSuspensionRequest.getTaskId());
		Task task =null;
		if(taskOptional.isPresent()) {
			task = taskOptional.get();
		}

		if (task != null) {
			String taskMasterStatus = task.getMstStatus().getCode();
			System.out.println("=====>> taskMasterStatus: " + taskMasterStatus);
			if (MstStatusConstant.CLOSED.equalsIgnoreCase(taskMasterStatus) /* ||
					MstStatusConstant.OPEN.equalsIgnoreCase(taskMasterStatus) */) {
				throw new TclCommonRuntimeException(
						com.tcl.dias.servicefulfillmentutils.constants.ExceptionConstants.TASK_CLOSED,
						ResponseResource.R_CODE_ERROR);

			}
			// task.setClaimTime(new Timestamp(new Date().getTime()));
			taskStatusChangeResponse.setTaskId(task.getId());

			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.HOLD));
			taskRepository.save(task);

			// Create and update Task Suspension Reason Table
			NwaSuspensionReason nwaSuspensionReason = new NwaSuspensionReason();
			nwaSuspensionReason.setSuspensionReason(taskSuspensionRequest.getSuspensionReason());
			nwaSuspensionReason.setSuspendTillDate(new Timestamp(taskSuspensionRequest.getSuspensionUpto().getTime()));
			nwaSuspensionReason.setTask(task);

			ScOrder scOrder = scOrderRepository.findByOpOrderCode(task.getOrderCode());
			if(scOrder != null) {
				scOrder.setOrderStatus(TaskStatusConstants.HOLD);
				nwaSuspensionReason.setOrderCode(scOrder.getOpOrderCode());
				nwaSuspensionReason.setScOrder(scOrder);
				taskStatusChangeResponse.setScOrderId(scOrder.getId());
				scOrderRepository.save(scOrder);
			}

			nwaSuspensionReasonRepository.save(nwaSuspensionReason);

			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setAction("Suspend");
			mfTaskTrailBean.setCompletedBy(task.getAssignee());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Task suspended upto "+ taskSuspensionRequest.getSuspensionUpto().getTime() );
			mfTaskTrailBean.setComments(taskSuspensionRequest.getSuspensionReason());
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
			mfTaskTrailBean.setScenario(task.getScOrder().getScenarioType());
			//mfTaskTrailBean.setComments("Task suspended for "+ taskSuspensionRequest.getSuspensionReason() +"and upto "+ taskSuspensionRequest.getSuspensionUpto().getTime() );
			this.setTaskTrail(mfTaskTrailBean);
		}
		return taskStatusChangeResponse;
	}

	/**
	 * Method to retrieve all tasks status count based on Admin supplied
	 *
	 * @author krutsrin
	 * @param owner
	 * @return List @{AssignedGroupBean}
	 */
	public List<AssignedGroupBean> getAdminChart(String owner, String createdTimeFrom, String createdTimeTo) {

		List<AssignedGroupBean> assignedGroupBeans = new ArrayList<>();
		List<Task> tasks = taskRepository.findAll(TaskSpecification.getTaskSummary(null, getStatus(), null, null, null,
				null, null, null, null, null, createdTimeFrom, createdTimeTo, owner));
		getTaskCountDetails(tasks, assignedGroupBeans, owner);
		return assignedGroupBeans;

	}

	/**
	 * Validate and get Task created from and to date
	 *
	 * @param month
	 * @param fromDate
	 * @param toDate
	 * @return {@link Map}
	 */
	private Map<String, LocalDate> getTaskCreatedFromAndToDate(String month, String fromDate, String toDate) {
		Map<String, LocalDate> taskCreatedDates = new HashMap<>();
		if (Objects.nonNull(month)) {
			taskCreatedDates = Utils.getStartAndEndDateOfSpecificMonth(Integer.valueOf(month));
		} else if (Objects.nonNull(fromDate) && Objects.nonNull(toDate)) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern(YYYY_M_D);
			taskCreatedDates.put(FIRST_DAY_OF_MONTH, LocalDate.parse(fromDate, formatter));
			taskCreatedDates.put(LAST_DAY_OF_MONTH, LocalDate.parse(toDate, formatter));
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.DATES_AND_MONTH_FOR_TASK_SUMMARY_NULL,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
		return taskCreatedDates;
	}

	/**
	 * Method to get all task details
	 *
	 * @param taskCreatedFromDate
	 * @param taskCreatedToDate
	 * @return {@link List}
	 */
	private List<Map<String, Object>> getApprovedAndUnApprovedTaskDetails(LocalDate taskCreatedFromDate,
																		  LocalDate taskCreatedToDate) {
		List<Map<String, Object>> approvedTaskDetails = taskRepository
				.findApprovedTaskDetailsWithinDateRange(taskCreatedFromDate, taskCreatedToDate);
		List<Integer> approvedTaskIds = Optional.ofNullable(approvedTaskDetails).map(tasks -> {
			return tasks.stream().map(task -> (Integer) task.get(TASK_ID)).collect(Collectors.toList());
		}).orElse(null);

		List<Map<String, Object>> unapprovedTaskDetails = new ArrayList<>();
		if (!CollectionUtils.isEmpty(approvedTaskDetails)) {
			unapprovedTaskDetails = taskRepository.findUnapprovedTaskDetailsWithinDateRange(taskCreatedFromDate,
					taskCreatedToDate, approvedTaskIds);
		} else {
			unapprovedTaskDetails = taskRepository.findUnapprovedTaskDetailsWithinDateRange(taskCreatedFromDate,
					taskCreatedToDate);

		}

		return Stream.concat(approvedTaskDetails.stream(), unapprovedTaskDetails.stream()).collect(Collectors.toList());
	}

	/**
	 * Get Task Summary by assignee and status
	 *
	 * @param taskDetails
	 * @return {@link List}
	 */
	private List<TaskAssigneeSummaryBean> getTaskSummaryByAssigneeAndStatus(List<Map<String, Object>> taskDetails) {
		if (!CollectionUtils.isEmpty(taskDetails)) {
			return taskDetails.stream().map(summary -> {
				TaskAssigneeSummaryBean taskAssigneeSummaryBean = new TaskAssigneeSummaryBean();
				taskAssigneeSummaryBean.setTaskId((Integer) summary.get(TASK_ID));
				taskAssigneeSummaryBean.setTaskAssigmentId((Integer) summary.get(TASK_ASSIGNMENT_ID));
				taskAssigneeSummaryBean.setTaskAssigneeUserName((String) summary.get(TASK_ASSIGNMENT_USER_NAME));
				taskAssigneeSummaryBean.setTaskStatus((String) summary.get(STATUS));
				return taskAssigneeSummaryBean;
			}).collect(Collectors.toList());
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.TASK_DETAILS_EMPTY,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Group task summary by assignee name
	 *
	 * @param taskAssigneeSummaryBeans
	 * @param taskSummaryResponse
	 * @return {@link Map}
	 */
	private Map<String, List<TaskAssigneeSummaryBean>> groupTaskSummaryByAssigneeName(
			List<TaskAssigneeSummaryBean> taskAssigneeSummaryBeans, TaskSummaryResponse taskSummaryResponse) {
		taskSummaryResponse.setTotalUnassignedTasks(String.valueOf(taskAssigneeSummaryBeans.stream()
				.filter(summary -> Objects.isNull(summary.getTaskAssigneeUserName())).count()));
		taskSummaryResponse.setTotalAssignedTasks(String.valueOf(taskAssigneeSummaryBeans.stream()
				.filter(summary -> Objects.nonNull(summary.getTaskAssigneeUserName())).count()));
		return taskAssigneeSummaryBeans.stream().filter(summary -> Objects.nonNull(summary.getTaskAssigneeUserName()))
				.collect(Collectors.groupingBy(summary -> summary.getTaskAssigneeUserName()));
	}

	/**
	 * Group task status of assigness
	 *
	 * @param groupByUserName
	 * @return {@link Map}
	 */
	private Map<String, List<String>> groupTaskStatusOfAssignees(
			Map<String, List<TaskAssigneeSummaryBean>> groupByUserName) {
		Map<String, List<String>> groupUserNameByStatus = new HashMap<>();
		if (!CollectionUtils.isEmpty(groupByUserName)) {
			groupByUserName.entrySet().stream().forEach(summary -> {
				String userName = summary.getKey();
				List<String> statusListOfUser = summary.getValue().stream().map(TaskAssigneeSummaryBean::getTaskStatus)
						.collect(Collectors.toList());
				groupUserNameByStatus.put(userName, statusListOfUser);
			});
		}
		return groupUserNameByStatus;
	}

	private List<String> getStatusForSummary() {

		List<String> status = new ArrayList<>();
		status.add(TaskStatusConstants.CLOSED_STATUS);
		status.add(TaskStatusConstants.INPROGRESS);
		status.add(TaskStatusConstants.OPENED);
		status.add(TaskStatusConstants.REOPEN);
		status.add(TaskStatusConstants.PENDING);
		status.add(TaskStatusConstants.RETURNED);
		return status;
	}

	/**
	 * Get Count of tasks by status for each assignee
	 *
	 * @param groupUserNameByStatus
	 * @return {@link List}
	 */
	private List<TaskSummary> getCountOfTasksByStatusForEachAssignee(Map<String, List<String>> groupUserNameByStatus) {
		List<TaskSummary> taskSummaries = new ArrayList<>();
		if (!CollectionUtils.isEmpty(groupUserNameByStatus)) {
			taskSummaries = groupUserNameByStatus.entrySet().stream().map(summary -> {
				TaskSummary taskSummary = new TaskSummary();
				taskSummary.setCommercialManagerName(summary.getKey());

				Map<String, Long> statusCount = new HashMap<>();
				long inprogress = summary.getValue().stream().filter(status -> status.equalsIgnoreCase(INPROGRESS))
						.count();
				long closed = summary.getValue().stream().filter(status -> status.equalsIgnoreCase(CLOSED)).count();
				taskSummary.setInProgressTasks(String.valueOf(inprogress));
				taskSummary.setClosedTasks(String.valueOf(closed));
				taskSummary.setTotalTasks(String.valueOf(inprogress + closed));

				return taskSummary;
			}).collect(Collectors.toList());
		}
		return taskSummaries;
	}

	/**
	 * Get task summary by status and assignee for commercial work flow
	 *
	 * @param month
	 * @param fromDate
	 * @param toDate
	 * @return {@link TaskSummaryResponse}
	 */
	public TaskSummaryResponse gettaskSummary(String month, String fromDate,
																		  String toDate) {
		TaskSummaryResponse taskSummaryResponse = new TaskSummaryResponse();

		Map<String, LocalDate> taskCreatedDates = getTaskCreatedFromAndToDate(month, fromDate, toDate);
		LocalDate taskCreatedFromDate = taskCreatedDates.get(FIRST_DAY_OF_MONTH);
		LocalDate taskCreatedToDate = taskCreatedDates.get(LAST_DAY_OF_MONTH).plusDays(1);

		List<Map<String, Object>> taskDetails = getApprovedAndUnApprovedTaskDetails(taskCreatedFromDate,
				taskCreatedToDate);

		List<TaskAssigneeSummaryBean> taskAssigneeSummaryBeans = getTaskSummaryByAssigneeAndStatus(taskDetails);
		Map<String, List<TaskAssigneeSummaryBean>> groupByUserName = groupTaskSummaryByAssigneeName(
				taskAssigneeSummaryBeans, taskSummaryResponse);
		Map<String, List<String>> groupUserNameByStatus = groupTaskStatusOfAssignees(groupByUserName);
		List<TaskSummary> taskSummary = getCountOfTasksByStatusForEachAssignee(groupUserNameByStatus);

		taskSummaryResponse.setTaskSummaryList(taskSummary);
		return taskSummaryResponse;
	}

	/**
	 * Method to get assignedtask trail
	 *
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	public List<MfTaskDetailAuditBean> getAssignedTaskTrail(Integer taskId) throws TclCommonException {
		List<MfTaskDetailAuditBean> assignedTaskTrail = new ArrayList<>();
		try {
			LOGGER.info("Inside TaskService.getAssignedTaskTrail method to fetch getAssignedTaskTrail for taskId {} ",
					taskId);
			List<MfTaskDetailAudit> assignedTaskAudit = mfTaskDetailAuditRepository.findByTaskId(taskId);
			assignedTaskAudit.stream().forEach(audit -> {
				assignedTaskTrail.add(new MfTaskDetailAuditBean(audit));
			});

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return assignedTaskTrail;
	}

	public Set<MfTaskTrailBean> getTaskTrail(Integer taskId, String completedBy){

		Optional<ArrayList<MfTaskTrail>> mfTaskTrailSetOptional = mfTaskTrailRepository.findByTaskIdAndCompletedBy(taskId,completedBy);

		Set<MfTaskTrailBean> mfTaskTrailBeanSet = new HashSet<>();
		if(mfTaskTrailSetOptional.isPresent()){
			ArrayList<MfTaskTrail> mfTaskTrailSet = mfTaskTrailSetOptional.get();
			Iterator<MfTaskTrail> it =  mfTaskTrailSet.iterator() ;
			if(it != null){
				while (it.hasNext()){

					MfTaskTrail mfTaskTrail = it.next();
					MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();

					mfTaskTrailBean.setTaskId(mfTaskTrail.getTaskId());
					mfTaskTrailBean.setAction(mfTaskTrail.getAction());
					mfTaskTrailBean.setCompletedBy(mfTaskTrail.getCompletedBy());
					mfTaskTrailBean.setCreatedTime(mfTaskTrail.getCreatedTime());
					mfTaskTrailBean.setDescription(mfTaskTrail.getDescription());

					mfTaskTrailBeanSet.add(mfTaskTrailBean);
				}

			}
		}

		return  mfTaskTrailBeanSet;

	}

	public List<MfTaskTrailBean> getTaskTrails(Integer taskId){

		Optional<ArrayList<MfTaskTrail>> mfTaskTrailSetOptional = mfTaskTrailRepository.findByTaskId(taskId);

		List<MfTaskTrailBean> mfTaskTrailList = new ArrayList<>();

		if(mfTaskTrailSetOptional.isPresent()){
			ArrayList<MfTaskTrail> mfTaskTrailSet = mfTaskTrailSetOptional.get();
			Iterator<MfTaskTrail> it =  mfTaskTrailSet.iterator() ;
			if(it != null){

				while (it.hasNext()){

					MfTaskTrail mfTaskTrail = it.next();
					MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();

					mfTaskTrailBean.setTaskId(mfTaskTrail.getTaskId());
					mfTaskTrailBean.setAction(mfTaskTrail.getAction());
					mfTaskTrailBean.setCompletedBy(mfTaskTrail.getCompletedBy());
					mfTaskTrailBean.setCreatedTime(mfTaskTrail.getCreatedTime());
					mfTaskTrailBean.setDescription(mfTaskTrail.getDescription());
					mfTaskTrailBean.setUserGroup(mfTaskTrail.getUserGroup());
					mfTaskTrailBean.setTaskName(mfTaskTrail.getTaskName());
					mfTaskTrailBean.setScenario(mfTaskTrail.getScenario());
					mfTaskTrailBean.setComments(mfTaskTrail.getComments());

					mfTaskTrailList.add(mfTaskTrailBean);

					}

			}
		}

		//List<MfTaskTrailBean> mfTaskTrailList = new ArrayList<>(mfTaskTrailBeanSet);
		//mfTaskTrailList.sort(Comparator.comparing(MfTaskTrailBean::getCreatedTime));

		return  mfTaskTrailList;

	}

	public MfTaskTrailBean setTaskTrail(MfTaskTrailBean mfTaskTrailBean) {

		MfTaskTrail mfTaskTrail = new MfTaskTrail();

		mfTaskTrail.setTaskId(mfTaskTrailBean.getTaskId());
		mfTaskTrail.setAction(mfTaskTrailBean.getAction());
		mfTaskTrail.setCompletedBy(mfTaskTrailBean.getCompletedBy());
		mfTaskTrail.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
		mfTaskTrail.setDescription(mfTaskTrailBean.getDescription());
		mfTaskTrail.setUserGroup(mfTaskTrailBean.getUserGroup());
		mfTaskTrail.setTaskName(mfTaskTrailBean.getTaskName());
		mfTaskTrail.setScenario(mfTaskTrailBean.getScenario());
		mfTaskTrail.setComments(mfTaskTrailBean.getComments());

		mfTaskTrailRepository.save(mfTaskTrail);

		return  mfTaskTrailBean;

	}

	public List<TaskBean> getTasksCompletedBeforeReject(String orderCode, Integer taskId) {

		List<Task> task = taskRepository.findByOrderCodeAndIdLessThan(orderCode, taskId);
		List<TaskBean> taskBeanList = null;
		if (task != null) {
			taskBeanList = task.stream().map(task1 -> {
				TaskBean dto = new TaskBean();
				dto.setTaskId(task1.getId());
				dto.setTaskDefKey(task1.getMstTaskDef().getKey());
				return dto;
			}).collect(Collectors.toList());
		}
		System.out.println("======= Task is " + task);
		return taskBeanList;

	}

	public List<TaskBean> getTasksForwardAfterReject(String orderCode, Integer taskId) {

		List<Task> task = taskRepository.findTasksToList(orderCode, taskId);
		List<TaskBean> taskBeanList = null;
		if (task != null) {
			taskBeanList = task.stream().map(task1 -> {
				TaskBean dto = new TaskBean();
				dto.setTaskId(task1.getId());
				dto.setTaskDefKey(task1.getMstTaskDef().getKey());
				return dto;
			}).collect(Collectors.toList());
		}
		System.out.println("======= Task is " + task);
		return taskBeanList;

	}


	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String manuallyCompleteRejectTask(Integer taskId, RejectionTaskBean rejectionTaskBean) {

		System.out.println("===== Inside manuallyCompleteRejectTask method");
		try {
		Map<String, Object> map= new HashMap<>();

			Task task = getTaskById(taskId);
			String taskName = task.getMstTaskDef().getName();
			Activity activity = task.getActivity();
			List<Task> rejTask = taskRepository.findByMstTaskDef_key(rejectionTaskBean.getRejectionToTask());
			String rejActivityKey = rejTask.get(0).getActivity().getMstActivityDef().getKey();

			map.put("rejection",true);
			map.put("rejectionToTask",rejActivityKey);
			LOGGER.info("TASK COMPLETION AT REJECTION FOR TASKID {} REJECTION IS {} REJECTIONTOACTIVITY IS {} ", taskId, true, rejActivityKey);
			//ScOrder scOrder = task.getScOrder();
			//Integer orderId = scOrder.getId();
			Integer orderId = task.getServiceId();
			System.out.println("=======orderId"+orderId);
			// Updating rejection details table
			if (task.getNwaRejectionDetails() != null) {
				NwaRejectionDetails nwaRejectionDetails = new NwaRejectionDetails();
				nwaRejectionDetails.setRejectionReason(rejectionTaskBean.getRejectionReason());
				nwaRejectionDetails.setRejectionToTask(rejectionTaskBean.getRejectionToTask());
				nwaRejectionDetails.setRejectionType(rejectionTaskBean.getRejectionType());
				nwaRejectionDetails.setRejectedByUser(rejectionTaskBean.getRejectedByUser());
				nwaRejectionDetails.setTask(task);
				nwaRejectionDetails.setOrderId(orderId);
				nwaRejectionDetailsRepository.save(nwaRejectionDetails);
			}

			//if (scOrder != null && "EOREQUIPIP".equalsIgnoreCase(scOrder.getOrderType())){
				LOGGER.info("xxxxx is {} ", true);
				map.put("xxxx", true);
				map.put("eor_stage_workflow_ID",task.getActivity().getProcess().getStage().getId());
				map.put("eor-ipprov_ID",task.getActivity().getProcess().getId());
				map.put("eorEquipIp",true);
				map.put(ORDER_CODE,task.getOrderCode());
				map.put(SERVICE_CODE, task.getOrderCode());
				map.put(SERVICE_ID, task.getScOrderId());
				LOGGER.info("variable map while completing rejected task is {} ", map);
				System.out.println("+++++++++++++++++++++" + map);
			//}
			Integer serviceId = (Integer) map.get("SERVICE_ID");

			flowableBaseService.taskDataEntry(task, map,map);
			processTaskLogDetails(task,TaskLogConstants.CLOSED,"",null);

			/*System.out.println("======Service Id is "+serviceId);
			System.out.println("=====taskName is "+taskName);
			NwaRemarksDetails nwaRemarksDetails = nwaRemarksDetailsRepository.findByOrderIdAndByTaskName(serviceId,taskName);*/
			MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
			mfTaskTrailBean.setTaskId(task.getId());
			mfTaskTrailBean.setAction("Rejection");
			mfTaskTrailBean.setCompletedBy(task.getAssignee());
			mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
			mfTaskTrailBean.setDescription("Task rejected by "+ task.getAssignee() );
			mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
			mfTaskTrailBean.setComments(rejectionTaskBean.getRejectionReason());
			//mfTaskTrailBean.setScenario(scOrder.getScenarioType());
			mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
			this.setTaskTrail(mfTaskTrailBean);

			//return "SUCCESS";

			//Rejection set
			/*taskDataEntryReject(task, map, map);
			processTaskLogDetails(task, TaskLogConstants.CLOSED, "", null);*/

			return "SUCCESS";

		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}

}

