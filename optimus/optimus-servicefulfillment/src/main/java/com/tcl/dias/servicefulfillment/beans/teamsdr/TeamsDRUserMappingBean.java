package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * TeamsDR User Mapping Bean
 *
 * @author Srinivasa Raghavan
 */
public class TeamsDRUserMappingBean extends TaskDetailsBaseBean {
	private Integer componentId;
	private String componentName;
	private String didRangeAllocated;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String batchDate;

	private String batchTime;
	private Integer batchUserCount;
	private List<AttachmentIdBean> documentIds;

	public Integer getComponentId() {
		return componentId;
	}

	public void setComponentId(Integer componentId) {
		this.componentId = componentId;
	}

	public String getComponentName() {
		return componentName;
	}

	public void setComponentName(String componentName) {
		this.componentName = componentName;
	}

	public String getDidRangeAllocated() {
		return didRangeAllocated;
	}

	public void setDidRangeAllocated(String didRangeAllocated) {
		this.didRangeAllocated = didRangeAllocated;
	}

	public String getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(String batchDate) {
		this.batchDate = batchDate;
	}

	public String getBatchTime() {
		return batchTime;
	}

	public void setBatchTime(String batchTime) {
		this.batchTime = batchTime;
	}

	public Integer getBatchUserCount() {
		return batchUserCount;
	}

	public void setBatchUserCount(Integer batchUserCount) {
		this.batchUserCount = batchUserCount;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
