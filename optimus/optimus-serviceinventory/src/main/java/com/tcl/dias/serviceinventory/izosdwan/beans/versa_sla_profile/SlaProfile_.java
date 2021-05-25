
package com.tcl.dias.serviceinventory.izosdwan.beans.versa_sla_profile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "name",
    "latency",
    "loss-percentage",
    "forward-loss-percentage",
    "reverse-loss-percentage",
    "delay-variation",
    "circuit-transmit-utilization",
    "circuit-receive-utilization",
    "low-latency",
    "low-packet-loss",
    "low-forward-packet-loss",
    "low-reverse-packet-loss",
    "low-delay-variation",
    "mos-score"
})
public class SlaProfile_ implements Serializable
{

    @JsonProperty("name")
    private String name;
    @JsonProperty("latency")
    private String latency;
    @JsonProperty("loss")
    @JsonAlias({"loss","loss-percentage"})
    private String lossPercentage;
    @JsonProperty("forward-loss")
    @JsonAlias({"forward-loss","forward-loss-percentage"})
    private String forwardLossPercentage;
    @JsonProperty("reverse-loss")
    @JsonAlias({"reverse-loss","reverse-loss-percentage"})
    private String reverseLossPercentage;
    @JsonProperty("delay-variation")
    private String delayVariation;
    @JsonProperty("circuit-transmit-utilization")
    private String circuitTransmitUtilization;
    @JsonProperty("circuit-receive-utilization")
    private String circuitReceiveUtilization;
    @JsonProperty("low-latency")
    private List<Object> lowLatency = null;
    @JsonProperty("low-packet-loss")
    private List<Object> lowPacketLoss = null;
    @JsonProperty("low-forward-packet-loss")
    private List<Object> lowForwardPacketLoss = null;
    @JsonProperty("low-reverse-packet-loss")
    private List<Object> lowReversePacketLoss = null;
    @JsonProperty("low-delay-variation")
    private List<Object> lowDelayVariation = null;
    @JsonProperty("mos-score")
    private String mosScore;
    @JsonProperty("jitter")
    private String jitter;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
    private final static long serialVersionUID = -1423883087012096605L;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("latency")
    public String getLatency() {
        return latency;
    }

    @JsonProperty("latency")
    public void setLatency(String latency) {
        this.latency = latency;
    }

    @JsonProperty("loss")
    @JsonAlias({"loss","loss-percentage"})
    public String getLossPercentage() {
        return lossPercentage;
    }

    @JsonProperty("loss")
    @JsonAlias({"loss","loss-percentage"})
    public void setLossPercentage(String lossPercentage) {
        this.lossPercentage = lossPercentage;
    }

    @JsonProperty("forward-loss")
    @JsonAlias({"forward-loss","forward-loss-percentage"})
    public String getForwardLossPercentage() {
        return forwardLossPercentage;
    }

    @JsonProperty("forward-loss")
    @JsonAlias({"forward-loss","forward-loss-percentage"})
    public void setForwardLossPercentage(String forwardLossPercentage) {
        this.forwardLossPercentage = forwardLossPercentage;
    }

    @JsonProperty("reverse-loss")
    @JsonAlias({"reverse-loss","reverse-loss-percentage"})
    public String getReverseLossPercentage() {
        return reverseLossPercentage;
    }

    @JsonProperty("reverse-loss")
    @JsonAlias({"reverse-loss","reverse-loss-percentage"})
    public void setReverseLossPercentage(String reverseLossPercentage) {
        this.reverseLossPercentage = reverseLossPercentage;
    }

    @JsonProperty("delay-variation")
    public String getDelayVariation() {
        return delayVariation;
    }

    @JsonProperty("delay-variation")
    public void setDelayVariation(String delayVariation) {
        this.delayVariation = delayVariation;
    }

    @JsonProperty("circuit-transmit-utilization")
    public String getCircuitTransmitUtilization() {
        return circuitTransmitUtilization;
    }

    @JsonProperty("circuit-transmit-utilization")
    public void setCircuitTransmitUtilization(String circuitTransmitUtilization) {
        this.circuitTransmitUtilization = circuitTransmitUtilization;
    }

    @JsonProperty("circuit-receive-utilization")
    public String getCircuitReceiveUtilization() {
        return circuitReceiveUtilization;
    }

    @JsonProperty("circuit-receive-utilization")
    public void setCircuitReceiveUtilization(String circuitReceiveUtilization) {
        this.circuitReceiveUtilization = circuitReceiveUtilization;
    }

    @JsonProperty("low-latency")
    public List<Object> getLowLatency() {
        return lowLatency;
    }

    @JsonProperty("low-latency")
    public void setLowLatency(List<Object> lowLatency) {
        this.lowLatency = lowLatency;
    }

    @JsonProperty("low-packet-loss")
    public List<Object> getLowPacketLoss() {
        return lowPacketLoss;
    }

    @JsonProperty("low-packet-loss")
    public void setLowPacketLoss(List<Object> lowPacketLoss) {
        this.lowPacketLoss = lowPacketLoss;
    }

    @JsonProperty("low-forward-packet-loss")
    public List<Object> getLowForwardPacketLoss() {
        return lowForwardPacketLoss;
    }

    @JsonProperty("low-forward-packet-loss")
    public void setLowForwardPacketLoss(List<Object> lowForwardPacketLoss) {
        this.lowForwardPacketLoss = lowForwardPacketLoss;
    }

    @JsonProperty("low-reverse-packet-loss")
    public List<Object> getLowReversePacketLoss() {
        return lowReversePacketLoss;
    }

    @JsonProperty("low-reverse-packet-loss")
    public void setLowReversePacketLoss(List<Object> lowReversePacketLoss) {
        this.lowReversePacketLoss = lowReversePacketLoss;
    }

    @JsonProperty("low-delay-variation")
    public List<Object> getLowDelayVariation() {
        return lowDelayVariation;
    }

    @JsonProperty("low-delay-variation")
    public void setLowDelayVariation(List<Object> lowDelayVariation) {
        this.lowDelayVariation = lowDelayVariation;
    }

    @JsonProperty("mos-score")
    public String getMosScore() {
        return mosScore;
    }

    @JsonProperty("mos-score")
    public void setMosScore(String mosScore) {
        this.mosScore = mosScore;
    }

    @JsonProperty("jitter")
    public String getJitter() {
        return jitter;
    }

    @JsonProperty("jitter")
    public void setJitter(String jitter) {
        this.jitter = jitter;
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
		return "SlaProfile_ [name=" + name + ", latency=" + latency + ", lossPercentage=" + lossPercentage
				+ ", forwardLossPercentage=" + forwardLossPercentage + ", reverseLossPercentage="
				+ reverseLossPercentage + ", delayVariation=" + delayVariation + ", circuitTransmitUtilization="
				+ circuitTransmitUtilization + ", circuitReceiveUtilization=" + circuitReceiveUtilization
				+ ", lowLatency=" + lowLatency + ", lowPacketLoss=" + lowPacketLoss + ", lowForwardPacketLoss="
				+ lowForwardPacketLoss + ", lowReversePacketLoss=" + lowReversePacketLoss + ", lowDelayVariation="
				+ lowDelayVariation + ", mosScore=" + mosScore + ", jitter=" + jitter + ", additionalProperties="
				+ additionalProperties + "]";
	}
    
    

}
