
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for matchCommunity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="matchCommunity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isExtendedCommunity" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="communityList" type="{http://www.tcl.com/2011/11/ipsvc/xsd}communityList" minOccurs="0"/>
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
@XmlType(name = "matchCommunity", propOrder = {
    "isExtendedCommunity",
    "communityList",
    "isObjectInstanceModified"
})
public class MatchCommunity {

    protected Boolean isExtendedCommunity;
    protected CommunityList communityList;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the isExtendedCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsExtendedCommunity() {
        return isExtendedCommunity;
    }

    /**
     * Sets the value of the isExtendedCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsExtendedCommunity(Boolean value) {
        this.isExtendedCommunity = value;
    }

    /**
     * Gets the value of the communityList property.
     * 
     * @return
     *     possible object is
     *     {@link CommunityList }
     *     
     */
    public CommunityList getCommunityList() {
        return communityList;
    }

    /**
     * Sets the value of the communityList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CommunityList }
     *     
     */
    public void setCommunityList(CommunityList value) {
        this.communityList = value;
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
