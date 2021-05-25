package com.tcl.dias.common.beans;

/**
 * @author VISHESH AWASTHI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class CustomerCodeBean {

    private String customerCode;
    private String customerLeCode;

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerLeCode() {
        return customerLeCode;
    }

    public void setCustomerLeCode(String customerLeCode) {
        this.customerLeCode = customerLeCode;
    }

    @Override
    public String toString() {
        return "CustomerCodeBean{" +
                "customerCode='" + customerCode + '\'' +
                ", customerLeCode='" + customerLeCode + '\'' +
                '}';
    }
}
