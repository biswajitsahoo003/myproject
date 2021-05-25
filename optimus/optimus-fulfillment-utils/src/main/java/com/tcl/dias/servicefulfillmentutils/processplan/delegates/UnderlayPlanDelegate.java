package com.tcl.dias.servicefulfillmentutils.processplan.delegates;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.ORDER_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SITE_DETAIL_ID;

import java.sql.Timestamp;
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

import com.tcl.dias.servicefulfillment.entity.entities.MstProcessDef;
import com.tcl.dias.servicefulfillment.entity.entities.ProcessPlan;
import com.tcl.dias.servicefulfillment.entity.entities.StagePlan;
import com.tcl.dias.servicefulfillment.entity.repository.MstProcessDefRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ProcessPlanRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.StagePlanRepository;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.TaskCacheService;

/**
 * @author dimples
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component("underlayPlanDelegate")
public class UnderlayPlanDelegate implements JavaDelegate {
	
	private static final Logger logger = LoggerFactory.getLogger(UnderlayPlanDelegate.class);

	private Expression preceders;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	ProcessPlanRepository processPlanRepository;
	
	@Autowired
	MstProcessDefRepository mstProcessDefRepository;
	
	@Autowired
	StagePlanRepository stagePlanRepository;
	
	@Autowired
	TaskCacheService taskCacheService;

	@Override
	public void execute(DelegateExecution execution) {
		logger.info("UnderlayPlanDelegate invoked for{} and preceders{}", execution.getCurrentActivityId(), preceders);
		try {
			Map<String, Object> fMap = execution.getVariables();
			Integer serviceId = (Integer) fMap.get(SERVICE_ID);
			String serviceCode = (String) fMap.get(SERVICE_CODE);
			String orderCode = (String) fMap.get(ORDER_CODE);
			logger.info("Overlay Id{}", serviceId);
			Set<Timestamp> maxDates = new TreeSet<Timestamp>(Collections.reverseOrder());
			List<Map<String,Timestamp>> targetDeliveryDateMap=scServiceDetailRepository.findByIdAndTargetedDeliveryDate(serviceId);
			if(targetDeliveryDateMap!=null && !targetDeliveryDateMap.isEmpty()){
				logger.info("Underlay Target Delivery Time exists with size {}",targetDeliveryDateMap.size());
				String defKey = execution.getCurrentActivityId().replaceAll("_end_plan", "");
				defKey = defKey.replaceAll("_end", "");
				Integer siteId=fMap.containsKey(SITE_DETAIL_ID)?(Integer)fMap.get(SITE_DETAIL_ID):null;
				String precedersValues = (String) preceders.getValue(execution);
				logger.info("processPlanCompletion with preceders {} ", precedersValues);
				ProcessPlan processPlan = null;
				String siteType = StringUtils.trimToEmpty((String)fMap.get("site_type"));
				if(StringUtils.isBlank(siteType))siteType="A";
				logger.info("defKey: {} siteType: {} ", defKey,siteType);
				processPlan = processPlanRepository.findByServiceIdAndMstProcessDefKey(serviceId, defKey, siteType);
				logger.info("processPlan info: {} ", processPlan);
				if(siteId!=null) {
					processPlan=processPlanRepository.findBySiteIdAndMstProcessDefKey(siteId, defKey);
				}
				List<Timestamp> targetDeliveryTimeStamp=targetDeliveryDateMap.stream().flatMap(map->map.entrySet().stream()).map(Map.Entry::getValue).collect(Collectors.toList());
				maxDates.addAll(targetDeliveryTimeStamp);
				logger.info("Max date size {}", maxDates.size());
				Timestamp startTime = maxDates.stream().findFirst().orElse(null);
				logger.info("Underlay Id {} with max date {}", execution.getCurrentActivityId(),startTime);
				execution.setVariable(execution.getCurrentActivityId()+"_endDate", startTime);
				if (processPlan != null) {
					logger.info("Underlay Plan exists for given overlay:{}",serviceId);
					processPlan.setEstimatedEndTime(startTime);
					processPlan.setTargettedEndTime(startTime);
					processPlan.setPlannedEndTime(startTime);
					processPlanRepository.save(processPlan);
				}else{
					logger.info("Underlay Process creation for given overlay:{}",serviceId);
					MstProcessDef mstProcessDef = mstProcessDefRepository.findByKey(defKey);
					processPlan = new ProcessPlan();
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
					processPlan.setActiveVersion(1L);
					processPlan.setSiteType(siteType);
					Optional<StagePlan> stagePlan = stagePlanRepository
							.findById((Integer) fMap.get(mstProcessDef.getMstStageDef().getKey() + "PLAN_ID"));
					if (stagePlan.isPresent() && processPlan.getStagePlan() == null) {
						logger.info("Underlay Process related stage exists for given overlay:{}",serviceId);
						processPlan.setStagePlan(stagePlan.get());
						processPlanRepository.save(processPlan);
					}
				}
				
				//logger.info("fMap:{}",fMap);
			}
		} catch (Exception e) {
			e.printStackTrace();	
			throw new BpmnError(e.getMessage());
		}
	}
}
