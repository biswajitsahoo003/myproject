package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillment.beans.webex.CugDialOutBean;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class VoiceAdvanceEnrichmentBean extends TaskDetailsBaseBean {

	private List<VoiceAdvanceEnrichment> configurationDetails;
	private String cugDialInNumber;
	private List<CugDialOutBean> cugDialOut;
	private List<String> onNetDialBack;
	private List<AttachmentIdBean> documentIds;

	public List<VoiceAdvanceEnrichment> getConfigurationDetails() {
		return configurationDetails;
	}

	public void setConfigurationDetails(List<VoiceAdvanceEnrichment> configurationDetails) {
		this.configurationDetails = configurationDetails;
	}

	public String getCugDialInNumber() {
		return cugDialInNumber;
	}

	public void setCugDialInNumber(String cugDialInNumber) {
		this.cugDialInNumber = cugDialInNumber;
	}

	public List<CugDialOutBean> getCugDialOut() {
		return cugDialOut;
	}

	public void setCugDialOut(List<CugDialOutBean> cugDialOut) {
		this.cugDialOut = cugDialOut;
	}

	public List<String> getOnNetDialBack() {
		return onNetDialBack;
	}

	public void setOnNetDialBack(List<String> onNetDialBack) {
		this.onNetDialBack = onNetDialBack;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
