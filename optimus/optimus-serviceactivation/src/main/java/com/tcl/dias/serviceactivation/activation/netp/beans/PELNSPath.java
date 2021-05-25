
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for PE-LNS-Path complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="PE-LNS-Path">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="peRouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *         &lt;element name="lnsRouter" type="{http://www.tcl.com/2011/11/ipsvc/xsd}Router" minOccurs="0"/>
 *         &lt;element name="peLNSRoutingProtocol" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol" minOccurs="0"/>
 *                   &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol" minOccurs="0"/>
 *                   &lt;element name="staticRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="LNSVRFName" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VirtualRouteForwardingServiceInstance" minOccurs="0"/>
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
@XmlType(name = "PE-LNS-Path", namespace = "http://IPServicesLibrary/ipsvc/bo/_2013/_02", propOrder = {
    "peRouter",
    "lnsRouter",
    "peLNSRoutingProtocol",
    "lnsvrfName",
    "isObjectInstanceModified"
})
public class PELNSPath {

    protected Router peRouter;
    protected Router lnsRouter;
    protected PELNSPath.PeLNSRoutingProtocol peLNSRoutingProtocol;
    @XmlElement(name = "LNSVRFName")
    protected VirtualRouteForwardingServiceInstance lnsvrfName;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the peRouter property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getPeRouter() {
        return peRouter;
    }

    /**
     * Sets the value of the peRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setPeRouter(Router value) {
        this.peRouter = value;
    }

    /**
     * Gets the value of the lnsRouter property.
     * 
     * @return
     *     possible object is
     *     {@link Router }
     *     
     */
    public Router getLnsRouter() {
        return lnsRouter;
    }

    /**
     * Sets the value of the lnsRouter property.
     * 
     * @param value
     *     allowed object is
     *     {@link Router }
     *     
     */
    public void setLnsRouter(Router value) {
        this.lnsRouter = value;
    }

    /**
     * Gets the value of the peLNSRoutingProtocol property.
     * 
     * @return
     *     possible object is
     *     {@link PELNSPath.PeLNSRoutingProtocol }
     *     
     */
    public PELNSPath.PeLNSRoutingProtocol getPeLNSRoutingProtocol() {
        return peLNSRoutingProtocol;
    }

    /**
     * Sets the value of the peLNSRoutingProtocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link PELNSPath.PeLNSRoutingProtocol }
     *     
     */
    public void setPeLNSRoutingProtocol(PELNSPath.PeLNSRoutingProtocol value) {
        this.peLNSRoutingProtocol = value;
    }

    /**
     * Gets the value of the lnsvrfName property.
     * 
     * @return
     *     possible object is
     *     {@link VirtualRouteForwardingServiceInstance }
     *     
     */
    public VirtualRouteForwardingServiceInstance getLNSVRFName() {
        return lnsvrfName;
    }

    /**
     * Sets the value of the lnsvrfName property.
     * 
     * @param value
     *     allowed object is
     *     {@link VirtualRouteForwardingServiceInstance }
     *     
     */
    public void setLNSVRFName(VirtualRouteForwardingServiceInstance value) {
        this.lnsvrfName = value;
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
     *         &lt;element name="bgpRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}BGPRoutingProtocol" minOccurs="0"/>
     *         &lt;element name="ospfRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OSPFRoutingProtocol" minOccurs="0"/>
     *         &lt;element name="staticRoutingProtocol" type="{http://www.tcl.com/2011/11/ipsvc/xsd}StaticRoutingProtocol" minOccurs="0"/>
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
        "ospfRoutingProtocol",
        "staticRoutingProtocol"
    })
    public static class PeLNSRoutingProtocol {

        protected BGPRoutingProtocol bgpRoutingProtocol;
        protected OSPFRoutingProtocol ospfRoutingProtocol;
        protected StaticRoutingProtocol staticRoutingProtocol;

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

    }

}
