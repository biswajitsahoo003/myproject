package com.tcl.dias.servicefulfillment.beans.teamsdr;

/**
 * Management and monitoring site bean
 */
public class ManagementAndMonitoringSiteDetailBean {

	// For management and monitoring
	private String siteName;
	private String siteCode;
	private String zipCode;
	private String city;
	private String state;
	private String streetAddress;
	private String network;
	private String networkRange;
	private String networkName;
	private String vpn;
	private String coordinatesLatitude;
	private String coordinatesLongitude;
	private String region;
	private String description;
	private String external;
	private String wireless;

	public ManagementAndMonitoringSiteDetailBean() {
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		this.streetAddress = streetAddress;
	}

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getNetworkRange() {
		return networkRange;
	}

	public void setNetworkRange(String networkRange) {
		this.networkRange = networkRange;
	}

	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	public String getVpn() {
		return vpn;
	}

	public void setVpn(String vpn) {
		this.vpn = vpn;
	}

	public String getCoordinatesLatitude() {
		return coordinatesLatitude;
	}

	public void setCoordinatesLatitude(String coordinatesLatitude) {
		this.coordinatesLatitude = coordinatesLatitude;
	}

	public String getCoordinatesLongitude() {
		return coordinatesLongitude;
	}

	public void setCoordinatesLongitude(String coordinatesLongitude) {
		this.coordinatesLongitude = coordinatesLongitude;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getExternal() {
		return external;
	}

	public void setExternal(String external) {
		this.external = external;
	}

	public String getWireless() {
		return wireless;
	}

	public void setWireless(String wireless) {
		this.wireless = wireless;
	}

	@Override
	public String toString() {
		return "ManagementAndMonitoringSiteDetailBean{" + "siteCode='" + siteCode + '\'' + ", zipCode='" + zipCode + '\'' + ", city='" + city + '\'' + ", state='" + state + '\'' + ", streetAddress='" + streetAddress + '\'' + ", network='" + network + '\'' + ", networkRange='" + networkRange + '\'' + ", networkName='" + networkName + '\'' + ", vpn='" + vpn + '\'' + ", coordinatesLatitude='" + coordinatesLatitude + '\'' + ", coordinatesLongitude='" + coordinatesLongitude + '\'' + ", region='" + region + '\'' + ", description='" + description + '\'' + ", external='" + external + '\'' + ", wireless='" + wireless + '\'' + '}';
	}
}
