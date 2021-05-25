
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUBackboneSerialInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUBackboneSerialInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneRouterInterface">
 *       &lt;sequence>
 *         &lt;element name="TDMMode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mtu" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="NETWORK"/>
 *               &lt;enumeration value="ACCESS"/>
 *               &lt;enumeration value="HYBRID"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="encapsulationType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="PPP"/>
 *               &lt;enumeration value="HDLC"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="HDLCConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}HDLCConfig" minOccurs="0"/>
 *         &lt;element name="portType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="channelGroupNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timeslot" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="field1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ALUBackboneSerialInterface", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "tdmMode",
    "mtu",
    "mode",
    "encapsulationType",
    "hdlcConfig",
    "portType",
    "channelGroupNumber",
    "timeslot",
    "field1"
})
public class ALUBackboneSerialInterface
    extends ALUBackboneRouterInterface
{

    @XmlElement(name = "TDMMode")
    protected String tdmMode;
    protected String mtu;
    protected String mode;
    protected String encapsulationType;
    @XmlElement(name = "HDLCConfig")
    protected HDLCConfig hdlcConfig;
    protected String portType;
    protected String channelGroupNumber;
    protected List<String> timeslot;
    protected String field1;

    /**
     * Gets the value of the tdmMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTDMMode() {
        return tdmMode;
    }

    /**
     * Sets the value of the tdmMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTDMMode(String value) {
        this.tdmMode = value;
    }

    /**
     * Gets the value of the mtu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMtu() {
        return mtu;
    }

    /**
     * Sets the value of the mtu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMtu(String value) {
        this.mtu = value;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMode(String value) {
        this.mode = value;
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
     * Gets the value of the hdlcConfig property.
     * 
     * @return
     *     possible object is
     *     {@link HDLCConfig }
     *     
     */
    public HDLCConfig getHDLCConfig() {
        return hdlcConfig;
    }

    /**
     * Sets the value of the hdlcConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link HDLCConfig }
     *     
     */
    public void setHDLCConfig(HDLCConfig value) {
        this.hdlcConfig = value;
    }

    /**
     * Gets the value of the portType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortType() {
        return portType;
    }

    /**
     * Sets the value of the portType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortType(String value) {
        this.portType = value;
    }

    /**
     * Gets the value of the channelGroupNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelGroupNumber() {
        return channelGroupNumber;
    }

    /**
     * Sets the value of the channelGroupNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelGroupNumber(String value) {
        this.channelGroupNumber = value;
    }

    /**
     * Gets the value of the timeslot property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeslot property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeslot().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTimeslot() {
        if (timeslot == null) {
            timeslot = new ArrayList<String>();
        }
        return this.timeslot;
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

}
