package com.tcl.dias.networkaugment.entity.entities;

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
@Table(name="order_details")
@NamedQuery(name = "OrderDetails.findAll", query = "SELECT a FROM OrderDetails a")
public class OrderDetails implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "completed_time")
	private Timestamp completedTime;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@Column(name = "due_date")
	private Timestamp duedate;

	@Column(name = "updated_time")
	private Timestamp updatedTime;

	@Column(name = "wf_activity_id")
	private String wfActivityId;
	
	@Column(name = "wf_task_id")
	private String wfTaskId;
	
	@Column(name = "order_id")
	private String  orderId;
	
	@Column(name = "order_type")
	private String  orderType;
	
	@Column(name = "project_type")
	private String  projectType;
	
	@Column(name = "pm_name")
	private String  pmName;
	
	@Column(name = "pm_email")
	private String  pmEmail;
	
	@Column(name = "name")
	private String  name;
	
	@Column(name = "contact_number")
	private String  contactNumber;

//	@Column(name = "originated_date")
//	private Timestamp originatedDate;
//	
//	@Column(name = "group_id")
//	private Timestamp groupId;
//	
//	@Column(name = "device_name_known")
//	private Boolean deviceNameKnown;
//	
//	@Column(name = "hostname")
//	private String hostname;
//	
//	@Column(name = "country")
//	private String country;
//	
//	@Column(name = "state")
//	private String state;
//	
//	@Column(name = "city")
//	private String city;
//	
//	@Column(name = "other_city")
//	private String otherCity;
//	
//	@Column(name = "area")
//	private String area;
//
//	@Column(name = "other_area")
//	private String otherArea;
//
//	@Column(name = "building")
//	private String building;
//
//	@Column(name = "other_building")
//	private String otherBuilding;

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Timestamp getCompletedTime() {
		return completedTime;
	}

	public void setCompletedTime(Timestamp completedTime) {
		this.completedTime = completedTime;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public Timestamp getDuedate() {
		return duedate;
	}

	public void setDuedate(Timestamp duedate) {
		this.duedate = duedate;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getWfActivityId() {
		return wfActivityId;
	}

	public void setWfActivityId(String wfActivityId) {
		this.wfActivityId = wfActivityId;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getPmName() {
		return pmName;
	}

	public void setPmName(String pmName) {
		this.pmName = pmName;
	}

	public String getPmEmail() {
		return pmEmail;
	}

	public void setPmEmail(String pmEmail) {
		this.pmEmail = pmEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

}
