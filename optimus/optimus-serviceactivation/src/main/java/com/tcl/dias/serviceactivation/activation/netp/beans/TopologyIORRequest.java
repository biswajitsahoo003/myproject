
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for TopologyIORRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="TopologyIORRequest">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="TopologyList" type="{http://www.tcl.com/2011/11/ace/common/xsd}Topology" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="nodeUploadList" type="{http://www.tcl.com/2011/11/netordsvc/xsd}ManagedElement" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="OrderType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="LINKUPLOAD"/>
 *               &lt;enumeration value="TOPOLOGYUPLOAD"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
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
@XmlType(name = "TopologyIORRequest", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2011/_11", propOrder = {
    "topologyList",
    "nodeUploadList",
    "orderType",
    "isObjectInstanceModified"
})
public class TopologyIORRequest {

    @XmlElement(name = "TopologyList")
    protected List<Topology> topologyList;
    protected List<ManagedElement> nodeUploadList;
    @XmlElement(name = "OrderType")
    protected String orderType;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the topologyList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the topologyList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTopologyList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Topology }
     * 
     * 
     */
    public List<Topology> getTopologyList() {
        if (topologyList == null) {
            topologyList = new ArrayList<Topology>();
        }
        return this.topologyList;
    }

    /**
     * Gets the value of the nodeUploadList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nodeUploadList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNodeUploadList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ManagedElement }
     * 
     * 
     */
    public List<ManagedElement> getNodeUploadList() {
        if (nodeUploadList == null) {
            nodeUploadList = new ArrayList<ManagedElement>();
        }
        return this.nodeUploadList;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderType(String value) {
        this.orderType = value;
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
