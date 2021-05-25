package com.tcl.dias.batch.odr.ill;

import org.springframework.stereotype.Service;

import com.tcl.dias.batch.odr.base.service.OrderService;
import com.tcl.dias.batch.odr.constants.OdrConstants;

/**
 * This class is used to define the ILL related Order flat table
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Service
public class IasOdrService extends OrderService {

	/**
	 * getIllSiteType
	 */
	protected String getReferenceName() {
		return OdrConstants.ILLSITES;

	}

}
