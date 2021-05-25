package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import java.io.Serializable;
import java.util.List;

/**
 * Bean class for SDWAN application
 * @author archchan
 *
 */
public class CiscoApplications implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer customerId;
	private List<Integer> customerLeIds;
	private Integer partnerId;
	private List<Integer> partnerLeId;
	private Integer productId;
	private List<CiscoApplicationsDetailBean> ciscoApplications;
	private String lastUpdatedDate;
	
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public List<Integer> getCustomerLeIds() {
		return customerLeIds;
	}
	public void setCustomerLeIds(List<Integer> customerLeIds) {
		this.customerLeIds = customerLeIds;
	}
	public Integer getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}
	public List<Integer> getPartnerLeId() {
		return partnerLeId;
	}
	public void setPartnerLeId(List<Integer> partnerLeId) {
		this.partnerLeId = partnerLeId;
	}
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public List<CiscoApplicationsDetailBean> getCiscoApplications() {
		return ciscoApplications;
	}
	public void setCiscoApplications(List<CiscoApplicationsDetailBean> ciscoApplications) {
		this.ciscoApplications = ciscoApplications;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	@Override
	public String toString() {
		return "CiscoApplications [customerId=" + customerId + ", customerLeIds=" + customerLeIds + ", partnerId="
				+ partnerId + ", partnerLeId=" + partnerLeId + ", productId=" + productId + ", ciscoApplications="
				+ ciscoApplications + ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}
}
