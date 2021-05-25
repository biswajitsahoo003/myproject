package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * This bean is used for Customer CPE Configuration Confirmation.
 * 
 *  @author yogesh
 */
public class CustomerCPEConfigConfirmationBean extends TaskDetailsBaseBean {

	private String cabelingConfimation;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String tentativeCpeConfigConfirmationDate;

	public String getCabelingConfimation() {
		return cabelingConfimation;
	}

	public void setCabelingConfimation(String cabelingConfimation) {
		this.cabelingConfimation = cabelingConfimation;
	}

	public String getTentativeCpeConfigConfirmationDate() {
		return tentativeCpeConfigConfirmationDate;
	}

	public void setTentativeCpeConfigConfirmationDate(String tentativeCpeConfigConfirmationDate) {
		this.tentativeCpeConfigConfirmationDate = tentativeCpeConfigConfirmationDate;
	}
	
	
}
