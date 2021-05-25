package com.tcl.dias.servicefulfillmentutils.project.workflow;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SITE_ID;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.factory.IProjectWorkFlowHandler;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used for project plan tracking
 */
@Component
public class ProjectPlanTrackingWorkFlow extends ProjectPlanAbstractService implements IProjectWorkFlowHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectPlanTrackingWorkFlow.class);
	

	@Override
	public Boolean processStagePlanStart(DelegateExecution execution, Expression preceders) throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		defKey = defKey.replaceAll("_start", "");
		LOGGER.info("StagePlanStart started {} ", defKey);
		Map<String, Object> fMap = execution.getVariables();
		Integer orderId = (Integer) fMap.get(SC_ORDER_ID);
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId = (Integer) fMap.get(SITE_ID);
		StagePlan stagePlan = null;
		String precedersValues = (String) preceders.getValue(execution);
		if (serviceId != null) {

			stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
			LOGGER.info("stagePlan {} ", stagePlan);
		} else if (siteId != null) {
			stagePlan = stagePlanRepository.findBySiteIdAndMstStageDefKey(siteId, defKey);

		}

		setActiveVersion(execution, stagePlan, precedersValues);
		if (stagePlan == null && !precedersValues.contains("root")) {
			Timestamp startTime = getMaxDate(precedersValues, execution);
			LOGGER.info("max date  {} ", startTime);

			stagePlan = createStagePlan(startTime, defKey, serviceId, orderId, siteId, serviceCode, orderCode,
					execution);
		}
		
		if (stagePlan == null && defKey.equalsIgnoreCase("order_placement_stage")) {
			Timestamp startTime = getMaxDate(precedersValues, execution);
			LOGGER.info("getMaxDate for order_placement_stage for serviceId  {}  with startTime {}", serviceCode,
					startTime);
			if (startTime == null) {
				StagePlan orderStagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId,
						"order_enrichment_stage");
				startTime = orderStagePlan.getEstimatedStartTime();
			}
			LOGGER.info("createStagePlan for order_placement_stage for serviceId  {}  with startTime {}", serviceCode,
					startTime);
			stagePlan = createStagePlan(startTime, defKey, serviceId, orderId, siteId, serviceCode, orderCode,
					execution);
		}
		
		
		Timestamp startTime = getMaxDate(precedersValues, execution, stagePlan.getTargettedStartTime());
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		LOGGER.info("stagePlan {} ", stagePlan);
		updateStage(stagePlan, startTime, defKey, serviceId, orderId, siteId, serviceCode, orderCode, execution,precedersValues);
		return true;

	}



	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		LOGGER.info("processPlan started {} ", defKey);
		defKey = defKey.replaceAll("_start", "");

		Map<String, Object> fMap = execution.getVariables();
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId = (Integer) fMap.get(SITE_ID);

		String precedersValues = (String) preceders.getValue(execution);
		String siteType=null;
		ProcessPlan processPlan = null;
		if (serviceId != null) {
			
			siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
			if(StringUtils.isBlank(siteType))siteType="A";

			processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
		}

		else if (siteId != null) {
			processPlan = processPlanRepository.findBySiteIdAndMstProcessDefKey(siteId, defKey);
		}
		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);
		if (processPlan == null) {

			processPlan = createProcessPlan(startTime, defKey, serviceId, siteId, serviceCode, orderCode, execution,fMap);
		}

		LOGGER.info("processPlan with preceders {} ", precedersValues);

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

		LOGGER.info("processPlan info: {} ", processPlan);

		updateProcessPlan(processPlan, startTime, defKey, serviceId, siteId, serviceCode, orderCode, execution,siteType,precedersValues);

		return true;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processActivityPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		LOGGER.info("activityPlan started {} ", defKey);
		defKey = defKey.replaceAll("_start", "");

		Map<String, Object> fMap = execution.getVariables();

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId = (Integer) fMap.get(SITE_ID);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("ActivityPlan with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		if (serviceId != null) {
			activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);
		} else if (siteId != null) {
			activityPlan = activityPlanRepository.findBySiteIdAndMstActivityDefKey(siteId, defKey);

		}

		if (activityPlan == null) {
			activityPlan = createActivityPlan(activityPlan, startTime, defKey, serviceId, siteId, serviceCode,
					orderCode, execution,fMap);
		}
		LOGGER.info("activityPlan info: {} ", activityPlan);
		updateActivityPlan(activityPlan, startTime, defKey, serviceId, siteId, serviceCode, orderCode, execution,siteType,precedersValues);

		return true;
	}

	/**
	 * @author vivek
	 * @param activityPlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param siteId
	 * @param serviceCode
	 * @param orderCode
	 * @param fMap 
	 * @return
	 */
	private ActivityPlan createActivityPlan(ActivityPlan activityPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution, Map<String, Object> fMap) {
		LOGGER.info("updateActivityPlan for : {} startTime={} activityPlan={} ProcessInstanceId={}", defKey, startTime,
				activityPlan, execution.getProcessInstanceId());

		if (activityPlan == null) {
			activityPlan = new ActivityPlan();
			activityPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
			activityPlan.setMstActivityDef(mstActivityDefRepository.findByKey(defKey));
			activityPlan.setEstimatedStartTime(startTime);
			activityPlan.setTargettedStartTime(startTime);
			activityPlan.setPlannedStartTime(startTime);
			activityPlan.setServiceCode(serviceCode);
			activityPlan.setServiceId(serviceId);
			activityPlan.setOrderCode(orderCode);
			activityPlan.setSiteId(siteId);
			activityPlan.setServiceId(serviceId);

			String siteType = (String) fMap.get("site_type");
			if (siteType == null) {
				activityPlan.setSiteType("A");

			} else {
				activityPlan.setSiteType(siteType);

			}
		} else {
			activityPlan.setEstimatedStartTime(startTime);
			activityPlan.setTargettedStartTime(startTime);
			activityPlan.setPlannedStartTime(startTime);

		}
		LOGGER.info("updateActivityPlan completed {}", activityPlan);
		return activityPlanRepository.save(activityPlan);
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processTaskPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		TaskPlan taskPlan = null;
		Timestamp startTime = null;
		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_plan", "");
		defKey = defKey.replaceAll("_end", "");
		defKey=defKey.replaceAll("_appchange", "").replaceAll("_reopen", "");


		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		LOGGER.info("processTaskPlan started {} ", defKey);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		

		taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, defKey, siteType);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("TaskPlan with preceders {} ", precedersValues);
		startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date: {} ", startTime);

		if (taskPlan == null) {
			taskPlan=createTaskPlan(taskPlan, startTime, execution, serviceId, serviceCode, orderCode, defKey,fMap);
		}

		updateTaskPlan(taskPlan, startTime, execution, serviceId, serviceCode, orderCode, defKey,precedersValues,siteType);

		return true;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processActivityPlanCompletion(DelegateExecution execution, Expression preceders)
			throws TclCommonException {
		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		LOGGER.info("ActivityPlanCompletion started {} ", defKey);
		defKey = defKey.replaceAll("_end", "");

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("ActivityPlanCompletion with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);

		LOGGER.info("ActivityPlan with preceders {} ", precedersValues);

		updateActivityCompletion(activityPlan, startTime, execution,precedersValues);
		LOGGER.info("activityPlan info: {} ", activityPlan);

		return true;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processPlanCompletion(DelegateExecution execution, Expression preceders) throws TclCommonException {
		Map<String, Object> fMap = execution.getVariables();

		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		LOGGER.info("processPlan started {} ", defKey);

		defKey = defKey.replaceAll("_end", "");

		
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("processPlanCompletion with preceders {} ", precedersValues);

		ProcessPlan processPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";


		processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
		LOGGER.info("processPlan info: {} ", processPlan);
		updateProcessPlanCompletion(processPlan, startTime, defKey, serviceId, execution,precedersValues);

		return true;

	}

	@Override
	public Boolean processStagePlanCompletion(DelegateExecution execution, Expression preceders)
			throws TclCommonException {
		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		defKey = defKey.replaceAll("_end", "");

		LOGGER.info("StagePlanCompletion started {} ", defKey);

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("StagePlanCompletion with preceders {} ", precedersValues);

		StagePlan stagePlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

		LOGGER.info("max date  {} ", startTime);

		stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
		LOGGER.info("stagePlan  {} ", stagePlan);

		updateStageCompletion(stagePlan, execution, startTime,precedersValues);

		return true;
	}

	private StagePlan createStagePlan(Timestamp startTime, String defKey, Integer serviceId, Integer orderId,
			Integer siteId, String serviceCode, String orderCode, DelegateExecution execution) {

		StagePlan stagePlan = new StagePlan();
		stagePlan.setMstStageDef(mstStageDefRepository.findByKey(defKey));
		stagePlan.setServiceCode(serviceCode);
		stagePlan.setServiceId(serviceId);
		stagePlan.setOrderCode(orderCode);
		stagePlan.setScOrderId(orderId);
		stagePlan.setPlannedStartTime(startTime);
		stagePlan.setTargettedStartTime(startTime);
		stagePlan.setEstimatedStartTime(startTime);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		stagePlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
		if (siteId != null) {
			stagePlan.setSiteId(siteId);
		}
		stagePlan.setActiveVersion(getActiveVersion(execution));
		

		stagePlan.setServiceId(serviceId);

		return stagePlanRepository.save(stagePlan);
	}

	/**
	 * @author vivek
	 * @param preceders
	 * @param execution
	 * @return
	 */
	private Timestamp getMaxDate(String preceders, DelegateExecution execution, Timestamp startTime) {
		String[] precedersList = preceders.split(",");
		Set<Timestamp> maxDates = new TreeSet<Timestamp>(Collections.reverseOrder());
		Arrays.stream(precedersList).forEach(preced -> {

			if (preced.equalsIgnoreCase("root")) {
				maxDates.add(startTime);

			} else {
				Timestamp endTime = (Timestamp) execution.getVariable(preced.trim() + "_endDate");
				if (endTime != null) {
					maxDates.add(endTime);
				} else {
					LOGGER.info("skip  precede {}", preced);

				}
			}

		});
		return maxDates.stream().findFirst().orElse(null);

	}

	/**
	 * @author vivek
	 * @param stagePlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param orderId
	 * @param serviceCode
	 * @param orderCode
	 * @return
	 */
	protected StagePlan updateStage(StagePlan stagePlan, Timestamp startTime, String defKey, Integer serviceId,
			Integer orderId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution,String startPreceder) {

		LOGGER.info("updateStage for : {} startTime={} stagePlan={} ProcessInstanceId={}", defKey, startTime, stagePlan,
				execution.getProcessInstanceId());

		if (stagePlan == null) {
			stagePlan = new StagePlan();
			stagePlan.setMstStageDef(mstStageDefRepository.findByKey(defKey));
			stagePlan.setServiceCode(serviceCode);
			stagePlan.setServiceId(serviceId);
			stagePlan.setSiteId(siteId);
			stagePlan.setServiceId(serviceId);
			stagePlan.setServiceCode(serviceCode);
			stagePlan.setOrderCode(orderCode);
			stagePlan.setScOrderId(orderId);
			stagePlan.setTargettedStartTime(startTime);
			stagePlan.setActiveVersion(getActiveVersion(execution));
			stagePlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
		} else {
			stagePlan.setTargettedStartTime(startTime);
			stagePlan.setActiveVersion(getActiveVersion(execution));


		}
		stagePlan.setStartPreceders(startPreceder);
		LOGGER.info("updateStage completed {}", stagePlan);
		return stagePlanRepository.save(stagePlan);
	}

	/**
	 * @author vivek
	 * @param stagePlan
	 * @param execution
	 * @param startTime
	 * @return
	 */
	protected StagePlan updateStageCompletion(StagePlan stagePlan, DelegateExecution execution, Timestamp startTime,String precedersValue) {
		if (stagePlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

			stagePlan.setTargettedEndTime(startTime);
			stagePlan.setEndPreceders(precedersValue);
			return stagePlanRepository.save(stagePlan);

		}

		return stagePlan;

	}

	private ProcessPlan createProcessPlan(Timestamp startTime, String defKey, Integer serviceId, Integer siteId,
			String serviceCode, String orderCode, DelegateExecution execution, Map<String, Object> fMap) {
		ProcessPlan processPlan = new ProcessPlan();
		processPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
		processPlan.setMstProcessDef(mstProcessDefRepository.findByKey(defKey));
		processPlan.setEstimatedStartTime(startTime);
		processPlan.setTargettedStartTime(startTime);
		processPlan.setPlannedStartTime(startTime);
		processPlan.setServiceCode(serviceCode);
		processPlan.setServiceId(serviceId);
		processPlan.setOrderCode(orderCode);
		processPlan.setServiceId(serviceId);
		processPlan.setSiteId(siteId);
		processPlan.setServiceId(serviceId);
		processPlan.setServiceCode(serviceCode);
		String siteType = (String) fMap.get("site_type");
		if (siteType == null) {
			processPlan.setSiteType("A");

		} else {
			processPlan.setSiteType(siteType);

		}

		processPlan.setOrderCode(orderCode);
		return processPlanRepository.save(processPlan);

	}

	/**
	 * @author vivek
	 * @param processPlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param serviceCode
	 * @param orderCode
	 * @return
	 */
	protected ProcessPlan updateProcessPlan(ProcessPlan processPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution,String siteType,String startPreceders) {
		if (processPlan != null) {
			processPlan.setTargettedStartTime(startTime);
			processPlan.setActiveVersion(getActiveVersion(execution));
			processPlan.setStartPreceders(startPreceders);
			return processPlanRepository.save(processPlan);

		}

		return processPlan;

	}

	/**
	 * @author vivek
	 * @param processPlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param execution
	 * @return
	 */
	protected ProcessPlan updateProcessPlanCompletion(ProcessPlan processPlan, Timestamp startTime, String defKey,
			Integer serviceId, DelegateExecution execution,String preceders) {

		if (processPlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
			processPlan.setTargettedEndTime(startTime);
			processPlan.setEndPreceders(preceders);
			return processPlanRepository.save(processPlan);
		}
		return processPlan;
	}

	/**
	 * @author vivek
	 * @param activityPlan
	 * @param startTime
	 * @param defKey
	 * @param serviceId
	 * @param serviceCode
	 * @param orderCode
	 * @return
	 */
	protected ActivityPlan updateActivityPlan(ActivityPlan activityPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution,String siteType,String startpreceders) {

		if (activityPlan != null) {
			activityPlan.setTargettedStartTime(startTime);
			activityPlan.setActiveVersion(getActiveVersion(execution));
			activityPlan.setStartPreceders(startpreceders);
			return activityPlanRepository.save(activityPlan);

		}
		return activityPlan;

	}

	/**
	 * @author vivek
	 * @param activityPlan
	 * @param startTime
	 * @param execution
	 * @return
	 */
	protected ActivityPlan updateActivityCompletion(ActivityPlan activityPlan, Timestamp startTime,
			DelegateExecution execution,String preceders) {
		if (activityPlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

			LOGGER.info("activity completed end date {}",
					execution.getVariable(execution.getCurrentActivityId() + "_endDate"));
			activityPlan.setTargettedEndTime(startTime);
			activityPlan.setEndPreceders(preceders);
			return activityPlanRepository.save(activityPlan);
		}
		return activityPlan;

	}

	/**
	 * @author vivek
	 * @param taskPlan
	 * @param startTime
	 * @param execution
	 * @param serviceId
	 * @param serviceCode
	 * @param orderCode
	 * @param defKey
	 * @param isDelayedTask
	 * @return
	 */
	private TaskPlan updateTaskPlan(TaskPlan taskPlan, Timestamp startTime, DelegateExecution execution,
			Integer serviceId, String serviceCode, String orderCode, String defKey,String preceders,String siteType)

	{
		Timestamp dueDate = null;

		MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);

		Timestamp maxDate = startTime;
		
		if (taskPlan != null) {
			taskPlan.setTargettedStartTime(startTime);
			Task task = taskPlan.getTask();
			if (taskPlan.getTask() != null) {

				LOGGER.info("updateTaskPlan for taskPlan.getTask() != null for service:{}", serviceCode);
				if (task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
						|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
						|| task.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED)
						|| task.getMstStatus().getCode()
								.equalsIgnoreCase(TaskStatusConstants.INPROGRESS)) {
					startTime = new Timestamp(new Date().getTime());
					dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
					LOGGER.info("updateTaskPlan for open case for service:{} and due date :{}", serviceCode, dueDate);
				} else if (taskPlan.getTask().getMstStatus().getCode()
						.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
					dueDate = task.getCompletedTime()!=null ? task.getCompletedTime() : task.getCreatedTime();
					LOGGER.info("updateTaskPlan for closed case for service:{} and due date :{}", serviceCode, dueDate);

				} else {
					dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
					LOGGER.info("updateTaskPlan for else case for service:{} and due date :{}", serviceCode, dueDate);

				}

			} 
			
			 else if (taskRepository.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeOrderByCreatedTimeDesc(serviceId,
						defKey, siteType) != null) {

				 	LOGGER.info(
						"updateTaskPlan for taskRepository.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeOrderByCreatedTimeDesc(serviceId ,defKey, siteType) != null for service:{}",
						serviceCode);
					Task openTask = taskRepository.findFirstByServiceIdAndMstTaskDef_keyAndSiteTypeOrderByCreatedTimeDesc(serviceId,
							defKey, siteType);
					taskPlan.setTask(openTask);
					taskPlanRepository.save(taskPlan);
					LOGGER.info("updateTaskPlan updated for openTask for service:{}", serviceCode);

					if (openTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.REOPEN)
							|| openTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.HOLD)
							|| openTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.OPENED)
							|| openTask.getMstStatus().getCode().equalsIgnoreCase(TaskStatusConstants.INPROGRESS)) {
						startTime = new Timestamp(new Date().getTime());
						dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
						LOGGER.info("updateTaskPlan for open case for service:{} and due date :{}", serviceCode, dueDate);
					} else if (openTask.getMstStatus().getCode()
							.equalsIgnoreCase(TaskStatusConstants.CLOSED_STATUS)) {
						dueDate = openTask.getCompletedTime();
						LOGGER.info("updateTaskPlan for closed case for service:{} and due date :{}", serviceCode, dueDate);

					} else {
						dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
						LOGGER.info("updateTaskPlan for else case for service:{} and due date :{}", serviceCode, dueDate);

					}
				}
			else {

				LOGGER.info("updateTaskPlan updated for else block for service:{}", serviceCode);

				if (taskPlan.getActualStartTime() != null && taskPlan.getActualEndTime() == null) {
					startTime = new Timestamp(new Date().getTime());
					dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
					LOGGER.info("updateTaskPlan for else without task case for service:{} and due date :{}",
							serviceCode, dueDate);

				}

				else if (taskPlan.getActualEndTime() != null) {
					dueDate = taskPlan.getActualEndTime();
					LOGGER.info(
							"updateTaskPlan for else without task  getActualEndTime case for service:{} and due date :{}",
							serviceCode, dueDate);

				} else {
					dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
					LOGGER.info("updateTaskPlan for else without task no matches case for service:{} and due date :{}",
							serviceCode, dueDate);

				}

			}

			dueDate = maxDateCalc(dueDate,maxDate);
			
			LOGGER.info("due date info: {} ", dueDate);
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", dueDate);
			taskPlan.setTargettedEndTime(dueDate);
			taskPlan.setServiceId(serviceId);
			taskPlan.setServiceCode(serviceCode);
			taskPlan.setOrderCode(orderCode);
			taskPlan.setActiveVersion(getActiveVersion(execution));
			taskPlan.setPreceders(preceders);

			return taskPlanRepository.save(taskPlan);

		}

		return taskPlan;

	}

	private Timestamp maxDateCalc(Timestamp dueDate, Timestamp maxDate) {
		
		boolean valueTs = dueDate.after(maxDate);
		if(valueTs) {
			LOGGER.info("maxDateCalc comparison between dueDate {} and maxDate(startTime) {} with valueTs {} returning dueDate {}",dueDate,maxDate,valueTs,dueDate);
			return dueDate;
		} else {
			LOGGER.info("maxDateCalc comparison between dueDate {} and maxDate(startTime) {} with valueTs {} returning maxDate {}",dueDate,maxDate,valueTs,maxDate);
			return maxDate;
		}
		
	}

	private TaskPlan createTaskPlan(TaskPlan taskPlan, Timestamp startTime, DelegateExecution execution,
			Integer serviceId, String serviceCode, String orderCode, String defKey, Map<String, Object> fMap) {

		if (taskPlan == null) {
			taskPlan = new TaskPlan();
			MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);
			taskPlan.setTargettedEndTime(startTime);
			taskPlan.setPlannedStartTime(startTime);
			taskPlan.setEstimatedStartTime(startTime);
			Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
			LOGGER.info("due date info: {} ", dueDate);
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", dueDate);
			taskPlan.setTargettedEndTime(dueDate);
			taskPlan.setPlannedEndTime(dueDate);
			taskPlan.setEstimatedEndTime(dueDate);
			taskPlan.setMstTaskDef(mstTaskDef);
			taskPlan.setServiceId(serviceId);
			taskPlan.setServiceCode(serviceCode);
			taskPlan.setOrderCode(orderCode);
			String siteType = (String) fMap.get("site_type");
			if (siteType == null) {
				taskPlan.setSiteType("A");

			} else {
				taskPlan.setSiteType(siteType);

			}

		}
		return taskPlanRepository.save(taskPlan);

	}

}
