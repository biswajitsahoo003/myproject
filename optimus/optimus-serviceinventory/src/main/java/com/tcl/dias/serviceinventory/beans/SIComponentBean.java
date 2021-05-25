package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.common.serviceinventory.bean.ScComponentAttributeBean;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class SIComponentBean {

    private Integer id;
    private String componentName;
    private String createdBy;
    private Timestamp createdDate;
    private String isActive;
    private String siteType;
    private String updatedBy;
    private Timestamp updatedDate;
    private String uuid;

    private List<ScComponentAttributeBean> scComponentAttributeBeans = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public List<ScComponentAttributeBean> getScComponentAttributeBeans() {
        return scComponentAttributeBeans;
    }

    public void setScComponentAttributeBeans(List<ScComponentAttributeBean> scComponentAttributeBeans) {
        this.scComponentAttributeBeans = scComponentAttributeBeans;
    }
}
