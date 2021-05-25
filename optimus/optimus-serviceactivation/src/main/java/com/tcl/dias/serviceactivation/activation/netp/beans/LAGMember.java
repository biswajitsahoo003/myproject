
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for LAGMember complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LAGMember">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ethernetInterface" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EthernetInterface" minOccurs="0"/>
 *         &lt;element name="subgroupID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="shutdown" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="crcMonitor" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CRCMonitor" minOccurs="0"/>
 *         &lt;element name="EOAMConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}EOAMConfig" minOccurs="0"/>
 *         &lt;element name="memberDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="forceDeleteHuaweiLAGIORLastMember" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="updateDeltaHuaweiLAGIORCommands" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="overrideDescription" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="inboundCIRValue" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="outboundCIRValue" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
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
@XmlType(name = "LAGMember", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "ethernetInterface",
    "subgroupID",
    "priority",
    "shutdown",
    "crcMonitor",
    "eoamConfig",
    "memberDescription",
    "forceDeleteHuaweiLAGIORLastMember",
    "updateDeltaHuaweiLAGIORCommands",
    "overrideDescription",
    "inboundCIRValue",
    "outboundCIRValue",
    "isObjectInstanceModified"
})
public class LAGMember {

    protected EthernetInterface ethernetInterface;
    protected String subgroupID;
    protected String priority;
    protected String shutdown;
    protected CRCMonitor crcMonitor;
    @XmlElement(name = "EOAMConfig")
    protected EOAMConfig eoamConfig;
    protected String memberDescription;
    protected Boolean forceDeleteHuaweiLAGIORLastMember;
    protected Boolean updateDeltaHuaweiLAGIORCommands;
    protected String overrideDescription;
    protected Bandwidth inboundCIRValue;
    protected Bandwidth outboundCIRValue;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the ethernetInterface property.
     * 
     * @return
     *     possible object is
     *     {@link EthernetInterface }
     *     
     */
    public EthernetInterface getEthernetInterface() {
        return ethernetInterface;
    }

    /**
     * Sets the value of the ethernetInterface property.
     * 
     * @param value
     *     allowed object is
     *     {@link EthernetInterface }
     *     
     */
    public void setEthernetInterface(EthernetInterface value) {
        this.ethernetInterface = value;
    }

    /**
     * Gets the value of the subgroupID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSubgroupID() {
        return subgroupID;
    }

    /**
     * Sets the value of the subgroupID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSubgroupID(String value) {
        this.subgroupID = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPriority(String value) {
        this.priority = value;
    }

    /**
     * Gets the value of the shutdown property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getShutdown() {
        return shutdown;
    }

    /**
     * Sets the value of the shutdown property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setShutdown(String value) {
        this.shutdown = value;
    }

    /**
     * Gets the value of the crcMonitor property.
     * 
     * @return
     *     possible object is
     *     {@link CRCMonitor }
     *     
     */
    public CRCMonitor getCrcMonitor() {
        return crcMonitor;
    }

    /**
     * Sets the value of the crcMonitor property.
     * 
     * @param value
     *     allowed object is
     *     {@link CRCMonitor }
     *     
     */
    public void setCrcMonitor(CRCMonitor value) {
        this.crcMonitor = value;
    }

    /**
     * Gets the value of the eoamConfig property.
     * 
     * @return
     *     possible object is
     *     {@link EOAMConfig }
     *     
     */
    public EOAMConfig getEOAMConfig() {
        return eoamConfig;
    }

    /**
     * Sets the value of the eoamConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link EOAMConfig }
     *     
     */
    public void setEOAMConfig(EOAMConfig value) {
        this.eoamConfig = value;
    }

    /**
     * Gets the value of the memberDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemberDescription() {
        return memberDescription;
    }

    /**
     * Sets the value of the memberDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemberDescription(String value) {
        this.memberDescription = value;
    }

    /**
     * Gets the value of the forceDeleteHuaweiLAGIORLastMember property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isForceDeleteHuaweiLAGIORLastMember() {
        return forceDeleteHuaweiLAGIORLastMember;
    }

    /**
     * Sets the value of the forceDeleteHuaweiLAGIORLastMember property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setForceDeleteHuaweiLAGIORLastMember(Boolean value) {
        this.forceDeleteHuaweiLAGIORLastMember = value;
    }

    /**
     * Gets the value of the updateDeltaHuaweiLAGIORCommands property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isUpdateDeltaHuaweiLAGIORCommands() {
        return updateDeltaHuaweiLAGIORCommands;
    }

    /**
     * Sets the value of the updateDeltaHuaweiLAGIORCommands property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setUpdateDeltaHuaweiLAGIORCommands(Boolean value) {
        this.updateDeltaHuaweiLAGIORCommands = value;
    }

    /**
     * Gets the value of the overrideDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOverrideDescription() {
        return overrideDescription;
    }

    /**
     * Sets the value of the overrideDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOverrideDescription(String value) {
        this.overrideDescription = value;
    }

    /**
     * Gets the value of the inboundCIRValue property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getInboundCIRValue() {
        return inboundCIRValue;
    }

    /**
     * Sets the value of the inboundCIRValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setInboundCIRValue(Bandwidth value) {
        this.inboundCIRValue = value;
    }

    /**
     * Gets the value of the outboundCIRValue property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getOutboundCIRValue() {
        return outboundCIRValue;
    }

    /**
     * Sets the value of the outboundCIRValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setOutboundCIRValue(Bandwidth value) {
        this.outboundCIRValue = value;
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
