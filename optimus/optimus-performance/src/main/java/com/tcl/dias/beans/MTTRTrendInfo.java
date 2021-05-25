package com.tcl.dias.beans;

/**
 * This file contains the month wise - customer & tata end MTTR data.
 * 
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MTTRTrendInfo {

	private String monthYear;

	private String averageMttrCustomerEnd;
	
	private String averageMttrTataEnd;
	
	private String averageUnidentifiedEnd;

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
	 * @return the averageMttrCustomerEnd
	 */
	public String getAverageMttrCustomerEnd() {
		return averageMttrCustomerEnd;
	}

	/**
	 * @param averageMttrCustomerEnd the averageMttrCustomerEnd to set
	 */
	public void setAverageMttrCustomerEnd(String averageMttrCustomerEnd) {
		this.averageMttrCustomerEnd = averageMttrCustomerEnd;
	}

	/**
	 * @return the averageMttrTataEnd
	 */
	public String getAverageMttrTataEnd() {
		return averageMttrTataEnd;
	}

	/**
	 * @param averageMttrTataEnd the averageMttrTataEnd to set
	 */
	public void setAverageMttrTataEnd(String averageMttrTataEnd) {
		this.averageMttrTataEnd = averageMttrTataEnd;
	}

	/**
	 * @return the averageUnidentifiedEnd
	 */
	public String getAverageUnidentifiedEnd() {
		return averageUnidentifiedEnd;
	}

	/**
	 * @param averageUnidentifiedEnd the averageUnidentifiedEnd to set
	 */
	public void setAverageUnidentifiedEnd(String averageUnidentifiedEnd) {
		this.averageUnidentifiedEnd = averageUnidentifiedEnd;
	}

}
