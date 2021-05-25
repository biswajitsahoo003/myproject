package com.tcl.dias.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This file contains the product wise Uptime response data.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class TrendResponseBean {

	private String product;
	
	private List<TrendInfoBean> trendInfo;

	/**
	 * @return the product
	 */
	public String getProduct() {
		return product;
	}

	/**
	 * @param product the product to set
	 */
	public void setProduct(String product) {
		this.product = product;
	}

	/**
	 * @return the trendInfo
	 */
	public List<TrendInfoBean> getTrendInfo() {
		return trendInfo;
	}

	/**
	 * @param trendInfo the trendInfo to set
	 */
	public void setTrendInfo(List<TrendInfoBean> trendInfo) {
		this.trendInfo = trendInfo;
	}

	
}
