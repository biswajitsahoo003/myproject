
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VPNSolutionTable complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VPNSolutionTable">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="VPN" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VPN" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="SolutionID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
@XmlType(name = "VPNSolutionTable", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "vpn",
    "solutionID",
    "isObjectInstanceModified"
})
public class VPNSolutionTable {

    @XmlElement(name = "VPN")
    protected List<VPN> vpn;
    @XmlElement(name = "SolutionID")
    protected String solutionID;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the vpn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the vpn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVPN().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VPN }
     * 
     * 
     */
    public List<VPN> getVPN() {
        if (vpn == null) {
            vpn = new ArrayList<VPN>();
        }
        return this.vpn;
    }

    /**
     * Gets the value of the solutionID property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSolutionID() {
        return solutionID;
    }

    /**
     * Sets the value of the solutionID property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolutionID(String value) {
        this.solutionID = value;
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
