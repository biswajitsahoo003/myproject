
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CienaParam complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CienaParam">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dtlProfileName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="finalAdminWeight" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="homeRoute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="profileType" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}ProfileType" minOccurs="0"/>
 *         &lt;element name="secondaryRoute" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="activationAdminWeight" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *         &lt;element name="dualRouteInfo" type="{http://www.tcl.com/2014/11/transmissionsvc/xsd}CienaDualRouteParams" minOccurs="0"/>
 *         &lt;element name="homeRouteAdminWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="secondaryRouteAdminWeight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CienaParam", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd", propOrder = {
    "dtlProfileName",
    "finalAdminWeight",
    "homeRoute",
    "profileType",
    "secondaryRoute",
    "activationAdminWeight",
    "dualRouteInfo",
    "homeRouteAdminWeight",
    "secondaryRouteAdminWeight"
})
public class CienaParam {

    protected String dtlProfileName;
    protected Long finalAdminWeight;
    protected String homeRoute;
    @XmlSchemaType(name = "string")
    protected ProfileType profileType;
    protected String secondaryRoute;
    protected Long activationAdminWeight;
    protected CienaDualRouteParams dualRouteInfo;
    protected String homeRouteAdminWeight;
    protected String secondaryRouteAdminWeight;

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
     * Gets the value of the finalAdminWeight property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getFinalAdminWeight() {
        return finalAdminWeight;
    }

    /**
     * Sets the value of the finalAdminWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setFinalAdminWeight(Long value) {
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
     * Gets the value of the profileType property.
     * 
     * @return
     *     possible object is
     *     {@link ProfileType }
     *     
     */
    public ProfileType getProfileType() {
        return profileType;
    }

    /**
     * Sets the value of the profileType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ProfileType }
     *     
     */
    public void setProfileType(ProfileType value) {
        this.profileType = value;
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
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getActivationAdminWeight() {
        return activationAdminWeight;
    }

    /**
     * Sets the value of the activationAdminWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setActivationAdminWeight(Long value) {
        this.activationAdminWeight = value;
    }

    /**
     * Gets the value of the dualRouteInfo property.
     * 
     * @return
     *     possible object is
     *     {@link CienaDualRouteParams }
     *     
     */
    public CienaDualRouteParams getDualRouteInfo() {
        return dualRouteInfo;
    }

    /**
     * Sets the value of the dualRouteInfo property.
     * 
     * @param value
     *     allowed object is
     *     {@link CienaDualRouteParams }
     *     
     */
    public void setDualRouteInfo(CienaDualRouteParams value) {
        this.dualRouteInfo = value;
    }

    /**
     * Gets the value of the homeRouteAdminWeight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHomeRouteAdminWeight() {
        return homeRouteAdminWeight;
    }

    /**
     * Sets the value of the homeRouteAdminWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHomeRouteAdminWeight(String value) {
        this.homeRouteAdminWeight = value;
    }

    /**
     * Gets the value of the secondaryRouteAdminWeight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecondaryRouteAdminWeight() {
        return secondaryRouteAdminWeight;
    }

    /**
     * Sets the value of the secondaryRouteAdminWeight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecondaryRouteAdminWeight(String value) {
        this.secondaryRouteAdminWeight = value;
    }

}
