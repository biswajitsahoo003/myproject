package com.tcl.dias.servicefulfillment.beans;

import java.sql.Timestamp;

/**
 * This class is used for Complete Internal Cabling-CE Details
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CompleteInternalCablingCEBean {
	
	private Timestamp internalCablingCompletionDate;
	
	private String actualCableLengthUsed;
	
	private String filePath;

	public Timestamp getInternalCablingCompletionDate() {
		return internalCablingCompletionDate;
	}

	public void setInternalCablingCompletionDate(Timestamp internalCablingCompletionDate) {
		this.internalCablingCompletionDate = internalCablingCompletionDate;
	}

	public String getActualCableLengthUsed() {
		return actualCableLengthUsed;
	}

	public void setActualCableLengthUsed(String actualCableLengthUsed) {
		this.actualCableLengthUsed = actualCableLengthUsed;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
