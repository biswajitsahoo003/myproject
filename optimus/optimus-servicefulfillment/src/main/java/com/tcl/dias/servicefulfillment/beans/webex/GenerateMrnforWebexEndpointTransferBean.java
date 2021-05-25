package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class GenerateMrnforWebexEndpointTransferBean extends TaskDetailsBaseBean {
	
private String endpointMaterialRequisitionNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date endpointMaterialRequisitionDate;
	
	private List<AttachmentIdBean> documentIds;

	public String getEndpointMaterialRequisitionNumber() {
		return endpointMaterialRequisitionNumber;
	}

	public void setEndpointMaterialRequisitionNumber(String endpointMaterialRequisitionNumber) {
		this.endpointMaterialRequisitionNumber = endpointMaterialRequisitionNumber;
	}

	public Date getCpeMaterialRequisitionDate() {
		return endpointMaterialRequisitionDate;
	}

	public void setCpeMaterialRequisitionDate(Date endpointMaterialRequisitionDate) {
		this.endpointMaterialRequisitionDate = endpointMaterialRequisitionDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	

}


