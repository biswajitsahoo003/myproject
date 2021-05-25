
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				criteriaValue can take any value from 0 to 7
 * 			
 * 
 * <p>Java class for IPPrecedent complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPPrecedent">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="criteriaValue1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="criteriaValue2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="criteriaValue3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="criteriaValue4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="criteriaValue5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="criteriaValue6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="criteriaValue7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPPrecedent", propOrder = {
    "criteriaValue1",
    "criteriaValue2",
    "criteriaValue3",
    "criteriaValue4",
    "criteriaValue5",
    "criteriaValue6",
    "criteriaValue7"
})
public class IPPrecedent {

    protected String criteriaValue1;
    protected String criteriaValue2;
    protected String criteriaValue3;
    protected String criteriaValue4;
    protected String criteriaValue5;
    protected String criteriaValue6;
    protected String criteriaValue7;

    /**
     * Gets the value of the criteriaValue1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaValue1() {
        return criteriaValue1;
    }

    /**
     * Sets the value of the criteriaValue1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaValue1(String value) {
        this.criteriaValue1 = value;
    }

    /**
     * Gets the value of the criteriaValue2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaValue2() {
        return criteriaValue2;
    }

    /**
     * Sets the value of the criteriaValue2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaValue2(String value) {
        this.criteriaValue2 = value;
    }

    /**
     * Gets the value of the criteriaValue3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaValue3() {
        return criteriaValue3;
    }

    /**
     * Sets the value of the criteriaValue3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaValue3(String value) {
        this.criteriaValue3 = value;
    }

    /**
     * Gets the value of the criteriaValue4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaValue4() {
        return criteriaValue4;
    }

    /**
     * Sets the value of the criteriaValue4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaValue4(String value) {
        this.criteriaValue4 = value;
    }

    /**
     * Gets the value of the criteriaValue5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaValue5() {
        return criteriaValue5;
    }

    /**
     * Sets the value of the criteriaValue5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaValue5(String value) {
        this.criteriaValue5 = value;
    }

    /**
     * Gets the value of the criteriaValue6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaValue6() {
        return criteriaValue6;
    }

    /**
     * Sets the value of the criteriaValue6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaValue6(String value) {
        this.criteriaValue6 = value;
    }

    /**
     * Gets the value of the criteriaValue7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCriteriaValue7() {
        return criteriaValue7;
    }

    /**
     * Sets the value of the criteriaValue7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCriteriaValue7(String value) {
        this.criteriaValue7 = value;
    }

}
