
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUBackboneEthernetInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUBackboneEthernetInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneRouterInterface">
 *       &lt;sequence>
 *         &lt;element name="crcMonitor" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CRCMonitor" minOccurs="0"/>
 *         &lt;element name="encapsulationType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="QinQ"/>
 *               &lt;enumeration value="DOT1Q"/>
 *               &lt;enumeration value="NULL"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="HoldTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EOAMConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}EOAMConfig" minOccurs="0"/>
 *         &lt;element name="svlan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asmode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="NETWORK"/>
 *               &lt;enumeration value="HYBRID"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ALUBackboneEthernetInterface", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "crcMonitor",
    "encapsulationType",
    "holdTime",
    "eoamConfig",
    "svlan",
    "asmode"
})
public class ALUBackboneEthernetInterface
    extends ALUBackboneRouterInterface
{

    protected CRCMonitor crcMonitor;
    protected String encapsulationType;
    @XmlElement(name = "HoldTime")
    protected String holdTime;
    @XmlElement(name = "EOAMConfig")
    protected EOAMConfig eoamConfig;
    protected String svlan;
    protected String asmode;

    /**
     * Gets the value of the crcMonitor property.
     * 
     * @return
     *     possible object is
     *     {@link CRCMonitor }
     *     
     */
    public CRCMonitor getCrcMonitor() {
        return crcMonitor;
    }

    /**
     * Sets the value of the crcMonitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRCMonitor }
     *     
     */
    public void setCrcMonitor(CRCMonitor value) {
        this.crcMonitor = value;
    }

    /**
     * Gets the value of the encapsulationType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncapsulationType() {
        return encapsulationType;
    }

    /**
     * Sets the value of the encapsulationType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncapsulationType(String value) {
        this.encapsulationType = value;
    }

    /**
     * Gets the value of the holdTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHoldTime() {
        return holdTime;
    }

    /**
     * Sets the value of the holdTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHoldTime(String value) {
        this.holdTime = value;
    }

    /**
     * Gets the value of the eoamConfig property.
     * 
     * @return
     *     possible object is
     *     {@link EOAMConfig }
     *     
     */
    public EOAMConfig getEOAMConfig() {
        return eoamConfig;
    }

    /**
     * Sets the value of the eoamConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link EOAMConfig }
     *     
     */
    public void setEOAMConfig(EOAMConfig value) {
        this.eoamConfig = value;
    }

    /**
     * Gets the value of the svlan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSvlan() {
        return svlan;
    }

    /**
     * Sets the value of the svlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSvlan(String value) {
        this.svlan = value;
    }

    /**
     * Gets the value of the asmode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsmode() {
        return asmode;
    }

    /**
     * Sets the value of the asmode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsmode(String value) {
        this.asmode = value;
    }

}
