package com.tcl.dias.servicehandover.network.listener;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScOrder;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceAttribute;
import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScOrderRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceAttributeRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.service.BillingProductTerminationService;
import com.tcl.dias.servicehandover.service.BillingServiceTerminationService;

/**
 * ProductCommListener class for product commissioning
 * @author yomagesh
 *
 */
@Component
public class NetworkServiceTerminationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkServiceTerminationListener.class);
	
	@Autowired
	BillingServiceTerminationService billingServiceTerminationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	@Autowired
	ScOrderRepository scOrderRepository;
	
	@Autowired
	ScServiceAttributeRepository scServiceAttributeRepository;
	/**
	 * Listener for Product Commissioning
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.serviceTermSync.network}") })
	public String serviceTerminationSync(String request) {
		LOGGER.info("inside createNetworkProdCommSync");
		try {
			String req = request;
			String orderCode = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("serviceTerminationSync orderId{} serviceCode{}",orderCode,serviceCode);
			CreateOrderResponse createOrderResponse = billingServiceTerminationService.serviceTerminationOptimus(orderCode,processInstanceId,serviceCode,serviceType,serviceId);
			if(createOrderResponse!=null) {
				String status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				return status != null ? status : NetworkConstants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in serviceTerminationSync", e);
		}
		return null;
	}
}
