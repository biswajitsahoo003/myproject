
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HostedExchangeCEConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HostedExchangeCEConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HostedExchangeACL" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="natLoopbackInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}LoopbackInterface" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HostedExchangeCEConfig", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "hostedExchangeACL",
    "natLoopbackInterface"
})
public class HostedExchangeCEConfig {

    @XmlElement(name = "HostedExchangeACL")
    protected AccessControlList hostedExchangeACL;
    protected LoopbackInterface natLoopbackInterface;

    /**
     * Gets the value of the hostedExchangeACL property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getHostedExchangeACL() {
        return hostedExchangeACL;
    }

    /**
     * Sets the value of the hostedExchangeACL property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setHostedExchangeACL(AccessControlList value) {
        this.hostedExchangeACL = value;
    }

    /**
     * Gets the value of the natLoopbackInterface property.
     * 
     * @return
     *     possible object is
     *     {@link LoopbackInterface }
     *     
     */
    public LoopbackInterface getNatLoopbackInterface() {
        return natLoopbackInterface;
    }

    /**
     * Sets the value of the natLoopbackInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoopbackInterface }
     *     
     */
    public void setNatLoopbackInterface(LoopbackInterface value) {
        this.natLoopbackInterface = value;
    }

}
