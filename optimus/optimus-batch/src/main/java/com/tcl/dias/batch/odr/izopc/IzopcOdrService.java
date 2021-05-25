package com.tcl.dias.batch.odr.izopc;

import org.springframework.stereotype.Service;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;

/**
 * 
 * This file contains the IzopcOdrService.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IzopcOdrService extends OrderService {

	/**
	 * 
	 * getReferenceName for IZO PC 
	 * @return
	 */
	protected String getReferenceName() {
		return OdrConstants.IZO_PC_SITES;

	}

}
