
package com.tcl.dias.serviceactivation.offnetdetails.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReserveTSTally complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ReserveTSTally"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Header" type="{urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption}requestHeader" minOccurs="0"/&gt;
 *         &lt;element name="WorkerOffnetInterface" type="{urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption}workerOffnetInterface" minOccurs="0"/&gt;
 *         &lt;element name="ProtectionOffnetInterface" type="{urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption}protectionOffnetInterface" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReserveTSTally", propOrder = {
    "header",
    "workerOffnetInterface",
    "protectionOffnetInterface"
})
public class ReserveTSTally {

    @XmlElement(name = "Header")
    protected RequestHeader header;
    @XmlElement(name = "WorkerOffnetInterface")
    protected WorkerOffnetInterface workerOffnetInterface;
    @XmlElement(name = "ProtectionOffnetInterface")
    protected ProtectionOffnetInterface protectionOffnetInterface;

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
     * Gets the value of the workerOffnetInterface property.
     * 
     * @return
     *     possible object is
     *     {@link WorkerOffnetInterface }
     *     
     */
    public WorkerOffnetInterface getWorkerOffnetInterface() {
        return workerOffnetInterface;
    }

    /**
     * Sets the value of the workerOffnetInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link WorkerOffnetInterface }
     *     
     */
    public void setWorkerOffnetInterface(WorkerOffnetInterface value) {
        this.workerOffnetInterface = value;
    }

    /**
     * Gets the value of the protectionOffnetInterface property.
     * 
     * @return
     *     possible object is
     *     {@link ProtectionOffnetInterface }
     *     
     */
    public ProtectionOffnetInterface getProtectionOffnetInterface() {
        return protectionOffnetInterface;
    }

    /**
     * Sets the value of the protectionOffnetInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProtectionOffnetInterface }
     *     
     */
    public void setProtectionOffnetInterface(ProtectionOffnetInterface value) {
        this.protectionOffnetInterface = value;
    }

}
