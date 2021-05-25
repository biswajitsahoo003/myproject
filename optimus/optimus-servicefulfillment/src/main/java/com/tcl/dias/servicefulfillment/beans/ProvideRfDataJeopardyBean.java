package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * Bean class for Rf Data Jeopardy
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class ProvideRfDataJeopardyBean extends BaseRequest{

	private String rfDataJeopardyRemarks;

	public String getRfDataJeopardyRemarks() {
		return rfDataJeopardyRemarks;
	}

	public void setRfDataJeopardyRemarks(String rfDataJeopardyRemarks) {
		this.rfDataJeopardyRemarks = rfDataJeopardyRemarks;
	}

}
