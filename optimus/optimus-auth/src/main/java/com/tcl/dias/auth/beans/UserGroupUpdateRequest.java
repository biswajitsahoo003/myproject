package com.tcl.dias.auth.beans;

import java.io.Serializable;
import java.util.List;

public class UserGroupUpdateRequest implements Serializable{
	
	private Integer userGroupId;
	private String userGroupType;
	private List<UserGroupToLeUpdateRequest> userGroupToLeUpdateRequests;
	private List<UserGroupToLeUpdateRequest> userGroupToPartnerLeUpdateRequests;
	private List<UserGroupToUserUpdateRequest> userGroupToUserUpdateRequests;
	public List<UserGroupToUserUpdateRequest> getUserGroupToUserUpdateRequests() {
		return userGroupToUserUpdateRequests;
	}
	public void setUserGroupToUserUpdateRequests(List<UserGroupToUserUpdateRequest> userGroupToUserUpdateRequests) {
		this.userGroupToUserUpdateRequests = userGroupToUserUpdateRequests;
	}
	public Integer getUserGroupId() {
		return userGroupId;
	}
	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}
	public String getUserGroupType() {
		return userGroupType;
	}
	public void setUserGroupType(String userGroupType) {
		this.userGroupType = userGroupType;
	}
	public List<UserGroupToLeUpdateRequest> getUserGroupToLeUpdateRequests() {
		return userGroupToLeUpdateRequests;
	}
	public void setUserGroupToLeUpdateRequests(List<UserGroupToLeUpdateRequest> userGroupToLeUpdateRequests) {
		this.userGroupToLeUpdateRequests = userGroupToLeUpdateRequests;
	}
	public List<UserGroupToLeUpdateRequest> getUserGroupToPartnerLeUpdateRequests() {
		return userGroupToPartnerLeUpdateRequests;
	}
	public void setUserGroupToPartnerLeUpdateRequests(List<UserGroupToLeUpdateRequest> userGroupToPartnerLeUpdateRequests) {
		this.userGroupToPartnerLeUpdateRequests = userGroupToPartnerLeUpdateRequests;
	}
	
}
