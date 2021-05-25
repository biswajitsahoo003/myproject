package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the ServiceAcceptancePdfBean class for service acceptance
 * pdf attributes
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class WelcomeLetterPdfBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String subject;
	private String fromAddress;
	private String toAddress;
	private String ccAddress;
	private String customerName;
	private String serviceId;
	private String orderType;
	private String customerContractingEntity;
	private String supplierContractingEntity;
	private String productName;
	private String customerOrderFormRef;
	
	
	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFromAddress() {
		return fromAddress;
	}

	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public String getToAddress() {
		return toAddress;
	}

	public void setToAddress(String toAddress) {
		this.toAddress = toAddress;
	}

	public String getCcAddress() {
		return ccAddress;
	}

	public void setCcAddress(String ccAddress) {
		this.ccAddress = ccAddress;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	public String getSupplierContractingEntity() {
		return supplierContractingEntity;
	}

	public void setSupplierContractingEntity(String supplierContractingEntity) {
		this.supplierContractingEntity = supplierContractingEntity;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getCustomerOrderFormRef() {
		return customerOrderFormRef;
	}

	public void setCustomerOrderFormRef(String customerOrderFormRef) {
		this.customerOrderFormRef = customerOrderFormRef;
	}

}
