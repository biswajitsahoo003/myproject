
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="serviceType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="burstableBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="usageModel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataTransferCommit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dataTransferCommitUnit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="descriptionFreeText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="redundancyRole" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;whiteSpace value="collapse"/>
 *               &lt;enumeration value="PRIMARY"/>
 *               &lt;enumeration value="SECONDARY"/>
 *               &lt;enumeration value="SINGLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="serviceLink" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ServiceLink" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isConfigManaged" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="wanV4Addresses" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lanV4Addresses" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="wanV6Addresses" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="lanV6Addresses" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Router" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="irsRouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IRSRouter" minOccurs="0"/>
 *         &lt;element name="cpe" type="{http://www.tcl.com/2011/11/ipsvc/xsd}CustomerPremiseEquipment" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="qos" type="{http://www.tcl.com/2011/11/ipsvc/xsd}QoS" minOccurs="0"/>
 *         &lt;element name="solutionTable" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}VPNSolutionTable" minOccurs="0"/>
 *         &lt;element name="wanRoutingProtocol" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol"/>
 *                   &lt;element name="eigprRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EIGRPRoutingProtocol"/>
 *                   &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol"/>
 *                   &lt;element name="staticRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol"/>
 *                   &lt;element name="ripRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RIPRoutingProtocol" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="extendedLAN" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ExtendedLAN" minOccurs="0"/>
 *         &lt;element name="isAPSEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isIDCService" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="lastMile" type="{http://www.tcl.com/2011/11/ipsvc/xsd}LastMile" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="InstanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="L3NNIunManagedPartnerDevice" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PartnerDevice" minOccurs="0"/>
 *         &lt;element name="InterfaceDescriptionServiceTag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FlexiCOSIdentifier" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SAMCustomerDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CSSSAMID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUServiceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ALUServiceName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UAFlag" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="UAImportMap" type="{http://com/tcl/www/_2011/_11/ipsvc/xsd}UAImportMap" minOccurs="0"/>
 *         &lt;element name="UAExportMap" type="{http://www.tcl.com/2014/2/ipsvc/xsd}RoutingPolicy" minOccurs="0"/>
 *         &lt;element name="PEWANAdditionalStaticRoutes" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}StaticRoutes" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPService", propOrder = {
    "serviceType",
    "serviceSubType",
    "serviceBandwidth",
    "burstableBandwidth",
    "usageModel",
    "dataTransferCommit",
    "dataTransferCommitUnit",
    "descriptionFreeText",
    "redundancyRole",
    "serviceLink",
    "isConfigManaged",
    "wanV4Addresses",
    "lanV4Addresses",
    "wanV6Addresses",
    "lanV6Addresses",
    "router",
    "irsRouter",
    "cpe",
    "qos",
    "solutionTable",
    "wanRoutingProtocol",
    "extendedLAN",
    "isAPSEnabled",
    "isIDCService",
    "lastMile",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified",
    "instanceID",
    "l3NNIunManagedPartnerDevice",
    "interfaceDescriptionServiceTag",
    "flexiCOSIdentifier",
    "samCustomerDescription",
    "csssamid",
    "aluServiceID",
    "aluServiceName",
    "uaFlag",
    "uaImportMap",
    "uaExportMap",
    "pewanAdditionalStaticRoutes"
})
@XmlSeeAlso({
    HVPLS.class,
    GVPNService.class,
    IAService.class
})
public class IPService {

    protected String serviceType;
    protected String serviceSubType;
    protected Bandwidth serviceBandwidth;
    protected Bandwidth burstableBandwidth;
    protected String usageModel;
    protected String dataTransferCommit;
    protected String dataTransferCommitUnit;
    protected String descriptionFreeText;
    protected String redundancyRole;
    protected List<ServiceLink> serviceLink;
    protected Boolean isConfigManaged;
    protected List<IPV4Address> wanV4Addresses;
    protected List<IPV4Address> lanV4Addresses;
    protected List<IPV6Address> wanV6Addresses;
    protected List<IPV6Address> lanV6Addresses;
    @XmlElement(name = "Router")
    protected List<Router> router;
    protected IRSRouter irsRouter;
    protected List<CustomerPremiseEquipment> cpe;
    protected QoS qos;
    protected VPNSolutionTable solutionTable;
    protected IPService.WanRoutingProtocol wanRoutingProtocol;
    protected ExtendedLAN extendedLAN;
    protected Boolean isAPSEnabled;
    protected Boolean isIDCService;
    protected List<LastMile> lastMile;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;
    @XmlElement(name = "InstanceID")
    protected String instanceID;
    @XmlElement(name = "L3NNIunManagedPartnerDevice")
    protected PartnerDevice l3NNIunManagedPartnerDevice;
    @XmlElement(name = "InterfaceDescriptionServiceTag")
    protected String interfaceDescriptionServiceTag;
    @XmlElement(name = "FlexiCOSIdentifier")
    protected String flexiCOSIdentifier;
    @XmlElement(name = "SAMCustomerDescription")
    protected String samCustomerDescription;
    @XmlElement(name = "CSSSAMID")
    protected String csssamid;
    @XmlElement(name = "ALUServiceID")
    protected String aluServiceID;
    @XmlElement(name = "ALUServiceName")
    protected String aluServiceName;
    @XmlElement(name = "UAFlag")
    protected String uaFlag;
    @XmlElement(name = "UAImportMap")
    protected UAImportMap uaImportMap;
    @XmlElement(name = "UAExportMap")
    protected RoutingPolicy uaExportMap;
    @XmlElement(name = "PEWANAdditionalStaticRoutes")
    protected StaticRoutes pewanAdditionalStaticRoutes;

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
     * Gets the value of the usageModel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUsageModel() {
        return usageModel;
    }

    /**
     * Sets the value of the usageModel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUsageModel(String value) {
        this.usageModel = value;
    }

    /**
     * Gets the value of the dataTransferCommit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataTransferCommit() {
        return dataTransferCommit;
    }

    /**
     * Sets the value of the dataTransferCommit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataTransferCommit(String value) {
        this.dataTransferCommit = value;
    }

    /**
     * Gets the value of the dataTransferCommitUnit property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataTransferCommitUnit() {
        return dataTransferCommitUnit;
    }

    /**
     * Sets the value of the dataTransferCommitUnit property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataTransferCommitUnit(String value) {
        this.dataTransferCommitUnit = value;
    }

    /**
     * Gets the value of the descriptionFreeText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescriptionFreeText() {
        return descriptionFreeText;
    }

    /**
     * Sets the value of the descriptionFreeText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescriptionFreeText(String value) {
        this.descriptionFreeText = value;
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
     * Gets the value of the isConfigManaged property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsConfigManaged() {
        return isConfigManaged;
    }

    /**
     * Sets the value of the isConfigManaged property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsConfigManaged(Boolean value) {
        this.isConfigManaged = value;
    }

    /**
     * Gets the value of the wanV4Addresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wanV4Addresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWanV4Addresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPV4Address }
     * 
     * 
     */
    public List<IPV4Address> getWanV4Addresses() {
        if (wanV4Addresses == null) {
            wanV4Addresses = new ArrayList<IPV4Address>();
        }
        return this.wanV4Addresses;
    }

    /**
     * Gets the value of the lanV4Addresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lanV4Addresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanV4Addresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPV4Address }
     * 
     * 
     */
    public List<IPV4Address> getLanV4Addresses() {
        if (lanV4Addresses == null) {
            lanV4Addresses = new ArrayList<IPV4Address>();
        }
        return this.lanV4Addresses;
    }

    /**
     * Gets the value of the wanV6Addresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the wanV6Addresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWanV6Addresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPV6Address }
     * 
     * 
     */
    public List<IPV6Address> getWanV6Addresses() {
        if (wanV6Addresses == null) {
            wanV6Addresses = new ArrayList<IPV6Address>();
        }
        return this.wanV6Addresses;
    }

    /**
     * Gets the value of the lanV6Addresses property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lanV6Addresses property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLanV6Addresses().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPV6Address }
     * 
     * 
     */
    public List<IPV6Address> getLanV6Addresses() {
        if (lanV6Addresses == null) {
            lanV6Addresses = new ArrayList<IPV6Address>();
        }
        return this.lanV6Addresses;
    }

    /**
     * Gets the value of the router property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the router property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRouter().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Router }
     * 
     * 
     */
    public List<Router> getRouter() {
        if (router == null) {
            router = new ArrayList<Router>();
        }
        return this.router;
    }

    /**
     * Gets the value of the irsRouter property.
     * 
     * @return
     *     possible object is
     *     {@link IRSRouter }
     *     
     */
    public IRSRouter getIrsRouter() {
        return irsRouter;
    }

    /**
     * Sets the value of the irsRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link IRSRouter }
     *     
     */
    public void setIrsRouter(IRSRouter value) {
        this.irsRouter = value;
    }

    /**
     * Gets the value of the cpe property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cpe property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCpe().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CustomerPremiseEquipment }
     * 
     * 
     */
    public List<CustomerPremiseEquipment> getCpe() {
        if (cpe == null) {
            cpe = new ArrayList<CustomerPremiseEquipment>();
        }
        return this.cpe;
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
     * Gets the value of the wanRoutingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link IPService.WanRoutingProtocol }
     *     
     */
    public IPService.WanRoutingProtocol getWanRoutingProtocol() {
        return wanRoutingProtocol;
    }

    /**
     * Sets the value of the wanRoutingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPService.WanRoutingProtocol }
     *     
     */
    public void setWanRoutingProtocol(IPService.WanRoutingProtocol value) {
        this.wanRoutingProtocol = value;
    }

    /**
     * Gets the value of the extendedLAN property.
     * 
     * @return
     *     possible object is
     *     {@link ExtendedLAN }
     *     
     */
    public ExtendedLAN getExtendedLAN() {
        return extendedLAN;
    }

    /**
     * Sets the value of the extendedLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link ExtendedLAN }
     *     
     */
    public void setExtendedLAN(ExtendedLAN value) {
        this.extendedLAN = value;
    }

    /**
     * Gets the value of the isAPSEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsAPSEnabled() {
        return isAPSEnabled;
    }

    /**
     * Sets the value of the isAPSEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsAPSEnabled(Boolean value) {
        this.isAPSEnabled = value;
    }

    /**
     * Gets the value of the isIDCService property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsIDCService() {
        return isIDCService;
    }

    /**
     * Sets the value of the isIDCService property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsIDCService(Boolean value) {
        this.isIDCService = value;
    }

    /**
     * Gets the value of the lastMile property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lastMile property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLastMile().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LastMile }
     * 
     * 
     */
    public List<LastMile> getLastMile() {
        if (lastMile == null) {
            lastMile = new ArrayList<LastMile>();
        }
        return this.lastMile;
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
     * Gets the value of the isChildObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChildObjectInstanceModified() {
        return isChildObjectInstanceModified;
    }

    /**
     * Sets the value of the isChildObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChildObjectInstanceModified(Boolean value) {
        this.isChildObjectInstanceModified = value;
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
     * Gets the value of the l3NNIunManagedPartnerDevice property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerDevice }
     *     
     */
    public PartnerDevice getL3NNIunManagedPartnerDevice() {
        return l3NNIunManagedPartnerDevice;
    }

    /**
     * Sets the value of the l3NNIunManagedPartnerDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerDevice }
     *     
     */
    public void setL3NNIunManagedPartnerDevice(PartnerDevice value) {
        this.l3NNIunManagedPartnerDevice = value;
    }

    /**
     * Gets the value of the interfaceDescriptionServiceTag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInterfaceDescriptionServiceTag() {
        return interfaceDescriptionServiceTag;
    }

    /**
     * Sets the value of the interfaceDescriptionServiceTag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInterfaceDescriptionServiceTag(String value) {
        this.interfaceDescriptionServiceTag = value;
    }

    /**
     * Gets the value of the flexiCOSIdentifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFlexiCOSIdentifier() {
        return flexiCOSIdentifier;
    }

    /**
     * Sets the value of the flexiCOSIdentifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFlexiCOSIdentifier(String value) {
        this.flexiCOSIdentifier = value;
    }

    /**
     * Gets the value of the samCustomerDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSAMCustomerDescription() {
        return samCustomerDescription;
    }

    /**
     * Sets the value of the samCustomerDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSAMCustomerDescription(String value) {
        this.samCustomerDescription = value;
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

    /**
     * Gets the value of the aluServiceID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUServiceID() {
        return aluServiceID;
    }

    /**
     * Sets the value of the aluServiceID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUServiceID(String value) {
        this.aluServiceID = value;
    }

    /**
     * Gets the value of the aluServiceName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getALUServiceName() {
        return aluServiceName;
    }

    /**
     * Sets the value of the aluServiceName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setALUServiceName(String value) {
        this.aluServiceName = value;
    }

    /**
     * Gets the value of the uaFlag property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUAFlag() {
        return uaFlag;
    }

    /**
     * Sets the value of the uaFlag property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUAFlag(String value) {
        this.uaFlag = value;
    }

    /**
     * Gets the value of the uaImportMap property.
     * 
     * @return
     *     possible object is
     *     {@link UAImportMap }
     *     
     */
    public UAImportMap getUAImportMap() {
        return uaImportMap;
    }

    /**
     * Sets the value of the uaImportMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link UAImportMap }
     *     
     */
    public void setUAImportMap(UAImportMap value) {
        this.uaImportMap = value;
    }

    /**
     * Gets the value of the uaExportMap property.
     * 
     * @return
     *     possible object is
     *     {@link RoutingPolicy }
     *     
     */
    public RoutingPolicy getUAExportMap() {
        return uaExportMap;
    }

    /**
     * Sets the value of the uaExportMap property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoutingPolicy }
     *     
     */
    public void setUAExportMap(RoutingPolicy value) {
        this.uaExportMap = value;
    }

    /**
     * Gets the value of the pewanAdditionalStaticRoutes property.
     * 
     * @return
     *     possible object is
     *     {@link StaticRoutes }
     *     
     */
    public StaticRoutes getPEWANAdditionalStaticRoutes() {
        return pewanAdditionalStaticRoutes;
    }

    /**
     * Sets the value of the pewanAdditionalStaticRoutes property.
     * 
     * @param value
     *     allowed object is
     *     {@link StaticRoutes }
     *     
     */
    public void setPEWANAdditionalStaticRoutes(StaticRoutes value) {
        this.pewanAdditionalStaticRoutes = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol"/>
     *         &lt;element name="eigprRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EIGRPRoutingProtocol"/>
     *         &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol"/>
     *         &lt;element name="staticRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol"/>
     *         &lt;element name="ripRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RIPRoutingProtocol" minOccurs="0"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "bgpRoutingProtocol",
        "eigprRoutingProtocol",
        "ospfRoutingProtocol",
        "staticRoutingProtocol",
        "ripRoutingProtocol"
    })
    public static class WanRoutingProtocol {

        protected BGPRoutingProtocol bgpRoutingProtocol;
        protected EIGRPRoutingProtocol eigprRoutingProtocol;
        protected OSPFRoutingProtocol ospfRoutingProtocol;
        protected StaticRoutingProtocol staticRoutingProtocol;
        protected RIPRoutingProtocol ripRoutingProtocol;

        /**
         * Gets the value of the bgpRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link BGPRoutingProtocol }
         *     
         */
        public BGPRoutingProtocol getBgpRoutingProtocol() {
            return bgpRoutingProtocol;
        }

        /**
         * Sets the value of the bgpRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link BGPRoutingProtocol }
         *     
         */
        public void setBgpRoutingProtocol(BGPRoutingProtocol value) {
            this.bgpRoutingProtocol = value;
        }

        /**
         * Gets the value of the eigprRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link EIGRPRoutingProtocol }
         *     
         */
        public EIGRPRoutingProtocol getEigprRoutingProtocol() {
            return eigprRoutingProtocol;
        }

        /**
         * Sets the value of the eigprRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link EIGRPRoutingProtocol }
         *     
         */
        public void setEigprRoutingProtocol(EIGRPRoutingProtocol value) {
            this.eigprRoutingProtocol = value;
        }

        /**
         * Gets the value of the ospfRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link OSPFRoutingProtocol }
         *     
         */
        public OSPFRoutingProtocol getOspfRoutingProtocol() {
            return ospfRoutingProtocol;
        }

        /**
         * Sets the value of the ospfRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link OSPFRoutingProtocol }
         *     
         */
        public void setOspfRoutingProtocol(OSPFRoutingProtocol value) {
            this.ospfRoutingProtocol = value;
        }

        /**
         * Gets the value of the staticRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link StaticRoutingProtocol }
         *     
         */
        public StaticRoutingProtocol getStaticRoutingProtocol() {
            return staticRoutingProtocol;
        }

        /**
         * Sets the value of the staticRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link StaticRoutingProtocol }
         *     
         */
        public void setStaticRoutingProtocol(StaticRoutingProtocol value) {
            this.staticRoutingProtocol = value;
        }

        /**
         * Gets the value of the ripRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link RIPRoutingProtocol }
         *     
         */
        public RIPRoutingProtocol getRipRoutingProtocol() {
            return ripRoutingProtocol;
        }

        /**
         * Sets the value of the ripRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link RIPRoutingProtocol }
         *     
         */
        public void setRipRoutingProtocol(RIPRoutingProtocol value) {
            this.ripRoutingProtocol = value;
        }

    }

}
