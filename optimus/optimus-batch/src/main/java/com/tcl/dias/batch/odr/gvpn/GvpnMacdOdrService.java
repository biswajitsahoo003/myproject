package com.tcl.dias.batch.odr.gvpn;

import org.springframework.stereotype.Service;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;

/**
 * This class is used to define the Gvpn related Order flat table
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class GvpnMacdOdrService extends OrderService {

	protected String getReferenceName() {
		return OdrConstants.GVPN_SITES;
	}

}
