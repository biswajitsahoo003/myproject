package com.tcl.dias.oms.partner.beans;

import com.tcl.dias.oms.beans.UserDetails;

import java.util.Set;

public class ParnterPsamDetail {

    private String partnerLEName;
    private Integer partnerLEId;
    private Set<UserDetails> psamEmailList;

    public String getPartnerLEName() {
        return partnerLEName;
    }

    public void setPartnerLEName(String partnerLEName) {
        this.partnerLEName = partnerLEName;
    }

    public Integer getPartnerLEId() {
        return partnerLEId;
    }

    public void setPartnerLEId(Integer partnerLEId) {
        this.partnerLEId = partnerLEId;
    }



    public Set<UserDetails> getPsamEmailList() {
        return psamEmailList;
    }

    public void setPsamEmailList(Set<UserDetails> psamEmailList) {
        this.psamEmailList = psamEmailList;
    }

}
