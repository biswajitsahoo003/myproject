
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for internetWANVRConnectionIpNumberVo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="internetWANVRConnectionIpNumberVo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://com.tatacommunications.cramer.reverseisc.ws}sdWanVo"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="INTERNET_WAN_VR_SUB_INT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
@XmlType(name = "internetWANVRConnectionIpNumberVo", propOrder = {
    "internetwanvrsubint",
    "lanvrsubint",
    "vlannumberfromversape"
})
public class InternetWANVRConnectionIpNumberVo
    extends SdWanVo
{

    @XmlElement(name = "INTERNET_WAN_VR_SUB_INT")
    protected String internetwanvrsubint;
    @XmlElement(name = "LAN_VR_SUB_INT")
    protected String lanvrsubint;
    @XmlElement(name = "VLAN_NUMBER_FROM_VERSA_PE")
    protected String vlannumberfromversape;

    /**
     * Gets the value of the internetwanvrsubint property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getINTERNETWANVRSUBINT() {
        return internetwanvrsubint;
    }

    /**
     * Sets the value of the internetwanvrsubint property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setINTERNETWANVRSUBINT(String value) {
        this.internetwanvrsubint = value;
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
