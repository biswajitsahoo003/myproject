package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.ipcBeans.VMDetail;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpcServiceAcceptancePdfBean implements Serializable {

	private String orderId;
	private String serviceId;
	private String orderType;
	private String orderCategory;
	private String customerContractingEntity;
	private String customerGstNumberAddress;
	private String billStartDate;
	private String commissioningDate;
	private String deemedAcceptanceApplicable;
	private Set<SolutionPdfBean> solutions = new LinkedHashSet<>();
	private Set<DeletedLineItemDetailsBean> deletedVms = new LinkedHashSet<>();
	private List<VMDetail> vmDetails;
	// private AccessDetail accessDetail;
	// private AddonDetail addonDetail;
	private Boolean isVMsManaged = false;
	private String hostingLocation;
	private Boolean isInternational = false;

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

	public String getDeemedAcceptanceApplicable() {
		return deemedAcceptanceApplicable;
	}

	public void setDeemedAcceptanceApplicable(String deemedAcceptanceApplicable) {
		this.deemedAcceptanceApplicable = deemedAcceptanceApplicable;
	}

	public List<VMDetail> getVmDetails() {
		return vmDetails;
	}

	public void setVmDetails(List<VMDetail> vmDetails) {
		this.vmDetails = vmDetails;
	}

	public Set<SolutionPdfBean> getSolutions() {
		return solutions;
	}

	public void setSolutions(Set<SolutionPdfBean> solutions) {
		this.solutions = solutions;
	}

	public Boolean getIsVMsManaged() {
		return isVMsManaged;
	}

	public void setIsVMsManaged(Boolean isVMsManaged) {
		this.isVMsManaged = isVMsManaged;
	}

	public String getHostingLocation() {
		return hostingLocation;
	}

	public void setHostingLocation(String hostingLocation) {
		this.hostingLocation = hostingLocation;
	}

	public Set<DeletedLineItemDetailsBean> getDeletedVms() {
		return deletedVms;
	}

	public void setDeletedVms(Set<DeletedLineItemDetailsBean> deletedVms) {
		this.deletedVms = deletedVms;
	}
	
	public Boolean getIsInternational() {
		return isInternational;
	}

	public void setIsInternational(Boolean isInternational) {
		this.isInternational = isInternational;
	}

	@Override
	public String toString() {
		return "IpcServiceAcceptancePdfBean [orderId=" + orderId + ", serviceId=" + serviceId + ", orderType="
				+ orderType + ", orderCategory=" + orderCategory + ", customerContractingEntity="
				+ customerContractingEntity + ", customerGstNumberAddress=" + customerGstNumberAddress
				+ ", billStartDate=" + billStartDate + ", commissioningDate=" + commissioningDate
				+ ", deemedAcceptanceApplicable=" + deemedAcceptanceApplicable + ", solutions=" + solutions
				+ ", vmDetails=" + vmDetails + ", isVMsManaged=" + isVMsManaged + ", hostingLocation=" + hostingLocation
				+ ", isInternational=" + isInternational
				+ "]";
	}

	/*
	 * public AccessDetail getAccessDetail() { return accessDetail; }
	 * 
	 * public void setAccessDetail(AccessDetail accessDetail) { this.accessDetail =
	 * accessDetail; }
	 * 
	 * public AddonDetail getAddonDetail() { return addonDetail; }
	 * 
	 * public void setAddonDetail(AddonDetail addonDetail) { this.addonDetail =
	 * addonDetail; }
	 */

}
