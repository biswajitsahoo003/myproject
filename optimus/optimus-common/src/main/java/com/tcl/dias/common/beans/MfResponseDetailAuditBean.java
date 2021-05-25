package com.tcl.dias.common.beans;

import java.sql.Timestamp;

/**
 * Bean for mapping MFResponseDetailAudit Entity
 *
 * @author krutsrin
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MfResponseDetailAuditBean {

    private Integer id;
    private Integer mfResponseDetailId;
    private Integer mfResponseJson; ;
    private String isUpdated;
    private String createdBy;
    private Timestamp createdTime;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getMfResponseDetailId() {
		return mfResponseDetailId;
	}
	public void setMfResponseDetailId(Integer mfResponseDetailId) {
		this.mfResponseDetailId = mfResponseDetailId;
	}
	public Integer getMfResponseJson() {
		return mfResponseJson;
	}
	public void setMfResponseJson(Integer mfResponseJson) {
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

}
