package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tcl.dias.servicefulfillmentutils.base.request.BaseRequest;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

public class TaskSuspensionRequest extends BaseRequest {

    private Integer taskId;
    private Integer orderId;
    private String orderCode;
    private String suspensionReason;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Timestamp suspensionUpto;

    public TaskSuspensionRequest() {
    }

    public TaskSuspensionRequest(Integer taskId, Integer orderId, String orderCode, String suspensionReason, Timestamp suspensionUpto) {
        this.taskId = taskId;
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.suspensionReason = suspensionReason;
        this.suspensionUpto = suspensionUpto;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSuspensionReason() {
        return suspensionReason;
    }

    public void setSuspensionReason(String suspensionReason) {
        this.suspensionReason = suspensionReason;
    }

    public Date getSuspensionUpto() {
        return suspensionUpto;
    }

    public void setSuspensionUpto(Timestamp suspensionUpto) {
        this.suspensionUpto = suspensionUpto;
    }

    @Override
    public String toString() {
        return "TaskSuspensionRequest{" +
                "TaskId=" + this.taskId +
                ", OrderId=" + this.orderId +
                ", OrderCode='" + this.orderCode + '\'' +
                ", SuspensionReason='" + this.suspensionReason + '\'' +
                ", SuspensionUpto=" + this.suspensionUpto +
                '}';
    }
}