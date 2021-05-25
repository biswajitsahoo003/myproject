package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;

public class UserManagementBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer userId;

	private String email;

	private String firstName;

	private String lastName;

	private String userName;

	private String userType;

	private Integer partnerId;

	private String designation;

	private String contactNumber;

	private Integer customerId;

	private List<String> roles;

	private String customerName;

	private String password;

	private Boolean isNotifyCustomer;

	private List<UserGroupToUserBean> userGroupToUsers;

	private List<Integer> userGroupIds;

	private Byte forceChangePassword;

	private List<KeycloakIdentityProviderBean> keycloakIdentityProviderBeans;

	public Byte getIsOtpEnabled() {
		return isOtpEnabled;
	}

	public void setIsOtpEnabled(Byte isOtpEnabled) {
		this.isOtpEnabled = isOtpEnabled;
	}

	private Boolean isConfigureOtpEnabled;
	
	private Byte isOtpEnabled;

	public Boolean getIsConfigureOtpEnabled() {
		return isConfigureOtpEnabled;
	}

	public void setIsConfigureOtpEnabled(Boolean isConfigureOtpEnabled) {
		this.isConfigureOtpEnabled = isConfigureOtpEnabled;
	}

	public List<Integer> getUserGroupIds() {
		return userGroupIds;
	}

	public void setUserGroupIds(List<Integer> userGroupIds) {
		this.userGroupIds = userGroupIds;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Integer getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userGroupToUsers
	 */
	public List<UserGroupToUserBean> getUserGroupToUsers() {
		return userGroupToUsers;
	}

	/**
	 * @param userGroupToUsers
	 *            the userGroupToUsers to set
	 */
	public void setUserGroupToUsers(List<UserGroupToUserBean> userGroupToUsers) {
		this.userGroupToUsers = userGroupToUsers;
	}

	/**
	 * @return the forceChangePassword
	 */
	public Byte getForceChangePassword() {
		return forceChangePassword;
	}

	/**
	 * @param forceChangePassword
	 *            the forceChangePassword to set
	 */
	public void setForceChangePassword(Byte forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}

	public List<KeycloakIdentityProviderBean> getKeycloakIdentityProviderBeans() {
		return keycloakIdentityProviderBeans;
	}

	public void setKeycloakIdentityProviderBeans(List<KeycloakIdentityProviderBean> keycloakIdentityProviderBeans) {
		this.keycloakIdentityProviderBeans = keycloakIdentityProviderBeans;
	}

	public Boolean getIsNotifyCustomer() {
		return isNotifyCustomer;
	}

	public void setIsNotifyCustomer(Boolean isNotifyCustomer) {
		this.isNotifyCustomer = isNotifyCustomer;
	}

	@Override
	public String toString() {
		return "UserManagementBean [userId=" + userId + ", email=" + email + ", firstName=" + firstName + ", lastName="
				+ lastName + ", userName=" + userName + ", userType=" + userType + ", partnerId=" + partnerId
				+ ", designation=" + designation + ", contactNumber=" + contactNumber + ", customerId=" + customerId
				+ ", roles=" + roles + ", customerName=" + customerName + ", password=" + password
				+ ", isNotifyCustomer=" + isNotifyCustomer + ", userGroupToUsers=" + userGroupToUsers
				+ ", userGroupIds=" + userGroupIds + ", forceChangePassword=" + forceChangePassword
				+ ", keycloakIdentityProviderBeans=" + keycloakIdentityProviderBeans + ", isConfigureOtpEnabled="
				+ isConfigureOtpEnabled + ", isOtpEnabled=" + isOtpEnabled + "]";
	}

	

}
