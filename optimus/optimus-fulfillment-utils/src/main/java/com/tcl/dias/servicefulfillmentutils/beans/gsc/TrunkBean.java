package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import java.util.List;

public class TrunkBean {
    private String trunkType;
    private String gsx;
    private List<String> deviceIp;
    private String trunkCc;
    private String trunkCps;
    private String trunkGroupName;
    private String vlanId;
    private String gtmPrefix;
    private String provisioningStatus;
    private String notes;
    private List<String> customerDeviceAddress;
    private String ifMultipleDeviceInfo;
    private String comments;
    private String routeLabel;
    private String interconnectId;
    private String testingStatus;
    private String trunkId;
    private Integer trunkPriority;

    public List<String> getDeviceIp() {
		return deviceIp;
	}

	public void setDeviceIp(List<String> deviceIp) {
		this.deviceIp = deviceIp;
	}

	public String getTrunkId() {
		return trunkId;
	}

	public void setTrunkId(String trunkId) {
		this.trunkId = trunkId;
	}

	public Integer getTrunkPriority() {
		return trunkPriority;
	}

	public void setTrunkPriority(Integer trunkPriority) {
		this.trunkPriority = trunkPriority;
	}

	public String getTrunkType() {
        return trunkType;
    }

    public void setTrunkType(String trunkType) {
        this.trunkType = trunkType;
    }

    public String getGsx() {
        return gsx;
    }

    public void setGsx(String gsx) {
        this.gsx = gsx;
    }

    public String getTrunkCc() {
        return trunkCc;
    }

    public void setTrunkCc(String trunkCc) {
        this.trunkCc = trunkCc;
    }

    public String getTrunkCps() {
        return trunkCps;
    }

    public void setTrunkCps(String trunkCps) {
        this.trunkCps = trunkCps;
    }

    public String getTrunkGroupName() {
        return trunkGroupName;
    }

    public void setTrunkGroupName(String trunkGroupName) {
        this.trunkGroupName = trunkGroupName;
    }

    public String getVlanId() {
        return vlanId;
    }

    public void setVlanId(String vlanId) {
        this.vlanId = vlanId;
    }

    public String getGtmPrefix() {
        return gtmPrefix;
    }

    public void setGtmPrefix(String gtmPrefix) {
        this.gtmPrefix = gtmPrefix;
    }

    public String getProvisioningStatus() {
        return provisioningStatus;
    }

    public void setProvisioningStatus(String provisioningStatus) {
        this.provisioningStatus = provisioningStatus;
    }

    public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<String> getCustomerDeviceAddress() {
		return customerDeviceAddress;
	}

	public void setCustomerDeviceAddress(List<String> customerDeviceAddress) {
		this.customerDeviceAddress = customerDeviceAddress;
	}

	public String getIfMultipleDeviceInfo() {
        return ifMultipleDeviceInfo;
    }

    public void setIfMultipleDeviceInfo(String ifMultipleDeviceInfo) {
        this.ifMultipleDeviceInfo = ifMultipleDeviceInfo;
    }

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getRouteLabel() {
		return routeLabel;
	}

	public void setRouteLabel(String routeLabel) {
		this.routeLabel = routeLabel;
	}

	public String getInterconnectId() {
		return interconnectId;
	}

	public void setInterconnectId(String interconnectId) {
		this.interconnectId = interconnectId;
	}

	public String getTestingStatus() {
		return testingStatus;
	}

	public void setTestingStatus(String testingStatus) {
		this.testingStatus = testingStatus;
	}

}
