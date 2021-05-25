package com.tcl.dias.customer.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Customer Legal Entity Company Code
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "customer_legal_entity_company_code")
@NamedQuery(name = "CustomerLegalEntityCompanyCode.findAll", query = "SELECT c FROM CustomerLegalEntityCompanyCode c")
public class CustomerLegalEntityCompanyCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "code_value")
    private String codeValue;

    @Column(name = "is_active")
    private String isActive;

    @Column(name = "supplier_secs_gbs")
    private String supplierSecsGbs;

    @Column(name = "supplier_name_gbs")
    private String supplierNameGbs;

    // bi-directional many-to-one association to LegalEntitySapCode
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_le_sap_code_id")
    private LegalEntitySapCode legalEntitySapCode;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodeValue() {
        return codeValue;
    }

    public void setCodeValue(String codeValue) {
        this.codeValue = codeValue;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }

    public String getSupplierSecsGbs() {
        return supplierSecsGbs;
    }

    public void setSupplierSecsGbs(String supplierSecsGbs) {
        this.supplierSecsGbs = supplierSecsGbs;
    }

    public String getSupplierNameGbs() {
        return supplierNameGbs;
    }

    public void setSupplierNameGbs(String supplierNameGbs) {
        this.supplierNameGbs = supplierNameGbs;
    }

    public LegalEntitySapCode getLegalEntitySapCode() {
        return legalEntitySapCode;
    }

    public void setLegalEntitySapCode(LegalEntitySapCode legalEntitySapCode) {
        this.legalEntitySapCode = legalEntitySapCode;
    }
}
