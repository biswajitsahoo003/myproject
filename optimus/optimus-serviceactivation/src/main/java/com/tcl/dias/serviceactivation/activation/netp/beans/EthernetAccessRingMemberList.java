
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EthernetAccessRingMemberList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EthernetAccessRingMemberList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ethernetRingMember" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSNodePortDetails" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EthernetAccessRingMemberList", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "ethernetRingMember",
    "name"
})
public class EthernetAccessRingMemberList {

    protected List<HuaweiERPSNodePortDetails> ethernetRingMember;
    protected String name;

    /**
     * Gets the value of the ethernetRingMember property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ethernetRingMember property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEthernetRingMember().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HuaweiERPSNodePortDetails }
     * 
     * 
     */
    public List<HuaweiERPSNodePortDetails> getEthernetRingMember() {
        if (ethernetRingMember == null) {
            ethernetRingMember = new ArrayList<HuaweiERPSNodePortDetails>();
        }
        return this.ethernetRingMember;
    }

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

}
