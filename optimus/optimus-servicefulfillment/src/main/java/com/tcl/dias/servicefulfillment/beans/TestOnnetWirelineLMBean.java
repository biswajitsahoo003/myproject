package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * TestOnnetWirelineLMBean -  Bean to conduct test for lm onnet wireline
 *
 * @author Yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TestOnnetWirelineLMBean extends TaskDetailsBaseBean {

	private List<AttachmentIdBean> documentIds;
	private String remark;
	private String feNeeded;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String lMTestCompletionDate;
	
	private String testStatus;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getlMTestCompletionDate() {
		return lMTestCompletionDate;
	}

	public void setlMTestCompletionDate(String lMTestCompletionDate) {
		this.lMTestCompletionDate = lMTestCompletionDate;
	}

	public String getTestStatus() {
		return testStatus;
	}

	public void setTestStatus(String testStatus) {
		this.testStatus = testStatus;
	}

	public String getRemark() { return remark; }

	public void setRemark(String remark) { this.remark = remark; }

	public String getFeNeeded() {
		return feNeeded;
	}

	public void setFeNeeded(String feNeeded) {
		this.feNeeded = feNeeded;
	}
}
