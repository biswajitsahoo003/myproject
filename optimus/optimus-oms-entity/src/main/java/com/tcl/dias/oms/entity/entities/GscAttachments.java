package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Entity Class
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "gsc_attachments")
@NamedQuery(name = "GscAttachments.findAll", query = "SELECT g FROM GscAttachments g")
public class GscAttachments implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oms_attachment_id")
	private OmsAttachment omsAttachment;

	@Column(name = "document_name")
	private String documentName;

	@Column(name = "document_uid")
	private String documentUId;

	@Column(name = "document_category")
	private String documentCategory;

	@Column(name = "document_type")
	private String documentType;

	@Column(name = "template_name")
	private String templateName;

	private String status;

	@Column(name = "uploaded_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date uploadedTime;

	@Column(name = "uploaded_by")
	private String uploadedBy;

	public GscAttachments() {
		// DO NOTHING
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public OmsAttachment getOmsAttachment() {
		return omsAttachment;
	}

	public void setOmsAttachment(OmsAttachment omsAttachment) {
		this.omsAttachment = omsAttachment;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentUId() {
		return documentUId;
	}

	public void setDocumentUId(String documentUId) {
		this.documentUId = documentUId;
	}

	public String getDocumentCategory() {
		return documentCategory;
	}

	public void setDocumentCategory(String documentCategory) {
		this.documentCategory = documentCategory;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getUploadedTime() {
		return uploadedTime;
	}

	public void setUploadedTime(Date uploadedTime) {
		this.uploadedTime = uploadedTime;
	}

	public String getUploadedBy() {
		return uploadedBy;
	}

	public void setUploadedBy(String uploadedBy) {
		this.uploadedBy = uploadedBy;
	}

	@Override
	public String toString() {
		return "GscAttachments [id=" + id + ", omsAttachment=" + omsAttachment + ", documentName=" + documentName
				+ ", documentUId=" + documentUId + ", documentCategory=" + documentCategory + ", documentType="
				+ documentType + ", templateName=" + templateName + ", status=" + status + ", uploadedTime="
				+ uploadedTime + ", uploadedBy=" + uploadedBy + "]";
	}
	
	

}
