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
@Table(name = "mst_locality")
@NamedQuery(name = "MstLocality.findAll", query = "SELECT m FROM MstLocality m")
public class MstLocality implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private String code;

	@Column(name = "created_by")
	private Integer createdBy;

	private String name;

	private String source;

	@Column(name = "created_time")
	private Timestamp createdTime;

	// bi-directional many-to-one association to LocalityPincode
	@OneToMany(mappedBy = "mstLocality")
	private Set<LocalityPincode> localityPincodes;

	// bi-directional many-to-one association to MstCity
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "city_id")
	private MstCity mstCity;

	public MstLocality() {
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

	public Integer getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSource() {
		return this.source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Set<LocalityPincode> getLocalityPincodes() {
		return this.localityPincodes;
	}

	public void setLocalityPincodes(Set<LocalityPincode> localityPincodes) {
		this.localityPincodes = localityPincodes;
	}

	public LocalityPincode addLocalityPincode(LocalityPincode localityPincode) {
		getLocalityPincodes().add(localityPincode);
		localityPincode.setMstLocality(this);

		return localityPincode;
	}

	public LocalityPincode removeLocalityPincode(LocalityPincode localityPincode) {
		getLocalityPincodes().remove(localityPincode);
		localityPincode.setMstLocality(null);

		return localityPincode;
	}

	public MstCity getMstCity() {
		return this.mstCity;
	}

	public void setMstCity(MstCity mstCity) {
		this.mstCity = mstCity;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

}