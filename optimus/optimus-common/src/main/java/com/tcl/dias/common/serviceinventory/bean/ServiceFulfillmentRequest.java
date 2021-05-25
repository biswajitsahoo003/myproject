package com.tcl.dias.common.serviceinventory.bean;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class ServiceFulfillmentRequest implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OrderInfoBean orderInfo;
	private CustomerInfoBean customerInfo;
	private ServiceInfoBean primaryServiceInfo;
	private ServiceInfoBean secondaryServiceInfo;
	private String productName;
	private String offeringName;

	public OrderInfoBean getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfoBean orderInfo) {
		this.orderInfo = orderInfo;
	}

	public CustomerInfoBean getCustomerInfo() {
		return customerInfo;
	}

	public void setCustomerInfo(CustomerInfoBean customerInfo) {
		this.customerInfo = customerInfo;
	}

	public ServiceInfoBean getPrimaryServiceInfo() {
		return primaryServiceInfo;
	}

	public void setPrimaryServiceInfo(ServiceInfoBean primaryServiceInfo) {
		this.primaryServiceInfo = primaryServiceInfo;
	}

	public ServiceInfoBean getSecondaryServiceInfo() {
		return secondaryServiceInfo;
	}

	public void setSecondaryServiceInfo(ServiceInfoBean secondaryServiceInfo) {
		this.secondaryServiceInfo = secondaryServiceInfo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	@Override
	public String toString() {
		return "ServiceFulfillmentRequest [orderInfo=" + orderInfo + ", customerInfo=" + customerInfo
				+ ", primaryServiceInfo=" + primaryServiceInfo + ", secondaryServiceInfo=" + secondaryServiceInfo + "]";
	}

}
