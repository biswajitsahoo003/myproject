package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VersaUserDefinedApplications bean class
 * @author archchan
 *
 */
public class VersaUserDefinedApplications implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("app-name")
	private String name; 
//	@JsonProperty("v-proto-id")
//	private Integer vProtoId;
	@JsonProperty("family")
	private String family;
	@JsonProperty("subfamily")
	private String subfamily;
	@JsonProperty("productivity")
	private String productivity;
	@JsonProperty("risk")
	private Integer risk;
//	@JsonProperty("spack-version")
//	private Integer spackVersion;
//	@JsonProperty("latency")
//	private Integer latency;
//	@JsonProperty("jitter")
//	private Integer jitter;
//	@JsonProperty("path_mtu")
//	private Integer pathMtu;
//	@JsonProperty("bandwidth")
//	private Integer bandwidth;
//	@JsonProperty("throughput")
//	private Integer throughput;
//	@JsonProperty("pkt_loss")
//	private Integer packetLoss;
//	@JsonProperty("app-timeout")
//	private Integer appTimeout;
	@JsonProperty("tag")
	private List<String> tag;
	@JsonProperty("description")
	private String description;
	@JsonProperty("precedence")
	private Integer precedence;
	@JsonProperty("app-match-ips")
	private Boolean appMatchIps;
	@JsonProperty("app-match-rules")
	private List<VersaAppMatchedRules> appMatchRules;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public Integer getvProtoId() {
//		return vProtoId;
//	}
//	public void setvProtoId(Integer vProtoId) {
//		this.vProtoId = vProtoId;
//	}
	public String getFamily() {
		return family;
	}
	public void setFamily(String family) {
		this.family = family;
	}
	public String getSubfamily() {
		return subfamily;
	}
	public void setSubfamily(String subfamily) {
		this.subfamily = subfamily;
	}
	public String getProductivity() {
		return productivity;
	}
	public void setProductivity(String productivity) {
		this.productivity = productivity;
	}
	public Integer getRisk() {
		return risk;
	}
	public void setRisk(Integer risk) {
		this.risk = risk;
	}
//	public Integer getSpackVersion() {
//		return spackVersion;
//	}
//	public void setSpackVersion(Integer spackVersion) {
//		this.spackVersion = spackVersion;
//	}
//	public Integer getLatency() {
//		return latency;
//	}
//	public void setLatency(Integer latency) {
//		this.latency = latency;
//	}
//	public Integer getJitter() {
//		return jitter;
//	}
//	public void setJitter(Integer jitter) {
//		this.jitter = jitter;
//	}
//	public Integer getPathMtu() {
//		return pathMtu;
//	}
//	public void setPathMtu(Integer pathMtu) {
//		this.pathMtu = pathMtu;
//	}
//	public Integer getBandwidth() {
//		return bandwidth;
//	}
//	public void setBandwidth(Integer bandwidth) {
//		this.bandwidth = bandwidth;
//	}
//	public Integer getThroughput() {
//		return throughput;
//	}
//	public void setThroughput(Integer throughput) {
//		this.throughput = throughput;
//	}
//	public Integer getPacketLoss() {
//		return packetLoss;
//	}
//	public void setPacketLoss(Integer packetLoss) {
//		this.packetLoss = packetLoss;
//	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
//	public Integer getAppTimeout() {
//		return appTimeout;
//	}
//	public void setAppTimeout(Integer appTimeout) {
//		this.appTimeout = appTimeout;
//	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getPrecedence() {
		return precedence;
	}
	public void setPrecedence(Integer precedence) {
		this.precedence = precedence;
	}
	public Boolean getAppMatchIps() {
		return appMatchIps;
	}
	public void setAppMatchIps(Boolean appMatchIps) {
		this.appMatchIps = appMatchIps;
	}
	public List<VersaAppMatchedRules> getAppMatchRules() {
		return appMatchRules;
	}
	public void setAppMatchRules(List<VersaAppMatchedRules> appMatchRules) {
		this.appMatchRules = appMatchRules;
	}
	@Override
	public String toString() {
		return "VersaUserDefinedApplications [name=" + name + ", family=" + family + ", subfamily=" + subfamily
				+ ", productivity=" + productivity + ", risk=" + risk + ", tag=" + tag + ", description=" + description
				+ ", precedence=" + precedence + ", appMatchIps=" + appMatchIps + ", appMatchRules=" + appMatchRules
				+ "]";
	}
	
//	@Override
//	public String toString() {
//		return "VersaUserDefinedApplications [name=" + name + ", vProtoId=" + vProtoId + ", family=" + family
//				+ ", subfamily=" + subfamily + ", productivity=" + productivity + ", risk=" + risk + ", spackVersion="
//				+ spackVersion + ", latency=" + latency + ", jitter=" + jitter + ", pathMtu=" + pathMtu + ", bandwidth="
//				+ bandwidth + ", throughput=" + throughput + ", packetLoss=" + packetLoss + ", appTimeout=" + appTimeout
//				+ ", tag=" + tag + ", description=" + description + ", precedence=" + precedence + ", appMatchIps="
//				+ appMatchIps + ", appMatchRules=" + appMatchRules + "]";
//	}
	
	
	
	
	
	 


}
