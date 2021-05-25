package com.tcl.dias.oms.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * 
 * Entity class for geo_code table
 * 
 *
 * @author Prabhu A
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name = "geo_code")
@NamedQuery(name = "GeoCode.findAll", query = "SELECT s FROM GeoCode s")
public class GeoCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "zipcode")
	private String zipcode;

	@Column(name = "geo_code")
	private String geoCode;

	/**
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return the zipcode
	 */
	public String getZipcode() {
		return zipcode;
	}

	/**
	 * @param zipcode the zipcode to set
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/**
	 * @return the geoCode
	 */
	public String getGeoCode() {
		return geoCode;
	}

	/**
	 * @param geoCode the geoCode to set
	 */
	public void setGeoCode(String geoCode) {
		this.geoCode = geoCode;
	}
	
	
}
