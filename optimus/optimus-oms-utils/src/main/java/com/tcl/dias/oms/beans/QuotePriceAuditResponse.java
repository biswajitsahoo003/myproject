package com.tcl.dias.oms.beans;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.tcl.dias.common.beans.LocationDetail;

/**
 * 
 * This is the class used as response for the quote price audit api
 * 
 *
 * @author ANNE NISHA
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */

@JsonInclude(Include.NON_NULL)
public class QuotePriceAuditResponse {
	
	private List<QuotePriceAuditBean> quotePriceAuditDetails;
	
	private LocationDetail[] locationDetailsList;

	/**
	 * @return the quotePriceAuditDetails
	 */
	public List<QuotePriceAuditBean> getQuotePriceAuditDetails() {
		return quotePriceAuditDetails;
	}

	/**
	 * @param quotePriceAuditDetails the quotePriceAuditDetails to set
	 */
	public void setQuotePriceAuditDetails(List<QuotePriceAuditBean> quotePriceAuditDetails) {
		this.quotePriceAuditDetails = quotePriceAuditDetails;
	}

	/**
	 * @return the locationDetailsList
	 */
	public LocationDetail[] getLocationDetailsList() {
		return locationDetailsList;
	}

	/**
	 * @param locationDetailsList the locationDetailsList to set
	 */
	public void setLocationDetailsList(LocationDetail[] locationDetailsList) {
		this.locationDetailsList = locationDetailsList;
	}

	
	
	

}
