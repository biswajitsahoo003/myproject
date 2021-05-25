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
public class UserGroupBean {
	
	private String groupName;
	
	private List<Integer> customerLeIdList;

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @param groupName the groupName to set
	 */
	public void setGroupName(String groupName) {
		this.groupName = groupName;
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

	@Override
	public String toString() {
		return "UserGroupBean [groupName=" + groupName + ", customerLeIdList=" + customerLeIdList + "]";
	}
	
	
	

}
