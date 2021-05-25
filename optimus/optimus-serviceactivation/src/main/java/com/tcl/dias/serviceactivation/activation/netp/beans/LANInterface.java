
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LANInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LANInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EthernetInterface" minOccurs="0"/>
 *         &lt;element name="routingProtocol" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol"/>
 *                   &lt;element name="eigprRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EIGRPRoutingProtocol"/>
 *                   &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol"/>
 *                   &lt;element name="staticRoutingProtocol" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes"/>
 *                   &lt;element name="ripRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RIPRoutingProtocol" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="HSRPProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}HSRPProtocol" minOccurs="0"/>
 *         &lt;element name="secondaryV4LANIPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="secondaryV6LANIPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="LANPIMMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LANInterface", propOrder = {
    "_interface",
    "routingProtocol",
    "hsrpProtocol",
    "secondaryV4LANIPAddress",
    "secondaryV6LANIPAddress",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "lanpimMode",
    "rip"
})
public class LANInterface {

    @XmlElement(name = "interface")
    protected EthernetInterface _interface;
    protected LANInterface.RoutingProtocol routingProtocol;
    @XmlElement(name = "HSRPProtocol")
    protected HSRPProtocol hsrpProtocol;
    protected IPV4Address secondaryV4LANIPAddress;
    protected IPV6Address secondaryV6LANIPAddress;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    @XmlElement(name = "LANPIMMode")
    protected String lanpimMode;
    @XmlElement(name = "RIP")
    protected String rip;

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link EthernetInterface }
     *     
     */
    public EthernetInterface getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link EthernetInterface }
     *     
     */
    public void setInterface(EthernetInterface value) {
        this._interface = value;
    }

    /**
     * Gets the value of the routingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link LANInterface.RoutingProtocol }
     *     
     */
    public LANInterface.RoutingProtocol getRoutingProtocol() {
        return routingProtocol;
    }

    /**
     * Sets the value of the routingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link LANInterface.RoutingProtocol }
     *     
     */
    public void setRoutingProtocol(LANInterface.RoutingProtocol value) {
        this.routingProtocol = value;
    }

    /**
     * Gets the value of the hsrpProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link HSRPProtocol }
     *     
     */
    public HSRPProtocol getHSRPProtocol() {
        return hsrpProtocol;
    }

    /**
     * Sets the value of the hsrpProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link HSRPProtocol }
     *     
     */
    public void setHSRPProtocol(HSRPProtocol value) {
        this.hsrpProtocol = value;
    }

    /**
     * Gets the value of the secondaryV4LANIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getSecondaryV4LANIPAddress() {
        return secondaryV4LANIPAddress;
    }

    /**
     * Sets the value of the secondaryV4LANIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setSecondaryV4LANIPAddress(IPV4Address value) {
        this.secondaryV4LANIPAddress = value;
    }

    /**
     * Gets the value of the secondaryV6LANIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV6Address }
     *     
     */
    public IPV6Address getSecondaryV6LANIPAddress() {
        return secondaryV6LANIPAddress;
    }

    /**
     * Sets the value of the secondaryV6LANIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV6Address }
     *     
     */
    public void setSecondaryV6LANIPAddress(IPV6Address value) {
        this.secondaryV6LANIPAddress = value;
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

    /**
     * Gets the value of the isChildObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChildObjectInstanceModified() {
        return isChildObjectInstanceModified;
    }

    /**
     * Sets the value of the isChildObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChildObjectInstanceModified(Boolean value) {
        this.isChildObjectInstanceModified = value;
    }

    /**
     * Gets the value of the lanpimMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLANPIMMode() {
        return lanpimMode;
    }

    /**
     * Sets the value of the lanpimMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLANPIMMode(String value) {
        this.lanpimMode = value;
    }

    /**
     * Gets the value of the rip property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRIP() {
        return rip;
    }

    /**
     * Sets the value of the rip property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRIP(String value) {
        this.rip = value;
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
     *         &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol"/>
     *         &lt;element name="eigprRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EIGRPRoutingProtocol"/>
     *         &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol"/>
     *         &lt;element name="staticRoutingProtocol" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes"/>
     *         &lt;element name="ripRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RIPRoutingProtocol" minOccurs="0"/>
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
        "eigprRoutingProtocol",
        "ospfRoutingProtocol",
        "staticRoutingProtocol",
        "ripRoutingProtocol"
    })
    public static class RoutingProtocol {

        protected BGPRoutingProtocol bgpRoutingProtocol;
        protected EIGRPRoutingProtocol eigprRoutingProtocol;
        protected OSPFRoutingProtocol ospfRoutingProtocol;
        protected StaticRoutes staticRoutingProtocol;
        protected RIPRoutingProtocol ripRoutingProtocol;

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
         * Gets the value of the eigprRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link EIGRPRoutingProtocol }
         *     
         */
        public EIGRPRoutingProtocol getEigprRoutingProtocol() {
            return eigprRoutingProtocol;
        }

        /**
         * Sets the value of the eigprRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link EIGRPRoutingProtocol }
         *     
         */
        public void setEigprRoutingProtocol(EIGRPRoutingProtocol value) {
            this.eigprRoutingProtocol = value;
        }

        /**
         * Gets the value of the ospfRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link OSPFRoutingProtocol }
         *     
         */
        public OSPFRoutingProtocol getOspfRoutingProtocol() {
            return ospfRoutingProtocol;
        }

        /**
         * Sets the value of the ospfRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link OSPFRoutingProtocol }
         *     
         */
        public void setOspfRoutingProtocol(OSPFRoutingProtocol value) {
            this.ospfRoutingProtocol = value;
        }

        /**
         * Gets the value of the staticRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link StaticRoutes }
         *     
         */
        public StaticRoutes getStaticRoutingProtocol() {
            return staticRoutingProtocol;
        }

        /**
         * Sets the value of the staticRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link StaticRoutes }
         *     
         */
        public void setStaticRoutingProtocol(StaticRoutes value) {
            this.staticRoutingProtocol = value;
        }

        /**
         * Gets the value of the ripRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link RIPRoutingProtocol }
         *     
         */
        public RIPRoutingProtocol getRipRoutingProtocol() {
            return ripRoutingProtocol;
        }

        /**
         * Sets the value of the ripRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link RIPRoutingProtocol }
         *     
         */
        public void setRipRoutingProtocol(RIPRoutingProtocol value) {
            this.ripRoutingProtocol = value;
        }

    }

}
