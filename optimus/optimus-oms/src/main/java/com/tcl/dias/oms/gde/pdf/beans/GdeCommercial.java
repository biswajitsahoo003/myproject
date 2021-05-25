package com.tcl.dias.oms.gde.pdf.beans;

import java.util.List;

public class GdeCommercial {
    private String offeringName;
    private List<GdeLinkCommercial> linkCommercials;
    private Double totalMRC;
    private Double totalARC;
    private Double totalNRC;
    private String totalARCFormatted;
	private String totalNRCFormatted;

    public String getOfferingName() {
        return offeringName;
    }

    public void setOfferingName(String offeringName) {
        this.offeringName = offeringName;
    }

    public List<GdeLinkCommercial> getLinkCommercials() {
        return linkCommercials;
    }

    public void setLinkCommercials(List<GdeLinkCommercial> linkCommercials) {
        this.linkCommercials = linkCommercials;
    }

    public Double getTotalMRC() {
		return totalMRC;
	}

	public void setTotalMRC(Double totalMRC) {
		this.totalMRC = totalMRC;
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
    
}
