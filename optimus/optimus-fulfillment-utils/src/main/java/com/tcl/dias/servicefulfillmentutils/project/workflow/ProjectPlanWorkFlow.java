package com.tcl.dias.servicefulfillmentutils.project.workflow;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CREATED_DATE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SC_ORDER_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SITE_DETAIL_ID;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.MstActivityDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstProcessDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstStageDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.factory.IProjectWorkFlowHandler;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class ProjectPlanWorkFlow extends ProjectPlanAbstractService implements IProjectWorkFlowHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProjectPlanWorkFlow.class);
	
	private static final Long ACTIVE_VERSION_NUMBER=1L;


	@Override
	public Boolean processStagePlanStart(DelegateExecution execution, Expression preceders) throws TclCommonException {
		LOGGER.info("StagePlanStart with def key {} ", execution.getCurrentActivityId());

		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		defKey = defKey.replaceAll("_start", "");
		MstStageDef mstStageDef = mstStageDefRepository.findByKey(defKey);

		Map<String, Object> fMap = execution.getVariables();

		Integer orderId = (Integer) fMap.get(SC_ORDER_ID);
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId=(Integer)fMap.get(SITE_DETAIL_ID);
		StagePlan stagePlan = null;
		String precedersValues = (String) preceders.getValue(execution);
	//	LOGGER.info("variable map {} ", execution.getVariables());
		
		String O2CPROCESSKEY= fMap.get("O2CPROCESSKEY")!=null?(String)fMap.get("O2CPROCESSKEY"):null;
		
		LOGGER.info("StagePlanStart with service:{} and O2CPROCESSKEY {} ",serviceCode, O2CPROCESSKEY);



		LOGGER.info("StagePlanStart with preceders {} ", precedersValues);

		Timestamp startTime = getMaxDate(precedersValues, execution);

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		

		LOGGER.info("max date: {} ", startTime);
		
		if (serviceId != null) {

			stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
			LOGGER.info("stagePlan {} ", stagePlan);
		}

		else if (siteId != null) {
			stagePlan = stagePlanRepository.findBySiteIdAndMstStageDefKey(siteId, defKey);

		}

		stagePlan = updateStage(stagePlan, startTime, defKey, serviceId, orderId,siteId, serviceCode, orderCode,execution,precedersValues);
		updateDetailsForOrderPlacement(stagePlan,fMap);
		execution.setVariable(mstStageDef.getKey() + "PLAN_ID", stagePlan.getId());
		LOGGER.info("StagePlanStart ended for {} ", stagePlan.getId());

		return true;

	}

	private void updateDetailsForOrderPlacement(StagePlan stagePlan, Map<String, Object> fMap) {
		if(stagePlan!=null && stagePlan.getMstStageDef().getKey().equalsIgnoreCase("order_placement_stage")) {

			if (fMap.containsKey(ORDER_CREATED_DATE)) {
				Timestamp createdDate = (Timestamp) fMap.get(ORDER_CREATED_DATE);
				stagePlan.setPlannedStartTime(createdDate);
				stagePlan.setTargettedStartTime(createdDate);
				stagePlan.setEstimatedStartTime(createdDate);
				stagePlan.setEstimatedEndTime(createdDate);
				stagePlan.setTargettedEndTime(createdDate);
				stagePlan.setPlannedEndTime(createdDate);
				stagePlan.setActualStartTime(createdDate);
				stagePlan.setActualEndTime(createdDate);

				stagePlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.CLOSED_STATUS));
				stagePlanRepository.save(stagePlan);
			}else {
				LOGGER.info("ORDER_CREATED_DATE is null");
			}
		}		
	}

	@Override
	public Boolean processStagePlanCompletion(DelegateExecution execution, Expression preceders)
			throws TclCommonException {
		LOGGER.info("StagePlanCompletion Started with def key {} ", execution.getCurrentActivityId());

		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		defKey = defKey.replaceAll("_end", "");
		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		Integer siteId=fMap.containsKey(SITE_DETAIL_ID)?(Integer)fMap.get(SITE_DETAIL_ID):null;
		String O2CPROCESSKEY = fMap.get("O2CPROCESSKEY") != null ? (String) fMap.get("O2CPROCESSKEY") : null;

		LOGGER.info("processStagePlanCompletion with service:{} and O2CPROCESSKEY {} ", serviceId, O2CPROCESSKEY);


		String precedersValues = (String) preceders.getValue(execution);
		//LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("StagePlanCompletion with preceders {} ", precedersValues);

		StagePlan stagePlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date: {} ", startTime);

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);

		LOGGER.info("max date  {} ", startTime);

		stagePlan = stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, defKey);
		if(siteId!=null) {
			stagePlan = stagePlanRepository.findBySiteIdAndMstStageDefKey(siteId, defKey);
		}
		updateStageCompletion(stagePlan, execution, startTime, precedersValues);
		updateDetailsForOrderPlacement(stagePlan, fMap);
		LOGGER.info("StagePlancompletion ended with def key {} ", execution.getCurrentActivityId());

		return true;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		LOGGER.info("processPlan  started with def key {} ",  execution.getCurrentActivityId());

		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		defKey = defKey.replaceAll("_start", "");

		MstProcessDef mstProcessDef = mstProcessDefRepository.findByKey(defKey);


		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		String precedersValues = (String) preceders.getValue(execution);
		Integer siteId=fMap.containsKey(SITE_DETAIL_ID)?(Integer)fMap.get(SITE_DETAIL_ID):null;
		
		String O2CPROCESSKEY = fMap.get("O2CPROCESSKEY") != null ? (String) fMap.get("O2CPROCESSKEY") : null;

		LOGGER.info("processPlan key: {} with service:{} and O2CPROCESSKEY {} ", defKey, serviceCode, O2CPROCESSKEY);

		ProcessPlan processPlan = null;
		//LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("processPlan key:{} with preceders {} ", defKey,precedersValues);

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date: {} ", startTime);
		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		
		LOGGER.info("defKey: {} siteType: {} ", defKey,siteType);

		if (serviceId != null) {
			processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
		}

		else if (siteId != null) {
			processPlan = processPlanRepository.findBySiteIdAndMstProcessDefKey(siteId, defKey);
		}
		LOGGER.info("processPlan info: {} Parent key={}, PLAN_ID {}", processPlan, mstProcessDef.getMstStageDef().getKey(),fMap.get(mstProcessDef.getMstStageDef().getKey() + "PLAN_ID"));
	
		Optional<StagePlan> stagePlan = stagePlanRepository
				.findById((Integer) fMap.get(mstProcessDef.getMstStageDef().getKey() + "PLAN_ID"));
		processPlan = updateProcessPlan(processPlan, startTime, defKey, serviceId,siteId, serviceCode, orderCode,execution,siteType,precedersValues);

		if (stagePlan.isPresent() && processPlan.getStagePlan() == null) {
			processPlan.setStagePlan(stagePlan.get());
			processPlanRepository.save(processPlan);
		}

		execution.setVariable(mstProcessDef.getKey() + "PLAN_ID", processPlan.getId());

		LOGGER.info("processPlan created with id {} ", processPlan.getId());

		return true;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processPlanCompletion(DelegateExecution execution, Expression preceders) throws TclCommonException {
		LOGGER.info("processPlanCompletion Started with def key {} ", execution.getCurrentActivityId());

		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		defKey = defKey.replaceAll("_end", "");

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		Integer siteId=fMap.containsKey(SITE_DETAIL_ID)?(Integer)fMap.get(SITE_DETAIL_ID):null;
		
		String O2CPROCESSKEY = fMap.get("O2CPROCESSKEY") != null ? (String) fMap.get("O2CPROCESSKEY") : null;

		LOGGER.info("processPlan with key:{} service:{} and O2CPROCESSKEY {} ", defKey,serviceId, O2CPROCESSKEY);


		String precedersValues = (String) preceders.getValue(execution);
		//LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("processPlanCompletion with preceders {} ", precedersValues);

		ProcessPlan processPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date: {} ", startTime);

		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		
		LOGGER.info("defKey: {} siteType: {} ", defKey,siteType);

		processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
		LOGGER.info("processPlan info: {} ", processPlan);
		if(siteId!=null) {
			processPlan=processPlanRepository.findBySiteIdAndMstProcessDefKey(siteId, defKey);
		}
		updateProcessPlanCompletion(processPlan, startTime, defKey, serviceId, execution, precedersValues);
		
		LOGGER.info("processPlanCompletion ended with def key {} ", defKey);
		return true;

	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processActivityPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		LOGGER.info("ActivityPlan Started with def key {} ", execution.getCurrentActivityId());

		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_start_plan", "");
		defKey = defKey.replaceAll("_start", "");
		MstActivityDef mstActivitDef = mstActivityDefRepository.findByKey(defKey);

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId=(Integer)fMap.get(SITE_DETAIL_ID);
		
		String O2CPROCESSKEY = fMap.get("O2CPROCESSKEY") != null ? (String) fMap.get("O2CPROCESSKEY") : null;

		LOGGER.info("processActivityPlan with key:{} service:{} and O2CPROCESSKEY {} ",defKey, serviceCode, O2CPROCESSKEY);

		String precedersValues = (String) preceders.getValue(execution);
		//LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("ActivityPlan with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		
		LOGGER.info("max date: {} ", startTime);

		execution.setVariable(execution.getCurrentActivityId() + "_endDate", startTime);
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		if (serviceId != null) {
			activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);
			LOGGER.info("activityPlan info: {} Parent key={}, PLAN_ID {}", activityPlan,
					mstActivitDef.getMstProcessDef().getKey(),
					fMap.get(mstActivitDef.getMstProcessDef().getKey() + "PLAN_ID"));
		} else if (siteId != null) {
			activityPlan = activityPlanRepository.findBySiteIdAndMstActivityDefKey(siteId, defKey);

		}
		activityPlan = updateActivityPlan(activityPlan, startTime, defKey, serviceId,siteId, serviceCode, orderCode,execution,siteType,precedersValues);
		activityPlan.setActiveVersion(ACTIVE_VERSION_NUMBER);
		ProcessPlan processPlan=null;
		if (fMap.get(mstActivitDef.getMstProcessDef().getKey() + "PLAN_ID") != null) {
			Optional<ProcessPlan> optionalProcess = processPlanRepository
					.findById((Integer) fMap.get(mstActivitDef.getMstProcessDef().getKey() + "PLAN_ID"));

			processPlan = optionalProcess.isPresent() ? optionalProcess.get() : null;
		}
		else {
			processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId,
					mstActivitDef.getMstProcessDef().getKey(), siteType);
		}

		if (processPlan != null && activityPlan.getProcessPlan() == null) {
			activityPlan.setProcessPlan(processPlan);
			activityPlanRepository.save(activityPlan);

		}
		
		execution.setVariable(mstActivitDef.getKey() + "PLAN_ID", activityPlan.getId());
		LOGGER.info("processActivityPlan created Id {} ", activityPlan.getId());

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
		LOGGER.info("ActivityPlanCompletion Started with def key {} ", execution.getCurrentActivityId());

		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
		defKey = defKey.replaceAll("_end", "");

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);

		
		Integer siteId = (Integer) fMap.get(SITE_DETAIL_ID);
		
		String O2CPROCESSKEY = fMap.get("O2CPROCESSKEY") != null ? (String) fMap.get("O2CPROCESSKEY") : null;

		LOGGER.info("processActivityPlanCompletion with key:{} service:{} and O2CPROCESSKEY {} ", defKey,serviceId, O2CPROCESSKEY);

		String precedersValues = (String) preceders.getValue(execution);
		
		//LOGGER.info("variable map {} ", execution.getVariables());

		LOGGER.info("ActivityPlanCompletion with preceders {} ", precedersValues);

		ActivityPlan activityPlan = null;

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("max date: {} ", startTime);
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";
		
		activityPlan = activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, defKey, siteType);
		if(siteId!=null) {
			activityPlan = activityPlanRepository.findBySiteIdAndMstActivityDefKey(siteId, defKey);

		}
		updateActivityCompletion(activityPlan, startTime, execution,precedersValues);
		LOGGER.info("processActivityPlanCompletion ended : {} ", activityPlan);

		return true;
	}

	/**
	 * @author vivek
	 * @param execution
	 * @param preceders
	 * @throws TclCommonException
	 */
	public Boolean processTaskPlan(DelegateExecution execution, Expression preceders) throws TclCommonException {
		LOGGER.info("TaskPlan Started with def key {} ", execution.getCurrentActivityId());

		Map<String, Object> fMap = execution.getVariables();
		String defKey = execution.getCurrentActivityId().replaceAll("_plan", "");
		defKey = defKey.replaceAll("_end", "");

		MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(defKey);

		Integer serviceId = (Integer) fMap.get(SERVICE_ID);
		String serviceCode = (String) fMap.get(SERVICE_CODE);
		String orderCode = (String) fMap.get(ORDER_CODE);
		Integer siteId=(Integer)fMap.get(SITE_DETAIL_ID);

		String precedersValues = (String) preceders.getValue(execution);
		//LOGGER.info("variable map {} ", execution.getVariables());
		
		LOGGER.info("TaskPlan with preceders {} ", precedersValues);
		
		String O2CPROCESSKEY = fMap.get("O2CPROCESSKEY") != null ? (String) fMap.get("O2CPROCESSKEY") : null;

		LOGGER.info("processTaskPlan with key:{} service:{} and O2CPROCESSKEY {} ", defKey,serviceCode, O2CPROCESSKEY);

		TaskPlan taskPlan = null;
		
		String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
		if(StringUtils.isBlank(siteType))siteType="A";

		Timestamp startTime = getMaxDate(precedersValues, execution);
		LOGGER.info("{} max date: {} ", defKey,startTime);
		if (serviceId != null) {
			taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, defKey, siteType);
		}

		else if (siteId != null) {
			taskPlan = taskPlanRepository.findBySiteIdAndMstTaskDefKey(siteId, defKey);

		}
		LOGGER.info("TaskPlan info: {} ", taskPlan);
		LOGGER.info("TaskPlan info: {} Parent key={}, PLAN_ID {}", taskPlan,mstTakDef.getMstActivityDef().getKey(),fMap.get(mstTakDef.getMstActivityDef().getKey() + "PLAN_ID"));
		
		taskPlan = updateTaskPlan(taskPlan, startTime, execution, serviceId,siteId, serviceCode, orderCode, defKey,siteType,precedersValues);
		taskPlan.setActiveVersion(ACTIVE_VERSION_NUMBER);
		Optional<ActivityPlan> activityOptional = activityPlanRepository
				.findById((Integer) fMap.get(mstTakDef.getMstActivityDef().getKey() + "PLAN_ID"));

		if (activityOptional.isPresent() && taskPlan.getActivityPlan() == null) {
			taskPlan.setActivityPlan(activityOptional.get());
			taskPlanRepository.save(taskPlan);
		}
		LOGGER.info("TaskPlan ended with def key {} ", defKey);
		return true;
	}



}
