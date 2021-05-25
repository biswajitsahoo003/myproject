package com.tcl.dias.pricingengine.ipc.utils;

/**
 * This class IpcUtils is the utility class for IPC.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class IpcUtils {

	/**
	 * 
	 * roundOff2- This method will roundoff to 2 digits
	 * 
	 * @param input
	 * @return
	 */
	public static Double roundOff2(Double input) {
		return Math.round(input * 100.0) / 100.0;
	}

}
