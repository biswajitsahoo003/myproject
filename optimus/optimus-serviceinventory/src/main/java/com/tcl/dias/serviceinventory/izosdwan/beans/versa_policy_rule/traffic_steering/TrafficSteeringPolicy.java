
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Bean for receiving and mapping Traffic steering policy/rule response from Versa REST API
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "rules"
})
public class TrafficSteeringPolicy implements Serializable
{

    @JsonProperty("rules")
    private Rules_ rules = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 6935002515305267075L;

    @JsonProperty("rules")
    public Rules_ getRules() {
        return rules;
    }

    @JsonProperty("rules")
    public void setRules(Rules_ rules) {
        this.rules = rules;
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
        if(obj instanceof TrafficSteeringPolicy)
            return Objects.equals(this.rules, ((TrafficSteeringPolicy)obj).rules);
        else return false;
    }

    @Override
    public int hashCode() {
        return this.rules.hashCode();
    }
}
