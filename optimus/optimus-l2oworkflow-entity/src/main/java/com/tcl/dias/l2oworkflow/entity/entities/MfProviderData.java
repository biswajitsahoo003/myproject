package com.tcl.dias.l2oworkflow.entity.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "mf_provider_data")
@NamedQuery(name = "MfProviderData.findAll", query = "SELECT m FROM MfProviderData m")
public class MfProviderData {

    @Id
    private Integer id;

    @Column(name = "provider_name")
    private String providerName;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name="created_by")
    private String createdBy;


    @Column(name="updated_by")
    private String updatedBy;

    @Column(name = "updated_time")
    private Date updatedTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
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

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }


}
