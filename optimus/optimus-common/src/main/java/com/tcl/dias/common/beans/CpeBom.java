package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This  class used for cpe  bom related data
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CpeBom {
	
	private String bomName;

	private String uniCode;
	
	private List<CpeBomDetails> cpeBomDetails;

	public String getBomName() {
		return bomName;
	}

	public void setBomName(String bomName) {
		this.bomName = bomName;
	}

	public String getUniCode() {
		return uniCode;
	}

	public void setUniCode(String uniCode) {
		this.uniCode = uniCode;
	}

	public List<CpeBomDetails> getCpeBomDetails() {
		if(cpeBomDetails==null) {
			cpeBomDetails=new ArrayList<>();
		}
		return cpeBomDetails;
	}

	public void setCpeBomDetails(List<CpeBomDetails> cpeBomDetails) {
		this.cpeBomDetails = cpeBomDetails;
	}
	
	
	
	


}
