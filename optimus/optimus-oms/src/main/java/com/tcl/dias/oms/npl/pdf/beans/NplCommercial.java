package com.tcl.dias.oms.npl.pdf.beans;

import java.util.List;

import com.tcl.dias.oms.pdf.beans.IllSiteCommercial;

/**
 * This file contains the NplCommercial.java class.
 * 
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NplCommercial {

	private String offeringName;
	private List<NplLinkCommercial> linkCommercials;
	private Double totalARC;
	private Double totalNRC;
	private List<IllSiteCommercial> illSiteCommercials;
    private String serviceId; 
	public List<IllSiteCommercial> getIllSiteCommercials() {
		return illSiteCommercials;
	}

	public void setIllSiteCommercials(List<IllSiteCommercial> illSiteCommercials) {
		this.illSiteCommercials = illSiteCommercials;
	}
	
  //These Attributes hold currency specific formatted values
	private String totalARCFormatted;
	private String totalNRCFormatted;

	public String getTotalARCFormatted() {
		return totalARCFormatted;
	}

	public void setTotalARCFormatted(String totalARCFormatted) {
		this.totalARCFormatted = totalARCFormatted;
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

	public List<NplLinkCommercial> getLinkCommercials() {
		return linkCommercials;
	}

	public void setLinkCommercials(List<NplLinkCommercial> linkCommercials) {
		this.linkCommercials = linkCommercials;
	}


	public Double getTotalARC() {
		return totalARC;
	}

	public void setTotalARC(Double totalARC) {
		this.totalARC = totalARC;
	}


	public Double getTotalNRC() {
		return totalNRC;
	}

	public void setTotalNRC(Double totalNRC) {
		this.totalNRC = totalNRC;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

}
