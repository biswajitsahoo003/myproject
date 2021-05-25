package com.tcl.dias.servicehandover.ipc.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.servicehandover.beans.taxcapture.SetActualTaxResponse;
import com.tcl.dias.servicehandover.ipc.service.IpcApplyTaxService;

/**
 * Apply tax listener to Apply tax for UK & Row Orders
 * 
 * @author yomagesh
 *
 */
@Component
public class IpcApplyTaxListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(IpcApplyTaxListener.class);

	@Autowired
	IpcApplyTaxService ipcApplyTaxService;

	/**
	 * Listener to ApplyTax
	 * 
	 * @param request
	 * @return
	 */

	@RabbitListener(queuesToDeclare = { @Queue("${queue.ipc.apply.tax}") })
	public String applyTax(String request) {
		LOGGER.info("inside ipcApplyTax");
		String status = "";
		try {
			String req = request;
			String orderCode = req.split("#")[0];
			String serviceCode = req.split("#")[2];
			String serviceId = req.split("#")[3];
			String userName = req.split("#")[4];
			LOGGER.info("ipcApplyTax for orderId:{} serviceCode:{} serviceId:{} userName:{}", orderCode, serviceCode, serviceId, userName);
			SetActualTaxResponse setActualTaxResponse = ipcApplyTaxService.applyTax(orderCode, serviceCode, serviceId,userName);
			if (setActualTaxResponse != null) {
				status = setActualTaxResponse.getSetActualTaxOutput().getResponseHeader().getStatus() == 0 ? "Success" : "Failure";
				return status;
			} else {
				return "Failure";
			}
		} catch (Exception e) {
			LOGGER.error("Error in ipcApplyTax", e);
		}
		LOGGER.info("ipcApplyTax completed with status:  {} ", status);
		return status;
	}
};