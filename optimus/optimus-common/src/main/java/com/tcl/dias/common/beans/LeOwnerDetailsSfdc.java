package com.tcl.dias.common.beans;

public class LeOwnerDetailsSfdc {


    private String ownerName;
    private String teamRole;
    private String isTeamMember;
    private String email;
    private String mobile;
    private String region;
    private String subRegion;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTeamRole() {
        return teamRole;
    }

    public void setTeamRole(String teamRole) {
        this.teamRole = teamRole;
    }

    public String getIsTeamMember() {
        return isTeamMember;
    }

    public void setIsTeamMember(String isTeamMember) {
        this.isTeamMember = isTeamMember;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubRegion() {
        return subRegion;
    }

    public void setSubRegion(String subRegion) {
        this.subRegion = subRegion;
    }

    @Override
    public String toString() {
        return "LeOwnerDetailsSfdc{" +
                "ownerName='" + ownerName + '\'' +
                ", teamRole='" + teamRole + '\'' +
                ", isTeamMember='" + isTeamMember + '\'' +
                ", email='" + email + '\'' +
                ", mobile='" + mobile + '\'' +
                ", region='" + region + '\'' +
                ", subRegion='" + subRegion + '\'' +
                '}';
    }
}
