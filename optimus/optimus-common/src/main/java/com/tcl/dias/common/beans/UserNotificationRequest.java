package com.tcl.dias.common.beans;

import java.io.Serializable;

public class UserNotificationRequest implements Serializable{
	
	private String userId;
	
	private String mddFilterValue;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getMddFilterValue() {
		return mddFilterValue;
	}

	public void setMddFilterValue(String mddFilterValue) {
		this.mddFilterValue = mddFilterValue;
	}
	
	

}
