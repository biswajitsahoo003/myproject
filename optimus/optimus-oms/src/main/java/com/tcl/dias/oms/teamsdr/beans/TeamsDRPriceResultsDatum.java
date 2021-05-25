package com.tcl.dias.oms.teamsdr.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Price request bean for pricing team
 *
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "managed_services", "license", "media_gateway" })
public class TeamsDRPriceResultsDatum {

	@JsonProperty("managed_services")
	private ManagedServicesPricesBean managedServices;

	@JsonProperty("license")
	private LicensePricesBean license;

	@JsonProperty("media_gateway")
	private MediaGatewayPricesBean mediaGateway;

	@JsonProperty("error_flag")
	private String errorFlag;

	@JsonProperty("error_msg")
	private String errorMessage;

	@JsonProperty("version")
	private String version;

	public ManagedServicesPricesBean getManagedServices() {
		return managedServices;
	}

	public void setManagedServices(ManagedServicesPricesBean managedServices) {
		this.managedServices = managedServices;
	}

	public LicensePricesBean getLicense() {
		return license;
	}

	public void setLicense(LicensePricesBean license) {
		this.license = license;
	}

	public MediaGatewayPricesBean getMediaGateway() {
		return mediaGateway;
	}

	public void setMediaGateway(MediaGatewayPricesBean mediaGateway) {
		this.mediaGateway = mediaGateway;
	}

	public String getErrorFlag() {
		return errorFlag;
	}

	public void setErrorFlag(String errorFlag) {
		this.errorFlag = errorFlag;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
