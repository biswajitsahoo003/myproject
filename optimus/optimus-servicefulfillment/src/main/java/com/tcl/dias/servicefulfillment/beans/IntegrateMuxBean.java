package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;


/**
 * IntegrateMuxBean - Used for Integrate Mux API
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class IntegrateMuxBean extends TaskDetailsBaseBean {

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String muxIntegrationDate;
	
	private String muxIntegrationStatus;

	public String getMuxIntegrationDate() {
		return muxIntegrationDate;
	}

	public void setMuxIntegrationDate(String muxIntegrationDate) {
		this.muxIntegrationDate = muxIntegrationDate;
	}

	public String getMuxIntegrationStatus() {
		return muxIntegrationStatus;
	}

	public void setMuxIntegrationStatus(String muxIntegrationStatus) {
		this.muxIntegrationStatus = muxIntegrationStatus;
	}
	
	
	
}
