package com.tcl.dias.servicefulfillmentutils.processplan.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SITE_DETAIL_ID;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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
import com.tcl.dias.servicefulfillment.entity.entities.MstProcessDef;
import com.tcl.dias.servicefulfillment.entity.entities.MstTaskDef;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstProcessDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.MstTaskDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScSolutionComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TATService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("cgwPlanDelegate")
public class CGWPlanDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(CGWPlanDelegate.class);

	private Expression preceders;
	
	@Autowired
	ActivityPlanRepository activityPlanRepository;
	
	@Autowired
	TaskPlanRepository taskPlanRepository;
	
	@Autowired
	ScSolutionComponentRepository scSolutionComponentRepository;
	
	@Autowired
	TaskCacheService taskCacheService;

	@Autowired
	MstTaskDefRepository mstTaskDefRepository;
	
	@Autowired
	TATService tatService;
	
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("CGWPlanDelegate invoked for{} and preceders{}", execution.getCurrentActivityId(), preceders);
		try {
			Map<String, Object> fMap = execution.getVariables();
			Integer serviceId = (Integer) fMap.get(SERVICE_ID);
			String serviceCode = (String) fMap.get(SERVICE_CODE);
			String orderCode = (String) fMap.get(ORDER_CODE);
			Integer solutionId = (Integer) fMap.get("solutionId");
			String processType = (String) fMap.get("processType");
			String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
			if(StringUtils.isBlank(siteType))siteType="A";
			Integer siteId=(Integer)fMap.get(SITE_DETAIL_ID);
			String precedersValues = (String) preceders.getValue(execution);
			logger.info("SolutionId::{},Service Id::{},siteType::{},processType::{}", solutionId,serviceId,siteType,processType);
			Set<Timestamp> maxDates = new TreeSet<Timestamp>(Collections.reverseOrder());
			String defKey = execution.getCurrentActivityId().replaceAll("_plan", "");
			defKey = defKey.replaceAll("_end", "");
			MstTaskDef mstTaskDef = mstTaskDefRepository.findByKey(defKey);
			List<Integer> overlayServiceIdList=null;
			overlayServiceIdList=scSolutionComponentRepository.findAllByScServiceDetail3_idAndComponentGroupAndPriorityAndIsActive(solutionId,"OVERLAY","DC/DR","Y");
			if(overlayServiceIdList==null || overlayServiceIdList.isEmpty()){
				logger.info("Overlay with DC/DR not exists");
				overlayServiceIdList=scSolutionComponentRepository.findAllByScServiceDetail3_idAndComponentGroupAndPriorityAndIsActive(solutionId,"OVERLAY","P1","Y");
			}
			if(overlayServiceIdList!=null && !overlayServiceIdList.isEmpty()){
				logger.info("Overlay with DC/DR or P1 exists::{}",overlayServiceIdList.size());
				List<String> defKeys=new ArrayList<>();
				defKeys.add("sdwan-service-acceptance");
				List<TaskPlan> overlayTaskPlanList=taskPlanRepository.findByServiceIdInAndMstTaskDefKey(overlayServiceIdList, defKeys, siteType);
				List<Timestamp> targetDeliveryTimeStamp= new ArrayList<>();
				TaskPlan taskPlan = null;
				if(overlayTaskPlanList!=null && !overlayTaskPlanList.isEmpty()){
					for(TaskPlan overlayTaskPlan:overlayTaskPlanList){
						targetDeliveryTimeStamp.add(overlayTaskPlan.getActualEndTime()!=null?overlayTaskPlan.getActualEndTime():overlayTaskPlan.getTargettedEndTime());
					}
					logger.info("Target DeliveryTimestamp size {}", targetDeliveryTimeStamp.size());
					maxDates.addAll(targetDeliveryTimeStamp);
					logger.info("Max date size {}", maxDates.size());
					Timestamp startTime = maxDates.stream().findFirst().orElse(null);
					logger.info("CGW Id {} with max date {}", execution.getCurrentActivityId(),startTime);
					if (serviceId != null) {
						taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId, defKey, siteType);
					}
					logger.info("TaskPlan info: {} ", taskPlan);
					logger.info("TaskPlan info: {} Parent key={}, PLAN_ID {}", taskPlan,mstTaskDef.getMstActivityDef().getKey(),fMap.get(mstTaskDef.getMstActivityDef().getKey() + "PLAN_ID"));
					if (taskPlan == null) {
						taskPlan = new TaskPlan();
						taskPlan.setEstimatedStartTime(startTime);
						taskPlan.setTargettedStartTime(startTime);
						taskPlan.setPlannedStartTime(startTime);
						taskPlan.setMstTaskDef(mstTaskDef);
						taskPlan.setServiceId(serviceId);
						taskPlan.setMstStatus(taskCacheService.getMstStatus(TaskStatusConstants.PENDING));
						Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
						logger.info("Task not exists and due date info: {} ", dueDate);
						taskPlan.setServiceCode(serviceCode);
						taskPlan.setServiceId(serviceId);
						taskPlan.setSiteId(siteId);
						taskPlan.setOrderCode(orderCode);
						taskPlan.setServiceId(serviceId);
						execution.setVariable(execution.getCurrentActivityId()+"_endDate", dueDate);
						taskPlan.setEstimatedEndTime(dueDate);
						taskPlan.setTargettedEndTime(dueDate);
						taskPlan.setPlannedEndTime(dueDate);
						taskPlan.setServiceId(serviceId);
						taskPlan.setSiteType(siteType);
						if(fMap.get(mstTaskDef.getMstActivityDef().getKey() + "PLAN_ID")!=null) {
							logger.info("CGW Plan Task Activity key exists: {} ", fMap.get(mstTaskDef.getMstActivityDef().getKey() + "PLAN_ID"));
							Optional<ActivityPlan> activityOptional = activityPlanRepository
									.findById((Integer) fMap.get(mstTaskDef.getMstActivityDef().getKey() + "PLAN_ID"));
							if (activityOptional.isPresent()) {
								logger.info("CGW Plan Task Activity exists: {} ", activityOptional.get().getId());
								taskPlan.setActivityPlan(activityOptional.get());
							}
						}else {
							logger.info("CGW Plan Task Activity key not exists: {} ", fMap.get(mstTaskDef.getMstActivityDef().getKey() + "PLAN_ID"));
							ActivityPlan activityPlan=activityPlanRepository.findFirstByServiceIdAndMstActivityDefKey(serviceId,mstTaskDef.getMstActivityDef().getKey(),siteType);
							if(activityPlan!=null) {
								logger.info("CGW Plan Task Activity exists: {} ", activityPlan.getId());
								taskPlan.setActivityPlan(activityPlan);
							}
						}
					} else if (startTime.toLocalDateTime().isAfter(taskPlan.getEstimatedStartTime().toLocalDateTime())) {
						taskPlan.setEstimatedStartTime(startTime);
						taskPlan.setTargettedStartTime(startTime);
						taskPlan.setPlannedStartTime(startTime);
						Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
						taskPlan.setEstimatedEndTime(dueDate);
						taskPlan.setTargettedEndTime(dueDate);
						taskPlan.setPlannedEndTime(dueDate);
						execution.setVariable(execution.getCurrentActivityId()+"_endDate", dueDate);
						logger.info("Task LocalDateTime greater than estimated time and due date info: {} ", dueDate);
					}else {
						Timestamp dueDate = tatService.calculateDueDate(mstTaskDef.getTat(), mstTaskDef.getOwnerGroup(), startTime);
						execution.setVariable(execution.getCurrentActivityId()+"_endDate", dueDate);
						logger.info("due date info: {} ", dueDate);
					}
					taskPlan.setPreceders(precedersValues);
					logger.info("updateTaskPlan completed {}", taskPlan);
					taskPlanRepository.save(taskPlan);
					taskPlan.setActiveVersion(1L);
					execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();	
			throw new BpmnError(e.getMessage());
		}
	}
}
