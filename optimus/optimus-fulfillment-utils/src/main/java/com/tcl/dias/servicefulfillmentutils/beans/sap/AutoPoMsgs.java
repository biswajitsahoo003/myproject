
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/**
 * 
 * This file contains the AutoPoMsgs.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "HEADER",
    "LINE_ITEMS"
})
public class AutoPoMsgs {

    @JsonProperty("HEADER")
    private AutoPoHeader hEADER;
    @JsonProperty("LINE_ITEMS")
    private List<AutoPoLineItems> lINEITEMS;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("HEADER")
    public AutoPoHeader getHEADER() {
        return hEADER;
    }

    @JsonProperty("HEADER")
    public void setHEADER(AutoPoHeader hEADER) {
        this.hEADER = hEADER;
    }

    @JsonProperty("LINE_ITEMS")
    public List<AutoPoLineItems> getLINEITEMS() {
        return lINEITEMS;
    }

    @JsonProperty("LINE_ITEMS")
    public void setLINEITEMS(List<AutoPoLineItems> lINEITEMS) {
        this.lINEITEMS = lINEITEMS;
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
