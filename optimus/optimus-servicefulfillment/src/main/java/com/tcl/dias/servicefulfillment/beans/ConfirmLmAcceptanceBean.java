package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.util.List;

/**
 * This class is Used to Confirm Last Mile Acceptance
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class ConfirmLmAcceptanceBean extends TaskDetailsBaseBean {
	

	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	//private Date dateLocalLoopCommissioning;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String lastMileSla;
	private String lastMileSlaDeviation;
	private String lastMileSlaDeviationReason;
	private String supplierBillStartDate ;
	private String  supplierBsoCircuitId;
	private List<AttachmentIdBean> documentIds;

	
	public String getSupplierBillStartDate() {
		return supplierBillStartDate;
	}
	public void setSupplierBillStartDate(String supplierBillStartDate) {
		this.supplierBillStartDate = supplierBillStartDate;
	}
	public String getLastMileSla() {
		return lastMileSla;
	}
	public void setLastMileSla(String lastMileSla) {
		this.lastMileSla = lastMileSla;
	}
	public String getLastMileSlaDeviation() {
		return lastMileSlaDeviation;
	}
	public void setLastMileSlaDeviation(String lastMileSlaDeviation) {
		this.lastMileSlaDeviation = lastMileSlaDeviation;
	}
	public String getLastMileSlaDeviationReason() {
		return lastMileSlaDeviationReason;
	}
	public void setLastMileSlaDeviationReason(String lastMileSlaDeviationReason) {
		this.lastMileSlaDeviationReason = lastMileSlaDeviationReason;
	}
	public String getSupplierBsoCircuitId() {
		return supplierBsoCircuitId;
	}

	public void setSupplierBsoCircuitId(String supplierBsoCircuitId) {
		this.supplierBsoCircuitId = supplierBsoCircuitId;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	
}
