package com.tcl.dias.servicefulfillment.beans.webex;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class CustomerOnBoardTranningBean extends TaskDetailsBaseBean {
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String customerBoardingDate ;

	private String customerBoardingCompleted ;

	public String getCustomerBoardingDate() {
		return customerBoardingDate;
	}

	public void setCustomerBoardingDate(String customerBoardingDate) {
		this.customerBoardingDate = customerBoardingDate;
	}

	public String getCustomerBoardingCompleted() {
		return customerBoardingCompleted;
	}

	public void setCustomerBoardingCompleted(String customerBoardingCompleted) {
		this.customerBoardingCompleted = customerBoardingCompleted;
	}
	
	
}
