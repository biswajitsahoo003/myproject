package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.servicefulfillmentutils.beans.webex.CugDialOutBean;
import com.tcl.dias.servicefulfillmentutils.beans.webex.MicrositeDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.webex.SkuDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.webex.VoiceServicesDetailsBean;

@JsonInclude(Include.NON_NULL)
public class WebexServiceAcceptancePdfBean {

	private static final long serialVersionUID = 1L;

	private String orderId;
	private String serviceId;
	private String orderType;
	private String orderCategory;
	private String customerContractingEntity;
	private String customerGstNumberAddress;

	private String serviceType;
	private String bridge;
	private String audioPlan;
	// private String payPerUseOrpayPerSeat;
	private String subscription;
	private String accessType;

	private String billStartDate;
	private String billFreePeriod;
	private String commissioningDate;
	private String deemedAcceptanceApplicable;

	private List<MicrositeDetailsBean> micrositeDetails;

	// private List<SkuDetailsBean> skuDetails;

	private List<VoiceServicesDetailsBean> voiceServicesDetails;

	private List<String> dailInNumbers;

	private List<CugDialOutBean> cugDialOutDetails;

	private List<String> onNetBackNumbers;

	
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

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getBridge() {
		return bridge;
	}

	public void setBridge(String bridge) {
		this.bridge = bridge;
	}

	public String getAudioPlan() {
		return audioPlan;
	}

	public void setAudioPlan(String audioPlan) {
		this.audioPlan = audioPlan;
	}

//	public String getPayPerUseOrpayPerSeat() {
//		return payPerUseOrpayPerSeat;
//	}
//
//	public void setPayPerUseOrpayPerSeat(String payPerUseOrpayPerSeat) {
//		this.payPerUseOrpayPerSeat = payPerUseOrpayPerSeat;
//	}

	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
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

	public String getDeemedAcceptanceApplicable() {
		return deemedAcceptanceApplicable;
	}

	public void setDeemedAcceptanceApplicable(String deemedAcceptanceApplicable) {
		this.deemedAcceptanceApplicable = deemedAcceptanceApplicable;
	}

	public List<MicrositeDetailsBean> getMicrositeDetails() {
		return micrositeDetails;
	}

	public void setMicrositeDetails(List<MicrositeDetailsBean> micrositeDetails) {
		this.micrositeDetails = micrositeDetails;
	}

//	public List<SkuDetailsBean> getSkuDetails() {
//		return skuDetails;
//	}
//
//	public void setSkuDetails(List<SkuDetailsBean> skuDetails) {
//		this.skuDetails = skuDetails;
//	}

	public List<String> getDailInNumbers() {
		return dailInNumbers;
	}

	public void setDailInNumbers(List<String> dailInNumbers) {
		this.dailInNumbers = dailInNumbers;
	}

	public List<CugDialOutBean> getCugDialOutDetails() {
		return cugDialOutDetails;
	}

	public void setCugDialOutDetails(List<CugDialOutBean> cugDialOutDetails) {
		this.cugDialOutDetails = cugDialOutDetails;
	}

	public List<String> getOnNetBackNumbers() {
		return onNetBackNumbers;
	}

	public void setOnNetBackNumbers(List<String> onNetBackNumbers) {
		this.onNetBackNumbers = onNetBackNumbers;
	}

	public List<VoiceServicesDetailsBean> getVoiceServicesDetails() {
		return voiceServicesDetails;
	}

	public void setVoiceServicesDetails(List<VoiceServicesDetailsBean> voiceServicesDetails) {
		this.voiceServicesDetails = voiceServicesDetails;
	}

}
