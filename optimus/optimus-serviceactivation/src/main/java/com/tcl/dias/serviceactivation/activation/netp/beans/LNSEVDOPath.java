
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LNSEVDOPath complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LNSEVDOPath">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="rwLNSCEProtocol">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol" minOccurs="0"/>
 *                   &lt;element name="staticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LNSISDNInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ISDNInterface" minOccurs="0"/>
 *         &lt;element name="EVDOISDNInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ISDNInterface" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LNSEVDOPath", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_06", propOrder = {
    "rwLNSCEProtocol",
    "lnsisdnInterface",
    "evdoisdnInterface"
})
public class LNSEVDOPath {

    @XmlElement(required = true)
    protected LNSEVDOPath.RwLNSCEProtocol rwLNSCEProtocol;
    @XmlElement(name = "LNSISDNInterface")
    protected ISDNInterface lnsisdnInterface;
    @XmlElement(name = "EVDOISDNInterface")
    protected ISDNInterface evdoisdnInterface;

    /**
     * Gets the value of the rwLNSCEProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link LNSEVDOPath.RwLNSCEProtocol }
     *     
     */
    public LNSEVDOPath.RwLNSCEProtocol getRwLNSCEProtocol() {
        return rwLNSCEProtocol;
    }

    /**
     * Sets the value of the rwLNSCEProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link LNSEVDOPath.RwLNSCEProtocol }
     *     
     */
    public void setRwLNSCEProtocol(LNSEVDOPath.RwLNSCEProtocol value) {
        this.rwLNSCEProtocol = value;
    }

    /**
     * Gets the value of the lnsisdnInterface property.
     * 
     * @return
     *     possible object is
     *     {@link ISDNInterface }
     *     
     */
    public ISDNInterface getLNSISDNInterface() {
        return lnsisdnInterface;
    }

    /**
     * Sets the value of the lnsisdnInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link ISDNInterface }
     *     
     */
    public void setLNSISDNInterface(ISDNInterface value) {
        this.lnsisdnInterface = value;
    }

    /**
     * Gets the value of the evdoisdnInterface property.
     * 
     * @return
     *     possible object is
     *     {@link ISDNInterface }
     *     
     */
    public ISDNInterface getEVDOISDNInterface() {
        return evdoisdnInterface;
    }

    /**
     * Sets the value of the evdoisdnInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link ISDNInterface }
     *     
     */
    public void setEVDOISDNInterface(ISDNInterface value) {
        this.evdoisdnInterface = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol" minOccurs="0"/>
     *         &lt;element name="staticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bgpRoutingProtocol",
        "staticRoutes"
    })
    public static class RwLNSCEProtocol {

        protected BGPRoutingProtocol bgpRoutingProtocol;
        protected StaticRoutes staticRoutes;

        /**
         * Gets the value of the bgpRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link BGPRoutingProtocol }
         *     
         */
        public BGPRoutingProtocol getBgpRoutingProtocol() {
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
        public void setBgpRoutingProtocol(BGPRoutingProtocol value) {
            this.bgpRoutingProtocol = value;
        }

        /**
         * Gets the value of the staticRoutes property.
         * 
         * @return
         *     possible object is
         *     {@link StaticRoutes }
         *     
         */
        public StaticRoutes getStaticRoutes() {
            return staticRoutes;
        }

        /**
         * Sets the value of the staticRoutes property.
         * 
         * @param value
         *     allowed object is
         *     {@link StaticRoutes }
         *     
         */
        public void setStaticRoutes(StaticRoutes value) {
            this.staticRoutes = value;
        }

    }

}
