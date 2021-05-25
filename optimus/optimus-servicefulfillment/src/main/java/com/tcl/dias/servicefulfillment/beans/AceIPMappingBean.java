package com.tcl.dias.servicefulfillment.beans;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class AceIPMappingBean implements Serializable {

	private static final long serialVersionUID = 2600264533933832282L;
	private List<String> aceIPs;

	public List<String> getAceIPs() {
		return aceIPs;
	}

	public void setAceIPs(List<String> aceIPs) {
		this.aceIPs = aceIPs;
	}

}
