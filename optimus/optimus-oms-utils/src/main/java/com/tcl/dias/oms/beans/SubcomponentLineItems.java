package com.tcl.dias.oms.beans;
/**
 * This file contains the IllSiteCommercial.java class.
 * 
 *
 * @author AnneF
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SubcomponentLineItems {
	
	private String subComponentName;
	
	private Double mrc = 0D;
	
	private Double nrc = 0D;
	
	private Double arc = 0D;
	
	private String mrcFormatted;
	
	private String nrcFormatted;
	
	private String arcFormatted;
	
	private String hsnCode;

	public String getSubComponentName() {
		return subComponentName;
	}

	public void setSubComponentName(String subComponentName) {
		this.subComponentName = subComponentName;
	}

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

	public String getMrcFormatted() {
		return mrcFormatted;
	}

	public void setMrcFormatted(String mrcFormatted) {
		this.mrcFormatted = mrcFormatted;
	}

	public String getNrcFormatted() {
		return nrcFormatted;
	}

	public void setNrcFormatted(String nrcFormatted) {
		this.nrcFormatted = nrcFormatted;
	}

	public String getArcFormatted() {
		return arcFormatted;
	}

	public void setArcFormatted(String arcFormatted) {
		this.arcFormatted = arcFormatted;
	}
	
	

	public String getHsnCode() {
		return hsnCode;
	}

	public void setHsnCode(String hsnCode) {
		this.hsnCode = hsnCode;
	}

	@Override
	public String toString() {
		return "SubcomponentLineItems [subComponentName=" + subComponentName + ", mrc=" + mrc + ", nrc=" + nrc
				+ ", arc=" + arc + ", mrcFormatted=" + mrcFormatted + ", nrcFormatted=" + nrcFormatted
				+ ", arcFormatted=" + arcFormatted  + ", hsnCode=" + hsnCode + "]";
	}
	
	
	
	

}
