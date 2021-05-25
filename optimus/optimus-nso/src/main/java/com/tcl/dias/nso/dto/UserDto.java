package com.tcl.dias.nso.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.User;

/**
 * This file contains the UserDto.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class UserDto {

	private Integer id;

	private String contactNo;

	private String emailId;

	private String firstName;

	private String lastName;

	private Integer partnerId;

	private Integer status;

	private String userType;

	private String username;

	private CustomerDto customerDto;

	public UserDto() {
	}

	public UserDto(User user) {
		if (user != null) {

			this.id = user.getId();
			this.contactNo = user.getContactNo();
			this.emailId = user.getEmailId();
			this.firstName = user.getFirstName();
			this.lastName = user.getLastName();
			this.partnerId = user.getPartnerId();
			this.status = user.getStatus();
			this.username = user.getUsername();
			this.userType = user.getUserType();
			this.customerDto = user.getCustomer() != null ? new CustomerDto(user.getCustomer()) : null;

		}
	}

	/**
	 * this method is to return the id
	 * 
	 * @return the id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * the id to set
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * this method is to return the contactNo
	 * 
	 * @return the contactNo
	 */
	public String getContactNo() {
		return contactNo;
	}

	/**
	 * the contactNo to set
	 * 
	 * @param contactNo
	 *            the contactNo to set
	 */
	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	/**
	 * this method is to return the emailId
	 * 
	 * @return the emailId
	 */
	public String getEmailId() {
		return emailId;
	}

	/**
	 * the emailId to set
	 * 
	 * @param emailId
	 *            the emailId to set
	 */
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * this method is to return the firstName
	 * 
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * the firstName to set
	 * 
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * this method is to return the lastName
	 * 
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * the lastName to set
	 * 
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * this method is to return the partnerId
	 * 
	 * @return the partnerId
	 */
	public Integer getPartnerId() {
		return partnerId;
	}

	/**
	 * the partnerId to set
	 * 
	 * @param partnerId
	 *            the partnerId to set
	 */
	public void setPartnerId(Integer partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * this method is to return the status
	 * 
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * the status to set
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * this method is to return the userType
	 * 
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * the userType to set
	 * 
	 * @param userType
	 *            the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * this method is to return the username
	 * 
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * the username to set
	 * 
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	public CustomerDto getCustomerDto() {
		return customerDto;
	}

	public void setCustomerDto(CustomerDto customerDto) {
		this.customerDto = customerDto;
	}

	/**
	 * toString
	 * 
	 * @return
	 */
	@Override
	public String toString() {
		return "UserDto [id=" + id + ", contactNo=" + contactNo + ", emailId=" + emailId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", partnerId=" + partnerId + ", status=" + status + ", userType="
				+ userType + ", username=" + username + ", customerDto=" + customerDto + "]";
	}

}
