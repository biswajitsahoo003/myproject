package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "nwa_material_request_details")
public class NwaMaterialRequestDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "child_order_status")
    private String ChildOrderStatus;

    @Column(name = "child_orders")
    private String childOrders;

    @Column(name = "linked_pe")
    private String linkedPe;

    @Column(name = "parent_orders")
    private String parentOrders;

    @Column(name = "pe_date")
    private Timestamp peDate;

    @Column(name = "service_affecting")
    private String serviceAffecting;

    @Column(name = "sibling_orders")
    private String siblingOrders;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private	ScOrder scOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChildOrderStatus() {
        return ChildOrderStatus;
    }

    public void setChildOrderStatus(String childOrderStatus) {
        ChildOrderStatus = childOrderStatus;
    }

    public String getChildOrders() {
        return childOrders;
    }

    public void setChildOrders(String childOrders) {
        this.childOrders = childOrders;
    }

    public String getLinkedPe() {
        return linkedPe;
    }

    public void setLinkedPe(String linkedPe) {
        this.linkedPe = linkedPe;
    }

    public String getParentOrders() {
        return parentOrders;
    }

    public void setParentOrders(String parentOrders) {
        this.parentOrders = parentOrders;
    }

    public Timestamp getPeDate() {
        return peDate;
    }

    public void setPeDate(Timestamp peDate) {
        this.peDate = peDate;
    }

    public String getServiceAffecting() {
        return serviceAffecting;
    }

    public void setServiceAffecting(String serviceAffecting) {
        this.serviceAffecting = serviceAffecting;
    }

    public String getSiblingOrders() {
        return siblingOrders;
    }

    public void setSiblingOrders(String siblingOrders) {
        this.siblingOrders = siblingOrders;
    }

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
