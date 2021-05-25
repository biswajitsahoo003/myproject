package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ColoRequest extends TaskDetailsBaseBean {
	
	private String ipSiteId;
	private String ipSiteName;
	private String isTTIdRequired;
	private String ttId;
	private String ttIdRemarks;
	private String coloProvider;
	private String isColoFeasible;
	
	public String getIpSiteId() {
		return ipSiteId;
	}
	public void setIpSiteId(String ipSiteId) {
		this.ipSiteId = ipSiteId;
	}
	public String getIpSiteName() {
		return ipSiteName;
	}
	public void setIpSiteName(String ipSiteName) {
		this.ipSiteName = ipSiteName;
	}
	public String getIsTTIdRequired() {
		return isTTIdRequired;
	}
	public void setIsTTIdRequired(String isTTIdRequired) {
		this.isTTIdRequired = isTTIdRequired;
	}
	public String getTtId() {
		return ttId;
	}
	public void setTtId(String ttId) {
		this.ttId = ttId;
	}
	public String getTtIdRemarks() {
		return ttIdRemarks;
	}
	public void setTtIdRemarks(String ttIdRemarks) {
		this.ttIdRemarks = ttIdRemarks;
	}
	public String getColoProvider() {
		return coloProvider;
	}
	public void setColoProvider(String coloProvider) {
		this.coloProvider = coloProvider;
	}
	public String getIsColoFeasible() {
		return isColoFeasible;
	}
	public void setIsColoFeasible(String isColoFeasible) {
		this.isColoFeasible = isColoFeasible;
	}
	
	
}
