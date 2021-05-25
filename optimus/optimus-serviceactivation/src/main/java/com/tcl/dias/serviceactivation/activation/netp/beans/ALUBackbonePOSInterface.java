
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ALUBackbonePOSInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ALUBackbonePOSInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUBackboneRouterInterface">
 *       &lt;sequence>
 *         &lt;element name="holdTimeUp" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="holdTimeDown" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="cardType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="mtu" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="timeslot" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="channelGroupNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="speed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="crcValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="asmode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="NETWORK"/>
 *               &lt;enumeration value="HYBRID"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ALUBackbonePOSInterface", namespace = "http://www.tcl.com/2014/3/ipsvc/xsd", propOrder = {
    "holdTimeUp",
    "holdTimeDown",
    "cardType",
    "mtu",
    "timeslot",
    "channelGroupNumber",
    "speed",
    "crcValue",
    "asmode"
})
public class ALUBackbonePOSInterface
    extends ALUBackboneRouterInterface
{

    protected Integer holdTimeUp;
    protected Integer holdTimeDown;
    protected String cardType;
    protected String mtu;
    protected List<String> timeslot;
    protected String channelGroupNumber;
    protected String speed;
    protected String crcValue;
    protected String asmode;

    /**
     * Gets the value of the holdTimeUp property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHoldTimeUp() {
        return holdTimeUp;
    }

    /**
     * Sets the value of the holdTimeUp property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHoldTimeUp(Integer value) {
        this.holdTimeUp = value;
    }

    /**
     * Gets the value of the holdTimeDown property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getHoldTimeDown() {
        return holdTimeDown;
    }

    /**
     * Sets the value of the holdTimeDown property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setHoldTimeDown(Integer value) {
        this.holdTimeDown = value;
    }

    /**
     * Gets the value of the cardType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCardType() {
        return cardType;
    }

    /**
     * Sets the value of the cardType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCardType(String value) {
        this.cardType = value;
    }

    /**
     * Gets the value of the mtu property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMtu() {
        return mtu;
    }

    /**
     * Sets the value of the mtu property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMtu(String value) {
        this.mtu = value;
    }

    /**
     * Gets the value of the timeslot property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the timeslot property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTimeslot().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getTimeslot() {
        if (timeslot == null) {
            timeslot = new ArrayList<String>();
        }
        return this.timeslot;
    }

    /**
     * Gets the value of the channelGroupNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChannelGroupNumber() {
        return channelGroupNumber;
    }

    /**
     * Sets the value of the channelGroupNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChannelGroupNumber(String value) {
        this.channelGroupNumber = value;
    }

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeed(String value) {
        this.speed = value;
    }

    /**
     * Gets the value of the crcValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCrcValue() {
        return crcValue;
    }

    /**
     * Sets the value of the crcValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCrcValue(String value) {
        this.crcValue = value;
    }

    /**
     * Gets the value of the asmode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAsmode() {
        return asmode;
    }

    /**
     * Sets the value of the asmode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAsmode(String value) {
        this.asmode = value;
    }

}
