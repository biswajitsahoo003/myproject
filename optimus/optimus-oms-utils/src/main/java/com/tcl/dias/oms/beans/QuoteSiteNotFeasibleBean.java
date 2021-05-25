package com.tcl.dias.oms.beans;

import java.io.Serializable;

/**
 * Request Bean Class for Site Not Feasible Api
 * 
 * @author Kavya Singh
 *@link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class QuoteSiteNotFeasibleBean implements Serializable{

	private static final long serialVersionUID = 7016147286704806048L;

	private Integer quoteToLeId;
	
	private Integer siteId;
	
	private String description;

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public Integer getSiteId() {
		return siteId;
	}

	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	
}
