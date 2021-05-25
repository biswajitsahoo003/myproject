package com.tcl.dias.l2oworkflowutils.beans;

import java.sql.Timestamp;

import org.json.simple.JSONObject;

/**
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited used to assign the task data
 *            details
 */
public class TaskDataBean {

	private JSONObject data;

	private Timestamp createdTime;

	private String name;

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

}
