package com.tcl.dias.servicefulfillment.beans.webex;

import java.util.List;

import com.tcl.dias.servicefulfillment.beans.gsc.MicrositeDetailsBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class ActivateMicrositeBean extends TaskDetailsBaseBean {
	
	private List<MicrositeDetailsBean> micrositeDetails;

	public List<MicrositeDetailsBean> getMicrositeDetails() {
		return micrositeDetails;
	}

	public void setMicrositeDetails(List<MicrositeDetailsBean> micrositeDetails) {
		this.micrositeDetails = micrositeDetails;
	}
}
