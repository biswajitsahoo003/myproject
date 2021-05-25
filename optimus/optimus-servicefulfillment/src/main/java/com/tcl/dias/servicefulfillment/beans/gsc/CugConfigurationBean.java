package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CugConfigurationBean extends TaskDetailsBaseBean {
    private String isCUGConfigurationCompleted;
    private String cugConfigurationCompletionDate;
    public String getIsCUGConfigurationCompleted() {
        return isCUGConfigurationCompleted;
    }

    public void setIsCUGConfigurationCompleted(String isCUGConfigurationCompleted) {
        this.isCUGConfigurationCompleted = isCUGConfigurationCompleted;
    }

	public String getCugConfigurationCompletionDate() {
		return cugConfigurationCompletionDate;
	}

	public void setCugConfigurationCompletionDate(String cugConfigurationCompletionDate) {
		this.cugConfigurationCompletionDate = cugConfigurationCompletionDate;
	}
}
