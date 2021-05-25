package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.User;

/**
 * Bean class
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class UserDetails {

	public UserDetails(User user) {
		if (user != null) {
			this.userId = user.getId();
			this.contactNo = user.getContactNo();
			this.emailId = user.getEmailId();
			this.firstName = user.getFirstName();
			this.lastName = user.getLastName();
			this.partnerId = user.getPartnerId();
			this.username = user.getUsername();
			this.userType = user.getUserType();
			this.emailId = user.getEmailId();
			if (user.getCustomer() != null) {
				this.customerId = user.getCustomer().getId();
			}
		}
	}

	public UserDetails() {
		// DO NOTHING
	}

	private Integer userId;
	private String contactNo;
	private String emailId;
	private String firstName;
	private String lastName;
	private Integer partnerId;
	private Integer status;
	private String userType;
	private String username;
	private Integer customerId;
	private Byte IsPartnerGeneralTermsApproved;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
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

	public Byte getIsPartnerGeneralTermsApproved() {
		return IsPartnerGeneralTermsApproved;
	}

	public void setIsPartnerGeneralTermsApproved(Byte isPartnerGeneralTermsApproved) {
		IsPartnerGeneralTermsApproved = isPartnerGeneralTermsApproved;
	}

	@Override
	public String toString() {
		return "UserDetails{" +
				"userId=" + userId +
				", contactNo='" + contactNo + '\'' +
				", emailId='" + emailId + '\'' +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", partnerId=" + partnerId +
				", status=" + status +
				", userType='" + userType + '\'' +
				", username='" + username + '\'' +
				", customerId=" + customerId +
				", IsPartnerGeneralTermsApproved=" + IsPartnerGeneralTermsApproved +
				'}';
	}
}
