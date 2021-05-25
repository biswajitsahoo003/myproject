package com.tcl.dias.servicefulfillment.beans.gsc;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ServiceAcceptanceBillingBean {
	
	private Integer id;
	
	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String billingStartDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBillStartDate() {
		return billingStartDate;
	}

	public void setBillStartDate(String billStartDate) {
		this.billingStartDate = billStartDate;
	}

}
