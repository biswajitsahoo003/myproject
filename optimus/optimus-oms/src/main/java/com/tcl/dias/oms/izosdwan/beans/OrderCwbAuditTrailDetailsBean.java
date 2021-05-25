package com.tcl.dias.oms.izosdwan.beans;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderCwbAuditTrailDetailsBean {
	
	private Integer id;
	private Integer quoteId;
	private Integer customerId;
	private String userName;
	private String currency;
	private Integer versionNo;
	private String uploadUrl;
	private String downloadUrl;
	private String createdBy;
	private String updatedBy;
	private Date downloadDateTime;
	private Date uploadDateTime;
	private String contractTerm;
	private String reasonForReupload;
	
	public OrderCwbAuditTrailDetailsBean() {
		super();
	}
	
	public OrderCwbAuditTrailDetailsBean(Integer id, Integer quoteId, Integer customerId, String userName,
			String currency, Integer versionNo, String uploadUrl, String downloadUrl, String createdBy,
			String updatedBy, Date downloadDateTime, Date uploadDateTime, String contractTerm,
			String reasonForReupload) {
		super();
		this.id = id;
		this.quoteId = quoteId;
		this.customerId = customerId;
		this.userName = userName;
		this.currency = currency;
		this.versionNo = versionNo;
		this.uploadUrl = uploadUrl;
		this.downloadUrl = downloadUrl;
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.downloadDateTime = downloadDateTime;
		this.uploadDateTime = uploadDateTime;
		this.contractTerm = contractTerm;
		this.reasonForReupload = reasonForReupload;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public Integer getCustomerId() {
		return customerId;
	}
	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public Integer getVersionNo() {
		return versionNo;
	}
	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}
	public String getUploadUrl() {
		return uploadUrl;
	}
	public void setUploadUrl(String uploadUrl) {
		this.uploadUrl = uploadUrl;
	}
	public String getDownloadUrl() {
		return downloadUrl;
	}
	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public Date getDownloadDateTime() {
		return downloadDateTime;
	}
	public void setDownloadDateTime(Date downloadDateTime) {
		this.downloadDateTime = downloadDateTime;
	}
	public Date getUploadDateTime() {
		return uploadDateTime;
	}
	public void setUploadDateTime(Date uploadDateTime) {
		this.uploadDateTime = uploadDateTime;
	}
	public String getContractTerm() {
		return contractTerm;
	}
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}
	public String getReasonForReupload() {
		return reasonForReupload;
	}
	public void setReasonForReupload(String reasonForReupload) {
		this.reasonForReupload = reasonForReupload;
	}
	
	@Override
	public String toString() {
		return "OrderCwbAuditTrailDetailsBean [id=" + id + ", quoteId=" + quoteId + ", customerId=" + customerId
				+ ", userName=" + userName + ", currency=" + currency + ", versionNo=" + versionNo + ", uploadUrl="
				+ uploadUrl + ", downloadUrl=" + downloadUrl + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", downloadDateTime=" + downloadDateTime + ", uploadDateTime=" + uploadDateTime + ", contractTerm="
				+ contractTerm + ", reasonForReupload=" + reasonForReupload + "]";
	}
	
	
}
