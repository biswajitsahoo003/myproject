package com.tcl.dias.location.entity.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "mst_country")
@NamedQuery(name = "MstCountry.findAll", query = "SELECT m FROM MstCountry m")
public class MstCountry implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	private String name;

	private Byte status;

	private String source;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	// bi-directional many-to-one association to MstState
	@OneToMany(mappedBy = "mstCountry")
	private Set<MstState> mstStates;
	
	// bi-directional many-to-one association to Customer
	@OneToMany(mappedBy = "countryId")
	private Set<CountryToRegion> countryToRegion;
	
	

	public Set<CountryToRegion> getCountryToRegion() {
		return countryToRegion;
	}

	public void setCountryToRegion(Set<CountryToRegion> countryToRegion) {
		this.countryToRegion = countryToRegion;
	}

	public MstCountry() {
		// DO NOTHING
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Byte getStatus() {
		return this.status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public Set<MstState> getMstStates() {
		return this.mstStates;
	}

	public void setMstStates(Set<MstState> mstStates) {
		this.mstStates = mstStates;
	}

	public MstState addMstState(MstState mstState) {
		getMstStates().add(mstState);
		mstState.setMstCountry(this);

		return mstState;
	}

	public MstState removeMstState(MstState mstState) {
		getMstStates().remove(mstState);
		mstState.setMstCountry(null);

		return mstState;
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