package com.tcl.dias.servicefulfillmentutils.beans;

import java.io.Serializable;

public class EorIorDependencyBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer taskId;

    private String serviceCode;

    private String processInstanceId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
