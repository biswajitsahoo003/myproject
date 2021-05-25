package com.tcl.dias.serviceactivation.activation.factory.ill.base;

import java.util.Calendar;

import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;

/**
 * This class is used to build all the common configurations for RF and IP in
 * Ill
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public abstract class AbstractNPLConfiguration extends AbstractConfiguration {

	/**
	 * 
	 * generateRequestId
	 * 
	 * @param serviceUuid
	 * @return
	 */
	public String generateRequestId(String serviceUuid) {
		return "RID_" + serviceUuid + "_" + Calendar.getInstance().getTimeInMillis() + "_NPLE2E";

	}

}
