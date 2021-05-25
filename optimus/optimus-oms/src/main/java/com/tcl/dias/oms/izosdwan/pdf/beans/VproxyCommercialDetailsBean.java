package com.tcl.dias.oms.izosdwan.pdf.beans;

import java.util.List;

public class VproxyCommercialDetailsBean {
	private String solutionName;
	 private List<CommercialAttributesVproxy> commercialAttributesVproxy;
	public String getSolutionName() {
		return solutionName;
	}
	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}
	public List<CommercialAttributesVproxy> getCommercialAttributesVproxy() {
		return commercialAttributesVproxy;
	}
	public void setCommercialAttributesVproxy(List<CommercialAttributesVproxy> commercialAttributesVproxy) {
		this.commercialAttributesVproxy = commercialAttributesVproxy;
	}
}
