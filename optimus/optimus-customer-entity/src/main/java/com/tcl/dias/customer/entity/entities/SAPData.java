package com.tcl.dias.customer.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity Class for SAP Data
 *
 * @author AVALLAPI
 * @link http://www.tatacommunications.com/
 * @copyright 2019 Tata Communications Limited
 */
@Entity
@Table(name = "sap_data")
@NamedQuery(name = "SAPData.findAll", query = "SELECT s FROM SAPData s")
public class SAPData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",unique = true, nullable = false)
    private int id;

    @Column(name = "partner_Id")
    private String partnerId;

    @Column(name = "partner_name")
    private String partnerName;

    @Column(name = "month")
    private String month;

    @Column(name = "year")
    private String year;

    @Column(name = "commissions_paid")
    private Double commisionsPaid;

    @Column(name = "currency")
    private String currency;
//
//    @Column(name = "commission_type")
//    private String commisionsType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Double getCommisionsPaid() {
        return commisionsPaid;
    }

    public void setCommisionsPaid(Double commisionsPaid) {
        this.commisionsPaid = commisionsPaid;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
//
//    public String getCommisionsType() {
//        return commisionsType;
//    }
//
//    public void setCommisionsType(String commisionsType) {
//        this.commisionsType = commisionsType;
//    }
}
