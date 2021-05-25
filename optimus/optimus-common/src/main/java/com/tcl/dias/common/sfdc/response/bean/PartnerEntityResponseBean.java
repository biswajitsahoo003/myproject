package com.tcl.dias.common.sfdc.response.bean;

/**
 *
 * Response Bean for Partner Entity Creation
 *
 * @author Biswajit
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class PartnerEntityResponseBean {
    private String status;
    private String message;
    private String customerLegalEntityCode;
    private String referenceId;

    public String getCustomerLegalEntityCode() {
        return customerLegalEntityCode;
    }

    public void setCustomerLegalEntityCode(String customerLegalEntityCode) {
        this.customerLegalEntityCode = customerLegalEntityCode;
    }
    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReferenceId() {
        return referenceId;
    }
    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

}
