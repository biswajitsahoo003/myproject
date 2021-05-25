
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RoadWarriorService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RoadWarriorService">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.tcl.com/2011/11/ipsvc/xsd}GVPNService">
 *       &lt;sequence>
 *         &lt;element name="LNSConfigOption" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="peLNSPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PE-LNS-Path" minOccurs="0"/>
 *                   &lt;element name="peLNSLoopbackPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_06}PELNSLoopbackPath" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="remoteSite" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}RemoteSite" minOccurs="0"/>
 *         &lt;element name="field1" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="LNSEVDOPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_06}LNSEVDOPath" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RoadWarriorService", propOrder = {
    "lnsConfigOption",
    "remoteSite",
    "field1",
    "lnsevdoPath"
})
public class RoadWarriorService
    extends GVPNService
{

    @XmlElement(name = "LNSConfigOption")
    protected RoadWarriorService.LNSConfigOption lnsConfigOption;
    protected RemoteSite remoteSite;
    protected String field1;
    @XmlElement(name = "LNSEVDOPath")
    protected LNSEVDOPath lnsevdoPath;

    /**
     * Gets the value of the lnsConfigOption property.
     * 
     * @return
     *     possible object is
     *     {@link RoadWarriorService.LNSConfigOption }
     *     
     */
    public RoadWarriorService.LNSConfigOption getLNSConfigOption() {
        return lnsConfigOption;
    }

    /**
     * Sets the value of the lnsConfigOption property.
     * 
     * @param value
     *     allowed object is
     *     {@link RoadWarriorService.LNSConfigOption }
     *     
     */
    public void setLNSConfigOption(RoadWarriorService.LNSConfigOption value) {
        this.lnsConfigOption = value;
    }

    /**
     * Gets the value of the remoteSite property.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSite }
     *     
     */
    public RemoteSite getRemoteSite() {
        return remoteSite;
    }

    /**
     * Sets the value of the remoteSite property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSite }
     *     
     */
    public void setRemoteSite(RemoteSite value) {
        this.remoteSite = value;
    }

    /**
     * Gets the value of the field1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getField1() {
        return field1;
    }

    /**
     * Sets the value of the field1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setField1(String value) {
        this.field1 = value;
    }

    /**
     * Gets the value of the lnsevdoPath property.
     * 
     * @return
     *     possible object is
     *     {@link LNSEVDOPath }
     *     
     */
    public LNSEVDOPath getLNSEVDOPath() {
        return lnsevdoPath;
    }

    /**
     * Sets the value of the lnsevdoPath property.
     * 
     * @param value
     *     allowed object is
     *     {@link LNSEVDOPath }
     *     
     */
    public void setLNSEVDOPath(LNSEVDOPath value) {
        this.lnsevdoPath = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="peLNSPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PE-LNS-Path" minOccurs="0"/>
     *         &lt;element name="peLNSLoopbackPath" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_06}PELNSLoopbackPath" minOccurs="0"/>
     *       &lt;/choice>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "peLNSPath",
        "peLNSLoopbackPath"
    })
    public static class LNSConfigOption {

        protected PELNSPath peLNSPath;
        protected PELNSLoopbackPath peLNSLoopbackPath;

        /**
         * Gets the value of the peLNSPath property.
         * 
         * @return
         *     possible object is
         *     {@link PELNSPath }
         *     
         */
        public PELNSPath getPeLNSPath() {
            return peLNSPath;
        }

        /**
         * Sets the value of the peLNSPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link PELNSPath }
         *     
         */
        public void setPeLNSPath(PELNSPath value) {
            this.peLNSPath = value;
        }

        /**
         * Gets the value of the peLNSLoopbackPath property.
         * 
         * @return
         *     possible object is
         *     {@link PELNSLoopbackPath }
         *     
         */
        public PELNSLoopbackPath getPeLNSLoopbackPath() {
            return peLNSLoopbackPath;
        }

        /**
         * Sets the value of the peLNSLoopbackPath property.
         * 
         * @param value
         *     allowed object is
         *     {@link PELNSLoopbackPath }
         *     
         */
        public void setPeLNSLoopbackPath(PELNSLoopbackPath value) {
            this.peLNSLoopbackPath = value;
        }

    }

}
