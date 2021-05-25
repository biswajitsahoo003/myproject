package com.tcl.dias.products.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CpeBomGscDto {

    private String bomCode;
    private String bandwidthInMbps;
    private String noOfCubeLicenses;
    private String maxMFTCards;
    private String maxNimModules;
    private Map<String,String> powerCables;
    private Map<String,String> sfpModules;
    private Map<String,String> nimModules;

    public String getBomCode() {
        return bomCode;
    }

    public void setBomCode(String bomCode) {
        this.bomCode = bomCode;
    }

    public String getBandwidthInMbps() {
        return bandwidthInMbps;
    }

    public void setBandwidthInMbps(String bandwidthInMbps) {
        this.bandwidthInMbps = bandwidthInMbps;
    }

    public String getNoOfCubeLicenses() {
        return noOfCubeLicenses;
    }

    public void setNoOfCubeLicenses(String noOfCubeLicenses) {
        this.noOfCubeLicenses = noOfCubeLicenses;
    }

    public String getMaxMFTCards() {
        return maxMFTCards;
    }

    public void setMaxMFTCards(String maxMFTCards) {
        this.maxMFTCards = maxMFTCards;
    }

    public String getMaxNimModules() {
        return maxNimModules;
    }

    public void setMaxNimModules(String maxNimModules) {
        this.maxNimModules = maxNimModules;
    }

    public Map<String, String> getPowerCables() {
        return powerCables;
    }

    public void setPowerCables(Map<String, String> powerCables) {
        this.powerCables = powerCables;
    }

    public Map<String, String> getSfpModules() {
        return sfpModules;
    }

    public void setSfpModules(Map<String, String> sfpModules) {
        this.sfpModules = sfpModules;
    }

    public Map<String, String> getNimModules() {
        return nimModules;
    }

    public void setNimModules(Map<String, String> nimModules) {
        this.nimModules = nimModules;
    }
}
