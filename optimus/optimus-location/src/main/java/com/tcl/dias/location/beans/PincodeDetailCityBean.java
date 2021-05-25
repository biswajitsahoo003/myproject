package com.tcl.dias.location.beans;

import java.util.List;
import java.util.Map;

public class PincodeDetailCityBean {

    private String pincode;
    private String country;
    private String state;
    private List<Map<String, Object>> city;
    private List<String> localities;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<Map<String, Object>> getCity() {
        return city;
    }

    public void setCity(List<Map<String, Object>> city) {
        this.city = city;
    }

    public List<String> getLocalities() {
        return localities;
    }

    public void setLocalities(List<String> localities) {
        this.localities = localities;
    }
}
