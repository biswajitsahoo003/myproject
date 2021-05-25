package com.tcl.dias.cramer.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CramerConfigs {
    @Value("${app.cramer.base.url}")
    private String cramerBaseUrl;

    @Value("${app.countryendpoint}")
    private String countryEndpoint;
    @Value("${app.stateendpoint}")
    private String stateEndpoint;
    @Value("${app.cityendpoint}")
    private String cityEndpoint;
    @Value("${app.areaendpoint}")
    private String areaEndpoint;
    @Value("${app.isdeviceexistsendpoint}")
    private String isDeviceExistsEndpoint;
    @Value("${app.building4areaendpoint}")
    private String building4AreaEndpoint;
    @Value("${app.createObjectendpoint}")
    private String createObjectEndpoint;
    @Value("${app.updateObjectendpoint}")
    private String updateObjectEndpoint;
    @Value("${app.getSupportedCardTypeEndpoint}")
    private String getSupportedCardTypeEndpoint;

    public String getCramerBaseUrl() {
        return cramerBaseUrl;
    }

    public void setCramerBaseUrl(String cramerBaseUrl) {
        this.cramerBaseUrl = cramerBaseUrl;
    }

    public String getCountryEndpoint() {
        return countryEndpoint;
    }

    public void setCountryEndpoint(String countryEndpoint) {
        this.countryEndpoint = countryEndpoint;
    }

    public String getStateEndpoint() {
        return stateEndpoint;
    }

    public void setStateEndpoint(String stateEndpoint) {
        this.stateEndpoint = stateEndpoint;
    }

    public String getCityEndpoint() {
        return cityEndpoint;
    }

    public void setCityEndpoint(String cityEndpoint) {
        this.cityEndpoint = cityEndpoint;
    }

    public String getAreaEndpoint() {
        return areaEndpoint;
    }

    public void setAreaEndpoint(String areaEndpoint) {
        this.areaEndpoint = areaEndpoint;
    }

    public String getIsDeviceExistsEndpoint() {
        return isDeviceExistsEndpoint;
    }

    public void setIsDeviceExistsEndpoint(String isDeviceExistsEndpoint) {
        this.isDeviceExistsEndpoint = isDeviceExistsEndpoint;
    }

    public String getBuilding4AreaEndpoint() {
        return building4AreaEndpoint;
    }

    public void setBuilding4AreaEndpoint(String building4AreaEndpoint) {
        this.building4AreaEndpoint = building4AreaEndpoint;
    }

    public String getCreateObjectEndpoint() {
        return createObjectEndpoint;
    }

    public void setCreateObjectEndpoint(String createObjectEndpoint) {
        this.createObjectEndpoint = createObjectEndpoint;
    }

    public String getUpdateObjectEndpoint() {
        return updateObjectEndpoint;
    }

    public void setUpdateObjectEndpoint(String updateObjectEndpoint) {
        this.updateObjectEndpoint = updateObjectEndpoint;
    }

    public String getGetSupportedCardTypeEndpoint() {
        return getSupportedCardTypeEndpoint;
    }

    public void setGetSupportedCardTypeEndpoint(String getSupportedCardTypeEndpoint) {
        this.getSupportedCardTypeEndpoint = getSupportedCardTypeEndpoint;
    }
}
