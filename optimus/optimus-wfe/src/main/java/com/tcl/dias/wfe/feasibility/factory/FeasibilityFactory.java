package com.tcl.dias.wfe.feasibility.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.wfe.constants.WFEConstants;
import com.tcl.dias.wfe.feasibility.consumer.RFeasibilityConsumer;
import com.tcl.dias.wfe.feasibility.service.GvpnFeasibilityService;
import com.tcl.dias.wfe.feasibility.service.IzoPcFeasibilityService;
import com.tcl.dias.wfe.feasibility.service.NplFeasibilityService;
import com.tcl.dias.wfe.feasibility.service.RFeasibilityService;
/**
 * This factory class is used to get the respective product service instance.
 * @author PAULRAJ SUNDAR
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class FeasibilityFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeasibilityFactory.class);

	@Autowired
	private RFeasibilityService illFeasibilityService;

	@Autowired
	private NplFeasibilityService nplFeasibilityService;

	@Autowired
	private GvpnFeasibilityService gvpnFeasibilityService;
	
	@Autowired
	private IzoPcFeasibilityService izoPcFeasibilityService;

	public FeasibilityMapper getFeasibilityInstance(String productName) {

		switch (productName) {
		case WFEConstants.IAS_ILL:
			return illFeasibilityService;
		case WFEConstants.GVPN:
			return gvpnFeasibilityService;
		case WFEConstants.NPL:
			return nplFeasibilityService;
		case WFEConstants.IZO_PC:
			return izoPcFeasibilityService;
		case WFEConstants.NDE:
			return nplFeasibilityService;	
		default:
			return null;
		}

	}

}
