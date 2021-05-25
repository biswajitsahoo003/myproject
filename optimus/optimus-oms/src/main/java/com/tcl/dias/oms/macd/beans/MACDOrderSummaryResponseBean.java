package com.tcl.dias.oms.macd.beans;

import java.util.List;

public class MACDOrderSummaryResponseBean {
	
	private List<MACDOrderSummaryResponse> macdOrderSummaryResponseList;
	private String changeRequests;
	private String demarcDone;

	public List<MACDOrderSummaryResponse> getMacdOrderSummaryResponseList() {
		return macdOrderSummaryResponseList;
	}

	public void setMacdOrderSummaryResponseList(List<MACDOrderSummaryResponse> macdOrderSummaryResponseList) {
		this.macdOrderSummaryResponseList = macdOrderSummaryResponseList;
	}

	public String getDemarcDone() {
		return demarcDone;
	}

	public void setDemarcDone(String demarcDone) {
		this.demarcDone = demarcDone;
	}

	public String getChangeRequests() {
		return changeRequests;
	}

	public void setChangeRequests(String changeRequests) {
		this.changeRequests = changeRequests;
	}
		

}
