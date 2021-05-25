
package com.tcl.dias.serviceactivation.offnetdetails.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for submitTSSelectionFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="submitTSSelectionFault"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Header" type="{urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption}requestHeader" minOccurs="0"/&gt;
 *         &lt;element name="Fault" type="{urn:com.tatacommunications.cramer.ace.tsSubmition.ws.TimeSlotsConsumption}timeSlotSubmitionFault" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitTSSelectionFault", propOrder = {
    "header",
    "fault"
})
public class SubmitTSSelectionFault {

    @XmlElement(name = "Header")
    protected RequestHeader header;
    @XmlElement(name = "Fault")
    protected TimeSlotSubmitionFault fault;

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
     * Gets the value of the fault property.
     * 
     * @return
     *     possible object is
     *     {@link TimeSlotSubmitionFault }
     *     
     */
    public TimeSlotSubmitionFault getFault() {
        return fault;
    }

    /**
     * Sets the value of the fault property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeSlotSubmitionFault }
     *     
     */
    public void setFault(TimeSlotSubmitionFault value) {
        this.fault = value;
    }

}
