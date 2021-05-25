package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;

/**
 * This bean is used to provide details related to MRN Generation for CPE Transfer.
 * 
 * @author yogesh
 */
public class GenerateMrnforCPETransferBean {

	private String cpeMaterialRequisitionNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String cpeMaterialRequisitionDate;
	
	private List<AttachmentIdBean> documentIds;

	public String getCpeMaterialRequisitionNumber() {
		return cpeMaterialRequisitionNumber;
	}

	public void setCpeMaterialRequisitionNumber(String cpeMaterialRequisitionNumber) {
		this.cpeMaterialRequisitionNumber = cpeMaterialRequisitionNumber;
	}

	public String getCpeMaterialRequisitionDate() {
		return cpeMaterialRequisitionDate;
	}

	public void setCpeMaterialRequisitionDate(String cpeMaterialRequisitionDate) {
		this.cpeMaterialRequisitionDate = cpeMaterialRequisitionDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	
		
}
