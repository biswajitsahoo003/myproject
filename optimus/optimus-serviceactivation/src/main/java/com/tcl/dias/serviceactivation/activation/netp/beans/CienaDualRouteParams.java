
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CienaDualRouteParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CienaDualRouteParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="locationA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="locationZ" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="silver" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gold" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primaryRouteLocationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secondaryRouteLocationID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="primaryRouteAdminWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secondaryRouteAdminWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CienaDualRouteParams", namespace = "http://www.tcl.com/2014/11/transmissionsvc/xsd", propOrder = {
    "locationA",
    "locationZ",
    "silver",
    "gold",
    "primaryRouteLocationID",
    "secondaryRouteLocationID",
    "primaryRouteAdminWeight",
    "secondaryRouteAdminWeight",
    "attribute1",
    "attribute2"
})
public class CienaDualRouteParams {

    protected String locationA;
    protected String locationZ;
    protected String silver;
    protected String gold;
    protected String primaryRouteLocationID;
    protected String secondaryRouteLocationID;
    protected String primaryRouteAdminWeight;
    protected String secondaryRouteAdminWeight;
    protected String attribute1;
    protected String attribute2;

    /**
     * Gets the value of the locationA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationA() {
        return locationA;
    }

    /**
     * Sets the value of the locationA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationA(String value) {
        this.locationA = value;
    }

    /**
     * Gets the value of the locationZ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationZ() {
        return locationZ;
    }

    /**
     * Sets the value of the locationZ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationZ(String value) {
        this.locationZ = value;
    }

    /**
     * Gets the value of the silver property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSilver() {
        return silver;
    }

    /**
     * Sets the value of the silver property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSilver(String value) {
        this.silver = value;
    }

    /**
     * Gets the value of the gold property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGold() {
        return gold;
    }

    /**
     * Sets the value of the gold property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGold(String value) {
        this.gold = value;
    }

    /**
     * Gets the value of the primaryRouteLocationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryRouteLocationID() {
        return primaryRouteLocationID;
    }

    /**
     * Sets the value of the primaryRouteLocationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryRouteLocationID(String value) {
        this.primaryRouteLocationID = value;
    }

    /**
     * Gets the value of the secondaryRouteLocationID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondaryRouteLocationID() {
        return secondaryRouteLocationID;
    }

    /**
     * Sets the value of the secondaryRouteLocationID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondaryRouteLocationID(String value) {
        this.secondaryRouteLocationID = value;
    }

    /**
     * Gets the value of the primaryRouteAdminWeight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryRouteAdminWeight() {
        return primaryRouteAdminWeight;
    }

    /**
     * Sets the value of the primaryRouteAdminWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryRouteAdminWeight(String value) {
        this.primaryRouteAdminWeight = value;
    }

    /**
     * Gets the value of the secondaryRouteAdminWeight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondaryRouteAdminWeight() {
        return secondaryRouteAdminWeight;
    }

    /**
     * Sets the value of the secondaryRouteAdminWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondaryRouteAdminWeight(String value) {
        this.secondaryRouteAdminWeight = value;
    }

    /**
     * Gets the value of the attribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * Sets the value of the attribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute1(String value) {
        this.attribute1 = value;
    }

    /**
     * Gets the value of the attribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * Sets the value of the attribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute2(String value) {
        this.attribute2 = value;
    }

}
