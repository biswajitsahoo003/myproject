package com.tcl.dias.beans;

import java.util.List;

/**
 * This entity is used to set the monthly or product wise ticket trend with the count.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ServerityTicketResponse {

	private List<TicketTrend> trends;

	/**
	 * @return the trends
	 */
	public List<TicketTrend> getTrends() {
		return trends;
	}

	/**
	 * @param trends the trends to set
	 */
	public void setTrends(List<TicketTrend> trends) {
		this.trends = trends;
	}
	
}
