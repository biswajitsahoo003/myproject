
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EthernetAccessUNIInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EthernetAccessUNIInterface">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CustomerVLANMappingList" type="{http://www.tcl.com/2014/4/ipsvc/xsd}VLANMapping" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="mode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUNK"/>
 *               &lt;enumeration value="ACCESS"/>
 *               &lt;enumeration value="QINQ"/>
 *               &lt;enumeration value="TUNNEL"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="inboundCIR" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="outboundCIR" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="Speed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Duplex" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ACLname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="trafficPriority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isAutoNegotiateEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="overrideDescription" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="macLimitMaximum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isESOEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE_WITH_CVLAN"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="TRUE_WITHOUT_CVLAN"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ESOVLANMappingList" type="{http://www.tcl.com/2014/4/ipsvc/xsd}VLANMapping" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EthernetAccessUNIInterface", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "customerVLANMappingList",
    "mode",
    "description",
    "inboundCIR",
    "outboundCIR",
    "speed",
    "duplex",
    "acLname",
    "trafficPriority",
    "isAutoNegotiateEnabled",
    "overrideDescription",
    "macLimitMaximum",
    "isESOEnabled",
    "esovlanMappingList"
})
public class EthernetAccessUNIInterface {

    @XmlElement(name = "CustomerVLANMappingList")
    protected List<VLANMapping> customerVLANMappingList;
    protected String mode;
    @XmlElement(name = "Description")
    protected String description;
    protected Bandwidth inboundCIR;
    protected Bandwidth outboundCIR;
    @XmlElement(name = "Speed")
    protected String speed;
    @XmlElement(name = "Duplex")
    protected String duplex;
    @XmlElement(name = "ACLname")
    protected String acLname;
    protected String trafficPriority;
    protected String isAutoNegotiateEnabled;
    protected String overrideDescription;
    protected String macLimitMaximum;
    protected String isESOEnabled;
    @XmlElement(name = "ESOVLANMappingList")
    protected VLANMapping esovlanMappingList;

    /**
     * Gets the value of the customerVLANMappingList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the customerVLANMappingList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCustomerVLANMappingList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VLANMapping }
     * 
     * 
     */
    public List<VLANMapping> getCustomerVLANMappingList() {
        if (customerVLANMappingList == null) {
            customerVLANMappingList = new ArrayList<VLANMapping>();
        }
        return this.customerVLANMappingList;
    }

    /**
     * Gets the value of the mode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets the value of the mode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMode(String value) {
        this.mode = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the inboundCIR property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getInboundCIR() {
        return inboundCIR;
    }

    /**
     * Sets the value of the inboundCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setInboundCIR(Bandwidth value) {
        this.inboundCIR = value;
    }

    /**
     * Gets the value of the outboundCIR property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getOutboundCIR() {
        return outboundCIR;
    }

    /**
     * Sets the value of the outboundCIR property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setOutboundCIR(Bandwidth value) {
        this.outboundCIR = value;
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
     * Gets the value of the duplex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDuplex() {
        return duplex;
    }

    /**
     * Sets the value of the duplex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDuplex(String value) {
        this.duplex = value;
    }

    /**
     * Gets the value of the acLname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getACLname() {
        return acLname;
    }

    /**
     * Sets the value of the acLname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setACLname(String value) {
        this.acLname = value;
    }

    /**
     * Gets the value of the trafficPriority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTrafficPriority() {
        return trafficPriority;
    }

    /**
     * Sets the value of the trafficPriority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTrafficPriority(String value) {
        this.trafficPriority = value;
    }

    /**
     * Gets the value of the isAutoNegotiateEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAutoNegotiateEnabled() {
        return isAutoNegotiateEnabled;
    }

    /**
     * Sets the value of the isAutoNegotiateEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAutoNegotiateEnabled(String value) {
        this.isAutoNegotiateEnabled = value;
    }

    /**
     * Gets the value of the overrideDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverrideDescription() {
        return overrideDescription;
    }

    /**
     * Sets the value of the overrideDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverrideDescription(String value) {
        this.overrideDescription = value;
    }

    /**
     * Gets the value of the macLimitMaximum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMacLimitMaximum() {
        return macLimitMaximum;
    }

    /**
     * Sets the value of the macLimitMaximum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMacLimitMaximum(String value) {
        this.macLimitMaximum = value;
    }

    /**
     * Gets the value of the isESOEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsESOEnabled() {
        return isESOEnabled;
    }

    /**
     * Sets the value of the isESOEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsESOEnabled(String value) {
        this.isESOEnabled = value;
    }

    /**
     * Gets the value of the esovlanMappingList property.
     * 
     * @return
     *     possible object is
     *     {@link VLANMapping }
     *     
     */
    public VLANMapping getESOVLANMappingList() {
        return esovlanMappingList;
    }

    /**
     * Sets the value of the esovlanMappingList property.
     * 
     * @param value
     *     allowed object is
     *     {@link VLANMapping }
     *     
     */
    public void setESOVLANMappingList(VLANMapping value) {
        this.esovlanMappingList = value;
    }

}
