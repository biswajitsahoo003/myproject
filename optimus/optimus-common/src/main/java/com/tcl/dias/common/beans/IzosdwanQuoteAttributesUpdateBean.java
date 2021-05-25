package com.tcl.dias.common.beans;

import java.util.List;
/**
 * 
 * This file contains the IzosdwanQuoteAttributesUpdateBean.java class.
 * 
 *
 * @author AnandhiV
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class IzosdwanQuoteAttributesUpdateBean {
	
	private Integer quoteId;
	private List<IzosdwanQuoteAttributesUpdateRequest> izosdwanQuoteAttributesUpdateRequests;
	public Integer getQuoteId() {
		return quoteId;
	}
	public void setQuoteId(Integer quoteId) {
		this.quoteId = quoteId;
	}
	public List<IzosdwanQuoteAttributesUpdateRequest> getIzosdwanQuoteAttributesUpdateRequests() {
		return izosdwanQuoteAttributesUpdateRequests;
	}
	public void setIzosdwanQuoteAttributesUpdateRequests(
			List<IzosdwanQuoteAttributesUpdateRequest> izosdwanQuoteAttributesUpdateRequests) {
		this.izosdwanQuoteAttributesUpdateRequests = izosdwanQuoteAttributesUpdateRequests;
	}
	

}
