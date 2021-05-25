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
 * Partner Legal Entity Company Code
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@Entity
@Table(name = "partner_legal_entity_company_code")
@NamedQuery(name = "PartnerLegalEntityCompanyCode.findAll", query = "SELECT c FROM PartnerLegalEntityCompanyCode c")
public class PartnerLegalEntityCompanyCode {

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

    @Column(name = "supplier_type_gbs")
    private String supplierTypeGbs;

    // bi-directional many-to-one association to LegalEntitySapCode
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_le_sap_code_id")
    private PartnerLegalEntitySapCode partnerLegalEntitySapCode;

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

    public String getSupplierTypeGbs() {
        return supplierTypeGbs;
    }

    public void setSupplierTypeGbs(String supplierTypeGbs) {
        this.supplierTypeGbs = supplierTypeGbs;
    }

    public PartnerLegalEntitySapCode getPartnerLegalEntitySapCode() {
        return partnerLegalEntitySapCode;
    }

    public void setPartnerLegalEntitySapCode(PartnerLegalEntitySapCode partnerLegalEntitySapCode) {
        this.partnerLegalEntitySapCode = partnerLegalEntitySapCode;
    }
}
