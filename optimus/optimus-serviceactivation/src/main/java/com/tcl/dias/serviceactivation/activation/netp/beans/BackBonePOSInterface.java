
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for backBonePOSInterface complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="backBonePOSInterface">
 *   &lt;complexContent>
 *     &lt;extension base="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}backBoneRouterInterface">
 *       &lt;sequence>
 *         &lt;element name="isCRC32Enabled" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="TRUE"/>
 *               &lt;enumeration value="FALSE"/>
 *               &lt;enumeration value="NOT_APPLICABLE"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="posFraming" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="SONET"/>
 *               &lt;enumeration value="SDH"/>
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
@XmlType(name = "backBonePOSInterface", namespace = "http://NetworkOrderServicesLibrary/netord/bo/_2013/_01", propOrder = {
    "isCRC32Enabled",
    "posFraming"
})
public class BackBonePOSInterface
    extends BackBoneRouterInterface
{

    protected String isCRC32Enabled;
    @XmlElement(defaultValue = "SDH")
    protected String posFraming;

    /**
     * Gets the value of the isCRC32Enabled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIsCRC32Enabled() {
        return isCRC32Enabled;
    }

    /**
     * Sets the value of the isCRC32Enabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIsCRC32Enabled(String value) {
        this.isCRC32Enabled = value;
    }

    /**
     * Gets the value of the posFraming property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPosFraming() {
        return posFraming;
    }

    /**
     * Sets the value of the posFraming property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPosFraming(String value) {
        this.posFraming = value;
    }

}
