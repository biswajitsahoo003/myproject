package com.tcl.dias.beans;

import java.util.List;

/**
 * This file contains the month wise - hour bucket with tickets count for.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MTTRSeverityResponseBean {

	private String monthYear;
	
	private List<TicketByMTTRDuration> durationWise;

	/**
	 * @return the monthYear
	 */
	public String getMonthYear() {
		return monthYear;
	}

	/**
	 * @param monthYear the monthYear to set
	 */
	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}

	/**
	 * @return the durationWise
	 */
	public List<TicketByMTTRDuration> getDurationWise() {
		return durationWise;
	}

	/**
	 * @param durationWise the durationWise to set
	 */
	public void setDurationWise(List<TicketByMTTRDuration> durationWise) {
		this.durationWise = durationWise;
	}

}
