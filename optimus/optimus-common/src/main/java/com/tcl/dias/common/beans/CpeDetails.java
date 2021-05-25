package com.tcl.dias.common.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * This class used for cpe specific bom details
 *
 * @author VIVEK KUMAR K
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CpeDetails {

	private List<CpeBom> cpeBoms;

	public List<CpeBom> getCpeBoms() {
		if(cpeBoms==null) {
			cpeBoms=new ArrayList<>();
		}
		return cpeBoms;
	}

	public void setCpeBoms(List<CpeBom> cpeBoms) {
		this.cpeBoms = cpeBoms;
	}

}
