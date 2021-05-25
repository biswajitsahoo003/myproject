
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccessPoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccessPoint">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="switch" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Switch" minOccurs="0"/>
 *         &lt;element name="AccessInterface" type="{http://www.tcl.com/2014/4/ipsvc/xsd}EthernetAccessUNIInterface" minOccurs="0"/>
 *         &lt;element name="actionType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ADD"/>
 *               &lt;enumeration value="REMOVE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ringName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="huaweiCFMParams" type="{http://www.tcl.com/2014/5/ipsvc/xsd}HuaweiCFMParams" minOccurs="0"/>
 *         &lt;element name="attribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccessPoint", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "_switch",
    "accessInterface",
    "actionType",
    "ringName",
    "huaweiCFMParams",
    "attribute1",
    "attribute2"
})
public class AccessPoint {

    @XmlElement(name = "switch")
    protected Switch _switch;
    @XmlElement(name = "AccessInterface")
    protected EthernetAccessUNIInterface accessInterface;
    protected String actionType;
    protected String ringName;
    protected HuaweiCFMParams huaweiCFMParams;
    protected String attribute1;
    protected String attribute2;

    /**
     * Gets the value of the switch property.
     * 
     * @return
     *     possible object is
     *     {@link Switch }
     *     
     */
    public Switch getSwitch() {
        return _switch;
    }

    /**
     * Sets the value of the switch property.
     * 
     * @param value
     *     allowed object is
     *     {@link Switch }
     *     
     */
    public void setSwitch(Switch value) {
        this._switch = value;
    }

    /**
     * Gets the value of the accessInterface property.
     * 
     * @return
     *     possible object is
     *     {@link EthernetAccessUNIInterface }
     *     
     */
    public EthernetAccessUNIInterface getAccessInterface() {
        return accessInterface;
    }

    /**
     * Sets the value of the accessInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link EthernetAccessUNIInterface }
     *     
     */
    public void setAccessInterface(EthernetAccessUNIInterface value) {
        this.accessInterface = value;
    }

    /**
     * Gets the value of the actionType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getActionType() {
        return actionType;
    }

    /**
     * Sets the value of the actionType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setActionType(String value) {
        this.actionType = value;
    }

    /**
     * Gets the value of the ringName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRingName() {
        return ringName;
    }

    /**
     * Sets the value of the ringName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRingName(String value) {
        this.ringName = value;
    }

    /**
     * Gets the value of the huaweiCFMParams property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiCFMParams }
     *     
     */
    public HuaweiCFMParams getHuaweiCFMParams() {
        return huaweiCFMParams;
    }

    /**
     * Sets the value of the huaweiCFMParams property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiCFMParams }
     *     
     */
    public void setHuaweiCFMParams(HuaweiCFMParams value) {
        this.huaweiCFMParams = value;
    }

    /**
     * Gets the value of the attribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * Sets the value of the attribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute1(String value) {
        this.attribute1 = value;
    }

    /**
     * Gets the value of the attribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * Sets the value of the attribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute2(String value) {
        this.attribute2 = value;
    }

}
