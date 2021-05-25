package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class RfData extends TaskDetailsBaseBean {
	
	private String isColoRequired;
	private String ipSiteId;
	private String swConvertorRequired;
	private String btsConverterIp;
	private String bsSwitchHostName;
	private String bsSwitchIp;
	
	public String getIsColoRequired() {
		return isColoRequired;
	}
	public void setIsColoRequired(String isColoRequired) {
		this.isColoRequired = isColoRequired;
	}
	public String getIpSiteId() {
		return ipSiteId;
	}
	public void setIpSiteId(String ipSiteId) {
		this.ipSiteId = ipSiteId;
	}
	public String getSwConvertorRequired() {
		return swConvertorRequired;
	}
	public void setSwConvertorRequired(String swConvertorRequired) {
		this.swConvertorRequired = swConvertorRequired;
	}
	public String getBtsConverterIp() {
		return btsConverterIp;
	}
	public void setBtsConverterIp(String btsConverterIp) {
		this.btsConverterIp = btsConverterIp;
	}
	public String getBsSwitchHostName() {
		return bsSwitchHostName;
	}
	public void setBsSwitchHostName(String bsSwitchHostName) {
		this.bsSwitchHostName = bsSwitchHostName;
	}
	public String getBsSwitchIp() {
		return bsSwitchIp;
	}
	public void setBsSwitchIp(String bsSwitchIp) {
		this.bsSwitchIp = bsSwitchIp;
	}

	

}
