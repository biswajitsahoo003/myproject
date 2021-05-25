package com.tcl.dias.servicefulfillmentutils.delegates.teamsdr;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.beans.teamsdr.TeamsDRPlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;
import com.tcl.dias.servicefulfillmentutils.service.v1.TeamsDRService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;
import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

/* This class is used to trigger other managed services*/
@Component("triggerDRConfigurationDelegate")
public class TriggerDRConfigurationDelegate implements JavaDelegate {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerDRConfigurationDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	TeamsDRService teamsDRService;

	@Autowired
	ScComponentRepository scComponentRepository;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	CmmnHelperService cmmnHelperService;

	@Autowired
	MQUtils mqUtils;

	@Value("${rabbitmq.o2c.teamsdr.managed.services}")
	String o2cTeamsDRManagedServicesQueue;

	/**
	 * Execute the delegate
	 *
	 * @param execution
	 */

	@Override
	@Transactional(isolation = Isolation.READ_UNCOMMITTED)
	public void execute(DelegateExecution execution) {
		Map<String, Object> executionVariables = execution.getVariables();
		LOGGER.info("Inside triggerDRConfigurationDelegate variables {}", executionVariables);
		String serviceCode = (String) executionVariables.get(SERVICE_CODE);
		Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
		Integer componentId = (Integer) executionVariables.get(AttributeConstants.SC_COMPONENT_ID);
		String caseInstanceId = (String) executionVariables.get(AttributeConstants.KEY_CASE_INST_ID);
		LOGGER.info("Service Id,caseInstanceId :: {},{}", serviceId, caseInstanceId);
		try {
			Optional<ScComponent> scComponent = scComponentRepository.findById(componentId);
			if (scComponent.isPresent()) {
				LOGGER.info("SC Component ID : {}, name {} ", scComponent.get().getId(),
						scComponent.get().getComponentName());
				GscFlowGroup flowGroup = teamsDRService.getLatestFlowGroup(scComponent.get());
				if (Objects.nonNull(flowGroup)) {
					LOGGER.info("FlowGroup/Batch ID : {}", flowGroup.getId());
					Integer totalBatchCount = teamsDRService.calculateTotalBatchCount(serviceId, scComponent.get());
					if (Objects.nonNull(totalBatchCount)) {
						LOGGER.info("Total batch count : {} ", totalBatchCount);
						Integer noOfUsersInSite = teamsDRService.returnTotalUsersOnCount(serviceId, scComponent.get());
						Optional<ScServiceDetail> optionalScServiceDetail = scServiceDetailRepository
								.findById(serviceId);
						if (optionalScServiceDetail.isPresent()) {
							ScServiceDetail scServiceDetail = optionalScServiceDetail.get();
							if (Objects.nonNull(noOfUsersInSite)) {
								LOGGER.info("Total no of users in site : {} ", noOfUsersInSite);
								Integer remainingUserCount = (noOfUsersInSite - totalBatchCount);
								LOGGER.info("Pending user count : {} ", remainingUserCount);
								LOGGER.info("ScComponent :: {}", scComponent.get().getId());
								String newPlanItem = null;
								String planItemDefinitionId = null;
								if (remainingUserCount > 0) {
									execution.setVariable(AttributeConstants.IS_PENDING_USER_AVAILABLE,
											CommonConstants.YES.toLowerCase());
								} else
									execution.setVariable(AttributeConstants.IS_PENDING_USER_AVAILABLE,
											CommonConstants.NO.toLowerCase());
								LOGGER.info("gscFlowGroup :: isPendingUserAvailable{} , {}", flowGroup.getId(),
										execution.getVariable(AttributeConstants.IS_PENDING_USER_AVAILABLE));
								execution.setVariable(AttributeConstants.BATCH_COUNT, totalBatchCount);
								execution
										.setVariable(AttributeConstants.REMAINING_USER_COUNT, remainingUserCount);
								newPlanItem = cmmnHelperService
										.createPlanItem(caseInstanceId, "TeamsDRManagedService", null);
								planItemDefinitionId = "TeamsDRManagedService";
								if (newPlanItem != null) {
									TeamsDRPlanItemRequestBean planItemRequestBean = teamsDRService
											.constructTeamsDRPlanItemRequestBean(caseInstanceId, scComponent.get(),
													flowGroup, scServiceDetail.getId(), newPlanItem,
													planItemDefinitionId);
									try {
										String request = Utils.convertObjectToJson(Arrays.asList(planItemRequestBean));
										LOGGER.info("Queue triggered with request :: {}", request);
										try {
											LOGGER.info(
													"Waiting for 30 seconds if PlanItem is not found in flowable DB and retry.");
											TimeUnit.SECONDS.sleep(30);
										} catch (InterruptedException iEx) {
											LOGGER.warn("InterruptedException {} : ", iEx);
										}
										mqUtils.send(o2cTeamsDRManagedServicesQueue, request);
									} catch (TclCommonException e) {
										e.printStackTrace();
									}
								}
								execution.setVariableLocal("startProcInv", true);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("No ScServiceDetail found for that Id : {} , {} ", serviceId, e.getMessage());
		}
	}
}
