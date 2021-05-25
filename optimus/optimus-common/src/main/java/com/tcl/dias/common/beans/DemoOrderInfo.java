package com.tcl.dias.common.beans;

public class DemoOrderInfo {

    private Boolean isDemo;
    private String demoType;

    public Boolean getIsDemo() {
        return isDemo;
    }

    public void setIsDemo(Boolean isDemo) {
        this.isDemo = isDemo;
    }

    public String getDemoType() {
        return demoType;
    }

    public void setDemoType(String demoType) {
        this.demoType = demoType;
    }

    @Override
    public String toString() {
        return "DemoOrderInfo{" +
                "isDemo=" + isDemo +
                ", demoType='" + demoType + '\'' +
                '}';
    }
}
