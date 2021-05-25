package com.tcl.dias.beans;

import java.util.List;

/**
 * This file contains the SeverityTicketsBean.java class.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TicketTrendsResponseBean extends TicketInfoBean{
	
	private List<TicketInfoBean> severityBean;
	
	private Integer circuitCount;

	
	/**
	 * @return the severityBean
	 */
	public List<TicketInfoBean> getSeverityBean() {
		return severityBean;
	}

	/**
	 * @param ticketInfoList the list ofseverityBean to set
	 */
	public void setTicketInfoBean(List<TicketInfoBean> ticketInfoList) {
		this.severityBean = ticketInfoList;
	}

	/**
	 * @return the circuitCount
	 */
	public Integer getCircuitCount() {
		return circuitCount;
	}

	/**
	 * @param circuitCount the circuitCount to set
	 */
	public void setCircuitCount(Integer circuitCount) {
		this.circuitCount = circuitCount;
	}

}