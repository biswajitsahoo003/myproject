package com.tcl.dias.serviceactivation.activation.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.serviceactivation.entity.entities.OrderDetail;
import com.tcl.dias.serviceactivation.entity.entities.ServiceDetail;
import com.tcl.dias.serviceactivation.entity.repository.ServiceDetailRepository;
import com.tcl.dias.serviceactivation.rule.engine.service.IASRuleEngineService;
import com.tcl.dias.servicefulfillmentutils.constants.TaskStatusConstants;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class TriggerServiceOrderRules {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerServiceOrderRules.class);

	@Autowired
	IASRuleEngineService iasRuleEngineService;

	@Autowired
	ServiceDetailRepository serviceDetailRepository;

	@SuppressWarnings("unchecked")
	@RabbitListener(queuesToDeclare = { @Queue("${queue.trigger_service_order_rule}") })
	@Transactional
	public void triggerServiceOrderRules(String responseBody) throws TclCommonException {
		try {
			Map<String, String> request = Utils.convertJsonToObject(responseBody,
					Map.class);
			if (null != request && null != request.get("serviceCode")) {
				ServiceDetail serviceDetails = serviceDetailRepository
						.findFirstByServiceIdAndServiceStateOrderByVersionDesc(request.get("serviceCode"),TaskStatusConstants.ISSUED);
				if (null != serviceDetails) {
					OrderDetail orderDetail = serviceDetails.getOrderDetail();
					Map<String, String> manDatoryFields = iasRuleEngineService.findMandatoryValues(orderDetail,
							serviceDetails);
					iasRuleEngineService.applyOrderRule(orderDetail, serviceDetails, manDatoryFields);
					iasRuleEngineService.applyServiceDetailsRule(serviceDetails, manDatoryFields);
				}
			}
			LOGGER.info("Request received for TriggerServiceOrderRules listener is {}", request);
		} catch (Exception e) {
			LOGGER.error("Error in triggerServiceOrderRules ", e);
		}
	}
}
