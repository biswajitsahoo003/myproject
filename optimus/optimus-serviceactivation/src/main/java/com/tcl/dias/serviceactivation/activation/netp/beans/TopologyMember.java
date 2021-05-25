
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TopologyMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TopologyMember">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="aEndHostIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0" form="unqualified"/>
 *         &lt;element name="aEndHostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="aEndPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="zEndHostIP" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0" form="unqualified"/>
 *         &lt;element name="zEndHostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="zEndPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0" form="unqualified"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TopologyMember", namespace = "http://www.tcl.com/2011/11/ace/common/xsd", propOrder = {
    "aEndHostIP",
    "aEndHostName",
    "aEndPortName",
    "zEndHostIP",
    "zEndHostName",
    "zEndPortName",
    "isObjectInstanceModified"
})
public class TopologyMember {

    protected IPV4Address aEndHostIP;
    protected String aEndHostName;
    protected String aEndPortName;
    protected IPV4Address zEndHostIP;
    protected String zEndHostName;
    protected String zEndPortName;
    protected String isObjectInstanceModified;

    /**
     * Gets the value of the aEndHostIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getAEndHostIP() {
        return aEndHostIP;
    }

    /**
     * Sets the value of the aEndHostIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setAEndHostIP(IPV4Address value) {
        this.aEndHostIP = value;
    }

    /**
     * Gets the value of the aEndHostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndHostName() {
        return aEndHostName;
    }

    /**
     * Sets the value of the aEndHostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndHostName(String value) {
        this.aEndHostName = value;
    }

    /**
     * Gets the value of the aEndPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndPortName() {
        return aEndPortName;
    }

    /**
     * Sets the value of the aEndPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndPortName(String value) {
        this.aEndPortName = value;
    }

    /**
     * Gets the value of the zEndHostIP property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getZEndHostIP() {
        return zEndHostIP;
    }

    /**
     * Sets the value of the zEndHostIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setZEndHostIP(IPV4Address value) {
        this.zEndHostIP = value;
    }

    /**
     * Gets the value of the zEndHostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndHostName() {
        return zEndHostName;
    }

    /**
     * Sets the value of the zEndHostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndHostName(String value) {
        this.zEndHostName = value;
    }

    /**
     * Gets the value of the zEndPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndPortName() {
        return zEndPortName;
    }

    /**
     * Sets the value of the zEndPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndPortName(String value) {
        this.zEndPortName = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsObjectInstanceModified(String value) {
        this.isObjectInstanceModified = value;
    }

}
