package com.tcl.dias.oms.gde.pdf.beans;

public class GdeLinkCommercial {
    private String bandWidthOnDemand;
    private String serviceType;
    private String hsnCode;
    private String chargeableItem;
    private String subTotalArc;
    private String subTotalNrc;
    private Boolean isOnDemand = false;
    private Double onDemandARC = 0D;
    private Double onDemandNRC = 0D;
    private Double onDemandMRC = 0D;
    private String onDemandARCFormatted;
	private String onDemandNRCFormatted;
	private String onDemandMRCFormatted;
    
    private Boolean isBod = false;

    public String getBandWidthOnDemand() {
		return bandWidthOnDemand;
	}

	public void setBandWidthOnDemand(String bandWidthOnDemand) {
		this.bandWidthOnDemand = bandWidthOnDemand;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getHsnCode() {
        return hsnCode;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public String getChargeableItem() {
        return chargeableItem;
    }

    public void setChargeableItem(String chargeableItem) {
        this.chargeableItem = chargeableItem;
    }

    public String getSubTotalArc() {
        return subTotalArc;
    }

    public void setSubTotalArc(String subTotalArc) {
        this.subTotalArc = subTotalArc;
    }

    public String getSubTotalNrc() {
        return subTotalNrc;
    }

    public void setSubTotalNrc(String subTotalNrc) {
        this.subTotalNrc = subTotalNrc;
    }

	public Boolean getIsBod() {
		return isBod;
	}

	public void setIsBod(Boolean isBod) {
		this.isBod = isBod;
	}

	public Double getOnDemandARC() {
		return onDemandARC;
	}

	public void setOnDemandARC(Double onDemandARC) {
		this.onDemandARC = onDemandARC;
	}

	public Double getOnDemandNRC() {
		return onDemandNRC;
	}

	public void setOnDemandNRC(Double onDemandNRC) {
		this.onDemandNRC = onDemandNRC;
	}

	public Boolean getIsOnDemand() {
		return isOnDemand;
	}

	public void setIsOnDemand(Boolean isOnDemand) {
		this.isOnDemand = isOnDemand;
	}

	public Double getOnDemandMRC() {
		return onDemandMRC;
	}

	public void setOnDemandMRC(Double onDemandMRC) {
		this.onDemandMRC = onDemandMRC;
	}

	public String getOnDemandARCFormatted() {
		return onDemandARCFormatted;
	}

	public void setOnDemandARCFormatted(String onDemandARCFormatted) {
		this.onDemandARCFormatted = onDemandARCFormatted;
	}

	public String getOnDemandNRCFormatted() {
		return onDemandNRCFormatted;
	}

	public void setOnDemandNRCFormatted(String onDemandNRCFormatted) {
		this.onDemandNRCFormatted = onDemandNRCFormatted;
	}

	public String getOnDemandMRCFormatted() {
		return onDemandMRCFormatted;
	}

	public void setOnDemandMRCFormatted(String onDemandMRCFormatted) {
		this.onDemandMRCFormatted = onDemandMRCFormatted;
	}    
	
    
}

