package com.tcl.dias.common.redis.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * User and user role information class
 * 
 * @author Manojkumar R
 *
 */
public class UserInformation implements Serializable {

	private static final long serialVersionUID = -270724365993934930L;
	private String userId;
	private String firstName;
	private String lastName;
	private String userType;
	private List<String> userToUserGroupName;
	private String partnerId;
	private List<String> role;
	private Map<String, Object> actions;
	private List<CustomerDetail> customers;
	private List<PartnerDetail> partners;
	private Boolean pmi = false;
	private Boolean pmiro = false;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public List<String> getRole() {
		return role;
	}

	public void setRole(List<String> role) {
		this.role = role;
	}

	public List<CustomerDetail> getCustomers() {
		return customers;
	}

	public void setCustomers(List<CustomerDetail> customers) {
		this.customers = customers;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public List<String> getUserToUserGroupName() {
		return userToUserGroupName;
	}

	public void setUserToUserGroupName(List<String> userToUserGroupName) {
		this.userToUserGroupName = userToUserGroupName;
	}

	public Map<String, Object> getActions() {
		return actions;
	}

	public void setActions(Map<String, Object> actions) {
		this.actions = actions;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public List<PartnerDetail> getPartners() {
		return partners;
	}

	public void setPartners(List<PartnerDetail> partners) {
		this.partners = partners;
	}

	public Boolean getPmi() {
		return pmi;
	}

	public void setPmi(Boolean pmi) {
		this.pmi = pmi;
	}
	
	public Boolean getPmiro() {
		return pmiro;
	}

	public void setPmiro(Boolean pmiro) {
		this.pmiro = pmiro;
	}

	@Override
	public String toString() {
		return "UserInformation [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", userType=" + userType + ", userToUserGroupName=" + userToUserGroupName + ", partnerId=" + partnerId
				+ ", role=" + role + ", actions=" + actions + ", customers=" + customers + ", partners=" + partners
				+ ", pmi=" + pmi + "]";
	}


}
