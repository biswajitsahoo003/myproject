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
 * AccountCreationListener for Intl Account Creation
 * @author yomagesh
 *
 */
@Component
public class InternationalAccountCreationListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InternationalAccountCreationListener.class);
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	@Autowired
	InternationalBillingAccountCreationService internationalBillingAccountCreationService;
	
	@Autowired
	CommonFulfillmentUtils commonFulfillmentUtils;
	
	/**
	 * Listener for Intl Account Creation
	 * @param request
	 * @return
	 */
	@RabbitListener(queuesToDeclare = {@Queue("${queue.intl.accountCreateSync.network}") })
	public String createIasAccountSync(String request) {
		LOGGER.info("inside createIasAccountSync");
		String status= "";
		try {
			String req = request;
			String orderCode = req.split("#")[0];
			String processInstanceId = req.split("#")[1];
			String serviceCode = req.split("#")[2];
			String serviceId = req.split("#")[3];
			Optional<ScServiceDetail> scServiceDetail = scServiceDetailRepository.findById(Integer.parseInt(serviceId));
			String serviceType = scServiceDetail.get().getErfPrdCatalogProductName();
			LOGGER.info("createIasAccountSync orderId{} serviceCode{} serviceType{} serviceId{} ", orderCode,serviceCode, serviceType, serviceId);
			SetSECSProfileResponse secsProfileResponse = internationalBillingAccountCreationService
					.accountCreation(orderCode, processInstanceId, serviceCode, serviceType, serviceId);
			if (secsProfileResponse != null) {
				status = "0".equals(secsProfileResponse.getSetSECSProfileOutput().getResponseHeader().getStatus().toString()) ? "Success" : "Failure";
				return status;
			}
		} catch (Exception e) {
			LOGGER.error("Error in createAccountSync", e);
		}
		LOGGER.info("Network billing account creation listener completed with status:  {} ", status);
		return status;
	}
}
