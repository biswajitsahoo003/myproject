package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CienaParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CienaParam"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="finalAdminWeight" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="homeRoute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="secondaryRoute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="activationAdminWeight" type="{http://www.w3.org/2001/XMLSchema}long"/&gt;
 *         &lt;element name="dtlProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dtlProfileType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="primaryRouteAdminWt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="secondaryRouteAdminWt" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CienaParam", propOrder = {
    "finalAdminWeight",
    "homeRoute",
    "secondaryRoute",
    "activationAdminWeight",
    "dtlProfileName",
    "dtlProfileType",
    "primaryRouteAdminWt",
    "secondaryRouteAdminWt"
})
public class CienaParam {

    protected long finalAdminWeight;
    protected String homeRoute;
    protected String secondaryRoute;
    protected long activationAdminWeight;
    protected String dtlProfileName;
    @XmlElement(defaultValue = "SIGNAL_SNCP")
    protected String dtlProfileType;
    protected String primaryRouteAdminWt;
    protected String secondaryRouteAdminWt;

    /**
     * Gets the value of the finalAdminWeight property.
     * 
     */
    public long getFinalAdminWeight() {
        return finalAdminWeight;
    }

    /**
     * Sets the value of the finalAdminWeight property.
     * 
     */
    public void setFinalAdminWeight(long value) {
        this.finalAdminWeight = value;
    }

    /**
     * Gets the value of the homeRoute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeRoute() {
        return homeRoute;
    }

    /**
     * Sets the value of the homeRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeRoute(String value) {
        this.homeRoute = value;
    }

    /**
     * Gets the value of the secondaryRoute property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondaryRoute() {
        return secondaryRoute;
    }

    /**
     * Sets the value of the secondaryRoute property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondaryRoute(String value) {
        this.secondaryRoute = value;
    }

    /**
     * Gets the value of the activationAdminWeight property.
     * 
     */
    public long getActivationAdminWeight() {
        return activationAdminWeight;
    }

    /**
     * Sets the value of the activationAdminWeight property.
     * 
     */
    public void setActivationAdminWeight(long value) {
        this.activationAdminWeight = value;
    }

    /**
     * Gets the value of the dtlProfileName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDtlProfileName() {
        return dtlProfileName;
    }

    /**
     * Sets the value of the dtlProfileName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDtlProfileName(String value) {
        this.dtlProfileName = value;
    }

    /**
     * Gets the value of the dtlProfileType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDtlProfileType() {
        return dtlProfileType;
    }

    /**
     * Sets the value of the dtlProfileType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDtlProfileType(String value) {
        this.dtlProfileType = value;
    }

    /**
     * Gets the value of the primaryRouteAdminWt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPrimaryRouteAdminWt() {
        return primaryRouteAdminWt;
    }

    /**
     * Sets the value of the primaryRouteAdminWt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPrimaryRouteAdminWt(String value) {
        this.primaryRouteAdminWt = value;
    }

    /**
     * Gets the value of the secondaryRouteAdminWt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondaryRouteAdminWt() {
        return secondaryRouteAdminWt;
    }

    /**
     * Sets the value of the secondaryRouteAdminWt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondaryRouteAdminWt(String value) {
        this.secondaryRouteAdminWt = value;
    }

    @Override
    public String toString() {
        return "CienaParam{" +
                "finalAdminWeight=" + finalAdminWeight +
                ", homeRoute='" + homeRoute + '\'' +
                ", secondaryRoute='" + secondaryRoute + '\'' +
                ", activationAdminWeight=" + activationAdminWeight +
                ", dtlProfileName='" + dtlProfileName + '\'' +
                ", dtlProfileType='" + dtlProfileType + '\'' +
                ", primaryRouteAdminWt='" + primaryRouteAdminWt + '\'' +
                ", secondaryRouteAdminWt='" + secondaryRouteAdminWt + '\'' +
                '}';
    }
}
