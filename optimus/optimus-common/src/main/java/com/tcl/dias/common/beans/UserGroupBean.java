package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * This file contains the UserGroupBean.java class.
 * 
 *
 * @author ANANDHI VIJAY
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserGroupBean implements Serializable{
	
	private Integer userGroupId;
	
	private String userGroupType;
	
	private String userGroupName;
	
	private Byte isActive;
	
	private Set<UserGroupToCustomerLeBean> userGroupToCustomerLes;

	private Set<UserGroupToUserBean> userGroupToUsers;
	
	private Integer noOfUsers;
	
	private Integer noOfLegalEntities;
	
	private Set<Integer> customerIds;
	
	private CustomerBean assignedCustomers;
	
	private List<LegalEntityBean> leNotPresentInTheGroup;
	
	private List<UserManagementBean> usersNotPresentInTheUserGroup;

	public List<LegalEntityBean> getLeNotPresentInTheGroup() {
		return leNotPresentInTheGroup;
	}

	public void setLeNotPresentInTheGroup(List<LegalEntityBean> leNotPresentInTheGroup) {
		this.leNotPresentInTheGroup = leNotPresentInTheGroup;
	}

	public List<UserManagementBean> getUsersNotPresentInTheUserGroup() {
		return usersNotPresentInTheUserGroup;
	}

	public void setUsersNotPresentInTheUserGroup(List<UserManagementBean> usersNotPresentInTheUserGroup) {
		this.usersNotPresentInTheUserGroup = usersNotPresentInTheUserGroup;
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

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public Byte getIsActive() {
		return isActive;
	}

	public void setIsActive(Byte isActive) {
		this.isActive = isActive;
	}

	public Set<UserGroupToCustomerLeBean> getUserGroupToCustomerLes() {
		return userGroupToCustomerLes;
	}

	public void setUserGroupToCustomerLes(Set<UserGroupToCustomerLeBean> userGroupToCustomerLes) {
		this.userGroupToCustomerLes = userGroupToCustomerLes;
	}

	public Set<UserGroupToUserBean> getUserGroupToUsers() {
		return userGroupToUsers;
	}

	public void setUserGroupToUsers(Set<UserGroupToUserBean> userGroupToUsers) {
		this.userGroupToUsers = userGroupToUsers;
	}

	public Integer getNoOfUsers() {
		return noOfUsers;
	}

	public void setNoOfUsers(Integer noOfUsers) {
		this.noOfUsers = noOfUsers;
	}

	public Integer getNoOfLegalEntities() {
		return noOfLegalEntities;
	}

	public void setNoOfLegalEntities(Integer noOfLegalEntities) {
		this.noOfLegalEntities = noOfLegalEntities;
	}

	public Set<Integer> getCustomerIds() {
		return customerIds;
	}

	public void setCustomerIds(Set<Integer> customerIds) {
		this.customerIds = customerIds;
	}

	public CustomerBean getAssignedCustomers() {
		return assignedCustomers;
	}

	public void setAssignedCustomers(CustomerBean assignedCustomers) {
		this.assignedCustomers = assignedCustomers;
	}

	
	
	
}
