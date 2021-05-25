package com.tcl.dias.servicefulfillmentutils.delegates.gsc;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.constants.AttributeConstants;
import com.tcl.dias.servicefulfillmentutils.constants.GscConstants;
import com.tcl.dias.servicefulfillmentutils.constants.MasterDefConstants;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicefulfillmentutils.service.v1.WorkFlowService;

/**
 * This file contains the CheckSipConfigStatusDelegate.java class.
 * 
 *
 * @author ASyed
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component("checkSipConfigStatusDelegate")
public class CheckSipConfigStatusDelegate implements JavaDelegate {

	private static final Logger logger = LoggerFactory.getLogger(CheckSipConfigStatusDelegate.class);

	@Autowired
	WorkFlowService workFlowService;

	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;

	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;

	/**
	 * execute
	 * 
	 * @param execution
	 */
	@Override
	public void execute(DelegateExecution execution) {
		logger.info("CheckSipConfigStatusDelegate  invoked");
		workFlowService.processServiceTask(execution);
		String waitForSip = "yes";



		String parentServiceId = StringUtils.isEmpty(execution.getVariable(MasterDefConstants.PARENT_SERVICE_ID))?"0":String.valueOf(execution.getVariable(MasterDefConstants.PARENT_SERVICE_ID));
		//String parentServiceId = (String) execution.getVariable(MasterDefConstants.PARENT_SERVICE_ID);
		List<ScServiceDetail> serviceDetails = scServiceDetailRepository
				.findByProductNameAndParentId(GscConstants.PRODUCT_SIP, parentServiceId);

		if (Objects.nonNull(serviceDetails) && !serviceDetails.isEmpty()) {
			ScServiceDetail sipScServiceDetail = serviceDetails.get(0);

			Map<String, String> sipAttributes = commonFulfillmentUtils.getComponentAttributes(
					sipScServiceDetail.getId(), AttributeConstants.COMPONENT_LM, "A", Arrays.asList("routeLabel"));
			if (sipAttributes.containsKey("routeLabel")) {
				execution.setTransientVariable("waitForSip", "no");
			}
		}

		logger.info("waitForSip::{}", waitForSip);
		execution.setVariable("waitForSip", waitForSip);
		workFlowService.processServiceTaskCompletion(execution);

	}

}
