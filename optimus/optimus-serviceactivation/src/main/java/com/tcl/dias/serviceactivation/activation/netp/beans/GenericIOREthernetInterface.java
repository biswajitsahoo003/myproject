
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for genericIOREthernetInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="genericIOREthernetInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="v4IPAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vlan" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="enableUnicastSourceReachable" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ipHelperAddress1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ipHelperAddress2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enableIPDHCPRelayInfo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="enableIPRouteCache" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="arpTimeout" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="inboundQoSPolicyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="outboundQoSPolicyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "genericIOREthernetInterface", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_02", propOrder = {
    "name",
    "v4IPAddress",
    "description",
    "vlan",
    "enableUnicastSourceReachable",
    "ipHelperAddress1",
    "ipHelperAddress2",
    "enableIPDHCPRelayInfo",
    "enableIPRouteCache",
    "arpTimeout",
    "inboundQoSPolicyName",
    "outboundQoSPolicyName"
})
public class GenericIOREthernetInterface {

    protected String name;
    protected String v4IPAddress;
    protected String description;
    protected Integer vlan;
    protected String enableUnicastSourceReachable;
    protected String ipHelperAddress1;
    protected String ipHelperAddress2;
    protected String enableIPDHCPRelayInfo;
    protected String enableIPRouteCache;
    protected Integer arpTimeout;
    protected String inboundQoSPolicyName;
    protected String outboundQoSPolicyName;

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
     * Gets the value of the v4IPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getV4IPAddress() {
        return v4IPAddress;
    }

    /**
     * Sets the value of the v4IPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setV4IPAddress(String value) {
        this.v4IPAddress = value;
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
     * Gets the value of the vlan property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVlan() {
        return vlan;
    }

    /**
     * Sets the value of the vlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVlan(Integer value) {
        this.vlan = value;
    }

    /**
     * Gets the value of the enableUnicastSourceReachable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableUnicastSourceReachable() {
        return enableUnicastSourceReachable;
    }

    /**
     * Sets the value of the enableUnicastSourceReachable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableUnicastSourceReachable(String value) {
        this.enableUnicastSourceReachable = value;
    }

    /**
     * Gets the value of the ipHelperAddress1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpHelperAddress1() {
        return ipHelperAddress1;
    }

    /**
     * Sets the value of the ipHelperAddress1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpHelperAddress1(String value) {
        this.ipHelperAddress1 = value;
    }

    /**
     * Gets the value of the ipHelperAddress2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIpHelperAddress2() {
        return ipHelperAddress2;
    }

    /**
     * Sets the value of the ipHelperAddress2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIpHelperAddress2(String value) {
        this.ipHelperAddress2 = value;
    }

    /**
     * Gets the value of the enableIPDHCPRelayInfo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableIPDHCPRelayInfo() {
        return enableIPDHCPRelayInfo;
    }

    /**
     * Sets the value of the enableIPDHCPRelayInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableIPDHCPRelayInfo(String value) {
        this.enableIPDHCPRelayInfo = value;
    }

    /**
     * Gets the value of the enableIPRouteCache property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEnableIPRouteCache() {
        return enableIPRouteCache;
    }

    /**
     * Sets the value of the enableIPRouteCache property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEnableIPRouteCache(String value) {
        this.enableIPRouteCache = value;
    }

    /**
     * Gets the value of the arpTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getArpTimeout() {
        return arpTimeout;
    }

    /**
     * Sets the value of the arpTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setArpTimeout(Integer value) {
        this.arpTimeout = value;
    }

    /**
     * Gets the value of the inboundQoSPolicyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInboundQoSPolicyName() {
        return inboundQoSPolicyName;
    }

    /**
     * Sets the value of the inboundQoSPolicyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInboundQoSPolicyName(String value) {
        this.inboundQoSPolicyName = value;
    }

    /**
     * Gets the value of the outboundQoSPolicyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOutboundQoSPolicyName() {
        return outboundQoSPolicyName;
    }

    /**
     * Sets the value of the outboundQoSPolicyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOutboundQoSPolicyName(String value) {
        this.outboundQoSPolicyName = value;
    }

}
