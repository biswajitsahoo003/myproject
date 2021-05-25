package com.tcl.dias.oms.webex.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.oms.sfdc.service.OmsSfdcService;

/**
 * Component to add all common services
 *
 * @author Syed Ali
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Component
public class UcaasOmsSfdcComponent {

	@Autowired
	OmsSfdcService omsSfdcService;

	public OmsSfdcService getOmsSfdcService() {
		return omsSfdcService;
	}

	public void setOmsSfdcService(OmsSfdcService omsSfdcService) {
		this.omsSfdcService = omsSfdcService;
	}

}
