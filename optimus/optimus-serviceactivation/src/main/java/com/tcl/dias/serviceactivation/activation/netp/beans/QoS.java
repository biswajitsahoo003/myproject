
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for QoS complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QoS">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cosType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="4COS"/>
 *               &lt;enumeration value="6COS"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="cos" type="{http://www.tcl.com/2011/11/ipsvc/xsd}CoS" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="QoSProfile" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="STANDARD"/>
 *               &lt;enumeration value="PREMIUM"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isBandwidthApplicable" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="TRUE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="QoSTrafficMode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="UNICAST"/>
 *               &lt;enumeration value="MULTICAST"/>
 *               &lt;enumeration value="BOTH"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ALUSchedulerPolicy" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUSchedulerPolicy" minOccurs="0"/>
 *         &lt;element name="ALUIngressPolicy" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUSchedulerPolicy" minOccurs="0"/>
 *         &lt;element name="ALUEgressPolicy" type="{http://www.tcl.com/2014/3/ipsvc/xsd}ALUSchedulerPolicy" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QoS", propOrder = {
    "cosType",
    "cos",
    "qoSProfile",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "isBandwidthApplicable",
    "qoSTrafficMode",
    "aluSchedulerPolicy",
    "aluIngressPolicy",
    "aluEgressPolicy"
})
public class QoS {

    protected String cosType;
    protected List<CoS> cos;
    @XmlElement(name = "QoSProfile")
    protected String qoSProfile;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    protected String isBandwidthApplicable;
    @XmlElement(name = "QoSTrafficMode")
    protected String qoSTrafficMode;
    @XmlElement(name = "ALUSchedulerPolicy")
    protected ALUSchedulerPolicy aluSchedulerPolicy;
    @XmlElement(name = "ALUIngressPolicy")
    protected ALUSchedulerPolicy aluIngressPolicy;
    @XmlElement(name = "ALUEgressPolicy")
    protected ALUSchedulerPolicy aluEgressPolicy;

    /**
     * Gets the value of the cosType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCosType() {
        return cosType;
    }

    /**
     * Sets the value of the cosType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCosType(String value) {
        this.cosType = value;
    }

    /**
     * Gets the value of the cos property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cos property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCos().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CoS }
     * 
     * 
     */
    public List<CoS> getCos() {
        if (cos == null) {
            cos = new ArrayList<CoS>();
        }
        return this.cos;
    }

    /**
     * Gets the value of the qoSProfile property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQoSProfile() {
        return qoSProfile;
    }

    /**
     * Sets the value of the qoSProfile property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQoSProfile(String value) {
        this.qoSProfile = value;
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
     * Gets the value of the isBandwidthApplicable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsBandwidthApplicable() {
        return isBandwidthApplicable;
    }

    /**
     * Sets the value of the isBandwidthApplicable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsBandwidthApplicable(String value) {
        this.isBandwidthApplicable = value;
    }

    /**
     * Gets the value of the qoSTrafficMode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQoSTrafficMode() {
        return qoSTrafficMode;
    }

    /**
     * Sets the value of the qoSTrafficMode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQoSTrafficMode(String value) {
        this.qoSTrafficMode = value;
    }

    /**
     * Gets the value of the aluSchedulerPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link ALUSchedulerPolicy }
     *     
     */
    public ALUSchedulerPolicy getALUSchedulerPolicy() {
        return aluSchedulerPolicy;
    }

    /**
     * Sets the value of the aluSchedulerPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ALUSchedulerPolicy }
     *     
     */
    public void setALUSchedulerPolicy(ALUSchedulerPolicy value) {
        this.aluSchedulerPolicy = value;
    }

    /**
     * Gets the value of the aluIngressPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link ALUSchedulerPolicy }
     *     
     */
    public ALUSchedulerPolicy getALUIngressPolicy() {
        return aluIngressPolicy;
    }

    /**
     * Sets the value of the aluIngressPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ALUSchedulerPolicy }
     *     
     */
    public void setALUIngressPolicy(ALUSchedulerPolicy value) {
        this.aluIngressPolicy = value;
    }

    /**
     * Gets the value of the aluEgressPolicy property.
     * 
     * @return
     *     possible object is
     *     {@link ALUSchedulerPolicy }
     *     
     */
    public ALUSchedulerPolicy getALUEgressPolicy() {
        return aluEgressPolicy;
    }

    /**
     * Sets the value of the aluEgressPolicy property.
     * 
     * @param value
     *     allowed object is
     *     {@link ALUSchedulerPolicy }
     *     
     */
    public void setALUEgressPolicy(ALUSchedulerPolicy value) {
        this.aluEgressPolicy = value;
    }

}
