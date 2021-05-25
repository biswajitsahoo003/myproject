package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.DIDSupplierBean;

public class ValidateDocumentBean extends TaskDetailsBaseBean {

	@JsonIgnoreProperties(ignoreUnknown = true)
	private List<DIDSupplierBean> selectedSuppliers;


	public List<DIDSupplierBean> getSelectedSuppliers() {
		return selectedSuppliers;
	}

	public void setSelectedSuppliers(List<DIDSupplierBean> selectedSuppliers) {
		this.selectedSuppliers = selectedSuppliers;
	}

	

}
