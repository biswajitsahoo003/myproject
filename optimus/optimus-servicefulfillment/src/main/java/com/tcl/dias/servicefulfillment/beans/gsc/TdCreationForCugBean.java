package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.tcl.dias.servicefulfillment.beans.webex.CugDialOutBean;
import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class TdCreationForCugBean extends TaskDetailsBaseBean {
	
	private List<CugDialOutBean> cugDialOut;
	private List<String> onNetDialBack;
	private String cugDialInNumber;
	private String cugCreationcompletionDate;

	public List<CugDialOutBean> getCugDialOut() {
		return cugDialOut;
	}

	public void setCugDialOut(List<CugDialOutBean> cugDialOut) {
		this.cugDialOut = cugDialOut;
	}

	public List<String> getOnNetDialBack() {
		return onNetDialBack;
	}

	public void setOnNetDialBack(List<String> onNetDialBack) {
		this.onNetDialBack = onNetDialBack;
	}

	public String getCugDialInNumber() {
		return cugDialInNumber;
	}

	public void setCugDialInNumber(String cugDialInNumber) {
		this.cugDialInNumber = cugDialInNumber;
	}

	public String getCugCreationcompletionDate() {
		return cugCreationcompletionDate;
	}

	public void setCugCreationcompletionDate(String cugCreationcompletionDate) {
		this.cugCreationcompletionDate = cugCreationcompletionDate;
	}
}
