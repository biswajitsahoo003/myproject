package com.tcl.dias.servicehandover.network.listener;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;
import com.tcl.dias.servicefulfillmentutils.service.v1.CommonFulfillmentUtils;
import com.tcl.dias.servicehandover.constants.Constants;
import com.tcl.dias.servicehandover.intl.account.beans.SetSECSProfileResponse;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;
import com.tcl.dias.servicehandover.service.InternationalBillingAccountCreationService;

/**
 * AccountCreationListener for Account Creation
 * @author yomagesh
 *
 */
@Component
public class NetworkAccountCreationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(NetworkAccountCreationListener.class);
	
	@Autowired
	BillingAccountCreationService accountCreationService;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	/**
	 * Listener for Account Creation
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.accountCreateSync.network}") })
	public String createIasAccountSync(String request) {
		LOGGER.info("inside createIasAccountSync");
		String status= "";
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String processInstanceId = req.split("#")[1];
			String serviceCode = req.split("#")[2];
			String serviceId = req.split("#")[3];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType= scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("createIasAccountSync orderId{} serviceCode{} serviceType{} serviceId{} ",orderId,serviceCode,serviceType,serviceId);
			CreateOrderResponse createOrderResponse = accountCreationService.accountCreation(orderId,processInstanceId,serviceCode,serviceType,serviceId,null);
			if(createOrderResponse!=null) {
				status = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getStatus();
				String errorMsg = createOrderResponse.getCreateOrderRequestOutput().getAcknowledge().getErrorMsg();
				LOGGER.info("network account creation Status for Sync Call from WPS : {} ", status);
				return status != null ? status.concat(errorMsg != null ? errorMsg : "") : Constants.UNSUCCESSFUL;
			}
		} catch (Exception e) {
			LOGGER.error("Error in createAccountSync", e);
		}
		LOGGER.info("Network billing account creation listener completed with status:  {} ", status);
		return status;
	}
}
