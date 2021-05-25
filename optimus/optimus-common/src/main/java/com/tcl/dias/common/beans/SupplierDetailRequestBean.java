package com.tcl.dias.common.beans;

import java.io.Serializable;

public class SupplierDetailRequestBean implements Serializable{
	
	private Integer supplierId;
	
	private String mddFilterValue;

	public Integer getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Integer supplierId) {
		this.supplierId = supplierId;
	}

	public String getMddFilterValue() {
		return mddFilterValue;
	}

	public void setMddFilterValue(String mddFilterValue) {
		this.mddFilterValue = mddFilterValue;
	}

}
