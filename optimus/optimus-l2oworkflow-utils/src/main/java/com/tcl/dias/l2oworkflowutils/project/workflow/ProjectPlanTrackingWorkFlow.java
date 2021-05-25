package com.tcl.dias.l2oworkflowutils.project.workflow;

import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.l2oworkflowutils.constants.MasterDefConstants.SITE_ID;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tcl.dias.l2oworkflow.entity.entities.ActivityPlan;
import com.tcl.dias.l2oworkflow.entity.entities.MstTaskDef;
import com.tcl.dias.l2oworkflow.entity.entities.ProcessPlan;
import com.tcl.dias.l2oworkflow.entity.entities.StagePlan;
import com.tcl.dias.l2oworkflow.entity.entities.TaskPlan;
import com.tcl.dias.l2oworkflowutils.constants.TaskStatusConstants;
import com.tcl.dias.l2oworkflowutils.factory.IProjectWorkFlowHandler;
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
		Timestamp startTime = getMaxDate(precedersValues, execution, stagePlan.getTargettedStartTime());
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		LOGGER.info("stagePlan {} ", stagePlan);
		updateStage(stagePlan, startTime, defKey, serviceId, orderId, siteId, serviceCode, orderCode, execution);
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
		ProcessPlan processPlan = null;
		if (serviceId != null) {
			processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey);
		}

		else if (siteId != null) {
			processPlan = processPlanRepository.findBySiteIdAndMstProcessDefKey(siteId, defKey);
		}
		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);
		if (processPlan == null) {

			processPlan = createProcessPlan(startTime, defKey, serviceId, siteId, serviceCode, orderCode, execution);
		}

		LOGGER.info("processPlan with preceders {} ", precedersValues);

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

		LOGGER.info("processPlan info: {} ", processPlan);

		updateProcessPlan(processPlan, startTime, defKey, serviceId, siteId, serviceCode, orderCode, execution);

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

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		if (serviceId != null) {
			activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey);
		} else if (siteId != null) {
			activityPlan = activityPlanRepository.findBySiteIdAndMstActivityDefKey(siteId, defKey);

		}

		if (activityPlan == null) {
			activityPlan = createActivityPlan(activityPlan, startTime, defKey, serviceId, siteId, serviceCode,
					orderCode, execution);
		}
		LOGGER.info("activityPlan info: {} ", activityPlan);
		updateActivityPlan(activityPlan, startTime, defKey, serviceId, siteId, serviceCode, orderCode, execution);

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
	 * @return
	 */
	private ActivityPlan createActivityPlan(ActivityPlan activityPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution) {
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

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		LOGGER.info("processTaskPlan started {} ", defKey);

		taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, defKey);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("TaskPlan with preceders {} ", precedersValues);
		startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date: {} ", startTime);

		if (taskPlan == null) {
			createTaskPlan(taskPlan, startTime, execution, serviceId, serviceCode, orderCode, defKey);
		}

		updateTaskPlan(taskPlan, startTime, execution, serviceId, serviceCode, orderCode, defKey);

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

		activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey);

		LOGGER.info("ActivityPlan with preceders {} ", precedersValues);

		updateActivityCompletion(activityPlan, startTime, execution);
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

		processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey);
		LOGGER.info("processPlan info: {} ", processPlan);
		updateProcessPlanCompletion(processPlan, startTime, defKey, serviceId, execution);

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

		updateStageCompletion(stagePlan, execution, startTime);

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
			Integer orderId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution) {

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
	@Override
	protected StagePlan updateStageCompletion(StagePlan stagePlan, DelegateExecution execution, Timestamp startTime) {
		if (stagePlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

			stagePlan.setTargettedEndTime(startTime);
			return stagePlanRepository.save(stagePlan);

		}

		return stagePlan;

	}

	private ProcessPlan createProcessPlan(Timestamp startTime, String defKey, Integer serviceId, Integer siteId,
			String serviceCode, String orderCode, DelegateExecution execution) {
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
	@Override
	protected ProcessPlan updateProcessPlan(ProcessPlan processPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution) {
		if (processPlan != null) {
			processPlan.setTargettedStartTime(startTime);
			processPlan.setActiveVersion(getActiveVersion(execution));
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
	@Override
	protected ProcessPlan updateProcessPlanCompletion(ProcessPlan processPlan, Timestamp startTime, String defKey,
			Integer serviceId, DelegateExecution execution) {

		if (processPlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
			processPlan.setTargettedEndTime(startTime);
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
	@Override
	protected ActivityPlan updateActivityPlan(ActivityPlan activityPlan, Timestamp startTime, String defKey,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, DelegateExecution execution) {

		if (activityPlan != null) {
			activityPlan.setTargettedStartTime(startTime);
			activityPlan.setActiveVersion(getActiveVersion(execution));
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
	@Override
	protected ActivityPlan updateActivityCompletion(ActivityPlan activityPlan, Timestamp startTime,
			DelegateExecution execution) {
		if (activityPlan != null) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

			LOGGER.info("activity completed end date {}",
					execution.getVariable(execution.getCurrentActivityId() + "_endDate"));
			activityPlan.setTargettedEndTime(startTime);
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
			Integer serviceId, String serviceCode, String orderCode, String defKey)

	{
		Timestamp dueDate =null;

		MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);

		if (taskPlan != null) {

			taskPlan.setTargettedStartTime(startTime);
			if(taskPlan.getActualEndTime()!=null) {
				dueDate=taskPlan.getActualEndTime();
			}
			else {
				dueDate	= tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
			}
			LOGGER.info("due date info: {} ", dueDate);
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", dueDate);
			taskPlan.setTargettedEndTime(dueDate);
			taskPlan.setServiceId(serviceId);
			taskPlan.setServiceCode(serviceCode);
			taskPlan.setOrderCode(orderCode);
			taskPlan.setActiveVersion(getActiveVersion(execution));
			return taskPlanRepository.save(taskPlan);

		}

		return taskPlan;

	}

	private TaskPlan createTaskPlan(TaskPlan taskPlan, Timestamp startTime, DelegateExecution execution,
			Integer serviceId, String serviceCode, String orderCode, String defKey) {

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
		}
		return taskPlanRepository.save(taskPlan);

	}

}
