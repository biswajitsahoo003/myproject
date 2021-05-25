package com.tcl.dias.serviceinventory.izosdwan.beans.cisco.applicationbeans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SlaEntries {
	@JsonProperty("latency")
	private String latency;
	@JsonProperty("loss")
	private String loss;
	@JsonProperty("jitter")
	private String jitter;
	public String getLatency() {
		return latency;
	}
	public void setLatency(String latency) {
		this.latency = latency;
	}
	public String getLoss() {
		return loss;
	}
	public void setLoss(String loss) {
		this.loss = loss;
	}
	public String getJitter() {
		return jitter;
	}
	public void setJitter(String jitter) {
		this.jitter = jitter;
	}
	@Override
	public String toString() {
		return "SlaEntries [latency=" + latency + ", loss=" + loss + ", jitter=" + jitter + "]";
	}

	}
