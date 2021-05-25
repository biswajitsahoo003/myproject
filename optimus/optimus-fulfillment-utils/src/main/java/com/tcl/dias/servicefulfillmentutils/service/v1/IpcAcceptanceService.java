package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.flowable.engine.RuntimeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.utils.DateUtil;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillment.entity.repository.TaskRepository;
import com.tcl.dias.servicefulfillmentutils.constants.IPCServiceFulfillmentConstant;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the logic for the IPC Order acceptance process.
 * 
 *
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Service
public class IpcAcceptanceService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcAcceptanceService.class);

	@Value("${rabbitmq.o2c.ipc.order.stage.queue}")
	private String o2cIPCOrderStageQueue;

	@Autowired
	private MQUtils mqUtils;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	private ScServiceAttributeRepository scServiceAttributeRepository;

	@Autowired
	private TaskRepository taskRepository;

	@Autowired
	private TaskService taskService;

	@SuppressWarnings("unchecked")
	@Transactional
	public String processServiceAcceptance(Integer taskId, Boolean acceptanceFlag, Map<String, Object> processVariableMap) throws TclCommonException {
		Optional<Task> optTask = taskRepository.findById(taskId);
		if (acceptanceFlag) {
			optTask.ifPresent(task -> {
				List<ScServiceDetail> scServiceDetails = scServiceDetailRepository.findByScOrderId(task.getScOrderId());
				scServiceDetails.stream().findFirst().ifPresent(scServiceDetail -> {
					try {
						String isDeemedAcceptance=(String) processVariableMap.get("isDeemedAcceptance");
						String userName=(String) processVariableMap.get("acceptanceUserName");
						LOGGER.info("Deemed Acceptance : {}", isDeemedAcceptance);
						LOGGER.info("UserName : {}", userName);
						ScServiceAttribute scServiceAttribute = new ScServiceAttribute();
						scServiceAttribute.setAttributeAltValueLabel(isDeemedAcceptance);
						scServiceAttribute.setAttributeName("isDeemedAcceptance");
						scServiceAttribute.setAttributeValue(isDeemedAcceptance);
						scServiceAttribute.setCategory("task");
						scServiceAttribute.setCreatedBy(userName);
						scServiceAttribute.setUpdatedBy(userName);
						scServiceAttribute.setCreatedDate(new Timestamp(new Date().getTime()));
						scServiceAttribute.setIsActive("Y");
						scServiceAttribute.setUpdatedDate(new Timestamp(new Date().getTime()));
						scServiceAttribute.setScServiceDetail(scServiceDetail);
						scServiceAttributeRepository.save(scServiceAttribute);
						
						ScServiceAttribute deemedAcceptance = new ScServiceAttribute();
						deemedAcceptance.setAttributeAltValueLabel(isDeemedAcceptance);
						deemedAcceptance.setAttributeName("deemedAcceptance");
						deemedAcceptance.setAttributeValue("Customer");
						deemedAcceptance.setCategory("task");
						deemedAcceptance.setCreatedBy(userName);
						deemedAcceptance.setUpdatedBy(userName);
						deemedAcceptance.setCreatedDate(new Timestamp(new Date().getTime()));
						deemedAcceptance.setIsActive("Y");
						deemedAcceptance.setUpdatedDate(new Timestamp(new Date().getTime()));
						deemedAcceptance.setScServiceDetail(scServiceDetail);
						scServiceAttributeRepository.save(deemedAcceptance);
						
						ScServiceAttribute deemedAcceptanceDate = new ScServiceAttribute();
						deemedAcceptanceDate.setAttributeAltValueLabel(isDeemedAcceptance);
						deemedAcceptanceDate.setAttributeName("customerAcceptanceDate");
						deemedAcceptanceDate.setAttributeValue(DateUtil.convertDateToString(new Date()));
						deemedAcceptanceDate.setCategory("task");
						deemedAcceptanceDate.setCreatedBy(userName);
						deemedAcceptanceDate.setUpdatedBy(userName);
						deemedAcceptanceDate.setCreatedDate(new Timestamp(new Date().getTime()));
						deemedAcceptanceDate.setIsActive("Y");
						deemedAcceptanceDate.setUpdatedDate(new Timestamp(new Date().getTime()));
						deemedAcceptanceDate.setScServiceDetail(scServiceDetail);
						scServiceAttributeRepository.save(deemedAcceptanceDate);
						scServiceDetail.setServiceAceptanceDate(new Timestamp(new Date().getTime()));
						scServiceDetailRepository.save(scServiceDetail);
						LOGGER.info("Added task service attribute");
						updateOrderStage(scServiceDetail, IPCServiceFulfillmentConstant.ORDER_STAGE_SERVICE_ACCEPTANCE,isDeemedAcceptance,userName);
					} catch (TclCommonException e) {
						LOGGER.error("error while updating the IPC order stage: {}", e.getMessage());
					}
				});
			});
		} else {
			optTask.ifPresent(task -> {
				processVariableMap.put(IPCServiceFulfillmentConstant.SERVICE_ACCEPTED, acceptanceFlag);
				String processInstanceId = task.getWfProcessInstId();
				if (Objects.nonNull(processInstanceId)) {

					Map<String, Object> processVariables = runtimeService.getVariables(processInstanceId);

					if (processVariableMap.containsKey(IPCServiceFulfillmentConstant.SERVICE_ISSUE)) {

						Map<String, Object> serviceIssue = (Map<String, Object>) processVariableMap
								.get(IPCServiceFulfillmentConstant.SERVICE_ISSUE);
						serviceIssue.put(IPCServiceFulfillmentConstant.TIMESTAMP, System.currentTimeMillis());

						if (processVariables.containsKey(IPCServiceFulfillmentConstant.SERVICE_ISSUES_LOG)) {

							processVariableMap.put(IPCServiceFulfillmentConstant.SERVICE_ISSUE_ITERATION,
									Integer.sum((Integer) processVariables
											.get(IPCServiceFulfillmentConstant.SERVICE_ISSUE_ITERATION), 1));

							List<Map<String, Object>> issueLogs = (List<Map<String, Object>>) processVariables
									.get(IPCServiceFulfillmentConstant.SERVICE_ISSUES_LOG);
							issueLogs.add(serviceIssue);

							processVariableMap.put(IPCServiceFulfillmentConstant.SERVICE_ISSUES_LOG, issueLogs);

						} else {
							processVariableMap.put(IPCServiceFulfillmentConstant.SERVICE_ISSUE_ITERATION, 1);
							List<Map<String, Object>> issueLogs = new ArrayList<>();
							issueLogs.add(serviceIssue);
							processVariableMap.put(IPCServiceFulfillmentConstant.SERVICE_ISSUES_LOG, issueLogs);
						}

					}

				}
			});
		}
		return taskService.manuallyCompleteTask(taskId, processVariableMap);
	}

	@Transactional
	public void updateOrderStage(ScServiceDetail scServiceDetail, String stage,String isDeemedAcceptance, String userName) throws TclCommonException {
		Map<String, String> orderDetails = new HashMap<>();
		orderDetails.put("ORDER_CODE", scServiceDetail.getScOrderUuid());
		orderDetails.put("ORDER_SERVICE_ID", scServiceDetail.getUuid());
		orderDetails.put("ORDER_TYPE", scServiceDetail.getScOrder().getOrderType());
		orderDetails.put("ORDER_STAGE", stage);
		orderDetails.put("IS_DEEMED_ACCEPTANCE",isDeemedAcceptance);
		orderDetails.put("ACCEPTANCE_USER_NAME",userName);
		LOGGER.info("Order stage update status Request : {}", orderDetails);
		LOGGER.info("MDC Filter token value in before Queue call fulfillment {} :",
				MDC.get(CommonConstants.MDC_TOKEN_KEY));
		Boolean status = (Boolean) mqUtils.sendAndReceive(o2cIPCOrderStageQueue,
				Utils.convertObjectToJson(orderDetails));
		LOGGER.info("Order stage update status {} :", status);
	}

}
