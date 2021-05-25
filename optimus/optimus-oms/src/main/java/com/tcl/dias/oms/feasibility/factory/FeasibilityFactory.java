package com.tcl.dias.oms.feasibility.factory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcl.dias.common.constants.FeasibilityConstants;
import com.tcl.dias.oms.gvpn.service.v1.GvpnPricingFeasibilityService;
import com.tcl.dias.oms.ill.service.v1.IllPricingFeasibilityService;
import com.tcl.dias.oms.izopc.service.v1.IzoPcPricingFeasibilityService;
import com.tcl.dias.oms.npl.service.v1.NplPricingFeasibilityService;
/**
 * This factory is used to get the respective product service.
 * @author PAULRAJ SUNDAR
 * * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Component
public class FeasibilityFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(FeasibilityFactory.class);

	@Autowired
	private IllPricingFeasibilityService illPricingFeasibilityService;

	@Autowired
	private NplPricingFeasibilityService nplPricingFeasibilityService;

	@Autowired
	private  GvpnPricingFeasibilityService gvpnPricingFeasibilityService;
	
	@Autowired
	private  IzoPcPricingFeasibilityService izoPcPricingFeasibilityService;
/**
 * This method will return an instance based on the product.
 * @param productName
 * @return
 */
	public FeasibilityMapper getFeasibilityInstance(String productName) {

		switch (productName) {
		case FeasibilityConstants.IAS_ILL:
			return illPricingFeasibilityService;
		case FeasibilityConstants.GVPN:
			return gvpnPricingFeasibilityService;
		case FeasibilityConstants.NPL:
			return nplPricingFeasibilityService;
		case FeasibilityConstants.IZO_PC:
			return izoPcPricingFeasibilityService;
		case FeasibilityConstants.NDE:
			return nplPricingFeasibilityService;
		default:
			return null;
		}

	}

}
