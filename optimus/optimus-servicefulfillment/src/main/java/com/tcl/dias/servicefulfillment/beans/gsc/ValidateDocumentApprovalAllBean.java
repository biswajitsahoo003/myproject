package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ValidateDocumentApprovalAllBean extends TaskDetailsBaseBean {

	private List<ValidateDocumentApprovalBean> validateDocumentApproval;

	public List<ValidateDocumentApprovalBean> getValidateDocumentApproval() {
		return validateDocumentApproval;
	}

	public void setValidateDocumentApproval(List<ValidateDocumentApprovalBean> validateDocumentApproval) {
		this.validateDocumentApproval = validateDocumentApproval;
	}
}
