package com.tcl.dias.servicehandover.network.listener;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScComponent;
import com.tcl.dias.servicefulfillment.entity.repository.ScComponentRepository;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;

/**
 * AccountCreationListener for Account Creation
 * @author yomagesh
 *
 */
@Component
public class CpeNetworkAccountCreationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAccountCreationListener.class);
	
	@Autowired
	BillingAccountCreationService accountCreationService;
	
	@Autowired
	ScComponentRepository scComponentRepository;
	
	/**
	 * Listener for Account Creation
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.accountCreateSync.network.cpe}") })
	public String createCpeAccountSync(String request) {
		LOGGER.info("inside createCpeAccountSync");
		String status="";
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId= req.split("#")[1];
			String serviceCode= req.split("#")[2];
			String serviceType= NetworkConstants.CPE;
			String serviceId= req.split("#")[3];
			Integer cpeOverlayComponentId = Integer.parseInt(req.split("#")[4]);
			String siteType=null;
			if(cpeOverlayComponentId >0 ) {
				ScComponent scComponent =scComponentRepository.findById(cpeOverlayComponentId).get();
				if(scComponent!=null) {
					siteType=scComponent.getSiteType();
				}
			}
			LOGGER.info("createCpeAccountSync orderId{} serviceCode{} serviceType{}",orderId,serviceCode,serviceType);
			CreateOrderResponse createOrderResponse = accountCreationService.accountCreation(orderId,processInstanceId,serviceCode,serviceType,serviceId,siteType);
			if(createOrderResponse!=null) {
			   status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				String errorMsg = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getErrorMsg();
				LOGGER.info("cpe account creation Status for Sync Call from WPS : {} ",status);
				return status!=null?status.concat(errorMsg!=null?errorMsg:""):Constants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in createAccountSync", e);
		}
		LOGGER.info("CPE billing account creation delegate completed with status:  {} ",status);
		return status;
	}
}
;