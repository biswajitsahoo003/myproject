package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * The persistent class for the vw_service_area_matrix_ucaas_all database table.
 *
 */
@Entity
@Table(name = "vw_service_area_matrix_ucaas_all")
@Immutable
@NamedQuery(name = "ServiceAreaMatrixWEBEXALLProducts.findAll", query = "SELECT v FROM ServiceAreaMatrixWEBEXALLProducts v")
public class ServiceAreaMatrixWEBEXALLProducts implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "country")
	private String country;

	@Column(name = "code")
	private String code;

	@Column(name = "isdcode")
	private String isdcode;
	
	public ServiceAreaMatrixWEBEXALLProducts() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsdcode() {
		return isdcode;
	}

	public void setIsdcode(String isdcode) {
		this.isdcode = isdcode;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
