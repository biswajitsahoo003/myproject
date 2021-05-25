package com.tcl.dias.serviceactivation.beans;

import java.io.Serializable;
import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

public class MuxJeopardyBean extends BaseRequest implements Serializable {

	private static final long serialVersionUID = -9158343118036268273L;

	private String endMuxNodePort;
	private String endMuxNodeIp;
	private String action;
	private String endMuxNodeName;
	private String remarks;
	private String delayReason;
	private String commissioningDate;
	private String popSiteCode;
	private String endMuxNodePortB;
	private String endMuxNodeIpB;
	private String endMuxNodeNameB;
	private String popSiteCodeB;
	private String eorIorDependencyReason;

	private List<AttachmentIdBean> documentIds;
	
	
	
	public String getEorIorDependencyReason() {
		return eorIorDependencyReason;
	}

	public void setEorIorDependencyReason(String eorIorDependencyReason) {
		this.eorIorDependencyReason = eorIorDependencyReason;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getEndMuxNodePort() {
		return endMuxNodePort;
	}

	public void setEndMuxNodePort(String endMuxNodePort) {
		this.endMuxNodePort = endMuxNodePort;
	}

	public String getEndMuxNodeIp() {
		return endMuxNodeIp;
	}

	public void setEndMuxNodeIp(String endMuxNodeIp) {
		this.endMuxNodeIp = endMuxNodeIp;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEndMuxNodeName() {
		return endMuxNodeName;
	}

	public void setEndMuxNodeName(String endMuxNodeName) {
		this.endMuxNodeName = endMuxNodeName;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelayReason() {
		return delayReason;
	}

	public void setDelayReason(String delayReason) {
		this.delayReason = delayReason;
	}

	public String getEndMuxNodePortB() {
		return endMuxNodePortB;
	}

	public void setEndMuxNodePortB(String endMuxNodePortB) {
		this.endMuxNodePortB = endMuxNodePortB;
	}

	public String getEndMuxNodeIpB() {
		return endMuxNodeIpB;
	}

	public void setEndMuxNodeIpB(String endMuxNodeIpB) {
		this.endMuxNodeIpB = endMuxNodeIpB;
	}

	public String getEndMuxNodeNameB() {
		return endMuxNodeNameB;
	}

	public void setEndMuxNodeNameB(String endMuxNodeNameB) {
		this.endMuxNodeNameB = endMuxNodeNameB;
	}

	public String getCommissioningDate() {
		return commissioningDate;
	}

	public void setCommissioningDate(String commissioningDate) {
		this.commissioningDate = commissioningDate;
	}

	public String getPopSiteCode() {
		return popSiteCode;
	}

	public void setPopSiteCode(String popSiteCode) {
		this.popSiteCode = popSiteCode;
	}

	public String getPopSiteCodeB() {
		return popSiteCodeB;
	}

	public void setPopSiteCodeB(String popSiteCodeB) {
		this.popSiteCodeB = popSiteCodeB;
	}

}