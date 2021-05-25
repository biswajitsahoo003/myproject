package com.tcl.dias.common.beans;

import com.tcl.dias.common.sfdc.bean.OpportunityBean;

public class ThirdPartyResponseBean {

//    private String stageName;
    private String status;
    private String message;
    private OpportunityBean opportunity;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OpportunityBean getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(OpportunityBean opportunity) {
        this.opportunity = opportunity;
    }
}
