package com.tcl.dias.oms.renewals.bean;

public class RenewalsPriceBean {
	
	private Double mrc=0D;
	private Double nrc=0D;
	private Double arc=0D;
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
