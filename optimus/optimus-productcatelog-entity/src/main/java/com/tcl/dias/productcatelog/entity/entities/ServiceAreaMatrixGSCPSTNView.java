package com.tcl.dias.productcatelog.entity.entities;

import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * The persistent class for the vw_service_area_pstn_destination_GSC database table.
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "vw_service_area_pstn_destination_GSC")
@Immutable
@NamedQuery(name = "ServiceAreaMatrixGSCPSTNView.findAll", query = "SELECT v FROM ServiceAreaMatrixGSCPSTNView v")
public class ServiceAreaMatrixGSCPSTNView implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;

    @Column(name = "country_cd")
    private String countryCode;

    @Column(name = "country_nm")
    private String countryName;

    @Column(name = "intl_dial_cd")
    private String internationalCountryDialCodes;

    @Column(name = "is_active_ind")
    private String isActive;

    @Column(name = "remarks")
    private String remarks;

    public ServiceAreaMatrixGSCPSTNView() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getInternationalCountryDialCodes() {
        return internationalCountryDialCodes;
    }

    public void setInternationalCountryDialCodes(String internationalCountryDialCodes) {
        this.internationalCountryDialCodes = internationalCountryDialCodes;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
