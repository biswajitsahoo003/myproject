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
@Table(name = "mst_city")
@NamedQuery(name = "MstCity.findAll", query = "SELECT m FROM MstCity m")
public class MstCity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	private String name;

	private String source;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "created_time")
	private Timestamp createdTime;

	// bi-directional many-to-one association to MstState
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "state_id")
	private MstState mstState;

	// bi-directional many-to-one association to MstLocality
	@OneToMany(mappedBy = "mstCity")
	private Set<MstLocality> mstLocalities;

	// bi-directional many-to-one association to MstPincode
	@OneToMany(mappedBy = "mstCity")
	private Set<MstPincode> mstPincodes;

	public MstCity() {
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

	public MstState getMstState() {
		return this.mstState;
	}

	public void setMstState(MstState mstState) {
		this.mstState = mstState;
	}

	public Set<MstLocality> getMstLocalities() {
		return this.mstLocalities;
	}

	public void setMstLocalities(Set<MstLocality> mstLocalities) {
		this.mstLocalities = mstLocalities;
	}

	public MstLocality addMstLocality(MstLocality mstLocality) {
		getMstLocalities().add(mstLocality);
		mstLocality.setMstCity(this);

		return mstLocality;
	}

	public MstLocality removeMstLocality(MstLocality mstLocality) {
		getMstLocalities().remove(mstLocality);
		mstLocality.setMstCity(null);

		return mstLocality;
	}

	public Set<MstPincode> getMstPincodes() {
		return this.mstPincodes;
	}

	public void setMstPincodes(Set<MstPincode> mstPincodes) {
		this.mstPincodes = mstPincodes;
	}

	public MstPincode addMstPincode(MstPincode mstPincode) {
		getMstPincodes().add(mstPincode);
		mstPincode.setMstCity(this);

		return mstPincode;
	}

	public MstPincode removeMstPincode(MstPincode mstPincode) {
		getMstPincodes().remove(mstPincode);
		mstPincode.setMstCity(null);

		return mstPincode;
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