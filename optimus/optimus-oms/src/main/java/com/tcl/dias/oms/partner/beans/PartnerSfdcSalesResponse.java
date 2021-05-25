package com.tcl.dias.oms.partner.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;

/**
 * PartnerSfdcSalesResponse with relevant data of sales,
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonPropertyOrder({"salesData", "currency", "applicableProducts", "applicablePartnerLegalEntities", "applicableEngagementModes"})
public class PartnerSfdcSalesResponse {

    @JsonProperty("salesData")
    Map<String, Map<String, SfdcSalesFunnelData>> salesData = new HashMap<>();

    @JsonProperty("currency")
    String currency;

    @JsonProperty("applicableProducts")
    List<String> applicableProducts;

    @JsonProperty("applicablePartnerLegalEntities")
    Set<PartnerLegalEntityBean> applicablePartnerLegalEntities;

    @JsonProperty("applicableEngagementModes")
    List<String> applicableEngagementModes;

    public Map<String, Map<String, SfdcSalesFunnelData>> getSalesData() {
        return salesData;
    }

    public void setSalesData(Map<String, Map<String, SfdcSalesFunnelData>> salesData) {
        this.salesData = salesData;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<String> getApplicableProducts() {
        return applicableProducts;
    }

    public void setApplicableProducts(List<String> applicableProducts) {
        this.applicableProducts = applicableProducts;
    }

    public Set<PartnerLegalEntityBean> getApplicablePartnerLegalEntities() {
        return applicablePartnerLegalEntities;
    }

    public void setApplicablePartnerLegalEntities(Set<PartnerLegalEntityBean> applicablePartnerLegalEntities) {
        this.applicablePartnerLegalEntities = applicablePartnerLegalEntities;
    }

    public List<String> getApplicableEngagementModes() {
        return applicableEngagementModes;
    }

    public void setApplicableEngagementModes(List<String> applicableEngagementModes) {
        this.applicableEngagementModes = applicableEngagementModes;
    }
}
