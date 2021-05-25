
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for mplswan2MPLSWANPEVo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="mplswan2MPLSWANPEVo"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://com.tatacommunications.cramer.reverseisc.ws}sdWanVo"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="PHYISCAL_PORT_ON_SG_LAN_VR_LOOPING_PORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PHYSICAL_PORT_MPLS_PE_1_FACING_VERSA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PHYSICAL_PORT_MPLS_PE_2_FACING_VERSA" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PHYSICAL_PORT_ON_SG_WAN_VR_LOOPING_PORT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PHYSICAL_PORT_ON_VERSA_MPLS_PE1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="PHYSICAL_PORT_ON_VERSA_MPLS_PE2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ROUTER_MPLS_PE_1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="ROUTER_MPLS_PE_2" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mplswan2MPLSWANPEVo", propOrder = {
    "phyiscalportonsglanvrloopingport",
    "physicalportmplspe1FACINGVERSA",
    "physicalportmplspe2FACINGVERSA",
    "physicalportonsgwanvrloopingport",
    "physicalportonversamplspe1",
    "physicalportonversamplspe2",
    "routermplspe1",
    "routermplspe2"
})
public class Mplswan2MPLSWANPEVo
    extends SdWanVo
{

    @XmlElement(name = "PHYISCAL_PORT_ON_SG_LAN_VR_LOOPING_PORT")
    protected String phyiscalportonsglanvrloopingport;
    @XmlElement(name = "PHYSICAL_PORT_MPLS_PE_1_FACING_VERSA")
    protected String physicalportmplspe1FACINGVERSA;
    @XmlElement(name = "PHYSICAL_PORT_MPLS_PE_2_FACING_VERSA")
    protected String physicalportmplspe2FACINGVERSA;
    @XmlElement(name = "PHYSICAL_PORT_ON_SG_WAN_VR_LOOPING_PORT")
    protected String physicalportonsgwanvrloopingport;
    @XmlElement(name = "PHYSICAL_PORT_ON_VERSA_MPLS_PE1")
    protected String physicalportonversamplspe1;
    @XmlElement(name = "PHYSICAL_PORT_ON_VERSA_MPLS_PE2")
    protected String physicalportonversamplspe2;
    @XmlElement(name = "ROUTER_MPLS_PE_1")
    protected String routermplspe1;
    @XmlElement(name = "ROUTER_MPLS_PE_2")
    protected String routermplspe2;

    /**
     * Gets the value of the phyiscalportonsglanvrloopingport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHYISCALPORTONSGLANVRLOOPINGPORT() {
        return phyiscalportonsglanvrloopingport;
    }

    /**
     * Sets the value of the phyiscalportonsglanvrloopingport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHYISCALPORTONSGLANVRLOOPINGPORT(String value) {
        this.phyiscalportonsglanvrloopingport = value;
    }

    /**
     * Gets the value of the physicalportmplspe1FACINGVERSA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHYSICALPORTMPLSPE1FACINGVERSA() {
        return physicalportmplspe1FACINGVERSA;
    }

    /**
     * Sets the value of the physicalportmplspe1FACINGVERSA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHYSICALPORTMPLSPE1FACINGVERSA(String value) {
        this.physicalportmplspe1FACINGVERSA = value;
    }

    /**
     * Gets the value of the physicalportmplspe2FACINGVERSA property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHYSICALPORTMPLSPE2FACINGVERSA() {
        return physicalportmplspe2FACINGVERSA;
    }

    /**
     * Sets the value of the physicalportmplspe2FACINGVERSA property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHYSICALPORTMPLSPE2FACINGVERSA(String value) {
        this.physicalportmplspe2FACINGVERSA = value;
    }

    /**
     * Gets the value of the physicalportonsgwanvrloopingport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHYSICALPORTONSGWANVRLOOPINGPORT() {
        return physicalportonsgwanvrloopingport;
    }

    /**
     * Sets the value of the physicalportonsgwanvrloopingport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHYSICALPORTONSGWANVRLOOPINGPORT(String value) {
        this.physicalportonsgwanvrloopingport = value;
    }

    /**
     * Gets the value of the physicalportonversamplspe1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHYSICALPORTONVERSAMPLSPE1() {
        return physicalportonversamplspe1;
    }

    /**
     * Sets the value of the physicalportonversamplspe1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHYSICALPORTONVERSAMPLSPE1(String value) {
        this.physicalportonversamplspe1 = value;
    }

    /**
     * Gets the value of the physicalportonversamplspe2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPHYSICALPORTONVERSAMPLSPE2() {
        return physicalportonversamplspe2;
    }

    /**
     * Sets the value of the physicalportonversamplspe2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPHYSICALPORTONVERSAMPLSPE2(String value) {
        this.physicalportonversamplspe2 = value;
    }

    /**
     * Gets the value of the routermplspe1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROUTERMPLSPE1() {
        return routermplspe1;
    }

    /**
     * Sets the value of the routermplspe1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROUTERMPLSPE1(String value) {
        this.routermplspe1 = value;
    }

    /**
     * Gets the value of the routermplspe2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getROUTERMPLSPE2() {
        return routermplspe2;
    }

    /**
     * Sets the value of the routermplspe2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setROUTERMPLSPE2(String value) {
        this.routermplspe2 = value;
    }

}
