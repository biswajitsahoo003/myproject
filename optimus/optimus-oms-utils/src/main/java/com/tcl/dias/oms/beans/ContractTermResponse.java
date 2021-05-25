package com.tcl.dias.oms.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Method to get contract Term Info
 */
public class ContractTermResponse {

    List<ContractTermBean> contractTermBeans=new ArrayList<>();

    public List<ContractTermBean> getContractTermBeans() {
        return contractTermBeans;
    }

    public void setContractTermBeans(List<ContractTermBean> contractTermBeans) {
        this.contractTermBeans = contractTermBeans;
    }
}
