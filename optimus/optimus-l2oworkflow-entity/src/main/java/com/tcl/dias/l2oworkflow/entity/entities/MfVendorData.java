package com.tcl.dias.l2oworkflow.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "mf_vendor_data")
@NamedQuery(name = "MfVendorData.findAll", query = "SELECT m FROM MfVendorData m")
public class MfVendorData {

	@Id
    private Integer id;
	
    @Column(name = "vendor_id")
	private String vendorId;

	@Column(name = "vendor_name")
    private String vendorName;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "updated_time")
    private Date updatedTime;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="updated_by")
    private String updatedBy;
    
    @Column(name="tcl_entity_name")
    private String tclEntityName;

    @Column(name="address")
    private String address;
    
    @Column(name="city")
    private String city;
    
    @Column(name="region")
    private String region;
    
    @Column(name="sfdc_provider_name")
    private String sfdcProviderName;
    
    public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
    
	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

	public String getTclEntityName() {
		return tclEntityName;
	}

	public void setTclEntityName(String tclEntityName) {
		this.tclEntityName = tclEntityName;
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
    

}
