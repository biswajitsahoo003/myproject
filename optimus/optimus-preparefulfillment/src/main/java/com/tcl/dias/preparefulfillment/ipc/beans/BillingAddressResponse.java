package com.tcl.dias.preparefulfillment.ipc.beans;


/**
 * This file contains the BillingAddressResponse.java class.
 * 
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class BillingAddressResponse {
	
	private IPCAddressDetail customerAddressDetail;
	
	private IPCAddressDetail supplierAddressDetail;
	
	private IPCBillingContact billingAddressDetail;
	
	private IPCAddressDetail siteLocationA;
	
	private IPCAddressDetail siteLocationB;

	public IPCAddressDetail getCustomerAddressDetail() {
		return customerAddressDetail;
	}

	public void setCustomerAddressDetail(IPCAddressDetail customerAddressDetail) {
		this.customerAddressDetail = customerAddressDetail;
	}

	public IPCAddressDetail getSupplierAddressDetail() {
		return supplierAddressDetail;
	}

	public void setSupplierAddressDetail(IPCAddressDetail supplierAddressDetail) {
		this.supplierAddressDetail = supplierAddressDetail;
	}

	public IPCBillingContact getBillingAddressDetail() {
		return billingAddressDetail;
	}

	public void setBillingAddressDetail(IPCBillingContact billingAddressDetail) {
		this.billingAddressDetail = billingAddressDetail;
	}

	public IPCAddressDetail getSiteLocationA() {
		return siteLocationA;
	}

	public void setSiteLocationA(IPCAddressDetail siteLocationA) {
		this.siteLocationA = siteLocationA;
	}

	public IPCAddressDetail getSiteLocationB() {
		return siteLocationB;
	}

	public void setSiteLocationB(IPCAddressDetail siteLocationB) {
		this.siteLocationB = siteLocationB;
	}

	@Override
	public String toString() {
		return "BillingAddressResponse [customerAddressDetail=" + customerAddressDetail + ", supplierAddressDetail="
				+ supplierAddressDetail + ", billingAddressDetail=" + billingAddressDetail + ", siteLocationA="
				+ siteLocationA + ", siteLocationB=" + siteLocationB + "]";
	}
	
}
