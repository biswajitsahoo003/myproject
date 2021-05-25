package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class GscServiceAcceptancePdfBean {
	
	private static final long serialVersionUID = 1L;
	
	private String orderId;
	private String serviceId;
	private String orderType;
	private String orderCategory;
	private String customerContractingEntity;
	private String customerGstNumberAddress;
	private String gstnVatNo;
	
	private String productName;
	private String productType;
	
	private String deemedAcceptanceApplicable;
	
	private String customerAddress;
	private String billStartDate;
	private String commissioningDate;
	
	private List<NumberCountryDetailsBean> numberCountryDetails;
	
	private List<SipTrunkDetails> sipTrunkDetails;
	
	private Boolean isSipTrunkAvailable;
	public List<SipTrunkDetails> getSipTrunkDetails() {
		return sipTrunkDetails;
	}

	public void setSipTrunkDetails(List<SipTrunkDetails> sipTrunkDetails) {
		this.sipTrunkDetails = sipTrunkDetails;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	public String getOrderCategory() {
		return orderCategory;
	}

	public void setOrderCategory(String orderCategory) {
		this.orderCategory = orderCategory;
	}

	public String getCustomerContractingEntity() {
		return customerContractingEntity;
	}

	public void setCustomerContractingEntity(String customerContractingEntity) {
		this.customerContractingEntity = customerContractingEntity;
	}

	public String getCustomerGstNumberAddress() {
		return customerGstNumberAddress;
	}

	public void setCustomerGstNumberAddress(String customerGstNumberAddress) {
		this.customerGstNumberAddress = customerGstNumberAddress;
	}

	public String getGstnVatNo() {
		return gstnVatNo;
	}

	public void setGstnVatNo(String gstnVatNo) {
		this.gstnVatNo = gstnVatNo;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getDeemedAcceptanceApplicable() {
		return deemedAcceptanceApplicable;
	}

	public void setDeemedAcceptanceApplicable(String deemedAcceptanceApplicable) {
		this.deemedAcceptanceApplicable = deemedAcceptanceApplicable;
	}

	public List<NumberCountryDetailsBean> getNumberCountryDetails() {
		return numberCountryDetails;
	}

	public void setNumberCountryDetails(List<NumberCountryDetailsBean> numberCountryDetails) {
		this.numberCountryDetails = numberCountryDetails;
	}
	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getCommissioningDate() {
		return commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}
	public Boolean getIsSipTrunkAvailable() {
		return isSipTrunkAvailable;
	}

	public void setIsSipTrunkAvailable(Boolean isSipTrunkAvailable) {
		this.isSipTrunkAvailable = isSipTrunkAvailable;
	}

}
