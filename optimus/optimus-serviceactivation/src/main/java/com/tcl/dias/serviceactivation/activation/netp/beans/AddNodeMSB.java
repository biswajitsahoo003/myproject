
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AddNodeMSB complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AddNodeMSB">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ManagedElement" type="{http://www.tcl.com/2011/11/netordsvc/xsd}ManagedElement" minOccurs="0"/>
 *         &lt;element name="FibcomVC4List" type="{http://www.tcl.com/2011/11/netordsvc/xsd}FibcomVC4List" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="HOCircuitList" type="{http://www.tcl.com/2011/11/netordsvc/xsd}HOCircuitList" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AddNodeMSB", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "managedElement",
    "fibcomVC4List",
    "hoCircuitList"
})
public class AddNodeMSB {

    @XmlElement(name = "ManagedElement")
    protected ManagedElement managedElement;
    @XmlElement(name = "FibcomVC4List")
    protected List<FibcomVC4List> fibcomVC4List;
    @XmlElement(name = "HOCircuitList")
    protected HOCircuitList hoCircuitList;

    /**
     * Gets the value of the managedElement property.
     * 
     * @return
     *     possible object is
     *     {@link ManagedElement }
     *     
     */
    public ManagedElement getManagedElement() {
        return managedElement;
    }

    /**
     * Sets the value of the managedElement property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagedElement }
     *     
     */
    public void setManagedElement(ManagedElement value) {
        this.managedElement = value;
    }

    /**
     * Gets the value of the fibcomVC4List property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fibcomVC4List property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFibcomVC4List().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link FibcomVC4List }
     * 
     * 
     */
    public List<FibcomVC4List> getFibcomVC4List() {
        if (fibcomVC4List == null) {
            fibcomVC4List = new ArrayList<FibcomVC4List>();
        }
        return this.fibcomVC4List;
    }

    /**
     * Gets the value of the hoCircuitList property.
     * 
     * @return
     *     possible object is
     *     {@link HOCircuitList }
     *     
     */
    public HOCircuitList getHOCircuitList() {
        return hoCircuitList;
    }

    /**
     * Sets the value of the hoCircuitList property.
     * 
     * @param value
     *     allowed object is
     *     {@link HOCircuitList }
     *     
     */
    public void setHOCircuitList(HOCircuitList value) {
        this.hoCircuitList = value;
    }

}
