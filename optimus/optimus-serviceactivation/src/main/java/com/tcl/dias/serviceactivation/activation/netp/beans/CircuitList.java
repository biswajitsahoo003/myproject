
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CircuitList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CircuitList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="circuit" type="{http://www.tcl.com/2011/11/netordsvc/xsd}Circuit" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CircuitList", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "circuit"
})
public class CircuitList {

    protected List<Circuit> circuit;

    /**
     * Gets the value of the circuit property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the circuit property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCircuit().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Circuit }
     * 
     * 
     */
    public List<Circuit> getCircuit() {
        if (circuit == null) {
            circuit = new ArrayList<Circuit>();
        }
        return this.circuit;
    }

}
