
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DMVPNCESideConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DMVPNCESideConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ipSECProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyRingName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isakmpProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ipSECTransformSetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "DMVPNCESideConfig", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "ipSECProfileName",
    "keyRingName",
    "isakmpProfileName",
    "ipSECTransformSetName",
    "isObjectInstanceModified"
})
public class DMVPNCESideConfig {

    protected String ipSECProfileName;
    protected String keyRingName;
    protected String isakmpProfileName;
    protected String ipSECTransformSetName;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the ipSECProfileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpSECProfileName() {
        return ipSECProfileName;
    }

    /**
     * Sets the value of the ipSECProfileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpSECProfileName(String value) {
        this.ipSECProfileName = value;
    }

    /**
     * Gets the value of the keyRingName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKeyRingName() {
        return keyRingName;
    }

    /**
     * Sets the value of the keyRingName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKeyRingName(String value) {
        this.keyRingName = value;
    }

    /**
     * Gets the value of the isakmpProfileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsakmpProfileName() {
        return isakmpProfileName;
    }

    /**
     * Sets the value of the isakmpProfileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsakmpProfileName(String value) {
        this.isakmpProfileName = value;
    }

    /**
     * Gets the value of the ipSECTransformSetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpSECTransformSetName() {
        return ipSECTransformSetName;
    }

    /**
     * Sets the value of the ipSECTransformSetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpSECTransformSetName(String value) {
        this.ipSECTransformSetName = value;
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
