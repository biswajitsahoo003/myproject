package com.tcl.dias.servicefulfillmentutils.processplan.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.BpmnError;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("overlayPlanDelegate")
public class OverlayPlanDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(OverlayPlanDelegate.class);

	private Expression preceders;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ProcessPlanRepository processPlanRepository;
	
	@Autowired
	MstTaskDefRepository mstTaskDefRepository;
	
	@Autowired
	ActivityPlanRepository activityPlanRepository;
	
	@Autowired
	TaskCacheService taskCacheService;
	
	@Autowired
	TaskPlanRepository taskPlanRepository;

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("OverlayPlanDelegate invoked for{} and preceders{}", execution.getCurrentActivityId(), preceders);
		try {
			Map<String, Object> fMap = execution.getVariables();
			Integer serviceId = (Integer) fMap.get(SERVICE_ID);
			String serviceCode = (String) fMap.get(SERVICE_CODE);
			String orderCode = (String) fMap.get(ORDER_CODE);
			String processType = (String) fMap.get("processType");
			logger.info("OverlayPlanDelegate.map details:{}", fMap);
			String defKey = execution.getCurrentActivityId().replaceAll("_plan", "");
			defKey = defKey.replaceAll("_end", "");
			logger.info("OverlayPlanDelegate.defKey:{}", defKey);
			MstTaskDef mstTakDef = mstTaskDefRepository.findByKey(defKey);
			Timestamp triggeredTimestamp=null;
			if(!processType.equalsIgnoreCase("computeProjectPlanTracking")){
				triggeredTimestamp=(Timestamp) fMap.get("root_endDate");
				logger.info("Underlay Id:{},Triggered Date:{}", serviceId,triggeredTimestamp);
				logger.info("Add 105 days for Underlay Id:{} with processType::{}", serviceId,processType);
				Calendar cal = Calendar.getInstance();
				cal.setTime(triggeredTimestamp);
				cal.add(Calendar.DAY_OF_WEEK, 105);
				triggeredTimestamp.setTime(cal.getTime().getTime());
				triggeredTimestamp = new Timestamp(cal.getTime().getTime());
			}
			logger.info("Underlay Id:{},Overlay CPE Date:{}", serviceId,triggeredTimestamp);
			Set<Timestamp> maxDates = new TreeSet<Timestamp>(Collections.reverseOrder());
			String precedersValues = (String) preceders.getValue(execution);
			logger.info("OverlayPlanDelegate with preceders {} ", precedersValues);
			TaskPlan taskPlan = null;
			String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
			if(StringUtils.isBlank(siteType))siteType="A";
			String[] precedersList = precedersValues.split(",");
			Arrays.stream(precedersList).forEach(preced -> {
				logger.info("OverlayPlanDelegate.precede name :{}| and date{}", preced,execution.getVariable(preced.trim()+"_endDate"));
				Timestamp endTime = (Timestamp) execution.getVariable(preced.trim()+"_endDate");
				if (endTime != null) {
					maxDates.add(endTime);
				} else {
					logger.info("skip  precede {}", preced);
				}
			});
			if(triggeredTimestamp!=null) {
				logger.info("triggeredTimestamp exists:: {}", triggeredTimestamp);
				maxDates.add(triggeredTimestamp);
			}
			logger.info("Max date size {}", maxDates.size());
			Timestamp startTime = maxDates.stream().findFirst().orElse(null);
			logger.info("Underlay Id {} with max date {}", execution.getCurrentActivityId(),startTime);
			if (serviceId != null) {
				taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, defKey, siteType);
			}
			logger.info("TaskPlan info: {} ", taskPlan);
			logger.info("TaskPlan info: {} Parent key={}, PLAN_ID {}", taskPlan,mstTakDef.getMstActivityDef().getKey(),fMap.get(mstTakDef.getMstActivityDef().getKey() + "PLAN_ID"));
			taskPlan = updateTaskPlan(taskPlan, startTime, execution, serviceId,null, serviceCode, orderCode, defKey,siteType,precedersValues);
			taskPlan.setActiveVersion(1L);
			if(taskPlan.getActivityPlan() == null) {
				Optional<ActivityPlan> activityOptional = activityPlanRepository
						.findById((Integer) fMap.get(mstTakDef.getMstActivityDef().getKey() + "PLAN_ID"));
				if (activityOptional.isPresent()) {
					logger.info("activity plan not exists");
					taskPlan.setActivityPlan(activityOptional.get());
					taskPlanRepository.save(taskPlan);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();	
			throw new BpmnError(e.getMessage());
		}
	}
	
	protected TaskPlan updateTaskPlan(TaskPlan taskPlan, Timestamp startTime, DelegateExecution execution,
			Integer serviceId, Integer siteId, String serviceCode, String orderCode, String defKey, String siteType, String preceders)
	{
		logger.info("updateTaskPlan for : {} startTime={} taskPlan={} ProcessInstanceId={}", defKey,startTime,taskPlan,execution.getProcessInstanceId());
		MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);
		if (taskPlan == null) {
			taskPlan = new TaskPlan();
			taskPlan.setEstimatedStartTime(startTime);
			taskPlan.setTargettedStartTime(startTime);
			taskPlan.setPlannedStartTime(startTime);
			taskPlan.setMstTaskDef(mstTaskDef);
			taskPlan.setServiceId(serviceId);
			taskPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
			logger.info("due date info: {} ", startTime);
			taskPlan.setServiceCode(serviceCode);
			taskPlan.setServiceId(serviceId);
			taskPlan.setSiteId(siteId);
			taskPlan.setOrderCode(orderCode);
			taskPlan.setServiceId(serviceId);
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);
			taskPlan.setEstimatedEndTime(startTime);
			taskPlan.setTargettedEndTime(startTime);
			taskPlan.setPlannedEndTime(startTime);
			taskPlan.setServiceId(serviceId);
			taskPlan.setSiteType(siteType);
		} else if (startTime.toLocalDateTime().isAfter(taskPlan.getEstimatedStartTime().toLocalDateTime())) {
			taskPlan.setEstimatedStartTime(startTime);
			taskPlan.setTargettedStartTime(startTime);
			taskPlan.setPlannedStartTime(startTime);
			taskPlan.setEstimatedEndTime(startTime);
			taskPlan.setTargettedEndTime(startTime);
			taskPlan.setPlannedEndTime(startTime);
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);
			logger.info("due date info: {} ", startTime);

		}else {
			execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);
		}
		taskPlan.setPreceders(preceders);
		logger.info("updateTaskPlan completed {}", taskPlan);
		return taskPlanRepository.save(taskPlan);
	}
}
