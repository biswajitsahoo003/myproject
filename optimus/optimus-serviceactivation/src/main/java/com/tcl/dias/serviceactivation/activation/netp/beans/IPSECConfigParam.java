
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPSECConfigParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPSECConfigParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isakmpPolicyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ipSECProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transformSetName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="keyRingName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isakmpProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cryptoMapName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cryptoMapSeqno" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="cryptoMapACL" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
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
@XmlType(name = "IPSECConfigParam", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "isakmpPolicyName",
    "ipSECProfileName",
    "transformSetName",
    "keyRingName",
    "isakmpProfileName",
    "cryptoMapName",
    "cryptoMapSeqno",
    "cryptoMapACL",
    "isObjectInstanceModified"
})
public class IPSECConfigParam {

    protected String isakmpPolicyName;
    protected String ipSECProfileName;
    protected String transformSetName;
    protected String keyRingName;
    protected String isakmpProfileName;
    protected String cryptoMapName;
    protected String cryptoMapSeqno;
    protected AccessControlList cryptoMapACL;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the isakmpPolicyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsakmpPolicyName() {
        return isakmpPolicyName;
    }

    /**
     * Sets the value of the isakmpPolicyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsakmpPolicyName(String value) {
        this.isakmpPolicyName = value;
    }

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
     * Gets the value of the transformSetName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransformSetName() {
        return transformSetName;
    }

    /**
     * Sets the value of the transformSetName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransformSetName(String value) {
        this.transformSetName = value;
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
     * Gets the value of the cryptoMapName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCryptoMapName() {
        return cryptoMapName;
    }

    /**
     * Sets the value of the cryptoMapName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCryptoMapName(String value) {
        this.cryptoMapName = value;
    }

    /**
     * Gets the value of the cryptoMapSeqno property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCryptoMapSeqno() {
        return cryptoMapSeqno;
    }

    /**
     * Sets the value of the cryptoMapSeqno property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCryptoMapSeqno(String value) {
        this.cryptoMapSeqno = value;
    }

    /**
     * Gets the value of the cryptoMapACL property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getCryptoMapACL() {
        return cryptoMapACL;
    }

    /**
     * Sets the value of the cryptoMapACL property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setCryptoMapACL(AccessControlList value) {
        this.cryptoMapACL = value;
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
