package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ConfigureEndpointBean extends TaskDetailsBaseBean {
	
	private String configurationCompleted;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String configurationDate;
	
	private List<AttachmentIdBean> documentIds;

	public String getConfigurationCompleted() {
		return configurationCompleted;
	}

	public void setConfigurationCompleted(String configurationCompleted) {
		this.configurationCompleted = configurationCompleted;
	}

	public String getConfigurationDate() {
		return configurationDate;
	}

	public void setConfigurationDate(String configurationDate) {
		this.configurationDate = configurationDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
