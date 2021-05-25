
package com.tcl.dias.l2oworkflowutils.service.v1;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import com.tcl.dias.l2oworkflowutils.beans.*;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.flowable.cmmn.api.CmmnRuntimeService;
import org.flowable.cmmn.api.runtime.CaseInstance;
import org.flowable.cmmn.api.runtime.PlanItemInstance;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.runtime.Execution;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kie.api.runtime.KieSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mchange.lang.IntegerUtils;
import com.mchange.lang.ObjectUtils;
import com.tcl.dias.common.beans.AdminReportBean;
import com.tcl.dias.common.beans.CommercialTaskDetailsBean;
import com.tcl.dias.common.beans.CompleteTaskBean;
import com.tcl.dias.common.beans.CreateResponseBean;
import com.tcl.dias.common.beans.FRAManResponse;
import com.tcl.dias.common.beans.FRAOffnetWirelessResponse;
import com.tcl.dias.common.beans.FRAOffnetWirelineResponse;
import com.tcl.dias.common.beans.FRARadwinResponse;
import com.tcl.dias.common.beans.FRAUbrResponse;
import com.tcl.dias.common.beans.FetchResponseBean;
import com.tcl.dias.common.beans.MfDetailAttributes;
import com.tcl.dias.common.beans.MfDetailsBean;
import com.tcl.dias.common.beans.MfL2OReportBean;
import com.tcl.dias.common.beans.MfAttachmentBean;
import com.tcl.dias.common.beans.MfResponseDetailBean;
import com.tcl.dias.common.beans.OmsAttachBean;
import com.tcl.dias.common.beans.OmsListenerBean;
import com.tcl.dias.common.beans.PagedResult;
import com.tcl.dias.common.beans.PartnerDocumentBean;
import com.tcl.dias.common.beans.PriceDiscountBean;
import com.tcl.dias.common.beans.ResponseResource;
import com.tcl.dias.common.beans.TaskDetailBean;
import com.tcl.dias.common.beans.TaskTrailResponseBean;
import com.tcl.dias.common.beans.TempUploadUrlInfo;
import com.tcl.dias.common.beans.UserGroupBeans;
import com.tcl.dias.common.constants.AttachmentTypeConstants;
import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.constants.IzosdwanCommonConstants;
import com.tcl.dias.common.constants.LeAttributesConstants;
import com.tcl.dias.common.file.storage.services.v1.FileStorageService;
import com.tcl.dias.common.utils.Constants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Source;
import com.tcl.dias.common.utils.Status;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.l2oworkflow.entity.entities.CommercialTaskDetails;
import com.tcl.dias.l2oworkflow.entity.entities.FeasibilityCategoryMap;
import com.tcl.dias.l2oworkflow.entity.entities.MfBtsData;
import com.tcl.dias.l2oworkflow.entity.entities.MfDependantTeam;
import com.tcl.dias.l2oworkflow.entity.entities.MfDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfHhData;
import com.tcl.dias.l2oworkflow.entity.entities.MfPopData;
import com.tcl.dias.l2oworkflow.entity.entities.MfProviderData;
import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfResponseDetailAudit;
import com.tcl.dias.l2oworkflow.entity.entities.MfSupportGroup;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetail;
import com.tcl.dias.l2oworkflow.entity.entities.MfTaskDetailAudit;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskAssignment;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskDef;
import com.tcl.dias.l2oworkflow.entity.entities.PreMfResponse;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessTaskLog;
import com.tcl.dias.l2oworkflow.entity.entities.SiteDetail;
import com.tcl.dias.l2oworkflow.entity.entities.Task;
import com.tcl.dias.l2oworkflow.entity.entities.TaskAssignment;
import com.tcl.dias.l2oworkflow.entity.entities.TaskData;
import com.tcl.dias.l2oworkflow.entity.entities.TaskPlan;
import com.tcl.dias.l2oworkflow.entity.repository.AppointmentDocumentsRepository;
import com.tcl.dias.l2oworkflow.entity.repository.AppointmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.CommercialTaskDetailsRepository;
import com.tcl.dias.l2oworkflow.entity.repository.FeasibilityCategoryMapRepository;
import com.tcl.dias.l2oworkflow.entity.repository.FieldEngineerRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfBtsDataRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfDependantTeamRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfHhDataRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfPopDataRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfProviderDataRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfResponseDetailAuditRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfResponseDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfSupportGroupRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailAuditRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfTaskPlanItemRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MfVendorDataRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstAppointmentDocumentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstAppointmentSlotsRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskAssignmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.MstTaskDefRepository;
import com.tcl.dias.l2oworkflow.entity.repository.PreMfResponseRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessRepository;
import com.tcl.dias.l2oworkflow.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.l2oworkflow.entity.repository.SiteDetailRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.l2oworkflow.entity.repository.TaskPlanRepository;
import com.tcl.dias.l2oworkflow.specification.TaskLogSpecification;
import com.tcl.dias.l2oworkflow.specification.TaskSpecification;
import com.tcl.dias.l2oworkflowutils.abstractservice.ITaskDataService;
import com.tcl.dias.l2oworkflowutils.beans.AssignedGroupBean;
import com.tcl.dias.l2oworkflowutils.beans.AssignedGroupingBean;
import com.tcl.dias.l2oworkflowutils.beans.AssigneeRequest;
import com.tcl.dias.l2oworkflowutils.beans.AssigneeResponse;
import com.tcl.dias.l2oworkflowutils.beans.BtsDataBean;
import com.tcl.dias.l2oworkflowutils.beans.CommercialTaskDataBean;
import com.tcl.dias.l2oworkflowutils.beans.CreateMfTaskRequestBean;
import com.tcl.dias.l2oworkflowutils.beans.FeasibilityCategoryRequest;
import com.tcl.dias.l2oworkflowutils.beans.MfDetailManualUpdateBean;
import com.tcl.dias.l2oworkflowutils.beans.MfHhDataBean;
import com.tcl.dias.l2oworkflowutils.beans.MfPopDataBean;
import com.tcl.dias.l2oworkflowutils.beans.MfProviderDataBean;
import com.tcl.dias.l2oworkflowutils.beans.MfTaskData;
import com.tcl.dias.l2oworkflowutils.beans.MfTaskDataBean;
import com.tcl.dias.l2oworkflowutils.beans.MfTaskDetailAuditBean;
import com.tcl.dias.l2oworkflowutils.beans.MfTaskDetailBean;
import com.tcl.dias.l2oworkflowutils.beans.MfTaskRequestBean;
import com.tcl.dias.l2oworkflowutils.beans.MfVendorDataBean;
import com.tcl.dias.l2oworkflowutils.beans.OmsTaskDetailBean;
import com.tcl.dias.l2oworkflowutils.beans.ProcessTaskLogBean;
import com.tcl.dias.l2oworkflowutils.beans.QuoteDetailForTask;
import com.tcl.dias.l2oworkflowutils.beans.TaskAssigneeSummaryBean;
import com.tcl.dias.l2oworkflowutils.beans.TaskAssignmentBean;
import com.tcl.dias.l2oworkflowutils.beans.TaskBean;
import com.tcl.dias.l2oworkflowutils.beans.TaskDataBean;
import com.tcl.dias.l2oworkflowutils.beans.TaskGroupBean;
import com.tcl.dias.l2oworkflowutils.beans.TaskRequest;
import com.tcl.dias.l2oworkflowutils.beans.TaskResponse;
import com.tcl.dias.l2oworkflowutils.beans.TaskSummary;
import com.tcl.dias.l2oworkflowutils.beans.TaskSummaryResponse;
import com.tcl.dias.l2oworkflowutils.beans.drools.ManualFeasibility;
import com.tcl.dias.l2oworkflowutils.constants.ExceptionConstants;
import com.tcl.dias.l2oworkflowutils.constants.GroupByConstants;
import com.tcl.dias.l2oworkflowutils.constants.ManualFeasibilityWFConstants;
import com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants;
import com.tcl.dias.l2oworkflowutils.constants.MstStatusConstant;
import com.tcl.dias.l2oworkflowutils.constants.TaskLogConstants;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;
import com.tcl.dias.l2oworkflowutils.mfutils.ExcelUtil;
import com.tcl.dias.l2oworkflowutils.mfutils.MfAttachmentUtil;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import com.tcl.gvg.exceptionhandler.custom.TclCommonRuntimeException;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import com.tcl.dias.l2oworkflow.entity.repository.SupplierIORRepository;

//import com.tcl.dias.wfe.drools.feasibilty.Feasiblity;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
@Transactional(readOnly = false)
public class TaskService extends ServiceFulfillmentBaseService {
	public static final String TASK_ID = "taskId";
	public static final String TO = "_To_";
	public static final String TCV = "tcv";
	public static final String FEASIBLE = "FEASIBLE";
	public static final String OFFNET_WIRELESS = "Offnet Wireless";
	public static final String LM_NRC_MAST_OFRF = "lm_nrc_mast_ofrf";
	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);
	public static final String FIRST_DAY_OF_MONTH = "firstDayOfMonth";
	public static final String LAST_DAY_OF_MONTH = "lastDayOfMonth";
	public static final String YYYY_M_D = "yyyy-M-d";
	public static final String TASK_ASSIGNMENT_ID = "taskAssignmentId";
	public static final String TASK_ASSIGNMENT_USER_NAME = "taskAssignmentUserName";
	public static final String STATUS = "status";
	public static final String CLOSED = "CLOSED";
	public static final String INPROGRESS = "INPROGRESS";
	public static final String TASK_DETAIL_REPORT = "TaskDetailReport";
	public static final String FROM = "_From_";
	public static final String COMMERCIAL_MANAGER_NAME = "Commercial Manager Name";
	public static final String OPPORTUNITY = "OpportunityId";
	public static final String QUOTE = "Quote";
	public static final String ACCOUNT_NAME = "Account Name";
	public static final String REGION = "Region";
	public static final String ASSIGNED_DATE = "Created Date/ Assigned Date";
	public static final String APPROVAL_DATE = "Pricing Closure Date / Approval Date";
	public static final String OPPORTUNITY_ID = "opportunityId";
	public static final String QUOTE_CODE = "quoteCode";
	public static final String STATUS1 = "status";
	public static final String ACCOUNTNAME = "accountName";
	public static final String CREATED_DATE = "createdDate";
	public static final String COMPLETED_DATE = "completedDate";
	public static final String APPLICATION_MS_EXCEL = "application/ms-excel";
	public static final String TAT_DAYS = "TAT (Days)";
	public static final String TAT_CREATEDTIME = "tatCreatedtime";
	public static final String TAT_ENDTIME = "tatEndtime";
	public static final String XLSX = ".xlsx";
	public static final String TOTAL_UNASSIGNED_TASKS = "Total unassigned tasks : ";
	public static final String ACV = "ACV";
	public static final String ONNET = "ONNET";
	public static final String OFFNET = "OFFNET";
	public static final String OFFNET_WIRELINE = "Offnet wireline";
	public static final String LM_NRC_BW_PROV_OFRF = "lm_nrc_bw_prov_ofrf";
	public static final String LM_ARC_BW_PROV_OFRF = "lm_arc_bw_prov_ofrf";
	public static final String OTC_MODEM_CHARGES = "otc_modem_charges";
	public static final String ARC_MODEM_CHARGES = "arc_modem_charges";
	public static final String BW_MBPS = "bw_mbps";
	public static final String ORCH_LM_TYPE = "Orch_LM_Type";
	public static final String ORCH_CATEGORY = "Orch_Category";
	public static final String ORCH_CONNECTION = "Orch_Connection";
	public static final String RETURNED = "Return";
	public static final String ARC_BW = "arc_bw";
	public static final String TASK_DEF_KEY = "taskDefKey";
	public static final String DISCOUNT_LEVEL = "Discount Level";
	public static final String CDA_ARC = "CDA Approved ARC";
	public static final String CDA_NRC = "CDA Approved NRC";
	public static final String APPROVAL_COMMENTS = "Approval Comments";
	public static final String REJECT_COMMENTS ="Rejection Comments";
	public static final String ENGINE_ARC = "Engine Provided ARC";
	public static final String ENGINE_NRC = "Engine Provided NRC";
	public static final String TASK_DATA = "taskData";


	DecimalFormat df = new DecimalFormat("0.00");

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
	SupplierIORRepository supplierIORRepository;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	AppointmentDocumentsRepository appointmentDocumentsRepository;

	@Autowired
	FieldEngineerRepository fieldEngineerRepository;

	@Autowired
	MstAppointmentSlotsRepository mstAppointmentSlotsRepository;

	@Autowired
	MstAppointmentDocumentRepository mstAppointmentDocumentRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	SiteDetailRepository siteDetailRepository;

	@Autowired
	TaskPlanRepository taskPlanRepository;

	@Autowired
	TATService tatService;

	@Autowired
	CmmnRuntimeService cmmnRuntimeService;

	@Autowired
	CommercialTaskDetailsRepository commercialTaskDetailsRepository;

	@Autowired
	MfSupportGroupRepository mfSupportGroupRepository;

	@Autowired
	MfTaskPlanItemRepository mfTaskPlanItemRepository;

	@Autowired
	MstTaskDefRepository mstTaskDefRepository;

	@Autowired
	MfTaskDetailRepository mfTaskDetailRepository;

	@Autowired
	MfPopDataRepository mfPopDataRepository;

	@Autowired
	MfResponseDetailRepository mfResponseDetailRepository;

	@Autowired
	MfBtsDataRepository mfBtsDataRepository;

	@Autowired
	MfVendorDataRepository mfVendorDataRepository;

	@Autowired
	MfProviderDataRepository mfProviderDataRepository;

	@Autowired
	MfHhDataRepository mfHhDataRepository;

	@Autowired
	FeasibilityCategoryMapRepository feasibilityCategoryMapRepository;

	@Autowired //NOSONAR
	@Qualifier(value = "manualPolicy") //NOSONAR
	private KieSession kieSession; //NOSONAR
    
	@Autowired
	MfDetailRepository mfDetailRepository;

	@Autowired
	MfDependantTeamRepository mfDependantTeamRepository;

	@Autowired
	MfResponseDetailAuditRepository mfResponseDetailAuditRepository;

	@Autowired
	MfTaskDetailAuditRepository mfTaskDetailAuditRepository;

	@Value("${save.mf.response.in.oms.mq}")
	String saveMFResponseInOmsMQ;
	
	@Value("${cmd.task.queue}")
	String cmdTaskUpdateQueue;
	
	@Autowired
	MstTaskAssignmentRepository mstTaskAssignmentRepository;
	
	@Value("${swift.api.enabled}")
	String swiftApiEnabled;
	
	@Autowired
    FileStorageService fileStorageService;
	
    @Value("${temp.upload.url.expiryWindow}")
    String tempUploadUrlExpiryWindow;
    
    @Autowired
    MfAttachmentUtil mfAttachmentUtil;
    
    @Value("${oms.mf.attatchment.queue}")
    String omsAttachmentQueue;
    
    @Autowired
    PreMfResponseRepository preMfResponseRepository;
	
    @Autowired
    Mf3DMapsTaskService mf3DMapsTaskService;
    
    @Autowired
	NPLTaskService nplTaskService;
    
	/**
	 * This method is used to get the task count
	 *
	 * @param groupName
	 * @return
	 * @author vivek
	 * @param userName
	 * @param type
	 */
	public List<AssignedGroupBean> getTaskCount(String groupName, String user, String type) {
		List<AssignedGroupBean> assignedGroupBeans = new ArrayList<>();

		String name = groupName;
		String userName = user;
		if (type != null && type.equalsIgnoreCase("user")) {
			userName = userInfoUtils.getUserInformation().getUserId();
		}
		if (groupName == null || groupName.isEmpty())
			name = userName;

		List<Task> tasks = taskRepository.findAll(TaskSpecification.getTaskFilterForMF(groupName, getStatus(), userName,
				null, null, null, null, null, null, null, null, null,null,false));
		getTaskCountDetails(tasks, assignedGroupBeans, name);

		return assignedGroupBeans;
	}

	/**
	 * This method is used to get the task details
	 *
	 * @param groupName
	 * @return
	 * @author vivek
	 */
	public List<AssignedGroupingBean> getTaskDetails(String groupName, String userName, String groupby,
			List<String> status, Integer serviceId,String productName) throws TclCommonException {
		List<AssignedGroupingBean> assignedGroupingBeans = new ArrayList<>();

		Map<String, List<Task>> groupBasedAssignmnetTask = null;
		List<Task> tasks = taskRepository.findAll(TaskSpecification.getTaskFilterForMF(groupName, status, userName,
				serviceId, null, null, null, null, null, null, null, null,productName,true));
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
				TaskBean taskBean = new TaskBean();
				setTaskDetails(taskBean, task);
				taskGroup.getTaskBeans().add(taskBean);
				taskGroupList.add(taskGroup);

			});
			taskGroup.getTaskBeans()
					.sort(Comparator.comparing(TaskBean::getPriority).thenComparing(TaskBean::getDuedate).reversed());
			assignedGroupingBean.getTaskGroup().add(taskGroup);
		});
		assignedGroupingBeans.add(assignedGroupingBean);

		return assignedGroupingBeans;
	}
	
	public PagedResult<AssignedGroupingBean> getTaskDetailsWithPagination(String groupName, String userName, String groupby,
			List<String> status, Integer serviceId,Integer page,Integer size,String productName,String searchText, 
			String createdTimeTo, String createdTimeFrom, List<String> city, String serviceCode, List<String> taskKeys, List<String> serviceType, List<String> orderType, String wfName) throws TclCommonException {
		List<AssignedGroupingBean> assignedGroupingBeans = new ArrayList<>();

		Map<String, List<Task>> groupBasedAssignmnetTask = null;
		Page<Task> tasks = taskRepository.findAll(TaskSpecification.getTaskFilterForMF(groupName, status, userName,
				serviceId, orderType, serviceType, taskKeys, serviceCode, city, searchText, createdTimeFrom, createdTimeTo,productName,!(wfName != null && wfName.equalsIgnoreCase("mf"))), PageRequest.of(page - 1, size));
		if (groupby != null && groupby.equalsIgnoreCase(GroupByConstants.GROUPBYSTATUS)) {

			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstStatus().getCode()));
		} else {
			if (wfName != null && wfName.equalsIgnoreCase("mf")) {
				groupBasedAssignmnetTask = tasks.stream().filter(t -> t.getMstTaskDef().getName().startsWith("Manual"))
						.collect(Collectors.groupingBy(t -> t.getMstTaskDef().getName()));
			} else {
				groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstTaskDef().getName()));
			}
		}
		AssignedGroupingBean assignedGroupingBean = new AssignedGroupingBean();
		assignedGroupingBean.setGroupName(groupName);
		groupBasedAssignmnetTask.forEach((taskName, taskList) -> {
			List<TaskGroupBean> taskGroupList = new ArrayList<>();
			TaskGroupBean taskGroup = new TaskGroupBean();
			taskGroup.setName(taskName);
			taskList.forEach(task -> {
				TaskBean taskBean = new TaskBean();
				setTaskDetails(taskBean, task);
				taskGroup.getTaskBeans().add(taskBean);
				taskGroupList.add(taskGroup);

			});
			taskGroup.getTaskBeans()
					.sort(Comparator.comparing(TaskBean::getPriority).thenComparing(TaskBean::getDuedate).reversed());
			assignedGroupingBean.getTaskGroup().add(taskGroup);
		});
		assignedGroupingBeans.add(assignedGroupingBean);

		return new PagedResult(assignedGroupingBeans, tasks.getTotalElements(), tasks.getTotalPages());
	}

	private void setTaskDetails(TaskBean taskBean, Task task) {
		if (task != null && taskBean != null) {
	          //TODO:: check quote 
				
				taskBean.setTaskId(taskBean.getTaskId());
				taskBean.setTaskId(task.getId());
				taskBean.setCatagory(task.getCatagory());
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
				if (task.getTaskAssignments() != null && !task.getTaskAssignments().isEmpty()) {
					setTaskAssignment(task.getTaskAssignments(), taskBean);
				}
				taskBean.setTaskDelayed(isTaskDelayed(task));
				
				if (task.getSiteDetail() != null) {
					SiteDetail siteDetail = task.getSiteDetail();
					if (siteDetail.getSiteDetail() != null) {
						taskBean.setQuoteCode(siteDetail.getQuoteCode());
						taskBean.setQuoteId(siteDetail.getQuoteId());
						try {
							taskBean.setSiteDetail(Utils.convertJsonToObject(siteDetail.getSiteDetail(), List.class));
						} catch (TclCommonException e) {
							LOGGER.warn("Error on converting the string to object {} ", e.getMessage());
						}
						taskBean.setAccountName(siteDetail.getAccountName());
						taskBean.setQuoteCreatedBy(siteDetail.getQuoteCreatedBy());
						taskBean.setQuoteType(siteDetail.getQuoteType());
						taskBean.setContractTerm(siteDetail.getContractTerm());
						taskBean.setQuoteCreatedUserType(siteDetail.getQuoteCreatedUserType());	
					}
				}
				
				if(task.getMfDetail() != null) {
					MfDetail mfDetail = task.getMfDetail();
					if(!StringUtils.isEmpty(mfDetail.getMfDetails())) {
						taskBean.setSiteId(mfDetail.getSiteId());
						taskBean.setSiteCode(mfDetail.getSiteCode());
						taskBean.setQuoteId(mfDetail.getQuoteId());
						taskBean.setQuoteCode(mfDetail.getQuoteCode());
						taskBean.setFeasibilityId(task.getFeasibilityId());
						MfDetailsBean mfDetailsBean = new MfDetailsBean();
						try {
							mfDetailsBean.setMfDetails(Utils.convertJsonToObject(mfDetail.getMfDetails(), MfDetailAttributes.class));
						} catch (TclCommonException e) {
							LOGGER.warn("Error on converting the mfDetail json string to object {} ", e.getMessage());
						}
						mfDetailsBean.setQuoteLeId(mfDetail.getQuoteLeId());
						mfDetailsBean.setSiteId(mfDetail.getSiteId());
						mfDetailsBean.setSiteCode(mfDetail.getSiteCode());

						if(task.getMfDetail().getIsPreMfTask()!=null) {
							if(task.getMfDetail().getIsPreMfTask().equalsIgnoreCase("1")) {
								LOGGER.info("entered into 3d block task");
								mfDetailsBean.setIs3DMaps(true);
								mfDetailsBean.setMf3DMapSiteType(task.getMfDetail().getSiteType());
							}
						}

						taskBean.setQuoteCreatedUserType(mfDetail.getQuoteCreatedUserType());	

						taskBean.setMfDetail(mfDetailsBean);
					}
						
					MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
					if(mfTaskDetail!=null) {
						MfTaskDetailBean taskDetailsBean = new MfTaskDetailBean();
						taskDetailsBean.setSubject(mfTaskDetail.getSubject());
						taskBean.setMfTaskDetailBean(taskDetailsBean);
					}
				}

				//PIPF-427
			//setReqCommentsForCwb(taskBean, task);

		}
		}

	/*
	 * private void setReqCommentsForCwb(TaskBean taskBean, Task task) {
	 * LOGGER.info("Inside method set req comment for CWB");
	 * taskBean.setReqComments(""); List<SiteDetail> siteDetails =
	 * siteDetailRepository.findByQuoteId(task.getQuoteId());
	 * LOGGER.info("Task list size is---> {} ", siteDetails.size());
	 * if(Objects.nonNull(siteDetails) && !siteDetails.isEmpty()){
	 * LOGGER.info("Setting req comments"); siteDetails.forEach(detail->{
	 * if(Objects.nonNull(detail) && Objects.nonNull(detail.getReqComments()) &&
	 * !detail.getReqComments().isEmpty()){
	 * taskBean.setReqComments(detail.getReqComments()); } }); }
	 * taskBean.setReqComments(!"".equalsIgnoreCase(taskBean.getReqComments())?
	 * taskBean.getReqComments() : "NA");
	 * LOGGER.info("Req comments for quote ----> {}  is----> {}",
	 * taskBean.getQuoteCode(), taskBean.getReqComments() ) ; }
	 */

	public List<AssignedGroupingBean> getTaskDetailsBasedOnfilter(TaskRequest request) {
		List<AssignedGroupingBean> assignedGroupingBeans = new ArrayList<>();

		Map<String, List<Task>> groupBasedAssignmnetTask = null;
		List<Task> tasks = taskRepository.findAll(TaskSpecification.getTaskFilterForMF(request.getGroupName(),
				request.getStatus(), request.getUserName(), request.getServiceId(), request.getOrderType(),
				request.getServiceType(), request.getTaskName(), request.getServiceCode(), request.getCity(),
				request.getSearchText(), request.getCreatedTimeFrom(), request.getCreatedTimeTo(),request.getProductName(),false));

		if (request.getGroupBy() != null && request.getGroupBy().equalsIgnoreCase(GroupByConstants.GROUPBYSTATUS)) {

			groupBasedAssignmnetTask = tasks.stream().collect(Collectors.groupingBy(t -> t.getMstStatus().getCode()));
		} else {
			if (request.getWfName() != null && request.getWfName().equalsIgnoreCase("mf")) {
				groupBasedAssignmnetTask = tasks.stream().filter(t -> t.getMstTaskDef().getName().startsWith("Manual"))
						.collect(Collectors.groupingBy(t -> t.getMstTaskDef().getName()));
			} else {
				groupBasedAssignmnetTask = tasks.stream()
						.collect(Collectors.groupingBy(t -> t.getMstTaskDef().getName()));
			}
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
				taskGroup.getTaskBeans().add(taskBean);
				taskGroupList.add(taskGroup);

			});

			taskGroup.getTaskBeans().sort(Comparator.comparing(TaskBean::getPriority));
			assignedGroupingBean.getTaskGroup().add(taskGroup);
		});
		assignedGroupingBeans.add(assignedGroupingBean);

		return assignedGroupingBeans;
	}

	/**
	 * @param serviceId
	 * @return
	 */
	public TaskResponse delayTaskDetails(Integer serviceId) {
		TaskResponse taskResponse = new TaskResponse();

		List<Task> tasks = taskRepository.findByServiceId(serviceId);

		if (!tasks.isEmpty()) {
			tasks.forEach(task -> {
				TaskPlan plan = task.getTaskPlans().stream().findFirst().orElse(null);
				if (plan != null) {
					if (plan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
						if (plan.getMstTaskDef().getIsCustomerTask() != null

								&& plan.getActualEndTime() != null && plan.getActualStartTime() != null
								&& plan.getActualEndTime().after(plan.getActualStartTime())) {
							int delay = tatService.calculateDelay(task.getMstTaskDef().getOwnerGroup(),
									plan.getActualStartTime(), plan.getActualEndTime());
							LOGGER.info("task {} delay {} Tat {}", plan.getMstTaskDef().getKey(), delay,
									task.getMstTaskDef().getTat());
							if ((delay - task.getMstTaskDef().getTat()) > 480) {
								TaskBean bean = constructTaskDetails(task, plan);
								bean.setDelayDays(delay / 480);
								taskResponse.getDelayTasks().add(bean);
							}

						}
					}

					else {
						Timestamp timestamp = new Timestamp(System.currentTimeMillis());
						if (plan.getActualStartTime() != null && plan.getActualStartTime().before(timestamp)) {
							int delay = tatService.calculateDelay(task.getMstTaskDef().getOwnerGroup(),
									plan.getActualStartTime(), timestamp);
							LOGGER.info("task {} delay {} Tat {}", plan.getMstTaskDef().getKey(), delay,
									task.getMstTaskDef().getTat());
							if ((delay - task.getMstTaskDef().getTat()) > 480) {
								TaskBean bean = constructTaskDetails(task, plan);
								bean.setDelayDays(delay / 480);
								taskResponse.getDelayTasks().add(bean);
							}

						}
					}
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
		taskBean.setIsCustomerTask(
				task.getMstTaskDef().getIsCustomerTask() != null ? task.getMstTaskDef().getIsCustomerTask() : "N");
		taskBean.setName(task.getMstTaskDef().getName());
		taskBean.setActualStartTime(plan.getActualStartTime());
		taskBean.setPlannedStartTime(plan.getPlannedStartTime());
		taskBean.setPlannedEndTime(plan.getPlannedEndTime());
		taskBean.setEstimatedEndTime(plan.getEstimatedEndTime());
		taskBean.setEstimatedStartTime(plan.getEstimatedStartTime());

		return taskBean;

	}

	/**
	 * s This method is used to get the latest activity of group
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
			Integer taskId, String user, String type) {
		List<ProcessTaskLogBean> taskLogBeans = new ArrayList<>();
		String userName = user;
		if (type != null && type.equalsIgnoreCase("user")) {
			userName = userInfoUtils.getUserInformation().getUserId();
		}
		Pageable sortedByCreatedTimeDesc = 
				  PageRequest.of(0, 10, Sort.by("createdTime").descending());
		Page<ProcessTaskLog> processTaskLogs = processTaskLogRepository
				.findAll(TaskLogSpecification.getTaskLogFilter(taskId, groupName, orderCode, serviceId, userName),sortedByCreatedTimeDesc);
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
	// to do need to implement once get clarity
	@Transactional(readOnly = false)
	public AssigneeResponse updateAssignee(AssigneeRequest request) throws TclCommonException {
		AssigneeResponse assigneeResponse = new AssigneeResponse();
		Optional<Task> optionalTask = taskRepository.findById(request.getTaskId());
		if (optionalTask.isPresent()) {
			Task task = optionalTask.get();
			task.setClaimTime(new Timestamp(new Date().getTime()));
			updateAssignmentDetails(task, request);
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			constructAssigneeResponse(task, request, assigneeResponse);
			taskRepository.save(task);

		}

		return assigneeResponse;
	}

	/**
	 * used to assign or re-assign a task to individual member of a team
	 * 
	 * @param request
	 * @return
	 * @author vivek
	 * @throws TclCommonException
	 */
	// to do need to implement once get clarity
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public AssigneeResponse updateMfAssignee(MfTaskRequestBean request) throws TclCommonException {
		if (request.getTaskId() == null || StringUtils.isEmpty(request.getAssignedFrom())
				|| StringUtils.isEmpty(request.getAssignedTo()))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		AssigneeResponse assigneeResponse = new AssigneeResponse();
		Optional<Task> optionalTask = taskRepository.findById(request.getTaskId());
		if (optionalTask.isPresent()) {
			Task task = optionalTask.get();
			task.setClaimTime(new Timestamp(new Date().getTime()));
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
			AssigneeRequest assigneeRequest = new AssigneeRequest();
			assigneeRequest.setAssigneeNameFrom(request.getAssignedFrom());
			assigneeRequest.setAssigneeNameTo(request.getAssignedTo());
			assigneeRequest.setTaskId(request.getTaskId());
			assigneeRequest.setGroupFrom(request.getGroupFrom());
			updateMfAssignmentDetails(task, assigneeRequest);
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
			updateMfTaskDetail(request);
			/*
			 * MfDetail mfDetail = task.getMfDetail();
			 * mfDetail.setRegion(request.getRegion()); mfDetailRepository.save(mfDetail);
			 */
			constructMFAssigneeResponse(task, assigneeRequest, assigneeResponse);
			taskRepository.save(task);

		}

		return assigneeResponse;
	}

	protected void updateMfTaskDetail(MfTaskRequestBean request) {
		MfTaskDetail taskDetail = mfTaskDetailRepository.findByTaskId(request.getTaskId());
		if (taskDetail != null) {
			taskDetail.setAssignedTo(taskDetail.getAssignedGroup().concat("-").concat(request.getAssignedTo()));
			if (!StringUtils.isEmpty(request.getRequestorComments()))
				taskDetail.setRequestorComments(request.getRequestorComments());
			MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(taskDetail);
			mfTaskDetailAudit.setCreatedBy(Utils.getSource());
			mfTaskDetailAuditRepository.save(mfTaskDetailAudit);
			mfTaskDetailRepository.save(taskDetail);
		}
	}

	private void updateAssignmentDetails(Task task, AssigneeRequest request) {
		TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst().orElse(null);
		//String status = isClaimed(request) ? "Claimed" : "Assigned";
		if (taskAssignment != null) {

			if (request.getType() != null && request.getType().equalsIgnoreCase("CLAIM")) {
				if (task.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)) {
					LOGGER.warn("Cannot claim for task Id {}",task.getId());
					throw new TclCommonRuntimeException(ExceptionConstants.CANNOT_CLAIM, ResponseResource.R_CODE_ERROR);

				}
			}
			taskAssignment.setUserName(request.getAssigneeNameTo());
			LOGGER.info("saving the assignment details the taskId {}",task.getId());
			taskAssignmentRepository.save(taskAssignment);
			LOGGER.info("saving the task logs details the taskId {}",task.getId());
			processTaskLogRepository
					.save(createProcessTaskLog(task, request, isClaimed(request) ? "Claimed" : "Assigned"));

			try {
				String member = request.getAssigneeNameTo();
				if (task.getSiteDetail() != null) {
					LOGGER.info("sending notification the taskId {}",task.getId());
					sendAssignmentMailToCommercialTeam(task, request.getAssigneeNameTo());
				} else if (request.getAssigneeNameFrom() != null && request.getAssigneeNameTo() != null
						&& !request.getAssigneeNameFrom().equalsIgnoreCase(request.getAssigneeNameTo())) {
					LOGGER.info("sending notification the taskId {}",task.getId());
					notificationService.notifyTaskAssignment(request.getAssigneeNameTo(), member, task.getServiceCode(),
							task.getMstTaskDef().getName(), Utils.converTimeToString(task.getDuedate()), "");

				}
			} catch (Exception e) {
				LOGGER.error("error in sending notification", e);
			}
		}
	}

	private void updateMfAssignmentDetails(Task task, AssigneeRequest request) {
		TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst().orElse(null);
		String status = isClaimed(request) ? "Claimed" : "Assigned";
		if (taskAssignment != null) {

			if (isClaimed(request)) {
				if (task.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)) {
					if (task.getMfDetail() == null)
						throw new TclCommonRuntimeException(ExceptionConstants.CANNOT_CLAIM,
								ResponseResource.R_CODE_ERROR);
					else
						status = "Re-claimed";
				}
			}
			taskAssignment.setUserName(request.getAssigneeNameTo());
			taskAssignmentRepository.save(taskAssignment);
			processTaskLogRepository.save(createProcessTaskLog(task, request, status));

			try {
				String member = request.getAssigneeNameTo();
				if (task.getSiteDetail() != null) {
					sendAssignmentMailToCommercialTeam(task, request.getAssigneeNameTo());
				} else if (task.getMfDetail() != null) {
					sendNotificationToFeasibilityTeam(task, request.getAssigneeNameTo());
				} else if (request.getAssigneeNameFrom() != null && request.getAssigneeNameTo() != null
						&& !request.getAssigneeNameFrom().equalsIgnoreCase(request.getAssigneeNameTo())) {

					notificationService.notifyTaskAssignment(request.getAssigneeNameTo(), member, task.getServiceCode(),
							task.getMstTaskDef().getName(), Utils.converTimeToString(task.getDuedate()), "");

				}
			} catch (Exception e) {
				LOGGER.error("error in sending notification{}", e);
			}
		}
	}

	public void sendAssignmentMailToCommercialTeam(Task task, String assigne) {
		List<String> customerMail = new ArrayList<>();
		customerMail.add(assigne);
		if (task.getSiteDetail() != null) {
			notificationService.notifyTaskAssignmentCommercialToAssigee(customerMail, task.getQuoteCode(),
					task.getSiteDetail().getOpportunityId(), task.getSiteDetail().getAccountName(),
					task.getSiteDetail().getQuoteCreatedBy());
		}
	}

	public void sendNotificationToFeasibilityTeam(Task task, String assignee) {
		List<String> customerMail = new ArrayList<>();
		customerMail.add(assignee);
		if (task.getMfDetail() != null) {
//		notificationService.notifyMfTaskAssignmentToAssigee(customerMail, task);
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
		} else {
			processTaskLog.setActionFrom(assigneeRequest.getAssigneeNameFrom());

		}
		processTaskLog.setActionTo(assigneeRequest.getAssigneeNameTo());
		if (StringUtils.isEmpty(assigneeRequest.getGroupFrom()))
			processTaskLog.setGroupFrom(task.getMstTaskDef().getAssignedGroup());
		else
			processTaskLog.setGroupFrom(assigneeRequest.getGroupFrom());
		processTaskLog.setGroupTo(assigneeRequest.getAssigneeNameTo());
		processTaskLog.setDescrption(assigneeRequest.getDescription());
		processTaskLog.setOrderCode(task.getOrderCode());
		processTaskLog.setScOrderId(task.getScOrderId());
		processTaskLog.setServiceId(task.getServiceId());
		processTaskLog.setQuoteId(task.getQuoteId());
		processTaskLog.setQuoteCode(task.getQuoteCode());
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
		}
	}

	/**
	 * @param task
	 * @return
	 */
	private TaskBean constructTaskBean(Task task) {
		TaskBean taskBean = new TaskBean();
		if (task != null) {
		}
		return taskBean;
	}

	/**
	 * This method is used to map the task details bean
	 *
	 * @param taskBean
	 * @param task
	 * @author vivek
	 */

	private boolean isTaskDelayed(Task task) {

		try {

			if (task.getTaskPlans() != null && !task.getTaskPlans().isEmpty()) {

				TaskPlan taskPlan = task.getTaskPlans().stream().findFirst().orElse(null);

				if (taskPlan != null && taskPlan.getPlannedEndTime() != null) {

					if (new Timestamp(new Date().getTime()).toLocalDateTime()
							.isAfter(taskPlan.getPlannedEndTime().toLocalDateTime())) {
						return true;

					}

				}
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
			assignedGroupBean.setAssignedCount(requirementCountMap.get(TaskStatusConstants.ASSIGNED));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.PENDING)) {
			assignedGroupBean.setPendingCount(requirementCountMap.get(TaskStatusConstants.PENDING));
		}

		if (requirementCountMap.containsKey(TaskStatusConstants.REOPEN)) {
			assignedGroupBean.setReopenCount(requirementCountMap.get(TaskStatusConstants.REOPEN));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.INPROGRESS)) {
			assignedGroupBean.setInprogressCount(requirementCountMap.get(TaskStatusConstants.INPROGRESS));
		}
		if (requirementCountMap.containsKey(TaskStatusConstants.RETURNED)) {
			assignedGroupBean.setReturnedCount(requirementCountMap.get(TaskStatusConstants.RETURNED));
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
		status.add(TaskStatusConstants.RETURNED);
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
	public TaskBean getTaskBasedOnTaskId(Integer taskId) throws TclCommonException {
		TaskBean taskbean = new TaskBean();

		if (taskId != null) {
			Optional<Task> optionalTask = taskRepository.findById(taskId);

			if (optionalTask.isPresent()) {
				Task task = optionalTask.get();
				setTaskData(task, taskbean);
				Map<String, Object> commonData = itaskDataService.getTaskData(task);
				taskbean.setNonEditable(checkIsNonEditable(task));
				taskbean.setCommonData(commonData);

			}

		}

		return taskbean;
	}

	private boolean checkIsNonEditable(Task task) {

		try {

			if (task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment")) {

				Task serviceDesignTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(
						task.getServiceId(), "enrich-service-design");

				TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(task.getServiceId(),
						"enrich-service-design");

				if (serviceDesignTask != null) {
					if ((serviceDesignTask.getMstStatus() != null)
							&& serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)
							|| serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.OPEN)
							|| serviceDesignTask.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
						return true;
					}
				} else if (taskPlan != null && taskPlan.getPlannedStartTime() != null
						&& new Timestamp(System.currentTimeMillis()).toLocalDateTime()
								.isAfter(taskPlan.getPlannedStartTime().toLocalDateTime())) {
					return true;
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

			if (map != null)
				runtimeService.trigger(execution.getId(), map);
			else
				runtimeService.trigger(execution.getId());

			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String manuallyCompleteTask(Integer taskId, Map<String, Object> map) {
		try {
			Task task = getTaskById(taskId);
			// Updating discount approval level in site detail table
			if (task.getSiteDetail() != null && map.containsKey("discountApprovalLevel")) {
				SiteDetail siteDetail = task.getSiteDetail();
				siteDetail.setDiscountApprovalLevel((Integer) map.get("discountApprovalLevel"));
				siteDetailRepository.saveAndFlush(siteDetail);
			}
			taskDataEntry(task, map, map);
			processTaskLogDetails(task, TaskLogConstants.CLOSED, "", null);
			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}

	/**
	 * used to delete an mf task manually
	 * 
	 * @param taskId
	 * @param map
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String manuallyDeleteTask(Integer taskId) {
		try {
			Task task = getTaskById(taskId);
			// updateTaskStatusToDeleted(task);
			processMfTaskLogDetails(task, TaskLogConstants.DELETED, "", null, "");
			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}

	public void processMfResponse(Task task, String mfStatus) {

		Integer siteId = task.getSiteId();
		Integer quoteId = task.getQuoteId();
		MfDetail mfDetail = task.getMfDetail();
		Map<String, Object> map = new HashMap<>();
		Map<Integer, String> siteStatus = (HashMap<Integer, String>) cmmnRuntimeService
				.getVariables(task.getWfProcessInstId()).get("siteStatus");
		String siteMfStatus = siteStatus.get(siteId);
		if (StringUtils.isEmpty(siteMfStatus)
				|| ManualFeasibilityWFConstants.NOT_FEASIBLE.equalsIgnoreCase(siteMfStatus)
				|| ManualFeasibilityWFConstants.RETURN.equalsIgnoreCase(siteMfStatus))
			siteStatus.put(siteId, mfStatus);

		cmmnRuntimeService.setVariable(task.getWfProcessInstId(), ManualFeasibilityWFConstants.SITESTATUS, siteStatus);

		LOGGER.info("ProcessMFResponse invoked for {} Id={} mfDetailId={}", task.getMstTaskDef().getKey(), task.getId(),
				mfDetail.getId());
		if (mfDetail != null) {
			LOGGER.info("MF detail ID : {} ", mfDetail.getId());
			map.put("quoteId", quoteId);
			if(checkIfTaskClosed(task.getWfProcessInstId(), "asp")
					&& checkIfTaskClosed(task.getWfProcessInstId(), "afm")) {
				selectRelevantManualFeasibleResponse(siteId, mfStatus, mfDetail);
			}
			try {
				List<Task> mfTaskList = taskRepository.findByQuoteId(quoteId).stream()
						.filter(mfTask -> mfTask.getMfDetail() != null).collect(Collectors.toList());
				//Map<Integer, List<Task>> tasksForSite = mfTaskList.stream().collect(Collectors.groupingBy(mfTask -> mfTask.getSiteId()));
				Set<String> caseInstanceIds = new HashSet<String>();
				//Map<Integer,String> caseIdsForSite = new HashMap<Integer,String>();
				mfTaskList.stream().forEach(mfTask -> {
					if (mfTask.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)
							|| mfTask.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP)) {
						caseInstanceIds.add(mfTask.getWfProcessInstId());
					}
				});
				LOGGER.info("Total tasks for a quote : {}", mfTaskList.size());
				List<Task> completedTasks = null;
				Map<Integer,String> allSiteStatusesOfQuote = new HashedMap<Integer,String>();
				
				if (!ManualFeasibilityWFConstants.RETURN.equalsIgnoreCase(mfStatus)) {
					LOGGER.info("Checking if tasks closed for all sites of the quote. ");
					completedTasks = mfTaskList.stream().filter(taskDetail -> taskDetail.getMstStatus().getCode()
							.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
							|| taskDetail.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.DELETED))
							.collect(Collectors.toList());
					if (mfTaskList.size() == completedTasks.size()) {
						LOGGER.info("All tasks closed for all sites of the quote {} ", quoteId);
						/*Set<String> caseInstanceIds = mfTaskList.stream().map(mfTask -> mfTask.getWfProcessInstId())
								.collect(Collectors.toSet());
						*/LOGGER.info("Total case instances for the quote {}  : {} ", quoteId, caseInstanceIds.size());
						/*caseInstanceIds.stream().forEach(
								id -> allSiteStatusesOfQuote.putAll((HashMap<Integer, String>) cmmnRuntimeService
										.getVariable(id, ManualFeasibilityWFConstants.SITESTATUS))); */
						if(mfTaskList.stream().findFirst().get().getQuoteCode()!=null && mfTaskList.stream().findFirst().get().getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
							mfTaskList.stream().forEach(mfTask->{
								List<MfResponseDetail> mfResponseDetail = mfResponseDetailRepository.findBySiteId(mfTask.getSiteId());
								if(mfResponseDetail!=null && !mfResponseDetail.isEmpty()) {
									allSiteStatusesOfQuote.put(mfTask.getSiteId(), mfResponseDetail.get(0).getFeasibilityStatus());
								}
							});
						}else {
						caseInstanceIds.forEach(id ->  {
							HashMap<Integer, String> individualStatus= (HashMap<Integer, String>) cmmnRuntimeService.getVariable(id,
									ManualFeasibilityWFConstants.SITESTATUS);
							individualStatus.entrySet().stream().forEach(indStatusEntry -> {
								if (allSiteStatusesOfQuote.containsKey(indStatusEntry.getKey())) {
									String status = allSiteStatusesOfQuote.get(indStatusEntry.getKey());
									LOGGER.info("Status in allSiteStatusMap for site Id {} is {} ",
											indStatusEntry.getKey(), indStatusEntry.getValue());
									if (!status.equalsIgnoreCase(ManualFeasibilityWFConstants.FEASIBLE)
											&& !status.equalsIgnoreCase(indStatusEntry.getValue())) {
										LOGGER.info("Updating allSiteStatusMap for site {} from {} to {}",
												indStatusEntry.getKey(), status, indStatusEntry.getValue());
										allSiteStatusesOfQuote.put(indStatusEntry.getKey(), indStatusEntry.getValue());
									}
								} else {
									LOGGER.info("Status not present in the map for site {} ", indStatusEntry.getKey());
									allSiteStatusesOfQuote.put(indStatusEntry.getKey(), indStatusEntry.getValue());
								}
							});
						});
						}
						LOGGER.info("Total Number of site status maps : {}",allSiteStatusesOfQuote.size());
						saveMfResponseDetailsInOms(quoteId, allSiteStatusesOfQuote, task.getId(),task.getServiceType(),mfDetail);
					}
				} else {
					/*HashMap<Integer, String> returnedSite = new HashMap<Integer, String>();
					List<HashMap<Integer, String>> listOfMaps = new ArrayList<HashMap<Integer, String>>();
					returnedSite.put(siteId, mfStatus);
					listOfMaps.add(returnedSite);*/
					allSiteStatusesOfQuote.put(siteId, mfStatus);
					saveMfResponseDetailsInOms(quoteId, allSiteStatusesOfQuote, task.getId(),task.getServiceType(),mfDetail);
				}
			} catch (Exception e) {
				LOGGER.error("Error in fetching response details {}", e.getMessage());
			}

			try {
				/*
				 * if (commercialQuoteDetailBean != null) {
				 * notificationService.notifyCommercialFlowComplete(commercialQuoteDetailBean.
				 * getEmail(), (String) processMap.get("quoteCode"),
				 * commercialQuoteDetailBean.getOptyId(),
				 * commercialQuoteDetailBean.getAccountName(), history,
				 * commercialQuoteDetailBean.getEmail()); }
				 */
				// Update task closed status to MfDetail
				mfDetail.setStatus(TaskStatusConstants.CLOSED_STATUS);
				mfDetail.setIsActive(CommonConstants.INACTIVE);
				mfDetailRepository.save(mfDetail);

			} catch (IllegalArgumentException e) {
				LOGGER.error("MF response process failed ", e);
			}
		}
	}

	/**
	 * Method to save MFResponseDetails in Oms
	 * 
	 * @param taskId
	 * @param mfDetail 
	 *
	 * @param siteDetails
	 * @throws TclCommonException 
	 */
	public void saveMfResponseDetailsInOms(Integer quoteId, Map<Integer, String> sitestatusList, Integer taskId,
			String productName, MfDetail mfDetail) throws TclCommonException {
		
		MfDetailAttributes mfDetailAttrs=Utils.convertJsonToObject(mfDetail.getMfDetails(), MfDetailAttributes.class);
		boolean dualCBCheck = true ;

		if((mfDetailAttrs.getQuoteType()!=null && mfDetailAttrs.getQuoteType().equals("MACD") )
				&&( mfDetailAttrs.getQuoteCategory()!=null  && mfDetailAttrs.getQuoteCategory().equals("CHANGE_BANDWIDTH"))
				&& mfDetailAttrs.getChangeBandwidthFlag() !=null) {
			
			String cbFlag = mfDetailAttrs.getChangeBandwidthFlag();
			if(cbFlag.equalsIgnoreCase("Secondary") || cbFlag.equalsIgnoreCase("Primary")) {
				dualCBCheck = false;
			}
			
		}
		if(mfDetail.getQuoteCode().startsWith(IzosdwanCommonConstants.IZOSDWAN)) {
			dualCBCheck = false;
		}
		List<MfResponseDetailBean> allResponsesOfAllSites = new ArrayList<MfResponseDetailBean>();

		List<MfResponseDetailBean> mfResponseDetailBeans = getMfResponseDetailsOfSiteIds(quoteId, sitestatusList,
				productName,dualCBCheck);
		allResponsesOfAllSites.addAll(mfResponseDetailBeans);

		LOGGER.info("Sending {} response to oms.", allResponsesOfAllSites.size());
		saveMfResponseDetailMQ(allResponsesOfAllSites);
	}

	/**
	 * Returns mf response detais of all siteid's
	 *
	 * @param quoteId
	 * @param siteDetails
	 * @param ignoreResponseRestriction 
	 * @return {@link List}
	 */
	public List<MfResponseDetailBean> getMfResponseDetailsOfSiteIds(Integer quoteId,
			Map<Integer, String> siteDetails,String productName, boolean dualCBCheck) {
		List<MfResponseDetailBean> mfResponseDetailBeans = new ArrayList<>();
		siteDetails.entrySet().forEach(siteDetail -> {
			LOGGER.info("Feasibility status of the site {} is {} ", siteDetail.getKey(),siteDetail.getValue());
			if (siteDetail.getValue().equalsIgnoreCase(RETURNED)) {
				LOGGER.info("creating sample bean for site id {} having return status", siteDetail.getKey());
				MfResponseDetailBean mfResponseDetailBean = new MfResponseDetailBean();
				mfResponseDetailBean.setSiteId(siteDetail.getKey());
				mfResponseDetailBean.setQuoteId(quoteId);
				mfResponseDetailBean.setFeasibilityStatus(RETURNED);
				mfResponseDetailBean.setProduct(productName);
				mfResponseDetailBeans.add(mfResponseDetailBean);
			} else if(siteDetail.getValue().equalsIgnoreCase(ManualFeasibilityWFConstants.NOT_FEASIBLE)){
				List<MfResponseDetail> mfResponseDetails = mfResponseDetailRepository.findBySiteId(siteDetail.getKey());
				LOGGER.info("Number of responses present for not feasible site {} ",siteDetail.getKey());
				if(!mfResponseDetails.isEmpty()) {
					mfResponseDetailBeans.addAll(mfResponseDetails.stream()
						.map(mfResponseDetail -> constructMfResponseDetailBeanFromEntity(mfResponseDetail,false,ManualFeasibilityWFConstants.NOT_FEASIBLE)).collect(Collectors.toList()));
				}else {
					LOGGER.info("creating sample bean for site id {} having not-feasible status", siteDetail.getKey());
					MfResponseDetailBean mfResponseDetailBean = new MfResponseDetailBean();
					mfResponseDetailBean.setSiteId(siteDetail.getKey());
					mfResponseDetailBean.setQuoteId(quoteId);
					mfResponseDetailBean.setFeasibilityStatus(ManualFeasibilityWFConstants.NOT_FEASIBLE);
					mfResponseDetailBean.setProduct(productName);
					mfResponseDetailBean.setOverallSiteStatus(ManualFeasibilityWFConstants.NOT_FEASIBLE);
					mfResponseDetailBean.setHitPrice(false);
					mfResponseDetailBeans.add(mfResponseDetailBean);
				}
			
			} else  if(siteDetail.getValue().equalsIgnoreCase(ManualFeasibilityWFConstants.FEASIBLE)){
			
				LOGGER.info("status of site id {} is {}", siteDetail.getKey(), siteDetail.getValue());

				List<MfDetail> mfDetailsList = mfDetailRepository.findByQuoteIdAndSiteId(quoteId, siteDetail.getKey());

				List<MfDetail> dualSiteDetailsList = mfDetailsList.stream().filter(x -> x.getSiteType() != null)
						.filter(y -> y.getSiteType().startsWith("Dual")).collect(Collectors.toList());

				List<MfResponseDetail> mfResponseDetails = mfResponseDetailRepository.findBySiteId(siteDetail.getKey());

				boolean hitprice = false;
				if (dualSiteDetailsList != null && !dualSiteDetailsList.isEmpty() && dualCBCheck) {
					// this site is a dual site. check both primary and secondary has response
					LOGGER.info("Inside dual condition.This site with siteID {} is dual site",siteDetail.getKey());
					if (mfResponseDetails != null && !mfResponseDetails.isEmpty()) {
						long secondaryFeasibleResponseCount = mfResponseDetails.stream()
								.filter(x -> x.getType() != null).filter(y -> y.getType().equals("secondary"))
								.filter(z -> z.getFeasibilityStatus() != null)
								.filter(z -> z.getFeasibilityStatus().toUpperCase().startsWith(FEASIBLE)).count();

						long primaryFeasibleResponseCount = mfResponseDetails.stream().filter(x -> x.getType() != null)
								.filter(y -> y.getType().equals("primary"))
								.filter(z -> z.getFeasibilityStatus() != null)
								.filter(z -> z.getFeasibilityStatus().toUpperCase().startsWith(FEASIBLE)).count();

						if (primaryFeasibleResponseCount > 0 && secondaryFeasibleResponseCount == 0) {
							// find the selected response and its feasibility type
							LOGGER.info("primary has one or more feasible response and No secondary Feasible Response.");
							LOGGER.info("PrimaryFeasibleResponse Count ={} , SecondaryFeasibleResponseCount = {}",
									primaryFeasibleResponseCount, secondaryFeasibleResponseCount);
							
							Optional<MfResponseDetail> selectedResponse = filterSeletedResponseBasedonType(
									mfResponseDetails, "primary");
							if (selectedResponse.isPresent() && selectedResponse.get().getFeasibilityType() != null) {
								List<MfResponseDetail> remainingPrimaryFeasibleResp = filterNonSelectedFeasibleResponseBasedOnType(
										mfResponseDetails, selectedResponse, "primary");

								// pass this to priority matrix for rank
								if (remainingPrimaryFeasibleResp != null && !remainingPrimaryFeasibleResp.isEmpty()) {
									MfResponseDetail oneMoreSelectedResponse = filterResponsesBasedOnStatusAndRankAndFeasibleMode(
											remainingPrimaryFeasibleResp);
									if(oneMoreSelectedResponse!=null) {
										LOGGER.info(
												"Second response selection in available primary responses with ID  -{}",
												oneMoreSelectedResponse.getId());
										oneMoreSelectedResponse.setType("secondary");
										if(oneMoreSelectedResponse.getCreateResponseJson()!=null) {
											changeSiteType(oneMoreSelectedResponse,"secondary");
										}
									saveIsSelectedForSelectedMfResponse(oneMoreSelectedResponse);
									hitprice = true;
									}
								} else {
									//To handle condition of only primary circuit task in workbench
									long secondaryResponses = mfResponseDetails.stream().filter(mfResponse -> Objects.nonNull(mfResponse.getType()) && mfResponse.getType().equalsIgnoreCase("secondary")).count();
									if (secondaryResponses == 0) {
										LOGGER.info("Only primary circuit available, and no secondary either feasible/nonfeasble");
										hitprice = true;
									}
								}
							}
						} // end of scenario 1
						else if (primaryFeasibleResponseCount == 0 && secondaryFeasibleResponseCount > 0) {
							LOGGER.info("primary has no feasible response and one or more secondary Feasible Response.");
							LOGGER.info("PrimaryFeasibleResponse Count ={} , SecondaryFeasibleResponseCount = {}",
									primaryFeasibleResponseCount, secondaryFeasibleResponseCount);
							
							Optional<MfResponseDetail> selectedResponseSecondary = filterSeletedResponseBasedonType(
									mfResponseDetails, "secondary");
							if (selectedResponseSecondary.isPresent()
									&& selectedResponseSecondary.get().getFeasibilityType() != null) {
								List<MfResponseDetail> remainingSecondaryFeasibleResp = filterNonSelectedFeasibleResponseBasedOnType(
										mfResponseDetails, selectedResponseSecondary, "secondary");

								// pass this to priority matrix for rank
								if (remainingSecondaryFeasibleResp != null && !remainingSecondaryFeasibleResp.isEmpty()) {
									MfResponseDetail oneMoreSelectedResponse = filterResponsesBasedOnStatusAndRankAndFeasibleMode(
											remainingSecondaryFeasibleResp);
									if (oneMoreSelectedResponse != null) {
										LOGGER.info("Second response selection in available sec responses with ID  -{}",
												oneMoreSelectedResponse.getId());
										oneMoreSelectedResponse.setType("primary");
										if(oneMoreSelectedResponse.getCreateResponseJson()!=null) {
										changeSiteType(oneMoreSelectedResponse,"primary");
										}
										saveIsSelectedForSelectedMfResponse(oneMoreSelectedResponse);
										hitprice = true;
									}
								} else {
									//To handle condition of only secondary circuit task in workbench
									long primaryResponses = mfResponseDetails.stream().filter(mfResponse -> Objects.nonNull(mfResponse.getType()) && mfResponse.getType().equalsIgnoreCase("primary")).count();
									if (primaryResponses == 0) {
										LOGGER.info("Only secondary circuit available, and no primary either feasible/nonfeasble");
										hitprice = true;
									}
								}
							}
						}else if (primaryFeasibleResponseCount == 0 && secondaryFeasibleResponseCount == 0) {
							// non happy flow....
							LOGGER.info("no responses found !! primaryFResponseCount ={}, secFResponseCount = {}",
									primaryFeasibleResponseCount,secondaryFeasibleResponseCount);
							hitprice = false;
						} else if (primaryFeasibleResponseCount > 0 && secondaryFeasibleResponseCount > 0) {
							// happy flow....
							LOGGER.info("Happy FLow!! primaryFResponseCount ={}, secFResponseCount = {}",
									primaryFeasibleResponseCount,secondaryFeasibleResponseCount);
							hitprice = true;
						}
					} 
				} else {
					// this is single site
					LOGGER.info("Single Site with siteID ={}", siteDetail.getKey());
					hitprice = true;
				}
					decidePricingHit(mfResponseDetailBeans, mfResponseDetails, hitprice);
		}
		});
		return mfResponseDetailBeans;
	}

	/**
	 * Change the siteType suffix on response swap in dual site.
	 * @param oneMoreSelectedResponse
	 * @param type
	 */
	private void changeSiteType(MfResponseDetail oneMoreSelectedResponse, String type) {
		JSONObject dataEnvelopeObj = null;
		JSONParser jsonParser = new JSONParser();
		try {
			dataEnvelopeObj = (JSONObject) jsonParser.parse(oneMoreSelectedResponse.getCreateResponseJson());
		} catch (ParseException e) {
			LOGGER.info(" error in parsing response json while changing site type");
		}
		if (dataEnvelopeObj.get("site_id") != null) {
			String siteID = null;
			if (dataEnvelopeObj.get("site_id") instanceof String) {
				siteID = (String) dataEnvelopeObj.get("site_id");
				String[] siteArr = siteID.split("_");
				if (siteArr != null) {
					siteID = siteArr[0] + "_" + type;
					dataEnvelopeObj.put("site_id", siteID);
					oneMoreSelectedResponse.setCreateResponseJson(dataEnvelopeObj.toString());
				}
			}
		}
	}
	
	
	/**
	 * method to decide pricing hit
	 * @param mfResponseDetailBeans
	 * @param mfResponseDetails
	 * @param decision
	 */
	private void decidePricingHit(List<MfResponseDetailBean> mfResponseDetailBeans,
			List<MfResponseDetail> mfResponseDetails, boolean decision) {
		mfResponseDetailBeans.addAll(mfResponseDetails.stream()
				.map(n -> constructMfResponseDetailBeanFromEntity(n, decision,null)).collect(Collectors.toList()));
	}


	private List<MfResponseDetail> filterNonSelectedFeasibleResponseBasedOnType(
			List<MfResponseDetail> mfResponseDetails, Optional<MfResponseDetail> selectedResponse, String type) {
		
		List<MfResponseDetail> responsesForSelection = new ArrayList<MfResponseDetail>();
		List<MfResponseDetail> listOfResponseWithoutProviderFilter = mfResponseDetails.stream().filter(x -> x.getType() != null)
				    .filter(y -> y.getType().equals(type))
					.filter(z -> z.getFeasibilityStatus().toUpperCase().startsWith(FEASIBLE))
					.filter( x -> x.getIsSelected()==0).collect(Collectors.toList());
		
		if (listOfResponseWithoutProviderFilter != null && !listOfResponseWithoutProviderFilter.isEmpty()) {

			List<MfResponseDetail> nullProviderList = listOfResponseWithoutProviderFilter.stream()
					.filter(z -> z.getProvider() == null || z.getProvider().isEmpty()).collect(Collectors.toList());

			List<MfResponseDetail> listOfFeasibleResponseWithUniqueProviderName = listOfResponseWithoutProviderFilter
					.stream().filter(z -> z.getProvider() != null &&  !z.getProvider().isEmpty())
					.filter(z -> (!z.getProvider().equals(selectedResponse.get().getProvider())))
					.collect(Collectors.toList());

			responsesForSelection.addAll(nullProviderList);
			responsesForSelection.addAll(listOfFeasibleResponseWithUniqueProviderName);

		}
		return responsesForSelection;

	}

	private Optional<MfResponseDetail> filterSeletedResponseBasedonType(List<MfResponseDetail> mfResponseDetails,String type) {
		return mfResponseDetails.stream()
				.filter(x -> x.getType() != null).filter(y -> y.getType().equals(type))
				.filter(z -> z.getIsSelected() == 1).findFirst();
	}

	/**
	 * Save MF Response Detail MQ
	 *
	 * @param mfResponseDetailBeans
	 */
	public void saveMfResponseDetailMQ(List<MfResponseDetailBean> mfResponseDetailBeans) {
		if (!CollectionUtils.isEmpty(mfResponseDetailBeans)) {
			try {
				String request = Utils.convertObjectToJson(mfResponseDetailBeans);
				LOGGER.info(
						"MDC Filter token value in before Queue call saving mf response details in site feasibility {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				mqUtils.send(saveMFResponseInOmsMQ, request);
			} catch (Exception e) {
				throw new TclCommonRuntimeException(ExceptionConstants.SAVE_MF_RESPONSE_IN_OMS_MQ_ERROR, e,
						ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
			}
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.SITES_NOT_FOUND,
					ResponseResource.R_CODE_BAD_REQUEST);
		}
	}

	/**
	 * Method to construct Mf response bean from entity
	 *
	 * @param mfResponseDetail
	 * @return {@link MfResponseDetailBean}
	 */
	protected MfResponseDetailBean constructMfResponseDetailBeanFromEntity(MfResponseDetail mfResponseDetail, boolean hitprice,String overallStatusOfTheSite) {
		MfResponseDetailBean mfResponseDetailBean = new MfResponseDetailBean();
		mfResponseDetailBean.setId(mfResponseDetail.getId());
		mfResponseDetailBean.setTaskId(mfResponseDetail.getTaskId());
		mfResponseDetailBean.setSiteId(mfResponseDetail.getSiteId());
		mfResponseDetailBean.setProvider(mfResponseDetail.getProvider());
		mfResponseDetailBean.setCreateResponseJson(mfResponseDetail.getCreateResponseJson());
		mfResponseDetailBean.setCreatedBy(mfResponseDetail.getCreatedBy());
		mfResponseDetailBean.setCreatedTime(mfResponseDetail.getCreatedTime());
		mfResponseDetailBean.setUpdatedBy(mfResponseDetail.getUpdatedBy());
		mfResponseDetailBean.setUpdatedTime(mfResponseDetail.getUpdatedTime());
		mfResponseDetailBean.setType(mfResponseDetail.getType());
		mfResponseDetailBean.setMfRank(mfResponseDetail.getMfRank());
		mfResponseDetailBean.setIsSelected(mfResponseDetail.getIsSelected());
		mfResponseDetailBean.setFeasibilityMode(mfResponseDetail.getFeasibilityMode());
		mfResponseDetailBean.setFeasibilityStatus(mfResponseDetail.getFeasibilityStatus());
		mfResponseDetailBean.setFeasibilityCheck(mfResponseDetail.getFeasibilityCheck());
		mfResponseDetailBean.setFeasibilityType(mfResponseDetail.getFeasibilityType());
		mfResponseDetailBean.setQuoteId(mfResponseDetail.getQuoteId());
		mfResponseDetailBean.setHitPrice(hitprice);
		mfResponseDetailBean.setProduct(mfResponseDetail.getProduct());
		mfResponseDetailBean.setOverallSiteStatus(overallStatusOfTheSite);
		return mfResponseDetailBean;
	}

	/**
	 * used to complete an mf task manually
	 * 
	 * @param taskId
	 * @param map
	 * @return
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String completeMfTask(Integer taskId, Map<String, Object> map, boolean isDependantTask, String mfStatus) {
		try {
			Task task = getTaskById(taskId);
			String group = "";
			String type = ManualFeasibilityWFConstants.PRIMARY; // assume it when not supplied
			if (map != null && !map.isEmpty()) {
				group = (String) map.get("group");
				type = (String) map.get("type");
			}
			if (mfStatus.equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN)) {
				mfTaskDataEntry(task, map, map, isDependantTask, TaskStatusConstants.RETURNED);
				processMfTaskLogDetails(task, TaskLogConstants.RETURNED, "", null, group);
				processTaskCompletion(task, map, mfStatus, type);
			} else {
				mfTaskDataEntry(task, map, map, isDependantTask, TaskStatusConstants.CLOSED_STATUS);
				processMfTaskLogDetails(task, TaskLogConstants.CLOSED, "", null, group);
				processTaskCompletion(task, map, mfStatus, type);
			}
			// Trigger Mail
			decideMailTriggerBasedOnStatus(mfStatus, task);

			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}

	private void processTaskCompletion(Task task, Map<String, Object> map, String mfStatus, String type)
			throws TclCommonException {

		String preMfTask = task.getMfDetail().getIsPreMfTask();
		boolean isPreMfTask = false;
		
		if(preMfTask!=null && preMfTask.equals("1")) {
			isPreMfTask = true;
		}
		
		if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM) && !isPreMfTask) {
			closeAllMfManualTasks(task.getWfProcessInstId(), map);
				processMfResponse(task, mfStatus);
			
		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP) && !isPreMfTask) {
			if (ManualFeasibilityWFConstants.SECONDARY.equalsIgnoreCase(type)) {
				processMfResponse(task, mfStatus);
			} else {
				if (!mfStatus.equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN))
					processMfResponse(task, mfStatus);// triggerMfMilestone(task.getWfProcessInstId());
			}

		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_PRV) && !isPreMfTask) {
			if (mfStatus.equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN))
				processMfResponse(task, mfStatus);// triggerMfMilestone(task.getWfProcessInstId());

		}
		
		if(isPreMfTask) {
			if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)){
				closeAllMfManualTasks(task.getWfProcessInstId(), map);
				mf3DMapsTaskService.processMfResponse(task, mfStatus);
			}
			else if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP) ) {
				if (ManualFeasibilityWFConstants.SECONDARY.equalsIgnoreCase(type)) {
					mf3DMapsTaskService.processMfResponse(task, mfStatus);
				} else {
					if (!mfStatus.equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN))
						mf3DMapsTaskService.processMfResponse(task, mfStatus);
				}

			} else if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_PRV)) {
				if (mfStatus.equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN))
					mf3DMapsTaskService.processMfResponse(task, mfStatus);

			}
		}
	}

	protected void decideMailTriggerBasedOnStatus(String mfStatus, Task task) {
		// trigger mail
		if (!mfStatus.equalsIgnoreCase("return")) {
			notificationService.manualFeasibilityTaskClose(task);
		} else {
		  notificationService.manualFeasibilityTaskReturn(task);
		}
	}


	public boolean checkIfTaskClosed(String caseInstanceId, String team) {

		String teamTask = "manual_feasibility_".concat(team);
		List<Task> tasks = taskRepository.findByWfProcessInstId(caseInstanceId).stream()
				.filter(task -> task.getMstTaskDef().getKey().equalsIgnoreCase(teamTask)).collect(Collectors.toList());
		if (tasks.isEmpty()) {
			LOGGER.info("No tasks assigned to {} team.", team);
			return true;
		} else {
			List<Task> completedTasks = tasks.stream()
					.filter(task -> task.getMstStatus() != null && StringUtils.isNotEmpty(task.getMstStatus().getCode())
							&& task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.RETURNED)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.DELETED))
					.collect(Collectors.toList());
			if (tasks.size() == completedTasks.size())
				return true;

		}

		return false;

	}

	public void closeAllMfManualTasks(String caseInstanceId, Map<String, Object> map) throws TclCommonException {
		try {
			if (!StringUtils.isEmpty(caseInstanceId)) {
				List<Task> tasks = taskRepository.findByWfProcessInstId(caseInstanceId);
				tasks.stream().forEach(task -> {
					if (!(task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
							|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.DELETED))
							&& !task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)
							&& !task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP)) {
						LOGGER.info("Closing task for {} - task Id : {} ", task.getMstTaskDef().getKey(), task.getId());

						try {
							mfTaskDataEntry(task, map, map, true, TaskLogConstants.CLOSED);
						} catch (TclCommonException e) {
							throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR,
									ResponseResource.R_CODE_ERROR);
						}
						String group = "";
						Optional<ProcessTaskLog> taskLogOpt = processTaskLogRepository.findByTask(task).stream()
								.findAny();
						if (taskLogOpt.isPresent())
							group = taskLogOpt.get().getGroupFrom();
						processMfTaskLogDetails(task, TaskLogConstants.CLOSED, "", null, group);
					}
				});
			}
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);

		}

	}

	private void triggerMfMilestone(String caseInstanceId) {
		LOGGER.info("Triggering Response Complete Milestone");
		if (StringUtils.isEmpty(caseInstanceId))
			cmmnRuntimeService.setVariable(caseInstanceId, "processFinalResponse", true);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String manuallyCompleteFlowableTask(String taskId, Map<String, Object> wfMap) {
		try {

			flowableTaskService.complete(taskId, wfMap);
			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}

	/**
	 * 
	 * This function is used to update assignee for multiple tasks-- L2O Discount
	 * delegation Purpose
	 * 
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false,isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRED)
	public AssigneeResponse updateAssigneeForMultipleTasks(AssigneeRequest request) throws TclCommonException {
		AssigneeResponse assigneeResponse = new AssigneeResponse();
		if (request.getTaskIds() == null) {
			throw new TclCommonException(CommonConstants.ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Iterating the taskIds {}",request.getTaskIds());
		for (Integer taskId : request.getTaskIds()) {
			LOGGER.info("Processing the taskId {}",taskId);
			Optional<Task> optionalTask = taskRepository.findById(taskId);
			if (optionalTask.isPresent()) {
				Task task = optionalTask.get();
				task.setClaimTime(new Timestamp(new Date().getTime()));
				LOGGER.info("updating the assignment details the taskId {}",taskId);
				updateAssignmentDetails(task, request);
				LOGGER.info("updated the assignment details the taskId {}",taskId);
				task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.INPROGRESS));
				LOGGER.info("Saving the taskId {}",taskId);
				taskRepository.save(task);
				LOGGER.info("Saved the taskId {}",taskId);
				constructAssigneeResponse(task, request, assigneeResponse);
				LOGGER.info("constructed assignee response for task Id {} and response is {}",taskId,assigneeResponse);
			}
		
		}
		return assigneeResponse;
	}

	private void constructAssigneeResponse(Task task, AssigneeRequest request, AssigneeResponse assigneeResponse) {
		assigneeResponse.setId(task.getId());
		assigneeResponse.setStatus(task.getMstStatus().getCode());
		assigneeResponse.setName(task.getMstTaskDef().getName());
		assigneeResponse.setAssigneeNameFrom(request.getAssigneeNameFrom());
		assigneeResponse.setAssigneeNameTo(request.getAssigneeNameTo());
		//String queueResponse = null;
		TaskDetailBean taskDetailBean = new TaskDetailBean();
		taskDetailBean.setQuoteId(task.getQuoteId());
		taskDetailBean.setAssignee(request.getAssigneeNameTo());
		//Gvpn Commercial Comment
		
		try {
			String cmdTaskUpdate = Utils.convertObjectToJson(taskDetailBean);
			mqUtils.send(cmdTaskUpdateQueue, cmdTaskUpdate);
		} catch (Exception e) {
			LOGGER.error("Error in fetching task information ", e);
		}
		 
		}
		
	
	/**
	 * This is for MF - which needs to queue call to update QuoteStatus
	 * @param task
	 * @author krutsrin
	 * @param request
	 * @param assigneeResponse
	 */
	private void constructMFAssigneeResponse(Task task, AssigneeRequest request, AssigneeResponse assigneeResponse) {
		assigneeResponse.setId(task.getId());
		assigneeResponse.setStatus(task.getMstStatus().getCode());
		assigneeResponse.setName(task.getMstTaskDef().getName());
		assigneeResponse.setAssigneeNameFrom(request.getAssigneeNameFrom());
		assigneeResponse.setAssigneeNameTo(request.getAssigneeNameTo());
		TaskDetailBean taskDetailBean = new TaskDetailBean();
		taskDetailBean.setQuoteId(task.getQuoteId());
		taskDetailBean.setAssignee(request.getAssigneeNameTo());
	}
		

	

	/**
	 * 
	 * This is a test method --- will be deleted soon
	 * 
	 * @param priceDiscountBean
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public void createDummyTask(PriceDiscountBean priceDiscountBean) throws TclCommonException {
		if (validatePriceDiscountBean(priceDiscountBean)) {
			SiteDetail siteDetail = new SiteDetail();
			siteDetail.setCreatedTime(new Timestamp(new Date().getTime()));
			siteDetail.setUpdatedTime(new Timestamp(new Date().getTime()));
			siteDetail.setIsActive(CommonConstants.ACTIVE);
			// siteDetail.setLocationId(priceDiscountBean.getLocationId());
			siteDetail.setQuoteCode(priceDiscountBean.getQuoteCode());
			siteDetail.setQuoteId(priceDiscountBean.getQuoteId());
			// siteDetail.setSiteCode(priceDiscountBean.getSiteCode());
			// siteDetail.setSiteId(priceDiscountBean.getSiteId());
			siteDetail.setSiteDetail(Utils.convertObjectToJson(priceDiscountBean.getSiteDetail()));
			siteDetail.setStatus(MasterDefConstants.IN_PROGRESS);
			siteDetail.setRegion(priceDiscountBean.getRegion());
			siteDetail = siteDetailRepository.saveAndFlush(siteDetail);

			if (siteDetail.getId() != null) {
				Map<String, Object> processVar = new HashMap<>();
				processVar.put("discountApprovalLevel", priceDiscountBean.getDiscountApprovalLevel());
				processVar.put("quoteCode", priceDiscountBean.getQuoteCode());
				processVar.put("quoteId", priceDiscountBean.getQuoteId());
				// processVar.put("siteCode", priceDiscountBean.getSiteCode());
				// processVar.put("siteId", priceDiscountBean.getSiteId());
				processVar.put(MasterDefConstants.SITE_DETAIL_ID, siteDetail.getId());
				processVar.put("processType", "computeProjectPLan");
				processVar.put("root_endDate", new Timestamp(System.currentTimeMillis()));
				runtimeService.startProcessInstanceByKey("commercial_discount_workflow", processVar);
			}
		}
	}

	private Boolean validatePriceDiscountBean(PriceDiscountBean priceDiscountBean) {
		if (priceDiscountBean.getDiscountApprovalLevel() != null && priceDiscountBean.getQuoteCode() != null
				&& priceDiscountBean.getQuoteId() != null && priceDiscountBean.getSiteDetail() != null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Get the discount delegation details for the quote(Latest)
	 * 
	 * @param quoteCode
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
	public Map<String, String> getDiscountDelegationDetails(SiteDetail siteDetail) throws TclCommonException {
		Map<String, String> map = new HashedMap<>();

		if (siteDetail != null) {
			LOGGER.info("---Site details present---");
			List<Task> taskDetails = taskRepository.findBySiteDetail_id(siteDetail.getId());
			if (taskDetails != null) {
				LOGGER.info("---Completed task details present---");
				taskDetails.stream().forEach(task -> {
					map.put(task.getMstTaskDef().getKey(), task.getAssignee());
				});
			}
		}

		if (map != null && !map.isEmpty()) {
			return map;
		}
		LOGGER.info("---Task Data not found---");
		return null;
	}

	public void createCmmnTask() {
		Map<String, Object> variables = new HashMap<>();
		variables.put("initialResponseComplete", false);
		variables.put("isResponseComplete", false);
		variables.put("OSP", false);
		variables.put("PRVTX", false);
		variables.put("MAN", false);
		variables.put("txEngg", false);
		variables.put("AFM", false);
		variables.put("GIMEC", false);
		variables.put("PRV", false);
		variables.put("AssignedTo", "PRV");
		variables.put(ManualFeasibilityWFConstants.MF_DETAIL_ID, 1);
		// variables.put("notificationRepCounter", 0);

		CaseInstance caseInstance = cmmnRuntimeService.createCaseInstanceBuilder()
				.caseDefinitionKey("manual_feasibility_workflow").variables(variables).start();
		List<PlanItemInstance> planItemInstances = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstance.getId()).orderByName().asc().list();
		for (PlanItemInstance planItemInstance : planItemInstances) {
			System.out.println(planItemInstance.getName() + ", Id = " + planItemInstance.getId() + ", state="
					+ planItemInstance.getState() + ", def Id = " + planItemInstance.getPlanItemDefinitionId()
					+ ", type = " + planItemInstance.getPlanItemDefinitionType());

		}
	}

	/**
	 * get dependant team details for a support group
	 * 
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getMfSupportGroupDetails(String groupName) throws TclCommonException {
		List<String> dependantTeams = new ArrayList<>();
		try {
			MfSupportGroup mfSupportGroup = mfSupportGroupRepository.findByGroupName(groupName);
			String dependentTeams = mfSupportGroup.getDependantTeam();
			List<String> dependantTeamsList = Arrays.asList(dependentTeams.split(","));
			List<MfDependantTeam> dependantTeamData = mfDependantTeamRepository.findByTeamNameIn(dependantTeamsList);
			dependantTeamData.stream().forEach(teamData -> {
				String teams = null;
				if (teamData.getTeamRegion() != null && !teamData.getTeamRegion().isEmpty()) {
					teams = teamData.getTeamName() + CommonConstants.UNDERSCORE + teamData.getTeamRegion();
				} else {
					teams = teamData.getTeamName();
				}
				dependantTeams.add(teams);
			});
		} catch (Exception e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
		}

		return dependantTeams;

	}

	/**
	 * to create a task for a given team
	 * 
	 * @param team
	 * @param taskId
	 * @throws TclCommonException
	 */
	public void createTaskForTeam(CreateMfTaskRequestBean request) throws TclCommonException {

		if (!validateCreateMfTaskRequest(request))
			throw new TclCommonException(ExceptionConstants.CANNOT_CREATE_MF_TASK, ResponseResource.R_CODE_BAD_REQUEST);
		try {
			request.getTaskRequest().stream().forEach(taskRequest -> {
				int retryCount = 0;
				LOGGER.info("Creating task for {}", taskRequest.getAssignedTo());
				LOGGER.info("Requestor task ID {}", request.getRequestorTaskId());

				Optional<Task> task = taskRepository.findById(request.getRequestorTaskId());
				if (task.isPresent()) {
					String[] teamAndRegion = taskRequest.getAssignedTo().split("_");
					String team = teamAndRegion[0];
					String region = "";
					if (teamAndRegion.length > 1)
						region = teamAndRegion[1];
					MstTaskDef mstTaskDef = mstTaskDefRepository.findByOwnerGroupAndKeyStartsWith(team,
							"manual_feasibility");
					LOGGER.info("Task Definition Key for the task : {}", mstTaskDef.getKey());
					String taskDefId = mstTaskDef.getKey();
					String caseInstanceId = task.get().getWfProcessInstId();
					LOGGER.info("Case Instance Id in task service : {}", caseInstanceId);
					/*
					 * planItem =
					 * mfTaskPlanItemRepository.findByCaseInstIdAndPlanItemDefIdAndStatus(
					 * caseInstanceId, taskDefId,"enabled");
					 */// plan item definition id and task definition id will be same
					List<PlanItemInstance> planItems = cmmnRuntimeService.createPlanItemInstanceQuery()
							.caseInstanceId(caseInstanceId).list();
					if (planItems.isEmpty())
						LOGGER.info("Plan Items Empty");
					for (PlanItemInstance planItem : planItems) {
						if (planItem.getPlanItemDefinitionType()
								.equalsIgnoreCase(ManualFeasibilityWFConstants.HUMAN_TASK))
							LOGGER.info("Plan Item Id : {} , planItem def id : {} , planItem state : {}",
									planItem.getId(), planItem.getPlanItemDefinitionId(), planItem.getState());
					}
					Optional<PlanItemInstance> planItemOpt = planItems.stream()
							.filter(planItemIns -> planItemIns.getPlanItemDefinitionId().equalsIgnoreCase(taskDefId)
									&& planItemIns.getState().equalsIgnoreCase(ManualFeasibilityWFConstants.ENABLED))
							.findFirst();
					if (planItemOpt.isPresent()) {
						PlanItemInstance planItem = planItemOpt.get();
						LOGGER.info("Plan item Definition Key for the task : {}", planItem.getPlanItemDefinitionId());
						MfTaskDetail taskDetail = persistTaskDetails(taskRequest, request.getRequestorTaskId());

						cmmnRuntimeService.setVariable(caseInstanceId, planItem.getId(), taskDetail);
						/*
						 * cmmnRuntimeService.setVariable(caseInstanceId, "notifyTeam",
						 * taskRequest.getAssignedTo()); cmmnRuntimeService.setVariable(caseInstanceId,
						 * "assignedFrom", taskRequest.getAssignedFrom());
						 * cmmnRuntimeService.setVariable(caseInstanceId, "subject",
						 * taskRequest.getSubject()); cmmnRuntimeService.setVariable(caseInstanceId,
						 * "requestorComments", taskRequest.getRequestorComments());
						 */
						if (taskDefId.equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM))
							cmmnRuntimeService.setVariable(caseInstanceId,
									ManualFeasibilityWFConstants.SUB_GROUP_REGION, region);
						else
							cmmnRuntimeService.setLocalVariable(planItem.getId(),
									ManualFeasibilityWFConstants.SUB_GROUP_REGION, region);

						try {
							cmmnRuntimeService.startPlanItemInstance(planItem.getId());
							LOGGER.info("{} Task created for {} team. ", taskRequest.getSubject(),
									taskRequest.getAssignedTo());

						} catch (Exception e) {
							LOGGER.error("error while creating task ", e);
							LOGGER.info(" {} Task creation Failed for {} team. Retrying ..", taskRequest.getSubject(),
									taskRequest.getAssignedTo());
							retryTaskCreation(caseInstanceId, taskDefId, retryCount, taskRequest);
						}

					} else {
						if (taskDefId.equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM))
							throw new TclCommonRuntimeException(ExceptionConstants.AFM_TASK_ALREADY_PRESENT,
									ResponseResource.R_CODE_ERROR);
						else {
							LOGGER.info(" {} Task creation Failed for {} team. Retrying ..", taskRequest.getSubject(),
									taskRequest.getAssignedTo());
							retryTaskCreation(caseInstanceId, taskDefId, retryCount, taskRequest);
						}
					}
				}
			});
		} catch (Exception e) {
			if (e instanceof TclCommonRuntimeException) {
				throw e;
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_ERROR);
			}
		}

	}

	private void retryTaskCreation(String caseInstanceId, String taskDefId, int retryCount,
			MfTaskRequestBean taskRequest) {

		retryCount++;
		Optional<PlanItemInstance> planItemOpt = cmmnRuntimeService.createPlanItemInstanceQuery()
				.caseInstanceId(caseInstanceId).list().stream()
				.filter(planItemIns -> planItemIns.getPlanItemDefinitionId().equalsIgnoreCase(taskDefId)
						&& planItemIns.getState().equalsIgnoreCase("enabled"))
				.findFirst();
		if (planItemOpt.isPresent()) {
			PlanItemInstance planItem = planItemOpt.get();
			LOGGER.info("Plan item Definition Key for the task : {}", planItem.getPlanItemDefinitionId());
			try {
				cmmnRuntimeService.startPlanItemInstance(planItem.getId());
				LOGGER.info("{} Task created for {} team. ", taskRequest.getSubject(), taskRequest.getAssignedTo());

			} catch (Exception e) {
				LOGGER.error("Error while creating task ", e);
				LOGGER.info(" {} Task creation Failed for {} team. ", taskRequest.getSubject(),
						taskRequest.getAssignedTo());
			}

		} else {
			if (retryCount <= 3)
				retryTaskCreation(caseInstanceId, taskDefId, retryCount, taskRequest);
			else
				LOGGER.info(" {} Task creation Failed for {} team. ", taskRequest.getSubject(),
						taskRequest.getAssignedTo());

		}

	}

	private MfTaskDetail persistTaskDetails(MfTaskRequestBean request, Integer requestorTaskId)  {

		MfTaskDetail taskDetail = null;
		if (request.getTaskId() != null)
			taskDetail = mfTaskDetailRepository.findByTaskIdAndSubjectAndAssignedGroupAndStatus(request.getTaskId(),
					request.getSubject(), request.getAssignedTo(), ManualFeasibilityWFConstants.RETURN);

		if (taskDetail == null)
			taskDetail = new MfTaskDetail();
		else
			LOGGER.info("Existing task detail Id for the task {} : {}", request.getSubject(), taskDetail.getId());

		taskDetail.setSubject(request.getSubject());
		taskDetail.setAssignedTo(request.getAssignedTo());
		taskDetail.setAssignedGroup(request.getAssignedTo());
		taskDetail.setRequestorComments(request.getRequestorComments());
		taskDetail.setResponderComments(request.getResponderComments());
		taskDetail.setAssignedFrom(request.getAssignedFrom());
		taskDetail.setSiteId(request.getSiteId());
		taskDetail.setQuoteId(request.getQuoteId());
		taskDetail.setRequestorTaskId(requestorTaskId);
		taskDetail.setStatus(ManualFeasibilityWFConstants.PENDING);
		taskDetail.setReason("");
		
		// Added for NPL MF
		try {
		if(request.getTaskRelatedTo()!=null) {
			MfTaskData mfTaskData = new MfTaskData();
			mfTaskData.setTaskRelatedTo(request.getTaskRelatedTo());
			taskDetail.setTaskData(Utils.convertObjectToJson(mfTaskData));
		}
		}catch(Exception e) {
			LOGGER.info("Error in setting task data for mfTaskDetail table for taskId {}",request.getTaskId());
		}
		MfTaskDetail mftaskDetail = mfTaskDetailRepository.save(taskDetail);
		return mftaskDetail;
	}

	private boolean validateCreateMfTaskRequest(CreateMfTaskRequestBean request) {
		if (request != null && request.getTaskRequest() != null && !request.getTaskRequest().isEmpty()
				&& request.getRequestorTaskId() != null)
			return true;

		return false;
	}

	/**
	 * Method to get assigned task for the given siteid
	 * 
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<MfTaskDetailBean> getAssignedTask(Integer siteId,Integer taskId) throws TclCommonException {
		List<MfTaskDetailBean> assignedTaskList = new ArrayList<>();
		try {
			/*String assignedFrom = Utils.getSource();
			LOGGER.info("Inside getAssignedTask method to fetch task created for siteid {} by {} ", siteId,
					assignedFrom); */
			
			List<MfTaskDetail> assignedTasks = mfTaskDetailRepository.findBySiteIdAndRequestorTaskIdOrderByIdDesc(siteId,
					taskId);
			assignedTasks.stream().forEach(assignedTask -> {
				LOGGER.info("Processing MfTaskDetail data for the taskId {} ", assignedTask.getTask().getId());
				Optional<Task> task = taskRepository.findByIdAndMstStatus_codeNotIn(assignedTask.getTask().getId(),
						TaskStatusConstants.DELETED);
				if (task.isPresent()) {
					MfTaskDetailBean taskDetailBean = new MfTaskDetailBean(assignedTask);
					taskDetailBean.setFeasibilityId(task.get().getFeasibilityId());
				MfTaskData taskData = null;
					try {
						taskData = Utils.convertJsonToObject(assignedTask.getTaskData(), MfTaskData.class);
					} catch (TclCommonException e) {
						LOGGER.error("Error while converting json to object", e);
					}
					// For NPL

					if (taskData != null) {
						taskDetailBean.setTaskData(taskData);
						taskDetailBean.setTaskRelatedTo(taskData.getTaskRelatedTo());

					}

					assignedTaskList.add(taskDetailBean);
				}
			});
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return assignedTaskList;

	}

	/**
	 * Method to edit task status
	 * 
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public void editTaskStatus(MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		LOGGER.info("Inside editTaskStatus method to edit status for the task {} ", mfTaskDetailBean.getTaskId());
		try {
			if (mfTaskDetailBean.getSiteId() != null && mfTaskDetailBean.getTaskId() != null) {
				Optional<Task> taskOpt = taskRepository.findById(mfTaskDetailBean.getTaskId());
				

				if(taskOpt.isPresent() && (taskOpt.get().getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)
						|| taskOpt.get().getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP))){
					
					List<MfResponseDetail> recordInDB =mfResponseDetailRepository.findBytaskId(taskOpt.get().getId());
					// for feasible sites task Re trigger look for site based response alone. -- PIPF55
					
					Boolean isRetriggeredTask = false;
					if(taskOpt.get().getMfDetail()!=null) {
						MfDetailsBean mfDetailsBean = nplTaskService.getMfDetailsBean(taskOpt.get().getMfDetail());
						isRetriggeredTask = mfDetailsBean.getMfDetails().isRetriggerTaskForFeasibleSites();
					LOGGER.info("Retriggered status of this task with ID {} is {} ",mfTaskDetailBean.getTaskId(), isRetriggeredTask);
						if (mfDetailsBean != null && mfDetailsBean.getMfDetails() != null
								&& mfDetailsBean.getMfDetails().isRetriggerTaskForFeasibleSites()) {
							recordInDB = mfResponseDetailRepository.findBySiteId(taskOpt.get().getSiteId());
						}
					}
					
					
					Optional<MfResponseDetail> result =null;
					if(recordInDB!=null && !recordInDB.isEmpty()) {
						result = recordInDB.stream().filter( x -> x.getFeasibilityStatus()!=null)
							.filter(y -> y.getFeasibilityStatus().startsWith(ManualFeasibilityWFConstants.FEASIBLE)).findAny();
						}
					
					if(mfTaskDetailBean.getStatus()!=null && mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.FEASIBLE)) {
						if(result==null || !result.isPresent()) {
							LOGGER.info("feasible responses has not been created but overall status is feasible for taskid {} :",taskOpt.get().getId());
							throw new TclCommonException(ExceptionConstants.NO_FEASIBLE_RESPONSE,
									ResponseResource.R_CODE_ERROR);
						}
					}else if(mfTaskDetailBean.getStatus()!=null && mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.NOT_FEASIBLE)) {
						if(result!=null && result.isPresent() && isRetriggeredTask.equals(false)) {
							LOGGER.info("feasible responses are available but overall status is Not-feasible for taskid {} :",taskOpt.get().getId());
							throw new TclCommonException(ExceptionConstants.FEASIBLE_RESPONSE_AVAILABLE,
									ResponseResource.R_CODE_ERROR);
						}
					}
				}
				if (mfTaskDetailBean.getStatus() != null
						&& mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN)
						&& taskOpt.isPresent()
						&& taskOpt.get().getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)) {
					LOGGER.info("Checking ASP task status..");
					List<MfTaskDetail> requestorAssignedTask = mfTaskDetailRepository
							.findByRequestorTaskIdAndAssignedToStartsWithAndStatusIn(mfTaskDetailBean.getTaskId(), "ASP",
									Arrays.asList(ManualFeasibilityWFConstants.PENDING));
					if (!requestorAssignedTask.isEmpty()) {
						LOGGER.info("ASP task is open while returning AFM task..");
						throw new TclCommonException(ExceptionConstants.ASP_TASK_IS_OPEN,
								ResponseResource.R_CODE_ERROR);
					}
				} else if (StringUtils.isNotEmpty(mfTaskDetailBean.getStatus())
						&& mfTaskDetailBean.getStatus().equalsIgnoreCase(ManualFeasibilityWFConstants.RETURN)
						&& taskOpt.isPresent() && taskOpt.get().getMstTaskDef().getKey()
								.equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP)) {
					MfTaskDetail aspTaskDetail = mfTaskDetailRepository.findByTaskId(mfTaskDetailBean.getTaskId());
					if(aspTaskDetail !=null && aspTaskDetail.getRequestorTaskId() !=null) {
					Optional<Task> parentTaskOpt = taskRepository
							.findById(aspTaskDetail.getRequestorTaskId());
					if (parentTaskOpt.isPresent() && parentTaskOpt.get().getMstStatus().getCode()
							.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
						LOGGER.info("Parent task with Id : {} is closed ",parentTaskOpt.get().getId());
						throw new TclCommonException(ExceptionConstants.PARENT_TASK_IS_CLOSED,
								ResponseResource.R_CODE_ERROR);
					}
					}
				}
				MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findBySiteIdAndTaskId(mfTaskDetailBean.getSiteId(),
						mfTaskDetailBean.getTaskId());
				if (mfTaskDetail != null) {
					mfTaskDetail.setResponderComments(mfTaskDetailBean.getResponderComments());
					mfTaskDetail.setStatus(mfTaskDetailBean.getStatus());
					mfTaskDetail.setReason(mfTaskDetailBean.getReason());
					MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(mfTaskDetail);
					mfTaskDetailAudit.setCreatedBy(Utils.getSource());
					mfTaskDetailAuditRepository.save(mfTaskDetailAudit);
					mfTaskDetailRepository.save(mfTaskDetail);
				}
				if (taskOpt.isPresent()) {
					Task task = taskOpt.get();
					if (task.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_PRV)) {
						cmmnRuntimeService.setVariable(task.getWfProcessInstId(),
								ManualFeasibilityWFConstants.PRV_STATUS, mfTaskDetailBean.getStatus());
						cmmnRuntimeService.setVariable(task.getWfProcessInstId(),
								ManualFeasibilityWFConstants.PRV_COMMENTS, mfTaskDetailBean.getResponderComments());
					}
				}
			} else
				throw new TclCommonException(ExceptionConstants.TASK_DETAILS_EMPTY,
						ResponseResource.R_CODE_BAD_REQUEST);
		} catch (Exception e) {
			if (e instanceof TclCommonException) {
				throw e;
			} else {
				throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
			}
		}

	}

	/**
	 * Get task summary by status and assignee for commercial work flow
	 * 
	 * @param month
	 * @param fromDate
	 * @param toDate
	 * @return {@link TaskSummaryResponse}
	 */
	public TaskSummaryResponse gettaskSummaryForCommercialWorkFlowReports(String month, String fromDate,
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
	 * Method to get task details Report
	 *
	 * @param response
	 * @param month
	 * @param fromDate
	 * @param toDate
	 * @throws TclCommonException
	 */
	public void gettaskDetailReport(HttpServletResponse response, String month, String fromDate, String toDate)
			throws TclCommonException {
		Map<String, LocalDate> taskCreatedDates = getTaskCreatedFromAndToDate(month, fromDate, toDate);
		LocalDate taskCreatedFromDate = taskCreatedDates.get(FIRST_DAY_OF_MONTH);
		LocalDate taskCreatedToDate = taskCreatedDates.get(LAST_DAY_OF_MONTH).plusDays(1);

		List<Map<String, Object>> taskDetails = getApprovedAndUnApprovedTaskDetails(taskCreatedFromDate,
				taskCreatedToDate);

		if (!CollectionUtils.isEmpty(taskDetails)) {
			List<Map<String, Object>> assignedTaskDetails = taskDetails.stream()
					.filter(taskDetailMap -> Objects.nonNull(taskDetailMap.get(TASK_ASSIGNMENT_USER_NAME)))
					.collect(Collectors.toList());
			Long unassignedTaskCount = taskDetails.stream()
					.filter(taskDetailMap -> Objects.isNull(taskDetailMap.get(TASK_ASSIGNMENT_USER_NAME))).count();
			createTaskDetailReport(response, assignedTaskDetails, taskCreatedFromDate, taskCreatedToDate,
					unassignedTaskCount);
		} else {
			throw new TclCommonRuntimeException(ExceptionConstants.TASK_DETAILS_EMPTY,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * Method to get task trail Report
	 * 
	 * @author krutsrin
	 * 
	 * @param response
	 * @param taskId
	 * @param siteId
	 * @return @List{ProcessTaskLog}
	 * @throws TclCommonException
	 */
	public void gettaskTrail(HttpServletResponse response, Integer taskId, Integer siteId) throws TclCommonException {

		List<TaskTrailResponseBean> processTaskLogListFromDB = new ArrayList<TaskTrailResponseBean>();
		// MfTaskDetail mfTaskDetail =null;
		if (siteId != null && taskId != null) {
			List<Integer> taskIdList = new ArrayList<Integer>();

			taskIdList.add(taskId); // master AFM ID
			List<MfTaskDetail> result = mfTaskDetailRepository.findBySiteIdAndRequestorTaskId(siteId, taskId);
			List<Integer> listFinal = result.stream().map(taskDetail -> taskDetail.getTask().getId())
					.collect(Collectors.toList());
			taskIdList.addAll(listFinal);

			/*
			 * if (mfTaskDetail != null) {
			 * taskIdList.add(mfTaskDetail.getRequestorTaskId()); }
			 */
			List<Map<String, Object>> data = processTaskLogRepository.findByTaskIds(taskIdList);
			final ObjectMapper mapper = new ObjectMapper();
			data.stream().forEach(map -> {
				processTaskLogListFromDB.add(mapper.convertValue(map, TaskTrailResponseBean.class));
			});

		}
		getTaskTrailExcel(processTaskLogListFromDB, response, siteId);
	}

	/**
	 * Method to generate excel sheet.
	 * 
	 * @author krutsrin
	 * 
	 * @param assignedGroupingList
	 * @param groupName
	 * @param response
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public HttpServletResponse getTaskTrailExcel(List<TaskTrailResponseBean> processTaskLogList,
			HttpServletResponse response, Integer siteId) throws TclCommonException {

		if (processTaskLogList != null && !processTaskLogList.isEmpty()) {
			String[] columns = { "TASK ID", "ACTION", " ACTION_FROM", "ACTION_TO", "CREATED_TIME", "QUOTE_CODE",
					"SUBJECT", "REQUESTOR'S COMMENTS", "RESPONDER'S COMMENTS" };

			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

			XSSFWorkbook workbook = new XSSFWorkbook();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerFont.setFontHeightInPoints((short) 12);
			CellStyle headerCellStyle = workbook.createCellStyle();
			headerCellStyle.setFont(headerFont);
			Context context = new Context();
			Sheet sheet = workbook.createSheet(
					processTaskLogList.get(0).getGroupFrom() != null ? processTaskLogList.get(0).getGroupFrom()
							: "TaskTrail");

			processTaskLogList.forEach(z -> {
				Row headerRow = sheet.createRow(0);
				// context class obj for initializing row and rowCount

				for (int i = 0; i < columns.length; i++) {
					Cell cell = headerRow.createCell(i);
					cell.setCellValue(columns[i]);
					cell.setCellStyle(headerCellStyle);
				}

				// Resize all columns to fit the content size
				for (int i = 0; i < columns.length; i++) {
					sheet.autoSizeColumn(i);
				}
				context.rowCount++;
				context.row = sheet.createRow(context.rowCount);
				if (z.getTaskId() != null) {
					context.row.createCell(0).setCellValue(z.getFeasibilityId());
				}
				if (z.getAction() != null) {
					context.row.createCell(1).setCellValue(z.getAction());
				}
				if (z.getActionFrom() != null) {
					context.row.createCell(2).setCellValue(z.getActionFrom());
				}
				if (z.getActionTo() != null) {
					context.row.createCell(3).setCellValue(z.getActionTo());
				}
				/*
				 * if (z.getGroupFrom() != null) {
				 * context.row.createCell(4).setCellValue(z.getGroupFrom()); } if
				 * (z.getGroupTo() != null) {
				 * 
				 * String[] groupToArr = z.getGroupTo().split("@"); if(groupToArr!=null &&
				 * groupToArr.length >0) {
				 * context.row.createCell(5).setCellValue(groupToArr[0].toUpperCase()); }else {
				 * context.row.createCell(5).setCellValue(z.getGroupTo().toUpperCase()); } }
				 */

				if (z.getCreatedTime() != null) {
					Date dateStr = z.getCreatedTime();
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					context.row.createCell(4).setCellValue(formatter.format(dateStr));
				}

				if (z.getQuoteCode() != null) {
					context.row.createCell(5).setCellValue(z.getQuoteCode());
				}

				if (z.getSubject() != null) {
					context.row.createCell(6).setCellValue(z.getSubject());
				}
				if (z.getRequestorComment() != null) {
					context.row.createCell(7).setCellValue(z.getRequestorComment());
				}
				if (z.getResponderComment() != null) {
					if (z.getAction().equalsIgnoreCase("Returned") || z.getAction().equalsIgnoreCase("Closed")) {
						context.row.createCell(8).setCellValue(z.getResponderComment());
					}
				}

				/*
				 * if (z.getStatus() != null) {
				 * context.row.createCell(11).setCellValue(z.getStatus());
				 * if(z.getAction().equalsIgnoreCase("Returned") ||
				 * z.getAction().equalsIgnoreCase("Closed")) {
				 * context.row.createCell(11).setCellValue(z.getStatus()); }else {
				 * context.row.createCell(11).setCellValue("Pending");
				 * 
				 * } }
				 */
			});
			generateExcel(response, "TaskTrailDetails", outByteStream, workbook);
		}
		return response;
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

		LOGGER.info("Approved task id count : {}",approvedTaskIds.size());
		List<Map<String, Object>> unapprovedTaskDetails = new ArrayList<>();
		if (!CollectionUtils.isEmpty(approvedTaskDetails)) {
			LOGGER.info("Approved task details present within this date range!!");
			unapprovedTaskDetails = taskRepository.findUnapprovedTaskDetailsWithinDateRange(taskCreatedFromDate,
					taskCreatedToDate, approvedTaskIds);
		} else {
			LOGGER.info("Approved task details not present within this date range!!");
			unapprovedTaskDetails = taskRepository.findUnapprovedTaskDetailsWithinDateRange(taskCreatedFromDate,
					taskCreatedToDate);

		}
		LOGGER.info("Unapproved task count : {}",unapprovedTaskDetails.size());

		return Stream.concat(approvedTaskDetails.stream(), unapprovedTaskDetails.stream()).collect(Collectors.toList());
	}

	/**
	 * Method to create report based on task details list within the date range
	 *
	 * @param response
	 * @param taskDetails
	 * @param taskCreatedFromDate
	 * @param taskCreatedToDate
	 * @param unassignedTaskCount
	 * @throws TclCommonException
	 */
	private void createTaskDetailReport(HttpServletResponse response, List<Map<String, Object>> taskDetails,
			LocalDate taskCreatedFromDate, LocalDate taskCreatedToDate, Long unassignedTaskCount)
			throws TclCommonException {
		String fileName = TASK_DETAIL_REPORT + XLSX;
		String sheetName = TASK_DETAIL_REPORT;
		byte[] outArray = null;
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
		XSSFWorkbook workbook = new XSSFWorkbook();
		createTaskDetailExcel(workbook, taskDetails, sheetName, unassignedTaskCount);
		generateExcel(response, fileName, outByteStream, workbook);
	}

	/**
	 * Method to generat created Excel
	 *
	 * @param response
	 * @param fileName
	 * @param outByteStream
	 * @param workbook
	 * @throws TclCommonException
	 */
	private void generateExcel(HttpServletResponse response, String fileName, ByteArrayOutputStream outByteStream,
			Workbook workbook) throws TclCommonException {
		byte[] outArray;
		try {
			workbook.write(outByteStream);
			outArray = outByteStream.toByteArray();
			response.reset();
			response.setContentType(APPLICATION_MS_EXCEL);
			response.setContentLength(outArray.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
			workbook.close();
			FileCopyUtils.copy(outArray, response.getOutputStream());
			outByteStream.flush();
			outByteStream.close();
		} catch (IOException e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
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
	 * Creates Excel based on task details
	 *
	 * @param workbook
	 * @param taskDetails
	 * @param sheetName
	 * @param unassignedTaskCount
	 * @return {@link Workbook}
	 */
	private Workbook createTaskDetailExcel(XSSFWorkbook workbook, List<Map<String, Object>> taskDetails,
			String sheetName, Long unassignedTaskCount) {
		Sheet sheet = workbook.createSheet(sheetName);
		sheet.setColumnWidth(10000, 10000);
		final int[] rowId = { 1 };

		if (!CollectionUtils.isEmpty(taskDetails)) {
			createHeaderRowForTaskDetailExcel(sheet);
			taskDetails.forEach(taskDetailMap -> {
				createRecords(sheet, rowId, taskDetailMap);
			});
		}

		setUnassignedTaskCountInExcel(workbook, unassignedTaskCount, sheet, rowId);
		return workbook;
	}

	/**
	 * Set Unassigned task counts in excel
	 *
	 * @param workbook
	 * @param unassignedTaskCount
	 * @param sheet
	 * @param rowId
	 */
	private void setUnassignedTaskCountInExcel(Workbook workbook, Long unassignedTaskCount, Sheet sheet, int[] rowId) {
		Row row = sheet.createRow(rowId[0]++);
		Cell cell = row.createCell(0);
		CellStyle style = workbook.createCellStyle();
		style.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		cell.setCellStyle(style);
		cell.setCellValue(TOTAL_UNASSIGNED_TASKS + String.valueOf(unassignedTaskCount));
	}

	/**
	 * Create records for each taskdetail map for excel
	 *
	 * @param sheet
	 * @param rowId
	 * @param taskDetailMap
	 */
	private void createRecords(Sheet sheet, int[] rowId, Map<String, Object> taskDetailMap) {
		int cellId = 0;
		Row row = sheet.createRow(rowId[0]++);
		Cell cell;

		cell = row.createCell(cellId++);
		cell.setCellValue(Objects.nonNull(taskDetailMap.get(TASK_ASSIGNMENT_USER_NAME))
				? String.valueOf(taskDetailMap.get(TASK_ASSIGNMENT_USER_NAME))
				: CommonConstants.EMPTY);
		cell = row.createCell(cellId++);
		cell.setCellValue(
				Objects.nonNull(taskDetailMap.get(OPPORTUNITY_ID)) ? String.valueOf(taskDetailMap.get(OPPORTUNITY_ID))
						: CommonConstants.EMPTY);
		cell = row.createCell(cellId++);
		cell.setCellValue(Objects.nonNull(taskDetailMap.get(QUOTE_CODE)) ? String.valueOf(taskDetailMap.get(QUOTE_CODE))
				: CommonConstants.EMPTY);
		cell = row.createCell(cellId++);
		cell.setCellValue(Objects.nonNull(taskDetailMap.get(STATUS)) ? String.valueOf(taskDetailMap.get(STATUS))
				: CommonConstants.EMPTY);
		cell = row.createCell(cellId++);
		cell.setCellValue(
				Objects.nonNull(taskDetailMap.get(ACCOUNTNAME)) ? String.valueOf(taskDetailMap.get(ACCOUNTNAME))
						: CommonConstants.EMPTY);
		cell = row.createCell(cellId++);
		cell.setCellValue(Objects.nonNull(taskDetailMap.get(REGION)) ? String.valueOf(taskDetailMap.get(REGION))
				: CommonConstants.EMPTY);
		cell = row.createCell(cellId++);
		Double tcv = Objects.nonNull(taskDetailMap.get(TCV)) ? Double.valueOf((String) taskDetailMap.get(TCV)) : 0;
		Double formatedTcv = Double.valueOf(new DecimalFormat("##.##").format(tcv));
		cell.setCellValue(formatedTcv);
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);
		cell = row.createCell(cellId++);
		cell.setCellValue(
				Objects.nonNull(taskDetailMap.get(CREATED_DATE)) ? String.valueOf(taskDetailMap.get(CREATED_DATE))
						: CommonConstants.EMPTY);

		cell = row.createCell(cellId++);
		cell.setCellValue(
				Objects.nonNull(taskDetailMap.get(COMPLETED_DATE)) ? String.valueOf(taskDetailMap.get(COMPLETED_DATE))
						: CommonConstants.EMPTY);

		Period period = getTatDays(taskDetailMap);
		cell = row.createCell(cellId++);
		cell.setCellValue(period.getDays());
		cell.setCellType(Cell.CELL_TYPE_NUMERIC);

		cell = row.createCell(cellId++);
		cell.setCellValue(
				Objects.nonNull(taskDetailMap.get(TASK_DEF_KEY)) ? String.valueOf(taskDetailMap.get(TASK_DEF_KEY))
						: CommonConstants.EMPTY);

		String cdaArc = CommonConstants.EMPTY;
		String cdaNrc = CommonConstants.EMPTY;
		String approverComments = CommonConstants.EMPTY;
		String rejectionComments = CommonConstants.EMPTY;
		String engineArc = CommonConstants.EMPTY;
		String engineNrc = CommonConstants.EMPTY;
		String taskData = CommonConstants.EMPTY;
		if (Objects.nonNull(taskDetailMap.get(TASK_ID))) {
			TaskData taskDataOpt = taskDataRepository
					.findFirstByTask_idOrderByCreatedTimeDesc(Integer.parseInt(taskDetailMap.get(TASK_ID).toString()));
			if (taskDataOpt!=null) {
				taskData = taskDataOpt.getData();
				if (StringUtils.isNotBlank(taskData)) {
					try {
						CommercialTaskDataBean commercialTaskDataBean = Utils.convertJsonToObject(taskData,
								CommercialTaskDataBean.class);
						if (commercialTaskDataBean.getSiteArc() != null) {
							cdaArc = commercialTaskDataBean.getSiteArc().toString();
						}
						if (commercialTaskDataBean.getSiteNrc() != null) {
							cdaNrc = commercialTaskDataBean.getSiteNrc().toString();
						}
						if (commercialTaskDataBean.getApproverComments() != null) {
							approverComments = commercialTaskDataBean.getApproverComments();
						}
						if (commercialTaskDataBean.getRejectedComments() != null) {
							rejectionComments = commercialTaskDataBean.getRejectedComments();
						}
						if (Objects.nonNull(taskDetailMap.get(QUOTE_CODE))
								&& StringUtils.isNotBlank(commercialTaskDataBean.getPricingResponse())) {
							String quoteCode = taskDetailMap.get(QUOTE_CODE).toString();
							Map<String, Object> pricingRespose = Utils
									.convertJsonToObject(commercialTaskDataBean.getPricingResponse(), Map.class);
							if (quoteCode.startsWith("IAS")) {

							} else if (quoteCode.startsWith("GVPN")) {

							} else if (quoteCode.startsWith("NPL")) {

							} else if (quoteCode.startsWith("NDE")) {

							}
						}
					} catch (Exception e) {
						LOGGER.error("Error on processing task data ", e);
					}
				}
			}
		}
		cell = row.createCell(cellId++);
		cell.setCellValue(cdaArc);
		cell = row.createCell(cellId++);
		cell.setCellValue(cdaNrc);
		cell = row.createCell(cellId++);
		cell = row.createCell(cellId++);
		cell.setCellValue(engineArc);
		cell = row.createCell(cellId++);
		cell.setCellValue(engineNrc);
		cell.setCellValue(approverComments);
		cell = row.createCell(cellId++);
		cell.setCellValue(rejectionComments);

	}

	/**
	 * Converts object to LocalDate
	 *
	 * @param timestampObject
	 * @return {@link LocalDate}
	 */
	private LocalDate getLocalDateFromTimeStampObject(Object timestampObject) {
		return Optional.ofNullable(timestampObject).map(object -> {
			Timestamp timestamp = (Timestamp) object;
			return timestamp.toLocalDateTime().toLocalDate();
		}).orElse(null);
	}

	/**
	 * Get task tat days from created and end date
	 *
	 * @param taskDetailMap
	 * @return {@link Period}
	 */
	private Period getTatDays(Map<String, Object> taskDetailMap) {
		Timestamp tatCreatedTimeStamp = (Timestamp) taskDetailMap.get(TAT_CREATEDTIME);
		Timestamp tatEndTimeStamp = (Timestamp) taskDetailMap.get(TAT_ENDTIME);
		Period period = null;
		if (Objects.isNull(tatCreatedTimeStamp)) {
			throw new TclCommonRuntimeException(ExceptionConstants.TASK_CREATED_DATE_NULL,
					ResponseResource.R_CODE_INTERNAL_SERVER_ERROR);
		} else if (Objects.isNull(tatEndTimeStamp)) {
			period = Period.between(getLocalDateFromTimeStampObject(tatCreatedTimeStamp), LocalDate.now());
		} else {
			period = Period.between(getLocalDateFromTimeStampObject(tatCreatedTimeStamp),
					getLocalDateFromTimeStampObject(tatEndTimeStamp));
		}
		return period;
	}

	/**
	 * Create header row for task details
	 *
	 * @param sheet
	 */
	private void createHeaderRowForTaskDetailExcel(Sheet sheet) {
		Row headerRow = sheet.createRow(0);
		Cell headerCellValue;
		headerCellValue = headerRow.createCell(0);
		headerCellValue.setCellValue(COMMERCIAL_MANAGER_NAME);
		headerCellValue = headerRow.createCell(1);
		headerCellValue.setCellValue(OPPORTUNITY);
		headerCellValue = headerRow.createCell(2);
		headerCellValue.setCellValue(QUOTE);
		headerCellValue = headerRow.createCell(3);
		headerCellValue.setCellValue(STATUS);
		headerCellValue = headerRow.createCell(4);
		headerCellValue.setCellValue(ACCOUNT_NAME);
		headerCellValue = headerRow.createCell(5);
		headerCellValue.setCellValue(REGION);
		headerCellValue = headerRow.createCell(6);
		headerCellValue.setCellValue(ACV);
		headerCellValue = headerRow.createCell(7);
		headerCellValue.setCellValue(ASSIGNED_DATE);
		headerCellValue = headerRow.createCell(8);
		headerCellValue.setCellValue(APPROVAL_DATE);
		headerCellValue = headerRow.createCell(9);
		headerCellValue.setCellValue(TAT_DAYS);
		headerCellValue = headerRow.createCell(10);
		headerCellValue.setCellValue(DISCOUNT_LEVEL);
		headerCellValue = headerRow.createCell(11);
		headerCellValue.setCellValue(CDA_ARC);
		headerCellValue = headerRow.createCell(12);
		headerCellValue.setCellValue(CDA_NRC);
		headerCellValue = headerRow.createCell(13);
		headerCellValue.setCellValue(ENGINE_ARC);
		headerCellValue = headerRow.createCell(14);
		headerCellValue.setCellValue(ENGINE_NRC);
		headerCellValue = headerRow.createCell(15);
		headerCellValue.setCellValue(APPROVAL_COMMENTS);
		headerCellValue = headerRow.createCell(16);
		headerCellValue.setCellValue(REJECT_COMMENTS);

	}

	/**
	 * 
	 * This function is used to save commercial Tcv Details
	 * 
	 * @param CommercialTaskDetailsBean
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false)
	public void createCommercialTcv(CommercialTaskDetailsBean commercialTaskDetailsBean) throws TclCommonException {
		LOGGER.info("Inside createCommercialTcv method" );
		if (commercialTaskDetailsBean.getApproverName() == null || commercialTaskDetailsBean.getQuoteCode() == null
				|| commercialTaskDetailsBean.getQuoteId() == null || commercialTaskDetailsBean.getTaskId() == null
				|| commercialTaskDetailsBean.getApproverName() == null) {
			throw new TclCommonException(CommonConstants.ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		}
		LOGGER.info("Save commercial tcv value to CommercialTaskDetails ");
		CommercialTaskDetails commercialTaskDetails = new CommercialTaskDetails();
		commercialTaskDetails.setApproverName(commercialTaskDetailsBean.getApproverName());
		commercialTaskDetails.setCreatedTime(new Timestamp(new Date().getTime()));
		commercialTaskDetails.setQuoteCode(commercialTaskDetailsBean.getQuoteCode());
		commercialTaskDetails.setQuoteId(commercialTaskDetailsBean.getQuoteId());
		commercialTaskDetails.setTaskId(commercialTaskDetailsBean.getTaskId());
		commercialTaskDetails.setTcv(commercialTaskDetailsBean.getTcv());
		commercialTaskDetailsRepository.save(commercialTaskDetails);
		
		//Gvpn Commercial Comment
		
		//commercial status not refelected correctly
		
		/*Optional<Task> task = taskRepository.findById(Integer.parseInt(commercialTaskDetailsBean.getTaskId()));
		if (task.get() != null) {
			Task taskchck = task.get();
			SiteDetail siteDetail = taskchck.getSiteDetail();
			if (siteDetail != null) {
				Integer siteId = siteDetail.getId();
				List<Task> ts = taskRepository.findByQuoteIdAndSiteDetail_id(
						Integer.parseInt(commercialTaskDetailsBean.getQuoteId()), siteId);
				LOGGER.info("task >>>" + ts.toString());
				LOGGER.info("task size >>>" + ts.size());
				if (commercialTaskDetailsBean.getApproverLevel().equalsIgnoreCase("commercial-discount-1")) {
					if (ts.size() == 1) {
						LOGGER.info("Final approval triggered after C1 level");
						TaskDetailBean taskDetailBean = new TaskDetailBean();
						taskDetailBean.setQuoteId(Integer.parseInt(commercialTaskDetails.getQuoteId()));
						taskDetailBean.setAssignee("Commercial updated");

						try {
							String cmdTaskApproverUpdate = Utils.convertObjectToJson(taskDetailBean);
							LOGGER.info("Sending Commercial updated  C1 approval");
							mqUtils.send(cmdTaskUpdateQueue, cmdTaskApproverUpdate);
						} catch (Exception e) {
							LOGGER.error("Error in fetching task information ", e);
						}
					} else {
						Optional<MstTaskAssignment> mstTaskAssignment = mstTaskAssignmentRepository
								.findFirstByAssignedUserAndMstTaskDef_key(commercialTaskDetailsBean.getApproverName(),
										commercialTaskDetailsBean.getApproverLevel());
						if (mstTaskAssignment != null) {

							TaskDetailBean taskDetailBean = new TaskDetailBean();
							taskDetailBean.setQuoteId(Integer.parseInt(commercialTaskDetails.getQuoteId()));
							taskDetailBean.setAssignee(mstTaskAssignment.get().getNextAssignedUser());

							try {
								String cmdTaskApproverUpdate = Utils.convertObjectToJson(taskDetailBean);
								LOGGER.info("Sending task next assignee information after C1 approval");
								mqUtils.send(cmdTaskUpdateQueue, cmdTaskApproverUpdate);
							} catch (Exception e) {
								LOGGER.error("Error in fetching task information ", e);
							}
						}
					}
				} else if (commercialTaskDetailsBean.getApproverLevel().equalsIgnoreCase("commercial-discount-2")) {
					if (ts.size() == 2) {
						LOGGER.info("Final approval triggered after C2 level");
						TaskDetailBean taskDetailBean = new TaskDetailBean();
						taskDetailBean.setQuoteId(Integer.parseInt(commercialTaskDetails.getQuoteId()));
						taskDetailBean.setAssignee("Commercial updated");

						try {
							String cmdTaskApproverUpdate = Utils.convertObjectToJson(taskDetailBean);
							LOGGER.info("Sending Commercial updated  C2 approval");
							mqUtils.send(cmdTaskUpdateQueue, cmdTaskApproverUpdate);
						} catch (Exception e) {
							LOGGER.error("Error in fetching task information ", e);
						}

					} else {
						Optional<MstTaskAssignment> mstTaskAssignment = mstTaskAssignmentRepository
								.findFirstByAssignedUserAndMstTaskDef_key(commercialTaskDetailsBean.getApproverName(),
										commercialTaskDetailsBean.getApproverLevel());
						if (mstTaskAssignment != null) {

							TaskDetailBean taskDetailBean = new TaskDetailBean();
							taskDetailBean.setQuoteId(Integer.parseInt(commercialTaskDetails.getQuoteId()));
							taskDetailBean.setAssignee(mstTaskAssignment.get().getNextAssignedUser());

							try {
								String cmdTaskApproverUpdate = Utils.convertObjectToJson(taskDetailBean);
								LOGGER.info("Sending task next assignee information after C2 approval");
								mqUtils.send(cmdTaskUpdateQueue, cmdTaskApproverUpdate);
							} catch (Exception e) {
								LOGGER.error("Error in fetching task information ", e);
							}
						}
					}
				} else if (commercialTaskDetailsBean.getApproverLevel().equalsIgnoreCase("commercial-discount-3")) {
					LOGGER.info("Final approval triggered after C3 level");
					TaskDetailBean taskDetailBean = new TaskDetailBean();
					taskDetailBean.setQuoteId(Integer.parseInt(commercialTaskDetails.getQuoteId()));
					taskDetailBean.setAssignee("Commercial updated");

					try {
						String cmdTaskApproverUpdate = Utils.convertObjectToJson(taskDetailBean);
						LOGGER.info("Sending Commercial updated  C3 approval");
						mqUtils.send(cmdTaskUpdateQueue, cmdTaskApproverUpdate);
					} catch (Exception e) {
						LOGGER.error("Error in fetching task information ", e);
					}
				}
			}
		}*/
		 
	}

	/**
	 * 
	 * This function is used to saveResponse Details
	 * 
	 * @param action
	 * @param        <CreateResponseBean>
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public <createResponseBean> void saveOrUpdateResponse(CreateResponseBean createResponseBean, String action)
			throws TclCommonException {

		if (action.equals("save")) {
			LOGGER.info("Save create response in DB");
			String orchCategory = null;
			Optional<Task> taskOpt = taskRepository.findById(createResponseBean.getTaskId());
			MfResponseDetail mfCreateResponse = new MfResponseDetail();
			mfCreateResponse.setTaskId(createResponseBean.getTaskId());
			mfCreateResponse.setSiteId(createResponseBean.getSiteId());
			mfCreateResponse.setProvider(createResponseBean.getProvider());
			if(taskOpt.isPresent())
				mfCreateResponse.setProduct(taskOpt.get().getServiceType());
			ObjectMapper mapper = new ObjectMapper();

			JSONObject dataEnvelopeObj = null;
			JSONParser jsonParser = new JSONParser();
			try {
				LOGGER.info("response json coming from UI for save - {}", createResponseBean.getCreateResponseAttr().toString());
				dataEnvelopeObj = (JSONObject) jsonParser.parse(createResponseBean.getCreateResponseAttr());
				if (dataEnvelopeObj.get("Predicted_Access_Feasibility") != null) {
					mfCreateResponse.setFeasibilityStatus((String) dataEnvelopeObj.get("Predicted_Access_Feasibility"));
				}
				if (dataEnvelopeObj.get("provider_name") != null) {
					mfCreateResponse.setProvider((String) dataEnvelopeObj.get("provider_name"));
				}
				if (!dataEnvelopeObj.containsKey("feasibility_response_id")
						|| (dataEnvelopeObj.get("feasibility_response_id") == null)
						|| (dataEnvelopeObj.get("feasibility_response_id") != null
								&& (dataEnvelopeObj.get("feasibility_response_id").equals("")))) {
					dataEnvelopeObj.put("feasibility_response_id", Utils.generateTaskResponseId());
				}

				if (dataEnvelopeObj.get("Orch_LM_Type") != null && !dataEnvelopeObj.get("Orch_LM_Type").equals("")) {
					dataEnvelopeObj.put("Type", dataEnvelopeObj.get("Orch_LM_Type"));
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			String jsonStr = "";
			try {
				jsonStr = mapper.writeValueAsString(dataEnvelopeObj);
				LOGGER.info("Response json string for save call- {}", jsonStr);
			} catch (JsonProcessingException e) {
				LOGGER.info("Exception occurred in parsing response json");
			}

			if (createResponseBean.getFeasibilityType() != null) {
				if (createResponseBean.getFeasibilityType().equals("UBR PMP / WiMax'")) {
					orchCategory = "UBR PMP ( 2mb and 4 mb - BTS column)";
					dataEnvelopeObj.put("Orch_Category", orchCategory);
				}
				if (createResponseBean.getFeasibilityType().equals("RADWIN'")) {
					orchCategory = "UBR PMP ( 2mb and 4 mb - BTS column)";
					dataEnvelopeObj.put("Orch_Category", orchCategory);
				}
			}
			mfCreateResponse.setCreateResponseJson(jsonStr);
			mfCreateResponse.setCreatedBy(Utils.getSource());
			// mfCreateResponse.setUpdatedTime(new Timestamp(new Date().getTime()));
			mfCreateResponse.setCreatedTime(new Timestamp(new Date().getTime()));
			mfCreateResponse.setUpdatedTime(mfCreateResponse.getCreatedTime());
			mfCreateResponse.setType(createResponseBean.getType());
			mfCreateResponse.setFeasibilityMode(createResponseBean.getFeasibilityMode());
			mfCreateResponse.setFeasibilityType(createResponseBean.getFeasibilityType());
			mfCreateResponse.setIsSelected(0);
			mfCreateResponse.setFeasibilityCheck("manual");

			if (createResponseBean.getQuoteId() != null) {
				mfCreateResponse.setQuoteId(Integer.valueOf(createResponseBean.getQuoteId()));
			}
			MfResponseDetail savedResponse = mfResponseDetailRepository.save(mfCreateResponse);
			processMfResponseDetailAudit(savedResponse, false);
		} else if (action.equals("update") && createResponseBean.getTaskId() != null
				&& createResponseBean.getRowId() != null) {

			Optional<MfResponseDetail> mfCreateResponseopt = mfResponseDetailRepository
					.findById(Integer.valueOf(createResponseBean.getRowId()));

			if (mfCreateResponseopt.isPresent()) {
				MfResponseDetail mfCreateResponseForUpdate = mfCreateResponseopt.get();
				mfCreateResponseForUpdate.setTaskId(createResponseBean.getTaskId());

				if (createResponseBean.getSiteId() != null) {
					mfCreateResponseForUpdate.setSiteId(createResponseBean.getSiteId());
				}

				if (createResponseBean.getCreateResponseAttr() != null) {
					ObjectMapper mapper = new ObjectMapper();
					JSONObject dataEnvelopeObj = null;
					JSONParser jsonParser = new JSONParser();
					try {
						LOGGER.info("response json coming from UI for update - {}", createResponseBean.getCreateResponseAttr().toString());
						dataEnvelopeObj = (JSONObject) jsonParser.parse(createResponseBean.getCreateResponseAttr());
						if (dataEnvelopeObj.get("Predicted_Access_Feasibility") != null) {
							mfCreateResponseForUpdate
									.setFeasibilityStatus((String) dataEnvelopeObj.get("Predicted_Access_Feasibility"));
						}

						if (dataEnvelopeObj.get("provider_name") != null) {
							mfCreateResponseForUpdate.setProvider((String) dataEnvelopeObj.get("provider_name"));
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					String jsonStr = "";
					try {
						jsonStr = mapper.writeValueAsString(dataEnvelopeObj);
						LOGGER.info("Response json string for update call- {}", jsonStr);
					} catch (JsonProcessingException e) {
						LOGGER.info("Exception occurred in parsing response json");
					}
					mfCreateResponseForUpdate.setCreateResponseJson(jsonStr);
					LOGGER.info("mfCreateResponseForUpdate value -{}", mfCreateResponseForUpdate.getCreateResponseJson());
				}
				if (createResponseBean.getType() != null) {
					mfCreateResponseForUpdate.setType(createResponseBean.getType());
				}
				if (createResponseBean.getFeasibilityMode() != null) {
					mfCreateResponseForUpdate.setFeasibilityMode(createResponseBean.getFeasibilityMode());
				}
				mfCreateResponseForUpdate.setUpdatedBy(Utils.getSource());
				if (createResponseBean.getFeasibilityType() != null) {
					mfCreateResponseForUpdate.setFeasibilityType(createResponseBean.getFeasibilityType());
				}
				if (createResponseBean.getQuoteId() != null) {
					mfCreateResponseForUpdate.setQuoteId(Integer.valueOf(createResponseBean.getQuoteId()));
				}
				// saving updated time as current time stamp
				mfCreateResponseForUpdate.setUpdatedTime(new Timestamp(new Date().getTime()));

				MfResponseDetail updatedResponse = mfResponseDetailRepository.save(mfCreateResponseForUpdate);
				LOGGER.info("updatedResponse is ------- {}", updatedResponse.getCreateResponseJson());
				processMfResponseDetailAudit(updatedResponse, true);
			}
		}
	}

	/**
	 * Method to get best MF response of a siteId
	 * 
	 * @param mfDetail
	 *
	 * @param siteId
	 * @param siteId
	 * @return {@link MfResponseDetail}
	 * @throws TclCommonException 
	 */
	public MfResponseDetail selectRelevantManualFeasibleResponse(Integer siteId, String siteStatus, MfDetail mfDetail)  {
		MfResponseDetail selectedResponse = null;
		if (!siteStatus.equalsIgnoreCase(RETURNED)) {

			List<MfResponseDetail> mfResponsDetails = mfResponseDetailRepository.findBySiteId(siteId);

			List<MfResponseDetail> mfResponsDetailDualPrimary = new ArrayList<MfResponseDetail>();
			List<MfResponseDetail> mfResponsDetailDualSecondary = new ArrayList<MfResponseDetail>();
			
			MfDetailAttributes mfDetailAttr = null;
			try {
				mfDetailAttr = Utils.convertJsonToObject(mfDetail.getMfDetails(), MfDetailAttributes.class);
			} catch (TclCommonException e) {
				LOGGER.error("Error while converting json to object",e );
			}
			String primaryLmProvider =mfDetailAttr!=null ?mfDetailAttr.getPrimarylastMileProvider() : null;
			String secondaryLmProvider =mfDetailAttr!=null ?mfDetailAttr.getSecondarylastMileProvider() : null;
			
			LOGGER.info("The primaryLMProvider -->{}, The SecondaryLmProvider --- > {}",primaryLmProvider,secondaryLmProvider);

			// dual scenario
			//
			if (mfDetail.getSiteType().equals("Dual-secondary")) {

				mfResponsDetailDualSecondary = mfResponsDetails.stream().filter(x -> x.getType() != null)
						.filter(y -> y.getType().equals("secondary")).collect(Collectors.toList());

				if(mfDetailAttr.isRetriggerTaskForFeasibleSites())
					mfResponsDetails.forEach( mfResponseDetail -> {
						mfResponseDetail.setIsSelected(0);
						mfResponseDetailRepository.save(mfResponseDetail);
							}
					);

				 selectedResponse = lmProviderBasedResponseSelection(secondaryLmProvider,
						mfResponsDetailDualSecondary, siteId);
			}
			if (mfDetail.getSiteType().equals("Dual-primary")) {

				mfResponsDetailDualPrimary = mfResponsDetails.stream().filter(x -> x.getType() != null)
						.filter(y -> y.getType().equals("primary")).collect(Collectors.toList());

				if(mfDetailAttr.isRetriggerTaskForFeasibleSites())
					mfResponsDetails.forEach( mfResponseDetail -> {
								mfResponseDetail.setIsSelected(0);
								mfResponseDetailRepository.save(mfResponseDetail);
							}
					);
				
				selectedResponse = lmProviderBasedResponseSelection(primaryLmProvider,
						mfResponsDetailDualPrimary, siteId);

			} else if (mfDetail.getSiteType().equals("Single-primary")) {
				// single scenario
				if(mfDetailAttr.isRetriggerTaskForFeasibleSites())
					mfResponsDetails.forEach( mfResponseDetail -> {
								mfResponseDetail.setIsSelected(0);
								mfResponseDetailRepository.save(mfResponseDetail);
							}
					);
				selectedResponse = segregateResponse(siteId, mfResponsDetails);
			}
		} else {
			LOGGER.info("task status of siteId {} is Returned {}", siteId);
			return null;
		}
		return selectedResponse;
	}
	
	private MfResponseDetail lmProviderBasedResponseSelection(String lmProvider, List<MfResponseDetail> mfResponses,
			Integer siteId) {

		List<MfResponseDetail> lmMatchList = null;
		
		List<MfResponseDetail> feasibleSites = mfResponses.stream()
				.filter(mfResponseDetail -> mfResponseDetail.getFeasibilityStatus() != null)
				.filter(mfResponseDetail -> mfResponseDetail.getFeasibilityStatus().toUpperCase().startsWith(FEASIBLE))
				.collect(Collectors.toList());
		
		if(lmProvider!=null) {
			if (lmProvider.toUpperCase().contains("MAN")) {
				lmMatchList = feasibleSites.stream().filter(z -> z.getProvider() != null)
						.filter(y -> y.getProvider().toUpperCase().contains(lmProvider.toUpperCase()))
						.collect(Collectors.toList());
			} else {
				lmMatchList = feasibleSites.stream().filter(z -> z.getProvider() != null)
						.filter(y -> y.getProvider().equalsIgnoreCase(lmProvider)).collect(Collectors.toList());
			}
		}
		

		MfResponseDetail selectedResponse = null;
		MfResponseDetail lmBasedResponse =null;
		if (!CollectionUtils.isEmpty(lmMatchList)) {
			lmBasedResponse =lmMatchList.size() == 1 ?  saveIsSelectedForSelectedMfResponse(lmMatchList.get(0)) : segregateResponse(siteId, lmMatchList);
			
		}
		
		selectedResponse = lmBasedResponse != null ?lmBasedResponse : segregateResponse(siteId, feasibleSites);
		
		return selectedResponse;
	}

	private MfResponseDetail segregateResponse(Integer siteId, List<MfResponseDetail> mfResponsDetails) {
		if (!CollectionUtils.isEmpty(mfResponsDetails)) {
			MfResponseDetail selectedMfResponseDetail = filterResponsesBasedOnStatusAndRankAndFeasibleMode(
					mfResponsDetails);
			return saveIsSelectedForSelectedMfResponse(selectedMfResponseDetail);
		} else {
			LOGGER.info("No MF response found for site {} ", siteId);
			return null;
		}
	}

	/**
	 * Method to filter responses based on status and rank and feaisble Model
	 *
	 * @param mfResponsDetails
	 * @return {@link MfResponseDetail}
	 */
	protected MfResponseDetail filterResponsesBasedOnStatusAndRankAndFeasibleMode(
			List<MfResponseDetail> mfResponsDetails) {

		LOGGER.info("Filtering mfResponse details based on rank and feasible mode");
		List<MfResponseDetail> feasibleSites = mfResponsDetails.stream()
				.filter(mfResponseDetail -> mfResponseDetail.getFeasibilityStatus() != null)
				.filter(mfResponseDetail -> mfResponseDetail.getFeasibilityStatus().toUpperCase().startsWith(FEASIBLE))
				.collect(Collectors.toList());

		if (!CollectionUtils.isEmpty(feasibleSites)) {
			List<MfResponseDetail> mfResponseDetailsWithRanks = getRankForMfResponses(feasibleSites);

			Map<String, List<MfResponseDetail>> groupResponsesByFeasibleMode = mfResponseDetailsWithRanks.stream()
					.collect(Collectors.groupingBy(mfResponseDetail -> mfResponseDetail.getFeasibilityMode()));
			List<MfResponseDetail> onnetResponses = getResponsesByFeasibleMode(groupResponsesByFeasibleMode, ONNET);
			List<MfResponseDetail> offnetResponses = getResponsesByFeasibleMode(groupResponsesByFeasibleMode, OFFNET);

			return getBestResponseBasedOnPriorityMatrix(onnetResponses, offnetResponses);
		} else {
			LOGGER.info("No responses have feasible status as feasible");
			return null;
		}

	}

	/**
	 * 
	 * This function is used to delete response Details
	 * 
	 * @param <CreateResponseBean>
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public <createResponseBean> void deleteResponse(Integer rowId, Integer taskID) throws TclCommonException {
		LOGGER.info("delete response from DB");
		// TODO Audit entry before delete...
		mfResponseDetailRepository.deleteByIdAndTaskId(rowId, taskID);
	}

	/**
	 * Get onnet or offnet response based on feasible mode
	 *
	 * @param groupResponsesByFeasibleMode
	 * @param feasibleMode
	 *
	 * @return {@link List<MfResponseDetail>}
	 */
	private List<MfResponseDetail> getResponsesByFeasibleMode(
			Map<String, List<MfResponseDetail>> groupResponsesByFeasibleMode, String feasibleMode) {
		List<String> feasibleModes = groupResponsesByFeasibleMode.keySet().stream()
				.filter(s -> s.toLowerCase().contains(feasibleMode.toLowerCase())).distinct()
				.collect(Collectors.toList());
		if (!CollectionUtils.isEmpty(feasibleModes)) {
			return feasibleModes.stream().map(key -> groupResponsesByFeasibleMode.get(key)).flatMap(List::stream)
					.collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

	/**
	 * Method to get best response based on priority matrix
	 * 
	 * @param onnetResponses
	 * @param offnetResponses
	 *
	 * @return {@link MfResponseDetail}
	 */
	private MfResponseDetail getBestResponseBasedOnPriorityMatrix(List<MfResponseDetail> onnetResponses,
			List<MfResponseDetail> offnetResponses) {
		if (!CollectionUtils.isEmpty(onnetResponses)) {
			LOGGER.info("Selecting the Response based on priority matrix having onnetResponse of size {} ",
					onnetResponses.size());
			return getBestOfOnnet(onnetResponses);
		} else if (!CollectionUtils.isEmpty(offnetResponses) && CollectionUtils.isEmpty(onnetResponses)) {
			LOGGER.info("Selecting the Response based on priority matrix having offnetResponse of size {} ",
					offnetResponses.size());
			return getBestOfOffnet(offnetResponses);
		} else {
			LOGGER.info("Site types are neither onnet nor offnet");
			return null;
		}
	}

	/**
	 * Get best of Offnet responses
	 *
	 * @param offnetResponses
	 * @return {@link MfResponseDetail}
	 */
	private MfResponseDetail getBestOfOffnet(List<MfResponseDetail> offnetResponses) {
		Map<Double, List<MfResponseDetail>> costMfResponseDetailsMap = mapOffnetResponseDetailByCostBasedOnFeasibilityMode(
				offnetResponses);
		double minCost = costMfResponseDetailsMap.keySet().stream().mapToDouble(Double::doubleValue).min()
				.getAsDouble();
		return costMfResponseDetailsMap.get(minCost).stream().findFirst().get();
	}

	/**
	 * Group by feasibility mode and get the cost
	 *
	 * @param offnetResponses
	 * @return {@link Map}
	 */
	private Map<Double, List<MfResponseDetail>> mapOffnetResponseDetailByCostBasedOnFeasibilityMode(
			List<MfResponseDetail> offnetResponses) {
		Map<Double, List<MfResponseDetail>> costMfResponseDetailsMap = new HashMap<>();

		List<MfResponseDetail> offnetWirelineResponses = offnetResponses.stream()
				.filter(mfResponseDetail -> mfResponseDetail.getFeasibilityMode().equalsIgnoreCase(OFFNET_WIRELINE))
				.collect(Collectors.toList());
		offnetWirelineResponses.forEach(mfResponseDetail -> {
			Map<String, Object> responseMap = getMapByString(mfResponseDetail);
			Double cost = calculateOffnetWirelineCost(responseMap);
			costMfResponseDetailsMap.computeIfAbsent(cost, k -> new ArrayList<>()).add(mfResponseDetail);
		});

		List<MfResponseDetail> offnetWirelessResponses = offnetResponses.stream()
				.filter(mfResponseDetail -> mfResponseDetail.getFeasibilityMode().equalsIgnoreCase(OFFNET_WIRELESS))
				.collect(Collectors.toList());
		offnetWirelessResponses.forEach(mfResponseDetail -> {
			Map<String, Object> responseMap = getMapByString(mfResponseDetail);
			Double cost = calculateOffnetWirelessCost(responseMap);
			costMfResponseDetailsMap.computeIfAbsent(cost, k -> new ArrayList<>()).add(mfResponseDetail);
		});
		return costMfResponseDetailsMap;
	}

	/**
	 * Get Map by Json
	 *
	 * @param mfResponseDetail
	 * @return {@link Map}
	 */
	private Map<String, Object> getMapByString(MfResponseDetail mfResponseDetail) {
		String responseJson = mfResponseDetail.getCreateResponseJson();
		return (Map<String, Object>) Utils.convertJsonStingToJson(responseJson);
	}

	/**
	 * Calculate the offnetwireless cost
	 *
	 * @param responseMap
	 * @return {@link Double}
	 */
	private Double calculateOffnetWirelessCost(Map<String, Object> responseMap) {
		Double nrc = getCharges(responseMap.get(LM_NRC_BW_PROV_OFRF));
		Double mast = getCharges(responseMap.get(LM_NRC_MAST_OFRF));
		Double arc = getCharges(responseMap.get(LM_ARC_BW_PROV_OFRF));
		return (nrc + mast + arc);
	}

	/**
	 * Calculate the offnet wirline cost
	 *
	 * @param responseMap
	 * @return {@link Double}
	 */
	private Double calculateOffnetWirelineCost(Map<String, Object> responseMap) {
		Double nrc = getCharges(responseMap.get(LM_NRC_BW_PROV_OFRF));
		Double otcModemCharges = getCharges(responseMap.get(OTC_MODEM_CHARGES));
		Double arcModemCharges = getCharges(responseMap.get(ARC_MODEM_CHARGES));
		Double arcbw = getCharges(responseMap.get(ARC_BW));
		return (nrc + otcModemCharges + arcModemCharges + arcbw);
	}

	/**
	 * Get best of Onnet responses
	 *
	 * @param onnetResponses
	 * @return {@link MfResponseDetail}
	 */
	private MfResponseDetail getBestOfOnnet(List<MfResponseDetail> onnetResponses) {
		/*int minRankOfOnnet = getMinRankOfResponses(onnetResponses);
		return onnetResponses.stream().filter(mfResponseDetail -> mfResponseDetail.getMfRank().equals(minRankOfOnnet))
				.findFirst().get();*/
		// PIPF-360 - get latest created respnse based on updated time
		return  onnetResponses.stream().sorted(Comparator.comparing(MfResponseDetail::getCreatedTime).reversed()).findFirst().orElse(null);
	}

	/**
	 * Get minimum rank of responses
	 *
	 * @param responseDetails
	 * @return {@link int}
	 */
	private int getMinRankOfResponses(List<MfResponseDetail> responseDetails) {
		Map<Integer, List<MfResponseDetail>> groupResponseByRank = responseDetails.stream()
				.collect(Collectors.groupingBy(mfResponseDetail -> mfResponseDetail.getMfRank()));
		return groupResponseByRank.keySet().stream().mapToInt(Integer::intValue).min().getAsInt();
	}

	/**
	 * Get Rank of MF response
	 *
	 * @param mfResponsDetails
	 * @return {@link List}
	 */
	private List<MfResponseDetail> getRankForMfResponses(List<MfResponseDetail> mfResponseDetails) {

		// List<MfResponseDetail> primaryList = new ArrayList<MfResponseDetail>();
		// List<MfResponseDetail> secondaryList = new ArrayList<MfResponseDetail>();

		return mfResponseDetails.stream().map(mfResponseDetail -> {
			Map<String, Object> responseMap = getMapByString(mfResponseDetail);
			String provider = (String)responseMap.get("provider_name");
			Double bandwidth = getBandwidth(responseMap);
			String lastMile = (String) responseMap.get(ORCH_LM_TYPE);
			String category = (String) responseMap.get(ORCH_CATEGORY);
			if(StringUtils.isNotEmpty(provider)) {
				if(provider.toLowerCase().contains("radwin") && provider.equalsIgnoreCase("Radwin from TCL POP"))
					category = "UBR P2P - Onnet";
				else if(provider.toLowerCase().contains("radwin"))
					category = "UBR P2P - offnet + Colo";
			}
			String connectionType = (String) responseMap.get(ORCH_CONNECTION);
			int rank = getRank(bandwidth.intValue(), lastMile, connectionType, category);
			return saveRankForMfResponse(mfResponseDetail, rank);
		}).collect(Collectors.toList());

		/*
		 * primaryList = mfResponseDetails.stream().filter(x ->
		 * x.getType().equals("primary")).map(mfResponseDetail -> { Map<String, Object>
		 * responseMap = getMapByString(mfResponseDetail); Double bandwidth =
		 * getBandwidth(responseMap); String lastMile = (String)
		 * responseMap.get(ORCH_LM_TYPE); String category = (String)
		 * responseMap.get(ORCH_CATEGORY); String connectionType = (String)
		 * responseMap.get(ORCH_CONNECTION); int rank = getRank(bandwidth.intValue(),
		 * lastMile, connectionType, category); rank =10; return
		 * saveRankForMfResponse(mfResponseDetail, rank);
		 * }).collect(Collectors.toList());
		 * 
		 * 
		 * primaryList.addAll(secondaryList);
		 * 
		 * return primaryList;
		 */

	}

	/**
	 * Get bandwidth
	 *
	 * @param responseMap
	 * @return {@link Double}
	 */
	private Double getBandwidth(Map<String, Object> responseMap) {
		Double bandwidth = 0.0D;
		if (responseMap.get(BW_MBPS) instanceof Double) {
			bandwidth = (Double) responseMap.get(BW_MBPS);
		} else if (responseMap.get(BW_MBPS) instanceof Long) {
			bandwidth = new Double((Long) responseMap.get(BW_MBPS));
		} else if (responseMap.get(BW_MBPS) instanceof Integer) {
			bandwidth = new Double((Integer) responseMap.get(BW_MBPS));
		}
		return bandwidth;
	}

	/**
	 * Get Charges
	 *
	 * @param double value
	 * @return {@link Double}
	 */
	private Double getCharges(Object charge) {
		Double mfCharge = 0.0D;
		if (charge != null) {
			if (charge instanceof Double) {
				mfCharge = (Double) charge;
			} else if (charge instanceof String && !charge.equals("")) {
				mfCharge = new Double((String) charge);
			} else if (charge instanceof Long) {
				mfCharge = new Double((Long) charge);
			} else if (charge instanceof Integer) {
				mfCharge = new Double((Integer) charge);
			}
		}
		return mfCharge;
	}

	/**
	 * Save the rank of particular mf response
	 *
	 * @param mfResponseDetail
	 * @param rank
	 * @return {@link MfResponseDetail}
	 */
	private MfResponseDetail saveRankForMfResponse(MfResponseDetail mfResponseDetail, int rank) {
		mfResponseDetail.setMfRank(rank);
		return mfResponseDetailRepository.save(mfResponseDetail);
	}

	/**
	 * Saves selected Mf Response detail with enabled Is Selected
	 *
	 * @param selectedMfResponseDetail
	 * @return {@link MfResponseDetail}
	 */
	protected MfResponseDetail saveIsSelectedForSelectedMfResponse(MfResponseDetail selectedMfResponseDetail) {
		if (Objects.nonNull(selectedMfResponseDetail)) {
			LOGGER.info("Selected MF Response details for site {} is {}", selectedMfResponseDetail.getSiteId(),
					selectedMfResponseDetail.getId());
			selectedMfResponseDetail.setIsSelected(1);
			return mfResponseDetailRepository.save(selectedMfResponseDetail);
		}
		return null;
	}

	/**
	 * 
	 * This function is used to fetch response Details for given taskID
	 * 
	 * @param <CreateResponseBean>
	 * @return
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public List<MfResponseDetail> fetchResponse(Integer siteId) throws TclCommonException {
		LOGGER.info("Fetch response from DB");
		List<MfResponseDetail> recordInDB= mfResponseDetailRepository.findBySiteId(siteId);
        return processMfResponseDetailList(recordInDB);
		
		
	}

	/**
	 * Method to process MFResponse Detail
	 * 
	 * @author krutsrin
	 * @param recordInDB
	 * @return list of processed MFResponseDetail
	 */
	private List<MfResponseDetail> processMfResponseDetailList(List<MfResponseDetail> recordInDB) {
		recordInDB = recordInDB.stream().filter(p -> p.getCreateResponseJson() != null).map(x -> {
			FetchResponseBean frBean = new FetchResponseBean();

			frBean.setId(x.getId());
			frBean.setTaskId(x.getTaskId());
			frBean.setSiteId(x.getSiteId());
			frBean.setProvider(x.getProvider());
			frBean.setCreateResponseJson(x.getCreateResponseJson());
			frBean.setCreatedBy(x.getCreatedBy());
			frBean.setCreatedTime(x.getCreatedTime());
			frBean.setUpdatedBy(x.getUpdatedBy());
			frBean.setUpdatedTime(x.getUpdatedTime());
			frBean.setType(x.getType());
			frBean.setFeasibilityMode(x.getFeasibilityMode());
			frBean.setMfRank(x.getMfRank());
			frBean.setIsSelected(x.getIsSelected());
			frBean.setFeasibilityStatus(x.getFeasibilityStatus());
			frBean.setFeasibilityCheck(x.getFeasibilityCheck());
			frBean.setFeasibilityType(x.getFeasibilityType());
			frBean.setQuoteId(x.getQuoteId());

			JSONObject dataEnvelopeObj = null;
			JSONParser jsonParser = new JSONParser();
			try {
				if (x.getCreateResponseJson() != null) {
					dataEnvelopeObj = (JSONObject) jsonParser.parse(x.getCreateResponseJson());
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
			if (x.getFeasibilityType() != null && dataEnvelopeObj != null && !dataEnvelopeObj.isEmpty()) {

				chargesCalculation(x, dataEnvelopeObj);
			}

			return x;
		}).collect(Collectors.toList());
		return recordInDB;
	}

	private void chargesCalculation(MfResponseDetail x, JSONObject dataEnvelopeObj) {
		// ================================================================================
		// Offnet Wireline
		// ================================================================================

		if (x.getFeasibilityType().equals("Offnet Wireline")) {

			// otc charges =OTC Modem Charges +OTC/NRC - Installation
			if (dataEnvelopeObj.get("lm_otc_modem_charges_offwl") != null
					|| dataEnvelopeObj.get("lm_otc_nrc_installation_offwl") != null) {
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_otc_modem_charges_offwl"))+
						+ getCharges( dataEnvelopeObj.get("lm_otc_nrc_installation_offwl"));
				x.setOtcTotal(otcCharges);
			}
			// arc charges = ARC - BW + arc _modem charges
			if (dataEnvelopeObj.get("lm_arc_modem_charges_offwl") != null || dataEnvelopeObj.get("lm_arc_bw_offwl") != null) {
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_modem_charges_offwl"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_bw_offwl")));
			}

			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
		// ================================================================================
		// offnet wireless
		// ================================================================================

		else if (x.getFeasibilityType().equals("Offnet Wireless")) {

			if (dataEnvelopeObj.get("lm_nrc_mast_ofrf") != null || dataEnvelopeObj.get("lm_nrc_bw_prov_ofrf") != null) {

				// otc charges = Mast Charges + OTC/NRC - Installation
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_mast_ofrf"))
						+ +getCharges(dataEnvelopeObj.get("lm_nrc_bw_prov_ofrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("lm_arc_bw_prov_ofrf") != null) {
				// arc charges = ARC - BW + ARC Modem Charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_bw_prov_ofrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}

		// ================================================================================
		// MAN VBL
		// ================================================================================

		else if (x.getFeasibilityType().equals("MAN/VBL")) {

			if (dataEnvelopeObj.get("lm_nrc_bw_onwl") != null || dataEnvelopeObj.get("lm_nrc_prow_onwl") != null) {

				// otc charges =OTC/NRC - Installation + prov value otc
				Double otcinstal = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onwl"));
				Double prov = getCharges(dataEnvelopeObj.get("lm_nrc_prow_onwl"));
				x.setOtcTotal(otcinstal + prov);
			}
			if (dataEnvelopeObj.get("lm_nrc_nerental_onwl") != null || dataEnvelopeObj.get("lm_arc_bw_onwl") != null
					|| dataEnvelopeObj.get("lm_arc_prow_onwl") != null) {
				// arc charges = ARC - LRC/NE Rental + ARC - BW + PROW value ARC
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_nrc_nerental_onwl"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_bw_onwl"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_prow_onwl")));
			}
			// network capex

			if (dataEnvelopeObj.get("network_capex") != null) {
				x.setNetworkCapex(getCharges(dataEnvelopeObj.get("network_capex")));

			}
			// capex calculation - Man VBL
			if (dataEnvelopeObj.get("lm_nrc_ospcapex_onwl") != null || dataEnvelopeObj.get("capex_in_building") != null
					|| dataEnvelopeObj.get("lm_nrc_mux_onwl") != null) {

				x.setCapexTotal(getCharges(dataEnvelopeObj.get("lm_nrc_ospcapex_onwl"))
						+ getCharges(dataEnvelopeObj.get("capex_in_building"))
						+ getCharges(dataEnvelopeObj.get("lm_nrc_mux_onwl")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
		// ================================================================================
		// RADWIN
		// ================================================================================

		else if (x.getFeasibilityType().equals("RADWIN")) {
			if (dataEnvelopeObj.get("lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("lm_nrc_mast_onrf") != null) {

				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("lm_arc_bw_onrf") != null || dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf") != null
					|| dataEnvelopeObj.get("lm_arc_converter_charges_onrf") != null
					|| dataEnvelopeObj.get("lm_arc_colocation_onrf") != null) {
				// arc charges = ARC-Radwin(BW) + ARC - BW + ARC convertor charges + colocation
				// charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_colocation_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}

		// ================================================================================
		// UBR PMP / WiMax
		// ================================================================================

		else if (x.getFeasibilityType().equals("UBR PMP/WiMax")) {

			if (dataEnvelopeObj.get("lm_nrc_bw_onrf") != null || dataEnvelopeObj.get("lm_nrc_mast_onrf") != null) {
				// otc charges =OTC/NRC - Installation + Mast Charges
				Double otcCharges = getCharges(dataEnvelopeObj.get("lm_nrc_bw_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_nrc_mast_onrf"));
				x.setOtcTotal(otcCharges);
			}
			if (dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf") != null || dataEnvelopeObj.get("lm_arc_converter_charges_onrf") != null) {
				// arc charges = ARC - BW + arc convertor charges
				x.setArcTotal(getCharges(dataEnvelopeObj.get("lm_arc_bw_backhaul_onrf"))
						+ getCharges(dataEnvelopeObj.get("lm_arc_converter_charges_onrf")));
			}
			x.setTotalCharges(x.getArcTotal() + x.getOtcTotal());
		}
	}

	/**
	 * Method to get task details for the given task id
	 * 
	 * @param taskId
	 * @return
	 * @throws TclCommonException
	 */
	public MfTaskDataBean getTaskDetails(Integer taskId) throws TclCommonException {
		LOGGER.info("Inside getTaskDetails method to fetch task details for the taskId {} ", taskId);
		MfTaskDataBean mfTaskDataBean = new MfTaskDataBean();
		try {
			Optional<Task> taskOpt = taskRepository.findById(taskId);
			if (taskOpt.isPresent()) {
				MfTaskDetail mfTaskDetails = mfTaskDetailRepository.findByTaskId(taskId);
				Task task = taskOpt.get();
				mfTaskDataBean.setTaskId(task.getId());
				mfTaskDataBean.setCreationDate(task.getCreatedTime());
				mfTaskDataBean.setAssignedTo(task.getTaskAssignments().stream().findFirst().get().getGroupName());
				if (task.getMfDetail().getMfDetails() != null) {
					LOGGER.info("Inside getTaskDetails method to fetch mf details");
					String[] siteLmType = task.getMfDetail().getSiteType().split("-");
					MfDetailAttributes mfdetailAttr = Utils.convertJsonToObject(task.getMfDetail().getMfDetails(),
							MfDetailAttributes.class);
					mfTaskDataBean.setCopfId(mfdetailAttr.getMacdServiceId());
					mfTaskDataBean.setProductType(task.getServiceType());
					mfTaskDataBean.setOrderType(mfdetailAttr.getQuoteType());
					mfTaskDataBean.setProductSubType(mfdetailAttr.getQuoteCategory());
					mfTaskDataBean.setFeasibilityCoordinator(task.getMfDetail().getCreatedBy());
					mfTaskDataBean.setQuoteCreatedUserType(task.getMfDetail().getQuoteCreatedUserType());
					mfTaskDataBean.setOpportunityAccountName(mfdetailAttr.getOpportunityAccountName());
					mfTaskDataBean.setCustomerSegment(mfdetailAttr.getCustomerSegment());
					mfTaskDataBean.setOpportunityOwnerEmail(mfTaskDataBean.getOpportunityOwnerEmail());
					mfTaskDataBean.setOpportunityStage(mfdetailAttr.getOpportunityStage());
					mfTaskDataBean.setAddressLineOne(mfdetailAttr.getAddressLineOne());
					mfTaskDataBean.setAddressLineTwo(mfdetailAttr.getAddressLineTwo());
					mfTaskDataBean.setCity(mfdetailAttr.getCity());
					mfTaskDataBean.setState(mfdetailAttr.getState());
					mfTaskDataBean.setLatLong(mfdetailAttr.getLatLong());
					mfTaskDataBean.setPincode(mfdetailAttr.getPincode());
					mfTaskDataBean.setLocalLoopInterface(mfdetailAttr.getLocalLoopInterface());
					mfTaskDataBean.setFeasibilityId(task.getFeasibilityId());
					mfTaskDataBean.setSiteId(task.getMfDetail().getSiteId());
					mfTaskDataBean.setSiteCode(task.getSiteCode());
					mfTaskDataBean.setSiteType(siteLmType.length > 1 ? siteLmType[1] : "primary");
					mfTaskDataBean.setLmType(siteLmType[0]);
					mfTaskDataBean.setPortCapacity(mfdetailAttr.getPortCapacity());
					mfTaskDataBean.setLocalLoopCapacity(mfdetailAttr.getLocalLoopBandwidth());
					mfTaskDataBean.setQuoteStage(mfdetailAttr.getQuoteStage());
					mfTaskDataBean.setQuoteType(mfdetailAttr.getQuoteType());
					mfTaskDataBean.setQuoteCategory(mfdetailAttr.getQuoteCategory());
					mfTaskDataBean.setQuoteId(task.getMfDetail().getQuoteId());
					mfTaskDataBean.setQuoteLeId(task.getMfDetail().getQuoteLeId());
					mfTaskDataBean.setLocality(mfdetailAttr.getLocality());

					// For NPL
					mfTaskDataBean.setMfLinkType(mfdetailAttr.getMfLinkType());
					mfTaskDataBean.setMfLinkEndType(mfdetailAttr.getMfLinkEndType());
					mfTaskDataBean.setLinkId(mfdetailAttr.getLinkId());
					
					mfTaskDataBean.setAddressLineOneSiteA(mfdetailAttr.getAddressLineOneSiteA());
					mfTaskDataBean.setAddressLineTwoSiteA(mfdetailAttr.getAddressLineTwoSiteA());
					mfTaskDataBean.setCitySiteA(mfdetailAttr.getCitySiteA());
					mfTaskDataBean.setLatLongSiteA(mfdetailAttr.getLatLongSiteA());
					mfTaskDataBean.setStateSiteA(mfdetailAttr.getStateSiteA());
					mfTaskDataBean.setPincodeSiteA(mfdetailAttr.getPincodeSiteA());
					mfTaskDataBean.setCountrySiteA(mfdetailAttr.getCountrySiteA());
					mfTaskDataBean.setLocalitySiteA(mfdetailAttr.getLocalitySiteA());

				
					mfTaskDataBean.setAddressLineOneSiteB(mfdetailAttr.getAddressLineOneSiteB());
					mfTaskDataBean.setAddressLineTwoSiteB(mfdetailAttr.getAddressLineTwoSiteB());
					mfTaskDataBean.setCitySiteB(mfdetailAttr.getCitySiteB());
					mfTaskDataBean.setLatLongSiteB(mfdetailAttr.getLatLongSiteB());
					mfTaskDataBean.setStateSiteB(mfdetailAttr.getStateSiteB());
					mfTaskDataBean.setPincodeSiteB(mfdetailAttr.getPincodeSiteB());
					mfTaskDataBean.setCountrySiteB(mfdetailAttr.getCountrySiteB());
					mfTaskDataBean.setLocalitySiteB(mfdetailAttr.getLocalitySiteB());
					
					mfTaskDataBean.setaEndlocalLoopInterface(mfdetailAttr.getaEndlocalLoopInterface());
					mfTaskDataBean.setbEndlocalLoopInterface(mfdetailAttr.getbEndlocalLoopInterface());
					LOGGER.info("Local loop Interface for AEnd {} and BEnd {} ",
							mfdetailAttr.getaEndlocalLoopInterface(), mfdetailAttr.getbEndlocalLoopInterface());

					mfTaskDataBean.setaEndLocalLoopBandwidth(mfdetailAttr.getaEndLocalLoopBandwidth());
					mfTaskDataBean.setbEndLocalLoopBandwidth(mfdetailAttr.getbEndLocalLoopBandwidth());
					
					LOGGER.info("Local loop Bandwidth for AEnd {} and BEnd {} ",
							mfdetailAttr.getaEndLocalLoopBandwidth(),mfdetailAttr.getbEndLocalLoopBandwidth());
					
					mfTaskDataBean.setSiteGettingShifted(mfdetailAttr.getSiteGettingShifted());
					LOGGER.info("SiteGettingShifted {} ",mfdetailAttr.getSiteGettingShifted());		
					
					mfTaskDataBean.setaEndLMProvider(mfdetailAttr.getaEndLMProvider());
					LOGGER.info("aEndLMProvider {} ",mfdetailAttr.getaEndLMProvider());	
					
					mfTaskDataBean.setbEndLMProvider(mfdetailAttr.getbEndLMProvider());
					LOGGER.info("bEndLMProvider {} ",mfdetailAttr.getbEndLMProvider());	

					mfTaskDataBean.setMf3DSiteType(task.getMfDetail().getSiteType());	
					if (task.getMfDetail().getIsPreMfTask() != null) {
						if (task.getMfDetail().getIsPreMfTask().equalsIgnoreCase("1")) {
							LOGGER.info("entered into 3d maps block");
							mfTaskDataBean.setIs3DTask(true);
							mfTaskDataBean.setLmType("Single");
							mfTaskDataBean.setSiteType("primary");
							mfTaskDataBean.setBandwidth(mfdetailAttr.getBandwidth());
							
						}
					}
				}
				persistProcessTaskLog(mfTaskDataBean, task);
				mfTaskDataBean.setAssignedOn(task.getCreatedTime());
				mfTaskDataBean.setTaskCloseDate(task.getCompletedTime());
				mfTaskDataBean.setCreationDate(task.getClaimTime());
				mfTaskDataBean.setLastModifiedBy(task.getUpdatedTime());
				mfTaskDataBean.setTaskStatus(task.getMstStatus().getCode());
				mfTaskDataBean.setQuoteCode(task.getQuoteCode());
				// get prv data task id

				List<Task> tasks = taskRepository.findByMfDetail(task.getMfDetail());
				Optional<Task> prvTaskOpt = tasks.stream()
						.filter(prvTask -> prvTask.getMstTaskDef().getKey().equalsIgnoreCase("manual_feasibility_prv"))
						.findFirst();
				if (prvTaskOpt.isPresent())
					mfTaskDataBean.setPrvTaskId(prvTaskOpt.get().getFeasibilityId());
				processMfTaskDetail(mfTaskDataBean, mfTaskDetails);

			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return mfTaskDataBean;
	}

	public static class Context {
		Integer rowCount = 0;
		Integer cellCount = 0;
		Row row;
	}

	/**
	 * Method to generate excel sheet.
	 * 
	 * @param assignedGroupingList
	 * @param groupName
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	public HttpServletResponse getExcel(List<AssignedGroupingBean> assignedGroupingList, HttpServletResponse response)
			throws TclCommonException {
		String[] columns = { "QUOTE CODE", "TASK ID", "SUBJECT", "ASSIGNED ON", "PRODUCT NAME", "ORDER TYPE",
				" PRODUCT SUB TYPE", "LOCAL LOOP CAPACITY", "PORT/CIRCUIT CAPACITY", "INTERFACE", "STATUS",
				"SITE CONTACT NAME (A End)", "SALES REMARKS", "CUSTOMER ADDRESS", "CITY", "STATE", "PINCODE",
				"LATITUDE", "LONGITUDE", "LAST MILE CONTRACT TERM", "TASK RELATED TO", "OPPURTUNITY OWNER EMAIL",
				"CUSTOMER SEGMENT", "OPPURTUNITY ACCOUNT NAME", "OPPURTUNITY STAGE" };
		ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();

		XSSFWorkbook workbook = new XSSFWorkbook();
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		headerFont.setFontHeightInPoints((short) 12);
		CellStyle headerCellStyle = workbook.createCellStyle();
		headerCellStyle.setFont(headerFont);

		assignedGroupingList.forEach(nextBean -> {
			Sheet sheet = workbook.createSheet(nextBean.getGroupName());
			Row headerRow = sheet.createRow(0);
			// context class obj for initializing row and rowCount
			Context context = new Context();

			for (int i = 0; i < columns.length; i++) {
				Cell cell = headerRow.createCell(i);
				cell.setCellValue(columns[i]);
				cell.setCellStyle(headerCellStyle);
			}

			// Resize all columns to fit the content size
			for (int i = 0; i < columns.length; i++) {
				sheet.autoSizeColumn(i);
			}
			nextBean.getTaskGroup().forEach(y -> y.getTaskBeans().forEach(z -> {
				context.rowCount++;
				context.row = sheet.createRow(context.rowCount);
				if (z.getQuoteId() != null) {
					context.row.createCell(0).setCellValue(z.getQuoteCode());
				}
				if (z.getFeasibilityId() != null) {
					context.row.createCell(1).setCellValue(z.getFeasibilityId());

				}
				if (z.getMfTaskDetailBean() != null && z.getMfTaskDetailBean().getSubject() != null) {
					context.row.createCell(2).setCellValue(z.getMfTaskDetailBean().getSubject());

				}
				// Assigned ON
				if (z.getCreatedTime() != null) {
					Date dateStr = z.getCreatedTime();
					DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					context.row.createCell(3).setCellValue(formatter.format(dateStr));
				}

				// product name
				if (z.getServiceType() != null) {
					context.row.createCell(4).setCellValue(z.getServiceType());
				}

				// order type
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getQuoteType() != null) {
					context.row.createCell(5).setCellValue(z.getMfDetail().getMfDetails().getQuoteType());
				}

				// product sub type
				context.row.createCell(6).setCellValue("NA");

				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getLocalLoopBandwidth() != null) {
					context.row.createCell(7)
							.setCellValue(z.getMfDetail().getMfDetails().getLocalLoopBandwidth() + " Mbps");
				}
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getPortCapacity() != null) {
					context.row.createCell(8).setCellValue(z.getMfDetail().getMfDetails().getPortCapacity() + " Mbps");
				}

				// Interface
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getMfInterface() != null) {
					context.row.createCell(9).setCellValue(z.getMfDetail().getMfDetails().getMfInterface());
				}

				// status
				if (z.getStatus() != null) {
					context.row.createCell(10).setCellValue(z.getStatus());
				}

				// site contact name A END
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getLconName() != null
						&& z.getMfDetail().getMfDetails().getLconContactNum() != null) {

					context.row.createCell(11).setCellValue(z.getMfDetail().getMfDetails().getLconName() + " "
							+ z.getMfDetail().getMfDetails().getLconContactNum());
				}

				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getLconSalesRemarks() != null) {
					context.row.createCell(12).setCellValue(z.getMfDetail().getMfDetails().getLconSalesRemarks());
				}

				// customer Address
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getAddressLineOne() != null) {
					context.row.createCell(13).setCellValue(z.getMfDetail().getMfDetails().getAddressLineOne());
				}
				// city
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getCity() != null) {
					context.row.createCell(14).setCellValue(z.getMfDetail().getMfDetails().getCity());
				}
				// state
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getState() != null) {
					context.row.createCell(15).setCellValue(z.getMfDetail().getMfDetails().getState());
				}

				// pincode
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getPincode() != null) {
					context.row.createCell(16).setCellValue(z.getMfDetail().getMfDetails().getPincode());
				}
				// latitude and longitude

				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getLatLong() != null) {
					String[] values = z.getMfDetail().getMfDetails().getLatLong().split(",");
					if (values != null && values.length > 0) {
						context.row.createCell(17).setCellValue(values[0]);
						context.row.createCell(18).setCellValue(values[1]);
					}
				}

				// LAST MILE CONTRACT TERM
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getLastMileContractTerm() != null) {
					context.row.createCell(19).setCellValue(z.getMfDetail().getMfDetails().getLastMileContractTerm());
				}

				// task related to
				context.row.createCell(20).setCellValue("A End");

				// Opportunity Owner email
				if (z.getTaskAssignments() != null && !z.getTaskAssignments().isEmpty()) {
					context.row.createCell(21).setCellValue(z.getTaskAssignments().get(0).getUserName());
				}
				// customer segment
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getCustomerSegment() != null) {
					context.row.createCell(22).setCellValue(z.getMfDetail().getMfDetails().getCustomerSegment());
				}
				// Opportunity account name
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getOpportunityAccountName() != null) {
					context.row.createCell(23).setCellValue(z.getMfDetail().getMfDetails().getOpportunityAccountName());
				}
				// Opportunity Stage
				if (z.getMfDetail() != null && z.getMfDetail().getMfDetails() != null
						&& z.getMfDetail().getMfDetails().getOpportunityStage() != null) {
					context.row.createCell(24).setCellValue(z.getMfDetail().getMfDetails().getOpportunityStage());
				}
			}));
		});

		generateExcel(response, "TaskDetails", outByteStream, workbook);

		return response;
	}

	private void processMfTaskDetail(MfTaskDataBean mfTaskDataBean, MfTaskDetail mfTaskDetails) {
		try {
			if (mfTaskDetails != null) {
				LOGGER.info("Inside processMfTaskDetail method to fetch MfTaskDetail ");
				mfTaskDataBean.setRequestorComments(mfTaskDetails.getRequestorComments());
				mfTaskDataBean.setResponderComments(mfTaskDetails.getResponderComments());
				mfTaskDataBean.setReason(mfTaskDetails.getReason());
				mfTaskDataBean.setFeasibilityStatus(mfTaskDetails.getStatus());
				Optional<Task> requestorTaskOpt = Optional.empty();
				if(mfTaskDetails.getRequestorTaskId()!=null)
					requestorTaskOpt = taskRepository.findById(mfTaskDetails.getRequestorTaskId());
				if(requestorTaskOpt.isPresent() && !requestorTaskOpt.get().getTaskAssignments().isEmpty()) {
					mfTaskDataBean.setAssignedFrom(requestorTaskOpt.get().getTaskAssignments().stream().findFirst().get().getGroupName());
				}else {
					mfTaskDataBean.setAssignedFrom(mfTaskDetails.getAssignedFrom());
				}
				mfTaskDataBean.setCreatedBy(mfTaskDetails.getAssignedFrom());
				mfTaskDataBean.setSubject(mfTaskDetails.getSubject());
				mfTaskDataBean.setPrvStatus(mfTaskDetails.getPrvStatus());
				mfTaskDataBean.setPrvComments(mfTaskDetails.getPrvComments());
				if (mfTaskDetails.getTaskData() != null) {
					MfTaskData taskData = Utils.convertJsonToObject(mfTaskDetails.getTaskData(), MfTaskData.class);
					mfTaskDataBean.setCustomerLat(taskData.getCustomerLat());
					mfTaskDataBean.setCustomerLong(taskData.getCustomerLong());
					
					// if NPL - AEnd and B End will also come
					mfTaskDataBean.setCustomerLatA(taskData.getCustomerLatA());
					mfTaskDataBean.setCustomerLongA(taskData.getCustomerLongA());
					mfTaskDataBean.setCustomerLatB(taskData.getCustomerLatB());
					mfTaskDataBean.setCustomerLongB(taskData.getCustomerLongB());
					
				}
			}
		} catch (Exception e) {
			LOGGER.error("Exception while constructing MfTaskdetail {} ", e.getMessage());
		}

	}

	private void persistProcessTaskLog(MfTaskDataBean mfTaskDataBean, Task task) {
		List<Integer> logId = new ArrayList<>();
		List<ProcessTaskLog> processLogs = new ArrayList<>();
		task.getProcessTaskLogs().stream().forEach(processLog -> {
			LOGGER.info("Inside processProcessTaskLog method to fetch processtasklog for the taskId ");
			if (processLog.getAction().equals("Claimed")) {
				if (!logId.isEmpty()) {
					if (processLog.getId() < logId.get(0)) {
						logId.add(0, processLog.getId());
						processLogs.add(0, processLog);
					}
				}
				if (logId.isEmpty()) {
					logId.add(0, processLog.getId());
					processLogs.add(0, processLog);
				}

			}
		});
		if (!processLogs.isEmpty()) {
			mfTaskDataBean.setTaskAcknowledgedBy(processLogs.get(0).getActionTo());
		}
	}

	public List<MfPopDataBean> getMfPopData() throws TclCommonException {

		List<MfPopDataBean> mfPopDataBeans = new ArrayList<>();
		try {

			List<MfPopData> mfPopData = mfPopDataRepository.findTop500ByOrderByNameAsc();
			if (mfPopData != null && !mfPopData.isEmpty()) {
				mfPopData.forEach(data -> {
					MfPopDataBean mfPopDataBean = new MfPopDataBean(data);
					LOGGER.info("Adding  MfPopData {}  to list ", mfPopDataBean);
					mfPopDataBeans.add(mfPopDataBean);
				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return mfPopDataBeans;
	}

	public List<MfPopDataBean> getMfPopDataByNameOrCityOrState(String searchValue) throws TclCommonException {
		List<MfPopDataBean> masterList = new ArrayList<MfPopDataBean>();

		List<MfPopDataBean> popDataName = mfPopDataRepository.findByname(searchValue).stream().map(MfPopDataBean::new)
				.collect(Collectors.toList());

		List<MfPopDataBean> popDataCity = mfPopDataRepository.findByCity(searchValue).stream().map(MfPopDataBean::new)
				.collect(Collectors.toList());

		List<MfPopDataBean> popDataState = mfPopDataRepository.findByState(searchValue).stream().map(MfPopDataBean::new)
				.collect(Collectors.toList());

		masterList.addAll(popDataName);
		masterList.addAll(popDataCity);
		masterList.addAll(popDataState);

		Set<MfPopDataBean> set = masterList.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(MfPopDataBean::getId))));
		masterList.clear();
		masterList.addAll(set);

		return masterList;

	}

	public List<MfVendorDataBean> getMfVendorData() throws TclCommonException {

		List<MfVendorDataBean> mfVendorDataBeans = new ArrayList<>();
		try {

			mfVendorDataBeans = mfVendorDataRepository.findVendorByLimit().stream().map(MfVendorDataBean::new)
					.collect(Collectors.toList());
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return mfVendorDataBeans;
	}

	/**
	 * Method to search MF vendor data table by name or provider name
	 * 
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	public Set<MfVendorDataBean> getMfVendorDataByNameOrProvideName(String searchValue) throws TclCommonException {
		List<MfVendorDataBean> masterList = new ArrayList<>();
		try {
			// By vendor
			List<MfVendorDataBean> mfVendorDataVendor = mfVendorDataRepository.findByVendor(searchValue).stream()
					.map(MfVendorDataBean::new).collect(Collectors.toList());
			masterList.addAll(mfVendorDataVendor);

			// By sfdc Provider

			List<MfVendorDataBean> mfVendorDataProvider = mfVendorDataRepository.findBysfdcProvider(searchValue)
					.stream().map(MfVendorDataBean::new).collect(Collectors.toList());
			masterList.addAll(mfVendorDataProvider);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		Set<MfVendorDataBean> set = masterList.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(MfVendorDataBean::getId))));

		return set;

	}

	/**
	 * Method to get Rank based on parameters
	 *
	 * @param bandWidth
	 * @param lastMile
	 * @param connectionType
	 * @param category
	 * @return {@link Integer}
	 */
	private int getRank(int bandWidth, String lastMile, String connectionType, String category) {
		ManualFeasibility manualFeasibility = new ManualFeasibility();
		manualFeasibility.setBandwidth(bandWidth);
		manualFeasibility.setLastMile(lastMile);
		manualFeasibility.setConnectiontype(connectionType);
		manualFeasibility.setCategory(category);
		kieSession.insert(manualFeasibility); //NOSONAR
		kieSession.fireAllRules(); //NOSONAR
		LOGGER.info(manualFeasibility.getRank()
				+ "Manual feasibility Rank is --===----#####################################");
		return manualFeasibility.getRank();
	}

	public List<BtsDataBean> getMfBtsData() throws TclCommonException {
		List<BtsDataBean> btsDataBeanList = new ArrayList<>();
		try {
			List<MfBtsData> mfBtsData = mfBtsDataRepository.findTop500By();
			generateBts(btsDataBeanList, mfBtsData);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		return btsDataBeanList;
	}

	public List<MfProviderDataBean> getMfProviderData() throws TclCommonException {

		List<MfProviderDataBean> mfProviderDataBeanList = new ArrayList<>();
		try {
			List<MfProviderData> mfProviderData = mfProviderDataRepository.findAll();
			if (mfProviderData != null && !mfProviderData.isEmpty()) {
				mfProviderData.forEach(data -> {
					MfProviderDataBean mfProviderDataBean = new MfProviderDataBean();
					mfProviderDataBean.setKey(data.getProviderName());
					mfProviderDataBean.setValue(data.getProviderName());
					mfProviderDataBeanList.add(mfProviderDataBean);
				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return mfProviderDataBeanList;
	}

	/**
	 * This method fetch mfHHData
	 * 
	 * @param quoteId
	 * @param siteId
	 * @return
	 * @throws TclCommonException
	 */
	public List<MfHhDataBean> getHandHoldDetails() throws TclCommonException {

		List<MfHhDataBean> mfHandHoldDataBeans = new ArrayList<>();
		LOGGER.info("Inside getHandHoldDetails method to fetch mfHHData");
		try {
			List<MfHhData> mfHhData = mfHhDataRepository.findTop500ByOrderByNumHh();
			if (!mfHhData.isEmpty()) {
				mfHhData.forEach(data -> {
					MfHhDataBean mfHhDataBean = new MfHhDataBean(data);
					LOGGER.info("Adding  mfHhData to list  {} ");
					mfHandHoldDataBeans.add(mfHhDataBean);
				});
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return mfHandHoldDataBeans;
	}

	private MfDetailAttributes getMfDetailFromQuoteIdAndSiteId(Integer quoteId, Integer siteId)
			throws TclCommonException {
		List<MfDetail> mfDetailsList = mfDetailRepository.findByQuoteIdAndSiteId(quoteId, siteId);
		MfDetailAttributes mfdetailAttr = null;
		String mfDetail = null;
		if (mfDetailsList != null && !mfDetailsList.isEmpty()) {
			mfDetail = mfDetailsList.stream().findFirst().get().getMfDetails();
			mfdetailAttr = Utils.convertJsonToObject(mfDetail, MfDetailAttributes.class);

		}
		return mfdetailAttr;
	}

	/**
	 * Method to fetchResponseBasedOnRequestor
	 * 
	 * @param siteId
	 * @return @List{MfResponseDetail}
	 * @throws TclCommonException
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public List<MfResponseDetail> fetchResponseBasedOnRequestor(Integer taskId, Integer siteId)
			throws TclCommonException {
		LOGGER.info("Inside getTaskDetailsBasedOnRequestor method to get details of the task {} ",
				" Task ID :" + taskId + "SiteId: " + siteId);

		List<MfResponseDetail> mfresponseDetailList = null;
		if (siteId != null && taskId != null) {
			MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findBySiteIdAndTaskId(siteId, taskId);

			List<MfResponseDetail> recordInDB = new ArrayList<MfResponseDetail>();
			if (mfTaskDetail != null) {

				if (mfTaskDetail.getRequestorTaskId() != null && (Utils.getSource().contains("asp")
						&& mfTaskDetail.getAssignedFrom().toLowerCase().contains("afm"))) {
					recordInDB = mfResponseDetailRepository.findBytaskId(mfTaskDetail.getRequestorTaskId());

				} else if (Utils.getSource().contains("afm")) {

					List<MfTaskDetail> result = mfTaskDetailRepository.findByRequestorTaskId(taskId);

					List<Integer> aspTaskList = result.stream().filter(x -> x.getAssignedTo() != null)
							.filter(y -> y.getAssignedTo().toLowerCase().contains("asp")).map(z -> z.getTask().getId())
							.collect(Collectors.toList());

					List<MfResponseDetail> aspResponses = new ArrayList<MfResponseDetail>();
					if (aspTaskList != null && !aspTaskList.isEmpty()) {
						aspTaskList.forEach(x -> {
							aspResponses.addAll(mfResponseDetailRepository.findBytaskId(x));
						});
						recordInDB.addAll(aspResponses);
					}
				}

				if (recordInDB != null && !recordInDB.isEmpty()) {
					mfresponseDetailList = processMfResponseDetailList(recordInDB);
				}

			}

		}
		return mfresponseDetailList;
	}

	public String getRandomNumberForCrammer() {
		return RandomStringUtils.random(5, true, true).toUpperCase() + "#";
	}

	public String getRandomNumberForNetp(String serviceCode, String processInstanceId, String configType,
			String productName) {
		return "RID_" + serviceCode + "#" + processInstanceId + "#" + configType + "#"
				+ DateUtil.convertDateToStringWithTime(new Date()) + productName;
	}

	public List<BtsDataBean> getBtsDataByName(String searchValue) throws TclCommonException {
		List<BtsDataBean> btsDataBeanList = new ArrayList<>();
		List<MfBtsData> mFBtsDataByAddress = new ArrayList<MfBtsData>();
		List<MfBtsData> mFBtsDataByName = new ArrayList<MfBtsData>();
		mFBtsDataByName = mfBtsDataRepository.findByname(searchValue);
		mFBtsDataByAddress = mfBtsDataRepository.findByAddress(searchValue);
		mFBtsDataByAddress.addAll(mFBtsDataByName);
		generateBts(btsDataBeanList, mFBtsDataByAddress);

		Set<BtsDataBean> set = btsDataBeanList.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(BtsDataBean::getId))));
		btsDataBeanList.clear();
		btsDataBeanList.addAll(set);
		return btsDataBeanList;
	}

	private void generateBts(List<BtsDataBean> btsDataBeanList, List<MfBtsData> mFBtsData) {
		mFBtsData.forEach(btsData -> {
			BtsDataBean btsDataBean = new BtsDataBean();
			btsDataBean.setId(btsData.getId());

			String siteIdStr = btsData.getSiteId();
			String[] siteIdStrArr = siteIdStr.split("#");
			if (siteIdStrArr != null && siteIdStrArr.length > 0) {
				btsDataBean.setSiteId(siteIdStrArr[0]);
			}

			btsDataBean.setSiteName(btsData.getSiteName());
			btsDataBean.setLongitude(btsData.getLongitude());
			btsDataBean.setLatitude(btsData.getLatitude());
			btsDataBean.setSiteAddress(btsData.getSiteAddress());
			btsDataBean.setCreatedTime(btsData.getCreatedTime());
			btsDataBean.setUpdatedTime(btsData.getUpdatedTime());
			btsDataBean.setIpaddress(btsData.getIpaddress());
			btsDataBean.setSectorId(btsData.getSectorId());
			btsDataBean.setSectorName(btsData.getSectorName());
			btsDataBeanList.add(btsDataBean);

		});
	}

	public List<MfHhDataBean> getHhDataBySearch(String searchValue) throws TclCommonException {

		List<MfHhDataBean> masterList = new ArrayList<MfHhDataBean>();

		List<MfHhDataBean> hhDataByName = mfHhDataRepository.findByname(searchValue).stream().map(MfHhDataBean::new)
				.collect(Collectors.toList());

		List<MfHhDataBean> hhDataByState = mfHhDataRepository.findByState(searchValue).stream().map(MfHhDataBean::new)
				.collect(Collectors.toList());

		masterList.addAll(hhDataByName);
		masterList.addAll(hhDataByState);

		Set<MfHhDataBean> set = masterList.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(MfHhDataBean::getId))));
		masterList.clear();
		masterList.addAll(set);

		return masterList;
	}

	/**
	 * Method to fetch groupnames
	 * 
	 * @param groupName
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getGroupList(String groupName) throws TclCommonException {
		List<String> groupNames = new ArrayList<>();
		try {
			LOGGER.info("Inside TaskService.getGroupList method to fetch group names for {} ", groupName);
			String[] groupNameArray = groupName.split("_");
			String group = groupNameArray[0];
			List<MfDependantTeam> dependantTeamData = null;
			if(groupName.toUpperCase().contains("ASP")) {
				 dependantTeamData = mfDependantTeamRepository.findByTeamName("AFM");
			}else {
			 dependantTeamData = mfDependantTeamRepository.findByTeamName(group);
			 List<MfDependantTeam> dependantTeamDataAsp = mfDependantTeamRepository.findByTeamName("ASP");
				Optional<MfDependantTeam> depandantAspOpt = dependantTeamDataAsp.stream().findFirst();
				
				if(depandantAspOpt.isPresent()) {
					groupNames.add(depandantAspOpt.get().getTeamName());
				}
			}
			dependantTeamData.stream().forEach(teamData -> {
				String teams = null;
				if (teamData.getTeamRegion() != null && !teamData.getTeamRegion().isEmpty()) {
					teams = teamData.getTeamName() + CommonConstants.UNDERSCORE + teamData.getTeamRegion();
				} else {
					teams = teamData.getTeamName();
				}
				groupNames.add(teams);
			});
			if (groupNames.contains(groupName)) {
				groupNames.remove(groupName);
			}
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		return groupNames;
	}

	/**
	 * Method to reassign task
	 * 
	 * @param request
	 * @throws TclCommonException
	 */
	public String reAssignTask(AssigneeRequest request) throws TclCommonException {
		String response = CommonConstants.FAILIURE;
		try {
			LOGGER.info("Inside TaskService.reAssignTask method to fetch group names for {} ", request.getTaskId());
			Optional<Task> task = taskRepository.findById(request.getTaskId());
			if (task.isPresent()) {
				Optional<TaskAssignment> taskAssignments = task.get().getTaskAssignments().stream().findFirst();
				if (taskAssignments.isPresent()) {
					TaskAssignment taskAssignment = taskAssignments.get();
					taskAssignment.setGroupName(request.getAssigneeNameTo());
					taskAssignmentRepository.save(taskAssignment);
					processTaskLogRepository.save(createProcessTaskLog(task.get(), request, TaskLogConstants.CREATED));
				}

				task.get().setUpdatedTime(new Timestamp(new Date().getTime()));
				if(request.getAssigneeNameTo().equalsIgnoreCase("asp")) {
					//task.get().getMstTaskDef().setKey(ManualFeasibilityWFConstants.MF_ASP);
					MstTaskDef mstTaskDefLoc = masterTaskDefMapper(task,ManualFeasibilityWFConstants.MF_ASP);
					task.get().setMstTaskDef(mstTaskDefLoc);
					taskRepository.save(task.get());
				}
				
				else if(request.getAssigneeNameTo().toUpperCase().contains("AFM") && request.getGroupFrom()!=null && 
						request.getGroupFrom().toUpperCase().contains("ASP")) {
					MstTaskDef mstTaskDefLoc = masterTaskDefMapper(task,ManualFeasibilityWFConstants.MF_AFM);
					task.get().setMstTaskDef(mstTaskDefLoc);
					taskRepository.save(task.get());
				}
				
				// defect fixes start..
				MfTaskDetail taskDetail = mfTaskDetailRepository.findByTaskId(request.getTaskId());
				if (taskDetail != null) {
					//taskDetail.setAssignedTo(request.getAssigneeNameTo());
					taskDetail.setAssignedGroup(request.getAssigneeNameTo());
					MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(taskDetail);
					mfTaskDetailAudit.setCreatedBy(Utils.getSource());
					mfTaskDetailRepository.save(taskDetail);
					mfTaskDetailAuditRepository.save(mfTaskDetailAudit);
				}
				// defect fixes end..
				response = CommonConstants.SUCCESS;
			}

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return response;
	}
	
	/**
	 * Method to reassign claimed  task
	 * @author krutsrin
	 * @param MfTaskRequestBean
	 * @throws TclCommonException
	 */
	public String reAssignClaimedTask(MfTaskRequestBean request) throws TclCommonException {
		String response = CommonConstants.FAILIURE;
		try {
			LOGGER.info("Inside TaskService.reAssignTask method to fetch group names for {} ", request.getTaskId());
			Optional<Task> task = taskRepository.findById(request.getTaskId());
			if (task.isPresent()) {

				Optional<TaskAssignment> taskAssignments = task.get().getTaskAssignments().stream().findFirst();
				if (taskAssignments.isPresent()
						&& task.get().getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)) {
					TaskAssignment taskAssignment = taskAssignments.get();
					taskAssignment.setGroupName(request.getAssignedTo());
					taskAssignment.setUserName(null);

					taskAssignmentRepository.save(taskAssignment);

					MfTaskDetail taskDetail = mfTaskDetailRepository.findByTaskId(request.getTaskId());
					if (taskDetail != null) {
						taskDetail.setAssignedTo(request.getAssignedTo());
						taskDetail.setAssignedFrom(request.getAssignedFrom());
						// defect fix
						taskDetail.setAssignedGroup(request.getAssignedTo());

						if (!StringUtils.isEmpty(request.getRequestorComments()))
							taskDetail.setRequestorComments(request.getRequestorComments());
						MfTaskDetailAudit mfTaskDetailAudit = new MfTaskDetailAudit(taskDetail);
						mfTaskDetailAudit.setCreatedBy(Utils.getSource());
						mfTaskDetailAuditRepository.save(mfTaskDetailAudit);
						mfTaskDetailRepository.save(taskDetail);

						AssigneeRequest assigneeRequest = new AssigneeRequest();
						assigneeRequest.setAssigneeNameFrom(request.getAssignedFrom());
						assigneeRequest.setAssigneeNameTo(request.getAssignedTo());
						assigneeRequest.setTaskId(request.getTaskId());
						assigneeRequest.setGroupFrom(request.getGroupFrom());
						processTaskLogRepository
								.save(createProcessTaskLog(task.get(), assigneeRequest, TaskLogConstants.CREATED));

						// make task to open state
						task.get().setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.OPENED));
						task.get().setUpdatedTime(new Timestamp(new Date().getTime()));
						task.get().setAssignee(request.getAssignedTo());
						
						if(request.getAssignedTo().equalsIgnoreCase("asp")) {
							//task.get().getMstTaskDef().setKey(ManualFeasibilityWFConstants.MF_ASP);
							MstTaskDef mstTaskDefLoc = masterTaskDefMapper(task,ManualFeasibilityWFConstants.MF_ASP);
							task.get().setMstTaskDef(mstTaskDefLoc);
							taskRepository.save(task.get());
						} 
						else if(request.getAssignedTo().toUpperCase().contains("AFM") && request.getGroupFrom().toUpperCase().contains("ASP")) {
							MstTaskDef mstTaskDefLoc = masterTaskDefMapper(task,ManualFeasibilityWFConstants.MF_AFM);
							task.get().setMstTaskDef(mstTaskDefLoc);
							taskRepository.save(task.get());
						}
						
						// closing all old assigned Tasks while changing region.
						List<MfTaskDetail> result = mfTaskDetailRepository.findByRequestorTaskId(request.getTaskId());
						if (result != null) {
							result.forEach(assignedTaskDetail -> {
								Task assignedTask = assignedTaskDetail.getTask();
								try {
									mfTaskDataEntry(assignedTask, new HashMap<String, Object>(),
											new HashMap<String, Object>(), true, TaskStatusConstants.CLOSED_STATUS);
								} catch (TclCommonException e) {
									LOGGER.error("Error while closing assigned tasks while re-assigning parent task.");
								}
								processMfTaskLogDetails(assignedTask, TaskLogConstants.CLOSED, "", null,
										request.getGroupFrom());
								assignedTaskDetail.setStatus(TaskLogConstants.CLOSED);
								mfTaskDetailRepository.save(assignedTaskDetail);
							});
						}
						
						
						response = CommonConstants.SUCCESS;
					}
				} else if (taskAssignments.isPresent()
						&& !task.get().getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.INPROGRESS)) {
					throw new TclCommonRuntimeException(ExceptionConstants.CANNOT_CLAIM, ResponseResource.R_CODE_ERROR);
				}
			}

		} catch (Exception e) {
			throw e;

		}
		return response;
	}

	/**
	 * Method to map existing master task def to new object
	 * @param task
	 * @param key 
	 * @return new mst task def
	 */
	private MstTaskDef masterTaskDefMapper(Optional<Task> task, String key) {
		MstTaskDef existingMstDef = task.get().getMstTaskDef();
		MstTaskDef mstTaskDefLoc = new MstTaskDef();
		
		mstTaskDefLoc.setAdminView(existingMstDef.getAdminView());
		mstTaskDefLoc.setAssignedGroup(existingMstDef.getAssignedGroup());
		mstTaskDefLoc.setButtonLabel(existingMstDef.getButtonLabel());
		mstTaskDefLoc.setDependentTaskKey(existingMstDef.getDependentTaskKey());
		mstTaskDefLoc.setDescription(existingMstDef.getDescription());
		mstTaskDefLoc.setDynamicAssignment(existingMstDef.getDynamicAssignment());
		mstTaskDefLoc.setFeEngineer(existingMstDef.getFeEngineer());
		mstTaskDefLoc.setFeType(existingMstDef.getFeType());
		mstTaskDefLoc.setFormKey(existingMstDef.getFormKey());
		mstTaskDefLoc.setIsCustomerTask(existingMstDef.getIsCustomerTask());
		mstTaskDefLoc.setIsDependentTask(existingMstDef.getIsDependentTask());
		mstTaskDefLoc.setIsManualTask(existingMstDef.getIsManualTask());
		
		mstTaskDefLoc.setKey(key);
		
		mstTaskDefLoc.setMstActivityDef(existingMstDef.getMstActivityDef());
		mstTaskDefLoc.setName(existingMstDef.getName());
		mstTaskDefLoc.setOwnerGroup(existingMstDef.getOwnerGroup());
		mstTaskDefLoc.setReminderCycle(existingMstDef.getReminderCycle());
		mstTaskDefLoc.setTat(existingMstDef.getTat());
		return mstTaskDefLoc;
	}

	private void processMfResponseDetailAudit(MfResponseDetail mfResponseDetail, boolean isUpdated)
			throws TclCommonException {
		MfResponseDetailAudit mfResponseDetailAudit = new MfResponseDetailAudit();
		mfResponseDetailAudit.setCreatedBy(Utils.getSource());
		mfResponseDetailAudit.setCreatedTime(new Timestamp(new Date().getTime()));
		mfResponseDetailAudit.setMfResponseDetail(mfResponseDetail);

		if (isUpdated) {
			mfResponseDetailAudit.setIsUpdated("1");
		}
		ObjectMapper mapper = new ObjectMapper();

		String jsonStr = "";
		try {
			jsonStr = mapper.writeValueAsString(mfResponseDetail);
		} catch (JsonProcessingException e) {
			LOGGER.info("Exception occurred in parsing response json");
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}

		mfResponseDetailAudit.setMfResponseJson(jsonStr);
		mfResponseDetailAuditRepository.save(mfResponseDetailAudit);

	}

	/**
	 * Method to get create response Audit - excel sheet
	 * 
	 * @param idsList
	 * @param httpResponse
	 * @return
	 * @throws TclCommonException
	 */
	public HttpServletResponse getAuditForFRAResponses(Integer idsList, HttpServletResponse httpResponse)
			throws TclCommonException {
		List<MfResponseDetailAudit> auditList = mfResponseDetailAuditRepository.findByMfResponseDetailId(idsList);
		HashMap<String, List<Object>> resultMap = new HashMap<String, List<Object>>();
		List<Object> resultList = new ArrayList<Object>();

		auditList.forEach(x -> {
			String jsonStr = x.getMfResponseJson();

			LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
			try {
				Object sitef = null;

				if (jsonStr != null) {
					result = new ObjectMapper().readValue(jsonStr, LinkedHashMap.class);
					String feasibilityType = null;
					if (result != null && result.containsKey("createResponseJson")
							&& result.get("createResponseJson") != null
							&& (result.get("createResponseJson") instanceof String)) {
						DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						if (result.get("feasibilityType") != null) {
							feasibilityType = (String) result.get("feasibilityType");

							if (feasibilityType.equals("RADWIN")) {
								sitef = (FRARadwinResponse) Utils.convertJsonToObject(
										(String) result.get("createResponseJson"), FRARadwinResponse.class);

								if (x.getCreatedTime() != null) {
									Timestamp ts = x.getCreatedTime();
									Date date = new Date(ts.getTime());
									((FRARadwinResponse) sitef).setCreatedtimeOrUpdatedtime(formatter.format(date));
								}

								// Created By
								if (x.getCreatedBy() != null) {
									((FRARadwinResponse) sitef).setCreatedbyOrUpdatedby(x.getCreatedBy());
								}

							} else if (feasibilityType.equals("MAN/VBL")) {
								sitef = (FRAManResponse) Utils.convertJsonToObject(
										(String) result.get("createResponseJson"), FRAManResponse.class);

								if (x.getCreatedTime() != null) {
									Timestamp ts = x.getCreatedTime();
									Date date = new Date(ts.getTime());
									((FRAManResponse) sitef).setCreatedtimeOrUpdatedtime(formatter.format(date));
								}

								// Created By
								if (x.getCreatedBy() != null) {
									((FRAManResponse) sitef).setCreatedbyOrUpdatedby(x.getCreatedBy());
								}

							} else if (feasibilityType.equals("Offnet Wireline")) {
								sitef = (FRAOffnetWirelineResponse) Utils.convertJsonToObject(
										(String) result.get("createResponseJson"), FRAOffnetWirelineResponse.class);

								if (x.getCreatedTime() != null) {
									Timestamp ts = x.getCreatedTime();
									Date date = new Date(ts.getTime());
									((FRAOffnetWirelineResponse) sitef)
											.setCreatedtimeOrUpdatedtime(formatter.format(date));
								}

								// Created By
								if (x.getCreatedBy() != null) {
									((FRAOffnetWirelineResponse) sitef).setCreatedbyOrUpdatedby(x.getCreatedBy());
								}

							} else if (feasibilityType.equals("Offnet Wireless")) {
								sitef = (FRAOffnetWirelessResponse) Utils.convertJsonToObject(
										(String) result.get("createResponseJson"), FRAOffnetWirelessResponse.class);

								if (x.getCreatedTime() != null) {
									Timestamp ts = x.getCreatedTime();
									Date date = new Date(ts.getTime());
									((FRAOffnetWirelessResponse) sitef)
											.setCreatedtimeOrUpdatedtime(formatter.format(date));
								}

								// Created By
								if (x.getCreatedBy() != null) {
									((FRAOffnetWirelessResponse) sitef).setCreatedbyOrUpdatedby(x.getCreatedBy());
								}

							} else if (feasibilityType.equals("UBR PMP/WiMax")) {
								sitef = (FRAUbrResponse) Utils.convertJsonToObject(
										(String) result.get("createResponseJson"), FRAUbrResponse.class);

								if (x.getCreatedTime() != null) {
									Timestamp ts = x.getCreatedTime();
									Date date = new Date(ts.getTime());
									((FRAUbrResponse) sitef).setCreatedtimeOrUpdatedtime(formatter.format(date));
								}

								// Created By
								if (x.getCreatedBy() != null) {
									((FRAUbrResponse) sitef).setCreatedbyOrUpdatedby(x.getCreatedBy());
								}

							}

							resultList.add(sitef);
						}
					}
					resultMap.put(feasibilityType, resultList);
				}
			} catch (IOException | TclCommonException e) {
				LOGGER.info("Exception occurred in parsing json String...");

			}
		});
		getExcelForAudit(resultMap, httpResponse);
		return httpResponse;

	}

	public void populateCreatedByAndTime(MfResponseDetailAudit x) {

		if (x.getCreatedTime() != null) {
			Timestamp ts = x.getCreatedTime();
			Date date = new Date(ts.getTime());
			// sitef.setCreatedTimeStr(formatter.format(date));
		}
	}

	/**
	 * Method to generate excel sheet.
	 * 
	 * @param feasibilityType
	 * @param feasibilityType
	 * @param assignedGroupingList
	 * @param groupName
	 * @param response
	 * @return
	 * @throws TclCommonException
	 */
	public HttpServletResponse getExcelForAudit(HashMap<String, List<Object>> resultMap, HttpServletResponse response)
			throws TclCommonException {

		byte[] excelByteArr;
		try {

			Entry<String, List<Object>> entry = resultMap.entrySet().iterator().next();
			String key = entry.getKey();
			List<Object> value = entry.getValue();
			excelByteArr = ExcelUtil.writeXLSXFile(value, key, "FRADetails", "FRADetails",
					"no response trail available");
			response.setContentType(APPLICATION_MS_EXCEL);
			response.setContentLength(excelByteArr.length);
			response.setHeader("Expires:", "0");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + "FRA_response_trail" + "\"");
			FileCopyUtils.copy(excelByteArr, response.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
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

	public List<AdminReportBean> reportSummary(TaskRequest request) {

		List<Task> tasks = taskRepository.findAll(
				TaskSpecification.getTaskSummary(null, getStatusForSummary(), null, null, null, null, null, null, null,
						null, request.getCreatedTimeFrom(), request.getCreatedTimeTo(), request.getOwner()));

		List<MfDependantTeam> dependantTeams = mfDependantTeamRepository.findByTeamName(request.getOwner());

		Map<String, List<Task>> groupList = new HashMap<>();
		dependantTeams.stream().forEach(af -> {
			String groupName = af.getTeamName().concat(CommonConstants.UNDERSCORE).concat(af.getTeamRegion());
			groupList.put(groupName, new ArrayList<Task>());
		});

		tasks.stream().forEach(t -> {
			t.getTaskAssignments().forEach(ta -> {

				if (groupList.keySet().contains(ta.getGroupName())) {

					List<Task> taskListFOrGroup = groupList.get(ta.getGroupName());
					taskListFOrGroup.add(t);
				} else {
					List<Task> newTaskList = new ArrayList<Task>();
					newTaskList.add(t);
					groupList.put(ta.getGroupName(), newTaskList);
				}

			});
		});

		// master region wise status map
		List<AdminReportBean> masterRegionlist = new ArrayList<AdminReportBean>();

		groupList.entrySet().stream().forEach(al -> {
			AdminReportBean bean = new AdminReportBean();
			HashMap<String, HashMap<String, Double>> masterStatusWiseMap = new HashMap<String, HashMap<String, Double>>();
			List<Task> closedTaskList = new ArrayList<Task>();
			List<Task> pendingTaskList = new ArrayList<Task>();

			al.getValue().stream().forEach(task -> {

				if (task.getMstStatus().getCode().equalsIgnoreCase("CLOSED")
						|| task.getMstStatus().getCode().equalsIgnoreCase("RETURNED")) {
					closedTaskList.add(task);
				} else {
					pendingTaskList.add(task);
				}

			});

			// now put in a master map
			masterStatusWiseMap.put("Pending", constructStatusCountMap(pendingTaskList, "Pending"));
			masterStatusWiseMap.put("Completed", constructStatusCountMap(closedTaskList, "Closed"));
			bean.setRegion(al.getKey());
			bean.setStat(masterStatusWiseMap);
			masterRegionlist.add(bean);
		});

		return masterRegionlist;
	}

	private HashMap<String, Double> constructStatusCountMap(List<Task> pendingTaskList, String stat) {

		LinkedHashMap<String, Double> mastercountMap = new LinkedHashMap<String, Double>();
		// pre populate Map
		mastercountMap.put("0", 0.0D);
		mastercountMap.put("1", 0.0D);
		mastercountMap.put("2", 0.0D);
		mastercountMap.put("3", 0.0D);
		mastercountMap.put(">3", 0.0D);

		pendingTaskList.stream().forEach(x -> {

			int diff = calculateDaysDiffStatusBased(x, stat);

			switch (diff) {

			case 0:
				populateMasterCountMap(mastercountMap, "0");
				break;

			case 1:
				populateMasterCountMap(mastercountMap, "1");

				break;

			case 2:
				populateMasterCountMap(mastercountMap, "2");

				break;

			case 3:
				populateMasterCountMap(mastercountMap, "3");
				break;
			default:
				populateMasterCountMap(mastercountMap, ">3");

			}
			System.out.println("Iteration   " + mastercountMap);

		}); // end of all tasks segregation..

		double avg = (mastercountMap.values() != null && !mastercountMap.values().isEmpty())
				? mastercountMap.values().stream().mapToDouble(Double::doubleValue).average().getAsDouble()
				: 0.0D;
		mastercountMap.put("avg", Double.parseDouble(df.format(avg)));
		return mastercountMap;
	}

	private void populateMasterCountMap(HashMap<String, Double> mastercountMap, String keyStr) {
		if (mastercountMap.containsKey(keyStr)) {
			Double val = mastercountMap.get(keyStr);
			val = val + 1;
			mastercountMap.put(keyStr, val);
		} else {
			mastercountMap.put(keyStr, (double) 1);

		}
	}

	public int calculateDaysDiffStatusBased(Task task, String stat) {

		int daysDiff = 0;

		if (stat.equals("Pending")) {

			daysDiff = Period.between(task.getCreatedTime().toLocalDateTime().toLocalDate(), LocalDate.now()).getDays();
		}

		if (stat.equals("Closed")) {

			daysDiff = Period.between(task.getCreatedTime().toLocalDateTime().toLocalDate(),
					task.getCompletedTime().toLocalDateTime().toLocalDate()).getDays();
		}

		return daysDiff;

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
	 * Method to add customer Lat long for task id
	 * 
	 * @param mfTaskDetailBean
	 * @return
	 * @throws TclCommonException
	 */
	public String saveCustomerLatLong(MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		String response = CommonConstants.FAILIURE;
		LOGGER.info("Inside TaskService.saveCustomerLatLong method to save lat & long for taskId{} ",
				mfTaskDetailBean.getTaskId());
		try {
			MfTaskDetail mfTask = mfTaskDetailRepository.findByTaskId(mfTaskDetailBean.getTaskId());
			if (mfTask != null) {
				MfTaskData mfTaskData = new MfTaskData();
				mfTaskData.setCustomerLat(mfTaskDetailBean.getTaskData().getCustomerLat());
				mfTaskData.setCustomerLong(mfTaskDetailBean.getTaskData().getCustomerLong());
				mfTask.setTaskData(Utils.convertObjectToJson(mfTaskData));
				mfTaskDetailRepository.save(mfTask);
				response = CommonConstants.SUCCESS;
			} else
				throw new TclCommonException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return response;
	}
	
	
	public String saveCustomerLatLongForNPL(MfTaskDetailBean mfTaskDetailBean) throws TclCommonException {
		String response = CommonConstants.FAILIURE;
		LOGGER.info("Inside TaskService.saveCustomerLatLong method to save lat & long for taskId{} ",
				mfTaskDetailBean.getTaskId());
		try {
			MfTaskDetail mfTask = mfTaskDetailRepository.findByTaskId(mfTaskDetailBean.getTaskId());
			if (mfTask != null) {
				MfTaskData mfTaskData = new MfTaskData();
				mfTaskData.setCustomerLatA(mfTaskDetailBean.getTaskData().getCustomerLatA());
				mfTaskData.setCustomerLongA(mfTaskDetailBean.getTaskData().getCustomerLongA());
				
				mfTaskData.setCustomerLatB(mfTaskDetailBean.getTaskData().getCustomerLatB());
				mfTaskData.setCustomerLongB(mfTaskDetailBean.getTaskData().getCustomerLongB());
				
				mfTask.setTaskData(Utils.convertObjectToJson(mfTaskData));
				mfTaskDetailRepository.save(mfTask);
				response = CommonConstants.SUCCESS;
			} else
				throw new TclCommonException(ExceptionConstants.TASK_NOT_FOUND, ResponseResource.R_CODE_ERROR);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);

		}
		return response;
	}

	/**
	 * get feasibility category based on mode and status
	 * @param groupName
	 * @param request
	 * @return
	 * @throws TclCommonException
	 */
	public List<String> getFeasibilityCategory(FeasibilityCategoryRequest request) throws TclCommonException{
		List<String> categories = new ArrayList<>();
		try {
			List<FeasibilityCategoryMap> feasibilityCategoryMaps = feasibilityCategoryMapRepository
					.findByFModeAndFStatus(request.getMode(),request.getStatus());
			feasibilityCategoryMaps.forEach(feasibilityCategoryMap -> {
				LOGGER.info("Adding  FeasibilityCategoryMap to list  {} ",feasibilityCategoryMap);
				categories.add(feasibilityCategoryMap.getfCategory());
			});
		} catch(Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
		
		return categories;
	}

	/** 
	 * this method is used to get the tasks assigned for afm and asp team for a site
	 * @param siteCode
	 * @return
	 * @throws TclCommonException
	 */
	public List<OmsTaskDetailBean> getTaskDetailsForOms(List<String> siteCodeList) throws TclCommonException {
		List<OmsTaskDetailBean> taskDetailList = new ArrayList<>();
		if (!siteCodeList.isEmpty()) {
			siteCodeList.stream().forEach(siteCode -> {
				if (StringUtils.isNotEmpty(siteCode)) {
					try {
						List<Task> tasks = taskRepository.findBySiteCode(siteCode);
						LOGGER.info("Total tasks raised for site {} : {}", siteCode, tasks.size());
						if (!tasks.isEmpty()) {
							tasks = tasks.stream()
									.filter(task -> task.getMstTaskDef().getKey().endsWith("afm")
											|| task.getMstTaskDef().getKey().endsWith("asp"))
									.collect(Collectors.toList());
							if (!tasks.isEmpty()) {
								LOGGER.debug("Total tasks assigned for afm and asp team : {}", tasks.size());
								tasks.stream().forEach(task -> {
									LOGGER.debug("Task ID : {}", task.getId());
									MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
									if (mfTaskDetail != null && (mfTaskDetail.getStatus() == null
											|| (mfTaskDetail.getStatus() != null && !mfTaskDetail.getStatus()
													.equalsIgnoreCase(TaskLogConstants.CLOSED)))) {
										LOGGER.debug("Mf task detail ID : {}", mfTaskDetail.getId());
										String[] siteType = task.getMfDetail().getSiteType().split("-");
										OmsTaskDetailBean taskDetail = new OmsTaskDetailBean();
										taskDetail.setSiteCode(siteCode);
										taskDetail.setTaskId(task.getId());
										taskDetail.setFeasibilityId(task.getFeasibilityId());
										taskDetail.setFeasibilityStatus(mfTaskDetail.getStatus());
										taskDetail.setReason(mfTaskDetail.getReason());
										taskDetail.setRemarks(mfTaskDetail.getResponderComments());
										taskDetail.setTeam(mfTaskDetail.getAssignedGroup());
										taskDetail.setAssignedOn(task.getCreatedTime());
										taskDetail.setAssignedTo(mfTaskDetail.getAssignedTo());
										taskDetail.setClaimedOn(task.getClaimTime());
										taskDetail.setUpdatedOn(task.getUpdatedTime());
										taskDetail.setSiteType(siteType.length>1?siteType[1]:siteType[0]);
										taskDetailList.add(taskDetail);
									} else {
										LOGGER.error("Mf Task Detail is empty for task Id : {}", task.getId());
									}
								});

							}
						} else {
							LOGGER.info("No tasks available for the site : {}", siteCode);
						}
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				}
			});

		}
		return taskDetailList;
	}
	
	
	public List<OmsTaskDetailBean> getNPLTaskDetailsForOms(List<Integer> linkIdList) throws TclCommonException {
		List<OmsTaskDetailBean> taskDetailList = new ArrayList<>();
		// if (!linkIdList.isEmpty()) {
		linkIdList.stream().forEach(linkId -> {

			List<MfDetail> mfetailsList = mfDetailRepository.findByLinkId(linkId);
			mfetailsList.stream().forEach(x -> {

				List<Task> tasks = taskRepository.findBySiteCode(x.getSiteCode());
				LOGGER.info("Total tasks raised for  {} : {}", x.getSiteCode(), tasks.size());
				if (!tasks.isEmpty()) {
					tasks = tasks.stream().filter(task -> task.getMstTaskDef().getKey().endsWith("afm")
							|| task.getMstTaskDef().getKey().endsWith("asp")).collect(Collectors.toList());
					if (!tasks.isEmpty()) {
						LOGGER.debug("Total tasks assigned for afm and asp team : {}", tasks.size());
						tasks.stream().forEach(task -> {
							LOGGER.debug("Task ID : {}", task.getId());
							MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
							if (mfTaskDetail != null
									&& (mfTaskDetail.getStatus() == null || (mfTaskDetail.getStatus() != null
											&& !mfTaskDetail.getStatus().equalsIgnoreCase(TaskLogConstants.CLOSED)))) {
								LOGGER.debug("Mf task detail ID : {}", mfTaskDetail.getId());
								String[] siteType = task.getMfDetail().getSiteType().split("-");
								OmsTaskDetailBean taskDetail = new OmsTaskDetailBean();
								taskDetail.setLinkId(String.valueOf(linkId));
								taskDetail.setSiteCode(x.getSiteCode());
								taskDetail.setTaskId(task.getId());
								taskDetail.setFeasibilityId(task.getFeasibilityId());
								taskDetail.setFeasibilityStatus(mfTaskDetail.getStatus());
								taskDetail.setReason(mfTaskDetail.getReason());
								taskDetail.setRemarks(mfTaskDetail.getResponderComments());
								taskDetail.setTeam(mfTaskDetail.getAssignedGroup());
								taskDetail.setAssignedOn(task.getCreatedTime());
								taskDetail.setAssignedTo(mfTaskDetail.getAssignedTo());
								taskDetail.setClaimedOn(task.getClaimTime());
								taskDetail.setUpdatedOn(task.getUpdatedTime());
								taskDetail.setSiteType(siteType.length > 1 ? siteType[1] : siteType[0]);
								taskDetailList.add(taskDetail);
							} else {
								LOGGER.error("Mf Task Detail is empty for task Id : {}", task.getId());
							}
						});
					}
				}
			});
		});

		return taskDetailList;
	} 
				
			
	
	
	
	public OmsTaskDetailBean getCommercialTaskDetailsForOms(Integer quoteId) throws TclCommonException{
		if(quoteId == null)
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR,ResponseResource.R_CODE_BAD_REQUEST);
		
		OmsTaskDetailBean taskDetailBean = new OmsTaskDetailBean();
		
		List<Task> taskList = taskRepository.findByQuoteId(quoteId);
		LOGGER.info("Total tasks for the quote {} - {}",quoteId,taskList.size());
		if(!taskList.isEmpty()) {
			List<Task> commercialTaskList = taskList.stream().filter(commTask -> commTask.getSiteDetail()!=null).collect(Collectors.toList());
			LOGGER.info("Total commercial tasks for the quote {} - {}",quoteId,commercialTaskList.size());
			if(!commercialTaskList.isEmpty()) {
				Optional<Task> taskOptInprogress = commercialTaskList.stream().filter(taskIp -> taskIp.getMstStatus().getCode().equalsIgnoreCase("INPROGRESS") ||taskIp.getMstStatus().getCode().equalsIgnoreCase("OPEN")).findFirst();
				if(taskOptInprogress.isPresent()) {
					Task task = taskOptInprogress.get();
					LOGGER.info("Site Detail Id for the task {} - {}", task.getId(),task.getSiteDetail().getId());
					taskDetailBean.setTaskId(task.getId());
					taskDetailBean.setAssignedOn(task.getCreatedTime());
					Timestamp claimedTime = task.getClaimTime();
					if(claimedTime == null && !task.getMstTaskDef().getKey().equalsIgnoreCase("commercial-discount-1"))
						claimedTime = task.getCreatedTime();
					taskDetailBean.setClaimedOn(claimedTime);
					TaskAssignment taskAssignment = task.getTaskAssignments().stream().findFirst().get();
					taskDetailBean.setAssignedTo(taskAssignment.getGroupName());
	                
					
				}
				
			}
		}
		return taskDetailBean;
	}
	
	/**
	 * used to get afm and asp teams task details
	 * @param siteCode
	 * @return
	 * @throws TclCommonException
	 */
	public List<OmsTaskDetailBean> getAfmAndAspTaskDetails(String siteCode) throws TclCommonException {
		if (StringUtils.isEmpty(siteCode))
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, ResponseResource.R_CODE_BAD_REQUEST);
		List<String> mstDefKeys = new ArrayList<>();
		mstDefKeys.add(ManualFeasibilityWFConstants.MF_AFM);
		mstDefKeys.add(ManualFeasibilityWFConstants.MF_ASP);
		List<OmsTaskDetailBean> omsTaskBeanList = new ArrayList<>();
		List<Task> tasks = taskRepository.findBySiteCodeAndMstTaskDef_keyIn(siteCode, mstDefKeys);
		if (!tasks.isEmpty()) {
			omsTaskBeanList.addAll(tasks.stream().map(task -> {
				OmsTaskDetailBean omsTaskDetailBean = new OmsTaskDetailBean();
				omsTaskDetailBean.setFeasibilityId(task.getFeasibilityId());
				omsTaskDetailBean.setAssignedOn(task.getCreatedTime());
				omsTaskDetailBean.setUpdatedOn(task.getUpdatedTime());
				MfTaskDetail mfTaskDetail = mfTaskDetailRepository.findByTaskId(task.getId());
				omsTaskDetailBean.setAssignedTo(mfTaskDetail.getAssignedGroup());
				omsTaskDetailBean.setAssignedFrom(mfTaskDetail.getAssignedFrom());
				omsTaskDetailBean.setFeasibilityStatus(mfTaskDetail.getStatus());
				omsTaskDetailBean.setReason(mfTaskDetail.getReason());
				omsTaskDetailBean.setRemarks(mfTaskDetail.getResponderComments());
				JSONObject dataObj = null;
				JSONParser jsonParser = new JSONParser();
				try {
					if (task.getMfDetail() != null && task.getMfDetail().getMfDetails() != null) {
						dataObj = (JSONObject) jsonParser.parse(task.getMfDetail().getMfDetails());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(dataObj.get("mfLinkEndType") !=null) {
				omsTaskDetailBean.setTastRealatedTo((String) dataObj.get("mfLinkEndType"));
				}
				return omsTaskDetailBean;
			}).collect(Collectors.toList()));

		}
		return omsTaskBeanList;

	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String manuallyCompleteRejectTask(Integer taskId, CompleteTaskBean completeTaskBean) {
		try {
			Map<String, Object> map=completeTaskBean.getDiscountapproval();
			LOGGER.info("TASK COMPLETION AT REJECTION TASKID "+taskId+" APPROVAL:"+map.get("discountApprovalLevel"));
			Task task = getTaskById(taskId);
			// Updating discount approval level in site detail table
			if (task.getSiteDetail() != null && map.containsKey("discountApprovalLevel")&& !completeTaskBean.getSitedetail().isEmpty()) {
				LOGGER.info("completeTaskBean sitedetails list"+completeTaskBean.getSitedetail().size());
				SiteDetail siteDetail = task.getSiteDetail();
				siteDetail.setDiscountApprovalLevel((Integer) map.get("discountApprovalLevel"));
				siteDetail.setStatus("REJECTED");
				siteDetail.setSiteDetail(Utils.convertObjectToJson(completeTaskBean.getSitedetail()));
				siteDetailRepository.saveAndFlush(siteDetail);
			}
			
			//FIX FOR auto escalation mail bulk
			taskDataEntryReject(task, map, map);
			processTaskLogDetails(task, TaskLogConstants.CLOSED, "", null);
			
			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}
	
	public MfAttachmentBean objectUploadL2OReport( MultipartFile file, Integer quoteId)
			throws TclCommonException {
		TempUploadUrlInfo tempUploadUrlInfo = null;
		MfAttachmentBean mfL2OReportBean = new MfAttachmentBean();
		try {
			if (file == null) {
				throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
						ResponseResource.R_CODE_FORBIDDEN_ERROR);
			}
			
			MfDetailAttributes mfdetailAttr = null;
			List<MfDetail> mfDetailsList = mfDetailRepository.findByQuoteId(quoteId);
			if(!CollectionUtils.isEmpty(mfDetailsList)) {
				String mfDetail = mfDetailsList.stream().findFirst().get().getMfDetails();
			    mfdetailAttr = Utils.convertJsonToObject(mfDetail, MfDetailAttributes.class);
			}
			
			tempUploadUrlInfo = fileStorageService.getTempUploadUrl(file.getOriginalFilename(),
					Long.parseLong(tempUploadUrlExpiryWindow), MasterDefConstants.MF_L2O_REPORT+CommonConstants.UNDERSCORE +mfdetailAttr.getCustomerCode());
			mfL2OReportBean.setName(tempUploadUrlInfo.getRequestId());
			mfL2OReportBean.setUrlPath(tempUploadUrlInfo.getTemporaryUploadUrl());
			return mfL2OReportBean;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	public MfAttachmentBean fileUploadL2OReport( MultipartFile file)
			throws TclCommonException {
		MfAttachmentBean mfL2OReportBean = new MfAttachmentBean();
		try {
			if (file == null) {
				throw new TclCommonException(ExceptionConstants.DOCUMENT_VALIDATION_ERROR,
						ResponseResource.R_CODE_FORBIDDEN_ERROR);
			}
			  Integer attachmentID = mfAttachmentUtil.saveAttachment(MasterDefConstants.MF_L2O_REPORT, file);
		        if(attachmentID!=null && attachmentID >0) {
		        mfL2OReportBean.setId(attachmentID);
		        mfL2OReportBean.setName(file.getOriginalFilename());
		        }
			return mfL2OReportBean;
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}
	}
	
	
	
	 /**
     * Method to update the uploaded File
     *
     * @param requestId
     * @param path
     * @return {@link PartnerDocumentBean}
     */
    public MfAttachmentBean updateUploadObjectConfigurationDocument(String requestId, String path) throws TclCommonException {
    	MfAttachmentBean mfAttachBean = new MfAttachmentBean();
    	
        Integer attachmentID = mfAttachmentUtil.saveObjectAttachment(requestId, path);
        mfAttachBean.setId(attachmentID);
        mfAttachBean.setName(requestId);
        return mfAttachBean;
    }

    
    public String mapDocumentsByFeasibilityResponseId(MfL2OReportBean bean) throws TclCommonException  {
    	String response = CommonConstants.FAILIURE;
        if (bean !=null && bean.getTaskId()!=null && !CollectionUtils.isEmpty(bean.getMfAttachments()) && StringUtils.isNotBlank(bean.getFeasibilityResponseId())) {

        	Optional<Task> mfTask = taskRepository.findById(bean.getTaskId());
        	
			List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
			
			if (mfTask.isPresent()) {

				bean.getMfAttachments().stream().forEach(documentBean -> {
					OmsAttachBean omsAttachBean = new OmsAttachBean();
					omsAttachBean.setAttachmentId(documentBean.getId());
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode("MF").toString());
					omsAttachBean.setReferenceId(bean.getQuoteId());
					omsAttachBean.setQouteLeId(bean.getQuoteTole());
					//As we are in quote phase - setting as 0. 
					omsAttachBean.setOrderLeId(0);
					omsAttachBean.setReferenceName(bean.getFeasibilityResponseId());
					omsAttachBeanList.add(omsAttachBean);
				});

				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				try {
					mqUtils.sendAndReceive(omsAttachmentQueue, oattachmentrequest);
				} catch (TclCommonException e) {
					LOGGER.error("Error in sending attachment to omsAttachment table via queue call ", e);
				}
				response =CommonConstants.SUCCESS;
			}else {
				LOGGER.error("No Such task available in DB ");
				response =CommonConstants.FAILIURE;

			}
        }
        return response;
    }
    
    
    public String deleteL2oReport(MfL2OReportBean bean) throws TclCommonException  {
    	String response = CommonConstants.FAILIURE;
        if (bean !=null && !CollectionUtils.isEmpty(bean.getMfAttachments()) 
        		&& StringUtils.isNotBlank(bean.getFeasibilityResponseId())) {
			List<OmsAttachBean> omsAttachBeanList = new ArrayList<>();
				bean.getMfAttachments().stream().forEach(documentBean -> {
					OmsAttachBean omsAttachBean = new OmsAttachBean();
					omsAttachBean.setAttachmentId(documentBean.getId());
					omsAttachBean.setAttachmentType(AttachmentTypeConstants.getByCode("MF").toString());
					omsAttachBean.setReferenceName(bean.getFeasibilityResponseId());
					omsAttachBeanList.add(omsAttachBean);
				});

				OmsListenerBean listenerBean = new OmsListenerBean();
				listenerBean.setOmsAttachBean(omsAttachBeanList);
				listenerBean.setDeleteAttachmentReference(true);
				String oattachmentrequest = Utils.convertObjectToJson(listenerBean);
				LOGGER.info("MDC Filter token value in before Queue call processUploadFiles {} :",
						MDC.get(CommonConstants.MDC_TOKEN_KEY));
				try {
					mqUtils.sendAndReceive(omsAttachmentQueue, oattachmentrequest);
				} catch (TclCommonException e) {
					LOGGER.error("Error in sending attachment to omsAttachment table via queue call ", e);
				}
				response =CommonConstants.SUCCESS;
			}else {
				LOGGER.error("Mandatory params are blank");
				response =CommonConstants.FAILIURE;

			}
        return response;
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED)
	public String manuallyCompleteCommercialTask(Integer taskId, CompleteTaskBean completeTaskBean) {
		try {
			Task task = getTaskById(taskId);
			Map<String, Object> map=completeTaskBean.getDiscountapproval();
			LOGGER.info("map"+map.get("discountApprovalLevel"));
			// Updating discount approval level in site detail table
			if (task.getSiteDetail() != null && map.containsKey("discountApprovalLevel") && !completeTaskBean.getSitedetail().isEmpty()) {
				LOGGER.info("completeTaskBean sitedetails list"+completeTaskBean.getSitedetail().size());
				SiteDetail siteDetail = task.getSiteDetail();
				siteDetail.setDiscountApprovalLevel((Integer) map.get("discountApprovalLevel"));
				siteDetail.setSiteDetail(Utils.convertObjectToJson(completeTaskBean.getSitedetail()));
				siteDetailRepository.saveAndFlush(siteDetail);
			}
			taskDataEntry(task, map, map);
			processTaskLogDetails(task, TaskLogConstants.CLOSED, "", null);
			return "SUCCESS";
		} catch (Exception ee) {
			LOGGER.error(ee.getMessage(), ee);
			return ee.getMessage();
		}
	}
    
    public void populateTaskTable(QuoteDetailForTask quoteDetail) {

		// if you knw site code
		if (quoteDetail.getSitecode() != null && !quoteDetail.getSitecode().isEmpty()) {
			List<Task> tasks = taskRepository.findBySiteCode(quoteDetail.getSitecode());
			if (!CollectionUtils.isEmpty(tasks)) {
				tasks.forEach(x -> {
					fillQuoteDetailsInTask(quoteDetail, x);
				});
			}
		}
		
		// if you know feasibility ID 
		else if (quoteDetail.getFeasibilityId() != null && !quoteDetail.getFeasibilityId().isEmpty()) {
		 Optional<Task> taskFT= taskRepository.findByFeasibilityId(quoteDetail.getFeasibilityId());
			if (taskFT.isPresent()) {
				fillQuoteDetailsInTask(quoteDetail, taskFT.get());
			}
		}
		
		// if you know siteID
		else if (quoteDetail.getSiteId() != null) {
			List<Task> tasks = taskRepository.findBySiteId(quoteDetail.getSiteId());
			if (!CollectionUtils.isEmpty(tasks)) {
				tasks.forEach(x -> {
					fillQuoteDetailsInTask(quoteDetail, x);
				});
			}
		}
	}

	private void fillQuoteDetailsInTask(QuoteDetailForTask quoteDetail, Task x) {

		// update mf tasks alone
		if (x.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_AFM)
				|| x.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_ASP) 
				||  x.getMstTaskDef().getKey().equalsIgnoreCase(ManualFeasibilityWFConstants.MF_PRV) ) {
			if (quoteDetail.getQuotecode() != null) {
				x.setQuoteCode(quoteDetail.getQuotecode());
				if (x.getMfDetail() != null) {
					x.getMfDetail().setQuoteCode(quoteDetail.getQuotecode());
					taskRepository.save(x);
				}
			}

			if (quoteDetail.getQuoteId() != null) {
				x.setQuoteId(quoteDetail.getQuoteId());
				if (x.getMfDetail() != null) {
					x.getMfDetail().setQuoteId(quoteDetail.getQuoteId());
					taskRepository.save(x);
				}
			}
		}
	}

	public void updateMfDetailsJson(QuoteDetailForTask quoteDetail) {

		if (quoteDetail.getMfDetailId() != null) {

			Optional<MfDetail> mfDetailEntry = mfDetailRepository.findById(quoteDetail.getMfDetailId());
			if (mfDetailEntry.isPresent()) {

				MfDetail mfDetailFromDB = mfDetailEntry.get();
				
				// Mf_details
				if (quoteDetail.getColumnName().equals("mf_details")) {
					String mfDetailsJson = mfDetailFromDB.getMfDetails();
					if (mfDetailsJson != null) {
						mfDetailEntry.get().setMfDetails(parseAndUpdateJson(quoteDetail,mfDetailsJson,mfDetailEntry));
						mfDetailRepository.save(mfDetailEntry.get());
					}}
				
				// system_link_response_json
				else if (quoteDetail.getColumnName().equals("system_link_response_json")) {
					String mfDetailsJson = mfDetailFromDB.getSystemLinkResponseJson();
					if (mfDetailsJson != null) {
						mfDetailEntry.get().setSystemLinkResponseJson(parseAndUpdateJson(quoteDetail,mfDetailsJson,mfDetailEntry));
						mfDetailRepository.save(mfDetailEntry.get());
					}}
			}
		}
	}

	private String parseAndUpdateJson(QuoteDetailForTask quoteDetail, String mfDetailsJson, Optional<MfDetail> mfDetailEntry) {
		JSONObject dataEnvelopeObj = null;
		JSONParser jsonParser = new JSONParser();

		try {
			dataEnvelopeObj = (JSONObject) jsonParser.parse(mfDetailsJson);
		} catch (ParseException e) {
			throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		if (!CollectionUtils.isEmpty(quoteDetail.getListofFields())) {
			for (MfDetailManualUpdateBean action : quoteDetail.getListofFields()) {
				LOGGER.info("json field {}, json value {}", action.getJsonFieldName(), action.getValueToBeUpdated());

				if (dataEnvelopeObj.containsKey(action.getJsonFieldName())) {
					LOGGER.info("Inside json field {}, json value {}", action.getJsonFieldName(),
							action.getValueToBeUpdated());
					dataEnvelopeObj.put(action.getJsonFieldName(), action.getValueToBeUpdated());
				}
			}
		}
		return dataEnvelopeObj.toJSONString();
	}

	/*
	 * public String setReqComments(Integer quoteId, UpdateRequestorComments
	 * reqComments) {
	 * LOGGER.info("Entered method setReqComments for quote Id ----> {} ", quoteId);
	 * 
	 * List<SiteDetail> siteDetails = siteDetailRepository.findByQuoteId(quoteId);
	 * LOGGER.info("Tasks list size is ---> {} ", siteDetails.size());
	 * if(Objects.nonNull(siteDetails) && !siteDetails.isEmpty()){ boolean present =
	 * siteDetails.stream().findFirst().isPresent(); if(present){ SiteDetail
	 * siteDetail = siteDetails.stream().findFirst().get();
	 * LOGGER.info("Task is present for quote ---> {} and task ID is ----> {} "
	 * ,siteDetail.getQuoteCode() , siteDetail.getId());
	 * siteDetail.setReqComments(reqComments.getValue());
	 * siteDetailRepository.save(siteDetail);
	 * LOGGER.info("Requestor comments for quote code ----> {} is ----> {} ",
	 * siteDetail.getQuoteCode(), siteDetail.getReqComments()); return
	 * siteDetail.getReqComments(); } } return ""; }
	 */
	
	/**
	 * this method is used to get the tasks assigned for afm and asp team for a site
	 * 
	 * @param siteCode
	 * @return
	 * @throws TclCommonException
	 */
	public List<FetchTaskDetailBean> getTaskDetailsFetch(List<Integer> siteIdList) throws TclCommonException {
		List<FetchTaskDetailBean> taskDetailList = new ArrayList<>();
		if (!siteIdList.isEmpty()) {
			siteIdList.stream().forEach(siteId -> {
				if (siteId != null) {
					try {
						List<Task> tasks = taskRepository.findBySiteId(siteId);
						FetchTaskDetailBean fetchTaskDetail = new FetchTaskDetailBean();
						List<TaskDetailsBean> taskList = new ArrayList<TaskDetailsBean>();
						fetchTaskDetail.setSiteId(siteId);
						LOGGER.info("Total tasks raised for site {} : {}", siteId, tasks.size());
						if (!tasks.isEmpty()) {
							fetchTaskDetail.setSiteCode(tasks.get(0).getSiteCode());
							tasks = tasks.stream()
									.filter(task -> task.getMstTaskDef().getKey().endsWith("afm")
											|| task.getMstTaskDef().getKey().endsWith("asp"))
									.collect(Collectors.toList());
							if (!tasks.isEmpty()) {
								LOGGER.debug("Total tasks assigned for afm and asp team : {}", tasks.size());
								tasks.stream().forEach(task -> {
									LOGGER.debug("Task ID : {}", task.getId());
									if (!task.getMstStatus().getCode().equalsIgnoreCase("CLOSED")
											&& !task.getMstStatus().getCode().equalsIgnoreCase("RETURNED")) {
										Optional<TaskAssignment> taskAssignment = taskAssignmentRepository
												.findByTaskId(task.getId());
										TaskDetailsBean taskDetail = new TaskDetailsBean();
										taskDetail.setTaskId(task.getId());
										taskDetail.setFeasibilityId(task.getFeasibilityId());
										taskDetail.setStatus(task.getMstStatus().getCode());
										taskDetail.setMfDetailId(task.getMfDetail().getId());
										taskDetail.setTaskDefKey(task.getMstTaskDef().getKey());
										if (taskAssignment.isPresent()) {
											taskDetail.setUserName(taskAssignment.get().getUserName());
											taskDetail.setGroupName(taskAssignment.get().getGroupName());
										}
										taskList.add(taskDetail);
									}
								});

							}
							fetchTaskDetail.setTaskList(taskList);
							taskDetailList.add(fetchTaskDetail);
						} else {
							LOGGER.info("No tasks available for the site : {}", siteId);
						}
					} catch (Exception e) {
						throw new TclCommonRuntimeException(ExceptionConstants.COMMON_ERROR, e,
								ResponseResource.R_CODE_ERROR);
					}
				}
			});

		}
		return taskDetailList;
	}
	
	public Set<SupplierIORBean> getSupplierIORList() throws TclCommonException {

		List<SupplierIORBean> masterList = new ArrayList<>();
		try {
			List<SupplierIORBean> supplier = supplierIORRepository.findAll().stream().map(SupplierIORBean::new)
					.collect(Collectors.toList());
			masterList.addAll(supplier);
		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		Set<SupplierIORBean> set = masterList.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SupplierIORBean::getId))));

		return set;
	} 
	
	
		
	/**
	 * Method to search SupplierIOR By NmiLocation Or SupplierName Or IorId
	 * 
	 * @param searchValue
	 * @return
	 * @throws TclCommonException
	 */
	public Set<SupplierIORBean> getSupplierIORByNmiLocationOrSupplierNameOrIorId(String searchValue) throws TclCommonException {
		List<SupplierIORBean> masterList = new ArrayList<>();
		try {
			// By SupplierName
			List<SupplierIORBean> supplier= supplierIORRepository.findBySupplierName(searchValue).stream()
					.map(SupplierIORBean::new).collect(Collectors.toList());
			masterList.addAll(supplier);

			// By NmiLocation

			List<SupplierIORBean> nmiLocation = supplierIORRepository.findByNmiLocation(searchValue)
					.stream().map(SupplierIORBean::new).collect(Collectors.toList());
			masterList.addAll(nmiLocation);
			
			// By IORId
			List<SupplierIORBean> iorId = supplierIORRepository.findByIorId(searchValue)
					.stream().map(SupplierIORBean::new).collect(Collectors.toList());
			masterList.addAll(iorId);

		} catch (Exception e) {
			throw new TclCommonException(ExceptionConstants.COMMON_ERROR, e, ResponseResource.R_CODE_ERROR);
		}

		Set<SupplierIORBean> set = masterList.stream()
				.collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(SupplierIORBean::getId))));
		
		return set;
	}

}
