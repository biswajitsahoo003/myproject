package com.tcl.dias.beans;

import java.util.List;

/**
 * This file contains the severity impact and the corresponding count against a month or a product.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TicketTrend {

	private String productOrMonthWise;
	
	private List<ImpactWithCount> impactedTickets;
	
	/**
	 * @return the product
	 */
	public String getProductOrMonth() {
		return productOrMonthWise;
	}

	/**
	 * @param product the product to set
	 */
	public void setProductOrMonth(String productOrMonthWise) {
		this.productOrMonthWise = productOrMonthWise;
	}

	/**
	 * @return the impactTickets
	 */
	public List<ImpactWithCount> getImpactTickets() {
		return impactedTickets;
	}

	/**
	 * @param impactTickets the impactTickets to set
	 */
	public void setImpactTickets(List<ImpactWithCount> impactTickets) {
		this.impactedTickets = impactTickets;
	}

}
