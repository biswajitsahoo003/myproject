
package com.tcl.dias.serviceactivation.offnetdetails.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for offnetInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="offnetInterface"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="NNIID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PROVIDER_NodeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PROVIDER_NodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TCL_NodeId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TCL_NodeName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TCL_PortId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TCL_PortName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="TimeSlots" type="{urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption}timeSlot" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "offnetInterface", propOrder = {
    "nniid",
    "providerNodeId",
    "providerNodeName",
    "tclNodeId",
    "tclNodeName",
    "tclPortId",
    "tclPortName",
    "timeSlots"
})
@XmlSeeAlso({
    WorkerOffnetInterface.class,
    ProtectionOffnetInterface.class
})
public class OffnetInterface {

    @XmlElement(name = "NNIID")
    protected String nniid;
    @XmlElement(name = "PROVIDER_NodeId")
    protected String providerNodeId;
    @XmlElement(name = "PROVIDER_NodeName")
    protected String providerNodeName;
    @XmlElement(name = "TCL_NodeId")
    protected String tclNodeId;
    @XmlElement(name = "TCL_NodeName")
    protected String tclNodeName;
    @XmlElement(name = "TCL_PortId")
    protected String tclPortId;
    @XmlElement(name = "TCL_PortName")
    protected String tclPortName;
    @XmlElement(name = "TimeSlots")
    protected TimeSlot timeSlots;

    /**
     * Gets the value of the nniid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNNIID() {
        return nniid;
    }

    /**
     * Sets the value of the nniid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNNIID(String value) {
        this.nniid = value;
    }

    /**
     * Gets the value of the providerNodeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROVIDERNodeId() {
        return providerNodeId;
    }

    /**
     * Sets the value of the providerNodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROVIDERNodeId(String value) {
        this.providerNodeId = value;
    }

    /**
     * Gets the value of the providerNodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPROVIDERNodeName() {
        return providerNodeName;
    }

    /**
     * Sets the value of the providerNodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPROVIDERNodeName(String value) {
        this.providerNodeName = value;
    }

    /**
     * Gets the value of the tclNodeId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTCLNodeId() {
        return tclNodeId;
    }

    /**
     * Sets the value of the tclNodeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTCLNodeId(String value) {
        this.tclNodeId = value;
    }

    /**
     * Gets the value of the tclNodeName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTCLNodeName() {
        return tclNodeName;
    }

    /**
     * Sets the value of the tclNodeName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTCLNodeName(String value) {
        this.tclNodeName = value;
    }

    /**
     * Gets the value of the tclPortId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTCLPortId() {
        return tclPortId;
    }

    /**
     * Sets the value of the tclPortId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTCLPortId(String value) {
        this.tclPortId = value;
    }

    /**
     * Gets the value of the tclPortName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTCLPortName() {
        return tclPortName;
    }

    /**
     * Sets the value of the tclPortName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTCLPortName(String value) {
        this.tclPortName = value;
    }

    /**
     * Gets the value of the timeSlots property.
     * 
     * @return
     *     possible object is
     *     {@link TimeSlot }
     *     
     */
    public TimeSlot getTimeSlots() {
        return timeSlots;
    }

    /**
     * Sets the value of the timeSlots property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeSlot }
     *     
     */
    public void setTimeSlots(TimeSlot value) {
        this.timeSlots = value;
    }

}
