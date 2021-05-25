
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TransmissionService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TransmissionService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="protections" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}Protection" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="topologies" type="{http://www.tcl.com/2011/11/ace/common/xsd}Topology" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="worker" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}Worker" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cienaParam" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}CienaParam" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isACEActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="isNOCActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="broadcast" type="{http://www.tcl.com/2011/11/transmissionsvc/xsd}Broadcast" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verifyAndActivate" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="L2Params" type="{http://www.tcl.com/2014/03/transmissionsvc/xsd}L2Params" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransmissionService", namespace = "http://www.tcl.com/2011/11/transmissionsvc/xsd", propOrder = {
    "protections",
    "topologies",
    "worker",
    "cienaParam",
    "isACEActionRequired",
    "isNOCActionRequired",
    "broadcast",
    "verifyAndActivate",
    "l2Params"
})
public class TransmissionService {

    protected List<Protection> protections;
    protected List<Topology> topologies;
    protected List<Worker> worker;
    protected List<CienaParam> cienaParam;
    protected boolean isACEActionRequired;
    protected boolean isNOCActionRequired;
    protected List<Broadcast> broadcast;
    protected Boolean verifyAndActivate;
    @XmlElement(name = "L2Params")
    protected List<L2Params> l2Params;

    /**
     * Gets the value of the protections property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the protections property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtections().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Protection }
     * 
     * 
     */
    public List<Protection> getProtections() {
        if (protections == null) {
            protections = new ArrayList<Protection>();
        }
        return this.protections;
    }

    /**
     * Gets the value of the topologies property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the topologies property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTopologies().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Topology }
     * 
     * 
     */
    public List<Topology> getTopologies() {
        if (topologies == null) {
            topologies = new ArrayList<Topology>();
        }
        return this.topologies;
    }

    /**
     * Gets the value of the worker property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the worker property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorker().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Worker }
     * 
     * 
     */
    public List<Worker> getWorker() {
        if (worker == null) {
            worker = new ArrayList<Worker>();
        }
        return this.worker;
    }

    /**
     * Gets the value of the cienaParam property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cienaParam property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCienaParam().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CienaParam }
     * 
     * 
     */
    public List<CienaParam> getCienaParam() {
        if (cienaParam == null) {
            cienaParam = new ArrayList<CienaParam>();
        }
        return this.cienaParam;
    }

    /**
     * Gets the value of the isACEActionRequired property.
     * 
     */
    public boolean isIsACEActionRequired() {
        return isACEActionRequired;
    }

    /**
     * Sets the value of the isACEActionRequired property.
     * 
     */
    public void setIsACEActionRequired(boolean value) {
        this.isACEActionRequired = value;
    }

    /**
     * Gets the value of the isNOCActionRequired property.
     * 
     */
    public boolean isIsNOCActionRequired() {
        return isNOCActionRequired;
    }

    /**
     * Sets the value of the isNOCActionRequired property.
     * 
     */
    public void setIsNOCActionRequired(boolean value) {
        this.isNOCActionRequired = value;
    }

    /**
     * Gets the value of the broadcast property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the broadcast property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBroadcast().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Broadcast }
     * 
     * 
     */
    public List<Broadcast> getBroadcast() {
        if (broadcast == null) {
            broadcast = new ArrayList<Broadcast>();
        }
        return this.broadcast;
    }

    /**
     * Gets the value of the verifyAndActivate property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isVerifyAndActivate() {
        return verifyAndActivate;
    }

    /**
     * Sets the value of the verifyAndActivate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setVerifyAndActivate(Boolean value) {
        this.verifyAndActivate = value;
    }

    /**
     * Gets the value of the l2Params property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the l2Params property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getL2Params().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link L2Params }
     * 
     * 
     */
    public List<L2Params> getL2Params() {
        if (l2Params == null) {
            l2Params = new ArrayList<L2Params>();
        }
        return this.l2Params;
    }

}
