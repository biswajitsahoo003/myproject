package com.tcl.dias.serviceactivation.integratemux.beans;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"NodeFieldName", "NodeIP", "PEstarttime", "PEendtime", "Requestid", "OptimusPEid", "EOR",
        "RequestingSystem", "RequestTime"})
public class NodeInfoRequestBean {

    @JsonProperty("NodeFieldName")
    private String nodeFieldName;
    @JsonProperty("NodeIP")
    private String nodeIP;
    @JsonProperty("PEstarttime")
    private String pEstarttime;
    @JsonProperty("PEendtime")
    private String pEendtime;
    @JsonProperty("Requestid")
    private String requestid;
    @JsonProperty("OptimusPEid")
    private String optimusPEid;
    @JsonProperty("EOR")
    private String eOR;
    @JsonProperty("RequestingSystem")
    private String requestingSystem;
    @JsonProperty("RequestTime")
    private String requestTime;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("NodeFieldName")
    public String getNodeFieldName() {
        return nodeFieldName;
    }

    @JsonProperty("NodeFieldName")
    public void setNodeFieldName(String nodeFieldName) {
        this.nodeFieldName = nodeFieldName;
    }

    @JsonProperty("NodeIP")
    public String getNodeIP() {
        return nodeIP;
    }

    @JsonProperty("NodeIP")
    public void setNodeIP(String nodeIP) {
        this.nodeIP = nodeIP;
    }

    @JsonProperty("PEstarttime")
    public String getPEstarttime() {
        return pEstarttime;
    }

    @JsonProperty("PEstarttime")
    public void setPEstarttime(String pEstarttime) {
        this.pEstarttime = pEstarttime;
    }

    @JsonProperty("PEendtime")
    public String getPEendtime() {
        return pEendtime;
    }

    @JsonProperty("PEendtime")
    public void setPEendtime(String pEendtime) {
        this.pEendtime = pEendtime;
    }

    @JsonProperty("Requestid")
    public String getRequestid() {
        return requestid;
    }

    @JsonProperty("Requestid")
    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    @JsonProperty("OptimusPEid")
    public String getOptimusPEid() {
        return optimusPEid;
    }

    @JsonProperty("OptimusPEid")
    public void setOptimusPEid(String optimusPEid) {
        this.optimusPEid = optimusPEid;
    }

    @JsonProperty("EOR")
    public String getEOR() {
        return eOR;
    }

    @JsonProperty("EOR")
    public void setEOR(String eOR) {
        this.eOR = eOR;
    }

    @JsonProperty("RequestingSystem")
    public String getRequestingSystem() {
        return requestingSystem;
    }

    @JsonProperty("RequestingSystem")
    public void setRequestingSystem(String requestingSystem) {
        this.requestingSystem = requestingSystem;
    }

    @JsonProperty("RequestTime")
    public String getRequestTime() {
        return requestTime;
    }

    @JsonProperty("RequestTime")
    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
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
    public String toString() {
        return "NodeInfoRequestBean{" +
                "nodeFieldName='" + nodeFieldName + '\'' +
                ", nodeIP='" + nodeIP + '\'' +
                ", pEstarttime='" + pEstarttime + '\'' +
                ", pEendtime='" + pEendtime + '\'' +
                ", requestid='" + requestid + '\'' +
                ", optimusPEid='" + optimusPEid + '\'' +
                ", eOR='" + eOR + '\'' +
                ", requestingSystem='" + requestingSystem + '\'' +
                ", requestTime='" + requestTime + '\'' +
                ", additionalProperties=" + additionalProperties +
                '}';
    }
}