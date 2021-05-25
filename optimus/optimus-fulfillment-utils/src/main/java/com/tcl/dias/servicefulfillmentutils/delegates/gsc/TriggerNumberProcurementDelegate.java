/**
 * 
 */
package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.ACANS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.ACDTFS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.ITFS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.LNS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.NEW_SIP_CREATION;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.PROCURE_DID_NUMBERS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.PROCURE_PROV_VAS_NUMBERS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.PROCURE_UIFN_NUMBERS;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.SIP;
import static com.tcl.dias.servicefulfillmentutils.constants.GscConstants.UIFN;
import static com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants.SERVICE_ID;

import java.util.List;
import java.util.Map;

import org.flowable.cmmn.api.delegate.DelegatePlanItemInstance;
import org.flowable.cmmn.api.delegate.PlanItemJavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlanItemRequestBean;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CmmnHelperService;
import com.tcl.dias.servicefulfillmentutils.service.v1.GscService;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author ASyed
 *
 */
@Component("triggerNumberProcurement")
public class TriggerNumberProcurementDelegate implements PlanItemJavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(TriggerNumberProcurementDelegate.class);

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

		logger.info("Triggered ################## TriggerNumberProcurementDelegate ############## ");
	
		Map<String, Object> executionVariables = planItemInstance.getVariables();
		logger.info("Inside TriggerNumberProcurementDelegate delegate, variables {}", executionVariables);
		String caseInstanceId = planItemInstance.getCaseInstanceId();
		Integer serviceId = (Integer) executionVariables.get(SERVICE_ID);
		List<ScServiceDetail> scServiceDetails = gscService.getChildServiceDetails(serviceId);
		try {
			boolean uifnServiceFound = false;
			for (ScServiceDetail scServiceDetail : scServiceDetails) {
				String newPlanItem = null;
				String planItemDefinitionId = null;
				String prdCatalogOffering = scServiceDetail.getErfPrdCatalogOfferingName();
				if (prdCatalogOffering != null && prdCatalogOffering.contains(UIFN)) {
					// for uifn service type procure uifn numbers plan item will be triggered one
					// time i.e. product level.
					if (!uifnServiceFound) {
						newPlanItem = cmmnHelperService.getEnabledPlanItem(caseInstanceId, PROCURE_UIFN_NUMBERS);
						uifnServiceFound = true;
						planItemDefinitionId = PROCURE_UIFN_NUMBERS;
					}
				}else if (notUifnVasProduct(prdCatalogOffering)) {
					// other than UIFN VAS service types procure numbers plan item triggered on country level.
					newPlanItem = cmmnHelperService.createPlanItem(caseInstanceId, PROCURE_PROV_VAS_NUMBERS, null);
					planItemDefinitionId = PROCURE_PROV_VAS_NUMBERS;
				}else if (!StringUtils.isEmpty(prdCatalogOffering) && prdCatalogOffering.contains(GscConstants.DOMESTIC_VOICE)) {
					newPlanItem = cmmnHelperService.createPlanItem(caseInstanceId, PROCURE_DID_NUMBERS, null);
					planItemDefinitionId = PROCURE_DID_NUMBERS;
				}else if (SIP.equalsIgnoreCase(prdCatalogOffering)) {
					newPlanItem = cmmnHelperService.createPlanItem(caseInstanceId, NEW_SIP_CREATION, null);
					planItemDefinitionId = NEW_SIP_CREATION;
				}
				
				if (newPlanItem != null) {
					PlanItemRequestBean planItemRequestBean = new PlanItemRequestBean();
					planItemRequestBean.setCaseInstanceId(caseInstanceId);
					planItemRequestBean.setPlanItem(newPlanItem);
					planItemRequestBean.setServiceId(scServiceDetail.getId());
					planItemRequestBean.setPlanItemDefinitionId(planItemDefinitionId);
					mqUtils.send(o2cGscFulfillmentQueue, Utils.convertObjectToJson(planItemRequestBean));
					logger.info("Trigger country/product level process - request {}", scServiceDetail.getId());
				}
			}
		planItemInstance.setVariableLocal("startProcInv", true);
		
		} catch (TclCommonException e) {
			logger.error("Exception in TriggerNumberProcurementDelegate",e);
		}
		

	}

	private boolean notUifnVasProduct(String prdCatalogOffering) {
		boolean flag = false;
		if (prdCatalogOffering != null) {
			if (prdCatalogOffering.contains(ITFS)) {
				flag = true;
			} else if (prdCatalogOffering.contains(LNS)) {
				flag = true;
			} else if (prdCatalogOffering.contains(ACANS)) {
				flag = true;
			} else if (prdCatalogOffering.contains(ACDTFS)) {
				flag = true;
			}
		}

		return flag;
	}
}
