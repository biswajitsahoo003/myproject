package com.tcl.dias.oms.gsc.pricing.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * LNS Price format bean
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"origin", "dest", "currency", "NRC", "MRC", "ARC", "term_name", "price", "surcharge_amt",
        "surcharge_curr"})
public class LnsPriceBean {

    @JsonProperty("origin")
    private String origin;

    @JsonProperty("dest")
    private String dest;

    @JsonProperty("currency")
    private String currency;

    @JsonProperty("NRC")
    private String nrc;

    @JsonProperty("ARC")
    private String arc;

    @JsonProperty("MRC")
    private String mrc;

    @JsonProperty("term_name")
    private List<String> terminationName;

    @JsonProperty("price")
    private List<String> price;

    @JsonProperty("surcharge_amt")
    private List<String> surchargeAmount;

    @JsonProperty("surcharge_curr")
    private List<String> surchargeCurrency;

    @JsonProperty("origin")
    public String getOrigin() {
        return origin;
    }

    @JsonProperty("origin")
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    @JsonProperty("dest")
    public String getDest() {
        return dest;
    }

    @JsonProperty("dest")
    public void setDest(String dest) {
        this.dest = dest;
    }

    @JsonProperty("currency")
    public String getCurrency() {
        return currency;
    }

    @JsonProperty("currency")
    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @JsonProperty("NRC")
    public String getNrc() {
        return nrc;
    }

    @JsonProperty("NRC")
    public void setNrc(String nrc) {
        this.nrc = nrc;
    }

    @JsonProperty("ARC")
    public String getArc() {
        return arc;
    }

    @JsonProperty("ARC")
    public void setArc(String arc) {
        this.arc = arc;
    }

    @JsonProperty("MRC")
    public String getMrc() {
        return mrc;
    }

    @JsonProperty("MRC")
    public void setMrc(String mrc) {
        this.mrc = mrc;
    }

    @JsonProperty("term_name")
    public List<String> getTerminationName() {
        return terminationName;
    }

    @JsonProperty("term_name")
    public void setTerminationName(List<String> terminationName) {
        this.terminationName = terminationName;
    }

    @JsonProperty("price")
    public List<String> getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(List<String> price) {
        this.price = price;
    }

    @JsonProperty("surcharge_amt")
    public List<String> getSurchargeAmount() {
        return surchargeAmount;
    }

    @JsonProperty("surcharge_amt")
    public void setSurchargeAmount(List<String> surchargeAmount) {
        this.surchargeAmount = surchargeAmount;
    }

    @JsonProperty("surcharge_curr")
    public List<String> getSurchargeCurrency() {
        return surchargeCurrency;
    }

    @JsonProperty("surcharge_curr")
    public void setSurchargeCurrency(List<String> surchargeCurrency) {
        this.surchargeCurrency = surchargeCurrency;
    }

}
