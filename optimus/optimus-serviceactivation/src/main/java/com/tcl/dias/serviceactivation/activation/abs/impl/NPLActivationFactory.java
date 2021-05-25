package com.tcl.dias.serviceactivation.activation.abs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.activation.abs.base.AbstractActivationFactory;
import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.ill.impl.NPLConfiguration;
import com.tcl.dias.serviceactivation.activation.utils.RouterType;

/**
 * This file contains the IllActivationFactory.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class NPLActivationFactory extends AbstractActivationFactory {

	@Autowired
	ApplicationContext appCtx;

	/**
	 * 
	 * getNetPConfigFactory
	 * 
	 * @param routerType
	 * @return
	 */
	@Override
	public AbstractConfiguration getActivationConfigFactory(RouterType routerType) {

			return appCtx.getBean(NPLConfiguration.class);
	
	}

}
