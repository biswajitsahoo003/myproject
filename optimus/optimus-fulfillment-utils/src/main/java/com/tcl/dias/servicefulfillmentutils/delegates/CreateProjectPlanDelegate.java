package com.tcl.dias.servicefulfillmentutils.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ActivityPlan;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.entities.TaskPlan;
import com.tcl.dias.servicefulfillment.entity.repository.ActivityPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskPlanRepository;
import com.tcl.dias.servicefulfillmentutils.constants.MstStatusConstant;
import com.tcl.dias.servicefulfillmentutils.factory.ProjectPlanInitiateService;
import com.tcl.dias.servicefulfillmentutils.service.v1.PriorityService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * @author samuel
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("createProjectPlanDelegate")
public class CreateProjectPlanDelegate implements JavaDelegate {
	private static final Logger logger = LoggerFactory.getLogger(CreateProjectPlanDelegate.class);

	
	@Autowired
	StagePlanRepository stagePlanRepository;	
	
	@Autowired
	ActivityPlanRepository  activityPlanRepository;	
		
	@Autowired
	ProcessPlanRepository processPlanRepository;
	
    @Autowired
    TaskCacheService taskCacheService;
	
    @Autowired
	RuntimeService runtimeService;
    
    @Autowired
	WorkFlowService workFlowService;
    
    @Autowired
    TaskPlanRepository taskPlanRepository;
    
   
    
    @Autowired
    ScServiceDetailRepository scServiceDetailRepository;
    
    
    @Autowired
    PriorityService priorityService;
    
    @Autowired
    ProjectPlanInitiateService projectPlanInitiateService;

    
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("CreateProjectPlanDelegate invoked for {} id={}", execution.getCurrentActivityId(),execution.getId());
		
		logger.info("CreateProjectPlanDelegate invoked for {} id: {} ProcessInstanceId:{}  EventName:{} ",execution.getCurrentActivityId(),
				execution.getId(),execution.getProcessInstanceId(),execution.getEventName());
		try {
			Map<String, Object> fMap = execution.getVariables();

			//workFlowService.processServiceTask(execution);
			
			Integer serviceId= (Integer) fMap.get(SERVICE_ID);
            
            Timestamp serviceDeliveryDate = priorityService.processServiceDelivery(serviceId);
            execution.setVariable("serviceDeliveryDate", serviceDeliveryDate);
            
            StagePlan orderEnrichmentStagePlan=  stagePlanRepository.findByServiceIdAndMstStageDefKey(serviceId, "order_enrichment_stage");
            if(orderEnrichmentStagePlan!=null && !orderEnrichmentStagePlan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {                    	
            	orderEnrichmentStagePlan.setActualStartTime(new Timestamp(new Date().getTime()));
            	orderEnrichmentStagePlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
            	stagePlanRepository.save(orderEnrichmentStagePlan);
            }
            
            String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
    		if(StringUtils.isBlank(siteType))siteType="A";

            
            ProcessPlan processPlan= processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, "prepare_project_plan_process", siteType);        	
			if(processPlan!=null && !processPlan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {   
				processPlan.setActualStartTime(new Timestamp(new Date().getTime()));
				processPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.INPROGRESS));
				processPlanRepository.save(processPlan);
			}
			
			ActivityPlan activityPlan=	activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, "prepare_project_plan_activity", siteType);        	
 			if(activityPlan!=null && !activityPlan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
 				activityPlan.setActualStartTime(new Timestamp(new Date().getTime()));
 				activityPlan.setActualEndTime(new Timestamp(new Date().getTime()));
 				activityPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
 				activityPlanRepository.save(activityPlan);
 			}
 			/*
 			TaskPlan taskPlan = taskPlanRepository.findByServiceIdAndMstTaskDefKey(serviceId,"basic_enrichment", siteType);
			if (taskPlan != null && !taskPlan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
				taskPlan.setActualEndTime(new Timestamp(new Date().getTime()));
				taskPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
				taskPlanRepository.save(taskPlan);
			}
           
 			ActivityPlan basicActivityPlan=	activityPlanRepository.findByServiceIdAndMstActivityDefKey(serviceId, "basic_enrichment_activity", siteType);        	
 			if(basicActivityPlan!=null && !basicActivityPlan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {
 				basicActivityPlan.setActualEndTime(new Timestamp(new Date().getTime()));
 				basicActivityPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
 				activityPlanRepository.save(basicActivityPlan);
 			}
 			
 			ProcessPlan basicProcessPlan= processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, "basic_enrichment_process", siteType);        	
 			if(basicProcessPlan!=null && !basicProcessPlan.getMstStatus().getCode().equalsIgnoreCase(MstStatusConstant.CLOSED)) {   
 				basicProcessPlan.setActualEndTime(new Timestamp(new Date().getTime()));
 				basicProcessPlan.setMstStatus(taskCacheService.getMstStatus(MstStatusConstant.CLOSED));
 				processPlanRepository.save(basicProcessPlan);
 			}*/
 			
            logger.info("CreateProjectPlanDelegate completed");
            
           // workFlowService.processServiceTaskCompletion(execution);
			
		} catch (Exception e) {
			logger.error("error processing CreateProjectPlanDelegate{}", e);
		}

	}





}
