package com.tcl.dias.customer.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "customer_attachment")
public class CustomerAttachment implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "attachment_id")
	private Integer attachmentId;
	
	@Column(name = "attachment_name")
	private String attachmentName;


	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Column(name = "status")
	private Byte status;

	public CustomerAttachment() {
		
	}


	public Integer getAttachmentId() {
		return attachmentId;
	}


	public void setAttachmentId(Integer attachmentId) {
		this.attachmentId = attachmentId;
	}


	public String getAttachmentName() {
		return attachmentName;
	}


	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public Date getCreatedTime() {
		return createdTime;
	}


	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	public Byte getStatus() {
		return status;
	}


	public void setStatus(Byte status) {
		this.status = status;
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

}
