package com.tcl.dias.servicefulfillment.beans;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to Create MRN for Mux and Release Mrn for OSP/IBD Material
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class MrnForMuxBean extends TaskDetailsBaseBean {

	private String mrnNumber;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String mrnDate;
	private List<AttachmentIdBean> documentIds;
	private String distributionCenterName;
	private String distributionCenterAddress;
	private String remarks;
	private String isKroneRequired;
	
	private String mrnMuxRequired;

	

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDistributionCenterName() {
		return distributionCenterName;
	}

	public void setDistributionCenterName(String distributionCenterName) {
		this.distributionCenterName = distributionCenterName;
	}

	public String getDistributionCenterAddress() {
		return distributionCenterAddress;
	}

	public void setDistributionCenterAddress(String distributionCenterAddress) {
		this.distributionCenterAddress = distributionCenterAddress;
	}

//	private List<MaterialListBean> materialList;

	public String getMrnNumber() {
		return mrnNumber;
	}

	public void setMrnNumber(String mrnNumber) {
		this.mrnNumber = mrnNumber;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getMrnDate() {
		return mrnDate;
	}

	public void setMrnDate(String mrnDate) {
		this.mrnDate = mrnDate;
	}

	public String getIsKroneRequired() {
		return isKroneRequired;
	}

	public void setIsKroneRequired(String isKroneRequired) {
		this.isKroneRequired = isKroneRequired;
	}
	
	public String getMrnMuxRequired() {
		return mrnMuxRequired;
	}

	public void setMrnMuxRequired(String mrnMuxRequired) {
		this.mrnMuxRequired = mrnMuxRequired;
	}

}
