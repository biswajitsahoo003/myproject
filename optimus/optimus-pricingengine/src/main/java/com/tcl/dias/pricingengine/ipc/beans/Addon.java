
package com.tcl.dias.pricingengine.ipc.beans;

import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 
 * This file contains the Addon.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "itemId", "vdomcount", "ipcount", "backupsize", "backuplocation", "nrc", "mrc", "priceBreakup", 
	"clientToSiteVpnQuantity", "siteToSiteVpnQuantity", "dbLicenseQuantity", "drLicenseQuantity", "hybridConnections" })
public class Addon {

	@JsonProperty("itemId")
	private String itemId;
	
	@JsonProperty("vdomcount")
	private Integer vdomcount;
	
	@JsonProperty("ipcount")
	private Integer ipcount;
	
	@JsonProperty("backupsize")
	private String backupsize;
	
	@JsonProperty("backuplocation")
	private String backuplocation;
	
	@JsonProperty("clientToSiteVpnQuantity")
	private Integer clientToSiteVpnQuantity;
	
	@JsonProperty("siteToSiteVpnQuantity")
	private Integer siteToSiteVpnQuantity;
	
	@JsonProperty("dbLicenseQuantity")
	private Map<String, Map<String, Integer>> dbLicenseQuantity = new TreeMap<>();
	
	@JsonProperty("drLicenseQuantity")
	private Map<String, Map<String, String>> drLicenseQuantity = new TreeMap<>();
	
	@JsonProperty("hybridConnections")
	private Map<String, Map<String, String>> hybridConnections = new TreeMap<>();
	
	@JsonProperty("nrc")
	private Double nrc;
	
	@JsonProperty("mrc")
	private Double mrc;

	@JsonProperty("priceBreakup")
	private Map<String, Component> priceBreakup = new TreeMap<>();

	@JsonProperty("itemId")
	public String getItemId() {
		return itemId;
	}

	@JsonProperty("itemId")
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@JsonProperty("vdomcount")
	public Integer getVdomcount() {
		return vdomcount;
	}

	@JsonProperty("vdomcount")
	public void setVdomcount(Integer vdomcount) {
		this.vdomcount = vdomcount;
	}

	@JsonProperty("ipcount")
	public Integer getIpcount() {
		return ipcount;
	}

	@JsonProperty("ipcount")
	public void setIpcount(Integer ipcount) {
		this.ipcount = ipcount;
	}

	@JsonProperty("backupsize")
	public String getBackupsize() {
		return backupsize;
	}

	@JsonProperty("backupsize")
	public void setBackupsize(String backupsize) {
		this.backupsize = backupsize;
	}

	@JsonProperty("backuplocation")
	public String getBackuplocation() {
		return backuplocation;
	}

	@JsonProperty("backuplocation")
	public void setBackuplocation(String backuplocation) {
		this.backuplocation = backuplocation;
	}
	
	@JsonProperty("clientToSiteVpnQuantity")
	public Integer getClientToSiteVpnQuantity() {
		return clientToSiteVpnQuantity;
	}

	@JsonProperty("clientToSiteVpnQuantity")
	public void setClientToSiteVpnQuantity(Integer clientToSiteVpnQuantity) {
		this.clientToSiteVpnQuantity = clientToSiteVpnQuantity;
	}

	@JsonProperty("siteToSiteVpnQuantity")
	public Integer getSiteToSiteVpnQuantity() {
		return siteToSiteVpnQuantity;
	}

	@JsonProperty("siteToSiteVpnQuantity")
	public void setSiteToSiteVpnQuantity(Integer siteToSiteVpnQuantity) {
		this.siteToSiteVpnQuantity = siteToSiteVpnQuantity;
	}

	@JsonProperty("dbLicenseQuantity")
	public Map<String, Map<String, Integer>> getDbLicenseQuantity() {
		return dbLicenseQuantity;
	}

	@JsonProperty("dbLicenseQuantity")
	public void setDbLicenseQuantity(Map<String, Map<String, Integer>> dbLicenseQuantity) {
		this.dbLicenseQuantity = dbLicenseQuantity;
	}
	
	@JsonProperty("drLicenseQuantity")
	public Map<String, Map<String, String>> getDrLicenseQuantity() {
		return drLicenseQuantity;
	}

	@JsonProperty("drLicenseQuantity")
	public void setDrLicenseQuantity(Map<String, Map<String, String>> drLicenseQuantity) {
		this.drLicenseQuantity = drLicenseQuantity;
	}
	
	@JsonProperty("hybridConnections")
	public Map<String, Map<String, String>> getHybridConnections() {
		return hybridConnections;
	}

	@JsonProperty("hybridConnections")
	public void setHybridConnections(Map<String, Map<String, String>> hybridConnections) {
		this.hybridConnections = hybridConnections;
	}

	@JsonProperty("nrc")
	public Double getNrc() {
		return nrc;
	}

	@JsonProperty("nrc")
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}

	@JsonProperty("mrc")
	public Double getMrc() {
		return mrc;
	}

	@JsonProperty("mrc")
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}

	@JsonProperty("priceBreakup")
	public Map<String, Component> getPriceBreakup() {
		return priceBreakup;
	}

	@JsonProperty("priceBreakup")
	public void setPriceBreakup(Map<String, Component> priceBreakup) {
		this.priceBreakup = priceBreakup;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("itemId", itemId).append("vdomcount", vdomcount)
				.append("ipcount", ipcount).append("backupsize", backupsize).append("backuplocation", backuplocation)
				.append("nrc", nrc).append("mrc", mrc).append("priceBreakup", priceBreakup).toString();
	}

}
