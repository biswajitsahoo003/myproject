package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillment.beans.SIPAttributes;
import com.tcl.dias.servicefulfillment.beans.ServiceAttributes;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class VoiceBasicEnrichmentBean extends TaskDetailsBaseBean{
	
	private SIPAttributes sipAttributes;
	private ServiceAttributes serviceAttributes;
	private List<String> onNetDialBack;
	private List<AttachmentIdBean> documentIds;
	

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

	public SIPAttributes getSipAttributes() {
		return sipAttributes;
	}

	public void setSipAttributes(SIPAttributes sipAttributes) {
		this.sipAttributes = sipAttributes;
	}

	public ServiceAttributes getServiceAttributes() {
		return serviceAttributes;
	}

	public void setServiceAttributes(ServiceAttributes serviceAttributes) {
		this.serviceAttributes = serviceAttributes;
	}
}
