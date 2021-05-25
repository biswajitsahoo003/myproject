package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * VersaPredefinedApplications class
 * @author archchan
 *
 */
public class VersaPredefinedApplications implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("name")
	private String name; 
	@JsonProperty("v-proto-id")
	private Integer vProtoId;
	@JsonProperty("family")
	private String family;
	@JsonProperty("subfamily")
	private String subfamily;
	@JsonProperty("productivity")
	private String productivity;
	@JsonProperty("risk")
	private Integer risk;
	@JsonProperty("spack-version")
	private Integer spackVersion;
	@JsonProperty("latency")
	private Integer latency;
	@JsonProperty("jitter")
	private Integer jitter;
	@JsonProperty("path_mtu")
	private Integer pathMtu;
	@JsonProperty("bandwidth")
	private Integer bandwidth;
	@JsonProperty("throughput")
	private Integer throughput;
	@JsonProperty("pkt_loss")
	private Integer packetLoss;
	@JsonProperty("tcp_timeout")
	private Integer tcpTimeout;
	@JsonProperty("tag")
	private List<String> tag;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getvProtoId() {
		return vProtoId;
	}
	public void setvProtoId(Integer vProtoId) {
		this.vProtoId = vProtoId;
	}
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
	public Integer getSpackVersion() {
		return spackVersion;
	}
	public void setSpackVersion(Integer spackVersion) {
		this.spackVersion = spackVersion;
	}
	public Integer getLatency() {
		return latency;
	}
	public void setLatency(Integer latency) {
		this.latency = latency;
	}
	public Integer getJitter() {
		return jitter;
	}
	public void setJitter(Integer jitter) {
		this.jitter = jitter;
	}
	public Integer getPathMtu() {
		return pathMtu;
	}
	public void setPathMtu(Integer pathMtu) {
		this.pathMtu = pathMtu;
	}
	public Integer getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}
	public Integer getThroughput() {
		return throughput;
	}
	public void setThroughput(Integer throughput) {
		this.throughput = throughput;
	}
	public Integer getPacketLoss() {
		return packetLoss;
	}
	public void setPacketLoss(Integer packetLoss) {
		this.packetLoss = packetLoss;
	}
	public Integer getTcpTimeout() {
		return tcpTimeout;
	}
	public void setTcpTimeout(Integer tcpTimeout) {
		this.tcpTimeout = tcpTimeout;
	}
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	@Override
	public String toString() {
		return "VersaPredefinedApplications [name=" + name + ", vProtoId=" + vProtoId + ", family=" + family
				+ ", subfamily=" + subfamily + ", productivity=" + productivity + ", risk=" + risk + ", spackVersion="
				+ spackVersion + ", latency=" + latency + ", jitter=" + jitter + ", pathMtu=" + pathMtu + ", bandwidth="
				+ bandwidth + ", throughput=" + throughput + ", packetLoss=" + packetLoss + ", tcpTimeout=" + tcpTimeout
				+ ", tag=" + tag + ", getName()=" + getName() + ", getvProtoId()=" + getvProtoId() + ", getFamily()="
				+ getFamily() + ", getSubfamily()=" + getSubfamily() + ", getProductivity()=" + getProductivity()
				+ ", getRisk()=" + getRisk() + ", getSpackVersion()=" + getSpackVersion() + ", getLatency()="
				+ getLatency() + ", getJitter()=" + getJitter() + ", getPathMtu()=" + getPathMtu() + ", getBandwidth()="
				+ getBandwidth() + ", getThroughput()=" + getThroughput() + ", getPacketLoss()=" + getPacketLoss()
				+ ", getTcpTimeout()=" + getTcpTimeout() + ", getTag()=" + getTag() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	} 
	
	 


}
