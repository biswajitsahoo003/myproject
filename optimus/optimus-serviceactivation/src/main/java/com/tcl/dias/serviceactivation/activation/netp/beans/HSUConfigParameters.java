
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HSUConfigParameters complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HSUConfigParameters">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="siteContact" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerLocation" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="frequency" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sectorID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="latitude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="longitude" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hsuMgmtIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPAddress" minOccurs="0"/>
 *         &lt;element name="hsuMgmtSubnet" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPAddress" minOccurs="0"/>
 *         &lt;element name="hsuGatewayIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPAddress" minOccurs="0"/>
 *         &lt;element name="hsuMacAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="managementVlanID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ntpServerIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPAddress" minOccurs="0"/>
 *         &lt;element name="ntpOffset" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="protocol" type="{http://test-rad/com/tcl/www/_2014/_2/ipsvc/xsd}Protocol" minOccurs="0"/>
 *         &lt;element name="channelBandwidth" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="requiredTxPower" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="ethernetPortConfiguration" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="Auto"/>
 *               &lt;enumeration value="10Mbps/Half Duplex"/>
 *               &lt;enumeration value="10 Mbps/Full Duplex"/>
 *               &lt;enumeration value="100 Mbps/Half Duplex"/>
 *               &lt;enumeration value="100 Mbps/Full Duplex"/>
 *               &lt;enumeration value="Disable"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="field1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HSUConfigParameters", namespace = "http://test-rad/com/tcl/www/_2014/_2/ipsvc/xsd", propOrder = {
    "name",
    "siteContact",
    "customerLocation",
    "frequency",
    "sectorID",
    "latitude",
    "longitude",
    "hsuMgmtIP",
    "hsuMgmtSubnet",
    "hsuGatewayIP",
    "hsuMacAddress",
    "managementVlanID",
    "ntpServerIP",
    "ntpOffset",
    "protocol",
    "channelBandwidth",
    "requiredTxPower",
    "ethernetPortConfiguration",
    "field1",
    "field2",
    "field3",
    "field4",
    "isObjectInstanceModified"
})
public class HSUConfigParameters {

    protected String name;
    protected String siteContact;
    protected String customerLocation;
    protected String frequency;
    protected String sectorID;
    protected String latitude;
    protected String longitude;
    protected IPAddress hsuMgmtIP;
    protected IPAddress hsuMgmtSubnet;
    protected IPAddress hsuGatewayIP;
    protected String hsuMacAddress;
    protected Integer managementVlanID;
    protected IPAddress ntpServerIP;
    protected Integer ntpOffset;
    protected Protocol protocol;
    protected Integer channelBandwidth;
    protected Integer requiredTxPower;
    protected String ethernetPortConfiguration;
    protected String field1;
    protected String field2;
    protected String field3;
    protected String field4;
    protected Boolean isObjectInstanceModified;

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
     * Gets the value of the siteContact property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiteContact() {
        return siteContact;
    }

    /**
     * Sets the value of the siteContact property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiteContact(String value) {
        this.siteContact = value;
    }

    /**
     * Gets the value of the customerLocation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerLocation() {
        return customerLocation;
    }

    /**
     * Sets the value of the customerLocation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerLocation(String value) {
        this.customerLocation = value;
    }

    /**
     * Gets the value of the frequency property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFrequency() {
        return frequency;
    }

    /**
     * Sets the value of the frequency property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFrequency(String value) {
        this.frequency = value;
    }

    /**
     * Gets the value of the sectorID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSectorID() {
        return sectorID;
    }

    /**
     * Sets the value of the sectorID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSectorID(String value) {
        this.sectorID = value;
    }

    /**
     * Gets the value of the latitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLatitude() {
        return latitude;
    }

    /**
     * Sets the value of the latitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLatitude(String value) {
        this.latitude = value;
    }

    /**
     * Gets the value of the longitude property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLongitude() {
        return longitude;
    }

    /**
     * Sets the value of the longitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLongitude(String value) {
        this.longitude = value;
    }

    /**
     * Gets the value of the hsuMgmtIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPAddress }
     *     
     */
    public IPAddress getHsuMgmtIP() {
        return hsuMgmtIP;
    }

    /**
     * Sets the value of the hsuMgmtIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPAddress }
     *     
     */
    public void setHsuMgmtIP(IPAddress value) {
        this.hsuMgmtIP = value;
    }

    /**
     * Gets the value of the hsuMgmtSubnet property.
     * 
     * @return
     *     possible object is
     *     {@link IPAddress }
     *     
     */
    public IPAddress getHsuMgmtSubnet() {
        return hsuMgmtSubnet;
    }

    /**
     * Sets the value of the hsuMgmtSubnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPAddress }
     *     
     */
    public void setHsuMgmtSubnet(IPAddress value) {
        this.hsuMgmtSubnet = value;
    }

    /**
     * Gets the value of the hsuGatewayIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPAddress }
     *     
     */
    public IPAddress getHsuGatewayIP() {
        return hsuGatewayIP;
    }

    /**
     * Sets the value of the hsuGatewayIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPAddress }
     *     
     */
    public void setHsuGatewayIP(IPAddress value) {
        this.hsuGatewayIP = value;
    }

    /**
     * Gets the value of the hsuMacAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHsuMacAddress() {
        return hsuMacAddress;
    }

    /**
     * Sets the value of the hsuMacAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHsuMacAddress(String value) {
        this.hsuMacAddress = value;
    }

    /**
     * Gets the value of the managementVlanID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getManagementVlanID() {
        return managementVlanID;
    }

    /**
     * Sets the value of the managementVlanID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setManagementVlanID(Integer value) {
        this.managementVlanID = value;
    }

    /**
     * Gets the value of the ntpServerIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPAddress }
     *     
     */
    public IPAddress getNtpServerIP() {
        return ntpServerIP;
    }

    /**
     * Sets the value of the ntpServerIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPAddress }
     *     
     */
    public void setNtpServerIP(IPAddress value) {
        this.ntpServerIP = value;
    }

    /**
     * Gets the value of the ntpOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getNtpOffset() {
        return ntpOffset;
    }

    /**
     * Sets the value of the ntpOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setNtpOffset(Integer value) {
        this.ntpOffset = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link Protocol }
     *     
     */
    public Protocol getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link Protocol }
     *     
     */
    public void setProtocol(Protocol value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the channelBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getChannelBandwidth() {
        return channelBandwidth;
    }

    /**
     * Sets the value of the channelBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setChannelBandwidth(Integer value) {
        this.channelBandwidth = value;
    }

    /**
     * Gets the value of the requiredTxPower property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRequiredTxPower() {
        return requiredTxPower;
    }

    /**
     * Sets the value of the requiredTxPower property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRequiredTxPower(Integer value) {
        this.requiredTxPower = value;
    }

    /**
     * Gets the value of the ethernetPortConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEthernetPortConfiguration() {
        return ethernetPortConfiguration;
    }

    /**
     * Sets the value of the ethernetPortConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEthernetPortConfiguration(String value) {
        this.ethernetPortConfiguration = value;
    }

    /**
     * Gets the value of the field1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField1() {
        return field1;
    }

    /**
     * Sets the value of the field1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField1(String value) {
        this.field1 = value;
    }

    /**
     * Gets the value of the field2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField2() {
        return field2;
    }

    /**
     * Sets the value of the field2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField2(String value) {
        this.field2 = value;
    }

    /**
     * Gets the value of the field3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField3() {
        return field3;
    }

    /**
     * Sets the value of the field3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField3(String value) {
        this.field3 = value;
    }

    /**
     * Gets the value of the field4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField4() {
        return field4;
    }

    /**
     * Sets the value of the field4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField4(String value) {
        this.field4 = value;
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

}
