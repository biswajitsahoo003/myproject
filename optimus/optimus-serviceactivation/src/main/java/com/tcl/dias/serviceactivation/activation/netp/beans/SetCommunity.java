
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for setCommunity complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="setCommunity">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="isExtendedCommunity" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="setCommunityListEntry" type="{http://www.tcl.com/2011/11/ipsvc/xsd}setCommunityListEntry" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "setCommunity", propOrder = {
    "isExtendedCommunity",
    "setCommunityListEntry",
    "isObjectInstanceModified"
})
public class SetCommunity {

    protected Boolean isExtendedCommunity;
    protected List<SetCommunityListEntry> setCommunityListEntry;
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
     * Gets the value of the setCommunityListEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the setCommunityListEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSetCommunityListEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SetCommunityListEntry }
     * 
     * 
     */
    public List<SetCommunityListEntry> getSetCommunityListEntry() {
        if (setCommunityListEntry == null) {
            setCommunityListEntry = new ArrayList<SetCommunityListEntry>();
        }
        return this.setCommunityListEntry;
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
