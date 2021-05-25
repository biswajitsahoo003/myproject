package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 *
 * This file contains the SIComponent.java class for creating records in Service Inventory
 *
 *
 * @author Mayank S
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@Entity
@Table(name="si_component")
@NamedQuery(name="SIComponent.findAll", query="SELECT s FROM SIComponent s")
public class SIComponent implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="component_name")
    private String componentName;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="created_date")
    private Timestamp createdDate;

    @Column(name="is_active")
    private String isActive;

    @Column(name = "site_type")
    private String siteType;

    @Column(name="si_service_detail_id")
    private Integer siServiceDetailId;

    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="updated_date")
    private Timestamp updatedDate;

    private String uuid;

    //bi-directional many-to-one association to MstComponentType
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="component_type_id")
    private MstComponentType mstComponentType;

    //bi-directional many-to-one association to SIComponentAttribute
    @OneToMany(mappedBy="siComponent", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<SIComponentAttribute> SIComponentAttributes;

    public SIComponent() {
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
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

    public MstComponentType getMstComponentType() {
        return this.mstComponentType;
    }

    public void setMstComponentType(MstComponentType mstComponentType) {
        this.mstComponentType = mstComponentType;
    }


    public Set<SIComponentAttribute> getSIComponentAttributes() {
        return this.SIComponentAttributes;
    }

    public void setSIComponentAttributes(Set<SIComponentAttribute> SIComponentAttributes) {
        this.SIComponentAttributes = SIComponentAttributes;
    }

    public String getSiteType() {
        return siteType;
    }

    public void setSiteType(String siteType) {
        this.siteType = siteType;
    }

    public SIComponentAttribute addSIComponentAttribute(SIComponentAttribute SIComponentAttribute) {
        getSIComponentAttributes().add(SIComponentAttribute);
        SIComponentAttribute.setSiComponent(this);

        return SIComponentAttribute;
    }

    public SIComponentAttribute removeSIComponentAttribute(SIComponentAttribute SIComponentAttribute) {
        getSIComponentAttributes().remove(SIComponentAttribute);
        SIComponentAttribute.setSiComponent(null);

        return SIComponentAttribute;
    }

}
