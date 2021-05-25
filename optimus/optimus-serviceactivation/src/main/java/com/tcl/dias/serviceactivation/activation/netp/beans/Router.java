
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Router complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Router">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="type" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="role" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="APS_SECONDARY"/>
 *               &lt;enumeration value="PE"/>
 *               &lt;enumeration value="LNS"/>
 *               &lt;enumeration value="IRS"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="v4ManagementIPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="v6ManagementIPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" minOccurs="0"/>
 *         &lt;element name="wanInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}WANInterface" minOccurs="0"/>
 *         &lt;element name="topologyInfo" type="{http://www.tcl.com/2011/11/ipsvc/xsd}TopologyInfo" minOccurs="0"/>
 *         &lt;element name="routerTopologyInterface1" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EthernetInterface" minOccurs="0"/>
 *         &lt;element name="routerTopologyInterface2" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EthernetInterface" minOccurs="0"/>
 *         &lt;element name="managementLoopbackInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}LoopbackInterface" minOccurs="0"/>
 *         &lt;element name="isRouterBFDEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "Router", propOrder = {
    "hostName",
    "type",
    "role",
    "v4ManagementIPAddress",
    "v6ManagementIPAddress",
    "wanInterface",
    "topologyInfo",
    "routerTopologyInterface1",
    "routerTopologyInterface2",
    "managementLoopbackInterface",
    "isRouterBFDEnabled",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified"
})
public class Router {

    protected String hostName;
    protected String type;
    protected String role;
    protected IPV4Address v4ManagementIPAddress;
    protected IPV6Address v6ManagementIPAddress;
    protected WANInterface wanInterface;
    protected TopologyInfo topologyInfo;
    protected EthernetInterface routerTopologyInterface1;
    protected EthernetInterface routerTopologyInterface2;
    protected LoopbackInterface managementLoopbackInterface;
    protected Boolean isRouterBFDEnabled;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    /**
     * Gets the value of the role property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRole() {
        return role;
    }

    /**
     * Sets the value of the role property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRole(String value) {
        this.role = value;
    }

    /**
     * Gets the value of the v4ManagementIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getV4ManagementIPAddress() {
        return v4ManagementIPAddress;
    }

    /**
     * Sets the value of the v4ManagementIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setV4ManagementIPAddress(IPV4Address value) {
        this.v4ManagementIPAddress = value;
    }

    /**
     * Gets the value of the v6ManagementIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV6Address }
     *     
     */
    public IPV6Address getV6ManagementIPAddress() {
        return v6ManagementIPAddress;
    }

    /**
     * Sets the value of the v6ManagementIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV6Address }
     *     
     */
    public void setV6ManagementIPAddress(IPV6Address value) {
        this.v6ManagementIPAddress = value;
    }

    /**
     * Gets the value of the wanInterface property.
     * 
     * @return
     *     possible object is
     *     {@link WANInterface }
     *     
     */
    public WANInterface getWanInterface() {
        return wanInterface;
    }

    /**
     * Sets the value of the wanInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link WANInterface }
     *     
     */
    public void setWanInterface(WANInterface value) {
        this.wanInterface = value;
    }

    /**
     * Gets the value of the topologyInfo property.
     * 
     * @return
     *     possible object is
     *     {@link TopologyInfo }
     *     
     */
    public TopologyInfo getTopologyInfo() {
        return topologyInfo;
    }

    /**
     * Sets the value of the topologyInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link TopologyInfo }
     *     
     */
    public void setTopologyInfo(TopologyInfo value) {
        this.topologyInfo = value;
    }

    /**
     * Gets the value of the routerTopologyInterface1 property.
     * 
     * @return
     *     possible object is
     *     {@link EthernetInterface }
     *     
     */
    public EthernetInterface getRouterTopologyInterface1() {
        return routerTopologyInterface1;
    }

    /**
     * Sets the value of the routerTopologyInterface1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link EthernetInterface }
     *     
     */
    public void setRouterTopologyInterface1(EthernetInterface value) {
        this.routerTopologyInterface1 = value;
    }

    /**
     * Gets the value of the routerTopologyInterface2 property.
     * 
     * @return
     *     possible object is
     *     {@link EthernetInterface }
     *     
     */
    public EthernetInterface getRouterTopologyInterface2() {
        return routerTopologyInterface2;
    }

    /**
     * Sets the value of the routerTopologyInterface2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link EthernetInterface }
     *     
     */
    public void setRouterTopologyInterface2(EthernetInterface value) {
        this.routerTopologyInterface2 = value;
    }

    /**
     * Gets the value of the managementLoopbackInterface property.
     * 
     * @return
     *     possible object is
     *     {@link LoopbackInterface }
     *     
     */
    public LoopbackInterface getManagementLoopbackInterface() {
        return managementLoopbackInterface;
    }

    /**
     * Sets the value of the managementLoopbackInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoopbackInterface }
     *     
     */
    public void setManagementLoopbackInterface(LoopbackInterface value) {
        this.managementLoopbackInterface = value;
    }

    /**
     * Gets the value of the isRouterBFDEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsRouterBFDEnabled() {
        return isRouterBFDEnabled;
    }

    /**
     * Sets the value of the isRouterBFDEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsRouterBFDEnabled(Boolean value) {
        this.isRouterBFDEnabled = value;
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
