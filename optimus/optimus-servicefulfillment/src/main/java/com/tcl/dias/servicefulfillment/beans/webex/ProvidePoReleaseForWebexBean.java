package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ProvidePoReleaseForWebexBean extends TaskDetailsBaseBean {

    private String webexLicencePoRelease;
    private String webexLicenceCCWWebOrderId;
    private String webexLicencePoReleaseCreatedDate;
    private String poReleaseCompletionDate;

    public String getWebexLicencePoRelease() {
        return webexLicencePoRelease;
    }

    public void setWebexLicencePoRelease(String webexLicencePoRelease) {
        this.webexLicencePoRelease = webexLicencePoRelease;
    }

	public String getWebexLicenceCCWWebOrderId() {
		return webexLicenceCCWWebOrderId;
	}

	public void setWebexLicenceCCWWebOrderId(String webexLicenceCCWWebOrderId) {
		this.webexLicenceCCWWebOrderId = webexLicenceCCWWebOrderId;
	}

	public String getWebexLicencePoReleaseCreatedDate() {
		return webexLicencePoReleaseCreatedDate;
	}

	public void setWebexLicencePoReleaseCreatedDate(String webexLicencePoReleaseCreatedDate) {
		this.webexLicencePoReleaseCreatedDate = webexLicencePoReleaseCreatedDate;
	}

	public String getPoReleaseCompletionDate() {
		return poReleaseCompletionDate;
	}

	public void setPoReleaseCompletionDate(String poReleaseCompletionDate) {
		this.poReleaseCompletionDate = poReleaseCompletionDate;
	}
    
    
    
    
}
