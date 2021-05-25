
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the AutoPoServiceItems.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "EXT_LINE",
    "KTEXT_NUM",
    "KTEXT1",
    "QUANTITY",
    "BASE_UOM",
    "EEIND",
    "WAERS",
    "TBTWR",
    "ZZCDATE",
    "ZZDDATE"
})
public class AutoPoServiceItems {

    @JsonProperty("EXT_LINE")
    private String eXTLINE;
    @JsonProperty("KTEXT_NUM")
    private String kTEXTNUM;
    @JsonProperty("KTEXT1")
    private String kTEXT1;
    @JsonProperty("QUANTITY")
    private String qUANTITY;
    @JsonProperty("BASE_UOM")
    private String bASEUOM;
    @JsonProperty("EEIND")
    private String eEIND;
    @JsonProperty("WAERS")
    private String wAERS;
    @JsonProperty("TBTWR")
    private String tBTWR;
    @JsonProperty("ZZCDATE")
    private String zZCDATE;
    @JsonProperty("ZZDDATE")
    private String zZDDATE;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("EXT_LINE")
    public String getEXTLINE() {
        return eXTLINE;
    }

    @JsonProperty("EXT_LINE")
    public void setEXTLINE(String eXTLINE) {
        this.eXTLINE = eXTLINE;
    }

    @JsonProperty("KTEXT_NUM")
    public String getKTEXTNUM() {
        return kTEXTNUM;
    }

    @JsonProperty("KTEXT_NUM")
    public void setKTEXTNUM(String kTEXTNUM) {
        this.kTEXTNUM = kTEXTNUM;
    }

    @JsonProperty("KTEXT1")
    public String getKTEXT1() {
        return kTEXT1;
    }

    @JsonProperty("KTEXT1")
    public void setKTEXT1(String kTEXT1) {
        this.kTEXT1 = kTEXT1;
    }

    @JsonProperty("QUANTITY")
    public String getQUANTITY() {
        return qUANTITY;
    }

    @JsonProperty("QUANTITY")
    public void setQUANTITY(String qUANTITY) {
        this.qUANTITY = qUANTITY;
    }

    @JsonProperty("BASE_UOM")
    public String getBASEUOM() {
        return bASEUOM;
    }

    @JsonProperty("BASE_UOM")
    public void setBASEUOM(String bASEUOM) {
        this.bASEUOM = bASEUOM;
    }

    @JsonProperty("EEIND")
    public String getEEIND() {
        return eEIND;
    }

    @JsonProperty("EEIND")
    public void setEEIND(String eEIND) {
        this.eEIND = eEIND;
    }

    @JsonProperty("WAERS")
    public String getWAERS() {
        return wAERS;
    }

    @JsonProperty("WAERS")
    public void setWAERS(String wAERS) {
        this.wAERS = wAERS;
    }

    @JsonProperty("TBTWR")
    public String getTBTWR() {
        return tBTWR;
    }

    @JsonProperty("TBTWR")
    public void setTBTWR(String tBTWR) {
        this.tBTWR = tBTWR;
    }

    @JsonProperty("ZZCDATE")
    public String getZZCDATE() {
        return zZCDATE;
    }

    @JsonProperty("ZZCDATE")
    public void setZZCDATE(String zZCDATE) {
        this.zZCDATE = zZCDATE;
    }

    @JsonProperty("ZZDDATE")
    public String getZZDDATE() {
        return zZDDATE;
    }

    @JsonProperty("ZZDDATE")
    public void setZZDDATE(String zZDDATE) {
        this.zZDDATE = zZDDATE;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
