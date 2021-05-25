/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

/**
 * @author vivek
 *
 */
public class WbsTransferJeopardy extends BaseRequest{
	
	private String action;
	
	private String reason;

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	
	
	

}
