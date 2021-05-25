package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponentAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.TeamsDRPlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.TeamsDRFulfillmentConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TeamsDRService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

/**
 * @author Syed Ali.
 * @createdAt 19/02/2021, Friday, 19:38
 */
@Component("triggerManagedService")
public class TriggerManagedServiceDelegate implements PlanItemJavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerManagedServiceDelegate.class);

	@Autowired
	CmmnHelperService cmmnHelperService;

	@Autowired
	MQUtils mqUtils;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	TeamsDRService teamsDRService;

	@Value("${rabbitmq.o2c.teamsdr.managed.services}")
	String o2cTeamsDRManagedServicesQueue;

	@Override
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void execute(DelegatePlanItemInstance planItemInstance) {
		LOGGER.info("Triggered ################## TriggerManagedServiceDelegate ############## ");

		Map<String, Object> executionVariables = planItemInstance.getVariables();
		LOGGER.info("Inside TriggerManagedServiceDelegate delegate, variables {}", executionVariables);
		String caseInstanceId = planItemInstance.getCaseInstanceId();
		Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
		LOGGER.info("Service Id,caseInstanceId :: {},{}",serviceId,caseInstanceId);
		Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository.findById(serviceId);
		if (optionalScServiceDetail.isPresent()) {
			ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
			List<ScComponent> scComponents = scComponentRepository
					.findByScServiceDetailIdAndSiteType(scServiceDetail.getId(), AttributeConstants.DR_SITE);
			LOGGER.info("Sccomponents size :: {}",scComponents.size());
			List<TeamsDRPlanItemRequestBean> teamsDRPlanItemRequests = new ArrayList<>();
			scComponents.forEach(scComponent -> {
				LOGGER.info("ScComponent :: {}",scComponent.getId());
				String newPlanItem = null;
				String planItemDefinitionId = null;
				newPlanItem = cmmnHelperService.createPlanItem(caseInstanceId, "TeamsDRUserMapping", null);
				planItemDefinitionId = "TeamsDRUserMapping";
				if (newPlanItem != null) {
					TeamsDRPlanItemRequestBean planItemRequestBean = teamsDRService
							.constructTeamsDRPlanItemRequestBean(caseInstanceId, scComponent, null, serviceId,
									newPlanItem, planItemDefinitionId);
					teamsDRPlanItemRequests.add(planItemRequestBean);
				}
			});

			try {
				String request = Utils.convertObjectToJson(teamsDRPlanItemRequests);
				LOGGER.info("Queue triggered with request :: {}", request);
				try {
					LOGGER.info("Waiting for 30 seconds if PlanItem is not found in flowable DB and retry.");
					TimeUnit.SECONDS.sleep(30);
				} catch (InterruptedException iEx) {
					LOGGER.warn("InterruptedException {} : ", iEx);
				}
				mqUtils.send(o2cTeamsDRManagedServicesQueue, request);
			} catch (TclCommonException e) {
				e.printStackTrace();
			}
			String isTrainingPresent = (String) executionVariables.get(AttributeConstants.IS_TRAINING_PRESENT);
			if (CommonConstants.YES.equalsIgnoreCase(isTrainingPresent)) {
				String newPlanItem = cmmnHelperService.createPlanItem(caseInstanceId, "TeamsDRTraining", null);
				String planItemDefinitionId = "TeamsDRTraining";
				if (newPlanItem != null) {
					TeamsDRPlanItemRequestBean planItemRequestBean = teamsDRService
							.constructTeamsDRPlanItemRequestBean(caseInstanceId, null, null, serviceId, newPlanItem,
									planItemDefinitionId);
					try {
						String request = Utils.convertObjectToJson(Arrays.asList(planItemRequestBean));
						LOGGER.info("Queue triggered with request :: {}", request);
						try {
							LOGGER.info("Waiting for 30 seconds if PlanItem is not found in flowable DB and retry.");
							TimeUnit.SECONDS.sleep(30);
						} catch (InterruptedException iEx) {
							LOGGER.warn("InterruptedException {} : ", iEx);
						}
						mqUtils.send(o2cTeamsDRManagedServicesQueue, request);
					} catch (TclCommonException e) {
						LOGGER.info("Exception in TriggerManagedServiceDelegate {}", e.getMessage());
					}
				}
			}
			planItemInstance.setVariableLocal("startProcInv", true);
		} else {
			LOGGER.info("No ScServiceDetail found for that Id : {}", serviceId);
		}
	}
}
