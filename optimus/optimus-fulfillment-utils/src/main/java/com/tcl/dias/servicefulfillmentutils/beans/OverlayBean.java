package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;
import java.util.List;

public class OverlayBean {

	public Integer serviceId;

	public String serviceCode;

	public String orderCode;

	public Integer orderId;

	public String orderType;

	public String orderCategory;

	public String orderSubCategory;

	public String productName;

	public String offeringName;

	public List<UnderlayBean> underlayBeanList;

	public int underlaySize;
	
	private String customerContractingEntity;
	
	private String customerGstNumberAddress;
	
	private String siteAddress;
	
	private String associatedServiceIds;
	private String billStartDate;
	private String deemedAcceptanceApplicable;
	private String billFreePeriod;
	private String commissioningDate;
	private String cpeManagement;
	private String cpeSerialNumbers;
	

	private Timestamp rrfsDate;

	private String priority;

	private Timestamp commitedDeliveryDate;
	
	public String getSiteAddress() {
		return siteAddress;
	}

	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	public Timestamp getCommitedDeliveryDate() {
		return commitedDeliveryDate;
	}

	public void setCommitedDeliveryDate(Timestamp commitedDeliveryDate) {
		this.commitedDeliveryDate = commitedDeliveryDate;
	}

	public Timestamp getRrfsDate() {
		return rrfsDate;
	}

	public void setRrfsDate(Timestamp rrfsDate) {
		this.rrfsDate = rrfsDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
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

	public String getOrderSubCategory() {
		return orderSubCategory;
	}

	public void setOrderSubCategory(String orderSubCategory) {
		this.orderSubCategory = orderSubCategory;
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

	public List<UnderlayBean> getUnderlayBeanList() {
		return underlayBeanList;
	}

	public void setUnderlayBeanList(List<UnderlayBean> underlayBeanList) {
		this.underlayBeanList = underlayBeanList;
	}

	public int getUnderlaySize() {
		return underlaySize;
	}

	public void setUnderlaySize(int underlaySize) {
		this.underlaySize = underlaySize;
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

	public String getAssociatedServiceIds() {
		return associatedServiceIds;
	}

	public void setAssociatedServiceIds(String associatedServiceIds) {
		this.associatedServiceIds = associatedServiceIds;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getDeemedAcceptanceApplicable() {
		return deemedAcceptanceApplicable;
	}

	public void setDeemedAcceptanceApplicable(String deemedAcceptanceApplicable) {
		this.deemedAcceptanceApplicable = deemedAcceptanceApplicable;
	}

	public String getBillFreePeriod() {
		return billFreePeriod;
	}

	public void setBillFreePeriod(String billFreePeriod) {
		this.billFreePeriod = billFreePeriod;
	}

	public String getCommissioningDate() {
		return commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	public String getCpeManagement() {
		return cpeManagement;
	}

	public void setCpeManagement(String cpeManagement) {
		this.cpeManagement = cpeManagement;
	}

	public String getCpeSerialNumbers() {
		return cpeSerialNumbers;
	}

	public void setCpeSerialNumbers(String cpeSerialNumbers) {
		this.cpeSerialNumbers = cpeSerialNumbers;
	}
	
	
}
