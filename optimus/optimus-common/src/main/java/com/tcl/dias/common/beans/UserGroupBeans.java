package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

public class UserGroupBeans {
	
	private List<UserBean> user;

	public List<UserBean> getUser() {
		if(user ==null) {
			user=new ArrayList<>();
		}
		return user;
	}

	public void setUser(List<UserBean> user) {
		this.user = user;
	}
	
	

}
