package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Bean class to hold product family information
 *
 * @author Ramasubramanian Sankar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SIProductFamilySummaryBean {
	private String productFamily;
	private String summary;
	private List<SIAttributeBean> attributes;
	private String productfamilyShortName;
	
	public String getProductFamily() {
		return productFamily;
	}

	public void setProductFamily(String productFamily) {
		this.productFamily = productFamily;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<SIAttributeBean> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<SIAttributeBean> attributes) {
		this.attributes = attributes;
	}
	
	public String getProductfamilyShortName() {
		return productfamilyShortName;
	}

	public void setProductfamilyShortName(String productfamilyShortName) {
		this.productfamilyShortName = productfamilyShortName;
	}
}
