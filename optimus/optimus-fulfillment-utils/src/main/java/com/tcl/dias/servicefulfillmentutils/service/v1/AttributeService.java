package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.Map;

import com.tcl.gvg.exceptionhandler.custom.TclCommonException;

/**
 * 
 * This file contains the AttributeService.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public interface AttributeService {

	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId,String siteType) throws TclCommonException;

	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId,
			AttributeManipulator attributeManipulator,String siteType) throws TclCommonException;

}
