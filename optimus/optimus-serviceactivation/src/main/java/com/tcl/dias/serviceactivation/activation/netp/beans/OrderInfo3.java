
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for OrderInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="OrderInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="service">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="iaService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IAService"/>
 *                   &lt;element name="gvpnService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}GVPNService"/>
 *                   &lt;element name="ersService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}P2PL2VPNService"/>
 *                   &lt;element name="iasMVoipService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IAS_MVOIPService" minOccurs="0"/>
 *                   &lt;element name="ewsService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}P2PL2VPNService" minOccurs="0"/>
 *                   &lt;element name="iasGipvcService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IAS_GIPVCService" minOccurs="0"/>
 *                   &lt;element name="wimaxLastmile" type="{http://www.tcl.com/2011/11/ipsvc/xsd}WimaxLastMile" minOccurs="0"/>
 *                   &lt;element name="roadWarriorService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RoadWarriorService" minOccurs="0"/>
 *                   &lt;element name="vplsService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VPLSService" minOccurs="0"/>
 *                   &lt;element name="dmvpnService" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}DMVPNService" minOccurs="0"/>
 *                   &lt;element name="ipsecService" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}IPSECService" minOccurs="0"/>
 *                   &lt;element name="hvpls" type="{http://www.tcl.com/2011/11/ipsvc/xsd}HVPLS" minOccurs="0"/>
 *                   &lt;element name="vutmService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VUTMService" minOccurs="0"/>
 *                   &lt;element name="cambiumLastmile" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CambiumLastmile" minOccurs="0"/>
 *                   &lt;element name="huaweiEthernetAccessService" type="{http://www.tcl.com/2014/4/ipsvc/xsd}EthernetAccessService" minOccurs="0"/>
 *                   &lt;element name="radwin5kLastmile" type="{http://www.tcl.com/2014/2/ipsvc/xsd}Radwin5kLastmile" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="serviceId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="oldServiceId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="solutionId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="copfId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="customerDetails" type="{http://www.tcl.com/2011/11/ace/common/xsd}Customer"/>
 *         &lt;element name="scheduleId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderCategory" type="{http://www.tcl.com/2011/11/ace/common/xsd}OrderCategory" minOccurs="0"/>
 *         &lt;element name="orderType" type="{http://www.tcl.com/2011/11/ipsvc/xsd}OrderType" minOccurs="0"/>
 *         &lt;element name="optyBidCategory" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="serviceBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="burstableBandwidth" type="{http://www.tcl.com/2011/11/ace/common/xsd}Bandwidth" minOccurs="0"/>
 *         &lt;element name="addOns" type="{http://www.tcl.com/2011/11/ipsvc/xsd}AddonFeatures" minOccurs="0"/>
 *         &lt;element name="isDowntimeRequired" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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
@XmlType(name = "OrderInfo", propOrder = {
    "service",
    "serviceId",
    "oldServiceId",
    "solutionId",
    "copfId",
    "customerDetails",
    "scheduleId",
    "orderCategory",
    "orderType",
    "optyBidCategory",
    "serviceBandwidth",
    "burstableBandwidth",
    "addOns",
    "isDowntimeRequired",
    "isObjectInstanceModified",
    "isChildObjectInstanceModified"
})
public class OrderInfo3 {

    @XmlElement(required = true)
    protected OrderInfo3 .Service service;
    @XmlElement(required = true)
    protected String serviceId;
    protected String oldServiceId;
    protected String solutionId;
    protected String copfId;
    @XmlElement(required = true)
    protected Customer customerDetails;
    protected String scheduleId;
    @XmlSchemaType(name = "string")
    protected OrderCategory orderCategory;
    @XmlSchemaType(name = "string")
    protected OrderType2 orderType;
    protected String optyBidCategory;
    protected Bandwidth serviceBandwidth;
    protected Bandwidth burstableBandwidth;
    protected AddonFeatures addOns;
    protected Boolean isDowntimeRequired;
    protected Boolean isObjectInstanceModified;
    protected Boolean isChildObjectInstanceModified;

    /**
     * Gets the value of the service property.
     * 
     * @return
     *     possible object is
     *     {@link OrderInfo3 .Service }
     *     
     */
    public OrderInfo3 .Service getService() {
        return service;
    }

    /**
     * Sets the value of the service property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderInfo3 .Service }
     *     
     */
    public void setService(OrderInfo3 .Service value) {
        this.service = value;
    }

    /**
     * Gets the value of the serviceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Sets the value of the serviceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceId(String value) {
        this.serviceId = value;
    }

    /**
     * Gets the value of the oldServiceId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOldServiceId() {
        return oldServiceId;
    }

    /**
     * Sets the value of the oldServiceId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOldServiceId(String value) {
        this.oldServiceId = value;
    }

    /**
     * Gets the value of the solutionId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSolutionId() {
        return solutionId;
    }

    /**
     * Sets the value of the solutionId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSolutionId(String value) {
        this.solutionId = value;
    }

    /**
     * Gets the value of the copfId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCopfId() {
        return copfId;
    }

    /**
     * Sets the value of the copfId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCopfId(String value) {
        this.copfId = value;
    }

    /**
     * Gets the value of the customerDetails property.
     * 
     * @return
     *     possible object is
     *     {@link Customer }
     *     
     */
    public Customer getCustomerDetails() {
        return customerDetails;
    }

    /**
     * Sets the value of the customerDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link Customer }
     *     
     */
    public void setCustomerDetails(Customer value) {
        this.customerDetails = value;
    }

    /**
     * Gets the value of the scheduleId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * Sets the value of the scheduleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScheduleId(String value) {
        this.scheduleId = value;
    }

    /**
     * Gets the value of the orderCategory property.
     * 
     * @return
     *     possible object is
     *     {@link OrderCategory }
     *     
     */
    public OrderCategory getOrderCategory() {
        return orderCategory;
    }

    /**
     * Sets the value of the orderCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderCategory }
     *     
     */
    public void setOrderCategory(OrderCategory value) {
        this.orderCategory = value;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link OrderType2 }
     *     
     */
    public OrderType2 getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link OrderType2 }
     *     
     */
    public void setOrderType(OrderType2 value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the optyBidCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOptyBidCategory() {
        return optyBidCategory;
    }

    /**
     * Sets the value of the optyBidCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOptyBidCategory(String value) {
        this.optyBidCategory = value;
    }

    /**
     * Gets the value of the serviceBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getServiceBandwidth() {
        return serviceBandwidth;
    }

    /**
     * Sets the value of the serviceBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setServiceBandwidth(Bandwidth value) {
        this.serviceBandwidth = value;
    }

    /**
     * Gets the value of the burstableBandwidth property.
     * 
     * @return
     *     possible object is
     *     {@link Bandwidth }
     *     
     */
    public Bandwidth getBurstableBandwidth() {
        return burstableBandwidth;
    }

    /**
     * Sets the value of the burstableBandwidth property.
     * 
     * @param value
     *     allowed object is
     *     {@link Bandwidth }
     *     
     */
    public void setBurstableBandwidth(Bandwidth value) {
        this.burstableBandwidth = value;
    }

    /**
     * Gets the value of the addOns property.
     * 
     * @return
     *     possible object is
     *     {@link AddonFeatures }
     *     
     */
    public AddonFeatures getAddOns() {
        return addOns;
    }

    /**
     * Sets the value of the addOns property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddonFeatures }
     *     
     */
    public void setAddOns(AddonFeatures value) {
        this.addOns = value;
    }

    /**
     * Gets the value of the isDowntimeRequired property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsDowntimeRequired() {
        return isDowntimeRequired;
    }

    /**
     * Sets the value of the isDowntimeRequired property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDowntimeRequired(Boolean value) {
        this.isDowntimeRequired = value;
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
     *         &lt;element name="iaService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IAService"/>
     *         &lt;element name="gvpnService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}GVPNService"/>
     *         &lt;element name="ersService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}P2PL2VPNService"/>
     *         &lt;element name="iasMVoipService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IAS_MVOIPService" minOccurs="0"/>
     *         &lt;element name="ewsService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}P2PL2VPNService" minOccurs="0"/>
     *         &lt;element name="iasGipvcService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}IAS_GIPVCService" minOccurs="0"/>
     *         &lt;element name="wimaxLastmile" type="{http://www.tcl.com/2011/11/ipsvc/xsd}WimaxLastMile" minOccurs="0"/>
     *         &lt;element name="roadWarriorService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}RoadWarriorService" minOccurs="0"/>
     *         &lt;element name="vplsService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VPLSService" minOccurs="0"/>
     *         &lt;element name="dmvpnService" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}DMVPNService" minOccurs="0"/>
     *         &lt;element name="ipsecService" type="{http://IPServicesLibrary/ipsvc/bo/_2013/_02}IPSECService" minOccurs="0"/>
     *         &lt;element name="hvpls" type="{http://www.tcl.com/2011/11/ipsvc/xsd}HVPLS" minOccurs="0"/>
     *         &lt;element name="vutmService" type="{http://www.tcl.com/2011/11/ipsvc/xsd}VUTMService" minOccurs="0"/>
     *         &lt;element name="cambiumLastmile" type="{http://www.tcl.com/2014/2/ipsvc/xsd}CambiumLastmile" minOccurs="0"/>
     *         &lt;element name="huaweiEthernetAccessService" type="{http://www.tcl.com/2014/4/ipsvc/xsd}EthernetAccessService" minOccurs="0"/>
     *         &lt;element name="radwin5kLastmile" type="{http://www.tcl.com/2014/2/ipsvc/xsd}Radwin5kLastmile" minOccurs="0"/>
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
        "iaService",
        "gvpnService",
        "ersService",
        "iasMVoipService",
        "ewsService",
        "iasGipvcService",
        "wimaxLastmile",
        "roadWarriorService",
        "vplsService",
        "dmvpnService",
        "ipsecService",
        "hvpls",
        "vutmService",
        "cambiumLastmile",
        "huaweiEthernetAccessService",
        "radwin5KLastmile"
    })
    public static class Service {

        protected IAService iaService;
        protected GVPNService gvpnService;
        protected P2PL2VPNService ersService;
        protected IASMVOIPService iasMVoipService;
        protected P2PL2VPNService ewsService;
        protected IASGIPVCService iasGipvcService;
        protected WimaxLastMile wimaxLastmile;
        protected RoadWarriorService roadWarriorService;
        protected VPLSService vplsService;
        protected DMVPNService dmvpnService;
        protected IPSECService ipsecService;
        protected HVPLS hvpls;
        protected VUTMService vutmService;
        protected CambiumLastmile cambiumLastmile;
        protected EthernetAccessService huaweiEthernetAccessService;
        @XmlElement(name = "radwin5kLastmile")
        protected Radwin5KLastmile radwin5KLastmile;

        /**
         * Gets the value of the iaService property.
         * 
         * @return
         *     possible object is
         *     {@link IAService }
         *     
         */
        public IAService getIaService() {
            return iaService;
        }

        /**
         * Sets the value of the iaService property.
         * 
         * @param value
         *     allowed object is
         *     {@link IAService }
         *     
         */
        public void setIaService(IAService value) {
            this.iaService = value;
        }

        /**
         * Gets the value of the gvpnService property.
         * 
         * @return
         *     possible object is
         *     {@link GVPNService }
         *     
         */
        public GVPNService getGvpnService() {
            return gvpnService;
        }

        /**
         * Sets the value of the gvpnService property.
         * 
         * @param value
         *     allowed object is
         *     {@link GVPNService }
         *     
         */
        public void setGvpnService(GVPNService value) {
            this.gvpnService = value;
        }

        /**
         * Gets the value of the ersService property.
         * 
         * @return
         *     possible object is
         *     {@link P2PL2VPNService }
         *     
         */
        public P2PL2VPNService getErsService() {
            return ersService;
        }

        /**
         * Sets the value of the ersService property.
         * 
         * @param value
         *     allowed object is
         *     {@link P2PL2VPNService }
         *     
         */
        public void setErsService(P2PL2VPNService value) {
            this.ersService = value;
        }

        /**
         * Gets the value of the iasMVoipService property.
         * 
         * @return
         *     possible object is
         *     {@link IASMVOIPService }
         *     
         */
        public IASMVOIPService getIasMVoipService() {
            return iasMVoipService;
        }

        /**
         * Sets the value of the iasMVoipService property.
         * 
         * @param value
         *     allowed object is
         *     {@link IASMVOIPService }
         *     
         */
        public void setIasMVoipService(IASMVOIPService value) {
            this.iasMVoipService = value;
        }

        /**
         * Gets the value of the ewsService property.
         * 
         * @return
         *     possible object is
         *     {@link P2PL2VPNService }
         *     
         */
        public P2PL2VPNService getEwsService() {
            return ewsService;
        }

        /**
         * Sets the value of the ewsService property.
         * 
         * @param value
         *     allowed object is
         *     {@link P2PL2VPNService }
         *     
         */
        public void setEwsService(P2PL2VPNService value) {
            this.ewsService = value;
        }

        /**
         * Gets the value of the iasGipvcService property.
         * 
         * @return
         *     possible object is
         *     {@link IASGIPVCService }
         *     
         */
        public IASGIPVCService getIasGipvcService() {
            return iasGipvcService;
        }

        /**
         * Sets the value of the iasGipvcService property.
         * 
         * @param value
         *     allowed object is
         *     {@link IASGIPVCService }
         *     
         */
        public void setIasGipvcService(IASGIPVCService value) {
            this.iasGipvcService = value;
        }

        /**
         * Gets the value of the wimaxLastmile property.
         * 
         * @return
         *     possible object is
         *     {@link WimaxLastMile }
         *     
         */
        public WimaxLastMile getWimaxLastmile() {
            return wimaxLastmile;
        }

        /**
         * Sets the value of the wimaxLastmile property.
         * 
         * @param value
         *     allowed object is
         *     {@link WimaxLastMile }
         *     
         */
        public void setWimaxLastmile(WimaxLastMile value) {
            this.wimaxLastmile = value;
        }

        /**
         * Gets the value of the roadWarriorService property.
         * 
         * @return
         *     possible object is
         *     {@link RoadWarriorService }
         *     
         */
        public RoadWarriorService getRoadWarriorService() {
            return roadWarriorService;
        }

        /**
         * Sets the value of the roadWarriorService property.
         * 
         * @param value
         *     allowed object is
         *     {@link RoadWarriorService }
         *     
         */
        public void setRoadWarriorService(RoadWarriorService value) {
            this.roadWarriorService = value;
        }

        /**
         * Gets the value of the vplsService property.
         * 
         * @return
         *     possible object is
         *     {@link VPLSService }
         *     
         */
        public VPLSService getVplsService() {
            return vplsService;
        }

        /**
         * Sets the value of the vplsService property.
         * 
         * @param value
         *     allowed object is
         *     {@link VPLSService }
         *     
         */
        public void setVplsService(VPLSService value) {
            this.vplsService = value;
        }

        /**
         * Gets the value of the dmvpnService property.
         * 
         * @return
         *     possible object is
         *     {@link DMVPNService }
         *     
         */
        public DMVPNService getDmvpnService() {
            return dmvpnService;
        }

        /**
         * Sets the value of the dmvpnService property.
         * 
         * @param value
         *     allowed object is
         *     {@link DMVPNService }
         *     
         */
        public void setDmvpnService(DMVPNService value) {
            this.dmvpnService = value;
        }

        /**
         * Gets the value of the ipsecService property.
         * 
         * @return
         *     possible object is
         *     {@link IPSECService }
         *     
         */
        public IPSECService getIpsecService() {
            return ipsecService;
        }

        /**
         * Sets the value of the ipsecService property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPSECService }
         *     
         */
        public void setIpsecService(IPSECService value) {
            this.ipsecService = value;
        }

        /**
         * Gets the value of the hvpls property.
         * 
         * @return
         *     possible object is
         *     {@link HVPLS }
         *     
         */
        public HVPLS getHvpls() {
            return hvpls;
        }

        /**
         * Sets the value of the hvpls property.
         * 
         * @param value
         *     allowed object is
         *     {@link HVPLS }
         *     
         */
        public void setHvpls(HVPLS value) {
            this.hvpls = value;
        }

        /**
         * Gets the value of the vutmService property.
         * 
         * @return
         *     possible object is
         *     {@link VUTMService }
         *     
         */
        public VUTMService getVutmService() {
            return vutmService;
        }

        /**
         * Sets the value of the vutmService property.
         * 
         * @param value
         *     allowed object is
         *     {@link VUTMService }
         *     
         */
        public void setVutmService(VUTMService value) {
            this.vutmService = value;
        }

        /**
         * Gets the value of the cambiumLastmile property.
         * 
         * @return
         *     possible object is
         *     {@link CambiumLastmile }
         *     
         */
        public CambiumLastmile getCambiumLastmile() {
            return cambiumLastmile;
        }

        /**
         * Sets the value of the cambiumLastmile property.
         * 
         * @param value
         *     allowed object is
         *     {@link CambiumLastmile }
         *     
         */
        public void setCambiumLastmile(CambiumLastmile value) {
            this.cambiumLastmile = value;
        }

        /**
         * Gets the value of the huaweiEthernetAccessService property.
         * 
         * @return
         *     possible object is
         *     {@link EthernetAccessService }
         *     
         */
        public EthernetAccessService getHuaweiEthernetAccessService() {
            return huaweiEthernetAccessService;
        }

        /**
         * Sets the value of the huaweiEthernetAccessService property.
         * 
         * @param value
         *     allowed object is
         *     {@link EthernetAccessService }
         *     
         */
        public void setHuaweiEthernetAccessService(EthernetAccessService value) {
            this.huaweiEthernetAccessService = value;
        }

        /**
         * Gets the value of the radwin5KLastmile property.
         * 
         * @return
         *     possible object is
         *     {@link Radwin5KLastmile }
         *     
         */
        public Radwin5KLastmile getRadwin5KLastmile() {
            return radwin5KLastmile;
        }

        /**
         * Sets the value of the radwin5KLastmile property.
         * 
         * @param value
         *     allowed object is
         *     {@link Radwin5KLastmile }
         *     
         */
        public void setRadwin5KLastmile(Radwin5KLastmile value) {
            this.radwin5KLastmile = value;
        }

    }

}
