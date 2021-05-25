package com.tcl.dias.serviceinventory.renewals;

public class RenewalsPriceBean {
	
	private Double mrc;
	private Double nrc;
	private Double arc;
	public Double getMrc() {
		return mrc;
	}
	public void setMrc(Double mrc) {
		this.mrc = mrc;
	}
	public Double getNrc() {
		return nrc;
	}
	public void setNrc(Double nrc) {
		this.nrc = nrc;
	}
	public Double getArc() {
		return arc;
	}
	public void setArc(Double arc) {
		this.arc = arc;
	}
	@Override
	public String toString() {
		return "RenewalsPriceBean [mrc=" + mrc + ", nrc=" + nrc + ", arc=" + arc + "]";
	}
	
	

}
