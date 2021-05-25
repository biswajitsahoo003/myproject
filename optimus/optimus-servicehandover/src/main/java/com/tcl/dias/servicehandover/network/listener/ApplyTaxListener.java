package com.tcl.dias.servicehandover.network.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTaxResponse;
import com.tcl.dias.servicehandover.constants.NetworkConstants;
import com.tcl.dias.servicehandover.service.ApplyTaxService;
import com.tcl.dias.servicehandover.service.BillingAccountCreationService;

/**
 * Apply tax listener to Apply tax for UK & Row Orders
 * 
 * @author yomagesh
 *
 */
@Component
public class ApplyTaxListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(ApplyTaxListener.class);

	@Autowired
	ApplyTaxService applyTaxService;

	/**
	 * Listener to ApplyTax
	 * 
	 * @param request
	 * @return
	 */

	@RabbitListener(queuesToDeclare = { @Queue("${queue.apply.tax}") })
	public String applyTax(String request) {
		LOGGER.info("inside applyTax");
		String status = "";
		try {
			String req = request;
			String orderCode = req.split("#")[0];
			String serviceCode = req.split("#")[2];
			String serviceId = req.split("#")[3];
			String userName = req.split("#")[4];
			LOGGER.info("applyTax for orderId{} serviceCode{} serviceType{}", orderCode, serviceCode);
			SetActualTaxResponse setActualTaxResponse = applyTaxService.applyTax(orderCode, serviceCode, serviceId,userName);
			if (setActualTaxResponse != null) {
				status = setActualTaxResponse.getSetActualTaxOutput().getResponseHeader().getStatus() > 0 ? "Success" : "Failure";
				return status;
			} else {
				return "Failure";
			}
		} catch (Exception e) {
			LOGGER.error("Error in applyTax", e);
		}
		LOGGER.info("applyTax completed with status:  {} ", status);
		return status;
	}
};