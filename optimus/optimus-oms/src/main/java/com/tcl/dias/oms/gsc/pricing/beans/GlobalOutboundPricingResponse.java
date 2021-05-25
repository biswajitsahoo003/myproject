package com.tcl.dias.oms.gsc.pricing.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Pricing engine response for globaloutbound a-z prices
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GlobalOutboundPricingResponse {

    @JsonProperty("glob_display_rate")
    private GlobalOutboundDisplayRate globalOutboundDisplayRate;

    public GlobalOutboundDisplayRate getGlobalOutboundDisplayRate() {
        return globalOutboundDisplayRate;
    }

    public void setGlobalOutboundDisplayRate(GlobalOutboundDisplayRate globalOutboundDisplayRate) {
        this.globalOutboundDisplayRate = globalOutboundDisplayRate;
    }
}
