package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 *
 * This file contains the TerminationDateBean.java class.
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class TerminateDateBean extends TaskDetailsBaseBean {

	private String offnetTerminationDate;
	private String isSupplierEtcAvailable;
	private String supplierEtcCharges;
	private List<AttachmentIdBean> documentIds;

	public String getOffnetTerminationDate() {
		return offnetTerminationDate;
	}

	public void setOffnetTerminationDate(String offnetTerminationDate) {
		this.offnetTerminationDate = offnetTerminationDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getIsSupplierEtcAvailable() {
		return isSupplierEtcAvailable;
	}

	public void setIsSupplierEtcAvailable(String isSupplierEtcAvailable) {
		this.isSupplierEtcAvailable = isSupplierEtcAvailable;
	}

	public String getSupplierEtcCharges() {
		return supplierEtcCharges;
	}

	public void setSupplierEtcCharges(String supplierEtcCharges) {
		this.supplierEtcCharges = supplierEtcCharges;
	}

}
