package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class SrnGenerationEndpointBean extends TaskDetailsBaseBean {
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String srnDate;
	
	private List<AttachmentIdBean> documentIds;

	public String getSrnDate() {
		return srnDate;
	}

	public void setSrnDate(String srnDate) {
		this.srnDate = srnDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	
	

}
