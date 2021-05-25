package com.tcl.dias.servicefulfillmentutils.beans;

/**
 *
 * This file contains the ScCharelineitemBean.java class.
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
public class ScChargeLineItemBean {
    private Integer id;
    private String accountNumber;
    private String chargeLineitem;
    private String serviceId;
    private String serviceCode;
    private String commissioningFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getChargeLineitem() {
        return chargeLineitem;
    }

    public void setChargeLineitem(String chargeLineitem) {
        this.chargeLineitem = chargeLineitem;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getCommissioningFlag() {
        return commissioningFlag;
    }

    public void setCommissioningFlag(String commissioningFlag) {
        this.commissioningFlag = commissioningFlag;
    }
}
