
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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "match",
    "set",
    "monitor"
})
public class Rule implements Serializable
{

    @JsonProperty("name")
    private String name;
    @JsonProperty("match")
    private Match match;
    @JsonProperty("set")
    private Set set;
    @JsonProperty("monitor")
    private Monitor monitor;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -8420020521785189723L;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("match")
    public Match getMatch() {
        return match;
    }

    @JsonProperty("match")
    public void setMatch(Match match) {
        this.match = match;
    }

    @JsonProperty("set")
    public Set getSet() {
        return set;
    }

    @JsonProperty("set")
    public void setSet(Set set) {
        this.set = set;
    }

    @JsonProperty("monitor")
    public Monitor getMonitor() {
        return monitor;
    }

    @JsonProperty("monitor")
    public void setMonitor(Monitor monitor) {
        this.monitor = monitor;
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
        if(obj instanceof Rule)
            return Objects.equals(this.name, ((Rule) obj).name);
        else return false;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

	@Override
	public String toString() {
		return "Rule [name=" + name + ", match=" + match + ", set=" + set + ", monitor=" + monitor
				+ ", additionalProperties=" + additionalProperties + "]";
	}
    
}
