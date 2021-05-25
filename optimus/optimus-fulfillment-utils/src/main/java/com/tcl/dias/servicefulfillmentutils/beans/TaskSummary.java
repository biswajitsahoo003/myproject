package com.tcl.dias.servicefulfillmentutils.beans;

/**
 * Bean for Task Summary
 *
 * @author ANUSHA UNNI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class TaskSummary {

    private String commercialManagerName;
    private String closedTasks;
    private String inProgressTasks;
    private String totalTasks;

    public String getCommercialManagerName() {
        return commercialManagerName;
    }

    public void setCommercialManagerName(String commercialManagerName) {
        this.commercialManagerName = commercialManagerName;
    }

    public String getClosedTasks() {
        return closedTasks;
    }

    public void setClosedTasks(String closedTasks) {
        this.closedTasks = closedTasks;
    }

    public String getTotalTasks() {
        return totalTasks;
    }

    public String getInProgressTasks() {
        return inProgressTasks;
    }

    public void setInProgressTasks(String inProgressTasks) {
        this.inProgressTasks = inProgressTasks;
    }

    public void setTotalTasks(String totalTasks) {
        this.totalTasks = totalTasks;
    }
}
