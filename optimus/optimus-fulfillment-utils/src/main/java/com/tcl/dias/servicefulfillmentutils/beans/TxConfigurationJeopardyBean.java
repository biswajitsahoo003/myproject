package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * TxConfigurationJeopardyBean - bean for TxConfigurationJeopardy
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TxConfigurationJeopardyBean extends BaseRequest {

	private String action;
	private String isResolved;

	
	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getIsResolved() {
		return isResolved;
	}

	public void setIsResolved(String isResolved) {
		this.isResolved = isResolved;
	}

}
