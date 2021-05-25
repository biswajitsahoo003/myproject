package com.tcl.dias.beans;

import java.util.ArrayList;

/**
 * Class to hold input request's values.
 * 
 * @author KRUTSRIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PDFRequestBean {
	private String month;
	ArrayList<String> listOfMonths;
	private String faultRateMonth;


	public String getFaultRateMonth() {
		return faultRateMonth;
	}

	public void setFaultRateMonth(String faultRateMonth) {
		this.faultRateMonth = faultRateMonth;
	}

	public ArrayList<String> getListOfMonths() {
		return listOfMonths;
	}

	public void setListOfMonths(ArrayList<String> listOfMonths) {
		this.listOfMonths = listOfMonths;
	}

	public String getMonth() {
		return month;
	}

	// Setter Methods

	public void setMonth(String month) {
		this.month = month;
	}
}