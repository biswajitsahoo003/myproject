package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class SitesBean {
	
	private List<SiteBean> sites;

	public List<SiteBean> getSites() {
		
		if(sites==null) {
			sites=new ArrayList<SiteBean>();
		}
		return sites;
	}

	public void setSites(List<SiteBean> sites) {
		this.sites = sites;
	}

}
