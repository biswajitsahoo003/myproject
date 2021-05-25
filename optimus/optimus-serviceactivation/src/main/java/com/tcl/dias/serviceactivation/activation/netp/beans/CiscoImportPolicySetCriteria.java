
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CiscoImportPolicySetCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CiscoImportPolicySetCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="AS_Path_Prepend" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AS_Path_Prepend_Index" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="localPreference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="metric" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="setCommunity" type="{http://www.tcl.com/2011/11/ipsvc/xsd}setCommunity" minOccurs="0"/>
 *         &lt;element name="clnsNextHop" type="{http://www.tcl.com/2011/11/ipsvc/xsd}clnsNextHop" minOccurs="0"/>
 *         &lt;element name="commListDelete" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="extcommListDelete" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="weight" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="origin" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="IGP"/>
 *               &lt;enumeration value="INCOMPLETE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="ipNextHop" type="{http://www.tcl.com/2011/11/ipsvc/xsd}ipNextHop" minOccurs="0"/>
 *         &lt;element name="ipV6PrefixList" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="ipv6NextHop" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="field1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="field4" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CiscoImportPolicySetCriteria", propOrder = {
    "asPathPrepend",
    "asPathPrependIndex",
    "localPreference",
    "metric",
    "setCommunity",
    "clnsNextHop",
    "commListDelete",
    "extcommListDelete",
    "weight",
    "origin",
    "ipNextHop",
    "ipV6PrefixList",
    "ipv6NextHop",
    "isObjectInstanceModified",
    "field1",
    "field2",
    "field3",
    "field4"
})
public class CiscoImportPolicySetCriteria {

    @XmlElement(name = "AS_Path_Prepend")
    protected String asPathPrepend;
    @XmlElement(name = "AS_Path_Prepend_Index")
    protected String asPathPrependIndex;
    protected String localPreference;
    protected String metric;
    protected SetCommunity setCommunity;
    protected ClnsNextHop clnsNextHop;
    protected String commListDelete;
    protected String extcommListDelete;
    protected String weight;
    protected String origin;
    protected IpNextHop ipNextHop;
    protected PrefixList ipV6PrefixList;
    protected List<String> ipv6NextHop;
    protected Boolean isObjectInstanceModified;
    protected String field1;
    protected String field2;
    protected String field3;
    protected String field4;

    /**
     * Gets the value of the asPathPrepend property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASPathPrepend() {
        return asPathPrepend;
    }

    /**
     * Sets the value of the asPathPrepend property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASPathPrepend(String value) {
        this.asPathPrepend = value;
    }

    /**
     * Gets the value of the asPathPrependIndex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getASPathPrependIndex() {
        return asPathPrependIndex;
    }

    /**
     * Sets the value of the asPathPrependIndex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setASPathPrependIndex(String value) {
        this.asPathPrependIndex = value;
    }

    /**
     * Gets the value of the localPreference property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocalPreference() {
        return localPreference;
    }

    /**
     * Sets the value of the localPreference property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocalPreference(String value) {
        this.localPreference = value;
    }

    /**
     * Gets the value of the metric property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMetric() {
        return metric;
    }

    /**
     * Sets the value of the metric property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMetric(String value) {
        this.metric = value;
    }

    /**
     * Gets the value of the setCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link SetCommunity }
     *     
     */
    public SetCommunity getSetCommunity() {
        return setCommunity;
    }

    /**
     * Sets the value of the setCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link SetCommunity }
     *     
     */
    public void setSetCommunity(SetCommunity value) {
        this.setCommunity = value;
    }

    /**
     * Gets the value of the clnsNextHop property.
     * 
     * @return
     *     possible object is
     *     {@link ClnsNextHop }
     *     
     */
    public ClnsNextHop getClnsNextHop() {
        return clnsNextHop;
    }

    /**
     * Sets the value of the clnsNextHop property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClnsNextHop }
     *     
     */
    public void setClnsNextHop(ClnsNextHop value) {
        this.clnsNextHop = value;
    }

    /**
     * Gets the value of the commListDelete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommListDelete() {
        return commListDelete;
    }

    /**
     * Sets the value of the commListDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommListDelete(String value) {
        this.commListDelete = value;
    }

    /**
     * Gets the value of the extcommListDelete property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getExtcommListDelete() {
        return extcommListDelete;
    }

    /**
     * Sets the value of the extcommListDelete property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setExtcommListDelete(String value) {
        this.extcommListDelete = value;
    }

    /**
     * Gets the value of the weight property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeight() {
        return weight;
    }

    /**
     * Sets the value of the weight property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeight(String value) {
        this.weight = value;
    }

    /**
     * Gets the value of the origin property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Sets the value of the origin property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrigin(String value) {
        this.origin = value;
    }

    /**
     * Gets the value of the ipNextHop property.
     * 
     * @return
     *     possible object is
     *     {@link IpNextHop }
     *     
     */
    public IpNextHop getIpNextHop() {
        return ipNextHop;
    }

    /**
     * Sets the value of the ipNextHop property.
     * 
     * @param value
     *     allowed object is
     *     {@link IpNextHop }
     *     
     */
    public void setIpNextHop(IpNextHop value) {
        this.ipNextHop = value;
    }

    /**
     * Gets the value of the ipV6PrefixList property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getIpV6PrefixList() {
        return ipV6PrefixList;
    }

    /**
     * Sets the value of the ipV6PrefixList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setIpV6PrefixList(PrefixList value) {
        this.ipV6PrefixList = value;
    }

    /**
     * Gets the value of the ipv6NextHop property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipv6NextHop property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIpv6NextHop().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getIpv6NextHop() {
        if (ipv6NextHop == null) {
            ipv6NextHop = new ArrayList<String>();
        }
        return this.ipv6NextHop;
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
     * Gets the value of the field1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField1() {
        return field1;
    }

    /**
     * Sets the value of the field1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField1(String value) {
        this.field1 = value;
    }

    /**
     * Gets the value of the field2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField2() {
        return field2;
    }

    /**
     * Sets the value of the field2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField2(String value) {
        this.field2 = value;
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

}
