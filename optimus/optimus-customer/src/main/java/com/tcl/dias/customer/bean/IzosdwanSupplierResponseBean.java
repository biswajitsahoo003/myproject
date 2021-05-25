package com.tcl.dias.customer.bean;

import java.io.Serializable;
import java.util.List;
/**
 * 
 * This bean is used to return all the matching supplier details
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzosdwanSupplierResponseBean implements Serializable{
	private List<IzosdwanSupplierBean> izosdwanSupplierBeans;
	private Boolean isLouRequired;
	private Boolean isTaxClearanceDocumentRequired;
	public List<IzosdwanSupplierBean> getIzosdwanSupplierBeans() {
		return izosdwanSupplierBeans;
	}
	public void setIzosdwanSupplierBeans(List<IzosdwanSupplierBean> izosdwanSupplierBeans) {
		this.izosdwanSupplierBeans = izosdwanSupplierBeans;
	}
	public Boolean getIsLouRequired() {
		return isLouRequired;
	}
	public void setIsLouRequired(Boolean isLouRequired) {
		this.isLouRequired = isLouRequired;
	}
	public Boolean getIsTaxClearanceDocumentRequired() {
		return isTaxClearanceDocumentRequired;
	}
	public void setIsTaxClearanceDocumentRequired(Boolean isTaxClearanceDocumentRequired) {
		this.isTaxClearanceDocumentRequired = isTaxClearanceDocumentRequired;
	}
	
}
