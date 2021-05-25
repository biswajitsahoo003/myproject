package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * 
 * Internal cabling completion details.
 * 
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 *
 */
public class InternalCablingRequest extends TaskDetailsBaseBean {
	
	private String actualCableLengthUsed;
	private String actualCableLengthUsedB;
	private String remark;
	private String connectivityDetail;
	private String  connectivityDetailB;
	private String  connectivityStatus;
	private String rackDetail;
	private String  rackDetailB;
	private String typeOfRack;
	private String  typeOfRackB;
	private String  pathAvailable;
	private String  fiberAvailable;
	private String  typeOfIBDWork;
	private String  surveyRemarks;

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String internalCablingCompletionDate;
	
	private List<AttachmentIdBean> documentIds;
	
	private String type;

	public String getActualCableLengthUsed() {
		return actualCableLengthUsed;
	}

	public void setActualCableLengthUsed(String actualCableLengthUsed) {
		this.actualCableLengthUsed = actualCableLengthUsed;
	}

	public String getInternalCablingCompletionDate() {
		return internalCablingCompletionDate;
	}

	public void setInternalCablingCompletionDate(String internalCablingCompletionDate) {
		this.internalCablingCompletionDate = internalCablingCompletionDate;
	}

	public String getRemark() { return remark; }

	public void setRemark(String remark) { this.remark = remark; }

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getConnectivityDetail() {
		return connectivityDetail;
	}

	public void setConnectivityDetail(String connectivityDetail) {
		this.connectivityDetail = connectivityDetail;
	}

	public String getConnectivityDetailB() {
		return connectivityDetailB;
	}

	public void setConnectivityDetailB(String connectivityDetailB) {
		this.connectivityDetailB = connectivityDetailB;
	}

	public String getConnectivityStatus() {
		return connectivityStatus;
	}

	public void setConnectivityStatus(String connectivityStatus) {
		this.connectivityStatus = connectivityStatus;
	}



	public String getActualCableLengthUsedB() {
		return actualCableLengthUsedB;
	}

	public void setActualCableLengthUsedB(String actualCableLengthUsedB) {
		this.actualCableLengthUsedB = actualCableLengthUsedB;
	}

	public String getRackDetail() {
		return rackDetail;
	}

	public void setRackDetail(String rackDetail) {
		this.rackDetail = rackDetail;
	}

	public String getRackDetailB() {
		return rackDetailB;
	}

	public void setRackDetailB(String rackDetailB) {
		this.rackDetailB = rackDetailB;
	}

	public String getTypeOfRack() {
		return typeOfRack;
	}

	public void setTypeOfRack(String typeOfRack) {
		this.typeOfRack = typeOfRack;
	}

	public String getTypeOfRackB() {
		return typeOfRackB;
	}

	public void setTypeOfRackB(String typeOfRackB) {
		this.typeOfRackB = typeOfRackB;
	}

	public String getPathAvailable() {
		return pathAvailable;
	}

	public void setPathAvailable(String pathAvailable) {
		this.pathAvailable = pathAvailable;
	}

	public String getFiberAvailable() {
		return fiberAvailable;
	}

	public void setFiberAvailable(String fiberAvailable) {
		this.fiberAvailable = fiberAvailable;
	}

	public String getTypeOfIBDWork() {
		return typeOfIBDWork;
	}

	public void setTypeOfIBDWork(String typeOfIBDWork) {
		this.typeOfIBDWork = typeOfIBDWork;
	}

	public String getSurveyRemarks() {
		return surveyRemarks;
	}

	public void setSurveyRemarks(String surveyRemarks) {
		this.surveyRemarks = surveyRemarks;
	}
	

}
