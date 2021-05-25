package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * MastInstallationPlanBean - bean for mast installation api
 *
 * @author Yogesh
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class MastInstallationPlanBean extends TaskDetailsBaseBean {

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String proposedMastInstallationDate;
	
	private String feName;
	
	private String feContactNumber;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String mastStructureReadyDate;
	
	private String feEmailId;

	public String getProposedMastInstallationDate() {
		return proposedMastInstallationDate;
	}

	public void setProposedMastInstallationDate(String proposedMastInstallationDate) {
		this.proposedMastInstallationDate = proposedMastInstallationDate;
	}

	public String getFeName() {
		return feName;
	}

	public void setFeName(String feName) {
		this.feName = feName;
	}

	public String getFeContactNumber() {
		return feContactNumber;
	}

	public void setFeContactNumber(String feContactNumber) {
		this.feContactNumber = feContactNumber;
	}

	public String getMastStructureReadyDate() {
		return mastStructureReadyDate;
	}

	public void setMastStructureReadyDate(String mastStructureReadyDate) {
		this.mastStructureReadyDate = mastStructureReadyDate;
	}

	public String getFeEmailId() {
		return feEmailId;
	}

	public void setFeEmailId(String feEmailId) {
		this.feEmailId = feEmailId;
	}

	
	
	
	
	
}
