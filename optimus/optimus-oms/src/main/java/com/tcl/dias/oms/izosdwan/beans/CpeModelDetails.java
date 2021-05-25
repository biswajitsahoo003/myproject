package com.tcl.dias.oms.izosdwan.beans;

import java.util.List;

public class CpeModelDetails {

	private String nameOfCpe;
	
	private List<CpeModel> cpeModel;

	public String getNameOfCpe() {
		return nameOfCpe;
	}

	public void setNameOfCpe(String nameOfCpe) {
		this.nameOfCpe = nameOfCpe;
	}

	public List<CpeModel> getCpeModel() {
		return cpeModel;
	}

	public void setCpeModel(List<CpeModel> cpeModel) {
		this.cpeModel = cpeModel;
	}
	
}
