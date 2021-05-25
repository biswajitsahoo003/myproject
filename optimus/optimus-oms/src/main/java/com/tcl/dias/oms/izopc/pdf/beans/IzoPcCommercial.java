package com.tcl.dias.oms.izopc.pdf.beans;

import java.util.List;

/**
 * 
 * Bean class holding commercial information of IZOPC
 * 
 *
 * @author Dinahar V
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 * TODO: class structure to be implemented based on requirement
 */
public class IzoPcCommercial {
	
	private String offeringName;
	private List<IzoPcSiteCommercial> siteCommercials;
	private Double totalARC;
	private Double totalMRC;
	private Double totalNRC;
	
	private String totalARCFormatted;
	private String totalMRCFormatted;
	private String totalNRCFormatted;
	
	public String getTotalARCFormatted() {
		return totalARCFormatted;
	}
	public void setTotalARCFormatted(String totalARCFormatted) {
		this.totalARCFormatted = totalARCFormatted;
	}
	public String getTotalMRCFormatted() {
		return totalMRCFormatted;
	}
	public void setTotalMRCFormatted(String totalMRCFormatted) {
		this.totalMRCFormatted = totalMRCFormatted;
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
	public List<IzoPcSiteCommercial> getSiteCommercials() {
		return siteCommercials;
	}
	public void setSiteCommercials(List<IzoPcSiteCommercial> siteCommercials) {
		this.siteCommercials = siteCommercials;
	}
	public Double getTotalARC() {
		return totalARC;
	}
	public void setTotalARC(Double totalARC) {
		this.totalARC = totalARC;
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


}
