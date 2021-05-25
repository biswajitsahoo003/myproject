package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
@Entity
@Table(name = "mf_response_detail_audit")
@NamedQuery(name = "MfResponseDetailAudit.findAll", query = "SELECT l FROM MfResponseDetailAudit l")
public class MfResponseDetailAudit implements Serializable{private static final long serialVersionUID = 1L;

@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

// bi-directional many-to-one association to mf_response_detail
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "mf_response_detail_id") 
private MfResponseDetail mfResponseDetail;


@Column(name = "mf_response_json")
private String mfResponseJson;


@Column(name = "is_updated")
private String isUpdated;


@Column(name = "created_by")
private String createdBy;

@Column(name = "created_time")
private Timestamp createdTime;
	 

public Integer getId() {
	return id;
}


public void setId(Integer id) {
	this.id = id;
}


public MfResponseDetail getMfResponseDetail() {
	return mfResponseDetail;
}


public void setMfResponseDetail(MfResponseDetail mfResponseDetail) {
	this.mfResponseDetail = mfResponseDetail;
}


public String getMfResponseJson() {
	return mfResponseJson;
}


public void setMfResponseJson(String mfResponseJson) {
	this.mfResponseJson = mfResponseJson;
}


public String getIsUpdated() {
	return isUpdated;
}


public void setIsUpdated(String isUpdated) {
	this.isUpdated = isUpdated;
}


public String getCreatedBy() {
	return createdBy;
}


public void setCreatedBy(String createdBy) {
	this.createdBy = createdBy;
}


public Timestamp getCreatedTime() {
	return createdTime;
}


public void setCreatedTime(Timestamp createdTime) {
	this.createdTime = createdTime;
}


public MfResponseDetailAudit() {
	// DO NOTHING
}



}
