
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for IOROrderInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IOROrderInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="request" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="backboneIOR" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}MPLSBackboneIORRequest" minOccurs="0"/>
 *                   &lt;element name="topologyUpdateIOR" type="{http://NetworkOrderServicesLibrary/netord/bo/_2011/_11}TopologyIORRequest" minOccurs="0"/>
 *                   &lt;element name="genericCiscoIOR" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}GenericCiscoIORRequest" minOccurs="0"/>
 *                   &lt;element name="ethernetTrunkConfigRequest" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}EthernetTrunkPortConfigRequest" minOccurs="0"/>
 *                   &lt;element name="lagIORRequest" type="{http://www.tcl.com/2014/2/ipsvc/xsd}LAGIORRequest" minOccurs="0"/>
 *                   &lt;element name="huaweiEthernetAccessIORRequest" type="{http://www.tcl.com/2014/5/ipsvc/xsd}HuaweiEthernetAccessIORRequest" minOccurs="0"/>
 *                 &lt;/choice>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="orderId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="orderType" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="BACKBONE_IOR"/>
 *               &lt;enumeration value="TOPOLOGY_IOR"/>
 *               &lt;enumeration value="GENERIC_IOR"/>
 *               &lt;enumeration value="HUAWEI_ACCESS_IOR"/>
 *               &lt;enumeration value="LAG_IOR"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="orderCategory" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="IOR_IP"/>
 *               &lt;enumeration value="IOR_ETHERNET"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "IOROrderInfo", namespace = "http://www.tcl.com/2012/11/netordsvc/xsd", propOrder = {
    "request",
    "orderId",
    "orderType",
    "orderCategory"
})
public class IOROrderInfo {

    protected IOROrderInfo.Request request;
    protected String orderId;
    protected String orderType;
    protected String orderCategory;

    /**
     * Gets the value of the request property.
     * 
     * @return
     *     possible object is
     *     {@link IOROrderInfo.Request }
     *     
     */
    public IOROrderInfo.Request getRequest() {
        return request;
    }

    /**
     * Sets the value of the request property.
     * 
     * @param value
     *     allowed object is
     *     {@link IOROrderInfo.Request }
     *     
     */
    public void setRequest(IOROrderInfo.Request value) {
        this.request = value;
    }

    /**
     * Gets the value of the orderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the orderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(String value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the orderType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderType() {
        return orderType;
    }

    /**
     * Sets the value of the orderType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderType(String value) {
        this.orderType = value;
    }

    /**
     * Gets the value of the orderCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOrderCategory() {
        return orderCategory;
    }

    /**
     * Sets the value of the orderCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderCategory(String value) {
        this.orderCategory = value;
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
     *         &lt;element name="backboneIOR" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_01}MPLSBackboneIORRequest" minOccurs="0"/>
     *         &lt;element name="topologyUpdateIOR" type="{http://NetworkOrderServicesLibrary/netord/bo/_2011/_11}TopologyIORRequest" minOccurs="0"/>
     *         &lt;element name="genericCiscoIOR" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}GenericCiscoIORRequest" minOccurs="0"/>
     *         &lt;element name="ethernetTrunkConfigRequest" type="{http://NetworkOrderServicesLibrary/netord/bo/_2013/_02}EthernetTrunkPortConfigRequest" minOccurs="0"/>
     *         &lt;element name="lagIORRequest" type="{http://www.tcl.com/2014/2/ipsvc/xsd}LAGIORRequest" minOccurs="0"/>
     *         &lt;element name="huaweiEthernetAccessIORRequest" type="{http://www.tcl.com/2014/5/ipsvc/xsd}HuaweiEthernetAccessIORRequest" minOccurs="0"/>
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
        "backboneIOR",
        "topologyUpdateIOR",
        "genericCiscoIOR",
        "ethernetTrunkConfigRequest",
        "lagIORRequest",
        "huaweiEthernetAccessIORRequest"
    })
    public static class Request {

        protected MPLSBackboneIORRequest backboneIOR;
        protected TopologyIORRequest topologyUpdateIOR;
        protected GenericCiscoIORRequest genericCiscoIOR;
        protected EthernetTrunkPortConfigRequest ethernetTrunkConfigRequest;
        protected LAGIORRequest lagIORRequest;
        protected HuaweiEthernetAccessIORRequest huaweiEthernetAccessIORRequest;

        /**
         * Gets the value of the backboneIOR property.
         * 
         * @return
         *     possible object is
         *     {@link MPLSBackboneIORRequest }
         *     
         */
        public MPLSBackboneIORRequest getBackboneIOR() {
            return backboneIOR;
        }

        /**
         * Sets the value of the backboneIOR property.
         * 
         * @param value
         *     allowed object is
         *     {@link MPLSBackboneIORRequest }
         *     
         */
        public void setBackboneIOR(MPLSBackboneIORRequest value) {
            this.backboneIOR = value;
        }

        /**
         * Gets the value of the topologyUpdateIOR property.
         * 
         * @return
         *     possible object is
         *     {@link TopologyIORRequest }
         *     
         */
        public TopologyIORRequest getTopologyUpdateIOR() {
            return topologyUpdateIOR;
        }

        /**
         * Sets the value of the topologyUpdateIOR property.
         * 
         * @param value
         *     allowed object is
         *     {@link TopologyIORRequest }
         *     
         */
        public void setTopologyUpdateIOR(TopologyIORRequest value) {
            this.topologyUpdateIOR = value;
        }

        /**
         * Gets the value of the genericCiscoIOR property.
         * 
         * @return
         *     possible object is
         *     {@link GenericCiscoIORRequest }
         *     
         */
        public GenericCiscoIORRequest getGenericCiscoIOR() {
            return genericCiscoIOR;
        }

        /**
         * Sets the value of the genericCiscoIOR property.
         * 
         * @param value
         *     allowed object is
         *     {@link GenericCiscoIORRequest }
         *     
         */
        public void setGenericCiscoIOR(GenericCiscoIORRequest value) {
            this.genericCiscoIOR = value;
        }

        /**
         * Gets the value of the ethernetTrunkConfigRequest property.
         * 
         * @return
         *     possible object is
         *     {@link EthernetTrunkPortConfigRequest }
         *     
         */
        public EthernetTrunkPortConfigRequest getEthernetTrunkConfigRequest() {
            return ethernetTrunkConfigRequest;
        }

        /**
         * Sets the value of the ethernetTrunkConfigRequest property.
         * 
         * @param value
         *     allowed object is
         *     {@link EthernetTrunkPortConfigRequest }
         *     
         */
        public void setEthernetTrunkConfigRequest(EthernetTrunkPortConfigRequest value) {
            this.ethernetTrunkConfigRequest = value;
        }

        /**
         * Gets the value of the lagIORRequest property.
         * 
         * @return
         *     possible object is
         *     {@link LAGIORRequest }
         *     
         */
        public LAGIORRequest getLagIORRequest() {
            return lagIORRequest;
        }

        /**
         * Sets the value of the lagIORRequest property.
         * 
         * @param value
         *     allowed object is
         *     {@link LAGIORRequest }
         *     
         */
        public void setLagIORRequest(LAGIORRequest value) {
            this.lagIORRequest = value;
        }

        /**
         * Gets the value of the huaweiEthernetAccessIORRequest property.
         * 
         * @return
         *     possible object is
         *     {@link HuaweiEthernetAccessIORRequest }
         *     
         */
        public HuaweiEthernetAccessIORRequest getHuaweiEthernetAccessIORRequest() {
            return huaweiEthernetAccessIORRequest;
        }

        /**
         * Sets the value of the huaweiEthernetAccessIORRequest property.
         * 
         * @param value
         *     allowed object is
         *     {@link HuaweiEthernetAccessIORRequest }
         *     
         */
        public void setHuaweiEthernetAccessIORRequest(HuaweiEthernetAccessIORRequest value) {
            this.huaweiEthernetAccessIORRequest = value;
        }

    }

}
