package com.tcl.dias.servicefulfillment.entity.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * This file contains the MasterTclDistributionCenter.java class.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "mst_tcl_distribution_center")
@NamedQuery(name = "MasterTclDistributionCenter.findAll", query = "SELECT m FROM MasterTclDistributionCenter m")
public class MasterTclDistributionCenter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "distribution_center_name")
	private String distributionCenterName;

	@Column(name = "sap_storage_location")
	private Integer sapStorageLocation;

	@Column(name = "plant")
	private String plant;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "masterTclDistributionCenter")
	private List<MstStateToDistributionCenterMapping> mstStateToDistributionCenterMappings;
	
	@Column(name = "distribution_center_state")
	private String distributionCenterState;

	@Column(name = "distribution_center_address")
	private String distributionCenterAddress;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDistributionCenterName() {
		return distributionCenterName;
	}

	public void setDistributionCenterName(String distributionCenterName) {
		this.distributionCenterName = distributionCenterName;
	}

	public Integer getSapStorageLocation() {
		return sapStorageLocation;
	}

	public void setSapStorageLocation(Integer sapStorageLocation) {
		this.sapStorageLocation = sapStorageLocation;
	}

	public String getPlant() {
		return plant;
	}

	public void setPlant(String plant) {
		this.plant = plant;
	}

	public List<MstStateToDistributionCenterMapping> getMstStateToDistributionCenterMappings() {
		return mstStateToDistributionCenterMappings;
	}

	public void setMstStateToDistributionCenterMappings(
			List<MstStateToDistributionCenterMapping> mstStateToDistributionCenterMappings) {
		this.mstStateToDistributionCenterMappings = mstStateToDistributionCenterMappings;
	}

	public String getDistributionCenterState() {
		return distributionCenterState;
	}

	public void setDistributionCenterState(String distributionCenterState) {
		this.distributionCenterState = distributionCenterState;
	}
	public String getDistributionCenterAddress() {
		return distributionCenterAddress;
	}

	public void setDistributionCenterAddress(String distributionCenterAddress) {
		this.distributionCenterAddress = distributionCenterAddress;
	}

	
}
