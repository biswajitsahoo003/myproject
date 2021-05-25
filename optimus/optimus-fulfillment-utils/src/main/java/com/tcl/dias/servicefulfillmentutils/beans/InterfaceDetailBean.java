package com.tcl.dias.servicefulfillmentutils.beans;

import java.sql.Timestamp;

public class InterfaceDetailBean {

    private Integer interfaceProtocolMappingId;
    private Timestamp endDate;
    private Boolean iscpeLanInterface;
    private Boolean iscpeWanInterface;
    private boolean isEdited;
    private Timestamp lastModifiedDate;
    private String modifiedBy;
    private Timestamp startDate;
    private Integer version;

    public Integer getInterfaceProtocolMappingId() {
        return interfaceProtocolMappingId;
    }

    public void setInterfaceProtocolMappingId(Integer interfaceProtocolMappingId) {
        this.interfaceProtocolMappingId = interfaceProtocolMappingId;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public Boolean getIscpeLanInterface() {
        return iscpeLanInterface;
    }

    public void setIscpeLanInterface(Boolean iscpeLanInterface) {
        this.iscpeLanInterface = iscpeLanInterface;
    }

    public Boolean getIscpeWanInterface() {
        return iscpeWanInterface;
    }

    public void setIscpeWanInterface(Boolean iscpeWanInterface) {
        this.iscpeWanInterface = iscpeWanInterface;
    }

    public Timestamp getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Timestamp lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public boolean isEdited() {
        return isEdited;
    }

    public void setEdited(boolean edited) {
        isEdited = edited;
    }
}
