package com.tcl.dias.common.beans;

import java.util.Objects;

/**
 * This class for Gsc Domestic Voice Address Detail
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class DomesticVoiceAddressDetail {

    private String country;

    private String city;

    private String address;

    private String locality;

    private String floor;

    private String state;

    private String postalCode;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DomesticVoiceAddressDetail that = (DomesticVoiceAddressDetail) o;
        return country.equals(that.country) &&
                city.equals(that.city) &&
                address.equals(that.address) &&
                locality.equals(that.locality) &&
                floor.equals(that.floor) &&
                state.equals(that.state) &&
                postalCode.equals(that.postalCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(country, city, address, floor, state, postalCode);
    }

}
