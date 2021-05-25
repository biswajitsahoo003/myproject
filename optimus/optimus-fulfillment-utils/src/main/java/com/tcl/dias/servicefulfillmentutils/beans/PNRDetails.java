package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

public class PNRDetails {

	private String pnrCrossed;
	
	private String pnrCrossedForASite;
	
	private String pnrCrossedForBSite;

	private List<PNRTaskDetails> pnrTaskDetails;

	public String getPnrCrossed() {
		return pnrCrossed;
	}

	public void setPnrCrossed(String pnrCrossed) {
		this.pnrCrossed = pnrCrossed;
	}

	public List<PNRTaskDetails> getPnrTaskDetails() {
		return pnrTaskDetails;
	}

	public void setPnrTaskDetails(List<PNRTaskDetails> pnrTaskDetails) {
		this.pnrTaskDetails = pnrTaskDetails;
	}

	public String getPnrCrossedForASite() {
		return pnrCrossedForASite;
	}

	public void setPnrCrossedForASite(String pnrCrossedForASite) {
		this.pnrCrossedForASite = pnrCrossedForASite;
	}

	public String getPnrCrossedForBSite() {
		return pnrCrossedForBSite;
	}

	public void setPnrCrossedForBSite(String pnrCrossedForBSite) {
		this.pnrCrossedForBSite = pnrCrossedForBSite;
	}
	
	

}
