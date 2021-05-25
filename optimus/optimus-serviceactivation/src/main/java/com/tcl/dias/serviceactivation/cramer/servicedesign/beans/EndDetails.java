
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for endDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="endDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="CITY" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="INTERFACE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ISMODIFIED" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="NODEID_W" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="NODE_W" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ONNET" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="Topology" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "endDetails", propOrder = {
    "city",
    "_interface",
    "ismodified",
    "nodeidw",
    "nodew",
    "onnet",
    "topology"
})
public class EndDetails {

    @XmlElement(name = "CITY")
    protected String city;
    @XmlElement(name = "INTERFACE")
    protected String _interface;
    @XmlElement(name = "ISMODIFIED")
    protected boolean ismodified;
    @XmlElement(name = "NODEID_W")
    protected long nodeidw;
    @XmlElement(name = "NODE_W")
    protected String nodew;
    @XmlElement(name = "ONNET")
    protected Boolean onnet;
    @XmlElement(name = "Topology")
    protected String topology;

    /**
     * Gets the value of the city property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCITY() {
        return city;
    }

    /**
     * Sets the value of the city property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCITY(String value) {
        this.city = value;
    }

    /**
     * Gets the value of the interface property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINTERFACE() {
        return _interface;
    }

    /**
     * Sets the value of the interface property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINTERFACE(String value) {
        this._interface = value;
    }

    /**
     * Gets the value of the ismodified property.
     * 
     */
    public boolean isISMODIFIED() {
        return ismodified;
    }

    /**
     * Sets the value of the ismodified property.
     * 
     */
    public void setISMODIFIED(boolean value) {
        this.ismodified = value;
    }

    /**
     * Gets the value of the nodeidw property.
     * 
     */
    public long getNODEIDW() {
        return nodeidw;
    }

    /**
     * Sets the value of the nodeidw property.
     * 
     */
    public void setNODEIDW(long value) {
        this.nodeidw = value;
    }

    /**
     * Gets the value of the nodew property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNODEW() {
        return nodew;
    }

    /**
     * Sets the value of the nodew property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNODEW(String value) {
        this.nodew = value;
    }

    /**
     * Gets the value of the onnet property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isONNET() {
        return onnet;
    }

    /**
     * Sets the value of the onnet property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setONNET(Boolean value) {
        this.onnet = value;
    }

    /**
     * Gets the value of the topology property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopology() {
        return topology;
    }

    /**
     * Sets the value of the topology property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopology(String value) {
        this.topology = value;
    }

}
