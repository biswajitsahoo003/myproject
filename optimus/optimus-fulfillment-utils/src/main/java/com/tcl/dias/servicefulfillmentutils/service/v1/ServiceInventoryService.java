package com.tcl.dias.servicefulfillmentutils.service.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.common.constants.CommonConstants;
import com.tcl.dias.common.servicefulfillment.beans.ScOrderBean;
import com.tcl.dias.common.utils.MQUtils;
import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillmentutils.mapper.ServiceInventoryMapper;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * @author Mohamed Danish A
 * @link http://www.tatacommunications.com/
 * @copyright 2019 TATA Communications Limited
 */
@Service
public class ServiceInventoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceInventoryService.class);

	@Value("${rabbitmq.ipc.si.details.queue}")
	private String siDetailQueue;

	@Autowired
	private MQUtils mqUtils;

	@Autowired
	private ScOrderRepository scOrderRepository;
	
	public void processOrderInventoryRequest(String orderCode) {
		try {
			LOGGER.info("processOrderInventoryRequest invoked for the order: {}", orderCode);
			LOGGER.info("MDC Filter token value in before Queue call fulfillment {} :",	MDC.get(CommonConstants.MDC_TOKEN_KEY));
			ScOrderBean scOrderBean = processOrder(orderCode);
			mqUtils.send(siDetailQueue, Utils.convertObjectToJson(scOrderBean));
			LOGGER.info("processOrderInventoryRequest completed.");
		} catch (TclCommonException e) {
			LOGGER.error("Exception in processOrderInventoryRequest {}", e);
		}
	}

	@Transactional
	public ScOrderBean processOrder(String orderCode) {
		ScOrder scOrder = scOrderRepository.findDistinctByOpOrderCode(orderCode);
		return ServiceInventoryMapper.mapIPCEntityToOrderBean(scOrder);
	}
	
}
