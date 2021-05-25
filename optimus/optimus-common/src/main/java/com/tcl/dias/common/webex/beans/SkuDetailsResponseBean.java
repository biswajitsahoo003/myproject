package com.tcl.dias.common.webex.beans;

import java.math.BigDecimal;

/**
 * This bean contains fields of SkuResponseBean
 *
 * @author ssyedali
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class SkuDetailsResponseBean {
	private String skuName;
	private String description;
	private BigDecimal mrc;
	
	public SkuDetailsResponseBean() {

	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getMrc() {
		return mrc;
	}

	public void setMrc(BigDecimal mrc) {
		this.mrc = mrc;
	}

	@Override
	public String toString() {
		return "SkuDetailsResponseBean [skuName=" + skuName + ", description=" + description + ", mrc=" + mrc + "]";
	}

}
