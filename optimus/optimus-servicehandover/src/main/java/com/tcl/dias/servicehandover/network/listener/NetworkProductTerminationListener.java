package com.tcl.dias.servicehandover.network.listener;

import java.util.Arrays;
import java.util.Objects;

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
import com.tcl.dias.servicefulfillmentutils.OrderCategoryMapping;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.service.BillingProductTerminationService;

/**
 * ProductCommListener class for product commissioning
 * @author yomagesh
 *
 */
@Component
public class NetworkProductTerminationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkProductTerminationListener.class);
	
	@Autowired
	BillingProductTerminationService billingProductTerminationService;
	
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
	@RabbitListener(queuesToDeclare = {@Queue("${queue.productTermSync.network}") })
	public String createNetworkProdCommSync(String request) {
		LOGGER.info("inside createNetworkProdCommSync");
		try {
			
			String req = request;
			String orderCode = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId)).get();
			ScOrder scOrder= scOrderRepository.findByOpOrderCodeAndIsActive(orderCode, "Y");
			String serviceType= scServiceDetail.getErfPrdCatalogProductName();
			LOGGER.info("createNetworkProdCommSync orderId{} serviceCode{}",orderCode,serviceCode);
			
			CreateOrderResponse createOrderResponse = null;
			if (scOrder != null && scOrder.getDemoFlag() != null && "Y".equals(scOrder.getDemoFlag())) {
				createOrderResponse = billingProductTerminationService.demoProductTermination(orderCode, processInstanceId,
						serviceCode, serviceType, serviceId);
			} else {
				createOrderResponse = billingProductTerminationService.productTermination(orderCode, processInstanceId,
						serviceCode, serviceType, serviceId);
			}
			if(createOrderResponse!=null) {
				String status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				String errorMsg = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getErrorMsg();
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):NetworkConstants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in createNetworkProdCommSync", e);
		}
		return null;
	}
}
