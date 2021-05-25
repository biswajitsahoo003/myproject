package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;

/**
 * For displaying CPE info for each service ID
 * @author Srinivasa Raghavan
 */
public class SdwanCpeInfoBean {

    private Integer cpeId;
    private String cpeName;
    private String cpeStatus;
    private String cpeAvailability;
    private String inSyncStatus;
    private String underlayServiceId;

	public SdwanCpeInfoBean() {
		this.cpeStatus = ServiceInventoryConstants.OFFLINE;
		this.cpeAvailability = ServiceInventoryConstants.UNAVAILABLE;
		this.inSyncStatus = ServiceInventoryConstants.OUT_OF_SYNC;
	}

    public Integer getCpeId() {
        return cpeId;
    }

    public void setCpeId(Integer cpeId) {
        this.cpeId = cpeId;
    }

    public String getCpeName() {
        return cpeName;
    }

    public void setCpeName(String cpeName) {
        this.cpeName = cpeName;
    }

    public String getCpeStatus() {
        return cpeStatus;
    }

    public void setCpeStatus(String cpeStatus) {
        this.cpeStatus = cpeStatus;
    }

    public String getCpeAvailability() {
        return cpeAvailability;
    }

    public void setCpeAvailability(String cpeAvailability) {
        this.cpeAvailability = cpeAvailability;
    }

    public String getUnderlayServiceId() {
        return underlayServiceId;
    }

    public void setUnderlayServiceId(String underlayServiceId) {
        this.underlayServiceId = underlayServiceId;
    }

    public String getInSyncStatus() {
        return inSyncStatus;
    }

    public void setInSyncStatus(String inSyncStatus) {
        this.inSyncStatus = inSyncStatus;
    }
}
