package com.tcl.dias.location.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Entity Class
 *
 * @author Gnana prakash
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "mst_location_data")
@NamedQuery(name = "MstLocationData.findAll", query = "SELECT m FROM MstLocationData m")
public class MstLocationData implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "state_name")
    private String stateName;

    @Column(name = "country_name")
    private String countryName;

    @Column(name = "continent_name")
    private String continentName;

    @Column(name = "opt_mst_city_id")
    private Integer optMstCityId;

    @Column(name = "opt_mst_state_id")
    private Integer optMstStateId;

    @Column(name = "opt_mst_country_id")
    private Integer optMstCountryId;

    @Column(name = "region_name")
    private String regionName;

    @Column(name = "source_system")
    private String sourceSystem;

    @Column(name = "data_input")
    private String dataInput;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    private String createdTime;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_time")
    private String updatedTime;

    @Column(name = "is_active")
    private Byte isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    public Integer getOptMstCityId() {
        return optMstCityId;
    }

    public void setOptMstCityId(Integer optMstCityId) {
        this.optMstCityId = optMstCityId;
    }

    public Integer getOptMstStateId() {
        return optMstStateId;
    }

    public void setOptMstStateId(Integer optMstStateId) {
        this.optMstStateId = optMstStateId;
    }

    public Integer getOptMstCountryId() {
        return optMstCountryId;
    }

    public void setOptMstCountryId(Integer optMstCountryId) {
        this.optMstCountryId = optMstCountryId;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getDataInput() {
        return dataInput;
    }

    public void setDataInput(String dataInput) {
        this.dataInput = dataInput;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Byte getIsActive() {
        return isActive;
    }

    public void setIsActive(Byte isActive) {
        this.isActive = isActive;
    }
}
