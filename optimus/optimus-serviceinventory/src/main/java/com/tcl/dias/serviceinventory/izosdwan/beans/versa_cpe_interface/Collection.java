package com.tcl.dias.serviceinventory.izosdwan.beans.versa_cpe_interface;

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
    "sdwan:wan-interfaces"
})
public class Collection {

    @JsonProperty("sdwan:wan-interfaces")
    private List<SdwanWanInterface> sdwanWanInterfaces = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("sdwan:wan-interfaces")
    public List<SdwanWanInterface> getSdwanWanInterfaces() {
        return sdwanWanInterfaces;
    }

    @JsonProperty("sdwan:wan-interfaces")
    public void setSdwanWanInterfaces(List<SdwanWanInterface> sdwanWanInterfaces) {
        this.sdwanWanInterfaces = sdwanWanInterfaces;
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
