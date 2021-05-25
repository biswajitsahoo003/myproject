package com.tcl.dias.serviceactivation.beans;

/**
 * Bean for setting end Date functionality
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class IPServiceEndDateBean {

    private String serviceId;
    private Integer version;
    private Boolean reverseEndDate;
    private Boolean currentDate;
    private String modifiedBy;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getReverseEndDate() {
        return reverseEndDate;
    }

    public void setReverseEndDate(Boolean reverseEndDate) {
        this.reverseEndDate = reverseEndDate;
    }

    public Boolean getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Boolean currentDate) {
        this.currentDate = currentDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
