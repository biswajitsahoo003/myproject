package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for L2Params complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="L2Params"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="nodeType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="physicalPortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="physicalPortAlias1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="physicalPortAlias2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="vcgNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="vlan" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "L2Params", namespace = "http://www.tcl.com/2014/03/transmissionsvc/xsd", propOrder = {
        "nodeType",
        "nodeAlias1",
        "nodeAlias2",
        "nodeName",
        "physicalPortName",
        "physicalPortAlias1",
        "physicalPortAlias2",
        "vcgNumber",
        "vlan"
})
public class L2Params {

    protected String nodeType;
    protected String nodeAlias1;
    protected String nodeAlias2;
    protected String nodeName;
    protected String physicalPortName;
    protected String physicalPortAlias1;
    protected String physicalPortAlias2;
    protected String vcgNumber;
    protected String vlan;

    /**
     * Gets the value of the nodeType property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * Sets the value of the nodeType property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNodeType(String value) {
        this.nodeType = value;
    }

    /**
     * Gets the value of the nodeAlias1 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNodeAlias1() {
        return nodeAlias1;
    }

    /**
     * Sets the value of the nodeAlias1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNodeAlias1(String value) {
        this.nodeAlias1 = value;
    }

    /**
     * Gets the value of the nodeAlias2 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNodeAlias2() {
        return nodeAlias2;
    }

    /**
     * Sets the value of the nodeAlias2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNodeAlias2(String value) {
        this.nodeAlias2 = value;
    }

    /**
     * Gets the value of the nodeName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNodeName() {
        return nodeName;
    }

    /**
     * Sets the value of the nodeName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNodeName(String value) {
        this.nodeName = value;
    }

    /**
     * Gets the value of the physicalPortName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPhysicalPortName() {
        return physicalPortName;
    }

    /**
     * Sets the value of the physicalPortName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPhysicalPortName(String value) {
        this.physicalPortName = value;
    }

    /**
     * Gets the value of the physicalPortAlias1 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPhysicalPortAlias1() {
        return physicalPortAlias1;
    }

    /**
     * Sets the value of the physicalPortAlias1 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPhysicalPortAlias1(String value) {
        this.physicalPortAlias1 = value;
    }

    /**
     * Gets the value of the physicalPortAlias2 property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPhysicalPortAlias2() {
        return physicalPortAlias2;
    }

    /**
     * Sets the value of the physicalPortAlias2 property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPhysicalPortAlias2(String value) {
        this.physicalPortAlias2 = value;
    }

    /**
     * Gets the value of the vcgNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVcgNumber() {
        return vcgNumber;
    }

    /**
     * Sets the value of the vcgNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVcgNumber(String value) {
        this.vcgNumber = value;
    }

    /**
     * Gets the value of the vlan property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVlan() {
        return vlan;
    }

    /**
     * Sets the value of the vlan property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVlan(String value) {
        this.vlan = value;
    }

    @Override
    public String toString() {
        return "L2Params{" +
                "nodeType='" + nodeType + '\'' +
                ", nodeAlias1='" + nodeAlias1 + '\'' +
                ", nodeAlias2='" + nodeAlias2 + '\'' +
                ", nodeName='" + nodeName + '\'' +
                ", physicalPortName='" + physicalPortName + '\'' +
                ", physicalPortAlias1='" + physicalPortAlias1 + '\'' +
                ", physicalPortAlias2='" + physicalPortAlias2 + '\'' +
                ", vcgNumber='" + vcgNumber + '\'' +
                ", vlan='" + vlan + '\'' +
                '}';
    }
}
