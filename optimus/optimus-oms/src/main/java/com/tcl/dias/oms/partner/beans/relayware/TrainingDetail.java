package com.tcl.dias.oms.partner.beans.relayware;

import com.tcl.dias.oms.partner.thirdpartysystem.relayware.RelayWareServiceTrainingResponse;

/**
 * Bean for Training Detail
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class TrainingDetail {

    private String relayWareOrgId;
    private String sfdcAccountID;
    private String sfdcCUID;
    private String certficationName;
    private String uniqueCode;
    private String registrationDate;

    public String getRelayWareOrgId() {
        return relayWareOrgId;
    }

    public void setRelayWareOrgId(String relayWareOrgId) {
        this.relayWareOrgId = relayWareOrgId;
    }

    public String getSfdcAccountID() {
        return sfdcAccountID;
    }

    public void setSfdcAccountID(String sfdcAccountID) {
        this.sfdcAccountID = sfdcAccountID;
    }

    public String getSfdcCUID() {
        return sfdcCUID;
    }

    public void setSfdcCUID(String sfdcCUID) {
        this.sfdcCUID = sfdcCUID;
    }

    public String getCertficationName() {
        return certficationName;
    }

    public void setCertficationName(String certficationName) {
        this.certficationName = certficationName;
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

    public static TrainingDetail fromRelayWareServiceTrainingResponse(RelayWareServiceTrainingResponse response) {
        TrainingDetail trainingDetail = new TrainingDetail();
        trainingDetail.setCertficationName(response.getCertificationName());
        trainingDetail.setRegistrationDate(response.getRegistrationDate());
        trainingDetail.setRelayWareOrgId(response.getRelaywareOrgId());
        trainingDetail.setSfdcAccountID(response.getSfdcAccountId());
        trainingDetail.setSfdcCUID(response.getSfdcCUID());
        trainingDetail.setUniqueCode(response.getUniqueCode());
        return trainingDetail;
    }
}
