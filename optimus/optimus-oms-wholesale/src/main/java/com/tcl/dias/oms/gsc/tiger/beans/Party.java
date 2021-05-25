package com.tcl.dias.oms.gsc.tiger.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Party {
    private String partyId;
    private Boolean isRequestor;
    private String orgId;
    private String role;
    private Contact contact;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    @JsonProperty("isRequestor")
    public Boolean getRequestor() {
        return isRequestor;
    }

    @JsonProperty("isRequestor")
    public void setRequestor(Boolean requestor) {
        isRequestor = requestor;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }
}
