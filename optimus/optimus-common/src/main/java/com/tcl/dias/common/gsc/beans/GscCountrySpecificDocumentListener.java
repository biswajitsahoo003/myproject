package com.tcl.dias.common.gsc.beans;

public class GscCountrySpecificDocumentListener {

	private GscCountrySpecificDocumentBean gscCountrySpecificDocumentBean;

	public GscCountrySpecificDocumentBean getGscCountrySpecificDocumentBean() {
		return gscCountrySpecificDocumentBean;
	}

	public void setGscCountrySpecificDocumentBean(GscCountrySpecificDocumentBean gscCountrySpecificDocumentBean) {
		this.gscCountrySpecificDocumentBean = gscCountrySpecificDocumentBean;
	}

	@Override
	public String toString() {
		return "GscCountrySpecificDocumentListener{" +
				"gscCountrySpecificDocumentBean=" + gscCountrySpecificDocumentBean +
				'}';
	}
}
