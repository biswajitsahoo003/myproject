package com.tcl.dias.sfdc.bean;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({ "RecordTypeName","approverEmail", "opportunityId", "etcRecord"})
public class SfdcWaiverRequestBean extends BaseBean {

    @JsonProperty("RecordTypeName")
    private String recordTypeName;
    @JsonProperty("approverEmail")
    private String approverEmail;
    @JsonProperty("opportunityId")
    private String opportunityId;
    @JsonProperty("etcRecord")
    private EtcRecordBean etcRecord;

    @JsonProperty("RecordTypeName")
    public String getRecordTypeName() { return recordTypeName; }

    @JsonProperty("RecordTypeName")
    public void setRecordTypeName(String recordTypeName) { this.recordTypeName = recordTypeName; }

    @JsonProperty("approverEmail")
    public String getApproverEmail() { return approverEmail; }

    @JsonProperty("approverEmail")
    public void setApproverEmail(String approverEmail) { this.approverEmail = approverEmail; }

    @JsonProperty("etcRecord")
    public EtcRecordBean getEtcRecord() {
        return etcRecord;
    }

    @JsonProperty("etcRecord")
    public void setEtcRecord(EtcRecordBean etcRecord) {
        this.etcRecord = etcRecord;
    }

    @JsonProperty("opportunityId")
    public String getOpportunityId() {
        return opportunityId;
    }

    @JsonProperty("opportunityId")
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }
}
