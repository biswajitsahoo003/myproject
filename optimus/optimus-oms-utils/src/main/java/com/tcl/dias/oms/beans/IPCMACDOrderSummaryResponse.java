package com.tcl.dias.oms.beans;

import java.util.List;

/**
 * This file contains the bean to display the Order Summary details which will used in
 * order summary screen.
 *
 * @author Dimple S
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class IPCMACDOrderSummaryResponse {

	private List<IPCMACDAttributeSummaryResponse> ipcMACDAttributeSummaryList;

	private TotalSolutionQuote totalSolQuote;

	public List<IPCMACDAttributeSummaryResponse> getIpcMACDAttributeSummaryList() {
		return ipcMACDAttributeSummaryList;
	}

	public void setIpcMACDAttributeSummaryList(List<IPCMACDAttributeSummaryResponse> ipcMACDAttributeSummaryList) {
		this.ipcMACDAttributeSummaryList = ipcMACDAttributeSummaryList;
	}

	public TotalSolutionQuote getTotalSolQuote() {
		return totalSolQuote;
	}

	public void setTotalSolQuote(TotalSolutionQuote totalSolQuote) {
		this.totalSolQuote = totalSolQuote;
	}

}
