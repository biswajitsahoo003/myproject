package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * AssignedGroupingBean for grouping of bean
 *
 */
public class AssignedGroupingBean {
	
	private String groupName;
	
	private String userId;
	
	private List<TaskGroupBean> taskGroup;

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public List<TaskGroupBean> getTaskGroup() {
		if(taskGroup==null) {
			taskGroup=new ArrayList<TaskGroupBean>();
		}
		return taskGroup;
	}

	public void setTaskGroup(List<TaskGroupBean> taskGroup) {
		this.taskGroup = taskGroup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
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
		AssignedGroupingBean other = (AssignedGroupingBean) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	
	

}
