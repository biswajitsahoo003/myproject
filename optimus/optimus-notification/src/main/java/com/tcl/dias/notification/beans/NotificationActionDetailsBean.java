package com.tcl.dias.notification.beans;

import java.io.Serializable;
/**
 * 
 * 
 * 
 * This bean is used for fetching and updating user notification subscriptions
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class NotificationActionDetailsBean implements Serializable{
	private Integer actionId;
	private String actionName;
	private Boolean isEnabled;
	public Integer getActionId() {
		return actionId;
	}
	public void setActionId(Integer actionId) {
		this.actionId = actionId;
	}
	public String getActionName() {
		return actionName;
	}
	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	public Boolean getIsEnabled() {
		return isEnabled;
	}
	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
}
