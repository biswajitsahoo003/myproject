package com.tcl.dias.servicefulfillment.beans.gsc;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CreateDpFormBean extends TaskDetailsBaseBean {
	
	private String dpFormWebLink;
    private String isDpFormCreationCompleted;
    private String dpFormCompletionDate;
	public String getDpFormWebLink() {
		return dpFormWebLink;
	}
	
	public void setDpFormWebLink(String dpFormWebLink) {
		this.dpFormWebLink = dpFormWebLink;
	}
	
	public String getIsDpFormCreationCompleted() {
		return isDpFormCreationCompleted;
	}
	
	public void setIsDpFormCreationCompleted(String isDpFormCreationCompleted) {
		this.isDpFormCreationCompleted = isDpFormCreationCompleted;
	}

	public String getDpFormCompletionDate() {
		return dpFormCompletionDate;
	}

	public void setDpFormCompletionDate(String dpFormCompletionDate) {
		this.dpFormCompletionDate = dpFormCompletionDate;
	}


    
}
