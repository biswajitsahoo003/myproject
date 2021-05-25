package com.tcl.dias.oms.gsc.beans.excel;

/**
 * Bean for Order Enrichment Bulk upload excel
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class DomesticVoiceSheetBean extends OrderEnrichmentExcel {

    private String originCountry;
    private String city;
    private String streetAddress;
    private String locality;
    private String suit;
    private String state;
    private String postalCode;
    private String registrationNumber;
    private String quantityRequired;
    private String portedStatus;
    private String portedNumber;

    public DomesticVoiceSheetBean() {
    }

    public DomesticVoiceSheetBean(String originCountry, String city, String streetAddress, String locality, String suit, String
            state, String postalCode, String registrationNumber, String quantityRequired, String portedStatus, String portedNumber) {
        this.originCountry = originCountry;
        this.city = city;
        this.streetAddress = streetAddress;
        this.locality = locality;
        this.suit = suit;
        this.state = state;
        this.postalCode = postalCode;
        this.registrationNumber = registrationNumber;
        this.quantityRequired = quantityRequired;
        this.portedStatus = portedStatus;
        this.portedNumber = portedNumber;
    }

    public String getOriginCountry() {
        return originCountry;
    }

    public void setOriginCountry(String originCountry) {
        this.originCountry = originCountry;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getSuit() {
        return suit;
    }

    public void setSuit(String suit) {
        this.suit = suit;
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

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(String quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public String getPortedStatus() {
        return portedStatus;
    }

    public void setPortedStatus(String portedStatus) {
        this.portedStatus = portedStatus;
    }

    public String getPortedNumber() {
        return portedNumber;
    }

    public void setPortedNumber(String portedNumber) {
        this.portedNumber = portedNumber;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return "DomesticVoiceSheetBean{" +
                "originCountry='" + originCountry + '\'' +
                ", city='" + city + '\'' +
                ", streetAddress='" + streetAddress + '\'' +
                ", locality='" + locality + '\'' +
                ", suit='" + suit + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", quantityRequired='" + quantityRequired + '\'' +
                ", portedStatus='" + portedStatus + '\'' +
                ", portedNumber='" + portedNumber + '\'' +
                '}';
    }
}
