package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.TrunkBean;

public class GscSipTrunkRouteLabelCreationApprovalBean extends TaskDetailsBaseBean{
	private List<Integer> id;
	private String status;
	private String remarks;
	private String comments;
	private List<AttachmentIdBean> documentIds;
	private List<TrunkBean> trunks;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	public List<TrunkBean> getTrunks() {
		return trunks;
	}
	public void setTrunks(List<TrunkBean> trunks) {
		this.trunks = trunks;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public List<Integer> getId() {
		return id;
	}
	public void setId(List<Integer> id) {
		this.id = id;
	}
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
