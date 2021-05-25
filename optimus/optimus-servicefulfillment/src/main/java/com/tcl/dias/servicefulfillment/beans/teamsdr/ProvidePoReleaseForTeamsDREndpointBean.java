package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Provide PO release for teams DR media gateway
 *
 * @author srraghav
 */
public class ProvidePoReleaseForTeamsDREndpointBean extends TaskDetailsBaseBean {

	private String teamsDrEndpointPoRelease;
	private String endpointPoReleaseCompletionDate;

	public String getTeamsDrEndpointPoRelease() {
		return teamsDrEndpointPoRelease;
	}

	public void setTeamsDrEndpointPoRelease(String teamsDrEndpointPoRelease) {
		this.teamsDrEndpointPoRelease = teamsDrEndpointPoRelease;
	}

	public String getEndpointPoReleaseCompletionDate() {
		return endpointPoReleaseCompletionDate;
	}

	public void setEndpointPoReleaseCompletionDate(String endpointPoReleaseCompletionDate) {
		this.endpointPoReleaseCompletionDate = endpointPoReleaseCompletionDate;
	}

}
