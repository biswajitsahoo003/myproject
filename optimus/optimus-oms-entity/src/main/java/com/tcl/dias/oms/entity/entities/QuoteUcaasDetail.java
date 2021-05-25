package com.tcl.dias.oms.entity.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * This file contains the QuoteUcaasDetail.java class.
 *
 *
 * @author Syed ALi
 * @link http://www.tatacommunications.com/
 *
 */
@Entity
@Table(name = "quote_ucaas_details")
@NamedQuery(name = "QuoteUcaasDetail.findAll", query = "SELECT q FROM QuoteUcaasDetail q")
public class QuoteUcaasDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "response")
    private String response;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    // bi-directional many-to-one association to QuoteGsc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quote_ucaas_id")
    private QuoteUcaas quoteUcaas;

    public QuoteUcaasDetail() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public QuoteUcaas getQuoteUcaas() {
        return quoteUcaas;
    }

    public void setQuoteUcaas(QuoteUcaas quoteUcaas) {
        this.quoteUcaas = quoteUcaas;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}