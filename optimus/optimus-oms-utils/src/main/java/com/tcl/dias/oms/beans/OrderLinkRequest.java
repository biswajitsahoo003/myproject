package com.tcl.dias.oms.beans;

import org.springframework.lang.NonNull;

/** 
 * This file contains the details for the order site update request
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * 
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OrderLinkRequest {
	
	@NonNull
	private String mstOrderLinkStatusName;
	
	@NonNull
	private String mstOrderLinkStageName;
	
	public String getMstOrderLinkStatusName() {
		return mstOrderLinkStatusName;
	}

	public void setMstOrderLinkStatusName(String mstOrderLinkStatusName) {
		this.mstOrderLinkStatusName = mstOrderLinkStatusName;
	}

	public String getMstOrderLinkStageName() {
		return mstOrderLinkStageName;
	}

	public void setMstOrderLinkStageName(String mstOrderLinkStageName) {
		this.mstOrderLinkStageName = mstOrderLinkStageName;
	}



	

	
	

}
