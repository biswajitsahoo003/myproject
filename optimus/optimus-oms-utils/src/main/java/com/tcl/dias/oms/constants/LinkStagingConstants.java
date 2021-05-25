package com.tcl.dias.oms.constants;

/**
 * This file contains the LinkStagingConstants.java class.
 * 
 *
 * @author Dinahar Vivekanandan
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum LinkStagingConstants {

	CONFIGURE_LINK("CONFIGURE_LINK"),PROVISION_LINK("PROVISION_LINK");
	private String stage;

	private LinkStagingConstants(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}

}
