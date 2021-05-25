package com.tcl.dias.servicefulfillmentutils.beans;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddCardToCramerBean {

    private String cardType;
    private String name;
    private String shelfNumber;
    private String shelfSlotNumber;
    private String eorId;
    private Map<String, String> node;
    private int id;
    @Value("${default.empty:}")
    private String cardSlotNumber;
    @Value("${default.empty:}")
    private String subSlotNumber;

    public String getCardSlotNumber() {
        return cardSlotNumber;
    }

    public void setCardSlotNumber(String cardSlotNumber) {
        this.cardSlotNumber = cardSlotNumber;
    }

    public String getSubSlotNumber() {
        return subSlotNumber;
    }

    public void setSubSlotNumber(String subSlotNumber) {
        this.subSlotNumber = subSlotNumber;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setShelfNumber(String shelfNumber) {
        this.shelfNumber = shelfNumber;
    }

    public String getShelfNumber() {
        return shelfNumber;
    }

    public void setShelfSlotNumber(String shelfSlotNumber) {
        this.shelfSlotNumber = shelfSlotNumber;
    }

    public String getShelfSlotNumber() {
        return shelfSlotNumber;
    }

    public void setEorId(String eorId) {
        this.eorId = eorId;
    }

    public String getEorId() {
        return eorId;
    }

    public void setNode(Map<String, String> node) {
        this.node = node;
    }

    public Map<String, String> getNode() {
        return node;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    @Override
    public String toString() {
        return "AddCardToCramerBean{" +
                "cardType='" + cardType + '\'' +
                ", name='" + name + '\'' +
                ", shelfNumber='" + shelfNumber + '\'' +
                ", shelfSlotNumber='" + shelfSlotNumber + '\'' +
                ", eorId='" + eorId + '\'' +
                ", node=" + node +
                ", id=" + id +
                ", cardSlotNumber='" + cardSlotNumber + '\'' +
                ", subSlotNumber='" + subSlotNumber + '\'' +
                '}';
    }
}
