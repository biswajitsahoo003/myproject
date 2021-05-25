/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.entity.entities;

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

/**
 * @author vivek
 *
 */
@Entity
@Table(name = "service_status_details")
@NamedQuery(name = "ServiceStatusDetails.findAll", query = "SELECT s FROM ServiceStatusDetails s")
public class ServiceStatusDetails implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5701485624653759204L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "user_name")
	private String userName;

	@Column(name = "start_time")
	private Timestamp startTime;

	@Column(name = "end_time")
	private Timestamp endTime;

	@Column(name = "created_time")
	private Timestamp createdTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "service_id", referencedColumnName = "id")
	private ScServiceDetail scServiceDetail;

	@Column(name = "status")
	private String status;

	@Column(name = "updated_time")
	private Timestamp updateTime;
	
	@Column(name = "service_change_category")
	private String serviceChangeCategory;
	
	

	/**
	 * @return the serviceChangeCategory
	 */
	public String getServiceChangeCategory() {
		return serviceChangeCategory;
	}

	/**
	 * @param serviceChangeCategory the serviceChangeCategory to set
	 */
	public void setServiceChangeCategory(String serviceChangeCategory) {
		this.serviceChangeCategory = serviceChangeCategory;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public ScServiceDetail getScServiceDetail() {
		return scServiceDetail;
	}

	public void setScServiceDetail(ScServiceDetail scServiceDetail) {
		this.scServiceDetail = scServiceDetail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
	
	

}
