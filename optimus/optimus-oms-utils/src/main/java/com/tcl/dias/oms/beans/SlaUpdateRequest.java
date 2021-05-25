package com.tcl.dias.oms.beans;

/**
 * This file contains the SlaUpdateRequest.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SlaUpdateRequest {
	
	private Integer quoteLeId;
	
	private String productFamily;
	
	private Integer siteId;
	
	

	/**
	 * @return the productFamily
	 */
	public String getProductFamily() {
		return productFamily;
	}

	/**
	 * @param productFamily the productFamily to set
	 */
	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}

	/**
	 * @return the quoteLeId
	 */
	public Integer getQuoteLeId() {
		return quoteLeId;
	}

	/**
	 * @param quoteLeId the quoteLeId to set
	 */
	public void setQuoteLeId(Integer quoteLeId) {
		this.quoteLeId = quoteLeId;
	}

	/**
	 * @return the siteId
	 */
	public Integer getSiteId() {
		return siteId;
	}

	/**
	 * @param siteId the siteId to set
	 */
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	
	
	

}
