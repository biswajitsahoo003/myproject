/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.entity.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author vivek
 *
 */

@Entity
@Table(name = "project_plan_audit_track")
@NamedQuery(name = "ProjectPlanAuditTrack.findAll", query = "SELECT p FROM ProjectPlanAuditTrack p")
public class ProjectPlanAuditTrack {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer Id;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "work_flow_name")
	private String workFlowName;

	@Column(name = "status")
	private String status;

	@Column(name = "service_state")
	private String serviceState;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Column(name = "message")
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getServiceState() {
		return serviceState;
	}

	public void setServiceState(String serviceState) {
		this.serviceState = serviceState;
	}

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getWorkFlowName() {
		return workFlowName;
	}

	public void setWorkFlowName(String workFlowName) {
		this.workFlowName = workFlowName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

}
