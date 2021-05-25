package com.tcl.dias.oms.constants;

/**
 * This file contains the OrderStagingConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum OrderStagingConstants {


	ORDER_CONFIRMED("ORDER_CONFIRMED"),ORDER_CREATED("ORDER_CREATED"),ORDER_COMPLETED("ORDER_COMPLETED"),ORDER_DELIVERED("ORDER_DELIVERED"),
	ORDER_IN_PROGRESS("ORDER_IN_PROGRESS"), ORDER_CANCELLED("ORDER_CANCELLED");

	private String stage;
	private OrderStagingConstants(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}
	
	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return this.stage;
	}



}
