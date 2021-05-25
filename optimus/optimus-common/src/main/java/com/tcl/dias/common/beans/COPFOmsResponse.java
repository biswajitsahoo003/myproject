package com.tcl.dias.common.beans;

import java.util.List;
/**
 * 
 * This file contains the COPFOmsResponse bean class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class COPFOmsResponse {
	private Integer tpsId;
	private String status;
	private String message;
	private String errorCode;
	private List<String> customCOPFIDId = null;
	private List<COPFIDOmsList> COPFIDOmsList;
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
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public List<String> getCustomCOPFIDId() {
		return customCOPFIDId;
	}
	public void setCustomCOPFIDId(List<String> customCOPFIDId) {
		this.customCOPFIDId = customCOPFIDId;
	}
	public List<COPFIDOmsList> getCOPFIDOmsList() {
		return COPFIDOmsList;
	}
	public void setCOPFIDOmsList(List<COPFIDOmsList> cOPFIDOmsList) {
		COPFIDOmsList = cOPFIDOmsList;
	}
	
	

}
