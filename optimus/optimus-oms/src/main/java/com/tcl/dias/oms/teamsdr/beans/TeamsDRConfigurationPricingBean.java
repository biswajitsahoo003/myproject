package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.math.BigDecimal;
import java.util.List;

/**
 * Bean for Configuration Pricing
 *
 * @author Syed Ali
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "id", "country", "is_gsc", "is_regulated_country", "is_exceptional_country", "no_of_named_users", "no_of_common_area_devices",
"total_users","cities","mrc","nrc","arc","tcv"})
public class TeamsDRConfigurationPricingBean {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("country")
    private String country;
    @JsonProperty("is_gsc")
    private Boolean isGsc;
    @JsonProperty("is_regulated_country")
    private Boolean isRegulatedCountry;
    @JsonProperty("is_exceptional_country")
    private Boolean isExceptionalCountry;
    @JsonProperty("no_of_named_users")
    private Integer noOfNamedUsers;
    @JsonProperty("no_of_common_area_devices")
    private Integer noOfCommonAreaDevices;
    @JsonProperty("total_users")
    private Integer totalUsers;
    @JsonProperty("cities")
    private List<TeamsDRCityPricingBean> cities;
    @JsonProperty("mrc")
    private BigDecimal mrc;
    @JsonProperty("nrc")
    private BigDecimal nrc;
    @JsonProperty("arc")
    private BigDecimal arc;
    @JsonProperty("tcv")
    private BigDecimal tcv;


    public TeamsDRConfigurationPricingBean() {
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("country")
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonProperty("is_gsc")
    public Boolean getGsc() {
        return isGsc;
    }

    @JsonProperty("is_gsc")
    public void setGsc(Boolean gsc) {
        isGsc = gsc;
    }

    @JsonProperty("is_regulated_country")
    public Boolean getRegulatedCountry() {
        return isRegulatedCountry;
    }

    @JsonProperty("is_regulated_country")
    public void setRegulatedCountry(Boolean regulatedCountry) {
        isRegulatedCountry = regulatedCountry;
    }

    @JsonProperty("is_exceptional_country")
    public Boolean getExceptionalCountry() {
        return isExceptionalCountry;
    }

    @JsonProperty("is_exceptional_country")
    public void setExceptionalCountry(Boolean exceptionalCountry) {
        isExceptionalCountry = exceptionalCountry;
    }

    @JsonProperty("no_of_named_users")
    public Integer getNoOfNamedUsers() {
        return noOfNamedUsers;
    }

    @JsonProperty("no_of_named_users")
    public void setNoOfNamedUsers(Integer noOfNamedUsers) {
        this.noOfNamedUsers = noOfNamedUsers;
    }

    @JsonProperty("no_of_common_area_devices")
    public Integer getNoOfCommonAreaDevices() {
        return noOfCommonAreaDevices;
    }

    @JsonProperty("no_of_common_area_devices")
    public void setNoOfCommonAreaDevices(Integer noOfCommonAreaDevices) {
        this.noOfCommonAreaDevices = noOfCommonAreaDevices;
    }

    @JsonProperty("total_users")
    public Integer getTotalUsers() {
        return totalUsers;
    }

    @JsonProperty("total_users")
    public void setTotalUsers(Integer totalUsers) {
        this.totalUsers = totalUsers;
    }

    @JsonProperty("cities")
    public List<TeamsDRCityPricingBean> getCities() {
        return cities;
    }

    @JsonProperty("cities")
    public void setCities(List<TeamsDRCityPricingBean> cities) {
        this.cities = cities;
    }

    @JsonProperty("mrc")
    public BigDecimal getMrc() {
        return mrc;
    }

    @JsonProperty("mrc")
    public void setMrc(BigDecimal mrc) {
        this.mrc = mrc;
    }

    @JsonProperty("nrc")
    public BigDecimal getNrc() {
        return nrc;
    }

    @JsonProperty("nrc")
    public void setNrc(BigDecimal nrc) {
        this.nrc = nrc;
    }

    @JsonProperty("arc")
    public BigDecimal getArc() {
        return arc;
    }

    @JsonProperty("arc")
    public void setArc(BigDecimal arc) {
        this.arc = arc;
    }

    @JsonProperty("trc")
    public BigDecimal getTcv() {
        return tcv;
    }

    @JsonProperty("trc")
    public void setTcv(BigDecimal tcv) {
        this.tcv = tcv;
    }

    @Override
    public String toString() {
        return "TeamsDRConfigurationPricingBean{" +
                "id=" + id +
                ", country='" + country + '\'' +
                ", isGsc=" + isGsc +
                ", isRegulatedCountry=" + isRegulatedCountry +
                ", isExceptionalCountry=" + isExceptionalCountry +
                ", noOfNamedUsers=" + noOfNamedUsers +
                ", noOfCommonAreaDevices=" + noOfCommonAreaDevices +
                ", totalUsers=" + totalUsers +
                ", cities=" + cities +
                ", mrc=" + mrc +
                ", nrc=" + nrc +
                ", arc=" + arc +
                ", tcv=" + tcv +
                '}';
    }
}
