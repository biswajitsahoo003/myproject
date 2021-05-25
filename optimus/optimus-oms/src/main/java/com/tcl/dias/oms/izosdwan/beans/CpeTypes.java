package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class CpeTypes {
	private String cpeTypeName;
	private List<CpeLinks>  cpeLinks;
	public String getCpeTypeName() {
		return cpeTypeName;
	}
	public void setCpeTypeName(String cpeTypeName) {
		this.cpeTypeName = cpeTypeName;
	}
	public List<CpeLinks> getCpeLinks() {
		return cpeLinks;
	}
	public void setCpeLinks(List<CpeLinks> cpeLinks) {
		this.cpeLinks = cpeLinks;
	}

}
