
package com.tcl.dias.oms.ipc.beans.pricebean;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "quotes"
})
public class PricingBean {

    @JsonProperty("quotes")
    private List<Quote> quotes = new ArrayList<>();

    private String vmPricingResponse = null;

    private String errorResponse = null;

    @JsonProperty("quotes")
    public List<Quote> getQuotes() {
        return quotes;
    }

    @JsonProperty("quotes")
    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public String getVmPricingResponse() {
        return vmPricingResponse;
    }

    public void setVmPricingResponse(String vmPricingResponse) {
        this.vmPricingResponse = vmPricingResponse;
    }

    public String getErrorResponse() {
        return errorResponse;
    }

    public void setErrorResponse(String errorResponse) {
        this.errorResponse = errorResponse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("quotes", quotes).toString();
    }

}
