package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * Internal Cabling Completion Status
 * 
 * @author arjayapa
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class InternalCablingCompletionStatus extends TaskDetailsBaseBean {
	
	private String status;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String internalCablingCompletionDate;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getInternalCablingCompletionDate() {
		return internalCablingCompletionDate;
	}

	public void setInternalCablingCompletionDate(String internalCablingCompletionDate) {
		this.internalCablingCompletionDate = internalCablingCompletionDate;
	}	

}
