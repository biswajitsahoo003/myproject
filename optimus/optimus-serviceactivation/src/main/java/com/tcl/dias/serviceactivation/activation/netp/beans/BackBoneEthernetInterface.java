
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for backBoneEthernetInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="backBoneEthernetInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBoneRouterInterface">
 *       &lt;sequence>
 *         &lt;element name="speed" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="100"/>
 *               &lt;enumeration value="1000"/>
 *               &lt;enumeration value="NO_NEGOTIATE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *               &lt;enumeration value="AUTO"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="duplex" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="FULL"/>
 *               &lt;enumeration value="HALF"/>
 *               &lt;enumeration value="AUTO"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="isAutoNegotiationEnabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "backBoneEthernetInterface", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_01", propOrder = {
    "speed",
    "duplex",
    "isAutoNegotiationEnabled"
})
public class BackBoneEthernetInterface
    extends BackBoneRouterInterface
{

    protected String speed;
    protected String duplex;
    protected String isAutoNegotiationEnabled;

    /**
     * Gets the value of the speed property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpeed() {
        return speed;
    }

    /**
     * Sets the value of the speed property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpeed(String value) {
        this.speed = value;
    }

    /**
     * Gets the value of the duplex property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDuplex() {
        return duplex;
    }

    /**
     * Sets the value of the duplex property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDuplex(String value) {
        this.duplex = value;
    }

    /**
     * Gets the value of the isAutoNegotiationEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsAutoNegotiationEnabled() {
        return isAutoNegotiationEnabled;
    }

    /**
     * Sets the value of the isAutoNegotiationEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsAutoNegotiationEnabled(String value) {
        this.isAutoNegotiationEnabled = value;
    }

}
