package com.tcl.dias.servicefulfillment.beans.gsc;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OrganisationBean {
    private String status;
    private String message;
    private String orgId;
    private OrgDetailsBean orgDetails;
    private List< OrgAddressBean > orgAddress;
    private ContactBean contact;
    private String parentOrgId;
    private String customerType;
    private String orgIsLinkToOrg;
    private String corpCntryName;
    private String custCd;
    private String intgOwnerCd;
    private String orgAbbrCd;
    private String orgFinAbbrCd;
    private String printTaxFlag;

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

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public OrgDetailsBean getOrgDetails() {
        return orgDetails;
    }

    public void setOrgDetails(OrgDetailsBean orgDetails) {
        this.orgDetails = orgDetails;
    }

    public List<OrgAddressBean> getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(List<OrgAddressBean> orgAddress) {
        this.orgAddress = orgAddress;
    }

    public ContactBean getContact() {
        return contact;
    }

    public void setContact(ContactBean contact) {
        this.contact = contact;
    }

    public String getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(String parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getOrgIsLinkToOrg() {
        return orgIsLinkToOrg;
    }

    public void setOrgIsLinkToOrg(String orgIsLinkToOrg) {
        this.orgIsLinkToOrg = orgIsLinkToOrg;
    }

    public String getCorpCntryName() {
        return corpCntryName;
    }

    public void setCorpCntryName(String corpCntryName) {
        this.corpCntryName = corpCntryName;
    }

    public String getCustCd() {
        return custCd;
    }

    public void setCustCd(String custCd) {
        this.custCd = custCd;
    }

    public String getIntgOwnerCd() {
        return intgOwnerCd;
    }

    public void setIntgOwnerCd(String intgOwnerCd) {
        this.intgOwnerCd = intgOwnerCd;
    }

    public String getOrgAbbrCd() {
        return orgAbbrCd;
    }

    public void setOrgAbbrCd(String orgAbbrCd) {
        this.orgAbbrCd = orgAbbrCd;
    }

    public String getOrgFinAbbrCd() {
        return orgFinAbbrCd;
    }

    public void setOrgFinAbbrCd(String orgFinAbbrCd) {
        this.orgFinAbbrCd = orgFinAbbrCd;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

	public String getPrintTaxFlag() {
		return printTaxFlag;
	}

	public void setPrintTaxFlag(String printTaxFlag) {
		this.printTaxFlag = printTaxFlag;
	}
}
