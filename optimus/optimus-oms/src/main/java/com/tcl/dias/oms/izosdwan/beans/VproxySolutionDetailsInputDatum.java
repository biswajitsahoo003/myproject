package com.tcl.dias.oms.izosdwan.beans;

import java.math.BigInteger;
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

/**
 * 
 * This file contains the VproxySolutionDetailsPricingInputDatum.java class.
 * 
 *
 * @author mpalanis
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"solution_name",
    "offering_name",
    "offering_value",
    "offering_cost",
    "offering_price",
    "addon_details",
    "middle_east_users",
    "other_users"
})
public class VproxySolutionDetailsInputDatum {
	@JsonProperty("solution_name")
	private String solutionName;
	@JsonProperty("offering_name")
	private String offeringName;
	@JsonProperty("offering_value")
	private BigInteger offeringValue;
	@JsonProperty("offering_cost")
	private Double offeringCost;
	@JsonProperty("offering_price")
	private Double offeringPrice;
	@JsonProperty("middle_east_users")
	private BigInteger middleEastUsers;
	@JsonProperty("other_users")
	private BigInteger otherUsers;
	
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
	
	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getOfferingName() {
		return offeringName;
	}

	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}

	public BigInteger getOfferingValue() {
		return offeringValue;
	}

	public void setOfferingValue(BigInteger offeringValue) {
		this.offeringValue = offeringValue;
	}

	public Double getOfferingCost() {
		return offeringCost;
	}

	public void setOfferingCost(Double offeringCost) {
		this.offeringCost = offeringCost;
	}

	public Double getOfferingPrice() {
		return offeringPrice;
	}

	public void setOfferingPrice(Double offeringPrice) {
		this.offeringPrice = offeringPrice;
	}

	public BigInteger getMiddleEastUsers() {
		return middleEastUsers;
	}

	public void setMiddleEastUsers(BigInteger middleEastUsers) {
		this.middleEastUsers = middleEastUsers;
	}

	public BigInteger getOtherUsers() {
		return otherUsers;
	}

	public void setOtherUsers(BigInteger otherUsers) {
		this.otherUsers = otherUsers;
	}

	@JsonProperty("addon_details")
	private List<VproxyAddonDetailsInputDatum> inputData = null;
		
	public List<VproxyAddonDetailsInputDatum> getInputData() {
		return inputData;
	}

	public void setInputData(List<VproxyAddonDetailsInputDatum> inputData) {
		this.inputData = inputData;
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("inputData", inputData).toString();
	}
}
