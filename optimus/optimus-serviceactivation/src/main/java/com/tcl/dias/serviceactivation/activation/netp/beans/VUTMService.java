
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VUTMService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VUTMService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="icgPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_01}ICGPath" minOccurs="0"/>
 *         &lt;element name="peVDOMPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_01}PEVDOMPath" minOccurs="0"/>
 *         &lt;element name="qos" type="{http://www.tcl.com/2011/11/ipsvc/xsd}QoS" minOccurs="0"/>
 *         &lt;element name="solutionTable" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}VPNSolutionTable" minOccurs="0"/>
 *         &lt;element name="InstanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentSiteID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="currentLegID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="burstableBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceLink" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ServiceLink" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="redundancyRole" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CSSSAMID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VUTMService", propOrder = {
    "serviceType",
    "serviceSubType",
    "icgPath",
    "peVDOMPath",
    "qos",
    "solutionTable",
    "instanceID",
    "currentSiteID",
    "currentLegID",
    "serviceBandwidth",
    "burstableBandwidth",
    "serviceId",
    "serviceLink",
    "isObjectInstanceModified",
    "redundancyRole",
    "csssamid"
})
public class VUTMService {

    @XmlElement(name = "ServiceType")
    protected String serviceType;
    @XmlElement(name = "ServiceSubType")
    protected String serviceSubType;
    protected ICGPath icgPath;
    protected PEVDOMPath peVDOMPath;
    protected QoS qos;
    protected VPNSolutionTable solutionTable;
    @XmlElement(name = "InstanceID")
    protected String instanceID;
    protected String currentSiteID;
    protected String currentLegID;
    protected Bandwidth serviceBandwidth;
    protected Bandwidth burstableBandwidth;
    protected String serviceId;
    protected List<ServiceLink> serviceLink;
    protected Boolean isObjectInstanceModified;
    protected String redundancyRole;
    @XmlElement(name = "CSSSAMID")
    protected String csssamid;

    /**
     * Gets the value of the serviceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * Sets the value of the serviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceType(String value) {
        this.serviceType = value;
    }

    /**
     * Gets the value of the serviceSubType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceSubType() {
        return serviceSubType;
    }

    /**
     * Sets the value of the serviceSubType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceSubType(String value) {
        this.serviceSubType = value;
    }

    /**
     * Gets the value of the icgPath property.
     * 
     * @return
     *     possible object is
     *     {@link ICGPath }
     *     
     */
    public ICGPath getIcgPath() {
        return icgPath;
    }

    /**
     * Sets the value of the icgPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link ICGPath }
     *     
     */
    public void setIcgPath(ICGPath value) {
        this.icgPath = value;
    }

    /**
     * Gets the value of the peVDOMPath property.
     * 
     * @return
     *     possible object is
     *     {@link PEVDOMPath }
     *     
     */
    public PEVDOMPath getPeVDOMPath() {
        return peVDOMPath;
    }

    /**
     * Sets the value of the peVDOMPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link PEVDOMPath }
     *     
     */
    public void setPeVDOMPath(PEVDOMPath value) {
        this.peVDOMPath = value;
    }

    /**
     * Gets the value of the qos property.
     * 
     * @return
     *     possible object is
     *     {@link QoS }
     *     
     */
    public QoS getQos() {
        return qos;
    }

    /**
     * Sets the value of the qos property.
     * 
     * @param value
     *     allowed object is
     *     {@link QoS }
     *     
     */
    public void setQos(QoS value) {
        this.qos = value;
    }

    /**
     * Gets the value of the solutionTable property.
     * 
     * @return
     *     possible object is
     *     {@link VPNSolutionTable }
     *     
     */
    public VPNSolutionTable getSolutionTable() {
        return solutionTable;
    }

    /**
     * Sets the value of the solutionTable property.
     * 
     * @param value
     *     allowed object is
     *     {@link VPNSolutionTable }
     *     
     */
    public void setSolutionTable(VPNSolutionTable value) {
        this.solutionTable = value;
    }

    /**
     * Gets the value of the instanceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInstanceID() {
        return instanceID;
    }

    /**
     * Sets the value of the instanceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInstanceID(String value) {
        this.instanceID = value;
    }

    /**
     * Gets the value of the currentSiteID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentSiteID() {
        return currentSiteID;
    }

    /**
     * Sets the value of the currentSiteID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentSiteID(String value) {
        this.currentSiteID = value;
    }

    /**
     * Gets the value of the currentLegID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrentLegID() {
        return currentLegID;
    }

    /**
     * Sets the value of the currentLegID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrentLegID(String value) {
        this.currentLegID = value;
    }

    /**
     * Gets the value of the serviceBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getServiceBandwidth() {
        return serviceBandwidth;
    }

    /**
     * Sets the value of the serviceBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setServiceBandwidth(Bandwidth value) {
        this.serviceBandwidth = value;
    }

    /**
     * Gets the value of the burstableBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getBurstableBandwidth() {
        return burstableBandwidth;
    }

    /**
     * Sets the value of the burstableBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setBurstableBandwidth(Bandwidth value) {
        this.burstableBandwidth = value;
    }

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the serviceLink property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the serviceLink property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getServiceLink().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ServiceLink }
     * 
     * 
     */
    public List<ServiceLink> getServiceLink() {
        if (serviceLink == null) {
            serviceLink = new ArrayList<ServiceLink>();
        }
        return this.serviceLink;
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

    /**
     * Gets the value of the redundancyRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRedundancyRole() {
        return redundancyRole;
    }

    /**
     * Sets the value of the redundancyRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRedundancyRole(String value) {
        this.redundancyRole = value;
    }

    /**
     * Gets the value of the csssamid property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCSSSAMID() {
        return csssamid;
    }

    /**
     * Sets the value of the csssamid property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCSSSAMID(String value) {
        this.csssamid = value;
    }

}
