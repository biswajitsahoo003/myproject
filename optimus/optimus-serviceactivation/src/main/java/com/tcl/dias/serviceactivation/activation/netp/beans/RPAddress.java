
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RPAddress complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RPAddress">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" minOccurs="0"/>
 *         &lt;element name="MulticastingPEACL" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="MulticastingCEACL" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AccessControlList" minOccurs="0"/>
 *         &lt;element name="ALURPGroupAddressPE" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="ALURPGroupAddressCE" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RPAddress", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "ipAddress",
    "multicastingPEACL",
    "multicastingCEACL",
    "alurpGroupAddressPE",
    "alurpGroupAddressCE"
})
public class RPAddress {

    @XmlElement(name = "IPAddress")
    protected IPV4Address ipAddress;
    @XmlElement(name = "MulticastingPEACL")
    protected AccessControlList multicastingPEACL;
    @XmlElement(name = "MulticastingCEACL")
    protected AccessControlList multicastingCEACL;
    @XmlElement(name = "ALURPGroupAddressPE")
    protected List<IPV4Address> alurpGroupAddressPE;
    @XmlElement(name = "ALURPGroupAddressCE")
    protected List<IPV4Address> alurpGroupAddressCE;

    /**
     * Gets the value of the ipAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IPV4Address }
     *     
     */
    public IPV4Address getIPAddress() {
        return ipAddress;
    }

    /**
     * Sets the value of the ipAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IPV4Address }
     *     
     */
    public void setIPAddress(IPV4Address value) {
        this.ipAddress = value;
    }

    /**
     * Gets the value of the multicastingPEACL property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getMulticastingPEACL() {
        return multicastingPEACL;
    }

    /**
     * Sets the value of the multicastingPEACL property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setMulticastingPEACL(AccessControlList value) {
        this.multicastingPEACL = value;
    }

    /**
     * Gets the value of the multicastingCEACL property.
     * 
     * @return
     *     possible object is
     *     {@link AccessControlList }
     *     
     */
    public AccessControlList getMulticastingCEACL() {
        return multicastingCEACL;
    }

    /**
     * Sets the value of the multicastingCEACL property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccessControlList }
     *     
     */
    public void setMulticastingCEACL(AccessControlList value) {
        this.multicastingCEACL = value;
    }

    /**
     * Gets the value of the alurpGroupAddressPE property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alurpGroupAddressPE property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALURPGroupAddressPE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPV4Address }
     * 
     * 
     */
    public List<IPV4Address> getALURPGroupAddressPE() {
        if (alurpGroupAddressPE == null) {
            alurpGroupAddressPE = new ArrayList<IPV4Address>();
        }
        return this.alurpGroupAddressPE;
    }

    /**
     * Gets the value of the alurpGroupAddressCE property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the alurpGroupAddressCE property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getALURPGroupAddressCE().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPV4Address }
     * 
     * 
     */
    public List<IPV4Address> getALURPGroupAddressCE() {
        if (alurpGroupAddressCE == null) {
            alurpGroupAddressCE = new ArrayList<IPV4Address>();
        }
        return this.alurpGroupAddressCE;
    }

}
