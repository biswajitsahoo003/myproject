
package com.tcl.dias.common.sfdc.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * This file contains the CreateRequestV1.java class. used for sfdc
 * 
 * @author Muthuselvi S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bcrRecord",
    "RecordTypeName",
    "OpportunityId",
    "salesApproverEmailId",
    "cdaCmLevel1ApproverEmailId",
    "cdaCmLevel2ApproverEmailId",
    "cdaCmLevel3ApproverEmailId",
    "assignedToEmailId",
    
})
public class CreateRequestV1 {

    @JsonProperty("bcrRecord")
    private BcrRecord bcrRecord;
    @JsonProperty("RecordTypeName")
    private String recordTypeName;
    @JsonProperty("OpportunityId")
    private String opportunityId;
    @JsonProperty("salesApproverEmailId")
    private String salesApproverEmailId; 
    @JsonProperty("cdaCmLevel1ApproverEmailId")
    private String cdaCmLevel1ApproverEmailId;
    @JsonProperty("cdaCmLevel2ApproverEmailId")
    private String cdaCmLevel2ApproverEmailId;
    @JsonProperty("cdaCmLevel3ApproverEmailId")
    private String cdaCmLevel3ApproverEmailId;
    @JsonProperty("assignedToEmailId")
    private String assignedToEmailId;
    
    
    @JsonProperty("salesApproverEmailId")
    public String getSalesApproverEmailId() {
		return salesApproverEmailId;
	}

    @JsonProperty("salesApproverEmailId")
	public void setSalesApproverEmailId(String salesApproverEmailId) {
		this.salesApproverEmailId = salesApproverEmailId;
	}

	@JsonProperty("cdaCmLevel1ApproverEmailId")
    public String getCdaCmLevel1ApproverEmailId() {
		return cdaCmLevel1ApproverEmailId;
	}

    @JsonProperty("cdaCmLevel1ApproverEmailId")
	public void setCdaCmLevel1ApproverEmailId(String cdaCmLevel1ApproverEmailId) {
		this.cdaCmLevel1ApproverEmailId = cdaCmLevel1ApproverEmailId;
	}

    @JsonProperty("cdaCmLevel2ApproverEmailId")
	public String getCdaCmLevel2ApproverEmailId() {
		return cdaCmLevel2ApproverEmailId;
	}

    @JsonProperty("cdaCmLevel2ApproverEmailId")
	public void setCdaCmLevel2ApproverEmailId(String cdaCmLevel2ApproverEmailId) {
		this.cdaCmLevel2ApproverEmailId = cdaCmLevel2ApproverEmailId;
	}

    @JsonProperty("cdaCmLevel3ApproverEmailId")
	public String getCdaCmLevel3ApproverEmailId() {
		return cdaCmLevel3ApproverEmailId;
	}

    @JsonProperty("cdaCmLevel3ApproverEmailId")
	public void setCdaCmLevel3ApproverEmailId(String cdaCmLevel3ApproverEmailId) {
		this.cdaCmLevel3ApproverEmailId = cdaCmLevel3ApproverEmailId;
	}

    @JsonProperty("assignedToEmailId")
	public String getAssignedToEmailId() {
		return assignedToEmailId;
	}

    @JsonProperty("assignedToEmailId")
	public void setAssignedToEmailId(String assignedToEmailId) {
		this.assignedToEmailId = assignedToEmailId;
	}

	@JsonProperty("bcrRecord")
    public BcrRecord getBcrRecord() {
        return bcrRecord;
    }

    @JsonProperty("bcrRecord")
    public void setBcrRecord(BcrRecord bcrRecord) {
        this.bcrRecord = bcrRecord;
    }

    @JsonProperty("RecordTypeName")
    public String getRecordTypeName() {
        return recordTypeName;
    }

    @JsonProperty("RecordTypeName")
    public void setRecordTypeName(String recordTypeName) {
        this.recordTypeName = recordTypeName;
    }

    @JsonProperty("OpportunityId")
    public String getOpportunityId() {
        return opportunityId;
    }

    @JsonProperty("OpportunityId")
    public void setOpportunityId(String opportunityId) {
        this.opportunityId = opportunityId;
    }


}
