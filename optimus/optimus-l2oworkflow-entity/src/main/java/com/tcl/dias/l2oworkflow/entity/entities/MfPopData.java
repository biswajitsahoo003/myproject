package com.tcl.dias.l2oworkflow.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "mf_pop_data")
@NamedQuery(name = "MfPopData.findAll", query = "SELECT m FROM MfPopData m")
public class MfPopData {

    @Id
    private Integer id;
    private String name;

    @Column(name = "network_location_id")
    private String networkLocationId;

    @Column(name = "site_address")
    private String siteAddress;

    private String city;

    private String state;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_date")
    private Date updatedDate;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "network_location_type")
    private String networkLocationType;

    @Column(name = "pop_service_type")
    private String popServiceType;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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

