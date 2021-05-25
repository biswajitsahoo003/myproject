
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Protocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Protocol">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="webAccess" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="telnet" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="snmp" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="V1"/>
 *               &lt;enumeration value="V3"/>
 *               &lt;enumeration value="V1andV3"/>
 *               &lt;enumeration value="NA"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
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
@XmlType(name = "Protocol", namespace = "http://test-rad/com/tcl/www/_2014/_2/ipsvc/xsd", propOrder = {
    "webAccess",
    "telnet",
    "snmp"
})
public class Protocol {

    protected String webAccess;
    protected String telnet;
    protected String snmp;

    /**
     * Gets the value of the webAccess property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */


    /**
     * Gets the value of the telnet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */


    /**
     * Gets the value of the snmp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSnmp() {
        return snmp;
    }

    public String getWebAccess() {
		return webAccess;
	}

	public void setWebAccess(String webAccess) {
		this.webAccess = webAccess;
	}

	public String getTelnet() {
		return telnet;
	}

	public void setTelnet(String telnet) {
		this.telnet = telnet;
	}

	/**
     * Sets the value of the snmp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSnmp(String value) {
        this.snmp = value;
    }

}
