package com.tcl.dias.networkaugment.entity.entities;

import java.io.Serializable;
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

/**
 * 
 * This file contains the ServiceFulfillmentJob.java class.
 * 
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "service_fulfillment_jobs")
@NamedQuery(name = "ServiceFulfillmentJob.findAll", query = "SELECT s FROM ServiceFulfillmentJob s")
public class ServiceFulfillmentJob implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "erf_odr_service_id")
	private Integer erfOdrServiceId;
	
	@Column(name="service_code")
	private String serviceCode;

	/*@Column(name = "service_uuid")
	private String serviceUuid;*/

	public Integer getErfOdrServiceId() {
		return erfOdrServiceId;
	}

	public void setErfOdrServiceId(Integer erfOdrServiceId) {
		this.erfOdrServiceId = erfOdrServiceId;
	}

	@Column(name = "retry_count")
	private Integer retryCount;

	@Column(name = "type")
	private String type;

	private String status;

	public ServiceFulfillmentJob() {
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

	public Integer getServiceId() {
		return this.serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

/*	public String getServiceUuid() {
		return this.serviceUuid;
	}

	public void setServiceUuid(String serviceUuid) {
		this.serviceUuid = serviceUuid;
	}*/

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
}