package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

public class CpeInstallationBean{

    private Integer componentId;
    private String deviceName;
    private String localItContactName;
    private String localContactNumber;
    private String localContactEmailId;

    private String cpeBasicChassis;
    private String cpeInstallationCheckList;

    private String cpeSupplyHardwarePoNumber;
    private String cpeSupplyHardwareVendorName;
    private String cpeSerialNumber;
    private String cpeInstallationPoNumber;
    private String cpeInstallationVendorName;
    private String cpeSupportPoNumber;
    private String cpeSupportVendorName;
    private String cpeCardSerialNumber;

    private String cpeConsoleCableConnected;
    private List<AttachmentIdBean> documentIds;
    private String cpeOsVersion;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String dateOfCpeInstallation;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeAmcEndDate;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String cpeAmcStartDate;



    public Integer getComponentId() {
        return componentId;
    }

    public void setComponentId(Integer componentId) {
        this.componentId = componentId;
    }

    public String getLocalItContactName() {
        return localItContactName;
    }

    public void setLocalItContactName(String localItContactName) {
        this.localItContactName = localItContactName;
    }

    public String getLocalContactNumber() {
        return localContactNumber;
    }

    public void setLocalContactNumber(String localContactNumber) {
        this.localContactNumber = localContactNumber;
    }

    public String getLocalContactEmailId() {
        return localContactEmailId;
    }

    public void setLocalContactEmailId(String localContactEmailId) {
        this.localContactEmailId = localContactEmailId;
    }

    public String getCpeInstallationPoNumber() {
        return cpeInstallationPoNumber;
    }

    public void setCpeInstallationPoNumber(String cpeInstallationPoNumber) {
        this.cpeInstallationPoNumber = cpeInstallationPoNumber;
    }

    public String getCpeInstallationVendorName() {
        return cpeInstallationVendorName;
    }

    public void setCpeInstallationVendorName(String cpeInstallationVendorName) {
        this.cpeInstallationVendorName = cpeInstallationVendorName;
    }

    public String getCpeSupportVendorName() {
        return cpeSupportVendorName;
    }

    public void setCpeSupportVendorName(String cpeSupportVendorName) {
        this.cpeSupportVendorName = cpeSupportVendorName;
    }

    public String getCpeSupportPoNumber() {
        return cpeSupportPoNumber;
    }

    public void setCpeSupportPoNumber(String cpeSupportPoNumber) {
        this.cpeSupportPoNumber = cpeSupportPoNumber;
    }

    public String getCpeSupplyHardwarePoNumber() {
        return cpeSupplyHardwarePoNumber;
    }

    public void setCpeSupplyHardwarePoNumber(String cpeSupplyHardwarePoNumber) {
        this.cpeSupplyHardwarePoNumber = cpeSupplyHardwarePoNumber;
    }

    public String getCpeSupplyHardwareVendorName() {
        return cpeSupplyHardwareVendorName;
    }

    public void setCpeSupplyHardwareVendorName(String cpeSupplyHardwareVendorName) {
        this.cpeSupplyHardwareVendorName = cpeSupplyHardwareVendorName;
    }

    public String getCpeSerialNumber() {
        return cpeSerialNumber;
    }

    public void setCpeSerialNumber(String cpeSerialNumber) {
        this.cpeSerialNumber = cpeSerialNumber;
    }

    public String getCpeCardSerialNumber() {
        return cpeCardSerialNumber;
    }

    public void setCpeCardSerialNumber(String cpeCardSerialNumber) {
        this.cpeCardSerialNumber = cpeCardSerialNumber;
    }

    public String getCpeConsoleCableConnected() {
        return cpeConsoleCableConnected;
    }

    public void setCpeConsoleCableConnected(String cpeConsoleCableConnected) {
        this.cpeConsoleCableConnected = cpeConsoleCableConnected;
    }

    public List<AttachmentIdBean> getDocumentIds() {
        return documentIds;
    }

    public void setDocumentIds(List<AttachmentIdBean> documentIds) {
        this.documentIds = documentIds;
    }

    public String getDateOfCpeInstallation() {
        return dateOfCpeInstallation;
    }

    public void setDateOfCpeInstallation(String dateOfCpeInstallation) {
        this.dateOfCpeInstallation = dateOfCpeInstallation;
    }

    public String getCpeAmcEndDate() {
        return cpeAmcEndDate;
    }

    public void setCpeAmcEndDate(String cpeAmcEndDate) {
        this.cpeAmcEndDate = cpeAmcEndDate;
    }

    public String getCpeAmcStartDate() {
        return cpeAmcStartDate;
    }

    public void setCpeAmcStartDate(String cpeAmcStartDate) {
        this.cpeAmcStartDate = cpeAmcStartDate;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getCpeBasicChassis() {
        return cpeBasicChassis;
    }

    public void setCpeBasicChassis(String cpeBasicChassis) {
        this.cpeBasicChassis = cpeBasicChassis;
    }

    public String getCpeInstallationCheckList() {
        return cpeInstallationCheckList;
    }

    public void setCpeInstallationCheckList(String cpeInstallationCheckList) {
        this.cpeInstallationCheckList = cpeInstallationCheckList;
    }

    public String getCpeOsVersion() {
        return cpeOsVersion;
    }

    public void setCpeOsVersion(String cpeOsVersion) {
        this.cpeOsVersion = cpeOsVersion;
    }
}
