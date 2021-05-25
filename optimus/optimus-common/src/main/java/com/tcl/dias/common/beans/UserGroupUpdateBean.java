package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the UserGroupUpdateBean.java class.
 * 
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserGroupUpdateBean {
	
	
	private Integer userGroupId;
	
	
	private List<UserGroupCustomerLegalEntityBean> userGroupLegalEntity;
	
	
	private List<Integer> userGroupToUser;


	/**
	 * @return the userGroupId
	 */
	public Integer getUserGroupId() {
		return userGroupId;
	}


	/**
	 * @param userGroupId the userGroupId to set
	 */
	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}




	public List<UserGroupCustomerLegalEntityBean> getUserGroupLegalEntity() {
		return userGroupLegalEntity;
	}


	public void setUserGroupLegalEntity(List<UserGroupCustomerLegalEntityBean> userGroupLegalEntity) {
		this.userGroupLegalEntity = userGroupLegalEntity;
	}


	/**
	 * @return the userGroupToUser
	 */
	public List<Integer> getUserGroupToUser() {
		
		if(userGroupToUser==null) {
			userGroupToUser=new ArrayList<>();
		}
		return userGroupToUser;
	}


	/**
	 * @param userGroupToUser the userGroupToUser to set
	 */
	public void setUserGroupToUser(List<Integer> userGroupToUser) {
		this.userGroupToUser = userGroupToUser;
	}
	
	
	
	
	
}