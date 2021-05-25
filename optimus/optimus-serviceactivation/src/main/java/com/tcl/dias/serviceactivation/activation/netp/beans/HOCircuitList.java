
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HOCircuitList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HOCircuitList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HOCircuit" type="{http://www.tcl.com/2011/11/netordsvc/xsd}HOCircuit" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HOCircuitList", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "hoCircuit"
})
public class HOCircuitList {

    @XmlElement(name = "HOCircuit")
    protected List<HOCircuit> hoCircuit;

    /**
     * Gets the value of the hoCircuit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the hoCircuit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getHOCircuit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link HOCircuit }
     * 
     * 
     */
    public List<HOCircuit> getHOCircuit() {
        if (hoCircuit == null) {
            hoCircuit = new ArrayList<HOCircuit>();
        }
        return this.hoCircuit;
    }

}
