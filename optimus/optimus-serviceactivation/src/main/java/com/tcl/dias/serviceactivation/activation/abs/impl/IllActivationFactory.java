package com.tcl.dias.serviceactivation.activation.abs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.activation.abs.base.AbstractActivationFactory;
import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.ill.impl.AluIllConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.ill.impl.CiscoIllConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.ill.impl.JuniperIllConfiguration;
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
public class IllActivationFactory extends AbstractActivationFactory {

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
		if (RouterType.ALU.equals(routerType)) {
			return appCtx.getBean(AluIllConfiguration.class);
		} else if (RouterType.CISCO.equals(routerType)) {
			return appCtx.getBean(CiscoIllConfiguration.class);
		} else if (RouterType.JUNIPER.equals(routerType)) {
			return appCtx.getBean(JuniperIllConfiguration.class);
		} else {
			return null;
		}
	}

}
