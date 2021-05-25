package com.tcl.dias.servicefulfillment.beans;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

import java.io.Serializable;

public class DemarcDetailsBean extends TaskDetailsBaseBean implements Serializable {
    private String demarcationWing;
    private String demarcationFloor;
    private String demarcationRoom;
    private String demarcationBuildingName;
    private String remarks;

    public String getDemarcationWing() { return demarcationWing; }

    public void setDemarcationWing(String demarcationWing) { this.demarcationWing = demarcationWing; }

    public String getDemarcationFloor() { return demarcationFloor;
    }

    public void setDemarcationFloor(String demarcationFloor) {
        this.demarcationFloor = demarcationFloor;
    }

    public String getDemarcationRoom() {
        return demarcationRoom;
    }

    public void setDemarcationRoom(String demarcationRoom) {
        this.demarcationRoom = demarcationRoom;
    }

    public String getDemarcationBuildingName() {
        return demarcationBuildingName;
    }

    public void setDemarcationBuildingName(String demarcationBuildingName) {
        this.demarcationBuildingName = demarcationBuildingName;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
