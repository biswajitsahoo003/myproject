package com.tcl.dias.productcatelog.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The persistent class for the vw_mstmdr_mediagateway_AC_PRI_Calculations database table.
 *
 * @author Syed Ali
 */
@Entity
@Table(name = "vw_mstmdr_mediagateway_AC_PRI_Calculations")
@NamedQuery(name = "TeamsDRPRICalculations.findAll", query = "SELECT v FROM TeamsDRPRICalculations v")
public class TeamsDRPRICalculations {
    private static final long serialVersionUID = 1L;

    @Column(name = "MediaGateway_with_Redundancy")
    private String mediaGatewayWithRedundancy;

    @Column(name = "MediaGateway_without_Redundancy")
    private String mediaGatewayWithoutRedundancy;

    @Id
    @Column(name = "PRI_Value")
    private int priValue;

    @Column(name="vendor_name")
    private String vendorName;

    public TeamsDRPRICalculations() {
    }

    public String getMediaGatewayWithRedundancy() {
        return this.mediaGatewayWithRedundancy;
    }

    public void setMediaGatewayWithRedundancy(String mediaGatewayWithRedundancy) {
        this.mediaGatewayWithRedundancy = mediaGatewayWithRedundancy;
    }

    public String getMediaGatewayWithoutRedundancy() {
        return this.mediaGatewayWithoutRedundancy;
    }

    public void setMediaGatewayWithoutRedundancy(String mediaGatewayWithoutRedundancy) {
        this.mediaGatewayWithoutRedundancy = mediaGatewayWithoutRedundancy;
    }

    public int getPriValue() {
        return this.priValue;
    }

    public void setPriValue(int priValue) {
        this.priValue = priValue;
    }

    public String getVendorName() {
        return this.vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }
}
