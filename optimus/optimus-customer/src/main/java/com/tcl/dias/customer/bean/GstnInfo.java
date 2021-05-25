package com.tcl.dias.customer.bean;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * This file Contains customerContracting Entity information
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GstnInfo {
    private Integer leStateGstId;
    private String address;
    private String locality;
    private String state;
    private String city;
    private String pincode;
    private String gstn;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getGstn() {
        return gstn;
    }

    public void setGstn(String gstn) {
        this.gstn = gstn;
    }

    public Integer getLeStateGstId() {
        return leStateGstId;
    }

    public void setLeStateGstId(Integer leStateGstId) {
        this.leStateGstId = leStateGstId;
    }

    @Override
    public String toString() {
        return "GstnInfo [address=" + address + ", locality=" + locality + ", state=" + state + ", city=" + city
                + ", pincode=" + pincode + ", gstn=" + gstn + "]";
    }

}
