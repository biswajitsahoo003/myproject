
package com.tcl.dias.serviceactivation.activation.netp.beans;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VPN complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="VPN">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vpnId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="topology" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="HUBnSPOKE"/>
 *               &lt;enumeration value="MESH"/>
 *               &lt;enumeration value="MGMT"/>
 *               &lt;enumeration value="TYPEBL3NNI"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="leg" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Leg" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vpnType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="CUSTOMER"/>
 *               &lt;enumeration value="MANAGEMENT"/>
 *               &lt;enumeration value="PREPROVISIONED_CUSTOMER"/>
 *               &lt;enumeration value="PREPROVISIONED_MANAGEMENT"/>
 *               &lt;enumeration value="value"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="OptionBNNITCLRT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="OptionBNNIPartnerRT" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="isChildObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VPN", propOrder = {
    "vpnId",
    "topology",
    "leg",
    "vpnType",
    "optionBNNITCLRT",
    "optionBNNIPartnerRT",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified"
})
public class VPN {

    protected String vpnId;
    protected String topology;
    protected List<Leg> leg;
    protected String vpnType;
    @XmlElement(name = "OptionBNNITCLRT")
    protected String optionBNNITCLRT;
    @XmlElement(name = "OptionBNNIPartnerRT")
    protected String optionBNNIPartnerRT;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;

    /**
     * Gets the value of the vpnId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVpnId() {
        return vpnId;
    }

    /**
     * Sets the value of the vpnId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVpnId(String value) {
        this.vpnId = value;
    }

    /**
     * Gets the value of the topology property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTopology() {
        return topology;
    }

    /**
     * Sets the value of the topology property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTopology(String value) {
        this.topology = value;
    }

    /**
     * Gets the value of the leg property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the leg property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLeg().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Leg }
     * 
     * 
     */
    public List<Leg> getLeg() {
        if (leg == null) {
            leg = new ArrayList<Leg>();
        }
        return this.leg;
    }

    /**
     * Gets the value of the vpnType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVpnType() {
        return vpnType;
    }

    /**
     * Sets the value of the vpnType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVpnType(String value) {
        this.vpnType = value;
    }

    /**
     * Gets the value of the optionBNNITCLRT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptionBNNITCLRT() {
        return optionBNNITCLRT;
    }

    /**
     * Sets the value of the optionBNNITCLRT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptionBNNITCLRT(String value) {
        this.optionBNNITCLRT = value;
    }

    /**
     * Gets the value of the optionBNNIPartnerRT property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptionBNNIPartnerRT() {
        return optionBNNIPartnerRT;
    }

    /**
     * Sets the value of the optionBNNIPartnerRT property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptionBNNIPartnerRT(String value) {
        this.optionBNNIPartnerRT = value;
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

    /**
     * Gets the value of the isChildObjectInstanceModified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsChildObjectInstanceModified() {
        return isChildObjectInstanceModified;
    }

    /**
     * Sets the value of the isChildObjectInstanceModified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsChildObjectInstanceModified(Boolean value) {
        this.isChildObjectInstanceModified = value;
    }

}
