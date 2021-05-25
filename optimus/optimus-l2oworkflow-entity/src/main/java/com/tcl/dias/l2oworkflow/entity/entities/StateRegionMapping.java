package com.tcl.dias.l2oworkflow.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "state_region_mapping")
@NamedQuery(name = "StateRegionMapping.findAll", query = "SELECT s FROM StateRegionMapping s")
public class StateRegionMapping implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5520823793473717166L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "state")
	private String state;

	@Column(name = "region")
	private String region;
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
