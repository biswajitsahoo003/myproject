package com.tcl.dias.oms.beans;

/**
 * 
 * This file contains the bean to display the Old and New attribute of the IPC
 * MACD order which will used in order summary screen.
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class IPCMACDAttributeSummaryResponse {

	private String type;
	private String oldAttrName;
	private String oldAttrVal;
	private String newAttrName;
	private String newAttrVal;

	private IPCMACDPricingBeanResponse ipcMacdPricingBeanResponse;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getOldAttrName() {
		return oldAttrName;
	}

	public void setOldAttrName(String oldAttrName) {
		this.oldAttrName = oldAttrName;
	}

	public String getOldAttrVal() {
		return oldAttrVal;
	}

	public void setOldAttrVal(String oldAttrVal) {
		this.oldAttrVal = oldAttrVal;
	}

	public String getNewAttrName() {
		return newAttrName;
	}

	public void setNewAttrName(String newAttrName) {
		this.newAttrName = newAttrName;
	}

	public String getNewAttrVal() {
		return newAttrVal;
	}

	public void setNewAttrVal(String newAttrVal) {
		this.newAttrVal = newAttrVal;
	}

	public IPCMACDPricingBeanResponse getIpcMacdPricingBeanResponse() {
		return ipcMacdPricingBeanResponse;
	}

	public void setIpcMacdPricingBeanResponse(IPCMACDPricingBeanResponse ipcMacdPricingBeanResponse) {
		this.ipcMacdPricingBeanResponse = ipcMacdPricingBeanResponse;
	}

}
