package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to Prepare PO for Government Authority
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PreparePoGovernmentAuthorityBean extends TaskDetailsBaseBean {
	
	private String govtAuthVendorId	;
	private String rowServiceRequestNumber;
	private String rowPoNumber;
	private String wbsCostCentre;
	private List<AttachmentIdBean> documentIds;
	
	public String getRowServiceRequestNumber() {
		return rowServiceRequestNumber;
	}
	public void setRowServiceRequestNumber(String rowServiceRequestNumber) {
		this.rowServiceRequestNumber = rowServiceRequestNumber;
	}
	public String getRowPoNumber() {
		return rowPoNumber;
	}
	public void setRowPoNumber(String rowPoNumber) {
		this.rowPoNumber = rowPoNumber;
	}
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	public String getGovtAuthVendorId() {
		return govtAuthVendorId;
	}
	public void setGovtAuthVendorId(String govtAuthVendorId) {
		this.govtAuthVendorId = govtAuthVendorId;
	}
	public String getWbsCostCentre() {
		return wbsCostCentre;
	}
	public void setWbsCostCentre(String wbsCostCentre) {
		this.wbsCostCentre = wbsCostCentre;
	}
	

}
