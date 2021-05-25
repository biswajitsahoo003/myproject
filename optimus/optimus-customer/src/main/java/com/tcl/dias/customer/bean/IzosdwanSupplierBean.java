package com.tcl.dias.customer.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 
 *This bean is used to return all the matching supplier details.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzosdwanSupplierBean implements Serializable{
	private Integer supplierId;
	private String supplierName;
	private List<String> currency;
	public Integer getSupplierId() {
		return supplierId;
	}
	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public List<String> getCurrency() {
		return currency;
	}
	public void setCurrency(List<String> currency) {
		this.currency = currency;
	}
	
}
