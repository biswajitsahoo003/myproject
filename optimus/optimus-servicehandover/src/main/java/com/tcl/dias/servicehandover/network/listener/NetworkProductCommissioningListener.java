package com.tcl.dias.servicehandover.network.listener;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;
import com.tcl.dias.servicehandover.service.BillingProductCreationService;

/**
 * ProductCommListener class for product commissioning
 * @author yomagesh
 *
 */
@Component
public class NetworkProductCommissioningListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkProductCommissioningListener.class);
	
	@Autowired
	BillingProductCreationService billingProductCreationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	/**
	 * Listener for Product Commissioning
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.productCommSync.network}") })
	public String createNetworkProdCommSync(String request) {
		LOGGER.info("inside createNetworkProdCommSync");
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("createNetworkProdCommSync orderId{} serviceCode{}",orderId,serviceCode);
			CreateOrderResponse createOrderResponse = billingProductCreationService.productCreation(orderId,processInstanceId,serviceCode,serviceType,serviceId,null);
			if(createOrderResponse!=null) {
				String status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				return status != null ? status : NetworkConstants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in createNetworkProdCommSync", e);
		}
		return null;
	}
}
