package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ColoPO extends TaskDetailsBaseBean {

	private String isColoPORequired;
	private String coloPONumber;
	private String coloProvider;
	private String coloPORemarks;
	private String ttIdShared;
	
	public String getIsColoPORequired() {
		return isColoPORequired;
	}
	public void setIsColoPORequired(String isColoPORequired) {
		this.isColoPORequired = isColoPORequired;
	}
	public String getColoPONumber() {
		return coloPONumber;
	}
	public void setColoPONumber(String coloPONumber) {
		this.coloPONumber = coloPONumber;
	}
	public String getColoProvider() {
		return coloProvider;
	}
	public void setColoProvider(String coloProvider) {
		this.coloProvider = coloProvider;
	}
	public String getColoPORemarks() {
		return coloPORemarks;
	}
	public void setColoPORemarks(String coloPORemarks) {
		this.coloPORemarks = coloPORemarks;
	}
	public String getTtIdShared() {
		return ttIdShared;
	}
	public void setTtIdShared(String ttIdShared) {
		this.ttIdShared = ttIdShared;
	}
	
	
}
