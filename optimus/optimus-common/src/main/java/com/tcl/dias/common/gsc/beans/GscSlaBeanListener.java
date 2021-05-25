package com.tcl.dias.common.gsc.beans;

import java.util.List;

/**
 * Listener class for GscSlaView
 * 
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class GscSlaBeanListener {

	private String accessType;

	private List<GscSlaBean> gscSlaBeans;

	public List<GscSlaBean> getGscSlaBeans() {
		return gscSlaBeans;
	}

	public void setGscSlaBeans(List<GscSlaBean> gscSlaBeans) {
		this.gscSlaBeans = gscSlaBeans;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

}
