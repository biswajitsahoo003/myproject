package com.tcl.dias.oms.beans;

import java.util.Date;

/**
 * This file contains the OptyStageRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class OptyStageRequest {

	private Integer orderLeId;
	private Date cofSignedDate;
	private String stage;

	/**
	 * @return the orderLeId
	 */
	public Integer getOrderLeId() {
		return orderLeId;
	}

	/**
	 * @param orderLeId
	 *            the orderLeId to set
	 */
	public void setOrderLeId(Integer orderLeId) {
		this.orderLeId = orderLeId;
	}

	/**
	 * @return the cofSignedDate
	 */
	public Date getCofSignedDate() {
		return cofSignedDate;
	}

	/**
	 * @param cofSignedDate
	 *            the cofSignedDate to set
	 */
	public void setCofSignedDate(Date cofSignedDate) {
		this.cofSignedDate = cofSignedDate;
	}

	/**
	 * @return the stage
	 */
	public String getStage() {
		return stage;
	}

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(String stage) {
		this.stage = stage;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "OptyStageRequest [orderLeId=" + orderLeId + ", cofSignedDate=" + cofSignedDate + ", stage=" + stage
				+ "]";
	}

}
