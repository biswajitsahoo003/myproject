package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "mrn_creation")
@NamedQuery(name = "MrnCreationEntity.findAll", query = "SELECT m FROM MrnCreationEntity m")
public class MrnCreationEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8049280515642856965L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "service_code")
	private String serviceCode;

	@Column(name = "mrn_number")
	private String mrnNumber;
	
	@Column(name = "mrn_status")
	private String mrnStatus;
	
	@Column(name = "type")
	private String type;

	@Column(name = "min_status")
	private String minStatus;

	@Column(name = "remark")
	private String remark;

	@Column(name = "min_number")
	private String minNumer;

	@Column(name = "courier_name")
	private String courierName;

	@Column(name = "vehicle_docket_number")
	private String vehicleDockertNumber;

	@Column(name = "task_name")
	private String taskName;

	@Column(name = "process_instance_id")
	private String processInstanceId;
	
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getMrnNumber() {
		return mrnNumber;
	}

	public void setMrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
	}

	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMinNumer() {
		return minNumer;
	}

	public void setMinNumer(String minNumer) {
		this.minNumer = minNumer;
	}

	public String getCourierName() {
		return courierName;
	}

	public void setCourierName(String courierName) {
		this.courierName = courierName;
	}

	public String getVehicleDockertNumber() {
		return vehicleDockertNumber;
	}

	public void setVehicleDockertNumber(String vehicleDockertNumber) {
		this.vehicleDockertNumber = vehicleDockertNumber;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getMinStatus() {
		return minStatus;
	}

	public void setMinStatus(String minStatus) {
		this.minStatus = minStatus;
	}

	public String getMrnStatus() {
		return mrnStatus;
	}

	public void setMrnStatus(String mrnStatus) {
		this.mrnStatus = mrnStatus;
	}
	
	

}
