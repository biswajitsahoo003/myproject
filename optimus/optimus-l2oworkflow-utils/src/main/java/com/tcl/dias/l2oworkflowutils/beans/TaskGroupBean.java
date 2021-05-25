package com.tcl.dias.l2oworkflowutils.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 * used to group th etask
 *
 */
public class TaskGroupBean {

	private String name;
	
	private String status;

	private List<TaskBean> taskBeans;

	public String getName() {
		return name;
	}
	
	

	public String getStatus() {
		return status;
	}



	public void setStatus(String status) {
		this.status = status;
	}



	public void setName(String name) {
		this.name = name;
	}

	public List<TaskBean> getTaskBeans() {

		if (taskBeans == null) {
			taskBeans = new ArrayList<TaskBean>();
		}
		return taskBeans;
	}

	public void setTaskBeans(List<TaskBean> taskBeans) {
		this.taskBeans = taskBeans;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		TaskGroupBean other = (TaskGroupBean) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
