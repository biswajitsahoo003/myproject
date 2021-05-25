package com.tcl.dias.oms.gde.pdf.beans;

import java.io.Serializable;

public class GdeBodSpecificAttribute implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String bandwidthOnDemand;
    private String baseCircuitBandwidth;
    private String upgradedBandwidth;
    private String scheduledStartDate;
    private String scheduledEndDate;
    
	public String getBandwidthOnDemand() {
		return bandwidthOnDemand;
	}
	public void setBandwidthOnDemand(String bandwidthOnDemand) {
		this.bandwidthOnDemand = bandwidthOnDemand;
	}
	public String getBaseCircuitBandwidth() {
		return baseCircuitBandwidth;
	}
	public void setBaseCircuitBandwidth(String baseCircuitBandwidth) {
		this.baseCircuitBandwidth = baseCircuitBandwidth;
	}
	public String getUpgradedBandwidth() {
		return upgradedBandwidth;
	}
	public void setUpgradedBandwidth(String upgradedBandwidth) {
		this.upgradedBandwidth = upgradedBandwidth;
	}
	public String getScheduledStartDate() {
		return scheduledStartDate;
	}
	public void setScheduledStartDate(String scheduledStartDate) {
		this.scheduledStartDate = scheduledStartDate;
	}
	public String getScheduledEndDate() {
		return scheduledEndDate;
	}
	public void setScheduledEndDate(String scheduledEndDate) {
		this.scheduledEndDate = scheduledEndDate;
	}
    
    

   
}
