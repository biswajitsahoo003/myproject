package com.tcl.dias.servicefulfillment.beans;

public class MuxDetailBean {

    private String endMUXNodeIP;
    private String endMUXNodeName;

    public String getEndMUXNodeIP() {
        return endMUXNodeIP;
    }

    public void setEndMUXNodeIP(String endMUXNodeIP) {
        this.endMUXNodeIP = endMUXNodeIP;
    }

    public String getEndMUXNodeName() {
        return endMUXNodeName;
    }

    public void setEndMUXNodeName(String endMUXNodeName) {
        this.endMUXNodeName = endMUXNodeName;
    }

    @Override
    public String toString() {
        return "MuxDetailBean{" +
                "endMUXNodeIP='" + endMUXNodeIP + '\'' +
                ", endMUXNodeName='" + endMUXNodeName + '\'' +
                '}';
    }
}
