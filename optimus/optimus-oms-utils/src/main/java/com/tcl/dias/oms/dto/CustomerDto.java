package com.tcl.dias.oms.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.oms.entity.entities.Customer;

/**
 * This file contains the CustomerDto.java class.
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class CustomerDto {

	private Integer id;

	private String customerAcId;

	private String customerCode;

	private String customerEmailId;

	private String customerName;

	private Byte status;

	private List<UserDto> users;

	public CustomerDto() {
	}

	public CustomerDto(Customer customer) {
		this.customerCode = customer.getCustomerCode();
		this.customerEmailId = customer.getCustomerEmailId();
		this.customerName = customer.getCustomerName();

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
	 * this method is to set the id
	 * 
	 * @param id
	 *            the id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * this method is to return the customerAcId
	 * 
	 * @return the customerAcId
	 */
	public String getCustomerAcId() {
		return customerAcId;
	}

	/**
	 * this method is to set the customerAcId
	 * 
	 * @param customerAcId
	 *            the customerAcId to set
	 */
	public void setCustomerAcId(String customerAcId) {
		this.customerAcId = customerAcId;
	}

	/**
	 * this method is to return the customerCode
	 * 
	 * @return the customerCode
	 */
	public String getCustomerCode() {
		return customerCode;
	}

	/**
	 * this method is to set the customerCode
	 * 
	 * @param customerCode
	 *            the customerCode to set
	 */
	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	/**
	 * this method is to return the customerEmailId
	 * 
	 * @return the customerEmailId
	 */
	public String getCustomerEmailId() {
		return customerEmailId;
	}

	/**
	 * this method is to set the customerEmailId
	 * 
	 * @param customerEmailId
	 *            the customerEmailId to set
	 */
	public void setCustomerEmailId(String customerEmailId) {
		this.customerEmailId = customerEmailId;
	}

	/**
	 * this method is to return the customerName
	 * 
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * this method is to set the customerName
	 * 
	 * @param customerName
	 *            the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * this method is to return the status
	 * 
	 * @return the status
	 */
	public Byte getStatus() {
		return status;
	}

	/**
	 * this method is to set the status
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Byte status) {
		this.status = status;
	}

	/**
	 * this method is to return the users
	 * 
	 * @return the users
	 */
	public List<UserDto> getUsers() {
		return users;
	}

	/**
	 * this method is to set the users
	 * 
	 * @param users
	 *            the users to set
	 */
	public void setUsers(List<UserDto> users) {
		this.users = users;
	}

	/**
	 * toString
	 * @return
	 */
	@Override
	public String toString() {
		return "CustomerDto [id=" + id + ", customerAcId=" + customerAcId + ", customerCode=" + customerCode
				+ ", customerEmailId=" + customerEmailId + ", customerName=" + customerName + ", status=" + status
				+ ", users=" + users + "]";
	}

	
}
