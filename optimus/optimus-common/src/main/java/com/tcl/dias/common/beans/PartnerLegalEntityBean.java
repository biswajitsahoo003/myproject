package com.tcl.dias.common.beans;

import java.util.Objects;

/**
 * Partner Legal Entity Bean
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class PartnerLegalEntityBean {

    Integer id;

    String entityName;

    Integer partnerId;

    String tpsSfdcCuid;

    String agreementId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public String getTpsSfdcCuid() {
        return tpsSfdcCuid;
    }

    public void setTpsSfdcCuid(String tpsSfdcCuid) {
        this.tpsSfdcCuid = tpsSfdcCuid;
    }

    public String getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(String agreementId) {
        this.agreementId = agreementId;
    }

    @Override
    public String toString() {
        return "PartnerLegalEntityBean{" +
                "id=" + id +
                ", entityName='" + entityName + '\'' +
                ", partnerId=" + partnerId +
                ", tpsSfdcCuid='" + tpsSfdcCuid + '\'' +
                ", agreementId='" + agreementId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartnerLegalEntityBean that = (PartnerLegalEntityBean) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(entityName, that.entityName) &&
                Objects.equals(partnerId, that.partnerId) &&
                Objects.equals(tpsSfdcCuid, that.tpsSfdcCuid) &&
                Objects.equals(agreementId, that.agreementId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, partnerId, tpsSfdcCuid, agreementId);
    }
}
