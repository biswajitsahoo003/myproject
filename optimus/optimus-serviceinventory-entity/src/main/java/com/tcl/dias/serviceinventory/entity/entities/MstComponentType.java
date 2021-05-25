package com.tcl.dias.serviceinventory.entity.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


/**
 * The persistent class for the mst_component_type database table.
 *
 */
@Entity
@Table(name="mst_component_type")
@NamedQuery(name="MstComponentType.findAll", query="SELECT m FROM MstComponentType m")
public class MstComponentType implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="component_type_name")
    private String componentTypeName;

    @Column(name="created_by")
    private String createdBy;

    @Column(name="created_date")
    private Timestamp createdDate;

    @Column(name="is_active")
    private String isActive;

    @Column(name="updated_by")
    private String updatedBy;

    @Column(name="updated_date")
    private Timestamp updatedDate;

    private String uuid;

    //bi-directional many-to-one association to SiComponent
    @OneToMany(mappedBy="mstComponentType")
    private Set<SIComponent> siComponents;

    public MstComponentType() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComponentTypeName() {
        return this.componentTypeName;
    }

    public void setComponentTypeName(String componentTypeName) {
        this.componentTypeName = componentTypeName;
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

    public Set<SIComponent> getSiComponents() {
        return this.siComponents;
    }

    public void setSiComponents(Set<SIComponent> siComponents) {
        this.siComponents = siComponents;
    }

    public SIComponent addSiComponent(SIComponent siComponent) {
        getSiComponents().add(siComponent);
        siComponent.setMstComponentType(this);

        return siComponent;
    }

    public SIComponent removeSiComponent(SIComponent siComponent) {
        getSiComponents().remove(siComponent);
        siComponent.setMstComponentType(null);
        return siComponent;
    }

}