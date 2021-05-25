package com.tcl.dias.oms.teamsdr.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Response bean for teams DR pricing response
 * 
 * @author Srinivasa Raghavan
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "plan_add_on_price", "media_gateway_price", "license_price" })
public class TeamsDRPricingResponseBean {

	@JsonProperty("plan_add_on_price")
	List<PlanAddOnMediaGatewayPricesBean> planAndAddOnPrices;

	@JsonProperty("media_gateway_price")
	List<PlanAddOnMediaGatewayPricesBean> mediaGatewayPrices;

	@JsonProperty("license_price")
	List<LicensePricesBean> licensePrices;

	public List<PlanAddOnMediaGatewayPricesBean> getPlanAndAddOnPrices() {
		return planAndAddOnPrices;
	}

	public void setPlanAndAddOnPrices(List<PlanAddOnMediaGatewayPricesBean> planAndAddOnPrices) {
		this.planAndAddOnPrices = planAndAddOnPrices;
	}

	public List<PlanAddOnMediaGatewayPricesBean> getMediaGatewayPrices() {
		return mediaGatewayPrices;
	}

	public void setMediaGatewayPrices(List<PlanAddOnMediaGatewayPricesBean> mediaGatewayPrices) {
		this.mediaGatewayPrices = mediaGatewayPrices;
	}

	public List<LicensePricesBean> getLicensePrices() {
		return licensePrices;
	}

	public void setLicensePrices(List<LicensePricesBean> licensePrices) {
		this.licensePrices = licensePrices;
	}
}
