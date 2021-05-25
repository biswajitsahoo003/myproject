
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CiscoImportPolicyMatchCriteria complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CiscoImportPolicyMatchCriteria">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="PrefixList" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}PrefixList" minOccurs="0"/>
 *         &lt;element name="matchCommunity" type="{http://www.tcl.com/2011/11/ipsvc/xsd}matchCommunity" minOccurs="0"/>
 *         &lt;element name="protocol" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="asPath" type="{http://www.tcl.com/2011/11/ipsvc/xsd}asPath" minOccurs="0"/>
 *         &lt;element name="CLNSAddress" type="{http://www.tcl.com/2011/11/ipsvc/xsd}CLNSAddress" minOccurs="0"/>
 *         &lt;element name="inboundAccessControlList" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="mdtGroup" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="mplsLable" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="policyList" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sourceProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}sourceProtocol" minOccurs="0"/>
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
@XmlType(name = "CiscoImportPolicyMatchCriteria", propOrder = {
    "prefixList",
    "matchCommunity",
    "protocol",
    "asPath",
    "clnsAddress",
    "inboundAccessControlList",
    "mdtGroup",
    "mplsLable",
    "policyList",
    "sourceProtocol",
    "isObjectInstanceModified",
    "field1",
    "field2",
    "field3",
    "field4"
})
public class CiscoImportPolicyMatchCriteria {

    @XmlElement(name = "PrefixList")
    protected PrefixList prefixList;
    protected MatchCommunity matchCommunity;
    protected List<String> protocol;
    protected AsPath asPath;
    @XmlElement(name = "CLNSAddress")
    protected CLNSAddress clnsAddress;
    protected AccessControlList inboundAccessControlList;
    protected AccessControlList mdtGroup;
    protected String mplsLable;
    protected List<String> policyList;
    protected SourceProtocol sourceProtocol;
    protected Boolean isObjectInstanceModified;
    protected String field1;
    protected String field2;
    protected String field3;
    protected String field4;

    /**
     * Gets the value of the prefixList property.
     * 
     * @return
     *     possible object is
     *     {@link PrefixList }
     *     
     */
    public PrefixList getPrefixList() {
        return prefixList;
    }

    /**
     * Sets the value of the prefixList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PrefixList }
     *     
     */
    public void setPrefixList(PrefixList value) {
        this.prefixList = value;
    }

    /**
     * Gets the value of the matchCommunity property.
     * 
     * @return
     *     possible object is
     *     {@link MatchCommunity }
     *     
     */
    public MatchCommunity getMatchCommunity() {
        return matchCommunity;
    }

    /**
     * Sets the value of the matchCommunity property.
     * 
     * @param value
     *     allowed object is
     *     {@link MatchCommunity }
     *     
     */
    public void setMatchCommunity(MatchCommunity value) {
        this.matchCommunity = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the protocol property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProtocol().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getProtocol() {
        if (protocol == null) {
            protocol = new ArrayList<String>();
        }
        return this.protocol;
    }

    /**
     * Gets the value of the asPath property.
     * 
     * @return
     *     possible object is
     *     {@link AsPath }
     *     
     */
    public AsPath getAsPath() {
        return asPath;
    }

    /**
     * Sets the value of the asPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link AsPath }
     *     
     */
    public void setAsPath(AsPath value) {
        this.asPath = value;
    }

    /**
     * Gets the value of the clnsAddress property.
     * 
     * @return
     *     possible object is
     *     {@link CLNSAddress }
     *     
     */
    public CLNSAddress getCLNSAddress() {
        return clnsAddress;
    }

    /**
     * Sets the value of the clnsAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link CLNSAddress }
     *     
     */
    public void setCLNSAddress(CLNSAddress value) {
        this.clnsAddress = value;
    }

    /**
     * Gets the value of the inboundAccessControlList property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getInboundAccessControlList() {
        return inboundAccessControlList;
    }

    /**
     * Sets the value of the inboundAccessControlList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setInboundAccessControlList(AccessControlList value) {
        this.inboundAccessControlList = value;
    }

    /**
     * Gets the value of the mdtGroup property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getMdtGroup() {
        return mdtGroup;
    }

    /**
     * Sets the value of the mdtGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setMdtGroup(AccessControlList value) {
        this.mdtGroup = value;
    }

    /**
     * Gets the value of the mplsLable property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMplsLable() {
        return mplsLable;
    }

    /**
     * Sets the value of the mplsLable property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMplsLable(String value) {
        this.mplsLable = value;
    }

    /**
     * Gets the value of the policyList property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the policyList property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPolicyList().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPolicyList() {
        if (policyList == null) {
            policyList = new ArrayList<String>();
        }
        return this.policyList;
    }

    /**
     * Gets the value of the sourceProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link SourceProtocol }
     *     
     */
    public SourceProtocol getSourceProtocol() {
        return sourceProtocol;
    }

    /**
     * Sets the value of the sourceProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceProtocol }
     *     
     */
    public void setSourceProtocol(SourceProtocol value) {
        this.sourceProtocol = value;
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
