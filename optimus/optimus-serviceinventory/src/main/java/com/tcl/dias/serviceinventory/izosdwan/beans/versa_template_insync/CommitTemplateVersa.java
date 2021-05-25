package com.tcl.dias.serviceinventory.izosdwan.beans.versa_template_insync;

import java.io.Serializable;

/**
 * Bean class for template commit object
 * @author archchan
 *
 */
public class CommitTemplateVersa implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String result;
	private String message;
	private Integer taskId;
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer getTaskId() {
		return taskId;
	}
	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}
	@Override
	public String toString() {
		return "CommitTemplateVersa [result=" + result + ", message=" + message + ", taskId=" + taskId + "]";
	}
	
}
