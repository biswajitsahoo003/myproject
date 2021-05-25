package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

/**
 * 
 * InterfaceProtocolMapping Bean Class
 * 
 *
 * @author diksha garg
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */

public class InterfaceProtocolMappingBean implements Serializable {
	private static final long serialVersionUID = 1L;

	private PEDetailsBean peDetailsBean;

	private CEDetailsBean ceDetailsBean;

	public PEDetailsBean getPeDetailsBean() {
		return peDetailsBean;
	}

	public void setPeDetailsBean(PEDetailsBean peDetailsBean) {
		this.peDetailsBean = peDetailsBean;
	}

	public CEDetailsBean getCeDetailsBean() {
		return ceDetailsBean;
	}

	public void setCeDetailsBean(CEDetailsBean ceDetailsBean) {
		this.ceDetailsBean = ceDetailsBean;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
}