package com.tcl.dias.networkaugmentation.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NwaOrderDetailsExtndBean {

    private Integer orderId;


    private String orderCode;


    private String ethernetAccess;


    private String ethernetAccessEorType;


    private String fiberFeasibility;


    private String fieldOpsTeam;


    private String gspiBranch;

    private String infraFeasibility;


    private String ipPool;


    private String isMuxDetailsChanged;

    private String isTejasMumbaiPuneEci;

    private String isTermExistingLinkReqd;


    private String loopBackIp0;


    private String loopBackIp1;


    private String networkType;


    private String poFrnNo;


    private String portFeasibility;


    private String powerFeasibility;

    private String serialNo;


    private String softwareVersion;


    private String warehouseLocation;


    private String webReportAttached;

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

    public String getEthernetAccess() {
        return ethernetAccess;
    }

    public void setEthernetAccess(String ethernetAccess) {
        this.ethernetAccess = ethernetAccess;
    }

    public String getEthernetAccessEorType() {
        return ethernetAccessEorType;
    }

    public void setEthernetAccessEorType(String ethernetAccessEorType) {
        this.ethernetAccessEorType = ethernetAccessEorType;
    }

    public String getFiberFeasibility() {
        return fiberFeasibility;
    }

    public void setFiberFeasibility(String fiberFeasibility) {
        this.fiberFeasibility = fiberFeasibility;
    }

    public String getFieldOpsTeam() {
        return fieldOpsTeam;
    }

    public void setFieldOpsTeam(String fieldOpsTeam) {
        this.fieldOpsTeam = fieldOpsTeam;
    }

    public String getGspiBranch() {
        return gspiBranch;
    }

    public void setGspiBranch(String gspiBranch) {
        this.gspiBranch = gspiBranch;
    }

    public String getInfraFeasibility() {
        return infraFeasibility;
    }

    public void setInfraFeasibility(String infraFeasibility) {
        this.infraFeasibility = infraFeasibility;
    }

    public String getIpPool() {
        return ipPool;
    }

    public void setIpPool(String ipPool) {
        this.ipPool = ipPool;
    }

    public String getIsMuxDetailsChanged() {
        return isMuxDetailsChanged;
    }

    public void setIsMuxDetailsChanged(String isMuxDetailsChanged) {
        this.isMuxDetailsChanged = isMuxDetailsChanged;
    }

    public String getIsTejasMumbaiPuneEci() {
        return isTejasMumbaiPuneEci;
    }

    public void setIsTejasMumbaiPuneEci(String isTejasMumbaiPuneEci) {
        this.isTejasMumbaiPuneEci = isTejasMumbaiPuneEci;
    }

    public String getIsTermExistingLinkReqd() {
        return isTermExistingLinkReqd;
    }

    public void setIsTermExistingLinkReqd(String isTermExistingLinkReqd) {
        this.isTermExistingLinkReqd = isTermExistingLinkReqd;
    }

    public String getLoopBackIp0() {
        return loopBackIp0;
    }

    public void setLoopBackIp0(String loopBackIp0) {
        this.loopBackIp0 = loopBackIp0;
    }

    public String getLoopBackIp1() {
        return loopBackIp1;
    }

    public void setLoopBackIp1(String loopBackIp1) {
        this.loopBackIp1 = loopBackIp1;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getPoFrnNo() {
        return poFrnNo;
    }

    public void setPoFrnNo(String poFrnNo) {
        this.poFrnNo = poFrnNo;
    }

    public String getPortFeasibility() {
        return portFeasibility;
    }

    public void setPortFeasibility(String portFeasibility) {
        this.portFeasibility = portFeasibility;
    }

    public String getPowerFeasibility() {
        return powerFeasibility;
    }

    public void setPowerFeasibility(String powerFeasibility) {
        this.powerFeasibility = powerFeasibility;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }

    public String getWebReportAttached() {
        return webReportAttached;
    }

    public void setWebReportAttached(String webReportAttached) {
        this.webReportAttached = webReportAttached;
    }
}
