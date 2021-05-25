package com.tcl.dias.common.beans;

import java.io.Serializable;
/**
 * 
 * This file contains the IzopcDcRequestBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzopcDcRequestBean implements Serializable{
	private String popId;
	private String cloudProvider;
	public String getPopId() {
		return popId;
	}
	public void setPopId(String popId) {
		this.popId = popId;
	}
	public String getCloudProvider() {
		return cloudProvider;
	}
	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}
	
}
