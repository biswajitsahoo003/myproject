package com.tcl.common.keycloack.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the KeyCloackRoles.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class KeyCloackRoles {
	
	
	private List<RolesBean> roles;

	/**
	 * @return the roles
	 */
	public List<RolesBean> getRoles() {
		
		if(roles==null) {
			roles=new ArrayList<>();
		}
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<RolesBean> roles) {
		this.roles = roles;
	}
	
	
	
	

}
