
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for HOCircuit complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HOCircuit">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HOTrail" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}Worker" minOccurs="0"/>
 *         &lt;element name="MSBList" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}Worker" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="VirtualBearer" type="{http://www.tcl.com/2011/11/netordsvc/xsd}VirtualBearer" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "HOCircuit", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "hoTrail",
    "msbList",
    "virtualBearer"
})
public class HOCircuit {

    @XmlElement(name = "HOTrail")
    protected Worker hoTrail;
    @XmlElement(name = "MSBList")
    protected List<Worker> msbList;
    @XmlElement(name = "VirtualBearer")
    protected List<VirtualBearer> virtualBearer;

    /**
     * Gets the value of the hoTrail property.
     * 
     * @return
     *     possible object is
     *     {@link Worker }
     *     
     */
    public Worker getHOTrail() {
        return hoTrail;
    }

    /**
     * Sets the value of the hoTrail property.
     * 
     * @param value
     *     allowed object is
     *     {@link Worker }
     *     
     */
    public void setHOTrail(Worker value) {
        this.hoTrail = value;
    }

    /**
     * Gets the value of the msbList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the msbList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMSBList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Worker }
     * 
     * 
     */
    public List<Worker> getMSBList() {
        if (msbList == null) {
            msbList = new ArrayList<Worker>();
        }
        return this.msbList;
    }

    /**
     * Gets the value of the virtualBearer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the virtualBearer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVirtualBearer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VirtualBearer }
     * 
     * 
     */
    public List<VirtualBearer> getVirtualBearer() {
        if (virtualBearer == null) {
            virtualBearer = new ArrayList<VirtualBearer>();
        }
        return this.virtualBearer;
    }

}
