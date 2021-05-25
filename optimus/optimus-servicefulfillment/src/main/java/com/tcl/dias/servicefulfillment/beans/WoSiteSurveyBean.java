package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This class is used to Provide Work Order for Site Survey for RF LM Implementation
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class WoSiteSurveyBean extends TaskDetailsBaseBean {

	private String woNumber;
	private String workorderRequired;
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String woReleaseDate;
	private List<AttachmentIdBean> documentIds;

	
	public String getWoNumber() {
		return woNumber;
	}
	public void setWoNumber(String woNumber) {
		this.woNumber = woNumber;
	}
	public String getWoReleaseDate() {
		return woReleaseDate;
	}
	public void setWoReleaseDate(String woReleaseDate) {
		this.woReleaseDate = woReleaseDate;
	}
	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}
	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
	public String getWorkorderRequired() { return workorderRequired; }
	public void setWorkorderRequired(String workorderRequired) { this.workorderRequired = workorderRequired; }
}
