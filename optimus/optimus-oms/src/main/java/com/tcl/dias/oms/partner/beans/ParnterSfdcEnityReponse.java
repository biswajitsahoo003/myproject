package com.tcl.dias.oms.partner.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.common.beans.PartnerLeCustomerLe;
import com.tcl.dias.common.beans.PartnerLegalEntityBean;

/**
 * ParnterSfdcEnityReponse with relevant data of sales,
 *
 * @author Arunmani
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonPropertyOrder({"salesData", "currency", "applicableProducts", "applicablePartnerLegalEntities", "applicableEngagementModes"})
public class ParnterSfdcEnityReponse {

    @JsonProperty("salesData")
    Map<String, Map<String, SfdcSalesFunnelData>> salesData = new HashMap<>();

    @JsonProperty("currency")
    String currency;

    @JsonProperty("applicableProducts")
    List<String> applicableProducts;

    @JsonProperty("applicablePartnerLeAndCustomerLe")
    List<PartnerLeCustomerLe> applicablePartnerLeAndCustomerLe;

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

    public List<PartnerLeCustomerLe> getApplicablePartnerLeAndCustomerLe() {
        return applicablePartnerLeAndCustomerLe;
    }

    public void setApplicablePartnerLeAndCustomerLe(List<PartnerLeCustomerLe> applicablePartnerLeAndCustomerLe) {
        this.applicablePartnerLeAndCustomerLe = applicablePartnerLeAndCustomerLe;
    }


    public List<String> getApplicableEngagementModes() {
        return applicableEngagementModes;
    }

    public void setApplicableEngagementModes(List<String> applicableEngagementModes) {
        this.applicableEngagementModes = applicableEngagementModes;
    }
}
