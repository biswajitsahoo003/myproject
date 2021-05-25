package com.tcl.dias.l2oworkflowutils.beans;

import com.tcl.dias.l2oworkflow.entity.entities.SupplierIOR;

public class SupplierIORBean {
	
	private Integer id;
	private String supplierName;
	private String iorId;
	private String nmiLocation;
	
	public SupplierIORBean(SupplierIOR ior) {
		this.id = ior.getId();
		this.supplierName = ior.getSupplierName();
		this.iorId = ior.getIorId();
		this.nmiLocation = ior.getNmiLocation();
	}
	
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
	
	
}
