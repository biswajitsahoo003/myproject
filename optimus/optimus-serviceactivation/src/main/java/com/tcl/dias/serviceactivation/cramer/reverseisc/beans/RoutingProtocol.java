
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Routing_Protocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Routing_Protocol"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DISTRIBUTION_LAN_VR_BGP_AS_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="INTERNET_WAN_VR_BGP_AS_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LAN_VR_BGP_AS_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MPLS_WAN_VR_BGP_AS_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ROUTING_PROTOCOL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Routing_Protocol", propOrder = {
    "distributionlanvrbgpasnumber",
    "internetwanvrbgpasnumber",
    "lanvrbgpasnumber",
    "mplswanvrbgpasnumber",
    "routingprotocol"
})
public class RoutingProtocol {

    @XmlElement(name = "DISTRIBUTION_LAN_VR_BGP_AS_NUMBER")
    protected String distributionlanvrbgpasnumber;
    @XmlElement(name = "INTERNET_WAN_VR_BGP_AS_NUMBER")
    protected String internetwanvrbgpasnumber;
    @XmlElement(name = "LAN_VR_BGP_AS_NUMBER")
    protected String lanvrbgpasnumber;
    @XmlElement(name = "MPLS_WAN_VR_BGP_AS_NUMBER")
    protected String mplswanvrbgpasnumber;
    @XmlElement(name = "ROUTING_PROTOCOL")
    protected String routingprotocol;

    /**
     * Gets the value of the distributionlanvrbgpasnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDISTRIBUTIONLANVRBGPASNUMBER() {
        return distributionlanvrbgpasnumber;
    }

    /**
     * Sets the value of the distributionlanvrbgpasnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDISTRIBUTIONLANVRBGPASNUMBER(String value) {
        this.distributionlanvrbgpasnumber = value;
    }

    /**
     * Gets the value of the internetwanvrbgpasnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINTERNETWANVRBGPASNUMBER() {
        return internetwanvrbgpasnumber;
    }

    /**
     * Sets the value of the internetwanvrbgpasnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINTERNETWANVRBGPASNUMBER(String value) {
        this.internetwanvrbgpasnumber = value;
    }

    /**
     * Gets the value of the lanvrbgpasnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLANVRBGPASNUMBER() {
        return lanvrbgpasnumber;
    }

    /**
     * Sets the value of the lanvrbgpasnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLANVRBGPASNUMBER(String value) {
        this.lanvrbgpasnumber = value;
    }

    /**
     * Gets the value of the mplswanvrbgpasnumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMPLSWANVRBGPASNUMBER() {
        return mplswanvrbgpasnumber;
    }

    /**
     * Sets the value of the mplswanvrbgpasnumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMPLSWANVRBGPASNUMBER(String value) {
        this.mplswanvrbgpasnumber = value;
    }

    /**
     * Gets the value of the routingprotocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROUTINGPROTOCOL() {
        return routingprotocol;
    }

    /**
     * Sets the value of the routingprotocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROUTINGPROTOCOL(String value) {
        this.routingprotocol = value;
    }

}
