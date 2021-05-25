
package com.tcl.dias.serviceactivation.cramer.reverseisc.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Phyiscal_Port_Details complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Phyiscal_Port_Details"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Connection_between_MPLS_WAN_VR_MPLS_PE" type="{http://com.tatacommunications.cramer.reverseisc.ws}mplswan2MPLSWANPEVo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Phyiscal_Port_Details", propOrder = {
    "connectionBetweenMPLSWANVRMPLSPE"
})
public class PhyiscalPortDetails {

    @XmlElement(name = "Connection_between_MPLS_WAN_VR_MPLS_PE")
    protected Mplswan2MPLSWANPEVo connectionBetweenMPLSWANVRMPLSPE;

    /**
     * Gets the value of the connectionBetweenMPLSWANVRMPLSPE property.
     * 
     * @return
     *     possible object is
     *     {@link Mplswan2MPLSWANPEVo }
     *     
     */
    public Mplswan2MPLSWANPEVo getConnectionBetweenMPLSWANVRMPLSPE() {
        return connectionBetweenMPLSWANVRMPLSPE;
    }

    /**
     * Sets the value of the connectionBetweenMPLSWANVRMPLSPE property.
     * 
     * @param value
     *     allowed object is
     *     {@link Mplswan2MPLSWANPEVo }
     *     
     */
    public void setConnectionBetweenMPLSWANVRMPLSPE(Mplswan2MPLSWANPEVo value) {
        this.connectionBetweenMPLSWANVRMPLSPE = value;
    }

}
