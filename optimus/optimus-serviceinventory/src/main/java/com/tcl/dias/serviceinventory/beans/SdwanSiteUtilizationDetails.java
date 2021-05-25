package com.tcl.dias.serviceinventory.beans;

import java.util.List;

public class SdwanSiteUtilizationDetails {
    private List<SiteBWUtilizationBean> siteBWUtilization;
    private String timestamp;
	private String key;

    public List<SiteBWUtilizationBean> getSiteBWUtilization() {
        return siteBWUtilization;
    }

    public void setSiteBWUtilization(List<SiteBWUtilizationBean> siteBWUtilization) {
        this.siteBWUtilization = siteBWUtilization;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
