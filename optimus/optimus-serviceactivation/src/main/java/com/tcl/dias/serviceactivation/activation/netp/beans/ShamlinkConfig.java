
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShamlinkConfig complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShamlinkConfig">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="interface" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localAddress" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="IPv4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *                   &lt;element name="IPv6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="remoteAddress" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="IPv4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *                   &lt;element name="IPv6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShamlinkConfig", namespace = "http://www.tcl.com/2014/5/ipsvc/xsd", propOrder = {
    "_interface",
    "localAddress",
    "remoteAddress"
})
public class ShamlinkConfig {

    @XmlElement(name = "interface")
    protected String _interface;
    protected ShamlinkConfig.LocalAddress localAddress;
    protected ShamlinkConfig.RemoteAddress remoteAddress;

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterface() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterface(String value) {
        this._interface = value;
    }

    /**
     * Gets the value of the localAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ShamlinkConfig.LocalAddress }
     *     
     */
    public ShamlinkConfig.LocalAddress getLocalAddress() {
        return localAddress;
    }

    /**
     * Sets the value of the localAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShamlinkConfig.LocalAddress }
     *     
     */
    public void setLocalAddress(ShamlinkConfig.LocalAddress value) {
        this.localAddress = value;
    }

    /**
     * Gets the value of the remoteAddress property.
     * 
     * @return
     *     possible object is
     *     {@link ShamlinkConfig.RemoteAddress }
     *     
     */
    public ShamlinkConfig.RemoteAddress getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * Sets the value of the remoteAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link ShamlinkConfig.RemoteAddress }
     *     
     */
    public void setRemoteAddress(ShamlinkConfig.RemoteAddress value) {
        this.remoteAddress = value;
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
     *         &lt;element name="IPv4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
     *         &lt;element name="IPv6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
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
        "iPv4Address",
        "iPv6Address"
    })
    public static class LocalAddress {

        @XmlElement(name = "IPv4Address")
        protected IPV4Address iPv4Address;
        @XmlElement(name = "IPv6Address")
        protected IPV6Address iPv6Address;

        /**
         * Gets the value of the iPv4Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV4Address }
         *     
         */
        public IPV4Address getIPv4Address() {
            return iPv4Address;
        }

        /**
         * Sets the value of the iPv4Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV4Address }
         *     
         */
        public void setIPv4Address(IPV4Address value) {
            this.iPv4Address = value;
        }

        /**
         * Gets the value of the iPv6Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV6Address }
         *     
         */
        public IPV6Address getIPv6Address() {
            return iPv6Address;
        }

        /**
         * Sets the value of the iPv6Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV6Address }
         *     
         */
        public void setIPv6Address(IPV6Address value) {
            this.iPv6Address = value;
        }

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
     *         &lt;element name="IPv4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
     *         &lt;element name="IPv6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
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
        "iPv4Address",
        "iPv6Address"
    })
    public static class RemoteAddress {

        @XmlElement(name = "IPv4Address")
        protected IPV4Address iPv4Address;
        @XmlElement(name = "IPv6Address")
        protected IPV6Address iPv6Address;

        /**
         * Gets the value of the iPv4Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV4Address }
         *     
         */
        public IPV4Address getIPv4Address() {
            return iPv4Address;
        }

        /**
         * Sets the value of the iPv4Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV4Address }
         *     
         */
        public void setIPv4Address(IPV4Address value) {
            this.iPv4Address = value;
        }

        /**
         * Gets the value of the iPv6Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV6Address }
         *     
         */
        public IPV6Address getIPv6Address() {
            return iPv6Address;
        }

        /**
         * Sets the value of the iPv6Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV6Address }
         *     
         */
        public void setIPv6Address(IPV6Address value) {
            this.iPv6Address = value;
        }

    }

}
