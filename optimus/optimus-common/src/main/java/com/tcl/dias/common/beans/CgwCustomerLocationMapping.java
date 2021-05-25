package com.tcl.dias.common.beans;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Set;

/**
 * This file contains the CgwCustomerLocationMapping.java class.
 *
 * @author Anway Bhutkar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CgwCustomerLocationMapping implements Serializable {

    private static final long serialVersionUID = -3596334693289410970L;

    private Set<Integer> locationIds;

    private Integer customerId;

    private Integer customerLeId;

    public Set<Integer> getLocationIds() {
        return locationIds;
    }

    public void setLocationIds(Set<Integer> locationIds) {
        this.locationIds = locationIds;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getCustomerLeId() {
        return customerLeId;
    }

    public void setCustomerLeId(Integer customerLeId) {
        this.customerLeId = customerLeId;
    }

    @Override
    public String toString() {
        return "CgwCustomerLocationMapping{" +
                "locationId=" + locationIds +
                ", customerId=" + customerId +
                ", customerLeId=" + customerLeId +
                '}';
    }
}
