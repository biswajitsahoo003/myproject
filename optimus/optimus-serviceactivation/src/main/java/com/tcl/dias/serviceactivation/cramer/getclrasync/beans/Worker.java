package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Worker complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Worker"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="bandwidth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="circuitId" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="circuitName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="circuitLabel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="circuitType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="level" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="isMarkedForAction" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="objectType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="routeSequence" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sequence" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tsName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="usedBy2Circuit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="uses2Circuit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndNodeDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndNodeDefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndNodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aNodeAddr1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aNodeAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aNodeAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aNodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aPortAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aPortAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndNodeDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndNodeDefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndNodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zNodeAddr1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zNodeAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zNodeAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zNodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zPortAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zPortAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndParentPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndParentPortAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndParentPortAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndParentPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndParentPortAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndParentPortAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="aEndVLAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="zEndVLAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Worker", propOrder = {
    "bandwidth",
    "circuitId",
    "circuitName",
    "circuitLabel",
    "circuitType",
    "level",
    "isMarkedForAction",
    "objectType",
    "routeSequence",
    "sequence",
    "tsName",
    "usedBy2Circuit",
    "uses2Circuit",
    "aEndNodeDef",
    "aEndNodeDefId",
    "aEndNodeType",
    "aNodeAddr1",
    "aNodeAlias1",
    "aNodeAlias2",
    "aNodeName",
    "aPortAlias1",
    "aPortAlias2",
    "aPortName",
    "zEndNodeDef",
    "zEndNodeDefId",
    "zEndNodeType",
    "zNodeAddr1",
    "zNodeAlias1",
    "zNodeAlias2",
    "zNodeName",
    "zPortAlias1",
    "zPortAlias2",
    "zPortName",
    "aEndParentPortName",
    "aEndParentPortAlias1",
    "aEndParentPortAlias2",
    "zEndParentPortName",
    "zEndParentPortAlias1",
    "zEndParentPortAlias2",
    "aEndServiceType",
    "zEndServiceType",
    "aEndVLAN",
    "zEndVLAN"
})
public class Worker {

    protected String bandwidth;
    protected long circuitId;
    protected String circuitName;
    protected String circuitLabel;
    protected String circuitType;
    protected String level;
    protected boolean isMarkedForAction;
    protected String objectType;
    protected String routeSequence;
    protected String sequence;
    protected String tsName;
    protected String usedBy2Circuit;
    protected String uses2Circuit;
    protected String aEndNodeDef;
    protected String aEndNodeDefId;
    protected String aEndNodeType;
    protected String aNodeAddr1;
    protected String aNodeAlias1;
    protected String aNodeAlias2;
    protected String aNodeName;
    protected String aPortAlias1;
    protected String aPortAlias2;
    protected String aPortName;
    protected String zEndNodeDef;
    protected String zEndNodeDefId;
    protected String zEndNodeType;
    protected String zNodeAddr1;
    protected String zNodeAlias1;
    protected String zNodeAlias2;
    protected String zNodeName;
    protected String zPortAlias1;
    protected String zPortAlias2;
    protected String zPortName;
    protected String aEndParentPortName;
    protected String aEndParentPortAlias1;
    protected String aEndParentPortAlias2;
    protected String zEndParentPortName;
    protected String zEndParentPortAlias1;
    protected String zEndParentPortAlias2;
    protected String aEndServiceType;
    protected String zEndServiceType;
    protected String aEndVLAN;
    protected String zEndVLAN;

    /**
     * Gets the value of the bandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBandwidth() {
        return bandwidth;
    }

    /**
     * Sets the value of the bandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBandwidth(String value) {
        this.bandwidth = value;
    }

    /**
     * Gets the value of the circuitId property.
     * 
     */
    public long getCircuitId() {
        return circuitId;
    }

    /**
     * Sets the value of the circuitId property.
     * 
     */
    public void setCircuitId(long value) {
        this.circuitId = value;
    }

    /**
     * Gets the value of the circuitName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCircuitName() {
        return circuitName;
    }

    /**
     * Sets the value of the circuitName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCircuitName(String value) {
        this.circuitName = value;
    }

    /**
     * Gets the value of the circuitLabel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCircuitLabel() {
        return circuitLabel;
    }

    /**
     * Sets the value of the circuitLabel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCircuitLabel(String value) {
        this.circuitLabel = value;
    }

    /**
     * Gets the value of the circuitType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCircuitType() {
        return circuitType;
    }

    /**
     * Sets the value of the circuitType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCircuitType(String value) {
        this.circuitType = value;
    }

    /**
     * Gets the value of the level property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets the value of the level property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLevel(String value) {
        this.level = value;
    }

    /**
     * Gets the value of the isMarkedForAction property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean getIsMarkedForAction() {
        return isMarkedForAction;
    }

    /**
     * Sets the value of the isMarkedForAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsMarkedForAction(boolean value) {
        this.isMarkedForAction = value;
    }

    /**
     * Gets the value of the objectType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getObjectType() {
        return objectType;
    }

    /**
     * Sets the value of the objectType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setObjectType(String value) {
        this.objectType = value;
    }

    /**
     * Gets the value of the routeSequence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRouteSequence() {
        return routeSequence;
    }

    /**
     * Sets the value of the routeSequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRouteSequence(String value) {
        this.routeSequence = value;
    }

    /**
     * Gets the value of the sequence property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSequence() {
        return sequence;
    }

    /**
     * Sets the value of the sequence property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSequence(String value) {
        this.sequence = value;
    }

    /**
     * Gets the value of the tsName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTsName() {
        return tsName;
    }

    /**
     * Sets the value of the tsName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTsName(String value) {
        this.tsName = value;
    }

    /**
     * Gets the value of the usedBy2Circuit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsedBy2Circuit() {
        return usedBy2Circuit;
    }

    /**
     * Sets the value of the usedBy2Circuit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsedBy2Circuit(String value) {
        this.usedBy2Circuit = value;
    }

    /**
     * Gets the value of the uses2Circuit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUses2Circuit() {
        return uses2Circuit;
    }

    /**
     * Sets the value of the uses2Circuit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUses2Circuit(String value) {
        this.uses2Circuit = value;
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
     * Gets the value of the aEndNodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndNodeType() {
        return aEndNodeType;
    }

    /**
     * Sets the value of the aEndNodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndNodeType(String value) {
        this.aEndNodeType = value;
    }

    /**
     * Gets the value of the aNodeAddr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getANodeAddr1() {
        return aNodeAddr1;
    }

    /**
     * Sets the value of the aNodeAddr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setANodeAddr1(String value) {
        this.aNodeAddr1 = value;
    }

    /**
     * Gets the value of the aNodeAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getANodeAlias1() {
        return aNodeAlias1;
    }

    /**
     * Sets the value of the aNodeAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setANodeAlias1(String value) {
        this.aNodeAlias1 = value;
    }

    /**
     * Gets the value of the aNodeAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getANodeAlias2() {
        return aNodeAlias2;
    }

    /**
     * Sets the value of the aNodeAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setANodeAlias2(String value) {
        this.aNodeAlias2 = value;
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
     * Gets the value of the aPortAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPortAlias1() {
        return aPortAlias1;
    }

    /**
     * Sets the value of the aPortAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPortAlias1(String value) {
        this.aPortAlias1 = value;
    }

    /**
     * Gets the value of the aPortAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAPortAlias2() {
        return aPortAlias2;
    }

    /**
     * Sets the value of the aPortAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAPortAlias2(String value) {
        this.aPortAlias2 = value;
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
     * Gets the value of the zEndNodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndNodeType() {
        return zEndNodeType;
    }

    /**
     * Sets the value of the zEndNodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndNodeType(String value) {
        this.zEndNodeType = value;
    }

    /**
     * Gets the value of the zNodeAddr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZNodeAddr1() {
        return zNodeAddr1;
    }

    /**
     * Sets the value of the zNodeAddr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZNodeAddr1(String value) {
        this.zNodeAddr1 = value;
    }

    /**
     * Gets the value of the zNodeAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZNodeAlias1() {
        return zNodeAlias1;
    }

    /**
     * Sets the value of the zNodeAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZNodeAlias1(String value) {
        this.zNodeAlias1 = value;
    }

    /**
     * Gets the value of the zNodeAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZNodeAlias2() {
        return zNodeAlias2;
    }

    /**
     * Sets the value of the zNodeAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZNodeAlias2(String value) {
        this.zNodeAlias2 = value;
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
     * Gets the value of the zPortAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZPortAlias1() {
        return zPortAlias1;
    }

    /**
     * Sets the value of the zPortAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZPortAlias1(String value) {
        this.zPortAlias1 = value;
    }

    /**
     * Gets the value of the zPortAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZPortAlias2() {
        return zPortAlias2;
    }

    /**
     * Sets the value of the zPortAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZPortAlias2(String value) {
        this.zPortAlias2 = value;
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
     * Gets the value of the aEndParentPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndParentPortName() {
        return aEndParentPortName;
    }

    /**
     * Sets the value of the aEndParentPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndParentPortName(String value) {
        this.aEndParentPortName = value;
    }

    /**
     * Gets the value of the aEndParentPortAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndParentPortAlias1() {
        return aEndParentPortAlias1;
    }

    /**
     * Sets the value of the aEndParentPortAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndParentPortAlias1(String value) {
        this.aEndParentPortAlias1 = value;
    }

    /**
     * Gets the value of the aEndParentPortAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndParentPortAlias2() {
        return aEndParentPortAlias2;
    }

    /**
     * Sets the value of the aEndParentPortAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndParentPortAlias2(String value) {
        this.aEndParentPortAlias2 = value;
    }

    /**
     * Gets the value of the zEndParentPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndParentPortName() {
        return zEndParentPortName;
    }

    /**
     * Sets the value of the zEndParentPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndParentPortName(String value) {
        this.zEndParentPortName = value;
    }

    /**
     * Gets the value of the zEndParentPortAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndParentPortAlias1() {
        return zEndParentPortAlias1;
    }

    /**
     * Sets the value of the zEndParentPortAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndParentPortAlias1(String value) {
        this.zEndParentPortAlias1 = value;
    }

    /**
     * Gets the value of the zEndParentPortAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndParentPortAlias2() {
        return zEndParentPortAlias2;
    }

    /**
     * Sets the value of the zEndParentPortAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndParentPortAlias2(String value) {
        this.zEndParentPortAlias2 = value;
    }

    /**
     * Gets the value of the aEndServiceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndServiceType() {
        return aEndServiceType;
    }

    /**
     * Sets the value of the aEndServiceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndServiceType(String value) {
        this.aEndServiceType = value;
    }

    /**
     * Gets the value of the zEndServiceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndServiceType() {
        return zEndServiceType;
    }

    /**
     * Sets the value of the zEndServiceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndServiceType(String value) {
        this.zEndServiceType = value;
    }

    /**
     * Gets the value of the aEndVLAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAEndVLAN() {
        return aEndVLAN;
    }

    /**
     * Sets the value of the aEndVLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAEndVLAN(String value) {
        this.aEndVLAN = value;
    }

    /**
     * Gets the value of the zEndVLAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZEndVLAN() {
        return zEndVLAN;
    }

    /**
     * Sets the value of the zEndVLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZEndVLAN(String value) {
        this.zEndVLAN = value;
    }

    @Override
    public String toString() {
        return "Worker{" +
                "bandwidth='" + bandwidth + '\'' +
                ", circuitId=" + circuitId +
                ", circuitName='" + circuitName + '\'' +
                ", circuitLabel='" + circuitLabel + '\'' +
                ", circuitType='" + circuitType + '\'' +
                ", level='" + level + '\'' +
                ", isMarkedForAction=" + isMarkedForAction +
                ", objectType='" + objectType + '\'' +
                ", routeSequence='" + routeSequence + '\'' +
                ", sequence='" + sequence + '\'' +
                ", tsName='" + tsName + '\'' +
                ", usedBy2Circuit='" + usedBy2Circuit + '\'' +
                ", uses2Circuit='" + uses2Circuit + '\'' +
                ", aEndNodeDef='" + aEndNodeDef + '\'' +
                ", aEndNodeDefId='" + aEndNodeDefId + '\'' +
                ", aEndNodeType='" + aEndNodeType + '\'' +
                ", aNodeAddr1='" + aNodeAddr1 + '\'' +
                ", aNodeAlias1='" + aNodeAlias1 + '\'' +
                ", aNodeAlias2='" + aNodeAlias2 + '\'' +
                ", aNodeName='" + aNodeName + '\'' +
                ", aPortAlias1='" + aPortAlias1 + '\'' +
                ", aPortAlias2='" + aPortAlias2 + '\'' +
                ", aPortName='" + aPortName + '\'' +
                ", zEndNodeDef='" + zEndNodeDef + '\'' +
                ", zEndNodeDefId='" + zEndNodeDefId + '\'' +
                ", zEndNodeType='" + zEndNodeType + '\'' +
                ", zNodeAddr1='" + zNodeAddr1 + '\'' +
                ", zNodeAlias1='" + zNodeAlias1 + '\'' +
                ", zNodeAlias2='" + zNodeAlias2 + '\'' +
                ", zNodeName='" + zNodeName + '\'' +
                ", zPortAlias1='" + zPortAlias1 + '\'' +
                ", zPortAlias2='" + zPortAlias2 + '\'' +
                ", zPortName='" + zPortName + '\'' +
                ", aEndParentPortName='" + aEndParentPortName + '\'' +
                ", aEndParentPortAlias1='" + aEndParentPortAlias1 + '\'' +
                ", aEndParentPortAlias2='" + aEndParentPortAlias2 + '\'' +
                ", zEndParentPortName='" + zEndParentPortName + '\'' +
                ", zEndParentPortAlias1='" + zEndParentPortAlias1 + '\'' +
                ", zEndParentPortAlias2='" + zEndParentPortAlias2 + '\'' +
                ", aEndServiceType='" + aEndServiceType + '\'' +
                ", zEndServiceType='" + zEndServiceType + '\'' +
                ", aEndVLAN='" + aEndVLAN + '\'' +
                ", zEndVLAN='" + zEndVLAN + '\'' +
                '}';
    }
}
