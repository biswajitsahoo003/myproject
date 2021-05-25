package com.tcl.dias.servicefulfillmentutils.beans.gsc;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DidNumberRequest {

    private String ownerType;
    private Integer customerId;
    private Integer ownerEntityId;
    private String contractServiceAbbr;
    private String tags;
    private List<DidNumberCustRequest> custRequests;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getOwnerEntityId() {
        return ownerEntityId;
    }

    public void setOwnerEntityId(Integer ownerEntityId) {
        this.ownerEntityId = ownerEntityId;
    }

    public String getContractServiceAbbr() {
        return contractServiceAbbr;
    }

    public void setContractServiceAbbr(String contractServiceAbbr) {
        this.contractServiceAbbr = contractServiceAbbr;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public List<DidNumberCustRequest> getCustRequests() {
        return custRequests;
    }

    public void setCustRequests(List<DidNumberCustRequest> custRequests) {
        this.custRequests = custRequests;
    }
}
