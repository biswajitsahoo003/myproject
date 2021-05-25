package com.tcl.dias.oms.renewals.bean;

import java.util.List;
import java.util.Map;

public class RenewalsExcelObject {
	
	private boolean dual;
	
//	private Map<String, Double> mappingexcel;
	
	private Map<String, Map<String, Double>> renewalsComponentDetailList;

	public boolean isDual() {
		return dual;
	}

	public void setDual(boolean dual) {
		this.dual = dual;
	}

	public Map<String, Map<String, Double>> getRenewalsComponentDetailList() {
		return renewalsComponentDetailList;
	}

	public void setRenewalsComponentDetailList(Map<String, Map<String, Double>> renewalsComponentDetailList) {
		this.renewalsComponentDetailList = renewalsComponentDetailList;
	}

	/*
	 * public Map<String, Double> getMappingexcel() { return mappingexcel; }
	 * 
	 * public void setMappingexcel(Map<String, Double> mappingexcel) {
	 * this.mappingexcel = mappingexcel; }
	 */




}
