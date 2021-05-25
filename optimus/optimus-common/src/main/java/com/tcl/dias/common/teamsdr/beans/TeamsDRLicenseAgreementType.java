package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

/**
 * Bean for teamsDR License based on agreement type
 * @author Srinivasa Raghavan
 */
public class TeamsDRLicenseAgreementType {
    private String agreementType;
    private List<TeamsDRLicenseBasedOnProvider> teamsDRLicenseBasedOnProviders;

    public TeamsDRLicenseAgreementType() {
    }

    public String getAgreementType() {
        return agreementType;
    }

    public void setAgreementType(String agreementType) {
        this.agreementType = agreementType;
    }

    public List<TeamsDRLicenseBasedOnProvider> getTeamsDRLicenseBasedOnProviders() {
        return teamsDRLicenseBasedOnProviders;
    }

    public void setTeamsDRLicenseBasedOnProviders(List<TeamsDRLicenseBasedOnProvider> teamsDRLicenseBasedOnProviders) {
        this.teamsDRLicenseBasedOnProviders = teamsDRLicenseBasedOnProviders;
    }

    @Override
    public String toString() {
        return "TeamsDRLicenseAgreementType{" +
                "agreementType='" + agreementType + '\'' +
                ", teamsDRLicenseBasedOnProviders=" + teamsDRLicenseBasedOnProviders +
                '}';
    }
}
