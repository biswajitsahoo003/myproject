package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * This bean is used to provide details related to MRN Generation for CPE Transfer.
 * 
 * @author yogesh
 */
public class GenerateMrnforCPETransferBean extends TaskDetailsBaseBean {

	private String cpeMaterialRequisitionNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private Date cpeMaterialRequisitionDate;
	
	private List<AttachmentIdBean> documentIds;

	public String getCpeMaterialRequisitionNumber() {
		return cpeMaterialRequisitionNumber;
	}

	public void setCpeMaterialRequisitionNumber(String cpeMaterialRequisitionNumber) {
		this.cpeMaterialRequisitionNumber = cpeMaterialRequisitionNumber;
	}

	public Date getCpeMaterialRequisitionDate() {
		return cpeMaterialRequisitionDate;
	}

	public void setCpeMaterialRequisitionDate(Date cpeMaterialRequisitionDate) {
		this.cpeMaterialRequisitionDate = cpeMaterialRequisitionDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	
		
}
