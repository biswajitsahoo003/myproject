
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VLAN_NUMBER complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VLAN_NUMBER"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="VLAN_NUMBER" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VLAN_NUMBER", propOrder = {
    "vlannumber"
})
public class VLANNUMBER {

    @XmlElement(name = "VLAN_NUMBER")
    protected String vlannumber;

    /**
     * Gets the value of the vlannumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVLANNUMBER() {
        return vlannumber;
    }

    /**
     * Sets the value of the vlannumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVLANNUMBER(String value) {
        this.vlannumber = value;
    }

}
