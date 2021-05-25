
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CambiumVLANAttributes complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CambiumVLANAttributes">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="allowFrameTypes" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="dynamicLearning" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="vlanAgingTimeout" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="defaultPortVID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="managementVID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="providerVID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SSManagementVIDPassthrough" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="mappedMACAddress" type="{http://www.tcl.com/2014/2/ipsvc/xsd}MACAddressVIDMap" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vlanPortType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="acceptQinQFrames" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ENABLE"/>
 *               &lt;enumeration value="DISABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="MappedVID1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MappedVID2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field5" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field6" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field7" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field8" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field9" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field10" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field11" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field12" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CambiumVLANAttributes", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "allowFrameTypes",
    "dynamicLearning",
    "vlanAgingTimeout",
    "defaultPortVID",
    "managementVID",
    "providerVID",
    "ssManagementVIDPassthrough",
    "mappedMACAddress",
    "vlanPortType",
    "acceptQinQFrames",
    "mappedVID1",
    "mappedVID2",
    "field3",
    "field4",
    "field5",
    "field6",
    "field7",
    "field8",
    "field9",
    "field10",
    "field11",
    "field12"
})
public class CambiumVLANAttributes {

    protected String allowFrameTypes;
    protected String dynamicLearning;
    protected String vlanAgingTimeout;
    protected Integer defaultPortVID;
    protected String managementVID;
    protected String providerVID;
    @XmlElement(name = "SSManagementVIDPassthrough")
    protected String ssManagementVIDPassthrough;
    protected List<MACAddressVIDMap> mappedMACAddress;
    protected String vlanPortType;
    protected String acceptQinQFrames;
    @XmlElement(name = "MappedVID1")
    protected String mappedVID1;
    @XmlElement(name = "MappedVID2")
    protected String mappedVID2;
    protected String field3;
    protected String field4;
    protected String field5;
    protected String field6;
    protected String field7;
    protected String field8;
    protected String field9;
    protected String field10;
    protected String field11;
    protected String field12;

    /**
     * Gets the value of the allowFrameTypes property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAllowFrameTypes() {
        return allowFrameTypes;
    }

    /**
     * Sets the value of the allowFrameTypes property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAllowFrameTypes(String value) {
        this.allowFrameTypes = value;
    }

    /**
     * Gets the value of the dynamicLearning property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDynamicLearning() {
        return dynamicLearning;
    }

    /**
     * Sets the value of the dynamicLearning property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDynamicLearning(String value) {
        this.dynamicLearning = value;
    }

    /**
     * Gets the value of the vlanAgingTimeout property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVlanAgingTimeout() {
        return vlanAgingTimeout;
    }

    /**
     * Sets the value of the vlanAgingTimeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVlanAgingTimeout(String value) {
        this.vlanAgingTimeout = value;
    }

    /**
     * Gets the value of the defaultPortVID property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDefaultPortVID() {
        return defaultPortVID;
    }

    /**
     * Sets the value of the defaultPortVID property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDefaultPortVID(Integer value) {
        this.defaultPortVID = value;
    }

    /**
     * Gets the value of the managementVID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getManagementVID() {
        return managementVID;
    }

    /**
     * Sets the value of the managementVID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setManagementVID(String value) {
        this.managementVID = value;
    }

    /**
     * Gets the value of the providerVID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProviderVID() {
        return providerVID;
    }

    /**
     * Sets the value of the providerVID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProviderVID(String value) {
        this.providerVID = value;
    }

    /**
     * Gets the value of the ssManagementVIDPassthrough property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSSManagementVIDPassthrough() {
        return ssManagementVIDPassthrough;
    }

    /**
     * Sets the value of the ssManagementVIDPassthrough property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSSManagementVIDPassthrough(String value) {
        this.ssManagementVIDPassthrough = value;
    }

    /**
     * Gets the value of the mappedMACAddress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the mappedMACAddress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMappedMACAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MACAddressVIDMap }
     * 
     * 
     */
    public List<MACAddressVIDMap> getMappedMACAddress() {
        if (mappedMACAddress == null) {
            mappedMACAddress = new ArrayList<MACAddressVIDMap>();
        }
        return this.mappedMACAddress;
    }

    /**
     * Gets the value of the vlanPortType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVlanPortType() {
        return vlanPortType;
    }

    /**
     * Sets the value of the vlanPortType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVlanPortType(String value) {
        this.vlanPortType = value;
    }

    /**
     * Gets the value of the acceptQinQFrames property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAcceptQinQFrames() {
        return acceptQinQFrames;
    }

    /**
     * Sets the value of the acceptQinQFrames property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAcceptQinQFrames(String value) {
        this.acceptQinQFrames = value;
    }

    /**
     * Gets the value of the mappedVID1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMappedVID1() {
        return mappedVID1;
    }

    /**
     * Sets the value of the mappedVID1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMappedVID1(String value) {
        this.mappedVID1 = value;
    }

    /**
     * Gets the value of the mappedVID2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMappedVID2() {
        return mappedVID2;
    }

    /**
     * Sets the value of the mappedVID2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMappedVID2(String value) {
        this.mappedVID2 = value;
    }

    /**
     * Gets the value of the field3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField3() {
        return field3;
    }

    /**
     * Sets the value of the field3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField3(String value) {
        this.field3 = value;
    }

    /**
     * Gets the value of the field4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField4() {
        return field4;
    }

    /**
     * Sets the value of the field4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField4(String value) {
        this.field4 = value;
    }

    /**
     * Gets the value of the field5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField5() {
        return field5;
    }

    /**
     * Sets the value of the field5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField5(String value) {
        this.field5 = value;
    }

    /**
     * Gets the value of the field6 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField6() {
        return field6;
    }

    /**
     * Sets the value of the field6 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField6(String value) {
        this.field6 = value;
    }

    /**
     * Gets the value of the field7 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField7() {
        return field7;
    }

    /**
     * Sets the value of the field7 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField7(String value) {
        this.field7 = value;
    }

    /**
     * Gets the value of the field8 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField8() {
        return field8;
    }

    /**
     * Sets the value of the field8 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField8(String value) {
        this.field8 = value;
    }

    /**
     * Gets the value of the field9 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField9() {
        return field9;
    }

    /**
     * Sets the value of the field9 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField9(String value) {
        this.field9 = value;
    }

    /**
     * Gets the value of the field10 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField10() {
        return field10;
    }

    /**
     * Sets the value of the field10 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField10(String value) {
        this.field10 = value;
    }

    /**
     * Gets the value of the field11 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField11() {
        return field11;
    }

    /**
     * Sets the value of the field11 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField11(String value) {
        this.field11 = value;
    }

    /**
     * Gets the value of the field12 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField12() {
        return field12;
    }

    /**
     * Sets the value of the field12 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField12(String value) {
        this.field12 = value;
    }

}
