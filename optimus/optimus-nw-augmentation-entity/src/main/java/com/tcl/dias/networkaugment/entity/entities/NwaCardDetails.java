package com.tcl.dias.networkaugment.entity.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nwa_card_details")
public class NwaCardDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "card_sr_number")
    String cardSrNumber;

    @Column(name =  "card_type")
    String cardType;

    @Column(name =  "unique_id")
    String uniqueId;

    @Column(name = "shelf_details")
    String shelfDetails;

    @Column(name = "shelf_slot_details")
    String shelfSlotDetails;

    @Column(name = "slot_details")
    String slotDetails;

    @Column(name = "sub_slot_no")
    String subSlotNo;

    @Column(name = "card_description")
    String cardDescription;

    @Column(name = "shelf_type")
    String shelfType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private ScOrder scOrder;

    @Column(name = "seq_no")
    private Integer seqNo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardSrNumber() {
        return cardSrNumber;
    }

    public void setCardSrNumber(String cardSrNumber) {
        this.cardSrNumber = cardSrNumber;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getShelfDetails() {
        return shelfDetails;
    }

    public void setShelfDetails(String shelfDetails) {
        this.shelfDetails = shelfDetails;
    }

    public String getShelfSlotDetails() {
        return shelfSlotDetails;
    }

    public void setShelfSlotDetails(String shelfSlotDetails) {
        this.shelfSlotDetails = shelfSlotDetails;
    }

    public String getSlotDetails() {
        return slotDetails;
    }

    public void setSlotDetails(String slotDetails) {
        this.slotDetails = slotDetails;
    }

    public String getCardDescription() {
        return cardDescription;
    }

    public void setCardDescription(String cardDescription) {
        this.cardDescription = cardDescription;
    }

    public String getShelfType() {
        return shelfType;
    }

    public void setShelfType(String shelfType) {
        this.shelfType = shelfType;
    }

    public String getSubSlotNo() {
        return subSlotNo;
    }

    public void setSubSlotNo(String subSlotNo) {
        this.subSlotNo = subSlotNo;
    }

    public ScOrder getScOrder() {
        return scOrder;
    }

    public void setScOrder(ScOrder scOrder) {
        this.scOrder = scOrder;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public Integer getSeqNo() {
        return seqNo;
    }
}
