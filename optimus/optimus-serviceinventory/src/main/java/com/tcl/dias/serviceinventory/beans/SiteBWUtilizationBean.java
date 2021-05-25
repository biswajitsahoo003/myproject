package com.tcl.dias.serviceinventory.beans;

import java.util.Set;

/**
 * Bean for calculating bandwidth utilization in a site
 * 
 * @author Srinivasa Raghavan
 */
public class SiteBWUtilizationBean {
	private String siteName;
	private Set<String> templates;
	private String siteAlias;
	private String instanceRegion;
	private Set<String> associatedCpes;
	private String orgName;
	private Double uplinkBwUtil;
	private Double downlinkBwUtil;
	private Double totalBw;
	private Double totalUplinkBw;
	private Double totalDownlinkBw;
	private Double uplinkUtilPercent;
	private Double downlinkUtilPercent;
	private Double totalUtilPercent;
	private String bwUnit;
	private Boolean linkAvailable;
	private Set<String> interfaces;
	private Set<String> linkNames;

	public SiteBWUtilizationBean() {
		this.uplinkBwUtil = 0D;
		this.downlinkBwUtil = 0D;
		this.totalBw = 0D;
		this.uplinkUtilPercent = 0D;
		this.downlinkUtilPercent = 0D;
		this.totalUtilPercent = 0D;
		this.totalUplinkBw = 0D;
		this.totalDownlinkBw = 0D;
		this.linkAvailable = false;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Double getUplinkBwUtil() {
		return uplinkBwUtil;
	}

	public void setUplinkBwUtil(Double uplinkBwUtil) {
		this.uplinkBwUtil = uplinkBwUtil;
	}

	public Double getDownlinkBwUtil() {
		return downlinkBwUtil;
	}

	public void setDownlinkBwUtil(Double downlinkBwUtil) {
		this.downlinkBwUtil = downlinkBwUtil;
	}

	public Double getTotalBw() {
		return totalBw;
	}

	public void setTotalBw(Double totalBw) {
		this.totalBw = totalBw;
	}

	public Double getUplinkUtilPercent() {
		return uplinkUtilPercent;
	}

	public void setUplinkUtilPercent(Double uplinkUtilPercent) {
		this.uplinkUtilPercent = uplinkUtilPercent;
	}

	public Double getDownlinkUtilPercent() {
		return downlinkUtilPercent;
	}

	public void setDownlinkUtilPercent(Double downlinkUtilPercent) {
		this.downlinkUtilPercent = downlinkUtilPercent;
	}

	public String getBwUnit() {
		return bwUnit;
	}

	public void setBwUnit(String bwUnit) {
		this.bwUnit = bwUnit;
	}

	public Set<String> getTemplates() {
		return templates;
	}

	public void setTemplates(Set<String> templates) {
		this.templates = templates;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public Double getTotalUtilPercent() {
		return totalUtilPercent;
	}

	public void setTotalUtilPercent(Double totalUtilPercent) {
		this.totalUtilPercent = totalUtilPercent;
	}

	public String getSiteAlias() {
		return siteAlias;
	}

	public void setSiteAlias(String siteAlias) {
		this.siteAlias = siteAlias;
	}

	public Set<String> getAssociatedCpes() {
		return associatedCpes;
	}

	public void setAssociatedCpes(Set<String> associatedCpes) {
		this.associatedCpes = associatedCpes;
	}

	public Double getTotalUplinkBw() {
		return totalUplinkBw;
	}

	public void setTotalUplinkBw(Double totalUplinkBw) {
		this.totalUplinkBw = totalUplinkBw;
	}

	public Double getTotalDownlinkBw() {
		return totalDownlinkBw;
	}

	public void setTotalDownlinkBw(Double totalDownlinkBw) {
		this.totalDownlinkBw = totalDownlinkBw;
	}

	public String getInstanceRegion() {
		return instanceRegion;
	}

	public void setInstanceRegion(String instanceRegion) {
		this.instanceRegion = instanceRegion;
	}

	public Boolean getLinkAvailable() {
		return linkAvailable;
	}

	public void setLinkAvailable(Boolean linkAvailable) {
		this.linkAvailable = linkAvailable;
	}

	public Set<String> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(Set<String> interfaces) {
		this.interfaces = interfaces;
	}

	public Set<String> getLinkNames() {
		return linkNames;
	}

	public void setLinkNames(Set<String> linkNames) {
		this.linkNames = linkNames;
	}
}
