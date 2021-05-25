/**
 * @author vivek
 *
 * 
 */
package com.tcl.dias.servicefulfillmentutils.beans.sap;

import java.util.List;

/**
 * @author vivek
 *
 */
public class GrnSapHanaResponse {
	
	private List<GrnDetails> grnDetails;

	/**
	 * @return the grnDetails
	 */
	public List<GrnDetails> getGrnDetails() {
		return grnDetails;
	}

	/**
	 * @param grnDetails the grnDetails to set
	 */
	public void setGrnDetails(List<GrnDetails> grnDetails) {
		this.grnDetails = grnDetails;
	}

	@Override
	public String toString() {
		return "GrnSapHanaResponse [grnDetails=" + grnDetails + ", getGrnDetails()=" + getGrnDetails() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
	
	


	



}
