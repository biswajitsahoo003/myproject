package com.tcl.dias.beans;

/**
 * This file contains the property for cause of impact and the corresponding ticket count.
 *
 * @author DSIVALIN
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ImpactWithCount {

	private String impact;
	
	private Integer count;

	/**
	 * @return the impact
	 */
	public String getImpact() {
		return impact;
	}

	/**
	 * @param impact the imapct to set
	 */
	public void setImpact(String impact) {
		this.impact = impact;
	}

	/**
	 * @return the count
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(Integer count) {
		this.count = count;
	}
	
}
