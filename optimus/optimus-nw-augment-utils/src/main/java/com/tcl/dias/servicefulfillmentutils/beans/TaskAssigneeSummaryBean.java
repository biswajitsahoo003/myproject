package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * Bean for Task Summary details
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class TaskAssigneeSummaryBean {

    private Integer taskId;
    private Integer taskAssigmentId;
    private String taskAssigneeUserName;
    private String taskStatus;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getTaskAssigmentId() {
        return taskAssigmentId;
    }

    public void setTaskAssigmentId(Integer taskAssigmentId) {
        this.taskAssigmentId = taskAssigmentId;
    }

    public String getTaskAssigneeUserName() {
        return taskAssigneeUserName;
    }

    public void setTaskAssigneeUserName(String taskAssigneeUserName) {
        this.taskAssigneeUserName = taskAssigneeUserName;
    }

    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }
}
