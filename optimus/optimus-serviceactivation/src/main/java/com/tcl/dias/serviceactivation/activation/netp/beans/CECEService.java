
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CECEService complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CECEService">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="CPEremote" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}PartnerDevice" minOccurs="0"/>
 *         &lt;element name="CE2CERoutingProtocol">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol"/>
 *                   &lt;element name="eigprRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EIGRPRoutingProtocol"/>
 *                   &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol"/>
 *                   &lt;element name="staticRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol"/>
 *                   &lt;element name="ripRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RIPRoutingProtocol" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="isObjectInstanceModified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CECEService", namespace = "http://IPServicesLibrary/ipsvc/bo/_2011/_11", propOrder = {
    "cpEremote",
    "ce2CERoutingProtocol",
    "isObjectInstanceModified"
})
public class CECEService {

    @XmlElement(name = "CPEremote")
    protected PartnerDevice cpEremote;
    @XmlElement(name = "CE2CERoutingProtocol", required = true)
    protected CECEService.CE2CERoutingProtocol ce2CERoutingProtocol;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the cpEremote property.
     * 
     * @return
     *     possible object is
     *     {@link PartnerDevice }
     *     
     */
    public PartnerDevice getCPEremote() {
        return cpEremote;
    }

    /**
     * Sets the value of the cpEremote property.
     * 
     * @param value
     *     allowed object is
     *     {@link PartnerDevice }
     *     
     */
    public void setCPEremote(PartnerDevice value) {
        this.cpEremote = value;
    }

    /**
     * Gets the value of the ce2CERoutingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link CECEService.CE2CERoutingProtocol }
     *     
     */
    public CECEService.CE2CERoutingProtocol getCE2CERoutingProtocol() {
        return ce2CERoutingProtocol;
    }

    /**
     * Sets the value of the ce2CERoutingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link CECEService.CE2CERoutingProtocol }
     *     
     */
    public void setCE2CERoutingProtocol(CECEService.CE2CERoutingProtocol value) {
        this.ce2CERoutingProtocol = value;
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
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;choice>
     *         &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol"/>
     *         &lt;element name="eigprRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}EIGRPRoutingProtocol"/>
     *         &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol"/>
     *         &lt;element name="staticRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol"/>
     *         &lt;element name="ripRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RIPRoutingProtocol" minOccurs="0"/>
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
        "eigprRoutingProtocol",
        "ospfRoutingProtocol",
        "staticRoutingProtocol",
        "ripRoutingProtocol"
    })
    public static class CE2CERoutingProtocol {

        protected BGPRoutingProtocol bgpRoutingProtocol;
        protected EIGRPRoutingProtocol eigprRoutingProtocol;
        protected OSPFRoutingProtocol ospfRoutingProtocol;
        protected StaticRoutingProtocol staticRoutingProtocol;
        protected RIPRoutingProtocol ripRoutingProtocol;

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
         * Gets the value of the eigprRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link EIGRPRoutingProtocol }
         *     
         */
        public EIGRPRoutingProtocol getEigprRoutingProtocol() {
            return eigprRoutingProtocol;
        }

        /**
         * Sets the value of the eigprRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link EIGRPRoutingProtocol }
         *     
         */
        public void setEigprRoutingProtocol(EIGRPRoutingProtocol value) {
            this.eigprRoutingProtocol = value;
        }

        /**
         * Gets the value of the ospfRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link OSPFRoutingProtocol }
         *     
         */
        public OSPFRoutingProtocol getOspfRoutingProtocol() {
            return ospfRoutingProtocol;
        }

        /**
         * Sets the value of the ospfRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link OSPFRoutingProtocol }
         *     
         */
        public void setOspfRoutingProtocol(OSPFRoutingProtocol value) {
            this.ospfRoutingProtocol = value;
        }

        /**
         * Gets the value of the staticRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link StaticRoutingProtocol }
         *     
         */
        public StaticRoutingProtocol getStaticRoutingProtocol() {
            return staticRoutingProtocol;
        }

        /**
         * Sets the value of the staticRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link StaticRoutingProtocol }
         *     
         */
        public void setStaticRoutingProtocol(StaticRoutingProtocol value) {
            this.staticRoutingProtocol = value;
        }

        /**
         * Gets the value of the ripRoutingProtocol property.
         * 
         * @return
         *     possible object is
         *     {@link RIPRoutingProtocol }
         *     
         */
        public RIPRoutingProtocol getRipRoutingProtocol() {
            return ripRoutingProtocol;
        }

        /**
         * Sets the value of the ripRoutingProtocol property.
         * 
         * @param value
         *     allowed object is
         *     {@link RIPRoutingProtocol }
         *     
         */
        public void setRipRoutingProtocol(RIPRoutingProtocol value) {
            this.ripRoutingProtocol = value;
        }

    }

}
