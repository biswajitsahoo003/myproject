package com.tcl.dias.servicehandover.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.tcl.dias.common.utils.Utils;
import com.tcl.dias.servicehandover.service.WpsAsyncService;
import com.tcl.dias.servicehandover.wps.beans.InvoiceOperationInput;
import com.tcl.dias.servicehandover.wps.beans.OptimusAccoutInputBO;
import com.tcl.dias.servicehandover.wps.beans.OptimusProductInputBO;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

@Service
public class WpsAsyncListener {
	
	@Autowired
    WpsAsyncService wpsAsyncService;

    public static final Logger LOGGER = LoggerFactory.getLogger(WpsAsyncListener.class);

	@RabbitListener(queuesToDeclare = {@Queue("${wps.async.account.queue}")})
    public String accountResponse(Message<String> response) throws TclCommonException, InterruptedException {
        LOGGER.info(String.format("account async, Input received : %s", response.getPayload()));
        OptimusAccoutInputBO optimusAccoutInputBO = Utils.convertJsonToObject(response.getPayload(), OptimusAccoutInputBO.class);
        String accountResponse = Utils.convertObjectToJson(wpsAsyncService.accountAsync(optimusAccoutInputBO));
        return accountResponse;
        
    }

    @RabbitListener(queuesToDeclare = {@Queue("${wps.async.order.queue}")})
    public String  orderResponse(Message<String> response) throws TclCommonException, InterruptedException {
    	LOGGER.info(String.format("order async, Input received : %s", response.getPayload()));
    	OptimusProductInputBO optimusProductInputBO = Utils.convertJsonToObject(response.getPayload(), OptimusProductInputBO.class);
    	String orderResponse = Utils.convertObjectToJson(wpsAsyncService.orderAsync(optimusProductInputBO));
        return orderResponse;
    }
    
    @RabbitListener(queuesToDeclare = {@Queue("${wps.async.invoice.queue}")})
    public String invoiceResponse(Message<String> response) throws TclCommonException {
    	LOGGER.info(String.format("invoice async, Input received : %s", response.getPayload()));
    	InvoiceOperationInput invoiceOperationInput = Utils.convertJsonToObject(response.getPayload(), InvoiceOperationInput.class);
    	String invoiceResponse = Utils.convertObjectToJson(wpsAsyncService.invoiceAsync(invoiceOperationInput));
        return invoiceResponse;
    }
}
