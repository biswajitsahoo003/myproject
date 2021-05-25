
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IPAddressList complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IPAddressList">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="IPAddress" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPAddress" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IPAddressList", namespace = "http://www.tcl.com/2014/2/ipsvc/xsd", propOrder = {
    "ipAddress"
})
public class IPAddressList {

    @XmlElement(name = "IPAddress")
    protected List<IPAddress> ipAddress;

    /**
     * Gets the value of the ipAddress property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ipAddress property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIPAddress().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IPAddress }
     * 
     * 
     */
    public List<IPAddress> getIPAddress() {
        if (ipAddress == null) {
            ipAddress = new ArrayList<IPAddress>();
        }
        return this.ipAddress;
    }

}
