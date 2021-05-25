package com.tcl.dias.customer.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tcl.dias.common.beans.AddressDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * This file Contains customerContracting Entity information
 *
 *
 * @author Thamizhselvi Perumal
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerContractingAddressResponseDto {

    private Integer legalEntityId;
    private List<CustomerContractingAddressInfo> customerContractingAddressInfoList=new ArrayList<>();

    public Integer getLegalEntityId() {
        return legalEntityId;
    }

    public void setLegalEntityId(Integer legalEntityId) {
        this.legalEntityId = legalEntityId;
    }

    public List<CustomerContractingAddressInfo> getCustomerContractingAddressInfoList() {
        return customerContractingAddressInfoList;
    }

    public void setCustomerContractingAddressInfoList(List<CustomerContractingAddressInfo> customerContractingAddressInfoList) {
        this.customerContractingAddressInfoList = customerContractingAddressInfoList;
    }

    @Override
    public String toString() {
        return "CustomerContractingAddressResponseDto{" +
                "legalEntityId=" + legalEntityId +
                ", customerContractingAddressInfoList=" + customerContractingAddressInfoList +
                '}';
    }
}
