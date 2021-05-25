package com.tcl.dias.serviceinventory.izosdwan.beans.versa_firewall_get_configuration;


import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "send-pcap-data"
})
public class Options {

    @JsonProperty("send-pcap-data")
    private SendPcapData sendPcapData;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("send-pcap-data")
    public SendPcapData getSendPcapData() {
        return sendPcapData;
    }

    @JsonProperty("send-pcap-data")
    public void setSendPcapData(SendPcapData sendPcapData) {
        this.sendPcapData = sendPcapData;
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
