package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This file contains the MACDAttributesComparisonBean.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class MACDPricingsComparisonBean {
	
	private String breakUp;
	private String oldQuote;
	private String newQuote;
	/**
	 * @return the breakUp
	 */
	public String getBreakUp() {
		return breakUp;
	}
	/**
	 * @param breakUp the breakUp to set
	 */
	public void setBreakUp(String breakUp) {
		this.breakUp = breakUp;
	}
	/**
	 * @return the oldQuote
	 */
	public String getOldQuote() {
		return oldQuote;
	}
	/**
	 * @param oldQuote the oldQuote to set
	 */
	public void setOldQuote(String oldQuote) {
		this.oldQuote = oldQuote;
	}
	/**
	 * @return the newQuote
	 */
	public String getNewQuote() {
		return newQuote;
	}
	/**
	 * @param newQuote the newQuote to set
	 */
	public void setNewQuote(String newQuote) {
		this.newQuote = newQuote;
	}
	
	
	
	

}
