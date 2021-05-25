package com.tcl.dias.oms.gsc.beans;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * Pricing Request for GSC pricing
 * 
 * @author Thamizhselvi Perumal
 * 
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscPricingRequest {

	@NotNull
	private String productName;

	@NotNull
	private List<Integer> gscConfigurationIds;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<Integer> getGscConfigurationIds() {
		return gscConfigurationIds;
	}

	public void setGscConfigurationIds(List<Integer> gscConfigurationIds) {
		this.gscConfigurationIds = gscConfigurationIds;
	}

}
