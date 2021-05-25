package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.AttachmentIdBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * Provide PR for teams dr media gateway bean
 *
 * @author srraghav
 */
public class ProvidePrForTeamsDREndpointBean extends TaskDetailsBaseBean {

	private String teamsdrEndpointPrNumber;

	private String teamsdrEndpointPrVendorName;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String teamsdrEndpointPrDate;

	private List<AttachmentIdBean> documentIds;

	public String getTeamsdrEndpointPrNumber() {
		return teamsdrEndpointPrNumber;
	}

	public void setTeamsdrEndpointPrNumber(String teamsdrEndpointPrNumber) {
		this.teamsdrEndpointPrNumber = teamsdrEndpointPrNumber;
	}

	public String getTeamsdrEndpointPrVendorName() {
		return teamsdrEndpointPrVendorName;
	}

	public void setTeamsdrEndpointPrVendorName(String teamsdrEndpointPrVendorName) {
		this.teamsdrEndpointPrVendorName = teamsdrEndpointPrVendorName;
	}

	public String getTeamsdrEndpointPrDate() {
		return teamsdrEndpointPrDate;
	}

	public void setTeamsdrEndpointPrDate(String teamsdrEndpointPrDate) {
		this.teamsdrEndpointPrDate = teamsdrEndpointPrDate;
	}

	public List<AttachmentIdBean> getDocumentIds() {
		return documentIds;
	}

	public void setDocumentIds(List<AttachmentIdBean> documentIds) {
		this.documentIds = documentIds;
	}
}
