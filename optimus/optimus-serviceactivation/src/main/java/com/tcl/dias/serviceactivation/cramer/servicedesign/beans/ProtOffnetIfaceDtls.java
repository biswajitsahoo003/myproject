
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for protOffnetIfaceDtls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="protOffnetIfaceDtls"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PORTID_S" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="PORT_S" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="timeslot" type="{http://cramerserviceslibrary/csvc/wsdl/v2/inventorycreation/clr}timeSlot" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "protOffnetIfaceDtls", propOrder = {
    "portids",
    "ports",
    "timeslot"
})
public class ProtOffnetIfaceDtls {

    @XmlElement(name = "PORTID_S")
    protected long portids;
    @XmlElement(name = "PORT_S")
    protected String ports;
    protected TimeSlot timeslot;

    /**
     * Gets the value of the portids property.
     * 
     */
    public long getPORTIDS() {
        return portids;
    }

    /**
     * Sets the value of the portids property.
     * 
     */
    public void setPORTIDS(long value) {
        this.portids = value;
    }

    /**
     * Gets the value of the ports property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPORTS() {
        return ports;
    }

    /**
     * Sets the value of the ports property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPORTS(String value) {
        this.ports = value;
    }

    /**
     * Gets the value of the timeslot property.
     * 
     * @return
     *     possible object is
     *     {@link TimeSlot }
     *     
     */
    public TimeSlot getTimeslot() {
        return timeslot;
    }

    /**
     * Sets the value of the timeslot property.
     * 
     * @param value
     *     allowed object is
     *     {@link TimeSlot }
     *     
     */
    public void setTimeslot(TimeSlot value) {
        this.timeslot = value;
    }

}
