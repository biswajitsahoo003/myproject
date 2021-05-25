package com.tcl.dias.oms.beans;

public class QuoteAccessBean {

	private Boolean customerView;
	private Boolean salesView;

	public Boolean getCustomerView() {
		return customerView;
	}

	public void setCustomerView(Boolean customerView) {
		this.customerView = customerView;
	}

	public Boolean getSalesView() {
		return salesView;
	}

	public void setSalesView(Boolean salesView) {
		this.salesView = salesView;
	}

	@Override
	public String toString() {
		return "QuoteAccessBean [customerView=" + customerView + ", salesView=" + salesView + "]";
	}

}
