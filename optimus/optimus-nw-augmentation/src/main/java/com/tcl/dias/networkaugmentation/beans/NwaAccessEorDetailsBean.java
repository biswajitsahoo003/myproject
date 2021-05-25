package com.tcl.dias.networkaugmentation.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NwaAccessEorDetailsBean {
    private Integer orderId;

    private  String orderCode;

    private String  aEndEorID;


    private String  aEndNeighbourHostman;


    private String  aEndNeighbourPortDetails;


    private String  bEndEorId ;


    private String  bEndNeighbourPortDetails ;


    private String  cfmMipLevel ;


    private String  cityName ;


    private String  cmfMdLevel ;


    private String  configureAEndUplinkPort;


    private String  controlVlanId ;


    private String  descAEndNeighbour ;


    private String  descNewDeviceAEndUplinkPort ;


    private String  erpsRingId ;


    private String  gateWayIpAddressNtp ;


    private String  hostName ;


    private String  initTemplate;


    private String  managementVlanDescription ;


    private String  protectedInstanceId;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getaEndEorID() {
        return aEndEorID;
    }

    public void setaEndEorID(String aEndEorID) {
        this.aEndEorID = aEndEorID;
    }

    public String getaEndNeighbourHostman() {
        return aEndNeighbourHostman;
    }

    public void setaEndNeighbourHostman(String aEndNeighbourHostman) {
        this.aEndNeighbourHostman = aEndNeighbourHostman;
    }

    public String getaEndNeighbourPortDetails() {
        return aEndNeighbourPortDetails;
    }

    public void setaEndNeighbourPortDetails(String aEndNeighbourPortDetails) {
        this.aEndNeighbourPortDetails = aEndNeighbourPortDetails;
    }

    public String getbEndEorId() {
        return bEndEorId;
    }

    public void setbEndEorId(String bEndEorId) {
        this.bEndEorId = bEndEorId;
    }

    public String getbEndNeighbourPortDetails() {
        return bEndNeighbourPortDetails;
    }

    public void setbEndNeighbourPortDetails(String bEndNeighbourPortDetails) {
        this.bEndNeighbourPortDetails = bEndNeighbourPortDetails;
    }

    public String getCfmMipLevel() {
        return cfmMipLevel;
    }

    public void setCfmMipLevel(String cfmMipLevel) {
        this.cfmMipLevel = cfmMipLevel;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCmfMdLevel() {
        return cmfMdLevel;
    }

    public void setCmfMdLevel(String cmfMdLevel) {
        this.cmfMdLevel = cmfMdLevel;
    }

    public String getConfigureAEndUplinkPort() {
        return configureAEndUplinkPort;
    }

    public void setConfigureAEndUplinkPort(String configureAEndUplinkPort) {
        this.configureAEndUplinkPort = configureAEndUplinkPort;
    }

    public String getControlVlanId() {
        return controlVlanId;
    }

    public void setControlVlanId(String controlVlanId) {
        this.controlVlanId = controlVlanId;
    }

    public String getDescAEndNeighbour() {
        return descAEndNeighbour;
    }

    public void setDescAEndNeighbour(String descAEndNeighbour) {
        this.descAEndNeighbour = descAEndNeighbour;
    }

    public String getDescNewDeviceAEndUplinkPort() {
        return descNewDeviceAEndUplinkPort;
    }

    public void setDescNewDeviceAEndUplinkPort(String descNewDeviceAEndUplinkPort) {
        this.descNewDeviceAEndUplinkPort = descNewDeviceAEndUplinkPort;
    }

    public String getErpsRingId() {
        return erpsRingId;
    }

    public void setErpsRingId(String erpsRingId) {
        this.erpsRingId = erpsRingId;
    }

    public String getGateWayIpAddressNtp() {
        return gateWayIpAddressNtp;
    }

    public void setGateWayIpAddressNtp(String gateWayIpAddressNtp) {
        this.gateWayIpAddressNtp = gateWayIpAddressNtp;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getInitTemplate() {
        return initTemplate;
    }

    public void setInitTemplate(String initTemplate) {
        this.initTemplate = initTemplate;
    }

    public String getManagementVlanDescription() {
        return managementVlanDescription;
    }

    public void setManagementVlanDescription(String managementVlanDescription) {
        this.managementVlanDescription = managementVlanDescription;
    }

    public String getProtectedInstanceId() {
        return protectedInstanceId;
    }

    public void setProtectedInstanceId(String protectedInstanceId) {
        this.protectedInstanceId = protectedInstanceId;
    }

    public String getSwitchLocation() {
        return switchLocation;
    }

    public void setSwitchLocation(String switchLocation) {
        this.switchLocation = switchLocation;
    }

    public String getSubring() {
        return subring;
    }

    public void setSubring(String subring) {
        this.subring = subring;
    }

    public String getUplinkAPort() {
        return uplinkAPort;
    }

    public void setUplinkAPort(String uplinkAPort) {
        this.uplinkAPort = uplinkAPort;
    }

    public String getUploadNodeFlag() {
        return uploadNodeFlag;
    }

    public void setUploadNodeFlag(String uploadNodeFlag) {
        this.uploadNodeFlag = uploadNodeFlag;
    }

    private String  switchLocation ;


    private String  subring;


    private String  uplinkAPort;


    private String  uploadNodeFlag;

}
