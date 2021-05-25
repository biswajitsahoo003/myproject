package com.tcl.dias.common.beans;

import java.util.List;

/**
 * 
 * This file contains the information for the oms attachment listener
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class OmsListenerBean {
	
	private OmsAttachBean omsAttachmentBean;
	
	private List<OmsAttachBean> omsAttachBean;
	
	private boolean deleteAttachmentReference;

	public boolean isDeleteAttachmentReference() {
		return deleteAttachmentReference;
	}

	public void setDeleteAttachmentReference(boolean deleteAttachmentReference) {
		this.deleteAttachmentReference = deleteAttachmentReference;
	}

	public OmsAttachBean getOmsAttachmentBean() {
		return omsAttachmentBean;
	}

	public void setOmsAttachmentBean(OmsAttachBean omsAttachmentBean) {
		this.omsAttachmentBean = omsAttachmentBean;
	}

	public List<OmsAttachBean> getOmsAttachBean() {
		return omsAttachBean;
	}

	public void setOmsAttachBean(List<OmsAttachBean> omsAttachBean) {
		this.omsAttachBean = omsAttachBean;
	}
	
	

}
