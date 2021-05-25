package com.tcl.dias.serviceactivation.activation.abs.base;

import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.utils.RouterType;

/**
 * This file contains the AbstractActivationFactory.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public abstract class AbstractActivationFactory {

	public abstract AbstractConfiguration getActivationConfigFactory(RouterType routerType);

}
