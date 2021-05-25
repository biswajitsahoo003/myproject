
package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Preference {

	private List<String> orderType;
	private List<String> product;
	private List<String> status;
	private List<String> taskName;

	private List<String> city;
	private List<String> country;
	private List<String> state;
	private List<String> assignedPM;
	private List<String> lmProvider;
	private List<String> lmType;

	private List<String> serviceConfigurationStatus;

	private List<String> activationConfigStatus;

	private List<String> billingStatus;

	private List<String> assignedCSM;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String commissionedStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String commissionedEndDate;

	public List<String> getOrderType() {
		return orderType;
	}

	public void setOrderType(List<String> orderType) {
		this.orderType = orderType;
	}

	public List<String> getProduct() {
		return product;
	}

	public void setProduct(List<String> product) {
		this.product = product;
	}

	public List<String> getStatus() {
		return status;
	}

	public void setStatus(List<String> status) {
		this.status = status;
	}

	public List<String> getTaskName() {
		return taskName;
	}

	public void setTaskName(List<String> taskName) {
		this.taskName = taskName;
	}

	public List<String> getCity() {
		return city;
	}

	public void setCity(List<String> city) {
		this.city = city;
	}

	public List<String> getCountry() {
		return country;
	}

	public void setCountry(List<String> country) {
		this.country = country;
	}

	public List<String> getState() {
		return state;
	}

	public void setState(List<String> state) {
		this.state = state;
	}

	public List<String> getAssignedPM() {
		return assignedPM;
	}

	public void setAssignedPM(List<String> assignedPM) {
		this.assignedPM = assignedPM;
	}

	public List<String> getLmProvider() {
		return lmProvider;
	}

	public void setLmProvider(List<String> lmProvider) {
		this.lmProvider = lmProvider;
	}

	public List<String> getLmType() {
		return lmType;
	}

	public void setLmType(List<String> lmType) {
		this.lmType = lmType;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billingCompletionStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String billingCompletionEndDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String serviceConfigStartDate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String serviceConfigEndDate;

	public String getBillingCompletionStartDate() {
		return billingCompletionStartDate;
	}

	public void setBillingCompletionStartDate(String billingCompletionStartDate) {
		this.billingCompletionStartDate = billingCompletionStartDate;
	}

	public String getBillingCompletionEndDate() {
		return billingCompletionEndDate;
	}

	public void setBillingCompletionEndDate(String billingCompletionEndDate) {
		this.billingCompletionEndDate = billingCompletionEndDate;
	}

	public String getServiceConfigStartDate() {
		return serviceConfigStartDate;
	}

	public void setServiceConfigStartDate(String serviceConfigStartDate) {
		this.serviceConfigStartDate = serviceConfigStartDate;
	}

	public String getServiceConfigEndDate() {
		return serviceConfigEndDate;
	}

	public void setServiceConfigEndDate(String serviceConfigEndDate) {
		this.serviceConfigEndDate = serviceConfigEndDate;
	}

	public List<String> getServiceConfigurationStatus() {
		return serviceConfigurationStatus;
	}

	public void setServiceConfigurationStatus(List<String> serviceConfigurationStatus) {
		this.serviceConfigurationStatus = serviceConfigurationStatus;
	}

	public List<String> getActivationConfigStatus() {
		return activationConfigStatus;
	}

	public void setActivationConfigStatus(List<String> activationConfigStatus) {
		this.activationConfigStatus = activationConfigStatus;
	}

	public List<String> getBillingStatus() {
		return billingStatus;
	}

	public void setBillingStatus(List<String> billingStatus) {
		this.billingStatus = billingStatus;
	}

	public String getBillStartDate() {
		return billStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billStartDate = billStartDate;
	}

	public String getBillEndDate() {
		return billEndDate;
	}

	public void setBillEndDate(String billEndDate) {
		this.billEndDate = billEndDate;
	}

	public String getCommissionedStartDate() {
		return commissionedStartDate;
	}

	public void setCommissionedStartDate(String commissionedStartDate) {
		this.commissionedStartDate = commissionedStartDate;
	}

	public String getCommissionedEndDate() {
		return commissionedEndDate;
	}

	public void setCommissionedEndDate(String commissionedEndDate) {
		this.commissionedEndDate = commissionedEndDate;
	}
	public List<String> getAssignedCSM() {
		return assignedCSM;
	}

	public void setAssignedCSM(List<String> assignedCSM) {
		this.assignedCSM = assignedCSM;
	}
}
