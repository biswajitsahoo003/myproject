
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PEVDOMPath complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PEVDOMPath">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="primaryHubRouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *         &lt;element name="secondaryHubRouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *         &lt;element name="primaryHubStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="secondaryHubStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *         &lt;element name="primaryVDOM" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PartnerDevice" minOccurs="0"/>
 *         &lt;element name="secondaryVDOM" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PartnerDevice" minOccurs="0"/>
 *         &lt;element name="BGPRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PEVDOMPath", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_01", propOrder = {
    "primaryHubRouter",
    "secondaryHubRouter",
    "primaryHubStaticRoutes",
    "secondaryHubStaticRoutes",
    "primaryVDOM",
    "secondaryVDOM",
    "bgpRoutingProtocol"
})
public class PEVDOMPath {

    protected Router primaryHubRouter;
    protected Router secondaryHubRouter;
    protected StaticRoutes primaryHubStaticRoutes;
    protected StaticRoutes secondaryHubStaticRoutes;
    protected PartnerDevice primaryVDOM;
    protected PartnerDevice secondaryVDOM;
    @XmlElement(name = "BGPRoutingProtocol")
    protected BGPRoutingProtocol bgpRoutingProtocol;

    /**
     * Gets the value of the primaryHubRouter property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getPrimaryHubRouter() {
        return primaryHubRouter;
    }

    /**
     * Sets the value of the primaryHubRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setPrimaryHubRouter(Router value) {
        this.primaryHubRouter = value;
    }

    /**
     * Gets the value of the secondaryHubRouter property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getSecondaryHubRouter() {
        return secondaryHubRouter;
    }

    /**
     * Sets the value of the secondaryHubRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setSecondaryHubRouter(Router value) {
        this.secondaryHubRouter = value;
    }

    /**
     * Gets the value of the primaryHubStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getPrimaryHubStaticRoutes() {
        return primaryHubStaticRoutes;
    }

    /**
     * Sets the value of the primaryHubStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setPrimaryHubStaticRoutes(StaticRoutes value) {
        this.primaryHubStaticRoutes = value;
    }

    /**
     * Gets the value of the secondaryHubStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getSecondaryHubStaticRoutes() {
        return secondaryHubStaticRoutes;
    }

    /**
     * Sets the value of the secondaryHubStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setSecondaryHubStaticRoutes(StaticRoutes value) {
        this.secondaryHubStaticRoutes = value;
    }

    /**
     * Gets the value of the primaryVDOM property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerDevice }
     *     
     */
    public PartnerDevice getPrimaryVDOM() {
        return primaryVDOM;
    }

    /**
     * Sets the value of the primaryVDOM property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerDevice }
     *     
     */
    public void setPrimaryVDOM(PartnerDevice value) {
        this.primaryVDOM = value;
    }

    /**
     * Gets the value of the secondaryVDOM property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerDevice }
     *     
     */
    public PartnerDevice getSecondaryVDOM() {
        return secondaryVDOM;
    }

    /**
     * Sets the value of the secondaryVDOM property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerDevice }
     *     
     */
    public void setSecondaryVDOM(PartnerDevice value) {
        this.secondaryVDOM = value;
    }

    /**
     * Gets the value of the bgpRoutingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link BGPRoutingProtocol }
     *     
     */
    public BGPRoutingProtocol getBGPRoutingProtocol() {
        return bgpRoutingProtocol;
    }

    /**
     * Sets the value of the bgpRoutingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link BGPRoutingProtocol }
     *     
     */
    public void setBGPRoutingProtocol(BGPRoutingProtocol value) {
        this.bgpRoutingProtocol = value;
    }

}
