package com.tcl.dias.beans;

import java.util.Date;

/**
 * This file contains the MTTRReportBean.java class.
 * 
 *
 * @author Deepika S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MTTRReportBean {

	private String ticketNo;
	
	private Double mttrInHrsPerTicket;
	
	private Date tktCreationDate;
	
	private String ticketSeverity;

	/**
	 * @return the ticketNo
	 */
	public String getTicketNo() {
		return ticketNo;
	}

	/**
	 * @param ticketNo the ticketNo to set
	 */
	public void setTicketNo(String ticketNo) {
		this.ticketNo = ticketNo;
	}

	/**
	 * @return the mttrInHrsPerTicket
	 */
	public Double getMttrInHrsPerTicket() {
		return mttrInHrsPerTicket;
	}

	/**
	 * @param mttrInHrsPerTicket the mttrInHrsPerTicket to set
	 */
	public void setMttrInHrsPerTicket(Double mttrInHrsPerTicket) {
		this.mttrInHrsPerTicket = mttrInHrsPerTicket;
	}

	/**
	 * @return the tktCreationDate
	 */
	public Date getTktCreationDate() {
		return tktCreationDate;
	}

	/**
	 * @param tktCreationDate the tktCreationDate to set
	 */
	public void setTktCreationDate(Date tktCreationDate) {
		this.tktCreationDate = tktCreationDate;
	}

	/**
	 * @return the ticketSeverity
	 */
	public String getTicketSeverity() {
		return ticketSeverity;
	}

	/**
	 * @param ticketSeverity the ticketSeverity to set
	 */
	public void setTicketSeverity(String ticketSeverity) {
		this.ticketSeverity = ticketSeverity;
	}
	
}