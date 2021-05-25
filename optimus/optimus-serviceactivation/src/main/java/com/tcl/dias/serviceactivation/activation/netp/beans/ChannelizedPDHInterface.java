
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ChannelizedPDHInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ChannelizedPDHInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}Interface">
 *       &lt;sequence>
 *         &lt;element name="sixtyFourKTimeslot" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="channelGroupNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="logicalInterfaceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="klmValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bfdConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}BFDConfig" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChannelizedPDHInterface", propOrder = {
    "sixtyFourKTimeslot",
    "channelGroupNumber",
    "logicalInterfaceName",
    "klmValue",
    "bfdConfig"
})
public class ChannelizedPDHInterface
    extends Interface
{

    protected List<String> sixtyFourKTimeslot;
    protected String channelGroupNumber;
    protected String logicalInterfaceName;
    protected String klmValue;
    protected BFDConfig bfdConfig;

    /**
     * Gets the value of the sixtyFourKTimeslot property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sixtyFourKTimeslot property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSixtyFourKTimeslot().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getSixtyFourKTimeslot() {
        if (sixtyFourKTimeslot == null) {
            sixtyFourKTimeslot = new ArrayList<String>();
        }
        return this.sixtyFourKTimeslot;
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
     * Gets the value of the logicalInterfaceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogicalInterfaceName() {
        return logicalInterfaceName;
    }

    /**
     * Sets the value of the logicalInterfaceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogicalInterfaceName(String value) {
        this.logicalInterfaceName = value;
    }

    /**
     * Gets the value of the klmValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKlmValue() {
        return klmValue;
    }

    /**
     * Sets the value of the klmValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKlmValue(String value) {
        this.klmValue = value;
    }

    /**
     * Gets the value of the bfdConfig property.
     * 
     * @return
     *     possible object is
     *     {@link BFDConfig }
     *     
     */
    public BFDConfig getBfdConfig() {
        return bfdConfig;
    }

    /**
     * Sets the value of the bfdConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link BFDConfig }
     *     
     */
    public void setBfdConfig(BFDConfig value) {
        this.bfdConfig = value;
    }

}
