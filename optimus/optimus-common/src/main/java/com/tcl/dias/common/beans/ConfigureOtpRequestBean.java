package com.tcl.dias.common.beans;

import java.io.Serializable;
/**
 * 
 * This file contains the ConfigureOtpRequestBean.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ConfigureOtpRequestBean implements Serializable{
	private String action;
	private Integer userId;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	
}
