package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author diksha garg
 * 
 * VendorResponseBean for Vendor Master Integration with AKANA
 *
 ** @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class VendorResponseBean {

   
    private Integer status;
    private String message;
    private List<VendorDetail> vendorDetails = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<VendorDetail> getVendorDetails() {
        return vendorDetails;
    }

    public void setVendorDetails(List<VendorDetail> vendorDetails) {
        this.vendorDetails = vendorDetails;
    }

}
