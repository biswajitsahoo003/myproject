package com.tcl.dias.common.sfdc.response.bean;

/**
 *
 * Request Bean for Partner Entity Contact Response
 *
 * @author Anusha Unni
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
public class PartnerEntityContactResponseBean {
    private String status;
    private String message;
    private String customerContactId;
    private String referenceId;

    public String getCustomerContactId() {
        return customerContactId;
    }

    public void setCustomerContactId(String customerContactId) {
        this.customerContactId = customerContactId;
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
