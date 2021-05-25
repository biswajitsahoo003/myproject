
package com.tcl.dias.serviceactivation.activation.netp.beans;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 * 				for Netp hostname is sufficient. bgplocalAddress is
 * 				optional
 * 			
 * 
 * <p>Java class for IRSRouter complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="IRSRouter">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hostName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="bgplocalAddress" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;choice>
 *                   &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
 *                   &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
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
@XmlType(name = "IRSRouter", propOrder = {
    "hostName",
    "bgplocalAddress",
    "isObjectInstanceModified"
})
public class IRSRouter {

    protected String hostName;
    protected IRSRouter.BgplocalAddress bgplocalAddress;
    protected Boolean isObjectInstanceModified;

    /**
     * Gets the value of the hostName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * Sets the value of the hostName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHostName(String value) {
        this.hostName = value;
    }

    /**
     * Gets the value of the bgplocalAddress property.
     * 
     * @return
     *     possible object is
     *     {@link IRSRouter.BgplocalAddress }
     *     
     */
    public IRSRouter.BgplocalAddress getBgplocalAddress() {
        return bgplocalAddress;
    }

    /**
     * Sets the value of the bgplocalAddress property.
     * 
     * @param value
     *     allowed object is
     *     {@link IRSRouter.BgplocalAddress }
     *     
     */
    public void setBgplocalAddress(IRSRouter.BgplocalAddress value) {
        this.bgplocalAddress = value;
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
     *         &lt;element name="v4Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV4Address"/>
     *         &lt;element name="v6Address" type="{http://www.tcl.com/2011/11/ace/common/xsd}IPV6Address"/>
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
        "v4Address",
        "v6Address"
    })
    public static class BgplocalAddress {

        protected IPV4Address v4Address;
        protected IPV6Address v6Address;

        /**
         * Gets the value of the v4Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV4Address }
         *     
         */
        public IPV4Address getV4Address() {
            return v4Address;
        }

        /**
         * Sets the value of the v4Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV4Address }
         *     
         */
        public void setV4Address(IPV4Address value) {
            this.v4Address = value;
        }

        /**
         * Gets the value of the v6Address property.
         * 
         * @return
         *     possible object is
         *     {@link IPV6Address }
         *     
         */
        public IPV6Address getV6Address() {
            return v6Address;
        }

        /**
         * Sets the value of the v6Address property.
         * 
         * @param value
         *     allowed object is
         *     {@link IPV6Address }
         *     
         */
        public void setV6Address(IPV6Address value) {
            this.v6Address = value;
        }

    }

}
