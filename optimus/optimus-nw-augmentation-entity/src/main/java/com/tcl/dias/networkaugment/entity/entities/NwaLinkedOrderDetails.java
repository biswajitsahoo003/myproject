package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;

@Entity
@Table(name = "nwa_linked_order_details")
public class NwaLinkedOrderDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_code")
    private String orderCode;

    @Column(name = "all_material_received")
    private String allMaterialReceived;

    @Column(name = "dispatch_details")
    private String dispatchDetails;

    @Column(name = "ml_group")
    private String mlGroup;

    @Column(name = "mrn_type")
    private String mrnType;

    @Column(name="m_and_l_required")
    private String mAndLRequired;

    @Column(name = "sibling_order")
    private String siblingOrder;

    @Column(name = "linked_pe")
    private String linkedPe;

    @Column(name = "linked_order_id")
    private  String linkedOrderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private	ScOrder scOrder;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAllMaterialReceived() {
        return allMaterialReceived;
    }

    public void setAllMaterialReceived(String allMaterialReceived) {
        this.allMaterialReceived = allMaterialReceived;
    }

    public String getDispatchDetails() {
        return dispatchDetails;
    }

    public void setDispatchDetails(String dispatchDetails) {
        this.dispatchDetails = dispatchDetails;
    }

    public String getMlGroup() {
        return mlGroup;
    }

    public void setMlGroup(String mlGroup) {
        this.mlGroup = mlGroup;
    }

    public String getMrnType() {
        return mrnType;
    }

    public void setMrnType(String mrnType) {
        this.mrnType = mrnType;
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

    public String getmAndLRequired() {
        return mAndLRequired;
    }

    public void setmAndLRequired(String mAndLRequired) {
        this.mAndLRequired = mAndLRequired;
    }

    public String getSiblingOrder() {
        return siblingOrder;
    }

    public void setSiblingOrder(String siblingOrder) {
        this.siblingOrder = siblingOrder;
    }

    public String getLinkedPe() {
        return linkedPe;
    }

    public void setLinkedPe(String linkedPe) {
        this.linkedPe = linkedPe;
    }

    public String getLinkedOrderId() {
        return linkedOrderId;
    }

    public void setLinkedOrderId(String linkedOrderId) {
        this.linkedOrderId = linkedOrderId;
    }
}
