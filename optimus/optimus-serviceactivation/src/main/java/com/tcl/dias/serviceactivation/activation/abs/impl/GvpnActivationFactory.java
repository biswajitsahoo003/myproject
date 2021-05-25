package com.tcl.dias.serviceactivation.activation.abs.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.activation.abs.base.AbstractActivationFactory;
import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.gvpn.impl.AluGvpnConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.gvpn.impl.CiscoGvpnConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.gvpn.impl.JuniperGvpnConfiguration;
import com.tcl.dias.serviceactivation.activation.utils.RouterType;

/**
 * This file contains the GvpnActivationFactory.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class GvpnActivationFactory extends AbstractActivationFactory {

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
			return appCtx.getBean(AluGvpnConfiguration.class);
		} else if (RouterType.CISCO.equals(routerType)) {
			return appCtx.getBean(CiscoGvpnConfiguration.class);
		} else if (RouterType.JUNIPER.equals(routerType)) {
			return appCtx.getBean(JuniperGvpnConfiguration.class);
		} else {
			return null;
		}
	}

}
