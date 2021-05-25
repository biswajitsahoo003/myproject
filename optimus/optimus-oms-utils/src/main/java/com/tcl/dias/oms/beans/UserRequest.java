package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * This file contains the UserRequest.java class.
 * 
 *
 * @author Manojkumar R
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class UserRequest {

	private List<Integer> userIds;

	public List<Integer> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Integer> userIds) {
		this.userIds = userIds;
	}

	@Override
	public String toString() {
		return "UserRequest [userIds=" + userIds + "]";
	}

}
