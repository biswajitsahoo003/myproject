//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.02.06 at 03:13:07 PM IST 
//


package com.tcl.dias.serviceactivation.cramer.getipserviceasync.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CramerDMVPNService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CramerDMVPNService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2012/09/csvc/inf}IPService">
 *       &lt;sequence>
 *         &lt;element name="LNSDetails" type="{http://www.tcl.com/2012/09/csvc/inf}CramerLNSDetails" minOccurs="0"/>
 *         &lt;element name="LNSCETunnelPath" type="{http://www.tcl.com/2012/09/csvc/inf}PERouter" minOccurs="0"/>
 *         &lt;element name="attribute1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="attribute3" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CramerDMVPNService", propOrder = {
    "lnsDetails",
    "lnsceTunnelPath",
    "attribute1",
    "attribute2",
    "attribute3"
})
public class CramerDMVPNService
    extends IPService
{

    @XmlElement(name = "LNSDetails")
    protected CramerLNSDetails lnsDetails;
    @XmlElement(name = "LNSCETunnelPath")
    protected PERouter lnsceTunnelPath;
    protected String attribute1;
    protected String attribute2;
    protected String attribute3;

    /**
     * Gets the value of the lnsDetails property.
     * 
     * @return
     *     possible object is
     *     {@link CramerLNSDetails }
     *     
     */
    public CramerLNSDetails getLNSDetails() {
        return lnsDetails;
    }

    /**
     * Sets the value of the lnsDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link CramerLNSDetails }
     *     
     */
    public void setLNSDetails(CramerLNSDetails value) {
        this.lnsDetails = value;
    }

    /**
     * Gets the value of the lnsceTunnelPath property.
     * 
     * @return
     *     possible object is
     *     {@link PERouter }
     *     
     */
    public PERouter getLNSCETunnelPath() {
        return lnsceTunnelPath;
    }

    /**
     * Sets the value of the lnsceTunnelPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link PERouter }
     *     
     */
    public void setLNSCETunnelPath(PERouter value) {
        this.lnsceTunnelPath = value;
    }

    /**
     * Gets the value of the attribute1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute1() {
        return attribute1;
    }

    /**
     * Sets the value of the attribute1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute1(String value) {
        this.attribute1 = value;
    }

    /**
     * Gets the value of the attribute2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute2() {
        return attribute2;
    }

    /**
     * Sets the value of the attribute2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute2(String value) {
        this.attribute2 = value;
    }

    /**
     * Gets the value of the attribute3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAttribute3() {
        return attribute3;
    }

    /**
     * Sets the value of the attribute3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAttribute3(String value) {
        this.attribute3 = value;
    }

	@Override
	public String toString() {
		return "CramerDMVPNService [lnsDetails=" + lnsDetails + ", lnsceTunnelPath=" + lnsceTunnelPath + ", attribute1="
				+ attribute1 + ", attribute2=" + attribute2 + ", attribute3=" + attribute3 + "]";
	}

    
}
