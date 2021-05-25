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

/*
 * *
 *  * @author Syed Ali
 *  * @link http://www.tatacommunications.com/
 *  * @copyright 2020 Tata Communications Limited
 *
 */

@Entity
@Table(name = "order_ucaas_site_details")
@NamedQuery(name = "OrderUcaasSiteDetails.findAll", query = "SELECT q FROM OrderUcaasSiteDetails q")
public class OrderUcaasSiteDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(name = "endpoint_location_id")
    private Integer endpointLocationId;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_solution_id")
    private OrderProductSolution orderProductSolution;

    @Column(name = "site_code")
    private String siteCode;

    public OrderUcaasSiteDetails() {
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

    public OrderProductSolution getOrderProductSolution() {
        return orderProductSolution;
    }

    public void setOrderProductSolution(OrderProductSolution orderProductSolution) {
        this.orderProductSolution = orderProductSolution;
    }

    public Integer getEndpointLocationId() {
        return endpointLocationId;
    }

    public void setEndpointLocationId(Integer endpointLocationId) {
        this.endpointLocationId = endpointLocationId;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
    }
}