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
 * This file contains the OrderUcaasDetail.java class.
 *
 *
 * @author Syed ALi
 * @link http://www.tatacommunications.com/
 *
 */
@Entity
@Table(name = "order_ucaas_details")
@NamedQuery(name = "OrderUcaasDetail.findAll", query = "SELECT q FROM OrderUcaasDetail q")
public class OrderUcaasDetail implements Serializable {
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
    @JoinColumn(name = "order_ucaas_id")
    private OrderUcaas orderUcaas;

    public OrderUcaasDetail() {
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

    public OrderUcaas getOrderUcaas() {
        return orderUcaas;
    }

    public void setOrderUcaas(OrderUcaas orderUcaas) {
        this.orderUcaas = orderUcaas;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

}