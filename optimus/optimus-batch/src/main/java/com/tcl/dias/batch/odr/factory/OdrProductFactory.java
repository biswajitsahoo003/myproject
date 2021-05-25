package com.tcl.dias.batch.odr.factory;


import com.tcl.dias.batch.odr.gsip.GscOdrService;
import com.tcl.dias.batch.odr.gsip.WebexGscOdrService;
import com.tcl.dias.batch.odr.ipc.IpcOrdService;
import com.tcl.dias.batch.odr.izopc.IzopcOdrService;
import com.tcl.dias.batch.odr.izosdwan.IzosdwanOdrService;
import com.tcl.dias.batch.odr.npl.NplOdrService;
import com.tcl.dias.batch.odr.teamsdr.TeamsDROdrService;
import com.tcl.dias.batch.odr.webex.WebexOdrService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;
import com.tcl.dias.batch.odr.gde.GdeOdrService;
import com.tcl.dias.batch.odr.gvpn.GvpnOdrService;
import com.tcl.dias.batch.odr.ill.IasOdrService;

/**
 * This class acts as the factory in choosing the product and trigger the
 * service
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OdrProductFactory {

	@Autowired
	ApplicationContext appContext;

	/**
	 * 
	 * processOrderFreeze - This method choose the product and process the service
	 * 
	 * @param productName
	 * @param orderId
	 * @return
	 */
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_UNCOMMITTED)
	public Boolean processOrderFreeze(String productName, Integer orderId, String username) {
		OrderService orderService;
		switch (productName.toUpperCase()) {
		case OdrConstants.IAS:
			orderService = appContext.getBean(IasOdrService.class);
			break;
		case OdrConstants.GVPN:
			orderService = appContext.getBean(GvpnOdrService.class);
			break;
		case OdrConstants.IPC:
			orderService = appContext.getBean(IpcOrdService.class);
			break;
		case OdrConstants.NPL:
			orderService = appContext.getBean(NplOdrService.class);
			break;
		case OdrConstants.IAS_MACD:
			orderService = appContext.getBean(IasOdrService.class);
			break;
		case OdrConstants.GVPN_MACD:
			orderService = appContext.getBean(GvpnOdrService.class);
			break;
		case OdrConstants.GDE:
			orderService = appContext.getBean(GdeOdrService.class);
			break;
		case OdrConstants.IZOSDWAN:
		case OdrConstants.IZO_SDWAN:
			orderService = appContext.getBean(IzosdwanOdrService.class);
			break;
		case OdrConstants.UCAAS_WEBEX:
			orderService = appContext.getBean(WebexOdrService.class);
			break;
		case OdrConstants.GSIP:
				orderService = appContext.getBean(GscOdrService.class);
				break;
		case OdrConstants.GSIP_MACD:
				orderService = appContext.getBean(GscOdrService.class);
				break;
		case OdrConstants.TEAMSDR:
				orderService = appContext.getBean(TeamsDROdrService.class);
				break;
		case OdrConstants.IZOPC:
			orderService = appContext.getBean(IzopcOdrService.class);
			break;
		case OdrConstants.IZOPCMACD:
			orderService = appContext.getBean(IzopcOdrService.class);
			break;
		default:
			throw new IllegalArgumentException("No product defined");
		}
		return orderService.processOrderFrost(orderId, username);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_UNCOMMITTED)
	public Boolean processOrderEnrichmentFreeze(String productName, Integer orderId, String username) {
		OrderService orderService;
		switch (productName.toUpperCase()) {
		case OdrConstants.IAS:
			orderService = appContext.getBean(IasOdrService.class);
			break;
		case OdrConstants.GVPN:
			orderService = appContext.getBean(GvpnOdrService.class);
			break;
		case OdrConstants.NPL:
			orderService = appContext.getBean(NplOdrService.class);
			break;
		case OdrConstants.IZOSDWAN:
			orderService = appContext.getBean(IzosdwanOdrService.class);
			break;
		case OdrConstants.UCAAS_WEBEX:
			orderService = appContext.getBean(WebexOdrService.class);
			break;
		case OdrConstants.IZOPC:
			orderService = appContext.getBean(IzopcOdrService.class);
			break;
		default:
			throw new IllegalArgumentException("No product defined");
		}
		return orderService.processOrderEnrichment(orderId, username);
	}
	
	@Transactional(propagation=Propagation.REQUIRED,isolation=Isolation.READ_UNCOMMITTED)
	public Boolean processOrderUpdate(String productName, Integer siteId, String username) {
		OrderService orderService;
		switch (productName.toUpperCase()) {
		case OdrConstants.IAS:
			orderService = appContext.getBean(IasOdrService.class);
			break;
		case OdrConstants.GVPN:
			orderService = appContext.getBean(GvpnOdrService.class);
			break;
		case OdrConstants.NPL:
			orderService = appContext.getBean(NplOdrService.class);
			break;
		case OdrConstants.IZOSDWAN:
			orderService = appContext.getBean(IzosdwanOdrService.class);
			break;
		case OdrConstants.UCAAS_WEBEX:
			orderService = appContext.getBean(WebexOdrService.class);
			break;
		case OdrConstants.IZOPC:
			orderService = appContext.getBean(IzopcOdrService.class);
			break;
		default:
			throw new IllegalArgumentException("No product defined");
		}
		return orderService.processStatus(siteId, username);
	}

}
