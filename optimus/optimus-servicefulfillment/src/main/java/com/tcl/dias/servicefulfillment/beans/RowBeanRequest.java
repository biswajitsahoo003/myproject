package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Bean class for RowBean
 *
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RowBeanRequest extends TaskDetailsBaseBean {

	private String roadAuthorityName;
	private Double oneTimeCharge;
	private Double recurringFixedCharge;
	private Double recurringVariableCharge;
	private Double taxes;
	private String demandNoteAvailable;
	private String demandNoteRemarks;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String tentativeClosureDate;
	private String exceptionReason;

	private List<AttachmentIdBean> documentIds;

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}

	public Double getOneTimeCharge() {
		return oneTimeCharge;
	}

	public void setOneTimeCharge(Double oneTimeCharge) {
		this.oneTimeCharge = oneTimeCharge;
	}

	public Double getRecurringFixedCharge() {
		return recurringFixedCharge;
	}

	public void setRecurringFixedCharge(Double recurringFixedCharge) {
		this.recurringFixedCharge = recurringFixedCharge;
	}

	public Double getRecurringVariableCharge() {
		return recurringVariableCharge;
	}

	public void setRecurringVariableCharge(Double recurringVariableCharge) {
		this.recurringVariableCharge = recurringVariableCharge;
	}

	public Double getTaxes() {
		return taxes;
	}

	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	public String getRoadAuthorityName() {
		return roadAuthorityName;
	}

	public void setRoadAuthorityName(String roadAuthorityName) {
		this.roadAuthorityName = roadAuthorityName;
	}

	public String getDemandNoteAvailable() {
		return demandNoteAvailable;
	}

	public void setDemandNoteAvailable(String demandNoteAvailable) {
		this.demandNoteAvailable = demandNoteAvailable;
	}

	public String getDemandNoteRemarks() {
		return demandNoteRemarks;
	}

	public void setDemandNoteRemarks(String demandNoteRemarks) {
		this.demandNoteRemarks = demandNoteRemarks;
	}

	public String getExceptionReason() {
		return exceptionReason;
	}

	public void setExceptionReason(String exceptionReason) {
		this.exceptionReason = exceptionReason;
	}

	public String getTentativeClosureDate() {
		return tentativeClosureDate;
	}

	public void setTentativeClosureDate(String tentativeClosureDate) {
		this.tentativeClosureDate = tentativeClosureDate;
	}

}
