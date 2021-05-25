package com.tcl.dias.common.beans;

public class BillingContactInfo {

	private Integer customerLeId;
	private String firstName;
	private String lastName;
	private String emailId;
	private String phoneNumber;
	private String mobileNumber;

	public Integer getCustomerLeId() {
		return customerLeId;
	}

	public void setCustomerLeId(Integer customerLeId) {
		this.customerLeId = customerLeId;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	
	

	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((customerLeId == null) ? 0 : customerLeId.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BillingContactInfo other = (BillingContactInfo) obj;
		if (customerLeId == null) {
			if (other.customerLeId != null)
				return false;
		} else if (!customerLeId.equals(other.customerLeId))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BillingContactInfo [customerLeId=" + customerLeId + ", firstName=" + firstName + ", lastName="
				+ lastName + ", emailId=" + emailId + ", phoneNumber=" + phoneNumber + ", mobileNumber=" + mobileNumber
				+ "]";
	}

}
