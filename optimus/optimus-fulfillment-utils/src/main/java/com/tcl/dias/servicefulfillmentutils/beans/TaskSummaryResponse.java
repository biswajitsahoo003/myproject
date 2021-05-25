package com.tcl.dias.servicefulfillmentutils.beans;

import java.util.List;

/**
 * Bean for Task Summary Response
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class TaskSummaryResponse {

    private String totalUnassignedTasks;
    private String totalAssignedTasks;
    private List<TaskSummary> taskSummaryList;

    public List<TaskSummary> getTaskSummaryList() {
        return taskSummaryList;
    }

    public void setTaskSummaryList(List<TaskSummary> taskSummaryList) {
        this.taskSummaryList = taskSummaryList;
    }

    public String getTotalUnassignedTasks() {
        return totalUnassignedTasks;
    }

    public void setTotalUnassignedTasks(String totalUnassignedTasks) {
        this.totalUnassignedTasks = totalUnassignedTasks;
    }

    public String getTotalAssignedTasks() {
        return totalAssignedTasks;
    }

    public void setTotalAssignedTasks(String totalAssignedTasks) {
        this.totalAssignedTasks = totalAssignedTasks;
    }
}
