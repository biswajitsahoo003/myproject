package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;


/**
 *
 * This file contains the SIComponentAttribute.java class for creating records in Service Inventory
 *
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="si_component_attributes")
@NamedQuery(name="SIComponentAttribute.findAll", query="SELECT s FROM SIComponentAttribute s")
public class SIComponentAttribute implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="attribute_alt_value_label")
    private String attributeAltValueLabel;

    @Column(name="attribute_name")
    private String attributeName;

    @Column(name="attribute_value")
    private String attributeValue;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="created_date")
    private Timestamp createdDate;

    @Column(name="is_active")
    private String isActive;

    @Column(name="si_service_detail_id")
    private Integer siServiceDetailId;

    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="updated_date")
    private Timestamp updatedDate;

    private String uuid;

    //bi-directional many-to-one association to SiComponent
    @ManyToOne
    @JoinColumn(name="si_component_id")
    private SIComponent siComponent;

    public SIComponentAttribute() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAttributeAltValueLabel() {
        return this.attributeAltValueLabel;
    }

    public void setAttributeAltValueLabel(String attributeAltValueLabel) {
        this.attributeAltValueLabel = attributeAltValueLabel;
    }

    public String getAttributeName() {
        return this.attributeName;
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeValue() {
        return this.attributeValue;
    }

    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedDate() {
        return this.createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    public String getIsActive() {
        return this.isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public Integer getSiServiceDetailId() {
        return this.siServiceDetailId;
    }

    public void setSiServiceDetailId(Integer siServiceDetailId) {
        this.siServiceDetailId = siServiceDetailId;
    }

    public String getUpdatedBy() {
        return this.updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Timestamp getUpdatedDate() {
        return this.updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public SIComponent getSiComponent() {
        return this.siComponent;
    }

    public void setSiComponent(SIComponent siComponent) {
        this.siComponent = siComponent;
    }

}
