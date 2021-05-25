package com.tcl.dias.servicefulfillment.beans.teamsdr;

/**
 * Advanced enrichment attributes
 *
 * @author srraghav
 */
public class AdvancedEnrichmentAttributes {
	private String mediaGatewayName;
	private String managementInterfaceIp;
	private String gatewayIp;
	private String subnetMask;
	private String sbcLocatedBehindFirewall;

	public AdvancedEnrichmentAttributes() {
	}

	public String getMediaGatewayName() {
		return mediaGatewayName;
	}

	public void setMediaGatewayName(String mediaGatewayName) {
		this.mediaGatewayName = mediaGatewayName;
	}

	public String getManagementInterfaceIp() {
		return managementInterfaceIp;
	}

	public void setManagementInterfaceIp(String managementInterfaceIp) {
		this.managementInterfaceIp = managementInterfaceIp;
	}

	public String getGatewayIp() {
		return gatewayIp;
	}

	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public String getSubnetMask() {
		return subnetMask;
	}

	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}

	public String getSbcLocatedBehindFirewall() {
		return sbcLocatedBehindFirewall;
	}

	public void setSbcLocatedBehindFirewall(String sbcLocatedBehindFirewall) {
		this.sbcLocatedBehindFirewall = sbcLocatedBehindFirewall;
	}
}
