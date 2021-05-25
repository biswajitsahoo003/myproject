
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for WANInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WANInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interface">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="serialInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}SerialInterface"/>
 *                   &lt;element name="ethernetInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EthernetInterface"/>
 *                   &lt;element name="channelizedPDHInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ChannelizedPDHInterface"/>
 *                   &lt;element name="channelizedSDHInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ChannelizedSDHInterface"/>
 *                   &lt;element name="isdnInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ISDNInterface" minOccurs="0"/>
 *                   &lt;element name="tunnelInterface" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}TunnelInterface" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="staticRoutes" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol" minOccurs="0"/>
 *         &lt;element name="HSRP" type="{http://www.tcl.com/2011/11/ipsvc/xsd}HSRPProtocol" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="secondaryV4WANIPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WANInterface", propOrder = {
    "_interface",
    "staticRoutes",
    "hsrp",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "secondaryV4WANIPAddress"
})
public class WANInterface {

    @XmlElement(name = "interface", required = true)
    protected WANInterface.Interface _interface;
    protected StaticRoutingProtocol staticRoutes;
    @XmlElement(name = "HSRP")
    protected HSRPProtocol hsrp;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    protected IPV4Address secondaryV4WANIPAddress;

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link WANInterface.Interface }
     *     
     */
    public WANInterface.Interface getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link WANInterface.Interface }
     *     
     */
    public void setInterface(WANInterface.Interface value) {
        this._interface = value;
    }

    /**
     * Gets the value of the staticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutingProtocol }
     *     
     */
    public StaticRoutingProtocol getStaticRoutes() {
        return staticRoutes;
    }

    /**
     * Sets the value of the staticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutingProtocol }
     *     
     */
    public void setStaticRoutes(StaticRoutingProtocol value) {
        this.staticRoutes = value;
    }

    /**
     * Gets the value of the hsrp property.
     * 
     * @return
     *     possible object is
     *     {@link HSRPProtocol }
     *     
     */
    public HSRPProtocol getHSRP() {
        return hsrp;
    }

    /**
     * Sets the value of the hsrp property.
     * 
     * @param value
     *     allowed object is
     *     {@link HSRPProtocol }
     *     
     */
    public void setHSRP(HSRPProtocol value) {
        this.hsrp = value;
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
     * Gets the value of the secondaryV4WANIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getSecondaryV4WANIPAddress() {
        return secondaryV4WANIPAddress;
    }

    /**
     * Sets the value of the secondaryV4WANIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setSecondaryV4WANIPAddress(IPV4Address value) {
        this.secondaryV4WANIPAddress = value;
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
     *         &lt;element name="serialInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}SerialInterface"/>
     *         &lt;element name="ethernetInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EthernetInterface"/>
     *         &lt;element name="channelizedPDHInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ChannelizedPDHInterface"/>
     *         &lt;element name="channelizedSDHInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ChannelizedSDHInterface"/>
     *         &lt;element name="isdnInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ISDNInterface" minOccurs="0"/>
     *         &lt;element name="tunnelInterface" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}TunnelInterface" minOccurs="0"/>
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
        "serialInterface",
        "ethernetInterface",
        "channelizedPDHInterface",
        "channelizedSDHInterface",
        "isdnInterface",
        "tunnelInterface"
    })
    public static class Interface {

        protected SerialInterface serialInterface;
        protected EthernetInterface ethernetInterface;
        protected ChannelizedPDHInterface channelizedPDHInterface;
        protected ChannelizedSDHInterface channelizedSDHInterface;
        protected ISDNInterface isdnInterface;
        protected TunnelInterface tunnelInterface;

        /**
         * Gets the value of the serialInterface property.
         * 
         * @return
         *     possible object is
         *     {@link SerialInterface }
         *     
         */
        public SerialInterface getSerialInterface() {
            return serialInterface;
        }

        /**
         * Sets the value of the serialInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link SerialInterface }
         *     
         */
        public void setSerialInterface(SerialInterface value) {
            this.serialInterface = value;
        }

        /**
         * Gets the value of the ethernetInterface property.
         * 
         * @return
         *     possible object is
         *     {@link EthernetInterface }
         *     
         */
        public EthernetInterface getEthernetInterface() {
            return ethernetInterface;
        }

        /**
         * Sets the value of the ethernetInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link EthernetInterface }
         *     
         */
        public void setEthernetInterface(EthernetInterface value) {
            this.ethernetInterface = value;
        }

        /**
         * Gets the value of the channelizedPDHInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ChannelizedPDHInterface }
         *     
         */
        public ChannelizedPDHInterface getChannelizedPDHInterface() {
            return channelizedPDHInterface;
        }

        /**
         * Sets the value of the channelizedPDHInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ChannelizedPDHInterface }
         *     
         */
        public void setChannelizedPDHInterface(ChannelizedPDHInterface value) {
            this.channelizedPDHInterface = value;
        }

        /**
         * Gets the value of the channelizedSDHInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ChannelizedSDHInterface }
         *     
         */
        public ChannelizedSDHInterface getChannelizedSDHInterface() {
            return channelizedSDHInterface;
        }

        /**
         * Sets the value of the channelizedSDHInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ChannelizedSDHInterface }
         *     
         */
        public void setChannelizedSDHInterface(ChannelizedSDHInterface value) {
            this.channelizedSDHInterface = value;
        }

        /**
         * Gets the value of the isdnInterface property.
         * 
         * @return
         *     possible object is
         *     {@link ISDNInterface }
         *     
         */
        public ISDNInterface getIsdnInterface() {
            return isdnInterface;
        }

        /**
         * Sets the value of the isdnInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link ISDNInterface }
         *     
         */
        public void setIsdnInterface(ISDNInterface value) {
            this.isdnInterface = value;
        }

        /**
         * Gets the value of the tunnelInterface property.
         * 
         * @return
         *     possible object is
         *     {@link TunnelInterface }
         *     
         */
        public TunnelInterface getTunnelInterface() {
            return tunnelInterface;
        }

        /**
         * Sets the value of the tunnelInterface property.
         * 
         * @param value
         *     allowed object is
         *     {@link TunnelInterface }
         *     
         */
        public void setTunnelInterface(TunnelInterface value) {
            this.tunnelInterface = value;
        }

    }

}
