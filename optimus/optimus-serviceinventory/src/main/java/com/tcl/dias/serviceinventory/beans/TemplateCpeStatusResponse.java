package com.tcl.dias.serviceinventory.beans;

import java.util.List;

/**
 * Response bean for sending Cpes insync and outsync list
 * 
 * @author Srinivasa Raghavan
 */
public class TemplateCpeStatusResponse {
	private List<Integer> taskIds;
	private List<String> templates;
	private List<String> inSyncCpes;
	private List<String> outOfSyncCpes;
	private String createUpdateStatus;

	public List<Integer> getTaskIds() {
		return taskIds;
	}

	public void setTaskIds(List<Integer> taskIds) {
		this.taskIds = taskIds;
	}

	public List<String> getTemplates() {
		return templates;
	}

	public void setTemplates(List<String> templates) {
		this.templates = templates;
	}

	public List<String> getInSyncCpes() {
		return inSyncCpes;
	}

	public void setInSyncCpes(List<String> inSyncCpes) {
		this.inSyncCpes = inSyncCpes;
	}

	public List<String> getOutOfSyncCpes() {
		return outOfSyncCpes;
	}

	public void setOutOfSyncCpes(List<String> outOfSyncCpes) {
		this.outOfSyncCpes = outOfSyncCpes;
	}

	public String getCreateUpdateStatus() {
		return createUpdateStatus;
	}

	public void setCreateUpdateStatus(String createUpdateStatus) {
		this.createUpdateStatus = createUpdateStatus;
	}
	
}
