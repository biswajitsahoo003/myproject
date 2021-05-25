package com.tcl.dias.customer.bean;

import com.tcl.dias.customer.entity.entities.SAPData;

/**
 * Bean including Partner SAP Commisions
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2018 Tata Communications Limited
 */
public class SapCommissions {

    private int id;
    private String partnerName;
    private String partnerId;
    private String year;
    private String month;
    private String currency;
    private Double commissionsPaid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getCommissionsPaid() {
        return commissionsPaid;
    }

    public void setCommissionsPaid(Double commissionsPaid) {
        this.commissionsPaid = commissionsPaid;
    }

    public static SapCommissions fromSAPData(SAPData sapData) {
        SapCommissions sapCommissions = new SapCommissions();
        sapCommissions.setId(sapData.getId());
        sapCommissions.setPartnerName(sapData.getPartnerName());
        sapCommissions.setPartnerId(sapData.getPartnerId());
        sapCommissions.setCommissionsPaid(sapData.getCommisionsPaid());
        sapCommissions.setMonth(sapData.getMonth());
        sapCommissions.setYear(sapData.getYear());
        sapCommissions.setCurrency(sapData.getCurrency());
       //sapCommissions.setCommissionType(sapData.getCommisionsType());
        return sapCommissions;
    }
}
