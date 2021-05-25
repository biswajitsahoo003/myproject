package com.tcl.dias.auth.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
	private List<Integer> customerLeIdList;
    private List<Integer> partnerLeIdList;
	private List<Integer> userGroupId;
	private String username;
	
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
	 * @return the customerLeIdList
	 */
	public List<Integer> getCustomerLeIdList() {
		return customerLeIdList;
	}
	/**
	 * @param customerLeIdList the customerLeIdList to set
	 */
	public void setCustomerLeIdList(List<Integer> customerLeIdList) {
		this.customerLeIdList = customerLeIdList;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	public List<Integer> getPartnerLeIdList() {
		return partnerLeIdList;
	}
	public void setPartnerLeIdList(List<Integer> partnerLeIdList) {
		this.partnerLeIdList = partnerLeIdList;
	}
	@Override
	public String toString() {
		return "UserMappingBean [userId=" + userId + ", customerLeIdList=" + customerLeIdList + ", userGroupId="
				+ userGroupId + ", username=" + username + ", partnerLeIdList=" + partnerLeIdList + "]";
	}
	
	
	

}
