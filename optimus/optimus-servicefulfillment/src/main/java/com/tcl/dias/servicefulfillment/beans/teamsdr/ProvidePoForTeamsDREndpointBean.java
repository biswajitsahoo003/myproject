package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * Bean to provide PO for teamsDR Media gateway
 *
 * @author srraghav
 */
public class ProvidePoForTeamsDREndpointBean extends TaskDetailsBaseBean {

	private String teamsDrEndpointPoNumber;

	private String teamsDrEndpointPoVendorName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String teamsDrEndpointPoDate;

	private List<AttachmentIdBean> documentIds;

	public String getTeamsDrEndpointPoNumber() {
		return teamsDrEndpointPoNumber;
	}

	public void setTeamsDrEndpointPoNumber(String teamsDrEndpointPoNumber) {
		this.teamsDrEndpointPoNumber = teamsDrEndpointPoNumber;
	}

	public String getTeamsDrEndpointPoVendorName() {
		return teamsDrEndpointPoVendorName;
	}

	public void setTeamsDrEndpointPoVendorName(String teamsDrEndpointPoVendorName) {
		this.teamsDrEndpointPoVendorName = teamsDrEndpointPoVendorName;
	}

	public String getTeamsDrEndpointPoDate() {
		return teamsDrEndpointPoDate;
	}

	public void setTeamsDrEndpointPoDate(String teamsDrEndpointPoDate) {
		this.teamsDrEndpointPoDate = teamsDrEndpointPoDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
