
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VLANTranslationPoint complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VLANTranslationPoint">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VLANTranslationDetails" type="{http://www.tcl.com/2014/4/ipsvc/xsd}VLANTranslationDetails" minOccurs="0"/>
 *         &lt;element name="ActionType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ADD"/>
 *               &lt;enumeration value="REMOVE"/>
 *               &lt;enumeration value="MODIFY"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isObjectModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VLANTranslationPoint", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "vlanTranslationDetails",
    "actionType",
    "isObjectModified"
})
public class VLANTranslationPoint {

    @XmlElement(name = "VLANTranslationDetails")
    protected VLANTranslationDetails vlanTranslationDetails;
    @XmlElement(name = "ActionType")
    protected String actionType;
    protected Boolean isObjectModified;

    /**
     * Gets the value of the vlanTranslationDetails property.
     * 
     * @return
     *     possible object is
     *     {@link VLANTranslationDetails }
     *     
     */
    public VLANTranslationDetails getVLANTranslationDetails() {
        return vlanTranslationDetails;
    }

    /**
     * Sets the value of the vlanTranslationDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link VLANTranslationDetails }
     *     
     */
    public void setVLANTranslationDetails(VLANTranslationDetails value) {
        this.vlanTranslationDetails = value;
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
     * Gets the value of the isObjectModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectModified() {
        return isObjectModified;
    }

    /**
     * Sets the value of the isObjectModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectModified(Boolean value) {
        this.isObjectModified = value;
    }

}
