package com.tcl.dias.common.beans;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * Mst User group bean class
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MstUserGroupBean implements Serializable {

	private static final long serialVersionUID = -1432609585414925593L;
	private Integer userGroupId;
	private Integer groupTypeId;
	private String userGroupName;
	private String groupTypeName;
	private List<Map<String, Object>> leIds;
	private List<Map<String, Object>> partnerLeIds;
	private List<Map<String, Object>> userIds;
	private Integer userCount;

	public String getGroupTypeName() {
		return groupTypeName;
	}

	public void setGroupTypeName(String groupTypeName) {
		this.groupTypeName = groupTypeName;
	}

	public Integer getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Integer userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Integer getGroupTypeId() {
		return groupTypeId;
	}

	public void setGroupTypeId(Integer groupTypeId) {
		this.groupTypeId = groupTypeId;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public List<Map<String, Object>> getLeIds() {
		return leIds;
	}

	public void setLeIds(List<Map<String, Object>> leIds) {
		this.leIds = leIds;
	}

	public List<Map<String, Object>> getPartnerLeIds() {
		return partnerLeIds;
	}

	public void setPartnerLeIds(List<Map<String, Object>> partnerLeIds) {
		this.partnerLeIds = partnerLeIds;
	}

	public List<Map<String, Object>> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Map<String, Object>> userIds) {
		this.userIds = userIds;
	}

	public Integer getUserCount() {
		return userCount;
	}

	public void setUserCount(Integer userCount) {
		this.userCount = userCount;
	}

}
