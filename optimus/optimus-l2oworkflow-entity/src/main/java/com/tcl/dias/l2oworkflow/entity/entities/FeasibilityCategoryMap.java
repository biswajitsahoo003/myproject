package com.tcl.dias.l2oworkflow.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "feasibility_category_map")
@NamedQuery(name = "FeasibilityCategoryMap.findAll", query = "SELECT m FROM FeasibilityCategoryMap m")
public class FeasibilityCategoryMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "f_mode")
    private String fMode;

    @Column(name = "f_status")
    private String fStatus;

    @Column(name = "f_category")
    private String fCategory;

    @Column(name = "is_active")
    private String isActive;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getfMode() {
        return fMode;
    }

    public void setfMode(String fMode) {
        this.fMode = fMode;
    }

    public String getfStatus() {
        return fStatus;
    }

    public void setfStatus(String fStatus) {
        this.fStatus = fStatus;
    }

    public String getfCategory() {
        return fCategory;
    }

    public void setfCategory(String fCategory) {
        this.fCategory = fCategory;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
