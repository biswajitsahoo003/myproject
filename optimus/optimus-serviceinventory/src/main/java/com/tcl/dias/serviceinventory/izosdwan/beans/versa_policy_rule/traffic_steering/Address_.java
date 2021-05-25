
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_policy_rule.traffic_steering;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "address-list",
    "address-group-list"
})
public class Address_ implements Serializable
{

    @JsonProperty("address-list")
    private List<String> addressList = null;
    @JsonProperty("address-group-list")
    private List<String> addressGroupList = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -7073463299037953974L;

    @JsonProperty("address-list")
    public List<String> getAddressList() {
        return addressList;
    }

    @JsonProperty("address-list")
    public void setAddressList(List<String> addressList) {
        this.addressList = addressList;
    }

    @JsonProperty("address-group-list")
    public List<String> getAddressGroupList() {
        return addressGroupList;
    }

    @JsonProperty("address-group-list")
    public void setAddressGroupList(List<String> addressGroupList) {
        this.addressGroupList = addressGroupList;
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
