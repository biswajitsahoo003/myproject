package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class E2eVoiceTestingBean extends TaskDetailsBaseBean {
	
	private String e2EVoiceTestingStatus;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String e2EVoiceTestingCompletedDate;
	
	private List<AttachmentIdBean> documentIds;

	public String getE2EVoiceTestingStatus() {
		return e2EVoiceTestingStatus;
	}

	public void setE2EVoiceTestingStatus(String e2eVoiceTestingStatus) {
		e2EVoiceTestingStatus = e2eVoiceTestingStatus;
	}

	public String getE2EVoiceTestingCompletedDate() {
		return e2EVoiceTestingCompletedDate;
	}

	public void setE2EVoiceTestingCompletedDate(String e2eVoiceTestingCompletedDate) {
		e2EVoiceTestingCompletedDate = e2eVoiceTestingCompletedDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	
	
}
