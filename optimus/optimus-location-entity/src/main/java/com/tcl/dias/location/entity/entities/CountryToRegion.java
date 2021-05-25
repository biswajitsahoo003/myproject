package com.tcl.dias.location.entity.entities;

import java.io.Serializable;

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
 * Entity Class
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "country_to_region")
@NamedQuery(name = "CountryToRegion.findAll", query = "SELECT m FROM CountryToRegion m")
public class CountryToRegion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "region_id")
	private MstRegion regionId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "country_id")
	private MstCountry countryId;
	
	@Column(name = "status")
	private Byte status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MstRegion getRegionId() {
		return regionId;
	}

	public void setRegionId(MstRegion regionId) {
		this.regionId = regionId;
	}

	public MstCountry getCountryId() {
		return countryId;
	}

	public void setCountryId(MstCountry countryId) {
		this.countryId = countryId;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}
	
	
	
	
}