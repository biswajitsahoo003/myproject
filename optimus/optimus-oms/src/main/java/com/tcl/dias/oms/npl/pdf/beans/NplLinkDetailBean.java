package com.tcl.dias.oms.npl.pdf.beans;

import java.util.ArrayList;
import java.util.List;

import com.tcl.dias.oms.beans.SolutionDetail;
import com.tcl.dias.oms.npl.beans.NplSite;
import com.tcl.dias.oms.npl.beans.NplSiteDetail;
import com.tcl.dias.oms.npl.constants.SiteTypeConstants;

public class NplLinkDetailBean {
	private String offeringName;
	private String serviceId;
	private List<NplSiteDetail> sites = new ArrayList<>();
	List<SolutionDetail> solutions;
	
	
	
	
	
	
	public List<SolutionDetail> getSolutions() {
		return solutions;
	}
	public void setSolutions(List<SolutionDetail> solutions) {
		this.solutions = solutions;
	}
	public String getOfferingName() {
		return offeringName;
	}
	public void setOfferingName(String offeringName) {
		this.offeringName = offeringName;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public List<NplSiteDetail> getSites() {
		return sites;
	}
	public void setSites(List<NplSiteDetail> sites) {
		this.sites = sites;
	}
	
	
	
	
	
	
	

}
