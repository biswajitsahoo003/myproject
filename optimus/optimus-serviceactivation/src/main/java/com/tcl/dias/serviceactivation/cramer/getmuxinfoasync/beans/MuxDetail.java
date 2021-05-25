
package com.tcl.dias.serviceactivation.cramer.getmuxinfoasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for muxDetail complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="muxDetail"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="muxIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="muxName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Bay" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EORID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="EORProvisionStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Floor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MuxprovisionStatus" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Priority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="Wing" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "muxDetail", propOrder = {
    "muxIP",
    "muxName",
    "bay",
    "eorid",
    "eorProvisionStatus",
    "floor",
    "muxprovisionStatus",
    "priority",
    "wing",
    "message"
})
public class MuxDetail {

    protected String muxIP;
    protected String muxName;
    @XmlElement(name = "Bay")
    protected String bay;
    @XmlElement(name = "EORID")
    protected String eorid;
    @XmlElement(name = "EORProvisionStatus")
    protected String eorProvisionStatus;
    @XmlElement(name = "Floor")
    protected String floor;
    @XmlElement(name = "MuxprovisionStatus")
    protected String muxprovisionStatus;
    @XmlElement(name = "Priority")
    protected String priority;
    @XmlElement(name = "Wing")
    protected String wing;
    protected String message;

    /**
     * Gets the value of the muxIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMuxIP() {
        return muxIP;
    }

    /**
     * Sets the value of the muxIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMuxIP(String value) {
        this.muxIP = value;
    }

    /**
     * Gets the value of the muxName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMuxName() {
        return muxName;
    }

    /**
     * Sets the value of the muxName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMuxName(String value) {
        this.muxName = value;
    }

    /**
     * Gets the value of the bay property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBay() {
        return bay;
    }

    /**
     * Sets the value of the bay property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBay(String value) {
        this.bay = value;
    }

    /**
     * Gets the value of the eorid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEORID() {
        return eorid;
    }

    /**
     * Sets the value of the eorid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEORID(String value) {
        this.eorid = value;
    }

    /**
     * Gets the value of the eorProvisionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEORProvisionStatus() {
        return eorProvisionStatus;
    }

    /**
     * Sets the value of the eorProvisionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEORProvisionStatus(String value) {
        this.eorProvisionStatus = value;
    }

    /**
     * Gets the value of the floor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFloor() {
        return floor;
    }

    /**
     * Sets the value of the floor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFloor(String value) {
        this.floor = value;
    }

    /**
     * Gets the value of the muxprovisionStatus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMuxprovisionStatus() {
        return muxprovisionStatus;
    }

    /**
     * Sets the value of the muxprovisionStatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMuxprovisionStatus(String value) {
        this.muxprovisionStatus = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the wing property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWing() {
        return wing;
    }

    /**
     * Sets the value of the wing property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWing(String value) {
        this.wing = value;
    }

    /**
     * Gets the value of the message property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the value of the message property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessage(String value) {
        this.message = value;
    }

}
