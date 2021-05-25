package com.tcl.dias.common.beans;
/**
 * 
 * This file contains the LinkCOPFDetails bean class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class LinkCOPFDetails {
	private String copfIdC;
	private String mrcC;
	private String nrc;
	private String city;
	private String effectiveDateOfTermination;
	private String preCopfId;
	
	public String getNrc() {
		return nrc;
	}
	public void setNrc(String nrc) {
		this.nrc = nrc;
	}
	public String getCopfIdC() {
		return copfIdC;
	}
	public void setCopfIdC(String copfIdC) {
		this.copfIdC = copfIdC;
	}
	public String getMrcC() {
		return mrcC;
	}
	public void setMrcC(String mrcC) {
		this.mrcC = mrcC;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getEffectiveDateOfTermination() {
		return effectiveDateOfTermination;
	}
	public void setEffectiveDateOfTermination(String effectiveDateOfTermination) {
		this.effectiveDateOfTermination = effectiveDateOfTermination;
	}
	public String getPreCopfId() {
		return preCopfId;
	}
	public void setPreCopfId(String preCopfId) {
		this.preCopfId = preCopfId;
	}	
}
