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

import com.fasterxml.jackson.core.type.TypeReference;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAsset;
import com.tcl.dias.servicefulfillment.entity.custom.GscScAssetReserved;
import com.tcl.dias.servicefulfillment.entity.entities.GscFlowGroup;
import com.tcl.dias.servicefulfillment.entity.entities.ScAssetAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.Task;
import com.tcl.dias.servicefulfillment.entity.repository.ScAssetAttributeRepository;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlaceOrderSupplierBean;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscNumberStatus;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * This file contains the ProcessSupplierOrderDelegate.java class.
 * 
 *
 * @author Selvakumar Palaniandy
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Component("ProcessSupplierOrderDelegate")
public class ProcessSupplierOrderDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(ProcessSupplierOrderDelegate.class);
	
	@Autowired
    GscService gscService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScAssetAttributeRepository scAssetAttributeRepository;
	
	@Autowired
	WorkFlowService workFlowService;
	
	@Override
	public void execute(DelegateExecution execution) {
		try {
			Map<String, Object> executionVariables = execution.getVariables();
			logger.info("Inside process supplier order delegate variables {}", executionVariables);
			Task task = workFlowService.processServiceTask(execution);
			Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
			String serviceCode = (String) executionVariables.get(SERVICE_CODE);
			List<String> attributes = new ArrayList<String>();
			attributes.add("placeOrderRes");
			attributes.add("supplierOrderFlow");
			Map<String, String> scComponentAttributes = commonFulfillmentUtils.getComponentAttributes(serviceId, AttributeConstants.COMPONENT_GSCLICENSE, "A", attributes);
			String supplierOrderFlow = scComponentAttributes.getOrDefault("supplierOrderFlow", null);
			if(Objects.nonNull(supplierOrderFlow)) {
				if (supplierOrderFlow.equalsIgnoreCase("Parllel")) {
					if(Objects.isNull(executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_COUNT))) {
						Integer gscFlowGroupId = (Integer) executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
						List<Integer> gscFlowGroupToAsset = gscService.getGscFlowGroupToAsset(gscFlowGroupId);
						List<GscScAssetReserved> routingNumbers = gscService.getAllRoutingNumbers(serviceId);
						if(routingNumbers != null && !routingNumbers.isEmpty()) {
							List<Integer> scAssets = new ArrayList<Integer>();
							for(GscScAssetReserved routingNumber: routingNumbers) {
								if(gscFlowGroupToAsset.contains(routingNumber.getOutpulseId())) {
									gscService.updatedScAssetStatus(routingNumber.getRoutingId(), GscNumberStatus.IN_SUPPLIER_RESPONSE, "system");
									scAssets.add(routingNumber.getRoutingId());
								}
							}
							GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("ProcessSupplierOrder-"+supplierOrderFlow, serviceCode, "serviceCode", "system", scAssets);
							execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
							execution.setVariable(GscConstants.KEY_SEND_PRI_NOTIFICATION_TO_SUPPLIER, GscConstants.YES);
							execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_COUNT, 1);
						}
					} else {
						/*Integer gscFlowGroupId = (Integer) execution.getVariable(GscConstants.KEY_GSC_SUPPLIER_RES_FLOW_GROUP_ID);
						List<Integer> gscFlowGroupToAsset = gscService.getGscFlowGroupToAsset(gscFlowGroupId);
						List<GscScAssetReserved> routingNumbers = gscService.getAllRoutingNumbers(serviceId, gscFlowGroupToAsset);*/
						Integer gscFlowGroupId = (Integer) executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_ID);
						List<GscScAsset> routingNumbers = gscService.getTollFreeAndRoutingFromOutpuseByFlowGrpIDAndStatus(serviceId, gscFlowGroupId, GscNumberStatus.IN_PROCESS_SUPPLIER);
						processFlagForRepcOrderCreateAndUpdate(routingNumbers, execution);
						List<GscScAsset> inSupplierRes = gscService.getTollFreeAndRoutingFromOutpuseByFlowGrpIDAndStatus(serviceId, gscFlowGroupId, GscNumberStatus.IN_SUPPLIER_RESPONSE);
						if(inSupplierRes != null && !inSupplierRes.isEmpty()) {
							execution.setVariable("isAnyNumSuplierResPending", "yes");
						} else {
							execution.setVariable("isAnyNumSuplierResPending", "no");
						}
					}
				} else if (supplierOrderFlow.equalsIgnoreCase("Sequence")) {
					if(Objects.isNull(executionVariables.get(GscConstants.KEY_GSC_FLOW_GROUP_COUNT))) {
						List<PlaceOrderSupplierBean> placeOrderSuppliersDetails = getPlaceOrderSuppliersDetails(scComponentAttributes, task.getMstTaskDef().getKey());
						List<Integer> scAssets = new ArrayList<Integer>();
						scAssets.addAll(getPrimarySupplierRoutingNumbers(placeOrderSuppliersDetails, serviceId));
						if(!scAssets.isEmpty() ) {
							GscFlowGroup createGscFlowGroup = gscService.createGscFlowGroup("ProcessSupplierOrder-"+supplierOrderFlow, execution.getProcessInstanceId(), "ProcessInstanceId", "system", scAssets);
							execution.setVariable(GscConstants.KEY_GSC_FLOW_GROUP_ID, createGscFlowGroup.getId());
							execution.setVariable(GscConstants.KEY_SEND_PRI_NOTIFICATION_TO_SUPPLIER, GscConstants.YES);
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("Exception in process supplier order {}", e);
		}
		workFlowService.processServiceTaskCompletion(execution);
	}
	
	private List<PlaceOrderSupplierBean> getPlaceOrderSuppliersDetails(Map<String, String> scComponentAttributes, String taskDefKey) {
		List<PlaceOrderSupplierBean> suppliers = new ArrayList<PlaceOrderSupplierBean>();
		String suppliersSelected;
		if(taskDefKey.contains("port")) {
			suppliersSelected = scComponentAttributes.getOrDefault("placeOrderRes-Port", null);
		} else {
			suppliersSelected = scComponentAttributes.getOrDefault("placeOrderRes", null);
		}
		if(suppliersSelected != null) {
			suppliers = Utils.fromJson(suppliersSelected, new TypeReference<List<PlaceOrderSupplierBean>>() {});
		}
		return suppliers;
	}
	
	private List<Integer> getPrimarySupplierRoutingNumbers(List<PlaceOrderSupplierBean> placeOrderSuppliersDetails, Integer serviceId) {
		String supplierId = null;
		for(PlaceOrderSupplierBean supplier : placeOrderSuppliersDetails) {
			if(supplier.getIsPrimary()) {
				supplierId = supplier.getSupplierId()+"";
				break;
			}
		}
		List<Integer> scAssets = new ArrayList<Integer>();
		if(Objects.nonNull(supplierId)) {
			List<GscScAssetReserved> routingNumbers = gscService.getAllRoutingNumbers(serviceId, supplierId);
			if(routingNumbers != null && !routingNumbers.isEmpty()) {
				for(GscScAssetReserved routingNumber: routingNumbers) {
					gscService.updatedScAssetStatus(routingNumber.getRoutingId(), GscNumberStatus.IN_SUPPLIER_RESPONSE, "system");
					scAssets.add(routingNumber.getRoutingId());
				}
			}
		}
		return scAssets;
	}
	
	private void processFlagForRepcOrderCreateAndUpdate(List<GscScAsset> routingNumbers, DelegateExecution execution) {
		boolean isRepcOrderCreationRequired = false;
		boolean isRepcOrderUpdateRequired = false;
		for(GscScAsset routingNumber : routingNumbers) {
			ScAssetAttribute scAssetAttributes = scAssetAttributeRepository
					.findByScAsset_IdAndAttributeName(routingNumber.getRoutingId(), "vasNumberId");
			if(scAssetAttributes == null) {
				isRepcOrderCreationRequired = true;
				gscService.updatedScAssetStatus(routingNumber.getRoutingId(), GscNumberStatus.IN_REPC_ORDER_CREATION, "system");
			} else {
				isRepcOrderUpdateRequired = true;
				gscService.updatedScAssetStatus(routingNumber.getRoutingId(), GscNumberStatus.IN_REPC_ORDER_UPDATE, "system");
			}
		}
		if(isRepcOrderCreationRequired) {
			execution.setVariable(GscConstants.KEY_FLOW_BYPASSREPCORDERCREATION, GscConstants.NO);
		} else {
			execution.setVariable(GscConstants.KEY_FLOW_BYPASSREPCORDERCREATION, GscConstants.YES);
		}
		if(isRepcOrderUpdateRequired) {
			execution.setVariable(GscConstants.KEY_FLOW_UPDATEREPCREQUIRED, GscConstants.YES);
		} else {
			execution.setVariable(GscConstants.KEY_FLOW_UPDATEREPCREQUIRED, GscConstants.NO);
		}
	}
}
