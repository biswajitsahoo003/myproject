package com.tcl.dias.l2oworkflowutils.service.v1;

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

	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId) throws TclCommonException;

	public Map<String, Object> getTaskAttributes(String taskName, Integer serviceId,
			AttributeManipulator attributeManipulator) throws TclCommonException;

}
