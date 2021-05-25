package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.PROCURE_PROV_VAS_NUMBERS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.UIFN;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_FLOW_BYPASS_INVENTORY_PROCUREMENT;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.KEY_IS_SUPPLIER_FLOW_REQ;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.YES;

import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.PARENT_SERVICE_ID;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the TriggerUifnNumberProcurementDelegate.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("triggerUifnNumberProcurement")
public class TriggerUifnNumberProcurementDelegate implements PlanItemJavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(TriggerUifnNumberProcurementDelegate.class);
	
	@Autowired
	CmmnHelperService cmmnHelperService;
	
	@Autowired
	MQUtils mqUtils;
	
	@Value("${rabbitmq.gsc.fulfillment}")
	String o2cGscFulfillmentQueue;
	
	@Autowired
	GscService gscService;
	

	@Override
	public void execute(DelegatePlanItemInstance planItemInstance) {
		logger.info("Inside TriggerUifnNumberProcurementDelegate ");
		
		String caseInstanceId = planItemInstance.getCaseInstanceId();
		Integer parentServiceId = (Integer)planItemInstance.getVariable(PARENT_SERVICE_ID);
		List<ScServiceDetail> scServiceDetails = gscService.getChildServiceDetails(parentServiceId);
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(KEY_FLOW_BYPASS_INVENTORY_PROCUREMENT, YES);
			params.put(KEY_IS_SUPPLIER_FLOW_REQ, YES);
			
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				String planItem = null;
				String prdCatalogOffering = scServiceDetail.getErfPrdCatalogOfferingName();
				if (prdCatalogOffering != null && prdCatalogOffering.contains(UIFN)) {

					planItem = cmmnHelperService.createPlanItem(caseInstanceId, PROCURE_PROV_VAS_NUMBERS, params);
					PlanItemRequestBean planItemRequestBean = new PlanItemRequestBean();
					planItemRequestBean.setCaseInstanceId(caseInstanceId);
					planItemRequestBean.setPlanItem(planItem);
					planItemRequestBean.setServiceId(scServiceDetail.getId());
					mqUtils.send(o2cGscFulfillmentQueue, Utils.convertObjectToJson(planItemRequestBean));
					logger.info("Trigger country/product level process - request {}", scServiceDetail.getId());
				}
				
			}
		}  catch (TclCommonException e) {
			logger.error("Exception in TriggerNumberProcurementDelegate",e);
		}

	}

}
