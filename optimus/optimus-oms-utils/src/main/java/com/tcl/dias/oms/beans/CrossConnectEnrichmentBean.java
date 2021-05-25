package com.tcl.dias.oms.beans;

/**
 *
 * Contains enrichment details site level
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class CrossConnectEnrichmentBean {
    private String siteAName;
    private String siteANumber;
    private String siteAEmail;

    private String siteZName;
    private String siteZNumber;
    private String siteZEmail;

    private String siteABuilding;
    private String siteARoom;
    private String siteARack;
    private String siteAFloor;

    private String siteZBuilding;
    private String siteZRoom;
    private String siteZRack;
    private String siteZFloor;

    public String getSiteAName() {
        return siteAName;
    }

    public void setSiteAName(String siteAName) {
        this.siteAName = siteAName;
    }

    public String getSiteANumber() {
        return siteANumber;
    }

    public void setSiteANumber(String siteANumber) {
        this.siteANumber = siteANumber;
    }

    public String getSiteAEmail() {
        return siteAEmail;
    }

    public void setSiteAEmail(String siteAEmail) {
        this.siteAEmail = siteAEmail;
    }

    public String getSiteZName() {
        return siteZName;
    }

    public void setSiteZName(String siteZName) {
        this.siteZName = siteZName;
    }

    public String getSiteZNumber() {
        return siteZNumber;
    }

    public void setSiteZNumber(String siteZNumber) {
        this.siteZNumber = siteZNumber;
    }

    public String getSiteZEmail() {
        return siteZEmail;
    }

    public void setSiteZEmail(String siteZEmail) {
        this.siteZEmail = siteZEmail;
    }

    public String getSiteARoom() {
        return siteARoom;
    }

    public void setSiteARoom(String siteARoom) {
        this.siteARoom = siteARoom;
    }

    public String getSiteARack() {
        return siteARack;
    }

    public void setSiteARack(String siteARack) {
        this.siteARack = siteARack;
    }

    public String getSiteAFloor() {
        return siteAFloor;
    }

    public void setSiteAFloor(String siteAFloor) {
        this.siteAFloor = siteAFloor;
    }

    public String getSiteZRoom() {
        return siteZRoom;
    }

    public void setSiteZRoom(String siteZRoom) {
        this.siteZRoom = siteZRoom;
    }

    public String getSiteZRack() {
        return siteZRack;
    }

    public void setSiteZRack(String siteZRack) {
        this.siteZRack = siteZRack;
    }

    public String getSiteZFloor() {
        return siteZFloor;
    }

    public void setSiteZFloor(String siteZFloor) {
        this.siteZFloor = siteZFloor;
    }

    public String getSiteABuilding() {
        return siteABuilding;
    }

    public void setSiteABuilding(String siteABuilding) {
        this.siteABuilding = siteABuilding;
    }

    public String getSiteZBuilding() {
        return siteZBuilding;
    }

    public void setSiteZBuilding(String siteZBuilding) {
        this.siteZBuilding = siteZBuilding;
    }

    @Override
    public String toString() {
        return "CrossConnectEnrichmentBean{" +
                "siteAName='" + siteAName + '\'' +
                ", siteANumber='" + siteANumber + '\'' +
                ", siteAEmail='" + siteAEmail + '\'' +
                ", siteZName='" + siteZName + '\'' +
                ", siteZNumber='" + siteZNumber + '\'' +
                ", siteZEmail='" + siteZEmail + '\'' +
                ", siteABuilding='" + siteABuilding + '\'' +
                ", siteARoom='" + siteARoom + '\'' +
                ", siteARack='" + siteARack + '\'' +
                ", siteAFloor='" + siteAFloor + '\'' +
                ", siteZBuilding='" + siteZBuilding + '\'' +
                ", siteZRoom='" + siteZRoom + '\'' +
                ", siteZRack='" + siteZRack + '\'' +
                ", siteZFloor='" + siteZFloor + '\'' +
                '}';
    }
}

