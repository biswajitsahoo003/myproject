
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for lanVRMPLSPEVo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="lanVRMPLSPEVo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://com.tatacommunications.cramer.reverseisc.ws}sdWanVo"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="LAN_VR_SUB_INT_TO_CONNECT_MPLS_PE_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LAN_VR_SUB_INT_TO_CONNECT_MPLS_PE_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LOOPBACK_IP_PE1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="LOOPBACK_IP_PE2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MPLS_PE_1_SUB_INTERFACE_TOWARD_VERSA_LAN_VR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="MPLS_PE_2_SUB_INTERFACE_TOWARD_VERSA_LAN_VR" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lanVRMPLSPEVo", propOrder = {
    "lanvrsubinttoconnectmplspe1",
    "lanvrsubinttoconnectmplspe2",
    "loopbackippe1",
    "loopbackippe2",
    "mplspe1SUBINTERFACETOWARDVERSALANVR",
    "mplspe2SUBINTERFACETOWARDVERSALANVR"
})
public class LanVRMPLSPEVo
    extends SdWanVo
{

    @XmlElement(name = "LAN_VR_SUB_INT_TO_CONNECT_MPLS_PE_1")
    protected String lanvrsubinttoconnectmplspe1;
    @XmlElement(name = "LAN_VR_SUB_INT_TO_CONNECT_MPLS_PE_2")
    protected String lanvrsubinttoconnectmplspe2;
    @XmlElement(name = "LOOPBACK_IP_PE1")
    protected String loopbackippe1;
    @XmlElement(name = "LOOPBACK_IP_PE2")
    protected String loopbackippe2;
    @XmlElement(name = "MPLS_PE_1_SUB_INTERFACE_TOWARD_VERSA_LAN_VR")
    protected String mplspe1SUBINTERFACETOWARDVERSALANVR;
    @XmlElement(name = "MPLS_PE_2_SUB_INTERFACE_TOWARD_VERSA_LAN_VR")
    protected String mplspe2SUBINTERFACETOWARDVERSALANVR;

    /**
     * Gets the value of the lanvrsubinttoconnectmplspe1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLANVRSUBINTTOCONNECTMPLSPE1() {
        return lanvrsubinttoconnectmplspe1;
    }

    /**
     * Sets the value of the lanvrsubinttoconnectmplspe1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLANVRSUBINTTOCONNECTMPLSPE1(String value) {
        this.lanvrsubinttoconnectmplspe1 = value;
    }

    /**
     * Gets the value of the lanvrsubinttoconnectmplspe2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLANVRSUBINTTOCONNECTMPLSPE2() {
        return lanvrsubinttoconnectmplspe2;
    }

    /**
     * Sets the value of the lanvrsubinttoconnectmplspe2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLANVRSUBINTTOCONNECTMPLSPE2(String value) {
        this.lanvrsubinttoconnectmplspe2 = value;
    }

    /**
     * Gets the value of the loopbackippe1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOOPBACKIPPE1() {
        return loopbackippe1;
    }

    /**
     * Sets the value of the loopbackippe1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOOPBACKIPPE1(String value) {
        this.loopbackippe1 = value;
    }

    /**
     * Gets the value of the loopbackippe2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLOOPBACKIPPE2() {
        return loopbackippe2;
    }

    /**
     * Sets the value of the loopbackippe2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLOOPBACKIPPE2(String value) {
        this.loopbackippe2 = value;
    }

    /**
     * Gets the value of the mplspe1SUBINTERFACETOWARDVERSALANVR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMPLSPE1SUBINTERFACETOWARDVERSALANVR() {
        return mplspe1SUBINTERFACETOWARDVERSALANVR;
    }

    /**
     * Sets the value of the mplspe1SUBINTERFACETOWARDVERSALANVR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMPLSPE1SUBINTERFACETOWARDVERSALANVR(String value) {
        this.mplspe1SUBINTERFACETOWARDVERSALANVR = value;
    }

    /**
     * Gets the value of the mplspe2SUBINTERFACETOWARDVERSALANVR property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMPLSPE2SUBINTERFACETOWARDVERSALANVR() {
        return mplspe2SUBINTERFACETOWARDVERSALANVR;
    }

    /**
     * Sets the value of the mplspe2SUBINTERFACETOWARDVERSALANVR property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMPLSPE2SUBINTERFACETOWARDVERSALANVR(String value) {
        this.mplspe2SUBINTERFACETOWARDVERSALANVR = value;
    }

}
