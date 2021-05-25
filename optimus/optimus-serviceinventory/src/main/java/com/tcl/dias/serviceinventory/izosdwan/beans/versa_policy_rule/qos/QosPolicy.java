
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.qos;

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
import com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering.Rules_;

/**
 * Bean for receiving and mapping QoS policy response from Versa REST API
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "app-qos-policy"
})
public class QosPolicy implements Serializable
{

    @JsonProperty("app-qos-policy")
    private List<AppQosPolicy> appQosPolicy = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = 8305615854957142475L;

    @JsonProperty("app-qos-policy")
    public List<AppQosPolicy> getAppQosPolicy() {
        return appQosPolicy;
    }

    @JsonProperty("app-qos-policy")
    public void setAppQosPolicy(List<AppQosPolicy> appQosPolicy) {
        this.appQosPolicy = appQosPolicy;
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
        if(obj instanceof QosPolicy)
            return Objects.equals(this.appQosPolicy, ((QosPolicy)obj).appQosPolicy);
        else return false;
    }

    @Override
    public int hashCode() {
        return this.appQosPolicy.hashCode();
    }
}
