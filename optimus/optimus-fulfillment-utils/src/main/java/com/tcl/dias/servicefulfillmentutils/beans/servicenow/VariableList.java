
package com.tcl.dias.servicefulfillmentutils.beans.servicenow;

public class VariableList {

    private String muxMake;
    private String muxModel;
    private String muxNodeIP;

    public VariableList() {
    }

    public VariableList(String muxMake, String muxModel, String muxNodeIP) {
        this.muxMake = muxMake;
        this.muxModel = muxModel;
        this.muxNodeIP = muxNodeIP;
    }

    public String getMuxMake() {
        return muxMake;
    }

    public void setMuxMake(String muxMake) {
        this.muxMake = muxMake;
    }

    public String getMuxModel() {
        return muxModel;
    }

    public void setMuxModel(String muxModel) {
        this.muxModel = muxModel;
    }

    public String getMuxNodeIP() {
        return muxNodeIP;
    }

    public void setMuxNodeIP(String muxNodeIP) {
        this.muxNodeIP = muxNodeIP;
    }

}
