
package com.tcl.dias.sfdc.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
    "commonWrapInput",
    "opptyWrap"
})
public class SfdcPartnerOpportunityRequest extends  BaseBean{

    @JsonProperty("commonWrapInput")
    private SfdcPartnerCommonWrapInput sfdcPartnerCommonWrapInput;

    @JsonProperty("opptyWrap")
    private List<SfdcPartnerOpportunityWrap> sfdcPartnerOpportunityWrap;

    @JsonProperty("commonWrapInput")
    public SfdcPartnerCommonWrapInput getSfdcPartnerCommonWrapInput() {
        return sfdcPartnerCommonWrapInput;
    }
    @JsonProperty("commonWrapInput")
    public void setSfdcPartnerCommonWrapInput(SfdcPartnerCommonWrapInput sfdcPartnerCommonWrapInput) {
        this.sfdcPartnerCommonWrapInput = sfdcPartnerCommonWrapInput;
    }
    @JsonProperty("opptyWrap")
    public List<SfdcPartnerOpportunityWrap> getSfdcPartnerOpportunityWrap() {
        return sfdcPartnerOpportunityWrap;
    }
    @JsonProperty("opptyWrap")
    public void setSfdcPartnerOpportunityWrap(List<SfdcPartnerOpportunityWrap> sfdcPartnerOpportunityWrap) {
        this.sfdcPartnerOpportunityWrap = sfdcPartnerOpportunityWrap;
    }

    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
