package com.tcl.dias.auth.beans;

import java.util.List;
import java.util.Set;


/**
 * This UserInfoBean.java class used to edit the user details.
 * 
 *
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserInfoBean {

	private String userId;

	private String contactNo;

	private String emailId;

	private String firstName;

	private String lastName;

	private Integer partnerId;

	private Integer vendorId;

	private Integer status;

	private String userType;

	private String designation;

	private String username;

	private Integer customerId;

	private List<Integer> leId;

	private Integer quoteToLeId;

	private String action;

	private List<String> roleActions;

	private String groupRefUsername;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	

	public List<Integer> getLeId() {
		return leId;
	}

	public void setLeId(List<Integer> leId) {
		this.leId = leId;
	}

	public Integer getQuoteToLeId() {
		return quoteToLeId;
	}

	public void setQuoteToLeId(Integer quoteToLeId) {
		this.quoteToLeId = quoteToLeId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<String> getRoleActions() {
		return roleActions;
	}

	public void setRoleActions(List<String> roleActions) {
		this.roleActions = roleActions;
	}

	public String getGroupRefUsername() {
		return groupRefUsername;
	}

	public void setGroupRefUsername(String groupRefUsername) {
		this.groupRefUsername = groupRefUsername;
	}

	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}

	@Override
	public String toString() {
		return "UserBean [userId=" + userId + ", contactNo=" + contactNo + ", emailId=" + emailId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", partnerId=" + partnerId + ", vendorId=" + vendorId
				+ ", status=" + status + ", userType=" + userType + ", designation=" + designation + ", username="
				+ username + ", customerId=" + customerId + ", leId=" + leId + ", quoteToLeId=" + quoteToLeId
				+ ", action=" + action + ", roleActions=" + roleActions + ", groupRefUsername=" + groupRefUsername
				+ "]";
	}

}
