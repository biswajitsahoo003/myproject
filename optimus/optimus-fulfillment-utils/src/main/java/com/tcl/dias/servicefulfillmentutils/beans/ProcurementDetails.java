package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Savanya
 *
 */
public class ProcurementDetails {

	private List<ProcurementBean> procurementLst = new ArrayList<>();

	public List<ProcurementBean> getProcurementLst() {
		return procurementLst;
	}

	public void setProcurementLst(List<ProcurementBean> procurementLst) {
		this.procurementLst = procurementLst;
	}
	
	
}
