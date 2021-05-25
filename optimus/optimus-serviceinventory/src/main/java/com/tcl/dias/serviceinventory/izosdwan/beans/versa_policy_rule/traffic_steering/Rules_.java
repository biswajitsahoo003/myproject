
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "rule",
        "statistics"
})
public class Rules_ implements Serializable
{

    @JsonProperty("rule")
    private List<Rule> rule = null;
    @JsonProperty("statistics")
    private Statistics statistics;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 3604200868769948976L;

    @JsonProperty("rule")
    public List<Rule> getRule() {
        return rule;
    }

    @JsonProperty("rule")
    public void setRule(List<Rule> rule) {
        this.rule = rule;
    }

    @JsonProperty("statistics")
    public Statistics getStatistics() {
        return statistics;
    }

    @JsonProperty("statistics")
    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Rules_)
            return Objects.equals(this.rule, ((Rules_)obj).rule);
        else return false;
    }

    @Override
    public int hashCode() {
        return this.rule.hashCode();
    }
}