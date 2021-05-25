package com.tcl.dias.oms.entity.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 *
 * This file contains the OrderIzosdwanVutmLocationDetail.java class.
 *
 *
 * @author Anway Bhutkar
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="order_izosdwan_vutm_location_details")
@NamedQuery(name="OrderIzosdwanVutmLocationDetail.findAll", query="SELECT o FROM OrderIzosdwanVutmLocationDetail o")
public class OrderIzosdwanVutmLocationDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="breakup_location")
    private String breakupLocation;

    @Column(name="default_bw")
    private String defaultBw;

    @Column(name="is_active")
    private Integer isActive;

    @Column(name="location_id")
    private Integer locationId;

    @Column(name="max_bw")
    private String maxBw;

    @Column(name="reference_id")
    private Integer referenceId;

    @Column(name="selected_bw")
    private String selectedBw;

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBreakupLocation() {
        return this.breakupLocation;
    }

    public void setBreakupLocation(String breakupLocation) {
        this.breakupLocation = breakupLocation;
    }

    public String getDefaultBw() {
        return this.defaultBw;
    }

    public void setDefaultBw(String defaultBw) {
        this.defaultBw = defaultBw;
    }

    public Integer getIsActive() {
        return this.isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public Integer getLocationId() {
        return this.locationId;
    }

    public void setLocationId(Integer locationId) {
        this.locationId = locationId;
    }

    public String getMaxBw() {
        return this.maxBw;
    }

    public void setMaxBw(String maxBw) {
        this.maxBw = maxBw;
    }

    public Integer getReferenceId() {
        return this.referenceId;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public String getSelectedBw() {
        return this.selectedBw;
    }

    public void setSelectedBw(String selectedBw) {
        this.selectedBw = selectedBw;
    }
}
