package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author diksha garg
 * 
 * VendorContact for getting Vendor Contact Details
 *
 ** @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class VendorContact {

    private String firstName;
    private String lastName;
    private VendorContactMethod vendorContactMethod;
    private String vendorType;
    private List<String> contactRegion = null;
 
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public VendorContactMethod getVendorContactMethod() {
        return vendorContactMethod;
    }

    public void setVendorContactMethod(VendorContactMethod vendorContactMethod) {
        this.vendorContactMethod = vendorContactMethod;
    }

    public String getVendorType() {
        return vendorType;
    }

    public void setVendorType(String vendorType) {
        this.vendorType = vendorType;
    }

    public List<String> getContactRegion() {
        return contactRegion;
    }

    public void setContactRegion(List<String> contactRegion) {
        this.contactRegion = contactRegion;
    }

}
