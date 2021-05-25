package com.tcl.dias.oms.beans;

import java.util.Date;

public class CancelledServicesBean {

	private String serviceId;

	private String cancellationReason;
	
	private Double cancellationCharges;

	private String absorbedOrPassedOn;

	private String leadToRFSDate;

	private Date effectiveDateOfChange;

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public Double getCancellationCharges() {
		return cancellationCharges;
	}

	public void setCancellationCharges(Double cancellationCharges) {
		this.cancellationCharges = cancellationCharges;
	}

	public String getAbsorbedOrPassedOn() {
		return absorbedOrPassedOn;
	}

	public void setAbsorbedOrPassedOn(String absorbedOrPassedOn) {
		this.absorbedOrPassedOn = absorbedOrPassedOn;
	}

	public String getLeadToRFSDate() {
		return leadToRFSDate;
	}

	public void setLeadToRFSDate(String leadToRFSDate) {
		this.leadToRFSDate = leadToRFSDate;
	}

	public Date getEffectiveDateOfChange() {
		return effectiveDateOfChange;
	}

	public void setEffectiveDateOfChange(Date effectiveDateOfChange) {
		this.effectiveDateOfChange = effectiveDateOfChange;
	}

	public String getCancellationReason() {
		return cancellationReason;
	}

	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}

	@Override
	public String toString() {
		return "CancellationBean [serviceId=" + serviceId + ", cancellationCharges=" + cancellationCharges
				+ ", absorbedOrPassedOn=" + absorbedOrPassedOn + ", LeadToRFSDate=" + leadToRFSDate
				+ ", effectiveDateOfChange=" + effectiveDateOfChange 
				+ ", cancellationReason=" + cancellationReason +
				"]";
	}
	
	
	
	

}
