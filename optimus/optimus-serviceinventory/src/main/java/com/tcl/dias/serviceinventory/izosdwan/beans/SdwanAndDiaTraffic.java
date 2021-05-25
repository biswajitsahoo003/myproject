package com.tcl.dias.serviceinventory.izosdwan.beans;

/**
 * Bean for storing traffic usages
 * 
 * @author Kishore Nagarajan
 */
public class SdwanAndDiaTraffic {

	private String siteName;
	private SdwanBandwidths sdwanBandwidths;

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public SdwanBandwidths getSdwanBandwidths() {
		return sdwanBandwidths;
	}

	public void setSdwanBandwidths(SdwanBandwidths sdwanBandwidths) {
		this.sdwanBandwidths = sdwanBandwidths;
	}

	@Override
	public String toString() {
		return "SdwanAndDiaTraffic [siteName=" + siteName + ", sdwanBandwidths=" + sdwanBandwidths + "]";
	}

}
