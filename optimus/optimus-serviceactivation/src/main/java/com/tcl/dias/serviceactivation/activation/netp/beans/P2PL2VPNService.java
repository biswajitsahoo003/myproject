
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				ATM is not considered for configuration for
 * 				P2PL2VPNServices
 * 			
 * 
 * <p>Java class for P2PL2VPNService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="P2PL2VPNService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}GVPNService">
 *       &lt;sequence>
 *         &lt;element name="peerPEhostname" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="CE2CEService" type="{http://IPServicesLibrary/ipsvc/bo/_2011/_11}CECEService" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="VCID" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="SDPConfig" type="{http://www.tcl.com/2014/3/ipsvc/xsd}SDPConfig" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "P2PL2VPNService", propOrder = {
    "peerPEhostname",
    "ce2CEService",
    "vcid",
    "sdpConfig"
})
@XmlSeeAlso({
    VPLSService.class
})
public class P2PL2VPNService
    extends GVPNService
{

    protected String peerPEhostname;
    @XmlElement(name = "CE2CEService")
    protected List<CECEService> ce2CEService;
    @XmlElement(name = "VCID")
    protected Integer vcid;
    @XmlElement(name = "SDPConfig")
    protected SDPConfig sdpConfig;

    /**
     * Gets the value of the peerPEhostname property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPeerPEhostname() {
        return peerPEhostname;
    }

    /**
     * Sets the value of the peerPEhostname property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPeerPEhostname(String value) {
        this.peerPEhostname = value;
    }

    /**
     * Gets the value of the ce2CEService property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ce2CEService property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCE2CEService().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CECEService }
     * 
     * 
     */
    public List<CECEService> getCE2CEService() {
        if (ce2CEService == null) {
            ce2CEService = new ArrayList<CECEService>();
        }
        return this.ce2CEService;
    }

    /**
     * Gets the value of the vcid property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getVCID() {
        return vcid;
    }

    /**
     * Sets the value of the vcid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setVCID(Integer value) {
        this.vcid = value;
    }

    /**
     * Gets the value of the sdpConfig property.
     * 
     * @return
     *     possible object is
     *     {@link SDPConfig }
     *     
     */
    public SDPConfig getSDPConfig() {
        return sdpConfig;
    }

    /**
     * Sets the value of the sdpConfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link SDPConfig }
     *     
     */
    public void setSDPConfig(SDPConfig value) {
        this.sdpConfig = value;
    }

}
