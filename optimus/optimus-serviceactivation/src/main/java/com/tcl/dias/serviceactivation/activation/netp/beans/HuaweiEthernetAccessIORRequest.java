
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HuaweiEthernetAccessIORRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiEthernetAccessIORRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ANodePortDetails" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSNodePortDetails" minOccurs="0"/>
 *         &lt;element name="ZNodePortDetails" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSNodePortDetails" minOccurs="0"/>
 *         &lt;element name="ERPSRingID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ERPSRingDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HuaweiEthernetAccessIORRequest", namespace = "http://www.tcl.com/2014/5/ipsvc/xsd", propOrder = {
    "aNodePortDetails",
    "zNodePortDetails",
    "erpsRingID",
    "erpsRingDescription",
    "isObjectInstanceModified"
})
public class HuaweiEthernetAccessIORRequest {

    @XmlElement(name = "ANodePortDetails")
    protected HuaweiERPSNodePortDetails aNodePortDetails;
    @XmlElement(name = "ZNodePortDetails")
    protected HuaweiERPSNodePortDetails zNodePortDetails;
    @XmlElement(name = "ERPSRingID")
    protected String erpsRingID;
    @XmlElement(name = "ERPSRingDescription")
    protected String erpsRingDescription;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the aNodePortDetails property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public HuaweiERPSNodePortDetails getANodePortDetails() {
        return aNodePortDetails;
    }

    /**
     * Sets the value of the aNodePortDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public void setANodePortDetails(HuaweiERPSNodePortDetails value) {
        this.aNodePortDetails = value;
    }

    /**
     * Gets the value of the zNodePortDetails property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public HuaweiERPSNodePortDetails getZNodePortDetails() {
        return zNodePortDetails;
    }

    /**
     * Sets the value of the zNodePortDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiERPSNodePortDetails }
     *     
     */
    public void setZNodePortDetails(HuaweiERPSNodePortDetails value) {
        this.zNodePortDetails = value;
    }

    /**
     * Gets the value of the erpsRingID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getERPSRingID() {
        return erpsRingID;
    }

    /**
     * Sets the value of the erpsRingID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setERPSRingID(String value) {
        this.erpsRingID = value;
    }

    /**
     * Gets the value of the erpsRingDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getERPSRingDescription() {
        return erpsRingDescription;
    }

    /**
     * Sets the value of the erpsRingDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setERPSRingDescription(String value) {
        this.erpsRingDescription = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectInstanceModified(Boolean value) {
        this.isObjectInstanceModified = value;
    }

}
