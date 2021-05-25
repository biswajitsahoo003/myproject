package com.tcl.dias.servicefulfillmentutils.service.v1;

import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_GSC_FLOW_GROUP_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_COMPONENT_ID;
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

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.Activity;
import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.Appointment;
import com.tcl.dias.servicefulfillment.entity.entities.MstActivityDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstAppointmentSlots;
import com.tcl.dias.servicefulfillment.entity.entities.MstProcessDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstStageDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstStatus;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskRegion;
import com.tcl.dias.servicefulfillment.entity.entities.Process;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessTaskLog;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Stage;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskAssignment;
import com.tcl.dias.servicefulfillment.entity.entities.TaskData;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.entities.TaskTatTime;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityRepository;
import com.tcl.dias.servicefulfillment.entity.repository.AppointmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.DownTimeDetailsRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstActivityDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstProcessDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstSlotRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstStageDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstStatusRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskRegionRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessTaskLogRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScContractInfoRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StageRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskAssignmentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskDataRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskTatTimeRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AppointmentConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.constants.TaskLogConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.factory.ProjectPlanInitiateService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
public class WorkFlowService extends WorkFlowAbstractService {
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkFlowService.class);

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
	StagePlanRepository stagePlanRepository;

	@Autowired
	ProcessPlanRepository processPlanRepository;

	@Autowired
	ActivityPlanRepository activityPlanRepository;

	@Autowired
	TaskPlanRepository taskPlanRepository;

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
	ProjectPlanInitiateService projectPlanInitiateService;

	@Autowired
	NotificationService notificationService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	MstTaskRegionRepository mstTaskRegionRepository;

	@Autowired
	TaskDataRepository taskDataRepository;
	
	@Value("${app.host}")
	String appHost;
	

	@Value("${customer.support.email}")
	String customerSupportEmail;
	
	@Autowired
	ScContractInfoRepository scContractInfoRepository;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	@Autowired
	ScComponentAttributesRepository scComponentAttributesRepository;

	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	MstStatusRepository mstStatusRepository;
	
	@Autowired
	DownTimeDetailsRepository downTimeDetailsRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;


	/**
	 * @author vivek
	 * @param varibleMap
	 * @param string
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processStage(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		
		boolean reopen = false;
		if (execution.getCurrentActivityId() != null) {
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
			LOGGER.info("Stage started for {}, serviceId={}", defKey, serviceId);

			// LOGGER.info("varible map {}", varibleMap);

			MstStageDef mstStageDef = mstStageDefRepository.findByKey(defKey);
			MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS);
			Stage stage= stageRepository.findFirstByServiceIdAndMstStageDef_keyOrderByIdDesc(serviceId, defKey);
			
			if (stage == null) {
				stage = stageRepository.save(createStatge(mstStageDef, mstStatus, varibleMap, execution.getId()));
			} else {
				reopen=true;
				stage.setMstStatus(mstStatus);
			}
			StagePlan stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
			
			LOGGER.info("stagePlan fetch: {} ", stagePlan);

			if (stagePlan != null) {
				stagePlan.setStage(stage);
				if(!reopen) {
				stagePlan.setActualStartTime(new Timestamp(new Date().getTime()));
				}
				stagePlan.setMstStatus(mstStatus);
				stagePlanRepository.save(stagePlan);
			}

			execution.setVariable(mstStageDef.getKey() + "_ID", stage.getId());
			LOGGER.info("Stage created Id {} ", stage.getId());

		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @param string
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processStageCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		if (execution.getCurrentActivityId() != null) {

			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			String defKey = execution.getCurrentActivityId().replaceAll("_end", "");
			LOGGER.info("StageCompletion for {}, serviceId={}", defKey, serviceId);

			StagePlan stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);

			LOGGER.info("stagePlan fetch: {} ", stagePlan);
			try {
				if (stagePlan != null) {
					stagePlan.setActualEndTime(new Timestamp(new Date().getTime()));
					stagePlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
					stagePlanRepository.save(stagePlan);

					Stage stage = stagePlan.getStage();
					if (stage != null) {
						stage.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
						stage.setCompletedTime(new Timestamp(new Date().getTime()));
						stageRepository.save(stage);
					}else {
						LOGGER.info("stage_is_null_in_plan");
						Integer stageId = (Integer) varibleMap.get(defKey + "_ID");
						LOGGER.info("processStageCompletion::stageId={} ", stageId);
						Optional<Stage> stageOptional = stageRepository.findById(stageId);
						if (stageOptional.isPresent()) {
							LOGGER.info("processStageCompletion::stage exists");
							stage = stageOptional.get();
							stage.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
							stage.setCompletedTime(new Timestamp(new Date().getTime()));
							stageRepository.save(stage);
						}
					}
				} else {
					Integer stageId = (Integer) varibleMap.get(defKey + "_ID");
					
					if(stageId!=null) {
						LOGGER.info("processStageCompletion::stageId={} ", stageId);
						Optional<Stage> stageOptional = stageRepository.findById(stageId);
						if (stageOptional.isPresent()) {
							LOGGER.info("processStageCompletion::stage exists");
							Stage stage = stageOptional.get();
							stage.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
							stage.setCompletedTime(new Timestamp(new Date().getTime()));
							stageRepository.save(stage);
						}
					}else {
						Stage stage = stageRepository.findFirstByServiceIdAndMstStageDef_keyOrderByIdDesc(serviceId, defKey);
						LOGGER.info("processStageCompletion::stageId2={} ", stage.getId());
						if(stage!=null) {
							stage.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
							stage.setCompletedTime(new Timestamp(new Date().getTime()));
							stageRepository.save(stage);
						}
					}
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
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> initiateProcess(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		
		String orderCategory = (String) varibleMap.get(MasterDefConstants.ORDER_CATEGORY);


		String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
		LOGGER.info("Process started for {}, serviceId={}", defKey, serviceId);

		if(varibleMap.get("remainderCycle")==null) {
			varibleMap.put("remainderCycle", "R60/PT24H");
			execution.setVariable("remainderCycle", "R60/PT24H");
		}
		// LOGGER.info("varible map {}", varibleMap);

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
		String siteType=null;
		siteType = StringUtils.trimToEmpty((String) varibleMap.get("site_type"));
		if (StringUtils.isBlank(siteType)) {
			siteType = "A";
		}
		boolean reopen=false;
		Process process=null;
		if (stage != null) {

			process = processRepository.findByServiceIdAndMstProcessDefKeyAndSiteTye(serviceId, defKey, siteType);

			if (process != null) {

				process.setUpdatedTime(new Timestamp(new Date().getTime()));
				process.setWfProcInstId(execution.getId());
				process.setStage(stage);
				process.setServiceId(serviceId);
				process.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
				reopen = true;
				processRepository.save(process);

			} else {
				process = processRepository
						.save(createProcess(mstProcessDef, stage, mstStatus, execution.getId(), serviceId, siteType));
			}

			ProcessPlan processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey,
					siteType);

			LOGGER.info("processPlan info: {} ", processPlan);

			if (processPlan != null) {
				processPlan.setProcess(process);
				if(!reopen) {
				processPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				}
				processPlan.setMstStatus(mstStatus);

				processPlanRepository.save(processPlan);
			}
			execution.setVariable(mstProcessDef.getKey() + "_ID", process.getId());

			LOGGER.info("Process created with id {} ", process.getId());
		}
		if (defKey.equalsIgnoreCase("cpe-implementation-process") || defKey.equalsIgnoreCase("cpe-config-process")) {
			setCpeRequiredOrNot(serviceId, defKey, execution,siteType,varibleMap.get(MasterDefConstants.ORDER_TYPE)!=null?(String) varibleMap.get(MasterDefConstants.ORDER_TYPE):null,orderCategory);
		}
		updateServiceConfigStatus(serviceId, defKey);

		return varibleMap;

	}
	
	private void setCpeRequiredOrNot(Integer serviceId, String processDefKey, DelegateExecution execution,
			String siteType, String orderType,String orderCategory) {
		if (orderCategory!=null && !orderCategory.equalsIgnoreCase("ADD_SITE") && orderType != null && orderType.equalsIgnoreCase("MACD")) {
			ScServiceAttribute cpeRequired = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(
							serviceId, "cpe_chassis_changed");
			if (cpeRequired != null && cpeRequired.getAttributeValue() != null
					&& cpeRequired.getAttributeValue().equalsIgnoreCase("No")) {

				execution.setVariable("cpeSiScope","");
				execution.setVariable("isCPEArrangedByCustomer",true);

			}

		}

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
					scServiceDetail.setServiceConfigDate(new Timestamp(new Date().getTime()));
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
					scServiceDetail.setDeliveredDate(new Timestamp(new Date().getTime()));
					scServiceDetailRepository.save(scServiceDetail);
				}
			}
			
			

			
		} catch (Exception e) {
			LOGGER.error("updateDeliveryStatus for service:{} and error:{} and defkey:{}", serviceId,
					e, defKey);
		}
	}*/
	
	private void updateServiceAcceptanceStatus(Integer serviceId, String defKey, Map<String, Object> varibleMap) {
		try {
			if(defKey != null){
				if (defKey.equalsIgnoreCase("service-acceptance") || defKey.equalsIgnoreCase("sdwan-cgw-service-acceptance")) {
					Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository.findById(serviceId);
					if (optionalServiceDetail.isPresent()) {
						ScServiceDetail scServiceDetail = optionalServiceDetail.get();
						scServiceDetail.setServiceAceptanceStatus(TaskStatusConstants.ACTIVE);
						scServiceDetail.setServiceAceptanceDate(new Timestamp(new Date().getTime()));
						scServiceDetailRepository.save(scServiceDetail);
					}
				}else if(defKey.equalsIgnoreCase("sdwan-service-acceptance")){
					Integer solutionId = (Integer) varibleMap.get("solutionId");
					LOGGER.info("SdwanServiceAcceptance for solutionId:{}, service id:{}",solutionId, serviceId);
					List<Integer> scOverlayUnderlayServiceIdList=scSolutionComponentRepository.getServiceDetailsBySolutionIdAndOverlayId(solutionId, serviceId, serviceId, "Y");
					if(scOverlayUnderlayServiceIdList!=null && !scOverlayUnderlayServiceIdList.isEmpty()){
						LOGGER.info("SdwanServiceAcceptance serviceIdList size:{}",scOverlayUnderlayServiceIdList.size());
						scServiceDetailRepository.updateServiceAcceptanceStatus(TaskStatusConstants.ACTIVE,new Timestamp(new Date().getTime()),scOverlayUnderlayServiceIdList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("updateServiceAcceptanceStatus for service:{} and error:{} and defkey:{}", serviceId, e,
					defKey);
		}
	}
	
	
	private void updateServiceAssuranceStatus(Integer serviceId, String defKey, Map<String, Object> varibleMap) {
		try {
			if (defKey != null) {
				if(defKey.equalsIgnoreCase("service-handover")|| defKey.equalsIgnoreCase("assurance_handover_process") || defKey.equalsIgnoreCase("sdwan-cgw-service-handover")){
					Optional<ScServiceDetail> optionalServiceDetail = scServiceDetailRepository.findById(serviceId);
					if (optionalServiceDetail.isPresent()) {
						ScServiceDetail scServiceDetail = optionalServiceDetail.get();
						scServiceDetail.setAssuranceCompletionStatus(TaskStatusConstants.ACTIVE);
						scServiceDetail.setAssuranceCompletionDate(new Timestamp(new Date().getTime()));
						scServiceDetailRepository.save(scServiceDetail);
					}
				}else if(defKey.equalsIgnoreCase("sdwan-service-handover")){
					Integer solutionId = (Integer) varibleMap.get("solutionId");
					LOGGER.info("SdwanAssuranceHandover for solutionId:{}, service id:{}",solutionId, serviceId);
					List<Integer> scOverlayUnderlayServiceIdList=scSolutionComponentRepository.getServiceDetailsBySolutionIdAndOverlayId(solutionId, serviceId, serviceId, "Y");
					if(scOverlayUnderlayServiceIdList!=null && !scOverlayUnderlayServiceIdList.isEmpty()){
						LOGGER.info("SdwanAssuranceHandover serviceIdList size:{}",scOverlayUnderlayServiceIdList.size());
						scServiceDetailRepository.updateServiceAssuranceStatus(TaskStatusConstants.ACTIVE,new Timestamp(new Date().getTime()),scOverlayUnderlayServiceIdList);
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("updateServiceAssuranceStatus for service:{} and error:{} and defkey:{}", serviceId, e,
					defKey);
		}
	}
	
	

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> initiateProcessCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();

		if (execution.getCurrentActivityId() != null) {
			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			String defKey = execution.getCurrentActivityId().replaceAll("_end", "");
			

			String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
			
			String productName = StringUtils.trimToEmpty((String)varibleMap.get(MasterDefConstants.PRODUCT_NAME));

			if(StringUtils.isBlank(siteType))siteType="A";
			
			LOGGER.info("Process Completed for {}, serviceId={}, siteType={}", defKey, serviceId,siteType);

			ProcessPlan processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey,
					siteType);

			LOGGER.info("processPlan info: {} ", processPlan);
			try {
				if (processPlan != null) {
					processPlan.setActualEndTime(new Timestamp(new Date().getTime()));
					processPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
	
					processPlanRepository.save(processPlan);
					
					Process process = processPlan.getProcess();
					if(process!=null) {
						process.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
						process.setCompletedTime(new Timestamp(new Date().getTime()));
						processRepository.save(process);
					
					}else{
						LOGGER.info("process_is_null_in_plan");
						Integer processId = (Integer)varibleMap.get(defKey +"_ID");
						LOGGER.info("initiateProcessCompletion::processId={} ", processId);
						Optional<Process> processOptional = processRepository.findById(processId);
						if (processOptional.isPresent()) {
							LOGGER.info("initiateProcessCompletion::process exists");
							process=processOptional.get();
							process.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
							process.setCompletedTime(new Timestamp(new Date().getTime()));
							processRepository.save(process);
						}
					}
				}else{
					Integer processId = (Integer)varibleMap.get(defKey +"_ID");
                    LOGGER.info("initiateProcessCompletion::processId={} ", processId);
                    if(processId!=null) {
                        Optional<Process> processOptional = processRepository.findById(processId);
                        if (processOptional.isPresent()) {
                            LOGGER.info("initiateProcessCompletion::process exists");
                            Process process=processOptional.get();
                            process.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
                            process.setCompletedTime(new Timestamp(new Date().getTime()));
                            processRepository.save(process);
                        }
                    }else {                         
                        Process process=  processRepository.findByServiceIdAndMstProcessDefKeyAndSiteTye(serviceId, defKey, siteType);
                       if(process!=null) {
                            LOGGER.info("initiateProcessCompletion::processId2={} ", process.getId());
                            process.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
                            process.setCompletedTime(new Timestamp(new Date().getTime()));
                            processRepository.save(process);
                       }
                    }
				}
				
				if ("NPL".equalsIgnoreCase(productName) && defKey.equalsIgnoreCase("assurance_handover_process")) {
					updateServiceAssuranceStatus(serviceId, defKey,varibleMap);
				}

				


			}catch(Exception ee) {
				LOGGER.error("initiateProcessCompletion::Exception={} ", ee);
			}
		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processActivity(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

		String defKey = execution.getCurrentActivityId().replaceAll("_start", "");
		LOGGER.info("Activity started for {}, serviceId={}", defKey, serviceId);
		// LOGGER.info("varible map {}", varibleMap);

		MstActivityDef mstActivitDef = mstActivityDefRepository.findByKey(defKey);

		Optional<Process> process = null;
				
		if(varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID") !=null) {
			process = processRepository
				.findById((Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID"));
		}

		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS);
		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
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

			ActivityPlan activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);				
		
			
			LOGGER.info("ActivityPlan info: {} ", activityPlan);	
			

			if (activityPlan != null) {
				activityPlan.setActivity(activity);
				if(!reopen) {
				activityPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				}
				activityPlan.setMstStatus(mstStatus);
				activityPlanRepository.save(activityPlan);
			}

			LOGGER.info("Activity created with id {} ", activity.getId());
			execution.setVariable(mstActivitDef.getKey() + "_ID", activity.getId());

		}
		
        if(defKey!=null && defKey.equals("e2e_service_testing_activity")) {
        	LOGGER.info("e2e_service_testing_activity check-usage for serviceId:{}", serviceId);
        	ScServiceAttribute serviceType = scServiceAttributeRepository
					.findFirstByScServiceDetail_idAndAttributeNameOrderByIdDesc(serviceId, "Service type");
			if (serviceType != null && serviceType.getAttributeValue() != null
					&& serviceType.getAttributeValue().toLowerCase().contains("usage")) {
				LOGGER.info("e2e_service_testing_activity_usage_based for serviceId:{}", serviceId);
				execution.setVariable("burstableOrder", true);
			}
		}
		

		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processActivityCompletion(DelegateExecution execution) {
		Map<String, Object> varibleMap = execution.getVariables();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
	
		String defKey = execution.getCurrentActivityId().replaceAll("_end", "");
		
		
		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		
		LOGGER.info("Activity Completed for {}, serviceId={} siteType={}", defKey, serviceId,siteType);
		
		ActivityPlan activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);			
				
		LOGGER.info("ActivityPlan info: {} serviceId={} siteType={}", activityPlan,serviceId,siteType);
		try {
			if (activityPlan != null) {
				
				activityPlan.setActualEndTime(new Timestamp(new Date().getTime()));
				activityPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				activityPlanRepository.save(activityPlan);
				
				Activity activity = activityPlan.getActivity();
				if(activity!=null) {
					LOGGER.info("processActivityCompletionfromPlan::activityId={} ", activity.getId());
					activity.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
					activity.setCompletedTime(new Timestamp(new Date().getTime()));
					activityRepository.save(activity);
				}else{
					LOGGER.info("activity_is_null_in_plan");
					Integer activityId = (Integer)varibleMap.get(defKey +"_ID");
					LOGGER.info("processActivityCompletion::activityId={} ", activityId);
					Optional<Activity> activityOptional = activityRepository.findById(activityId);
					if (activityOptional.isPresent()) {
						LOGGER.info("processActivityCompletion::activity exists");
						activity=activityOptional.get();
						activity.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
						activity.setCompletedTime(new Timestamp(new Date().getTime()));
						activityRepository.save(activity);
					}
				}
			}else{
				Integer activityId = (Integer)varibleMap.get(defKey +"_ID");
				LOGGER.info("processActivityCompletion::activityId={} ", activityId);
                if(activityId!=null) {
                    Optional<Activity> activityOptional = activityRepository.findById(activityId);
                    if (activityOptional.isPresent()) {
                        LOGGER.info("processActivityCompletion::activity exists");
                        Activity activity=activityOptional.get();
                        activity.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
                        activity.setCompletedTime(new Timestamp(new Date().getTime()));
                        activityRepository.save(activity);
                    }
               }else {
                 
                   Activity activity=activityRepository.findByServiceIdAndMstActivityDefKeyAndSiteType(serviceId, defKey, siteType);
                   if(activity!=null) {
                    LOGGER.info("processActivityCompletion::activityId2={} ", activity.getId());
                       activity.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
                    activity.setCompletedTime(new Timestamp(new Date().getTime()));
                    activityRepository.save(activity);
                   }
               }
			}
		}catch(Exception ee) {
			LOGGER.error("processActivityCompletion::Exception={} ", ee);
		}

		LOGGER.info("processActivityCompletion created with id {} ", activityPlan);

		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Map<String, Object> processManulTask(DelegateTask execution) {

		boolean isReopenTask = false;
		Map<String, Object> varibleMap = execution.getVariables();
		Task task = null;
		String taskDefKey = execution.getTaskDefinitionKey();
		Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);
		String productType = (String) varibleMap.get(MasterDefConstants.PRODUCT_NAME);

		if(varibleMap.get("remainderCycle")==null) {
			varibleMap.put("remainderCycle", "R60/PT24H");
			execution.setVariable("remainderCycle", "R60/PT24H");
		}

		taskDefKey=taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");
		LOGGER.info("ManualTask started for {}, serviceId={}", taskDefKey, serviceId);

		MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);
		LOGGER.info("activityKey={}  ActivityID {}", mstTakDef.getMstActivityDef().getKey() + "_ID",
				varibleMap.get(mstTakDef.getMstActivityDef().getKey() + "_ID"));
		
		
		String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		LOGGER.info("siteType={}", siteType);
		// LOGGER.info("varible map {}", varibleMap);
		
		MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);
	
		MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();
		
		Integer processId=null;
		Integer serviceID =(Integer) varibleMap.get(SERVICE_ID);
		List<Task> tasks =null;
		if(serviceID!=null) {
			LOGGER.info("serviceID exists");
			
			if(productType != null && "Microsoft Cloud Solutions".equalsIgnoreCase(productType)
					&& ((Integer)varibleMap.get(KEY_GSC_FLOW_GROUP_ID)!=null) && !"teamsdr-user-mapping".equals(taskDefKey)) {
				task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyAndGscFlowGroupId((Integer) varibleMap.get(SERVICE_ID),
						taskDefKey,(Integer)varibleMap.get(KEY_GSC_FLOW_GROUP_ID));
			} else if (CommonConstants.MICROSOFT_CLOUD_SOLUTIONS.equalsIgnoreCase(productType) && "teamsdr-user-mapping"
					.equals(taskDefKey)) {
				task = taskRepository
						.findFirstByServiceIdAndMstTaskDef_keyAndScComponentId((Integer) varibleMap.get(SERVICE_ID),
								taskDefKey, (Integer) varibleMap.get(SC_COMPONENT_ID));
			}
			
			else if (productType != null && (productType.equalsIgnoreCase("IZO SDWAN"

			) || "IZOSDWAN".equalsIgnoreCase(productType))) {
				tasks = taskRepository.findByServiceIdAndMstTaskDef_keyAndSiteTypeAndProcessInstanceId(
						(Integer) varibleMap.get(SERVICE_ID), taskDefKey, siteType, execution.getProcessInstanceId());
			} else {
				
					tasks = taskRepository.findByServiceIdAndMstTaskDef_keyAndSiteType((Integer) varibleMap.get(SERVICE_ID),
						taskDefKey, siteType);
			}
			
			if (tasks!=null && !tasks.isEmpty()) {
				LOGGER.info("Task exists");
				task = tasks.stream()
						.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)||t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD))
						.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);
			}
		}

		if (task != null) {
			LOGGER.info("Task exists after filtering");
			task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
			updateFlowGroupIdForSpecificTask(task,varibleMap, execution);
			processId = task.getProcessId();
			task.setWfTaskId(execution.getId());
			task.setWfProcessInstId(execution.getProcessInstanceId());
			task.setWfExecutorId(execution.getExecutionId());
			task.setUpdatedTime(new Timestamp(new Date().getTime()));
			task.setSiteType(siteType);			
			isReopenTask = true;
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
		}
		

		LOGGER.info("ProcessId {} serviceID{}", processId,serviceID);
		
		if (task != null) {
			
			updateLmType(serviceId,task);
			updateDataCenterDetails(serviceId,task);
			updateVendorCode(serviceId,task,varibleMap);

			Timestamp dueDate = tatService.calculateDueDate(task.getMstTaskDef().getTat(),
					task.getMstTaskDef().getOwnerGroup(), task.getUpdatedTime()!=null?task.getUpdatedTime():task.getCreatedTime());
			task.setDuedate(dueDate);
			
			if(serviceId!=null)updateDueDateForSpecificTask(task, serviceId, varibleMap);

			taskRepository.save(task);
			
			
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, taskDefKey, siteType);
			
			LOGGER.info("TaskPlan {} ", taskPlan);

				if (taskPlan != null) {
					if (task.getMstTaskDef().getIsDependentTask().equalsIgnoreCase("Y")
							&& varibleMap.containsKey(task.getMstTaskDef().getKey().concat("-start-time"))
							&& varibleMap.containsKey(task.getMstTaskDef().getKey().concat("-time-slot"))) {

						Date acDate = (Date) varibleMap.get(task.getMstTaskDef().getKey().concat("-start-time"));

						Timestamp acTimestamp = new Timestamp(acDate.getTime());

						Integer slot = ((Integer) varibleMap.get(task.getMstTaskDef().getKey().concat("-time-slot")));

						Optional<MstAppointmentSlots> msOptional = mstSlotRepository.findById(slot);
						if (msOptional.isPresent()) {
							MstAppointmentSlots mstAppointmentSlots = msOptional.get();
							String changeTime = mstAppointmentSlots.getSlots().substring(0, 1);
							// TODO: find AM or PM
							Timestamp actualStartTime = Utils.addTimeToDate(acTimestamp, changeTime);
							LOGGER.info("{} DependentTask task set end time to {}", task.getMstTaskDef().getKey(),
									actualStartTime);
							taskPlan.setActualStartTime(actualStartTime);
						}
					} else {
						taskPlan.setActualStartTime(new Timestamp(new Date().getTime()));
					}
					taskPlan.setTask(task);
					taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
					taskPlanRepository.save(taskPlan);
				}

				if ((taskDefKey.startsWith(AppointmentConstants.CUSTOMER_APPOINTMENT) 
						|| taskDefKey.startsWith("sdwan-customer-appointment"))
						&& !execution.getTaskDefinitionKey().contains("_appchange") 
						&& !execution.getTaskDefinitionKey().contains("_reopen")
						&& !"customer-appointment-offnet-ss".equals(taskDefKey))createAppointmentTask(task);

			String logConstant = isReopenTask ? TaskLogConstants.REOPENED : TaskLogConstants.CREATED;

			processTaskLogDetails(task, logConstant);
			List<TaskAssignment> taskAssignmentList = createAssignment(task, execution, isReopenTask,varibleMap);
			
			for(TaskAssignment taskAssignment : taskAssignmentList) {
				LOGGER.info("Process id info{} ", processId);
				taskAssignment.setProcessId(processId);

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
				}else if (task.getMstTaskDef() != null) {
					// Send Email to Task Leads
					notifyTaskLeads(task, taskAssignment);
				}else {
					/*if(taskAssignment.getGroupName()!=null && taskAssignment.getGroupName().equalsIgnoreCase("customer")) {
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

					}*/
				} 
				triggerMailToAdminForProductCommercial(task);
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

	/**
	 * @author vivek
	 *
	 * @param task
	 * @param execution 
	 * @param varibleMap 
	 */
	private void updateFlowGroupIdForSpecificTask(Task task, Map<String, Object> varibleMap, DelegateTask execution) {
		
		List<String> didTasks = new ArrayList<>();
		didTasks.add("did-new-number-handover-note");
		didTasks.add("did-new-number-order-creation-repc");
		didTasks.add("did-new-number-order-creation-repc-jeopardy");
		didTasks.add("did-new-number-order-creation-testing");
		didTasks.add("did-new-number-patch-jeopardy");
		didTasks.add("did-new-number-patch");
		didTasks.add("did-porting-number-patch");
		didTasks.add("did-new-number-re-testing");
		didTasks.add("did-new-number-service-acceptence");
		didTasks.add("did-new-number-wait-for-sip");
		didTasks.add("did-number-info-rs-config");
		didTasks.add("did-porting-number-handover-note");
		didTasks.add("did-porting-number-info-rs-config");
		didTasks.add("did-porting-number-order-creation-repc");
		didTasks.add("did-porting-number-order-creation-repc-jeopardy");
		didTasks.add("did-porting-number-patch-jeopardy");
		didTasks.add("did-porting-number-re-testing");
		didTasks.add("did-porting-number-service-acceptence");
		didTasks.add("did-porting-number-testing");
		didTasks.add("did-porting-wait-for-sip");
		didTasks.add("gsc-did-provisioning-validation");
		didTasks.add("gsc-get-did-suppliers");
		didTasks.add("gsc-get-did-suppliers-jeopardy");
		didTasks.add("place-supplier-DID-new-order");
		didTasks.add("place-supplier-DID-porting-order");
		didTasks.add("process-supplier-did-orders");
		
		List<String> vasTasks = new ArrayList<>();
		vasTasks.add("gsc-validate-supplier-internal-db");
		vasTasks.add("gsc-test-numbers");
		vasTasks.add("gsc-service-acceptence");
		vasTasks.add("gsc-re-test-numbers");

		String taskDefKey = task.getMstTaskDef().getKey();
		
		if (didTasks.stream().anyMatch(taskDefKey::equalsIgnoreCase)) {
			if (null != varibleMap.get(KEY_GSC_FLOW_GROUP_ID)) {
				task.setGscFlowGroupId((Integer) varibleMap.get(KEY_GSC_FLOW_GROUP_ID));
			}
		} else if (vasTasks.stream().anyMatch(taskDefKey::equalsIgnoreCase)) {
			if (null != varibleMap.get(KEY_GSC_FLOW_GROUP_ID)) {
				task.setGscFlowGroupId((Integer) varibleMap.get(KEY_GSC_FLOW_GROUP_ID));
			}
		}
	}

	private void notifyTaskLeads(Task task, TaskAssignment taskAssignment) {
		if(taskAssignment.getUserName()!=null) {
		LOGGER.info(
				"Inside notify task assignment email to task leads section for this task: {} and service code is: {}",
				task.getId(), task.getServiceCode());
		
		String groupName = task.getMstTaskDef().getKey() + "_" + task.getMstTaskDef().getOwnerGroup();
		
		List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository
				.findByGroup(groupName);
		
		if (!mstTaskRegionList.isEmpty()) {
			
			LOGGER.info("Notify Task Assignment to : {} for this task : {} and service code is: {}",
					mstTaskRegionList.get(0).getEmail(), task.getId(), task.getServiceCode());
			List<String> ccAddresses = new ArrayList<>();
			
			String userName = taskAssignment.getUserName();
			if(userName == null || userName.trim().isEmpty()) {
				userName = "Lead";
			}
			
			notificationService.notifyTaskAssignment(mstTaskRegionList.get(0).getEmail(),
					userName, task.getServiceCode(), task.getMstTaskDef().getName(),
					Utils.converTimeToString(task.getDuedate()), "", false, ccAddresses);
		} else {
			LOGGER.info(
					"Mst Task Region is not there for this group name: {}, Task Id: {}, and Service code:{}",
					groupName, task.getId(), task.getServiceCode());
		}
		}
		/*
		List<String> gscTasks = new ArrayList<>();
		gscTasks.add("gsc-comm-vetting");
		gscTasks.add("gsc-cms-billing-profile");
		gscTasks.add("gsc-entmm-task");
		gscTasks.add("gsc-rate-upload");
		gscTasks.add("gsc-validate-supporting-document");
		gscTasks.add("gsc-provisioning-validation");
		gscTasks.add("gsc-did-provisioning-validation");
		gscTasks.add("gsc-uifn-procure");
		gscTasks.add("gsc-voice-sales-engr");
		
		String taskDefKey = task.getMstTaskDef().getKey();
		if (gscTasks.stream().anyMatch(taskDefKey::equalsIgnoreCase)) {
		*/
		
		List<String> didTasks = new ArrayList<>();
		didTasks.add("did-new-number-handover-note");
		didTasks.add("did-new-number-order-creation-repc");
		didTasks.add("did-new-number-order-creation-repc-jeopardy");
		didTasks.add("did-new-number-order-creation-testing");
		didTasks.add("did-new-number-patch-jeopardy");
		didTasks.add("did-new-number-patch");
		didTasks.add("did-porting-number-patch");
		didTasks.add("did-new-number-re-testing");
		didTasks.add("did-new-number-service-acceptence");
		didTasks.add("did-new-number-wait-for-sip");
		didTasks.add("did-number-info-rs-config");
		didTasks.add("did-porting-number-handover-note");
		didTasks.add("did-porting-number-info-rs-config");
		didTasks.add("did-porting-number-order-creation-repc");
		didTasks.add("did-porting-number-order-creation-repc-jeopardy");
		didTasks.add("did-porting-number-patch-jeopardy");
		didTasks.add("did-porting-number-re-testing");
		didTasks.add("did-porting-number-service-acceptence");
		didTasks.add("did-porting-number-testing");
		didTasks.add("did-porting-wait-for-sip");
		didTasks.add("gsc-did-provisioning-validation");
		didTasks.add("gsc-get-did-suppliers");
		didTasks.add("gsc-get-did-suppliers-jeopardy");
		didTasks.add("place-supplier-DID-new-order");
		didTasks.add("place-supplier-DID-porting-order");
		didTasks.add("process-supplier-did-orders");
		
		String taskDefKey = task.getMstTaskDef().getKey().toLowerCase();
		if(taskDefKey.startsWith("gsc") || didTasks.stream().anyMatch(taskDefKey::equalsIgnoreCase)) {
			LOGGER.info(
					"Inside notify task assignment email to task leads section for this task: {} and service code is: {}",
					task.getId(), task.getServiceCode());

			String groupName = task.getMstTaskDef().getKey() + "_" + task.getMstTaskDef().getOwnerGroup();

			List<MstTaskRegion> mstTaskRegionList = mstTaskRegionRepository.findByGroup(groupName);

			if (!mstTaskRegionList.isEmpty()) {

				LOGGER.info("Notify Task Assignment to : {} for this task : {} and service code is: {}",
						mstTaskRegionList.get(0).getEmail(), task.getId(), task.getServiceCode());
				List<String> ccAddresses = new ArrayList<>();

				String userName = taskAssignment.getUserName();
				if (userName == null || userName.trim().isEmpty()) {
					userName = "Lead";
				}

				notificationService.notifyTaskAssignment(mstTaskRegionList.get(0).getEmail(), userName,
						task.getServiceCode(), task.getMstTaskDef().getName(),
						Utils.converTimeToString(task.getDuedate()), "", false, ccAddresses);
			}
		}
	}
	
	private void updateVendorCode(Integer serviceId, Task task, Map<String, Object> varibleMap) {
		LOGGER.info("updateVendorCode invoked for Task Id::{},variableMap::{}", task.getId(),varibleMap);
		if((task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-pr-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-po-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-po-release-for-cpe-order")
				|| task.getMstTaskDef().getKey().equalsIgnoreCase("sdwan-confirm-material-availability")) && 
				varibleMap.containsKey("vendorCode")){
			String vendorCode=varibleMap.get("vendorCode")!=null?(String) varibleMap.get("vendorCode"):null;
			String vendorName=varibleMap.get("vendorName")!=null?(String) varibleMap.get("vendorName"):null;
			LOGGER.info("VendorCode::{},VendorName::{}",vendorCode,vendorName);
			task.setVendorCode(vendorCode);
			task.setVendorName(vendorName);
		}
	}

	private void updateLmType(Integer serviceId, Task task) {
		try {
			List<ScComponentAttribute> scComponentAttributes = scComponentAttributesRepository
					.findByScServiceDetailIdAndAttributeNameInAndScComponent_componentNameAndScComponent_siteType(
							serviceId,
							Arrays.asList("lastmileProvider", "lastmileType", "lastMileScenario", "downtimeDuration",
									"fromTime", "toTime", "isTxDowntimeReqd", "isIpDownTimeRequired"),
							"LM", task.getSiteType() == null ? "A" : task.getSiteType());
			if (!scComponentAttributes.isEmpty()) {
				Map<String, String> lmMap = new HashMap<String, String>();
				scComponentAttributes.forEach(scComponentAtt -> {
					lmMap.put(scComponentAtt.getAttributeName(), scComponentAtt.getAttributeValue());
				});
				task.setLmProvider(lmMap.get("lastMileProvider"));
				task.setLastMileScenario(lmMap.get("lastMileScenario"));
				task.setLmType(lmMap.get("lmType"));
				if (lmMap.containsKey("isIpDownTimeRequired")
						&& "true".equalsIgnoreCase(lmMap.get("isIpDownTimeRequired"))) {
					task.setIsIpDownTimeRequired("Yes");

				} else {
					task.setIsIpDownTimeRequired("No");

				}
				if (lmMap.containsKey("isTxDowntimeReqd") && "true".equalsIgnoreCase(lmMap.get("isTxDowntimeReqd"))) {
					task.setIsTxDowntimeReqd("Yes");

				} else {
					task.setIsTxDowntimeReqd("No");

				}
				String downtimeDuration = StringUtils.trimToEmpty(lmMap.get("downtimeDuration"));
				String fromTime = StringUtils.trimToEmpty(lmMap.get("fromTime"));
				String toTime = StringUtils.trimToEmpty(lmMap.get("toTime"));
				if (StringUtils.isNotBlank(downtimeDuration)) {
					Date downtimeDurationDate = new SimpleDateFormat("yyyy-MM-dd").parse(downtimeDuration);
					downtimeDuration = DateUtil.convertDateToMMMString(downtimeDurationDate);
					task.setDowntime(downtimeDuration + "<BR>" + fromTime + " - " + toTime);
				}
			}
			task.setOrderSubCategory(task.getScServiceDetail().getOrderSubCategory());
		} catch (Exception e) {
			LOGGER.error("erro in updating lm type:{}", e);
		}
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
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "enrich-service-design", task.getSiteType()==null?"A":task.getServiceType());
			if (taskPlan != null) {
				varibleMap.put("tentativeDate", taskPlan.getPlannedStartTime());
				task.setDuedate(taskPlan.getPlannedStartTime());
			}
		}else if (task.getMstTaskDef().getKey().equalsIgnoreCase("izopc-advanced-enrichment")) {
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "izopc-manual-enrich-service-design", task.getSiteType()==null?"A":task.getServiceType());
			if (taskPlan != null) {
				varibleMap.put("tentativeDate", taskPlan.getPlannedStartTime());
				task.setDuedate(taskPlan.getPlannedStartTime());
			}
		}

	}

	private void updateDueDateOfMux(Task task, Map<String, Object> varibleMap, Integer serviceId) {
		TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, "lm-deliver-mux",  task.getSiteType()==null?"A":task.getServiceType());
		if (taskPlan != null) {
			varibleMap.put("tentativeDate", taskPlan.getPlannedStartTime());
			task.setDuedate(taskPlan.getPlannedStartTime());
		}
	}

	/**
	 * @param execution
	 * @param task
	 * @param tasks     used to create appointment
	 */
	private void createAppointmentTask(Task task) {
		Appointment appointment = new Appointment();
		if(task.getMstTaskDef().getKey().equals("customer-appointment-mux-recovery") ||
				task.getMstTaskDef().getKey().equals("customer-appointment-rf-recovery")||
				task.getMstTaskDef().getKey().equals("customer-appointment-cpe-recovery") ||
				task.getMstTaskDef().getKey().equals("customer-appointment-mast-dismantling")) {
			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(task.getServiceId(),
							"terminationEffectiveDate", "LM", task.getSiteType() == null ? "A" : task.getSiteType());
			if(scComponentAttribute != null && scComponentAttribute.getAttributeValue() != null) {
				Timestamp timestamp = DateUtil.convertStringToTimeStampYYMMDD(scComponentAttribute.getAttributeValue());
				appointment.setAppointmentDate(timestamp);
			}
		}else {
			appointment.setAppointmentDate(tatService.calculateDueDate(960, "optimus_regus", task.getCreatedTime()));
		}
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
		TaskTatTime taskTatTime = new TaskTatTime();
		taskTatTime.setTask(task);
		taskTatTime.setStartTime(new Timestamp(new Date().getTime()));

		if (taskAssignment != null) {
			taskTatTime.setAssignee(taskAssignment.getGroupName());

		}
		taskTatTime.setCreatedTime(new Timestamp(new Date().getTime()));
		taskTatTimeRepository.save(taskTatTime);
	}

	/**
	 * @author vivek
	 * @param varibleMap
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
			// LOGGER.info("varible map {}", varibleMap);
			
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
			
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, taskDefKey, siteType);
				
			LOGGER.info("TaskPlan {} ", taskPlan);
			updateServiceConfigStatus(serviceId, taskDefKey);
			updateServiceAcceptanceStatus(serviceId, taskDefKey,varibleMap);
			updateServiceAssuranceStatus(serviceId, taskDefKey,varibleMap);
			if (taskPlan != null) {

					if (task != null && task.getMstTaskDef().getIsCustomerTask().equalsIgnoreCase("Y")
							&& varibleMap.containsKey(taskDefKey.concat("-end-time"))
							&& varibleMap.containsKey(taskDefKey.concat("-time-slot"))) {
	
						Timestamp plannedEndTime = taskPlan.getPlannedStartTime();
						Date acTime = (Date) varibleMap.get(taskDefKey.concat("-end-time"));
	
						Timestamp acTimestamp = new Timestamp(acTime.getTime());
	
						Integer slot = ((Integer) varibleMap.get(taskDefKey.concat("-time-slot")));
	
						Optional<MstAppointmentSlots> msOptional = mstSlotRepository.findById(slot);
						if (msOptional.isPresent()) {
							MstAppointmentSlots mstAppointmentSlots = msOptional.get();
							String changeTime = mstAppointmentSlots.getSlots().substring(0, 1);
							LOGGER.info("change time {}", changeTime);
							// TODO: find AM or PM
							Timestamp actualEndTime = Utils.addTimeToDate(acTimestamp, changeTime);
							LOGGER.info("added time {}", actualEndTime);
	
							// taskPlan.setActualEndTime(actualEndTime);
							taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
	
							/*Long noDaysDelay = ChronoUnit.DAYS.between(plannedEndTime.toLocalDateTime(),
									actualEndTime.toLocalDateTime());
							LOGGER.info("noDaysDelay  {}", noDaysDelay);
	
							LOGGER.info("{} planned time {} delayed {} days to {} by customer",
									task.getMstTaskDef().getKey(), plannedEndTime, noDaysDelay, actualEndTime);
*/
							/*if (noDaysDelay > 1) {
								
								List<MstTaskDef> mstTaskDefs=mstTaskDefRepository.findByDependentTaskKey(taskDefKey);
								
								if(!mstTaskDefs.isEmpty()) {
									
									mstTaskDefs.forEach(mstTaskDef ->{
										projectPlanInitiateService.initiateCustomerDelay(varibleMap, mstTaskDef.getKey(), actualEndTime);

									});

								}

							}*/
						}
					} else {
						taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
						taskPlan.setTargettedEndTime(new Timestamp(new Date().getTime()));
					}
					taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
					taskPlanRepository.save(taskPlan);
				}
	
				LOGGER.info("processManulTaskCompletion");
			
		}
		return varibleMap;
	}

	/**
	 * @author vivek
	 * @param varibleMap
	 * @return
	 * @throws TclCommonException
	 */
	public Task processServiceTask(DelegateExecution execution) {
		Task task = null;
		try {
			Map<String, Object> varibleMap = execution.getVariables();
			String productType = (String) varibleMap.get(MasterDefConstants.PRODUCT_NAME);


			String taskDefKey = execution.getCurrentActivityId();
			taskDefKey = taskDefKey.replaceAll("_appchange", "").replaceAll("_reopen", "");

			boolean isReopenTask = false;

			Integer serviceId = (Integer) varibleMap.get(MasterDefConstants.SERVICE_ID);

			String siteType = StringUtils.trimToEmpty((String) varibleMap.get("site_type"));
			if (StringUtils.isBlank(siteType)) siteType = "A";

			LOGGER.info("ServiceTask started for {}, serviceId={}", taskDefKey, serviceId);
			// LOGGER.info("varible map {}", varibleMap);

			MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);

			MstStatus mstStatus = taskCacheService.getMstStatus(MstStatusConstant.OPEN);

			MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();
			Integer processId = (Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID");
			
			if(productType != null && "Microsoft Cloud Solutions".equalsIgnoreCase(productType)
					&& ((Integer)varibleMap.get(KEY_GSC_FLOW_GROUP_ID)!=null) && !"teamsdr-user-mapping".equals(taskDefKey)) {
				task = taskRepository.findFirstByServiceIdAndMstTaskDef_keyAndGscFlowGroupId((Integer) varibleMap.get(SERVICE_ID),
						taskDefKey,(Integer)varibleMap.get(KEY_GSC_FLOW_GROUP_ID));
			} else if (CommonConstants.MICROSOFT_CLOUD_SOLUTIONS.equalsIgnoreCase(productType) && "teamsdr-user-mapping"
					.equals(taskDefKey)) {
				task = taskRepository
						.findFirstByServiceIdAndMstTaskDef_keyAndScComponentId((Integer) varibleMap.get(SERVICE_ID),
								taskDefKey, (Integer) varibleMap.get(SC_COMPONENT_ID));
			}

			LOGGER.info("Values of processId={}", processId);
			if (!"wait-for-downtime-from-customer".equals(taskDefKey)) {
				LOGGER.info("task other that wait-for-downtime-from-customer");
				if (serviceId != null) {
					List<Task> tasks =null;
					
					if (productType != null && (productType.equalsIgnoreCase("IZO SDWAN"

					) || "IZOSDWAN".equalsIgnoreCase(productType))) {
						taskRepository.findByServiceIdAndMstTaskDef_keyAndSiteTypeAndProcessInstanceId(
								(Integer) varibleMap.get(SERVICE_ID), execution.getCurrentActivityId(), siteType,
								execution.getProcessInstanceId());
					} else {
						tasks = taskRepository.findByServiceIdAndMstTaskDef_keyAndSiteType(
								(Integer) varibleMap.get(SERVICE_ID), taskDefKey, siteType);
					}
	
					LOGGER.info("task already presnet or not{}", tasks);
					if (tasks!=null && !tasks.isEmpty()) {
						task = tasks.stream()
								.filter(t -> t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)
										|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
										|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
										|| t.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED))
								.sorted(Comparator.comparing(Task::getCreatedTime).reversed()).findFirst().orElse(null);

					}
				}
			}
			if (task != null) {
				isReopenTask = true;
				updateFlowGroupIdForSpecificTask(task,varibleMap, null);
				task.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.REOPEN));
				task.setWfTaskId(execution.getId());
				task.setWfProcessInstId(execution.getProcessInstanceId());
				task.setUpdatedTime(new Timestamp(new Date().getTime()));
				task.setSiteType(siteType);
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
						taskRepository.save(task);
					}
				}else {
					task = createServiceTask(mstTakDef, activity, mstStatus, varibleMap, execution, processId);
					taskRepository.save(task);
				}
			}

			if (task != null) {


				TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, taskDefKey, siteType);

				LOGGER.info("TaskPlan {} ", taskPlan);

				if (taskPlan != null) {
					taskPlan.setActualStartTime(new Timestamp(new Date().getTime()));
					taskPlan.setTask(task);
					taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
					taskPlanRepository.save(taskPlan);
				}
				updateLmType(serviceId, task);
				String logConstant = isReopenTask ? TaskLogConstants.REOPENED : TaskLogConstants.CREATED;
				processTaskLogDetails(task, logConstant);
				execution.setVariable(mstTakDef.getKey() + "_ID", task.getId());
				LOGGER.info("Task created with id {} ", task.getId());
			}
		}catch(Exception e)
		{
			LOGGER.info("Error in processing service task "+e.getMessage());
			e.printStackTrace();
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
				
				TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId,taskDefKey, siteType);
				
				LOGGER.info("TaskPlan {} ", taskPlan);
				if (taskPlan != null) {
					taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
					taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
					taskPlanRepository.save(taskPlan);
				}
			
				
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
						taskRepository.save(task);
						processTaskLogDetails(task,(StringUtils.isNotBlank(errorMessage))? TaskLogConstants.FAILED:TaskLogConstants.CLOSED,errorMessage,"");
						if("auto-confirm-order-service-configuration".equalsIgnoreCase(taskDefKey) 
								|| "assign-poc-SCM-M-L".equalsIgnoreCase(taskDefKey) || "assign-poc-SCM-Mgmt".equalsIgnoreCase(taskDefKey)){
							LOGGER.info("Auto Confirm Order Service Configuration or SCMML or SCMMgmt");
							Optional<TaskAssignment> taskOptional=taskAssignmentRepository.findByTaskId(task.getId());
							if(!taskOptional.isPresent()){
								LOGGER.info("Task assigned already");
								TaskAssignment assignment = new TaskAssignment();		
								assignment.setGroupName(task.getMstTaskDef().getAssignedGroup());
								assignment.setOwner(task.getMstTaskDef().getOwnerGroup());
								assignment.setTask(task);	
								assignment.setUserName(null);
								MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(taskDefKey);
								MstActivityDef mstActivitDef = mstTakDef.getMstActivityDef();
								Integer processId = (Integer) varibleMap.get(mstActivitDef.getMstProcessDef().getKey() + "_ID");
								assignment.setProcessId(processId);
								taskAssignmentRepository.save(assignment);
							}
						}
					}
	
				}
				String siteType = StringUtils.trimToEmpty((String)varibleMap.get("site_type"));
				if(StringUtils.isBlank(siteType))siteType="A";
				LOGGER.info("siteType={}", siteType);
				
				TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId,taskDefKey, siteType);
				
				LOGGER.info("TaskPlan {} ", taskPlan);
				if (taskPlan != null) {
					taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
					taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
					taskPlanRepository.save(taskPlan);
				}
				LOGGER.info("processServiceTaskCompletion");
			}
		}catch(Exception ee) {
			LOGGER.error("processServiceTaskCompletion error",ee);
		}
		return varibleMap;
	}
	
	
	
	  /**
     * @author vivek
     * @param errorMessage
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
			
			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId,taskDefKey, siteType);
			
			LOGGER.info("TaskPlan {} ", taskPlan);
			if (taskPlan != null) {
				taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
				taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				taskPlanRepository.save(taskPlan);
			}
		
			
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
		
		TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, execution.getCurrentActivityId(), siteType);
		
		LOGGER.info("TaskPlan {} ", taskPlan);
		if (taskPlan != null) {
			taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
			taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
			taskPlanRepository.save(taskPlan);
		}
	
		
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

        List<String> cpeRelatedTaskName=Arrays.asList("sdwan-pr-for-cpe-order","sdwan-po-for-cpe-order","sdwan-po-release-for-cpe-order","pr-for-cpe-order","po-for-cpe-order","po-release-for-cpe-order","confirm-material-availability",
        		"sdwan-confirm-material-availability","sdwan-track-cpe-delivery","track-cpe-delivery","sdwan-dispatch-track-cpe-international","sdwan-provide-wbsglcc-details","provide-wbsglcc-details","sdwan-arrange-field-engineer-cpe-installation","arrange-field-engineer-cpe-installation","customer-appointment-cpe-installation",
        		"sdwan-install-cpe","install-cpe","cpe_invoice_jeopardy","dispatch-cpe","sdwan-dispatch-cpe");
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
			LOGGER.info("Data center attribute name and serviceId"+attributeName+" "+serviceId);
			ScComponentAttribute scComponentAttribute = scComponentAttributesRepository
					.findFirstByScServiceDetailIdAndAttributeNameAndScComponent_componentNameAndScComponent_siteTypeOrderByIdDesc(serviceId,
							attributeName, "LM", task.getSiteType() == null ? "A" : task.getSiteType());
			if (Objects.nonNull(scComponentAttribute)) {
				LOGGER.info("Data center attribute value and taskId "+scComponentAttribute.getAttributeValue()+" "+task.getId());
				task.setDistributionCenterName(scComponentAttribute.getAttributeValue());
			}
		}

	}
    private void triggerMailToAdminForProductCommercial(Task task){
		try {
			//sending mail to Master and Jamsheed after approve capex task for MMR Cross connect
			if (CommonConstants.MMR_CROSS_CONNECT.equalsIgnoreCase(task.getScServiceDetail().getErfPrdCatalogOfferingName())
			     && "lm-approve-capex".equalsIgnoreCase(task.getMstTaskDef().getKey())) {
				LOGGER.info("Inside admin mail trigger for approve capex in cross connect");
				List<String> adminEmailIds = getTaskRegionByGrroup("PRODUCT_COMMERCIAL");
				LOGGER.info("Admin emailid list {} :", adminEmailIds);
				adminEmailIds.stream().forEach(adminEmailId->{
					notificationService.notifyTaskAssignment(adminEmailId,
							adminEmailId, task.getServiceCode(), task.getMstTaskDef().getName(),
							Utils.converTimeToString(task.getDuedate()), "", true, new ArrayList<>());
				});
			}
		}catch (Exception ex){
			LOGGER.error("Error while mail trigger to admin for product commercial {} :",ex );
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
}
