package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name="nwa_order_details_extnd")
public class NwaOrderDetailsExtnd implements Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code",unique = true)
    private String orderCode;

    @Column(name = "ethernet_access")
    private String ethernetAccess;

    @Column(name = "ethernet_access_eor_type")
    private String ethernetAccessEorType;

    @Column(name = "fiber_feasibility")
    private String fiberFeasibility;

    @Column(name = "field_ops_team")
    private String fieldOpsTeam;

    @Column(name = "gspi_branch")
    private String gspiBranch;

    @Column(name = "infra_feasibility")
    private String infraFeasibility;

    @Column(name = "ip_pool")
    private String ipPool;

    @Column(name = "is_mux_details_changed")
    private String isMuxDetailsChanged;

    @Column(name = "is_tejas_mumbai_pune_eci")
    private String isTejasMumbaiPuneEci;

    @Column(name = "is_term_existing_link_reqd")
    private String isTermExistingLinkReqd;

    @Column(name = "loop_back_ip0")
    private String loopBackIp0;

    @Column(name = "loop_back_ip1")
    private String loopBackIp1;

    @Column(name = "network_type")
    private String networkType;

    @Column(name = "po_frn_no")
    private String poFrnNo;

    @Column(name = "iprm")
    private String iprm;

    @Column(name = "port_feasibility")
    private String portFeasibility;

    @Column(name = "power_feasibility")
    private String powerFeasibility;

    @Column(name = "serial_no")
    private String serialNo;

    @Column(name = "software_version")
    private String softwareVersion;

    @Column(name = "warehouse_location")
    private String warehouseLocation;

    @Column(name = "web_report_attached")
    private String webReportAttached;

    @Column(name = "pe_date")
    private String peDate;

    @Column(name = "pe_required")
    private String peRequired;

    @Column(name = "field_ops")
    private String fieldOps;

    @Column(name = "configuration_management")
    String configrationManagment;

    @Column(name = "cramer_team")
    String cramerTeam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private	ScOrder scOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getIprm() {
        return iprm;
    }

    public void setIprm(String iprm) {
        this.iprm = iprm;
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

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }

    public String getPeDate() {
        return peDate;
    }

    public void setPeDate(String peDate) {
        this.peDate = peDate;
    }

    public String getPeRequired() {
        return peRequired;
    }

    public void setPeRequired(String peRequired) {
        this.peRequired = peRequired;
    }

    public String getFieldOps() {
        return fieldOps;
    }

    public void setFieldOps(String fieldOps) {
        this.fieldOps = fieldOps;
    }

    public String getConfigrationManagment() {
        return configrationManagment;
    }

    public void setConfigrationManagment(String configrationManagment) {
        this.configrationManagment = configrationManagment;
    }

    public String getCramerTeam() {
        return cramerTeam;
    }

    public void setCramerTeam(String cramerTeam) {
        this.cramerTeam = cramerTeam;
    }

    @Override
    public String toString() {
        return "NwaOrderDetailsExtnd{" +
                "id=" + id +
                ", orderCode='" + orderCode + '\'' +
                ", ethernetAccess='" + ethernetAccess + '\'' +
                ", ethernetAccessEorType='" + ethernetAccessEorType + '\'' +
                ", fiberFeasibility='" + fiberFeasibility + '\'' +
                ", fieldOpsTeam='" + fieldOpsTeam + '\'' +
                ", gspiBranch='" + gspiBranch + '\'' +
                ", infraFeasibility='" + infraFeasibility + '\'' +
                ", ipPool='" + ipPool + '\'' +
                ", isMuxDetailsChanged='" + isMuxDetailsChanged + '\'' +
                ", isTejasMumbaiPuneEci='" + isTejasMumbaiPuneEci + '\'' +
                ", isTermExistingLinkReqd='" + isTermExistingLinkReqd + '\'' +
                ", loopBackIp0='" + loopBackIp0 + '\'' +
                ", loopBackIp1='" + loopBackIp1 + '\'' +
                ", networkType='" + networkType + '\'' +
                ", poFrnNo='" + poFrnNo + '\'' +
                ", iprm='" + iprm + '\'' +
                ", portFeasibility='" + portFeasibility + '\'' +
                ", powerFeasibility='" + powerFeasibility + '\'' +
                ", serialNo='" + serialNo + '\'' +
                ", softwareVersion='" + softwareVersion + '\'' +
                ", warehouseLocation='" + warehouseLocation + '\'' +
                ", webReportAttached='" + webReportAttached + '\'' +
                ", peDate='" + peDate + '\'' +
                ", peRequired='" + peRequired + '\'' +
                ", fieldOps='" + fieldOps + '\'' +
                ", configrationManagment='" + configrationManagment + '\'' +
                ", cramerTeam='" + cramerTeam + '\'' +
                ", scOrder=" + scOrder +
                '}';
    }
}
