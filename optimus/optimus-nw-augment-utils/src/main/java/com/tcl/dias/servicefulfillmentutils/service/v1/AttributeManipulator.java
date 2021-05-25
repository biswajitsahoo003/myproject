package com.tcl.dias.servicefulfillmentutils.service.v1;

import java.util.Map;

/**
 * This file contains the AttributeManipulator.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@FunctionalInterface
public interface AttributeManipulator {

	public Map<String, Object> manipulate(Map<String, Object> responseAttr);

}
