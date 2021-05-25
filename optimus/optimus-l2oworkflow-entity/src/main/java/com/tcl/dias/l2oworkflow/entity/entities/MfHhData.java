package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="mf_hh_data")
@NamedQuery(name = "MfHhData.findAll", query = "SELECT m FROM MfHhData m")
public class MfHhData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="num_hh")
	private String numHh;
	
	@Column(name="hh_state")
	private String hhState;
	
	@Column(name="hh_lat")
	private String hhLatitude;
	
	@Column(name="hh_long")
	private String hhLongitude;
	
	@Column(name="created_time")
	private Date createdTime;
	
	@Column(name="updated_time")
	private Date updatedTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNumHh() {
		return numHh;
	}

	public void setNumHh(String numHh) {
		this.numHh = numHh;
	}

	public String getHhState() {
		return hhState;
	}

	public void setHhState(String hhState) {
		this.hhState = hhState;
	}

	public String getHhLatitude() {
		return hhLatitude;
	}

	public void setHhLatitude(String hhLatitude) {
		this.hhLatitude = hhLatitude;
	}

	public String getHhLongitude() {
		return hhLongitude;
	}

	public void setHhLongitude(String hhLongitude) {
		this.hhLongitude = hhLongitude;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	
	
}
