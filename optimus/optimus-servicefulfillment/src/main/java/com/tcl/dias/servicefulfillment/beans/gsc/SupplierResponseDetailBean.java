package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

public class SupplierResponseDetailBean extends TaskDetailsBaseBean {
	
	AttachmentIdBean documents;
	
    List<SupplierResponseBean> supplierResponse;

	public AttachmentIdBean getDocuments() {
		return documents;
	}

	public void setDocuments(AttachmentIdBean documents) {
		this.documents = documents;
	}

	public List<SupplierResponseBean> getSupplierResponse() {
		return supplierResponse;
	}

	public void setSupplierResponse(List<SupplierResponseBean> supplierResponse) {
		this.supplierResponse = supplierResponse;
	}
}
