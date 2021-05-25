package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;
import java.util.List;

public class MuxInfoAsyncBeans implements Serializable {

	private static final long serialVersionUID = -747039771001223842L;
	private String requestId;

	private List<ZendMuxDetails> zendMuxDetails;

	private String serviceId;

	private List<AendMuxDetails> aendMuxDetails;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	 
	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
 
	
	public List<ZendMuxDetails> getZendMuxDetails() {
		return zendMuxDetails;
	}

	public void setZendMuxDetails(List<ZendMuxDetails> zendMuxDetails) {
		this.zendMuxDetails = zendMuxDetails;
	}

	public List<AendMuxDetails> getAendMuxDetails() {
		return aendMuxDetails;
	}

	public void setAendMuxDetails(List<AendMuxDetails> aendMuxDetails) {
		this.aendMuxDetails = aendMuxDetails;
	}

	@Override
	public String toString() {
		return "ClassPojo [requestId = " + requestId + ", zendMuxDetails = " + zendMuxDetails + ", serviceId = "
				+ serviceId + ", aendMuxDetails = " + aendMuxDetails + "]";
	}
}
