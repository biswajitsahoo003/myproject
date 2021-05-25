package com.tcl.dias.servicefulfillment.beans.webex;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ConfigAccessCodeBean extends TaskDetailsBaseBean {
    
	String accessCodeConfigCompleted;
    
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String accessCodeConfigDate;

    public String getAccessCodeConfigCompleted() {
        return accessCodeConfigCompleted;
    }

    public void setAccessCodeConfigCompleted(String accessCodeConfigCompleted) {
        this.accessCodeConfigCompleted = accessCodeConfigCompleted;
    }

    public String getAccessCodeConfigDate() {
        return accessCodeConfigDate;
    }

    public void setAccessCodeConfigDate(String accessCodeConfigDate) {
        this.accessCodeConfigDate = accessCodeConfigDate;
    }
}
