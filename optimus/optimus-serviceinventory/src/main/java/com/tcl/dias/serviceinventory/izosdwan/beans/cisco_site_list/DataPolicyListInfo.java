package com.tcl.dias.serviceinventory.izosdwan.beans.cisco_site_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean for DataPolicyListInfo
 * 
 * 
 */
public class DataPolicyListInfo {
	List<VpnListInfoNew> vpnListInfoListNew=new ArrayList<>();
	
	private String directorRegion;
	
	

	public List<VpnListInfoNew> getVpnListInfoListNew() {
		return vpnListInfoListNew;
	}

	public void setVpnListInfoListNew(List<VpnListInfoNew> vpnListInfoListNew) {
		this.vpnListInfoListNew = vpnListInfoListNew;
	}

	public String getDirectorRegion() {
		return directorRegion;
	}

	public void setDirectorRegion(String directorRegion) {
		this.directorRegion = directorRegion;
	}

	
	
}