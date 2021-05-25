package com.tcl.dias.oms.constants;

/**
 * This file contains the VersionConstants.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public enum VersionConstants {

	ONE(1);
	private Integer verionNumber;

	private VersionConstants(Integer verionNumber) {
		this.verionNumber = verionNumber;
	}

	public Integer getVersionNumber()
	{
		return verionNumber;
	}

}
