package com.tcl.dias.oms.pdf.beans;

import java.util.List;

/**
 * This file contains the IllCommercial.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IllCommercial {

	private String productName;
	private String offeringName;
	private String serviceFlavor;
	private List<IllSiteCommercial> siteCommercials;
	private String totalARC;
	private String totalMRC;
	private String totalNRC;
	private Double totalBurstableBwCharge;
	private String totalBurstableBwChargeFormatted;
	private String serviceId;


	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public String getServiceFlavor() {
		return serviceFlavor;
	}

	public void setServiceFlavor(String serviceFlavor) {
		this.serviceFlavor = serviceFlavor;
	}

	public List<IllSiteCommercial> getSiteCommercials() {
		return siteCommercials;
	}

	public void setSiteCommercials(List<IllSiteCommercial> siteCommercials) {
		this.siteCommercials = siteCommercials;
	}

	public String getTotalMRC() {
		return totalMRC;
	}

	public void setTotalMRC(String totalMRC) {
		this.totalMRC = totalMRC;
	}

	public String getTotalNRC() {
		return totalNRC;
	}

	public void setTotalNRC(String totalNRC) {
		this.totalNRC = totalNRC;
	}

	public String getTotalARC() {
		return totalARC;
	}

	public void setTotalARC(String totalARC) {
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
