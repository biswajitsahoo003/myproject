package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

public class ActivateVoiceMicrositeBean extends TaskDetailsBaseBean {
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String emailConfirmationDate;
	
    private List<AttachmentIdBean> documentIds;
    
    public String getEmailConfirmationDate() {
		return emailConfirmationDate;
	}

	public void setEmailConfirmationDate(String emailConfirmationDate) {
		this.emailConfirmationDate = emailConfirmationDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	

}
