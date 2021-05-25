package com.tcl.dias.servicefulfillmentutils.service.v1;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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
import java.util.stream.Collectors;

import com.tcl.dias.networkaugment.entity.entities.*;
import com.tcl.dias.networkaugment.entity.entities.Process;
import com.tcl.dias.networkaugment.entity.repository.*;
import com.tcl.dias.servicefulfillmentutils.beans.MfTaskTrailBean;
import com.tcl.dias.servicefulfillmentutils.beans.NetworkInventoryBean;
import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.task.service.delegate.DelegateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillmentutils.constants.AppointmentConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
public class NetworkAugmentationWorkFlowService extends WorkFlowAbstractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAugmentationWorkFlowService.class);

	@Autowired
	StageRepository stageRepository;

	@Autowired
	ProcessRepository processRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	MstStageDefRepository mstStageDefRepository;

	@Autowired
	MstTaskDefRepository mstTaskDefRepository;

	@Autowired
	MstActivityDefRepository mstActivityDefRepository;

	@Autowired
	MstProcessDefRepository mstProcessDefRepository;

	@Autowired
	TaskAssignmentRepository taskAssignmentRepository;

	@Autowired
	ProcessTaskLogRepository processTaskLogRepository;

	@Autowired
	TATService tatService;

	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	TaskTatTimeRepository taskTatTimeRepository;

	@Autowired
	MstSlotRepository mstSlotRepository;

	@Autowired
	AppointmentRepository appointmentRepository;

	@Autowired
	NotificationService notificationService;

	@Autowired
	TaskService taskService;

	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	@Autowired
	TaskDataRepository taskDataRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScOrderRepository scOrderRepository;

	@Value("${app.host}")
	String appHost;


	@Value("${customer.support.email}")
	String customerSupportEmail;

	@Autowired
	ScContractInfoRepository scContractInfoRepository;


	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	MstStatusRepository mstStatusRepository;

	@Autowired
	NetworkInventoryRepository networkInventoryRepository;

	@Autowired
	NwaOrderDetailsExtndRepository nwaOrderDetailsExtndRepository;

	@Autowired
	NwaEorEquipDetailsRepository nwaEorEquipDetailsRepository;


	/**
	 * @author vivek
	 * @param  //varibleMap
	 * @param  //string
	 * @return
	 * @throws TclCommonException
	 */

	public Map<String, Object> processStage(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		if (execution.getCurrentActivityId() != null) {
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			String defKey = "eor_stage_workflow";
			LOGGER.info("Stage started for {}, serviceId={}", defKey, serviceId);

			LOGGER.info("varible map for processStage {}", varibleMap);

			MstStageDef mstStageDef = mstStageDefRepository.findByKey(defKey);

			MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS);

			Stage stage = stageRepository.save(createStatge(mstStageDef, mstStatus, varibleMap, execution.getId()));

			execution.setVariable(mstStageDef.getKey() + "_ID", stage.getId());
			LOGGER.info("Stage created Id {} ", stage.getId());

		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param //varibleMap
	 * @param// string
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processStageCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		if (execution.getCurrentActivityId() != null) {

			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			String defKey = execution.getCurrentActivityId().replaceAll("_end", "");
			LOGGER.info("StageCompletion for {}, serviceId={}", defKey, serviceId);

			try {
				 	Integer stageId = (Integer) varibleMap.get(defKey + "_ID");
					LOGGER.info("processStageCompletion::stageId={} ", stageId);
					Optional<Stage> stageOptional = stageRepository.findById(stageId);
					if (stageOptional.isPresent()) {
						LOGGER.info("processStageCompletion::stage exists");
						Stage stage = stageOptional.get();
						stage.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
						stage.setCompletedTime(new Timestamp(new Date().getTime()));
						stageRepository.save(stage);
					}

			} catch (Exception ee) {
				LOGGER.error("processStageCompletion::Exception={} ", ee);
			}
			updateServiceConfigStatus(serviceId, defKey);

		}


		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param // varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> initiateProcess(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

		String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
		LOGGER.info("Process started for {}, serviceId={}", defKey, serviceId);

		LOGGER.info("varible map for initiateProcess {}", varibleMap);

		MstProcessDef mstProcessDef = mstProcessDefRepository.findByKey(defKey);

		Stage stage = null;

		if(varibleMap.get(mstProcessDef.getMstStageDef().getKey() + "_ID")==null) {
			stage = stageRepository.findFirstByServiceIdAndMstStageDef_keyOrderByIdDesc(serviceId, mstProcessDef.getMstStageDef().getKey());
			execution.setVariable(mstProcessDef.getMstStageDef().getKey() + "_ID", stage.getId());
		}else {
			stage = stageRepository.findById((Integer) varibleMap.get(mstProcessDef.getMstStageDef().getKey() + "_ID")).get();
		}
		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS);

		LOGGER.info("mstProcessDef.getMstStageDef().getKey() {}, stageID={},  isPresent={}",
				mstProcessDef.getMstStageDef().getKey(),
				varibleMap.get(mstProcessDef.getMstStageDef().getKey() + "_ID"), stage);

		if (stage!=null ) {
			Process process = processRepository
					.save(createProcess(mstProcessDef, stage, mstStatus, execution.getId()));

			String siteType = StringUtils.trimToEmpty((String) varibleMap.get("site_type"));
			if (StringUtils.isBlank(siteType))
				siteType = "A";

			execution.setVariable(mstProcessDef.getKey() + "_ID", process.getId());

			LOGGER.info("Process created with id {} ", process.getId());
		}
		updateServiceConfigStatus(serviceId, defKey);

		return varibleMap;

	}

	private void updateServiceConfigStatus(Integer serviceId, String defKey) {
		try {
			scServiceDetailRepository.flush();
			if (defKey != null && (defKey.equalsIgnoreCase("lm_test_onnet_wireline_process")
					|| defKey.equalsIgnoreCase("service_implementation_stage")
					|| defKey.equalsIgnoreCase("rf-config-p2p"))) {
				Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository.findById(serviceId);
				if (optionalServiceDetail.isPresent()) {
					ScServiceDetail scServiceDetail = optionalServiceDetail.get();
					scServiceDetail.setServiceConfigStatus(TaskStatusConstants.ACTIVE);
					scServiceDetail.setServiceConfigDate(new Timestamp(System.currentTimeMillis()));
					scServiceDetailRepository.save(scServiceDetail);
				}
			}

			else if (defKey != null && defKey.equalsIgnoreCase("product-commissioning-jeopardy")) {

				Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository.findById(serviceId);
				if (optionalServiceDetail.isPresent()) {
					ScServiceDetail scServiceDetail = optionalServiceDetail.get();
					scServiceDetail.setBillingStatus(TaskStatusConstants.ACTIVE);
					scServiceDetail.setBillingCompletedDate(new Timestamp(System.currentTimeMillis()));
					scServiceDetailRepository.save(scServiceDetail);
				}

			}
		} catch (Exception e) {
			LOGGER.error("error in updating service config status for service:{} and error:{} and defkey:{}", serviceId,
					e, defKey);
		}
	}

	/*private void updateDeliveryStatus(Integer serviceId, String defKey) {
		try {
			if (defKey != null && defKey.equalsIgnoreCase("experience-survey")) {
				Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository.findById(serviceId);
				if (optionalServiceDetail.isPresent()) {
					MstStatus mstStatus = mstStatusRepository.findByCode("ACTIVE");
					ScServiceDetail scServiceDetail = optionalServiceDetail.get();
					scServiceDetail.setIsDelivered(TaskStatusConstants.ACTIVE);
					scServiceDetail.setMstStatus(mstStatus);
					scServiceDetail.setDeliveredDate(new Timestamp(System.currentTimeMillis()));
					scServiceDetailRepository.save(scServiceDetail);
				}
			}




		} catch (Exception e) {
			LOGGER.error("updateDeliveryStatus for service:{} and error:{} and defkey:{}", serviceId,
					e, defKey);
		}
	}*/

	private void updateServiceAcceptanceStatus(Integer serviceId, String defKey) {
		try {
			if (defKey != null && defKey.equalsIgnoreCase("service-acceptance")) {
				Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository.findById(serviceId);
				if (optionalServiceDetail.isPresent()) {
					ScServiceDetail scServiceDetail = optionalServiceDetail.get();
					scServiceDetail.setServiceAceptanceStatus(TaskStatusConstants.ACTIVE);
					scServiceDetail.setServiceAceptanceDate(new Timestamp(System.currentTimeMillis()));
					scServiceDetailRepository.save(scServiceDetail);
				}
			}

		} catch (Exception e) {
			LOGGER.error("updateServiceAcceptanceStatus for service:{} and error:{} and defkey:{}", serviceId, e,
					defKey);
		}
	}


	private void updateServiceAssuranceStatus(Integer serviceId, String defKey) {
		try {
			if (defKey != null && defKey.equalsIgnoreCase("service-handover")) {
				Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository.findById(serviceId);
				if (optionalServiceDetail.isPresent()) {
					ScServiceDetail scServiceDetail = optionalServiceDetail.get();
					scServiceDetail.setAssuranceCompletionStatus(TaskStatusConstants.ACTIVE);
					scServiceDetail.setAssuranceCompletionDate(new Timestamp(System.currentTimeMillis()));
					scServiceDetailRepository.save(scServiceDetail);
				}
			}

		} catch (Exception e) {
			LOGGER.error("updateServiceAssuranceStatus for service:{} and error:{} and defkey:{}", serviceId, e,
					defKey);
		}
	}



	/**
	 * @author vivek
	 * @param //varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> initiateProcessCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		if (execution.getCurrentActivityId() != null) {
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			String defKey = execution.getCurrentActivityId().replaceAll("_end", "");

			String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
			if(StringUtils.isBlank(siteType))siteType="A";

			LOGGER.info("Process Completed for {}, serviceId={}, siteType={}", defKey, serviceId,siteType);

			try {

				Integer processId = (Integer)varibleMap.get(defKey +"_ID");
				LOGGER.info("initiateProcessCompletion::processId={} ", processId);
				Optional<Process> processOptional = processRepository.findById(processId);
				if (processOptional.isPresent()) {
					LOGGER.info("initiateProcessCompletion::process exists");
					Process process=processOptional.get();
					process.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
					process.setCompletedTime(new Timestamp(new Date().getTime()));
					processRepository.save(process);

				}
			}catch(Exception ee) {
				LOGGER.error("initiateProcessCompletion::Exception={} ", ee);
			}
		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param  // varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processActivity(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		System.out.println("====variable Map before process Activity "+varibleMap);

		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

		String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
		LOGGER.info("Activity started for {}, serviceId={}", defKey, serviceId);
		LOGGER.info("varible map for processActivity {}", varibleMap);

		MstActivityDef mstActivitDef = mstActivityDefRepository.findByKey(defKey);

		Optional<Process> process = null;

		if(varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID") !=null) {
			process = processRepository
					.findById((Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID"));
			System.out.println("After=================================="+mstActivitDef.getMstProcessDef().getKey());
		}

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS);
		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		/*if (process!=null && process.isPresent()) {
			System.out.println("============#############"+process.isPresent());

			Activity activity = activityRepository.save(createActivity(mstActivitDef, process.get(), mstStatus, execution.getId(), serviceId, siteType));

			LOGGER.info("Activity created with id {} ", activity.getId());
			execution.setVariable(mstActivitDef.getKey() + "_ID", activity.getId());

		}*/
		Activity activity=null;
		boolean reopen=false;
		if (process!=null && process.isPresent()) {

			activity=activityRepository.findByServiceIdAndMstActivityDefKeyAndSiteType(serviceId, defKey, siteType);
			if (activity != null) {
				activity.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
				activity.setProcess(process.get());
				reopen=true;
				if(activity.getSiteType()==null) {
					activity.setSiteType(siteType);
				}
				activity.setServiceId(serviceId);
				activity.setUpdatedTime(new Timestamp(new Date().getTime()));
			}
			else {
				activity = activityRepository.save(createActivity(mstActivitDef, process.get(), mstStatus,
						execution.getId(), serviceId, siteType));

			}

			/*ActivityPlan activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);


			LOGGER.info("ActivityPlan info: {} ", activityPlan);


			if (activityPlan != null) {
				activityPlan.setActivity(activity);
				if(!reopen) {
					activityPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				}
				activityPlan.setMstStatus(mstStatus);
				activityPlanRepository.save(activityPlan);
			}*/

			LOGGER.info("Activity created with id {} ", activity.getId());
			execution.setVariable(mstActivitDef.getKey() + "_ID", activity.getId());

		}

		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param  //varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processActivityCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

		String defKey = execution.getCurrentActivityId().replaceAll("_end", "");

		LOGGER.info("varible map for processActivityCompletion {}", varibleMap);

		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		LOGGER.info("Activity Completed for {}, serviceId={} siteType={}", defKey, serviceId,siteType);
		LOGGER.info("varible map after Activity {} Completed processActivityCompletion {}", defKey, varibleMap);

		try {

			Integer activityId = (Integer)varibleMap.get(defKey +"_ID");
			LOGGER.info("processActivityCompletion::activityId={} ", activityId);
			Optional<Activity> activityOptional = activityRepository.findById(activityId);
			if (activityOptional.isPresent()) {
				LOGGER.info("processActivityCompletion::activity exists");
				Activity activity=activityOptional.get();
				activity.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				activity.setCompletedTime(new Timestamp(new Date().getTime()));
				activityRepository.save(activity);
			}
		}catch(Exception ee) {
			LOGGER.error("Error on processActivityCompletion::Exception={} ", ee);
		}

		LOGGER.info("varible map after processActivityCompletion {}", varibleMap);
		return varibleMap;

	}

	/**
	 * @author vivek
	 * @param  /// varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processManulTask(DelegateTask execution) {

		boolean isReopenTask = false;
		Map<String, Object> varibleMap = execution.getVariables();
		Task task = null;
		String taskDefKey = execution.getTaskDefinitionKey();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		taskDefKey=taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");
		LOGGER.info("ManualTask started for {}, serviceId={}", taskDefKey, serviceId);

		MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);
		LOGGER.info("activityKey={}  ActivityID {}", mstTakDef.getMstActivityDef().getKey() + "_ID",
				varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID"));


		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		LOGGER.info("siteType={}", siteType);
		LOGGER.info("varible map for processManulTask {}", varibleMap);

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();

		Integer processId=null;
		Integer serviceID =(Integer) varibleMap.get(SERVICE_ID);

		if(serviceID!=null) {
			LOGGER.info("serviceID exists");
			List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_keyAndSiteType((Integer) varibleMap.get(SERVICE_ID),
					taskDefKey,siteType);
			if (!tasks.isEmpty()) {
				LOGGER.info("Task exists");
				task = tasks.stream()
						.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)||t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD))
						.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
			}
		}

		if (task != null) {
			LOGGER.info("Task exists after filtering");
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
			processId = task.getProcessId();
			task.setWfTaskId(execution.getId());
			task.setWfProcessInstId(execution.getProcessInstanceId());
			task.setWfExecutorId(execution.getExecutionId());
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
			task.setSiteType(siteType);
			task.setServiceCode(task.getOrderCode());
			isReopenTask = true;

			if("create_device_in_cramer_retry_task".equalsIgnoreCase(taskDefKey) ||
					"update_device_status_retry_task".equalsIgnoreCase(taskDefKey) ||
					"update_cramer_status_readyinservice_retry_task".equalsIgnoreCase(taskDefKey)){
				task.setIsJeopardyTask(Byte.valueOf("1"));

			}

			taskRepository.save(task);

		}else {
			LOGGER.info("Task doesn't exists");
			Activity activity =null;
			processId = (Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID");
			try {
				Integer activityId  = (Integer) varibleMap.get(mstActivitDef.getKey() + "_ID");
				if(activityId!=null) {
					Optional<Activity> activityOptional = activityRepository
							.findById(activityId);
					if (activityOptional.isPresent()) {
						activity = activityOptional.get();
						activityId = activity.getId();
						if (processId == null) processId = activity.getProcess().getId();
						LOGGER.info("Activity ID= {} processId={}", activityId,processId);
					}
				}
			}catch(Exception ee) {
				LOGGER.error(ee.getMessage(),ee);
			}

			task = createManualTask(mstTakDef,activity, mstStatus, varibleMap, execution, processId);
			Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById((Integer) varibleMap.get(SERVICE_ID));
			if(oScServiceDetail.isPresent()){
				task.setScServiceDetail(oScServiceDetail.get());
			}
		}


		LOGGER.info("ProcessId {} serviceID{}", processId,serviceID);

		if (task != null) {

//			updateLmType(serviceId,task);
//			updateDataCenterDetails(serviceId,task);

//			Timestamp dueDate = tatService.calculateDueDate(task.getMstTaskDef().getTat(),
//					task.getMstTaskDef().getOwnerGroup(), task.getCreatedTime());
//			task.setDuedate(dueDate);

//			if(serviceId!=null)updateDueDateForSpecificTask(task, serviceId, varibleMap);

			taskRepository.save(task);



//				if (taskDefKey.startsWith(AppointmentConstants.CUSTOMER_APPOINTMENT)
//						&& !execution.getTaskDefinitionKey().contains("_appchange")
//						&& !execution.getTaskDefinitionKey().contains("_reopen")
//						&& !"customer-appointment-offnet-ss".equals(taskDefKey))createAppointmentTask(task);

			String logConstant = isReopenTask ? TaskLogConstants.REOPENED : TaskLogConstants.CREATED;

			processTaskLogDetails(task, logConstant);
			List<TaskAssignment> taskAssignmentList = createAssignment(task, execution,  isReopenTask);

			/*List<TaskAssignment> taskAssignmentList = new ArrayList<>();
			taskAssignmentList.add(taskAssignment1);*/
			for(TaskAssignment taskAssignment : taskAssignmentList) {
				LOGGER.info("Process id info{} ", processId);
				taskAssignment.setProcessId(processId);

				Integer scOrderId = taskRepository.findOrderIdByTaskId(task.getId());

				switch(taskDefKey){
					case "validate_and_at_task" :
					case "eor_tx_pro_physical_AT_task" :
					case "eor_eth_pro_physical_AT_task":
					case "add_card_patching_validate_activity":
					case "eor_eqp_tx_card_rmvl_rmv_crmr_details_retry_task":
					case "eor_equip_wl_prov_install_equip_phy_conn_task":
					case "eor_equip_wl_prov_verify_BTS_config_in_NMS_task":
					case "eor_wl_ip_alloc_network_config_task":
						Optional<NwaOrderDetailsExtnd> nwaOrderDetailsExtndOptional = nwaOrderDetailsExtndRepository.findByOrderId(scOrderId);
						if (nwaOrderDetailsExtndOptional.isPresent()) {
							NwaOrderDetailsExtnd nwaOrderDetailsExtnd = nwaOrderDetailsExtndOptional.get();
							if(!nwaOrderDetailsExtnd.getFieldOps().isEmpty()){
								taskAssignment.setOwner(nwaOrderDetailsExtnd.getFieldOps());
								taskAssignment.setGroupName(nwaOrderDetailsExtnd.getFieldOps());
							}
						}
					break;
				}
				/*if("validate_and_at_task".equalsIgnoreCase(task.getMstTaskDef().getKey()) || "eor_tx_pro_physical_AT_task".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
					Optional<NwaOrderDetailsExtnd> nwaOrderDetailsExtndOptional = nwaOrderDetailsExtndRepository.findByOrderId(scOrderId);
					if (nwaOrderDetailsExtndOptional.isPresent()) {
						NwaOrderDetailsExtnd nwaOrderDetailsExtnd = nwaOrderDetailsExtndOptional.get();
							taskAssignment.setOwner(nwaOrderDetailsExtnd.getFieldOps());
							taskAssignment.setGroupName(nwaOrderDetailsExtnd.getFieldOps());
					}
				}*/


				taskAssignmentRepository.save(taskAssignment);
				if (taskAssignment.getUserName() != null) {
					processTaskLogDetails(task, TaskLogConstants.ASSIGNED, "", taskAssignment.getUserName());
					task.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));

					try {

						List<String> ccAddresses = new ArrayList<>();
						/*List<ScContractInfo> scContractInfos = scContractInfoRepository
								.findByScOrder_id(task.getScOrderId());
						ScContractInfo scContractInfo = scContractInfos.stream().findFirst().orElse(null);

						ccAddresses.add(customerSupportEmail);
						if (scContractInfo != null) {
							ccAddresses.add(scContractInfo.getAccountManagerEmail());
						}

						List<MstTaskRegion> mstTaskRegionListCIM = mstTaskRegionRepository.findByGroup("CIM");

						if (!mstTaskRegionListCIM.isEmpty()) {
							ccAddresses.addAll(mstTaskRegionListCIM.stream().filter(re -> re.getEmail() != null)
									.map(region -> region.getEmail()).collect(Collectors.toList()));
						}*/
						notificationService.notifyTaskAssignment(taskAssignment.getUserName(),
								taskAssignment.getUserName(), task.getServiceCode(), task.getMstTaskDef().getName(),
								Utils.converTimeToString(task.getDuedate()), "", false, ccAddresses);
					} catch (Exception e) {
						LOGGER.error("Error in sending customer Mail for task assignment {} ", e);
					}

				}else {
					if(taskAssignment.getGroupName()!=null && taskAssignment.getGroupName().equalsIgnoreCase("customer")) {
						try {
							LOGGER.info("processManulTask Customer notification invoked for taskDefKey{} serviceId={}",taskDefKey,serviceId);
							String customerName = StringUtils.trimToEmpty((String) varibleMap.get(MasterDefConstants.LOCAL_IT_CONTACT_NAME));
							String customerEmail = StringUtils.trimToEmpty((String) varibleMap.get(MasterDefConstants.LOCAL_IT_CONTACT_EMAIL));
							String customerUrl = appHost + "/optimus";
							notificationService.notifyCustomerTasks(customerEmail, task.getScOrderId(),
											customerName, task.getServiceCode(), task.getOrderCode(), customerUrl, task.getMstTaskDef().getName());
						}catch(Exception ee) {
							LOGGER.error("processManulTask Customer notification error ",ee);
						}

					} else if (taskAssignment.getGroupName() != null) {

						List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
								.findByGroup(taskAssignment.getGroupName());
						for (MstTaskRegion mstTaskRegion : mstTaskRegionList) {
							if (mstTaskRegion != null && mstTaskRegion.getEmail() != null) {
								try {
									List<ScContractInfo> scContractInfos = scContractInfoRepository
											.findByScOrder_id(task.getScOrderId());
									ScContractInfo scContractInfo = scContractInfos.stream().findFirst().orElse(null);
									List<String> ccAddresses = new ArrayList<>();
									ccAddresses.add(customerSupportEmail);
									if (scContractInfo != null) {
										ccAddresses.add(scContractInfo.getAccountManagerEmail());
									}

									List<MstTaskRegion> mstTaskRegionListCIM = mstTaskRegionRepository
											.findByGroup("CIM");

									if (!mstTaskRegionListCIM.isEmpty()) {
										ccAddresses.addAll(
												mstTaskRegionListCIM.stream().filter(re -> re.getEmail() != null)
														.map(region -> region.getEmail()).collect(Collectors.toList()));
									}
									notificationService.notifyTaskAssignment(mstTaskRegion.getEmail(),
											mstTaskRegion.getEmail(), task.getServiceCode(),
											task.getMstTaskDef().getName(), Utils.converTimeToString(task.getDuedate()),
											"", true, ccAddresses);
								} catch (Exception e) {
									LOGGER.error("Error in sending customer Mail for task assignment {} ", e);
								}
							}
						}

					}
				}


				createTatTime(task, taskAssignment);
			}

			execution.setVariable(mstTakDef.getKey() + "_ID", task.getId());
			LOGGER.info("Task created with id {} ", task.getId());
		}
		//updateDeliveryStatus(serviceID, taskDefKey);

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("track-offnet-lm-delivery")) {
			processTrackOffnet(task);
		}

		return varibleMap;
	}

	private void updateLmType(Integer serviceId, Task task) {
//		try {
//			List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
//					.findByScServiceDetailIdAndAttributeNameInAndScComponent_componentNameAndScComponent_siteType(
//							serviceId,
//							Arrays.asList("lastmileProvider", "lastmileType", "lastMileScenario", "downtimeDuration",
//									"fromTime", "toTime", "isTxDowntimeReqd", "isIpDownTimeRequired"),
//							"LM", task.getSiteType() == null ? "A" : task.getSiteType());
//			if (!scComponentAttributes.isEmpty()) {
//				Map<String, String> lmMap = new HashMap<String, String>();
//				scComponentAttributes.forEach(scComponentAtt -> {
//					lmMap.put(scComponentAtt.getAttributeName(), scComponentAtt.getAttributeValue());
//				});
//				task.setLmProvider(lmMap.get("lastMileProvider"));
//				task.setLastMileScenario(lmMap.get("lastMileScenario"));
//				task.setLmType(lmMap.get("lmType"));
//				if (lmMap.containsKey("isIpDownTimeRequired")
//						&& "true".equalsIgnoreCase(lmMap.get("isIpDownTimeRequired"))) {
//					task.setIsIpDownTimeRequired("Yes");
//
//				} else {
//					task.setIsIpDownTimeRequired("No");
//
//				}
//				if (lmMap.containsKey("isTxDowntimeReqd") && "true".equalsIgnoreCase(lmMap.get("isTxDowntimeReqd"))) {
//					task.setIsTxDowntimeReqd("Yes");
//
//				} else {
//					task.setIsTxDowntimeReqd("No");
//
//				}
//				String downtimeDuration = StringUtils.trimToEmpty(lmMap.get("downtimeDuration"));
//				String fromTime = StringUtils.trimToEmpty(lmMap.get("fromTime"));
//				String toTime = StringUtils.trimToEmpty(lmMap.get("toTime"));
//				if (StringUtils.isNotBlank(downtimeDuration)) {
//					Date downtimeDurationDate = new SimpleDateFormat("yyyy-MM-dd").parse(downtimeDuration);
//					downtimeDuration = DateUtil.convertDateToMMMString(downtimeDurationDate);
//					task.setDowntime(downtimeDuration + "<BR>" + fromTime + " - " + toTime);
//				}
//			}
//			task.setOrderSubCategory(task.getScServiceDetail().getOrderSubCategory());
//		} catch (Exception e) {
//			LOGGER.error("erro in updating lm type:{}", e);
//		}
	}




	private void processTrackOffnet(Task finalTask) {

		LOGGER.info("track-offnet-lm-delivery Task created with id {} ", finalTask.getId());
		Optional.ofNullable(taskRepository.
				findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(finalTask.getServiceId(),
						"define-offfnet-project-plan"))
				.ifPresent(defineOffnetTask -> {
					LOGGER.info("define-offfnet-project-plan Task Id {} ServiceId={}",defineOffnetTask.getId(),defineOffnetTask.getServiceCode());
					Optional.ofNullable(taskDataRepository.findFirstByTask_idOrderByCreatedTimeDesc(defineOffnetTask.getId()))
							.ifPresent(taskData -> {
								LOGGER.info("update define-offfnet-project-plan_output to track-offnet-lm-delivery taskData {} ServiceId={}",taskData,finalTask.getServiceCode());
								TaskData taskData1 = new TaskData();
								taskData1.setData(taskData.getData());
								taskData1.setName(finalTask.getMstTaskDef().getName());
								taskData1.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
								taskData1.setTask(finalTask);
								taskDataRepository.save(taskData1);
							});
				});
	}

	/**
	 * @param task
	 * @param serviceId
	 * @param varibleMap
	 */
	private void updateDueDateForSpecificTask(Task task, Integer serviceId, Map<String, Object> varibleMap) {

		if (task.getMstTaskDef().getKey().equalsIgnoreCase("lm-confirm-site-readiness-details")) {

			updateDueDateOfMux(task, varibleMap, serviceId);

		} else if (task.getMstTaskDef().getKey().equalsIgnoreCase("advanced-enrichment")) {
		}

	}

	private void updateDueDateOfMux(Task task, Map<String, Object> varibleMap, Integer serviceId) {
	}

	/**
	 * @param // execution
	 * @param task
	 * @param // tasks     used to create appointment
	 */
	private void createAppointmentTask(Task task) {
		Appointment appointment = new Appointment();
		appointment.setAppointmentDate(tatService.calculateDueDate(960, "optimus_regus", task.getCreatedTime()));
		appointment.setServiceId(task.getServiceId());
		appointment.setTask(task);
		appointment.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.ACTIVE));
		appointment.setAppointmentType(getTaskType(task.getMstTaskDef().getKey()));
		appointment.setMstAppointmentSlot(getMstSlots(1));
		appointmentRepository.save(appointment);
	}



	private String getTaskType(String key) {
		String value ="";
		if(key.contains("arrange-field-engineer")) {
			value = key.replace("arrange-field-engineer-","");
		}else if(key.contains("customer-appointment")) {
			value = key.replace("customer-appointment-","");
		}else if(key.contains("select-vendor")) {
			value = key.replace("select-vendor-","");
		}
		return value;
	}

	/**
	 * @return
	 */
	private MstAppointmentSlots getMstSlots(Integer id) {
		return mstSlotRepository.findById(id).orElse(null);
	}

	private void createTatTime(Task task, TaskAssignment taskAssignment) {
//		TaskTatTime taskTatTime = new TaskTatTime();
//		taskTatTime.setTask(task);
//		taskTatTime.setStartTime(new Timestamp(new Date().getTime()));
//
//		if (taskAssignment != null) {
//			taskTatTime.setAssignee(taskAssignment.getGroupName());
//
//		}
//		taskTatTime.setCreatedTime(new Timestamp(new Date().getTime()));
//		taskTatTimeRepository.save(taskTatTime);
	}

	/**
	 * @author vivek
	 * @param  //varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processManulTaskCompletion(DelegateTask execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		String taskDefKey = execution.getTaskDefinitionKey();
		if (taskDefKey != null) {
			taskDefKey=taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");

			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			LOGGER.info("ManulTask Completed for {}, serviceId={}", taskDefKey, serviceId);
			LOGGER.info("varible map for processManulTaskCompletion {}", varibleMap);

			Task task = null;
			if (varibleMap.containsKey(execution.getTaskDefinitionKey() + "_ID")) {

				Integer taskId = (Integer) varibleMap.get(taskDefKey + "_ID");
				LOGGER.info("Task Id {} ", varibleMap.get(taskDefKey + "_ID"));
				Optional<Task> optionalTask = taskRepository.findById(taskId);
				if (optionalTask.isPresent()) {
					task = optionalTask.get();
					if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {

						TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);

						if (taskTatTime != null) {
							taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
							taskTatTimeRepository.save(taskTatTime);
						}
					}
					//processTaskLogDetails(task, TaskLogConstants.CLOSED);
				}
			}
			String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
			if(StringUtils.isBlank(siteType))siteType="A";


			updateServiceConfigStatus(serviceId, taskDefKey);
			updateServiceAcceptanceStatus(serviceId, taskDefKey);
			updateServiceAssuranceStatus(serviceId, taskDefKey);



			LOGGER.info("processManulTaskCompletion");

		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param  //varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Task processServiceTask(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		String taskDefKey=execution.getCurrentActivityId();
		taskDefKey=taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");

		Task task = null;

		boolean isReopenTask = false;

		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		LOGGER.info("ServiceTask started for {}, serviceId={}", taskDefKey, serviceId);
		LOGGER.info("varible map for processServiceTask {}", varibleMap);

		MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

		MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();
		Integer processId = (Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID");

		LOGGER.info("Values of processId={}", processId);
//		if(!"wait-for-downtime-from-customer".equals(taskDefKey)){
//			LOGGER.info("task other that wait-for-downtime-from-customer");
//			if(serviceId!=null) {
//				List<Task> tasks = taskRepository.findByServiceIdAndMstTaskDef_keyAndSiteType((Integer) varibleMap.get(SERVICE_ID),
//						execution.getCurrentActivityId(),siteType);
//				LOGGER.info("task already presnet or not{}", tasks);
//				if (!tasks.isEmpty()) {
//					task = tasks.stream()
//							.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
//									|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
//									|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
//									|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED))
//							.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
//
//				}
//			}
//		}
		if (task != null) {
			isReopenTask = true;
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
			task.setWfTaskId(execution.getId());
			task.setWfProcessInstId(execution.getProcessInstanceId());
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
//			task.setSiteType(siteType);
			taskRepository.save(task);

		} else {
			Activity activity = null;
			Integer activityId = (Integer) varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID");
			if (activityId != null) {
				Optional<Activity> activityOptional = activityRepository
						.findById((Integer) varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID"));
				if (activityOptional.isPresent()) {
					activity = activityOptional.get();
					task = createServiceTask(mstTakDef, activity, mstStatus, varibleMap, execution, processId);
					Optional<ScServiceDetail> oScServiceDetail = scServiceDetailRepository.findById((Integer) varibleMap.get(SERVICE_ID));
					if(oScServiceDetail.isPresent()){
						task.setScServiceDetail(oScServiceDetail.get());
					}
					taskRepository.save(task);
				}
			}
		}

		if (task != null) {

			updateLmType(serviceId,task);
			String logConstant = isReopenTask ? TaskLogConstants.REOPENED : TaskLogConstants.CREATED;
			processTaskLogDetails(task, logConstant);
			execution.setVariable(mstTakDef.getKey() + "_ID", task.getId());
			LOGGER.info("Task created with id {} ", task.getId());
		}

		return task;
	}


	public Map<String, Object> processServiceTaskCompletion(DelegateExecution execution) {
		return processServiceTaskCompletion(execution,null);
	}

	/**
	 * @author vivek
	 * @param errorMessage
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processServiceTaskCompletionByTaskKey(DelegateExecution execution,String taskDefKey,String errorMessage) {
		Map<String, Object> varibleMap = execution.getVariables();
		try {
			taskDefKey=taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
			LOGGER.info("ServiceTask Completed for {}, serviceId={}", taskDefKey, serviceId);

			Task task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyOrderByCreatedTimeDesc(serviceId,taskDefKey);
			if (task!=null) {
				if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {
					TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);
					if (taskTatTime != null) {
						taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
						taskTatTimeRepository.save(taskTatTime);
					}
				}
				task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
				task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
				task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
				taskRepository.save(task);
				processTaskLogDetails(task,(StringUtils.isNotBlank(errorMessage))? TaskLogConstants.FAILED:TaskLogConstants.CLOSED,errorMessage,"");
			}

			String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
			if(StringUtils.isBlank(siteType))siteType="A";


			LOGGER.info("processServiceTaskCompletion");

		}catch(Exception ee) {
			LOGGER.error("processServiceTaskCompletion error",ee);
		}
		return varibleMap;
	}

	public Map<String, Object> processServiceTaskCompletion(DelegateExecution execution,String errorMessage) {
		Map<String, Object> varibleMap = execution.getVariables();
		try {

			if (execution.getCurrentActivityId() != null) {
				String taskDefKey=execution.getCurrentActivityId();
				taskDefKey=taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");
				Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
				LOGGER.info("ServiceTask Completed for {}, serviceId={}", taskDefKey, serviceId);

				if (varibleMap.containsKey(taskDefKey+ "_ID")) {
					Integer taskId = (Integer) varibleMap.get(taskDefKey+ "_ID");
					LOGGER.info("Task Id {} ", varibleMap.get(taskDefKey+ "_ID"));
					Optional<Task> optionalTask = taskRepository.findById(taskId);
					if (optionalTask.isPresent()) {
						Task task = optionalTask.get();
						if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {
							TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);
							if (taskTatTime != null) {
								taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
								taskTatTimeRepository.save(taskTatTime);
							}
						}
						task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
						task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
						task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
						task.setScOrderId(serviceId);

						NwaEorEquipDetails nwaEorEquipDetails = nwaEorEquipDetailsRepository.findByOrderCode(task.getOrderCode());
						if(nwaEorEquipDetails != null) {
							String taskKey = task.getMstTaskDef().getKey().toLowerCase();
							switch (taskKey) {
								case "eor_tx_pro_cramer_status_installed_task":
									if (nwaEorEquipDetails != null && "SDNWNOC_NWA".equalsIgnoreCase(nwaEorEquipDetails.getSdNocSaNoc()) ) {
										LOGGER.info("isSdNoc {} ", true);
										varibleMap.put("isSdNoc", true);
									} else {
										LOGGER.info("isSdNoc {} ", false);
										varibleMap.put("isSdNoc", false);
									}
							}
						}



						if(taskDefKey.contains("retry")){
							if(varibleMap.get("action") == "retry" ){
								LOGGER.info("retry {} ", true);
								varibleMap.put("retry", true);
								task.setIsJeopardyTask((byte) 1);
							}else {
								LOGGER.info("isError {} ", false);
								varibleMap.put("isError", false);
								varibleMap.put("retry", false);
							}

						}

						taskRepository.save(task);

						MfTaskTrailBean mfTaskTrailBean = new MfTaskTrailBean();
						mfTaskTrailBean.setTaskId(task.getId());
						mfTaskTrailBean.setAction("Service Task");
						mfTaskTrailBean.setCompletedBy(task.getAssignee());
						mfTaskTrailBean.setCreatedTime(Timestamp.valueOf(LocalDateTime.now()));
						mfTaskTrailBean.setDescription("Service Task complete " );
						mfTaskTrailBean.setTaskName(task.getMstTaskDef().getName());
						ScOrder scOrder = task.getScOrder();
						if(scOrder != null){
							mfTaskTrailBean.setScenario(task.getScOrder().getScenarioType());
							System.out.println("====== Trail ScenarioType set");
						}else {
							System.out.println("====== Trail ScenarioType is not set");
						}
						mfTaskTrailBean.setUserGroup(task.getMstTaskDef().getAssignedGroup());
						//mfTaskTrailBean.setComments("Service task "+ task.getMstTaskDef().getName());
						taskService.setTaskTrail(mfTaskTrailBean);

						processTaskLogDetails(task,(StringUtils.isNotBlank(errorMessage))? TaskLogConstants.FAILED:TaskLogConstants.CLOSED,errorMessage,"");
					}

				}
				String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
				if(StringUtils.isBlank(siteType))siteType="A";
				LOGGER.info("siteType={}", siteType);

				LOGGER.info("processServiceTaskCompletion");
			}
		}catch(Exception ee) {
			LOGGER.error("processServiceTaskCompletion error",ee);
		}

		return varibleMap;
	}



	/**
	 * @author vivek
	 * @param // errorMessage
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processServiceTaskCompletionWithAction(DelegateExecution execution,String message,String action) {
		Map<String, Object> varibleMap = execution.getVariables();
		try {

			if (execution.getCurrentActivityId() != null) {
				String taskDefKey=execution.getCurrentActivityId();
				taskDefKey=taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");
				Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
				LOGGER.info("ServiceTask Completed for {}, serviceId={}", taskDefKey, serviceId);

				if (varibleMap.containsKey(taskDefKey+ "_ID")) {
					Integer taskId = (Integer) varibleMap.get(taskDefKey+ "_ID");
					LOGGER.info("Task Id {} ", varibleMap.get(taskDefKey+ "_ID"));
					Optional<Task> optionalTask = taskRepository.findById(taskId);
					if (optionalTask.isPresent()) {
						Task task = optionalTask.get();
						if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {
							TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);
							if (taskTatTime != null) {
								taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
								taskTatTimeRepository.save(taskTatTime);
							}
						}
						task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
						task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
						task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
						taskRepository.save(task);
						processTaskLogDetails(task,action,message,"");
					}

				}
				String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
				if(StringUtils.isBlank(siteType))siteType="A";


				LOGGER.info("processServiceTaskCompletion");
			}
		}catch(Exception ee) {
			LOGGER.error("processServiceTaskCompletion error",ee);
		}
		return varibleMap;
	}


	public Map<String, Object> processServiceTaskCompletion(DelegateExecution execution,String errorMessage,Task task) {


		if (task.getTatTimes() != null && !task.getTatTimes().isEmpty()) {
			TaskTatTime taskTatTime = task.getTatTimes().stream().findFirst().orElse(null);
			if (taskTatTime != null) {
				taskTatTime.setEndTime(new Timestamp(new Date().getTime()));
				taskTatTimeRepository.save(taskTatTime);
			}
		}
		task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
		task.setUpdatedTime(Timestamp.valueOf(LocalDateTime.now()));
		task.setCompletedTime(Timestamp.valueOf(LocalDateTime.now()));
		taskRepository.save(task);
		processTaskLogDetails(task, TaskLogConstants.CLOSED,errorMessage,"");
		Map<String, Object> varibleMap = execution.getVariables();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		LOGGER.info("processServiceTaskCompletion");

		return varibleMap;
	}

	private void processTaskLogDetails(Task task, String action,String description,String user) {
		LOGGER.info("Inside Process Task Log Details Method with .... ");

		ProcessTaskLog processTaskLog = createProcessTaskLog(task, action,description,user);
		processTaskLogRepository.save(processTaskLog);

	}

	private void processTaskLogDetails(Task task, String action) {
		processTaskLogDetails(task,action,null,"");
	}

	private void updateDataCenterDetails(Integer serviceId, Task task) {

		List<String> cpeRelatedTaskName=Arrays.asList("pr-for-cpe-order","po-for-cpe-order","po-release-for-cpe-order","confirm-material-availability",
				"track-cpe-delivery","provide-wbsglcc-details","arrange-field-engineer-cpe-installation","customer-appointment-cpe-installation",
				"install-cpe","cpe_invoice_jeopardy","dispatch-cpe");
		List<String> rfRelatedTaskName=Arrays.asList("lm-create-rf-mrn","deliver-rf-equipment");
		List<String> muxRelatedTaskName=Arrays.asList("lm-create-mux-mrn","lm-deliver-mux","arrange-field-engineer-mux-installation",
				"customer-appointment-mux-installation", "lm-install-mux","raise-planned-event","lm-integrate-mux");

		String attributeName="";
		if(muxRelatedTaskName.stream().anyMatch(task.getMstTaskDef().getKey()::equalsIgnoreCase)){
			attributeName="muxDistributionCenterName";
		}else if(rfRelatedTaskName.stream().anyMatch(task.getMstTaskDef().getKey()::equalsIgnoreCase)){
			attributeName="rfDistributionCenterName";
		}else if(cpeRelatedTaskName.stream().anyMatch(task.getMstTaskDef().getKey()::equalsIgnoreCase)){
			attributeName="distributionCenterName";
		}
		if(!attributeName.isEmpty()) {
//			LOGGER.info("Data center attribute name and serviceId"+attributeName+" "+serviceId);
//			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
//					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId,
//							attributeName, "LM", task.getSiteType() == null ? "A" : task.getSiteType());
//			if (Objects.nonNull(scComponentAttribute)) {
//				LOGGER.info("Data center attribute value and taskId "+scComponentAttribute.getAttributeValue()+" "+task.getId());
//				task.setDistributionCenterName(scComponentAttribute.getAttributeValue());
//			}
		}

	}
}
