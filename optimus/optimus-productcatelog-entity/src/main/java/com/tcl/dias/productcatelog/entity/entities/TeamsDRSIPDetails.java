package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_mediagateway_AC_SIP database table.
 *
 * @author Syed Ali
 *
 */
@Entity
@Table(name = "vw_mstmdr_mediagateway_AC_SIP")
@NamedQuery(name = "TeamsDRSIPDetails.findAll", query = "SELECT v FROM TeamsDRSIPDetails v")
public class TeamsDRSIPDetails {
    private static final long serialVersionUID = 1L;

    @Column(name="AC_DC_power_adapter_Partcode")
    private String acDcPowerAdapterPartcode;

    @Column(name="is_redundant")
    private String isRedundant;

    @Id
    @Column(name="Max_session_value")
    private Integer maxSessionValue;

    @Column(name="Media_Gateway_nm")
    private String mediaGatewayNm;

    @Column(name="Model_Number")
    private String modelNumber;

    @Column(name="OVOC_License_Partcode")
    private String OVOCLicensePartcode;

    @Column(name="OVOC_License_qty_per_MG")
    private Integer OVOCLicenseQtyPerMG;

    @Column(name="Partcode")
    private String partcode;

    @Column(name="SBC_License_Partcode")
    private String sbcLicensePartcode;

    @Column(name="SBC_License_qty")
    private Integer sbcLicenseQty;

    @Column(name="SBC_License_session")
    private Integer sbcLicenseSession;

    @Column(name="vendor_name")
    private String vendorName;

    public TeamsDRSIPDetails() {
    }

    public String getAcDcPowerAdapterPartcode() {
        return this.acDcPowerAdapterPartcode;
    }

    public void setAcDcPowerAdapterPartcode(String acDcPowerAdapterPartcode) {
        this.acDcPowerAdapterPartcode = acDcPowerAdapterPartcode;
    }

    public String getIsRedundant() {
        return this.isRedundant;
    }

    public void setIsRedundant(String isRedundant) {
        this.isRedundant = isRedundant;
    }

    public Integer getMaxSessionValue() {
        return this.maxSessionValue;
    }

    public void setMaxSessionValue(Integer maxSessionValue) {
        this.maxSessionValue = maxSessionValue;
    }

    public String getMediaGatewayNm() {
        return this.mediaGatewayNm;
    }

    public void setMediaGatewayNm(String mediaGatewayNm) {
        this.mediaGatewayNm = mediaGatewayNm;
    }

    public String getModelNumber() {
        return this.modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getOVOCLicensePartcode() {
        return this.OVOCLicensePartcode;
    }

    public void setOVOCLicensePartcode(String OVOCLicensePartcode) {
        this.OVOCLicensePartcode = OVOCLicensePartcode;
    }

    public Integer getOVOCLicenseQtyPerMG() {
        return this.OVOCLicenseQtyPerMG;
    }

    public void setOVOCLicenseQtyPerMG(Integer OVOCLicenseQtyPerMG) {
        this.OVOCLicenseQtyPerMG = OVOCLicenseQtyPerMG;
    }

    public String getPartcode() {
        return this.partcode;
    }

    public void setPartcode(String partcode) {
        this.partcode = partcode;
    }

    public String getSbcLicensePartcode() {
        return this.sbcLicensePartcode;
    }

    public void setSbcLicensePartcode(String sbcLicensePartcode) {
        this.sbcLicensePartcode = sbcLicensePartcode;
    }

    public Integer getSbcLicenseQty() {
        return this.sbcLicenseQty;
    }

    public void setSbcLicenseQty(Integer sbcLicenseQty) {
        this.sbcLicenseQty = sbcLicenseQty;
    }

    public Integer getSbcLicenseSession() {
        return this.sbcLicenseSession;
    }

    public void setSbcLicenseSession(Integer sbcLicenseSession) {
        this.sbcLicenseSession = sbcLicenseSession;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
