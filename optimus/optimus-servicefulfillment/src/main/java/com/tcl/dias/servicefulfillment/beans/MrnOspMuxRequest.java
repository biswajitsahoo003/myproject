package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * @author vivek
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class MrnOspMuxRequest extends TaskDetailsBaseBean {
	
	///private String approvingPerson;
	//private String requestingPerson;
	private String mrnNumber;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private String mrnDate;
	//private String verifiedBy;
	private List<AttachmentIdBean> documentIds;
	//private List<MaterialListBean> materialList;
	public String getMrnNumber() {
		return mrnNumber;
	}
	public void setMrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
	}
	public String getMrnDate() {
		return mrnDate;
	}
	public void setMrnDate(String mrnDate) {
		this.mrnDate = mrnDate;
	}
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
