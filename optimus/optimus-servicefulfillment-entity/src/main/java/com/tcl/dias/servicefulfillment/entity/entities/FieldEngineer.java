package com.tcl.dias.servicefulfillment.entity.entities;

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

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "field_engineer")
@NamedQuery(name = "FieldEngineer.findAll", query = "SELECT f FROM FieldEngineer f")
public class FieldEngineer {
	

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	

	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "type")
	private String appointmentType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "task_id")
	private Task task;
	
	@Column(name="name")
	private String name;
	
	@Column(name="mobile")
	private String mobile;
	
	@Column(name="email")
	private String email;
	
	@Column(name="secondary_name")
	private String secondaryname;
	
	@Column(name="secondary_mobile")
	private String secondarymobile;
	
	@Column(name="secondary_email")
	private String secondaryemail;
	
	@Column(name="primary_email")
	private String primaryEmail;
	
	@Column(name="fe_type")
	private String feType;
	
	@Column(name="work_type")
	private String workType;
	
 

	

	
	
	

	/**
	 * @return the workType
	 */
	public String getWorkType() {
		return workType;
	}

	/**
	 * @param workType the workType to set
	 */
	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getSecondaryname() {
		return secondaryname;
	}

	public void setSecondaryname(String secondaryname) {
		this.secondaryname = secondaryname;
	}



	/**
	 * @return the primaryEmail
	 */
	public String getPrimaryEmail() {
		return primaryEmail;
	}

	/**
	 * @param primaryEmail the primaryEmail to set
	 */
	public void setPrimaryEmail(String primaryEmail) {
		this.primaryEmail = primaryEmail;
	}

	public String getSecondarymobile() {
		return secondarymobile;
	}

	public void setSecondarymobile(String secondarymobile) {
		this.secondarymobile = secondarymobile;
	}

	public String getSecondaryemail() {
		return secondaryemail;
	}

	public void setSecondaryemail(String secondaryemail) {
		this.secondaryemail = secondaryemail;
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

	public String getAppointmentType() {
		return appointmentType;
	}

	public void setAppointmentType(String appointmentType) {
		this.appointmentType = appointmentType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the feType
	 */
	public String getFeType() {
		return feType;
	}

	/**
	 * @param feType the feType to set
	 */
	public void setFeType(String feType) {
		this.feType = feType;
	}
	
	

}
