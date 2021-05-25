package com.tcl.dias.serviceactivation.activation.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tcl.dias.serviceactivation.activation.abs.base.AbstractActivationFactory;
import com.tcl.dias.serviceactivation.activation.abs.impl.GvpnActivationFactory;
import com.tcl.dias.serviceactivation.activation.abs.impl.IllActivationFactory;
import com.tcl.dias.serviceactivation.activation.abs.impl.NPLActivationFactory;
import com.tcl.dias.serviceactivation.activation.abs.macd.impl.GvpnMACDActivationFactory;
import com.tcl.dias.serviceactivation.activation.abs.macd.impl.IllMACDActivationFactory;
import com.tcl.dias.serviceactivation.activation.utils.Product;

/**
 * This file contains the NetpConfigProducer.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Component
public class ActivationConfigProducer {

	@Autowired
	ApplicationContext appCtx;

	public AbstractActivationFactory getProductConfig(Product product) {
		if (Product.ILL.equals(product) || Product.IAS.equals(product)) {
			return appCtx.getBean(IllActivationFactory.class);
		} else if (Product.GVPN.equals(product)) {
			return appCtx.getBean(GvpnActivationFactory.class);
		} else if (Product.ILL_MACD.equals(product) || Product.IAS_MACD.equals(product)) {
			return appCtx.getBean(IllMACDActivationFactory.class);
		}else if (Product.GVPN_MACD.equals(product)) {
			return appCtx.getBean(GvpnMACDActivationFactory.class);
		}else if (Product.NPL.equals(product)) {
			return appCtx.getBean(NPLActivationFactory.class);
		}else {
			return null;
		}
	}

}
