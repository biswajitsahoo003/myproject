package com.tcl.dias.location.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * Entity Class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "mst_state")
@NamedQuery(name = "MstState.findAll", query = "SELECT m FROM MstState m")
public class MstState implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "is_union")
	private Byte isUnion;

	private String name;

	@Column(name = "region_name")
	private String regionName;

	@Column(name = "state_code")
	private String stateCode;

	private Integer timezone;

	private String source;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	// bi-directional many-to-one association to MstCity
	@OneToMany(mappedBy = "mstState")
	private Set<MstCity> mstCities;

	// bi-directional many-to-one association to MstCountry
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private MstCountry mstCountry;

	public MstState() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Byte getIsUnion() {
		return this.isUnion;
	}

	public void setIsUnion(Byte isUnion) {
		this.isUnion = isUnion;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRegionName() {
		return this.regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getStateCode() {
		return this.stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getTimezone() {
		return this.timezone;
	}

	public void setTimezone(Integer timezone) {
		this.timezone = timezone;
	}

	public Set<MstCity> getMstCities() {
		return this.mstCities;
	}

	public void setMstCities(Set<MstCity> mstCities) {
		this.mstCities = mstCities;
	}

	public MstCity addMstCity(MstCity mstCity) {
		getMstCities().add(mstCity);
		mstCity.setMstState(this);

		return mstCity;
	}

	public MstCity removeMstCity(MstCity mstCity) {
		getMstCities().remove(mstCity);
		mstCity.setMstState(null);

		return mstCity;
	}

	public MstCountry getMstCountry() {
		return this.mstCountry;
	}

	public void setMstCountry(MstCountry mstCountry) {
		this.mstCountry = mstCountry;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

}