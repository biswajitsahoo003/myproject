package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_service_area_matrix_ucaas_bridge_ctry_dtl
 * database table.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_service_area_matrix_ucaas_bridge_ctry_dtl")
@NamedQuery(name = "ServiceAreaMatrixWEBEXbridgecountrydetail.findAll", query = "SELECT v FROM ServiceAreaMatrixWEBEXbridgecountrydetail v")
public class ServiceAreaMatrixWEBEXbridgecountrydetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name = "country")
	private String country;

	@Column(name = "region")
	private String region;

	@Column(name = "code")
	private String code;

	@Column(name = "isdcode")
	private String isdcode;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsdCode() {
		return isdcode;
	}

	public ServiceAreaMatrixWEBEXbridgecountrydetail() {
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getRegion() {
		return this.region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
