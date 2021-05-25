//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.30 at 06:34:20 PM IST 
//


package com.tcl.dias.oms.cisco.beans;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ApplicationAreaType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ApplicationAreaType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}Sender" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}Receiver" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}CreationDateTime"/&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}BODID" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.openapplications.org/oagis/9}UserArea" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ApplicationAreaType", propOrder = {
    "sender",
    "receiver",
    "creationDateTime",
    "bodid",
    "userArea"
})
public class ApplicationAreaType {

    @XmlElement(name = "Sender")
    protected SenderType sender;
    @XmlElement(name = "Receiver")
    protected List<ReceiverType> receiver;
    @XmlElement(name = "CreationDateTime", required = true)
    @XmlSchemaType(name = "anySimpleType")
    protected String creationDateTime;
    @XmlElement(name = "BODID")
    protected IdentifierType2 bodid;
    @XmlElement(name = "UserArea")
    protected UserAreaType userArea;

    /**
     * Gets the value of the sender property.
     * 
     * @return
     *     possible object is
     *     {@link SenderType }
     *     
     */
    public SenderType getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     * 
     * @param value
     *     allowed object is
     *     {@link SenderType }
     *     
     */
    public void setSender(SenderType value) {
        this.sender = value;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the receiver property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReceiver().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ReceiverType }
     * 
     * 
     */
    public List<ReceiverType> getReceiver() {
        if (receiver == null) {
            receiver = new ArrayList<ReceiverType>();
        }
        return this.receiver;
    }

    /**
     * Gets the value of the creationDateTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCreationDateTime() {
        return creationDateTime;
    }

    /**
     * Sets the value of the creationDateTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCreationDateTime(String value) {
        this.creationDateTime = value;
    }

    /**
     * Gets the value of the bodid property.
     * 
     * @return
     *     possible object is
     *     {@link IdentifierType2 }
     *     
     */
    public IdentifierType2 getBODID() {
        return bodid;
    }

    /**
     * Sets the value of the bodid property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdentifierType2 }
     *     
     */
    public void setBODID(IdentifierType2 value) {
        this.bodid = value;
    }

    /**
     * Gets the value of the userArea property.
     * 
     * @return
     *     possible object is
     *     {@link UserAreaType }
     *     
     */
    public UserAreaType getUserArea() {
        return userArea;
    }

    /**
     * Sets the value of the userArea property.
     * 
     * @param value
     *     allowed object is
     *     {@link UserAreaType }
     *     
     */
    public void setUserArea(UserAreaType value) {
        this.userArea = value;
    }

}
