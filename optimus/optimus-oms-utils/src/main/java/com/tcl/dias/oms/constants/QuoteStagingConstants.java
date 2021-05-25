package com.tcl.dias.oms.constants;

/**
 * This file contains the QuoteStagingConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum QuoteStagingConstants {

	SOLUTIONS_CHOOSED("SOLUTIONS_CHOOSED");
	private String stage;

	private QuoteStagingConstants(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}

}
