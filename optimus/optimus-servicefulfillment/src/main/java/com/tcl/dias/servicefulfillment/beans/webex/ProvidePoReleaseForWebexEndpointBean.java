package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ProvidePoReleaseForWebexEndpointBean extends TaskDetailsBaseBean {

    private String webexEndpointPoRelease;
    private String endpointPoReleaseCompletionDate;

    public String getWebexEndpointPoRelease() {
        return webexEndpointPoRelease;
    }

    public void setWebexEndpointPoRelease(String webexEndpointPoRelease) {
        this.webexEndpointPoRelease = webexEndpointPoRelease;
    }

	public String getEndpointPoReleaseCompletionDate() {
		return endpointPoReleaseCompletionDate;
	}

	public void setEndpointPoReleaseCompletionDate(String endpointPoReleaseCompletionDate) {
		this.endpointPoReleaseCompletionDate = endpointPoReleaseCompletionDate;
	}
    
    
    
    
}
