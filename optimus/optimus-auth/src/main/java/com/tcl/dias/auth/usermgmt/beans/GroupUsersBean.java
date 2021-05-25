package com.tcl.dias.auth.usermgmt.beans;

import java.util.ArrayList;
import java.util.List;

public class GroupUsersBean {

	private List<UserDetailBean> additionalUsers = new ArrayList<>();
	private List<UserDetailBean> reductionalUsers = new ArrayList<>();

	public List<UserDetailBean> getAdditionalUsers() {
		return additionalUsers;
	}

	public void setAdditionalUsers(List<UserDetailBean> additionalUsers) {
		this.additionalUsers = additionalUsers;
	}

	public List<UserDetailBean> getReductionalUsers() {
		return reductionalUsers;
	}

	public void setReductionalUsers(List<UserDetailBean> reductionalUsers) {
		this.reductionalUsers = reductionalUsers;
	}

	@Override
	public String toString() {
		return "GroupUsersBean [additionalUsers=" + additionalUsers + ", reductionalUsers=" + reductionalUsers + "]";
	}

}
