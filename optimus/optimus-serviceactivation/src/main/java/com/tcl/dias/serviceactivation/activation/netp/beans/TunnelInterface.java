
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TunnelInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TunnelInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}Interface">
 *       &lt;sequence>
 *         &lt;element name="authenticationUserName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="authenticationPassword" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="networkID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tunnelSource" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="tunnelDestination" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="ipMTU" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
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
@XmlType(name = "TunnelInterface", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "authenticationUserName",
    "authenticationPassword",
    "networkID",
    "tunnelSource",
    "tunnelDestination",
    "ipMTU",
    "bfdConfig"
})
public class TunnelInterface
    extends Interface
{

    protected String authenticationUserName;
    protected String authenticationPassword;
    protected String networkID;
    protected String tunnelSource;
    protected IPV4Address tunnelDestination;
    protected Integer ipMTU;
    protected BFDConfig bfdConfig;

    /**
     * Gets the value of the authenticationUserName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationUserName() {
        return authenticationUserName;
    }

    /**
     * Sets the value of the authenticationUserName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationUserName(String value) {
        this.authenticationUserName = value;
    }

    /**
     * Gets the value of the authenticationPassword property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuthenticationPassword() {
        return authenticationPassword;
    }

    /**
     * Sets the value of the authenticationPassword property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuthenticationPassword(String value) {
        this.authenticationPassword = value;
    }

    /**
     * Gets the value of the networkID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkID() {
        return networkID;
    }

    /**
     * Sets the value of the networkID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkID(String value) {
        this.networkID = value;
    }

    /**
     * Gets the value of the tunnelSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTunnelSource() {
        return tunnelSource;
    }

    /**
     * Sets the value of the tunnelSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTunnelSource(String value) {
        this.tunnelSource = value;
    }

    /**
     * Gets the value of the tunnelDestination property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getTunnelDestination() {
        return tunnelDestination;
    }

    /**
     * Sets the value of the tunnelDestination property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setTunnelDestination(IPV4Address value) {
        this.tunnelDestination = value;
    }

    /**
     * Gets the value of the ipMTU property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getIpMTU() {
        return ipMTU;
    }

    /**
     * Sets the value of the ipMTU property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setIpMTU(Integer value) {
        this.ipMTU = value;
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
