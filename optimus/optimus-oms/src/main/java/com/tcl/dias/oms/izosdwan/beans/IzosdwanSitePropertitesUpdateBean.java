package com.tcl.dias.oms.izosdwan.beans;

import java.io.Serializable;

import com.tcl.dias.oms.beans.UpdateRequest;

public class IzosdwanSitePropertitesUpdateBean implements Serializable{
	private UpdateRequest gvpnSites;
	private UpdateRequest illSites;
	public UpdateRequest getGvpnSites() {
		return gvpnSites;
	}
	public void setGvpnSites(UpdateRequest gvpnSites) {
		this.gvpnSites = gvpnSites;
	}
	public UpdateRequest getIllSites() {
		return illSites;
	}
	public void setIllSites(UpdateRequest illSites) {
		this.illSites = illSites;
	}
	
}
