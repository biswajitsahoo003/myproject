package com.tcl.dias.servicefulfillmentutils.beans;

import com.tcl.dias.networkaugment.entity.entities.ScOrder;
import com.tcl.dias.networkaugment.entity.entities.Task;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class RejectionTaskBean {

    Integer taskId;

    String rejectionType;

    String rejectionReason;

    String rejectionToTask;

    String rejectedByUser;

    String rejectionTaskId;

    String orderId;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getRejectionType() {
        return rejectionType;
    }

    public void setRejectionType(String rejectionType) {
        this.rejectionType = rejectionType;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionToTask() {
        return rejectionToTask;
    }

    public void setRejectionToTask(String rejectionToTask) {
        this.rejectionToTask = rejectionToTask;
    }

    public String getRejectedByUser() {
        return rejectedByUser;
    }

    public void setRejectedByUser(String rejectedByUser) {
        this.rejectedByUser = rejectedByUser;
    }

    public String getRejectionTaskId() {
        return rejectionTaskId;
    }

    public void setRejectionTaskId(String rejectionTaskId) {
        this.rejectionTaskId = rejectionTaskId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
