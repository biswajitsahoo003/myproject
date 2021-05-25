
package com.tcl.dias.serviceactivation.offnetdetails.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setElectricalHandOffDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setElectricalHandOffDetails"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Header" type="{urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption}requestHeader" minOccurs="0"/&gt;
 *         &lt;element name="TopologyName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="NodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "setElectricalHandOffDetails", propOrder = {
    "header",
    "topologyName",
    "nodeName"
})
public class SetElectricalHandOffDetails {

    @XmlElement(name = "Header")
    protected RequestHeader header;
    @XmlElement(name = "TopologyName")
    protected String topologyName;
    @XmlElement(name = "NodeName")
    protected String nodeName;

    /**
     * Gets the value of the header property.
     * 
     * @return
     *     possible object is
     *     {@link RequestHeader }
     *     
     */
    public RequestHeader getHeader() {
        return header;
    }

    /**
     * Sets the value of the header property.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestHeader }
     *     
     */
    public void setHeader(RequestHeader value) {
        this.header = value;
    }

    /**
     * Gets the value of the topologyName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopologyName() {
        return topologyName;
    }

    /**
     * Sets the value of the topologyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopologyName(String value) {
        this.topologyName = value;
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

}
