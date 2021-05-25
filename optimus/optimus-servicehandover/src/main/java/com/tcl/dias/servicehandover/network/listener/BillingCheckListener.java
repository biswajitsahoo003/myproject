package com.tcl.dias.servicehandover.network.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicefulfillment.entity.entities.ScServiceDetail;
import com.tcl.dias.servicefulfillment.entity.repository.ScChargeLineitemRepository;
import com.tcl.dias.servicefulfillment.entity.repository.ScServiceDetailRepository;

/**
 * BillingCheckListener for Billing is Required or not ?
 * @author yomagesh
 *
 */
@Component
public class BillingCheckListener {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BillingCheckListener.class);
	
	@Autowired
	ScChargeLineitemRepository scChargeLineitemRepository;
	
	@Autowired
	ScServiceDetailRepository scServiceDetailRepository;
	
	/**
	 * Listener for Billing Check
	 * @param request
	 * @return
	 */

	@RabbitListener(queuesToDeclare = {@Queue("${queue.teamsdr.billing.check}") })
	public String billingCheck(String request) {
		LOGGER.info("inside billingCheck");
		String status="Not Completed";
		try {
			String req = request;
			String orderId = req.split("#")[0];
			String serviceCode = req.split("#")[1];
			String serviceId = req.split("#")[2];
			LOGGER.info("billingCheck orderId{} serviceCode{} serviceType{}", orderId, serviceCode);
			ScServiceDetail scServiceDetail = scServiceDetailRepository.findById(Integer.valueOf(serviceId)).get();
			if (scServiceDetail != null) {
				String count = scChargeLineitemRepository.findByServiceIdAndCommissioningFlag(serviceId,scServiceDetail.getErfPrdCatalogProductName());
				if (Integer.valueOf(count) > 0) {
					status= "Completed";
				}
			}
		} catch (Exception e) {
			LOGGER.error("Error in billing Check", e);
		}
		LOGGER.info("billingCheck completed with status:  {} ", status);
		return status;
	}
}
;