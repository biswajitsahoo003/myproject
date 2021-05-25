package com.tcl.dias.common.beans;

import java.io.Serializable;

public class TaskDetailBean implements Serializable
{
    private Integer quoteId;
    private String assignee;
    private String approverLevel;

    public Integer getQuoteId() {
        return quoteId;
    }

    public void setQuoteId(Integer quoteId) {
        this.quoteId = quoteId;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getApproverLevel() {
        return approverLevel;
    }

    public void setApproverLevel(String approverLevel) {
        this.approverLevel = approverLevel;
    }
}
