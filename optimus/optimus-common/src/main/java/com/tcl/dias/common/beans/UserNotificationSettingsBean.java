package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;

public class UserNotificationSettingsBean implements Serializable{
	private String userId;
	
	private List<NotificationActionBean> notificationActionDetails;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<NotificationActionBean> getNotificationActionDetails() {
		return notificationActionDetails;
	}

	public void setNotificationActionDetails(List<NotificationActionBean> notificationActionDetails) {
		this.notificationActionDetails = notificationActionDetails;
	}
	
}
