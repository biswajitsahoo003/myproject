package com.tcl.dias.servicefulfillment.beans.teamsdr;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionAttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * TeamsDR UAT Testing bean.
 * 
 * @author arjayapa
 *
 */
public class TeamsDRUatTestingBean extends TaskDetailsBaseBean  {
	
	private List<SolutionAttachmentBean> msUatDocIds;	

	String msUatTestingStatus;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String msUatCompletionDate;
	
	private Integer batchId;
	
	private Integer msSiteId;
	
	public TeamsDRUatTestingBean() {
		super();
	}

	public List<SolutionAttachmentBean> getMsUatDocIds() {
		return msUatDocIds;
	}

	public void setMsUatDocIds(List<SolutionAttachmentBean> msUatDocIds) {
		this.msUatDocIds = msUatDocIds;
	}

	public String getMsUatTestingStatus() {
		return msUatTestingStatus;
	}

	public void setMsUatTestingStatus(String msUatTestingStatus) {
		this.msUatTestingStatus = msUatTestingStatus;
	}

	public String getMsUatCompletionDate() {
		return msUatCompletionDate;
	}

	public void setMsUatCompletionDate(String msUatCompletionDate) {
		this.msUatCompletionDate = msUatCompletionDate;
	}

	public Integer getMsSiteId() {
		return msSiteId;
	}

	public void setMsSiteId(Integer msSiteId) {
		this.msSiteId = msSiteId;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}		

}
