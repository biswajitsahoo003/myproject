package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * This file contains the NotificationMailJob.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "notification_mail_job")
@NamedQuery(name = "NotificationMailJob.findAll", query = "SELECT n FROM NotificationMailJob n")
public class NotificationMailJob implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	@Lob
	@Column(name = "mail_request")
	private String mailRequest;

	@Column(name = "queue_name")
	private String queueName;

	@Column(name = "sc_order_id")
	private Integer scOrderId;

	private String status;
	
	@Column(name = "email")
	private String email;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "type")
	private String type;

	public NotificationMailJob() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedTime() {
		return this.createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getMailRequest() {
		return this.mailRequest;
	}

	public void setMailRequest(String mailRequest) {
		this.mailRequest = mailRequest;
	}

	public Integer getScOrderId() {
		return scOrderId;
	}

	public void setScOrderId(Integer scOrderId) {
		this.scOrderId = scOrderId;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getQueueName() {
		return queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}