package com.tcl.dias.common.beans;


import java.util.ArrayList;
import java.util.List;

/**
 * This file contains the TerminationWaiverCreationBean.java class. used for sfdc
 *
 */
public class TerminationWaiverBean {

    private String recordTypeName;
    private String approverEmail;
    private String opportunityId;
    private Integer tpsId;
    private ETCRecordBean etcRecord;



    public String getRecordTypeName() {
        return recordTypeName;
    }

    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    public String getApproverEmail() {
        return approverEmail;
    }

    public void setApproverEmail(String approverEmail) {
        this.approverEmail = approverEmail;
    }


    public ETCRecordBean getEtcRecord() {
        return etcRecord;
    }

    public void setEtcRecord(ETCRecordBean etcRecord) {
        this.etcRecord = etcRecord;
    }

    public Integer getTpsId() {
        return tpsId;
    }
    public void setTpsId(Integer tpsId) {
        this.tpsId = tpsId;
    }

    public String getOpportunityId() {
        return opportunityId;
    }

    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }
}
