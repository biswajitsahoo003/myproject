package com.tcl.dias.oms.gsc.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.oms.sfdc.service.OmsSfdcService;

/**
 * Component to add all common services
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class GscOmsSfdcComponent {

	@Autowired
	OmsSfdcService omsSfdcService;

	public OmsSfdcService getOmsSfdcService() {
		return omsSfdcService;
	}

	public void setOmsSfdcService(OmsSfdcService omsSfdcService) {
		this.omsSfdcService = omsSfdcService;
	}
}
