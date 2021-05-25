
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Broadcast complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Broadcast">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="monitoredCircuit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monitoringCircuit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="monitoredFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndNodeDef" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndNodeDefId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="EndNodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NodeAddr1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NodeAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NodeAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="NodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PortAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PortAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Broadcast", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd", propOrder = {
    "monitoredCircuit",
    "monitoringCircuit",
    "monitoredFrom",
    "endNodeDef",
    "endNodeDefId",
    "endNodeType",
    "nodeAddr1",
    "nodeAlias1",
    "nodeAlias2",
    "nodeName",
    "portAlias1",
    "portAlias2",
    "portName",
    "attribute1",
    "attribute2"
})
public class Broadcast {

    protected String monitoredCircuit;
    protected String monitoringCircuit;
    protected String monitoredFrom;
    @XmlElement(name = "EndNodeDef")
    protected String endNodeDef;
    @XmlElement(name = "EndNodeDefId")
    protected String endNodeDefId;
    @XmlElement(name = "EndNodeType")
    protected String endNodeType;
    @XmlElement(name = "NodeAddr1")
    protected String nodeAddr1;
    @XmlElement(name = "NodeAlias1")
    protected String nodeAlias1;
    @XmlElement(name = "NodeAlias2")
    protected String nodeAlias2;
    @XmlElement(name = "NodeName")
    protected String nodeName;
    @XmlElement(name = "PortAlias1")
    protected String portAlias1;
    @XmlElement(name = "PortAlias2")
    protected String portAlias2;
    @XmlElement(name = "PortName")
    protected String portName;
    protected String attribute1;
    protected String attribute2;

    /**
     * Gets the value of the monitoredCircuit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonitoredCircuit() {
        return monitoredCircuit;
    }

    /**
     * Sets the value of the monitoredCircuit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonitoredCircuit(String value) {
        this.monitoredCircuit = value;
    }

    /**
     * Gets the value of the monitoringCircuit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonitoringCircuit() {
        return monitoringCircuit;
    }

    /**
     * Sets the value of the monitoringCircuit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonitoringCircuit(String value) {
        this.monitoringCircuit = value;
    }

    /**
     * Gets the value of the monitoredFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMonitoredFrom() {
        return monitoredFrom;
    }

    /**
     * Sets the value of the monitoredFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMonitoredFrom(String value) {
        this.monitoredFrom = value;
    }

    /**
     * Gets the value of the endNodeDef property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndNodeDef() {
        return endNodeDef;
    }

    /**
     * Sets the value of the endNodeDef property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndNodeDef(String value) {
        this.endNodeDef = value;
    }

    /**
     * Gets the value of the endNodeDefId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndNodeDefId() {
        return endNodeDefId;
    }

    /**
     * Sets the value of the endNodeDefId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndNodeDefId(String value) {
        this.endNodeDefId = value;
    }

    /**
     * Gets the value of the endNodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEndNodeType() {
        return endNodeType;
    }

    /**
     * Sets the value of the endNodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEndNodeType(String value) {
        this.endNodeType = value;
    }

    /**
     * Gets the value of the nodeAddr1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeAddr1() {
        return nodeAddr1;
    }

    /**
     * Sets the value of the nodeAddr1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeAddr1(String value) {
        this.nodeAddr1 = value;
    }

    /**
     * Gets the value of the nodeAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeAlias1() {
        return nodeAlias1;
    }

    /**
     * Sets the value of the nodeAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeAlias1(String value) {
        this.nodeAlias1 = value;
    }

    /**
     * Gets the value of the nodeAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeAlias2() {
        return nodeAlias2;
    }

    /**
     * Sets the value of the nodeAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeAlias2(String value) {
        this.nodeAlias2 = value;
    }

    /**
     * Gets the value of the nodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets the value of the nodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeName(String value) {
        this.nodeName = value;
    }

    /**
     * Gets the value of the portAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortAlias1() {
        return portAlias1;
    }

    /**
     * Sets the value of the portAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortAlias1(String value) {
        this.portAlias1 = value;
    }

    /**
     * Gets the value of the portAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortAlias2() {
        return portAlias2;
    }

    /**
     * Sets the value of the portAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortAlias2(String value) {
        this.portAlias2 = value;
    }

    /**
     * Gets the value of the portName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPortName() {
        return portName;
    }

    /**
     * Sets the value of the portName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPortName(String value) {
        this.portName = value;
    }

    /**
     * Gets the value of the attribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * Sets the value of the attribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute1(String value) {
        this.attribute1 = value;
    }

    /**
     * Gets the value of the attribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * Sets the value of the attribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute2(String value) {
        this.attribute2 = value;
    }

}
