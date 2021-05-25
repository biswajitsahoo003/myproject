package com.tcl.dias.oms.beans;

import java.sql.Timestamp;
import java.util.Date;

public class ContractTermBean {

    private String serviceId;

    private Double contractTerm;

    private Timestamp commissionedDate;

    private Date contractStartDate;

    private Date contractEndDate;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Double getContractTerm() {
        return contractTerm;
    }

    public void setContractTerm(Double contractTerm) {
        this.contractTerm = contractTerm;
    }

    public Timestamp getCommissionedDate() {
        return commissionedDate;
    }

    public void setCommissionedDate(Timestamp commissionedDate) {
        this.commissionedDate = commissionedDate;
    }

    public Date getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(Date contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }
}
