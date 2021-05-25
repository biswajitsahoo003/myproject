package com.tcl.dias.servicefulfillment.beans.webex;

import com.tcl.dias.servicefulfillmentutils.beans.TaskDetailsBaseBean;

public class AdfsSsoIntegrationBean extends TaskDetailsBaseBean {

    private String claimRulesOnCustomerIdp;
    private String idpDetails;
    private String idpUrl;
    private String adfsIntergrationCompletionDate;
    private String ssoConfigurationDate;

    public String getClaimRulesOnCustomerIdp() {
        return claimRulesOnCustomerIdp;
    }

    public void setClaimRulesOnCustomerIdp(String claimRulesOnCustomerIdp) {
        this.claimRulesOnCustomerIdp = claimRulesOnCustomerIdp;
    }

    public String getIdpDetails() {
        return idpDetails;
    }

    public void setIdpDetails(String idpDetails) {
        this.idpDetails = idpDetails;
    }

    public String getIdpUrl() {
        return idpUrl;
    }

    public void setIdpUrl(String idpUrl) {
        this.idpUrl = idpUrl;
    }

    public String getAdfsIntergrationCompletionDate() {
        return adfsIntergrationCompletionDate;
    }

    public void setAdfsIntergrationCompletionDate(String adfsIntergrationCompletionDate) {
        this.adfsIntergrationCompletionDate = adfsIntergrationCompletionDate;
    }

    public String getSsoConfigurationDate() {
        return ssoConfigurationDate;
    }

    public void setSsoConfigurationDate(String ssoConfigurationDate) {
        this.ssoConfigurationDate = ssoConfigurationDate;
    }
}
