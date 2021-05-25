
package com.tcl.dias.wfe.dto;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "Latitude_final",
    "Longitude_final",
    "prospect_name",
    "BW_mbps",
    "Burstable_BW",
    "resp_city",
    "resp_state",
    "Customer_Segment",
    "Sales.Org",
    "Product.Name",
    "Feasibility.Response..Created.Date",
    "local_loop_interface",
    "last_mile_contract_term",
    "Site_Id"
})
public class InputDatum {

    @JsonProperty("Latitude_final")
    private String latitudeFinal;
    @JsonProperty("Longitude_final")
    private String longitudeFinal;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("BW_mbps")
    private String bWMbps;
    @JsonProperty("Burstable_BW")
    private String burstableBW;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("resp_state")
    private String respState;
    @JsonProperty("Customer_Segment")
    private String customerSegment;
    @JsonProperty("Sales.Org")
    private String salesOrg;
    @JsonProperty("Product.Name")
    private String productName;
    @JsonProperty("Feasibility.Response..Created.Date")
    private String feasibilityResponseCreatedDate;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("last_mile_contract_term")
    private String lastMileContractTerm;
    @JsonProperty("site_id")
    private String siteId;

    @JsonProperty("Latitude_final")
    public String getLatitudeFinal() {
        return latitudeFinal;
    }

    @JsonProperty("Latitude_final")
    public void setLatitudeFinal(String latitudeFinal) {
        this.latitudeFinal = latitudeFinal;
    }

    @JsonProperty("Longitude_final")
    public String getLongitudeFinal() {
        return longitudeFinal;
    }

    @JsonProperty("Longitude_final")
    public void setLongitudeFinal(String longitudeFinal) {
        this.longitudeFinal = longitudeFinal;
    }

    @JsonProperty("prospect_name")
    public String getProspectName() {
        return prospectName;
    }

    @JsonProperty("prospect_name")
    public void setProspectName(String prospectName) {
        this.prospectName = prospectName;
    }

    @JsonProperty("BW_mbps")
    public String getBWMbps() {
        return bWMbps;
    }

    @JsonProperty("BW_mbps")
    public void setBWMbps(String bWMbps) {
        this.bWMbps = bWMbps;
    }

    @JsonProperty("Burstable_BW")
    public String getBurstableBW() {
        return burstableBW;
    }

    @JsonProperty("Burstable_BW")
    public void setBurstableBW(String burstableBW) {
        this.burstableBW = burstableBW;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

    @JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("resp_state")
    public String getRespState() {
        return respState;
    }

    @JsonProperty("resp_state")
    public void setRespState(String respState) {
        this.respState = respState;
    }

    @JsonProperty("Customer_Segment")
    public String getCustomerSegment() {
        return customerSegment;
    }

    @JsonProperty("Customer_Segment")
    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    @JsonProperty("Sales.Org")
    public String getSalesOrg() {
        return salesOrg;
    }

    @JsonProperty("Sales.Org")
    public void setSalesOrg(String salesOrg) {
        this.salesOrg = salesOrg;
    }

    @JsonProperty("Product.Name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("Product.Name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @JsonProperty("Feasibility.Response..Created.Date")
    public String getFeasibilityResponseCreatedDate() {
        return feasibilityResponseCreatedDate;
    }

    @JsonProperty("Feasibility.Response..Created.Date")
    public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
        this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
    }

    @JsonProperty("local_loop_interface")
    public String getLocalLoopInterface() {
        return localLoopInterface;
    }

    @JsonProperty("local_loop_interface")
    public void setLocalLoopInterface(String localLoopInterface) {
        this.localLoopInterface = localLoopInterface;
    }

    @JsonProperty("last_mile_contract_term")
    public String getLastMileContractTerm() {
        return lastMileContractTerm;
    }

    @JsonProperty("last_mile_contract_term")
    public void setLastMileContractTerm(String lastMileContractTerm) {
        this.lastMileContractTerm = lastMileContractTerm;
    }
    
    @JsonProperty("site_id")
    public String getSiteId() {
        return siteId;
    }

    @JsonProperty("site_id")
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

}
