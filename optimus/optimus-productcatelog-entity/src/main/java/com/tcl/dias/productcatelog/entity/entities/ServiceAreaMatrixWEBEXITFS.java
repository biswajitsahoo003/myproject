package com.tcl.dias.productcatelog.entity.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

/**
 * The persistent class for the vw_service_area_matrix_ucaas_itfs database
 * table.
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "vw_service_area_matrix_ucaas_itfs")
@Immutable
@NamedQuery(name = "ServiceAreaMatrixWEBEXITFS.findAll", query = "SELECT v FROM ServiceAreaMatrixWEBEXITFS v")
public class ServiceAreaMatrixWEBEXITFS implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String uid;

	@Column(name = "country")
	private String country;

	public ServiceAreaMatrixWEBEXITFS() {

	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
