package com.tcl.dias.common.sfdc.bean;

import java.util.List;
/**
 * This file contains the BCROmsResponse.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


public class BCROmsResponse {
    private String status;
    private String message;
    private String errorcode;
    private List<String> customBCRId = null;
    private Integer tpsId;
    public Integer getTpsId() {
		return tpsId;
	}
	public void setTpsId(Integer tpsId) {
		this.tpsId = tpsId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public List<String> getCustomBCRId() {
		return customBCRId;
	}
	public void setCustomBCRId(List<String> customBCRId) {
		this.customBCRId = customBCRId;
	}
	public List<Object> getbCRList() {
		return bCRList;
	}
	public void setbCRList(List<Object> bCRList) {
		this.bCRList = bCRList;
	}
	private List<Object> bCRList = null;
}
