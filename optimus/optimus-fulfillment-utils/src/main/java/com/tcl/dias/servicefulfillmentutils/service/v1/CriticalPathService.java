package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * This file contains the CriticalPathService.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
@Transactional(readOnly = false, isolation = Isolation.READ_UNCOMMITTED)
public class CriticalPathService {
	
    private static final Logger logger = LoggerFactory.getLogger(CriticalPathService.class);
    private static final String CRITCAL_PATH_Y = "Y";
    
	@Autowired
	StagePlanRepository stagePlanRepository;
	
	@Autowired
	ProcessPlanRepository processPlanRepository;
	
	@Autowired
	ActivityPlanRepository activityPlanRepository;
	
	@Autowired
	TaskPlanRepository taskPlanRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Transactional
	public void computeCriticalPath(Integer serviceId) {
		
		try {
			List<StagePlan> stagePlans = stagePlanRepository.findByServiceId(serviceId);
			stagePlans.forEach(updateCrticalPath);		
		}catch(Exception ee) {
			logger.error("computeCriticalPath_exception_message={}",ee.getMessage());
			logger.error("computeCriticalPath_exception_trace={}",ee);
		}
	}
	
	private Consumer<? super TaskPlan> updateTaskCriticalPath =(taskPlan)	->{
		logger.info("Updating task_plan critical path. id: {}",taskPlan.getId());
		taskPlan.setCriticalPath(CRITCAL_PATH_Y);
		taskPlanRepository.save(taskPlan);
		logger.info("Updated task_plan critical path. id: {}",taskPlan.getId());
	};
	
	private Comparator<? super TaskPlan> sortTaskByActualEndDateDesc = (taskPlan1, taskPlan2)->{
		Optional<Timestamp> p1Optional = Optional.ofNullable(taskPlan1.getActualEndTime());
		Optional<Timestamp> p1Optiona2 = Optional.ofNullable(taskPlan2.getActualEndTime());
		
		if(p1Optional.isPresent() && p1Optiona2.isPresent()) {			
			if(taskPlan1.getActualEndTime().after(taskPlan2.getActualEndTime())) {
				return -1;
			}else if(taskPlan1.getActualEndTime().before(taskPlan2.getActualEndTime())) {
				return 1;
			}else {
				return 0;
			}
		}else if(p1Optional.isPresent()) {
			return -1;
		}else if(p1Optiona2.isPresent()) {
			return 1;
		}else {
			return 0;
		}	
	};
	
	private Consumer<? super ActivityPlan> updateActivityCriticalPath = (actiVityPlan)->{
		Set<TaskPlan> taskPlans = actiVityPlan.getTaskPlans();
		List<String> endPrecedersList = getPrecedersList(actiVityPlan.getEndPreceders());
		String activityDefKey = actiVityPlan.getMstActivityDef().getKey();
		logger.info("Updating activity_plan critical path. id: {} activityDefKey: {}",actiVityPlan.getId(),activityDefKey);

		if(endPrecedersList !=null) {		
			if(taskPlans.size()>1) {
				TaskPlan taskPlan = taskPlans.stream().filter(tasPlans -> endPrecedersList.contains(tasPlans.getMstTaskDef().getKey())).sorted(sortTaskByActualEndDateDesc).findFirst().orElse(null);
				if(taskPlan != null) {
				updateTaskCriticalPath.accept(taskPlan);
									
					List<String> startPrecedersList = getPrecedersList(taskPlan.getPreceders());
						String startPrecederDefKey = getFirstElement(startPrecedersList);
						List<String> processedTasks = new ArrayList<String>();
						processedTasks.add(taskPlan.getMstTaskDef().getKey());
		
						// below block will be executed if start preceder is other than activityDefKey or start precedr list is >1 
						while((startPrecederDefKey.length() >1 && !startPrecederDefKey.equals(activityDefKey))|| startPrecedersList.size()>1 ) { 
							List<String> startPrecedersList2 = startPrecedersList;
							
							taskPlan = taskPlans.stream().filter(tasPlans -> startPrecedersList2.contains(tasPlans.getMstTaskDef().getKey())).sorted(sortTaskByActualEndDateDesc).findFirst().orElse(null);
							if(taskPlan == null) {
								logger.warn("No task_plan records found with activity_plan_id{} and taskDefKeys {}",actiVityPlan.getId(), startPrecedersList2);
								break;
							}else if(processedTasks.contains(taskPlan.getMstTaskDef().getKey())) {
								logger.warn("Same preceder is defined in loop, To avoid infinite loop breaking for task_plan id {} ,processedTasks {}",taskPlan.getId(),processedTasks);
								break;
							}else {
								processedTasks.add(taskPlan.getMstTaskDef().getKey());
							}
							
							startPrecedersList = getPrecedersList(taskPlan.getPreceders());
								startPrecederDefKey = getFirstElement(startPrecedersList);
								updateTaskCriticalPath.accept(taskPlan);
					}
					if(startPrecederDefKey.equals("")) {
						logger.warn("Preceders details not found for task_plan id: {} :",taskPlan.getId());
					}
					
				}else {
					logger.warn("No task_plan records found with activity_plan_id{} and taskDefKeys {}",actiVityPlan.getId(), endPrecedersList);					
				}
			}else {
				taskPlans.stream().findFirst().ifPresent(updateTaskCriticalPath);			
			}
					
			actiVityPlan.setCriticalPath(CRITCAL_PATH_Y);
			activityPlanRepository.save(actiVityPlan);
			logger.info("Updated activity_plan critical path. id: {} activityDefKey: {}",actiVityPlan.getId(),activityDefKey);
		}else {
			logger.info("Preceders details not found, could not update activity_plan critical path. id: {} activityDefKey: {}",actiVityPlan.getId(),activityDefKey);
		}		
	};
	
	private Comparator<? super ActivityPlan> sortActivityByActualEndDateDesc = (activityPlan1, activityPlan2)->{
		Optional<Timestamp> p1Optional = Optional.ofNullable(activityPlan1.getActualEndTime());
		Optional<Timestamp> p1Optiona2 = Optional.ofNullable(activityPlan2.getActualEndTime());

		if(p1Optional.isPresent() && p1Optiona2.isPresent()) {
			if(activityPlan1.getActualEndTime().after(activityPlan2.getActualEndTime())) {
				return -1;
			}else if(activityPlan1.getActualEndTime().before(activityPlan2.getActualEndTime())) {
				return 1;
			}else {
				return 0;
			}
		}else if(p1Optional.isPresent()) {
			return -1;
		}else if(p1Optiona2.isPresent()) {
			return 1;
		}else {
			return 0;
		}
		
	};
	
	private Consumer<? super ProcessPlan> updateProcessCriticalPath = (processPlan)->{
		Set<ActivityPlan> activityPlans = processPlan.getActivityPlans();
		List<String> endPrecedersList = getPrecedersList(processPlan.getEndPreceders());
		String processDefKey = processPlan.getMstProcessDef().getKey();
		logger.info("Updating process_plan critical path. id: {} processDefKey: {}",processPlan.getId(),processDefKey);

		if(endPrecedersList !=null) {
			if(activityPlans.size()>1) {
				ActivityPlan activityPlan = activityPlans.stream().filter(actPlan -> endPrecedersList.contains(actPlan.getMstActivityDef().getKey())).sorted(sortActivityByActualEndDateDesc).findFirst().orElse(null);
				if(activityPlan != null) {
					updateActivityCriticalPath.accept(activityPlan);
										
					List<String> startPrecedersList = getPrecedersList(activityPlan.getStartPreceders());
					String startPrecederDefKey = getFirstElement(startPrecedersList);
					List<String> processedActivities = new ArrayList<String>();
					processedActivities.add(activityPlan.getMstActivityDef().getKey());
					
					// below block will be executed if start preceder is other than processDefKey or start precedr list is >1 
					while((startPrecederDefKey.length() >1 && !startPrecederDefKey.equals(processDefKey))|| startPrecedersList.size()>1 ) {
						List<String> startPrecedersList2 = startPrecedersList;
						
						activityPlan = activityPlans.stream().filter(actPlan -> startPrecedersList2.contains(actPlan.getMstActivityDef().getKey())).sorted(sortActivityByActualEndDateDesc).findFirst().orElse(null);
						if(activityPlan == null) {
							logger.warn("No activity_plan records found with process_plan_id{} and activityDefKeys {}",processPlan.getId(), startPrecedersList2);
							break;
						}else if(processedActivities.contains(activityPlan.getMstActivityDef().getKey())) {
							logger.warn("Same preceder is defined in loop, To avoid infinite loop breaking for activity_plan id {} ,processedActivities {}",processPlan.getId(),processedActivities);
							break;
						}else {
							processedActivities.add(activityPlan.getMstActivityDef().getKey());
						}
						
						startPrecedersList = getPrecedersList(activityPlan.getStartPreceders());
						startPrecederDefKey = getFirstElement(startPrecedersList);
						updateActivityCriticalPath.accept(activityPlan);
					}
					if(startPrecederDefKey.equals("")) {
						logger.warn("Preceders details not found for activity_plan id: {} :",activityPlan.getId());
					}
					
				}else {
					logger.warn("No activity_plan records found with process_plan_id{} and activityDefKeys {}",processPlan.getId(), endPrecedersList);					
				}
			}else {
				activityPlans.stream().findFirst().ifPresent(updateActivityCriticalPath);	
				
			}
			
			processPlan.setCriticalPath(CRITCAL_PATH_Y);
			processPlanRepository.save(processPlan);
			logger.info("Updated process_plan critical path. id: {} processDefKey: {}",processPlan.getId(),processDefKey);
		}else {
			logger.info("Preceders details not found, could not update process_plan critical path. id: {} processDefKey: {}",processPlan.getId(),processDefKey);
		}
				
	};
	
	
	private Comparator<? super ProcessPlan> sortProcessByActualEndDateDesc = (procePlan1, procePlan2)->{
		Optional<Timestamp> p1Optional = Optional.ofNullable(procePlan1.getActualEndTime());
		Optional<Timestamp> p1Optiona2 = Optional.ofNullable(procePlan2.getActualEndTime());
		
		if(p1Optional.isPresent() && p1Optiona2.isPresent()) {
			if(procePlan1.getActualEndTime().after(procePlan2.getActualEndTime())) {
				return -1;
			}else if(procePlan1.getActualEndTime().before(procePlan2.getActualEndTime())) {
				return 1;
			}else {
				return 0;
			}
		}else if(p1Optional.isPresent()) {
			return -1;
		}else if(p1Optiona2.isPresent()) {
			return 1;
		}else {
			return 0;
		}
		
	};
		
	
	private Consumer<StagePlan> updateCrticalPath = (stagePlan)->
	{		
		Set<ProcessPlan> processPlans = stagePlan.getProcessPlans();
		
		List<String> endPrecedersList = getPrecedersList(stagePlan.getEndPreceders());
		String stageDefKey = stagePlan.getMstStageDef().getKey();
		logger.info("Updating stage_plan critical path. id: {} stageDefKey: {}",stagePlan.getId(),stageDefKey);
		
		if(endPrecedersList !=null) {
			if(processPlans.size()>1) {
				ProcessPlan processPlan = processPlans.stream().filter(procePlan -> endPrecedersList.contains(procePlan.getMstProcessDef().getKey())).sorted(sortProcessByActualEndDateDesc).findFirst().orElse(null);
												
				if(processPlan != null) {
					updateProcessCriticalPath.accept(processPlan);
										
					List<String> startPrecedersList = getPrecedersList(processPlan.getStartPreceders());
					String startPrecederDefKey = getFirstElement(startPrecedersList);
					List<String> processedProcesses = new ArrayList<String>();
					processedProcesses.add(processPlan.getMstProcessDef().getKey());
					
					// below block will be executed if start preceder is other than stageDefKey or start precedr list is >1 
					while((startPrecederDefKey.length() >1 && !startPrecederDefKey.equals(stageDefKey) )|| startPrecedersList.size()>1 ) { 
						List<String> startPrecedersList2 = startPrecedersList;					
						processPlan = processPlans.stream().filter(procePlan -> startPrecedersList2.contains(procePlan.getMstProcessDef().getKey())).sorted(sortProcessByActualEndDateDesc).findFirst().orElse(null);
						if(processPlan == null) {
							logger.warn("No process_plan records found with stage_plan_id {} and procssDefKeys {}",stagePlan.getId(), startPrecedersList2);
							break;
						}else if(processedProcesses.contains(processPlan.getMstProcessDef().getKey())) {
							logger.warn("Same preceder is defined in loop, To avoid infinite loop breaking for process_plan id {} ,processedProcesses {}",processPlan.getId(),processedProcesses);
							break;
						}else {
							processedProcesses.add(processPlan.getMstProcessDef().getKey());
						}
						
						startPrecedersList = getPrecedersList(processPlan.getStartPreceders());
						startPrecederDefKey = getFirstElement(startPrecedersList);
						updateProcessCriticalPath.accept(processPlan);
					}
					if(startPrecederDefKey.equals("")) {
						logger.warn("Preceders details not found for process_plan id: {} :",processPlan.getId());
					}
				}else {
					logger.warn("No process_plan records found with stage_plan_id {} and procssDefKeys {}",stagePlan.getId(), endPrecedersList);					
				}
			}else {
				processPlans.stream().findFirst().ifPresent(updateProcessCriticalPath);
			}			
		
			stagePlan.setCriticalPath(CRITCAL_PATH_Y);
			stagePlanRepository.save(stagePlan);
			logger.info("Updated stage_plan critical path. id: {} stageDefKey: {}",stagePlan.getId(),stageDefKey);
			
		}else {
			logger.info("Preceders details not found, could not update stage_plan critical path. id: {} stageDefKey: {}",stagePlan.getId(),stageDefKey);
		}
		
	};
	
	
	private List<String> getPrecedersList(String preceders) {
		List<String> precedersList = null;
		
		if(preceders!=null) {
		String[] precedersArr = preceders.split(",");		
		precedersList = Stream.of(precedersArr).
				map(preceder -> preceder.replaceAll("_start_plan|_end_plan|_start|_end|_plan$", "")).
				collect(Collectors.toList());
		}		
		return precedersList;
	}

	private String getFirstElement(List<String> preceders) {
		String firstElement = null;
		if (preceders != null && preceders.size() > 0) {
			firstElement = preceders.get(0);
		} else {
			firstElement = "";
		}

		return firstElement;

	}
	
	@Transactional
	public void processCriticalPathUpdate(BaseRequest baseRequest) {
		try {
		List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByDeliveryDate();
		if (scServiceDetails != null && !scServiceDetails.isEmpty()) {
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				computeCriticalPath(scServiceDetail.getId());
			}
		}
		}
		catch(Exception e) {
			logger.error("processCriticalPathUpdate  Exception {} ", e);
		}
	}
}
