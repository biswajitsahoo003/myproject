package com.tcl.dias.serviceactivation.activation.utils;

import org.apache.commons.lang3.StringUtils;

import com.tcl.dias.serviceactivation.activation.netp.beans.BandwidthUnit;
import com.tcl.dias.serviceactivation.activation.netp.beans.Duplex;

/**
 * This file contains the ActivationUtils.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ActivationUtils {

	/**
	 * 
	 * getBandwithUnit- This method converts the mbps to Enum
	 * 
	 * @param bandwidth
	 * @return
	 */
	public static BandwidthUnit getBandwithUnit(String bandwidth) {
		BandwidthUnit bwUnit = BandwidthUnit.MBPS;
		if (bandwidth == null) {
			bwUnit = null;
		} else if (bandwidth.equalsIgnoreCase("kbps")) {
			bwUnit = BandwidthUnit.KBPS;
		} else if (bandwidth.equalsIgnoreCase("gbps")) {
			bwUnit = BandwidthUnit.GBPS;
		} else if (bandwidth.equalsIgnoreCase("bps")) {
			bwUnit = BandwidthUnit.BPS;
		}
		return bwUnit;

	}

	/**
	 * 
	 * getDuplex - This method converts from duplex
	 * 
	 * @param duplex
	 * @return
	 */
	public static Duplex getDuplex(String duplex) {
		Duplex duplexEnum = Duplex.NOT_APPLICABLE;
		if (duplex == null) {
			duplexEnum = null;
		} else if (duplex.equalsIgnoreCase("auto")) {
			duplexEnum = Duplex.AUTO;
		} else if (duplex.equalsIgnoreCase("full")) {
			duplexEnum = Duplex.FULL;
		} else if (duplex.equalsIgnoreCase("half")) {
			duplexEnum = Duplex.HALF;
		}
		return duplexEnum;

	}

	/**
	 * 
	 * getBooleanValue - Get Boolean Value
	 * 
	 * @param value
	 * @return
	 */
	public static Boolean getBooleanValue(Byte value) {
		Boolean bool = null;
		if (value == null) {
			bool = null;
		} else if (value == 1) {
			bool = true;
		} else if (value == 0) {
			bool = false;
		}
		return bool;
	}
	
	public static Boolean getBooleanValue(String value) {
		Boolean bool = null;
		if (value == null) {
			bool = null;
		} else if (value.equalsIgnoreCase("true")) {
			bool = true;
		} else if (value.equalsIgnoreCase("false")) {
			bool = false;
		}
		return bool;
	}

	/**
	 * 
	 * getStrValue
	 * 
	 * @param value
	 * @return
	 */
	public static String getStrValue(Byte value) {
		String bool = null;
		if (value == null) {
			bool = null;
		} else if (value == 1) {
			bool = "TRUE";
		} else if (value == 0) {
			bool = "FALSE";
		}
		return bool;
	}

	/**
	 * 
	 * toInteger
	 * 
	 * @param input
	 * @return
	 */
	public static Integer toInteger(String input) {
		if (StringUtils.isNotBlank(input)) {
			return Integer.valueOf(input);
		}
		return null;
	}

	/**
	 * 
	 * toFloat
	 * 
	 * @param input
	 * @return
	 */
	public static Float toFloat(String input) {
		if (StringUtils.isNotBlank(input)) {
			return Float.valueOf(input);
		}
		return null;
	}

	/**
	 * 
	 * toFloat
	 * 
	 * @param input
	 * @return
	 */
	public static Float toFloat(Integer input) {
		if (input != null) {
			return Float.valueOf(input);
		}
		return null;
	}

	/**
	 * 
	 * toString
	 * 
	 * @param input
	 * @return
	 */
	public static String toString(Integer input) {
		if (input != null) {
			return String.valueOf(input);
		}
		return null;
	}

	/**
	 * 
	 * toString
	 * 
	 * @param input
	 * @return
	 */
	public static String toString(Float input) {
		if (input != null) {
			return String.valueOf(input);
		}
		return null;
	}
	
	
}
