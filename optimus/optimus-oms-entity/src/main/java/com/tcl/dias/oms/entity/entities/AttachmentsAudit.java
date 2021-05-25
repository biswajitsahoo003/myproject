package com.tcl.dias.oms.entity.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "attachments_audit")
@NamedQuery(name = "AttachmentsAudit.findAll", query = "SELECT a FROM AttachmentsAudit a")
public class AttachmentsAudit {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="quote_code")
	private String quoteCode;
	
	@Column(name="quote_to_le_id")
	private Integer quoteToLeId;
	
	@Column(name="quote_site_id")
	private Integer quoteSiteId;
	
	@Column(name="quote_link_id")
	private Integer quoteLinkId;
	
	@Column(name="attachment_type")
	private String attachmentType;
	
	@Column(name="erf_cus_attachment_id")
	private Integer customerAttachmentId;
	
	@Column(name="version")
	private Integer version;
	
	@Column(name="reference_name")
	private String referenceName;
	
	@Column(name="reference_id")
	private String referenceId;
	
	@Column(name="filename")
	private String fileName;
	
	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getQuoteCode() {
		return quoteCode;
	}

	public void setQuoteCode(String quoteCode) {
		this.quoteCode = quoteCode;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public String getAttachmentType() {
		return attachmentType;
	}

	public void setAttachmentType(String attachmentType) {
		this.attachmentType = attachmentType;
	}

	public Integer getCustomerAttachmentId() {
		return customerAttachmentId;
	}

	public void setCustomerAttachmentId(Integer customerAttachmentId) {
		this.customerAttachmentId = customerAttachmentId;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Integer getQuoteSiteId() {
		return quoteSiteId;
	}

	public void setQuoteSiteId(Integer quoteSiteId) {
		this.quoteSiteId = quoteSiteId;
	}

	public Integer getQuoteLinkId() {
		return quoteLinkId;
	}

	public void setQuoteLinkId(Integer quoteLinkId) {
		this.quoteLinkId = quoteLinkId;
	}

	public String getReferenceName() {
		return referenceName;
	}

	public void setReferenceName(String referenceName) {
		this.referenceName = referenceName;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	} 
	
	
}
