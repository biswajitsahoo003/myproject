package com.tcl.dias.oms.npl.constants;

/**
 * This file contains the SiteTypeConstants.java class.
 *
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum SiteTypeConstants {

	SITE("SITE"), POP("POP"),DC("DC"),IDC("IDC");

	private String type;

	private SiteTypeConstants(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}