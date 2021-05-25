
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				Request contains all information related to the request
 * 				that has been raised. For example if the request is for
 * 				node insertion , request will contain the parameters
 * 				related to node insertion
 * 			
 * 
 * <p>Java class for EORequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EORequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="topology" type="{http://www.tcl.com/2011/11/ace/common/xsd}Topology" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="meDetails" type="{http://www.tcl.com/2011/11/netordsvc/xsd}ManagedElement" minOccurs="0"/>
 *         &lt;element name="insertNode" type="{http://www.tcl.com/2011/11/netordsvc/xsd}CircuitList" minOccurs="0"/>
 *         &lt;element name="deleteNode" type="{http://www.tcl.com/2011/11/netordsvc/xsd}CircuitList" minOccurs="0"/>
 *         &lt;element name="addConnectionList" type="{http://www.tcl.com/2011/11/netordsvc/xsd}CircuitList" minOccurs="0"/>
 *         &lt;element name="fibcomVC4List" type="{http://www.tcl.com/2011/11/netordsvc/xsd}FibcomVC4List" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EORequest", namespace = "http://www.tcl.com/2011/11/netordsvc/xsd", propOrder = {
    "topology",
    "meDetails",
    "insertNode",
    "deleteNode",
    "addConnectionList",
    "fibcomVC4List"
})
public class EORequest {

    protected List<Topology> topology;
    protected ManagedElement meDetails;
    protected CircuitList insertNode;
    protected CircuitList deleteNode;
    protected CircuitList addConnectionList;
    protected List<FibcomVC4List> fibcomVC4List;

    /**
     * Gets the value of the topology property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the topology property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTopology().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Topology }
     * 
     * 
     */
    public List<Topology> getTopology() {
        if (topology == null) {
            topology = new ArrayList<Topology>();
        }
        return this.topology;
    }

    /**
     * Gets the value of the meDetails property.
     * 
     * @return
     *     possible object is
     *     {@link ManagedElement }
     *     
     */
    public ManagedElement getMeDetails() {
        return meDetails;
    }

    /**
     * Sets the value of the meDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link ManagedElement }
     *     
     */
    public void setMeDetails(ManagedElement value) {
        this.meDetails = value;
    }

    /**
     * Gets the value of the insertNode property.
     * 
     * @return
     *     possible object is
     *     {@link CircuitList }
     *     
     */
    public CircuitList getInsertNode() {
        return insertNode;
    }

    /**
     * Sets the value of the insertNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CircuitList }
     *     
     */
    public void setInsertNode(CircuitList value) {
        this.insertNode = value;
    }

    /**
     * Gets the value of the deleteNode property.
     * 
     * @return
     *     possible object is
     *     {@link CircuitList }
     *     
     */
    public CircuitList getDeleteNode() {
        return deleteNode;
    }

    /**
     * Sets the value of the deleteNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CircuitList }
     *     
     */
    public void setDeleteNode(CircuitList value) {
        this.deleteNode = value;
    }

    /**
     * Gets the value of the addConnectionList property.
     * 
     * @return
     *     possible object is
     *     {@link CircuitList }
     *     
     */
    public CircuitList getAddConnectionList() {
        return addConnectionList;
    }

    /**
     * Sets the value of the addConnectionList property.
     * 
     * @param value
     *     allowed object is
     *     {@link CircuitList }
     *     
     */
    public void setAddConnectionList(CircuitList value) {
        this.addConnectionList = value;
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

}
