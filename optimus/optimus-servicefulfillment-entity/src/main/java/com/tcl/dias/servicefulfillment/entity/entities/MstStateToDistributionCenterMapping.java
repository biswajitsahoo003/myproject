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
 * 
 * This file contains the MstStateToDistributionCenterMapping.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_state_distribution_mapping")
@NamedQuery(name = "MstStateToDistributionCenterMapping.findAll", query = "SELECT m FROM MstStateToDistributionCenterMapping m")
public class MstStateToDistributionCenterMapping {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "city")
	private String city;

	@Column(name = "state")
	private String state;

	@Column(name = "state_code")
	private String stateCode;

	@Column(name = "country")
	private String country;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mst_dist_id")
	private MasterTclDistributionCenter masterTclDistributionCenter;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public MasterTclDistributionCenter getMasterTclDistributionCenter() {
		return masterTclDistributionCenter;
	}

	public void setMasterTclDistributionCenter(MasterTclDistributionCenter masterTclDistributionCenter) {
		this.masterTclDistributionCenter = masterTclDistributionCenter;
	}

}
