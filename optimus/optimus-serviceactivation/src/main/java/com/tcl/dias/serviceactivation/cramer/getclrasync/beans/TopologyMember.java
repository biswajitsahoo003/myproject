package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TopologyMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TopologyMember"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="aEndHostIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndHostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndHostIP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndHostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TopologyMember", propOrder = {
    "aEndHostIP",
    "aEndHostName",
    "aEndPortName",
    "zEndHostIP",
    "zEndHostName",
    "zEndPortName"
})
public class TopologyMember {

    protected String aEndHostIP;
    protected String aEndHostName;
    protected String aEndPortName;
    protected String zEndHostIP;
    protected String zEndHostName;
    protected String zEndPortName;

    /**
     * Gets the value of the aEndHostIP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndHostIP() {
        return aEndHostIP;
    }

    /**
     * Sets the value of the aEndHostIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndHostIP(String value) {
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
     *     {@link String }
     *     
     */
    public String getZEndHostIP() {
        return zEndHostIP;
    }

    /**
     * Sets the value of the zEndHostIP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndHostIP(String value) {
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

    @Override
    public String toString() {
        return "TopologyMember{" +
                "aEndHostIP='" + aEndHostIP + '\'' +
                ", aEndHostName='" + aEndHostName + '\'' +
                ", aEndPortName='" + aEndPortName + '\'' +
                ", zEndHostIP='" + zEndHostIP + '\'' +
                ", zEndHostName='" + zEndHostName + '\'' +
                ", zEndPortName='" + zEndPortName + '\'' +
                '}';
    }
}
