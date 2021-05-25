package com.tcl.dias.oms.gsc.pricing.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Pricing display rate class for global out a-z outbound
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalOutboundDisplayRate {

    @JsonProperty("destination")
    private List<String> destinationCountry = new ArrayList<>();

    @JsonProperty("destination_name")
    private List<String> destinationName = new ArrayList<>();

    @JsonProperty("disp_currency")
    private List<String> displayCurrency = new ArrayList<>();

    @JsonProperty("phone_type")
    private List<String> phoneType = new ArrayList<>();

    @JsonProperty("price")
    private List<String> price = new ArrayList<>();

    @JsonProperty("dest_id")
    private List<String> destId = new ArrayList<>();

    @JsonProperty("comments")
    private List<String> comments = new ArrayList<>();

    @JsonProperty("service_level")
    private List<String> serviceLevel = new ArrayList<>();

    public List<String> getDestinationCountry() {
        return destinationCountry;
    }

    public void setDestinationCountry(List<String> destinationCountry) {
        this.destinationCountry = destinationCountry;
    }

    public List<String> getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(List<String> destinationName) {
        this.destinationName = destinationName;
    }

    public List<String> getDisplayCurrency() {
        return displayCurrency;
    }

    public void setDisplayCurrency(List<String> displayCurrency) {
        this.displayCurrency = displayCurrency;
    }

    public List<String> getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(List<String> phoneType) {
        this.phoneType = phoneType;
    }

    public List<String> getPrice() {
        return price;
    }

    public void setPrice(List<String> price) {
        this.price = price;
    }

    public List<String> getDestId() {
        return destId;
    }

    public void setDestId(List<String> destId) {
        this.destId = destId;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getServiceLevel() {
        return serviceLevel;
    }

    public void setServiceLevel(List<String> serviceLevel) {
        this.serviceLevel = serviceLevel;
    }

    @Override
    public String toString() {
        return "GlobalOutboundDisplayRate{" +
                "destinationCountry=" + destinationCountry +
                ", destinationName=" + destinationName +
                ", displayCurrency=" + displayCurrency +
                ", phoneType=" + phoneType +
                ", price=" + price +
                ", destId=" + destId +
                ", comments=" + comments +
                ", serviceLevel=" + serviceLevel +
                '}';
    }
}
