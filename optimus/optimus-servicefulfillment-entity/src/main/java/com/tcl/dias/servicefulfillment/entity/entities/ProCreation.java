package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pro_creation")
@NamedQuery(name = "ProCreation.findAll", query = "SELECT p FROM ProCreation p")
public class ProCreation implements Serializable{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="service_id")
	private Integer serviceId;
	
	@Column(name="service_code")
	private String serviceCode;
	
	@Column(name="po_number")
	private String poNumber;
	
	@Column(name="pr_number")
	private String prNumber;
	
	@Column(name="pr_status")
	private String prStatus;
	
	@Column(name="po_status")
	private String poStatus;
	
	@Column(name="type")
	private String type;
	

	@Column(name="task_name")
	private String taskName;
	
	@Column(name="po_created_date")
	private Timestamp poCreatedDate;
	
	@Column(name="po_completed_date")
	private Timestamp poCompletedDate;
	
	@Column(name="process_instance_id")
	private String processInstanceId;
	
	@Column(name="vendor_code")
	private String vendorCode;
	
	@Column(name="component_id")
	private Integer componentId;
	
	@Column(name="pr_created_date")
	private Timestamp prCreatedDate;
	
	@Column(name="vendor_name")
	private String vendorName;
	
	@Column(name="pr_created_type")
	private String prCreatedType;
	

	public String getPoStatus() {
		return poStatus;
	}

	public void setPoStatus(String poStatus) {
		this.poStatus = poStatus;
	}

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

	public String getPoNumber() {
		return poNumber;
	}

	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}

	public String getPrNumber() {
		return prNumber;
	}

	public void setPrNumber(String prNumber) {
		this.prNumber = prNumber;
	}

	public String getPrStatus() {
		return prStatus;
	}

	public void setPrStatus(String prStatus) {
		this.prStatus = prStatus;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Timestamp getPoCreatedDate() {
		return poCreatedDate;
	}

	public void setPoCreatedDate(Timestamp poCreatedDate) {
		this.poCreatedDate = poCreatedDate;
	}

	public Timestamp getPoCompletedDate() {
		return poCompletedDate;
	}

	public void setPoCompletedDate(Timestamp poCompletedDate) {
		this.poCompletedDate = poCompletedDate;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getVendorCode() {
		return vendorCode;
	}

	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public Timestamp getPrCreatedDate() {
		return prCreatedDate;
	}

	public void setPrCreatedDate(Timestamp prCreatedDate) {
		this.prCreatedDate = prCreatedDate;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getPrCreatedType() {
		return prCreatedType;
	}

	public void setPrCreatedType(String prCreatedType) {
		this.prCreatedType = prCreatedType;
	}
}
