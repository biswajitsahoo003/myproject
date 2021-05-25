package com.tcl.dias.l2oworkflowutils.beans;

import java.util.Date;

import com.tcl.dias.l2oworkflow.entity.entities.MfPopData;

public class MfPopDataBean {

    private Integer id;
    private String name;
    private String networkLocationId;
    private String siteAddress;
    private String city;
    private String state;
    private Date createDate;
    private Date updatedDate;
    private String country;
    private String networkLocationType;
    private String popServiceType;

    public MfPopDataBean(MfPopData mfPopData) {
    	
    	this.id = mfPopData.getId();
    	this.name = mfPopData.getName();
    	this.networkLocationId = mfPopData.getNetworkLocationId();
    	this.siteAddress = mfPopData.getSiteAddress();
    	this.city = mfPopData.getCity();
    	this.state = mfPopData.getState();
    	this.createDate = mfPopData.getCreatedDate();
    	this.updatedDate = mfPopData.getUpdatedDate();
    	this.country = mfPopData.getCountry();
    	this.networkLocationType = mfPopData.getNetworkLocationType();
    	this.popServiceType =mfPopData.getPopServiceType()!=null?mfPopData.getPopServiceType():"";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNetworkLocationId() {
        return networkLocationId;
    }

    public void setNetworkLocationId(String networkLocationId) {
        this.networkLocationId = networkLocationId;
    }

    public String getSiteAddress() {
        return siteAddress;
    }

    public void setSiteAddress(String siteAddress) {
        this.siteAddress = siteAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNetworkLocationType() {
		return networkLocationType;
	}

	public void setNetworkLocationType(String networkLocationType) {
		this.networkLocationType = networkLocationType;
	}

    public String getPopServiceType() { return popServiceType; }

    public void setPopServiceType(String popServiceType) { this.popServiceType = popServiceType; }


}
