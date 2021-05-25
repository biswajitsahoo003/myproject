package com.tcl.dias.servicefulfillment.beans;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * This class is used to Get Internal Cabling Completion Confirmation
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class InternalCablingCompletionConfirmationBean {

	@DateTimeFormat(pattern="yyyy-MM-dd")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private String internalCablingCompletionDate;

	public String getInternalCablingCompletionDate() {
		return internalCablingCompletionDate;
	}

	public void setInternalCablingCompletionDate(String internalCablingCompletionDate) {
		this.internalCablingCompletionDate = internalCablingCompletionDate;
	}
	
	
}
