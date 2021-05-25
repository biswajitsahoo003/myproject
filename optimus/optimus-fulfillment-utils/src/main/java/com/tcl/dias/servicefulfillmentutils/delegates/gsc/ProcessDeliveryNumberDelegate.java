package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetWithStatus;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;

/**
 * This file contains the ProcessDeliveryNumberDelegate.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("ProcessDeliveryNumberDelegate")
public class ProcessDeliveryNumberDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(ProcessDeliveryNumberDelegate.class);
	
	@Autowired
    GscService gscService;
	
	@Override
	public void execute(DelegateExecution execution) {
		Map<String, Object> executionVariables = execution.getVariables();
		logger.info("Inside process delivery number delegate variables {}", executionVariables);
		Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
		String serviceCode = (String) executionVariables.get(SERVICE_CODE);
		if(Objects.nonNull(executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID))) {
			Integer gscFlowGroupId = (Integer) executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
			List<GscScAssetWithStatus> routingNumbers = gscService.getTollFreeAndRoutingFromOutpuseByFlowGrpIDWithStatus(serviceId, gscFlowGroupId);
			boolean isTestNumbersAvailable = false;
			if(routingNumbers != null && !routingNumbers.isEmpty()) {
				List<Integer> scAssets = new ArrayList<Integer>();
				for(GscScAssetWithStatus routingNumber : routingNumbers) {
					if(routingNumber.getStatus().equalsIgnoreCase(GscNumberStatus.IN_REPC_ORDER_CREATION) || routingNumber.getStatus().equalsIgnoreCase(GscNumberStatus.IN_REPC_ORDER_UPDATE)) {
						gscService.updatedScAssetStatus(routingNumber.getRoutingId(), GscNumberStatus.IN_TEST_NUMBER, "system");
						gscService.updatedScAssetStatus(routingNumber.getTollfreeId(), GscNumberStatus.IN_TEST_NUMBER, "system");
						scAssets.add(routingNumber.getTollfreeId());
						isTestNumbersAvailable = true;
					}
				}
				GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("ProcessDeliveryNumberDelegate", serviceCode, "serviceCode", "system", scAssets);
				execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_ID_TEST_HANDOVER, createGscFlowGroup.getId());
			}
			if(isTestNumbersAvailable) {
				execution.setVariable("triggerTestAndHandover", "yes");
			} else {
				execution.setVariable("triggerTestAndHandover", "no");
			}
		}
	}
}
