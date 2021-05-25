package com.tcl.dias.oms.sfdc.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;

/**
 * This file contains the OmsSfdcInputMapper.java class.
 * 
 *
 * @author MRajakum
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class OmsSfdcInputMapperFactory {

	@Autowired
	private ApplicationContext appCtx;

	public IOmsSfdcInputHandler getInstance(String type) {

		IOmsSfdcInputHandler instance = null;

		switch (type) {
		case "GVPN":
			return appCtx.getBean("omsSfdcGvpnMapper", IOmsSfdcInputHandler.class);
		case "NPL":
			return appCtx.getBean("omsSfdcNplMapper", IOmsSfdcInputHandler.class);
		case "IAS":
			return appCtx.getBean("omsSfdcIASMapper", IOmsSfdcInputHandler.class);
		case "IZO Internet WAN":
			return appCtx.getBean("omsSfdcIWANMapper", IOmsSfdcInputHandler.class);
		case "GSIP":
			return appCtx.getBean("omsSfdcGscMapper", IOmsSfdcInputHandler.class);
		case "IZOPC":
			return appCtx.getBean("omsSfdcIzopcMapper", IOmsSfdcInputHandler.class);
		case "IPC":
			return appCtx.getBean("omsSfdcIPCMapper", IOmsSfdcInputHandler.class);
		case "NDE":
			return appCtx.getBean("omsSfdcNdeMapper", IOmsSfdcInputHandler.class);
		case "UCAAS":
			return appCtx.getBean("omsSfdcUcaasMapper", IOmsSfdcInputHandler.class);
		case "Microsoft Cloud Solutions":
			return appCtx.getBean("omsSfdcTeamsDRMapper", IOmsSfdcInputHandler.class);
		default:
			return instance;
		}

	}

}
