package com.tcl.dias.oms.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.CustomerBean;

/**
 * Bean class
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(Include.NON_NULL)
public class UserMappingBean {
	

	private Integer userId;
	private List<CustomerBean> customerList;
	private List<Integer> userGroupId;
	/**
	 * @return the userId
	 */
	public Integer getUserId() {
		return userId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	/**
	 * @return the userGroupId
	 */
	public List<Integer> getUserGroupId() {
		return userGroupId;
	}
	/**
	 * @param userGroupId the userGroupId to set
	 */
	public void setUserGroupId(List<Integer> userGroupId) {
		this.userGroupId = userGroupId;
	}
	/**
	 * @return the customerList
	 */
	public List<CustomerBean> getCustomerList() {
		return customerList;
	}
	/**
	 * @param customerList the customerList to set
	 */
	public void setCustomerList(List<CustomerBean> customerList) {
		this.customerList = customerList;
	}
	
	
	

}
