package com.tcl.dias.oms.izosdwan.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.tcl.dias.oms.izopc.beans.CgwPricingInputDatum;

/**
 * 
 * This file contains the VproxyPricingInputDatum.java class.
 * 
 *
 * @author mpalanis
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "quote_id",
	"opportunity_term",
	"markup_pct",
	"tcl_management",
	"tcl_one_time_implementation",
	"tcl_management_charge",
    "tcl_one_time_implementation_charge",
	"swg_premium_support",
    "spa_premium_support",
    "swg_premium_support_charge",
    "spa_premium_support_charge",
    "swg_total_cost",
    "spa_total_cost",
    "swg_total_price",
    "spa_total_price",
   "solution_details"})
public class VproxyPricingInputDatum {
	
	@JsonProperty("quote_id")
	private String quoteId;
	@JsonProperty("opportunity_term")
	private Double opportunityTerm;
	@JsonProperty("markup_pct")
	private Double markupPct;
	@JsonProperty("tcl_management")
	private String tclManagement;
	@JsonProperty("tcl_one_time_implementation")
	private Double tclOneTimeImplementation;
	@JsonProperty("swg_premium_support_charge")
	private Double swgPremiumSupportCharge;
	@JsonProperty("spa_premium_support_charge")
	private Double spaPremiumSupportCharge;
	public Double getSwgPremiumSupportCharge() {
		return swgPremiumSupportCharge;
	}

	public void setSwgPremiumSupportCharge(Double swgPremiumSupportCharge) {
		this.swgPremiumSupportCharge = swgPremiumSupportCharge;
	}

	public Double getSpaPremiumSupportCharge() {
		return spaPremiumSupportCharge;
	}

	public void setSpaPremiumSupportCharge(Double spaPremiumSupportCharge) {
		this.spaPremiumSupportCharge = spaPremiumSupportCharge;
	}

	public String getTclManagement() {
		return tclManagement;
	}

	public void setTclManagement(String tclManagement) {
		this.tclManagement = tclManagement;
	}

	public Double getTclOneTimeImplementation() {
		return tclOneTimeImplementation;
	}

	public void setTclOneTimeImplementation(Double tclOneTimeImplementation) {
		this.tclOneTimeImplementation = tclOneTimeImplementation;
	}

	public Double getTclManagementCharge() {
		return tclManagementCharge;
	}

	public void setTclManagementCharge(Double tclManagementCharge) {
		this.tclManagementCharge = tclManagementCharge;
	}

	public Double getTclOneTimeImplementationCharge() {
		return tclOneTimeImplementationCharge;
	}

	public void setTclOneTimeImplementationCharge(Double tclOneTimeImplementationCharge) {
		this.tclOneTimeImplementationCharge = tclOneTimeImplementationCharge;
	}

	@JsonProperty("tcl_management_charge")
	private Double tclManagementCharge;
	@JsonProperty("tcl_one_time_implementation_charge")
	private Double tclOneTimeImplementationCharge;
	@JsonProperty("swg_premium_support")
	private Integer swgPremiumSupport;
	@JsonProperty("spa_premium_support")
	private Integer spaPremiumSupport;
	@JsonProperty("swg_total_cost")
	private Double swgTotalCost;
	@JsonProperty("spa_total_cost")
	private Double spaTotalCost;
	@JsonProperty("swg_total_price")
	private Double swgTotalPrice;
	@JsonProperty("spa_total_price")
	private Double spaTotalPrice;
	@JsonProperty("version")
	private Integer version;
	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	@JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();
	
	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	@JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

	public String getQuoteId() {
		return quoteId;
	}

	public void setQuoteId(String quoteId) {
		this.quoteId = quoteId;
	}

	public Double getOpportunityTerm() {
		return opportunityTerm;
	}

	public void setOpportunityTerm(Double opportunityTerm) {
		this.opportunityTerm = opportunityTerm;
	}

	public Double getMarkupPct() {
		return markupPct;
	}

	public void setMarkupPct(Double markupPct) {
		this.markupPct = markupPct;
	}

	public Integer getSwgPremiumSupport() {
		return swgPremiumSupport;
	}

	public void setSwgPremiumSupport(Integer swgPremiumSupport) {
		this.swgPremiumSupport = swgPremiumSupport;
	}

	public Integer getSpaPremiumSupport() {
		return spaPremiumSupport;
	}

	public void setSpaPremiumSupport(Integer spaPremiumSupport) {
		this.spaPremiumSupport = spaPremiumSupport;
	}

	public Double getSwgTotalCost() {
		return swgTotalCost;
	}

	public void setSwgTotalCost(Double swgTotalCost) {
		this.swgTotalCost = swgTotalCost;
	}

	public Double getSpaTotalCost() {
		return spaTotalCost;
	}

	public void setSpaTotalCost(Double spaTotalCost) {
		this.spaTotalCost = spaTotalCost;
	}

	public Double getSwgTotalPrice() {
		return swgTotalPrice;
	}

	public void setSwgTotalPrice(Double swgTotalPrice) {
		this.swgTotalPrice = swgTotalPrice;
	}

	public Double getSpaTotalPrice() {
		return spaTotalPrice;
	}

	public void setSpaTotalPrice(Double spaTotalPrice) {
		this.spaTotalPrice = spaTotalPrice;
	}

	@JsonProperty("solution_details")
	private List<VproxySolutionDetailsInputDatum> inputData = null;
		
	public List<VproxySolutionDetailsInputDatum> getInputData() {
		return inputData;
	}

	public void setInputData(List<VproxySolutionDetailsInputDatum> inputData) {
		this.inputData = inputData;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("inputData", inputData).toString();
	}
}
