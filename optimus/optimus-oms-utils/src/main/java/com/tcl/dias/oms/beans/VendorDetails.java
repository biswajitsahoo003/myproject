package com.tcl.dias.oms.beans;

public class VendorDetails {
    String name;
    int isSelected;
    String type;
    int siteFeasibilityId ;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSiteFeasibilityId() {
        return siteFeasibilityId;
    }

    public void setSiteFeasibilityId(int siteFeasibilityId) {
        this.siteFeasibilityId = siteFeasibilityId;
    }

    @Override
    public String toString() {
        return "VendorDetails{" +
                "name='" + name + '\'' +
                ", isSelected=" + isSelected +
                ", type='" + type + '\'' +
                '}';
    }
}
