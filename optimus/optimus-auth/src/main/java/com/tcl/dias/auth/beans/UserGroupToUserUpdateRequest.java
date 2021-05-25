package com.tcl.dias.auth.beans;

public class UserGroupToUserUpdateRequest {
	
	private Integer userMappingId;
	private Integer userId;
	private String userName;
	public Integer getUserMappingId() {
		return userMappingId;
	}
	public void setUserMappingId(Integer userMappingId) {
		this.userMappingId = userMappingId;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
