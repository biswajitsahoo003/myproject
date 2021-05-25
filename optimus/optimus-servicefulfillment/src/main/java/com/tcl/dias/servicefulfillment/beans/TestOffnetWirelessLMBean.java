package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * TestOffetWirelessLMBean -  Bean to conduct test for lm offnet wireline
 *
 * @author Yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class TestOffnetWirelessLMBean extends TaskDetailsBaseBean {

	private String lastMileTestResults;
	
	private List<AttachmentIdBean> documentIds;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String lastMileTestCompletionDate;

	public String getLastMileTestResults() {
		return lastMileTestResults;
	}

	public void setLastMileTestResults(String lastMileTestResults) {
		this.lastMileTestResults = lastMileTestResults;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getLastMileTestCompletionDate() {
		return lastMileTestCompletionDate;
	}

	public void setLastMileTestCompletionDate(String lastMileTestCompletionDate) {
		this.lastMileTestCompletionDate = lastMileTestCompletionDate;
	}
	
	
}
