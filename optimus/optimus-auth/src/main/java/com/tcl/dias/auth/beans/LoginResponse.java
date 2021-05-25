package com.tcl.dias.auth.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Manojkumar R
 *
 */
public class LoginResponse {

	private String username;
	private Integer customerId;
	private List<String> roles = new ArrayList<String>();
	private Map<String, Object> userActions;
	private Byte forceChangePassword;
	private Byte isOtpEnabled;
	private String firstName;
	private String lastName;
	private String emailId;
	private String platform;
	private List<Map<String, Object>> userGroups;
	private String userType;
	private Boolean pmi = false;
	private Boolean pmiro = false;
	private String partnerId;
	private List<Map<String, Object>> engagementProducts;
	private Byte IsPartnerGeneralTermsApproved;
	private Byte showCosMessage;
	
	private boolean internalUser;
	
	
	

	/**
	 * @return the internalUser
	 */
	public boolean isInternalUser() {
		return internalUser;
	}

	/**
	 * @param internalUser the internalUser to set
	 */
	public void setInternalUser(boolean internalUser) {
		this.internalUser = internalUser;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public Map<String, Object> getUserActions() {
		return userActions;
	}

	public void setUserActions(Map<String, Object> userActions) {
		this.userActions = userActions;
	}

	public Byte getForceChangePassword() {
		return forceChangePassword;
	}

	public void setForceChangePassword(Byte forceChangePassword) {
		this.forceChangePassword = forceChangePassword;
	}

	public Byte getIsOtpEnabled() {
		return isOtpEnabled;
	}

	public void setIsOtpEnabled(Byte isOtpEnabled) {
		this.isOtpEnabled = isOtpEnabled;
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

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	
	public List<Map<String, Object>> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<Map<String, Object>> userGroups) {
		this.userGroups = userGroups;
	}
	 
	/*
	 * public List<String> getUserGroups() { return userGroups; }
	 * 
	 * public void setUserGroups(List<String> userGroups) { this.userGroups =
	 * userGroups; }
	 */
	
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public List<Map<String, Object>> getEngagementProducts() {
		return engagementProducts;
	}

	public void setEngagementProducts(List<Map<String, Object>> engagementProducts) {
		this.engagementProducts = engagementProducts;
	}

	public Byte getIsPartnerGeneralTermsApproved() {
		return IsPartnerGeneralTermsApproved;
	}

	public void setIsPartnerGeneralTermsApproved(Byte isPartnerGeneralTermsApproved) {
		IsPartnerGeneralTermsApproved = isPartnerGeneralTermsApproved;
	}

	public Boolean getPmi() {
		return pmi;
	}

	public void setPmi(Boolean pmi) {
		this.pmi = pmi;
	}
	
	public Byte getShowCosMessage() {
		return showCosMessage;
	}

	public void setShowCosMessage(Byte showCosMessage) {
		this.showCosMessage = showCosMessage;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}
	
	public Boolean getPmiro() {
		return pmiro;
	}

	public void setPmiro(Boolean pmiro) {
		this.pmiro = pmiro;
	}

	@Override
	public String toString() {
		return "LoginResponse [username=" + username + ", customerId=" + customerId + ", roles=" + roles
				+ ", userActions=" + userActions + ", forceChangePassword=" + forceChangePassword + ", isOtpEnabled="
				+ isOtpEnabled + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", userGroups=" + userGroups + ", userType=" + userType + ", pmi=" + pmi + ", partnerId=" + partnerId
				+ ", engagementProducts=" + engagementProducts + ", IsPartnerGeneralTermsApproved="
				+ IsPartnerGeneralTermsApproved + ", showCosMessage="
						+ showCosMessage + "]";
	}


}
