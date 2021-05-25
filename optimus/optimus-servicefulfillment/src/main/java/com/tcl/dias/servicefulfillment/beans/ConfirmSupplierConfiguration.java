package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used for task - confirm supplier configuration.
 *
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class ConfirmSupplierConfiguration extends TaskDetailsBaseBean {
	
	private String supplierBsoCircuitId;

	public String getSupplierBsoCircuitId() {
		return supplierBsoCircuitId;
	}

	public void setSupplierBsoCircuitId(String supplierBsoCircuitId) {
		this.supplierBsoCircuitId = supplierBsoCircuitId;
	}
}
