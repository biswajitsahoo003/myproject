package com.tcl.dias.serviceactivation.cramer.getclrasync.beans;

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
 * &lt;complexType name="TransmissionService"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="isACEActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="isCienaActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="isNOCActionRequired" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="nodeToConfigure" type="{http://www.tcl.com/2012/09/csvc/xsd}NodeToBeConfigured" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="protections" type="{http://www.tcl.com/2012/09/csvc/xsd}Protection" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="topologies" type="{http://www.tcl.com/2012/09/csvc/xsd}Topology" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="worker" type="{http://www.tcl.com/2012/09/csvc/xsd}Worker" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="cienaParam" type="{http://www.tcl.com/2012/09/csvc/xsd}CienaParam" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="broadcast" type="{http://ACE_Common_Lib/com/tcl/www/_2012/_09/csvc/xsd}Broadcast" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="L2Params" type="{http://www.tcl.com/2014/03/transmissionsvc/xsd}L2Params" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TransmissionService", propOrder = {
    "isACEActionRequired",
    "isCienaActionRequired",
    "isNOCActionRequired",
    "nodeToConfigure",
    "protections",
    "topologies",
    "worker",
    "cienaParam",
    "broadcast",
    "l2Params"
})
public class TransmissionService {

    protected boolean isACEActionRequired;
    protected boolean isCienaActionRequired;
    protected boolean isNOCActionRequired;
    protected List<NodeToBeConfigured> nodeToConfigure;
    protected List<Protection> protections;
    protected List<Topology> topologies;
    protected List<Worker> worker;
    protected List<CienaParam> cienaParam;
    protected List<Broadcast> broadcast;
    @XmlElement(name = "L2Params")
    protected List<L2Params> l2Params;

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
     * Gets the value of the isCienaActionRequired property.
     * 
     */
    public boolean isIsCienaActionRequired() {
        return isCienaActionRequired;
    }

    /**
     * Sets the value of the isCienaActionRequired property.
     * 
     */
    public void setIsCienaActionRequired(boolean value) {
        this.isCienaActionRequired = value;
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
     * Gets the value of the nodeToConfigure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodeToConfigure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodeToConfigure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NodeToBeConfigured }
     * 
     * 
     */
    public List<NodeToBeConfigured> getNodeToConfigure() {
        if (nodeToConfigure == null) {
            nodeToConfigure = new ArrayList<NodeToBeConfigured>();
        }
        return this.nodeToConfigure;
    }

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

    @Override
    public String toString() {
        return "TransmissionService{" +
                "isACEActionRequired=" + isACEActionRequired +
                ", isCienaActionRequired=" + isCienaActionRequired +
                ", isNOCActionRequired=" + isNOCActionRequired +
                ", nodeToConfigure=" + nodeToConfigure +
                ", protections=" + protections +
                ", topologies=" + topologies +
                ", worker=" + worker +
                ", cienaParam=" + cienaParam +
                ", broadcast=" + broadcast +
                ", l2Params=" + l2Params +
                '}';
    }
}
