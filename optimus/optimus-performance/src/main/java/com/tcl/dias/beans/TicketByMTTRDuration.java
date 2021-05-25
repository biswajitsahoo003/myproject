package com.tcl.dias.beans;

/**
 * This file contains the hour bucket with ticket count data.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TicketByMTTRDuration {

	private String hrsBucket;
	
	private Integer ticketCount;

	/**
	 * @return the hrsBucket
	 */
	public String getHrsBucket() {
		return hrsBucket;
	}

	/**
	 * @param hrsBucket the hrsBucket to set
	 */
	public void setHrsBucket(String hrsBucket) {
		this.hrsBucket = hrsBucket;
	}

	/**
	 * @return the ticketCount
	 */
	public Integer getTicketCount() {
		return ticketCount;
	}

	/**
	 * @param ticketCount the ticketCount to set
	 */
	public void setTicketCount(Integer ticketCount) {
		this.ticketCount = ticketCount;
	}
	
	
}
