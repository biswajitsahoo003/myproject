package com.tcl.dias.servicefulfillment.beans.gsc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrgDetailsBean {
    private String orgBusType;
    private String orgActvDate;
    private String orgType;
    private String orgLegalName;
    private String orgAbbrName;
    private String prefLang;
    private String currency;
    private String cuId;
    private String vatExemptionReason;
    private String remarks;

    public String getOrgBusType() {
        return orgBusType;
    }

    public void setOrgBusType(String orgBusType) {
        this.orgBusType = orgBusType;
    }

    public String getOrgActvDate() {
        return orgActvDate;
    }

    public void setOrgActvDate(String orgActvDate) {
        this.orgActvDate = orgActvDate;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getOrgLegalName() {
        return orgLegalName;
    }

    public void setOrgLegalName(String orgLegalName) {
        this.orgLegalName = orgLegalName;
    }

    public String getOrgAbbrName() {
        return orgAbbrName;
    }

    public void setOrgAbbrName(String orgAbbrName) {
        this.orgAbbrName = orgAbbrName;
    }

    public String getPrefLang() {
        return prefLang;
    }

    public void setPrefLang(String prefLang) {
        this.prefLang = prefLang;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCuId() {
        return cuId;
    }

    public void setCuId(String cuId) {
        this.cuId = cuId;
    }

    public String getVatExemptionReason() {
        return vatExemptionReason;
    }

    public void setVatExemptionReason(String vatExemptionReason) {
        this.vatExemptionReason = vatExemptionReason;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
