
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BackboneOSPFRoutingProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="BackboneOSPFRoutingProtocol">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ospfCost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isNetworkTypeP2P" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isAuthenticationRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="authenticationMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authenticationPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MDKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BackboneOSPFRoutingProtocol", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_01", propOrder = {
    "ospfCost",
    "isNetworkTypeP2P",
    "isAuthenticationRequired",
    "authenticationMode",
    "authenticationPassword",
    "mdKey",
    "isObjectInstanceModified"
})
public class BackboneOSPFRoutingProtocol {

    protected String ospfCost;
    protected String isNetworkTypeP2P;
    protected Boolean isAuthenticationRequired;
    protected String authenticationMode;
    protected String authenticationPassword;
    @XmlElement(name = "MDKey")
    protected String mdKey;
    protected String isObjectInstanceModified;

    /**
     * Gets the value of the ospfCost property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOspfCost() {
        return ospfCost;
    }

    /**
     * Sets the value of the ospfCost property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOspfCost(String value) {
        this.ospfCost = value;
    }

    /**
     * Gets the value of the isNetworkTypeP2P property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsNetworkTypeP2P() {
        return isNetworkTypeP2P;
    }

    /**
     * Sets the value of the isNetworkTypeP2P property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsNetworkTypeP2P(String value) {
        this.isNetworkTypeP2P = value;
    }

    /**
     * Gets the value of the isAuthenticationRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAuthenticationRequired() {
        return isAuthenticationRequired;
    }

    /**
     * Sets the value of the isAuthenticationRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAuthenticationRequired(Boolean value) {
        this.isAuthenticationRequired = value;
    }

    /**
     * Gets the value of the authenticationMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationMode() {
        return authenticationMode;
    }

    /**
     * Sets the value of the authenticationMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationMode(String value) {
        this.authenticationMode = value;
    }

    /**
     * Gets the value of the authenticationPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationPassword() {
        return authenticationPassword;
    }

    /**
     * Sets the value of the authenticationPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationPassword(String value) {
        this.authenticationPassword = value;
    }

    /**
     * Gets the value of the mdKey property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMDKey() {
        return mdKey;
    }

    /**
     * Sets the value of the mdKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMDKey(String value) {
        this.mdKey = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsObjectInstanceModified(String value) {
        this.isObjectInstanceModified = value;
    }

}
