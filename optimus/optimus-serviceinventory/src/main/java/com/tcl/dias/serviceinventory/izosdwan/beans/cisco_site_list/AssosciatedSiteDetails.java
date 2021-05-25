package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;


import java.io.Serializable;


public class AssosciatedSiteDetails implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String siteName;
	private String sdwanSiteStaus;
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getSdwanSiteStaus() {
		return sdwanSiteStaus;
	}
	public void setSdwanSiteStaus(String sdwanSiteStaus) {
		this.sdwanSiteStaus = sdwanSiteStaus;
	}
	@Override
	public String toString() {
		return "AssosciatedSiteDetails [siteName=" + siteName + ", sdwanSiteStaus=" + sdwanSiteStaus + "]";
	}
	
	
		
}
