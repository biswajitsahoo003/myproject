package com.tcl.dias.oms.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
/**
 *
 * Request DTO / Bean for Partner Entity
 *
 * @author ArunMani
 * @link http://www.tatacommunications.com/
 * @copyright 2020 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountUpdationRequest {

    private String customerSFDCId;
    private String fySegmentation;
    private String accountOwner;
    private String accountRTM;

    public String getCustomerSFDCId() {
        return customerSFDCId;
    }

    public void setCustomerSFDCId(String customerSFDCId) {
        this.customerSFDCId = customerSFDCId;
    }

    public String getFySegmentation() {
        return fySegmentation;
    }

    public void setFySegmentation(String fySegmentation) {
        this.fySegmentation = fySegmentation;
    }

    public String getAccountOwner() {
        return accountOwner;
    }

    public void setAccountOwner(String accountOwner) {
        this.accountOwner = accountOwner;
    }


    public String getAccountRTM() {
        return accountRTM;
    }

    public void setAccountRTM(String accountRTM) {
        this.accountRTM = accountRTM;
    }


}
