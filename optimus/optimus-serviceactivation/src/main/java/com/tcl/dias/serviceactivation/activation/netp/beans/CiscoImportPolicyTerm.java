
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CiscoImportPolicyTerm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CiscoImportPolicyTerm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="termAction" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="matchCriteria" type="{http://www.tcl.com/2011/11/ipsvc/xsd}CiscoImportPolicyMatchCriteria" minOccurs="0"/>
 *         &lt;element name="setCriteria" type="{http://www.tcl.com/2011/11/ipsvc/xsd}CiscoImportPolicySetCriteria" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CiscoImportPolicyTerm", propOrder = {
    "id",
    "termAction",
    "matchCriteria",
    "setCriteria",
    "isObjectInstanceModified"
})
public class CiscoImportPolicyTerm {

    protected String id;
    protected String termAction;
    protected CiscoImportPolicyMatchCriteria matchCriteria;
    protected CiscoImportPolicySetCriteria setCriteria;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the termAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermAction() {
        return termAction;
    }

    /**
     * Sets the value of the termAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermAction(String value) {
        this.termAction = value;
    }

    /**
     * Gets the value of the matchCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link CiscoImportPolicyMatchCriteria }
     *     
     */
    public CiscoImportPolicyMatchCriteria getMatchCriteria() {
        return matchCriteria;
    }

    /**
     * Sets the value of the matchCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CiscoImportPolicyMatchCriteria }
     *     
     */
    public void setMatchCriteria(CiscoImportPolicyMatchCriteria value) {
        this.matchCriteria = value;
    }

    /**
     * Gets the value of the setCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link CiscoImportPolicySetCriteria }
     *     
     */
    public CiscoImportPolicySetCriteria getSetCriteria() {
        return setCriteria;
    }

    /**
     * Sets the value of the setCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CiscoImportPolicySetCriteria }
     *     
     */
    public void setSetCriteria(CiscoImportPolicySetCriteria value) {
        this.setCriteria = value;
    }

    /**
     * Gets the value of the isObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsObjectInstanceModified() {
        return isObjectInstanceModified;
    }

    /**
     * Sets the value of the isObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsObjectInstanceModified(Boolean value) {
        this.isObjectInstanceModified = value;
    }

}
