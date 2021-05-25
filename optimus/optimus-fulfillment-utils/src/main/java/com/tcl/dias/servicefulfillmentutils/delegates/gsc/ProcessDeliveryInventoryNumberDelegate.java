package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetReserved;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;

/**
 * This file contains the ProcessDeliveryInventoryNumberDelegate.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("ProcessDeliveryInventoryNumberDelegate")
public class ProcessDeliveryInventoryNumberDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(ProcessDeliveryInventoryNumberDelegate.class);
	
	@Autowired
    GscService gscService;
	
	@Override
	public void execute(DelegateExecution execution) {
		Map<String, Object> executionVariables = execution.getVariables();
		logger.info("Inside process delivery inventory number delegate variables {}", executionVariables);
		Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
		List<GscScAssetReserved> repcReservedTollNumbers = gscService.getRepcReservedTollNumbers(serviceId);
		boolean isTestNumbersAvailable = false;
		if(repcReservedTollNumbers != null && !repcReservedTollNumbers.isEmpty()) {
			List<Integer> scAssets = new ArrayList<Integer>();
			for(GscScAssetReserved tollFreeNumber : repcReservedTollNumbers) {
					//gscService.updatedScAssetStatus(routingNumber.getRoutingId(), GscNumberStatus.IN_TEST_NUMBER, "system");
					gscService.updatedScAssetStatus(tollFreeNumber.getTollfreeId(), GscNumberStatus.IN_TEST_NUMBER, "system");
					scAssets.add(tollFreeNumber.getTollfreeId());
					isTestNumbersAvailable = true;
			}
			
			GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("ProcessDeliveryInventoryNumber", execution.getProcessInstanceId(), "ProcessInstanceId", "system", scAssets);
			execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_ID_TEST_HANDOVER, createGscFlowGroup.getId());
		}
		if(isTestNumbersAvailable) {
			execution.setVariable("triggerTestAndHandover", "yes");
		} else {
			execution.setVariable("triggerTestAndHandover", "no");
		}
	}
}
