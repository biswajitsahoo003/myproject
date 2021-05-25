package com.tcl.dias.customer.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * Entity class for Supplier Legal Entity Company Code
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "service_provider_legal_entity_company_code")
@NamedQuery(name = "SupplierLegalEntityCompanyCode.findAll", query = "SELECT p FROM SupplierLegalEntityCompanyCode p")
public class SupplierLegalEntityCompanyCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "sp_le_id")
    private Integer supplierLeId;

    @Column(name = "code_value")
    private String codeValue;

    @Column(name = "code_type")
    private String codeType;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "secs_sap_flag")
    private String secsSapFlag;

    @Column(name = "sp_le_type")
    private String supplierLeType;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_dt")
    private Timestamp updatedDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "companycode_gbs")
    private String companyCodeGBS;

    @Column(name = "companyname_gbs")
    private String companyNameGBS;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSupplierLeId() {
        return supplierLeId;
    }

    public void setSupplierLeId(Integer supplierLeId) {
        this.supplierLeId = supplierLeId;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getCodeType() {
        return codeType;
    }

    public void setCodeType(String codeType) {
        this.codeType = codeType;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSecsSapFlag() {
        return secsSapFlag;
    }

    public void setSecsSapFlag(String secsSapFlag) {
        this.secsSapFlag = secsSapFlag;
    }

    public String getSupplierLeType() {
        return supplierLeType;
    }

    public void setSupplierLeType(String supplierLeType) {
        this.supplierLeType = supplierLeType;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Timestamp updatedDate) {
        this.updatedDate = updatedDate;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getCompanyCodeGBS() {
        return companyCodeGBS;
    }

    public void setCompanyCodeGBS(String companyCodeGBS) {
        this.companyCodeGBS = companyCodeGBS;
    }

    public String getCompanyNameGBS() {
        return companyNameGBS;
    }

    public void setCompanyNameGBS(String companyNameGBS) {
        this.companyNameGBS = companyNameGBS;
    }
}
