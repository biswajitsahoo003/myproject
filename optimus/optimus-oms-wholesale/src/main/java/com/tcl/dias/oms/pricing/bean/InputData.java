package com.tcl.dias.oms.pricing.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "bw_mbps",
        "customer_segment",
        "sales_org",
        "product_name",
        "local_loop_interface",
        "last_mile_contract_term",
        "site_id",
        "prospect_name",
        "burstable_bw",
        "resp_city",
        "account_id_with_18_digit",
        "opportunity_term",
        "quotetype_quote",
        "connection_type",
        "sum_no_of_sites_uni_len",
        "cpe_variant",
        "cpe_management_type",
        "cpe_supply_type",
        "topology",
        "pool_size",
        "additional_ip",
        "latitude_final",
        "longitude_final",
        "feasibility_response_created_date"
})
public class InputData {

    @JsonProperty("bw_mbps")
    private Double bwMbps;
    @JsonProperty("customer_segment")
    private String customerSegment;
    @JsonProperty("sales_org")
    private String salesOrg;
    @JsonProperty("product_name")
    private String productName;
    @JsonProperty("local_loop_interface")
    private String localLoopInterface;
    @JsonProperty("last_mile_contract_term")
    private String lastMileContractTerm;
    @JsonProperty("site_id")
    private String siteId;
    @JsonProperty("prospect_name")
    private String prospectName;
    @JsonProperty("burstable_bw")
    private Integer burstableBw;
    @JsonProperty("resp_city")
    private String respCity;
    @JsonProperty("account_id_with_18_digit")
    private String accountIdWith18Digit;
    @JsonProperty("opportunity_term")
    private Integer opportunityTerm;
    @JsonProperty("quotetype_quote")
    private String quotetypeQuote;
    @JsonProperty("connection_type")
    private String connectionType;
    @JsonProperty("sum_no_of_sites_uni_len")
    private Integer sumNoOfSitesUniLen;
    @JsonProperty("cpe_variant")
    private String cpeVariant;
    @JsonProperty("cpe_management_type")
    private String cpeManagementType;
    @JsonProperty("cpe_supply_type")
    private String cpeSupplyType;
    @JsonProperty("topology")
    private String topology;
    @JsonProperty("pool_size")
    private Integer poolSize;
    @JsonProperty("additional_ip")
    private Integer additionalIp;
    @JsonProperty("latitude_final")
    private Double latitudeFinal;
    @JsonProperty("longitude_final")
    private Double longitudeFinal;
    @JsonProperty("feasibility_response_created_date")
    private String feasibilityResponseCreatedDate;

    @JsonProperty("bw_mbps")
    public Double getBwMbps() {
        return bwMbps;
    }

    @JsonProperty("bw_mbps")
    public void setBwMbps(Double bwMbps) {
        this.bwMbps = bwMbps;
    }

    @JsonProperty("customer_segment")
    public String getCustomerSegment() {
        return customerSegment;
    }

    @JsonProperty("customer_segment")
    public void setCustomerSegment(String customerSegment) {
        this.customerSegment = customerSegment;
    }

    @JsonProperty("sales_org")
    public String getSalesOrg() {
        return salesOrg;
    }

    @JsonProperty("sales_org")
    public void setSalesOrg(String salesOrg) {
        this.salesOrg = salesOrg;
    }

    @JsonProperty("product_name")
    public String getProductName() {
        return productName;
    }

    @JsonProperty("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
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

    @JsonProperty("prospect_name")
    public String getProspectName() {
        return prospectName;
    }

    @JsonProperty("prospect_name")
    public void setProspectName(String prospectName) {
        this.prospectName = prospectName;
    }

    @JsonProperty("burstable_bw")
    public Integer getBurstableBw() {
        return burstableBw;
    }

    @JsonProperty("burstable_bw")
    public void setBurstableBw(Integer burstableBw) {
        this.burstableBw = burstableBw;
    }

    @JsonProperty("resp_city")
    public String getRespCity() {
        return respCity;
    }

    @JsonProperty("resp_city")
    public void setRespCity(String respCity) {
        this.respCity = respCity;
    }

    @JsonProperty("account_id_with_18_digit")
    public String getAccountIdWith18Digit() {
        return accountIdWith18Digit;
    }

    @JsonProperty("account_id_with_18_digit")
    public void setAccountIdWith18Digit(String accountIdWith18Digit) {
        this.accountIdWith18Digit = accountIdWith18Digit;
    }

    @JsonProperty("opportunity_term")
    public Integer getOpportunityTerm() {
        return opportunityTerm;
    }

    @JsonProperty("opportunity_term")
    public void setOpportunityTerm(Integer opportunityTerm) {
        this.opportunityTerm = opportunityTerm;
    }

    @JsonProperty("quotetype_quote")
    public String getQuotetypeQuote() {
        return quotetypeQuote;
    }

    @JsonProperty("quotetype_quote")
    public void setQuotetypeQuote(String quotetypeQuote) {
        this.quotetypeQuote = quotetypeQuote;
    }

    @JsonProperty("connection_type")
    public String getConnectionType() {
        return connectionType;
    }

    @JsonProperty("connection_type")
    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public Integer getSumNoOfSitesUniLen() {
        return sumNoOfSitesUniLen;
    }

    @JsonProperty("sum_no_of_sites_uni_len")
    public void setSumNoOfSitesUniLen(Integer sumNoOfSitesUniLen) {
        this.sumNoOfSitesUniLen = sumNoOfSitesUniLen;
    }

    @JsonProperty("cpe_variant")
    public String getCpeVariant() {
        return cpeVariant;
    }

    @JsonProperty("cpe_variant")
    public void setCpeVariant(String cpeVariant) {
        this.cpeVariant = cpeVariant;
    }

    @JsonProperty("cpe_management_type")
    public String getCpeManagementType() {
        return cpeManagementType;
    }

    @JsonProperty("cpe_management_type")
    public void setCpeManagementType(String cpeManagementType) {
        this.cpeManagementType = cpeManagementType;
    }

    @JsonProperty("cpe_supply_type")
    public String getCpeSupplyType() {
        return cpeSupplyType;
    }

    @JsonProperty("cpe_supply_type")
    public void setCpeSupplyType(String cpeSupplyType) {
        this.cpeSupplyType = cpeSupplyType;
    }

    @JsonProperty("topology")
    public String getTopology() {
        return topology;
    }

    @JsonProperty("topology")
    public void setTopology(String topology) {
        this.topology = topology;
    }

    @JsonProperty("pool_size")
    public Integer getPoolSize() {
        return poolSize;
    }

    @JsonProperty("pool_size")
    public void setPoolSize(Integer poolSize) {
        this.poolSize = poolSize;
    }

    @JsonProperty("additional_ip")
    public Integer getAdditionalIp() {
        return additionalIp;
    }

    @JsonProperty("additional_ip")
    public void setAdditionalIp(Integer additionalIp) {
        this.additionalIp = additionalIp;
    }

    @JsonProperty("latitude_final")
    public Double getLatitudeFinal() {
        return latitudeFinal;
    }

    @JsonProperty("latitude_final")
    public void setLatitudeFinal(Double latitudeFinal) {
        this.latitudeFinal = latitudeFinal;
    }

    @JsonProperty("longitude_final")
    public Double getLongitudeFinal() {
        return longitudeFinal;
    }

    @JsonProperty("longitude_final")
    public void setLongitudeFinal(Double longitudeFinal) {
        this.longitudeFinal = longitudeFinal;
    }

    @JsonProperty("feasibility_response_created_date")
    public String getFeasibilityResponseCreatedDate() {
        return feasibilityResponseCreatedDate;
    }

    @JsonProperty("feasibility_response_created_date")
    public void setFeasibilityResponseCreatedDate(String feasibilityResponseCreatedDate) {
        this.feasibilityResponseCreatedDate = feasibilityResponseCreatedDate;
    }

}
