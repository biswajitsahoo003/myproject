package com.tcl.dias.serviceinventory.beans;

/**
 * Bean class to hold all details of configurations
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class GSCConfigurationDetailsBean {

    private Integer customerLeId;
    private String customerLeName;
    private String accessType;
    private String secsId;
    private String tollFreeNumber;
    private Integer orderId;
    private Integer assetId;
    private String assetType;
    private String number;
    private String outpulse;
    private String origin;
    private String destination;
    private String originNetwork;
    private String tigerOrderId;
    private String gscOrderSequenceId;

    public Integer getCustomerLeId() {
        return customerLeId;
    }

    public void setCustomerLeId(Integer customerLeId) {
        this.customerLeId = customerLeId;
    }

    public String getCustomerLeName() {
        return customerLeName;
    }

    public void setCustomerLeName(String customerLeName) {
        this.customerLeName = customerLeName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getSecsId() {
        return secsId;
    }

    public void setSecsId(String secsId) {
        this.secsId = secsId;
    }

    public String getTollFreeNumber() {
        return tollFreeNumber;
    }

    public void setTollFreeNumber(String tollFreeNumber) {
        this.tollFreeNumber = tollFreeNumber;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAssetId() {
        return assetId;
    }

    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getOutpulse() {
        return outpulse;
    }

    public void setOutpulse(String outpulse) {
        this.outpulse = outpulse;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOriginNetwork() {
        return originNetwork;
    }

    public void setOriginNetwork(String originNetwork) {
        this.originNetwork = originNetwork;
    }

    public String getTigerOrderId() {
        return tigerOrderId;
    }

    public void setTigerOrderId(String tigerOrderId) {
        this.tigerOrderId = tigerOrderId;
    }

    public String getGscOrderSequenceId() {
        return gscOrderSequenceId;
    }

    public void setGscOrderSequenceId(String gscOrderSequenceId) {
        this.gscOrderSequenceId = gscOrderSequenceId;
    }
}
