package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "nwa_bts_details")
public class NwaBtsDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "bh_type")
    private String bhType;

    @Column(name = "device_type")
    private String deviceIp;

    @Column(name = "bts_vlan")
    private String btsVlan;

    @Column(name = "circle")
    private String circle;

    @Column(name = "eohs_category")
    private String eohsCategory;

    @Column(name = "infra_agreement_expiry_date")
    private Timestamp infraAggrementExpiryDate;

    @Column(name = "infra_provider")
    private String infraProvider;

    @Column(name = "infra_provider_site_up_time")
    private String infraProviderSiteUpTime;

    @Column(name = "per_antenna_dimension")
    private String perAntennaDimension;

    @Column(name = "per_odu_weight")
    private String perOduWeight;

    @Column(name = "planned_deinstallation_date")
    private String plannedDeinstallationDate;

    @Column(name = "planned_installation_date")
    private String plannedInstallationDate;

    @Column(name = "reason_for_exist")
    private String reasonForExist;

    @Column(name = "site_id")
    private String siteId;

    @Column(name = "site_name")
    private String siteName;

    @Column(name = "site_type")
    private String siteType;

    @Column(name = "bts_subnet_mask")
    private String btsSubnetMask;

    @Column(name = "tcl_agreement_start_date")
    private Timestamp tclAggrementStartDate;

    @Column(name = "wireless_tech_sub_type")
    private String wirelessTechSubType;

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

    public String getBhType() {
        return bhType;
    }

    public void setBhType(String bhType) {
        this.bhType = bhType;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getBtsVlan() {
        return btsVlan;
    }

    public void setBtsVlan(String btsVlan) {
        this.btsVlan = btsVlan;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getEohsCategory() {
        return eohsCategory;
    }

    public void setEohsCategory(String eohsCategory) {
        this.eohsCategory = eohsCategory;
    }

    public Timestamp getInfraAggrementExpiryDate() {
        return infraAggrementExpiryDate;
    }

    public void setInfraAggrementExpiryDate(Timestamp infraAggrementExpiryDate) {
        this.infraAggrementExpiryDate = infraAggrementExpiryDate;
    }

    public String getInfraProvider() {
        return infraProvider;
    }

    public void setInfraProvider(String infraProvider) {
        this.infraProvider = infraProvider;
    }

    public String getInfraProviderSiteUpTime() {
        return infraProviderSiteUpTime;
    }

    public void setInfraProviderSiteUpTime(String infraProviderSiteUpTime) {
        this.infraProviderSiteUpTime = infraProviderSiteUpTime;
    }

    public String getPerAntennaDimension() {
        return perAntennaDimension;
    }

    public void setPerAntennaDimension(String perAntennaDimension) {
        this.perAntennaDimension = perAntennaDimension;
    }

    public String getPerOduWeight() {
        return perOduWeight;
    }

    public void setPerOduWeight(String perOduWeight) {
        this.perOduWeight = perOduWeight;
    }

    public String getPlannedDeinstallationDate() {
        return plannedDeinstallationDate;
    }

    public void setPlannedDeinstallationDate(String plannedDeinstallationDate) {
        this.plannedDeinstallationDate = plannedDeinstallationDate;
    }

    public String getPlannedInstallationDate() {
        return plannedInstallationDate;
    }

    public void setPlannedInstallationDate(String plannedInstallationDate) {
        this.plannedInstallationDate = plannedInstallationDate;
    }

    public String getReasonForExist() {
        return reasonForExist;
    }

    public void setReasonForExist(String reasonForExist) {
        this.reasonForExist = reasonForExist;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public String getBtsSubnetMask() {
        return btsSubnetMask;
    }

    public void setBtsSubnetMask(String btsSubnetMask) {
        this.btsSubnetMask = btsSubnetMask;
    }

    public Timestamp getTclAggrementStartDate() {
        return tclAggrementStartDate;
    }

    public void setTclAggrementStartDate(Timestamp tclAggrementStartDate) {
        this.tclAggrementStartDate = tclAggrementStartDate;
    }

    public String getWirelessTechSubType() {
        return wirelessTechSubType;
    }

    public void setWirelessTechSubType(String wirelessTechSubType) {
        this.wirelessTechSubType = wirelessTechSubType;
    }

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }
}
