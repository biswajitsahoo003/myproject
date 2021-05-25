
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sourceProtocol complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sourceProtocol">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sorceProtocolEntry" type="{http://www.tcl.com/2011/11/ipsvc/xsd}sorceProtocolEntry" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "sourceProtocol", propOrder = {
    "sorceProtocolEntry",
    "isObjectInstanceModified"
})
public class SourceProtocol {

    protected List<SorceProtocolEntry> sorceProtocolEntry;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the sorceProtocolEntry property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sorceProtocolEntry property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSorceProtocolEntry().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SorceProtocolEntry }
     * 
     * 
     */
    public List<SorceProtocolEntry> getSorceProtocolEntry() {
        if (sorceProtocolEntry == null) {
            sorceProtocolEntry = new ArrayList<SorceProtocolEntry>();
        }
        return this.sorceProtocolEntry;
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
