package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;
import java.util.List;

public class SEASiteUpdateRequest implements Serializable{
	private List<SEASiteUpdateRequestDetails> updateRequests;

	public List<SEASiteUpdateRequestDetails> getUpdateRequests() {
		return updateRequests;
	}

	public void setUpdateRequests(List<SEASiteUpdateRequestDetails> updateRequests) {
		this.updateRequests = updateRequests;
	}
	
}
