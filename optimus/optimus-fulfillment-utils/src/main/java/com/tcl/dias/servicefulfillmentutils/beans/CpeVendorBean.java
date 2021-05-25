package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class CpeVendorBean {
	
	public String vendorCode;
	
	public String vendorName;
	
	public List<CpePrPoBean> cpePrPoBeanList;
	
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	public List<CpePrPoBean> getCpePrPoBeanList() {
		return cpePrPoBeanList;
	}
	public void setCpePrPoBeanList(List<CpePrPoBean> cpePrPoBeanList) {
		this.cpePrPoBeanList = cpePrPoBeanList;
	}
	
	
}
