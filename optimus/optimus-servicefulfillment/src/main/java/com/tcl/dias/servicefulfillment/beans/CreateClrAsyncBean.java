package com.tcl.dias.servicefulfillment.beans;

public class CreateClrAsyncBean {

    private String applicationName;
    private String desciption;
    private ExtraAttrsBean extraAttrsBean;
    private Boolean isCLRchanged;
    private String requestId;
    private String scenarioType;
    private String serviceId;
    private Boolean status;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }

    public ExtraAttrsBean getExtraAttrsBean() {
        return extraAttrsBean;
    }

    public void setExtraAttrsBean(ExtraAttrsBean extraAttrsBean) {
        this.extraAttrsBean = extraAttrsBean;
    }

    public Boolean getIsCLRchanged() {
        return isCLRchanged;
    }

    public void setIsCLRchanged(Boolean isCLRchanged) {
        this.isCLRchanged = isCLRchanged;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(String scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
