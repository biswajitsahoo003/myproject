package com.tcl.dias.servicehandover.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.tcl.dias.sap.service.SapService;
import com.tcl.servicehandover.bean.InvoiceOperationInput;
import com.tcl.servicehandover.bean.OptimusAccoutInputBO;
import com.tcl.servicehandover.bean.OptimusProductInputBO;
import com.tcl.servicehandover.bean.UpdateAccountSyncStatusResponse;
import com.tcl.servicehandover.bean.UpdateInvoiceStatusResponse;
import com.tcl.servicehandover.bean.UpdateOrderSyncStatusResponse;

@Service
public class WpsAsyncService {

	private static final Logger LOGGER = LoggerFactory.getLogger(SapService.class);

	@Value("${wps.async.account.queue}")
	String accountQueue;

	@Value("${wps.async.order.queue}")
	String orderQueue;

	@Value("${wps.async.invoice.queue}")
	String invoiceQueue;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	public UpdateAccountSyncStatusResponse accountAsync(OptimusAccoutInputBO accountInfo) {
		LOGGER.info(String.format("account async received : %s", accountInfo.toString()));
		UpdateAccountSyncStatusResponse updateAccountSyncStatusResponse = new UpdateAccountSyncStatusResponse();
		try {
			Gson gson = new Gson();
			String response = gson.toJson(accountInfo);
			rabbitTemplate.convertAndSend(accountQueue, response);
			updateAccountSyncStatusResponse.setResult("Data Received");
		} catch (AmqpException e) {
			LOGGER.error("Exception occurred in account async: {}", e);
		}
		return updateAccountSyncStatusResponse;

	}

	public UpdateOrderSyncStatusResponse orderAsync(OptimusProductInputBO orderInfo) {
		LOGGER.info(String.format("order async received : %s", orderInfo.toString()));
		UpdateOrderSyncStatusResponse updateOrderSyncStatusResponse = new UpdateOrderSyncStatusResponse();
		try {
			Gson gson = new Gson();
			String response = gson.toJson(orderInfo);
			rabbitTemplate.convertAndSend(orderQueue, response);
			updateOrderSyncStatusResponse.setResult("Data Received");
		} catch (AmqpException e) {
			LOGGER.error("Exception occurred in order async : {}", e);
		}
		return updateOrderSyncStatusResponse;
	}

	public UpdateInvoiceStatusResponse invoiceAsync(InvoiceOperationInput invoiceInfo) {
		LOGGER.info(String.format("invoice async received : %s", invoiceInfo.toString()));
		UpdateInvoiceStatusResponse updateInvoiceStatusResponse = new UpdateInvoiceStatusResponse();
		try {
			Gson gson = new Gson();
			String response = gson.toJson(invoiceInfo);
			rabbitTemplate.convertAndSend(invoiceQueue, response);
			updateInvoiceStatusResponse.setResult("Data Received");
		} catch (AmqpException e) {
			LOGGER.error("Exception occurred in invoice async : {}", e);
		}
		return updateInvoiceStatusResponse;
	}
}
