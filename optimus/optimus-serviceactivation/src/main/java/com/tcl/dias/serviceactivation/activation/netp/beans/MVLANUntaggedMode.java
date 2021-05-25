
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for MVLANUntaggedMode complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="MVLANUntaggedMode">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="mvlanQoSProfile" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}VLAN-QOSProfile" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SSVLANTaggingEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MVLANUntaggedMode", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_01", propOrder = {
    "mvlanQoSProfile",
    "ssvlanTaggingEnabled"
})
public class MVLANUntaggedMode {

    protected List<VLANQOSProfile> mvlanQoSProfile;
    @XmlElement(name = "SSVLANTaggingEnabled")
    protected Boolean ssvlanTaggingEnabled;

    /**
     * Gets the value of the mvlanQoSProfile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mvlanQoSProfile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMvlanQoSProfile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VLANQOSProfile }
     * 
     * 
     */
    public List<VLANQOSProfile> getMvlanQoSProfile() {
        if (mvlanQoSProfile == null) {
            mvlanQoSProfile = new ArrayList<VLANQOSProfile>();
        }
        return this.mvlanQoSProfile;
    }

    /**
     * Gets the value of the ssvlanTaggingEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isSSVLANTaggingEnabled() {
        return ssvlanTaggingEnabled;
    }

    /**
     * Sets the value of the ssvlanTaggingEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setSSVLANTaggingEnabled(Boolean value) {
        this.ssvlanTaggingEnabled = value;
    }

}
