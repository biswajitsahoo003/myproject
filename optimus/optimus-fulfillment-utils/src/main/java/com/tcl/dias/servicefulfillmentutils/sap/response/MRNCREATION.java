package com.tcl.dias.servicefulfillmentutils.sap.response;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "MRN_No",
    "Status",
    "Remark"
})
public class MRNCREATION {

    @JsonProperty("MRN_No")
    private String mRNNo;
    @JsonProperty("Status")
    private String status;
    @JsonProperty("Remark")
    private String remark;

    @JsonProperty("MRN_No")
    public String getMRNNo() {
        return mRNNo;
    }

    @JsonProperty("MRN_No")
    public void setMRNNo(String mRNNo) {
        this.mRNNo = mRNNo;
    }

    @JsonProperty("Status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("Status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("Remark")
    public String getRemark() {
        return remark;
    }

    @JsonProperty("Remark")
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
