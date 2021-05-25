package com.tcl.dias.customer.bean;

import com.tcl.dias.customer.entity.entities.CustomerLegalEntity;

/**
 * Customer Legal Entity Bean
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class CustomerLegalEntityBean {

    Integer id;

    String entityName;

    Integer customerId;

    String tpsSfdcCuid;

    String agreementId;

    public CustomerLegalEntityBean() {
    }

    public static CustomerLegalEntityBean fromEntity(CustomerLegalEntity entity) {
        CustomerLegalEntityBean customerLegalEntityBean = new CustomerLegalEntityBean();
        customerLegalEntityBean.setId(entity.getId());
        customerLegalEntityBean.setEntityName(entity.getEntityName());
        customerLegalEntityBean.setCustomerId(entity.getCustomer().getId());
        customerLegalEntityBean.setTpsSfdcCuid(entity.getTpsSfdcCuid());
        customerLegalEntityBean.setAgreementId(entity.getAgreementId());
        return customerLegalEntityBean;
    }

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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
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
}
