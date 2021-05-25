package com.tcl.dias.serviceinventory.beans;

import java.io.Serializable;
import java.util.List;

import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;

/**
 * Bean class for SDWAN application
 * @author archchan
 *
 */
public class SdwanApplicationsBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String applicationName;
	private String applicationType;
	private String applicationCategory;
	private Integer risk;
	private Integer precedenceTag;
	private String organisationName;
	private String templateName;
	private Integer serviceDetailId;
	private String serviceId;
	private List<String> tag;
	private String description;
	private Integer appTimeout;
	private Boolean appMatchIps;
	private List<VersaAppMatchedRules> appMatchRules;
	private Integer protoId;
	private Integer spackVersion;
	private Integer latency;
	private Integer jitter;
	private Integer pathMtu;
	private Integer bandwidth;
	private Integer throughput;
	private Integer packetLoss;
	private Integer tcpTimeout;
	private String productivity;
	private String sourceIp;
	private Integer sourcePort;
	private String destinationIp;
	private Integer destinationPort;
	private String hostPattern;
	private String directoryRegion;
	private String mappedCategory;
	
	
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getApplicationType() {
		return applicationType;
	}
	public void setApplicationType(String applicationType) {
		this.applicationType = applicationType;
	}
	public String getApplicationCategory() {
		return applicationCategory;
	}
	public void setApplicationCategory(String applicationCategory) {
		this.applicationCategory = applicationCategory;
	}
	public Integer getRisk() {
		return risk;
	}
	public void setRisk(Integer risk) {
		this.risk = risk;
	}
	public Integer getPrecedenceTag() {
		return precedenceTag;
	}
	public void setPrecedenceTag(Integer precedenceTag) {
		this.precedenceTag = precedenceTag;
	}
	public String getOrganisationName() {
		return organisationName;
	}
	public void setOrganisationName(String organisationName) {
		this.organisationName = organisationName;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public Integer getServiceDetailId() {
		return serviceDetailId;
	}
	public void setServiceDetailId(Integer serviceDetailId) {
		this.serviceDetailId = serviceDetailId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	
	public List<String> getTag() {
		return tag;
	}
	public void setTag(List<String> tag) {
		this.tag = tag;
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getAppTimeout() {
		return appTimeout;
	}
	public void setAppTimeout(Integer appTimeout) {
		this.appTimeout = appTimeout;
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
	public Integer getProtoId() {
		return protoId;
	}
	public void setProtoId(Integer protoId) {
		this.protoId = protoId;
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
	public String getProductivity() {
		return productivity;
	}
	public void setProductivity(String productivity) {
		this.productivity = productivity;
	}
	public String getSourceIp() {
		return sourceIp;
	}
	public void setSourceIp(String sourceIp) {
		this.sourceIp = sourceIp;
	}
	public Integer getSourcePort() {
		return sourcePort;
	}
	public void setSourcePort(Integer sourcePort) {
		this.sourcePort = sourcePort;
	}
	public String getDestinationIp() {
		return destinationIp;
	}
	public void setDestinationIp(String destinationIp) {
		this.destinationIp = destinationIp;
	}
	public Integer getDestinationPort() {
		return destinationPort;
	}
	public void setDestinationPort(Integer destinationPort) {
		this.destinationPort = destinationPort;
	}
	public String getHostPattern() {
		return hostPattern;
	}
	public void setHostPattern(String hostPattern) {
		this.hostPattern = hostPattern;
	}
	public String getDirectoryRegion() {
		return directoryRegion;
	}
	public void setDirectoryRegion(String directoryRegion) {
		this.directoryRegion = directoryRegion;
	}
	public SdwanApplicationsBean() {
		
	}	
	public String getMappedCategory() {
		return mappedCategory;
	}
	public void setMappedCategory(String mappedCategory) {
		this.mappedCategory = mappedCategory;
	}
	public SdwanApplicationsBean(VersaPredefinedApplications preDefinedApps) {
		this.applicationName = preDefinedApps.getName();
		this.applicationCategory = preDefinedApps.getTag() != null? preDefinedApps.getTag().get(0):null;
		this.applicationType = ServiceInventoryConstants.BUILT_IN;
		this.bandwidth = preDefinedApps.getBandwidth();
		this.jitter = preDefinedApps.getJitter();
		this.latency = preDefinedApps.getLatency();
		this.packetLoss = preDefinedApps.getPacketLoss();
		this.pathMtu = preDefinedApps.getPathMtu();
		this.protoId = preDefinedApps.getvProtoId();
		this.productivity = preDefinedApps.getProductivity();
		this.risk = preDefinedApps.getRisk();
		this.spackVersion = preDefinedApps.getSpackVersion();
		this.tag = preDefinedApps.getTag();
		this.tcpTimeout = preDefinedApps.getTcpTimeout();
		this.throughput = preDefinedApps.getThroughput();
		
		if(preDefinedApps.getTag().contains("v_business")) {
			this.mappedCategory = "v_business";
			this.applicationCategory = "v_business";
		} else if(preDefinedApps.getTag().contains("v_non_business")) {
			this.mappedCategory = "v_non_business";
			this.applicationCategory = "v_non_business";
		} else {
			this.applicationCategory = "others";
			this.mappedCategory = preDefinedApps.getTag().get(0);
		}
		
	}
	
	public SdwanApplicationsBean(VersaUserDefinedApplications userDefinedResp, String templateName, String orgName, Integer sysId, String instanceRegion) {
		this.applicationName = userDefinedResp.getName();
		this.applicationType = ServiceInventoryConstants.USER_DEFINED;
//		this.bandwidth = userDefinedResp.getBandwidth();
//		this.jitter = userDefinedResp.getJitter();
//		this.latency = userDefinedResp.getLatency();
//		this.packetLoss = userDefinedResp.getPacketLoss();
//		this.pathMtu = userDefinedResp.getPathMtu();
//		this.protoId = userDefinedResp.getvProtoId();
		this.productivity = userDefinedResp.getProductivity();
		this.risk = userDefinedResp.getRisk();
//		this.spackVersion = userDefinedResp.getSpackVersion();
		this.tag = userDefinedResp.getTag();
		if(userDefinedResp.getTag().contains("v_business")) {
			this.mappedCategory = "v_business";
			this.applicationCategory = "v_business";
		} else if(userDefinedResp.getTag().contains("v_non_business")) {
			this.mappedCategory = "v_non_business";
			this.applicationCategory = "v_non_business";
		} else {
			this.applicationCategory = "others";
			this.mappedCategory = userDefinedResp.getTag().get(0);
		}
		
//		this.tcpTimeout = userDefinedResp.getAppTimeout();
//		this.throughput = userDefinedResp.getThroughput();
		this.precedenceTag = userDefinedResp.getPrecedence();
		this.appMatchIps = userDefinedResp.getAppMatchIps();
		this.appMatchRules = userDefinedResp.getAppMatchRules();
		this.templateName = templateName;
		this.organisationName = orgName;
		this.serviceDetailId = sysId;
		this.directoryRegion = instanceRegion;
		if(userDefinedResp.getAppMatchRules() != null && !userDefinedResp.getAppMatchRules().isEmpty()) {
			this.sourceIp = userDefinedResp.getAppMatchRules().get(0).getSourcePrefix();
			this.destinationIp = userDefinedResp.getAppMatchRules().get(0).getDestinationPrefix();
			this.hostPattern = userDefinedResp.getAppMatchRules().get(0).getHostPattern();
			if(userDefinedResp.getAppMatchRules().get(0).getSourcePort() != null) {
				this.sourcePort = userDefinedResp.getAppMatchRules().get(0).getSourcePort().getValue();
			}	
			if(userDefinedResp.getAppMatchRules().get(0).getDestinationPort() != null) {
				this.destinationPort = userDefinedResp.getAppMatchRules().get(0).getDestinationPort().getValue();
			}		
		}
		this.description = userDefinedResp.getDescription();
		
	}
	@Override
	public String toString() {
		return "SdwanApplicationsBean [applicationName=" + applicationName + ", applicationType=" + applicationType
				+ ", applicationCategory=" + applicationCategory + ", risk=" + risk + ", precedenceTag=" + precedenceTag
				+ ", organisationName=" + organisationName + ", templateName=" + templateName + ", serviceDetailId="
				+ serviceDetailId + ", serviceId=" + serviceId + ", tag=" + tag + ", description=" + description
				+ ", appTimeout=" + appTimeout + ", appMatchIps=" + appMatchIps + ", appMatchRules=" + appMatchRules
				+ ", protoId=" + protoId + ", spackVersion=" + spackVersion + ", latency=" + latency + ", jitter="
				+ jitter + ", pathMtu=" + pathMtu + ", bandwidth=" + bandwidth + ", throughput=" + throughput
				+ ", packetLoss=" + packetLoss + ", tcpTimeout=" + tcpTimeout + ", productivity=" + productivity
				+ ", sourceIp=" + sourceIp + ", sourcePort=" + sourcePort + ", destinationIp=" + destinationIp
				+ ", destinationPort=" + destinationPort + ", hostPattern=" + hostPattern + ", directoryRegion="
				+ directoryRegion + ", mappedCategory=" + mappedCategory + "]";
	}
	
	
	

}
