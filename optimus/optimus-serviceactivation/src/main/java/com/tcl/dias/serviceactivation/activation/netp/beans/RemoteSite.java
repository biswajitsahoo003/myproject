
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for RemoteSite complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="RemoteSite">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nniCPEWANRouting" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol" minOccurs="0"/>
 *                   &lt;element name="staticRoutes" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="nniCPEQoS" type="{http://www.tcl.com/2011/11/ipsvc/xsd}QoS" minOccurs="0"/>
 *         &lt;element name="unManagedCEFacingPartnerDevice" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PartnerDevice" minOccurs="0"/>
 *         &lt;element name="CPE" type="{http://www.tcl.com/2011/11/ipsvc/xsd}CustomerPremiseEquipment" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RemoteSite", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "nniCPEWANRouting",
    "nniCPEQoS",
    "unManagedCEFacingPartnerDevice",
    "cpe"
})
public class RemoteSite {

    protected RemoteSite.NniCPEWANRouting nniCPEWANRouting;
    protected QoS nniCPEQoS;
    protected PartnerDevice unManagedCEFacingPartnerDevice;
    @XmlElement(name = "CPE")
    protected CustomerPremiseEquipment cpe;

    /**
     * Gets the value of the nniCPEWANRouting property.
     * 
     * @return
     *     possible object is
     *     {@link RemoteSite.NniCPEWANRouting }
     *     
     */
    public RemoteSite.NniCPEWANRouting getNniCPEWANRouting() {
        return nniCPEWANRouting;
    }

    /**
     * Sets the value of the nniCPEWANRouting property.
     * 
     * @param value
     *     allowed object is
     *     {@link RemoteSite.NniCPEWANRouting }
     *     
     */
    public void setNniCPEWANRouting(RemoteSite.NniCPEWANRouting value) {
        this.nniCPEWANRouting = value;
    }

    /**
     * Gets the value of the nniCPEQoS property.
     * 
     * @return
     *     possible object is
     *     {@link QoS }
     *     
     */
    public QoS getNniCPEQoS() {
        return nniCPEQoS;
    }

    /**
     * Sets the value of the nniCPEQoS property.
     * 
     * @param value
     *     allowed object is
     *     {@link QoS }
     *     
     */
    public void setNniCPEQoS(QoS value) {
        this.nniCPEQoS = value;
    }

    /**
     * Gets the value of the unManagedCEFacingPartnerDevice property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerDevice }
     *     
     */
    public PartnerDevice getUnManagedCEFacingPartnerDevice() {
        return unManagedCEFacingPartnerDevice;
    }

    /**
     * Sets the value of the unManagedCEFacingPartnerDevice property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerDevice }
     *     
     */
    public void setUnManagedCEFacingPartnerDevice(PartnerDevice value) {
        this.unManagedCEFacingPartnerDevice = value;
    }

    /**
     * Gets the value of the cpe property.
     * 
     * @return
     *     possible object is
     *     {@link CustomerPremiseEquipment }
     *     
     */
    public CustomerPremiseEquipment getCPE() {
        return cpe;
    }

    /**
     * Sets the value of the cpe property.
     * 
     * @param value
     *     allowed object is
     *     {@link CustomerPremiseEquipment }
     *     
     */
    public void setCPE(CustomerPremiseEquipment value) {
        this.cpe = value;
    }


    /**
     * 
     * 							Static routes will only involve CE side
     * 							static routes. PE side static routes are not
     * 							applicable for this object
     * 						
     * 
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol" minOccurs="0"/>
     *         &lt;element name="staticRoutes" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol" minOccurs="0"/>
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
        "bgpRoutingProtocol",
        "staticRoutes"
    })
    public static class NniCPEWANRouting {

        protected BGPRoutingProtocol bgpRoutingProtocol;
        protected StaticRoutingProtocol staticRoutes;

        /**
         * Gets the value of the bgpRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link BGPRoutingProtocol }
         *     
         */
        public BGPRoutingProtocol getBgpRoutingProtocol() {
            return bgpRoutingProtocol;
        }

        /**
         * Sets the value of the bgpRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link BGPRoutingProtocol }
         *     
         */
        public void setBgpRoutingProtocol(BGPRoutingProtocol value) {
            this.bgpRoutingProtocol = value;
        }

        /**
         * Gets the value of the staticRoutes property.
         * 
         * @return
         *     possible object is
         *     {@link StaticRoutingProtocol }
         *     
         */
        public StaticRoutingProtocol getStaticRoutes() {
            return staticRoutes;
        }

        /**
         * Sets the value of the staticRoutes property.
         * 
         * @param value
         *     allowed object is
         *     {@link StaticRoutingProtocol }
         *     
         */
        public void setStaticRoutes(StaticRoutingProtocol value) {
            this.staticRoutes = value;
        }

    }

}
