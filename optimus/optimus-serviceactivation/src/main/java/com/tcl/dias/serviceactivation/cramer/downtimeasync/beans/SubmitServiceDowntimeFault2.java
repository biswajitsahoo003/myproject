
package com.tcl.dias.serviceactivation.cramer.downtimeasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubmitServiceDowntimeFault complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SubmitServiceDowntimeFault"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header" type="{http://ACE_ServiceDowntime_Module}RequestHeader" minOccurs="0"/&gt;
 *         &lt;element name="fault" type="{http://ACE_ServiceDowntime_Module}ServiceDowntimeSubmitionFault" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubmitServiceDowntimeFault", propOrder = {
    "header",
    "fault"
})
public class SubmitServiceDowntimeFault2 {

    protected RequestHeader header;
    protected ServiceDowntimeSubmitionFault fault;

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
     *     {@link ServiceDowntimeSubmitionFault }
     *     
     */
    public ServiceDowntimeSubmitionFault getFault() {
        return fault;
    }

    /**
     * Sets the value of the fault property.
     * 
     * @param value
     *     allowed object is
     *     {@link ServiceDowntimeSubmitionFault }
     *     
     */
    public void setFault(ServiceDowntimeSubmitionFault value) {
        this.fault = value;
    }

}
