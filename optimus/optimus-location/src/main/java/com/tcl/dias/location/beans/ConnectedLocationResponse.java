package com.tcl.dias.location.beans;

/**
 * Connected Location bean
 *
 *
 * @author Thamizhsevi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class ConnectedLocationResponse {

    private String latitude;
    private String longitude;
    private Integer locationId;
    private String address;
    private String tempMarkerData;

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

    public Integer getLocationId() {
        return locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTempMarkerData() {
        return tempMarkerData;
    }

    public void setTempMarkerData(String tempMarkerData) {
        this.tempMarkerData = tempMarkerData;
    }



}
