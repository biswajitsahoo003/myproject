package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.SolutionAttachmentBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * @author Syed Ali.
 * @createdAt 01/03/2021, Monday, 12:11
 */
public class TeamsDRMSDirectRoutingBean extends TaskDetailsBaseBean {
	private String msDrConfigStatus;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String msDrConfigDate;

	private Integer batchId;

	private Integer msSiteId;

	private List<SolutionAttachmentBean> drAttachmentIds;

	public TeamsDRMSDirectRoutingBean() {
	}

	public String getMsDrConfigStatus() {
		return msDrConfigStatus;
	}

	public void setMsDrConfigStatus(String msDrConfigStatus) {
		this.msDrConfigStatus = msDrConfigStatus;
	}

	public String getMsDrConfigDate() {
		return msDrConfigDate;
	}

	public void setMsDrConfigDate(String msDrConfigDate) {
		this.msDrConfigDate = msDrConfigDate;
	}

	public List<SolutionAttachmentBean> getDrAttachmentIds() {
		return drAttachmentIds;
	}

	public void setDrAttachmentIds(List<SolutionAttachmentBean> drAttachmentIds) {
		this.drAttachmentIds = drAttachmentIds;
	}

	public Integer getBatchId() {
		return batchId;
	}

	public void setBatchId(Integer batchId) {
		this.batchId = batchId;
	}

	public Integer getMsSiteId() {
		return msSiteId;
	}

	public void setMsSiteId(Integer msSiteId) {
		this.msSiteId = msSiteId;
	}

	@Override
	public String toString() {
		return "TeamsDRMSDirectRoutingBean{" + "drConfigurationStatus='" + msDrConfigStatus + '\'' + ", " +
				"drConfigurationDate='" + msDrConfigDate + '\'' + ", batchId=" + batchId + ", drSiteId=" + msSiteId + ", drAttachmentIds=" + drAttachmentIds + '}';
	}
}
