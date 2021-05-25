package com.tcl.dias.servicehandover.network.listener;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentAttributesRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.service.BillingProductCreationService;

/**
 * ProductCommListener class for product commissioning
 * @author yomagesh
 *
 */
@Component
public class CpeNetworkProductCommListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CpeNetworkProductCommListener.class);
	
	@Autowired
	BillingProductCreationService billingHandoverService;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	/**
	 * Listener for Product Commissioning
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.productCommSync.network.cpe}") })
	public String cpeProdCommSync(String request) {
		LOGGER.info("inside cpeProdCommSync");
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceId = req.split("#")[3];
			Integer cpeOverlayComponentId = Integer.parseInt(req.split("#")[4]);
			String siteType=null;
			if(cpeOverlayComponentId>0 ) {
				ScComponent scComponent =scComponentRepository.findById(cpeOverlayComponentId).get();
				if(scComponent!=null) {
					siteType=scComponent.getSiteType();
				}
			}
			LOGGER.info("cpeProdCommSync orderId{} serviceCode{}",orderId,serviceCode);
			CreateOrderResponse createOrderResponse = billingHandoverService.productCreation(orderId,processInstanceId,serviceCode,NetworkConstants.CPE,serviceId,siteType);
			if(createOrderResponse!=null) {
				String status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				String errorMsg = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getErrorMsg();
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):NetworkConstants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in createAccountSync", e);
		}
		return null;
	}
}
