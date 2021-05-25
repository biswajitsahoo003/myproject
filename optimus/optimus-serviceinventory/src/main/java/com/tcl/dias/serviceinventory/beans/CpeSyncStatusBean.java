package com.tcl.dias.serviceinventory.beans;

import com.tcl.dias.serviceinventory.util.ServiceInventoryConstants;

/**
 * For fetching cpe status/availability/syncStatus for a given template
 */
public class CpeSyncStatusBean {

    private String cpeName;
    private String cpeStatus;
    private String cpeAvailability;
    private String templateSyncStatus;

	public CpeSyncStatusBean() {
		this.cpeStatus = ServiceInventoryConstants.OFFLINE;
		this.cpeAvailability = ServiceInventoryConstants.UNAVAILABLE;
		this.templateSyncStatus = ServiceInventoryConstants.OUT_OF_SYNC;
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

    public String getTemplateSyncStatus() {
        return templateSyncStatus;
    }

    public void setTemplateSyncStatus(String templateSyncStatus) {
        this.templateSyncStatus = templateSyncStatus;
    }

    @Override
    public String toString() {
        return "CpeSyncStatusBean{" +
                "cpeName='" + cpeName + '\'' +
                ", cpeAvailability='" + cpeAvailability + '\'' +
                ", templateSyncStatus='" + templateSyncStatus + '\'' +
                '}';
    }
}
