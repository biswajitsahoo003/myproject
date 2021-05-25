package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_CODE;
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

import com.tcl.dias.servicefulfillment.entity.custom.IGscScAsset;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;

/**
 * This file contains the ProcessPortAndNewNumberDelegate.java class.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("ProcessPortAndNewNumberDelegate")
public class ProcessPortAndNewNumberDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(ProcessPortAndNewNumberDelegate.class);
	
	@Autowired
    GscService gscService;
	
	@Override
	public void execute(DelegateExecution execution) {
		Map<String, Object> executionVariables = execution.getVariables();
		logger.info("Inside process port and new number delegate variables {}", executionVariables);
		Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
		String serviceCode = (String) executionVariables.get(SERVICE_CODE);
		
		List<IGscScAsset> unAssociatedOutpulses = gscService.getUnAssociatedOutpulse(serviceId);
		if(unAssociatedOutpulses != null && !unAssociatedOutpulses.isEmpty()) {
			List<Integer> scAssets = new ArrayList<Integer>();
			for(IGscScAsset unAssociatedOutpulse : unAssociatedOutpulses) {
				scAssets.add(unAssociatedOutpulse.getAssetId());
			}
			GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("ProcessSupplierOrder-NewNumber", serviceCode, "serviceCode", "system", scAssets);
			execution.setVariable(GscConstants.KEY_GSC_NEW_NUMBER_FLOW_GROUP_ID, createGscFlowGroup.getId());
			execution.setVariable(GscConstants.NEW_NUM_SUPPLIER_FLOW_REQUIRED, GscConstants.YES);
		}
		
		List<IGscScAsset> portingNumbers = gscService.getPortingNumbers(serviceId);
		if(portingNumbers != null && !portingNumbers.isEmpty()) {
			List<Integer> scAssets = new ArrayList<Integer>();
			for(IGscScAsset portingNumber : portingNumbers) {
				scAssets.add(portingNumber.getAssetId());
			}
			GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("ProcessSupplierOrder-PortNumber", serviceCode, "serviceCode", "system", scAssets);
			execution.setVariable(GscConstants.KEY_GSC_PORT_NUMBER_FLOW_GROUP_ID, createGscFlowGroup.getId());
			execution.setVariable(GscConstants.PORT_NUM_SUPPLIER_FLOW_REQUIRED, GscConstants.YES);
		}
	}
}
