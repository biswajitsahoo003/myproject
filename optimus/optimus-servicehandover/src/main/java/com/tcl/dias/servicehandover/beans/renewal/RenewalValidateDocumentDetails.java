package com.tcl.dias.servicehandover.beans.renewal;

import java.util.List;

import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

public class RenewalValidateDocumentDetails extends BaseRequest{

	private List<RenewalContractSiteDetails> renewalContractSiteDetailList;
	
	private Boolean isPODetailExists;
	
    private String action;

	public List<RenewalContractSiteDetails> getRenewalContractSiteDetailList() {
		return renewalContractSiteDetailList;
	}

	public void setRenewalContractSiteDetailList(List<RenewalContractSiteDetails> renewalContractSiteDetailList) {
		this.renewalContractSiteDetailList = renewalContractSiteDetailList;
	}

	public Boolean getIsPODetailExists() {
		return isPODetailExists;
	}

	public void setIsPODetailExists(Boolean isPODetailExists) {
		this.isPODetailExists = isPODetailExists;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
	
}
