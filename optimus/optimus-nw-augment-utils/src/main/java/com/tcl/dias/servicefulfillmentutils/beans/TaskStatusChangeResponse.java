package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.networkaugment.entity.entities.ScOrder;

import java.io.Serializable;

public class TaskStatusChangeResponse {
    Integer taskId;
    Integer scOrderId;

    public TaskStatusChangeResponse() {
    }

    public TaskStatusChangeResponse(Integer taskId, Integer scOrderId) {
        this.taskId = taskId;
        this.scOrderId = scOrderId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getScOrderId() {
        return scOrderId;
    }

    public void setScOrderId(Integer scOrderId) {
        this.scOrderId = scOrderId;
    }
}
