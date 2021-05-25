package com.tcl.dias.servicefulfillmentutils.project.workflow;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SITE_ID;

import java.sql.Timestamp;
import java.util.Map;

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
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillmentutils.factory.IProjectWorkFlowHandler;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used for estimation of delay
 *            process
 */
@Component
public class CustomerDelayWorkFlow extends ProjectPlanAbstractService implements IProjectWorkFlowHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDelayWorkFlow.class);

	@Override
	public Boolean processStagePlanStart(DelegateExecution execution, Expression preceders) throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		LOGGER.info("StagePlanStart started {} ", defKey);
		defKey = defKey.replaceAll("_start", "");

		Map<String, Object> fMap = execution.getVariables();

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		LOGGER.info("isCustomerDelayProcessEnabled: {} ", isCustomerDelayProcessEnabled);

		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer orderId = (Integer) fMap.get(SC_ORDER_ID);
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId=(Integer)fMap.get(SITE_ID);

		StagePlan stagePlan = null;

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("StagePlanStart with preceders {} ", precedersValues);

		Timestamp startTime = getMaxDate(precedersValues, execution);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

		LOGGER.info("max date  {} ", startTime);
		if (serviceId != null) {
			stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
		} else if (siteId != null) {
			stagePlan = stagePlanRepository.findBySiteIdAndMstStageDefKey(siteId, defKey);

		}
		LOGGER.info("stagePlan {} ", stagePlan);

		updateStage(stagePlan, startTime, defKey, serviceId,orderId,siteId, serviceCode, orderCode, execution);

		return true;

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
			Integer orderId,Integer siteId ,String serviceCode, String orderCode, DelegateExecution execution) {
		if (stagePlan != null
				&& startTime.toLocalDateTime().isAfter(stagePlan.getEstimatedStartTime().toLocalDateTime())) {

			stagePlan.setTargettedStartTime(startTime);
			//stagePlan.setPlannedStartTime(startTime);
			return stagePlanRepository.save(stagePlan);

		}

		else {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", stagePlan.getPlannedStartTime());

		}

		return stagePlan;
	}

	@Override
	public Boolean processStagePlanCompletion(DelegateExecution execution, Expression preceders)
			throws TclCommonException {
		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		defKey = defKey.replaceAll("_end", "");

		LOGGER.info("StagePlanCompletion started {} ", defKey);

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		LOGGER.info("isCustomerDelayProcessEnabled: {} ", isCustomerDelayProcessEnabled);

		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled) {
			return false;
		}
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

	/**
	 * @author vivek
	 * @param stagePlan
	 * @param execution
	 * @param startTime
	 * @return
	 */
	protected StagePlan updateStageCompletion(StagePlan stagePlan, DelegateExecution execution, Timestamp startTime) {
		if (stagePlan != null
				&& startTime.toLocalDateTime().isAfter(stagePlan.getEstimatedEndTime().toLocalDateTime())) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

			stagePlan.setTargettedEndTime(startTime);
			//stagePlan.setPlannedEndTime(startTime);
			return stagePlanRepository.save(stagePlan);

		} else {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", stagePlan.getPlannedEndTime());

		}

		return stagePlan;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		defKey = defKey.replaceAll("_start", "");

		LOGGER.info("processPlan started {} ", defKey);

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		LOGGER.info("isCustomerDelayProcessEnabled: {} ", isCustomerDelayProcessEnabled);

		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId=(Integer)fMap.get(SITE_ID);

		String precedersValues = (String) preceders.getValue(execution);
		ProcessPlan processPlan = null;

		LOGGER.info("processPlan with preceders {} ", precedersValues);

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";


		if (serviceId != null) {
			processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
		}

		else if (siteId != null) {
			processPlan = processPlanRepository.findBySiteIdAndMstProcessDefKey(siteId, defKey);
		}
		LOGGER.info("processPlan info: {} ", processPlan);

		updateProcessPlan(processPlan, startTime, defKey, serviceId, siteId, serviceCode, orderCode, execution,siteType);

		return true;

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
			Integer serviceId,Integer siteId, String serviceCode, String orderCode, DelegateExecution execution,String siteType) {
		if (processPlan != null
				&& startTime.toLocalDateTime().isAfter(processPlan.getEstimatedStartTime().toLocalDateTime())) {
			processPlan.setTargettedStartTime(startTime);
			//processPlan.setPlannedStartTime(startTime);
			return processPlanRepository.save(processPlan);

		}

		else {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", processPlan.getPlannedStartTime());

		}

		return processPlan;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processPlanCompletion(DelegateExecution execution, Expression preceders) throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		LOGGER.info("processPlanCompletion started {} ", defKey);
		defKey = defKey.replaceAll("_end", "");

		Map<String, Object> fMap = execution.getVariables();

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		LOGGER.info("isCustomerDelayProcessEnabled: {} ", isCustomerDelayProcessEnabled);

		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled) {
			return false;
		}
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
		updateProcessPlanCompletion(processPlan, startTime, defKey, serviceId, execution);

		return true;

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
			Integer serviceId, DelegateExecution execution) {

		if (processPlan != null
				&& startTime.toLocalDateTime().isAfter(processPlan.getEstimatedEndTime().toLocalDateTime())) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
			processPlan.setTargettedEndTime(startTime);
			//processPlan.setPlannedEndTime(startTime);
			return processPlanRepository.save(processPlan);
		} else {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", processPlan.getPlannedEndTime());

		}

		return processPlan;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processActivityPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		LOGGER.info("ActivityPlan started {} ", defKey);
		defKey = defKey.replaceAll("_start", "");

		Map<String, Object> fMap = execution.getVariables();

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		LOGGER.info("isCustomerDelayProcessEnabled: {} ", isCustomerDelayProcessEnabled);

		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId=(Integer)fMap.get(SITE_ID);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("ActivityPlan with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";


		if (serviceId != null) {
			activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);
			
		} else if (siteId != null) {
			activityPlan = activityPlanRepository.findBySiteIdAndMstActivityDefKey(siteId, defKey);

		}		LOGGER.info("activityPlan info: {} ", activityPlan);
		updateActivityPlan(activityPlan, startTime, defKey, serviceId,siteId, serviceCode, orderCode, execution,siteType);

		return true;
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
			Integer serviceId, Integer siteId, String serviceCode, String orderCode,DelegateExecution execution,String siteType) {

		if (startTime.toLocalDateTime().isAfter(activityPlan.getEstimatedStartTime().toLocalDateTime())) {
			activityPlan.setTargettedStartTime(startTime);
			//activityPlan.setPlannedStartTime(startTime);

		} else {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", activityPlan.getPlannedStartTime());

		}

		return activityPlanRepository.save(activityPlan);

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processActivityPlanCompletion(DelegateExecution execution, Expression preceders)
			throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		LOGGER.info("ActivityPlanCompletion started {} ", defKey);
		defKey = defKey.replaceAll("_end", "");

		Map<String, Object> fMap = execution.getVariables();

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		LOGGER.info("isCustomerDelayProcessEnabled: {} ", isCustomerDelayProcessEnabled);

		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled) {
			return false;
		}
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		String precedersValues = (String) preceders.getValue(execution);

		LOGGER.info("ActivityPlanCompletion with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date  {} ", startTime);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);
		updateActivityCompletion(activityPlan, startTime, execution);
		LOGGER.info("activityPlan info: {} ", activityPlan);

		return true;
	}

	/**
	 * @author vivek
	 * @param activityPlan
	 * @param startTime
	 * @param execution
	 * @return
	 */
	protected ActivityPlan updateActivityCompletion(ActivityPlan activityPlan, Timestamp startTime,
			DelegateExecution execution) {
		if (activityPlan != null
				&& startTime.toLocalDateTime().isAfter(activityPlan.getEstimatedEndTime().toLocalDateTime())) {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

			LOGGER.info("activity completed end date {}",
					execution.getVariable(execution.getCurrentActivityId() + "_endDate"));
			activityPlan.setTargettedEndTime(startTime);
			//activityPlan.setPlannedEndTime(startTime);
			return activityPlanRepository.save(activityPlan);
		} else {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", activityPlan.getPlannedEndTime());

		}
		return activityPlan;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processTaskPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		String defKey = execution.getCurrentActivityId().replaceAll("_plan", "");
		LOGGER.info("TaskPlan started {} ", defKey);
		defKey = defKey.replaceAll("_end", "");

		Map<String, Object> fMap = execution.getVariables();
		String customerTask = (String) fMap.get("customerDelayTask");
		Timestamp startTime = null;
		Boolean isCustomerDelayedTask = false;

		Boolean isCustomerDelayProcessEnabled = (Boolean) fMap.get("customerDelayProcess");
		LOGGER.info("isCustomerDelayProcessEnabled: {} ", isCustomerDelayProcessEnabled);

		if (isCustomerDelayProcessEnabled == null)
			isCustomerDelayProcessEnabled = false;

		if (!isCustomerDelayProcessEnabled && isDelayedTask(customerTask, defKey)) {
			startTime = (Timestamp) fMap.get("delayTask_StartDate");
			execution.setVariable("customerDelayProcess", true);
			isCustomerDelayProcessEnabled = true;
			isCustomerDelayedTask = true;
		}

		if (!isCustomerDelayProcessEnabled) {
			return false;
		}

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId=(Integer)fMap.get(SITE_ID);

		String precedersValues = (String) preceders.getValue(execution);
		LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("TaskPlan with preceders {} ", precedersValues);

		TaskPlan taskPlan = null;

		if (!isCustomerDelayedTask) {
			startTime = getMaxDate(precedersValues, execution);
		}
		LOGGER.info("max date: {} ", startTime);
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		if (serviceId != null) {
			taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, defKey, siteType);
		}

		else if (siteId != null) {
			taskPlan = taskPlanRepository.findBySiteIdAndMstTaskDefKey(siteId, defKey);

		}		LOGGER.info("TaskPlan info: {} ", taskPlan);

		updateTaskPlan(taskPlan, startTime, execution, serviceId,siteId, serviceCode, orderCode, defKey,siteType);

		return true;
	}

	protected TaskPlan updateTaskPlan(TaskPlan taskPlan, Timestamp startTime, DelegateExecution execution,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, String defKey,String siteType) {
		MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);
		if (taskPlan != null
				&& startTime.toLocalDateTime().isAfter(taskPlan.getEstimatedStartTime().toLocalDateTime())) {
			taskPlan.setTargettedStartTime(startTime);
			Timestamp dueDate = null;
			if (taskPlan.getActualEndTime() != null) {
				dueDate = taskPlan.getActualEndTime();
			} else {
				dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
				LOGGER.info("due date info: {} ", dueDate);
				execution.setVariable(execution.getCurrentActivityId() + "_endDate", dueDate);
			}
			taskPlan.setTargettedEndTime(dueDate);
			return taskPlanRepository.save(taskPlan);

		} else {
			execution.setVariable(execution.getCurrentActivityId() + "_endDate", taskPlan.getPlannedEndTime());
		}

		return taskPlan;

	}

}
