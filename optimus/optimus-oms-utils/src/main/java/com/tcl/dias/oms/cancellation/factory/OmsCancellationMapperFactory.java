package com.tcl.dias.oms.cancellation.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.tcl.dias.oms.cancellation.core.OmsCancellationHandler;
import com.tcl.dias.oms.sfdc.core.IOmsSfdcInputHandler;

@Component
public class OmsCancellationMapperFactory {
	
	@Autowired
	private ApplicationContext appCtx;

	public OmsCancellationHandler getInstance(String type) {

		OmsCancellationHandler instance = null;

		switch (type) {
		case "GVPN":
			return appCtx.getBean("omsCancellationGvpnMapper", OmsCancellationHandler.class);
		case "NPL":
			return appCtx.getBean("omsCancellationNplMapper", OmsCancellationHandler.class);
		case "IAS":
			return appCtx.getBean("omsCancellationIasMapper", OmsCancellationHandler.class);
		case "IZOPC":
			return appCtx.getBean("omsCancellationIzopcMapper", OmsCancellationHandler.class);
		case "NDE":
			return appCtx.getBean("omsCancellationNdeMapper", OmsCancellationHandler.class);
		case "IPC":
			return appCtx.getBean("omsCancellationIPCMapper", OmsCancellationHandler.class);
			
		default:
			return instance;
		}

	}


}
