package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * 
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "service_area_matrix_IAS")
@NamedQuery(name = "ServiceAreaMatrixIAS.findAll", query = "SELECT s FROM ServiceAreaMatrixIAS s")
public class ServiceAreaMatrixIAS implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "city_dtl")
	private String cityDtl;

	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_dt")
	private Date createdDt;

	@Column(name = "is_active_ind")
	private String isActiveInd;

	@Column(name = "network_location_id")
	private String networkLocationId;

	@Column(name = "pop_address_dtl")
	private String popAddressDtl;

	@Column(name = "pop_name")
	private String popName;

	@Column(name = "pop_sts")
	private String popSts;

	@Column(name = "state_dtl")
	private String stateDtl;

	@Column(name = "tier_cd_nrb")
	private Integer tierCdNrb;

	@Column(name = "updated_by")
	private String updatedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "updated_dt")
	private Date updatedDt;

	public ServiceAreaMatrixIAS() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCityDtl() {
		return this.cityDtl;
	}

	public void setCityDtl(String cityDtl) {
		this.cityDtl = cityDtl;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDt() {
		return this.createdDt;
	}

	public void setCreatedDt(Date createdDt) {
		this.createdDt = createdDt;
	}

	public String getIsActiveInd() {
		return this.isActiveInd;
	}

	public void setIsActiveInd(String isActiveInd) {
		this.isActiveInd = isActiveInd;
	}

	public String getNetworkLocationId() {
		return this.networkLocationId;
	}

	public void setNetworkLocationId(String networkLocationId) {
		this.networkLocationId = networkLocationId;
	}

	public String getPopAddressDtl() {
		return this.popAddressDtl;
	}

	public void setPopAddressDtl(String popAddressDtl) {
		this.popAddressDtl = popAddressDtl;
	}

	public String getPopName() {
		return this.popName;
	}

	public void setPopName(String popName) {
		this.popName = popName;
	}

	public String getPopSts() {
		return this.popSts;
	}

	public void setPopSts(String popSts) {
		this.popSts = popSts;
	}

	public String getStateDtl() {
		return this.stateDtl;
	}

	public void setStateDtl(String stateDtl) {
		this.stateDtl = stateDtl;
	}

	public Integer getTierCdNrb() {
		return this.tierCdNrb;
	}

	public void setTierCdNrb(Integer tierCdNrb) {
		this.tierCdNrb = tierCdNrb;
	}

	public String getUpdatedBy() {
		return this.updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDt() {
		return this.updatedDt;
	}

	public void setUpdatedDt(Date updatedDt) {
		this.updatedDt = updatedDt;
	}

}