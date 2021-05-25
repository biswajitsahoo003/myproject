package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CreateTigerOrderBean extends TaskDetailsBaseBean {
	
	private String tigerOrderId;
    private String isTigerOrderCreationCompleted;
    private String tigerOrderCompletionDate;

    public String getTigerOrderId() {
		return tigerOrderId;
	}

	public void setTigerOrderId(String tigerOrderId) {
		this.tigerOrderId = tigerOrderId;
	}

	public String getIsTigerOrderCreationCompleted() {
        return isTigerOrderCreationCompleted;
    }

    public void setIsTigerOrderCreationCompleted(String isTigerOrderCreationCompleted) {
        this.isTigerOrderCreationCompleted = isTigerOrderCreationCompleted;
    }

	public String getTigerOrderCompletionDate() {
		return tigerOrderCompletionDate;
	}

	public void setTigerOrderCompletionDate(String tigerOrderCompletionDate) {
		this.tigerOrderCompletionDate = tigerOrderCompletionDate;
	}
}
