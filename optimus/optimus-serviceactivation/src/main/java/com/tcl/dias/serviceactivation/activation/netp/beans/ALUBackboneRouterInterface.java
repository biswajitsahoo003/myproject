
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUBackboneRouterInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUBackboneRouterInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalPort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="v4IpAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="v6IpAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *         &lt;element name="ldpSyncTimer" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="qos" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bfdConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}BFDConfig" minOccurs="0"/>
 *         &lt;element name="icmpConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ICMPConfig" minOccurs="0"/>
 *         &lt;element name="PPDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="egressSlopePolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="egressSchedulerPolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="networkQueuePolicy" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="RSVPHelloInterval" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ALUBackboneRouterInterface", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "name",
    "physicalPort",
    "description",
    "v4IpAddress",
    "v6IpAddress",
    "ldpSyncTimer",
    "qos",
    "bfdConfig",
    "icmpConfig",
    "ppDescription",
    "egressSlopePolicy",
    "egressSchedulerPolicy",
    "networkQueuePolicy",
    "rsvpHelloInterval"
})
@XmlSeeAlso({
    ALUBackboneSerialInterface.class,
    ALUBackboneEthernetInterface.class,
    ALUBackboneLAGInterface.class,
    ALUBackbonePOSInterface.class
})
public class ALUBackboneRouterInterface {

    protected String name;
    protected String physicalPort;
    protected String description;
    protected IPV4Address v4IpAddress;
    protected IPV6Address v6IpAddress;
    protected Integer ldpSyncTimer;
    protected String qos;
    protected BFDConfig bfdConfig;
    protected ICMPConfig icmpConfig;
    @XmlElement(name = "PPDescription")
    protected String ppDescription;
    protected String egressSlopePolicy;
    protected String egressSchedulerPolicy;
    protected String networkQueuePolicy;
    @XmlElement(name = "RSVPHelloInterval")
    protected String rsvpHelloInterval;

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
     * Gets the value of the physicalPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalPort() {
        return physicalPort;
    }

    /**
     * Sets the value of the physicalPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalPort(String value) {
        this.physicalPort = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
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
     * Gets the value of the ldpSyncTimer property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getLdpSyncTimer() {
        return ldpSyncTimer;
    }

    /**
     * Sets the value of the ldpSyncTimer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setLdpSyncTimer(Integer value) {
        this.ldpSyncTimer = value;
    }

    /**
     * Gets the value of the qos property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQos() {
        return qos;
    }

    /**
     * Sets the value of the qos property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQos(String value) {
        this.qos = value;
    }

    /**
     * Gets the value of the bfdConfig property.
     * 
     * @return
     *     possible object is
     *     {@link BFDConfig }
     *     
     */
    public BFDConfig getBfdConfig() {
        return bfdConfig;
    }

    /**
     * Sets the value of the bfdConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link BFDConfig }
     *     
     */
    public void setBfdConfig(BFDConfig value) {
        this.bfdConfig = value;
    }

    /**
     * Gets the value of the icmpConfig property.
     * 
     * @return
     *     possible object is
     *     {@link ICMPConfig }
     *     
     */
    public ICMPConfig getIcmpConfig() {
        return icmpConfig;
    }

    /**
     * Sets the value of the icmpConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICMPConfig }
     *     
     */
    public void setIcmpConfig(ICMPConfig value) {
        this.icmpConfig = value;
    }

    /**
     * Gets the value of the ppDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPPDescription() {
        return ppDescription;
    }

    /**
     * Sets the value of the ppDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPPDescription(String value) {
        this.ppDescription = value;
    }

    /**
     * Gets the value of the egressSlopePolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEgressSlopePolicy() {
        return egressSlopePolicy;
    }

    /**
     * Sets the value of the egressSlopePolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEgressSlopePolicy(String value) {
        this.egressSlopePolicy = value;
    }

    /**
     * Gets the value of the egressSchedulerPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEgressSchedulerPolicy() {
        return egressSchedulerPolicy;
    }

    /**
     * Sets the value of the egressSchedulerPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEgressSchedulerPolicy(String value) {
        this.egressSchedulerPolicy = value;
    }

    /**
     * Gets the value of the networkQueuePolicy property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkQueuePolicy() {
        return networkQueuePolicy;
    }

    /**
     * Sets the value of the networkQueuePolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkQueuePolicy(String value) {
        this.networkQueuePolicy = value;
    }

    /**
     * Gets the value of the rsvpHelloInterval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRSVPHelloInterval() {
        return rsvpHelloInterval;
    }

    /**
     * Sets the value of the rsvpHelloInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRSVPHelloInterval(String value) {
        this.rsvpHelloInterval = value;
    }

}
