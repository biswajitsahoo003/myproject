package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

/**
 * ConfigureMuxBean - Bean for Configuring Mux
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

public class ConfigureMuxBean extends TaskDetailsBaseBean {

	private String muxInventoryStatus;

	public String getMuxInventoryStatus() {
		return muxInventoryStatus;
	}

	public void setMuxInventoryStatus(String muxInventoryStatus) {
		this.muxInventoryStatus = muxInventoryStatus;
	}
	
	
	
}
