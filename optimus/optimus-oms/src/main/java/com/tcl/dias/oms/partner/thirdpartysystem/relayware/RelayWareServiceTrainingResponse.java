package com.tcl.dias.oms.partner.thirdpartysystem.relayware;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Relayware Service Training Response
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelayWareServiceTrainingResponse {

    @JsonProperty("CERTIFICATIONCATEGORY")
    private String certificationCategory;

    @JsonProperty("RELAYWAREORGID")
    private String relaywareOrgId;

    @JsonProperty("SFDCCUID")
    private String sfdcCUID;

    @JsonProperty("CERTIFICATIONNAME")
    private String certificationName;

    @JsonProperty("UNIQUECODE")
    private String uniqueCode;

    @JsonProperty("REGISTRATIONDATE")
    private String registrationDate;

    @JsonProperty("STATUS")
    private String status;

    @JsonProperty("SFDCACCOUNTID")
    private String sfdcAccountId;

    public String getCertificationCategory() {
        return certificationCategory;
    }

    public void setCertificationCategory(String certificationCategory) {
        this.certificationCategory = certificationCategory;
    }

    public String getRelaywareOrgId() {
        return relaywareOrgId;
    }

    public void setRelaywareOrgId(String relaywareOrgId) {
        this.relaywareOrgId = relaywareOrgId;
    }

    public String getSfdcCUID() {
        return sfdcCUID;
    }

    public void setSfdcCUID(String sfdcCUID) {
        this.sfdcCUID = sfdcCUID;
    }

    public String getCertificationName() {
        return certificationName;
    }

    public void setCertificationName(String certificationName) {
        this.certificationName = certificationName;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSfdcAccountId() {
        return sfdcAccountId;
    }

    public void setSfdcAccountId(String sfdcAccountId) {
        this.sfdcAccountId = sfdcAccountId;
    }
}
