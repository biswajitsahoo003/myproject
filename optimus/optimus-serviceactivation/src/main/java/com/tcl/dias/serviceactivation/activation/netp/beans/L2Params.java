
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for L2Params complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="L2Params">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalPort" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalPortAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="physicalPortAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vcgNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="vlan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="huaweiMSPPParams" type="{http://www.tcl.com/2014/11/transmissionsvc/xsd}HuaweiMSPPParams" minOccurs="0"/>
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
@XmlType(name = "L2Params", namespace = "http://www.tcl.com/2014/03/transmissionsvc/xsd", propOrder = {
    "nodeType",
    "nodeAlias1",
    "nodeAlias2",
    "nodeName",
    "physicalPort",
    "physicalPortAlias1",
    "physicalPortAlias2",
    "vcgNumber",
    "vlan",
    "huaweiMSPPParams",
    "attribute1",
    "attribute2"
})
public class L2Params {

    protected String nodeType;
    protected String nodeAlias1;
    protected String nodeAlias2;
    protected String nodeName;
    protected String physicalPort;
    protected String physicalPortAlias1;
    protected String physicalPortAlias2;
    protected String vcgNumber;
    protected String vlan;
    protected HuaweiMSPPParams huaweiMSPPParams;
    protected String attribute1;
    protected String attribute2;

    /**
     * Gets the value of the nodeType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Sets the value of the nodeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNodeType(String value) {
        this.nodeType = value;
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
     * Gets the value of the physicalPort property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalPort() {
        return physicalPort;
    }

    /**
     * Sets the value of the physicalPort property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalPort(String value) {
        this.physicalPort = value;
    }

    /**
     * Gets the value of the physicalPortAlias1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalPortAlias1() {
        return physicalPortAlias1;
    }

    /**
     * Sets the value of the physicalPortAlias1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalPortAlias1(String value) {
        this.physicalPortAlias1 = value;
    }

    /**
     * Gets the value of the physicalPortAlias2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPhysicalPortAlias2() {
        return physicalPortAlias2;
    }

    /**
     * Sets the value of the physicalPortAlias2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPhysicalPortAlias2(String value) {
        this.physicalPortAlias2 = value;
    }

    /**
     * Gets the value of the vcgNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVcgNumber() {
        return vcgNumber;
    }

    /**
     * Sets the value of the vcgNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVcgNumber(String value) {
        this.vcgNumber = value;
    }

    /**
     * Gets the value of the vlan property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVlan() {
        return vlan;
    }

    /**
     * Sets the value of the vlan property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVlan(String value) {
        this.vlan = value;
    }

    /**
     * Gets the value of the huaweiMSPPParams property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiMSPPParams }
     *     
     */
    public HuaweiMSPPParams getHuaweiMSPPParams() {
        return huaweiMSPPParams;
    }

    /**
     * Sets the value of the huaweiMSPPParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiMSPPParams }
     *     
     */
    public void setHuaweiMSPPParams(HuaweiMSPPParams value) {
        this.huaweiMSPPParams = value;
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
