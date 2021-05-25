package com.tcl.dias.oms.constants;

/**
 * This file contains the SiteStagingConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum SiteStagingConstants {

	CONFIGURE_SITES("CONFIGURE_SITES"),PROVISION_SITES("PROVISION_SITES");
	private String stage;

	private SiteStagingConstants(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}

}
