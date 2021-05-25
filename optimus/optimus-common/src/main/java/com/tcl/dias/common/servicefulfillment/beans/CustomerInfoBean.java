package com.tcl.dias.common.servicefulfillment.beans;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * 
 * This file contains the CustomerInfoBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerInfoBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3527937278540737363L;
	private String customerId;
	private String customerName;
	private Integer customerLeId;
	private String customerLeName;
	private String sfdcCuid;
	private Integer supplierId;
	private String supplierName;
	private String sfdcAccountId;
	private String cusomerContactEmail;
	private String customerContact;
	private String accountManager;
	private String accountManagerEmail;
	private String billingFrequency;
	private String billingAddress;
	private String billingMethod;
	private String paymentTerm;
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public Integer getCustomerLeId() {
		return customerLeId;
	}
	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
	}
	public String getCustomerLeName() {
		return customerLeName;
	}
	public void setCustomerLeName(String customerLeName) {
		this.customerLeName = customerLeName;
	}
	public String getSfdcCuid() {
		return sfdcCuid;
	}
	public void setSfdcCuid(String sfdcCuid) {
		this.sfdcCuid = sfdcCuid;
	}
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSfdcAccountId() {
		return sfdcAccountId;
	}
	public void setSfdcAccountId(String sfdcAccountId) {
		this.sfdcAccountId = sfdcAccountId;
	}
	public String getCusomerContactEmail() {
		return cusomerContactEmail;
	}
	public void setCusomerContactEmail(String cusomerContactEmail) {
		this.cusomerContactEmail = cusomerContactEmail;
	}
	public String getCustomerContact() {
		return customerContact;
	}
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
	public String getAccountManager() {
		return accountManager;
	}
	public void setAccountManager(String accountManager) {
		this.accountManager = accountManager;
	}
	public String getAccountManagerEmail() {
		return accountManagerEmail;
	}
	public void setAccountManagerEmail(String accountManagerEmail) {
		this.accountManagerEmail = accountManagerEmail;
	}
	public String getBillingFrequency() {
		return billingFrequency;
	}
	public void setBillingFrequency(String billingFrequency) {
		this.billingFrequency = billingFrequency;
	}
	public String getBillingAddress() {
		return billingAddress;
	}
	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}
	public String getBillingMethod() {
		return billingMethod;
	}
	public void setBillingMethod(String billingMethod) {
		this.billingMethod = billingMethod;
	}
	public String getPaymentTerm() {
		return paymentTerm;
	}
	public void setPaymentTerm(String paymentTerm) {
		this.paymentTerm = paymentTerm;
	}
	@Override
	public String toString() {
		return "CustomerInfoBean [customerId=" + customerId + ", customerName=" + customerName + ", customerLeId="
				+ customerLeId + ", customerLeName=" + customerLeName + ", sfdcCuid=" + sfdcCuid + ", supplierId="
				+ supplierId + ", supplierName=" + supplierName + ", sfdcAccountId=" + sfdcAccountId
				+ ", cusomerContactEmail=" + cusomerContactEmail + ", customerContact=" + customerContact
				+ ", accountManager=" + accountManager + ", accountManagerEmail=" + accountManagerEmail
				+ ", billingFrequency=" + billingFrequency + ", billingAddress=" + billingAddress + ", billingMethod="
				+ billingMethod + ", paymentTerm=" + paymentTerm + "]";
	}
	
	
	
	

}
