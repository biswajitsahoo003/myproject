package com.tcl.dias.customer.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Bean including Partner NNI ID
 *
 * @author Sathishkumar Manogaran
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PartnerNNIBean {

    private Integer partnerId;
    private List<String> nniIDs = new ArrayList<>();

    public PartnerNNIBean() {
    }

    public PartnerNNIBean(Integer partnerId, List<String> nniIDs) {
        this.partnerId = partnerId;
        this.nniIDs = nniIDs;
    }

    public Integer getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(Integer partnerId) {
        this.partnerId = partnerId;
    }

    public List<String> getNniIDs() {
        return nniIDs;
    }

    public void setNniIDs(List<String> nniIDs) {
        this.nniIDs = nniIDs;
    }

    @Override
    public String toString() {
        return "PartnerNNIBean{" +
                "partnerId=" + partnerId +
                ", nniIDs=" + nniIDs +
                '}';
    }
}
