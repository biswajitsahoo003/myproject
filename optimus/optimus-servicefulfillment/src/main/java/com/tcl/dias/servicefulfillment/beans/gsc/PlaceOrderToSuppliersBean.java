package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import com.tcl.dias.servicefulfillmentutils.beans.gsc.PlaceOrderSupplierBean;

public class PlaceOrderToSuppliersBean extends TaskDetailsBaseBean {
	
	String orderFlow;
	
    List<PlaceOrderSupplierBean> suppliers;

	public String getOrderFlow() {
		return orderFlow;
	}

	public void setOrderFlow(String orderFlow) {
		this.orderFlow = orderFlow;
	}

	public List<PlaceOrderSupplierBean> getSuppliers() {
		return suppliers;
	}

	public void setSuppliers(List<PlaceOrderSupplierBean> suppliers) {
		this.suppliers = suppliers;
	}
}
