package com.tcl.dias.auth.usermgmt.beans;

public class UserDetailBean {

	private String userName;
	private Integer userId;

	public UserDetailBean() {
		// DO NOTHING
	}

	public UserDetailBean(String userName, Integer userId) {
		this.userName = userName;
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "UserDetailBean [userName=" + userName + ", userId=" + userId + "]";
	}

}
