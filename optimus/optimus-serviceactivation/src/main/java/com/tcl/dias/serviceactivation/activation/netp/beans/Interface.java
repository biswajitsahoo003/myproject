
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Interface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Interface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="v4IpAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="v6IpAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *         &lt;element name="QoS" type="{http://www.tcl.com/2011/11/ipsvc/xsd}QoS" minOccurs="0"/>
 *         &lt;element name="inboundAccessControlList" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="outboundAccessControlList" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="inboundAccessControlListV6" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="outboundAccessControlListV6" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Interface", propOrder = {
    "name",
    "physicalPortName",
    "v4IpAddress",
    "v6IpAddress",
    "qoS",
    "inboundAccessControlList",
    "outboundAccessControlList",
    "inboundAccessControlListV6",
    "outboundAccessControlListV6",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified"
})
@XmlSeeAlso({
    TunnelInterface.class,
    LoopbackInterface.class,
    ISDNInterface.class,
    ChannelizedPDHInterface.class,
    EthernetInterface.class,
    SerialInterface.class,
    ChannelizedSDHInterface.class
})
public class Interface {

    protected String name;
    protected String physicalPortName;
    protected IPV4Address v4IpAddress;
    protected IPV6Address v6IpAddress;
    @XmlElement(name = "QoS")
    protected QoS qoS;
    protected AccessControlList inboundAccessControlList;
    protected AccessControlList outboundAccessControlList;
    protected AccessControlList inboundAccessControlListV6;
    protected AccessControlList outboundAccessControlListV6;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the physicalPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalPortName() {
        return physicalPortName;
    }

    /**
     * Sets the value of the physicalPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalPortName(String value) {
        this.physicalPortName = value;
    }

    /**
     * Gets the value of the v4IpAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getV4IpAddress() {
        return v4IpAddress;
    }

    /**
     * Sets the value of the v4IpAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setV4IpAddress(IPV4Address value) {
        this.v4IpAddress = value;
    }

    /**
     * Gets the value of the v6IpAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV6Address }
     *     
     */
    public IPV6Address getV6IpAddress() {
        return v6IpAddress;
    }

    /**
     * Sets the value of the v6IpAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV6Address }
     *     
     */
    public void setV6IpAddress(IPV6Address value) {
        this.v6IpAddress = value;
    }

    /**
     * Gets the value of the qoS property.
     * 
     * @return
     *     possible object is
     *     {@link QoS }
     *     
     */
    public QoS getQoS() {
        return qoS;
    }

    /**
     * Sets the value of the qoS property.
     * 
     * @param value
     *     allowed object is
     *     {@link QoS }
     *     
     */
    public void setQoS(QoS value) {
        this.qoS = value;
    }

    /**
     * Gets the value of the inboundAccessControlList property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getInboundAccessControlList() {
        return inboundAccessControlList;
    }

    /**
     * Sets the value of the inboundAccessControlList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setInboundAccessControlList(AccessControlList value) {
        this.inboundAccessControlList = value;
    }

    /**
     * Gets the value of the outboundAccessControlList property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getOutboundAccessControlList() {
        return outboundAccessControlList;
    }

    /**
     * Sets the value of the outboundAccessControlList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setOutboundAccessControlList(AccessControlList value) {
        this.outboundAccessControlList = value;
    }

    /**
     * Gets the value of the inboundAccessControlListV6 property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getInboundAccessControlListV6() {
        return inboundAccessControlListV6;
    }

    /**
     * Sets the value of the inboundAccessControlListV6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setInboundAccessControlListV6(AccessControlList value) {
        this.inboundAccessControlListV6 = value;
    }

    /**
     * Gets the value of the outboundAccessControlListV6 property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getOutboundAccessControlListV6() {
        return outboundAccessControlListV6;
    }

    /**
     * Sets the value of the outboundAccessControlListV6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setOutboundAccessControlListV6(AccessControlList value) {
        this.outboundAccessControlListV6 = value;
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

}
