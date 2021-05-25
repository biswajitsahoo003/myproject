package com.tcl.dias.common.teamsdr.beans;

import java.util.List;

/**
 * Bean for returning the set of licenses and providers from product catalog
 * @author Srinivasa Raghavan
 */
public class TeamsDRLicenseBasedOnProvider {
    private String provider;
    private List<String> licenses;

    public TeamsDRLicenseBasedOnProvider() {
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

	public List<String> getLicenses() {
        return licenses;
    }

	public void setLicenses(List<String> licenses) {
        this.licenses = licenses;
    }

    @Override
    public String toString() {
        return "TeamsDRLicenseBasedOnProvider{" +
                "provider='" + provider + '\'' +
                ", licenses=" + licenses +
                '}';
    }
}
