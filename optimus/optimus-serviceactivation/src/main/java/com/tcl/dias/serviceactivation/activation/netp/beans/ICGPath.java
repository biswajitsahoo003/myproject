
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ICGPath complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ICGPath">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="primaryICGRouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *         &lt;element name="secondaryICGRouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *         &lt;element name="primaryICGStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="secondaryICGStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="primaryPartnerVDOMInterfaceIP" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PartnerDevice" minOccurs="0"/>
 *         &lt;element name="secondaryPartnerVDOMInterfaceIP" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PartnerDevice" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ICGPath", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_01", propOrder = {
    "primaryICGRouter",
    "secondaryICGRouter",
    "primaryICGStaticRoutes",
    "secondaryICGStaticRoutes",
    "primaryPartnerVDOMInterfaceIP",
    "secondaryPartnerVDOMInterfaceIP"
})
public class ICGPath {

    protected Router primaryICGRouter;
    protected Router secondaryICGRouter;
    protected StaticRoutes primaryICGStaticRoutes;
    protected StaticRoutes secondaryICGStaticRoutes;
    protected PartnerDevice primaryPartnerVDOMInterfaceIP;
    protected PartnerDevice secondaryPartnerVDOMInterfaceIP;

    /**
     * Gets the value of the primaryICGRouter property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getPrimaryICGRouter() {
        return primaryICGRouter;
    }

    /**
     * Sets the value of the primaryICGRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setPrimaryICGRouter(Router value) {
        this.primaryICGRouter = value;
    }

    /**
     * Gets the value of the secondaryICGRouter property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getSecondaryICGRouter() {
        return secondaryICGRouter;
    }

    /**
     * Sets the value of the secondaryICGRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setSecondaryICGRouter(Router value) {
        this.secondaryICGRouter = value;
    }

    /**
     * Gets the value of the primaryICGStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getPrimaryICGStaticRoutes() {
        return primaryICGStaticRoutes;
    }

    /**
     * Sets the value of the primaryICGStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setPrimaryICGStaticRoutes(StaticRoutes value) {
        this.primaryICGStaticRoutes = value;
    }

    /**
     * Gets the value of the secondaryICGStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getSecondaryICGStaticRoutes() {
        return secondaryICGStaticRoutes;
    }

    /**
     * Sets the value of the secondaryICGStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setSecondaryICGStaticRoutes(StaticRoutes value) {
        this.secondaryICGStaticRoutes = value;
    }

    /**
     * Gets the value of the primaryPartnerVDOMInterfaceIP property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerDevice }
     *     
     */
    public PartnerDevice getPrimaryPartnerVDOMInterfaceIP() {
        return primaryPartnerVDOMInterfaceIP;
    }

    /**
     * Sets the value of the primaryPartnerVDOMInterfaceIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerDevice }
     *     
     */
    public void setPrimaryPartnerVDOMInterfaceIP(PartnerDevice value) {
        this.primaryPartnerVDOMInterfaceIP = value;
    }

    /**
     * Gets the value of the secondaryPartnerVDOMInterfaceIP property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerDevice }
     *     
     */
    public PartnerDevice getSecondaryPartnerVDOMInterfaceIP() {
        return secondaryPartnerVDOMInterfaceIP;
    }

    /**
     * Sets the value of the secondaryPartnerVDOMInterfaceIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerDevice }
     *     
     */
    public void setSecondaryPartnerVDOMInterfaceIP(PartnerDevice value) {
        this.secondaryPartnerVDOMInterfaceIP = value;
    }

}
