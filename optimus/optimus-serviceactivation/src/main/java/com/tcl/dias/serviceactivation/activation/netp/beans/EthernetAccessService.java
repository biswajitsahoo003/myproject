
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for EthernetAccessService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EthernetAccessService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ServiceType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ILL"/>
 *               &lt;enumeration value="GVPN"/>
 *               &lt;enumeration value="NDE"/>
 *               &lt;enumeration value="GDE"/>
 *               &lt;enumeration value="EVPL"/>
 *               &lt;enumeration value="EMPT"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ServiceSubType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="ServiceBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="BurstableBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="AccessHandoffPoints" type="{http://www.tcl.com/2014/4/ipsvc/xsd}AccessPoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="VLANTranslationPoints" type="{http://www.tcl.com/2014/4/ipsvc/xsd}VLANTranslationPoint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RingMemberList" type="{http://www.tcl.com/2014/4/ipsvc/xsd}EthernetAccessRingMemberList" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isDowntimeRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="instanceID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="overrideOutofBandConfig" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="networkServiceType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ELINE_QINQ_PORT"/>
 *               &lt;enumeration value="ILL"/>
 *               &lt;enumeration value="GVPN"/>
 *               &lt;enumeration value="ELINE_QINQ_VLAN"/>
 *               &lt;enumeration value="ELINE_DOT1Q_TUNNEL"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EthernetAccessService", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "serviceType",
    "serviceSubType",
    "serviceBandwidth",
    "burstableBandwidth",
    "accessHandoffPoints",
    "vlanTranslationPoints",
    "ringMemberList",
    "isDowntimeRequired",
    "isObjectInstanceModified",
    "instanceID",
    "overrideOutofBandConfig",
    "networkServiceType"
})
public class EthernetAccessService {

    @XmlElement(name = "ServiceType")
    protected String serviceType;
    @XmlElement(name = "ServiceSubType")
    protected String serviceSubType;
    @XmlElement(name = "ServiceBandwidth")
    protected Bandwidth serviceBandwidth;
    @XmlElement(name = "BurstableBandwidth")
    protected Bandwidth burstableBandwidth;
    @XmlElement(name = "AccessHandoffPoints")
    protected List<AccessPoint> accessHandoffPoints;
    @XmlElement(name = "VLANTranslationPoints")
    protected List<VLANTranslationPoint> vlanTranslationPoints;
    @XmlElement(name = "RingMemberList")
    protected List<EthernetAccessRingMemberList> ringMemberList;
    protected Boolean isDowntimeRequired;
    protected Boolean isObjectInstanceModified;
    protected String instanceID;
    protected String overrideOutofBandConfig;
    protected String networkServiceType;

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
     * Gets the value of the accessHandoffPoints property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the accessHandoffPoints property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAccessHandoffPoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AccessPoint }
     * 
     * 
     */
    public List<AccessPoint> getAccessHandoffPoints() {
        if (accessHandoffPoints == null) {
            accessHandoffPoints = new ArrayList<AccessPoint>();
        }
        return this.accessHandoffPoints;
    }

    /**
     * Gets the value of the vlanTranslationPoints property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vlanTranslationPoints property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVLANTranslationPoints().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VLANTranslationPoint }
     * 
     * 
     */
    public List<VLANTranslationPoint> getVLANTranslationPoints() {
        if (vlanTranslationPoints == null) {
            vlanTranslationPoints = new ArrayList<VLANTranslationPoint>();
        }
        return this.vlanTranslationPoints;
    }

    /**
     * Gets the value of the ringMemberList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ringMemberList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRingMemberList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link EthernetAccessRingMemberList }
     * 
     * 
     */
    public List<EthernetAccessRingMemberList> getRingMemberList() {
        if (ringMemberList == null) {
            ringMemberList = new ArrayList<EthernetAccessRingMemberList>();
        }
        return this.ringMemberList;
    }

    /**
     * Gets the value of the isDowntimeRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDowntimeRequired() {
        return isDowntimeRequired;
    }

    /**
     * Sets the value of the isDowntimeRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDowntimeRequired(Boolean value) {
        this.isDowntimeRequired = value;
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
     * Gets the value of the overrideOutofBandConfig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverrideOutofBandConfig() {
        return overrideOutofBandConfig;
    }

    /**
     * Sets the value of the overrideOutofBandConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverrideOutofBandConfig(String value) {
        this.overrideOutofBandConfig = value;
    }

    /**
     * Gets the value of the networkServiceType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNetworkServiceType() {
        return networkServiceType;
    }

    /**
     * Sets the value of the networkServiceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNetworkServiceType(String value) {
        this.networkServiceType = value;
    }

}
