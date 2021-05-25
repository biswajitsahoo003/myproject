package com.tcl.dias.oms.entity.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "order_amendment_status")
public class OrderAmendmentStatus implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "parent_order_code")
    private String parentOrderCode;

    @Column(name = "parent_service_id")
    private String parentServiceId;

    @Column(name = "parent_site_code")
    private String parentSiteCode;

    @Column(name = "order_created_date")
    private Date orderCreatedDate;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "order_amendment_date")
    private Date orderAmendmentDate;

    @Column(name = "modified_by")
    private String modifiedBy;

    @Column (name = "created_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTime;

    @Column(name = "o2c_cancel_trigger_status")
    private Byte o2cCancelTriggerStatus;

    @Column(name = "amendment_status")
    private Byte amendmentStatus;

    @Column(name = "product_family")
    private String productFamily;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentOrderCode() {
        return parentOrderCode;
    }

    public void setParentOrderCode(String parentOrderCode) {
        this.parentOrderCode = parentOrderCode;
    }

    public String getParentServiceId() {
        return parentServiceId;
    }

    public void setParentServiceId(String parentServiceId) {
        this.parentServiceId = parentServiceId;
    }

    public String getParentSiteCode() {
        return parentSiteCode;
    }

    public void setParentSiteCode(String parentSiteCode) {
        this.parentSiteCode = parentSiteCode;
    }

    public Date getOrderCreatedDate() {
        return orderCreatedDate;
    }

    public void setOrderCreatedDate(Date orderCreatedDate) {
        this.orderCreatedDate = orderCreatedDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Date getOrderAmendmentDate() {
        return orderAmendmentDate;
    }

    public void setOrderAmendmentDate(Date orderAmendmentDate) {
        this.orderAmendmentDate = orderAmendmentDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Byte getO2cCancelTriggerStatus() {
        return o2cCancelTriggerStatus;
    }

    public void setO2cCancelTriggerStatus(Byte o2cCancelTriggerStatus) {
        this.o2cCancelTriggerStatus = o2cCancelTriggerStatus;
    }

    public Byte getAmendmentStatus() {
        return amendmentStatus;
    }

    public void setAmendmentStatus(Byte amendmentStatus) {
        this.amendmentStatus = amendmentStatus;
    }

    public String getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }
}
