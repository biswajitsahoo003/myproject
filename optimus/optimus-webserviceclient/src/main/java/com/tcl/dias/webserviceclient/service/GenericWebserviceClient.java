package com.tcl.dias.webserviceclient.service;

import com.tcl.dias.webserviceclient.beans.SoapRequest;
import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * This file contains the GenericWebserviceClient.java class.
 * 
 *
 * @author SAMUEL S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public interface GenericWebserviceClient {
	<T> T doSoapCallForObject(SoapRequest soapRequest, Class<T> clazz) throws TclCommonException ;
	String doSoapCallForString(SoapRequest soapRequest) throws TclCommonException ;
}
