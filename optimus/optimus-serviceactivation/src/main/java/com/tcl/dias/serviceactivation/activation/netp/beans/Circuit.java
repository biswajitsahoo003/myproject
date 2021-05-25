
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Circuit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Circuit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aNodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aNodeActualName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aNodeNBIName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aEndNodeDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aEndNodeDefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aNodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="aPortNBIName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zNodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zNodeActualName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zNodeNBIName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zEndNodeDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zEndNodeDefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zNodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="zPortNBIName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="modifiedTimeStamp" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="circuitSpeed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Circuit", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "id",
    "name",
    "aNodeName",
    "aNodeActualName",
    "aNodeNBIName",
    "aEndNodeDef",
    "aEndNodeDefId",
    "aNodeType",
    "aPortName",
    "aPortNBIName",
    "zNodeName",
    "zNodeActualName",
    "zNodeNBIName",
    "zEndNodeDef",
    "zEndNodeDefId",
    "zNodeType",
    "zPortName",
    "zPortNBIName",
    "modifiedTimeStamp",
    "circuitSpeed"
})
public class Circuit {

    protected String id;
    protected String name;
    protected String aNodeName;
    protected String aNodeActualName;
    protected String aNodeNBIName;
    protected String aEndNodeDef;
    protected String aEndNodeDefId;
    protected String aNodeType;
    protected String aPortName;
    protected String aPortNBIName;
    protected String zNodeName;
    protected String zNodeActualName;
    protected String zNodeNBIName;
    protected String zEndNodeDef;
    protected String zEndNodeDefId;
    protected String zNodeType;
    protected String zPortName;
    protected String zPortNBIName;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar modifiedTimeStamp;
    protected String circuitSpeed;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the aNodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getANodeName() {
        return aNodeName;
    }

    /**
     * Sets the value of the aNodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setANodeName(String value) {
        this.aNodeName = value;
    }

    /**
     * Gets the value of the aNodeActualName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getANodeActualName() {
        return aNodeActualName;
    }

    /**
     * Sets the value of the aNodeActualName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setANodeActualName(String value) {
        this.aNodeActualName = value;
    }

    /**
     * Gets the value of the aNodeNBIName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getANodeNBIName() {
        return aNodeNBIName;
    }

    /**
     * Sets the value of the aNodeNBIName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setANodeNBIName(String value) {
        this.aNodeNBIName = value;
    }

    /**
     * Gets the value of the aEndNodeDef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndNodeDef() {
        return aEndNodeDef;
    }

    /**
     * Sets the value of the aEndNodeDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndNodeDef(String value) {
        this.aEndNodeDef = value;
    }

    /**
     * Gets the value of the aEndNodeDefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndNodeDefId() {
        return aEndNodeDefId;
    }

    /**
     * Sets the value of the aEndNodeDefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndNodeDefId(String value) {
        this.aEndNodeDefId = value;
    }

    /**
     * Gets the value of the aNodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getANodeType() {
        return aNodeType;
    }

    /**
     * Sets the value of the aNodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setANodeType(String value) {
        this.aNodeType = value;
    }

    /**
     * Gets the value of the aPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPortName() {
        return aPortName;
    }

    /**
     * Sets the value of the aPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPortName(String value) {
        this.aPortName = value;
    }

    /**
     * Gets the value of the aPortNBIName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPortNBIName() {
        return aPortNBIName;
    }

    /**
     * Sets the value of the aPortNBIName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPortNBIName(String value) {
        this.aPortNBIName = value;
    }

    /**
     * Gets the value of the zNodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZNodeName() {
        return zNodeName;
    }

    /**
     * Sets the value of the zNodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZNodeName(String value) {
        this.zNodeName = value;
    }

    /**
     * Gets the value of the zNodeActualName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZNodeActualName() {
        return zNodeActualName;
    }

    /**
     * Sets the value of the zNodeActualName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZNodeActualName(String value) {
        this.zNodeActualName = value;
    }

    /**
     * Gets the value of the zNodeNBIName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZNodeNBIName() {
        return zNodeNBIName;
    }

    /**
     * Sets the value of the zNodeNBIName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZNodeNBIName(String value) {
        this.zNodeNBIName = value;
    }

    /**
     * Gets the value of the zEndNodeDef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndNodeDef() {
        return zEndNodeDef;
    }

    /**
     * Sets the value of the zEndNodeDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndNodeDef(String value) {
        this.zEndNodeDef = value;
    }

    /**
     * Gets the value of the zEndNodeDefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndNodeDefId() {
        return zEndNodeDefId;
    }

    /**
     * Sets the value of the zEndNodeDefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndNodeDefId(String value) {
        this.zEndNodeDefId = value;
    }

    /**
     * Gets the value of the zNodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZNodeType() {
        return zNodeType;
    }

    /**
     * Sets the value of the zNodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZNodeType(String value) {
        this.zNodeType = value;
    }

    /**
     * Gets the value of the zPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZPortName() {
        return zPortName;
    }

    /**
     * Sets the value of the zPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZPortName(String value) {
        this.zPortName = value;
    }

    /**
     * Gets the value of the zPortNBIName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZPortNBIName() {
        return zPortNBIName;
    }

    /**
     * Sets the value of the zPortNBIName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZPortNBIName(String value) {
        this.zPortNBIName = value;
    }

    /**
     * Gets the value of the modifiedTimeStamp property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getModifiedTimeStamp() {
        return modifiedTimeStamp;
    }

    /**
     * Sets the value of the modifiedTimeStamp property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setModifiedTimeStamp(XMLGregorianCalendar value) {
        this.modifiedTimeStamp = value;
    }

    /**
     * Gets the value of the circuitSpeed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCircuitSpeed() {
        return circuitSpeed;
    }

    /**
     * Sets the value of the circuitSpeed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCircuitSpeed(String value) {
        this.circuitSpeed = value;
    }

}
