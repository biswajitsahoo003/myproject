package com.tcl.dias.oms.webex.beans;

/**
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class SiteAddress {
    private String addressLineOne;
    private String addressLineTwo;
    private String addressLineThree;
    private String city;
    private String state;
    private String country;
    private String pinCode;

    public SiteAddress() {
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getAddressLineThree() {
        return addressLineThree;
    }

    public void setAddressLineThree(String addressLineThree) {
        this.addressLineThree = addressLineThree;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    @Override
    public String toString() {
        return "SiteAddress{" + "addressLineOne='" + addressLineOne + '\'' + ", addressLineTwo='" + addressLineTwo
                + '\'' + ", addressLineThree='" + addressLineThree + '\'' + ", city='" + city + '\'' + ", state='"
                + state + '\'' + ", country='" + country + '\'' + ", pinCode='" + pinCode + '\'' + '}';
    }
}
