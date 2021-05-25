package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This file contains the AuditTrailDetails.java class.
 * 
 *
 * @author Santosh.Tidke
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "quote_cwb_audit_trail")
public class AuditCwbTrailDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@Column(name = "quote_id")
	private Integer quoteId;

	@Column(name = "customer_id")
	private Integer customerId;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "currency")
	private String currency;

	@Column(name = "version_no")
	private Integer versionNo;

	@Column(name = "upload_url")
	private String uploadUrl;

	@Column(name = "download_url")
	private String downloadUrl;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "updated_by")
	private String updatedBy;

	@Column(name = "download_date_time")
	private Date downloadDateTime;

	@Column(name = "upload_date_time")
	private Date uploadDateTime;

	@Column(name = "contract_term")
	private String contractTerm;
	
	@Column(name = "reason_for_reupload")
	private String reasonForReupload;

	public AuditCwbTrailDetails() {
		super();
	}

	
	/**
	 * 
	 * @param id
	 * @param quoteId
	 * @param customerId
	 * @param userName
	 * @param currency
	 * @param versionNo
	 * @param uploadUrl
	 * @param downloadUrl
	 * @param createdBy
	 * @param updatedBy
	 * @param downloadDateTime
	 * @param uploadDateTime
	 * @param contractTerm
	 * @param reasonForReupload
	 */
	public AuditCwbTrailDetails(Integer id, Integer quoteId, Integer customerId, String userName, String currency,
			Integer versionNo, String uploadUrl, String downloadUrl, String createdBy, String updatedBy,
			Date downloadDateTime, Date uploadDateTime, String contractTerm, String reasonForReupload) {
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getReasonForReupload() {
		return reasonForReupload;
	}

	public void setReasonForReupload(String reasonForReupload) {
		this.reasonForReupload = reasonForReupload;
	}

	@Override
	public String toString() {
		return "AuditCwbTrailDetails [id=" + id + ", quoteId=" + quoteId + ", customerId=" + customerId + ", userName="
				+ userName + ", currency=" + currency + ", versionNo=" + versionNo + ", uploadUrl=" + uploadUrl
				+ ", downloadUrl=" + downloadUrl + ", createdBy=" + createdBy + ", updatedBy=" + updatedBy
				+ ", downloadDateTime=" + downloadDateTime + ", uploadDateTime=" + uploadDateTime + ", contractTerm="
				+ contractTerm + ", reasonForReupload=" + reasonForReupload + "]";
	}
}
