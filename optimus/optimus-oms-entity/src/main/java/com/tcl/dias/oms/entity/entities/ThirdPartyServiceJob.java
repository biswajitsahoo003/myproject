package com.tcl.dias.oms.entity.entities;

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
 * This file contains the SfdcServiceJob.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "thirdparty_service_jobs")
@NamedQuery(name = "ThirdPartyServiceJob.findAll", query = "SELECT s FROM ThirdPartyServiceJob s")
public class ThirdPartyServiceJob implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_time")
	private Date createdTime;

	@Column(name = "queue_name")
	private String queueName;

	@Column(name = "ref_id")
	private String refId;

	@Column(name = "tps_id")
	private String tpsId;

	@Lob
	@Column(name = "request_payload")
	private String requestPayload;

	@Lob
	@Column(name = "response_payload")
	private String responsePayload;

	@Column(name = "retry_count")
	private Integer retryCount;

	@Column(name = "seq_num")
	private Integer seqNum;

	@Column(name = "service_status")
	private String serviceStatus;

	@Column(name = "third_party_source")
	private String thirdPartySource;

	@Column(name = "service_type")
	private String serviceType;

	@Column(name = "updated_by")
	private String updatedBy;
	
	@Column(name = "service_ref_id")
	private String serviceRefId;
	
	@Column(name = "is_dropped")
	private Byte isDropped;

	@Column(name = "is_complete")
	private Byte isComplete;

	@Column(name = "is_active")
	private Byte isActive=1;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_time")
	private Date updatedTime;

	public ThirdPartyServiceJob() {
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

	public String getQueueName() {
		return this.queueName;
	}

	public void setQueueName(String queueName) {
		this.queueName = queueName;
	}

	public String getRefId() {
		return this.refId;
	}

	public void setRefId(String refId) {
		this.refId = refId;
	}

	public String getRequestPayload() {
		return this.requestPayload;
	}

	public void setRequestPayload(String requestPayload) {
		this.requestPayload = requestPayload;
	}

	public Integer getRetryCount() {
		return this.retryCount;
	}

	public void setRetryCount(Integer retryCount) {
		this.retryCount = retryCount;
	}

	public Integer getSeqNum() {
		return this.seqNum;
	}

	public void setSeqNum(Integer seqNum) {
		this.seqNum = seqNum;
	}

	public String getServiceStatus() {
		return this.serviceStatus;
	}

	public void setServiceStatus(String serviceStatus) {
		this.serviceStatus = serviceStatus;
	}

	public String getServiceType() {
		return this.serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedTime() {
		return this.updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public Byte getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Byte isComplete) {
		this.isComplete = isComplete;
	}

	public String getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(String responsePayload) {
		this.responsePayload = responsePayload;
	}

	public String getThirdPartySource() {
		return thirdPartySource;
	}

	public void setThirdPartySource(String thirdPartySource) {
		this.thirdPartySource = thirdPartySource;
	}

	public String getTpsId() {
		return tpsId;
	}

	public void setTpsId(String tpsId) {
		this.tpsId = tpsId;
	}

	public Byte getIsActive() {
		return isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public String getServiceRefId() {
		return serviceRefId;
	}

	public void setServiceRefId(String serviceRefId) {
		this.serviceRefId = serviceRefId;
	}

	public Byte getIsDropped() {
		return isDropped;
	}

	public void setIsDropped(Byte isDropped) {
		this.isDropped = isDropped;
	}
	
	

}