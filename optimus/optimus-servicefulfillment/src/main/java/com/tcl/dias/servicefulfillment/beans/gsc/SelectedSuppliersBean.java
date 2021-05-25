package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.SupplierBean;

import java.util.List;

public class SelectedSuppliersBean extends TaskDetailsBaseBean {
	
    List<SupplierBean> suppliers;

	public List<SupplierBean> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<SupplierBean> suppliers) {
		this.suppliers = suppliers;
	}
}
