package com.tcl.dias.servicefulfillment.beans.teamsdr;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Release PO for teamsDR media gateway
 *
 * @author srraghav
 */
public class ReleasePoForEndpointBean extends TaskDetailsBaseBean {
	private String poReleaseEndpointInstall;
	private String poReleaseEndpointSupport;
	private String endpointInstallPoReleaseCompletionDate;
	private String endpointSupportPoReleaseCompletionDate;

	public String getPoReleaseEndpointInstall() {
		return poReleaseEndpointInstall;
	}

	public void setPoReleaseEndpointInstall(String poReleaseEndpointInstall) {
		this.poReleaseEndpointInstall = poReleaseEndpointInstall;
	}

	public String getPoReleaseEndpointSupport() {
		return poReleaseEndpointSupport;
	}

	public void setPoReleaseEndpointSupport(String poReleaseEndpointSupport) {
		this.poReleaseEndpointSupport = poReleaseEndpointSupport;
	}

	public String getEndpointInstallPoReleaseCompletionDate() {
		return endpointInstallPoReleaseCompletionDate;
	}

	public void setEndpointInstallPoReleaseCompletionDate(String endpointInstallPoReleaseCompletionDate) {
		this.endpointInstallPoReleaseCompletionDate = endpointInstallPoReleaseCompletionDate;
	}

	public String getEndpointSupportPoReleaseCompletionDate() {
		return endpointSupportPoReleaseCompletionDate;
	}

	public void setEndpointSupportPoReleaseCompletionDate(String endpointSupportPoReleaseCompletionDate) {
		this.endpointSupportPoReleaseCompletionDate = endpointSupportPoReleaseCompletionDate;
	}
}
