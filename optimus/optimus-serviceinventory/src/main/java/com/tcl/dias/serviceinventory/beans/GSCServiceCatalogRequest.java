package com.tcl.dias.serviceinventory.beans;

/**
 * This file contains the GSCServiceCatalogRequest - For GSC case, the input to be sent
 * is Outpulse, Tollfree no, Order Id.
 * 
 *
 * @author Deepika Sivalingam
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GSCServiceCatalogRequest {

	private String tollfreeNum;

	private String outpulse;

	private String orderId;

	/**
	 * @return the tollfreeNum
	 */
	public String getTollfreeNum() {
		return tollfreeNum;
	}

	/**
	 * @param tollfreeNum the tollfreeNum to set
	 */
	public void setTollfreeNum(String tollfreeNum) {
		this.tollfreeNum = tollfreeNum;
	}

	/**
	 * @return the outpulse
	 */
	public String getOutpulse() {
		return outpulse;
	}

	/**
	 * @param outpulse the outpulse to set
	 */
	public void setOutpulse(String outpulse) {
		this.outpulse = outpulse;
	}

	/**
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	
}
