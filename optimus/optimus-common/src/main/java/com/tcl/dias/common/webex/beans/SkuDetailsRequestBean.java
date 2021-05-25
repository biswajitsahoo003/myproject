package com.tcl.dias.common.webex.beans;

/**
 * This bean contains fields of Sku
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SkuDetailsRequestBean {
	private String licenseType;
	private String audioPlan;
	private String bridgeRegion;

	public SkuDetailsRequestBean() {
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getAudioPlan() {
		return audioPlan;
	}

	public void setAudioPlan(String audioPlan) {
		this.audioPlan = audioPlan;
	}

	public String getBridgeRegion() {
		return bridgeRegion;
	}

	public void setBridgeRegion(String bridgeRegion) {
		this.bridgeRegion = bridgeRegion;
	}

	@Override
	public String toString() {
		return "SkuDetailsRequestBean [licenseType=" + licenseType + ", audioPlan=" + audioPlan + ", bridgeRegion="
				+ bridgeRegion + "]";
	}

}
