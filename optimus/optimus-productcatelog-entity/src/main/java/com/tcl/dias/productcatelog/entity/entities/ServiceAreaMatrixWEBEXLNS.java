package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * The persistent class for the vw_service_area_matrix_ucaas_lns database table.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_service_area_matrix_ucaas_lns")
@Immutable
@NamedQuery(name = "ServiceAreaMatrixWEBEXLNS.findAll", query = "SELECT v FROM ServiceAreaMatrixWEBEXLNS v")
public class ServiceAreaMatrixWEBEXLNS implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name = "country")
	private String country;

	@Column(name = "code")
	private String code;

	@Column(name = "isdcode")
	private String isdcode;

	@Column(name = "is_packaged_ind")
	private String is_packaged_ind;
	
	public ServiceAreaMatrixWEBEXLNS() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIsdCode() {
		return isdcode;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIs_packaged_ind() {
		return is_packaged_ind;
	}

	public void setIs_packaged_ind(String is_packaged_ind) {
		this.is_packaged_ind = is_packaged_ind;
	}
}
