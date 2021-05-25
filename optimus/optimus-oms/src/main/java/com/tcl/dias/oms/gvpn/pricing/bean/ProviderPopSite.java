package com.tcl.dias.oms.gvpn.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "DB_Code",
        "Country_ID",
        "Country",
        "All_Sites_Long",
        "TCL_POP_Address",
        "TCL_POP_Latitude",
        "TCL_POP_Longitude"
})

public class ProviderPopSite {

    @JsonProperty("DB_Code")
    private String dbCode;

    @JsonProperty("Country_ID")
    private String countryId;

    @JsonProperty("Country")
    private String country;

    @JsonProperty("All_Sites_Long")
    private String allSitesLong;

    @JsonProperty("TCL_POP_Address")
    private String tclPopAddress;

    @JsonProperty("TCL_POP_Latitude")
    private String tclPopLatitude;

    @JsonProperty("TCL_POP_Longitude")
    private String tclPopLongitude;

    @JsonProperty("DB_Code")
    public String getDbCode() {
        return dbCode;
    }

    @JsonProperty("DB_Code")
    public void setDbCode(String dbCode) {
        this.dbCode = dbCode;
    }

    @JsonProperty("Country_ID")
    public String getCountryId() {
        return countryId;
    }

    @JsonProperty("Country_ID")
    public void setCountryId(String countryId) {
        this.countryId = countryId;
    }

    @JsonProperty("Country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("Country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("All_Sites_Long")
    public String getAllSitesLong() {
        return allSitesLong;
    }
    @JsonProperty("All_Sites_Long")
    public void setAllSitesLong(String allSitesLong) {
        this.allSitesLong = allSitesLong;
    }
    @JsonProperty("TCL_POP_Address")
    public String getTclPopAddress() {
        return tclPopAddress;
    }
    @JsonProperty("TCL_POP_Address")
    public void setTclPopAddress(String tclPopAddress) {
        this.tclPopAddress = tclPopAddress;
    }

    @JsonProperty("TCL_POP_Latitude")
    public String getTclPopLatitude() {
        return tclPopLatitude;
    }

    @JsonProperty("TCL_POP_Latitude")
    public void setTclPopLatitude(String tclPopLatitude) {
        this.tclPopLatitude = tclPopLatitude;
    }

    @JsonProperty("TCL_POP_Longitude")
    public String getTclPopLongitude() {
        return tclPopLongitude;
    }

    @JsonProperty("TCL_POP_Longitude")
    public void setTclPopLongitude(String tclPopLongitude) {
        this.tclPopLongitude = tclPopLongitude;
    }
}
