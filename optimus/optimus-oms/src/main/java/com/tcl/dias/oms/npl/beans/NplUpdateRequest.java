package com.tcl.dias.oms.npl.beans;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.oms.beans.UpdateRequest;

public class NplUpdateRequest {
	
	List<UpdateRequest> updateRequest=new ArrayList<UpdateRequest>();

	public List<UpdateRequest> getUpdateRequest() {
		return updateRequest;
	}

	public void setUpdateRequest(List<UpdateRequest> updateRequest) {
		this.updateRequest = updateRequest;
	}
	

}
