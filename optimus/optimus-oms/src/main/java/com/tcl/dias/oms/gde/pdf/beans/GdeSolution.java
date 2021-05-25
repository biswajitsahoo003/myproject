package com.tcl.dias.oms.gde.pdf.beans;


import java.util.List;

public class GdeSolution {
    private String networkProtection;
    private String solutionName;
    private List<GdeSolutionLinkDetail> linkDetails;


    public String getNetworkProtection() {
        return networkProtection;
    }

    public void setNetworkProtection(String networkProtection) {
        this.networkProtection = networkProtection;
    }

    public String getSolutionName() {
        return solutionName;
    }

    public void setSolutionName(String solutionName) {
        this.solutionName = solutionName;
    }

    public List<GdeSolutionLinkDetail> getLinkDetails() {
        return linkDetails;
    }

    public void setLinkDetails(List<GdeSolutionLinkDetail> linkDetails) {
        this.linkDetails = linkDetails;
    }
}
