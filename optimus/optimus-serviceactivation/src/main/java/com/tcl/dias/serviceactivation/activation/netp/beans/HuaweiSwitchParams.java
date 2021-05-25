
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				These params are used for configuring the Huawei L2
 * 				Switches as part of an EOR.
 * 			
 * 
 * <p>Java class for HuaweiSwitchParams complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="HuaweiSwitchParams">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="HuaweiERPSConfig" type="{http://www.tcl.com/2014/4/ipsvc/xsd}HuaweiERPSConfig" minOccurs="0"/>
 *         &lt;element name="DeviceRole" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ACCESS"/>
 *               &lt;enumeration value="MAJOR"/>
 *               &lt;enumeration value="MEGA"/>
 *               &lt;enumeration value="AGGR"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="managementVLAN" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="managementVLANInterfaceDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CFMdefaultMDLevel" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="gatewayNTPIPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="uploadNode" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="applyInitTemplate" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
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
@XmlType(name = "HuaweiSwitchParams", namespace = "http://www.tcl.com/2014/4/ipsvc/xsd", propOrder = {
    "huaweiERPSConfig",
    "deviceRole",
    "managementVLAN",
    "managementVLANInterfaceDescription",
    "cfMdefaultMDLevel",
    "gatewayNTPIPAddress",
    "location",
    "isObjectInstanceModified",
    "uploadNode",
    "applyInitTemplate"
})
public class HuaweiSwitchParams {

    @XmlElement(name = "HuaweiERPSConfig")
    protected HuaweiERPSConfig huaweiERPSConfig;
    @XmlElement(name = "DeviceRole")
    protected String deviceRole;
    protected String managementVLAN;
    protected String managementVLANInterfaceDescription;
    @XmlElement(name = "CFMdefaultMDLevel")
    protected String cfMdefaultMDLevel;
    protected IPV4Address gatewayNTPIPAddress;
    protected String location;
    protected Boolean isObjectInstanceModified;
    protected String uploadNode;
    protected String applyInitTemplate;

    /**
     * Gets the value of the huaweiERPSConfig property.
     * 
     * @return
     *     possible object is
     *     {@link HuaweiERPSConfig }
     *     
     */
    public HuaweiERPSConfig getHuaweiERPSConfig() {
        return huaweiERPSConfig;
    }

    /**
     * Sets the value of the huaweiERPSConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link HuaweiERPSConfig }
     *     
     */
    public void setHuaweiERPSConfig(HuaweiERPSConfig value) {
        this.huaweiERPSConfig = value;
    }

    /**
     * Gets the value of the deviceRole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDeviceRole() {
        return deviceRole;
    }

    /**
     * Sets the value of the deviceRole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDeviceRole(String value) {
        this.deviceRole = value;
    }

    /**
     * Gets the value of the managementVLAN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagementVLAN() {
        return managementVLAN;
    }

    /**
     * Sets the value of the managementVLAN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagementVLAN(String value) {
        this.managementVLAN = value;
    }

    /**
     * Gets the value of the managementVLANInterfaceDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagementVLANInterfaceDescription() {
        return managementVLANInterfaceDescription;
    }

    /**
     * Sets the value of the managementVLANInterfaceDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagementVLANInterfaceDescription(String value) {
        this.managementVLANInterfaceDescription = value;
    }

    /**
     * Gets the value of the cfMdefaultMDLevel property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCFMdefaultMDLevel() {
        return cfMdefaultMDLevel;
    }

    /**
     * Sets the value of the cfMdefaultMDLevel property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCFMdefaultMDLevel(String value) {
        this.cfMdefaultMDLevel = value;
    }

    /**
     * Gets the value of the gatewayNTPIPAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getGatewayNTPIPAddress() {
        return gatewayNTPIPAddress;
    }

    /**
     * Sets the value of the gatewayNTPIPAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setGatewayNTPIPAddress(IPV4Address value) {
        this.gatewayNTPIPAddress = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
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
     * Gets the value of the uploadNode property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUploadNode() {
        return uploadNode;
    }

    /**
     * Sets the value of the uploadNode property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUploadNode(String value) {
        this.uploadNode = value;
    }

    /**
     * Gets the value of the applyInitTemplate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplyInitTemplate() {
        return applyInitTemplate;
    }

    /**
     * Sets the value of the applyInitTemplate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplyInitTemplate(String value) {
        this.applyInitTemplate = value;
    }

}
