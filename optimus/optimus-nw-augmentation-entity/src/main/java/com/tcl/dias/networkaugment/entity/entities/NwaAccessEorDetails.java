package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
@Entity
@Table(name = "nwa_access_eor_details")
public class NwaAccessEorDetails //implements Serializable
{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name="a_end_eor_id")
    private String  aEndEorID;

    @Column(name="a_end_neighbour_hostname")
    private String  aEndNeighbourHostName;

    @Column(name="a_end_neighbour_port_details")
    private String  aEndNeighbourPortDetails;

    @Column(name="b_end_eor_id")
    private String  bEndEorId ;

    @Column(name="b_end_neighbour_port_details")
    private String  bEndNeighbourPortDetails ;

    @Column(name="cfm_mip_level")
    private String  cfmMipLevel ;

    @Column(name="city_name")
    private String  cityName ;

    @Column(name="cmf_md_level")
    private String  cmfMdLevel ;

    @Column(name="configure_a_end_uplink_port")
    private String  configureAEndUplinkPort;

    @Column(name="control_vlan_id")
    private String  controlVlanId ;

    @Column(name="desc_a_end_neighbour")
    private String  descAEndNeighbour ;

    @Column(name="desc_new_device_a_end_uplink_port")
    private String  descNewDeviceAEndUplinkPort ;

    @Column(name="erps_ring_id")
    private String  erpsRingId ;

    @Column(name="gateway_ip_address_ntp")
    private String  gateWayIpAddressNtp ;

    @Column(name="host_name")
    private String  hostName ;

    @Column(name="init_template")
    private String  initTemplate;

    @Column(name="management_vlan_description")
    private String  managementVlanDescription ;

    @Column(name="protected_instance_id")
    private String  protectedInstanceId;

    @Column(name="switch_location")
    private String  switchLocation ;

    @Column(name="subring")
    private String  subring;

    @Column(name="uplink_a_port")
    private String  uplinkAPort;

    @Column(name="upload_node_flag")
    private String  uploadNodeFlag;

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

    public String getaEndEorID() {
        return aEndEorID;
    }

    public void setaEndEorID(String aEndEorID) {
        this.aEndEorID = aEndEorID;
    }

    public String getaEndNeighbourHostName() {
        return aEndNeighbourHostName;
    }

    public void setaEndNeighbourHostName(String aEndNeighbourHostName) {
        this.aEndNeighbourHostName = aEndNeighbourHostName;
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

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }

    @Override
    public String toString() {
        return "NwaAccessEorDetails{" +
                "id=" + id +
                //", orderId=" + orderId +
                ", orderCode='" + orderCode + '\'' +
                ", aEndEorID='" + aEndEorID + '\'' +
                ", aEndNeighbourHostName='" + aEndNeighbourHostName + '\'' +
                ", aEndNeighbourPortDetails='" + aEndNeighbourPortDetails + '\'' +
                ", bEndEorId='" + bEndEorId + '\'' +
                ", bEndNeighbourPortDetails='" + bEndNeighbourPortDetails + '\'' +
                ", cfmMipLevel='" + cfmMipLevel + '\'' +
                ", cityName='" + cityName + '\'' +
                ", cmfMdLevel='" + cmfMdLevel + '\'' +
                ", configureAEndUplinkPort='" + configureAEndUplinkPort + '\'' +
                ", controlVlanId='" + controlVlanId + '\'' +
                ", descAEndNeighbour='" + descAEndNeighbour + '\'' +
                ", descNewDeviceAEndUplinkPort='" + descNewDeviceAEndUplinkPort + '\'' +
                ", erpsRingId='" + erpsRingId + '\'' +
                ", gateWayIpAddressNtp='" + gateWayIpAddressNtp + '\'' +
                ", hostName='" + hostName + '\'' +
                ", initTemplate='" + initTemplate + '\'' +
                ", managementVlanDescription='" + managementVlanDescription + '\'' +
                ", protectedInstanceId='" + protectedInstanceId + '\'' +
                ", switchLocation='" + switchLocation + '\'' +
                ", subring='" + subring + '\'' +
                ", uplinkAPort='" + uplinkAPort + '\'' +
                ", uploadNodeFlag='" + uploadNodeFlag + '\'' +
                ", scOrder=" + scOrder +
                '}';
    }
}
