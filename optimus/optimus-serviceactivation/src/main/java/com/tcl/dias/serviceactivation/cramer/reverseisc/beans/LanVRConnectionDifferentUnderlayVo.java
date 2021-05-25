
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lanVRConnectionDifferentUnderlayVo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lanVRConnectionDifferentUnderlayVo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://com.tatacommunications.cramer.reverseisc.ws}sdWanVo"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="DISTRIBUTION_LAN_VR_SUB_INT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LAN_VR_SUB_INT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="VLAN_NUMBER_FROM_VERSA_PE" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lanVRConnectionDifferentUnderlayVo", propOrder = {
    "distributionlanvrsubint",
    "lanvrsubint",
    "vlannumberfromversape"
})
public class LanVRConnectionDifferentUnderlayVo
    extends SdWanVo
{

    @XmlElement(name = "DISTRIBUTION_LAN_VR_SUB_INT")
    protected String distributionlanvrsubint;
    @XmlElement(name = "LAN_VR_SUB_INT")
    protected String lanvrsubint;
    @XmlElement(name = "VLAN_NUMBER_FROM_VERSA_PE")
    protected String vlannumberfromversape;

    /**
     * Gets the value of the distributionlanvrsubint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDISTRIBUTIONLANVRSUBINT() {
        return distributionlanvrsubint;
    }

    /**
     * Sets the value of the distributionlanvrsubint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDISTRIBUTIONLANVRSUBINT(String value) {
        this.distributionlanvrsubint = value;
    }

    /**
     * Gets the value of the lanvrsubint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLANVRSUBINT() {
        return lanvrsubint;
    }

    /**
     * Sets the value of the lanvrsubint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLANVRSUBINT(String value) {
        this.lanvrsubint = value;
    }

    /**
     * Gets the value of the vlannumberfromversape property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVLANNUMBERFROMVERSAPE() {
        return vlannumberfromversape;
    }

    /**
     * Sets the value of the vlannumberfromversape property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVLANNUMBERFROMVERSAPE(String value) {
        this.vlannumberfromversape = value;
    }

}
