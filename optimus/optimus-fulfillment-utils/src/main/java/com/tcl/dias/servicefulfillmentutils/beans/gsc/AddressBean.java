package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressBean {
	
	private Integer addressSeqNo;
	private String countryName;
	private String addrLine1;
	private String addrLine2;
	private String addrLine3;
	private String addrLine4;
	private String cityName;
	private String postalCd;
	
	
	public String getPostalCd() {
		return postalCd;
	}

	public void setPostalCd(String postalCd) {
		this.postalCd = postalCd;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Integer getAddressSeqNo() {
		return addressSeqNo;
	}
	
	public void setAddressSeqNo(Integer addressSeqNo) {
		this.addressSeqNo = addressSeqNo;
	}
	
	public String getCountryName() {
		return countryName;
	}
	
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	
	public String getAddrLine1() {
		return addrLine1;
	}
	
	public void setAddrLine1(String addrLine1) {
		this.addrLine1 = addrLine1;
	}
	
	public String getAddrLine2() {
		return addrLine2;
	}
	
	public void setAddrLine2(String addrLine2) {
		this.addrLine2 = addrLine2;
	}
	
	public String getAddrLine3() {
		return addrLine3;
	}
	
	public void setAddrLine3(String addrLine3) {
		this.addrLine3 = addrLine3;
	}
	
	public String getAddrLine4() {
		return addrLine4;
	}
	
	public void setAddrLine4(String addrLine4) {
		this.addrLine4 = addrLine4;
	}
	
}
