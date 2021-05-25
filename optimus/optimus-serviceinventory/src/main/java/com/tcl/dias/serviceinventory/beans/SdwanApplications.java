package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;

/**
 * Bean class for SDWAN application
 * @author archchan
 *
 */
public class SdwanApplications implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Integer customerId;
	private List<Integer> customerLeIds;
	private Integer partnerId;
	private List<Integer> partnerLeId;
	private Integer productId;
	private List<SdwanApplicationsBean> versaApplications;
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
	public List<SdwanApplicationsBean> getVersaApplications() {
		return versaApplications;
	}
	public void setVersaApplications(List<SdwanApplicationsBean> versaApplications) {
		this.versaApplications = versaApplications;
	}
	public String getLastUpdatedDate() {
		return lastUpdatedDate;
	}
	public void setLastUpdatedDate(String lastUpdatedDate) {
		this.lastUpdatedDate = lastUpdatedDate;
	}
	
	
	
	

}
