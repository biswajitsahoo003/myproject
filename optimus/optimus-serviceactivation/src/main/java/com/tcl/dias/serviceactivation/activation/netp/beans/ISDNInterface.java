
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				This interface is available for both CPE and PE. However
 * 				Netp considers only teh CPE interface.
 * 			
 * 
 * <p>Java class for ISDNInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ISDNInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}Interface">
 *       &lt;sequence>
 *         &lt;element name="username" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="responsibility" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="accessNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerISDNNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ISDNPortType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PRI_PORT"/>
 *               &lt;enumeration value="BRI_PORT"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MeritRadiusServerIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="OpenRadiusServerIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="RadiusServer3IP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ISDNVRFloopback" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bfdConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}BFDConfig" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ISDNInterface", propOrder = {
    "username",
    "password",
    "responsibility",
    "accessNumber",
    "customerISDNNumber",
    "isdnPortType",
    "meritRadiusServerIP",
    "openRadiusServerIP",
    "radiusServer3IP",
    "isdnvrFloopback",
    "bfdConfig"
})
public class ISDNInterface
    extends Interface
{

    protected String username;
    protected String password;
    protected String responsibility;
    protected String accessNumber;
    protected String customerISDNNumber;
    @XmlElement(name = "ISDNPortType")
    protected String isdnPortType;
    @XmlElement(name = "MeritRadiusServerIP")
    protected IPV4Address meritRadiusServerIP;
    @XmlElement(name = "OpenRadiusServerIP")
    protected IPV4Address openRadiusServerIP;
    @XmlElement(name = "RadiusServer3IP")
    protected String radiusServer3IP;
    @XmlElement(name = "ISDNVRFloopback")
    protected String isdnvrFloopback;
    protected BFDConfig bfdConfig;

    /**
     * Gets the value of the username property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the value of the username property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsername(String value) {
        this.username = value;
    }

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the responsibility property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResponsibility() {
        return responsibility;
    }

    /**
     * Sets the value of the responsibility property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResponsibility(String value) {
        this.responsibility = value;
    }

    /**
     * Gets the value of the accessNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAccessNumber() {
        return accessNumber;
    }

    /**
     * Sets the value of the accessNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAccessNumber(String value) {
        this.accessNumber = value;
    }

    /**
     * Gets the value of the customerISDNNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCustomerISDNNumber() {
        return customerISDNNumber;
    }

    /**
     * Sets the value of the customerISDNNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCustomerISDNNumber(String value) {
        this.customerISDNNumber = value;
    }

    /**
     * Gets the value of the isdnPortType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISDNPortType() {
        return isdnPortType;
    }

    /**
     * Sets the value of the isdnPortType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISDNPortType(String value) {
        this.isdnPortType = value;
    }

    /**
     * Gets the value of the meritRadiusServerIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getMeritRadiusServerIP() {
        return meritRadiusServerIP;
    }

    /**
     * Sets the value of the meritRadiusServerIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setMeritRadiusServerIP(IPV4Address value) {
        this.meritRadiusServerIP = value;
    }

    /**
     * Gets the value of the openRadiusServerIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getOpenRadiusServerIP() {
        return openRadiusServerIP;
    }

    /**
     * Sets the value of the openRadiusServerIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setOpenRadiusServerIP(IPV4Address value) {
        this.openRadiusServerIP = value;
    }

    /**
     * Gets the value of the radiusServer3IP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRadiusServer3IP() {
        return radiusServer3IP;
    }

    /**
     * Sets the value of the radiusServer3IP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRadiusServer3IP(String value) {
        this.radiusServer3IP = value;
    }

    /**
     * Gets the value of the isdnvrFloopback property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getISDNVRFloopback() {
        return isdnvrFloopback;
    }

    /**
     * Sets the value of the isdnvrFloopback property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setISDNVRFloopback(String value) {
        this.isdnvrFloopback = value;
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

}
