package com.tcl.dias.oms.gvpn.pdf.beans;

import java.util.List;


/**
 * This file contains the GvpnCommercial.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GvpnCommercial {

	private String offeringName;
	private List<GvpnSiteCommercial> siteCommercials;
	private Double totalARC;
	private Double totalMRC;
	private Double totalNRC;
	
	// Attributes to hold formatted values - currency Specific
	
	private String totalARCFormatted;
	private String totalMRCFormatted;
	private String totalNRCFormatted;

	private Double totalBurstableBwCharge;
	private String totalBurstableBwChargeFormatted;
	private String serviceId;
	
	public String getTotalARCFormatted() {
		return totalARCFormatted;
	}

	public void setTotalARCFormatted(String totalARCFormatted) {
		this.totalARCFormatted = totalARCFormatted;
	}

	public String getTotalMRCFormatted() {
		return totalMRCFormatted;
	}

	public void setTotalMRCFormatted(String totalMRCFomatted) {
		this.totalMRCFormatted = totalMRCFomatted;
	}

	public String getTotalNRCFormatted() {
		return totalNRCFormatted;
	}

	public void setTotalNRCFormatted(String totalNRCFormatted) {
		this.totalNRCFormatted = totalNRCFormatted;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public List<GvpnSiteCommercial> getSiteCommercials() {
		return siteCommercials;
	}

	public void setSiteCommercials(List<GvpnSiteCommercial> siteCommercials) {
		this.siteCommercials = siteCommercials;
	}

	public Double getTotalMRC() {
		return totalMRC;
	}

	public void setTotalMRC(Double totalMRC) {
		this.totalMRC = totalMRC;
	}

	public Double getTotalNRC() {
		return totalNRC;
	}

	public void setTotalNRC(Double totalNRC) {
		this.totalNRC = totalNRC;
	}

	/**
	 * @return the totalARC
	 */
	public Double getTotalARC() {
		return totalARC;
	}

	/**
	 * @param totalARC the totalARC to set
	 */
	public void setTotalARC(Double totalARC) {
		this.totalARC = totalARC;
	}

	public Double getTotalBurstableBwCharge() {
		return totalBurstableBwCharge;
	}

	public void setTotalBurstableBwCharge(Double totalBurstableBwCharge) {
		this.totalBurstableBwCharge = totalBurstableBwCharge;
	}

	public String getTotalBurstableBwChargeFormatted() {
		return totalBurstableBwChargeFormatted;
	}

	public void setTotalBurstableBwChargeFormatted(String totalBurstableBwChargeFormatted) {
		this.totalBurstableBwChargeFormatted = totalBurstableBwChargeFormatted;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
