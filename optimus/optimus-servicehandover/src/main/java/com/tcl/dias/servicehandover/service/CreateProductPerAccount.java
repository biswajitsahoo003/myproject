package com.tcl.dias.servicehandover.service;

import java.util.concurrent.Callable;

import com.tcl.dias.servicefulfillment.entity.entities.ServicehandoverAudit;
import com.tcl.dias.servicehandover.config.SOAPConnector;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrder;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderBO;
import com.tcl.dias.servicehandover.ipc.beans.createorder.CreateOrderResponse;
import com.tcl.dias.servicehandover.util.JaxbMarshallerUtil;

public class CreateProductPerAccount implements Callable<CreateOrderResponse> {

	SOAPConnector orderSoapConnector;

	private String createOrderOperation;

	CreateOrderBO createOrderBO;

	public CreateProductPerAccount(CreateOrderBO createOrderBO,SOAPConnector orderSoapConnector,String createOrderOperation) {
		this.createOrderBO = createOrderBO;
		this.orderSoapConnector=orderSoapConnector;
		this.createOrderOperation=createOrderOperation;
	}

	@Override
	public CreateOrderResponse call() throws Exception {
		CreateOrder createOrder = new CreateOrder();;
		createOrder.setCreateOrderRequestInput(createOrderBO);
		JaxbMarshallerUtil.jaxbObjectToXML(createOrder, new ServicehandoverAudit());
		CreateOrderResponse createOrderResponse = (CreateOrderResponse) orderSoapConnector
				.callWebService(createOrderOperation, createOrder);
		return createOrderResponse;
	}

}
