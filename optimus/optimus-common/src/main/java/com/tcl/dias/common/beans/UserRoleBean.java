package com.tcl.dias.common.beans;

import java.io.Serializable;

/**
 * 
 * This file contains the UserRoleBean.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserRoleBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private String roleName;

	private String userName;

	private String description;

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UserRoleBean [roleName=" + roleName + ", userName=" + userName + ", description=" + description + "]";
	}

}
