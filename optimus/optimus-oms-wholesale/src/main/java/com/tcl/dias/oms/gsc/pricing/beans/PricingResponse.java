package com.tcl.dias.oms.gsc.pricing.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

/**
 * Response bean for Pricing
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"itfs_price", "lns_price", "uifn_price", "dtfs_price", "acns_price", "dom_voice_price",
        "glob_out_price", "uifn_reg_charge", "uifn_reg_curr", "price_slab"})
public class PricingResponse {

    @JsonProperty("itfs_price")
    private List<ItfsAndUifnPriceBean> itfsPrice;

    @JsonProperty("lns_price")
    private List<LnsPriceBean> lnsPrice;

    @JsonProperty("uifn_price")
    private List<ItfsAndUifnPriceBean> uifnPrice;

    @JsonProperty("uifn_reg_charge")
    private String uifnRegCharge;

    @JsonProperty("uifn_reg_curr")
    private String uifnRegCurr;

    @JsonProperty("price_slab")
    private String priceSlab;

    @JsonProperty("dtfs_price")
    private DtfsAndAcnsPriceBean dtfsPrice;

    @JsonProperty("acns_price")
    private DtfsAndAcnsPriceBean acnsPrice;

    @JsonProperty("dom_voice_price")
    private DomesticVoicePriceBean domesticVoicePrice;

    @JsonProperty("glob_out_price")
    private List<GlobalOutboundPriceBean> globalOutboundPrice;

    @JsonProperty("itfs_price")
    public List<ItfsAndUifnPriceBean> getItfsPrice() {
        return itfsPrice;
    }

    @JsonProperty("itfs_price")
    public void setItfsPrice(List<ItfsAndUifnPriceBean> itfsPrice) {
        this.itfsPrice = itfsPrice;
    }

    @JsonProperty("lns_price")
    public List<LnsPriceBean> getLnsPrice() {
        return lnsPrice;
    }

    @JsonProperty("lns_price")
    public void setLnsPrice(List<LnsPriceBean> lnsPrice) {
        this.lnsPrice = lnsPrice;
    }

    @JsonProperty("uifn_price")
    public List<ItfsAndUifnPriceBean> getUifnPrice() {
        return uifnPrice;
    }

    @JsonProperty("uifn_price")
    public void setUifnPrice(List<ItfsAndUifnPriceBean> uifnPrice) {
        this.uifnPrice = uifnPrice;
    }

    @JsonProperty("uifn_reg_charge")
    public String getUifnRegCharge() {
        return uifnRegCharge;
    }

    @JsonProperty("uifn_reg_charge")
    public void setUifnRegCharge(String uifnRegCharge) {
        this.uifnRegCharge = uifnRegCharge;
    }

    @JsonProperty("uifn_reg_curr")
    public String getUifnRegCurr() {
        return uifnRegCurr;
    }

    @JsonProperty("uifn_reg_curr")
    public void setUifnRegCurr(String uifnRegCurr) {
        this.uifnRegCurr = uifnRegCurr;
    }

    @JsonProperty("price_slab")
    public String getPriceSlab() {
        return priceSlab;
    }

    @JsonProperty("price_slab")
    public void setPriceSlab(String priceSlab) {
        this.priceSlab = priceSlab;
    }

    @JsonProperty("dtfs_price")
    public DtfsAndAcnsPriceBean getDtfsPrice() {
        return dtfsPrice;
    }

    @JsonProperty("dtfs_price")
    public void setDtfsPrice(DtfsAndAcnsPriceBean dtfsPrice) {
        this.dtfsPrice = dtfsPrice;
    }

    @JsonProperty("acns_price")
    public DtfsAndAcnsPriceBean getAcnsPrice() {
        return acnsPrice;
    }

    @JsonProperty("acns_price")
    public void setAcnsPrice(DtfsAndAcnsPriceBean acnsPrice) {
        this.acnsPrice = acnsPrice;
    }

    @JsonProperty("dom_voice_price")
    public DomesticVoicePriceBean getDomesticVoicePrice() {
        return domesticVoicePrice;
    }

    @JsonProperty("dom_voice_price")
    public void setDomesticVoicePrice(DomesticVoicePriceBean domesticVoicePrice) {
        this.domesticVoicePrice = domesticVoicePrice;
    }

    @JsonProperty("glob_out_price")
    public List<GlobalOutboundPriceBean> getGlobalOutboundPrice() {
        return globalOutboundPrice;
    }

    @JsonProperty("glob_out_price")
    public void setGlobalOutboundPrice(List<GlobalOutboundPriceBean> globalOutboundPrice) {
        this.globalOutboundPrice = globalOutboundPrice;
    }

    @Override
    public String toString() {
        return "PricingResponse [itfsPrice=" + itfsPrice + ", lnsPrice=" + lnsPrice + ", uifnPrice=" + uifnPrice
                + ", uifnRegCharge=" + uifnRegCharge + ", uifnRegCurr=" + uifnRegCurr + ", priceSlab=" + priceSlab
                + ", dtfsPrice=" + dtfsPrice + ", acnsPrice=" + acnsPrice + ", domesticVoicePrice=" + domesticVoicePrice
                + ", globalOutboundPrice=" + globalOutboundPrice + "]";
    }

}
