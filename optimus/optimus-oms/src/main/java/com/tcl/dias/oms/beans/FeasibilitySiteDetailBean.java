package com.tcl.dias.oms.beans;

/**this bean stores all the details of the sites feasible
 * @author Chetan chaudhary
 */
public class FeasibilitySiteDetailBean {
    private String siteCode;
    private Integer siteId;
    private String fpstatus;
    private Byte feasibility;
    private  String siteType;
    private String latitude;
    private String longitude;
    private String country;
    private String city;
    private String pincode;
    private String locality;
    private String address;
    private String iconName;
    private String iconNumber;
    private String remarks;
    private String locationId;

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }

    public Integer getSiteId() {
        return siteId;
    }

    public void setSiteId(Integer siteId) {
        this.siteId = siteId;
    }

    public String getFpstatus() {
        return fpstatus;
    }

    public void setFpstatus(String fpstatus) {
        this.fpstatus = fpstatus;
    }

    public Byte getFeasibility() {
        return feasibility;
    }

    public void setFeasibility(Byte feasibility) {
        this.feasibility = feasibility;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIconName() {
        return iconName;
    }

    public void setIconName(String iconName) {
        this.iconName = iconName;
    }

    public String getIconNumber() {
        return iconNumber;
    }

    public void setIconNumber(String iconNumber) {
        this.iconNumber = iconNumber;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "FeasibilitySiteDetailBean{" +
                "siteCode='" + siteCode + '\'' +
                ", siteId=" + siteId +
                ", fpstatus='" + fpstatus + '\'' +
                ", feasibility=" + feasibility +
                ", siteType='" + siteType + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", pincode='" + pincode + '\'' +
                ", locality='" + locality + '\'' +
                ", address='" + address + '\'' +
                ", iconName='" + iconName + '\'' +
                ", iconNumber='" + iconNumber + '\'' +
                ", remarks='" + remarks + '\'' +
                ", locationId=" + locationId +
                '}';
    }
}
