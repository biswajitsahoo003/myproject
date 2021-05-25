package com.tcl.dias.servicefulfillment.beans;

public class SubmitCLRCreationFailureResponseBean {

    private String applicationName;
    private ClrCreationFailureResponseBean clrCreationFailureResponse;
    private ExtraAttrsBean extraAttrs;
    private String requestId;
    private String scenarioType;
    private String serviceId;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public ClrCreationFailureResponseBean getClrCreationFailureResponse() {
        return clrCreationFailureResponse;
    }

    public void setClrCreationFailureResponse(ClrCreationFailureResponseBean clrCreationFailureResponse) {
        this.clrCreationFailureResponse = clrCreationFailureResponse;
    }

    public ExtraAttrsBean getExtraAttrs() {
        return extraAttrs;
    }

    public void setExtraAttrs(ExtraAttrsBean extraAttrs) {
        this.extraAttrs = extraAttrs;
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

}
