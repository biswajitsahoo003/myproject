
package com.tcl.dias.serviceactivation.cramer.servicedesign.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for wrkrOffnetIfaceDtls complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="wrkrOffnetIfaceDtls"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="OFFNET_PROVIDER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PORTID_W" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="PORT_W" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PROVIDER_NODEID" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
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
@XmlType(name = "wrkrOffnetIfaceDtls", propOrder = {
    "offnetprovider",
    "portidw",
    "portw",
    "providernodeid",
    "timeslot"
})
public class WrkrOffnetIfaceDtls {

    @XmlElement(name = "OFFNET_PROVIDER")
    protected String offnetprovider;
    @XmlElement(name = "PORTID_W")
    protected long portidw;
    @XmlElement(name = "PORT_W")
    protected String portw;
    @XmlElement(name = "PROVIDER_NODEID")
    protected long providernodeid;
    protected TimeSlot timeslot;

    /**
     * Gets the value of the offnetprovider property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOFFNETPROVIDER() {
        return offnetprovider;
    }

    /**
     * Sets the value of the offnetprovider property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOFFNETPROVIDER(String value) {
        this.offnetprovider = value;
    }

    /**
     * Gets the value of the portidw property.
     * 
     */
    public long getPORTIDW() {
        return portidw;
    }

    /**
     * Sets the value of the portidw property.
     * 
     */
    public void setPORTIDW(long value) {
        this.portidw = value;
    }

    /**
     * Gets the value of the portw property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPORTW() {
        return portw;
    }

    /**
     * Sets the value of the portw property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPORTW(String value) {
        this.portw = value;
    }

    /**
     * Gets the value of the providernodeid property.
     * 
     */
    public long getPROVIDERNODEID() {
        return providernodeid;
    }

    /**
     * Sets the value of the providernodeid property.
     * 
     */
    public void setPROVIDERNODEID(long value) {
        this.providernodeid = value;
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
