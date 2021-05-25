
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CoS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CoS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="classificationCriteria" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="accessControlList" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList"/>
 *                   &lt;element name="dscpValue" type="{http://www.tcl.com/2011/11/ipsvc/xsd}DscpValue"/>
 *                   &lt;element name="ipprecedent" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IPPrecedent"/>
 *                   &lt;element name="Any" minOccurs="0">
 *                     &lt;simpleType>
 *                       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *                         &lt;enumeration value="ANY"/>
 *                       &lt;/restriction>
 *                     &lt;/simpleType>
 *                   &lt;/element>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="bandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="cosUpdateAction" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ADD"/>
 *               &lt;enumeration value="REMOVE"/>
 *               &lt;enumeration value="COMPLETE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="bandwidthinKBPS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="PIRBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="isDefaultFC" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CoS", propOrder = {
    "name",
    "classificationCriteria",
    "bandwidth",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "cosUpdateAction",
    "bandwidthinKBPS",
    "pirBandwidth",
    "isDefaultFC"
})
public class CoS {

    protected String name;
    protected CoS.ClassificationCriteria classificationCriteria;
    protected Bandwidth bandwidth;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    protected String cosUpdateAction;
    protected String bandwidthinKBPS;
    @XmlElement(name = "PIRBandwidth")
    protected Bandwidth pirBandwidth;
    protected Boolean isDefaultFC;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the classificationCriteria property.
     * 
     * @return
     *     possible object is
     *     {@link CoS.ClassificationCriteria }
     *     
     */
    public CoS.ClassificationCriteria getClassificationCriteria() {
        return classificationCriteria;
    }

    /**
     * Sets the value of the classificationCriteria property.
     * 
     * @param value
     *     allowed object is
     *     {@link CoS.ClassificationCriteria }
     *     
     */
    public void setClassificationCriteria(CoS.ClassificationCriteria value) {
        this.classificationCriteria = value;
    }

    /**
     * Gets the value of the bandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getBandwidth() {
        return bandwidth;
    }

    /**
     * Sets the value of the bandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setBandwidth(Bandwidth value) {
        this.bandwidth = value;
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

    /**
     * Gets the value of the isChildObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChildObjectInstanceModified() {
        return isChildObjectInstanceModified;
    }

    /**
     * Sets the value of the isChildObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChildObjectInstanceModified(Boolean value) {
        this.isChildObjectInstanceModified = value;
    }

    /**
     * Gets the value of the cosUpdateAction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCosUpdateAction() {
        return cosUpdateAction;
    }

    /**
     * Sets the value of the cosUpdateAction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCosUpdateAction(String value) {
        this.cosUpdateAction = value;
    }

    /**
     * Gets the value of the bandwidthinKBPS property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBandwidthinKBPS() {
        return bandwidthinKBPS;
    }

    /**
     * Sets the value of the bandwidthinKBPS property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBandwidthinKBPS(String value) {
        this.bandwidthinKBPS = value;
    }

    /**
     * Gets the value of the pirBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getPIRBandwidth() {
        return pirBandwidth;
    }

    /**
     * Sets the value of the pirBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setPIRBandwidth(Bandwidth value) {
        this.pirBandwidth = value;
    }

    /**
     * Gets the value of the isDefaultFC property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDefaultFC() {
        return isDefaultFC;
    }

    /**
     * Sets the value of the isDefaultFC property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDefaultFC(Boolean value) {
        this.isDefaultFC = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="accessControlList" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList"/>
     *         &lt;element name="dscpValue" type="{http://www.tcl.com/2011/11/ipsvc/xsd}DscpValue"/>
     *         &lt;element name="ipprecedent" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IPPrecedent"/>
     *         &lt;element name="Any" minOccurs="0">
     *           &lt;simpleType>
     *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
     *               &lt;enumeration value="ANY"/>
     *             &lt;/restriction>
     *           &lt;/simpleType>
     *         &lt;/element>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "accessControlList",
        "dscpValue",
        "ipprecedent",
        "any"
    })
    public static class ClassificationCriteria {

        protected AccessControlList accessControlList;
        protected DscpValue dscpValue;
        protected IPPrecedent ipprecedent;
        @XmlElement(name = "Any")
        protected String any;

        /**
         * Gets the value of the accessControlList property.
         * 
         * @return
         *     possible object is
         *     {@link AccessControlList }
         *     
         */
        public AccessControlList getAccessControlList() {
            return accessControlList;
        }

        /**
         * Sets the value of the accessControlList property.
         * 
         * @param value
         *     allowed object is
         *     {@link AccessControlList }
         *     
         */
        public void setAccessControlList(AccessControlList value) {
            this.accessControlList = value;
        }

        /**
         * Gets the value of the dscpValue property.
         * 
         * @return
         *     possible object is
         *     {@link DscpValue }
         *     
         */
        public DscpValue getDscpValue() {
            return dscpValue;
        }

        /**
         * Sets the value of the dscpValue property.
         * 
         * @param value
         *     allowed object is
         *     {@link DscpValue }
         *     
         */
        public void setDscpValue(DscpValue value) {
            this.dscpValue = value;
        }

        /**
         * Gets the value of the ipprecedent property.
         * 
         * @return
         *     possible object is
         *     {@link IPPrecedent }
         *     
         */
        public IPPrecedent getIpprecedent() {
            return ipprecedent;
        }

        /**
         * Sets the value of the ipprecedent property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPPrecedent }
         *     
         */
        public void setIpprecedent(IPPrecedent value) {
            this.ipprecedent = value;
        }

        /**
         * Gets the value of the any property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getAny() {
            return any;
        }

        /**
         * Sets the value of the any property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setAny(String value) {
            this.any = value;
        }

    }

}
