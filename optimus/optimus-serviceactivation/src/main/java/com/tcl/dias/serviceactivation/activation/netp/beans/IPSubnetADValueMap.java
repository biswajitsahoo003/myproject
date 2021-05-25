
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				This will contain mapping between LAN IP addresses and
 * 				AD values.
 * 			
 * 
 * <p>Java class for IPSubnetADValueMap complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPSubnetADValueMap">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ipSubnet" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
 *                   &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="adValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nextHopIp" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="checkCPEAddress" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="checkCPEAddressInterval" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dropCountValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isGlobal" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPSubnetADValueMap", propOrder = {
    "ipSubnet",
    "adValue",
    "nextHopIp",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "checkCPEAddress",
    "checkCPEAddressInterval",
    "dropCountValue",
    "field1",
    "description",
    "isGlobal"
})
public class IPSubnetADValueMap {

    protected IPSubnetADValueMap.IpSubnet ipSubnet;
    protected String adValue;
    protected String nextHopIp;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    protected String checkCPEAddress;
    protected String checkCPEAddressInterval;
    protected String dropCountValue;
    protected String field1;
    protected String description;
    protected Boolean isGlobal;

    /**
     * Gets the value of the ipSubnet property.
     * 
     * @return
     *     possible object is
     *     {@link IPSubnetADValueMap.IpSubnet }
     *     
     */
    public IPSubnetADValueMap.IpSubnet getIpSubnet() {
        return ipSubnet;
    }

    /**
     * Sets the value of the ipSubnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPSubnetADValueMap.IpSubnet }
     *     
     */
    public void setIpSubnet(IPSubnetADValueMap.IpSubnet value) {
        this.ipSubnet = value;
    }

    /**
     * Gets the value of the adValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdValue() {
        return adValue;
    }

    /**
     * Sets the value of the adValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdValue(String value) {
        this.adValue = value;
    }

    /**
     * Gets the value of the nextHopIp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNextHopIp() {
        return nextHopIp;
    }

    /**
     * Sets the value of the nextHopIp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNextHopIp(String value) {
        this.nextHopIp = value;
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
     * Gets the value of the checkCPEAddress property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckCPEAddress() {
        return checkCPEAddress;
    }

    /**
     * Sets the value of the checkCPEAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckCPEAddress(String value) {
        this.checkCPEAddress = value;
    }

    /**
     * Gets the value of the checkCPEAddressInterval property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCheckCPEAddressInterval() {
        return checkCPEAddressInterval;
    }

    /**
     * Sets the value of the checkCPEAddressInterval property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCheckCPEAddressInterval(String value) {
        this.checkCPEAddressInterval = value;
    }

    /**
     * Gets the value of the dropCountValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDropCountValue() {
        return dropCountValue;
    }

    /**
     * Sets the value of the dropCountValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDropCountValue(String value) {
        this.dropCountValue = value;
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
     * Gets the value of the isGlobal property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsGlobal() {
        return isGlobal;
    }

    /**
     * Sets the value of the isGlobal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsGlobal(Boolean value) {
        this.isGlobal = value;
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
     *         &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
     *         &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
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
        "v4Address",
        "v6Address"
    })
    public static class IpSubnet {

        protected IPV4Address v4Address;
        protected IPV6Address v6Address;

        /**
         * Gets the value of the v4Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV4Address }
         *     
         */
        public IPV4Address getV4Address() {
            return v4Address;
        }

        /**
         * Sets the value of the v4Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV4Address }
         *     
         */
        public void setV4Address(IPV4Address value) {
            this.v4Address = value;
        }

        /**
         * Gets the value of the v6Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV6Address }
         *     
         */
        public IPV6Address getV6Address() {
            return v6Address;
        }

        /**
         * Sets the value of the v6Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV6Address }
         *     
         */
        public void setV6Address(IPV6Address value) {
            this.v6Address = value;
        }

    }

}
