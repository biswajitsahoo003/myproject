package com.tcl.dias.common.beans;

/**
 * This file contains the GstAddressInfo.java class.
 * 
 *
 * @author Nithya S
 * @link http://www.tatacommunications.com/
 * @copyright 2021 Tata Communications Limited
 */
public class GstAddressInfo {

	private Integer multisiteInfoId;
	private String gstNo;
	private String gstAddress;
	private String state;
	private String city;
	private Integer siteId;
	private String siteType;
	private Integer linkId;
	
	public Integer getMultisiteInfoId() {
		return multisiteInfoId;
	}
	public void setMultisiteInfoId(Integer multisiteInfoId) {
		this.multisiteInfoId = multisiteInfoId;
	}
	public String getGstNo() {
		return gstNo;
	}
	public void setGstNo(String gstNo) {
		this.gstNo = gstNo;
	}
	public String getGstAddress() {
		return gstAddress;
	}
	public void setGstAddress(String gstAddress) {
		this.gstAddress = gstAddress;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public String getSiteType() {
		return siteType;
	}
	public void setSiteType(String siteType) {
		this.siteType = siteType;
	}
	public Integer getLinkId() {
		return linkId;
	}
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	
	@Override
	public String toString() {
		return "GstAddressInfo [multisiteInfoId=" + multisiteInfoId + ", gstNo=" + gstNo + ", gstAddress=" + gstAddress
				+ ", state=" + state + ", city=" + city + ", siteId=" + siteId + ", siteType=" + siteType + ", linkId="
				+ linkId + "]";
	}
	
}
