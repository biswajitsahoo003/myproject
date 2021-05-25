package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The persistent class for the vw_service_area_matrix_GSC_ACANS table
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Immutable
@Table(name = "vw_service_area_matrix_GSC_ACANS")
@NamedQuery(name = "ServiceAreaMatrixGSCACANSView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCACANSView v")
public class ServiceAreaMatrixGSCACANSView implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer uid;

    @Column(name = "Pdt_Name")
    private String productName;

    @Column(name = "Iso_3_Ctry_Cd")
    private String iso3CountryCode;

    @Column(name = "Iso_Ctry_Name")
    private String isoCountryName;

    @Column(name = "City_Code")
    private String cityCode;

    @Column(name = "City_Name")
    private String cityName;

    @Column(name = "estimated_standard_lead_time_days")
    private String estimatedStandardLeadTimeDays;

    @Column(name = "Interntional_Country_Dial_Codes")
    private String internationalCountryDialCodes;

    /**
     * @return the productName
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @param productName the productName to set
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * @return the iso3CountryCode
     */
    public String getIso3CountryCode() {
        return iso3CountryCode;
    }

    /**
     * @param iso3CountryCode the iso3CountryCode to set
     */
    public void setIso3CountryCode(String iso3CountryCode) {
        this.iso3CountryCode = iso3CountryCode;
    }

    /**
     * @return the isoCountryName
     */
    public String getIsoCountryName() {
        return isoCountryName;
    }

    /**
     * @param isoCountryName the isoCountryName to set
     */
    public void setIsoCountryName(String isoCountryName) {
        this.isoCountryName = isoCountryName;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getEstimatedStandardLeadTimeDays() {
        return estimatedStandardLeadTimeDays;
    }

    public void setEstimatedStandardLeadTimeDays(String estimatedStandardLeadTimeDays) {
        this.estimatedStandardLeadTimeDays = estimatedStandardLeadTimeDays;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getInternationalCountryDialCodes() {
        return internationalCountryDialCodes;
    }

    public void setInternationalCountryDialCodes(String internationalCountryDialCodes) {
        this.internationalCountryDialCodes = internationalCountryDialCodes;
    }
}
