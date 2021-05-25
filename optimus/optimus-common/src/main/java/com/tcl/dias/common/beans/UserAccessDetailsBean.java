package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Bean for user access details
 * @author archchan
 *
 */
public class UserAccessDetailsBean implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String userName;
	private List<UserProductsBean> inventoryList;
	private Map<String, Object> userActions;
	private List<String> roles = new ArrayList<String>();
	private String userType;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<UserProductsBean> getInventoryList() {
		return inventoryList;
	}
	public void setInventoryList(List<UserProductsBean> inventoryList) {
		this.inventoryList = inventoryList;
	}
	public Map<String, Object> getUserActions() {
		return userActions;
	}
	public void setUserActions(Map<String, Object> userActions) {
		this.userActions = userActions;
	}
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	@Override
	public String toString() {
		return "UserAccessDetailsBean [userName=" + userName + ", inventoryList=" + inventoryList + ", userActions="
				+ userActions + ", roles=" + roles + ", userType=" + userType + "]";
	}	
	
}
