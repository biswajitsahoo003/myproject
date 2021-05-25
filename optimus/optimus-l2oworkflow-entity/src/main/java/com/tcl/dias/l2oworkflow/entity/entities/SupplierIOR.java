package com.tcl.dias.l2oworkflow.entity.entities;

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
 * This file contains the SupplierIOR.java class.
 * 
 *
 * @author Ninad.Pingale
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@Entity
@Table(name = "supplier_ior")
@NamedQuery(name = "SupplierIOR.findAll", query = "SELECT m FROM SupplierIOR m")
public class SupplierIOR implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "supplier_name")
	private String supplierName;

	@Column(name = "ior_id")
	private String iorId;

	@Column(name = "nmi_location")
	private String nmiLocation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public String getIorId() {
		return iorId;
	}

	public void setIorId(String iorId) {
		this.iorId = iorId;
	}

	public String getNmiLocation() {
		return nmiLocation;
	}

	public void setNmiLocation(String nmiLocation) {
		this.nmiLocation = nmiLocation;
	}

	@Override
	public String toString() {
		return "SupplierIOR [id=" + id + ", supplierName=" + supplierName + ", iorId=" + iorId + ", nmiLocation="
				+ nmiLocation + "]";
	}

}