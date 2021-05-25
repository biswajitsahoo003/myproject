package com.tcl.dias.beans;

import java.math.BigDecimal;

/**
 * This file contains the Uptime circuit details site level.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UptimeCircuitDetailsResponseBean {

	private String circuitId;
	
	private String serviceLink;

	private String siteAddress;
	
	private String primaryCircuitAlias;
	
	private String secondaryCircuitAlias;

	private String committedSLA;

	private BigDecimal uptimePercentage;

	/**
	 * @return the circuitId
	 */
	public String getCircuitId() {
		return circuitId;
	}

	/**
	 * @param circuitId the circuitId to set
	 */
	public void setCircuitId(String circuitId) {
		this.circuitId = circuitId;
	}

	/**
	 * @return the uptimePercentage
	 */
	public BigDecimal getUptimePercentage() {
		return uptimePercentage;
	}

	/**
	 * @param uptimePercentage the uptimePercentage to set
	 */
	public void setUptimePercentage(BigDecimal uptimePercentage) {
		this.uptimePercentage = uptimePercentage;
	}

	/**
	 * @return the siteAddress
	 */
	public String getSiteAddress() {
		return siteAddress;
	}

	/**
	 * @param siteAddress the siteAddress to set
	 */
	public void setSiteAddress(String siteAddress) {
		this.siteAddress = siteAddress;
	}

	/**
	 * @return the primaryCktAlias
	 */
	public String getPrimaryCktAlias() {
		return primaryCircuitAlias;
	}

	/**
	 * @param primaryCktAlias the primaryCktAlias to set
	 */
	public void setPrimaryCktAlias(String primaryCktAlias) {
		this.primaryCircuitAlias = primaryCktAlias;
	}

	/**
	 * @return the committedSLA
	 */
	public String getCommittedSLA() {
		return committedSLA;
	}

	/**
	 * @param committedSLA the committedSLA to set
	 */
	public void setCommittedSLA(String committedSLA) {
		this.committedSLA = committedSLA;
	}

	/**
	 * @return the serviceLink
	 */
	public String getServiceLink() {
		return serviceLink;
	}

	/**
	 * @param serviceLink the serviceLink to set
	 */
	public void setServiceLink(String serviceLink) {
		this.serviceLink = serviceLink;
	}

	/**
	 * @return the secCircuitAlias
	 */
	public String getSecCircuitAlias() {
		return secondaryCircuitAlias;
	}

	/**
	 * @param secCircuitAlias the secCircuitAlias to set
	 */
	public void setSecCircuitAlias(String secCircuitAlias) {
		this.secondaryCircuitAlias = secCircuitAlias;
	}

	
}
