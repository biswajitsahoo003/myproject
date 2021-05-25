package com.tcl.dias.l2oworkflowutils.beans;

import java.util.Date;

import com.tcl.dias.l2oworkflow.entity.entities.MfVendorData;

public class MfVendorDataBean {

	private String vendorName;

	private Date createdTime;

	private Date updatedTime;

	private String createdBy;

	private String updatedBy;

	private String tclEntityName;

	private String address;

	private String city;

	private String region;

	private String sfdcProviderName;
	
	private String vendorId;

	private Integer id;
	public MfVendorDataBean(MfVendorData mfPopData) {
		this.id = mfPopData.getId();
		this.vendorName = mfPopData.getVendorName();
		this.createdTime = mfPopData.getCreatedTime();
		this.createdBy = mfPopData.getCreatedBy();
		this.updatedBy = mfPopData.getUpdatedBy();
		this.tclEntityName = mfPopData.getTclEntityName();
		this.address = mfPopData.getAddress();
		this.city = mfPopData.getCity();
		this.region = mfPopData.getRegion();
		this.sfdcProviderName = mfPopData.getSfdcProviderName();
		this.vendorId = mfPopData.getVendorId();
		this.updatedTime = mfPopData.getUpdatedTime();
	} 
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getTclEntityName() {
		return tclEntityName;
	}

	public void setTclEntityName(String tclEntityName) {
		this.tclEntityName = tclEntityName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getSfdcProviderName() {
		return sfdcProviderName;
	}

	public void setSfdcProviderName(String sfdcProviderName) {
		this.sfdcProviderName = sfdcProviderName;
	}

}
