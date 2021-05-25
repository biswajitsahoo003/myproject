package com.tcl.dias.serviceactivation.activation.abs.macd.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.activation.abs.base.AbstractActivationFactory;
import com.tcl.dias.serviceactivation.activation.factory.base.AbstractConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.gvpn.macd.impl.AluGvpnMACDConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.gvpn.macd.impl.CiscoGvpnMACDConfiguration;
import com.tcl.dias.serviceactivation.activation.factory.gvpn.macd.impl.JuniperGvpnMACDConfiguration;
import com.tcl.dias.serviceactivation.activation.utils.RouterType;

/**
 * This file contains the GvpnMACDActivationFactory.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class GvpnMACDActivationFactory extends AbstractActivationFactory {

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
			return appCtx.getBean(AluGvpnMACDConfiguration.class);
		} else if (RouterType.CISCO.equals(routerType)) {
			return appCtx.getBean(CiscoGvpnMACDConfiguration.class);
		} else if (RouterType.JUNIPER.equals(routerType)) {
			return appCtx.getBean(JuniperGvpnMACDConfiguration.class);
		} else {
			return null;
		}
	}

}
